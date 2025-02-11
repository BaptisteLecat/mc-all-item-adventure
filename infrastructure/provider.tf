provider "google" {
  project = var.project
  region  = var.region
  zone    = var.zone
}

provider "github" {
  token = var.github_token # Un token personnel GitHub avec les droits sur le dépôt
  owner = var.github_owner # Par exemple, "weatherapp-journey"
}

provider "google-beta" {
  project = var.project
  region  = var.region
}
