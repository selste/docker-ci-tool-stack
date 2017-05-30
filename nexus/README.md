## Nexus Docker Container

The Dockerfile builds and starts a Nexus Repository Proxy.

### Usage - Build manually

```
docker build -t nexus .
docker run -d -p 8081:8081 --name nexus nexus
```
