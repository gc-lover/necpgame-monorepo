# Generate handlers.go from api.ServerInterface for any service

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName
)

$svcPath = "services\$ServiceName"
$serverGenPath = "$svcPath\pkg\api\server.gen.go"

if (-not (Test-Path $serverGenPath)) {
    Write-Host "❌ server.gen.go not found for $ServiceName" -ForegroundColor Red
    exit 1
}

# Read ServerInterface methods
$content = Get-Content $serverGenPath -Raw
$interfaceMatch = [regex]::Match($content, 'type ServerInterface interface \{([^}]+)\}', 'Singleline')

if (-not $interfaceMatch.Success) {
    Write-Host "❌ Could not find ServerInterface" -ForegroundColor Red
    exit 1
}

$methods = [regex]::Matches($interfaceMatch.Groups[1].Value, '\/\/ ([^\n]+)\n\s+\/\/ \(([A-Z]+) ([^\)]+)\)\n\s+([A-Za-z]+)\(([^\)]+)\)')

$handlersCode = @"
// Handlers for $ServiceName - implements api.ServerInterface
package server

import (
    "encoding/json"
    "net/http"

    "github.com/necpgame/$ServiceName/pkg/api"
    openapi_types "github.com/oapi-codegen/runtime/types"
    "github.com/sirupsen/logrus"
)

// ServiceHandlers implements api.ServerInterface
type ServiceHandlers struct {
    logger *logrus.Logger
}

// NewServiceHandlers creates new handlers
func NewServiceHandlers(logger *logrus.Logger) *ServiceHandlers {
    return &ServiceHandlers{logger: logger}
}

// Helper functions
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(status)
    json.NewEncoder(w).Encode(data)
}

// Implement api.ServerInterface methods

"@

foreach ($method in $methods) {
    $methodName = $method.Groups[4].Value
    $params = $method.Groups[5].Value
    
    $handlersCode += @"
// $($method.Groups[1].Value)
// $($method.Groups[2].Value) $($method.Groups[3].Value)
func (h *ServiceHandlers) $methodName($params) {
    // TODO: Implement business logic
    respondJSON(w, http.StatusOK, map[string]interface{}{"status": "ok"})
}

"@
}

Set-Content "$svcPath\server\handlers.go" $handlersCode
Write-Host "OK Generated handlers.go with $($methods.Count) methods" -ForegroundColor Green

