package server

import (
	"encoding/json"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// AIService OPTIMIZATION: Issue #1968 - Memory-aligned struct for AI performance
type AIService struct {
	logger   *logrus.Logger
	metrics  *AIMetrics
	activeAI sync.Map // OPTIMIZATION: Thread-safe map for concurrent AI operations

	// OPTIMIZATION: Issue #1968 - Memory pooling for hot path structs (zero allocations target!)
	getBehaviorResponsePool     sync.Pool
	executeBehaviorResponsePool sync.Pool
	calculatePathResponsePool   sync.Pool
	makeDecisionResponsePool    sync.Pool
	listTreesResponsePool       sync.Pool
	getTreeResponsePool         sync.Pool
	executeTreeResponsePool     sync.Pool
}

// AIProcessingContext OPTIMIZATION: Issue #1968 - Struct field alignment (large → small)
type AIProcessingContext struct {
	NPCID     string                 `json:"npc_id"`     // 16 bytes
	RequestID string                 `json:"request_id"` // 16 bytes
	StartTime time.Time              `json:"start_time"` // 24 bytes
	Data      map[string]interface{} `json:"data"`       // 8 bytes (map)
	Timeout   time.Duration          `json:"timeout"`    // 8 bytes
	Priority  int                    `json:"priority"`   // 8 bytes
}

func NewAIService(logger *logrus.Logger, metrics *AIMetrics) *AIService {
	s := &AIService{
		logger:  logger,
		metrics: metrics,
	}

	// OPTIMIZATION: Issue #1968 - Initialize memory pools (zero allocations target!)
	s.getBehaviorResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetBehaviorResponse{}
		},
	}
	s.executeBehaviorResponsePool = sync.Pool{
		New: func() interface{} {
			return &ExecuteBehaviorResponse{}
		},
	}
	s.calculatePathResponsePool = sync.Pool{
		New: func() interface{} {
			return &CalculatePathResponse{}
		},
	}
	s.makeDecisionResponsePool = sync.Pool{
		New: func() interface{} {
			return &MakeDecisionResponse{}
		},
	}
	s.listTreesResponsePool = sync.Pool{
		New: func() interface{} {
			return &ListBehaviorTreesResponse{}
		},
	}
	s.getTreeResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetBehaviorTreeResponse{}
		},
	}
	s.executeTreeResponsePool = sync.Pool{
		New: func() interface{} {
			return &ExecuteBehaviorTreeResponse{}
		},
	}

	return s
}

func (s *AIService) GetNPCBehavior(w http.ResponseWriter, r *http.Request) {
	npcID := chi.URLParam(r, "npcId")
	if npcID == "" {
		http.Error(w, "NPC ID is required", http.StatusBadRequest)
		return
	}

	s.metrics.BehaviorOps.Inc()

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.getBehaviorResponsePool.Get().(*GetBehaviorResponse)
	defer s.getBehaviorResponsePool.Put(resp)

	resp.NPCID = npcID
	resp.CurrentBehavior = "patrol"
	resp.BehaviorState = &AIState{
		NPCID:           npcID,
		CurrentBehavior: "patrol",
		StateVariables: map[string]interface{}{
			"health": 100,
			"mana":   50,
		},
		ActiveGoals: []*AIGoal{
			{
				GoalID:   "patrol_001",
				GoalType: "PATROL",
				Priority: 50,
			},
		},
	}
	resp.ActiveActions = []*BehaviorAction{
		{
			ActionType: "MOVE_TO",
			Target:     "waypoint_001",
		},
	}
	resp.NextDecisionTime = time.Now().Add(5 * time.Second)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("npc_id", npcID).Debug("NPC behavior retrieved")
}

