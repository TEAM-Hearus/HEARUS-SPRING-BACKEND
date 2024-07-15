FROM openjdk:17-jdk-slim

# Install FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

WORKDIR /app
COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY src ./src
RUN ./gradlew build --no-daemon

COPY build/libs/*.jar app.jar
EXPOSE 8080 9094

ENTRYPOINT ["java", "-jar", "app.jar"]