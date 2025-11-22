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
	return guild, nil
}

func (s *SocialService) GetGuild(ctx context.Context, id uuid.UUID) (*models.GuildDetailResponse, error) {
	cacheKey := "guild:" + id.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.GuildDetailResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
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

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)

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

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 2*time.Minute)

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

func (s *SocialService) InviteMember(ctx context.Context, guildID, inviterID uuid.UUID, req *models.InviteMemberRequest) (*models.GuildInvitation, error) {
	member, err := s.guildRepo.GetMember(ctx, guildID, inviterID)
	if err != nil {
		return nil, err
	}
	if member == nil || (member.Rank != models.GuildRankLeader && member.Rank != models.GuildRankOfficer) {
		return nil, nil
	}

	existing, _ := s.guildRepo.GetMember(ctx, guildID, req.CharacterID)
	if existing != nil {
		return nil, nil
	}

	invitation := &models.GuildInvitation{
		ID:          uuid.New(),
		GuildID:     guildID,
		CharacterID: req.CharacterID,
		InvitedBy:   inviterID,
		Message:     req.Message,
		Status:      "pending",
		CreatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
	}

	err = s.guildRepo.CreateInvitation(ctx, invitation)
	if err != nil {
		return nil, err
	}

	return invitation, nil
}

func (s *SocialService) AcceptInvitation(ctx context.Context, invitationID, characterID uuid.UUID) error {
	invitation, err := s.guildRepo.GetInvitation(ctx, invitationID)
	if err != nil {
		return err
	}
	if invitation == nil || invitation.CharacterID != characterID {
		return nil
	}

	guild, err := s.guildRepo.GetByID(ctx, invitation.GuildID)
	if err != nil {
		return err
	}
	if guild == nil {
		return nil
	}

	count, err := s.guildRepo.CountMembers(ctx, invitation.GuildID)
	if err != nil {
		return err
	}
	if count >= guild.MaxMembers {
		return nil
	}

	err = s.guildRepo.AcceptInvitation(ctx, invitationID)
	if err != nil {
		return err
	}

	member := &models.GuildMember{
		ID:           uuid.New(),
		GuildID:      invitation.GuildID,
		CharacterID: characterID,
		Rank:         models.GuildRankRecruit,
		Status:       models.GuildMemberStatusActive,
		Contribution: 0,
		JoinedAt:     time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = s.guildRepo.AddMember(ctx, member)
	if err != nil {
		return err
	}

	s.invalidateGuildCache(ctx, invitation.GuildID)
	return nil
}

func (s *SocialService) RejectInvitation(ctx context.Context, invitationID uuid.UUID) error {
	return s.guildRepo.RejectInvitation(ctx, invitationID)
}

func (s *SocialService) GetGuildMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildMemberListResponse, error) {
	members, err := s.guildRepo.GetMembers(ctx, guildID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.guildRepo.CountMembers(ctx, guildID)
	if err != nil {
		return nil, err
	}

	return &models.GuildMemberListResponse{
		Members: members,
		Total:   total,
	}, nil
}

func (s *SocialService) UpdateMemberRank(ctx context.Context, guildID, leaderID, characterID uuid.UUID, rank models.GuildRank) error {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return err
	}
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}

	return s.guildRepo.UpdateMemberRank(ctx, guildID, characterID, rank)
}

func (s *SocialService) RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	return s.guildRepo.RemoveMember(ctx, guildID, characterID)
}

func (s *SocialService) KickMember(ctx context.Context, guildID, leaderID, characterID uuid.UUID) error {
	member, err := s.guildRepo.GetMember(ctx, guildID, leaderID)
	if err != nil {
		return err
	}
	if member == nil || (member.Rank != models.GuildRankLeader && member.Rank != models.GuildRankOfficer) {
		return nil
	}

	return s.guildRepo.KickMember(ctx, guildID, characterID)
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

func (s *SocialService) GetGuildBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	return s.guildRepo.GetBank(ctx, guildID)
}

func (s *SocialService) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	return s.guildRepo.GetInvitationsByCharacter(ctx, characterID)
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

