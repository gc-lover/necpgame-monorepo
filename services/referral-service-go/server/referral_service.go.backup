package server

import (
	"context"
	cryptorand "crypto/rand"
	"encoding/base32"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ReferralService interface {
	GetReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error)
	GenerateReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error)
	ValidateReferralCode(ctx context.Context, code string) (*models.ReferralCode, error)
	
	RegisterWithCode(ctx context.Context, playerID uuid.UUID, referralCode string) (*models.Referral, error)
	GetReferralStatus(ctx context.Context, playerID uuid.UUID, status *models.ReferralStatus, limit, offset int) ([]models.Referral, int, error)
	
	GetMilestones(ctx context.Context, playerID uuid.UUID) ([]models.ReferralMilestone, *models.ReferralMilestoneType, error)
	ClaimMilestoneReward(ctx context.Context, playerID uuid.UUID, milestoneID uuid.UUID) (*models.ReferralMilestone, error)
	
	DistributeRewards(ctx context.Context, referralID uuid.UUID, rewardType models.ReferralRewardType) error
	GetRewardHistory(ctx context.Context, playerID uuid.UUID, rewardType *models.ReferralRewardType, limit, offset int) ([]models.ReferralReward, int, error)
	
	GetReferralStats(ctx context.Context, playerID uuid.UUID) (*models.ReferralStats, error)
	GetPublicReferralStats(ctx context.Context, code string) (*models.ReferralStats, error)
	
	GetLeaderboard(ctx context.Context, leaderboardType models.ReferralLeaderboardType, limit, offset int) ([]models.ReferralLeaderboardEntry, int, error)
	GetLeaderboardPosition(ctx context.Context, playerID uuid.UUID, leaderboardType models.ReferralLeaderboardType) (*models.ReferralLeaderboardEntry, int, error)
	
	GetEvents(ctx context.Context, playerID uuid.UUID, eventType *models.ReferralEventType, limit, offset int) ([]models.ReferralEvent, int, error)
}

type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: GetLogger(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

type referralService struct {
	repo     ReferralRepository
	logger   *logrus.Logger
	eventBus EventBus
}

func NewReferralService(repo ReferralRepository, logger *logrus.Logger, eventBus EventBus) ReferralService {
	return &referralService{
		repo:     repo,
		logger:   logger,
		eventBus: eventBus,
	}
}

func (s *referralService) GetReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error) {
	return s.repo.GetReferralCode(ctx, playerID)
}

func (s *referralService) GenerateReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error) {
	existing, err := s.repo.GetReferralCode(ctx, playerID)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		return existing, nil
	}
	
	code := s.generateUniqueCode()
	referralCode := &models.ReferralCode{
		ID:        uuid.New(),
		PlayerID:  playerID,
		Code:      code,
		IsActive:  true,
		CreatedAt: time.Now(),
	}
	
	if err := s.repo.CreateReferralCode(ctx, referralCode); err != nil {
		return nil, err
	}
	
	RecordCodeGenerated(playerID.String())
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"player_id": playerID.String(),
			"code":      code,
			"code_id":   referralCode.ID.String(),
		}
		s.eventBus.PublishEvent(ctx, string(models.EventTypeCodeGenerated), payload)
	}
	
	event := &models.ReferralEvent{
		ID:        uuid.New(),
		PlayerID:  playerID,
		EventType: models.EventTypeCodeGenerated,
		EventData: map[string]interface{}{
			"code": code,
		},
		CreatedAt: time.Now(),
	}
	s.repo.CreateEvent(ctx, event)
	
	return referralCode, nil
}

func (s *referralService) generateUniqueCode() string {
	bytes := make([]byte, 6)
	cryptorand.Read(bytes)
	code := base32.StdEncoding.WithPadding(base32.NoPadding).EncodeToString(bytes)[:8]
	return code
}

func (s *referralService) ValidateReferralCode(ctx context.Context, code string) (*models.ReferralCode, error) {
	return s.repo.ValidateReferralCode(ctx, code)
}

