resource "google_artifact_registry_repository" "mc_all_item_adventure_repository" {
  location      = var.region
  repository_id = var.minecraft_server_artifact_registry_repository
  format        = "DOCKER"
}

resource "google_artifact_registry_repository" "plugin_firebase_api_repository" {
  location      = var.region
  repository_id = var.plugin_firebase_api_artifact_registry_repository
  format        = "DOCKER"
}