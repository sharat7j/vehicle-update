# vehicle-update
service to send and manage vehicle update workflows

# What

A spring boot application that provides APIs to manage large firmware update workflows for connected vehicles.
This service uses an In-memory database which can be easily swapped out to a real persistent layer by adding the jdbc
properties to the spring boot properties files.

The service supports most of the CRUD operations and allows for managing and tracking status updates for specific vehicles/rollOuts

Tech Stack:
- Spring boot
- Spring JPA in memory database
- Hibernate based ORM
- Unit testing metrics tool included - Jacoco
- Swagger API documentation
- Dockerized build/deployment

# Requirements

You will need
- maven to build this project to build the jar.
- Docker installed to be able to deploy this easily anywhere.
- Postman to easily download and test the integration test suite


# API docs

The API documentation is powered by swagger and can be located at /target/generated/swagger.json

# Unit test coverage report

The Jacoco reports can be accessed at /target/site/jacoco/index.html

# Integration test suite

For integration testing a postman collection is provided. Please use vehicle-update.postman_collection file to import the
collection into postman and run the collection.
