// Package server Issue: #1515
package server

import (
	"context"
	"errors"
	"math/rand"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type AffixServiceInterface interface {
	GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error)
	GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error)
	GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error)
	GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) (*models.AffixRotationHistoryResponse, error)
	TriggerRotation(ctx context.Context, force bool, customAffixes []uuid.UUID) (*models.AffixRotation, error)
	GenerateAndApplyInstanceAffixes(ctx context.Context, instanceID uuid.UUID) error
}

type AffixService struct {
	repo   AffixRepositoryInterface
	logger *logrus.Logger
}

const (
	serviceTimeoutRead  = 2 * time.Second
	serviceTimeoutWrite = 3 * time.Second
)

func NewAffixService(db *pgxpool.Pool) *AffixService {
	return &AffixService{
		repo:   NewAffixRepository(db),
		logger: logrus.New(),
	}
}

func (s *AffixService) GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutRead)
	defer cancel()

	rotation, err := s.repo.GetActiveRotation(ctx)
	if err != nil {
		return nil, err
	}

	return &models.ActiveAffixesResponse{
		WeekStart:     rotation.WeekStart,
		WeekEnd:       rotation.WeekEnd,
		ActiveAffixes: rotation.ActiveAffixes,
		SeasonalAffix: rotation.SeasonalAffix,
	}, nil
}

func (s *AffixService) GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error) {
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutRead)
	defer cancel()

	return s.repo.GetAffix(ctx, id)
}

func (s *AffixService) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutRead)
	defer cancel()

	affixes, appliedAt, err := s.repo.GetInstanceAffixes(ctx, instanceID)
	if err != nil {
		return nil, err
	}

	totalRewardModifier := 1.0
	totalDifficultyModifier := 1.0

	for _, affix := range affixes {
		totalRewardModifier *= affix.RewardModifier
		totalDifficultyModifier *= affix.DifficultyModifier
	}

	return &models.InstanceAffixesResponse{
		InstanceID:              instanceID,
		AppliedAt:               appliedAt,
		Affixes:                 affixes,
		TotalRewardModifier:     totalRewardModifier,
		TotalDifficultyModifier: totalDifficultyModifier,
	}, nil
}

func (s *AffixService) GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) (*models.AffixRotationHistoryResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutRead)
	defer cancel()

	if weeksBack < 1 || weeksBack > 52 {
		weeksBack = 4
	}
	if limit < 1 || limit > 100 {
		limit = 20
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
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutWrite)
	defer cancel()

	if !force {
		active, err := s.repo.GetActiveRotation(ctx)
		if err == nil && active.WeekEnd.After(time.Now()) {
			return nil, errors.New("active rotation exists")
		}
	}

	now := time.Now()
	weekStart := getNextMonday(now)
	weekEnd := weekStart.AddDate(0, 0, 7)

	var affixIDs []uuid.UUID
	if len(customAffixes) >= 8 {
		affixIDs = customAffixes[:8]
	} else {
		allAffixes, err := s.repo.ListAffixIDs(ctx)
		if err != nil {
			return nil, err
		}
		if len(allAffixes) < 8 {
			return nil, errors.New("not enough affixes in database")
		}

		rand.Seed(time.Now().UnixNano())
		rand.Shuffle(len(allAffixes), func(i, j int) {
			allAffixes[i], allAffixes[j] = allAffixes[j], allAffixes[i]
		})
		affixIDs = allAffixes[:8]
	}

	rotation := &models.AffixRotation{
		ID:        uuid.New(),
		WeekStart: weekStart,
		WeekEnd:   weekEnd,
		CreatedAt: now,
	}

	for _, affixID := range affixIDs {
		affix, err := s.repo.GetAffix(ctx, affixID)
		if err != nil {
			continue
		}
		rotation.ActiveAffixes = append(rotation.ActiveAffixes, models.AffixSummary{
			ID:                 affix.ID,
			Name:               affix.Name,
			Category:           affix.Category,
			Description:        affix.Description,
			RewardModifier:     affix.RewardModifier,
			DifficultyModifier: affix.DifficultyModifier,
		})
	}

	if err := s.repo.CreateRotation(ctx, rotation); err != nil {
		return nil, err
	}

	return rotation, nil
}

func (s *AffixService) getAllAffixes(ctx context.Context) ([]uuid.UUID, error) {
	return s.repo.ListAffixIDs(ctx)
}

// GenerateAndApplyInstanceAffixes generates 2-4 random affixes from active rotation and applies to instance
// Issue: #1515
func (s *AffixService) GenerateAndApplyInstanceAffixes(ctx context.Context, instanceID uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, serviceTimeoutWrite)
	defer cancel()

	rotation, err := s.repo.GetActiveRotation(ctx)
	if err != nil {
		return errors.New("no active rotation")
	}

	if len(rotation.ActiveAffixes) < 2 {
		return errors.New("not enough active affixes")
	}

	// Generate 2-4 random affixes (Issue: #1515)
	affixCount := 2 + rand.Intn(3) // 2, 3, or 4
	if affixCount > len(rotation.ActiveAffixes) {
		affixCount = len(rotation.ActiveAffixes)
	}

	// Shuffle and select random affixes
	affixIDs := make([]uuid.UUID, len(rotation.ActiveAffixes))
	for i, affix := range rotation.ActiveAffixes {
		affixIDs[i] = affix.ID
	}

	rand.Seed(time.Now().UnixNano())
	rand.Shuffle(len(affixIDs), func(i, j int) {
		affixIDs[i], affixIDs[j] = affixIDs[j], affixIDs[i]
	})

	selectedAffixes := affixIDs[:affixCount]

	return s.repo.SaveInstanceAffixes(ctx, instanceID, selectedAffixes)
}

func getNextMonday(t time.Time) time.Time {
	daysUntilMonday := (8 - int(t.Weekday())) % 7
	if daysUntilMonday == 0 {
		daysUntilMonday = 7
	}
	nextMonday := t.AddDate(0, 0, daysUntilMonday)
	return time.Date(nextMonday.Year(), nextMonday.Month(), nextMonday.Day(), 0, 0, 0, 0, time.UTC)
}
