# Delivery System

Plateforme de gestion de livraisons et de flotte. Elle permet de suivre les missions, de gérer les chauffeurs, 
les véhicules et leurs réparations, ainsi que les clients, les devis et les commandes, depuis un tableau de bord d'administration.

[![Backend CI](https://github.com/[utilisateur]/delivery-system/actions/workflows/pipeline-backend.yml/badge.svg)](https://github.com/[utilisateur]/delivery-system/actions/workflows/pipeline-backend.yml)
[![Frontend CI](https://github.com/[utilisateur]/delivery-system/actions/workflows/pipeline-frontend.yml/badge.svg)](https://github.com/[utilisateur]/delivery-system/actions/workflows/pipeline-frontend.yml)

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Stack technique](#stack-technique)
- [Prérequis](#prérequis)
- [Démarrage rapide avec Docker](#démarrage-rapide-avec-docker)
- [Développement local](#développement-local)
- [Configuration](#configuration)
- [Documentation de l'API](#documentation-de-lapi)
- [Tests](#tests)
- [Déploiement et CI/CD](#déploiement-et-cicd)
- [Structure du projet](#structure-du-projet)
- [Feuille de route](#feuille-de-route)
- [Contribuer](#contribuer)
- [Licence](#licence)
- [Auteur](#auteur)

## Fonctionnalités

- **Authentification** : inscription, connexion, renouvellement du token JWT et gestion des rôles.
- **Utilisateurs** : administration des comptes et de leurs droits d'accès.
- **Chauffeurs** : fiches, contrats de travail, statistiques et affectation aux véhicules.
- **Flotte et maintenance** : véhicules, réparations et pièces de rechange.
- **Missions** : création, affectation des chauffeurs et des véhicules, démarrage et clôture, suivi du kilométrage et des frais.
- **Points de livraison** : catégories, destinations, contacts et arrêts.
- **Clients** : gestion des clients, des devis, des commandes et des réclamations.
- **Notifications** en temps réel via WebSocket, et retours utilisateurs.
- **Tableau de bord** : indicateurs et statistiques de l'activité.

## Architecture

```
┌──────────────┐    HTTP / WebSocket    ┌──────────────────┐        ┌──────────────┐
│   Frontend   │ ─────────────────────▶ │   Backend API    │ ─────▶ │  PostgreSQL  │
│  Angular 16  │   (proxy nginx /api)   │  Spring Boot 3.2 │  JDBC  │              │
│  port 8088   │                        │    port 8080     │        │  port 5432   │
└──────────────┘                        └──────────────────┘        └──────────────┘
```

Le backend est organisé en couches : les `controllers` exposent l'API, les `services` contiennent la logique métier et les `dao` (Spring Data JPA) accèdent à la base. Des DTO et des mappers séparent le contrat de l'API du modèle de données.

## Stack technique

| Couche | Technologies |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA, jjwt, WebSocket, springdoc-openapi |
| Base de données | PostgreSQL |
| Frontend | Angular 16, Bootstrap 5, ng-bootstrap, Chart.js, FullCalendar, SockJS et STOMP |
| Client API | Généré à partir de la spécification OpenAPI avec `ng-openapi-gen` |
| Conteneurs | Docker, Docker Compose, nginx |
| CI/CD | GitHub Actions : compilation, tests, construction des images, publication sur Docker Hub et déploiement SSH |

## Prérequis

Pour lancer l'application avec Docker, il suffit d'avoir **Docker** et **Docker Compose**.

Pour le développement local, il faut :

- JDK 17
- Maven, ou le wrapper `mvnw` fourni dans le projet
- Node.js 18 ou supérieur, avec npm
- PostgreSQL 15 ou supérieur

## Démarrage rapide avec Docker

```bash
git clone https://github.com/[utilisateur]/delivery-system.git
cd delivery-system
docker compose up -d
```

Une fois les conteneurs démarrés, les services sont accessibles aux adresses suivantes :

| Service | Adresse |
|---|---|
| Application web | http://localhost:8088 |
| API | http://localhost:8080/api/v1 |
| Documentation Swagger | http://localhost:8080/api/v1/swagger-ui/index.html |

Pour arrêter l'application :

```bash
docker compose down
```

> **Avant toute mise en production**, remplace les mots de passe et les secrets présents dans `docker-compose.yml` par des valeurs propres à ton environnement (voir [Configuration](#configuration)).

## Développement local

### 1. Base de données

Le plus simple est de démarrer uniquement PostgreSQL avec Docker :

```bash
docker compose up -d postgres
```

Tu peux aussi utiliser une instance PostgreSQL installée localement, à condition de créer la base `delivery_db` et de renseigner les identifiants correspondants dans la configuration.

### 2. Backend

```bash
cd delivery-system-api
./mvnw spring-boot:run
```

Sous Windows, utilise `mvnw.cmd spring-boot:run`.

Le profil `dev` est actif par défaut. L'API écoute sur le port `8080` et son chemin de base est `/api/v1`.

### 3. Frontend

```bash
cd delivery-dashboard-ui
npm install
npx ng serve
```

L'interface est disponible sur http://localhost:4200. Le backend autorise cette origine par défaut.

### Régénérer le client API

Lorsque les endpoints de l'API changent, il faut régénérer le client Angular à partir de la spécification OpenAPI exportée par le backend :

```bash
cd delivery-dashboard-ui
npx ng-openapi-gen -i ./tools/openapi/src/openapi.json -o src/ds-api/src --ignoreUnusedModels=false
```

## Configuration

### Variables d'environnement

Le backend lit ses paramètres sensibles depuis des variables d'environnement. Le tableau suivant liste les variables prises en charge.

| Variable | Rôle | Exemple |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Profil Spring actif (`dev` ou `prod`) | `prod` |
| `DB_URL` | URL JDBC de la base PostgreSQL | `jdbc:postgresql://postgres-sql-ds:5432/delivery_db` |
| `DB_USERNAME` | Utilisateur de la base | `postgres` |
| `DB_PASSWORD` | Mot de passe de la base | *(secret)* |
| `JWT_SECRET` | Clé de signature des JWT, encodée en Base64, d'au moins 256 bits | *(secret)* |
| `EMAIL_HOSTNAME` | Serveur SMTP | `smtp.example.com` |
| `EMAIL_USERNAME` | Identifiant SMTP | `no-reply@example.com` |
| `EMAIL_PASSWORD` | Mot de passe SMTP | *(secret)* |
| `CORS_ORIGINS` | Origines autorisées à appeler l'API | `https://app.example.com` |

Ne versionne jamais de secrets dans le dépôt. En développement, place-les dans un fichier `.env` local, qui est ignoré par Git. En CI, utilise les secrets GitHub Actions. Un modèle `.env.example` est fourni pour référence.

### Profils

- **`dev`** : journalisation détaillée, affichage des requêtes SQL et mise à jour automatique du schéma. Ce profil ne doit pas être utilisé en production.
- **`prod`** : journalisation réduite et documentation Swagger désactivée.

## Documentation de l'API

La spécification OpenAPI est générée automatiquement par springdoc-openapi :

- Interface Swagger UI : `/api/v1/swagger-ui/index.html`
- Spécification JSON : `/api/v1/v3/api-docs`

Pour appeler les endpoints protégés, obtiens un token auprès de `POST /api/v1/auth/signin`, puis transmets-le dans l'en-tête HTTP :

```
Authorization: Bearer <votre_token>
```

## Tests

| Périmètre | Commande |
|---|---|
| Backend (unitaires et intégration) | `cd delivery-system-api && ./mvnw test` |
| Frontend (Karma et Jasmine) | `cd delivery-dashboard-ui && npx ng test` |

La CI exécute ces tests à chaque push.

## Déploiement et CI/CD

Deux pipelines GitHub Actions sont définis dans `.github/workflows/` :

- **`pipeline-backend.yml`** : compilation, tests unitaires, packaging Maven, construction et publication de l'image `ds-api` sur Docker Hub, puis déploiement sur le serveur via SSH.
- **`pipeline-frontend.yml`** : construction et publication de l'image `ds-ui` sur Docker Hub, puis déploiement sur le serveur via SSH.

Les secrets suivants doivent être configurés dans *Settings → Secrets and variables → Actions* :

- `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`
- `SSH_PRIVATE_KEY`, `VPS_USERNAME`, `VPS_IP`
- `EMAIL_HOSTNAME`, `EMAIL_USERNAME`, `EMAIL_PASSWORD`

Sur le serveur, le déploiement exécute `docker compose pull` puis `docker compose up -d` à partir du `docker-compose.yml` copié pendant la CI.

## Structure du projet

```
delivery-system/
├── .github/workflows/          # Pipelines CI/CD
├── docker/
│   ├── backend/Dockerfile      # Image de l'API
│   └── frontend/Dockerfile     # Image de l'interface
├── delivery-system-api/        # Backend Spring Boot
│   └── src/main/java/.../
│       ├── controllers/        # Points d'entrée REST
│       ├── services/           # Logique métier
│       ├── dao/                # Repositories Spring Data
│       ├── model/              # Entités JPA
│       ├── dto/                # Objets de transfert
│       ├── mapper/             # Conversion entités / DTO
│       └── config/             # Sécurité, CORS, WebSocket, OpenAPI
├── delivery-dashboard-ui/      # Frontend Angular
│   └── src/app/
│       ├── modules/            # Fonctionnalités (missions, chauffeurs…)
│       ├── services/           # Services, guards, intercepteurs
│       └── components/         # Composants partagés
├── docker-compose.yml          # Orchestration locale et production
└── README.md
```

## Feuille de route

- [ ] Autorisations par rôle sur l'ensemble des endpoints
- [ ] Couverture de tests unitaires et d'intégration
- [ ] Migrations de base de données avec Flyway
- [ ] Pagination systématique des listes
- [ ] Rotation des refresh tokens et révocation des sessions
- [ ] Internationalisation (français et anglais)
- [ ] Mise à jour vers les dernières versions de Spring Boot et d'Angular

## Contribuer

1. Fais un fork du dépôt.
2. Crée une branche : `git checkout -b feat/ma-fonctionnalite`.
3. Rédige des messages de commit clairs, de préférence au format [Conventional Commits](https://www.conventionalcommits.org/fr/) (`feat:`, `fix:`, `docs:`…).
4. Vérifie que les tests passent.
5. Ouvre une Pull Request qui décrit le changement et son contexte.

## Licence

Ce projet est distribué sous licence [MIT](LICENSE).

## Auteur

[Ahmed](https://github.com/Ahmeding), [ton e-mail ou ton profil LinkedIn]
