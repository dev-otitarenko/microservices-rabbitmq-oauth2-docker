#!/usr/bin/env bash

# run db instance
docker-compose -p app-microservices-sample3 -f docker-compose-db.yml stop
docker-compose -p app-microservices-sample3 -f docker-compose-db.yml rm
docker-compose -p app-microservices-sample3 -f docker-compose-db.yml up -d --build