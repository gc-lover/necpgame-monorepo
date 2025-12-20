package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// InitiateCombat creates new combat session
func (s *CombatService) InitiateCombat(w http.ResponseWriter, r *http.Request) {
	var req InitiateCombatRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode initiate combat request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate request
	if req.AttackerID == "" || req.DefenderID == "" {
		http.Error(w, "Attacker and defender IDs are required", http.StatusBadRequest)
		return
	}

	// Generate combat ID
	combatID := uuid.New().String()

	// Create combat session
	session := &CombatSession{
		CombatID:   combatID,
		Status:     "INITIATED",
		CombatType: req.CombatType,
		Participants: []*CombatParticipant{
			{
				CharacterID: req.AttackerID,
				Health:      100, // TODO: Load from character service
				MaxHealth:   100,
				Position:    &Vector3{X: 0, Y: 0, Z: 0},
				IsAlive:     true,
				Team:        "attacker",
			},
			{
				CharacterID: req.DefenderID,
				Health:      100, // TODO: Load from character service
				MaxHealth:   100,
				Position:    &Vector3{X: 10, Y: 0, Z: 0},
				IsAlive:     true,
				Team:        "defender",
			},
		},
		CurrentTurn: req.AttackerID,
		StartTime:   time.Now(),
		TimeLimit:   5 * time.Minute, // 5 minute combat limit
		Location:    req.Location,
	}

	// Store session
	s.activeCombats.Store(combatID, session)
	s.metrics.ActiveCombats.Inc()

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.initiateCombatResponsePool.Get().(*InitiateCombatResponse)
	defer s.initiateCombatResponsePool.Put(resp)

	resp.CombatID = combatID
	resp.Status = "INITIATED"
	resp.Participants = make([]*CombatParticipant, len(session.Participants))
	copy(resp.Participants, session.Participants)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("combat_id", combatID).Info("combat session initiated")
}

// GetCombatStatus retrieves current combat status
func (s *CombatService) GetCombatStatus(w http.ResponseWriter, r *http.Request) {
	combatID := chi.URLParam(r, "combatId")
	if combatID == "" {
		http.Error(w, "Combat ID is required", http.StatusBadRequest)
		return
	}

	session, exists := s.activeCombats.Load(combatID)
	if !exists {
		http.Error(w, "Combat session not found", http.StatusNotFound)
		return
	}

	combat := session.(*CombatSession)

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.combatStatusResponsePool.Get().(*CombatStatusResponse)
	defer s.combatStatusResponsePool.Put(resp)

	resp.CombatID = combat.CombatID
	resp.Status = combat.Status
	resp.Participants = make([]*CombatParticipant, len(combat.Participants))
	copy(resp.Participants, combat.Participants)
	resp.CurrentTurn = combat.CurrentTurn
	resp.TimeRemaining = int((combat.TimeLimit - time.Since(combat.StartTime)).Seconds())
	resp.Events = make([]*CombatEvent, len(combat.Events))
	copy(resp.Events, combat.Events)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// ExecuteCombatAction processes combat actions
func (s *CombatService) ExecuteCombatAction(w http.ResponseWriter, r *http.Request) {
	combatID := chi.URLParam(r, "combatId")
	if combatID == "" {
		http.Error(w, "Combat ID is required", http.StatusBadRequest)
		return
	}

	var req CombatActionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode combat action request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	session, exists := s.activeCombats.Load(combatID)
	if !exists {
		http.Error(w, "Combat session not found", http.StatusNotFound)
		return
	}

	combat := session.(*CombatSession)
	s.metrics.CombatActions.Inc()

	// Process combat action (simplified for demo)
	actionID := uuid.New().String()

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.combatActionResponsePool.Get().(*CombatActionResponse)
	defer s.combatActionResponsePool.Put(resp)

	resp.ActionID = actionID
	resp.Success = true
	resp.Cooldown = 1000 // 1 second cooldown

	// Calculate damage if it's an attack
	if req.ActionType == "ATTACK" {
		damage := s.calculateDamage(req.ActionID, req.TargetID)
		resp.Damage = &damage
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"combat_id":  combatID,
		"action_id":  actionID,
		"action_type": req.ActionType,
	}).Info("combat action executed")
}

// EndCombat terminates combat session
func (s *CombatService) EndCombat(w http.ResponseWriter, r *http.Request) {
	combatID := chi.URLParam(r, "combatId")
	if combatID == "" {
		http.Error(w, "Combat ID is required", http.StatusBadRequest)
		return
	}

	session, exists := s.activeCombats.LoadAndDelete(combatID)
	if !exists {
		http.Error(w, "Combat session not found", http.StatusNotFound)
		return
	}

	combat := session.(*CombatSession)
	s.metrics.ActiveCombats.Dec()

	// Determine winner (simplified)
	var winner string
	for _, p := range combat.Participants {
		if p.IsAlive {
			winner = p.CharacterID
			break
		}
	}

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.endCombatResponsePool.Get().(*EndCombatResponse)
	defer s.endCombatResponsePool.Put(resp)

	resp.CombatID = combatID
	resp.Winner = winner
	resp.Duration = int(time.Since(combat.StartTime).Seconds())
	resp.ExperienceGained = 100 // TODO: Calculate based on combat difficulty

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"combat_id": combatID,
		"winner":    winner,
		"duration":  resp.Duration,
	}).Info("combat session ended")
}
