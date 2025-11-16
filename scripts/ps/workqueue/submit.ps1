param(
  [string]$BaseUrl = "http://localhost:8090",
  [string]$Role = "concept-director",
  [Parameter(Mandatory = $true)][string]$ItemId,
  [string]$Notes = "Concept brief",
  [string]$ArtifactUrl = "https://example.com/brief.md",
  [string]$ArtifactTitle = "Concept Brief",
  [string]$NextSegment = "vision",
  [string[]]$KnowledgeRefs = @()
)

$ErrorActionPreference = "Stop"
$knowledgeRefsJson = ""
if ($KnowledgeRefs -and $KnowledgeRefs.Length -gt 0) {
  $escaped = $KnowledgeRefs | ForEach-Object { '\"' + ($_ -replace '"', '\"') + '\"' }
  $knowledgeRefsJson = ('"knowledgeRefs": [' + ($escaped -join ',') + '],')
}
$payloadJson = @"
{
  "notes":"$Notes",
  "artifacts":[{"title":"$ArtifactTitle","url":"$ArtifactUrl"}],
  "metadata":"{ $knowledgeRefsJson \"handoff\": { \"nextSegment\":\"$NextSegment\" } }"
}
"@

$tmp = New-TemporaryFile
Set-Content -Path $tmp -Value $payloadJson -Encoding utf8

$url = "$BaseUrl/api/agents/tasks/$ItemId/submit"
$resp = curl.exe -s -i -X POST $url -H "X-Agent-Role: $Role" -H "Content-Type: multipart/form-data" -F "payload=@$tmp;type=application/json"
Write-Output $resp
Remove-Item $tmp -Force



