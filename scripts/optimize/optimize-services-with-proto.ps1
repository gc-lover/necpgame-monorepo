#!/usr/bin/env pwsh
# Оптимизация Dockerfile для сервисов, использующих proto/

$ErrorActionPreference = "Continue"

$SERVICES_WITH_PROTO = @(
    "achievement-service-go",
    "admin-service-go",
    "battle-pass-service-go",
    "character-service-go",
    "clan-war-service-go",
    "companion-service-go",
    "feedback-service-go",
    "housing-service-go",
    "inventory-service-go",
    "leaderboard-service-go",
    "movement-service-go",
    "progression-paragon-service-go",
    "referral-service-go",
    "voice-chat-service-go"
)

$OPTIMIZED = @()
$FAILED = @()

foreach ($service in $SERVICES_WITH_PROTO) {
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "Optimizing: $service" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    
    $serviceDir = "services\$service"
    $dockerfile = "$serviceDir\Dockerfile"
    
    if (-not (Test-Path $dockerfile)) {
        Write-Host "SKIP: Dockerfile not found" -ForegroundColor Yellow
        continue
    }
    
    $content = Get-Content $dockerfile -Raw
    $serviceName = $service -replace "-go$", ""
    
    Write-Host "Creating optimal Dockerfile..." -ForegroundColor Cyan
    
    $optimalDockerfile = @"
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata

COPY services/$service/go.mod services/$service/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/$service ./services/$service
COPY proto ./proto

WORKDIR /app/services/$service

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o $serviceName -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/$service/$serviceName /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/metrics || exit 1

ENTRYPOINT ["/app/$serviceName"]
"@
    
    try {
        $optimalDockerfile | Out-File -FilePath $dockerfile -Encoding utf8 -NoNewline
        Write-Host "Dockerfile updated" -ForegroundColor Green
        $OPTIMIZED += $service
    } catch {
        Write-Host "ERROR: Failed to update Dockerfile: $_" -ForegroundColor Red
        $FAILED += $service
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Optimized: $($OPTIMIZED.Count)" -ForegroundColor Green
Write-Host "Failed: $($FAILED.Count)" -ForegroundColor $(if ($FAILED.Count -eq 0) { "Green" } else { "Red" })

if ($OPTIMIZED.Count -gt 0) {
    Write-Host "`nOptimized services:" -ForegroundColor Green
    $OPTIMIZED | ForEach-Object { Write-Host "  - $_" -ForegroundColor Green }
}

if ($FAILED.Count -gt 0) {
    Write-Host "`nFailed services:" -ForegroundColor Red
    $FAILED | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
}

Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Update docker-compose.yml to use context from root for these services" -ForegroundColor Yellow
Write-Host "2. Update EXPOSE ports and health check paths for each service" -ForegroundColor Yellow
Write-Host "3. Test build and run for each service" -ForegroundColor Yellow

