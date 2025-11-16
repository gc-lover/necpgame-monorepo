param(
  [string]$BaseUrl = "http://localhost:8090",
  [string]$Role = "concept-director",
  [Parameter(Mandatory = $true)][string]$ItemId
)

$ErrorActionPreference = "Stop"
$headers = @{ "X-Agent-Role" = $Role }
$resp = Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/agents/tasks/items/$ItemId" -Headers $headers -Method Get
Write-Output ("Status: " + [int]$resp.StatusCode)
Write-Output $resp.Content



