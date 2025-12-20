package server

import (
	"time"
)

// CharacterInventory OPTIMIZATION: Issue #1968 - Memory-aligned structs for AI performance (large → small)
type CharacterInventory struct {
	CharacterID string                         `json:"character_id"` // 16 bytes
	Containers  map[string]*InventoryContainer `json:"containers"`   // 8 bytes (map)
	LastUpdated time.Time                      `json:"last_updated"` // 24 bytes
}

// InventoryContainer OPTIMIZATION: Issue #1968 - Memory-aligned container struct
type InventoryContainer struct {
	ContainerID string                    `json:"container_id"` // 16 bytes
	Name        string                    `json:"name"`         // 16 bytes
	Type        string                    `json:"type"`         // 16 bytes
	Capacity    int                       `json:"capacity"`     // 8 bytes
	UsedSlots   int                       `json:"used_slots"`   // 8 bytes
	Rows        int                       `json:"rows"`         // 8 bytes
	Columns     int                       `json:"columns"`      // 8 bytes
	Items       map[string]*InventoryItem `json:"items"`        // 8 bytes (map)
	IsLocked    bool                      `json:"is_locked"`    // 1 byte
}

// InventoryItem OPTIMIZATION: Issue #1968 - Memory-aligned item struct
type InventoryItem struct {
	InventoryItemID string     `json:"inventory_item_id"`    // 16 bytes
	ItemID          string     `json:"item_id"`              // 16 bytes
	CharacterID     string     `json:"character_id"`         // 16 bytes
	Container       string     `json:"container"`            // 16 bytes
	SlotX           int        `json:"slot_x"`               // 8 bytes
	SlotY           int        `json:"slot_y"`               // 8 bytes
	Quantity        int        `json:"quantity"`             // 8 bytes
	Durability      int        `json:"durability"`           // 8 bytes
	MaxDurability   int        `json:"max_durability"`       // 8 bytes
	IsEquipped      bool       `json:"is_equipped"`          // 1 byte
	IsLocked        bool       `json:"is_locked"`            // 1 byte
	AcquiredAt      time.Time  `json:"acquired_at"`          // 24 bytes
	ExpiresAt       *time.Time `json:"expires_at,omitempty"` // 8 bytes (pointer)
}

// CharacterEquipment OPTIMIZATION: Issue #1968 - Memory-aligned equipment struct
type CharacterEquipment struct {
	CharacterID string                    `json:"character_id"` // 16 bytes
	Slots       map[string]*EquipmentSlot `json:"slots"`        // 8 bytes (map)
	LastUpdated time.Time                 `json:"last_updated"` // 24 bytes
}

// EquipmentSlot OPTIMIZATION: Issue #1968 - Memory-aligned equipment slot
type EquipmentSlot struct {
	SlotType        string    `json:"slot_type"`         // 16 bytes
	InventoryItemID string    `json:"inventory_item_id"` // 16 bytes
	ItemID          string    `json:"item_id"`           // 16 bytes
	EquippedAt      time.Time `json:"equipped_at"`       // 24 bytes
}

// ItemDefinition OPTIMIZATION: Issue #1968 - Memory-aligned item definition
type ItemDefinition struct {
	ItemID      string                 `json:"item_id"`     // 16 bytes
	Name        string                 `json:"name"`        // 16 bytes
	Description string                 `json:"description"` // 16 bytes
	ItemType    string                 `json:"item_type"`   // 16 bytes
	Rarity      string                 `json:"rarity"`      // 16 bytes
	LevelReq    int                    `json:"level_req"`   // 8 bytes
	MaxStack    int                    `json:"max_stack"`   // 8 bytes
	SellPrice   int                    `json:"sell_price"`  // 8 bytes
	BuyPrice    int                    `json:"buy_price"`   // 8 bytes
	Stats       map[string]interface{} `json:"stats"`       // 8 bytes (map)
	Effects     []*ItemEffect          `json:"effects"`     // 24 bytes (slice)
	IconURL     string                 `json:"icon_url"`    // 16 bytes
	ModelURL    string                 `json:"model_url"`   // 16 bytes
}

