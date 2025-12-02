// Issue: #142
package main

import (
	"database/sql"
	"log"
	"os"

	_ "github.com/lib/pq"
	"github.com/gc-lover/necpgame/services/loot-service-go/server"
)

func main() {
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable"
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	repository := server.NewPostgresRepository(db)
	service := server.NewLootService(repository)

	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8095"
	}

	httpServer := server.NewHTTPServer(addr, service)

	log.Printf("Starting Loot Service on %s", addr)
	if err := httpServer.Start(); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}

