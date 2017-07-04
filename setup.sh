#!/bin/sh

# Prerequisites
#   - Docker & Docker Compose

unset DOCKER_GROUP_ID
DOCKER_GROUP_ID=`getent group docker | cut -d: -f3`
echo "DOCKER_GROUP_ID=${DOCKER_GROUP_ID}" > .env

echo "Startup Docker Compose"
docker-compose -f docker-compose.yml up -d --build
