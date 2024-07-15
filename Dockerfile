FROM openjdk:17-jdk-slim

# Install FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080 9094
ENTRYPOINT ["java", "-jar", "app.jar"]