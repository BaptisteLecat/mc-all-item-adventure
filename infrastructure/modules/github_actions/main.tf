resource "github_actions_environment_secret" "ci_service_account_json" {
  repository      = var.github_repository # ex: "mc-all-item-adventure"
  environment     = "prod"                # l'environnement ciblé
  secret_name     = "SERVICE_ACCOUNT_KEY"
  plaintext_value = base64decode(var.ci_service_account_key)
}

resource "github_actions_environment_secret" "cloud_run_plugin_firebase_api_service_account_json" {
  repository      = var.github_repository # ex: "mc-all-item-adventure"
  environment     = var.github_env
  secret_name     = "CLOUD_RUN_EXECUTOR_SERVICE_ACCOUNT_NAME"
  plaintext_value = var.cloud_run_plugin_firebase_api_service_account_email
}