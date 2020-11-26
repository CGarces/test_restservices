
# Test rest service

Project to test the communication between to rest services.

The goal is calculate the best route between two different locations, using two different criteria:

 - Fastest route, based on lower cost.
 - Shortest route, based on less stops.

It's a implementation of the dijkstra's algorithm path-finding

The project uses 5 different containers.

 - **routes-calculator**.
 
Calculate the best route using the destination and origin passed as parameter.

The possible routes are provided by another rest service (route-service) 

 - **routes-service**.
 
Allow CRUD actions of the different routes. The routes has the following format:

| id|city|destiny|departure|arrival|
| --|----|-------|---------|-------|
| 1|Barcelona|Madrid|09:00:00|12:00:00|
| 2|Barcelona|Valencia|09:00:00|16:00:00|
| 3|Madrid|Valencia|12:00:00|14:00:00|

 - **discovery-service**.
 
Eureka server instance to auto register the rest services.

 - **routes-gateway**.
 
Zuul proxy implementation to allow the use for the same endpoint for all rest APIs.

 - **mysql-standalone**.

Store the routes on MySQL database.

## Build

### Pre-requisites

 - [Git](https://git-scm.com/)
 - [Docker](https://docs.docker.com/engine/install/)
 - [Docker Composer](https://docs.docker.com/compose/install/)

The steps are:

 1. Clone the repository on the build machine

    git clone https://github.com/CGarces/test_restservices.git

 2. Build the containers using docker and maven wrapper

    ```Shell
    ./mvnw compile jib:dockerBuild
    ```

## Run

To run the application Docker Composer is used

    docker-compose up -d

## Usage

The deployed containers expose the services as rest micro-services

 - route-calculator
	 - http://localhost:8081/
	 - sample call
		 - http://localhost:8081/Barcelona/Valencia

 - route-service
	 - http://localhost:8080/
	 - sample call
		 - http://localhost:8080/routes

The services are forwarded by Zuul and can be accessed from a single port.

		 - curl http://localhost:8662/Barcelona/Valencia
		 - curl http://localhost:8662/routes


Swagger Documentation are available (after run the project) at the following urls: 

 - http://localhost:8080/swagger-ui.html 
 - http://localhost:8081/swagger-ui.html

The status of each service can be checked on Eureka server

 - http://localhost:8761

## Architecture used.

The micro-services are developed using [Spring Boot framework](https://spring.io/projects/spring-boot). 
It creates a standalone application with his own application server embedded.

The services are registered on the eureka server that allow different possibilities (not all part of this sample code).

 - Identify the services without know the ip or port used,
 - Load balancing
 - Allow horizontal scaling of each service.
 - Manage different service versions
 - Service maintenance operations.

The services are fordware by Netflix Zuul, to forward requests to the different application services from a single endpoint.

Swagger UI was used, in order to generate the API documentation, The Swagger interface can be used also to test the rest services.

MySQL was used to store the data. It's a well documented DDBB in order to work with java JPA and setup a docker image.

The services can be deployed as Docker containers. To simplify the build process JIB is used, it allows to generate the containers without needing a separate Docker file.
