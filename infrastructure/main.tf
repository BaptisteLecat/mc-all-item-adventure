resource "google_compute_instance" "mc_server" {
  name         = "mc-server"
  machine_type = var.machine_type
  zone         = var.zone
  tags         = ["minecraft-server"]

  boot_disk {
    initialize_params {
      image = "cos-cloud/cos-stable"
      size  = 10
    }
  }

  network_interface {
    network    = "default"
    subnetwork = "default"

    access_config {
      // Permet d'avoir une IP publique
    }
  }

  metadata = {
    google-logging-enabled    = "true"
    google-monitoring-enabled = "true"
    startup-script            = <<EOT
#!/bin/bash
set -x  # Debug mode

# Attendre que /dev/sdb soit bien attaché
while [ ! -e /dev/sdb ]; do sleep 1; done

# Vérifie si le disque est déjà formaté
if ! blkid /dev/sdb; then
  sudo mkfs.ext4 -F /dev/sdb
fi

# Monter le disque
sudo mkdir -p /mnt/disks/data
sudo mount /dev/sdb /mnt/disks/data
sudo chmod 777 /mnt/disks/data
echo "/dev/sdb /mnt/disks/data ext4 defaults 0 0" | sudo tee -a /etc/fstab
EOT

    gce-container-declaration = <<EOT
spec:
  containers:
  - name: mc-container
    image: europe-west1-docker.pkg.dev/weatherapp-journey/mc-all-item-adventure/mc-all-item-adventure:latest
    env:
    - name: EULA
      value: 'TRUE'
    - name: WHITELIST
      value: |
        38efa6ca-1964-4c6d-8861-a3426a81f1a0
    - name: OPS
      value: |
        38efa6ca-1964-4c6d-8861-a3426a81f1a0
    - name: ALLOW_NETHER
      value: 'TRUE'
    - name: ANNOUNCE_PLAYER_ACHIEVEMENTS
      value: 'TRUE'
    - name: VIEW_DISTANCE
        value: '20'
    - name: DIFFICULTY
        value: 'hard'
    volumeMounts:
    - name: host-path-0
      readOnly: false
      mountPath: /data
    stdin: true
    tty: true
  volumes:
  - name: host-path-0
    hostPath:
      path: /mnt/disks/data
  restartPolicy: Always
EOT
  }

  service_account {
    email  = "384868196694-compute@developer.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }

  attached_disk {
    device_name = "persistent-disk-1"
    mode        = "READ_WRITE"
    source      = google_compute_disk.mc_data_disk.self_link
  }

  scheduling {
    automatic_restart   = true
    on_host_maintenance = "MIGRATE"
  }
}

resource "google_compute_disk" "mc_data_disk" {
  name = "mc-data-disk"
  type = "pd-standard"
  zone = var.zone
  size = var.disk_size
}

resource "google_compute_attached_disk" "attach_disk" {
  disk     = google_compute_disk.mc_data_disk.id
  instance = google_compute_instance.mc_server.id
}

resource "google_compute_firewall" "mc_server_firewall" {
  name          = "mc-server-firewall"
  network       = "default"
  target_tags   = ["minecraft-server"]
  source_ranges = ["0.0.0.0/0"]
  allow {
    protocol = "tcp"
    ports    = ["25565"]
  }
}

resource "google_artifact_registry_repository" "mc_all_item_adventure_repository" {
  location      = var.region
  repository_id = var.artifact_registry_repository
  format        = "DOCKER"
}

resource "google_service_account" "ci_service_account" {
  account_id   = var.service_account
  display_name = "Github CI Service Account"
  description  = "Service account used by Github Actions to deploy the application"
}

resource "google_project_iam_member" "artifact_registry_writer" {
  project = var.project
  role    = "roles/artifactregistry.writer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "compute_instance_admin" {
  project = var.project
  role    = "roles/compute.instanceAdmin.v1"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

# role service account token creator
resource "google_project_iam_member" "service_account_token_creator" {
  project = var.project
  role    = "roles/iam.serviceAccountTokenCreator"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "service_account_user" {
  project = var.project
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "logging_viewer" {
  project = var.project
  role    = "roles/logging.viewer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "monitoring_viewer" {
  project = var.project
  role    = "roles/monitoring.viewer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

#can deploy cloud functions
resource "google_project_iam_member" "cloud_functions_developer" {
  project = var.project
  role    = "roles/cloudfunctions.developer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "firebase_admin" {
  project = var.project
  role    = "roles/firebase.admin"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_service_account_key" "ci_service_account_key" {
  service_account_id = google_service_account.ci_service_account.id
  public_key_type    = "TYPE_X509_PEM_FILE"
}

resource "github_actions_environment_secret" "ci_service_account_json" {
  repository      = var.github_repository # ex: "mc-all-item-adventure"
  environment     = "prod"                # l'environnement ciblé
  secret_name     = "SERVICE_ACCOUNT_KEY"
  plaintext_value = base64decode(google_service_account_key.ci_service_account_key.private_key)
}

resource "google_artifact_registry_repository" "plugin_firebase_api_repository" {
  location      = var.region
  repository_id = "plugin-firebase-api"
  format        = "DOCKER"
}

resource "google_service_account" "cloud_run_plugin_firebase_api_service_account" {
  account_id   = "cloud-run-plugin-firebase-api"
  display_name = "Cloud Run Plugin Firebase API Service Account"
  description  = "Service account used by the Cloud Run Plugin Firebase API"
}

resource "google_project_iam_member" "plugin_firebase_api_cloud_run_service_agent" {
  project = var.project
  role    = "roles/run.serviceAgent"
  member  = "serviceAccount:${google_service_account.cloud_run_plugin_firebase_api_service_account.email}"
}

resource "google_project_iam_member" "plugin_firebase_api_service_account_user" {
  project = var.project
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${google_service_account.cloud_run_plugin_firebase_api_service_account.email}"
}

#act as a service account
resource "google_project_iam_member" "plugin_firebase_api_service_account_token_creator" {
  project = var.project
  role    = "roles/iam.serviceAccountTokenCreator"
  member  = "serviceAccount:${google_service_account.cloud_run_plugin_firebase_api_service_account.email}"
}

resource "google_project_iam_member" "plugin_firebase_api_firebase_develop_admin" {
  project = var.project
  role    = "roles/firebase.developAdmin"
  member  = "serviceAccount:${google_service_account.cloud_run_plugin_firebase_api_service_account.email}"
}

resource "github_actions_environment_secret" "cloud_run_plugin_firebase_api_service_account_json" {
  repository      = var.github_repository # ex: "mc-all-item-adventure"
  environment     = "prod"                # l'environnement ciblé
  secret_name     = "CLOUD_RUN_EXECUTOR_SERVICE_ACCOUNT_NAME"
  plaintext_value = google_service_account.cloud_run_plugin_firebase_api_service_account.email
}

# Enable the Cloud Billing API
resource "google_project_service" "cloud_billing" {
  project            = var.project
  service            = "cloudbilling.googleapis.com"
  disable_on_destroy = false
}

# Active l'API Firebase Management (souvent nécessaire avant d'utiliser google_firebase_project)
resource "google_project_service" "firebase_management" {
  project = var.project
  service = "firebase.googleapis.com"
}

# Active l'API Firestore (pour Cloud Firestore)
resource "google_project_service" "firestore" {
  project = var.project
  service = "firestore.googleapis.com"
}

# Active Firebase sur le projet existant
resource "google_firebase_project" "default" {
  provider = "google-beta"
  project  = var.project

  depends_on = [
    google_project_service.firebase_management,
    google_project_service.firestore,
  ]
}

#Créer une base de données Firestore
resource "google_firestore_database" "default" {
  provider    = "google-beta"
  project     = var.project
  location_id = var.region
  name        = "default"
  type        = "FIRESTORE_NATIVE"
  depends_on = [
    google_firebase_project.default,
  ]
}


