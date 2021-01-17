#!/usr/bin/env bash

# remove all old unused images
docker rmi $(docker images -a -q)

# compile
mvn clean package -Dmaven.test.skip=true

# run services
docker-compose -p app-microservices-sample3 -f docker-compose.yml stop
docker-compose -p app-microservices-sample3 -f docker-compose.yml rm
docker-compose -p app-microservices-sample3 -f docker-compose.yml up -d --build