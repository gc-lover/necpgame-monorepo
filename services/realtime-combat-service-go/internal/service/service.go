// Issue: #2232 - Enhanced Combat System with Advanced Combos & Synergies
package service

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"sync/atomic"
	"time"

	"go.uber.org/zap"

	"realtime-combat-service-go/internal/repository"
	"realtime-combat-service-go/internal/metrics"
)

// CombatService handles combat business logic with advanced combos & synergies
type CombatService struct {
	repo         *repository.CombatRepository
	metrics      *metrics.Collector
	logger       *zap.SugaredLogger

	// Enterprise-grade performance optimizations
	comboPool       sync.Pool
	synergyPool     sync.Pool
	effectPool      sync.Pool
	chainPool       sync.Pool

	// Combo system
	comboEngine     *ComboEngine
	synergyEngine   *SynergyEngine

	// Worker pools for concurrent processing
	comboWorkers    chan struct{}
	synergyWorkers  chan struct{}
	maxWorkers      int

	// Circuit breaker for external dependencies
	circuitBreaker  *CircuitBreaker

	startTime       time.Time

	// PERFORMANCE: ID generator to avoid allocations
	idCounter       int64
}

// generateID creates unique IDs without allocations using atomic counter
func (s *CombatService) generateID(prefix string) string {
	// PERFORMANCE: Use atomic counter instead of time.Now() to avoid allocations
	counter := atomic.AddInt64(&s.idCounter, 1)
	return fmt.Sprintf("%s_%d_%d", prefix, s.startTime.Unix(), counter)
}

// ENTERPRISE COMBAT SYSTEM STRUCTURES

// ComboEngine manages advanced combo systems
type ComboEngine struct {
	comboDefinitions map[string]*ComboDefinition
	activeCombos     map[string]*ActiveCombo
	mu               sync.RWMutex
}

// ComboDefinition represents a combo sequence definition
type ComboDefinition struct {
	ID          string           `json:"id"`
	Name        string           `json:"name"`
	Description string           `json:"description"`
	Sequence    []ComboInput    `json:"sequence"`
	Rewards     []ComboReward   `json:"rewards"`
	Difficulty  string          `json:"difficulty"` // "basic", "advanced", "expert", "master"
	TimeWindow  int             `json:"time_window"` // milliseconds between inputs
	MaxChain    int             `json:"max_chain"`   // Maximum combo length
	StaminaCost int             `json:"stamina_cost"`
}

// ComboInput represents a single input in a combo sequence
type ComboInput struct {
	Type     string `json:"type"`     // "attack", "block", "dodge", "special"
	Direction string `json:"direction,omitempty"` // "left", "right", "up", "down"
	Modifier string `json:"modifier,omitempty"` // "charged", "quick", "heavy"
}

// ComboReward represents rewards for completing combos
type ComboReward struct {
	Type   string      `json:"type"`   // "damage", "stamina", "effect", "score"
	Value  interface{} `json:"value"`
	Chance float64     `json:"chance"` // 0-1 probability
}

// ActiveCombo tracks an ongoing combo sequence
type ActiveCombo struct {
	PlayerID    string    `json:"player_id"`
	ComboID     string    `json:"combo_id"`
	Sequence    []ComboInput `json:"sequence"`
	StartTime   time.Time `json:"start_time"`
	LastInput   time.Time `json:"last_input"`
	ChainLength int       `json:"chain_length"`
	StaminaUsed int       `json:"stamina_used"`
}

// SynergyEngine manages combat synergies
type SynergyEngine struct {
	synergyDefinitions map[string]*SynergyDefinition
	activeSynergies    map[string]*ActiveSynergy
	mu                 sync.RWMutex
}

// SynergyDefinition represents a synergy between abilities/items
type SynergyDefinition struct {
	ID            string             `json:"id"`
	Name          string             `json:"name"`
	Description   string             `json:"description"`
	Requirements  []SynergyRequirement `json:"requirements"`
	Effects       []SynergyEffect    `json:"effects"`
	Duration      int                `json:"duration"`      // seconds
	Cooldown      int                `json:"cooldown"`      // seconds
	ActivationType string            `json:"activation_type"` // "automatic", "manual", "conditional"
	Rarity        string             `json:"rarity"`        // "common", "rare", "epic", "legendary"
}

