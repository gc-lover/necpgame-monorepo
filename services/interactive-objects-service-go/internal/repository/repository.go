package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/jackc/pgx/v4/pgxpool"
)

// Repository handles data persistence for interactive objects
type Repository struct {
	db  *pgxpool.Pool
	rdb *redis.Client
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool, rdb *redis.Client) *Repository {
	return &Repository{
		db:  db,
		rdb: rdb,
	}
}

// InteractiveObject represents an interactive object in the database
type InteractiveObject struct {
	ID         string     `json:"id" db:"id"`
	ObjectType string     `json:"object_type" db:"object_type"`
	Position   Position   `json:"position" db:"position"`
	ZoneType   string     `json:"zone_type" db:"zone_type"`
	ZoneID     string     `json:"zone_id" db:"zone_id"`
	Status     string     `json:"status" db:"status"`
	Data       ObjectData `json:"data" db:"data"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
	LastUsed   *time.Time `json:"last_used,omitempty" db:"last_used"`
}

// Position represents 3D coordinates
type Position struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// ObjectData contains object-specific data
type ObjectData struct {
	Charges       *int `json:"charges,omitempty"`
	SecurityLevel *int `json:"security_level,omitempty"`
	RewardPool    *int `json:"reward_pool,omitempty"`
}

// SaveObject saves an interactive object to the database
func (r *Repository) SaveObject(ctx context.Context, obj *InteractiveObject) error {
	query := `
		INSERT INTO interactive_objects (
			id, object_type, position, zone_type, zone_id, status, data, created_at, last_used
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (id) DO UPDATE SET
			position = EXCLUDED.position,
			status = EXCLUDED.status,
			data = EXCLUDED.data,
			last_used = EXCLUDED.last_used
	`

	positionJSON, err := json.Marshal(obj.Position)
	if err != nil {
		return fmt.Errorf("failed to marshal position: %w", err)
	}

	dataJSON, err := json.Marshal(obj.Data)
	if err != nil {
		return fmt.Errorf("failed to marshal data: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		obj.ID,
		obj.ObjectType,
		positionJSON,
		obj.ZoneType,
		obj.ZoneID,
		obj.Status,
		dataJSON,
		obj.CreatedAt,
		obj.LastUsed,
	)

	if err != nil {
		return fmt.Errorf("failed to save object: %w", err)
	}

	// Cache in Redis for real-time access
	return r.cacheObject(ctx, obj)
}

// GetObject retrieves an object from the database or cache
func (r *Repository) GetObject(ctx context.Context, objectID string) (*InteractiveObject, error) {
	// Try cache first
	if obj, err := r.getCachedObject(ctx, objectID); err == nil && obj != nil {
		return obj, nil
	}

	// Fallback to database
	query := `
		SELECT id, object_type, position, zone_type, zone_id, status, data, created_at, last_used
		FROM interactive_objects
		WHERE id = $1
	`

	var obj InteractiveObject
	var positionJSON, dataJSON []byte

	err := r.db.QueryRow(ctx, query, objectID).Scan(
		&obj.ID,
		&obj.ObjectType,
		&positionJSON,
		&dataJSON,
		&obj.ZoneType,
		&obj.ZoneID,
		&obj.Status,
		&obj.CreatedAt,
		&obj.LastUsed,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get object: %w", err)
	}

	if err := json.Unmarshal(positionJSON, &obj.Position); err != nil {
		return nil, fmt.Errorf("failed to unmarshal position: %w", err)
	}

	if err := json.Unmarshal(dataJSON, &obj.Data); err != nil {
		return nil, fmt.Errorf("failed to unmarshal data: %w", err)
	}

	// Cache the result
	r.cacheObject(ctx, &obj)

	return &obj, nil
}

// GetActiveObjects retrieves all active objects in a zone
func (r *Repository) GetActiveObjects(ctx context.Context, zoneID string) ([]*InteractiveObject, error) {
	query := `
		SELECT id, object_type, position, zone_type, zone_id, status, data, created_at, last_used
		FROM interactive_objects
		WHERE zone_id = $1 AND status = 'active'
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, zoneID)
	if err != nil {
		return nil, fmt.Errorf("failed to query active objects: %w", err)
	}
	defer rows.Close()

	var objects []*InteractiveObject
	for rows.Next() {
		var obj InteractiveObject
		var positionJSON, dataJSON []byte

		err := rows.Scan(
			&obj.ID,
			&obj.ObjectType,
			&positionJSON,
			&dataJSON,
			&obj.ZoneType,
			&obj.ZoneID,
			&obj.Status,
			&obj.CreatedAt,
			&obj.LastUsed,
		)

		if err != nil {
			return nil, fmt.Errorf("failed to scan object: %w", err)
		}

		if err := json.Unmarshal(positionJSON, &obj.Position); err != nil {
			return nil, fmt.Errorf("failed to unmarshal position: %w", err)
		}

		if err := json.Unmarshal(dataJSON, &obj.Data); err != nil {
			return nil, fmt.Errorf("failed to unmarshal data: %w", err)
		}

		objects = append(objects, &obj)
	}

	return objects, rows.Err()
}

// UpdateObjectStatus updates object status and last used time
func (r *Repository) UpdateObjectStatus(ctx context.Context, objectID, status string) error {
	now := time.Now()
	query := `
		UPDATE interactive_objects
		SET status = $1, last_used = $2
		WHERE id = $3
	`

	_, err := r.db.Exec(ctx, query, status, now, objectID)
	if err != nil {
		return fmt.Errorf("failed to update object status: %w", err)
	}

	// Update cache
	obj, err := r.GetObject(ctx, objectID)
	if err != nil {
		return err
	}
	obj.Status = status
	obj.LastUsed = &now

	return r.cacheObject(ctx, obj)
}

// cacheObject stores object in Redis cache
func (r *Repository) cacheObject(ctx context.Context, obj *InteractiveObject) error {
	data, err := json.Marshal(obj)
	if err != nil {
		return fmt.Errorf("failed to marshal object for cache: %w", err)
	}

	key := fmt.Sprintf("object:%s", obj.ID)
	return r.rdb.Set(ctx, key, data, 30*time.Minute).Err()
}

// getCachedObject retrieves object from Redis cache
func (r *Repository) getCachedObject(ctx context.Context, objectID string) (*InteractiveObject, error) {
	key := fmt.Sprintf("object:%s", objectID)
	data, err := r.rdb.Get(ctx, key).Result()
	if err != nil {
		return nil, err
	}

	var obj InteractiveObject
	if err := json.Unmarshal([]byte(data), &obj); err != nil {
		return nil, err
	}

	return &obj, nil
}

// Issue: #1861