func (s *AIService) ExecuteNPCBehavior(w http.ResponseWriter, r *http.Request) {
	npcID := chi.URLParam(r, "npcId")
	if npcID == "" {
		http.Error(w, "NPC ID is required", http.StatusBadRequest)
		return
	}

	var req ExecuteBehaviorRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode execute behavior request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.BehaviorOps.Inc()

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.executeBehaviorResponsePool.Get().(*ExecuteBehaviorResponse)
	defer s.executeBehaviorResponsePool.Put(resp)

	resp.NPCID = npcID
	resp.BehaviorExecuted = req.BehaviorTreeID
	resp.Success = true
	resp.ActionsTaken = []*BehaviorAction{
		{
			ActionType: "MOVE_TO",
			Target:     "player_position",
			Parameters: map[string]interface{}{
				"speed": 5.0,
			},
		},
	}
	resp.ExecutionTimeMs = 15
	resp.Message = "Behavior executed successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"npc_id":            npcID,
		"behavior_tree_id":  req.BehaviorTreeID,
		"execution_time_ms": resp.ExecutionTimeMs,
	}).Info("NPC behavior executed successfully")
}

func (s *AIService) CalculatePath(w http.ResponseWriter, r *http.Request) {
	npcID := chi.URLParam(r, "npcId")
	if npcID == "" {
		http.Error(w, "NPC ID is required", http.StatusBadRequest)
		return
	}

	var req CalculatePathRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode calculate path request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.PathfindingOps.Inc()

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.calculatePathResponsePool.Get().(*CalculatePathResponse)
	defer s.calculatePathResponsePool.Put(resp)

	resp.NPCID = npcID
	resp.Path = &Path{
		PathID:            uuid.New().String(),
		Waypoints:         []Vector3{{X: 10, Y: 20, Z: 0}, {X: 15, Y: 25, Z: 0}},
		TotalDistance:     7.07,
		EstimatedTime:     1414, // ~1.4 seconds
		PathType:          req.PathType,
		CalculationTimeMs: 8,
	}
	resp.Success = true

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"npc_id":              npcID,
		"path_type":           req.PathType,
		"total_distance":      resp.Path.TotalDistance,
		"calculation_time_ms": resp.Path.CalculationTimeMs,
	}).Debug("path calculated successfully")
}

func (s *AIService) MakeDecision(w http.ResponseWriter, r *http.Request) {
	npcID := chi.URLParam(r, "npcId")
	if npcID == "" {
		http.Error(w, "NPC ID is required", http.StatusBadRequest)
		return
	}

	var req MakeDecisionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode make decision request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.DecisionOps.Inc()

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.makeDecisionResponsePool.Get().(*MakeDecisionResponse)
	defer s.makeDecisionResponsePool.Put(resp)

	resp.NPCID = npcID
	resp.ChosenAction = &DecisionAction{
		ActionID:           "attack_001",
		ActionType:         "ATTACK",
		PriorityScore:      85,
		RiskLevel:          30,
		ExpectedOutcome:    "damage_enemy",
		SuccessProbability: 0.75,
	}
	resp.DecisionReasoning = "High priority threat detected, sufficient health to engage"
	resp.ConfidenceScore = 0.82
	resp.DecisionTimeMs = 12

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"npc_id":           npcID,
		"chosen_action":    resp.ChosenAction.ActionType,
		"confidence_score": resp.ConfidenceScore,
		"decision_time_ms": resp.DecisionTimeMs,
	}).Debug("decision made successfully")
}

func (s *AIService) ListBehaviorTrees(w http.ResponseWriter) {
	limit := 50 // Default limit
	offset := 0 // Default offset

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.listTreesResponsePool.Get().(*ListBehaviorTreesResponse)
	defer s.listTreesResponsePool.Put(resp)

	resp.Trees = []*BehaviorTree{
		{
			TreeID:      "patrol_tree",
			Name:        "Patrol Behavior",
			Description: "Standard patrol behavior for guards",
			RootNode: &BehaviorNode{
				NodeID:   "patrol_root",
				NodeType: "SEQUENCE",
				Name:     "Patrol Sequence",
			},
		},
		{
			TreeID:      "combat_tree",
			Name:        "Combat Behavior",
			Description: "Aggressive combat behavior",
			RootNode: &BehaviorNode{
				NodeID:   "combat_root",
				NodeType: "SELECTOR",
				Name:     "Combat Selector",
			},
		},
	}
	resp.TotalCount = 2
	resp.Limit = limit
	resp.Offset = offset

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("total_count", resp.TotalCount).Debug("behavior trees listed")
}

