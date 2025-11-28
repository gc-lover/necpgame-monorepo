package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) CreateGuild(ctx context.Context, leaderID uuid.UUID, req *models.CreateGuildRequest) (*models.Guild, error) {
	existing, _ := s.guildRepo.GetByName(ctx, req.Name)
	if existing != nil {
		return nil, nil
	}

	existing, _ = s.guildRepo.GetByTag(ctx, req.Tag)
	if existing != nil {
		return nil, nil
	}

	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        req.Name,
		Tag:         req.Tag,
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: req.Description,
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := s.guildRepo.Create(ctx, guild)
	if err != nil {
		return nil, err
	}

	member := &models.GuildMember{
		ID:           uuid.New(),
		GuildID:      guild.ID,
		CharacterID: leaderID,
		Rank:         models.GuildRankLeader,
		Status:       models.GuildMemberStatusActive,
		Contribution: 0,
		JoinedAt:     time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = s.guildRepo.AddMember(ctx, member)
	if err != nil {
		return nil, err
	}

	bank := &models.GuildBank{
		ID:        uuid.New(),
		GuildID:   guild.ID,
		Currency:  make(map[string]int),
		Items:     []map[string]interface{}{},
		UpdatedAt: time.Now(),
	}

	err = s.guildRepo.CreateBank(ctx, bank)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guild.ID)

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"guild_id":   guild.ID.String(),
			"leader_id":  leaderID.String(),
			"name":       guild.Name,
			"tag":        guild.Tag,
			"level":      guild.Level,
			"max_members": guild.MaxMembers,
			"timestamp":  guild.CreatedAt.Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "guild:created", payload)
	}

	return guild, nil
}

func (s *SocialService) GetGuild(ctx context.Context, id uuid.UUID) (*models.GuildDetailResponse, error) {
	cacheKey := "guild:" + id.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.GuildDetailResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		} else {
			s.logger.WithError(err).Error("Failed to unmarshal cached guild JSON")
		}
	}

	guild, err := s.guildRepo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if guild == nil {
		return nil, nil
	}

	members, err := s.guildRepo.GetMembers(ctx, id, 100, 0)
	if err != nil {
		return nil, err
	}

	bank, _ := s.guildRepo.GetBank(ctx, id)

	response := &models.GuildDetailResponse{
		Guild:   *guild,
		Members: members,
		Bank:    bank,
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal guild response JSON")
	} else {
		s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)
	}

	return response, nil
}

func (s *SocialService) ListGuilds(ctx context.Context, limit, offset int) (*models.GuildListResponse, error) {
	cacheKey := "guilds:list:limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.GuildListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	guilds, err := s.guildRepo.List(ctx, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.guildRepo.Count(ctx)
	if err != nil {
		return nil, err
	}

	response := &models.GuildListResponse{
		Guilds: guilds,
		Total:  total,
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal guilds list response JSON")
	} else {
		s.cache.Set(ctx, cacheKey, responseJSON, 2*time.Minute)
	}

	return response, nil
}

func (s *SocialService) UpdateGuild(ctx context.Context, guildID uuid.UUID, leaderID uuid.UUID, req *models.UpdateGuildRequest) (*models.Guild, error) {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if guild == nil {
		return nil, nil
	}

	if guild.LeaderID != leaderID {
		return nil, nil
	}

	if req.Name != nil {
		guild.Name = *req.Name
	}
	if req.Description != nil {
		guild.Description = *req.Description
	}
	guild.UpdatedAt = time.Now()

	err = s.guildRepo.Update(ctx, guild)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guildID)
	return guild, nil
}

func (s *SocialService) DisbandGuild(ctx context.Context, guildID, leaderID uuid.UUID) error {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return err
	}
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}

	return s.guildRepo.Disband(ctx, guildID)
}

func (s *SocialService) invalidateGuildCache(ctx context.Context, guildID uuid.UUID) {
	pattern := "guild:" + guildID.String()
	s.cache.Del(ctx, pattern)
	pattern = "guilds:list:*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}
