FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
COPY pom.xml /build/

COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -DskipTests=true

FROM openjdk:8-jre-alpine

WORKDIR /app
COPY src/main.roles.sql /app/roles.sql
COPY --from=MAVEN_BUILD /build/target/*.jar /app/
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown -R spring:spring /app
USER spring:spring
ENTRYPOINT ["java","-jar", "*.jar"]
