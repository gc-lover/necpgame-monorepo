FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/workqueue-service-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]