func (s *referralService) RegisterWithCode(ctx context.Context, playerID uuid.UUID, referralCode string) (*models.Referral, error) {
	code, err := s.repo.ValidateReferralCode(ctx, referralCode)
	if err != nil {
		return nil, errors.New("invalid referral code")
	}
	
	if code.PlayerID == playerID {
		return nil, errors.New("cannot use own referral code")
	}
	
	existing, _, err := s.repo.GetReferralsByPlayer(ctx, code.PlayerID, nil, 100, 0)
	if err == nil {
		for _, ref := range existing {
			if ref.RefereeID == playerID {
				return nil, errors.New("already registered with this referrer")
			}
		}
	}
	
	referral := &models.Referral{
		ID:               uuid.New(),
		ReferrerID:       code.PlayerID,
		RefereeID:        playerID,
		ReferralCodeID:   code.ID,
		RegisteredAt:     time.Now(),
		Status:           models.ReferralStatusPending,
		Level10Reached:   false,
		WelcomeBonusGiven: false,
		ReferrerBonusGiven: false,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}
	
	if err := s.repo.CreateReferral(ctx, referral); err != nil {
		return nil, err
	}
	
	RecordReferralRegistered(string(models.ReferralStatusPending))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"referral_id": referral.ID.String(),
			"referrer_id": code.PlayerID.String(),
			"referee_id":  playerID.String(),
		}
		s.eventBus.PublishEvent(ctx, string(models.EventTypeRegistered), payload)
	}
	
	event := &models.ReferralEvent{
		ID:        uuid.New(),
		PlayerID:  code.PlayerID,
		EventType: models.EventTypeRegistered,
		EventData: map[string]interface{}{
			"referral_id": referral.ID.String(),
			"referee_id":  playerID.String(),
		},
		CreatedAt: time.Now(),
	}
	s.repo.CreateEvent(ctx, event)
	
	return referral, nil
}

func (s *referralService) GetReferralStatus(ctx context.Context, playerID uuid.UUID, status *models.ReferralStatus, limit, offset int) ([]models.Referral, int, error) {
	return s.repo.GetReferralsByPlayer(ctx, playerID, status, limit, offset)
}

func (s *referralService) GetMilestones(ctx context.Context, playerID uuid.UUID) ([]models.ReferralMilestone, *models.ReferralMilestoneType, error) {
	milestones, err := s.repo.GetMilestones(ctx, playerID)
	if err != nil {
		return nil, nil, err
	}
	
	var currentMilestone *models.ReferralMilestoneType
	stats, err := s.repo.GetReferralStats(ctx, playerID)
	if err == nil && stats.CurrentMilestone != nil {
		currentMilestone = stats.CurrentMilestone
	}
	
	return milestones, currentMilestone, nil
}

func (s *referralService) ClaimMilestoneReward(ctx context.Context, playerID uuid.UUID, milestoneID uuid.UUID) (*models.ReferralMilestone, error) {
	milestones, err := s.repo.GetMilestones(ctx, playerID)
	if err != nil {
		return nil, err
	}
	
	var milestone *models.ReferralMilestone
	for _, m := range milestones {
		if m.ID == milestoneID {
			milestone = &m
			break
		}
	}
	
	if milestone == nil {
		return nil, errors.New("milestone not found")
	}
	
	if milestone.RewardClaimed {
		return nil, errors.New("milestone reward already claimed")
	}
	
	now := time.Now()
	milestone.RewardClaimed = true
	milestone.RewardClaimedAt = &now
	
	if err := s.repo.UpdateMilestone(ctx, milestone); err != nil {
		return nil, err
	}
	
	RecordMilestoneAchieved(string(milestone.MilestoneType))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"milestone_id": milestone.ID.String(),
			"player_id":    playerID.String(),
			"milestone_type": string(milestone.MilestoneType),
		}
		s.eventBus.PublishEvent(ctx, string(models.EventTypeMilestoneAchieved), payload)
	}
	
	return milestone, nil
}

