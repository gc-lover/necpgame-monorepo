// Issue: #145 - achievement-service BLOCKER optimizations
package main

import (
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof"
	"os"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/server"
	_ "github.com/lib/pq"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	dbURL := getEnv("DATABASE_URL", "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable")

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed: %v", err)
	}
	defer db.Close()

	db.SetMaxOpenConns(50)
	db.SetMaxIdleConns(50)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	repository, err := server.NewRepository(dbURL)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repository.Close()

	service := server.NewService(repository)

	addr := getEnv("HTTP_ADDR", ":8097")
	httpServer := server.NewHTTPServer(addr, service)

	go func() {
		mux := http.NewServeMux()
		mux.Handle("/metrics", promhttp.Handler())
		http.ListenAndServe(":9097", mux)
	}()

	go func() {
		http.ListenAndServe("localhost:6097", nil)
	}()

	log.Printf("OK Achievement Service on %s", addr)
	httpServer.Start()
}

func getEnv(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
