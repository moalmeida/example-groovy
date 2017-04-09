#!/bin/bash

docker-compose down --remove-orphans
docker-compose pull
for i in `docker ps -a |grep desafio|awk '{print $1;}'`;do docker rmi $i -f;done
for i in `docker images |grep desafio|awk '{print $3;}'`;do docker rmi $i -f;done
docker-compose up -d --build
docker-compose logs --follow
