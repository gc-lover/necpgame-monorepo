param(
  [string]$BaseUrl = "http://localhost:8090",
  [string]$Role = "concept-director"
)

$ErrorActionPreference = "Stop"

function Invoke-Claim {
  $body = '{"segments":["concept"],"priorityFloor":1}'
  $headers = @{ "X-Agent-Role" = $Role; "Content-Type" = "application/json" }
  try {
    $resp = Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/agents/tasks/claim" -Headers $headers -Method Post -Body $body
    return @{ code = [int]$resp.StatusCode; content = $resp.Content }
  } catch {
    if ($_.Exception.Response) {
      $s = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
      $c = $s.ReadToEnd()
      return @{ code = [int]$_.Exception.Response.StatusCode; content = $c }
    }
    throw
  }
}

Write-Output "== Claim =="
$claim = Invoke-Claim
Write-Output ("HTTP " + $claim.code)
Write-Output $claim.content

if ($claim.code -eq 204) {
  Write-Output "== Queue empty → Ingest =="
  & "$PSScriptRoot/ingest.ps1" -BaseUrl $BaseUrl -Role $Role -SourceId "CD-SMOKE-$(Get-Random)"
  Write-Output "== Claim retry =="
  $claim = Invoke-Claim
  Write-Output ("HTTP " + $claim.code)
  Write-Output $claim.content
}

if ($claim.code -eq 409) {
  try {
    $json = $claim.content | ConvertFrom-Json
    $itemId = $null
    if ($json.activeItemId) { $itemId = $json.activeItemId }
    if (-not $itemId -and $json.details -and $json.details.Count -gt 0) {
      $itemId = $json.details[0].reason
    }
    if ($itemId) {
      Write-Output "== Active item: $itemId → GET instructions =="
      & "$PSScriptRoot/get-item.ps1" -BaseUrl $BaseUrl -Role $Role -ItemId $itemId
      Write-Output "== Submit active =="
      & "$PSScriptRoot/submit.ps1" -BaseUrl $BaseUrl -Role $Role -ItemId $itemId
      exit 0
    }
  } catch { }
  Write-Output "Active item not found in response."
  exit 2
}

if ($claim.code -eq 200) {
  if ($claim.content -match '"id"\s*:\s*"([0-9a-fA-F-]+)"') {
    $itemId = $Matches[1]
    Write-Output "== Fetch instructions for item: $itemId =="
    $itemResp = Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/agents/tasks/items/$itemId" -Headers @{ "X-Agent-Role" = $Role } -Method Get
    Write-Output "== Submit claimed item: $itemId =="
    & "$PSScriptRoot/submit.ps1" -BaseUrl $BaseUrl -Role $Role -ItemId $itemId
    exit 0
  } else {
    Write-Output "Item id not found in claim 200 body."
    exit 3
  }
}

exit 0



