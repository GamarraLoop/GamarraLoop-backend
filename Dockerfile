# ---------- Stage 1: build ----------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copiamos el wrapper de Maven y el pom primero para aprovechar la cache de capas
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw && ./mvnw -B -q dependency:go-offline

# Copiamos el código y empaquetamos (sin tests para acelerar el build)
COPY src ./src
RUN ./mvnw -B -q clean package -DskipTests

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el jar resultante desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Render inyecta la variable PORT; la app ya lee ${PORT:8080} en application.yml.
EXPOSE 8080

# MaxRAMPercentage=75 mantiene el heap dentro de los 512 MB del plan free de Render.
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75.0 -jar app.jar"]
