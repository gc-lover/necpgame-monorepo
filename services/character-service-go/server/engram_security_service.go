package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	ErrInvalidProtectionTier = errors.New("invalid protection tier (must be 1-5)")
)

type EngramSecurityServiceInterface interface {
	GetEngramProtection(ctx context.Context, engramID uuid.UUID) (*EngramProtectionInfo, error)
	EncodeEngram(ctx context.Context, engramID uuid.UUID, protectionTier int, settings *ProtectionSettings, encodedBy uuid.UUID, netrunnerSkillLevel int) (*EngramProtectionInfo, error)
}

type ProtectionSettings struct {
	CopyProtection    bool       `json:"copy_protection"`
	HackProtection    bool       `json:"hack_protection"`
	InstallProtection bool       `json:"install_protection"`
	BoundCharacterID  *uuid.UUID `json:"bound_character_id,omitempty"`
}

type EngramProtectionInfo struct {
	EngramID               uuid.UUID              `json:"engram_id"`
	ProtectionTier         int                    `json:"protection_tier"`
	ProtectionTierName     string                 `json:"protection_tier_name"`
	RequiredNetrunnerLevel int                    `json:"required_netrunner_level"`
	ProtectionSettings     *ProtectionSettings    `json:"protection_settings"`
	EncodedAt              time.Time              `json:"encoded_at"`
	EncodedBy              uuid.UUID              `json:"encoded_by"`
}

var protectionTierNames = map[int]string{
	1: "basic",
	2: "standard",
	3: "advanced",
	4: "corporate",
	5: "military",
}

var protectionTierNetrunnerLevels = map[int]int{
	1: 20,
	2: 50,
	3: 75,
	4: 90,
	5: 95,
}

type EngramSecurityService struct {
	repo   EngramSecurityRepositoryInterface
	cache  *redis.Client
	logger *logrus.Logger
}

func NewEngramSecurityService(repo EngramSecurityRepositoryInterface, cache *redis.Client) *EngramSecurityService {
	return &EngramSecurityService{
		repo:   repo,
		cache:  cache,
		logger: GetLogger(),
	}
}

func (s *EngramSecurityService) GetEngramProtection(ctx context.Context, engramID uuid.UUID) (*EngramProtectionInfo, error) {
	protection, err := s.repo.GetProtection(ctx, engramID)
	if err != nil {
		return nil, err
	}

	if protection == nil {
		return nil, ErrEngramNotFound
	}

	return &EngramProtectionInfo{
		EngramID:               protection.EngramID,
		ProtectionTier:         protection.ProtectionTier,
		ProtectionTierName:     protection.ProtectionTierName,
		RequiredNetrunnerLevel: protection.RequiredNetrunnerLevel,
		ProtectionSettings: &ProtectionSettings{
			CopyProtection:    protection.CopyProtection,
			HackProtection:    protection.HackProtection,
			InstallProtection: protection.InstallProtection,
			BoundCharacterID:  protection.BoundCharacterID,
		},
		EncodedAt: protection.EncodedAt,
		EncodedBy: protection.EncodedBy,
	}, nil
}

func (s *EngramSecurityService) EncodeEngram(ctx context.Context, engramID uuid.UUID, protectionTier int, settings *ProtectionSettings, encodedBy uuid.UUID, netrunnerSkillLevel int) (*EngramProtectionInfo, error) {
	if protectionTier < 1 || protectionTier > 5 {
		return nil, ErrInvalidProtectionTier
	}

	tierName, ok := protectionTierNames[protectionTier]
	if !ok {
		tierName = "basic"
	}

	requiredLevel := protectionTierNetrunnerLevels[protectionTier]
	if netrunnerSkillLevel > requiredLevel {
		requiredLevel = netrunnerSkillLevel
	}

	protection := &EngramProtection{
		EngramID:               engramID,
		ProtectionTier:         protectionTier,
		ProtectionTierName:     tierName,
		RequiredNetrunnerLevel: requiredLevel,
		EncodedAt:              time.Now(),
		EncodedBy:              encodedBy,
	}

	if settings != nil {
		protection.CopyProtection = settings.CopyProtection
		protection.HackProtection = settings.HackProtection
		protection.InstallProtection = settings.InstallProtection
		protection.BoundCharacterID = settings.BoundCharacterID
	}

	existing, err := s.repo.GetProtection(ctx, engramID)
	if err != nil {
		return nil, err
	}

	if existing != nil {
		err = s.repo.UpdateProtection(ctx, protection)
	} else {
		err = s.repo.CreateProtection(ctx, protection)
	}

	if err != nil {
		s.logger.WithError(err).Error("Failed to encode engram")
		return nil, err
	}

	return &EngramProtectionInfo{
		EngramID:               protection.EngramID,
		ProtectionTier:         protection.ProtectionTier,
		ProtectionTierName:     protection.ProtectionTierName,
		RequiredNetrunnerLevel: protection.RequiredNetrunnerLevel,
		ProtectionSettings: &ProtectionSettings{
			CopyProtection:    protection.CopyProtection,
			HackProtection:    protection.HackProtection,
			InstallProtection: protection.InstallProtection,
			BoundCharacterID:  protection.BoundCharacterID,
		},
		EncodedAt: protection.EncodedAt,
		EncodedBy: protection.EncodedBy,
	}, nil
}

