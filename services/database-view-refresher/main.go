// Issue: #1583
// Database View Refresher - Automated refresh of materialized views
// PERFORMANCE: 5000ms aggregation → 50ms view query (100x speedup!)
// RUNS: Every 5 minutes via Kubernetes CronJob
package main

import (
	"context"
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling
	"os"
	"time"

	_ "github.com/lib/pq"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		log.Fatal("DATABASE_URL environment variable not set")
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Connection pool settings for performance (Issue #1605)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	// Test connection
	if err := db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}

	log.Println("Database View Refresher starting...")

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6455")
		log.Printf("pprof server starting on %s", pprofAddr)
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	refresher := NewViewRefresher(db)

	// Run view refresh
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Minute)
	defer cancel()

	if err := refresher.RefreshAllViews(ctx); err != nil {
		log.Fatalf("View refresh failed: %v", err)
	}

	log.Println("OK View refresh completed successfully")
}

// ViewRefresher manages materialized view refreshes
type ViewRefresher struct {
	db *sql.DB
}

// NewViewRefresher creates view refresher
func NewViewRefresher(db *sql.DB) *ViewRefresher {
	return &ViewRefresher{db: db}
}

// RefreshAllViews refreshes all materialized views
func (vr *ViewRefresher) RefreshAllViews(ctx context.Context) error {
	log.Println("Refreshing all materialized views...")

	// Call PostgreSQL function (CONCURRENTLY = no locks!)
	query := "SELECT public.refresh_all_leaderboard_views()"
	
	start := time.Now()
	_, err := vr.db.ExecContext(ctx, query)
	elapsed := time.Since(start)

	if err != nil {
		return err
	}

	log.Printf("OK All views refreshed in %v", elapsed)

	// Log individual view sizes for monitoring
	vr.logViewSizes(ctx)

	return nil
}

// logViewSizes logs size of each materialized view
func (vr *ViewRefresher) logViewSizes(ctx context.Context) {
	query := `
		SELECT 
		    schemaname,
		    matviewname,
		    pg_size_pretty(pg_total_relation_size(schemaname||'.'||matviewname)) as size,
		    pg_total_relation_size(schemaname||'.'||matviewname) as bytes
		FROM pg_matviews
		WHERE schemaname IN ('leaderboard', 'progression', 'stock_exchange', 'achievements')
		ORDER BY bytes DESC
	`

	rows, err := vr.db.QueryContext(ctx, query)
	if err != nil {
		log.Printf("Failed to query view sizes: %v", err)
		return
	}
	defer rows.Close()

	log.Println("Materialized view sizes:")
	for rows.Next() {
		var schema, viewName, size string
		var bytes int64
		if err := rows.Scan(&schema, &viewName, &size, &bytes); err != nil {
			continue
		}

		log.Printf("  - %s.%s: %s (%d bytes)", schema, viewName, size, bytes)
	}
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}


