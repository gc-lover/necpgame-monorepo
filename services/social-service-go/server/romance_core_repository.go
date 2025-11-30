// Issue: #140876112, #141888033
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type RomanceRelationship struct {
	RelationshipID    uuid.UUID              `json:"relationship_id" db:"relationship_id"`
	RomanceType       string                 `json:"romance_type" db:"romance_type"`
	PlayerID          uuid.UUID              `json:"player_id" db:"player_id"`
	TargetID          uuid.UUID              `json:"target_id" db:"target_id"`
	RelationshipScore int                    `json:"relationship_score" db:"relationship_score"`
	ChemistryScore    int                    `json:"chemistry_score" db:"chemistry_score"`
	TrustScore        int                    `json:"trust_score" db:"trust_score"`
	PhysicalIntimacy  int                    `json:"physical_intimacy" db:"physical_intimacy"`
	EmotionalIntimacy int                    `json:"emotional_intimacy" db:"emotional_intimacy"`
	RelationshipStage string                 `json:"relationship_stage" db:"relationship_stage"`
	IsActive          bool                   `json:"is_active" db:"is_active"`
	IsRomantic        bool                   `json:"is_romantic" db:"is_romantic"`
	IsPublic          bool                   `json:"is_public" db:"is_public"`
	ConsentStatus     string                 `json:"consent_status" db:"consent_status"`
	RelationshipHealth int                   `json:"relationship_health" db:"relationship_health"`
	Flags             []string               `json:"flags" db:"flags"`
	Metadata          map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt         time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time              `json:"updated_at" db:"updated_at"`
}

type RomanceCoreRepository interface {
	GetRomanceTypes(ctx context.Context) ([]string, error)
	GetPlayerRomanceRelationships(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceRelationship, int, error)
	GetPlayerRomanceRelationshipsByType(ctx context.Context, playerID uuid.UUID, romanceType string, limit, offset int) ([]RomanceRelationship, int, error)
	GetPlayerPlayerRomance(ctx context.Context, playerID1, playerID2 uuid.UUID) (*RomanceRelationship, error)
	CreatePlayerPlayerRomance(ctx context.Context, playerID, targetPlayerID uuid.UUID, message string, privacySettings map[string]interface{}) (*RomanceRelationship, error)
	AcceptPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) (*RomanceRelationship, error)
	RejectPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error
	BreakupPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error
	GetRomanceCompatibility(ctx context.Context, playerID, targetID uuid.UUID) (*CompatibilityResult, error)
	UpdateRomancePrivacy(ctx context.Context, playerID uuid.UUID, romanceType string, settings map[string]interface{}) error
	GetRomanceNotifications(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceNotification, int, error)
}

type CompatibilityResult struct {
	CompatibilityScore int                    `json:"compatibility_score"`
	ChemistryScore     int                    `json:"chemistry_score"`
	Factors            map[string]interface{} `json:"factors"`
}

type RomanceNotification struct {
	ID             uuid.UUID              `json:"id" db:"id"`
	PlayerID       uuid.UUID              `json:"player_id" db:"player_id"`
	NotificationType string               `json:"notification_type" db:"notification_type"`
	RelationshipID *uuid.UUID             `json:"relationship_id,omitempty" db:"relationship_id"`
	Message        string                 `json:"message" db:"message"`
	IsRead         bool                   `json:"is_read" db:"is_read"`
	Metadata       map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt      time.Time              `json:"created_at" db:"created_at"`
}

type romanceCoreRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewRomanceCoreRepository(db *pgxpool.Pool) RomanceCoreRepository {
	return &romanceCoreRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *romanceCoreRepository) GetRomanceTypes(ctx context.Context) ([]string, error) {
	return []string{"player_player", "player_npc", "player_digital_avatar"}, nil
}

