provider "google" {
  #Credentials from the env variable GOOGLE_CREDENTIALS
  project = var.project
  region  = var.region
  zone    = var.zone
}

provider "github" {
  token = var.github_token # Un token personnel GitHub avec les droits sur le dépôt
  owner = var.github_owner # Par exemple, "weatherapp-journey"
}