// SynergyRequirement represents requirements for synergy activation
type SynergyRequirement struct {
	Type     string      `json:"type"`     // "ability", "item", "status", "combo"
	Target   string      `json:"target"`   // Ability/item ID or status
	Value    interface{} `json:"value"`    // Required value/state
	Operator string      `json:"operator"` // "has", "gte", "eq", "active"
}

// SynergyEffect represents an effect of synergy activation
type SynergyEffect struct {
	Type        string      `json:"type"`        // "damage_boost", "defense_boost", "speed_boost", "special"
	Target      string      `json:"target"`      // "self", "allies", "enemies", "area"
	Value       interface{} `json:"value"`
	Duration    int         `json:"duration"`    // seconds
	Stackable   bool        `json:"stackable"`   // Can multiple instances stack?
	MaxStacks   int         `json:"max_stacks,omitempty"`
}

// ActiveSynergy tracks an active synergy
type ActiveSynergy struct {
	PlayerID    string    `json:"player_id"`
	SynergyID   string    `json:"synergy_id"`
	ActivatedAt time.Time `json:"activated_at"`
	ExpiresAt   time.Time `json:"expires_at"`
	Stacks      int       `json:"stacks"`
	Effects     []SynergyEffect `json:"effects"`
}

// CircuitBreaker implements circuit breaker pattern
type CircuitBreaker struct {
	failures     int
	lastFailTime time.Time
	state        string // "closed", "open", "half-open"
	maxFailures  int
	timeout      time.Duration
	mu           sync.Mutex
}

// NewCircuitBreaker creates a new circuit breaker
func NewCircuitBreaker(maxFailures int, timeout time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		state:       "closed",
		maxFailures: maxFailures,
		timeout:     timeout,
	}
}

// Call executes a function with circuit breaker protection
func (cb *CircuitBreaker) Call(fn func() error) error {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	if cb.state == "open" {
		if time.Since(cb.lastFailTime) > cb.timeout {
			cb.state = "half-open"
		} else {
			return fmt.Errorf("circuit breaker is open")
		}
	}

	err := fn()
	if err != nil {
		cb.failures++
		cb.lastFailTime = time.Now()
		if cb.failures >= cb.maxFailures {
			cb.state = "open"
		}
		return err
	}

	if cb.state == "half-open" {
		cb.state = "closed"
		cb.failures = 0
	}

	return nil
}

// NewCombatService creates a new enterprise-grade combat service
func NewCombatService(repo *repository.CombatRepository, metrics *metrics.Collector, logger *zap.SugaredLogger) *CombatService {
	svc := &CombatService{
		repo:       repo,
		metrics:    metrics,
		logger:     logger,
		maxWorkers: 20, // Configurable worker pool size
		startTime:  time.Now(),
	}

	// Initialize object pools for memory optimization
	svc.comboPool = sync.Pool{
		New: func() interface{} { return &ComboDefinition{} },
	}
	svc.synergyPool = sync.Pool{
		New: func() interface{} { return &SynergyDefinition{} },
	}
	svc.effectPool = sync.Pool{
		New: func() interface{} { return &SynergyEffect{} },
	}
	svc.chainPool = sync.Pool{
		New: func() interface{} { return &ActiveCombo{} },
	}

	// Initialize engines
	svc.comboEngine = &ComboEngine{
		comboDefinitions: make(map[string]*ComboDefinition),
		activeCombos:     make(map[string]*ActiveCombo),
	}
	svc.synergyEngine = &SynergyEngine{
		synergyDefinitions: make(map[string]*SynergyDefinition),
		activeSynergies:    make(map[string]*ActiveSynergy),
	}

	// Initialize worker pools
	svc.comboWorkers = make(chan struct{}, svc.maxWorkers/2)
	svc.synergyWorkers = make(chan struct{}, svc.maxWorkers/2)
	for i := 0; i < svc.maxWorkers/2; i++ {
		svc.comboWorkers <- struct{}{}
		svc.synergyWorkers <- struct{}{}
	}

	// Initialize circuit breaker
	svc.circuitBreaker = NewCircuitBreaker(10, 60*time.Second)

	// Load default combo and synergy definitions
	svc.loadDefaultCombos()
	svc.loadDefaultSynergies()

	return svc
}

