// Issue: #141888033
package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	ban, err := s.moderationService.CheckBan(ctx, message.SenderID, &message.ChannelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to check ban")
		return nil, err
	}
	if ban != nil {
		return nil, errors.New("user is banned from this channel")
	}

	isSpam, err := s.moderationService.DetectSpam(ctx, message.SenderID, message.Content)
	if err != nil {
		s.logger.WithError(err).Error("Failed to detect spam")
		return nil, err
	}
	if isSpam {
		autoBan, err := s.moderationService.AutoBanIfSpam(ctx, message.SenderID, &message.ChannelID)
		if err != nil {
			s.logger.WithError(err).Error("Failed to create auto-ban for spam")
		} else if autoBan != nil {
			s.logger.WithField("character_id", message.SenderID).Warn("Auto-ban created for spam")
		}
		return nil, errors.New("message detected as spam")
	}

	filtered, hasViolation, err := s.moderationService.FilterMessage(ctx, message.Content)
	if err != nil {
		s.logger.WithError(err).Error("Failed to filter message")
		return nil, err
	}
	message.Content = filtered

	if hasViolation {
		s.logger.WithField("sender_id", message.SenderID).Warn("Message filtered for violations")
		
		violationKey := "violations:character:" + message.SenderID.String()
		violationCount, err := s.cache.Incr(ctx, violationKey).Result()
		if err == nil {
			if violationCount == 1 {
				s.cache.Expire(ctx, violationKey, 1*time.Hour)
			}
			
			if violationCount >= 3 {
				autoBan, err := s.moderationService.AutoBanIfSevereViolation(ctx, message.SenderID, &message.ChannelID, int(violationCount))
				if err != nil {
					s.logger.WithError(err).Error("Failed to create auto-ban for severe violations")
				} else if autoBan != nil {
					s.logger.WithField("character_id", message.SenderID).Warn("Auto-ban created for severe violations")
					s.cache.Del(ctx, violationKey)
				}
			}
		}
	}

	return s.chatRepo.CreateMessage(ctx, message)
}

func (s *SocialService) GetMessages(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, int, error) {
	messages, err := s.chatRepo.GetMessagesByChannel(ctx, channelID, limit, offset)
	if err != nil {
		return nil, 0, err
	}

	total, err := s.chatRepo.CountMessagesByChannel(ctx, channelID)
	if err != nil {
		return nil, 0, err
	}

	return messages, total, nil
}

func (s *SocialService) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	return s.chatRepo.GetChannels(ctx, channelType)
}

func (s *SocialService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	return s.chatRepo.GetChannelByID(ctx, channelID)
}

