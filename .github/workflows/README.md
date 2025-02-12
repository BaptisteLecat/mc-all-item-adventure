# Documentation of the CI/CD pipelines

## CI/CD pipelines

- Build & Deploy Minecraft Server with Plugins (build-plugins.yml)
- Rollback Minecraft Server (rollback.yml)
- Deploy Firebase API (plugin-firebase-api-deploy.yml)
- Deploy Cloud Functions (functions-deploy.yml)
- Terraform CI/CD (terraform.yml)

---

### Build & Deploy Minecraft Server with Plugins (build-plugins.yml)

Ce pipeline CI/CD a pour objectif de construire et déployer un serveur Minecraft intégrant des plugins personnalisés. Il se décline en quatre grandes étapes :

1. **Construction des plugins (_build-gradle-jar_)**
    - **Checkout** du dépôt.
    - Configuration de l'environnement avec JDK 21 et Gradle.
    - Compilation des plugins présents dans le dossier `plugins/` (via `gradle shadowJar`).
    - Archivage des fichiers JAR générés pour qu'ils soient disponibles pour l'étape suivante.

2. **Création et déploiement de l'image Docker (_build-push-docker-image_)**
    - Téléchargement des JAR archivés.
    - Authentification auprès de Google Cloud (à l'aide du secret `SERVICE_ACCOUNT_KEY` et du `GCP_PROJECT_ID`).
    - Configuration du SDK Cloud et de Docker pour interagir avec le registre de conteneurs.
    - Construction d'une image Docker intégrant les plugins, avec un tag dynamique basé sur le numéro de run.
    - Poussée de l'image Docker dans le registre (avec un tag spécifique et `latest`).

3. **Mise à jour de l'instance de déploiement (_deploy-to-vm_)**
    - Mise à jour de l'instance Compute Engine (`mc-server`) pour pointer sur la nouvelle image Docker.
    - Nettoyage des anciennes images Docker sur la VM.
    - Exécution d'un check de santé sur le serveur Minecraft (vérification du port 25565) afin de s'assurer de son bon démarrage.

4. **Génération d'un rapport de déploiement (_generate-deployment-report_)**
    - Assemblage d'un résumé incluant la version déployée, les informations de build (ID, commit, branche) et l'état du serveur.
    - Publication de ce rapport en tant que résumé de l'exécution GitHub Actions.

#### Variables et Secrets

Le pipeline s'appuie sur plusieurs secrets et variables pour l'authentification et la configuration. Voici un tableau récapitulatif :

| Nom                   | Origine               | Type         | Exemple de valeur                           |
|-----------------------|-----------------------|--------------|---------------------------------------------|
| `SERVICE_ACCOUNT_KEY` | GitHub Secrets        | JSON String  | `{ "type": "service_account", ... }`        |
| `GCP_PROJECT_ID`      | GitHub Secrets        | String       | `my-gcp-project-id`                         |
| `GCP_REGION`          | Variables du dépôt    | String       | `europe-west1`                              |
| `GCP_ZONE`            | Variables du dépôt    | String       | `europe-west1-b`                            |

*Note : Les noms et valeurs réelles peuvent varier selon la configuration de votre projet.*

---

### Rollback Minecraft Server (rollback.yml)

Ce pipeline permet de revenir à une version antérieure du serveur Minecraft en déployant une image Docker spécifique.

#### Déclenchement

- **Manuel via `workflow_dispatch`**  
  Le pipeline est déclenché manuellement et attend un input :
    - `version` : le tag de l'image Docker à rollbacker (ex. : `0.0.10`).

#### Étapes principales

1. **Pré-vérification (_pre-check_)**
    - **Checkout** du dépôt.
    - Authentification auprès de Google Cloud avec `SERVICE_ACCOUNT_KEY` et `GCP_PROJECT_ID`.
    - Mise en place du Cloud SDK.
    - **Validation de l'existence** de l'image correspondant au tag spécifié dans l'Artifact Registry.  
      Si le tag n'existe pas, le pipeline s'arrête avec une erreur.

2. **Rollback**
    - Nouvelle **checkout** du dépôt et ré-authentification.
    - **Revalidation** de l'existence du tag pour éviter tout problème entre les étapes.
    - **Mise à jour de l'instance Compute Engine** (`mc-server`) pour pointer sur l'image Docker correspondant à la version choisie.
    - **Confirmation** du rollback en récupérant l'image déployée et en la comparant à celle attendue.
    - **Nettoyage** des anciennes images Docker sur la VM pour libérer de l'espace.
    - **Contrôle de santé** du serveur Minecraft en testant le port 25565, avec plusieurs essais si nécessaire.

3. **Rapport de rollback**
    - Génération d'un résumé de rollback incluant :
        - La version rollbackée et l'image Docker déployée.
        - Les informations du workflow (Run ID, commit, branche).
        - Le statut final du rollback (succès ou échec).
    - Publication du rapport dans le résumé de l'exécution GitHub Actions.

#### Variables, Secrets et Inputs

Le pipeline s'appuie sur plusieurs éléments pour la configuration et l'authentification :

| Nom                   | Origine                   | Type         | Exemple de valeur                           |
|-----------------------|---------------------------|--------------|---------------------------------------------|
| `SERVICE_ACCOUNT_KEY` | GitHub Secrets            | JSON String  | `{ "type": "service_account", ... }`        |
| `GCP_PROJECT_ID`      | GitHub Secrets            | String       | `my-gcp-project-id`                         |
| `GCP_REGION`          | Variables du dépôt        | String       | `europe-west1`                              |
| `GCP_ZONE`            | Variables du dépôt        | String       | `europe-west1-b`                            |
| `version`             | Input workflow_dispatch   | String       | `0.0.10`                                    |

*Note : Veillez à adapter ces valeurs en fonction de la configuration réelle de votre projet.*

---

### Deploy Firebase API (plugin-firebase-api-deploy.yml)

Ce pipeline a pour objectif de déployer l'API Firebase associée aux plugins en construisant une image Docker à partir d'une application Node.js.

#### Déclenchement

- **Sur push vers la branche `main`**  
  Le pipeline se déclenche uniquement lorsqu'il y a des modifications dans le répertoire `api/plugin-firebase-api/**`.

#### Étapes principales

1. **Initialisation et Configuration**
    - **Checkout** du dépôt.
    - Authentification auprès de Google Cloud à l'aide des secrets `SERVICE_ACCOUNT_KEY` et `GCP_PROJECT_ID`.
    - Mise en place du Cloud SDK et configuration de Docker pour communiquer avec le registre d'images.

2. **Installation et Build de l'Application**
    - Utilisation de Node.js (version 20.x) pour installer les dépendances avec `npm install` dans le dossier `api/plugin-firebase-api`.
    - Construction de l'application via la commande `npm run build`.

3. **Création et Déploiement de l'Image Docker**
    - **Détermination d'un tag d'image dynamique** basé sur le numéro d'exécution GitHub.
    - Construction de l'image Docker à partir du Dockerfile situé dans `api/plugin-firebase-api`.
    - Poussée de l'image dans le registre Artifact Registry.
    - Déploiement de l'image sur Google Cloud Run avec une configuration spécifique (nombre d'instances, mémoire, CPU, port, etc.) et utilisation d'un compte de service dédié (`CLOUD_RUN_EXECUTOR_SERVICE_ACCOUNT_NAME`).