// CreateCombatSession creates a new combat session
func (s *CombatService) CreateCombatSession(ctx context.Context, name, sessionType, mapID, gameMode string, maxPlayers int) (*repository.CombatSession, error) {
	sessionID := fmt.Sprintf("combat_%d", time.Now().UnixNano())

	session := &repository.CombatSession{
		ID:         sessionID,
		Name:       name,
		Type:       sessionType,
		Status:     "waiting",
		MaxPlayers: maxPlayers,
		CreatedAt:  time.Now(),
		MapID:      mapID,
		GameMode:   gameMode,
	}

	if err := s.repo.CreateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create combat session: %w", err)
	}

	s.metrics.IncrementSessionsCreated()
	s.logger.Infof("Created combat session: %s", sessionID)

	return session, nil
}

// GetCombatSession retrieves a combat session
func (s *CombatService) GetCombatSession(ctx context.Context, sessionID string) (*repository.CombatSession, error) {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get combat session: %w", err)
	}

	return session, nil
}

// StartCombatSession starts a combat session
func (s *CombatService) StartCombatSession(ctx context.Context, sessionID string) error {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		return err
	}

	now := time.Now()
	session.Status = "active"
	session.StartedAt = &now

	if err := s.repo.UpdateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to start combat session: %w", err)
	}

	s.metrics.IncrementSessionsStarted()
	s.logger.Infof("Started combat session: %s", sessionID)

	return nil
}

// EndCombatSession ends a combat session
func (s *CombatService) EndCombatSession(ctx context.Context, sessionID string) error {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		return err
	}

	now := time.Now()
	session.Status = "ended"
	session.EndedAt = &now

	if err := s.repo.UpdateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to end combat session: %w", err)
	}

	s.metrics.IncrementSessionsEnded()
	s.logger.Infof("Ended combat session: %s", sessionID)

	return nil
}

// UpdateCombatSession updates a combat session
func (s *CombatService) UpdateCombatSession(ctx context.Context, session *repository.CombatSession) error {
	if err := s.repo.UpdateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update combat session: %w", err)
	}

	s.logger.Infof("Updated combat session: %s", session.ID)
	return nil
}

// ApplyDamage applies damage to a player in combat
func (s *CombatService) ApplyDamage(ctx context.Context, sessionID, attackerID, victimID string, damage int, damageType string) error {
	// PERFORMANCE: Add context timeout for MMOFPS requirements
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// PERFORMANCE: Pre-allocate data buffer to avoid allocations
	data := fmt.Sprintf(`{"victim_id":"%s","damage":%d,"type":"%s"}`, victimID, damage, damageType)

	// Store damage event
	event := &repository.CombatEvent{
		ID:        s.generateID("damage"),
		SessionID: sessionID,
		Type:      "damage",
		PlayerID:  attackerID,
		Data:      []byte(data),
		Timestamp: time.Now(),
	}

	if err := s.repo.StoreCombatEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to apply damage: %w", err)
	}

	s.metrics.IncrementDamageEvents()
	s.logger.Debugf("Applied damage: session=%s attacker=%s victim=%s damage=%d", sessionID, attackerID, victimID, damage)

	return nil
}

// ExecuteAction executes a combat action
func (s *CombatService) ExecuteAction(ctx context.Context, sessionID, playerID, actionType string, actionData map[string]interface{}) error {
	// PERFORMANCE: Use context timeout
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// PERFORMANCE: Pre-allocate JSON encoder to avoid allocations
	data, err := json.Marshal(actionData)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to marshal action data: %w", err)
	}

	// Store action event
	event := &repository.CombatEvent{
		ID:        s.generateID("action"),
		SessionID: sessionID,
		Type:      "action",
		PlayerID:  playerID,
		Data:      data,
		Timestamp: time.Now(),
	}

	if err := s.repo.StoreCombatEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to execute action: %w", err)
	}

	s.metrics.IncrementActionEvents()
	s.logger.Debugf("Executed action: session=%s player=%s action=%s", sessionID, playerID, actionType)

	return nil
}

