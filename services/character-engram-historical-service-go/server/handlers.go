package server

import (
	"net/http"

	"github.com/necpgame/character-engram-historical-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type HistoricalEngramHandlers struct{}

func NewHistoricalEngramHandlers() *HistoricalEngramHandlers {
	return &HistoricalEngramHandlers{}
}

func (h *HistoricalEngramHandlers) GetHistoricalEngrams(w http.ResponseWriter, r *http.Request, params api.GetHistoricalEngramsParams) {
	engramId := openapi_types.UUID{}
	originalPersonId := openapi_types.UUID{}
	uniqueBonuses := []string{"Silverhand's Charisma", "Rock Legend"}
	costMultiplier := float32(3.5)
	storySignificance := api.High
	questId := openapi_types.UUID{}

	engrams := []api.HistoricalEngram{
		{
			EngramId:          engramId,
			OriginalPersonName: "Johnny Silverhand",
			OriginalPersonId:  &originalPersonId,
			HistoricalYear:    2023,
			AvailableFromYear: 2077,
			IsAvailable:       true,
			UniqueBonuses:     &uniqueBonuses,
			SpecialDialogues:  func() *bool { v := true; return &v }(),
			StorySignificance: &storySignificance,
			CostMultiplier:    &costMultiplier,
			QuestRequired:     func() *bool { v := true; return &v }(),
			QuestId:           &questId,
		},
	}

	respondJSON(w, http.StatusOK, engrams)
}

func (h *HistoricalEngramHandlers) GetHistoricalEngram(w http.ResponseWriter, r *http.Request, engramId openapi_types.UUID) {
	originalPersonId := openapi_types.UUID{}
	uniqueBonuses := []string{"Silverhand's Charisma", "Rock Legend"}
	costMultiplier := float32(3.5)
	storySignificance := api.High
	questId := openapi_types.UUID{}

	engram := api.HistoricalEngram{
		EngramId:          engramId,
		OriginalPersonName: "Johnny Silverhand",
		OriginalPersonId:  &originalPersonId,
		HistoricalYear:    2023,
		AvailableFromYear: 2077,
		IsAvailable:       true,
		UniqueBonuses:     &uniqueBonuses,
		SpecialDialogues:  func() *bool { v := true; return &v }(),
		StorySignificance: &storySignificance,
		CostMultiplier:    &costMultiplier,
		QuestRequired:     func() *bool { v := true; return &v }(),
		QuestId:           &questId,
	}

	respondJSON(w, http.StatusOK, engram)
}



















