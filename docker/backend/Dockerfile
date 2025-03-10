# build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# Runtime stage
FROM amazoncorretto:17
# define few things
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/deliverysystem-*.jar /app/
EXPOSE 8080

ENV DB_URL=jdbc:postgresql://postgres-sql-ds:5432/delivery_db
ENV ACTIV_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=missing_hostname
ENV EMAIL_USERNAME=missing_username
ENV EMAIL_PASSWORD=missing_password

CMD java -jar -Dspring.profiles.active=${ACTIV_PROFILE} -Dspring.datasource.url=${DB_URL} deliverysystem-${JAR_VERSION}.jar
