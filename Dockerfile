#
#
#FROM maven:3.9.4-eclipse-temurin-17 AS deps
#WORKDIR /opt/app
#
##what copy and where copy to
#COPY server_component/pom.xml server_component/pom.xml
#COPY storage_component/pom.xml storage_component/pom.xml
#COPY pom.xml .
#RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
#
#
#FROM maven:3.9.4-eclipse-temurin-17 AS builder
#WORKDIR /opt/app
#COPY --from=deps /root/.m2 /root/.m2
#COPY --from=deps /opt/app/ /opt/app
#COPY module1/src /opt/app/server_component/src
#COPY module2/src /opt/app/storage_component/src
#
#RUN mvn -B -e -o clean install -DskipTests=true
#
#FROM khipu/openjdk17-alpine:latest
#WORKDIR /opt/app
#COPY --from=builder /opt/app/server_component/target/server_component-0.0.1-SNAPSHOT.jar .
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/opt/app/server_component-0.0.1-SNAPSHOT.jar"]

FROM openjdk:22-ea-17-jdk-oracle
ADD server_component/target/server_component-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
RUN mkdir /ssTables
ENTRYPOINT ["java","-jar","application.jar"]