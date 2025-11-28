package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

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

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"guild_id":    invitation.GuildID.String(),
			"character_id": characterID.String(),
			"rank":        string(member.Rank),
			"timestamp":   member.JoinedAt.Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "guild:member-joined", payload)
	}

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

func (s *SocialService) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	return s.guildRepo.GetInvitationsByCharacter(ctx, characterID)
}

