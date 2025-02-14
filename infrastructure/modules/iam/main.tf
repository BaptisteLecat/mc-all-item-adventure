#####################################
# Github Actions CI Service Account #
#####################################

resource "google_service_account" "ci_service_account" {
  account_id   = var.ci_service_account_name
  display_name = "Github CI Service Account"
  description  = "Service account used by Github Actions to deploy the application"
}

resource "google_service_account_key" "ci_service_account_key" {
  service_account_id = google_service_account.ci_service_account.name
  public_key_type    = "TYPE_X509_PEM_FILE"
}

# Grant roles to the service account

resource "google_project_iam_member" "ci_artifact_registry_writer" {
  project = var.project
  role    = "roles/artifactregistry.writer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_compute_instance_admin" {
  project = var.project
  role    = "roles/compute.instanceAdmin.v1"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_service_account_token_creator" {
  project = var.project
  role    = "roles/iam.serviceAccountTokenCreator"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_service_account_user" {
  project = var.project
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_logging_viewer" {
  project = var.project
  role    = "roles/logging.viewer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_monitoring_viewer" {
  project = var.project
  role    = "roles/monitoring.viewer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_cloud_functions_developer" {
  project = var.project
  role    = "roles/cloudfunctions.developer"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

resource "google_project_iam_member" "ci_firebase_admin" {
  project = var.project
  role    = "roles/firebase.admin"
  member  = "serviceAccount:${google_service_account.ci_service_account.email}"
}

#################################################
# Cloud Run Plugin Firebase API Service Account #
#################################################

resource "google_service_account" "plugin_firebase_api_service_account" {
  account_id   = "cloud-run-plugin-firebase-api"
  display_name = "Cloud Run Plugin Firebase API Service Account"
  description  = "Service account used by the Cloud Run Plugin Firebase API"
}

# Grant roles to the service account

resource "google_project_iam_member" "plugin_firebase_api_cloud_run_service_agent" {
  project = var.project
  role    = "roles/run.serviceAgent"
  member  = "serviceAccount:${google_service_account.plugin_firebase_api_service_account.email}"
}

resource "google_project_iam_member" "plugin_firebase_api_service_account_user" {
  project = var.project
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${google_service_account.plugin_firebase_api_service_account.email}"
}

resource "google_project_iam_member" "plugin_firebase_api_service_account_token_creator" {
  project = var.project
  role    = "roles/iam.serviceAccountTokenCreator"
  member  = "serviceAccount:${google_service_account.plugin_firebase_api_service_account.email}"
}

resource "google_project_iam_member" "plugin_firebase_api_firebase_develop_admin" {
  project = var.project
  role    = "roles/firebase.developAdmin"
  member  = "serviceAccount:${google_service_account.plugin_firebase_api_service_account.email}"
}