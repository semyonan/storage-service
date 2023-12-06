FROM openjdk:22-ea-17-jdk-oracle
ADD server_component/target/server_component-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
RUN mkdir /ssTables
ENTRYPOINT ["java","-jar","application.jar"]