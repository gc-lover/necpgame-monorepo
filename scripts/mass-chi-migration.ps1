# Mass Chi migration for remaining services

param(
    [string[]]$Services = @(
        "leaderboard-service-go", "combat-sessions-service-go", 
        "gameplay-service-go", "league-service-go", "social-player-orders-service-go",
        "housing-service-go", "companion-service-go", "world-service-go",
        "referral-service-go", "social-service-go", "cosmetic-service-go"
    )
)

$ports = @{
    "leaderboard-service-go" = "8124"
    "combat-sessions-service-go" = "8158"
    "gameplay-service-go" = "8120"
    "league-service-go" = "8157"
    "social-player-orders-service-go" = "8156"
    "housing-service-go" = "8122"
    "companion-service-go" = "8116"
    "world-service-go" = "8155"
    "referral-service-go" = "8134"
    "social-service-go" = "8143"
    "cosmetic-service-go" = "8117"
}

$httpServerTemplate = @'
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/necpgame/{SERVICE_NAME}/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	logger *logrus.Logger
}

func NewHTTPServer(addr string, logger *logrus.Logger) *HTTPServer {
	handlers := New{HANDLER_NAME}(logger)

	router := chi.NewRouter()

	router.Use(loggingMiddleware(logger))
	router.Use(recoveryMiddleware(logger))
	router.Use(corsMiddleware)

	// Generated API handlers with Chi
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseRouter: router,
	})

	router.Handle("/metrics", promhttp.Handler())
	router.Get("/health", healthCheckHandler)

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
		logger: logger,
	}
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("address", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}

func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			next.ServeHTTP(w, r)
			logger.WithFields(logrus.Fields{
				"method":   r.Method,
				"path":     r.URL.Path,
				"duration": time.Since(start),
			}).Info("HTTP request processed")
		})
	}
}

func recoveryMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.WithField("error", err).Error("Panic recovered")
					http.Error(w, "Internal Server Error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
}

func corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}
'@

$mainTemplate = @'
package main

import (
	"context"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/{SERVICE_NAME}/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)
	logger.Info("{SERVICE_TITLE} starting...")

	addr := getEnv("ADDR", "0.0.0.0:{PORT}")

	httpServer := server.NewHTTPServer(addr, logger)

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		httpServer.Shutdown(shutdownCtx)
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(); err != nil {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
'@

$successCount = 0

Write-Host "`n🔧 Mass Chi migration for $($Services.Count) services...`n" -ForegroundColor Cyan

foreach ($svc in $Services) {
    Write-Host "$svc..." -NoNewline
    
    $svcPath = "services\$svc"
    if (-not (Test-Path $svcPath)) {
        Write-Host " ⏭️  Not found" -ForegroundColor Gray
        continue
    }
    
    cd $svcPath
    
    # Backup old files
    Get-ChildItem server\*.go | Where-Object { -not $_.Name.EndsWith('.backup') } | ForEach-Object {
        if (Test-Path "$($_.FullName).backup") { return }
        Rename-Item $_.FullName "$($_.FullName).backup" -ErrorAction SilentlyContinue
    }
    
    # Generate handlers stub (health only for now)
    $handlersContent = @"
// Handlers for $svc - implements api.ServerInterface
package server

import (
    "net/http"
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

// HealthCheck implements GET /health
func (h *ServiceHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write([]byte(``{"status":"ok"}``))
}
"@
    
    Set-Content "server\handlers.go" $handlersContent
    
    # Create http_server.go from template
    $handlerName = "ServiceHandlers"
    $httpContent = $httpServerTemplate -replace '{SERVICE_NAME}', $svc -replace '{HANDLER_NAME}', $handlerName
    Set-Content "server\http_server.go" $httpContent
    
    # Create main.go from template
    $svcTitle = ($svc -replace '-go$', '' -replace '-', ' ').Split(' ') | ForEach-Object { $_.Substring(0,1).ToUpper() + $_.Substring(1) } | Join-String -Separator ' '
    $port = $ports[$svc]
    $mainContent = $mainTemplate -replace '{SERVICE_NAME}', $svc -replace '{SERVICE_TITLE}', "$svcTitle Service" -replace '{PORT}', $port
    Set-Content "main.go" $mainContent
    
    # Test compilation
    go build ./... 2>&1 | Out-Null
    
    cd ..\..
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host " OK" -ForegroundColor Green
        $successCount++
    }
    else {
        Write-Host " ❌" -ForegroundColor Red
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "OK Success: $successCount / $($Services.Count)" -ForegroundColor Green