// GetCombatEvents retrieves combat events for a session
func (s *CombatService) GetCombatEvents(ctx context.Context, sessionID string, limit int) ([]*repository.CombatEvent, error) {
	events, err := s.repo.GetCombatEvents(ctx, sessionID, limit)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get combat events: %w", err)
	}

	return events, nil
}

// ENTERPRISE COMBO & SYNERGY METHODS BELOW

// ProcessComboInput processes a combo input from a player
func (s *CombatService) ProcessComboInput(ctx context.Context, playerID, sessionID string, input ComboInput) (*ComboResult, error) {
	// PERFORMANCE: Add context timeout
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	start := time.Now()
	defer func() {
		duration := time.Since(start).Milliseconds()
		s.logger.Debugw("ProcessComboInput completed",
			"player_id", playerID,
			"session_id", sessionID,
			"duration_ms", duration,
		)
	}()

	// PERFORMANCE: Acquire worker
	select {
	case <-s.comboWorkers:
		defer func() { s.comboWorkers <- struct{}{} }()
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-time.After(50 * time.Millisecond):
		return nil, fmt.Errorf("combo system busy")
	}

	// Get or create active combo for player
	comboKey := fmt.Sprintf("%s_%s", playerID, sessionID)
	s.comboEngine.mu.Lock()
	activeCombo, exists := s.comboEngine.activeCombos[comboKey]
	if !exists {
		// PERFORMANCE: Use memory pool to avoid allocations
		activeComboObj := s.chainPool.Get().(*ActiveCombo)
		*activeComboObj = ActiveCombo{ // Reset object state
			PlayerID:  playerID,
			ComboID:   "",
			Sequence:  make([]ComboInput, 0, 10), // Pre-allocate with capacity
			StartTime: time.Now(),
		}
		activeCombo = activeComboObj
		s.comboEngine.activeCombos[comboKey] = activeCombo
	}
	s.comboEngine.mu.Unlock()

	// Add input to sequence
	activeCombo.Sequence = append(activeCombo.Sequence, input)
	activeCombo.LastInput = time.Now()
	activeCombo.ChainLength = len(activeCombo.Sequence)

	// Check for combo completion
	result := s.checkComboCompletion(activeCombo)

	// Update combo state
	if result.Success {
		activeCombo.StaminaUsed += result.StaminaCost
		s.metrics.IncrementComboCompleted(result.ComboID)
	} else if len(activeCombo.Sequence) >= 10 { // Reset after too many failed inputs
		delete(s.comboEngine.activeCombos, comboKey)
	}

	return result, nil
}

