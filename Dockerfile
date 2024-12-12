# Этап 1: Загрузка зависимостей
FROM maven:3.9.7-eclipse-temurin-17 AS dependencies
WORKDIR /opt/app
COPY pom.xml .
RUN mvn -B -e -am dependency:go-offline

# Этап 2: Сборка приложения
FROM maven:3.9.7-eclipse-temurin-17 AS builder
WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY .git/ /opt/app/.git/
COPY src /opt/app/src/
RUN mvn -B -e -am clean install -DskipTests

# Этап 3: Финальный образ
FROM eclipse-temurin:17.0.8_7-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]
