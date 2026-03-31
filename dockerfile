FROM openjdk:17
WORKDIR /app
COPY backend/ .
RUN javac TaskServer.java
CMD ["java", "TaskServer"]