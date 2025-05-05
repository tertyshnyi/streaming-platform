terraform {
  required_version = "= 1.11.4"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "= 4.26.0"
    }
  }

  backend "azurerm" {
    resource_group_name  = "tertyshnyi-fsatfstate"
    storage_account_name = "tertyshnyifsatfstate"
    container_name       = "fsa-infrastructure"
    key                  = "psql/tfstate/terraform.tfstate"
  }
}
