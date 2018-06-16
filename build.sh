#!/bin/bash

docker rm $(docker ps -aq)
docker rmi $(docker images --format "{{.Repository}} {{.ID}}" | awk '/^microservice/  {print $2}')

# uncomment if the docker-compose file does not include the build section.
# mvn clean package -DskipTests=true -f ./multiplication-service
# mvn clean package -DskipTests=true -f ./gamification-service
# mvn clean package -DskipTests=true -f ./gateway
# mvn clean package -DskipTests=true -f ./service-registry

# docker-compose up --detach --scale multiplication-service=2
# docker-compose up -d


# docker run -p 8761:8761 --env EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/ microservice/service-registry:0.0.1-SNAPSHOT

# theres too many issues with docker-compose and its way too slow. open each project in intellij and run maven. to scale change the port which will register with eureka. 

docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management


mvn spring-boot:run -f ./service-registry 
mvn spring-boot:run -f ./gateway

 mvn spring-boot:run -D server.port=8081 -f ./multiplication-serivce
 mvn spring-boot:run -D server.port=8082 -f ./multiplication-serivce


mvn spring-boot:run -Drun.arguments="--server.port=8083" -f ./gamification-service



# multiplication service
mvn spring-boot:run -D server.port=8081 
mvn spring-boot:run -D server.port=8082

# gamification service
mvn spring-boot:run -D server.port=8083

# everything else has its own port
mvn spring-boot:run