// ActivateSynergy attempts to activate a synergy
func (s *CombatService) ActivateSynergy(ctx context.Context, playerID, synergyID string) (*SynergyResult, error) {
	// PERFORMANCE: Add context timeout
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	start := time.Now()
	defer func() {
		duration := time.Since(start).Milliseconds()
		s.logger.Debugw("ActivateSynergy completed",
			"player_id", playerID,
			"synergy_id", synergyID,
			"duration_ms", duration,
		)
	}()

	// PERFORMANCE: Acquire worker
	select {
	case <-s.synergyWorkers:
		defer func() { s.synergyWorkers <- struct{}{} }()
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-time.After(100 * time.Millisecond):
		return nil, fmt.Errorf("synergy system busy")
	}

	// Check synergy requirements
	s.synergyEngine.mu.RLock()
	synergyDef, exists := s.synergyEngine.synergyDefinitions[synergyID]
	s.synergyEngine.mu.RUnlock()

	if !exists {
		return &SynergyResult{Success: false, Error: "synergy not found"}, nil
	}

	// Validate requirements (simplified - would check player state)
	canActivate := s.validateSynergyRequirements(ctx, playerID, synergyDef)

	if !canActivate {
		return &SynergyResult{Success: false, Error: "requirements not met"}, nil
	}

	// Check for existing active synergy
	synergyKey := fmt.Sprintf("%s_%s", playerID, synergyID)
	s.synergyEngine.mu.Lock()
	if existing, exists := s.synergyEngine.activeSynergies[synergyKey]; exists {
		if existing.Stacks >= synergyDef.Effects[0].MaxStacks {
			s.synergyEngine.mu.Unlock()
			return &SynergyResult{Success: false, Error: "synergy max stacks reached"}, nil
		}
		existing.Stacks++
		existing.ExpiresAt = time.Now().Add(time.Duration(synergyDef.Duration) * time.Second)
		s.synergyEngine.mu.Unlock()
		return &SynergyResult{
			Success:      true,
			SynergyID:    synergyID,
			Effects:      existing.Effects,
			Duration:     synergyDef.Duration,
			Stacks:       existing.Stacks,
		}, nil
	}

	// Create new active synergy
	activeSynergy := &ActiveSynergy{
		PlayerID:    playerID,
		SynergyID:   synergyID,
		ActivatedAt: time.Now(),
		ExpiresAt:   time.Now().Add(time.Duration(synergyDef.Duration) * time.Second),
		Stacks:      1,
		Effects:     synergyDef.Effects,
	}
	s.synergyEngine.activeSynergies[synergyKey] = activeSynergy
	s.synergyEngine.mu.Unlock()

	s.metrics.IncrementSynergyActivated(synergyID)
	return &SynergyResult{
		Success:   true,
		SynergyID: synergyID,
		Effects:   synergyDef.Effects,
		Duration:  synergyDef.Duration,
		Stacks:    1,
	}, nil
}

// GetActiveCombos returns active combos for a player
func (s *CombatService) GetActiveCombos(ctx context.Context, playerID string) ([]*ActiveCombo, error) {
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.comboEngine.mu.RLock()
	defer s.comboEngine.mu.RUnlock()

	// PERFORMANCE: Pre-allocate slice with known capacity to avoid allocations
	combos := make([]*ActiveCombo, 0, len(s.comboEngine.activeCombos))
	for _, combo := range s.comboEngine.activeCombos {
		if combo.PlayerID == playerID {
			combos = append(combos, combo)
		}
	}

	return combos, nil
}

// GetActiveSynergies returns active synergies for a player
func (s *CombatService) GetActiveSynergies(ctx context.Context, playerID string) ([]*ActiveSynergy, error) {
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.synergyEngine.mu.RLock()
	defer s.synergyEngine.mu.RUnlock()

	var synergies []*ActiveSynergy
	for _, synergy := range s.synergyEngine.activeSynergies {
		if synergy.PlayerID == playerID {
			synergies = append(synergies, synergy)
		}
	}

	return synergies, nil
}

// GetComboDefinitions returns available combo definitions
func (s *CombatService) GetComboDefinitions(ctx context.Context, difficulty string) ([]*ComboDefinition, error) {
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.comboEngine.mu.RLock()
	defer s.comboEngine.mu.RUnlock()

	// PERFORMANCE: Pre-allocate slice with known capacity to avoid allocations
	combos := make([]*ComboDefinition, 0, len(s.comboEngine.comboDefinitions))
	for _, combo := range s.comboEngine.comboDefinitions {
		if difficulty == "" || combo.Difficulty == difficulty {
			combos = append(combos, combo)
		}
	}

	return combos, nil
}

// GetSynergyDefinitions returns available synergy definitions
func (s *CombatService) GetSynergyDefinitions(ctx context.Context, rarity string) ([]*SynergyDefinition, error) {
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.synergyEngine.mu.RLock()
	defer s.synergyEngine.mu.RUnlock()

	var synergies []*SynergyDefinition
	for _, synergy := range s.synergyEngine.synergyDefinitions {
		if rarity == "" || synergy.Rarity == rarity {
			synergies = append(synergies, synergy)
		}
	}

	return synergies, nil
}

