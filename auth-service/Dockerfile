# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copiamos el pom padre primero (para cachear dependencias)
COPY pom.xml .

# Copiamos los pom.xml de todos los módulos (necesarios para que Maven resuelva el reactor)
COPY auth-service/pom.xml auth-service/pom.xml
COPY product-service/pom.xml product-service/pom.xml
COPY purchase-service/pom.xml purchase-service/pom.xml
COPY finance-service/pom.xml finance-service/pom.xml
COPY sales-service/pom.xml sales-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml
COPY audit-service/pom.xml audit-service/pom.xml
COPY reporting-service/pom.xml reporting-service/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

# Copiamos el código fuente de auth-service
COPY auth-service/src auth-service/src

# Compilamos solo este módulo
RUN mvn -pl auth-service -am package -DskipTests

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/auth-service/target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]