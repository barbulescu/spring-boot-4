Add the following environment variables to have Docker support working:

```
export DOCKER_HOST=unix://${XDG_RUNTIME_DIR}/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```

Enable the Podman socket (once, persists across reboots):

```
systemctl --user enable --now podman.socket
```
