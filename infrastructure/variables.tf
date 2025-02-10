variable "project" {
  description = "The ID of the project in which the resources will be provisioned."
  type        = string
}

variable "terraform_bucket_name" {
  description = "The name of the GCS bucket to use for storing Terraform state."
  type        = string
}

variable "region" {
  description = "The region in which the resources will be provisioned."
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "The zone in which the resources will be provisioned."
  type        = string
  default     = "us-central1-a"
}

variable "credentials_file" {
  description = "The path to the credentials file for the service account used to provision the resources."
  type        = string
  default     = "service.json"
}

variable "artifact_registry_repository" {
  description = "The name of the Artifact Registry repository to create."
  type        = string
  default     = "minecraft-server"
}

variable "machine_type" {
  description = "The machine type to use for the server."
  type        = string
  default     = "e2-medium"
}

variable "disk_size" {
  description = "The size of the disk to attach to the server."
  type        = number
  default     = 10
}

variable "service_account" {
  description = "The service account to use for provisioning resources."
  type        = string
  default     = "ci-service-account"
}

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