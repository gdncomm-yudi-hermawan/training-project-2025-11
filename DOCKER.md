# Docker Guide for Marketplace Microservices

This guide explains how to run the Marketplace Microservices application using Docker and Docker Compose. This setup containerizes all services (API Gateway, Member, Product, Cart) and their dependencies (PostgreSQL, MongoDB, Redis).

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- Git (to clone the repository)

## WSL2 (Windows) Setup Recommended

If you are on Windows, it is **highly recommended** to run this project using WSL2 (Windows Subsystem for Linux) for better performance and compatibility.

1.  **Install WSL2**: Run `wsl --install` in PowerShell as Administrator and restart your computer.
2.  **Configure Docker Desktop**:
    - Open Docker Desktop Settings.
    - Go to **General** and ensure "Use the WSL 2 based engine" is checked.
    - Go to **Resources > WSL Integration** and enable integration for your default distro (e.g., Ubuntu).
3.  **Move Project to WSL Filesystem**:
    - **Crucial for Performance**: Do not run the project from `/mnt/c/Users/...`.
    - Open your WSL terminal (e.g., Ubuntu).
    - Clone or move this project to your home directory inside WSL (e.g., `~/projects/marketplace`).
    - `cd` into that directory.
4.  **Run Commands**: Execute all `docker-compose` commands from within the WSL terminal.

### Option 2: Native Docker Engine (CLI Only - No Docker Desktop)

If you prefer a purely command-line experience without installing Docker Desktop on Windows, you can install Docker Engine directly inside your WSL2 distro.

1.  **Prerequisites**:
    - Ensure you are running WSL2.
    - Ensure `systemd` is enabled. Check `/etc/wsl.conf`:
      ```ini
      [boot]
      systemd=true
      ```
      (Restart WSL with `wsl --shutdown` if you change this).

2.  **Install Docker Engine**:
    Run the following commands inside your WSL terminal (Ubuntu):
    ```bash
    # Remove old versions
    sudo apt-get remove docker docker-engine docker.io containerd runc

    # Set up repository
    sudo apt-get update
    sudo apt-get install ca-certificates curl gnupg
    sudo install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    sudo chmod a+r /etc/apt/keyrings/docker.gpg

    # Add repository
    echo \
      "deb [arch=\"$(dpkg --print-architecture)\" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

    # Install Docker
    sudo apt-get update
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    ```

3.  **Post-Installation**:
    ```bash
    # Add your user to docker group (so you don't need sudo)
    sudo usermod -aG docker $USER
    
    # Activate changes
    newgrp docker
    
    # Start Docker service
    sudo service docker start
    ```

4.  **Verify**:
    ```bash
    docker run hello-world
    ```

## Quick Start

1. **Build and Start All Services**

   Open a terminal in the project root directory and run:

   ```bash
   docker-compose up -d --build
   ```

   This command will:
   - Build the Docker images for all microservices (using multi-stage builds)
   - Start PostgreSQL, MongoDB, and Redis containers
   - Start all microservice containers
   - Connect them via a custom bridge network

2. **Check Status**

   To see if all containers are running:

   ```bash
   docker-compose ps
   ```

   You should see all services with status `Up (healthy)`.

3. **View Logs**

   To follow the logs of all services:

   ```bash
   docker-compose logs -f
   ```

   To view logs for a specific service (e.g., member-service):

   ```bash
   docker-compose logs -f member-service
   ```

4. **Stop Services**

   To stop all services:

   ```bash
   docker-compose down
   ```

   To stop and remove volumes (WARNING: this deletes database data):

   ```bash
   docker-compose down -v
   ```

## Architecture

The Docker setup consists of the following containers:

| Service | Container Name | Port | Description |
|---------|----------------|------|-------------|
| **API Gateway** | `marketplace-gateway` | 8080 | Entry point for all requests |
| **Member Service** | `marketplace-member` | 8081 | Handles user registration & auth |
| **Product Service** | `marketplace-product` | 8082 | Manages product catalog |
| **Cart Service** | `marketplace-cart` | 8083 | Manages shopping carts |
| **PostgreSQL** | `marketplace-postgres` | 5432 | DB for Member & Cart services |
| **MongoDB** | `marketplace-mongodb` | 27017 | DB for Product service |
| **Redis** | `marketplace-redis` | 6379 | Caching service (optional) |

## Configuration

### Environment Variables

Configuration is managed via the `.env` file in the project root. You can modify this file to change ports, credentials, or other settings.

**Important:** The default `.env` file contains default credentials for development. For production, you should change these values.

### Database Initialization

- **PostgreSQL**: Automatically creates `marketplace_member` and `marketplace_cart` databases on first startup using the script in `docker/postgres-init/`.
- **MongoDB**: Automatically initializes the `marketplace_product` database using the script in `docker/mongo-init/`.

## Troubleshooting

### Services fail to start?

Check the logs to see the error:
```bash
docker-compose logs <service-name>
```

Common issues:
- **Port conflicts**: Ensure ports 8080-8083, 5432, 27017, and 6379 are not used by other applications on your host machine.
- **Database connection**: Services might try to connect before the database is ready. The `depends_on` with `condition: service_healthy` in `docker-compose.yml` should prevent this, but if it happens, the services will restart automatically.

### How to rebuild a specific service?

If you made changes to the code of one service (e.g., member-service), you can rebuild just that service:

```bash
docker-compose up -d --build member-service
```

### How to connect to the database manually?

**PostgreSQL:**
```bash
docker exec -it marketplace-postgres psql -U postgres -d marketplace_member
```

**MongoDB:**
```bash
docker exec -it marketplace-mongodb mongosh
```

## Development Workflow

1. Make code changes in your IDE.
2. Rebuild the specific service using `docker-compose up -d --build <service-name>`.
3. Test the changes via API Gateway (http://localhost:8080).

For faster development iteration, you might prefer running the services locally (in your IDE) while running only the databases in Docker.