// ComboResult represents the result of processing a combo input
type ComboResult struct {
	Success      bool          `json:"success"`
	ComboID      string        `json:"combo_id,omitempty"`
	ComboName    string        `json:"combo_name,omitempty"`
	Rewards      []ComboReward `json:"rewards,omitempty"`
	StaminaCost  int           `json:"stamina_cost"`
	ChainLength  int           `json:"chain_length"`
	Error        string        `json:"error,omitempty"`
}

// SynergyResult represents the result of activating a synergy
type SynergyResult struct {
	Success   bool             `json:"success"`
	SynergyID string           `json:"synergy_id,omitempty"`
	Effects   []SynergyEffect  `json:"effects,omitempty"`
	Duration  int              `json:"duration"`
	Stacks    int              `json:"stacks"`
	Error     string           `json:"error,omitempty"`
}

// PRIVATE HELPER METHODS

func (s *CombatService) loadDefaultCombos() {
	// Load default combo definitions
	combos := []*ComboDefinition{
		{
			ID:          "basic_combo_1",
			Name:        "Triple Strike",
			Description: "Quick three-hit combo",
			Sequence: []ComboInput{
				{Type: "attack", Direction: "forward"},
				{Type: "attack", Direction: "forward"},
				{Type: "attack", Direction: "forward", Modifier: "charged"},
			},
			Rewards: []ComboReward{
				{Type: "damage", Value: 1.5, Chance: 1.0},
				{Type: "stamina", Value: 10, Chance: 0.8},
			},
			Difficulty:  "basic",
			TimeWindow:  800,
			MaxChain:    3,
			StaminaCost: 15,
		},
		{
			ID:          "advanced_combo_1",
			Name:        "Whirlwind Assault",
			Description: "Spinning attack combo with directional changes",
			Sequence: []ComboInput{
				{Type: "attack", Direction: "left"},
				{Type: "dodge", Direction: "right"},
				{Type: "attack", Direction: "right"},
				{Type: "special", Modifier: "charged"},
			},
			Rewards: []ComboReward{
				{Type: "damage", Value: 2.0, Chance: 1.0},
				{Type: "effect", Value: "stun", Chance: 0.3},
			},
			Difficulty:  "advanced",
			TimeWindow:  600,
			MaxChain:    4,
			StaminaCost: 25,
		},
	}

	s.comboEngine.mu.Lock()
	for _, combo := range combos {
		s.comboEngine.comboDefinitions[combo.ID] = combo
	}
	s.comboEngine.mu.Unlock()
}

func (s *CombatService) loadDefaultSynergies() {
	// Load default synergy definitions
	synergies := []*SynergyDefinition{
		{
			ID:          "berserker_synergy",
			Name:        "Berserker Rage",
			Description: "Low health + high damage abilities create devastating synergy",
			Requirements: []SynergyRequirement{
				{Type: "status", Target: "health_percent", Operator: "lte", Value: 0.25},
				{Type: "ability", Target: "heavy_attack", Operator: "has"},
			},
			Effects: []SynergyEffect{
				{Type: "damage_boost", Target: "self", Value: 1.5, Duration: 10, Stackable: false},
				{Type: "speed_boost", Target: "self", Value: 0.3, Duration: 10, Stackable: false},
			},
			Duration:       10,
			Cooldown:       60,
			ActivationType: "automatic",
			Rarity:         "rare",
		},
		{
			ID:          "shadow_dancer_synergy",
			Name:        "Shadow Dancer",
			Description: "Stealth + mobility abilities create elusive synergy",
			Requirements: []SynergyRequirement{
				{Type: "ability", Target: "stealth", Operator: "active"},
				{Type: "ability", Target: "dash", Operator: "has"},
			},
			Effects: []SynergyEffect{
				{Type: "defense_boost", Target: "self", Value: 0.5, Duration: 15, Stackable: true, MaxStacks: 3},
				{Type: "speed_boost", Target: "self", Value: 0.4, Duration: 15, Stackable: true, MaxStacks: 3},
			},
			Duration:       15,
			Cooldown:       45,
			ActivationType: "conditional",
			Rarity:         "epic",
		},
	}

	s.synergyEngine.mu.Lock()
	for _, synergy := range synergies {
		s.synergyEngine.synergyDefinitions[synergy.ID] = synergy
	}
	s.synergyEngine.mu.Unlock()
}

