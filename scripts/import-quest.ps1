# Issue: #50
# PowerShell script to import quest YAML to database via API

param(
    [string]$QuestFile = "",
    [string]$ApiUrl = "http://localhost:8083/api/v1/gameplay/quests/content/reload",
    [string]$AuthToken = ""
)

if ([string]::IsNullOrEmpty($QuestFile)) {
    Write-Host "Usage: .\import-quest.ps1 -QuestFile <path> [-ApiUrl <url>] [-AuthToken <token>]"
    Write-Host "Example: .\import-quest.ps1 -QuestFile knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029/quest-001-strip.yaml"
    exit 1
}

if (-not (Test-Path $QuestFile)) {
    Write-Host "Error: Quest file not found: $QuestFile" -ForegroundColor Red
    exit 1
}

# Check if Python is available
try {
    $pythonVersion = python --version 2>&1
    Write-Host "Using Python: $pythonVersion"
} catch {
    Write-Host "Error: Python is not installed or not in PATH" -ForegroundColor Red
    exit 1
}

# Convert YAML to JSON using Python
Write-Host "Reading quest file: $QuestFile"
$env:PYTHONIOENCODING = "utf-8"
$jsonContent = python -c @"
import yaml
import json
import sys
from datetime import datetime, date

def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""
    if isinstance(obj, datetime):
        return obj.isoformat()
    if isinstance(obj, date):
        return obj.isoformat()
    raise TypeError(f"Type {type(obj)} not serializable")

with open(r'$QuestFile', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)
    result = json.dumps(data, ensure_ascii=False, default=json_serial)
    sys.stdout.buffer.write(result.encode('utf-8'))
"@

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Failed to parse YAML file" -ForegroundColor Red
    exit 1
}

# Extract quest_id from metadata
$questId = python -c @"
import yaml
import sys

with open(r'$QuestFile', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)
    result = data['metadata']['id']
    sys.stdout.buffer.write(result.encode('utf-8'))
"@

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Failed to extract quest_id from metadata" -ForegroundColor Red
    exit 1
}

# Create request payload
$yamlContentObj = $jsonContent | ConvertFrom-Json
$payload = @{
    quest_id = $questId
    yaml_content = $yamlContentObj
} | ConvertTo-Json -Depth 100 -Compress

# Send request
Write-Host "Importing quest: $questId" -ForegroundColor Green
Write-Host "API URL: $ApiUrl"

$headers = @{
    "Content-Type" = "application/json"
}

if (-not [string]::IsNullOrEmpty($AuthToken)) {
    $headers["Authorization"] = "Bearer $AuthToken"
}

try {
    $response = Invoke-RestMethod -Uri $ApiUrl -Method Post -Body $payload -Headers $headers -ContentType "application/json"
    Write-Host "OK Quest imported successfully!" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "❌ Failed to import quest" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response: $responseBody"
    }
    exit 1
}

