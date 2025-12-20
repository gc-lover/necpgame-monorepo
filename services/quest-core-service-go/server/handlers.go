// Package server provides HTTP server implementation for the quest core service.
package server

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/redis/go-redis/v9"
	"gopkg.in/yaml.v3"
)

const (
	DBTimeout = 50 * time.Millisecond
)

var (
	ErrNotFound = errors.New("not found")
)

// ReloadQuestContentRequest - request for importing quest from YAML
type ReloadQuestContentRequest struct {
	QuestID     string `json:"quest_id"`
	YamlContent string `json:"yaml_content"`
}

// ReloadQuestContentResponse - response for quest import
type ReloadQuestContentResponse struct {
	QuestID string `json:"quest_id"`
	Message string `json:"message"`
}

// QuestDefinition represents a quest definition for import
// OPTIMIZATION: Struct field alignment (large → small) Issue #300
type QuestDefinition struct {
	QuestID      string // 16 bytes
	Title        string // 16 bytes
	QuestType    string // 16 bytes
	Requirements string // JSON - 16 bytes
	Objectives   string // JSON - 16 bytes
	Rewards      string // JSON - 16 bytes
	ContentData  string // Full YAML as JSON - 16 bytes
	LevelMin     int    // 8 bytes
	LevelMax     int    // 8 bytes
	IsActive     bool   // 1 byte
}

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(redisClient *redis.Client) *Handlers {
	repo := NewRepository()
	service := NewService(repo, redisClient) // Issue: #1609 - pass Redis client
	return &Handlers{service: service}
}

// StartQuest - TYPED response!
func (h *Handlers) StartQuest(ctx context.Context, req *api.StartQuestRequest) (api.StartQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.StartQuest(ctx, req)
	if err != nil {
		return &api.StartQuestInternalServerError{}, err
	}

	return result, nil
}

// GetQuest - TYPED response!
func (h *Handlers) GetQuest(ctx context.Context, params api.GetQuestParams) (api.GetQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetQuest(ctx, params.QuestID)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetQuestNotFound{}, nil
		}
		return &api.GetQuestInternalServerError{}, err
	}

	return result, nil
}

// GetPlayerQuests - TYPED response!
func (h *Handlers) GetPlayerQuests(ctx context.Context, params api.GetPlayerQuestsParams) (api.GetPlayerQuestsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetPlayerQuests(ctx, params)
	if err != nil {
		return &api.GetPlayerQuestsInternalServerError{}, err
	}

	return result, nil
}

// CancelQuest - TYPED response!
func (h *Handlers) CancelQuest(ctx context.Context, params api.CancelQuestParams) (api.CancelQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CancelQuest(ctx, params.QuestID)
	if err != nil {
		if err == ErrNotFound {
			return &api.CancelQuestNotFound{}, nil
		}
		return &api.CancelQuestInternalServerError{}, err
	}

	return result, nil
}

// CompleteQuest - TYPED response!
func (h *Handlers) CompleteQuest(ctx context.Context, req api.OptCompleteQuestRequest, params api.CompleteQuestParams) (api.CompleteQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var reqPtr *api.CompleteQuestRequest
	if req.IsSet() {
		reqPtr = &req.Value
	}

	result, err := h.service.CompleteQuest(ctx, params.QuestID)
	if err != nil {
		if err == ErrNotFound {
			return &api.CompleteQuestNotFound{}, nil
		}
		return &api.CompleteQuestInternalServerError{}, err
	}

	return result, nil
}

// ReloadQuestContent - import quest from YAML content
func (h *Handlers) ReloadQuestContent(ctx context.Context, req *ReloadQuestContentRequest) (*ReloadQuestContentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Parse YAML content into quest definition
	questDef, err := h.parseQuestYAML(req.YamlContent)
	if err != nil {
		return nil, fmt.Errorf("failed to parse YAML content: %w", err)
	}

	// Import quest into database
	err = h.service.ImportQuestDefinition(ctx, questDef)
	if err != nil {
		return nil, errors.New("failed to import quest: " + err.Error())
	}

	return &ReloadQuestContentResponse{
		QuestID: req.QuestID,
		Message: "Quest imported successfully",
	}, nil
}

// parseQuestYAML parses YAML content into QuestDefinition struct
func (h *Handlers) parseQuestYAML(yamlContent string) (*QuestDefinition, error) {
	// Validate required content
	if yamlContent == "" {
		return nil, errors.New("YAML content is empty")
	}

	// Parse YAML string into map
	var yamlData map[string]interface{}
	if err := yaml.Unmarshal([]byte(yamlContent), &yamlData); err != nil {
		return nil, fmt.Errorf("failed to parse YAML: %w", err)
	}

	// Extract metadata
	metadata, ok := yamlData["metadata"].(map[string]interface{})
	if !ok {
		return nil, errors.New("missing or invalid metadata section")
	}

	// Validate metadata fields
	metadataID, ok := metadata["id"].(string)
	if !ok || metadataID == "" {
		return nil, errors.New("metadata.id must be a non-empty string")
	}

	metadataTitle, ok := metadata["title"].(string)
	if !ok || metadataTitle == "" {
		return nil, errors.New("metadata.title must be a non-empty string")
	}

	questID := metadataID
	title := metadataTitle

	// Extract quest_definition
	questDefData, ok := yamlData["quest_definition"].(map[string]interface{})
	if !ok {
		return nil, errors.New("missing or invalid quest_definition section")
	}

	questType, _ := questDefData["quest_type"].(string)
	levelMin, _ := questDefData["level_min"].(float64) // YAML numbers are float64
	levelMax, _ := questDefData["level_max"].(float64)

	// Extract requirements, objectives, rewards
	requirements, _ := questDefData["requirements"].(map[string]interface{})
	objectives, _ := questDefData["objectives"].([]interface{})
	rewards, _ := questDefData["rewards"].(map[string]interface{})

	// Convert objectives to JSON
	objectivesJSON, err := json.Marshal(objectives)
	if err != nil {
		return nil, errors.New("failed to marshal objectives: " + err.Error())
	}

	// Convert requirements and rewards to JSON
	requirementsJSON, err := json.Marshal(requirements)
	if err != nil {
		return nil, errors.New("failed to marshal requirements: " + err.Error())
	}

	rewardsJSON, err := json.Marshal(rewards)
	if err != nil {
		return nil, errors.New("failed to marshal rewards: " + err.Error())
	}

	// Create QuestDefinition
	questDef := &QuestDefinition{
		QuestID:      questID,
		Title:        title,
		QuestType:    questType,
		LevelMin:     int(levelMin),
		LevelMax:     int(levelMax),
		Requirements: string(requirementsJSON),
		Objectives:   string(objectivesJSON),
		Rewards:      string(rewardsJSON),
		ContentData:  "{}", // Will be populated from yamlContent
		IsActive:     true,
	}

	// Convert full content to JSON for ContentData
	contentJSON, err := json.Marshal(yamlContent)
	if err != nil {
		return nil, errors.New("failed to marshal content data: " + err.Error())
	}
	questDef.ContentData = string(contentJSON)

	return questDef, nil
}
