FROM azul/zulu-openjdk-alpine:17
RUN apk --no-cache add curl gcompat
COPY target/dapla-team-api-*.jar dapla-team-api.jar
EXPOSE 8080
CMD ["java", "-jar", "dapla-team-api.jar", "--spring.config.location=/config/"]
