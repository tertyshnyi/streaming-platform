# -----------------------------------------------------------------------------
# Azure auth - service principal - autentifikacia do azure
# -----------------------------------------------------------------------------

variable "subscription_id" {
  description = "Subscription ID"
  type        = string
}

variable "tenant_id" {
  description = "Tenant ID"
  type        = string
}

# -----------------------------------------------------------------------------
# Globalne nastavenia projektu
# -----------------------------------------------------------------------------

variable "location" {
  description = "Pouzije sa na vytvorenie resource_group, ostatne resouces dedia tieto nastavenia."
  default     = "northeurope"
}

variable "resource_group" {
  description = "Nazov resource groupy"
  default     = "fsa-tertyshnyi"
}

variable "environment" {
  description = "Identifikator prostredia."
  default     = "test"
}

variable "dns_prefix" {
  description = "DNS prefix pre AKS"
  default     = "fsa-tertyshnyi"
}

variable "resource_name" {
  description = "Nazov resource"
  default     = "fsa-tertyshnyi"
}

variable "nodepool_name" {
  description = "Nazov node"
}

variable "node_count" {
  description = "Pocet nodov"
  default     = 1
}

variable "vm_size" {
  description = "Velkost VM"
  default     = "Standard_B2s"
}

variable "os_disk_size" {
  description = "Velkost OS disku"
}