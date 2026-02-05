# =========================
# STAGE 1: Build
# =========================
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY . .
RUN mvn -B -q -DskipTests clean package

# =========================
# STAGE 2: Runtime
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    tzdata \
 && rm -rf /var/lib/apt/lists/*

ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC"
ENV SERVER_PORT=8080

EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar app.jar"]
