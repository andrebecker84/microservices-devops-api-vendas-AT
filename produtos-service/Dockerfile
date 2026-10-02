# --- Estágio 1: build ---
# Imagem com Maven + JDK 25, usada só para compilar. Não vai pra imagem final.
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copia só o pom.xml primeiro (sem o código-fonte ainda)
COPY pom.xml .
# Baixa as dependências agora. Se o pom.xml não mudar, o Docker reaproveita
# essa camada em cache e pula o download nos builds seguintes.
RUN mvn dependency:go-offline -B

# Só agora copia o código-fonte (muda com mais frequência que o pom.xml)
COPY src ./src
# Compila e empacota em .jar. -DskipTests acelera o build pulando os testes.
RUN mvn package -DskipTests -B

# --- Estágio 2: runtime ---
# Imagem final, só com o JRE (bem mais leve; não carrega Maven nem código-fonte)
FROM eclipse-temurin:25-jre
WORKDIR /app

# Traz apenas o .jar gerado no estágio "build" acima
COPY --from=build /app/target/*.jar app.jar

# Roda sem privilegio de root: uma falha explorada na aplicacao nao vira root no container.
RUN useradd --system --no-create-home app
USER app

# Documenta a porta usada pela aplicação (não publica sozinha; quem expõe
# de verdade pra fora do container é o "ports:" no docker-compose.yml)
EXPOSE 8081

# Comando executado quando o container sobe
ENTRYPOINT ["java", "-jar", "app.jar"]