// ItemEffect OPTIMIZATION: Issue #1968 - Memory-aligned item effect
type ItemEffect struct {
	EffectType string      `json:"effect_type"` // 16 bytes
	TargetStat string      `json:"target_stat"` // 16 bytes
	Value      interface{} `json:"value"`       // 16 bytes (interface)
	Duration   int         `json:"duration"`    // 8 bytes
	Conditions []string    `json:"conditions"`  // 24 bytes (slice)
}

// EquipmentStats OPTIMIZATION: Issue #1968 - Memory-aligned equipment stats
type EquipmentStats struct {
	TotalStats      map[string]interface{} `json:"total_stats"`      // 8 bytes (map)
	ActiveBonuses   []*ItemEffect          `json:"active_bonuses"`   // 24 bytes (slice)
	SetBonuses      map[string]string      `json:"set_bonuses"`      // 8 bytes (map)
	DefenseRating   int                    `json:"defense_rating"`   // 8 bytes
	MagicResistance int                    `json:"magic_resistance"` // 8 bytes
	AttackPower     int                    `json:"attack_power"`     // 8 bytes
	SpellPower      int                    `json:"spell_power"`      // 8 bytes
}

// Vector3 for 3D positioning
type Vector3 struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// MoveItemRequest Request structs
type MoveItemRequest struct {
	InventoryItemID string `json:"inventory_item_id"`
	FromContainer   string `json:"from_container"`
	ToContainer     string `json:"to_container"`
	ToSlotX         int    `json:"to_slot_x"`
	ToSlotY         int    `json:"to_slot_y"`
	Quantity        int    `json:"quantity"`
}

type EquipItemRequest struct {
	InventoryItemID string `json:"inventory_item_id"`
	SlotType        string `json:"slot_type"`
}

type UnequipItemRequest struct {
	SlotType        string `json:"slot_type"`
	TargetContainer string `json:"target_container"`
}

type UseItemRequest struct {
	InventoryItemID   string `json:"inventory_item_id"`
	TargetCharacterID string `json:"target_character_id,omitempty"`
	Quantity          int    `json:"quantity"`
}

type DropItemRequest struct {
	InventoryItemID string   `json:"inventory_item_id"`
	Quantity        int      `json:"quantity"`
	Position        *Vector3 `json:"position"`
}

type SearchItemsRequest struct {
	Query    string `json:"query"`
	ItemType string `json:"item_type,omitempty"`
	Rarity   string `json:"rarity,omitempty"`
	MinLevel int    `json:"min_level,omitempty"`
	MaxLevel int    `json:"max_level,omitempty"`
	Limit    int    `json:"limit,omitempty"`
	Offset   int    `json:"offset,omitempty"`
}

// GetInventoryResponse Response structs for memory pooling
type GetInventoryResponse struct {
	CharacterID string                `json:"character_id"`
	Containers  []*InventoryContainer `json:"containers"`
	TotalItems  int                   `json:"total_items"`
	TotalWeight int                   `json:"total_weight"`
	MaxWeight   int                   `json:"max_weight"`
}

type ListItemsResponse struct {
	Items      []*InventoryItem `json:"items"`
	TotalCount int              `json:"total_count"`
	Limit      int              `json:"limit"`
	Offset     int              `json:"offset"`
}

type MoveItemResponse struct {
	InventoryItemID string `json:"inventory_item_id"`
	OldContainer    string `json:"old_container"`
	OldSlotX        int    `json:"old_slot_x"`
	OldSlotY        int    `json:"old_slot_y"`
	NewContainer    string `json:"new_container"`
	NewSlotX        int    `json:"new_slot_x"`
	NewSlotY        int    `json:"new_slot_y"`
	QuantityMoved   int    `json:"quantity_moved"`
	Success         bool   `json:"success"`
}

