variable "project" {
  description = "The ID of the project in which the resources will be provisioned."
  type        = string
}

variable "ci_service_account_name" {
  description = "The service account name for the CI service account."
  type        = string
  default     = "ci-service-account"
}

variable "plugin_firebase_api_service_account_name" {
  description = "The service account name for the Cloud Run Plugin Firebase API service account."
  type        = string
  default     = "plugin-firebase-api"
}