//go:align 64
package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/faction-service-go/internal/models"
)

//go:align 64
type Repository interface {
	// Faction operations
	CreateFaction(ctx context.Context, faction *models.Faction) error
	GetFaction(ctx context.Context, factionID uuid.UUID) (*models.Faction, error)
	UpdateFaction(ctx context.Context, faction *models.Faction) error
	DeleteFaction(ctx context.Context, factionID uuid.UUID) error
	ListFactions(ctx context.Context, limit, offset int, filters map[string]interface{}) ([]*models.Faction, int, error)

	// Diplomacy operations
	CreateDiplomaticRelation(ctx context.Context, relation *models.DiplomaticRelation) error
	GetDiplomaticRelations(ctx context.Context, factionID uuid.UUID) ([]*models.DiplomaticRelation, error)
	UpdateDiplomaticRelation(ctx context.Context, relation *models.DiplomaticRelation) error
	CreateDiplomaticAction(ctx context.Context, action *models.DiplomaticAction) error
	GetDiplomaticAction(ctx context.Context, actionID uuid.UUID) (*models.DiplomaticAction, error)
	UpdateDiplomaticAction(ctx context.Context, actionID uuid.UUID, status string) error

	// Territory operations
	CreateTerritory(ctx context.Context, territory *models.Territory) error
	GetFactionTerritories(ctx context.Context, factionID uuid.UUID) ([]*models.Territory, error)
	CreateTerritoryClaim(ctx context.Context, claim *models.TerritoryClaim) error
	GetTerritoryClaim(ctx context.Context, claimID uuid.UUID) (*models.TerritoryClaim, error)
	UpdateTerritoryClaim(ctx context.Context, claimID uuid.UUID, status string) error

	// Reputation operations
	GetFactionReputation(ctx context.Context, factionID uuid.UUID) (int, error)
	UpdateFactionReputation(ctx context.Context, factionID uuid.UUID, reputation int) error
	LogReputationEvent(ctx context.Context, factionID uuid.UUID, event *models.ReputationEvent) error
	GetReputationHistory(ctx context.Context, factionID uuid.UUID, limit int) ([]*models.ReputationEvent, error)

	// Statistics operations
	UpdateFactionStatistics(ctx context.Context, factionID uuid.UUID, stats *models.FactionStatistics) error
	GetFactionRankings(ctx context.Context, category string, limit, offset int) ([]*models.Faction, error)

	// Health check
	GetSystemHealth(ctx context.Context) (*models.SystemHealth, error)
}

//go:align 64
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

//go:align 64
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) Repository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

