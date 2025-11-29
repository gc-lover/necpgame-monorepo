#!/usr/bin/env pwsh
# Массовая оптимизация Kubernetes deployments

$ErrorActionPreference = "Continue"

$SERVICES_CONFIG = @(
    @{Name="achievement-service-go"; HttpPort="8085"; MetricsPort="9095"; Tier="backend"; Resources="standard"},
    @{Name="movement-service-go"; HttpPort="8086"; MetricsPort="9091"; Tier="critical"; Resources="high"},
    @{Name="admin-service-go"; HttpPort="8090"; MetricsPort="9100"; Tier="backend"; Resources="standard"},
    @{Name="matchmaking-go"; HttpPort=""; MetricsPort="9090"; Tier="critical"; Resources="standard"},
    @{Name="ws-lobby-go"; HttpPort="18081"; MetricsPort="9090"; Tier="critical"; Resources="standard"},
    @{Name="economy-service-go"; HttpPort="8086"; MetricsPort="9096"; Tier="backend"; Resources="standard"},
    @{Name="gameplay-service-go"; HttpPort="8083"; MetricsPort="9093"; Tier="backend"; Resources="standard"},
    @{Name="social-service-go"; HttpPort="8084"; MetricsPort="9094"; Tier="backend"; Resources="standard"},
    @{Name="support-service-go"; HttpPort="8087"; MetricsPort="9097"; Tier="backend"; Resources="standard"},
    @{Name="reset-service-go"; HttpPort="8088"; MetricsPort="9098"; Tier="backend"; Resources="standard"}
)

$DEPLOYMENTS_DIR = "k8s"
$OPTIMIZED = @()
$FAILED = @()

function Get-ResourceConfig {
    param($ResourcesType)
    
    switch ($ResourcesType) {
        "high" {
            return @{
                RequestsMemory = "256Mi"
                RequestsCpu = "200m"
                LimitsMemory = "1Gi"
                LimitsCpu = "1000m"
                Replicas = 2
            }
        }
        "standard" {
            return @{
                RequestsMemory = "128Mi"
                RequestsCpu = "100m"
                LimitsMemory = "512Mi"
                LimitsCpu = "500m"
                Replicas = 2
            }
        }
        default {
            return @{
                RequestsMemory = "64Mi"
                RequestsCpu = "100m"
                LimitsMemory = "256Mi"
                LimitsCpu = "500m"
                Replicas = 1
            }
        }
    }
}

foreach ($svc in $SERVICES_CONFIG) {
    $deploymentFile = "$DEPLOYMENTS_DIR\$($svc.Name)-deployment.yaml"
    
    if (-not (Test-Path $deploymentFile)) {
        Write-Host "SKIP: $($svc.Name) (deployment not found)" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "Optimizing: $($svc.Name)" -ForegroundColor Cyan
    
    $resourceConfig = Get-ResourceConfig -ResourcesType $svc.Resources
    
    $httpPortSection = ""
    if ($svc.HttpPort) {
        $httpPortSection = @"
        - name: http
          containerPort: $($svc.HttpPort)
          protocol: TCP
"@
    }
    
    $httpServicePortSection = ""
    if ($svc.HttpPort) {
        $httpServicePortSection = @"
  - name: http
    port: $($svc.HttpPort)
    targetPort: $($svc.HttpPort)
    protocol: TCP
"@
    }
    
    $deploymentContent = @"
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $($svc.Name)
  namespace: necpgame
  labels:
    app: $($svc.Name)
    version: v1
    tier: $($svc.Tier)
spec:
  replicas: $($resourceConfig.Replicas)
  selector:
    matchLabels:
      app: $($svc.Name)
  template:
    metadata:
      labels:
        app: $($svc.Name)
        version: v1
        tier: $($svc.Tier)
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "$($svc.MetricsPort)"
        prometheus.io/path: "/metrics"
    spec:
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        fsGroup: 1000
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: $($svc.Name)
        image: necpgame-$($svc.Name):latest
        imagePullPolicy: IfNotPresent
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
            - ALL
        ports:
$httpPortSection
        - name: metrics
          containerPort: $($svc.MetricsPort)
          protocol: TCP
        resources:
          requests:
            memory: "$($resourceConfig.RequestsMemory)"
            cpu: "$($resourceConfig.RequestsCpu)"
          limits:
            memory: "$($resourceConfig.LimitsMemory)"
            cpu: "$($resourceConfig.LimitsCpu)"
        startupProbe:
          httpGet:
            path: /metrics
            port: $($svc.MetricsPort)
          initialDelaySeconds: 5
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 30
        livenessProbe:
          httpGet:
            path: /metrics
            port: $($svc.MetricsPort)
          initialDelaySeconds: 30
          periodSeconds: 30
          timeoutSeconds: 3
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /metrics
            port: $($svc.MetricsPort)
          initialDelaySeconds: 5
          periodSeconds: 10
          timeoutSeconds: 3
          failureThreshold: 3
        volumeMounts:
        - name: tmp
          mountPath: /tmp
      volumes:
      - name: tmp
        emptyDir: {}
---
apiVersion: v1
kind: Service
metadata:
  name: $($svc.Name)
  namespace: necpgame
  labels:
    app: $($svc.Name)
    prometheus: scrape
spec:
  type: ClusterIP
  ports:
$httpServicePortSection
  - name: metrics
    port: $($svc.MetricsPort)
    targetPort: $($svc.MetricsPort)
    protocol: TCP
  selector:
    app: $($svc.Name)

"@
    
    try {
        $originalContent = Get-Content $deploymentFile -Raw
        
        $envSection = $originalContent | Select-String -Pattern "(?s)(env:.*?resources:)" | ForEach-Object { $_.Matches[0].Groups[1].Value }
        if ($envSection) {
            $deploymentContent = $deploymentContent -replace "(ports:.*?protocol: TCP\s+)resources:", "`$1$envSection"
        }
        
        $deploymentContent | Out-File -FilePath $deploymentFile -Encoding utf8 -NoNewline
        Write-Host "  OK Deployment optimized" -ForegroundColor Green
        $OPTIMIZED += $svc.Name
    } catch {
        Write-Host "  ❌ Failed: $_" -ForegroundColor Red
        $FAILED += $svc.Name
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Optimized: $($OPTIMIZED.Count)" -ForegroundColor Green
Write-Host "Failed: $($FAILED.Count)" -ForegroundColor $(if ($FAILED.Count -eq 0) { "Green" } else { "Red" })

if ($OPTIMIZED.Count -gt 0) {
    Write-Host "`nOptimized deployments:" -ForegroundColor Green
    $OPTIMIZED | ForEach-Object { Write-Host "  OK $_" -ForegroundColor Green }
}

Write-Host "`nNote: Manual review required for:" -ForegroundColor Yellow
Write-Host "  - Environment variables (env section)" -ForegroundColor Yellow
Write-Host "  - Service-specific configurations" -ForegroundColor Yellow