type EquipItemResponse struct {
	InventoryItemID string `json:"inventory_item_id"`
	SlotType        string `json:"slot_type"`
	OldItemID       string `json:"old_item_id,omitempty"`
	Success         bool   `json:"success"`
	Message         string `json:"message"`
}

type UnequipItemResponse struct {
	SlotType        string `json:"slot_type"`
	InventoryItemID string `json:"inventory_item_id"`
	Container       string `json:"container"`
	SlotX           int    `json:"slot_x"`
	SlotY           int    `json:"slot_y"`
	Success         bool   `json:"success"`
	Message         string `json:"message"`
}

type UseItemResponse struct {
	InventoryItemID   string        `json:"inventory_item_id"`
	QuantityUsed      int           `json:"quantity_used"`
	EffectsApplied    []*ItemEffect `json:"effects_applied"`
	RemainingQuantity int           `json:"remaining_quantity"`
	Success           bool          `json:"success"`
	Message           string        `json:"message"`
}

type DropItemResponse struct {
	InventoryItemID   string   `json:"inventory_item_id"`
	QuantityDropped   int      `json:"quantity_dropped"`
	Position          *Vector3 `json:"position"`
	RemainingQuantity int      `json:"remaining_quantity"`
	Success           bool     `json:"success"`
	Message           string   `json:"message"`
}

type GetItemResponse struct {
	Item *ItemDefinition `json:"item"`
}

type SearchItemsResponse struct {
	Items        []*ItemDefinition `json:"items"`
	TotalCount   int               `json:"total_count"`
	Query        string            `json:"query"`
	SearchTimeMs int               `json:"search_time_ms"`
}

type GetEquipmentResponse struct {
	CharacterID string                    `json:"character_id"`
	Equipment   map[string]*EquipmentSlot `json:"equipment"`
}

type EquipmentStatsResponse struct {
	CharacterID string          `json:"character_id"`
	Stats       *EquipmentStats `json:"stats"`
}

// AI Service specific structs

// AIState OPTIMIZATION: Issue #1968 - Memory-aligned AI structs
type AIState struct {
	NPCID           string                 `json:"npc_id"`           // 16 bytes
	CurrentBehavior string                 `json:"current_behavior"` // 16 bytes
	StateVariables  map[string]interface{} `json:"state_variables"`  // 8 bytes (map)
	ActiveGoals     []*AIGoal              `json:"active_goals"`     // 24 bytes (slice)
	LastUpdate      time.Time              `json:"last_update"`      // 24 bytes
}

// AIGoal OPTIMIZATION: Issue #1968 - Memory-aligned AI goal
type AIGoal struct {
	GoalID   string    `json:"goal_id"`   // 16 bytes
	GoalType string    `json:"goal_type"` // 16 bytes
	Priority int       `json:"priority"`  // 8 bytes
	Target   string    `json:"target"`    // 16 bytes
	Deadline time.Time `json:"deadline"`  // 24 bytes
}

// BehaviorTree OPTIMIZATION: Issue #1968 - Memory-aligned behavior tree
type BehaviorTree struct {
	TreeID      string                 `json:"tree_id"`     // 16 bytes
	Name        string                 `json:"name"`        // 16 bytes
	Description string                 `json:"description"` // 16 bytes
	RootNode    *BehaviorNode          `json:"root_node"`   // 8 bytes (pointer)
	Variables   map[string]interface{} `json:"variables"`   // 8 bytes (map)
	CreatedAt   time.Time              `json:"created_at"`  // 24 bytes
	UpdatedAt   time.Time              `json:"updated_at"`  // 24 bytes
}

// BehaviorNode OPTIMIZATION: Issue #1968 - Memory-aligned behavior node
type BehaviorNode struct {
	NodeID      string                 `json:"node_id"`     // 16 bytes
	NodeType    string                 `json:"node_type"`   // 16 bytes
	Name        string                 `json:"name"`        // 16 bytes
	Description string                 `json:"description"` // 16 bytes
	Children    []*BehaviorNode        `json:"children"`    // 24 bytes (slice)
	Conditions  []*BehaviorCondition   `json:"conditions"`  // 24 bytes (slice)
	Actions     []*BehaviorAction      `json:"actions"`     // 24 bytes (slice)
	Parameters  map[string]interface{} `json:"parameters"`  // 8 bytes (map)
}

