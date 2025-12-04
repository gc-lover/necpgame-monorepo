package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/voice-chat-service-go/models"
	"github.com/sirupsen/logrus"
)

type SubchannelRepositoryInterface interface {
	CreateSubchannel(ctx context.Context, lobbyID uuid.UUID, req *models.CreateSubchannelRequest) (*models.Subchannel, error)
	GetSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.Subchannel, error)
	ListSubchannels(ctx context.Context, lobbyID uuid.UUID) ([]models.Subchannel, error)
	UpdateSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID, req *models.UpdateSubchannelRequest) (*models.Subchannel, error)
	DeleteSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) error
	MoveParticipant(ctx context.Context, subchannelID, characterID uuid.UUID) error
	GetParticipants(ctx context.Context, subchannelID uuid.UUID) ([]models.SubchannelParticipant, error)
}

type SubchannelService struct {
	repo   SubchannelRepositoryInterface
	logger *logrus.Logger
}

func NewSubchannelService(repo SubchannelRepositoryInterface) *SubchannelService {
	return &SubchannelService{
		repo:   repo,
		logger: GetLogger(),
	}
}

func NewSubchannelServiceFromDB(dbURL string) (*SubchannelService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	repo := NewSubchannelRepository(dbPool)
	return NewSubchannelService(repo), nil
}

func (s *SubchannelService) CreateSubchannel(ctx context.Context, lobbyID uuid.UUID, req *models.CreateSubchannelRequest) (*models.Subchannel, error) {
	if req.Name == "" || len(req.Name) < 2 || len(req.Name) > 32 {
		return nil, fmt.Errorf("name must be between 2 and 32 characters")
	}

	if req.MaxParticipants != nil && (*req.MaxParticipants < 1 || *req.MaxParticipants > 100) {
		return nil, fmt.Errorf("max_participants must be between 1 and 100")
	}

	subchannel, err := s.repo.CreateSubchannel(ctx, lobbyID, req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create subchannel")
		return nil, err
	}

	return subchannel, nil
}

func (s *SubchannelService) GetSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.Subchannel, error) {
	subchannel, err := s.repo.GetSubchannel(ctx, lobbyID, subchannelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get subchannel")
		return nil, err
	}

	if subchannel == nil {
		return nil, pgx.ErrNoRows
	}

	return subchannel, nil
}

func (s *SubchannelService) ListSubchannels(ctx context.Context, lobbyID uuid.UUID) (*models.SubchannelListResponse, error) {
	subchannels, err := s.repo.ListSubchannels(ctx, lobbyID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list subchannels")
		return nil, err
	}

	return &models.SubchannelListResponse{
		LobbyID:     lobbyID,
		Subchannels: subchannels,
		TotalCount:  len(subchannels),
	}, nil
}

func (s *SubchannelService) UpdateSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID, req *models.UpdateSubchannelRequest) (*models.Subchannel, error) {
	if req.Name != nil && (len(*req.Name) < 2 || len(*req.Name) > 32) {
		return nil, fmt.Errorf("name must be between 2 and 32 characters")
	}

	if req.MaxParticipants != nil && (*req.MaxParticipants < 1 || *req.MaxParticipants > 100) {
		return nil, fmt.Errorf("max_participants must be between 1 and 100")
	}

	subchannel, err := s.repo.UpdateSubchannel(ctx, lobbyID, subchannelID, req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update subchannel")
		return nil, err
	}

	if subchannel == nil {
		return nil, pgx.ErrNoRows
	}

	return subchannel, nil
}

func (s *SubchannelService) DeleteSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) error {
	subchannel, err := s.repo.GetSubchannel(ctx, lobbyID, subchannelID)
	if err != nil {
		return err
	}

	if subchannel == nil {
		return pgx.ErrNoRows
	}

	if subchannel.SubchannelType == models.SubchannelTypeMain {
		return fmt.Errorf("cannot delete main subchannel")
	}

	err = s.repo.DeleteSubchannel(ctx, lobbyID, subchannelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete subchannel")
		return err
	}

	return nil
}

func (s *SubchannelService) MoveToSubchannel(ctx context.Context, lobbyID, subchannelID, characterID uuid.UUID, force bool) (*models.MoveToSubchannelResponse, error) {
	subchannel, err := s.repo.GetSubchannel(ctx, lobbyID, subchannelID)
	if err != nil {
		return nil, err
	}

	if subchannel == nil {
		return nil, pgx.ErrNoRows
	}

	if subchannel.IsLocked && !force {
		return nil, fmt.Errorf("subchannel is locked")
	}

	if subchannel.MaxParticipants != nil && subchannel.CurrentParticipants >= *subchannel.MaxParticipants {
		return nil, fmt.Errorf("subchannel is full")
	}

	err = s.repo.MoveParticipant(ctx, subchannelID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to move participant")
		return nil, err
	}

	return &models.MoveToSubchannelResponse{
		SubchannelID: subchannelID,
		CharacterID:  characterID,
		MovedAt:      time.Now(),
	}, nil
}

func (s *SubchannelService) GetSubchannelParticipants(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.SubchannelParticipantsResponse, error) {
	subchannel, err := s.repo.GetSubchannel(ctx, lobbyID, subchannelID)
	if err != nil {
		return nil, err
	}

	if subchannel == nil {
		return nil, pgx.ErrNoRows
	}

	participants, err := s.repo.GetParticipants(ctx, subchannelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get participants")
		return nil, err
	}

	return &models.SubchannelParticipantsResponse{
		SubchannelID: subchannelID,
		Participants: participants,
		TotalCount:   len(participants),
	}, nil
}

