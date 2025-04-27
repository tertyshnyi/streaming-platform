provider "azurerm" {
  features {}

  subscription_id = var.subscription_id
  tenant_id       = var.tenant_id
}

# -----------------------------------------------------------------------------

resource "azurerm_container_registry" "fsa_tertyshnyi" {
  name                   = "${var.resource_name}${var.registry_name}"
  resource_group_name    = var.resource_group
  location               = var.location
  sku                    = "Standard"
  admin_enabled          = true
  anonymous_pull_enabled = "false"


  tags = {
    environment = var.environment
  }
}

resource "azurerm_container_registry_scope_map" "fsa_tertyshnyi_sm" {
  name                    = "${var.resource_name}-scope-map"
  container_registry_name = azurerm_container_registry.fsa_tertyshnyi.name
  resource_group_name     = var.resource_group
  actions = [
    "repositories/fsa/content/read",
    "repositories/fsa/content/write"
  ]
}

resource "azurerm_container_registry_token" "fsa_tertyshnyi_acr_token" {
  name                    = "${var.resource_name}registrytoken"
  container_registry_name = azurerm_container_registry.fsa_tertyshnyi.name
  resource_group_name     = var.resource_group
  scope_map_id            = azurerm_container_registry_scope_map.fsa_tertyshnyi_sm.id
}