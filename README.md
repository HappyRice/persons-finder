# Welcome

This is a start-up Git repo for a project we are calling 'Persons finder'.

Your job is to create a set of API's that will feed a mobile application that has one purpose: find people around you.

Requirements:
- POST API to create a 'person'
- PUT API to update/create someone's location using latitude and longitude
- GET API to retrieve people around query location with a radius in KM, Use query param for radius. Extra challenge: Return list ordered by distance to each person.
- GET API to retrieve a person or persons name using their ids
- Responses must follow a JSON format

You'll also need to build the logic and services for saving/retrieving locations and persons.

Steps:
- Clone the project
- Implement required API's and services
- Push your project to your personal github


### Extra challenge
### Add a million, 10 million and 100 million entries and challenge your API's efficiency 

For any questions, please reach out on: leo@getsquareone.app

To run unit & integration tests:
```sh
gradle test
```

To build:
```sh
gradle build assemble
```

To run:
```sh
java -jar build/libs/PersonsFinder-0.0.1-SNAPSHOT.jar
```

To manually test endpoints, open the swagger UI: http://localhost:8080/swagger-ui.html

Notes:
- We are using a precision of 6 decimal places for the lat and long values which should be accurate up to ~11cm enough to identify a person
- Accuracy is based on the Haversine formulae
- swagger.json is generated as part of the build
- Using Java 8

Improvements:
- Dockerise the service
- Addition of a UpdateHistory table to track location history
- Haven't actually used Gradle before but with Maven, I would split the project into 4 modules, common, model, persistence and service.
- Set up physical database with Flyway
