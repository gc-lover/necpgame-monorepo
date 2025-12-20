# Import Brexit Legacy Quest Definition to Database
# Uses simplified quest_definition format for database import

$QuestYamlPath = "C:\NECPGAME\knowledge\canon\narrative\quests\brexit-legacy-quest-definition.yaml"
$ApiUrl = "http://localhost:8083"
$QuestId = "canon-narrative-quests-brexit-legacy-london"

Write-Host "🚀 Starting Brexit Legacy quest definition import" -ForegroundColor Green
Write-Host "📁 YAML path: $QuestYamlPath" -ForegroundColor Cyan
Write-Host "🌐 API URL: $ApiUrl" -ForegroundColor Cyan
Write-Host "🎯 Quest ID: $QuestId" -ForegroundColor Cyan
Write-Host ""

# Check if YAML file exists
if (!(Test-Path $QuestYamlPath)) {
    Write-Error "❌ Quest YAML file not found: $QuestYamlPath"
    exit 1
}

# Read YAML content
try {
    $yamlContent = Get-Content $QuestYamlPath -Raw -Encoding UTF8
    Write-Host "OK YAML file loaded successfully" -ForegroundColor Green
}
catch {
    Write-Error "❌ Failed to read YAML file: $_"
    exit 1
}

# Prepare API request body
$requestBody = @{
    quest_id = $QuestId
    yaml_content = $yamlContent
} | ConvertTo-Json

Write-Host "📤 Sending import request to $ApiUrl/api/v1/gameplay/quests/content/reload" -ForegroundColor Yellow

# Send API request
try {
    $response = Invoke-RestMethod -Method POST -Uri "$ApiUrl/api/v1/gameplay/quests/content/reload" -Body $requestBody -ContentType "application/json" -TimeoutSec 30

    Write-Host "OK Quest definition import successful!" -ForegroundColor Green
    Write-Host "📋 Response:" -ForegroundColor Cyan
    Write-Host ($response | ConvertTo-Json -Depth 3) -ForegroundColor White

}
catch {
    Write-Error "❌ Quest definition import failed: $_"
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
Write-Host "🎉 Quest definition '$QuestId' imported successfully!" -ForegroundColor Green
Write-Host "🔍 You can verify the import by checking the database or API responses." -ForegroundColor Cyan