func (s *referralService) DistributeRewards(ctx context.Context, referralID uuid.UUID, rewardType models.ReferralRewardType) error {
	referral, err := s.repo.GetReferral(ctx, referralID)
	if err != nil {
		return err
	}
	if referral == nil {
		return errors.New("referral not found")
	}
	
	var playerID uuid.UUID
	var amount int64
	var currencyType string
	
	switch rewardType {
	case models.RewardTypeWelcomeBonus:
		if referral.WelcomeBonusGiven {
			return errors.New("welcome bonus already given")
		}
		playerID = referral.RefereeID
		amount = 1000
		currencyType = "credits"
		referral.WelcomeBonusGiven = true
	case models.RewardTypeReferrerBonus:
		if referral.ReferrerBonusGiven {
			return errors.New("referrer bonus already given")
		}
		playerID = referral.ReferrerID
		amount = 500
		currencyType = "credits"
		referral.ReferrerBonusGiven = true
	case models.RewardTypeMilestoneBonus:
		return errors.New("milestone bonus should be claimed through milestone endpoint")
	default:
		return errors.New("invalid reward type")
	}
	
	reward := &models.ReferralReward{
		ID:            uuid.New(),
		PlayerID:      playerID,
		ReferralID:    &referralID,
		RewardType:    rewardType,
		RewardAmount:  amount,
		CurrencyType:  currencyType,
		DistributedAt: time.Now(),
	}
	
	if err := s.repo.CreateReward(ctx, reward); err != nil {
		return err
	}
	
	if err := s.repo.UpdateReferral(ctx, referral); err != nil {
		return err
	}
	
	RecordRewardDistributed(string(rewardType))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"reward_id":   reward.ID.String(),
			"player_id":   playerID.String(),
			"referral_id": referralID.String(),
			"reward_type": string(rewardType),
			"amount":      amount,
		}
		s.eventBus.PublishEvent(ctx, string(models.EventTypeRewardDistributed), payload)
	}
	
	event := &models.ReferralEvent{
		ID:        uuid.New(),
		PlayerID:  playerID,
		EventType: models.EventTypeRewardDistributed,
		EventData: map[string]interface{}{
			"reward_id": reward.ID.String(),
			"referral_id": referralID.String(),
			"reward_type": string(rewardType),
		},
		CreatedAt: time.Now(),
	}
	s.repo.CreateEvent(ctx, event)
	
	return nil
}

func (s *referralService) GetRewardHistory(ctx context.Context, playerID uuid.UUID, rewardType *models.ReferralRewardType, limit, offset int) ([]models.ReferralReward, int, error) {
	return s.repo.GetRewardHistory(ctx, playerID, rewardType, limit, offset)
}

func (s *referralService) GetReferralStats(ctx context.Context, playerID uuid.UUID) (*models.ReferralStats, error) {
	return s.repo.GetReferralStats(ctx, playerID)
}

func (s *referralService) GetPublicReferralStats(ctx context.Context, code string) (*models.ReferralStats, error) {
	return s.repo.GetPublicReferralStats(ctx, code)
}

func (s *referralService) GetLeaderboard(ctx context.Context, leaderboardType models.ReferralLeaderboardType, limit, offset int) ([]models.ReferralLeaderboardEntry, int, error) {
	return s.repo.GetLeaderboard(ctx, leaderboardType, limit, offset)
}

func (s *referralService) GetLeaderboardPosition(ctx context.Context, playerID uuid.UUID, leaderboardType models.ReferralLeaderboardType) (*models.ReferralLeaderboardEntry, int, error) {
	return s.repo.GetLeaderboardPosition(ctx, playerID, leaderboardType)
}

func (s *referralService) GetEvents(ctx context.Context, playerID uuid.UUID, eventType *models.ReferralEventType, limit, offset int) ([]models.ReferralEvent, int, error) {
	return s.repo.GetEvents(ctx, playerID, eventType, limit, offset)
}

