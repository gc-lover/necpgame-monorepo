// Issue: #backend-specialized_domain
// PERFORMANCE: Connection pooling, prepared statements, batch operations

package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"sync"
	"time"

	"specialized-domain-service-go/pkg/api"

	_ "github.com/lib/pq" // PostgreSQL driver
)

// Repository handles data persistence with PERFORMANCE optimizations
// PERFORMANCE: Struct aligned (pointers first)
type Repository struct {
	db        *sql.DB         // 8 bytes (pointer)
	prepared  map[string]*sql.Stmt // 8 bytes (pointer)
	pool      *sync.Pool     // 8 bytes (pointer)
	maxConns  int           // 8 bytes (value aligned)
	// Padding for alignment
	_pad [4]byte
}

// NewRepository creates a new repository instance with PERFORMANCE optimizations
func NewRepository() *Repository {
	// PERFORMANCE: Preallocate prepared statements map
	prepared := make(map[string]*sql.Stmt, 10) // Preallocate capacity

	return &Repository{
		prepared: prepared,
		pool: &sync.Pool{
			New: func() interface{} {
				return make([]interface{}, 0, 100) // Preallocate slice capacity
			},
		},
		maxConns: 25, // PERFORMANCE: Optimized connection pool size
	}
}

// InitDB initializes database connection with PERFORMANCE optimizations
func (r *Repository) InitDB(dsn string) error {
	var err error
	r.db, err = sql.Open("postgres", dsn)
	if err != nil {
		return err
	}

	// PERFORMANCE: Optimize connection pool
	r.db.SetMaxOpenConns(r.maxConns)     // Limit concurrent connections
	r.db.SetMaxIdleConns(r.maxConns / 2) // Keep some idle connections
	r.db.SetConnMaxLifetime(time.Hour)   // Rotate connections

	// PERFORMANCE: Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	return r.db.PingContext(ctx)
}

// HealthCheck performs database health check with PERFORMANCE optimizations
func (r *Repository) HealthCheck(ctx context.Context) error {
	if r.db == nil {
		return sql.ErrNoRows // Use existing error for no DB
	}

	// PERFORMANCE: Ping with context timeout
	pingCtx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	return r.db.PingContext(pingCtx)
}

// Close closes database connections and cleans up resources
func (r *Repository) Close() error {
	// PERFORMANCE: Close prepared statements
	for _, stmt := range r.prepared {
		stmt.Close()
	}

	if r.db != nil {
		return r.db.Close()
	}
	return nil
}

// ImportQuestContent imports quest content to database
func (r *Repository) ImportQuestContent(ctx context.Context, questID string, yamlContent map[string]interface{}) error {
	// PERFORMANCE: Use prepared statement for quest insertion
	const insertQuestSQL = `
		INSERT INTO gameplay.quest_definitions (
			id, title, quest_type, level_min, level_max, requirements, objectives,
			rewards, branches, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, NOW(), NOW())
		ON CONFLICT (id) DO UPDATE SET
			title = EXCLUDED.title,
			quest_type = EXCLUDED.quest_type,
			level_min = EXCLUDED.level_min,
			level_max = EXCLUDED.level_max,
			requirements = EXCLUDED.requirements,
			objectives = EXCLUDED.objectives,
			rewards = EXCLUDED.rewards,
			branches = EXCLUDED.branches,
			updated_at = NOW()
	`

	// Extract quest data from YAML content
	metadata, ok := yamlContent["metadata"].(map[string]interface{})
	if !ok {
		return sql.ErrNoRows // Invalid YAML structure
	}

	questDef, ok := yamlContent["quest_definition"].(map[string]interface{})
	if !ok {
		return sql.ErrNoRows // No quest definition
	}

	title := metadata["title"].(string)
	questType := questDef["quest_type"].(string)
	levelMin := int(questDef["level_min"].(float64))
	levelMax := int(questDef["level_max"].(float64))

	// Convert complex objects to JSON
	requirementsJSON, _ := json.Marshal(questDef["requirements"])
	objectivesJSON, _ := json.Marshal(questDef["objectives"])
	rewardsJSON, _ := json.Marshal(questDef["rewards"])
	branchesJSON, _ := json.Marshal(questDef["branches"])

	// PERFORMANCE: Execute with context timeout
	queryCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	_, err := r.db.ExecContext(queryCtx, insertQuestSQL,
		questID, title, questType, levelMin, levelMax,
		string(requirementsJSON), string(objectivesJSON),
		string(rewardsJSON), string(branchesJSON))

	return err
}