// Package server SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"sync"
	"time"
)

// Service keeps in-memory state for advanced acrobatics.
type Service struct {
	mu        sync.RWMutex
	airDash   map[string]AirDashState
	wallKick  map[string]WallKickState
	vault     map[string]VaultState
	obstacles map[string]Obstacle
}

func NewService() *Service {
	return &Service{
		airDash:   make(map[string]AirDashState),
		wallKick:  make(map[string]WallKickState),
		vault:     make(map[string]VaultState),
		obstacles: make(map[string]Obstacle),
	}
}

func (s *Service) HandlePerformAirDash(w http.ResponseWriter, r *http.Request) {
	var req AirDashRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	ctx, cancel := context.WithTimeout(r.Context(), 1500*time.Millisecond)
	defer cancel()

	state := s.airDashPerform(req)
	writeJSON(w, state)
}

func (s *Service) HandleGetAirDashAvailability(w http.ResponseWriter, r *http.Request) {
	charID := r.URL.Query().Get("character_id")
	if charID == "" {
		http.Error(w, "missing character_id", http.StatusBadRequest)
		return
	}
	state := s.getAirDashState(charID)
	writeJSON(w, state)
}

func (s *Service) HandleGetAirDashCharges(w http.ResponseWriter, r *http.Request) {
	s.HandleGetAirDashAvailability(w, r)
}

func (s *Service) HandlePerformWallKick(w http.ResponseWriter, r *http.Request) {
	var req WallKickRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	ctx, cancel := context.WithTimeout(r.Context(), 1500*time.Millisecond)
	defer cancel()

	state := s.wallKickPerform(req)
	writeJSON(w, state)
}

func (s *Service) HandleGetWallKickAvailability(w http.ResponseWriter, r *http.Request) {
	charID := r.URL.Query().Get("character_id")
	if charID == "" {
		http.Error(w, "missing character_id", http.StatusBadRequest)
		return
	}
	state := s.getWallKickState(charID)
	writeJSON(w, state)
}

func (s *Service) HandlePerformVault(w http.ResponseWriter, r *http.Request) {
	var req VaultRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	ctx, cancel := context.WithTimeout(r.Context(), 1500*time.Millisecond)
	defer cancel()

	state := s.vaultPerform(req)
	writeJSON(w, state)
}

func (s *Service) HandleListVaultObstacles(w http.ResponseWriter, request *http.Request) {
	// Simple list; in a real service this would query spatial storage.
	s.mu.RLock()
	defer s.mu.RUnlock()

	res := make([]Obstacle, 0, len(s.obstacles))
	for _, o := range s.obstacles {
		res = append(res, o)
	}
	writeJSON(w, res)
}

func (s *Service) HandleGetAdvancedState(w http.ResponseWriter, r *http.Request) {
	charID := r.URL.Query().Get("character_id")
	if charID == "" {
		http.Error(w, "missing character_id", http.StatusBadRequest)
		return
	}
	state := AdvancedAcrobaticsState{
		CharacterID: charID,
		AirDash:     s.getAirDashState(charID),
		WallKick:    s.getWallKickState(charID),
		Vault:       s.getVaultState(charID),
	}
	writeJSON(w, state)
}

func (s *Service) airDashPerform(req AirDashRequest) AirDashState {
	s.mu.Lock()
	defer s.mu.Unlock()

	state := s.airDash[req.CharacterID]
	if state.MaxCharges == 0 {
		state.MaxCharges = 2
		state.CurrentCharges = 2
	}
	if state.CurrentCharges > 0 {
		state.CurrentCharges--
	}
	now := time.Now().UTC()
	state.CharacterID = req.CharacterID
	state.LastUsedAt = &now
	state.LastUsedDirection = req.Direction
	state.StaminaConsumed = maxInt(req.StaminaCost, 0)
	state.CooldownUntil = ptrTime(now.Add(2 * time.Second))
	s.airDash[req.CharacterID] = state
	return state
}

func (s *Service) getAirDashState(charID string) AirDashState {
	s.mu.RLock()
	defer s.mu.RUnlock()
	state := s.airDash[charID]
	if state.MaxCharges == 0 {
		state.MaxCharges = 2
		state.CurrentCharges = 2
		state.CharacterID = charID
	}
	return state
}

func (s *Service) wallKickPerform(req WallKickRequest) WallKickState {
	s.mu.Lock()
	defer s.mu.Unlock()

	state := s.wallKick[req.CharacterID]
	state.CharacterID = req.CharacterID
	state.IsAvailable = true
	state.ChainCount = req.ChainCount
	state.MaxChainCount = maxInt(req.ChainCount, 3)
	now := time.Now().UTC()
	state.LastUsedAt = &now
	state.LastUsedDirection = req.Direction
	state.StaminaConsumed = 8
	s.wallKick[req.CharacterID] = state
	return state
}

func (s *Service) getWallKickState(charID string) WallKickState {
	s.mu.RLock()
	defer s.mu.RUnlock()
	state := s.wallKick[charID]
	if state.MaxChainCount == 0 {
		state.MaxChainCount = 3
		state.IsAvailable = true
		state.CharacterID = charID
	}
	return state
}

func (s *Service) vaultPerform(req VaultRequest) VaultState {
	s.mu.Lock()
	defer s.mu.Unlock()

	state := s.vault[req.CharacterID]
	state.CharacterID = req.CharacterID
	state.IsActive = true
	state.ObstacleID = req.ObstacleID
	state.Direction = req.Direction
	now := time.Now().UTC()
	state.StartedAt = &now
	state.StaminaConsumed = 6

	if req.ManualMode {
		state.StaminaConsumed += 1
	}

	s.vault[req.CharacterID] = state
	return state
}

func (s *Service) getVaultState(charID string) VaultState {
	s.mu.RLock()
	defer s.mu.RUnlock()
	state := s.vault[charID]
	state.CharacterID = charID
	return state
}

func writeJSON(w http.ResponseWriter, v any) {
	w.Header().Set("Content-Type", "application/json")
	enc := json.NewEncoder(w)
	enc.SetEscapeHTML(false)
	if err := enc.Encode(v); err != nil {
		http.Error(w, "encode error", http.StatusInternalServerError)
	}
}

func ptrTime(t time.Time) *time.Time { return &t }

func maxInt(v int, fallback int) int {
	if v > fallback {
		return v
	}
	return fallback
}
