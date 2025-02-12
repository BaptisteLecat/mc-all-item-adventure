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
  name        = "(default)"
  type        = "FIRESTORE_NATIVE"
  depends_on = [
    google_firebase_project.default,
  ]
}