package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/referral-domain-service-go/internal/repository"
)

// Service handles business logic for the Referral Domain
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// Referral Code business logic

// CreateReferralCode creates a new referral code for a user
func (s *Service) CreateReferralCode(ctx context.Context, ownerID uuid.UUID, code string, expiresAt *time.Time, maxUses *int) (*repository.ReferralCode, error) {
	// Validate code uniqueness
	if existing, _ := s.repo.GetReferralCodeByCode(ctx, code); existing != nil {
		return nil, fmt.Errorf("referral code already exists")
	}

	referralCode := &repository.ReferralCode{
		ID:          uuid.New(),
		CharacterID: ownerID,
		Code:        code,
		Prefix:      "REF",
		CreatedAt:   time.Now(),
		ExpiresAt:   expiresAt,
		IsActive:    true,
		UsageCount:  0,
		MaxUsage:    maxUses,
	}

	if err := s.repo.CreateReferralCode(ctx, referralCode); err != nil {
		s.logger.Error("Failed to create referral code", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Referral code created",
		zap.String("code", code),
		zap.String("owner_id", ownerID.String()))

	return referralCode, nil
}

// ValidateReferralCode validates a referral code
func (s *Service) ValidateReferralCode(ctx context.Context, code string) (*repository.ReferralCode, error) {
	referralCode, err := s.repo.ValidateReferralCode(ctx, code)
	if err != nil {
		s.logger.Warn("Referral code validation failed",
			zap.String("code", code),
			zap.Error(err))
		return nil, err
	}

	s.logger.Info("Referral code validated",
		zap.String("code", code),
		zap.String("owner_id", referralCode.CharacterID.String()))

	return referralCode, nil
}

// GetReferralCode gets a referral code by ID
func (s *Service) GetReferralCode(ctx context.Context, id uuid.UUID) (*repository.ReferralCode, error) {
	return s.repo.GetReferralCode(ctx, id)
}

// GetUserReferralCodes gets all referral codes for a user
func (s *Service) GetUserReferralCodes(ctx context.Context, userID uuid.UUID) ([]*repository.ReferralCode, error) {
	return s.repo.GetUserReferralCodes(ctx, userID)
}

// RegisterReferral registers a new user through referral
func (s *Service) RegisterReferral(ctx context.Context, referrerID, refereeID uuid.UUID, referralCode string) error {
	registration := &repository.ReferralRegistration{
		ID:           uuid.New(),
		ReferrerID:   referrerID,
		ReferredID:   refereeID,
		ReferralCode: referralCode,
		Status:       "pending",
		RegisteredAt: time.Now(),
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	if err := s.repo.CreateReferralRegistration(ctx, registration); err != nil {
		s.logger.Error("Failed to create referral registration", zap.Error(err))
		return err
	}

	s.logger.Info("Referral registration created",
		zap.String("referrer_id", referrerID.String()),
		zap.String("referee_id", refereeID.String()))

	return nil
}

// ConvertReferral converts a pending referral to converted status
func (s *Service) ConvertReferral(ctx context.Context, registrationID uuid.UUID) error {
	now := time.Now()
	if err := s.repo.UpdateReferralStatus(ctx, registrationID, "converted", &now); err != nil {
		s.logger.Error("Failed to convert referral", zap.Error(err))
		return err
	}

	s.logger.Info("Referral converted",
		zap.String("registration_id", registrationID.String()))

	return nil
}

// GetReferralStatistics gets referral statistics for a user
func (s *Service) GetReferralStatistics(ctx context.Context, userID uuid.UUID) (*repository.ReferralStatistics, error) {
	stats, err := s.repo.GetReferralStatistics(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get referral statistics", zap.Error(err))
		return nil, err
	}

	return stats, nil
}

// ClaimReferralReward claims a referral reward for reaching a milestone
func (s *Service) ClaimReferralReward(ctx context.Context, userID uuid.UUID, milestoneLevel int) error {
	// Get the user's milestone for this level
	milestone, err := s.repo.GetUserMilestone(ctx, userID, milestoneLevel)
	if err != nil {
		return err
	}
	if milestone == nil {
		return fmt.Errorf("milestone not found for user and level")
	}

	// Check if milestone is completed
	if !milestone.IsCompleted {
		return fmt.Errorf("milestone not yet completed")
	}

	// Check if reward already claimed
	if milestone.IsRewardClaimed {
		return fmt.Errorf("reward already claimed")
	}

	// Create reward record
	reward := &repository.ReferralReward{
		ID:           uuid.New(),
		CharacterID:  userID,
		RewardType:   milestone.RewardType,
		RewardAmount: milestone.RewardAmount,
		CurrencyType: "eddies",
		Status:       "claimed",
		ClaimedAt:    &time.Time{},
		CreatedAt:    time.Now(),
	}
	*reward.ClaimedAt = time.Now()

	if err := s.repo.CreateReferralReward(ctx, reward); err != nil {
		s.logger.Error("Failed to claim referral reward", zap.Error(err))
		return err
	}

	// Update milestone to mark reward as claimed
	if err := s.repo.UpdateMilestoneRewardClaimed(ctx, milestone.ID); err != nil {
		s.logger.Error("Failed to update milestone reward claimed status", zap.Error(err))
		return err
	}

	s.logger.Info("Referral reward claimed",
		zap.String("user_id", userID.String()),
		zap.Int("milestone_level", milestoneLevel),
		zap.Int("amount", milestone.RewardAmount))

	return nil
}

// GetReferralLeaderboard gets the top referrers
func (s *Service) GetReferralLeaderboard(ctx context.Context, limit int) ([]repository.ReferralStatistics, error) {
	leaderboard, err := s.repo.GetReferralLeaderboard(ctx, limit)
	if err != nil {
		s.logger.Error("Failed to get referral leaderboard", zap.Error(err))
		return nil, err
	}

	return leaderboard, nil
}

// Health check
func (s *Service) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}
