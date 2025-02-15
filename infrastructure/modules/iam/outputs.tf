output "plugin_firebase_api_service_account_email" {
  description = "Email du service account pour Cloud Run du plugin Firebase API"
  value       = google_service_account.plugin_firebase_api_service_account.email
}

output "ci_service_account_email" {
  description = "Email du service account pour CI"
  value       = google_service_account.ci_service_account.email
}

output "ci_service_account_key" {
  description = "Clé du service account pour CI"
  value       = google_service_account_key.ci_service_account_key.private_key
}