# Create ogen-compatible handlers for migrated services
# For services that have ogen code generated but need handler structure

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName
)

$ServicePath = "services\$ServiceName"

if (!(Test-Path "$ServicePath\pkg\api\oas_server_gen.go")) {
    Write-Host "❌ Service not migrated to ogen yet" -ForegroundColor Red
    exit 1
}

Write-Host "🔧 Creating ogen handlers for $ServiceName..." -ForegroundColor Cyan
Write-Host ""

# Check if server/ exists
if (!(Test-Path "$ServicePath\server")) {
    Write-Host "Creating server/ directory..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path "$ServicePath\server" -Force | Out-Null
}

# Create go.mod if missing
if (!(Test-Path "$ServicePath\go.mod")) {
    Write-Host "Creating go.mod..." -ForegroundColor Yellow
    $gomod = @"
// Issue: #1595
module github.com/gc-lover/necpgame-monorepo/services/$ServiceName

go 1.24

require (
	github.com/go-chi/chi/v5 v5.2.0
	github.com/lib/pq v1.10.9
	github.com/ogen-go/ogen v1.18.0
	go.opentelemetry.io/otel v1.38.0
	go.opentelemetry.io/otel/metric v1.38.0
	go.opentelemetry.io/otel/trace v1.38.0
)
"@
    Set-Content -Path "$ServicePath\go.mod" -Value $gomod
}

# Create main.go if missing
if (!(Test-Path "$ServicePath\main.go")) {
    Write-Host "Creating main.go..." -ForegroundColor Yellow
    $port = Get-Random -Minimum 8100 -Maximum 8200
    $serviceName = $ServiceName -replace "-service-go", "" -replace "-", " "
    
    $main = @"
// Issue: #1595
package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/$ServiceName/server"
)

func main() {
	addr := getEnv("SERVER_ADDR", ":$port")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")

	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repo.Close()

	service := server.NewService(repo)
	httpServer := server.NewHTTPServer(addr, service)

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	go func() {
		log.Printf("Starting $serviceName Service on %s", addr)
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	<-stop
	log.Println("Shutting down gracefully...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("Shutdown error: %v", err)
	}

	log.Println("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
"@
    Set-Content -Path "$ServicePath\main.go" -Value $main
}

# Copy server files from template
Write-Host "Copying server structure from template..." -ForegroundColor Yellow
Copy-Item "services\combat-actions-service-go\server\*" "$ServicePath\server\" -Force

# Update imports in copied files
Write-Host "Updating imports..." -ForegroundColor Yellow
Get-ChildItem "$ServicePath\server\*.go" | ForEach-Object {
    (Get-Content $_.FullName) | 
        ForEach-Object { $_ -replace "combat-actions-service-go", $ServiceName } |
        Set-Content $_.FullName
}

Write-Host "OK Structure created!" -ForegroundColor Green
Write-Host ""
Write-Host "Next: cd $ServicePath && go mod tidy && go build ." -ForegroundColor Yellow

