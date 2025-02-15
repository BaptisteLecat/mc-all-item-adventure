terraform {
  backend "gcs" {
    bucket = "mc-all-item-adventure-terraform-state-bucket"
    prefix = "terraform/state"
  }
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.51.0"
    }
  }
}