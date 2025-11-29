package server

import (
	"context"
	"errors"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type AffixServiceInterface interface {
	GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error)
	GetAffix(ctx context.Context, affixID uuid.UUID) (*models.Affix, error)
	GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error)
	GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) (*models.AffixRotationHistoryResponse, error)
	TriggerRotation(ctx context.Context, force bool, customAffixes []uuid.UUID) (*models.AffixRotation, error)
	GenerateInstanceAffixes(ctx context.Context, instanceID uuid.UUID) error
}

type AffixService struct {
	repo   AffixRepositoryInterface
	cache  *redis.Client
	logger *logrus.Logger
}

func NewAffixService(db *pgxpool.Pool, redisURL string) (*AffixService, error) {
	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	return &AffixService{
		repo:   NewAffixRepository(db),
		cache:  redis.NewClient(redisOpts),
		logger: GetLogger(),
	}, nil
}

func (s *AffixService) GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error) {
	rotation, err := s.repo.GetActiveRotation(ctx)
	if err != nil {
		weekStart := GetWeekStart(time.Now())
		weekEnd := weekStart.AddDate(0, 0, 7)
		return &models.ActiveAffixesResponse{
			WeekStart:     weekStart,
			WeekEnd:       weekEnd,
			ActiveAffixes: []models.AffixSummary{},
		}, nil
	}

	response := &models.ActiveAffixesResponse{
		WeekStart:     rotation.WeekStart,
		WeekEnd:       rotation.WeekEnd,
		ActiveAffixes: rotation.ActiveAffixes,
		SeasonalAffix:  rotation.SeasonalAffix,
	}

	return response, nil
}

func (s *AffixService) GetAffix(ctx context.Context, affixID uuid.UUID) (*models.Affix, error) {
	return s.repo.GetAffix(ctx, affixID)
}

func (s *AffixService) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	affixes, err := s.repo.GetInstanceAffixes(ctx, instanceID)
	if err != nil {
		return nil, err
	}

	if len(affixes) == 0 {
		err = s.GenerateInstanceAffixes(ctx, instanceID)
		if err != nil {
			return nil, err
		}
		affixes, err = s.repo.GetInstanceAffixes(ctx, instanceID)
		if err != nil {
			return nil, err
		}
	}

	totalRewardModifier := 1.0
	totalDifficultyModifier := 1.0
	for _, affix := range affixes {
		totalRewardModifier *= affix.RewardModifier
		totalDifficultyModifier *= affix.DifficultyModifier
	}

	var appliedAt time.Time
	if len(affixes) > 0 {
		appliedAt = time.Now()
	}

	return &models.InstanceAffixesResponse{
		InstanceID:            instanceID,
		AppliedAt:             appliedAt,
		Affixes:               affixes,
		TotalRewardModifier:   totalRewardModifier,
		TotalDifficultyModifier: totalDifficultyModifier,
	}, nil
}

