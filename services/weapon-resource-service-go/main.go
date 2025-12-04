// Issue: #1574
package main

import (
	"database/sql"
	"log"
	"os"

	_ "github.com/lib/pq"
	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/server"
)

func main() {
	// Get configuration from environment
	addr := getEnv("HTTP_ADDR", ":8081")
	dbURL := getEnv("DATABASE_URL", "postgres://localhost/necpgame?sslmode=disable")

	// Connect to database
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	if err := db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}

	// Create and start HTTP server
	srv := server.NewHTTPServer(addr, db)
	log.Fatal(srv.Start())
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}








