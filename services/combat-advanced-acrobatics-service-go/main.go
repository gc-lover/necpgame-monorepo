package main

import (
	"log"
	"net/http"
	"os"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-advanced-acrobatics-service-go/server"
)

func main() {
	addr := env("HTTP_ADDR", ":8083")

	svc := server.NewService()
	router := server.NewRouter(svc)

	srv := &http.Server{
		Addr:              addr,
		Handler:           router,
		ReadHeaderTimeout: 3 * time.Second,
	}

	log.Printf("combat-advanced-acrobatics-service listening on %s", addr)
	if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		log.Fatalf("server error: %v", err)
	}
}

func env(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}

