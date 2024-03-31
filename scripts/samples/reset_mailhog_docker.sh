#!/bin/bash

container_name="rc-mail-hog"

if docker ps -a --format '{{.Names}}' | grep -q "^$container_name$"; then
    docker rm -f $container_name
fi

docker run -d --name $container_name -p 1025:1025 -p 8025:8025 mailhog/mailhog
