# Football API - REST Spring Boot

API REST pour gérer des équipes de football avec pagination et tri.

## 📋 Versions

- **Java** : 21+
- **Maven** : 3.6+
- **Spring Boot** : 4.0.0
- **PostgreSQL** : 15 (Docker)

## 🚀 Installation

### Prérequis
- Docker et Docker Compose
- Java 21+
- Maven 3.6+

### 1. Lancer PostgreSQL avec Docker
```bash
docker-compose up -d
```

Vérifie que le conteneur `football_db` est en cours d'exécution :
```bash
docker ps
```

### 2. Compiler et lancer l'app
```bash
mvn clean install
mvn spring-boot:run
```

La base de données sera créée automatiquement.

**API disponible sur :** `http://localhost:8080`

### 3. Arrêter PostgreSQL
```bash
docker-compose down
```

## 🧪 Tests
```bash
mvn test
```

## 📡 Bruno (Tester l'API)

1. Installer Bruno : https://www.usebruno.com/
2. Ouvrir Bruno
3. **File** → **Open Collection** → sélectionner le dossier `bruno/`
4. Les requêtes sont prêtes à tester

## 📚 API Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/teams` | Récupérer toutes les équipes (paginé) |
| GET | `/api/v1/teams/{id}` | Récupérer une équipe |
| POST | `/api/v1/teams` | Créer une équipe |
| PUT | `/api/v1/teams/{id}` | Modifier une équipe |
| DELETE | `/api/v1/teams/{id}` | Supprimer une équipe |

### Paramètres GET /api/v1/teams
```
page=0          # Numéro de page (défaut: 0)
size=10         # Résultats par page (défaut: 10)
sort=name,asc   # Tri: name, acronym, budget (asc/desc)
```

Exemple :
```
GET /api/v1/teams?page=0&size=10&sort=budget,desc
```

## 📋 Structure du Projet
```
src/
├── main/java/com/example/football_db/
│   ├── controller/      # REST Controllers
│   ├── service/         # Logique métier
│   ├── repository/      # Accès données
│   ├── entity/          # Entités JPA
│   ├── dto/             # Data Transfer Objects
│   ├── constant/        # Énumérations
│   └── exception/       # Exceptions custom
├── resources/
│   └── application.properties
└── test/                # Tests unitaires/intégration
```

## 🎯 Justification des Choix Techniques

### PostgreSQL
**Pourquoi :** Production-ready, simule mieux l'environnement final qu'une base embarquée (H2).

### Architecture 3-tiers (Controller → Service → Repository)
**Pourquoi :** Sépare les responsabilités.

### DTOs (Data Transfer Objects)
**Pourquoi :** Crée une couche d'isolation entre la base et l'API.

### Validation Jakarta
**Pourquoi :** Annotations sur les DTOs, validation centralisée et déclarative.

### UUID pour les IDs
**Pourquoi :** Générable sans coordination base de données, plus sécurisé que des IDs séquentiels.

### Docker pour PostgreSQL
**Pourquoi :** Environnement reproductible.

## 🎯 Positions Disponibles

- `GARDIEN`
- `DEFENSEUR`
- `MILIEU`
- `ATTAQUANT`

## 💡 Exemple Requête POST
```json
POST /api/v1/teams
Content-Type: application/json

{
  "name": "OGC Nice",
  "acronym": "OGCN",
  "budget": 60000000,
  "players": [
    {"name": "Alexandre Olliero", "position": "GARDIEN"},
    {"name": "Dante", "position": "DEFENSEUR"}
  ]
}
```

## 🏗️ Stack Technique

- **Spring Boot 4.0.0** : Framework principal
- **PostgreSQL** : Base de données
- **Spring Data JPA** : ORM
- **Hibernate** : Mapping entités-tables
- **Jakarta Validation** : Validation des données
- **SLF4J + Logback** : Logging
- **JUnit 5 + Mockito** : Tests