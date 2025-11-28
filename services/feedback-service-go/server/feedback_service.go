package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/feedback-service-go/models"
	"github.com/sirupsen/logrus"
)

type FeedbackServiceInterface interface {
	SubmitFeedback(ctx context.Context, playerID uuid.UUID, req *models.SubmitFeedbackRequest) (*models.SubmitFeedbackResponse, error)
	GetFeedback(ctx context.Context, id uuid.UUID) (*models.Feedback, error)
	GetPlayerFeedback(ctx context.Context, playerID uuid.UUID, status *models.FeedbackStatus, feedbackType *models.FeedbackType, limit, offset int) (*models.FeedbackList, error)
	UpdateStatus(ctx context.Context, id uuid.UUID, req *models.UpdateStatusRequest) (*models.Feedback, error)
	GetBoard(ctx context.Context, category *models.FeedbackCategory, status *models.FeedbackStatus, search *string, sort string, limit, offset int) (*models.FeedbackBoardList, error)
	Vote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error)
	Unvote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error)
	GetStats(ctx context.Context) (*models.FeedbackStats, error)
}

type FeedbackService struct {
	repo        *FeedbackRepository
	githubClient *GitHubClient
	logger      *logrus.Logger
}

func NewFeedbackService(db *pgxpool.Pool, githubToken string) (*FeedbackService, error) {
	repo := NewFeedbackRepository(db)
	githubClient := NewGitHubClient(githubToken, GetLogger())
	
	return &FeedbackService{
		repo:        repo,
		githubClient: githubClient,
		logger:      GetLogger(),
	}, nil
}

func (s *FeedbackService) SubmitFeedback(ctx context.Context, playerID uuid.UUID, req *models.SubmitFeedbackRequest) (*models.SubmitFeedbackResponse, error) {
	feedback := &models.Feedback{
		ID:          uuid.New(),
		PlayerID:    playerID,
		Type:        req.Type,
		Category:    req.Category,
		Title:       req.Title,
		Description: req.Description,
		Priority:    req.Priority,
		GameContext: req.GameContext,
		Screenshots: req.Screenshots,
		Status:      models.FeedbackStatusPending,
		VotesCount:  0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	if err := s.repo.Create(ctx, feedback); err != nil {
		return nil, err
	}

	issueNumber, issueURL, err := s.githubClient.CreateIssue(ctx, feedback)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to create GitHub issue")
	} else {
		feedback.GithubIssueNumber = &issueNumber
		feedback.GithubIssueURL = &issueURL
		if err := s.repo.UpdateStatus(ctx, feedback.ID, feedback.Status, &issueNumber, &issueURL); err != nil {
			s.logger.WithError(err).Warn("Failed to update GitHub issue info")
		}
	}

	return &models.SubmitFeedbackResponse{
		ID:                feedback.ID,
		Status:            feedback.Status,
		GithubIssueNumber: feedback.GithubIssueNumber,
		GithubIssueURL:    feedback.GithubIssueURL,
		CreatedAt:         feedback.CreatedAt,
	}, nil
}

func (s *FeedbackService) GetFeedback(ctx context.Context, id uuid.UUID) (*models.Feedback, error) {
	return s.repo.GetByID(ctx, id)
}

func (s *FeedbackService) GetPlayerFeedback(ctx context.Context, playerID uuid.UUID, status *models.FeedbackStatus, feedbackType *models.FeedbackType, limit, offset int) (*models.FeedbackList, error) {
	items, err := s.repo.GetByPlayerID(ctx, playerID, status, feedbackType, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountByPlayerID(ctx, playerID, status, feedbackType)
	if err != nil {
		return nil, err
	}

	return &models.FeedbackList{
		Items:  items,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *FeedbackService) UpdateStatus(ctx context.Context, id uuid.UUID, req *models.UpdateStatusRequest) (*models.Feedback, error) {
	if err := s.repo.UpdateStatus(ctx, id, req.Status, req.GithubIssueNumber, req.GithubIssueURL); err != nil {
		return nil, err
	}

	return s.repo.GetByID(ctx, id)
}

func (s *FeedbackService) GetBoard(ctx context.Context, category *models.FeedbackCategory, status *models.FeedbackStatus, search *string, sort string, limit, offset int) (*models.FeedbackBoardList, error) {
	items, err := s.repo.ListBoard(ctx, category, status, search, sort, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountBoard(ctx, category, status, search)
	if err != nil {
		return nil, err
	}

	return &models.FeedbackBoardList{
		Items:  items,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *FeedbackService) Vote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error) {
	if err := s.repo.Vote(ctx, feedbackID, playerID); err != nil {
		return nil, err
	}

	feedback, err := s.repo.GetByID(ctx, feedbackID)
	if err != nil {
		return nil, err
	}

	hasVoted, err := s.repo.HasVoted(ctx, feedbackID, playerID)
	if err != nil {
		return nil, err
	}

	return &models.VoteResponse{
		VotesCount: feedback.VotesCount,
		HasVoted:   hasVoted,
	}, nil
}

func (s *FeedbackService) Unvote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error) {
	if err := s.repo.Unvote(ctx, feedbackID, playerID); err != nil {
		return nil, err
	}

	feedback, err := s.repo.GetByID(ctx, feedbackID)
	if err != nil {
		return nil, err
	}

	hasVoted, err := s.repo.HasVoted(ctx, feedbackID, playerID)
	if err != nil {
		return nil, err
	}

	return &models.VoteResponse{
		VotesCount: feedback.VotesCount,
		HasVoted:   hasVoted,
	}, nil
}

func (s *FeedbackService) GetStats(ctx context.Context) (*models.FeedbackStats, error) {
	return s.repo.GetStats(ctx)
}








