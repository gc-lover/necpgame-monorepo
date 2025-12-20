package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type EngramCyberpsychosisRisk struct {
	ID               uuid.UUID     `json:"id"`
	CharacterID      uuid.UUID     `json:"character_id"`
	BaseRisk         float64       `json:"base_risk"`
	EngramRisk       float64       `json:"engram_risk"`
	TotalRisk        float64       `json:"total_risk"`
	BlockerReduction float64       `json:"blocker_reduction"`
	RiskFactors      []*RiskFactor `json:"risk_factors,omitempty"`
	LastUpdated      time.Time     `json:"last_updated"`
	CreatedAt        time.Time     `json:"created_at"`
}

type RiskFactor struct {
	FactorType string     `json:"factor_type"`
	RiskAmount float64    `json:"risk_amount"`
	EngramID   *uuid.UUID `json:"engram_id,omitempty"`
}

type EngramBlocker struct {
	ID                 uuid.UUID          `json:"id"`
	BlockerID          uuid.UUID          `json:"blocker_id"`
	CharacterID        uuid.UUID          `json:"character_id"`
	Tier               int                `json:"tier"`
	RiskReduction      float64            `json:"risk_reduction"`
	InfluenceReduction float64            `json:"influence_reduction"`
	DurationDays       int                `json:"duration_days"`
	Buffs              map[string]float64 `json:"buffs,omitempty"`
	Debuffs            map[string]float64 `json:"debuffs,omitempty"`
	InstalledAt        time.Time          `json:"installed_at"`
	ExpiresAt          time.Time          `json:"expires_at"`
	IsActive           bool               `json:"is_active"`
	CreatedAt          time.Time          `json:"created_at"`
	UpdatedAt          time.Time          `json:"updated_at"`
}

type EngramCyberpsychosisRepositoryInterface interface {
	GetCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*EngramCyberpsychosisRisk, error)
	CreateOrUpdateCyberpsychosisRisk(ctx context.Context, risk *EngramCyberpsychosisRisk) error
	GetBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlocker, error)
	GetActiveBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlocker, error)
	InstallBlocker(ctx context.Context, blocker *EngramBlocker) error
	DeactivateExpiredBlockers(ctx context.Context) error
}

type EngramCyberpsychosisRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramCyberpsychosisRepository(db *pgxpool.Pool) *EngramCyberpsychosisRepository {
	return &EngramCyberpsychosisRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramCyberpsychosisRepository) GetCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*EngramCyberpsychosisRisk, error) {
	var risk EngramCyberpsychosisRisk
	var riskFactorsJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, base_risk, engram_risk, total_risk, blocker_reduction,
		 risk_factors, last_updated, created_at
		 FROM character.engram_cyberpsychosis_risk
		 WHERE character_id = $1`,
		characterID,
	).Scan(
		&risk.ID, &risk.CharacterID, &risk.BaseRisk, &risk.EngramRisk,
		&risk.TotalRisk, &risk.BlockerReduction, &riskFactorsJSON,
		&risk.LastUpdated, &risk.CreatedAt,
	)

	if errors.Is(err, pgx.ErrNoRows) {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get cyberpsychosis risk")
		return nil, err
	}

	if len(riskFactorsJSON) > 0 {
		json.Unmarshal(riskFactorsJSON, &risk.RiskFactors)
	}

	return &risk, nil
}

func (r *EngramCyberpsychosisRepository) CreateOrUpdateCyberpsychosisRisk(ctx context.Context, risk *EngramCyberpsychosisRisk) error {
	risk.LastUpdated = time.Now()
	if risk.ID == uuid.Nil {
		risk.ID = uuid.New()
		risk.CreatedAt = time.Now()
	}

	riskFactorsJSON, _ := json.Marshal(risk.RiskFactors)

	_, err := r.db.Exec(ctx,
		`INSERT INTO character.engram_cyberpsychosis_risk 
		 (id, character_id, base_risk, engram_risk, total_risk, blocker_reduction,
		  risk_factors, last_updated, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		 ON CONFLICT (character_id) 
		 DO UPDATE SET 
		 base_risk = EXCLUDED.base_risk,
		 engram_risk = EXCLUDED.engram_risk,
		 total_risk = EXCLUDED.total_risk,
		 blocker_reduction = EXCLUDED.blocker_reduction,
		 risk_factors = EXCLUDED.risk_factors,
		 last_updated = EXCLUDED.last_updated`,
		risk.ID, risk.CharacterID, risk.BaseRisk, risk.EngramRisk,
		risk.TotalRisk, risk.BlockerReduction, riskFactorsJSON,
		risk.LastUpdated, risk.CreatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create or update cyberpsychosis risk")
		return err
	}

	return nil
}

func (r *EngramCyberpsychosisRepository) GetBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlocker, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, blocker_id, character_id, tier, risk_reduction, influence_reduction,
		 duration_days, buffs, debuffs, installed_at, expires_at, is_active, created_at, updated_at
		 FROM character.engram_blockers
		 WHERE character_id = $1
		 ORDER BY installed_at DESC`,
		characterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get engram blockers")
		return nil, err
	}
	defer rows.Close()

	var blockers []*EngramBlocker
	for rows.Next() {
		blocker := &EngramBlocker{}
		var buffsJSON []byte
		var debuffsJSON []byte

		err := rows.Scan(
			&blocker.ID, &blocker.BlockerID, &blocker.CharacterID, &blocker.Tier,
			&blocker.RiskReduction, &blocker.InfluenceReduction, &blocker.DurationDays,
			&buffsJSON, &debuffsJSON, &blocker.InstalledAt, &blocker.ExpiresAt,
			&blocker.IsActive, &blocker.CreatedAt, &blocker.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan engram blocker")
			continue
		}

		if len(buffsJSON) > 0 {
			json.Unmarshal(buffsJSON, &blocker.Buffs)
		}
		if len(debuffsJSON) > 0 {
			json.Unmarshal(debuffsJSON, &blocker.Debuffs)
		}

		blockers = append(blockers, blocker)
	}

	return blockers, nil
}

