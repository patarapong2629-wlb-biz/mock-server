# Mock-Server

This repo is for practicing on how to create mock API.

- [Mountebank](./mountebank/)
- [WireMock](./wiremock/)
- [Smocker](./smocker/)
- [Prism](./prism/)
- [MockServer](./mockserver/)
- [Node-RED](./node-red/)

## Getting Started

### Prerequisites

- Docker & Docker Compose

  Docker version must be equal or more than 20.x and Docker Compose version must be equal or more than 2.x.

  ```shell
  # To check Docker version
  docker --version

  # To check Docker Compose version
  docker compose --version
  ```

  If you have not installed the Docker Desktop yet. Download [here](https://www.docker.com/products/docker-desktop/).

### Installation

- Clone the repository: `git clone git@github.com:patarapong2629-wlb-biz/mock-server.git`

### Start & Stop Server

- To start mock servers: `make start` or `docker compose up --build -d`

- To stop mock servers: `make down` or `docker compose down`
