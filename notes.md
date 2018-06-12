# location 2373 in microservices book.
- Always keep ownership of the domain entities in 1 microservice so there is only 1 source of truth. Don't create a shared
module for the domain classes that both microservices use. Instead, generate simple copies of your model that are the same as whatever the other
REST service you're consuming. The key thing to note is: keep separate DTO's for different API versions. If a Person is modelling a json response from
api A for v1, then for v2 create another DTO to keep them separate.

Alternatively you can define a custom JsonDeserializer to only grab out the fields in the response to create a DTO that makes it easier for the 
microservice you're working in.


# when using docker toolbox, localhost doesnt work, use the IP address that docker toolbox gives to the VM

docker rm $(docker ps -a -q)

docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:3-management
docker run -d -p 8081:8080 --name gamification microservice/gamification-service:0.0.1-SNAPSHOT
docker run -d -p 8080:8080 --name multiplication microservice/multiplication-service:0.0.1-SNAPSHOT

docker images --format "{{.Repository}} {{.ID}}" | awk '/^microservice/  {print $2}' | xargs docker rmi
docker rmi $(docker images --format "{{.Repository}} {{.ID}}" | awk '/^microservice/  {print $2}')



# git submodules

### Adding a new git repostory as a submodule to the parent folder.
git submodule add ./service-registry


### when cloning a project with submodules
git clone https://github.com/...MainProject
MyProject will show all the submodules, but these submodules will be empty so...

git submodule init to initialize your local configuration file, and then git submodule update
to fetch all the data from that project and check out the appropriate commit listed in your superproject:
