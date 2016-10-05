#!/bin/sh

echo "Stopping container if it already exists..."
docker stop tesla
docker rm tesla 
echo "running a full maven build..."
mvn clean install
echo "building new container..."
docker build -t tesla .
echo "deploying container..."
docker run -d -p 80:8080 --name tesla tesla
