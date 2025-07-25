## build stage ##
FROM maven:3.5.3-jdk-8-alpine as build

WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

## run stage ##
FROM alpine:3.19

RUN adduser -D admin

RUN apk add openjdk8

WORKDIR /run
COPY --from=build /app/target/project_management-0.0.1-SNAPSHOT.jar /run/project_management-0.0.1-SNAPSHOT.jar

RUN chown -R admin:admin /run

USER admin

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/run/project_management-0.0.1-SNAPSHOT.jar"]
