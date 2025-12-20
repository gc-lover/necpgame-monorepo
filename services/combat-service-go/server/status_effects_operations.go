package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
)

// GetStatusEffects retrieves active status effects for character
func (s *CombatService) GetStatusEffects(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	// TODO: Load from database/cache
	effects := []*StatusEffect{
		{
			EffectID:   "regen_001",
			EffectType: "BUFF",
			Name:       "Health Regeneration",
			Description: "Restores 5 HP per second",
			Duration:   60,
			Stacks:     1,
			AppliedAt:  time.Now(),
		},
	}

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.statusEffectsResponsePool.Get().(*StatusEffectsResponse)
	defer s.statusEffectsResponsePool.Put(resp)

	resp.CharacterID = characterID
	resp.Effects = effects

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// ApplyStatusEffect applies status effect to character
func (s *CombatService) ApplyStatusEffect(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req ApplyStatusEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode apply status effect request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// TODO: Apply effect to character

	resp := &ApplyStatusEffectResponse{
		EffectID: req.EffectID,
		Applied:  true,
		Duration: 60,
		Message:  "Status effect applied successfully",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}
