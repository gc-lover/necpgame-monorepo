package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type TimeTrialServiceInterface interface {
	StartTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.StartTimeTrialRequest) (*models.TimeTrialSession, error)
	CompleteTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.CompleteTimeTrialRequest) (*models.TimeTrialCompletionResponse, error)
	GetTimeTrialSession(ctx context.Context, sessionID uuid.UUID, playerID uuid.UUID) (*models.TimeTrialSession, error)
	GetCurrentWeeklyChallenge(ctx context.Context) (*models.WeeklyTimeChallenge, error)
	GetWeeklyChallengeHistory(ctx context.Context, weeksBack, limit, offset int) (*models.WeeklyChallengeHistoryResponse, error)
}

type TimeTrialService struct {
	repo   TimeTrialRepositoryInterface
	cache  *redis.Client
	logger *logrus.Logger
}

func NewTimeTrialService(db *pgxpool.Pool, redisURL string) (*TimeTrialService, error) {
	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	return &TimeTrialService{
		repo:   NewTimeTrialRepository(db),
		cache:  redis.NewClient(redisOpts),
		logger: GetLogger(),
	}, nil
}

func (s *TimeTrialService) StartTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.StartTimeTrialRequest) (*models.TimeTrialSession, error) {
	session := &models.TimeTrialSession{
		TrialType: req.TrialType,
		ContentID: req.ContentID,
		PlayerID:  playerID,
		TeamID:    req.TeamID,
		StartTime: time.Now(),
		Status:    models.SessionStatusInProgress,
	}

	err := s.repo.CreateSession(ctx, session)
	if err != nil {
		return nil, err
	}

	return session, nil
}

func (s *TimeTrialService) CompleteTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.CompleteTimeTrialRequest) (*models.TimeTrialCompletionResponse, error) {
	session, err := s.repo.GetSession(ctx, req.SessionID)
	if err != nil {
		return nil, err
	}

	if session.PlayerID != playerID {
		return nil, errors.New("session does not belong to player")
	}

	if session.Status != models.SessionStatusInProgress {
		return nil, errors.New("session is not in progress")
	}

	now := time.Now()
	session.EndTime = &now
	completionTime := int64(req.CompletionTimeMs)
	session.CompletionTimeMs = &completionTime
	session.Status = models.SessionStatusCompleted

	err = s.repo.UpdateSession(ctx, session)
	if err != nil {
		return nil, err
	}

	personalBest, err := s.repo.GetPersonalBest(ctx, playerID, session.TrialType, session.ContentID)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to get personal best")
	}

	isPersonalBest := false
	if personalBest == nil || *session.CompletionTimeMs < *personalBest.CompletionTimeMs {
		isPersonalBest = true
	}

	rank, err := s.repo.GetGlobalRank(ctx, session.TrialType, session.ContentID, *session.CompletionTimeMs)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to get global rank")
		rank = 0
	}

	isNewRecord := rank == 1

	rewardModifier := s.calculateRewardModifier(session.TrialType, *session.CompletionTimeMs)

	err = s.repo.CreateRecord(ctx, session.ID, playerID, session.TrialType, session.ContentID, session.TeamID, *session.CompletionTimeMs, rank, isPersonalBest)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to create record")
	}

	achievements := s.calculateAchievements(isNewRecord, isPersonalBest, rank)

	response := &models.TimeTrialCompletionResponse{
		SessionID:        session.ID,
		CompletionTimeMs: *session.CompletionTimeMs,
		Rank:             rank,
		IsNewRecord:      isNewRecord,
		IsPersonalBest:   isPersonalBest,
		RewardModifier:   rewardModifier,
		Achievements:     achievements,
		LeaderboardURL:   s.buildLeaderboardURL(session.TrialType, session.ContentID),
	}

	return response, nil
}

func (s *TimeTrialService) GetTimeTrialSession(ctx context.Context, sessionID uuid.UUID, playerID uuid.UUID) (*models.TimeTrialSession, error) {
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	if session.PlayerID != playerID {
		return nil, errors.New("session does not belong to player")
	}

	return session, nil
}

func (s *TimeTrialService) GetCurrentWeeklyChallenge(ctx context.Context) (*models.WeeklyTimeChallenge, error) {
	return s.repo.GetCurrentWeeklyChallenge(ctx)
}

func (s *TimeTrialService) GetWeeklyChallengeHistory(ctx context.Context, weeksBack, limit, offset int) (*models.WeeklyChallengeHistoryResponse, error) {
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

	challenges, total, err := s.repo.GetWeeklyChallengeHistory(ctx, weeksBack, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.WeeklyChallengeHistoryResponse{
		Items:  challenges,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *TimeTrialService) calculateRewardModifier(trialType models.TrialType, completionTimeMs int64) float64 {
	if trialType == models.TrialTypeTimeAttackDungeon {
		baseTime := int64(1800000)
		if completionTimeMs <= baseTime*75/100 {
			return 2.0
		} else if completionTimeMs <= baseTime {
			return 1.5
		} else if completionTimeMs <= baseTime*125/100 {
			return 1.25
		} else if completionTimeMs <= baseTime*150/100 {
			return 1.1
		}
	}
	return 1.0
}

func (s *TimeTrialService) calculateAchievements(isNewRecord, isPersonalBest bool, rank int) []models.Achievement {
	var achievements []models.Achievement

	if isNewRecord {
		achievements = append(achievements, models.Achievement{
			ID:          uuid.New(),
			Name:        "Speedrun Champion",
			Description: "Новый глобальный рекорд",
		})
	}

	if isPersonalBest {
		achievements = append(achievements, models.Achievement{
			ID:          uuid.New(),
			Name:        "Personal Best",
			Description: "Новый личный рекорд",
		})
	}

	if rank <= 10 && rank > 1 {
		achievements = append(achievements, models.Achievement{
			ID:          uuid.New(),
			Name:        "Speedrun Master",
			Description: "Топ-10 в лидерборде",
		})
	}

	if rank <= 100 && rank > 10 {
		achievements = append(achievements, models.Achievement{
			ID:          uuid.New(),
			Name:        "Speedrunner",
			Description: "Топ-100 в лидерборде",
		})
	}

	return achievements
}

func (s *TimeTrialService) buildLeaderboardURL(trialType models.TrialType, contentID uuid.UUID) string {
	return "/api/v1/leaderboard/time-trials/" + string(trialType) + "?content_id=" + contentID.String()
}

