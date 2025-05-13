FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY skeleton-starter-flow-spring-24/ .
RUN chmod +x mvnw && ./mvnw clean package -Pproduction -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