// BehaviorCondition OPTIMIZATION: Issue #1968 - Memory-aligned behavior condition
type BehaviorCondition struct {
	ConditionType string      `json:"condition_type"` // 16 bytes
	Operator      string      `json:"operator"`       // 16 bytes
	Value         interface{} `json:"value"`          // 16 bytes (interface)
	Target        string      `json:"target"`         // 16 bytes
}

// BehaviorAction OPTIMIZATION: Issue #1968 - Memory-aligned behavior action
type BehaviorAction struct {
	ActionType string                 `json:"action_type"` // 16 bytes
	Target     string                 `json:"target"`      // 16 bytes
	Parameters map[string]interface{} `json:"parameters"`  // 8 bytes (map)
	Duration   int                    `json:"duration"`    // 8 bytes
}

// Path OPTIMIZATION: Issue #1968 - Memory-aligned path
type Path struct {
	PathID            string          `json:"path_id"`             // 16 bytes
	Waypoints         []Vector3       `json:"waypoints"`           // 24 bytes (slice)
	TotalDistance     float64         `json:"total_distance"`      // 8 bytes
	EstimatedTime     int             `json:"estimated_time"`      // 8 bytes
	PathType          string          `json:"path_type"`           // 16 bytes
	ObstaclesAvoided  []*PathObstacle `json:"obstacles_avoided"`   // 24 bytes (slice)
	CalculationTimeMs int             `json:"calculation_time_ms"` // 8 bytes
}

// PathObstacle OPTIMIZATION: Issue #1968 - Memory-aligned path obstacle
type PathObstacle struct {
	ObstacleID   string      `json:"obstacle_id"`   // 16 bytes
	ObstacleType string      `json:"obstacle_type"` // 16 bytes
	Position     Vector3     `json:"position"`      // 24 bytes
	BoundingBox  BoundingBox `json:"bounding_box"`  // 48 bytes
	ThreatLevel  int         `json:"threat_level"`  // 8 bytes
}

// BoundingBox OPTIMIZATION: Issue #1968 - Memory-aligned bounding box
type BoundingBox struct {
	Min Vector3 `json:"min"` // 24 bytes
	Max Vector3 `json:"max"` // 24 bytes
}

// DecisionContext OPTIMIZATION: Issue #1968 - Memory-aligned decision context
type DecisionContext struct {
	NPCID            string            `json:"npc_id"`            // 16 bytes
	CurrentState     *AIState          `json:"current_state"`     // 8 bytes (pointer)
	AvailableActions []*DecisionAction `json:"available_actions"` // 24 bytes (slice)
	TimePressure     int               `json:"time_pressure"`     // 8 bytes
	RiskTolerance    int               `json:"risk_tolerance"`    // 8 bytes
	NPCPersonality   *NPCPersonality   `json:"npc_personality"`   // 8 bytes (pointer)
}

// DecisionAction OPTIMIZATION: Issue #1968 - Memory-aligned decision action
type DecisionAction struct {
	ActionID           string                 `json:"action_id"`           // 16 bytes
	ActionType         string                 `json:"action_type"`         // 16 bytes
	PriorityScore      int                    `json:"priority_score"`      // 8 bytes
	RiskLevel          int                    `json:"risk_level"`          // 8 bytes
	ExpectedOutcome    string                 `json:"expected_outcome"`    // 16 bytes
	SuccessProbability float64                `json:"success_probability"` // 8 bytes
	ResourceCost       map[string]interface{} `json:"resource_cost"`       // 8 bytes (map)
}