//go:align 64
func (r *PostgresRepository) CreateFaction(ctx context.Context, faction *models.Faction) error {
	query := `
		INSERT INTO factions (
			faction_id, name, description, leader_id, reputation, influence,
			diplomatic_stance, member_count, max_members, activity_status,
			requirements, statistics, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
	`

	reqJSON, err := json.Marshal(faction.Requirements)
	if err != nil {
		return fmt.Errorf("failed to marshal requirements: %w", err)
	}

	statsJSON, err := json.Marshal(faction.Statistics)
	if err != nil {
		return fmt.Errorf("failed to marshal statistics: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		faction.FactionID, faction.Name, faction.Description, faction.LeaderID,
		faction.Reputation, faction.Influence, faction.DiplomaticStance,
		faction.MemberCount, faction.MaxMembers, faction.ActivityStatus,
		reqJSON, statsJSON, faction.CreatedAt, faction.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create faction", zap.Error(err), zap.String("faction_id", faction.FactionID.String()))
		return fmt.Errorf("failed to create faction: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetFaction(ctx context.Context, factionID uuid.UUID) (*models.Faction, error) {
	query := `
		SELECT faction_id, name, description, leader_id, reputation, influence,
			   diplomatic_stance, member_count, max_members, activity_status,
			   requirements, statistics, created_at, updated_at
		FROM factions WHERE faction_id = $1
	`

	var faction models.Faction
	var reqJSON, statsJSON []byte

	err := r.db.QueryRow(ctx, query, factionID).Scan(
		&faction.FactionID, &faction.Name, &faction.Description, &faction.LeaderID,
		&faction.Reputation, &faction.Influence, &faction.DiplomaticStance,
		&faction.MemberCount, &faction.MaxMembers, &faction.ActivityStatus,
		&reqJSON, &statsJSON, &faction.CreatedAt, &faction.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get faction", zap.Error(err), zap.String("faction_id", factionID.String()))
		return nil, fmt.Errorf("failed to get faction: %w", err)
	}

	if err := json.Unmarshal(reqJSON, &faction.Requirements); err != nil {
		return nil, fmt.Errorf("failed to unmarshal requirements: %w", err)
	}

	if err := json.Unmarshal(statsJSON, &faction.Statistics); err != nil {
		return nil, fmt.Errorf("failed to unmarshal statistics: %w", err)
	}

	return &faction, nil
}

//go:align 64
func (r *PostgresRepository) UpdateFaction(ctx context.Context, faction *models.Faction) error {
	query := `
		UPDATE factions SET
			name = $2, description = $3, reputation = $4, influence = $5,
			diplomatic_stance = $6, member_count = $7, max_members = $8,
			activity_status = $9, requirements = $10, statistics = $11, updated_at = $12
		WHERE faction_id = $1
	`

	reqJSON, err := json.Marshal(faction.Requirements)
	if err != nil {
		return fmt.Errorf("failed to marshal requirements: %w", err)
	}

	statsJSON, err := json.Marshal(faction.Statistics)
	if err != nil {
		return fmt.Errorf("failed to marshal statistics: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		faction.FactionID, faction.Name, faction.Description, faction.Reputation,
		faction.Influence, faction.DiplomaticStance, faction.MemberCount,
		faction.MaxMembers, faction.ActivityStatus, reqJSON, statsJSON, time.Now(),
	)

	if err != nil {
		r.logger.Error("Failed to update faction", zap.Error(err), zap.String("faction_id", faction.FactionID.String()))
		return fmt.Errorf("failed to update faction: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) DeleteFaction(ctx context.Context, factionID uuid.UUID) error {
	query := `DELETE FROM factions WHERE faction_id = $1`

	_, err := r.db.Exec(ctx, query, factionID)
	if err != nil {
		r.logger.Error("Failed to delete faction", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to delete faction: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) ListFactions(ctx context.Context, limit, offset int, filters map[string]interface{}) ([]*models.Faction, int, error) {
	query := `
		SELECT faction_id, name, description, leader_id, reputation, influence,
			   diplomatic_stance, member_count, max_members, activity_status,
			   requirements, statistics, created_at, updated_at
		FROM factions
		WHERE 1=1
	`

	args := []interface{}{}
	argCount := 0

	// Apply filters
	if name, ok := filters["name"].(string); ok && name != "" {
		argCount++
		query += fmt.Sprintf(" AND name ILIKE $%d", argCount)
		args = append(args, "%"+name+"%")
	}

	if minRep, ok := filters["reputation_min"].(int); ok {
		argCount++
		query += fmt.Sprintf(" AND reputation >= $%d", argCount)
		args = append(args, minRep)
	}

	if maxRep, ok := filters["reputation_max"].(int); ok {
		argCount++
		query += fmt.Sprintf(" AND reputation <= $%d", argCount)
		args = append(args, maxRep)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM (" + query + ") as count_query"
	var total int
	countArgs := make([]interface{}, len(args))
	copy(countArgs, args)

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Add ordering and pagination
	query += " ORDER BY reputation DESC, influence DESC"
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list factions: %w", err)
	}
	defer rows.Close()

	var factions []*models.Faction
	for rows.Next() {
		var faction models.Faction
		var reqJSON, statsJSON []byte

		err := rows.Scan(
			&faction.FactionID, &faction.Name, &faction.Description, &faction.LeaderID,
			&faction.Reputation, &faction.Influence, &faction.DiplomaticStance,
			&faction.MemberCount, &faction.MaxMembers, &faction.ActivityStatus,
			&reqJSON, &statsJSON, &faction.CreatedAt, &faction.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan faction: %w", err)
		}

		if err := json.Unmarshal(reqJSON, &faction.Requirements); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal requirements: %w", err)
		}

		if err := json.Unmarshal(statsJSON, &faction.Statistics); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal statistics: %w", err)
		}

		factions = append(factions, &faction)
	}

	return factions, total, nil
}

//go:align 64
func (r *PostgresRepository) CreateDiplomaticRelation(ctx context.Context, relation *models.DiplomaticRelation) error {
	query := `
		INSERT INTO diplomatic_relations (
			faction_id, target_faction_id, status, standing, established_at, last_action_at
		) VALUES ($1, $2, $3, $4, $5, $6)
	`

	_, err := r.db.Exec(ctx, query,
		relation.TargetFactionID, relation.TargetFactionID, relation.Status,
		relation.Standing, relation.EstablishedAt, relation.LastActionAt,
	)

	if err != nil {
		r.logger.Error("Failed to create diplomatic relation", zap.Error(err))
		return fmt.Errorf("failed to create diplomatic relation: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetDiplomaticRelations(ctx context.Context, factionID uuid.UUID) ([]*models.DiplomaticRelation, error) {
	query := `
		SELECT dr.target_faction_id, f.name, dr.status, dr.standing,
			   dr.established_at, dr.last_action_at
		FROM diplomatic_relations dr
		JOIN factions f ON dr.target_faction_id = f.faction_id
		WHERE dr.faction_id = $1
	`

	rows, err := r.db.Query(ctx, query, factionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get diplomatic relations: %w", err)
	}
	defer rows.Close()

	var relations []*models.DiplomaticRelation
	for rows.Next() {
		var relation models.DiplomaticRelation
		err := rows.Scan(
			&relation.TargetFactionID, &relation.TargetFactionName, &relation.Status,
			&relation.Standing, &relation.EstablishedAt, &relation.LastActionAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan diplomatic relation: %w", err)
		}
		relations = append(relations, &relation)
	}

	return relations, nil
}

//go:align 64
func (r *PostgresRepository) UpdateDiplomaticRelation(ctx context.Context, relation *models.DiplomaticRelation) error {
	query := `
		UPDATE diplomatic_relations SET
			status = $3, standing = $4, last_action_at = $5
		WHERE faction_id = $1 AND target_faction_id = $2
	`

	_, err := r.db.Exec(ctx, query,
		relation.TargetFactionID, relation.TargetFactionID, relation.Status,
		relation.Standing, time.Now(),
	)

	if err != nil {
		r.logger.Error("Failed to update diplomatic relation", zap.Error(err))
		return fmt.Errorf("failed to update diplomatic relation: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) CreateDiplomaticAction(ctx context.Context, action *models.DiplomaticAction) error {
	query := `
		INSERT INTO diplomatic_actions (
			action_id, faction_id, action_type, target_faction_id, status,
			message, treaty_terms, created_at, response_deadline
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		action.ActionID, action.FactionID, action.ActionType, action.TargetFactionID,
		action.Status, action.Message, action.TreatyTerms, action.CreatedAt, action.ResponseDeadline,
	)

	if err != nil {
		r.logger.Error("Failed to create diplomatic action", zap.Error(err))
		return fmt.Errorf("failed to create diplomatic action: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetDiplomaticAction(ctx context.Context, actionID uuid.UUID) (*models.DiplomaticAction, error) {
	query := `
		SELECT action_id, faction_id, action_type, target_faction_id, status,
			   message, treaty_terms, created_at, response_deadline
		FROM diplomatic_actions WHERE action_id = $1
	`

	var action models.DiplomaticAction
	err := r.db.QueryRow(ctx, query, actionID).Scan(
		&action.ActionID, &action.FactionID, &action.ActionType, &action.TargetFactionID,
		&action.Status, &action.Message, &action.TreatyTerms, &action.CreatedAt, &action.ResponseDeadline,
	)

	if err != nil {
		r.logger.Error("Failed to get diplomatic action", zap.Error(err), zap.String("action_id", actionID.String()))
		return nil, fmt.Errorf("failed to get diplomatic action: %w", err)
	}

	return &action, nil
}

//go:align 64
func (r *PostgresRepository) UpdateDiplomaticAction(ctx context.Context, actionID uuid.UUID, status string) error {
	query := `UPDATE diplomatic_actions SET status = $2 WHERE action_id = $1`

	_, err := r.db.Exec(ctx, query, actionID, status)
	if err != nil {
		r.logger.Error("Failed to update diplomatic action", zap.Error(err), zap.String("action_id", actionID.String()))
		return fmt.Errorf("failed to update diplomatic action: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) CreateTerritory(ctx context.Context, territory *models.Territory) error {
	query := `
		INSERT INTO territories (
			territory_id, name, boundaries, control_level, claimed_at, last_conflict_at
		) VALUES ($1, $2, $3, $4, $5, $6)
	`

	boundariesJSON, err := json.Marshal(territory.Boundaries)
	if err != nil {
		return fmt.Errorf("failed to marshal boundaries: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		territory.TerritoryID, territory.Name, boundariesJSON,
		territory.ControlLevel, territory.ClaimedAt, territory.LastConflictAt,
	)

	if err != nil {
		r.logger.Error("Failed to create territory", zap.Error(err), zap.String("territory_id", territory.TerritoryID.String()))
		return fmt.Errorf("failed to create territory: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetFactionTerritories(ctx context.Context, factionID uuid.UUID) ([]*models.Territory, error) {
	query := `
		SELECT t.territory_id, t.name, t.boundaries, t.control_level, t.claimed_at, t.last_conflict_at
		FROM territories t
		JOIN territory_claims tc ON t.territory_id = tc.territory_id
		WHERE tc.faction_id = $1 AND tc.status = 'approved'
	`

	rows, err := r.db.Query(ctx, query, factionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get faction territories: %w", err)
	}
	defer rows.Close()

	var territories []*models.Territory
	for rows.Next() {
		var territory models.Territory
		var boundariesJSON []byte

		err := rows.Scan(
			&territory.TerritoryID, &territory.Name, &boundariesJSON,
			&territory.ControlLevel, &territory.ClaimedAt, &territory.LastConflictAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan territory: %w", err)
		}

		if err := json.Unmarshal(boundariesJSON, &territory.Boundaries); err != nil {
			return nil, fmt.Errorf("failed to unmarshal boundaries: %w", err)
		}

		territories = append(territories, &territory)
	}

	return territories, nil
}

//go:align 64
func (r *PostgresRepository) CreateTerritoryClaim(ctx context.Context, claim *models.TerritoryClaim) error {
	query := `
		INSERT INTO territory_claims (
			claim_id, faction_id, center_x, center_y, radius, claim_type,
			status, justification, established_at, dispute_period
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.Exec(ctx, query,
		claim.ClaimID, claim.FactionID, claim.CenterX, claim.CenterY, claim.Radius,
		claim.ClaimType, claim.Status, claim.Justification, claim.EstablishedAt, claim.DisputePeriod,
	)

	if err != nil {
		r.logger.Error("Failed to create territory claim", zap.Error(err), zap.String("claim_id", claim.ClaimID.String()))
		return fmt.Errorf("failed to create territory claim: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetTerritoryClaim(ctx context.Context, claimID uuid.UUID) (*models.TerritoryClaim, error) {
	query := `
		SELECT claim_id, faction_id, center_x, center_y, radius, claim_type,
			   status, justification, established_at, dispute_period
		FROM territory_claims WHERE claim_id = $1
	`

	var claim models.TerritoryClaim
	err := r.db.QueryRow(ctx, query, claimID).Scan(
		&claim.ClaimID, &claim.FactionID, &claim.CenterX, &claim.CenterY, &claim.Radius,
		&claim.ClaimType, &claim.Status, &claim.Justification, &claim.EstablishedAt, &claim.DisputePeriod,
	)

	if err != nil {
		r.logger.Error("Failed to get territory claim", zap.Error(err), zap.String("claim_id", claimID.String()))
		return nil, fmt.Errorf("failed to get territory claim: %w", err)
	}

	return &claim, nil
}

//go:align 64
func (r *PostgresRepository) UpdateTerritoryClaim(ctx context.Context, claimID uuid.UUID, status string) error {
	query := `UPDATE territory_claims SET status = $2 WHERE claim_id = $1`

	_, err := r.db.Exec(ctx, query, claimID, status)
	if err != nil {
		r.logger.Error("Failed to update territory claim", zap.Error(err), zap.String("claim_id", claimID.String()))
		return fmt.Errorf("failed to update territory claim: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetFactionReputation(ctx context.Context, factionID uuid.UUID) (int, error) {
	query := `SELECT reputation FROM factions WHERE faction_id = $1`

	var reputation int
	err := r.db.QueryRow(ctx, query, factionID).Scan(&reputation)
	if err != nil {
		r.logger.Error("Failed to get faction reputation", zap.Error(err), zap.String("faction_id", factionID.String()))
		return 0, fmt.Errorf("failed to get faction reputation: %w", err)
	}

	return reputation, nil
}

//go:align 64
func (r *PostgresRepository) UpdateFactionReputation(ctx context.Context, factionID uuid.UUID, reputation int) error {
	query := `UPDATE factions SET reputation = $2, updated_at = $3 WHERE faction_id = $1`

	_, err := r.db.Exec(ctx, query, factionID, reputation, time.Now())
	if err != nil {
		r.logger.Error("Failed to update faction reputation", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to update faction reputation: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) LogReputationEvent(ctx context.Context, factionID uuid.UUID, event *models.ReputationEvent) error {
	query := `
		INSERT INTO reputation_events (
			faction_id, event_type, value_change, timestamp, description
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.Exec(ctx, query,
		factionID, event.EventType, event.ValueChange, event.Timestamp, event.Description,
	)

	if err != nil {
		r.logger.Error("Failed to log reputation event", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to log reputation event: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetReputationHistory(ctx context.Context, factionID uuid.UUID, limit int) ([]*models.ReputationEvent, error) {
	query := `
		SELECT event_type, value_change, timestamp, description
		FROM reputation_events
		WHERE faction_id = $1
		ORDER BY timestamp DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, factionID, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get reputation history: %w", err)
	}
	defer rows.Close()

	var events []*models.ReputationEvent
	for rows.Next() {
		var event models.ReputationEvent
		err := rows.Scan(&event.EventType, &event.ValueChange, &event.Timestamp, &event.Description)
		if err != nil {
			return nil, fmt.Errorf("failed to scan reputation event: %w", err)
		}
		events = append(events, &event)
	}

	return events, nil
}

//go:align 64
func (r *PostgresRepository) UpdateFactionStatistics(ctx context.Context, factionID uuid.UUID, stats *models.FactionStatistics) error {
	statsJSON, err := json.Marshal(stats)
	if err != nil {
		return fmt.Errorf("failed to marshal statistics: %w", err)
	}

	query := `UPDATE factions SET statistics = $2, updated_at = $3 WHERE faction_id = $1`

	_, err = r.db.Exec(ctx, query, factionID, statsJSON, time.Now())
	if err != nil {
		r.logger.Error("Failed to update faction statistics", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to update faction statistics: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetFactionRankings(ctx context.Context, category string, limit, offset int) ([]*models.Faction, error) {
	var orderBy string
	switch category {
	case "reputation":
		orderBy = "reputation DESC"
	case "influence":
		orderBy = "influence DESC"
	case "territory_control":
		orderBy = "(SELECT COUNT(*) FROM territory_claims tc WHERE tc.faction_id = f.faction_id AND tc.status = 'approved') DESC"
	default:
		orderBy = "reputation DESC"
	}

	query := fmt.Sprintf(`
		SELECT faction_id, name, reputation, influence, member_count, created_at
		FROM factions f
		ORDER BY %s
		LIMIT $1 OFFSET $2
	`, orderBy)

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get faction rankings: %w", err)
	}
	defer rows.Close()

	var factions []*models.Faction
	for rows.Next() {
		var faction models.Faction
		err := rows.Scan(
			&faction.FactionID, &faction.Name, &faction.Reputation,
			&faction.Influence, &faction.MemberCount, &faction.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan faction ranking: %w", err)
		}
		factions = append(factions, &faction)
	}

	return factions, nil
}

//go:align 64
func (r *PostgresRepository) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	query := `
		SELECT
			(SELECT COUNT(*) FROM factions) as total_factions,
			(SELECT COUNT(*) FROM factions WHERE activity_status = 'active') as active_factions,
			(SELECT COUNT(*) FROM diplomatic_relations) as total_diplomacy,
			(SELECT COUNT(*) FROM diplomatic_actions WHERE status = 'pending') as active_diplomacy
	`

	var health models.SystemHealth
	err := r.db.QueryRow(ctx, query).Scan(
		&health.TotalFactions, &health.ActiveFactions,
		&health.TotalDiplomacy, &health.ActiveDiplomacy,
	)

	if err != nil {
		r.logger.Error("Failed to get system health", zap.Error(err))
		return nil, fmt.Errorf("failed to get system health: %w", err)
	}

	// Set mechanics counts (this would be tracked separately in a real system)
	health.TotalMechanics = health.TotalFactions
	health.ActiveMechanics = health.ActiveFactions

	return &health, nil
}