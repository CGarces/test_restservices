FROM maven:3.6.3-openjdk-8-slim AS build 
COPY src /home/app/src
copy pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build "home/app/target/routescalculator-*.jar" "app.jar"
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
