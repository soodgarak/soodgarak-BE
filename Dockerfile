FROM openjdk:17-alpine

WORKDIR /app

RUN apk update && apk add tzdata
ENV TZ=Asia/Seoul

COPY build/libs/Soodgarak-0.0.1-SNAPSHOT.jar /app
COPY src/main/resources /app/resources

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Soodgarak-0.0.1-SNAPSHOT.jar"]