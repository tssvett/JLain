FROM openjdk
WORKDIR /app

COPY  ./target/schedule-bot-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "schedule-bot-0.0.1-SNAPSHOT.jar"]