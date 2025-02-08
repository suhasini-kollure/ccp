# Stage 1: Build the application
FROM gradle:jdk17 AS build
WORKDIR /home/gradle/project
COPY . .

# Apply code formatting
RUN ./gradlew spotlessApply --no-daemon

# Build the project
RUN gradle clean build --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/ccp-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
