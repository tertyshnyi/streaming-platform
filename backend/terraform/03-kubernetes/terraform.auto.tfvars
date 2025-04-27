# -----------------------------------------------------------------------------
# Secrets
# -----------------------------------------------------------------------------

# Azure account subscription ID (Required)
subscription_id = "e49f7ece-e4fa-4c21-b725-f084e6779e16"
tenant_id = "3590242b-a92d-4bb9-9ff9-eb7a1183f511"

# -----------------------------------------------------------------------------
# Global
# -----------------------------------------------------------------------------

location = "northeurope"

environment = "lab"

resource_group = "fsa-tertyshnyi"

# -----------------------------------------------------------------------------
# Resource Specific Variables
# -----------------------------------------------------------------------------

resource_name = "fsa-tertyshnyi"

dns_prefix = "fsa-tertyshnyi"

nodepool_name = "app"

node_count = "1"

vm_size = "Standard_B2s"

os_disk_size = "30"