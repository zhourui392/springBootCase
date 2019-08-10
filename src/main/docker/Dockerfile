FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN apk add -U tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    date

VOLUME /home/apps

ADD spring-tools-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xms1000m -Xmx1000m"


ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar  --spring.profiles.active=$SPRING_PROFILES_ACTIVE" ]