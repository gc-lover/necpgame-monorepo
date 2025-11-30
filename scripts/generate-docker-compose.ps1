# Script to auto-generate docker-compose.yml from services with Dockerfiles
# Analyzes main.go to determine dependencies and ports

$ErrorActionPreference = "Continue"
$servicesPath = "services"
$outputFile = "docker-compose.yml"

# Port allocation starting from 8100
$basePort = 8100
$baseMetricsPort = 9200
$redisDbIndex = 13
$currentPort = $basePort
$currentMetricsPort = $baseMetricsPort

# Infrastructure services (keep existing)
$infrastructureServices = @"
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: necpgame
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    command: ["redis-server","--appendonly","yes"]
    ports:
      - "6379:6379"
    volumes:
      - redisdata:/data
    healthcheck:
      test: ["CMD", "redis-cli", "PING"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped

  keycloak:
    image: quay.io/keycloak/keycloak:24.0
    command: start-dev --http-enabled=true --hostname-strict=false --import-realm
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    ports:
      - "8080:8080"
    volumes:
      - ./infrastructure/docker/auth-envoy/realm-export:/opt/keycloak/data/import:ro

  envoy:
    image: envoyproxy/envoy:v1.31.0
    depends_on:
      - keycloak
    ports:
      - "9901:9901"
      - "8443:8443"
    volumes:
      - ./infrastructure/envoy/envoy.yaml:/etc/envoy/envoy.yaml:ro
      - ./infrastructure/envoy/certs:/etc/envoy/certs:ro

  prometheus:
    image: prom/prometheus:v2.53.0
    volumes:
      - ./infrastructure/observability/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana:11.1.0
    environment:
      GF_SECURITY_ADMIN_USER: admin
      GF_SECURITY_ADMIN_PASSWORD: admin
    volumes:
      - ./infrastructure/observability/grafana/provisioning:/etc/grafana/provisioning:ro
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
      - loki
      - tempo

  loki:
    image: grafana/loki:3.0.0
    command: -config.file=/etc/loki/loki-config.yml
    volumes:
      - ./infrastructure/observability/loki/loki-config.yml:/etc/loki/loki-config.yml:ro
    ports:
      - "3100:3100"

  promtail:
    image: grafana/promtail:3.0.0
    command: -config.file=/etc/promtail/promtail-config.yml
    volumes:
      - ./infrastructure/observability/promtail/promtail-config.yml:/etc/promtail/promtail-config.yml:ro
      - /var/run/docker.sock:/var/run/docker.sock:ro
      - /var/lib/docker/containers:/var/lib/docker/containers:ro
    depends_on:
      - loki
    ports:
      - "9080:9080"

  tempo:
    image: grafana/tempo:2.5.0
    command: -config.file=/etc/tempo/tempo.yaml
    volumes:
      - ./infrastructure/observability/tempo/tempo.yaml:/etc/tempo/tempo.yaml:ro
    ports:
      - "3200:3200"

  nexus:
    image: sonatype/nexus3:3.68.1
    ports:
      - "8081:8081"
    volumes:
      - nexus-data:/nexus-data

  mcp-github:
    image: ghcr.io/github/github-mcp-server:latest
    environment:
      - GITHUB_PERSONAL_ACCESS_TOKEN=`${GITHUB_PERSONAL_ACCESS_TOKEN}
      - GITHUB_TOOLSETS=default,projects
    stdin_open: true
    tty: true
    restart: unless-stopped

"@

function Get-ServiceDependencies {
  param($servicePath)
    
  $mainGo = Join-Path $servicePath "main.go"
  if (-not (Test-Path $mainGo)) {
    return @()
  }
    
  $content = Get-Content $mainGo -Raw
  $deps = @()
    
  if ($content -match "DATABASE_URL|postgres|pgxpool") {
    $deps += "postgres"
  }
  if ($content -match "REDIS_URL|redis") {
    $deps += "redis"
  }
  if ($content -match "KEYCLOAK|keycloak|JWT|jwks") {
    $deps += "keycloak"
  }
  if ($content -match "realtime-gateway|GATEWAY_URL") {
    $deps += "realtime-gateway"
  }
    
  return $deps
}

function Get-ServiceEnvironment {
  param($servicePath, $port, $metricsPort, $redisDbIndex)
    
  $mainGo = Join-Path $servicePath "main.go"
  if (-not (Test-Path $mainGo)) {
    return @()
  }
    
  $content = Get-Content $mainGo -Raw
  $env = @()
    
  $env += "      - ADDR=0.0.0.0:$port"
  $env += "      - METRICS_ADDR=:$metricsPort"
  $env += "      - LOG_LEVEL=info"
    
  if ($content -match "DATABASE_URL") {
    $env += "      - DATABASE_URL=postgresql://postgres:postgres@postgres:5432/necpgame?sslmode=disable"
  }
  if ($content -match "REDIS_URL") {
    $env += "      - REDIS_URL=redis://redis:6379/$redisDbIndex"
  }
  if ($content -match "KEYCLOAK_URL|KEYCLOAK_REALM") {
    $env += "      - KEYCLOAK_URL=http://keycloak:8080"
    $env += "      - KEYCLOAK_REALM=necpgame"
    $env += "      - AUTH_ENABLED=true"
  }
  if ($content -match "KEYCLOAK_ISSUER|JWKS_URL") {
    $env += "      - KEYCLOAK_ISSUER=http://keycloak:8080/realms/necpgame"
    $env += "      - JWKS_URL=http://keycloak:8080/realms/necpgame/protocol/openid-connect/certs"
    $env += "      - AUTH_ENABLED=true"
  }
  if ($content -match "GATEWAY_URL|realtime-gateway") {
    $env += "      - GATEWAY_URL=ws://realtime-gateway:18080/client"
  }
  if ($content -match "GITHUB_TOKEN") {
    $env += "      - GITHUB_TOKEN=`${GITHUB_TOKEN}"
  }
  if ($content -match "WEBRTC") {
    $env += "      - WEBRTC_URL=wss://voice.necp.game"
    $env += "      - WEBRTC_KEY="
  }
  if ($content -match "HTTP_ADDR") {
    # Some services use HTTP_ADDR instead of ADDR
    $env = $env | Where-Object { $_ -notmatch "ADDR=0.0.0.0" }
    $env = @($env | Where-Object { $_ -notmatch "ADDR" }) + "      - HTTP_ADDR=:$port"
  }
    
  return $env
}