func (r *EngramCyberpsychosisRepository) GetActiveBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlocker, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, blocker_id, character_id, tier, risk_reduction, influence_reduction,
		 duration_days, buffs, debuffs, installed_at, expires_at, is_active, created_at, updated_at
		 FROM character.engram_blockers
		 WHERE character_id = $1 AND is_active = true AND expires_at > NOW()
		 ORDER BY installed_at DESC`,
		characterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get active engram blockers")
		return nil, err
	}
	defer rows.Close()

	var blockers []*EngramBlocker
	for rows.Next() {
		blocker := &EngramBlocker{}
		var buffsJSON []byte
		var debuffsJSON []byte

		err := rows.Scan(
			&blocker.ID, &blocker.BlockerID, &blocker.CharacterID, &blocker.Tier,
			&blocker.RiskReduction, &blocker.InfluenceReduction, &blocker.DurationDays,
			&buffsJSON, &debuffsJSON, &blocker.InstalledAt, &blocker.ExpiresAt,
			&blocker.IsActive, &blocker.CreatedAt, &blocker.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan engram blocker")
			continue
		}

		if len(buffsJSON) > 0 {
			json.Unmarshal(buffsJSON, &blocker.Buffs)
		}
		if len(debuffsJSON) > 0 {
			json.Unmarshal(debuffsJSON, &blocker.Debuffs)
		}

		blockers = append(blockers, blocker)
	}

	return blockers, nil
}

func (r *EngramCyberpsychosisRepository) InstallBlocker(ctx context.Context, blocker *EngramBlocker) error {
	blocker.ID = uuid.New()
	if blocker.BlockerID == uuid.Nil {
		blocker.BlockerID = uuid.New()
	}
	blocker.InstalledAt = time.Now()
	blocker.CreatedAt = time.Now()
	blocker.UpdatedAt = time.Now()

	buffsJSON, _ := json.Marshal(blocker.Buffs)
	debuffsJSON, _ := json.Marshal(blocker.Debuffs)

	_, err := r.db.Exec(ctx,
		`INSERT INTO character.engram_blockers 
		 (id, blocker_id, character_id, tier, risk_reduction, influence_reduction,
		  duration_days, buffs, debuffs, installed_at, expires_at, is_active, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)`,
		blocker.ID, blocker.BlockerID, blocker.CharacterID, blocker.Tier,
		blocker.RiskReduction, blocker.InfluenceReduction, blocker.DurationDays,
		buffsJSON, debuffsJSON, blocker.InstalledAt, blocker.ExpiresAt,
		blocker.IsActive, blocker.CreatedAt, blocker.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to install engram blocker")
		return err
	}

	return nil
}

func (r *EngramCyberpsychosisRepository) DeactivateExpiredBlockers(ctx context.Context) error {
	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_blockers
		 SET is_active = false, updated_at = NOW()
		 WHERE expires_at <= NOW() AND is_active = true`,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to deactivate expired blockers")
		return err
	}

	return nil
}
