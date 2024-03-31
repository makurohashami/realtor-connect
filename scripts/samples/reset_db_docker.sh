#!/bin/bash

container_name="rc-db"
database_name="realtor-connect"
postgres_user="postgres"
postgres_pass="postgres"
postgres_port="5432"

if docker ps -a --format '{{.Names}}' | grep -q "^$container_name$"; then
    docker rm -f $container_name
fi

docker run -d --name $container_name -p $postgres_port:5432 -e POSTGRES_USER=$postgres_user -e POSTGRES_PASSWORD=$postgres_pass -e POSTGRES_DB=$database_name postgres:15.0
