// Issue: #1442
package main

import (
	"database/sql"
	"log"
	"os"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/server"
	_ "github.com/lib/pq"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	if err := db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}

	// Create repository
	repo := server.NewRepository(db)

	// Create service
	svc := server.NewService(repo)

	// Create handlers
	handlers := server.NewHandlers(svc)

	// Create HTTP server
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8091"
	}

	httpServer := server.NewHTTPServer(addr, handlers, svc)

	log.Printf("Starting Faction Core Service on %s", addr)
	if err := httpServer.Start(); err != nil {
		log.Fatalf("Server failed: %v", err)
	}
}




