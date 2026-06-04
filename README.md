# DataShare — Backend

API REST développée avec Spring Boot pour l'application de partage de fichiers DataShare.

## Stack technique

- Java 21
- Spring Boot 3.4.5
- PostgreSQL 16
- Amazon S3
- JWT (jjwt 0.12.6)

## Prérequis

- Java 21
- Maven 3.8.7
- PostgreSQL 16

## Installation

```bash
git clone git@github.com:SylvainR-dev/backend-datashare.git
cd backend-datashare
```

## Configuration

Créer un fichier `.env` à la racine :


DB_URL=jdbc:postgresql://localhost:5432/datashare
DB_USERNAME=ton_utilisateur
DB_PASSWORD=ton_mot_de_passe
JWT_SECRET=ta_clé_secrète
AWS_ACCESS_KEY=ta_clé_aws
AWS_SECRET_KEY=ta_clé_secrète_aws
AWS_BUCKET_NAME=ton_bucket
AWS_REGION=ta_région



## Lancer l'application

```bash
mvn spring-boot:run
```

API disponible sur `http://localhost:8080`  
Swagger UI : `http://localhost:8080/swagger-ui/index.html`

## Lancer les tests

```bash
mvn test
```

## Documentation

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [TESTING.md](TESTING.md)
- [SECURITY.md](SECURITY.md)
- [PERF.md](PERF.md)
- [MAINTENANCE.md](MAINTENANCE.md)