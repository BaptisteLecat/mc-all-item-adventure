variable "github_token" {
  description = "The GitHub token to use for authenticating with the GitHub provider."
  type        = string
}

variable "github_owner" {
  description = "The owner of the GitHub repository to use for the GitHub provider."
  type        = string
}

variable "github_repository" {
  description = "The name of the GitHub repository to use for the GitHub provider."
  type        = string
}

variable "github_env" {
  description = "The environment to use for the GitHub provider."
  type        = string
}

variable "ci_service_account_key" {
  description = "The service account key to use for the GitHub provider."
  type        = string
}

variable "cloud_run_plugin_firebase_api_service_account_email" {
  description = "The email of the service account to use for the GitHub provider."
  type        = string
}