package server

import (
	"context"
	"encoding/json"
	"strconv"
	"strings"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Quest content import handlers

// ReloadQuestContent implements POST /gameplay/quests/content/reload
// Issue: #50
func (h *Handlers) ReloadQuestContent(ctx context.Context, req *api.ReloadQuestContentRequest) (api.ReloadQuestContentRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("ReloadQuestContent: questRepository not initialized")
		return &api.ReloadQuestContentInternalServerError{}, nil
	}

	if req == nil || strings.TrimSpace(req.QuestID) == "" {
		h.logger.Warn("ReloadQuestContent: empty request or quest_id")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	if len(req.YamlContent) == 0 {
		h.logger.Warn("ReloadQuestContent: yaml_content is empty")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	contentData := make(map[string]interface{}, len(req.YamlContent))
	for key, raw := range req.YamlContent {
		var decoded interface{}
		if err := json.Unmarshal(raw, &decoded); err != nil {
			h.logger.WithError(err).WithField("field", key).Warn("ReloadQuestContent: failed to decode yaml_content field")
			return &api.ReloadQuestContentBadRequest{}, nil
		}
		contentData[key] = decoded
	}

	metadata := extractMap(contentData, "metadata")
	if metaID := extractString(metadata, "id"); metaID != "" && metaID != req.QuestID {
		h.logger.WithFields(logrus.Fields{
			"quest_id":    req.QuestID,
			"metadata.id": metaID,
		}).Warn("ReloadQuestContent: quest_id mismatch with metadata.id")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	title := req.QuestID
	description := ""
	questType := "side"
	isActive := true
	version := 1

	if meta := metadata; len(meta) > 0 {
		if t := extractString(meta, "title"); t != "" {
			title = t
		}
		if v := extractString(meta, "version"); v != "" {
			if parsed, err := parseVersionMajor(v); err == nil && parsed > 0 {
				version = parsed
			}
		}
		if status := extractString(meta, "status"); status != "" {
			isActive = status != "archived"
		}
		if qt := extractString(meta, "quest_type"); qt != "" {
			questType = qt
		}
	}

	if summary := extractMap(contentData, "summary"); len(summary) > 0 {
		if essence := extractString(summary, "essence"); essence != "" {
			description = essence
		} else if goal := extractString(summary, "goal"); goal != "" {
			description = goal
		}
		if questType == "side" {
			if inferred := parseQuestType(extractSlice(summary, "points")); inferred != "" {
				questType = inferred
			}
		}
	}

	saved, err := h.questRepository.SaveQuest(ctx, req.QuestID, version, title, description, questType, isActive, contentData)
	if err != nil {
		h.logger.WithError(err).Error("ReloadQuestContent: failed to save quest")
		return &api.ReloadQuestContentInternalServerError{}, nil
	}

	now := time.Now()
	response := &api.ReloadQuestContentResponse{
		QuestID:    api.NewOptString(saved.QuestID),
		Message:    api.NewOptString("Quest content imported"),
		ImportedAt: api.NewOptDateTime(now),
	}

	return response, nil
}

func parseVersionMajor(raw string) (int, error) {
	for i := 0; i < len(raw); i++ {
		if raw[i] < '0' || raw[i] > '9' {
			if i == 0 {
				return 1, nil
			}
			raw = raw[:i]
			break
		}
	}
	return strconv.Atoi(raw)
}

func parseQuestType(points []interface{}) string {
	for _, p := range points {
		text, ok := p.(string)
		if !ok {
			continue
		}
		lower := strings.ToLower(text)
		if strings.Contains(lower, "тип") {
			parts := strings.SplitN(lower, "тип", 2)
			if len(parts) == 2 {
				return strings.TrimSpace(strings.Trim(parts[1], "-—: "))
			}
		}
	}
	return ""
}

func extractMap(payload map[string]interface{}, key string) map[string]interface{} {
	if payload == nil {
		return nil
	}
	val, ok := payload[key]
	if !ok {
		return nil
	}
	if m, ok := val.(map[string]interface{}); ok {
		return m
	}
	return nil
}

func extractSlice(payload map[string]interface{}, key string) []interface{} {
	if payload == nil {
		return nil
	}
	val, ok := payload[key]
	if !ok {
		return nil
	}
	if m, ok := val.([]interface{}); ok {
		return m
	}
	return nil
}

func extractString(payload map[string]interface{}, key string) string {
	if payload == nil {
		return ""
	}
	if val, ok := payload[key]; ok {
		if str, ok := val.(string); ok {
			return strings.TrimSpace(str)
		}
	}
	return ""
}
