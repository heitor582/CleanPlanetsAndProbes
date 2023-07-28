FROM openjdk:17-oracle AS TEMP_BUILD_IMAGE

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

RUN microdnf install findutils

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY buildSrc $APP_HOME/buildSrc

RUN ./gradlew build
COPY . .
RUN ./gradlew build

FROM openjdk:17-oracle

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENV ARTIFACT_NAME=application.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT java -jar ./$ARTIFACT_NAME --spring.profiles.active=production