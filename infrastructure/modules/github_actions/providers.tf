provider "github" {
  token = var.github_token # Un token personnel GitHub avec les droits sur le dépôt
  owner = var.github_owner # Par exemple, "weatherapp-journey"
}