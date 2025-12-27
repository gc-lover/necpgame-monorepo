#!/usr/bin/env python3
"""
Simple script to generate economy-domain Go service using direct generation
"""

import sys
from pathlib import Path
import yaml

def generate_economy_service():
    """Generate economy-domain service directly"""

    # Load spec
    spec_path = Path("proto/openapi/economy-domain/main.yaml")
    with open(spec_path, 'r', encoding='utf-8') as f:
        spec = yaml.safe_load(f)

    service_dir = Path("services/economy-domain-service-go")
    service_dir.mkdir(parents=True, exist_ok=True)

    # Generate main.go
    main_content = '''package main

import (
    "context"
    "log"
    "net/http"
    "os"
    "os/signal"
    "syscall"
    "time"

    "economy-domain-service-go/pkg/api"
    "economy-domain-service-go/server"
)

func main() {
    logger := log.New(os.Stdout, "[economy] ", log.LstdFlags)

    svc := server.NewEconomyService()

    server := &http.Server{
        Addr:    ":8080",
        Handler: svc.Handler(),
    }

    quit := make(chan os.Signal, 1)
    signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

    go func() {
        logger.Printf("Starting economy service on :8080 (GOGC=50)")
        if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
            logger.Fatalf("HTTP server error: %v", err)
        }
    }()

    <-quit
    logger.Println("Shutting down server...")

    ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
    defer cancel()

    if err := server.Shutdown(ctx); err != nil {
        logger.Printf("Server forced to shutdown: %v", err)
    }

    logger.Println("Server exited")
}
'''
    (service_dir / "main.go").write_text(main_content)

    # Generate go.mod
    go_mod_content = '''module economy-domain-service-go

go 1.25.3

require (
    github.com/go-chi/chi/v5 v5.0.12
    github.com/go-chi/cors v1.2.2
    github.com/prometheus/client_golang v1.23.2
    go.uber.org/zap v1.27.1
)
'''
    (service_dir / "go.mod").write_text(go_mod_content)

    # Generate Dockerfile
    dockerfile_content = '''FROM golang:1.21-alpine AS builder

RUN apk add --no-cache git ca-certificates

WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o economy-service .

FROM alpine:latest
RUN apk --no-cache add ca-certificates
RUN adduser -D -s /bin/sh appuser

WORKDIR /root/
COPY --from=builder /app/economy-service .
RUN chown appuser economy-service
USER appuser

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \\
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

CMD ["./economy-service"]
'''
    (service_dir / "Dockerfile").write_text(dockerfile_content)

    # Generate Makefile
    makefile_content = '''build:
	go build -o bin/economy-service .

run:
	go run main.go

test:
	go test ./...

deps:
	go mod tidy
	go mod download

fmt:
	go fmt ./...

docker-build:
	docker build -t economy-service .

docker-run:
	docker run -p 8080:8080 economy-service

setup: deps build docker-build
'''
    (service_dir / "Makefile").write_text(makefile_content)

    # Create server directory
    server_dir = service_dir / "server"
    server_dir.mkdir(exist_ok=True)

    # Generate server.go
    server_content = '''package server

import (
    "net/http"
    "time"

    "github.com/go-chi/chi/v5"
    "github.com/go-chi/chi/v5/middleware"
    "github.com/go-chi/cors"
    "go.uber.org/zap"

    "economy-domain-service-go/pkg/api"
)

type EconomyService struct {
    api *api.Server
}

func NewEconomyService() *EconomyService {
    handler := NewHandler()

    r := chi.NewRouter()
    r.Use(middleware.RequestID)
    r.Use(middleware.RealIP)
    r.Use(middleware.Logger)
    r.Use(middleware.Recoverer)
    r.Use(middleware.Timeout(60 * time.Second))
    r.Use(cors.Handler(cors.Options{
        AllowedOrigins:   []string{"*"},
        AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
        AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
        ExposedHeaders:   []string{"Link"},
        AllowCredentials: true,
        MaxAge:           300,
    }))

    r.Get("/health", handler.HealthCheck)
    r.Route("/api/v1/economy", func(r chi.Router) {
        r.Get("/characters/{characterId}/inventory", handler.GetCharacterInventory)
        r.Get("/overview", handler.GetEconomyOverview)
        r.Get("/currencies", handler.GetCurrencies)
    })

    return &EconomyService{
        api: api.NewServer(handler),
    }
}

func (s *EconomyService) Handler() http.Handler {
    return s.api
}
'''
    (server_dir / "server.go").write_text(server_content)

    # Generate handlers.go
    handlers_content = '''package server

import (
    "context"
    "net/http"

    "economy-domain-service-go/pkg/api"
    "go.uber.org/zap"
)

type Handler struct {
    service *Service
    logger  *zap.Logger
}

func NewHandler() *Handler {
    logger, _ := zap.NewProduction()
    return &Handler{
        service: NewService(),
        logger:  logger,
    }
}

func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write([]byte(`{"status":"healthy","service":"economy-domain"}`))
}

func (h *Handler) GetCharacterInventory(w http.ResponseWriter, r *http.Request) (api.GetCharacterInventoryRes, error) {
    // TODO: Implement based on OpenAPI spec
    return nil, nil
}

func (h *Handler) GetEconomyOverview(w http.ResponseWriter, r *http.Request) (api.GetEconomyOverviewRes, error) {
    // TODO: Implement based on OpenAPI spec
    return nil, nil
}

func (h *Handler) GetCurrencies(w http.ResponseWriter, r *http.Request) (api.GetCurrenciesRes, error) {
    // TODO: Implement based on OpenAPI spec
    return nil, nil
}
'''
    (server_dir / "handlers.go").write_text(handlers_content)

    # Generate service.go
    service_content = '''package server

import (
    "context"
    "sync"

    "economy-domain-service-go/pkg/api"
    "go.uber.org/zap"
)

type Service struct {
    repo   *Repository
    logger *zap.Logger
    pool   *sync.Pool
}

func NewService() *Service {
    logger, _ := zap.NewProduction()
    return &Service{
        repo:   NewRepository(),
        logger: logger,
        pool: &sync.Pool{
            New: func() interface{} {
                return &api.HealthResponse{}
            },
        },
    }
}

func (s *Service) HealthCheck(ctx context.Context) error {
    return s.repo.HealthCheck(ctx)
}
'''
    (server_dir / "service.go").write_text(service_content)

    # Generate repository.go
    repo_content = '''package server

import (
    "context"
    "database/sql"
    "time"

    _ "github.com/lib/pq"
    "go.uber.org/zap"
)

type Repository struct {
    db    *sql.DB
    logger *zap.Logger
}

func NewRepository() *Repository {
    logger, _ := zap.NewProduction()
    return &Repository{
        logger: logger,
    }
}

func (r *Repository) InitDB(dsn string) error {
    var err error
    r.db, err = sql.Open("postgres", dsn)
    if err != nil {
        return err
    }

    r.db.SetMaxOpenConns(25)
    r.db.SetMaxIdleConns(25 / 2)
    r.db.SetConnMaxLifetime(time.Hour)

    return r.db.Ping()
}

func (r *Repository) HealthCheck(ctx context.Context) error {
    if r.db == nil {
        return sql.ErrNoRows
    }
    return r.db.PingContext(ctx)
}
'''
    (server_dir / "repository.go").write_text(repo_content)

    # Create pkg/api directory
    api_dir = service_dir / "pkg" / "api"
    api_dir.mkdir(parents=True, exist_ok=True)

    # Generate interfaces.go
    interfaces_content = '''package api

import "context"

// Generated API interfaces based on OpenAPI spec

type GetCharacterInventoryParams struct {
    CharacterId string `path:"characterId"`
}

type GetCharacterInventoryRequest struct {
    Params GetCharacterInventoryParams
}

type GetCharacterInventoryResponse struct {
    CharacterId string                 `json:"characterId"`
    Items       []InventoryItem        `json:"items"`
    Credits     int64                  `json:"credits"`
    LastUpdated string                 `json:"lastUpdated"`
}

type InventoryItem struct {
    ItemId   string `json:"itemId"`
    ItemType string `json:"itemType"`
    Name     string `json:"name"`
    Quantity int32  `json:"quantity"`
    Rarity   string `json:"rarity"`
}

type GetCharacterInventoryRes interface {
    isGetCharacterInventoryRes()
}

type GetEconomyOverviewParams struct {
    IncludeMarketData   *bool `query:"include_market_data"`
    IncludeCurrencyRates *bool `query:"include_currency_rates"`
}

type GetEconomyOverviewRequest struct {
    Params GetEconomyOverviewParams
}

type GetEconomyOverviewResponse struct {
    Timestamp        string                 `json:"timestamp"`
    MarketStats      *MarketStats           `json:"market_stats,omitempty"`
    CurrencyRates    *CurrencyRates         `json:"currency_rates,omitempty"`
}

type MarketStats struct {
    TotalActiveTrades   int64  `json:"total_active_trades"`
    TotalMarketVolume   float64 `json:"total_market_volume"`
    AverageTradePrice   float64 `json:"average_trade_price"`
}

type CurrencyRates struct {
    BaseCurrency string             `json:"base_currency"`
    Rates        map[string]float64 `json:"rates"`
}

type GetEconomyOverviewRes interface {
    isGetEconomyOverviewRes()
}

type GetCurrenciesRequest struct{}

type GetCurrenciesResponse struct {
    Currencies []Currency `json:"currencies"`
}

type Currency struct {
    CurrencyId   string  `json:"currency_id"`
    Code         string  `json:"code"`
    Name         string  `json:"name"`
    Symbol       string  `json:"symbol"`
    ExchangeRate float64 `json:"exchange_rate,omitempty"`
    IsActive     bool    `json:"is_active"`
}

type GetCurrenciesRes interface {
    isGetCurrenciesRes()
}

type HealthResponse struct {
    Status      string `json:"status"`
    Timestamp   string `json:"timestamp"`
    Service     string `json:"service"`
    Version     string `json:"version,omitempty"`
    Uptime      int64  `json:"uptime_seconds,omitempty"`
}

type Error struct {
    Message   string `json:"message"`
    Code      int32  `json:"code"`
    Domain    string `json:"domain,omitempty"`
}

// Handler interface
type Handler interface {
    GetCharacterInventory(ctx context.Context, params GetCharacterInventoryParams) (GetCharacterInventoryRes, error)
    GetEconomyOverview(ctx context.Context, params GetEconomyOverviewParams) (GetEconomyOverviewRes, error)
    GetCurrencies(ctx context.Context, req GetCurrenciesRequest) (GetCurrenciesRes, error)
}

// Server implementation
type Server struct {
    handler Handler
}

func NewServer(handler Handler) *Server {
    return &Server{handler: handler}
}

func (s *Server) ServeHTTP(w http.ResponseWriter, r *http.Request) {
    // TODO: Implement HTTP routing based on OpenAPI spec
    w.WriteHeader(http.StatusNotImplemented)
    w.Write([]byte("Not implemented"))
}
'''
    (api_dir / "interfaces.go").write_text(interfaces_content)

    print("Successfully generated economy-domain Go service!")
    print(f"Service created in: {service_dir}")
    print("Files generated:")
    print("  - main.go")
    print("  - go.mod")
    print("  - Dockerfile")
    print("  - Makefile")
    print("  - server/server.go")
    print("  - server/handlers.go")
    print("  - server/service.go")
    print("  - server/repository.go")
    print("  - pkg/api/interfaces.go")

if __name__ == '__main__':
    generate_economy_service()
