FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY backend/ /app/backend/
COPY frontend/ /app/frontend/

WORKDIR /app/backend

RUN find . -name "*.java" > sources.txt && javac @sources.txt

CMD ["java", "TaskServer"]docker build --no-cache -t student-task-manager .