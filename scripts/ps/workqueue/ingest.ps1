param(
  [string]$BaseUrl = "http://localhost:8090",
  [string]$Role = "concept-director",
  [string]$SourceId = "CD-SMOKE",
  [string]$Title = "Concept brief",
  [string]$Summary = "Initial concept intake",
  [string]$Topic = "workqueue"
)

$ErrorActionPreference = "Stop"
$headers = @{ "X-Agent-Role" = $Role; "Content-Type" = "application/json" }
$payload = @"
{
  "sourceId":"$SourceId",
  "segment":"concept",
  "initialStatus":"queued",
  "priority":3,
  "title":"$Title",
  "summary":"$Summary",
  "knowledgeRefs":["agent-brief:concept"],
  "templates":{"primary":[],"checklists":[],"references":[]},
  "payload":{"topic":"$Topic"},
  "handoffPlan":{"nextSegment":"vision","conditions":[{"status":"completed","targetSegment":"vision"}],"notes":"brief"}
}
"@
$resp = Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/ingest/tasks" -Headers $headers -Method Post -Body $payload
Write-Output ("Status: " + [int]$resp.StatusCode)
Write-Output $resp.Content



