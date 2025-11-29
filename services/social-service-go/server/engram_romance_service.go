package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	ErrEngramNotFound = errors.New("engram not found")
)

type EngramRomanceServiceInterface interface {
	CreateRomanceComment(ctx context.Context, engramID uuid.UUID, characterID uuid.UUID, romanceEventType string, partnerID *uuid.UUID, eventContext map[string]interface{}, influenceLevel float64) (*RomanceCommentResult, error)
	GetEngramRomanceInfluence(ctx context.Context, engramID uuid.UUID, relationshipID *uuid.UUID) (*EngramRomanceInfluenceResult, error)
}

type RomanceCommentResult struct {
	CommentID       uuid.UUID   `json:"comment_id"`
	EngramID        uuid.UUID   `json:"engram_id"`
	CharacterID     uuid.UUID   `json:"character_id"`
	CommentText     string      `json:"comment_text"`
	RomanceEventType string     `json:"romance_event_type"`
	InfluenceLevel  float64     `json:"influence_level"`
	CreatedAt       time.Time   `json:"created_at"`
}

type EngramRomanceInfluenceResult struct {
	EngramID            uuid.UUID              `json:"engram_id"`
	InfluenceLevel      float64                `json:"influence_level"`
	InfluenceCategory   string                 `json:"influence_category"`
	EngramType          *string                `json:"engram_type,omitempty"`
	RelationshipImpact  *RelationshipImpact    `json:"relationship_impact,omitempty"`
	SpecialEvents       []string               `json:"special_events"`
}

type RelationshipImpact struct {
	HelpsRelationship    bool    `json:"helps_relationship"`
	InterferesRelationship bool  `json:"interferes_relationship"`
	ImpactPercentage     float64 `json:"impact_percentage"`
}

var romanceCommentTexts = map[string]map[string]string{
	"kiss": {
		"friendly":    "Мило, но я бы поцеловал иначе...",
		"aggressive":  "Слишком мягко!",
		"romantic":    "Как красиво...",
		"jealous":     "И зачем это нужно?",
	},
	"intimate": {
		"friendly":    "Ого, интересный момент...",
		"aggressive":  "Хм, ожидал большего.",
		"romantic":    "Как романтично...",
		"jealous":     "Серьезно?",
	},
	"dialogue": {
		"friendly":    "Неплохой разговор.",
		"aggressive":  "Слишком много слов.",
		"romantic":    "Как мило говоришь...",
		"jealous":     "Опять болтаешь?",
	},
	"conflict": {
		"friendly":    "Конфликты неизбежны.",
		"aggressive":  "Да, давай разбираться!",
		"romantic":    "Не стоит ссориться...",
		"jealous":     "Точно, проблема!",
	},
	"breakup": {
		"friendly":    "Время двигаться дальше.",
		"aggressive":  "Наконец-то!",
		"romantic":    "Как печально...",
		"jealous":     "Ожидаемо.",
	},
}

type EngramRomanceService struct {
	repo  EngramRomanceRepositoryInterface
	cache *redis.Client
	logger *logrus.Logger
}

func NewEngramRomanceService(repo EngramRomanceRepositoryInterface, cache *redis.Client) *EngramRomanceService {
	return &EngramRomanceService{
		repo:   repo,
		cache:  cache,
		logger: GetLogger(),
	}
}

func (s *EngramRomanceService) CreateRomanceComment(ctx context.Context, engramID uuid.UUID, characterID uuid.UUID, romanceEventType string, partnerID *uuid.UUID, eventContext map[string]interface{}, influenceLevel float64) (*RomanceCommentResult, error) {
	engramType := "friendly"
	if eventContext != nil {
		if et, ok := eventContext["engram_type"].(string); ok {
			engramType = et
		}
	}

	commentText := "Интересный выбор..."
	if eventComments, ok := romanceCommentTexts[romanceEventType]; ok {
		if text, ok := eventComments[engramType]; ok {
			commentText = text
		}
	}

	comment := &EngramRomanceComment{
		EngramID:         engramID,
		CharacterID:      characterID,
		CommentText:      commentText,
		RomanceEventType: romanceEventType,
		PartnerID:        partnerID,
		EventContext:     eventContext,
		InfluenceLevel:   influenceLevel,
	}

	err := s.repo.CreateRomanceComment(ctx, comment)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create romance comment")
		return nil, err
	}

	return &RomanceCommentResult{
		CommentID:        comment.CommentID,
		EngramID:         comment.EngramID,
		CharacterID:      comment.CharacterID,
		CommentText:      comment.CommentText,
		RomanceEventType: comment.RomanceEventType,
		InfluenceLevel:   comment.InfluenceLevel,
		CreatedAt:        comment.CreatedAt,
	}, nil
}

func (s *EngramRomanceService) GetEngramRomanceInfluence(ctx context.Context, engramID uuid.UUID, relationshipID *uuid.UUID) (*EngramRomanceInfluenceResult, error) {
	influence, err := s.repo.GetRomanceInfluence(ctx, engramID, relationshipID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get engram romance influence")
		return nil, err
	}

	if influence == nil {
		return &EngramRomanceInfluenceResult{
			EngramID:          engramID,
			InfluenceLevel:    0.0,
			InfluenceCategory: "low",
			SpecialEvents:     []string{},
		}, nil
	}

	result := &EngramRomanceInfluenceResult{
		EngramID:          influence.EngramID,
		InfluenceLevel:    influence.InfluenceLevel,
		InfluenceCategory: influence.InfluenceCategory,
		EngramType:        influence.EngramType,
		SpecialEvents:     influence.SpecialEvents,
	}

	if influence.HelpsRelationship || influence.InterferesRelationship {
		result.RelationshipImpact = &RelationshipImpact{
			HelpsRelationship:      influence.HelpsRelationship,
			InterferesRelationship: influence.InterferesRelationship,
			ImpactPercentage:       influence.ImpactPercentage,
		}
	}

	return result, nil
}



