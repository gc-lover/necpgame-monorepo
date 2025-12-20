package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type EngramProtection struct {
	ID                     uuid.UUID  `json:"id"`
	EngramID               uuid.UUID  `json:"engram_id"`
	ProtectionTier         int        `json:"protection_tier"`
	ProtectionTierName     string     `json:"protection_tier_name"`
	RequiredNetrunnerLevel int        `json:"required_netrunner_level"`
	CopyProtection         bool       `json:"copy_protection"`
	HackProtection         bool       `json:"hack_protection"`
	InstallProtection      bool       `json:"install_protection"`
	BoundCharacterID       *uuid.UUID `json:"bound_character_id,omitempty"`
	EncodedAt              time.Time  `json:"encoded_at"`
	EncodedBy              uuid.UUID  `json:"encoded_by"`
	CreatedAt              time.Time  `json:"created_at"`
	UpdatedAt              time.Time  `json:"updated_at"`
}

type EngramSecurityRepositoryInterface interface {
	GetProtection(ctx context.Context, engramID uuid.UUID) (*EngramProtection, error)
	CreateProtection(ctx context.Context, protection *EngramProtection) error
	UpdateProtection(ctx context.Context, protection *EngramProtection) error
}

type EngramSecurityRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramSecurityRepository(db *pgxpool.Pool) *EngramSecurityRepository {
	return &EngramSecurityRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramSecurityRepository) GetProtection(ctx context.Context, engramID uuid.UUID) (*EngramProtection, error) {
	var protection EngramProtection
	var boundCharacterID *uuid.UUID

	err := r.db.QueryRow(ctx,
		`SELECT id, engram_id, protection_tier, protection_tier_name, required_netrunner_level,
		 copy_protection, hack_protection, install_protection, bound_character_id,
		 encoded_at, encoded_by, created_at, updated_at
		 FROM character.engram_protection
		 WHERE engram_id = $1`,
		engramID,
	).Scan(
		&protection.ID, &protection.EngramID, &protection.ProtectionTier, &protection.ProtectionTierName,
		&protection.RequiredNetrunnerLevel, &protection.CopyProtection, &protection.HackProtection,
		&protection.InstallProtection, &boundCharacterID, &protection.EncodedAt, &protection.EncodedBy,
		&protection.CreatedAt, &protection.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get engram protection")
		return nil, err
	}

	protection.BoundCharacterID = boundCharacterID
	return &protection, nil
}

func (r *EngramSecurityRepository) CreateProtection(ctx context.Context, protection *EngramProtection) error {
	protection.ID = uuid.New()
	protection.CreatedAt = time.Now()
	protection.UpdatedAt = time.Now()

	_, err := r.db.Exec(ctx,
		`INSERT INTO character.engram_protection 
		 (id, engram_id, protection_tier, protection_tier_name, required_netrunner_level,
		  copy_protection, hack_protection, install_protection, bound_character_id,
		  encoded_at, encoded_by, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)`,
		protection.ID, protection.EngramID, protection.ProtectionTier, protection.ProtectionTierName,
		protection.RequiredNetrunnerLevel, protection.CopyProtection, protection.HackProtection,
		protection.InstallProtection, protection.BoundCharacterID, protection.EncodedAt,
		protection.EncodedBy, protection.CreatedAt, protection.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create engram protection")
		return err
	}

	return nil
}

func (r *EngramSecurityRepository) UpdateProtection(ctx context.Context, protection *EngramProtection) error {
	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_protection 
		 SET protection_tier = $1, protection_tier_name = $2, required_netrunner_level = $3,
		 copy_protection = $4, hack_protection = $5, install_protection = $6,
		 bound_character_id = $7, updated_at = NOW()
		 WHERE engram_id = $8`,
		protection.ProtectionTier, protection.ProtectionTierName, protection.RequiredNetrunnerLevel,
		protection.CopyProtection, protection.HackProtection, protection.InstallProtection,
		protection.BoundCharacterID, protection.EngramID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update engram protection")
		return err
	}

	return nil
}
