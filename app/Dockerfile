FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
COPY pom.xml /build/

COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -DskipTests=true

FROM openjdk:8-jre-alpine

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app/
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown -R spring:spring /app
USER spring:spring
ENTRYPOINT ["/bin/sh", "-c", "sleep 20 && java -jar spring-security-0.0.1-SNAPSHOT.jar"]