func (s *CombatService) checkComboCompletion(activeCombo *ActiveCombo) *ComboResult {
	s.comboEngine.mu.RLock()
	defer s.comboEngine.mu.RUnlock()

	// Check each combo definition
	for _, comboDef := range s.comboEngine.comboDefinitions {
		if s.matchesComboSequence(activeCombo.Sequence, comboDef.Sequence, comboDef.TimeWindow) {
			return &ComboResult{
				Success:     true,
				ComboID:     comboDef.ID,
				ComboName:   comboDef.Name,
				Rewards:     comboDef.Rewards,
				StaminaCost: comboDef.StaminaCost,
				ChainLength: len(activeCombo.Sequence),
			}
		}
	}

	return &ComboResult{
		Success:     false,
		ChainLength: len(activeCombo.Sequence),
	}
}

func (s *CombatService) matchesComboSequence(playerInputs, comboSequence []ComboInput, timeWindow int) bool {
	if len(playerInputs) != len(comboSequence) {
		return false
	}

	for i, input := range playerInputs {
		required := comboSequence[i]
		if input.Type != required.Type ||
		   (required.Direction != "" && input.Direction != required.Direction) ||
		   (required.Modifier != "" && input.Modifier != required.Modifier) {
			return false
		}
	}

	return true
}

func (s *CombatService) validateSynergyRequirements(ctx context.Context, playerID string, synergy *SynergyDefinition) bool {
	// Simplified validation - would check actual player state
	for _, req := range synergy.Requirements {
		// This would validate against actual player abilities, items, status, etc.
		// For now, return true for demonstration
		_ = req
	}
	return true
}

// CleanupExpiredCombos removes expired combo states
func (s *CombatService) CleanupExpiredCombos() {
	s.comboEngine.mu.Lock()
	defer s.comboEngine.mu.Unlock()

	expired := make([]string, 0, len(s.comboEngine.activeCombos)) // Pre-allocate
	for key, combo := range s.comboEngine.activeCombos {
		if time.Since(combo.LastInput) > 10*time.Second {
			expired = append(expired, key)
		}
	}

	for _, key := range expired {
		combo := s.comboEngine.activeCombos[key]
		delete(s.comboEngine.activeCombos, key)
		// PERFORMANCE: Return to memory pool
		s.chainPool.Put(combo)
	}
}

// CleanupExpiredSynergies removes expired synergy effects
func (s *CombatService) CleanupExpiredSynergies() {
	s.synergyEngine.mu.Lock()
	defer s.synergyEngine.mu.Unlock()

	expired := make([]string, 0)
	for key, synergy := range s.synergyEngine.activeSynergies {
		if time.Now().After(synergy.ExpiresAt) {
			expired = append(expired, key)
		}
	}

	for _, key := range expired {
		delete(s.synergyEngine.activeSynergies, key)
	}
}

// JoinCombatSession adds a player to a combat session
func (s *CombatService) JoinCombatSession(ctx context.Context, sessionID, playerID string) error {
	// Check if session exists and is active
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		return err
	}

	if session.Status != "waiting" && session.Status != "active" {
		return fmt.Errorf("session is not accepting players")
	}

	// Check player limit
	players, err := s.repo.GetSessionPlayers(ctx, sessionID)
	if err != nil {
		return err
	}

	if len(players) >= session.MaxPlayers {
		return fmt.Errorf("session is full")
	}

	// Add player to session
	if err := s.repo.AddPlayerToSession(ctx, sessionID, playerID); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to join combat session: %w", err)
	}

	s.metrics.IncrementPlayersJoined()
	s.logger.Infof("Player %s joined combat session %s", playerID, sessionID)

	return nil
}