function Get-DockerfileContext {
  param($servicePath, $serviceName)
    
  $dockerfile = Join-Path $servicePath "Dockerfile"
  if (-not (Test-Path $dockerfile)) {
    return $null, $null
  }
    
  $content = Get-Content $dockerfile -Raw
    
  # Check if Dockerfile uses context from root
  if ($content -match "COPY services/|COPY \.\./") {
    return ".", "services/$serviceName/Dockerfile"
  }
  else {
    return "./services/$serviceName", "Dockerfile"
  }
}

# Find all services with Dockerfiles
$services = @()
Get-ChildItem -Path $servicesPath -Directory | Where-Object {
  Test-Path (Join-Path $_.FullName "Dockerfile")
} | ForEach-Object {
  $serviceName = $_.Name
  $servicePath = $_.FullName
    
  # Skip services that don't have main.go (not ready)
  $mainGo = Join-Path $servicePath "main.go"
  if (-not (Test-Path $mainGo)) {
    Write-Host "Skipping $serviceName (no main.go)" -ForegroundColor Yellow
    return
  }
    
  $deps = Get-ServiceDependencies $servicePath
  $context, $dockerfile = Get-DockerfileContext $servicePath $serviceName
    
  if ($null -eq $context) {
    Write-Host "Skipping $serviceName (no Dockerfile)" -ForegroundColor Yellow
    return
  }
    
  $envVars = Get-ServiceEnvironment $servicePath $currentPort $currentMetricsPort $redisDbIndex
    
  $serviceConfig = @{
    Name         = $serviceName
    Port         = $currentPort
    MetricsPort  = $currentMetricsPort
    RedisDb      = $redisDbIndex
    Dependencies = $deps
    Context      = $context
    Dockerfile   = $dockerfile
    Environment  = $envVars
  }
    
  $services += $serviceConfig
    
  Write-Host "Found: $serviceName (port $currentPort, metrics $currentMetricsPort)" -ForegroundColor Green
    
  $currentPort++
  $currentMetricsPort++
  $redisDbIndex++
}

