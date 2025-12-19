# Import Quest Paris Montmartre Artists to Database
# Usage: .\scripts\import-quest-paris-artists.ps1

param(
    [string]$QuestYamlPath = "knowledge/canon/lore/timeline-author/quests/europe/paris/2020-2029/quest-006-montmartre-artists.yaml",
    [string]$ApiUrl = "http://localhost:8080",  # gameplay-service port
    [string]$QuestId = "canon-quest-paris-2020-2029-montmartre-artists"
)

Write-Host "🚀 Starting quest import: $QuestId" -ForegroundColor Green
Write-Host "📁 YAML path: $QuestYamlPath" -ForegroundColor Cyan
Write-Host "🌐 API URL: $ApiUrl" -ForegroundColor Cyan
Write-Host ""

# Check if YAML file exists
if (!(Test-Path $QuestYamlPath)) {
    Write-Error "❌ Quest YAML file not found: $QuestYamlPath"
    exit 1
}

# Read and parse YAML content
try {
    $yamlContent = Get-Content $QuestYamlPath -Raw -Encoding UTF8
    Write-Host "OK YAML file loaded successfully" -ForegroundColor Green
}
catch {
    Write-Error "❌ Failed to read YAML file: $_"
    exit 1
}

# Convert YAML to JSON (simplified - using PowerShell's ConvertFrom-Yaml if available)
$jsonContent = $null
try {
    if (Get-Command ConvertFrom-Yaml -ErrorAction SilentlyContinue) {
        $yamlObject = ConvertFrom-Yaml $yamlContent
        $jsonContent = $yamlObject | ConvertTo-Json -Depth 10 -Compress
        Write-Host "OK YAML converted to JSON" -ForegroundColor Green
    }
    else {
        Write-Warning "WARNING  PowerShell YAML module not available, using raw YAML as JSON"
        $jsonContent = $yamlContent
    }
}
catch {
    Write-Warning "WARNING  YAML conversion failed, using raw YAML: $_"
    $jsonContent = $yamlContent
}

# Prepare API request
$requestBody = @{
    quest_id     = $QuestId
    yaml_content = @{
        content = $jsonContent
    }
} | ConvertTo-Json -Depth 10

Write-Host "📤 Sending import request to $ApiUrl/gameplay/quests/content/reload" -ForegroundColor Yellow

# Send API request
try {
    $response = Invoke-RestMethod -Method POST -Uri "$ApiUrl/gameplay/quests/content/reload" -Body $requestBody -ContentType "application/json" -TimeoutSec 30

    Write-Host "OK Quest import successful!" -ForegroundColor Green
    Write-Host "📋 Response:" -ForegroundColor Cyan
    Write-Host ($response | ConvertTo-Json -Depth 3) -ForegroundColor White

}
catch {
    Write-Error "❌ Quest import failed: $_"
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "📊 HTTP Status Code: $statusCode" -ForegroundColor Red

        try {
            $errorResponse = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorResponse)
            $errorContent = $reader.ReadToEnd()
            Write-Host "📋 Error Response:" -ForegroundColor Red
            Write-Host $errorContent -ForegroundColor Red
        }
        catch {
            Write-Host "📋 Could not read error response" -ForegroundColor Red
        }
    }
    exit 1
}

Write-Host ""
Write-Host "🎉 Quest '$QuestId' imported successfully!" -ForegroundColor Green
Write-Host "🔍 You can verify the import by checking the database or API responses." -ForegroundColor Cyan

# Issue: #616