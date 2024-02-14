#!/bin/bash

container_name="realtor-connect-db"
database_name="realtor-connect"

if docker ps -a --format '{{.Names}}' | grep -q "^$container_name$"; then
    docker rm -f $container_name
fi

docker run -d --name $container_name -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=rc.postgres -e POSTGRES_DB=$database_name postgres:15.0