# Generate docker-compose.yml
$composeContent = $infrastructureServices
$composeContent += "`n"

# Special services that need custom configuration
$specialServices = @{
  "realtime-gateway-go" = @{
    Port         = 18080
    MetricsPort  = 9093
    Context      = "./services/realtime-gateway-go"
    Dockerfile   = "Dockerfile"
    Dependencies = @("keycloak")
    ExtraEnv     = @()
  }
  "ws-lobby-go"         = @{
    Port         = 18081
    MetricsPort  = 9091
    Context      = "./services/ws-lobby-go"
    Dockerfile   = "Dockerfile"
    Dependencies = @("keycloak")
    ExtraEnv     = @("      - PORT=18081")
  }
  "matchmaking-go"      = @{
    Port         = 9092
    MetricsPort  = 9092
    Context      = "./services/matchmaking-go"
    Dockerfile   = "Dockerfile"
    Dependencies = @("redis")
    ExtraEnv     = @("      - REDIS_URL=redis://redis:6379", "      - MODE=pve8", "      - TEAM_SIZE=8")
  }
}

foreach ($service in $services) {
  $serviceName = $service.Name
  $composeName = $serviceName -replace "-go$", "" -replace "-service-go$", "-service"
    
  # Check if it's a special service
  if ($specialServices.ContainsKey($serviceName)) {
    $special = $specialServices[$serviceName]
    $composeContent += "  ${composeName}:`n"
    $composeContent += "    build:`n"
    $composeContent += "      context: $($special.Context)`n"
    $composeContent += "      dockerfile: $($special.Dockerfile)`n"
        
    if ($special.Dependencies -and $special.Dependencies.Count -gt 0) {
      $composeContent += "    depends_on:`n"
      foreach ($dep in $special.Dependencies) {
        $composeContent += "      - $dep`n"
      }
    }
        
    $composeContent += "    environment:`n"
    if ($special.ExtraEnv -and $special.ExtraEnv.Count -gt 0) {
      foreach ($env in $special.ExtraEnv) {
        $composeContent += "$env`n"
      }
    }
    else {
      $composeContent += "      - LOG_LEVEL=info`n"
    }
        
    $composeContent += "    ports:`n"
    $composeContent += "      - `"$($special.Port):$($special.Port)/tcp`"`n"
    $composeContent += "      - `"$($special.MetricsPort):9090`"`n"
    $composeContent += "    restart: unless-stopped`n"
    $composeContent += "`n"
    continue
  }
    
  # Regular service
  $composeContent += "  ${composeName}:`n"
  $composeContent += "    build:`n"
  $composeContent += "      context: $($service.Context)`n"
  $composeContent += "      dockerfile: $($service.Dockerfile)`n"
    
  if ($service.Dependencies.Count -gt 0) {
    $composeContent += "    depends_on:`n"
    foreach ($dep in $service.Dependencies) {
      $composeContent += "      - $dep`n"
    }
  }
    
  $composeContent += "    environment:`n"
  foreach ($env in $service.Environment) {
    $composeContent += "      $env`n"
  }
    
  $composeContent += "    ports:`n"
  $composeContent += "      - `"$($service.Port):$($service.Port)`"`n"
  $composeContent += "      - `"$($service.MetricsPort):$($service.MetricsPort)`"`n"
  $composeContent += "    restart: unless-stopped`n"
  $composeContent += "`n"
}

# Add volumes
$composeContent += @"
volumes:
  pgdata:
  redisdata:
  nexus-data:

"@

# Write to file
$composeContent | Out-File -FilePath $outputFile -Encoding utf8 -NoNewline

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Generated docker-compose.yml" -ForegroundColor Green
Write-Host "Total services: $($services.Count)" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

