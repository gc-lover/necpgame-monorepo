// Social Repository - Database access layer
// Issue: #140875791
// PERFORMANCE: Connection pooling, prepared statements, optimized queries

package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/lib/pq"

	"social-service-go/pkg/models"
)

// Repository provides database access for social systems
type Repository struct {
	db *sql.DB
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

// RELATIONSHIP METHODS

// GetRelationship retrieves a relationship between two entities
func (r *Repository) GetRelationship(ctx context.Context, sourceID, targetID uuid.UUID, sourceType, targetType models.EntityType) (*models.Relationship, error) {
	query := `
		SELECT id, source_id, source_type, target_id, target_type, level, trust, reputation,
		       last_interaction, interaction_count, created_at, updated_at
		FROM social.relationships
		WHERE source_id = $1 AND target_id = $2 AND source_type = $3 AND target_type = $4
	`

	var rel models.Relationship
	var lastInteraction time.Time

	err := r.db.QueryRowContext(ctx, query, sourceID, targetID, sourceType, targetType).Scan(
		&rel.ID, &rel.SourceID, &rel.SourceType, &rel.TargetID, &rel.TargetType,
		&rel.Level, &rel.Trust, &rel.Reputation, &lastInteraction, &rel.InteractionCount,
		&rel.CreatedAt, &rel.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("relationship not found")
		}
		return nil, fmt.Errorf("failed to get relationship: %w", err)
	}

	rel.LastInteraction = lastInteraction
	return &rel, nil
}

// CreateOrUpdateRelationship creates or updates a relationship
func (r *Repository) CreateOrUpdateRelationship(ctx context.Context, rel *models.Relationship) error {
	query := `
		INSERT INTO social.relationships (
			id, source_id, source_type, target_id, target_type, level, trust, reputation,
			last_interaction, interaction_count, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
		ON CONFLICT (source_id, target_id, source_type, target_type)
		DO UPDATE SET
			level = EXCLUDED.level,
			trust = EXCLUDED.trust,
			reputation = EXCLUDED.reputation,
			last_interaction = EXCLUDED.last_interaction,
			interaction_count = social.relationships.interaction_count + 1,
			updated_at = EXCLUDED.updated_at
	`

	now := time.Now()
	if rel.ID == uuid.Nil {
		rel.ID = uuid.New()
	}
	if rel.CreatedAt.IsZero() {
		rel.CreatedAt = now
	}
	rel.UpdatedAt = now

	_, err := r.db.ExecContext(ctx, query,
		rel.ID, rel.SourceID, rel.SourceType, rel.TargetID, rel.TargetType,
		rel.Level, rel.Trust, rel.Reputation, rel.LastInteraction, rel.InteractionCount,
		rel.CreatedAt, rel.UpdatedAt,
	)

	return err
}

// GetSocialNetwork retrieves the social network for an entity
func (r *Repository) GetSocialNetwork(ctx context.Context, entityID uuid.UUID, entityType models.EntityType) (*models.SocialNetwork, error) {
	query := `
		SELECT id, source_id, source_type, target_id, target_type, level, trust, reputation,
		       last_interaction, interaction_count
		FROM social.relationships
		WHERE (source_id = $1 AND source_type = $2) OR (target_id = $1 AND target_type = $2)
	`

	rows, err := r.db.QueryContext(ctx, query, entityID, entityType)
	if err != nil {
		return nil, fmt.Errorf("failed to query social network: %w", err)
	}
	defer rows.Close()

	var relationships []models.Relationship
	for rows.Next() {
		var rel models.Relationship
		var lastInteraction time.Time

		err := rows.Scan(
			&rel.ID, &rel.SourceID, &rel.SourceType, &rel.TargetID, &rel.TargetType,
			&rel.Level, &rel.Trust, &rel.Reputation, &lastInteraction, &rel.InteractionCount,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan relationship: %w", err)
		}

		rel.LastInteraction = lastInteraction
		relationships = append(relationships, rel)
	}

	// Calculate network statistics
	stats := r.calculateNetworkStats(relationships, entityID, entityType)

	return &models.SocialNetwork{
		EntityID:       entityID,
		EntityType:     entityType,
		Relationships:  relationships,
		NetworkStats:   *stats,
		LastCalculated: time.Now(),
	}, nil
}