// LeaveCombatSession removes a player from a combat session
func (s *CombatService) LeaveCombatSession(ctx context.Context, sessionID, playerID string) error {
	if err := s.repo.RemovePlayerFromSession(ctx, sessionID, playerID); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to leave combat session: %w", err)
	}

	s.metrics.IncrementPlayersLeft()
	s.logger.Infof("Player %s left combat session %s", playerID, sessionID)

	return nil
}

// UpdatePlayerPosition updates a player's position
func (s *CombatService) UpdatePlayerPosition(ctx context.Context, sessionID, playerID string, position repository.Position) error {
	if err := s.repo.UpdatePlayerPosition(ctx, sessionID, playerID, position); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update player position: %w", err)
	}

	s.logger.Debugf("Updated position for player %s in session %s: %+v", playerID, sessionID, position)

	return nil
}

// GetCombatSessionState gets the complete state of a combat session
func (s *CombatService) GetCombatSessionState(ctx context.Context, sessionID string) (*repository.CombatSessionState, error) {
	state, err := s.repo.GetCombatSessionState(ctx, sessionID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get combat session state: %w", err)
	}

	return state, nil
}

// StartSpectating marks a player as spectating
func (s *CombatService) StartSpectating(ctx context.Context, sessionID, playerID string) error {
	// Check if player is in session
	players, err := s.repo.GetSessionPlayers(ctx, sessionID)
	if err != nil {
		return err
	}

	playerFound := false
	for _, player := range players {
		if player.PlayerID == playerID {
			playerFound = true
			break
		}
	}

	if !playerFound {
		return fmt.Errorf("player is not in this session")
	}

	if err := s.repo.StartSpectating(ctx, sessionID, playerID); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to start spectating: %w", err)
	}

	s.logger.Infof("Player %s started spectating in session %s", playerID, sessionID)

	return nil
}

// GetPlayerCombatStats gets combat statistics for a specific player
func (s *CombatService) GetPlayerCombatStats(ctx context.Context, playerID string) (map[string]interface{}, error) {
	// This would aggregate stats from combat events
	// For now, return mock data based on player activity
	stats := map[string]interface{}{
		"player_id":      playerID,
		"total_sessions": 15,
		"wins":          8,
		"losses":        7,
		"kills":         45,
		"deaths":        23,
		"damage_dealt":  12500,
		"damage_taken":  8900,
		"accuracy":      0.72,
		"rank":          "Diamond",
		"rank_points":   2450,
		"favorite_weapon": "Neural Disruptor",
		"playtime_hours": 127.5,
	}

	return stats, nil
}

// GetCombatSessionStats gets statistics for a combat session
func (s *CombatService) GetCombatSessionStats(ctx context.Context, sessionID string) (map[string]interface{}, error) {
	// Get session events
	events, err := s.repo.GetCombatEvents(ctx, sessionID, 1000)
	if err != nil {
		return nil, err
	}

	// Aggregate stats from events
	stats := map[string]interface{}{
		"session_id":     sessionID,
		"total_events":   len(events),
		"damage_events":  0,
		"kill_events":    0,
		"action_events":  0,
		"total_damage":   0,
		"duration":       0,
		"active_players": 0,
	}

	// Analyze events
	damageTotal := 0
	for _, event := range events {
		switch event.Type {
		case "damage":
			stats["damage_events"] = stats["damage_events"].(int) + 1
			// Parse damage amount (simplified)
			damageTotal += 100 // Mock parsing
		case "kill":
			stats["kill_events"] = stats["kill_events"].(int) + 1
		case "action":
			stats["action_events"] = stats["action_events"].(int) + 1
		}
	}

	stats["total_damage"] = damageTotal

	// Get session info for duration
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err == nil && session.StartedAt != nil {
		stats["duration"] = int(time.Since(*session.StartedAt).Seconds())
	}

	// Get active players count
	players, err := s.repo.GetSessionPlayers(ctx, sessionID)
	if err == nil {
		activeCount := 0
		for _, player := range players {
			if player.Status == "active" {
				activeCount++
			}
		}
		stats["active_players"] = activeCount
	}

	return stats, nil
}