// NPCPersonality OPTIMIZATION: Issue #1968 - Memory-aligned NPC personality
type NPCPersonality struct {
	Aggression int `json:"aggression"` // 8 bytes
	Caution    int `json:"caution"`    // 8 bytes
	Social     int `json:"social"`     // 8 bytes
	Curiosity  int `json:"curiosity"`  // 8 bytes
	Loyalty    int `json:"loyalty"`    // 8 bytes
}

// ExecuteBehaviorRequest AI Service request/response structs
type ExecuteBehaviorRequest struct {
	BehaviorTreeID   string                 `json:"behavior_tree_id"`
	ContextVariables map[string]interface{} `json:"context_variables"`
	TimeoutMs        int                    `json:"timeout_ms"`
}

type GetBehaviorResponse struct {
	NPCID            string            `json:"npc_id"`
	CurrentBehavior  string            `json:"current_behavior"`
	BehaviorState    *AIState          `json:"behavior_state"`
	ActiveActions    []*BehaviorAction `json:"active_actions"`
	NextDecisionTime time.Time         `json:"next_decision_time"`
}

type ExecuteBehaviorResponse struct {
	NPCID            string            `json:"npc_id"`
	BehaviorExecuted string            `json:"behavior_executed"`
	Success          bool              `json:"success"`
	ActionsTaken     []*BehaviorAction `json:"actions_taken"`
	ExecutionTimeMs  int               `json:"execution_time_ms"`
	NewState         *AIState          `json:"new_state"`
	Message          string            `json:"message"`
}

type CalculatePathRequest struct {
	StartPosition Vector3 `json:"start_position"`
	EndPosition   Vector3 `json:"end_position"`
	PathType      string  `json:"path_type"`
	MaxDistance   float64 `json:"max_distance"`
	AvoidEntities bool    `json:"avoid_entities"`
	AvoidHazards  bool    `json:"avoid_hazards"`
	MovementType  string  `json:"movement_type"`
}

type CalculatePathResponse struct {
	NPCID            string  `json:"npc_id"`
	Path             *Path   `json:"path"`
	Success          bool    `json:"success"`
	ErrorMessage     string  `json:"error_message"`
	AlternativePaths []*Path `json:"alternative_paths"`
}

type MakeDecisionRequest struct {
	DecisionContext  *DecisionContext  `json:"decision_context"`
	AvailableActions []*DecisionAction `json:"available_actions"`
	TimeLimitMs      int               `json:"time_limit_ms"`
	RiskThreshold    int               `json:"risk_threshold"`
}

type MakeDecisionResponse struct {
	NPCID              string            `json:"npc_id"`
	ChosenAction       *DecisionAction   `json:"chosen_action"`
	DecisionReasoning  string            `json:"decision_reasoning"`
	ConfidenceScore    float64           `json:"confidence_score"`
	AlternativeActions []*DecisionAction `json:"alternative_actions"`
	DecisionTimeMs     int               `json:"decision_time_ms"`
}

type ListBehaviorTreesResponse struct {
	Trees      []*BehaviorTree `json:"trees"`
	TotalCount int             `json:"total_count"`
	Limit      int             `json:"limit"`
	Offset     int             `json:"offset"`
}

type GetBehaviorTreeResponse struct {
	Tree *BehaviorTree `json:"tree"`
}

type ExecuteBehaviorTreeRequest struct {
	NPCID            string                 `json:"npc_id"`
	ContextVariables map[string]interface{} `json:"context_variables"`
	MaxDepth         int                    `json:"max_depth"`
	TimeoutMs        int                    `json:"timeout_ms"`
}

type ExecuteBehaviorTreeResponse struct {
	NPCID           string   `json:"npc_id"`
	TreeID          string   `json:"tree_id"`
	ExecutionResult string   `json:"execution_result"`
	FinalNode       string   `json:"final_node"`
	ExecutionPath   []string `json:"execution_path"`
	ExecutionTimeMs int      `json:"execution_time_ms"`
	NewAIState      *AIState `json:"new_ai_state"`
	Message         string   `json:"message"`
}
