# spring-boot-4

A Spring Boot 4 / Kotlin project using TestContainers with PostgreSQL via the TC JDBC URL approach (`jdbc:tc:`). Tests spin up a real PostgreSQL container automatically — no running database required.

## Prerequisites

- Java 25
- A container runtime: **Podman** (Linux / WSL) or **Docker Desktop** (Windows / macOS)

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

### 1. Create `.devcontainer/devcontainer.json`

```json
{
  "name": "spring-boot-4",
  "image": "mcr.microsoft.com/devcontainers/java:1-25",
  "mounts": [
    "source=/var/run/docker.sock,target=/var/run/docker.sock,type=bind"
  ],
  "containerEnv": {
    "TESTCONTAINERS_RYUK_DISABLED": "true"
  },
  "customizations": {
    "vscode": {
      "extensions": [
        "vmware.vscode-spring-boot",
        "redhat.java"
      ]
    }
  }
}
```

The `mounts` entry forwards the host Docker socket into the dev container so TestContainers can start sibling containers.

**On Linux with Podman**, replace the mount source path with the Podman socket:

```json
"source=${localEnv:XDG_RUNTIME_DIR}/podman/podman.sock,target=/var/run/docker.sock,type=bind"
```

### 2. Open in dev container

In VS Code: **Command Palette → Dev Containers: Reopen in Container**

In IntelliJ: **File → Remote Development → Dev Containers → Open Project in Dev Container**

### 3. Run the tests

```bash
./gradlew test
```
