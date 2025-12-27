package main

import (
    "context"
    "log"
    "net/http"
    "os"
    "os/signal"
    "syscall"
    "time"

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