func (r *romanceCoreRepository) GetPlayerRomanceRelationships(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceRelationship, int, error) {
	countQuery := `
		SELECT COUNT(*) FROM social.romance_relationships
		WHERE player_id = $1
	`
	var total int
	err := r.db.QueryRow(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	query := `
		SELECT relationship_id, romance_type, player_id, target_id, relationship_score,
			chemistry_score, trust_score, physical_intimacy, emotional_intimacy,
			relationship_stage, is_active, is_romantic, is_public, consent_status,
			relationship_health, flags, metadata, created_at, updated_at
		FROM social.romance_relationships
		WHERE player_id = $1
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var relationships []RomanceRelationship
	for rows.Next() {
		var rel RomanceRelationship
		var flagsJSON []byte
		var metadataJSON []byte

		err := rows.Scan(
			&rel.RelationshipID, &rel.RomanceType, &rel.PlayerID, &rel.TargetID,
			&rel.RelationshipScore, &rel.ChemistryScore, &rel.TrustScore,
			&rel.PhysicalIntimacy, &rel.EmotionalIntimacy, &rel.RelationshipStage,
			&rel.IsActive, &rel.IsRomantic, &rel.IsPublic, &rel.ConsentStatus,
			&rel.RelationshipHealth, &flagsJSON, &metadataJSON,
			&rel.CreatedAt, &rel.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if len(flagsJSON) > 0 {
			if err := json.Unmarshal(flagsJSON, &rel.Flags); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal flags JSON")
				rel.Flags = []string{}
			}
		}
		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &rel.Metadata); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
				rel.Metadata = make(map[string]interface{})
			}
		}

		relationships = append(relationships, rel)
	}

	return relationships, total, nil
}

func (r *romanceCoreRepository) GetPlayerRomanceRelationshipsByType(ctx context.Context, playerID uuid.UUID, romanceType string, limit, offset int) ([]RomanceRelationship, int, error) {
	countQuery := `
		SELECT COUNT(*) FROM social.romance_relationships
		WHERE player_id = $1 AND romance_type = $2
	`
	var total int
	err := r.db.QueryRow(ctx, countQuery, playerID, romanceType).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	query := `
		SELECT relationship_id, romance_type, player_id, target_id, relationship_score,
			chemistry_score, trust_score, physical_intimacy, emotional_intimacy,
			relationship_stage, is_active, is_romantic, is_public, consent_status,
			relationship_health, flags, metadata, created_at, updated_at
		FROM social.romance_relationships
		WHERE player_id = $1 AND romance_type = $2
		ORDER BY updated_at DESC
		LIMIT $3 OFFSET $4
	`

	rows, err := r.db.Query(ctx, query, playerID, romanceType, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var relationships []RomanceRelationship
	for rows.Next() {
		var rel RomanceRelationship
		var flagsJSON []byte
		var metadataJSON []byte

		err := rows.Scan(
			&rel.RelationshipID, &rel.RomanceType, &rel.PlayerID, &rel.TargetID,
			&rel.RelationshipScore, &rel.ChemistryScore, &rel.TrustScore,
			&rel.PhysicalIntimacy, &rel.EmotionalIntimacy, &rel.RelationshipStage,
			&rel.IsActive, &rel.IsRomantic, &rel.IsPublic, &rel.ConsentStatus,
			&rel.RelationshipHealth, &flagsJSON, &metadataJSON,
			&rel.CreatedAt, &rel.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if len(flagsJSON) > 0 {
			if err := json.Unmarshal(flagsJSON, &rel.Flags); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal flags JSON")
				rel.Flags = []string{}
			}
		}
		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &rel.Metadata); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
				rel.Metadata = make(map[string]interface{})
			}
		}

		relationships = append(relationships, rel)
	}

	return relationships, total, nil
}

func (r *romanceCoreRepository) GetPlayerPlayerRomance(ctx context.Context, playerID1, playerID2 uuid.UUID) (*RomanceRelationship, error) {
	query := `
		SELECT relationship_id, romance_type, player_id, target_id, relationship_score,
			chemistry_score, trust_score, physical_intimacy, emotional_intimacy,
			relationship_stage, is_active, is_romantic, is_public, consent_status,
			relationship_health, flags, metadata, created_at, updated_at
		FROM social.romance_relationships
		WHERE romance_type = 'player_player'
			AND ((player_id = $1 AND target_id = $2) OR (player_id = $2 AND target_id = $1))
		LIMIT 1
	`

	var rel RomanceRelationship
	var flagsJSON []byte
	var metadataJSON []byte

	err := r.db.QueryRow(ctx, query, playerID1, playerID2).Scan(
		&rel.RelationshipID, &rel.RomanceType, &rel.PlayerID, &rel.TargetID,
		&rel.RelationshipScore, &rel.ChemistryScore, &rel.TrustScore,
		&rel.PhysicalIntimacy, &rel.EmotionalIntimacy, &rel.RelationshipStage,
		&rel.IsActive, &rel.IsRomantic, &rel.IsPublic, &rel.ConsentStatus,
		&rel.RelationshipHealth, &flagsJSON, &metadataJSON,
		&rel.CreatedAt, &rel.UpdatedAt,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(flagsJSON) > 0 {
		if err := json.Unmarshal(flagsJSON, &rel.Flags); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal flags JSON")
			rel.Flags = []string{}
		}
	}
	if len(metadataJSON) > 0 {
		if err := json.Unmarshal(metadataJSON, &rel.Metadata); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
			rel.Metadata = make(map[string]interface{})
		}
	}

	return &rel, nil
}

func (r *romanceCoreRepository) CreatePlayerPlayerRomance(ctx context.Context, playerID, targetPlayerID uuid.UUID, message string, privacySettings map[string]interface{}) (*RomanceRelationship, error) {
	rel := &RomanceRelationship{
		RelationshipID:    uuid.New(),
		RomanceType:       "player_player",
		PlayerID:          playerID,
		TargetID:          targetPlayerID,
		RelationshipScore: 0,
		ChemistryScore:    0,
		TrustScore:        0,
		PhysicalIntimacy:  0,
		EmotionalIntimacy: 0,
		RelationshipStage: "stranger",
		IsActive:          true,
		IsRomantic:        false,
		IsPublic:          false,
		ConsentStatus:     "pending",
		RelationshipHealth: 100,
		Flags:             []string{},
		Metadata:          map[string]interface{}{"initiation_message": message},
		CreatedAt:         time.Now(),
		UpdatedAt:         time.Now(),
	}

	flagsJSON, _ := json.Marshal(rel.Flags)
	metadataJSON, _ := json.Marshal(rel.Metadata)

	query := `
		INSERT INTO social.romance_relationships (
			relationship_id, romance_type, player_id, target_id, relationship_score,
			chemistry_score, trust_score, physical_intimacy, emotional_intimacy,
			relationship_stage, is_active, is_romantic, is_public, consent_status,
			relationship_health, flags, metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19)
		RETURNING relationship_id, created_at, updated_at
	`

	err := r.db.QueryRow(ctx, query,
		rel.RelationshipID, rel.RomanceType, rel.PlayerID, rel.TargetID,
		rel.RelationshipScore, rel.ChemistryScore, rel.TrustScore,
		rel.PhysicalIntimacy, rel.EmotionalIntimacy, rel.RelationshipStage,
		rel.IsActive, rel.IsRomantic, rel.IsPublic, rel.ConsentStatus,
		rel.RelationshipHealth, flagsJSON, metadataJSON,
		rel.CreatedAt, rel.UpdatedAt,
	).Scan(&rel.RelationshipID, &rel.CreatedAt, &rel.UpdatedAt)

	if err != nil {
		return nil, err
	}

	return rel, nil
}

func (r *romanceCoreRepository) AcceptPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) (*RomanceRelationship, error) {
	query := `
		UPDATE social.romance_relationships
		SET consent_status = 'accepted',
			is_romantic = TRUE,
			relationship_stage = 'dating',
			updated_at = CURRENT_TIMESTAMP
		WHERE relationship_id = $1 AND (player_id = $2 OR target_id = $2)
		RETURNING relationship_id, romance_type, player_id, target_id, relationship_score,
			chemistry_score, trust_score, physical_intimacy, emotional_intimacy,
			relationship_stage, is_active, is_romantic, is_public, consent_status,
			relationship_health, flags, metadata, created_at, updated_at
	`

	var rel RomanceRelationship
	var flagsJSON []byte
	var metadataJSON []byte

	err := r.db.QueryRow(ctx, query, relationshipID, playerID).Scan(
		&rel.RelationshipID, &rel.RomanceType, &rel.PlayerID, &rel.TargetID,
		&rel.RelationshipScore, &rel.ChemistryScore, &rel.TrustScore,
		&rel.PhysicalIntimacy, &rel.EmotionalIntimacy, &rel.RelationshipStage,
		&rel.IsActive, &rel.IsRomantic, &rel.IsPublic, &rel.ConsentStatus,
		&rel.RelationshipHealth, &flagsJSON, &metadataJSON,
		&rel.CreatedAt, &rel.UpdatedAt,
	)
	if err != nil {
		return nil, err
	}

	if len(flagsJSON) > 0 {
		if err := json.Unmarshal(flagsJSON, &rel.Flags); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal flags JSON")
			rel.Flags = []string{}
		}
	}
	if len(metadataJSON) > 0 {
		if err := json.Unmarshal(metadataJSON, &rel.Metadata); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
			rel.Metadata = make(map[string]interface{})
		}
	}

	return &rel, nil
}

func (r *romanceCoreRepository) RejectPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error {
	query := `
		UPDATE social.romance_relationships
		SET consent_status = 'rejected',
			is_active = FALSE,
			updated_at = CURRENT_TIMESTAMP
		WHERE relationship_id = $1 AND (player_id = $2 OR target_id = $2)
	`
	_, err := r.db.Exec(ctx, query, relationshipID, playerID)
	return err
}

func (r *romanceCoreRepository) BreakupPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error {
	query := `
		UPDATE social.romance_relationships
		SET is_active = FALSE,
			is_romantic = FALSE,
			relationship_stage = 'stranger',
			consent_status = 'revoked',
			updated_at = CURRENT_TIMESTAMP
		WHERE relationship_id = $1 AND (player_id = $2 OR target_id = $2)
	`
	_, err := r.db.Exec(ctx, query, relationshipID, playerID)
	return err
}

func (r *romanceCoreRepository) GetRomanceCompatibility(ctx context.Context, playerID, targetID uuid.UUID) (*CompatibilityResult, error) {
	// Simplified compatibility calculation
	// In real implementation, this would calculate based on player stats, preferences, etc.
	result := &CompatibilityResult{
		CompatibilityScore: 75,
		ChemistryScore:     70,
		Factors: map[string]interface{}{
			"personality_match": 80,
			"interests_match":    70,
			"values_match":       75,
		},
	}
	return result, nil
}

func (r *romanceCoreRepository) UpdateRomancePrivacy(ctx context.Context, playerID uuid.UUID, romanceType string, settings map[string]interface{}) error {
	settingsJSON, _ := json.Marshal(settings)

	query := `
		INSERT INTO social.romance_privacy_settings (
			player_id, romance_type, show_relationship_status, show_romance_events,
			allow_romance_requests, updated_at
		) VALUES ($1, $2, $3, $4, $5, CURRENT_TIMESTAMP)
		ON CONFLICT (player_id, romance_type)
		DO UPDATE SET
			show_relationship_status = $3,
			show_romance_events = $4,
			allow_romance_requests = $5,
			updated_at = CURRENT_TIMESTAMP
	`

	showStatus := true
	showEvents := true
	allowRequests := true

	if val, ok := settings["show_relationship_status"].(bool); ok {
		showStatus = val
	}
	if val, ok := settings["show_romance_events"].(bool); ok {
		showEvents = val
	}
	if val, ok := settings["allow_romance_requests"].(bool); ok {
		allowRequests = val
	}

	_, err := r.db.Exec(ctx, query, playerID, romanceType, showStatus, showEvents, allowRequests)
	_ = settingsJSON // TODO: store additional settings in metadata
	return err
}

func (r *romanceCoreRepository) GetRomanceNotifications(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceNotification, int, error) {
	countQuery := `
		SELECT COUNT(*) FROM social.romance_notifications
		WHERE player_id = $1
	`
	var total int
	err := r.db.QueryRow(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	query := `
		SELECT id, player_id, notification_type, relationship_id, message, is_read, metadata, created_at
		FROM social.romance_notifications
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var notifications []RomanceNotification
	for rows.Next() {
		var notif RomanceNotification
		var metadataJSON []byte

		err := rows.Scan(
			&notif.ID, &notif.PlayerID, &notif.NotificationType,
			&notif.RelationshipID, &notif.Message, &notif.IsRead,
			&metadataJSON, &notif.CreatedAt,
		)
		if err != nil {
			continue
		}

		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &notif.Metadata); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
				notif.Metadata = make(map[string]interface{})
			}
		}

		notifications = append(notifications, notif)
	}

	return notifications, total, nil
}

