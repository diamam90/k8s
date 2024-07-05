FROM maven:3.9.8 as build

WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:17-jdk-jammy

COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar","app.jar"]