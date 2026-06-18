FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
RUN mvn -B dependency:go-offline -pl user-service -am

COPY user-service/src user-service/src
RUN mvn -B clean package -pl user-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]