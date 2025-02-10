terraform {
  gcs = {
    bucket = var.terraform_bucket_name
  }
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.51.0"
    }
  }
}