func (s *AffixService) GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) (*models.AffixRotationHistoryResponse, error) {
	if weeksBack < 1 {
		weeksBack = 4
	}
	if weeksBack > 52 {
		weeksBack = 52
	}
	if limit < 1 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	rotations, total, err := s.repo.GetRotationHistory(ctx, weeksBack, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.AffixRotationHistoryResponse{
		Items:  rotations,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *AffixService) TriggerRotation(ctx context.Context, force bool, customAffixes []uuid.UUID) (*models.AffixRotation, error) {
	now := time.Now()
	weekStart := GetWeekStart(now)
	weekEnd := weekStart.AddDate(0, 0, 7)

	existingRotation, err := s.repo.GetRotationByWeek(ctx, weekStart)
	if err == nil && !force {
		return existingRotation, errors.New("rotation already exists for this week")
	}

	allAffixes, err := s.repo.GetAllAffixes(ctx)
	if err != nil {
		return nil, err
	}

	var affixIDs []uuid.UUID
	if len(customAffixes) > 0 {
		if len(customAffixes) < 8 || len(customAffixes) > 10 {
			return nil, errors.New("custom_affixes must contain 8-10 affixes")
		}
		affixIDs = customAffixes
	} else {
		affixIDs = s.selectRandomAffixes(allAffixes, 8)
	}

	seasonalAffixID := uuid.Nil
	if len(allAffixes) > 0 {
		seasonalCandidates := make([]models.Affix, 0)
		for _, affix := range allAffixes {
			if affix.Category == models.AffixCategoryEnvironmental {
				seasonalCandidates = append(seasonalCandidates, affix)
			}
		}
		if len(seasonalCandidates) > 0 {
			seasonalAffixID = seasonalCandidates[rand.Intn(len(seasonalCandidates))].ID
		}
	}

	rotation := &models.AffixRotation{
		WeekStart:     weekStart,
		WeekEnd:       weekEnd,
		ActiveAffixes: make([]models.AffixSummary, 0),
	}

	if seasonalAffixID != uuid.Nil {
		seasonalAffix, err := s.repo.GetAffix(ctx, seasonalAffixID)
		if err == nil {
			rotation.SeasonalAffix = &models.AffixSummary{
				ID:                seasonalAffix.ID,
				Name:              seasonalAffix.Name,
				Category:          seasonalAffix.Category,
				Description:       seasonalAffix.Description,
				RewardModifier:    seasonalAffix.RewardModifier,
				DifficultyModifier: seasonalAffix.DifficultyModifier,
			}
		}
	}

	err = s.repo.CreateRotation(ctx, rotation, affixIDs)
	if err != nil {
		return nil, err
	}

	for _, affixID := range affixIDs {
		affix, err := s.repo.GetAffix(ctx, affixID)
		if err == nil {
			rotation.ActiveAffixes = append(rotation.ActiveAffixes, models.AffixSummary{
				ID:                affix.ID,
				Name:              affix.Name,
				Category:          affix.Category,
				Description:       affix.Description,
				RewardModifier:    affix.RewardModifier,
				DifficultyModifier: affix.DifficultyModifier,
			})
		}
	}

	return rotation, nil
}

func (s *AffixService) GenerateInstanceAffixes(ctx context.Context, instanceID uuid.UUID) error {
	rotation, err := s.repo.GetActiveRotation(ctx)
	if err != nil {
		return errors.New("no active rotation found")
	}

	availableAffixes := make([]uuid.UUID, 0)
	for _, affix := range rotation.ActiveAffixes {
		availableAffixes = append(availableAffixes, affix.ID)
	}
	if rotation.SeasonalAffix != nil {
		availableAffixes = append(availableAffixes, rotation.SeasonalAffix.ID)
	}

	if len(availableAffixes) < 2 {
		return errors.New("not enough affixes in rotation")
	}

	numAffixes := 2 + rand.Intn(3)
	if numAffixes > len(availableAffixes) {
		numAffixes = len(availableAffixes)
	}

	selectedAffixes := make([]uuid.UUID, 0)
	usedIndices := make(map[int]bool)
	for len(selectedAffixes) < numAffixes {
		idx := rand.Intn(len(availableAffixes))
		if !usedIndices[idx] {
			selectedAffixes = append(selectedAffixes, availableAffixes[idx])
			usedIndices[idx] = true
		}
	}

	return s.repo.AssignAffixesToInstance(ctx, instanceID, selectedAffixes)
}

func (s *AffixService) selectRandomAffixes(allAffixes []models.Affix, count int) []uuid.UUID {
	if count > len(allAffixes) {
		count = len(allAffixes)
	}

	selected := make([]uuid.UUID, 0)
	usedIndices := make(map[int]bool)
	for len(selected) < count {
		idx := rand.Intn(len(allAffixes))
		if !usedIndices[idx] {
			selected = append(selected, allAffixes[idx].ID)
			usedIndices[idx] = true
		}
	}

	return selected
}

