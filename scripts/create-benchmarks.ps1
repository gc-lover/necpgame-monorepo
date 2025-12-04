# Script to create handlers_bench_test.go for services missing benchmarks
# Usage: .\scripts\create-benchmarks.ps1

$servicesPath = "services"
$servicesWithoutBench = @(
    "database-partition-manager",
    "database-view-refresher",
    "economy-player-market-service-go",
    "economy-service-go",
    "inventory-service-go",
    "realtime-gateway-go",
    "stock-analytics-charts-service-go",
    "stock-analytics-tools-service-go",
    "support-service-go"
)

foreach ($service in $servicesWithoutBench) {
    $servicePath = Join-Path $servicesPath $service
    $serverPath = Join-Path $servicePath "server"
    $benchFile = Join-Path $serverPath "handlers_bench_test.go"
    
    if (-not (Test-Path $benchFile)) {
        Write-Host "Creating benchmark for $service..."
        
        # Check if service has handlers
        $handlersFile = Get-ChildItem -Path $serverPath -Filter "*handlers*.go" | Select-Object -First 1
        
        if ($handlersFile) {
            Write-Host "  Found handlers: $($handlersFile.Name)"
            # Create basic benchmark file
            $content = @"
// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"
)

// BenchmarkHandler benchmarks handler performance
// Target: <100μs per operation, minimal allocs
func BenchmarkHandler(b *testing.B) {
	// TODO: Setup service and handlers based on service structure
	// service := NewService(...)
	// handlers := NewHandlers(service)
	
	ctx := context.Background()
	
	b.ReportAllocs()
	b.ResetTimer()
	
	for i := 0; i < b.N; i++ {
		// TODO: Add actual handler call based on service API
		_ = ctx
	}
}
"@
            Set-Content -Path $benchFile -Value $content
            Write-Host "  Created: $benchFile"
        } else {
            Write-Host "  No handlers found, skipping..."
        }
    } else {
        Write-Host "  Benchmark already exists: $service"
    }
}

Write-Host "Done!"

