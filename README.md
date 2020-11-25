
# Test rest service

Project to test the communication between to rest services.

The goal is calculate the best route between two different locations, using two different criteria:

 - Fastest route, based on lower cost.
 - Shortest route, based on less stops.

It's a implementation of the dijkstra's algorithm path-finding

The project uses 3 different containers.

 - **route-calculator**.
 
Calculate the best route using the destination and origin passed as parameter.

The possible routes are provided by another rest service (route-service) 

 - **route-service**.
 
Allow CRUD actions of the different routes. The routes has the following format:

| id|city|destiny|departure|arrival|
| --|----|-------|---------|-------|
| 1|Barcelona|Madrid|09:00:00|12:00:00|
| 2|Barcelona|Valencia|09:00:00|16:00:00|
| 3|Madrid|Valencia|12:00:00|14:00:00|

 - **discovery-service**.
 
Eureka server instance to auto register the rest services.

 - **mysql-standalone**.

Store the routes on MySQL database.

## Build

The application is compiled inside the docker container using Maven.
It creates bigger containers, but allows build the project only with docker.

### Pre-requisites

 - [Git](https://git-scm.com/)
 - [Docker](https://docs.docker.com/engine/install/)
 - [Docker Composer](https://docs.docker.com/compose/install/)

The steps are:

 1. Clone the repository on the build machine

    git clone https://github.com/CGarces/test_restservices.git

 2. Build the containers using docker

    ```Shell
    docker build -t demo-routes/routes_service ./routes_service
    docker build -t demo-routes/discovery-service ./discovery-service
    docker build -t demo-routes/routes_calculator ./routes_calculator
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


Swagger Documentation are available (after run the project) at the following urls: 

 - http://localhost:8080/swagger-ui.html 
 - http://localhost:8081/swagger-ui.html

The status of each service can be checked on Eureka server

 - http://localhost:8761

## Architecture used.

The micro-services are developed using [Spring Boot framework](https://spring.io/projects/spring-boot). 
It creates stand alone application with this own application server embedded.

The services are registered on the eureka server that allow different possibilities (not all part of this sample code).

 - Identify the services without know the ip or port used,
 - Load balancing
 - Allow horizontal scaling of each service.
 - Manage different service versions
 - Service maintenance operations.

Swagger UI was used, in order to generate the API documentation, The Swagger interface can be used also to test the rest services.

MySQL was used to store the data. It's a well document DDBB in order to work with java JPA and setup a docker image.

