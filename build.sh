#!/bin/bash

docker rmi $(docker images --format "{{.Repository}} {{.ID}}" | awk '/^microservice/  {print $2}')

# uncomment if the docker-compose file does not include the build section.
mvn clean package -DskipTests=true -f ./multiplication-service
mvn clean package -DskipTests=true -f ./gamification-service
mvn clean package -DskipTests=true -f ./gateway
mvn clean package -DskipTests=true -f ./service-registry

docker-compose up --detach --scale multiplication-service=2
# docker-compose up --detach
