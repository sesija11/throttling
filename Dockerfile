FROM gradle:7.3-jdk17-alpine AS builder
WORKDIR /app
COPY . /app
RUN gradle clean build -x test

FROM openjdk:17-alpine AS runner
COPY --from=builder /app/build/libs/task-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","task-0.0.1-SNAPSHOT.jar"]