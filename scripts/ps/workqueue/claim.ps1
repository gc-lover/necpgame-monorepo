param(
  [string]$BaseUrl = "http://localhost:8090",
  [string]$Role = "concept-director",
  [string]$Segments = "concept",
  [int]$PriorityFloor = 1
)

$ErrorActionPreference = "Stop"
$body = "{`"segments`":[`"$Segments`"],`"priorityFloor`":$PriorityFloor}"
$headers = @{ "X-Agent-Role" = $Role; "Content-Type" = "application/json" }
$resp = Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/agents/tasks/claim" -Headers $headers -Method Post -Body $body
Write-Output ("Status: " + [int]$resp.StatusCode)
Write-Output $resp.Content



