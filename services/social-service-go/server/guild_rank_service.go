package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) GetGuildRanks(ctx context.Context, guildID uuid.UUID) (*models.GuildRanksResponse, error) {
	ranks, err := s.guildRepo.GetRanks(ctx, guildID)
	if err != nil {
		return nil, err
	}

	return &models.GuildRanksResponse{
		Ranks: ranks,
		Total: len(ranks),
	}, nil
}

func (s *SocialService) CreateGuildRank(ctx context.Context, guildID uuid.UUID, leaderID uuid.UUID, req *models.CreateGuildRankRequest) (*models.GuildRankEntity, error) {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if guild == nil || guild.LeaderID != leaderID {
		return nil, nil
	}

	ranks, err := s.guildRepo.GetRanks(ctx, guildID)
	if err != nil {
		return nil, err
	}

	maxOrder := 0
	for _, rank := range ranks {
		if rank.Order > maxOrder {
			maxOrder = rank.Order
		}
	}

	rank := &models.GuildRankEntity{
		ID:          uuid.New(),
		GuildID:     guildID,
		Name:        req.Name,
		Permissions: req.Permissions,
		Order:       maxOrder + 1,
		CreatedAt:   time.Now(),
	}

	err = s.guildRepo.CreateRank(ctx, rank)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guildID)
	return rank, nil
}

func (s *SocialService) UpdateGuildRank(ctx context.Context, guildID, rankID, leaderID uuid.UUID, req *models.UpdateGuildRankRequest) (*models.GuildRankEntity, error) {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if guild == nil || guild.LeaderID != leaderID {
		return nil, nil
	}

	rank, err := s.guildRepo.GetRankByID(ctx, rankID)
	if err != nil {
		return nil, err
	}
	if rank == nil || rank.GuildID != guildID {
		return nil, nil
	}

	if req.Name != nil {
		rank.Name = *req.Name
	}
	if req.Permissions != nil {
		rank.Permissions = req.Permissions
	}
	if req.Order != nil {
		rank.Order = *req.Order
	}

	err = s.guildRepo.UpdateRank(ctx, rank)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guildID)
	return rank, nil
}

func (s *SocialService) DeleteGuildRank(ctx context.Context, guildID, rankID, leaderID uuid.UUID) error {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return err
	}
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}

	err = s.guildRepo.DeleteRank(ctx, guildID, rankID)
	if err != nil {
		return err
	}

	s.invalidateGuildCache(ctx, guildID)
	return nil
}

