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