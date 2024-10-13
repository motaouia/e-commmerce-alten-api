FROM openjdk:17-jdk-alpine

WORKDIR /e-commerce-alten

COPY target/e-commerce-alten.jar /e-commerce-alten/e-commerce-alten.jar

EXPOSE 9988

ENTRYPOINT ["java", "-jar", "/e-commerce-alten/e-commerce-alten.jar"]