func (s *AIService) GetBehaviorTree(w http.ResponseWriter, r *http.Request) {
	treeID := chi.URLParam(r, "treeId")
	if treeID == "" {
		http.Error(w, "Tree ID is required", http.StatusBadRequest)
		return
	}

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.getTreeResponsePool.Get().(*GetBehaviorTreeResponse)
	defer s.getTreeResponsePool.Put(resp)

	resp.Tree = &BehaviorTree{
		TreeID:      treeID,
		Name:        "Combat Behavior Tree",
		Description: "Advanced combat decision making",
		RootNode: &BehaviorNode{
			NodeID:   "combat_root",
			NodeType: "SELECTOR",
			Name:     "Combat Root",
			Children: []*BehaviorNode{
				{
					NodeID:   "flee_condition",
					NodeType: "DECORATOR",
					Name:     "Flee if low health",
				},
				{
					NodeID:   "attack_sequence",
					NodeType: "SEQUENCE",
					Name:     "Attack sequence",
				},
			},
		},
		Variables: map[string]interface{}{
			"aggression": 70,
			"caution":    30,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("tree_id", treeID).Debug("behavior tree retrieved")
}

func (s *AIService) ExecuteBehaviorTree(w http.ResponseWriter, r *http.Request) {
	treeID := chi.URLParam(r, "treeId")
	if treeID == "" {
		http.Error(w, "Tree ID is required", http.StatusBadRequest)
		return
	}

	var req ExecuteBehaviorTreeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode execute behavior tree request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.BehaviorOps.Inc()

	// OPTIMIZATION: Issue #1968 - Use memory pool
	resp := s.executeTreeResponsePool.Get().(*ExecuteBehaviorTreeResponse)
	defer s.executeTreeResponsePool.Put(resp)

	resp.NPCID = req.NPCID
	resp.TreeID = treeID
	resp.ExecutionResult = "SUCCESS"
	resp.FinalNode = "attack_sequence"
	resp.ExecutionPath = []string{"combat_root", "attack_sequence", "move_to_target"}
	resp.ExecutionTimeMs = 25
	resp.NewAIState = &AIState{
		NPCID:           req.NPCID,
		CurrentBehavior: "combat",
		StateVariables: map[string]interface{}{
			"target_locked": true,
			"combat_mode":   true,
		},
	}
	resp.Message = "Behavior tree executed successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"npc_id":            req.NPCID,
		"tree_id":           treeID,
		"execution_result":  resp.ExecutionResult,
		"execution_time_ms": resp.ExecutionTimeMs,
	}).Info("behavior tree executed successfully")
}

func (s *AIService) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"ai-service","active_ai":42}`))
}

// Helper functions for AI processing
func (s *AIService) calculateThreatLevel(entity *AIEntity) int {
	// Simple threat calculation based on distance and entity type
	threat := 0

	switch entity.EntityType {
	case "PLAYER":
		threat = 80
	case "MONSTER":
		threat = 60
	case "NPC":
		if entity.HealthPercentage > 50 {
			threat = 30
		} else {
			threat = 10
		}
	}

	// Adjust based on distance
	if entity.Distance < 10 {
		threat += 20
	} else if entity.Distance > 50 {
		threat -= 30
	}

	return max(0, min(100, threat))
}

func (s *AIService) evaluateDecisionActions(actions []*DecisionAction, context *DecisionContext) *DecisionAction {
	if len(actions) == 0 {
		return nil
	}

	bestAction := actions[0]
	bestScore := s.calculateActionScore(bestAction, context)

	for _, action := range actions[1:] {
		score := s.calculateActionScore(action, context)
		if score > bestScore {
			bestScore = score
			bestAction = action
		}
	}

	return bestAction
}

func (s *AIService) calculateActionScore(action *DecisionAction, context *DecisionContext) float64 {
	// Simple scoring based on priority and risk
	baseScore := float64(action.PriorityScore)

	// Adjust for NPC personality
	if context.NPCPersonality != nil {
		switch action.ActionType {
		case "ATTACK":
			baseScore *= float64(context.NPCPersonality.Aggression) / 50.0
		case "FLEE":
			baseScore *= float64(context.NPCPersonality.Caution) / 50.0
		}
	}

	// Adjust for risk tolerance
	riskAdjustment := 1.0
	if action.RiskLevel > context.RiskTolerance {
		riskAdjustment = 0.5
	}

	return baseScore * riskAdjustment
}

// Utility functions
func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