// calculateNetworkStats calculates statistics for a social network
func (r *Repository) calculateNetworkStats(relationships []models.Relationship, entityID uuid.UUID, entityType models.EntityType) *models.NetworkStats {
	stats := &models.NetworkStats{
		TotalRelationships: len(relationships),
	}

	totalTrust := 0.0
	trustedAllies := 0
	hostileEnemies := 0

	for _, rel := range relationships {
		totalTrust += rel.Trust

		if rel.Level >= models.RelationshipLevelTrusted {
			trustedAllies++
		}
		if rel.Level <= models.RelationshipLevelHostile {
			hostileEnemies++
		}
	}

	if len(relationships) > 0 {
		stats.AverageTrust = totalTrust / float64(len(relationships))
	}

	stats.TrustedAllies = trustedAllies
	stats.HostileEnemies = hostileEnemies
	stats.NetworkStrength = stats.AverageTrust * float64(trustedAllies) / float64(len(relationships)+1)

	return stats
}

// ORDER METHODS

// CreateOrder creates a new player order
func (r *Repository) CreateOrder(ctx context.Context, order *models.Order) error {
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Insert order
	orderQuery := `
		INSERT INTO social.orders (
			id, creator_id, title, description, order_type, status, reward,
			requirements, region_id, expires_at, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	order.ID = uuid.New()
	order.CreatedAt = now
	order.UpdatedAt = now
	order.Status = models.OrderStatusOpen

	_, err = tx.ExecContext(ctx, orderQuery,
		order.ID, order.CreatorID, order.Title, order.Description, order.OrderType,
		order.Status, order.Reward, order.Requirements, order.RegionID, order.ExpiresAt,
		order.CreatedAt, order.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create order: %w", err)
	}

	return tx.Commit()
}

// GetOrder retrieves an order by ID
func (r *Repository) GetOrder(ctx context.Context, orderID uuid.UUID) (*models.Order, error) {
	query := `
		SELECT id, creator_id, title, description, order_type, status, reward,
		       requirements, region_id, expires_at, accepted_by, completed_at,
		       created_at, updated_at
		FROM social.orders
		WHERE id = $1
	`

	var order models.Order
	var expiresAt, acceptedBy, completedAt sql.NullTime
	var acceptedByID sql.NullString

	err := r.db.QueryRowContext(ctx, query, orderID).Scan(
		&order.ID, &order.CreatorID, &order.Title, &order.Description, &order.OrderType,
		&order.Status, &order.Reward, &order.Requirements, &order.RegionID,
		&expiresAt, &acceptedByID, &completedAt, &order.CreatedAt, &order.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("order not found")
		}
		return nil, fmt.Errorf("failed to get order: %w", err)
	}

	if expiresAt.Valid {
		order.ExpiresAt = &expiresAt.Time
	}
	if acceptedByID.Valid {
		if id, err := uuid.Parse(acceptedByID.String); err == nil {
			order.AcceptedBy = &id
		}
	}
	if completedAt.Valid {
		order.CompletedAt = &completedAt.Time
	}

	return &order, nil
}

// GetOrderBoard retrieves orders for a region
func (r *Repository) GetOrderBoard(ctx context.Context, regionID string, limit, offset int) (*models.OrderBoard, error) {
	query := `
		SELECT id, creator_id, title, description, order_type, status, reward,
		       requirements, region_id, expires_at, created_at, updated_at
		FROM social.orders
		WHERE region_id = $1 AND status = $2 AND (expires_at IS NULL OR expires_at > NOW())
		ORDER BY created_at DESC
		LIMIT $3 OFFSET $4
	`

	rows, err := r.db.QueryContext(ctx, query, regionID, models.OrderStatusOpen, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to query orders: %w", err)
	}
	defer rows.Close()

	var orders []models.Order
	for rows.Next() {
		var order models.Order
		var expiresAt sql.NullTime

		err := rows.Scan(
			&order.ID, &order.CreatorID, &order.Title, &order.Description, &order.OrderType,
			&order.Status, &order.Reward, &order.Requirements, &order.RegionID,
			&expiresAt, &order.CreatedAt, &order.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan order: %w", err)
		}

		if expiresAt.Valid {
			order.ExpiresAt = &expiresAt.Time
		}

		orders = append(orders, order)
	}

	// Get total count
	countQuery := `
		SELECT COUNT(*) FROM social.orders
		WHERE region_id = $1 AND status = $2 AND (expires_at IS NULL OR expires_at > NOW())
	`

	var totalOrders int
	err = r.db.QueryRowContext(ctx, countQuery, regionID, models.OrderStatusOpen).Scan(&totalOrders)
	if err != nil {
		return nil, fmt.Errorf("failed to count orders: %w", err)
	}

	return &models.OrderBoard{
		RegionID:     regionID,
		Orders:       orders,
		TotalOrders:  totalOrders,
		ActiveOrders: len(orders),
		LastUpdated:  time.Now(),
	}, nil
}

// NPC HIRING METHODS

// GetAvailableNPCs retrieves NPCs available for hiring
func (r *Repository) GetAvailableNPCs(ctx context.Context, regionID string) ([]models.NPCAvailability, error) {
	query := `
		SELECT n.id, n.name, n.npc_type, n.reputation, n.last_active,
		       CASE WHEN h.id IS NULL THEN true ELSE false END as available
		FROM social.npcs n
		LEFT JOIN social.npc_hirings h ON n.id = h.npc_id AND h.status = 'active'
		WHERE n.region_id = $1 AND n.available_for_hire = true
	`

	rows, err := r.db.QueryContext(ctx, query, regionID)
	if err != nil {
		return nil, fmt.Errorf("failed to query available NPCs: %w", err)
	}
	defer rows.Close()

	var npcs []models.NPCAvailability
	for rows.Next() {
		var npc models.NPCAvailability

		err := rows.Scan(
			&npc.NPCID, &npc.NPCName, &npc.NPCType, &npc.Reputation, &npc.LastActive, &npc.Available,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan NPC: %w", err)
		}

		// Get skills and rates (simplified)
		npc.Skills = []models.NPCSkill{}
		npc.Rates = []models.RateCard{}

		npcs = append(npcs, npc)
	}

	return npcs, nil
}

// CreateNPCHiring creates a new NPC hiring contract
func (r *Repository) CreateNPCHiring(ctx context.Context, hiring *models.NPCHiring) error {
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	query := `
		INSERT INTO social.npc_hirings (
			id, npc_id, npc_name, npc_type, employer_id, service_type,
			contract_terms, status, hired_at, expires_at, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	hiring.ID = uuid.New()
	hiring.Status = models.HiringStatusActive
	hiring.HiredAt = now
	hiring.CreatedAt = now
	hiring.UpdatedAt = now
	hiring.LastActive = now

	_, err = tx.ExecContext(ctx, query,
		hiring.ID, hiring.NPCID, hiring.NPCName, hiring.NPCType, hiring.EmployerID,
		hiring.ServiceType, hiring.ContractTerms, hiring.Status, hiring.HiredAt,
		hiring.ExpiresAt, hiring.CreatedAt, hiring.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create NPC hiring: %w", err)
	}

	return tx.Commit()
}

// GetNPCHiring retrieves an NPC hiring contract
func (r *Repository) GetNPCHiring(ctx context.Context, hiringID uuid.UUID) (*models.NPCHiring, error) {
	query := `
		SELECT id, npc_id, npc_name, npc_type, employer_id, service_type,
		       contract_terms, status, hired_at, expires_at, last_active,
		       created_at, updated_at
		FROM social.npc_hirings
		WHERE id = $1
	`

	var hiring models.NPCHiring
	var expiresAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, hiringID).Scan(
		&hiring.ID, &hiring.NPCID, &hiring.NPCName, &hiring.NPCType, &hiring.EmployerID,
		&hiring.ServiceType, &hiring.ContractTerms, &hiring.Status, &hiring.HiredAt,
		&expiresAt, &hiring.LastActive, &hiring.CreatedAt, &hiring.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("NPC hiring not found")
		}
		return nil, fmt.Errorf("failed to get NPC hiring: %w", err)
	}

	if expiresAt.Valid {
		hiring.ExpiresAt = &expiresAt.Time
	}

	return &hiring, nil
}
