# spring-boot-4

A Spring Boot 4 / Kotlin project using TestContainers with PostgreSQL via the TC JDBC URL approach (`jdbc:tc:`). Tests spin up a real PostgreSQL container automatically — no running database required.

## Prerequisites

- Java 25
- A container runtime: **Podman** (Linux / WSL) or **Docker Desktop** (Windows / macOS)

---

## Registry placeholder

This project routes container image pulls through a private/mirror Docker
registry. The literal placeholder `REGISTRY_HOST` appears in three files:

- `.devcontainer/devcontainer.json`
- `.devcontainer/registries.conf.example`
- `.gitlab-ci.yml`

Before using devpod/devcontainers or GitLab CI, substitute it with your real
registry hostname (and port, if any):

```bash
# macOS/BSD sed
grep -rl 'REGISTRY_HOST' --exclude-dir=.git . | xargs sed -i '' 's/REGISTRY_HOST/registry.example.com/g'
# GNU/Linux sed
grep -rl 'REGISTRY_HOST' --exclude-dir=.git . | xargs sed -i 's/REGISTRY_HOST/registry.example.com/g'
```

This assumes the registry passthrough-mirrors upstream images under their
original path (e.g. Docker Hub's `postgres:16-alpine` becomes
`REGISTRY_HOST/library/postgres:16-alpine`). Adjust the path segment if your
registry uses a different convention (e.g. GitLab's own Dependency Proxy).

`registries.conf.example` additionally needs to be *copied* (not just edited
in place) to `~/.config/containers/registries.conf.d/` — see the comments in
that file for details. If your registry requires authentication, GitLab CI's
`DOCKER_AUTH_CONFIG` CI/CD variable covers both `image:` and `services:`
pulls with no `.gitlab-ci.yml` changes needed.

---

## Linux

### 1. Install Java 25

The recommended way is [SDKMAN](https://sdkman.io):

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 25.0.3-tem
```

### 2. Install and enable Podman

```bash
sudo apt install podman          # Debian/Ubuntu
# or
sudo dnf install podman          # Fedora/RHEL
```

Enable the user socket (once — persists across reboots):

```bash
systemctl --user enable --now podman.socket
```

### 3. Set environment variables

Add these to your shell profile (`~/.bashrc`, `~/.zshrc`, etc.):

```bash
export DOCKER_HOST=unix://${XDG_RUNTIME_DIR}/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```

Then reload:

```bash
source ~/.bashrc
```

### 4. Run the tests

```bash
./gradlew test
```

---

## Windows

### 1. Install Java 25

Download and install the Temurin 25 JDK from [Adoptium](https://adoptium.net), or use [Scoop](https://scoop.sh):

```powershell
scoop bucket add java
scoop install temurin25-jdk
```

### 2. Install Docker Desktop

Download and install [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop). Start it and wait until the engine is running.

TestContainers detects Docker Desktop automatically — no extra environment variables needed.

### 3. Run the tests

```powershell
.\gradlew.bat test
```

---

## Windows WSL

You can use either Docker Desktop's WSL 2 integration (simpler) or Podman inside WSL (no Docker Desktop licence required).

### Option A — Docker Desktop WSL 2 integration

1. Install Docker Desktop and enable **Use the WSL 2 based engine** in Settings → General.
2. In Settings → Resources → WSL Integration, enable your distro.
3. Inside WSL, follow the **Linux** steps above to install Java 25, then run:

```bash
./gradlew test
```

No extra environment variables are needed; Docker Desktop exposes a socket that TestContainers finds automatically.

### Option B — Podman inside WSL

Inside your WSL distro, follow all four steps from the **Linux** section above.

---

## Dev Containers

Dev Containers run the full development environment inside a container. TestContainers needs access to a container runtime on the host.

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop) (Windows / macOS) or Podman with the socket enabled (Linux)
- VS Code with the [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers), or IntelliJ IDEA 2024.1+

### 1. Devcontainer configuration

The devcontainer configuration is already committed at
`.devcontainer/devcontainer.json` — no need to create it yourself. Before
first use, substitute `REGISTRY_HOST` as described in
[Registry placeholder](#registry-placeholder).

It mounts the **Podman** rootless socket by default (this is the primary
target for this project). The `mounts` entry forwards the host socket into
the dev container so TestContainers can start sibling containers.

**On Docker Desktop (Windows/macOS)** instead of Podman, change the mount
source in your local copy to:

```json
"source=/var/run/docker.sock,target=/var/run/docker.sock,type=bind"
```

**Bridge-networking note:** this dev environment's rootless Podman only
supports bridge networking (no `--network=host`). Because the devcontainer
itself is a sibling container on that bridge network, `localhost` inside it
does not reach containers TestContainers spins up via the mounted socket.
`devcontainer.json` sets `TESTCONTAINERS_HOST_OVERRIDE=host.containers.internal`
to work around this — Podman's built-in DNS alias for the host. If it doesn't
resolve in your setup, fall back to the Podman gateway IP instead. This only
affects the devcontainer's nested TestContainers usage; running
`./gradlew test` directly on the host (Linux/WSL sections above) is
unaffected.

### 2. Open in dev container

With [devpod](https://devpod.sh):

```bash
devpod provider add podman   # once
devpod up . --provider podman
```

In VS Code: **Command Palette → Dev Containers: Reopen in Container**

In IntelliJ: **File → Remote Development → Dev Containers → Open Project in Dev Container**

### 3. Run the tests

```bash
./gradlew test
```

---

## GitLab CI

The pipeline (`.gitlab-ci.yml`) runs without Docker-in-Docker (dind). Instead,
Postgres is provided via GitLab's `services:` keyword as a sidecar container,
and the test job overrides `SPRING_DATASOURCE_URL`/`USERNAME`/`PASSWORD` as
job variables. Spring Boot's relaxed env-var binding gives these a higher
precedence than the `jdbc:tc:postgresql:16-alpine:///test-db` URL in
`src/test/resources/application.yaml`, so the same test suite runs unmodified
against the real `services:` Postgres container instead of spinning up
TestContainers.

This means no GitLab runner/admin access is required — no privileged mode, no
`/dev/fuse`, no `cap_add` for nested Podman. That's precisely why this
approach was chosen over running Podman inside the CI job itself, which would
require runner-level configuration most projects can't get.

The job and service images are also routed through `REGISTRY_HOST` — see
[Registry placeholder](#registry-placeholder) before running the pipeline.
