# Build aşaması
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Uygulama aşaması
FROM openjdk:17-jdk-slim
WORKDIR /app

# OpenSSH istemcisini yükle
RUN apt-get update && apt-get install -y openssh-client

# Uygulama dosyalarını kopyala
COPY ./target/DatabaseManager-0.0.1-SNAPSHOT.jar /app/DatabaseManager.jar


EXPOSE 8080

# Uygulama başlatma komutu
CMD ["java", "-jar", "DatabaseManager.jar"]
