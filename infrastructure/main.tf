module "minecraft_server" {
  source       = "./modules/minecraft_server"
  machine_type = var.machine_type
  zone         = var.zone
  disk_size    = var.disk_size
}

module "artifact_registry" {
  source                                           = "./modules/artifact_registry"
  region                                           = var.region
  minecraft_server_artifact_registry_repository    = "mc-all-item-adventure"
  plugin_firebase_api_artifact_registry_repository = "plugin-firebase-api"
}

module "iam" {
  source                                   = "./modules/iam"
  project                                  = var.project
  ci_service_account_name                  = "ghaction-mc-all-item-adventure"
  plugin_firebase_api_service_account_name = "plugin-firebase-api"
}

module "firebase" {
  source  = "./modules/firebase"
  project = var.project
  region  = var.region

}

module "github_actions" {
  source                                              = "./modules/github_actions"
  github_owner                                        = var.github_owner
  github_token                                        = var.github_token
  github_repository                                   = var.github_repository
  github_env                                          = "prod"
  ci_service_account_key                              = module.iam.ci_service_account_key
  cloud_run_plugin_firebase_api_service_account_email = module.iam.plugin_firebase_api_service_account_email
}

# Enable the Cloud Billing API
resource "google_project_service" "cloud_billing" {
  project            = var.project
  service            = "cloudbilling.googleapis.com"
  disable_on_destroy = false
}