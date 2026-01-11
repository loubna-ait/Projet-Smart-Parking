# Étape 1 : Image de base avec Maven et JDK 17
FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app

# Copier le fichier pom.xml et les dépendances pour mise en cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

# On utilise Maven pour lancer les tests car il gère automatiquement le classpath
# Note : On pointe vers le service DB dans docker-compose
ENTRYPOINT ["mvn", "compile", "test-compile", "exec:java", "-Dexec.mainClass=ma.emsi.tests.MainTest", "-Dexec.classpathScope=test"]
