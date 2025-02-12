variable "zone" {
  description = "The zone in which the resources will be provisioned."
  type        = string
  default     = "us-central1-a"
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