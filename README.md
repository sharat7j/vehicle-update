# Vehicle Software Management Platform
Service to manage vehicle update workflows

## What

A spring boot application that provides APIs to manage large firmware update workflows for connected vehicles.
This service uses an In-memory database which can be easily swapped out to a real persistent layer by adding the jdbc
properties to the spring boot properties files.

The service supports most of the CRUD operations and allows for managing and tracking status updates for specific vehicles/rollOuts

### Tech Stack:
- Spring boot
- Spring JPA in memory database
- Hibernate based ORM
- Unit testing metrics tool included - Jacoco
- Swagger API documentation
- Dockerized build/deployment

## Requirements

You will need
- maven to build this project to build the jar.
- Docker (>Version 1.12.0) installed to be able to deploy this easily anywhere.
- Postman client to easily download and test the integration test suite


## Deployment

To deploy we have multiple ways:

- Docker:  A utility script is provided here to enable you to deploy out of the box (provided you have maven and docker installed)

   ```
   $> ./redeploy.sh
   ```
   The service would be deployed at localhost:80
- Jar: The jar can be built by using maven. Once built we can run the jar as indicated below
   ```
   $> mvn clean install
   $> java -jar version-updater/target/version-updater-1.0-SNAPSHOT.jar
   ```
   The service would be deployed at localhost:8080

## API docs

The API documentation is powered by swagger and can be located at /target/generated/swagger.json

To generate a html version of the API documentation you can use bootprint

```
npm install -g bootprint
npm install bootprint-swagger 
bootprint swagger target/generated/swagger.json docs
```
You'll see the index.html file created under the docs folder with all the css

## Unit test coverage report

The Jacoco reports can be accessed at /target/site/jacoco/index.html

## Integration test suite

For integration testing a postman collection is provided. Please use vehicle-update.postman_collection file to import the
collection into postman and run the collection.

To import a collection into postman refer: https://www.getpostman.com/docs/collections
To run the suite from the collection refer: https://www.getpostman.com/docs/running_collections

## Pending Work

Due to shortage of time the following areas could not be looked at 

- authentication
- SSL support
- logback configurations - logs need to be rolled up and rotated
- i18N and localizaton support for APIs. 

 
