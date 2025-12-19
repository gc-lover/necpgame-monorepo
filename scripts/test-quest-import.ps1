# Test script for quest import functionality
# Issue: #140904681

param(
    [string]$QuestYamlPath = "knowledge/canon/lore/_03-lore/timeline-author/quests/europe/paris/2020-2029/quest-007-catacombs-exploration.yaml",
    [string]$ServiceUrl = "http://localhost:8083/api/v1"
)

Write-Host "Testing quest import functionality..." -ForegroundColor Green
Write-Host "Quest YAML: $QuestYamlPath" -ForegroundColor Yellow
Write-Host "Service URL: $ServiceUrl" -ForegroundColor Yellow

# Check if YAML file exists
if (!(Test-Path $QuestYamlPath)) {
    Write-Host "ERROR: Quest YAML file not found: $QuestYamlPath" -ForegroundColor Red
    exit 1
}

# Read YAML content as raw text
try {
    $yamlContent = Get-Content $QuestYamlPath -Raw -Encoding UTF8
    Write-Host "Successfully read YAML file ($($yamlContent.Length) characters)" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to read YAML file: $_" -ForegroundColor Red
    exit 1
}

# Extract quest_id from YAML (simple parsing)
$questId = $null
if ($yamlContent -match "id:\s*([^\s]+)") {
    $questId = $matches[1]
}
if (!$questId) {
    Write-Host "ERROR: Could not extract quest_id from YAML" -ForegroundColor Red
    exit 1
}
if (!$questId) {
    Write-Host "ERROR: Could not extract quest_id from YAML metadata" -ForegroundColor Red
    exit 1
}

Write-Host "Quest ID: $questId" -ForegroundColor Cyan

# Create request payload (YAML as raw content)
$requestBody = @{
    quest_id = $questId
    yaml_content = $yamlContent
} | ConvertTo-Json

Write-Host "Request payload prepared ($($requestBody.Length) characters)" -ForegroundColor Green

# Make API request
$apiUrl = "$ServiceUrl/gameplay/quests/content/reload"
Write-Host "Making POST request to: $apiUrl" -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri $apiUrl -Method Post -Body $requestBody -ContentType "application/json"
    Write-Host "SUCCESS: Quest imported successfully!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: API request failed: $_" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Red
    }
    exit 1
}

Write-Host "Test completed successfully!" -ForegroundColor Green