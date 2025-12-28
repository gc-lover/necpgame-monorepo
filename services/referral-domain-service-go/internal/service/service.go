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
		Code:        code,
		OwnerID:     ownerID,
		IsActive:    true,
		ExpiresAt:   expiresAt,
		MaxUses:     maxUses,
		CurrentUses: 0,
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
		zap.String("owner_id", referralCode.OwnerID.String()))

	return referralCode, nil
}

// RegisterReferral registers a new user through referral
func (s *Service) RegisterReferral(ctx context.Context, referrerID, refereeID uuid.UUID, referralCodeID uuid.UUID) error {
	registration := &repository.ReferralRegistration{
		ID:             uuid.New(),
		ReferrerID:     referrerID,
		RefereeID:      refereeID,
		ReferralCodeID: referralCodeID,
		Status:         "pending",
		RegisteredAt:   time.Now(),
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

// ClaimReferralReward claims a referral reward
func (s *Service) ClaimReferralReward(ctx context.Context, userID, milestoneID uuid.UUID) error {
	// Check if user qualifies for the milestone
	stats, err := s.GetReferralStatistics(ctx, userID)
	if err != nil {
		return err
	}

	milestone, err := s.repo.GetReferralMilestone(ctx, milestoneID)
	if err != nil {
		return err
	}

	if stats.ConvertedReferrals < milestone.Threshold {
		return fmt.Errorf("user does not qualify for this milestone")
	}

	// Check if reward already claimed
	if existing, _ := s.repo.GetUserMilestoneReward(ctx, userID, milestoneID); existing != nil {
		return fmt.Errorf("reward already claimed")
	}

	// Create reward record
	reward := &repository.ReferralReward{
		ID:          uuid.New(),
		UserID:      userID,
		MilestoneID: milestoneID,
		Amount:      milestone.RewardValue,
		Status:      "claimed",
		ClaimedAt:   &time.Time{},
	}
	*reward.ClaimedAt = time.Now()

	if err := s.repo.CreateReferralReward(ctx, reward); err != nil {
		s.logger.Error("Failed to claim referral reward", zap.Error(err))
		return err
	}

	s.logger.Info("Referral reward claimed",
		zap.String("user_id", userID.String()),
		zap.String("milestone_id", milestoneID.String()),
		zap.Float64("amount", milestone.RewardValue))

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