#### Variables, Secrets et Configurations

| Nom                                        | Origine                   | Type         | Exemple de valeur                                                       |
|--------------------------------------------|---------------------------|--------------|-------------------------------------------------------------------------|
| `SERVICE_ACCOUNT_KEY`                      | GitHub Secrets            | JSON String  | `{ "type": "service_account", ... }`                                    |
| `GCP_PROJECT_ID`                           | GitHub Secrets            | String       | `my-gcp-project-id`                                                     |
| `GCP_REGION`                               | Variables du dépôt        | String       | `europe-west1`                                                          |
| `CLOUD_RUN_EXECUTOR_SERVICE_ACCOUNT_NAME`  | GitHub Secrets            | String       | `service-account@my-project.iam.gserviceaccount.com`                    |
| `IMAGE_TAG` (output)                       | Généré par le pipeline    | String       | `0.0.123` (basé sur le numéro d'exécution GitHub, ex: `0.0.10`)           |

*Note : Adaptez ces exemples de valeurs selon la configuration réelle de votre projet.*

---

### Deploy Cloud Functions (functions-deploy.yml)

Ce pipeline a pour objectif de déployer les Cloud Functions depuis le répertoire dédié, en utilisant la CLI Firebase pour la gestion des fonctions sur Google Cloud.

#### Déclenchement

- **Sur push vers la branche `main`**  
  Le pipeline se déclenche uniquement lorsqu'il y a des modifications dans le dossier `infrastructure/cloud_functions/functions/**`.

#### Étapes principales

1. **Initialisation et Installation**
    - **Checkout** du dépôt pour récupérer le code source.
    - Installation de la Firebase CLI avec `npm install -g firebase-tools` pour pouvoir déployer les fonctions.
    - Configuration de Node.js (version 18) via l'action `setup-node@v2`.
    - Installation des dépendances du projet situées dans `infrastructure/cloud_functions/functions` grâce à `npm install`.

2. **Configuration des paramètres**
    - Création d'un fichier `.env` dans le dossier des fonctions, dans lequel sont définies les variables spécifiques à Firebase (notamment `FN_MAX_INSTANCES` et `FN_REGION`).

3. **Authentification et Configuration Google Cloud**
    - Authentification auprès de Google Cloud via l'action `google-github-actions/auth@v2` en utilisant le secret `SERVICE_ACCOUNT_KEY` et le `GCP_PROJECT_ID`.
    - Configuration du Cloud SDK avec `google-github-actions/setup-gcloud@v1` pour préparer l'environnement de déploiement.

4. **Déploiement des Cloud Functions**
    - Exécution de la commande `npm run deploy` dans le dossier `infrastructure/cloud_functions/functions` afin de déployer les fonctions sur Google Cloud.

#### Variables, Secrets et Configurations

Le pipeline utilise plusieurs secrets et variables pour assurer la connexion à Google Cloud et pour configurer le déploiement :

| Nom                              | Origine                | Type         | Exemple de valeur                                          |
|----------------------------------|------------------------|--------------|------------------------------------------------------------|
| `SERVICE_ACCOUNT_KEY`            | GitHub Secrets         | JSON String  | `{ "type": "service_account", ... }`                       |
| `GCP_PROJECT_ID`                 | GitHub Secrets         | String       | `my-gcp-project-id`                                        |
| `GOOGLE_APPLICATION_CREDENTIALS` | GitHub Secrets         | JSON String  | (Identique à `SERVICE_ACCOUNT_KEY`)                        |
| `GCP_ZONE`                       | Variables du dépôt     | String       | `europe-west1-b`                                           |
| `FN_MAX_INSTANCES`               | Variables du dépôt     | String/Number| `5`                                                      |
| `FN_REGION`                      | Variables du dépôt     | String       | `europe-west1`                                             |
| `VM_INSTANCE_NAME`               | Variables du dépôt     | String       | `my-vm-instance`                                           |

*Note : Les valeurs indiquées sont des exemples et doivent être adaptées à votre environnement de déploiement.*

---

### Terraform CI/CD (terraform.yml)

Ce pipeline gère l'infrastructure du projet à l'aide de Terraform. Il se déclenche lors d'un push ou d'une pull request sur les modifications du répertoire `infrastructure/**`, à l'exception des changements dans `infrastructure/cloud_functions/**`.

#### Déclenchement

- **Push** sur la branche `main`
- **Pull Request** sur les modifications dans `infrastructure/**` (hors `infrastructure/cloud_functions/**`)

#### Étapes principales

1. **Checkout du dépôt**  
   Récupération du code source avec l'action `actions/checkout@v3`.

2. **Authentification et Configuration Google Cloud**
    - Authentification auprès de GCP via l'action `google-github-actions/auth@v2` en utilisant le secret `TERRAFORM_SERVICE_ACCOUNT_KEY` et `GCP_PROJECT_ID`.
    - Configuration du Cloud SDK avec `google-github-actions/setup-gcloud@v1`.

3. **Installation et Configuration de Terraform**
    - Installation de Terraform (version `1.10.5`) via l'action `hashicorp/setup-terraform@v2`.
    - Vérification du format des fichiers Terraform avec `terraform fmt -check -recursive`.

4. **Génération du Backend Terraform**
    - Création d'un fichier `backend.tfvars` dans le répertoire `infrastructure`, définissant le bucket de stockage et le préfixe pour l'état Terraform.

5. **Initialisation et Validation de Terraform**
    - Initialisation de Terraform avec `terraform init -backend-config=backend.tfvars`.
    - Validation de la configuration via `terraform validate`.

6. **Planification et Application**
    - Exécution de `terraform plan` pour générer un plan d'exécution sauvegardé dans un fichier (`tfplan`).
    - Application automatique du plan (`terraform apply -auto-approve tfplan`) sur la branche `main`.

#### Variables, Secrets et Configurations

| Nom                                        | Origine                | Type         | Exemple de valeur                           |
|--------------------------------------------|------------------------|--------------|---------------------------------------------|
| `GCP_PROJECT_ID`                           | GitHub Secrets         | String       | `my-gcp-project-id`                         |
| `TERRAFORM_SERVICE_ACCOUNT_KEY`            | GitHub Secrets         | JSON String  | `{ "type": "service_account", ... }`        |
| `TF_GCP_BUCKET_NAME`                       | Variables du dépôt     | String       | `my-terraform-bucket`                       |
| `GCP_REGION`                               | Variables du dépôt     | String       | `europe-west1`                              |
| `GCP_ZONE`                                 | Variables du dépôt     | String       | `europe-west1-b`                            |
| `TF_GCP_ARTIFACT_REGISTRY_REPOSITORY`       | Variables du dépôt     | String       | `my-artifact-repo`                          |
| `TF_GCP_MACHINE_TYPE`                      | Variables du dépôt     | String       | `n1-standard-1`                             |
| `TF_GCP_DISK_SIZE`                         | Variables du dépôt     | String/Number| `50`                                        |
| `TF_GCP_GH_SERVICE_ACCOUNT_NAME`           | Variables du dépôt     | String       | `terraform-service-account`                 |
| `TF_GITHUB_TOKEN`                          | GitHub Secrets         | String       | `ghp_...`                                   |

*Note : Les exemples de valeurs sont indicatifs et doivent être adaptés à votre environnement de déploiement.*

---