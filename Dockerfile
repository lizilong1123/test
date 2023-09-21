FROM java:8

WORKDIR /

ADD /target/HelloWorld.jar HelloWorld.jar

EXPOSE 8080

CMD java -jar HelloWorld.jar