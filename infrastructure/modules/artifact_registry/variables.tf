variable "minecraft_server_artifact_registry_repository" {
  description = "The name of the Artifact Registry repository to create."
  type        = string
  default     = "minecraft-server"
}

variable "plugin_firebase_api_artifact_registry_repository" {
  description = "The name of the Artifact Registry repository to create."
  type        = string
  default     = "plugin-firebase-api"
}

variable "region" {
  description = "The region in which the resources will be provisioned."
  type        = string
  default     = "us-central1"
}