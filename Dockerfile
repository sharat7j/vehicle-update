FROM alpine:3.3

ADD version-updater/target/version-updater-1.0-SNAPSHOT.jar /app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]