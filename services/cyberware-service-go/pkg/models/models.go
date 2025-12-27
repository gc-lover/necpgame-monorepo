// Issue: #2225 - Advanced Cyberware Integration System
// Models for Cyberware Service - Neural implants and biomechanical enhancements

package models

import (
	"time"
)

// Cyberware represents a cybernetic implant or enhancement
type Cyberware struct {
	ID              string            `json:"id" db:"id"`
	Name            string            `json:"name" db:"name"`
	Description     string            `json:"description" db:"description"`
	Category        string            `json:"category" db:"category"`         // "neural", "cybernetic", "biomechanical", "nano"
	Tier            int               `json:"tier" db:"tier"`                 // 1-5 (common to legendary)
	Rarity          string            `json:"rarity" db:"rarity"`             // "common", "uncommon", "rare", "epic", "legendary"
	BaseStats       CyberwareStats    `json:"base_stats" db:"base_stats"`     // Base stat bonuses
	ScalingStats    CyberwareStats    `json:"scaling_stats" db:"scaling_stats"` // Per-level scaling
	NeuralInterface NeuralInterface  `json:"neural_interface" db:"neural_interface"` // Neural integration data
	Compatibility   Compatibility     `json:"compatibility" db:"compatibility"`     // Installation requirements
	Risks           []CyberwareRisk   `json:"risks" db:"risks"`               // Installation/combat risks
	Abilities       []CyberwareAbility `json:"abilities" db:"abilities"`     // Special abilities
	Cost            CyberwareCost     `json:"cost" db:"cost"`                 // Installation/maintenance costs
	Status          string            `json:"status" db:"status"`             // "active", "deprecated", "experimental"
	CreatedAt       time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time         `json:"updated_at" db:"updated_at"`
}

// CyberwareStats represents stat bonuses provided by cyberware
type CyberwareStats struct {
	HealthBonus         int     `json:"health_bonus"`
	ArmorBonus          int     `json:"armor_bonus"`
	StrengthBonus       int     `json:"strength_bonus"`
	AgilityBonus        int     `json:"agility_bonus"`
	IntelligenceBonus   int     `json:"intelligence_bonus"`
	ReflexesBonus       int     `json:"reflexes_bonus"`
	CyberwareCapacity   int     `json:"cyberware_capacity"`   // Additional implant slots
	NeuralCapacity      int     `json:"neural_capacity"`      // Neural processing power
	HackingBonus        int     `json:"hacking_bonus"`
	StealthBonus        int     `json:"stealth_bonus"`
	DamageMultiplier    float64 `json:"damage_multiplier"`
	CritChanceBonus     float64 `json:"crit_chance_bonus"`
	CritDamageBonus     float64 `json:"crit_damage_bonus"`
}

// NeuralInterface represents neural integration data
type NeuralInterface struct {
	NeuralLoad         int               `json:"neural_load"`         // Processing load (1-100)
	IntegrationTime    int               `json:"integration_time"`    // Hours to fully integrate
	NeuralPathways     []string          `json:"neural_pathways"`     // Affected neural pathways
	FeedbackIntensity  int               `json:"feedback_intensity"`  // Pain/feedback level
	AdaptationRate     float64           `json:"adaptation_rate"`     // How quickly player adapts
	OverloadThreshold  int               `json:"overload_threshold"`  // When implant malfunctions
	ResonanceFrequency float64           `json:"resonance_frequency"` // Neural resonance frequency
	CompatibilityScore map[string]int    `json:"compatibility_score"` // Compatibility with other implants
}

// Compatibility represents installation requirements
type Compatibility struct {
	RequiredLevel      int               `json:"required_level"`
	RequiredReputation map[string]int    `json:"required_reputation"` // Required faction reputation
	Prerequisites      []string          `json:"prerequisites"`      // Required implants/achievements
	Incompatibilities   []string          `json:"incompatibilities"`   // Conflicting implants
	BodyPart           string            `json:"body_part"`           // Installation location
	InstallationTime   int               `json:"installation_time"`   // Minutes for surgery
	RiskLevel          string            `json:"risk_level"`          // "low", "medium", "high", "extreme"
}

// CyberwareRisk represents potential risks of cyberware
type CyberwareRisk struct {
	Type        string  `json:"type"`        // "cyberpsychosis", "rejection", "overload", "infection"
	Probability float64 `json:"probability"` // 0-1 chance per day
	Severity    string  `json:"severity"`    // "minor", "moderate", "severe", "fatal"
	Description string  `json:"description"`
	Triggers    []string `json:"triggers"`   // What triggers this risk
}

// CyberwareAbility represents special abilities granted by cyberware
type CyberwareAbility struct {
	ID          string            `json:"id"`
	Name        string            `json:"name"`
	Description string            `json:"description"`
	Type        string            `json:"type"`        // "passive", "active", "toggle"
	Cooldown    int               `json:"cooldown"`    // Seconds between uses
	EnergyCost  int               `json:"energy_cost"` // Energy consumption
	Effects     []AbilityEffect   `json:"effects"`
	Requirements map[string]interface{} `json:"requirements"` // Additional requirements
}

// AbilityEffect represents an effect of a cyberware ability
type AbilityEffect struct {
	Type     string      `json:"type"`     // "damage", "heal", "buff", "debuff", "summon"
	Target   string      `json:"target"`   // "self", "enemy", "area"
	Value    interface{} `json:"value"`    // Effect value
	Duration int         `json:"duration"` // Effect duration in seconds
}

// CyberwareCost represents costs associated with cyberware
type CyberwareCost struct {
	InstallationFee int            `json:"installation_fee"` // Eddies
	MaintenanceCost int            `json:"maintenance_cost"` // Monthly eddies
	EnergyConsumption int          `json:"energy_consumption"` // Per minute
	RareMaterials   map[string]int `json:"rare_materials"`   // Required rare materials
	BlackMarketFee  int            `json:"black_market_fee,omitempty"` // Additional fee for illegal implants
}

// PlayerCyberware represents cyberware installed on a player
type PlayerCyberware struct {
	ID              string               `json:"id" db:"id"`
	PlayerID        string               `json:"player_id" db:"player_id"`
	CyberwareID     string               `json:"cyberware_id" db:"cyberware_id"`
	Level           int                  `json:"level" db:"level"`                     // Upgrade level
	Condition       float64              `json:"condition" db:"condition"`             // 0-1 (1.0 = perfect)
	NeuralStability float64              `json:"neural_stability" db:"neural_stability"` // 0-1 stability rating
	IntegrationProgress float64          `json:"integration_progress" db:"integration_progress"` // 0-1 integration completion
	ActiveAbilities []string             `json:"active_abilities" db:"active_abilities"` // Enabled abilities
	Customization   map[string]interface{} `json:"customization" db:"customization"`   // Custom settings
	InstallationDate time.Time           `json:"installation_date" db:"installation_date"`
	LastMaintenance  time.Time           `json:"last_maintenance" db:"last_maintenance"`
	NextMaintenance  time.Time           `json:"next_maintenance" db:"next_maintenance"`
	Status          string               `json:"status" db:"status"`                   // "active", "malfunctioning", "rejected", "removed"
	CreatedAt       time.Time            `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time            `json:"updated_at" db:"updated_at"`
}

// CyberwareMalfunction represents a cyberware malfunction event
type CyberwareMalfunction struct {
	ID              string    `json:"id" db:"id"`
	PlayerID        string    `json:"player_id" db:"player_id"`
	CyberwareID     string    `json:"cyberware_id" db:"cyberware_id"`
	MalfunctionType string    `json:"malfunction_type" db:"malfunction_type"` // "overload", "rejection", "infection", "glitch"
	Severity        string    `json:"severity" db:"severity"`                 // "minor", "moderate", "severe", "critical"
	Description     string    `json:"description" db:"description"`
	Effects         []string  `json:"effects" db:"effects"`                   // Applied effects
	Resolved        bool      `json:"resolved" db:"resolved"`
	ResolutionCost  int       `json:"resolution_cost" db:"resolution_cost"`   // Eddies to fix
	OccurredAt      time.Time `json:"occurred_at" db:"occurred_at"`
	ResolvedAt      *time.Time `json:"resolved_at" db:"resolved_at"`
	CreatedAt       time.Time `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time `json:"updated_at" db:"updated_at"`
}

// CyberwareIntegration represents neural integration progress
type CyberwareIntegration struct {
	ID                  string               `json:"id" db:"id"`
	PlayerID            string               `json:"player_id" db:"player_id"`
	CyberwareID         string               `json:"cyberware_id" db:"cyberware_id"`
	IntegrationPhase    string               `json:"integration_phase" db:"integration_phase"` // "initial", "adaptation", "optimization", "complete"
	NeuralPathways      map[string]float64   `json:"neural_pathways" db:"neural_pathways"`     // Pathway adaptation progress 0-1
	SynapticConnections int                  `json:"synaptic_connections" db:"synaptic_connections"`
	PainLevel           int                  `json:"pain_level" db:"pain_level"`               // 0-10 pain scale
	FeedbackIntensity   int                  `json:"feedback_intensity" db:"feedback_intensity"`
	Hallucinations      []string             `json:"hallucinations" db:"hallucinations"`       // Current hallucinations
	AdaptationScore     float64              `json:"adaptation_score" db:"adaptation_score"`   // Overall adaptation 0-1
	LastUpdate          time.Time            `json:"last_update" db:"last_update"`
	EstimatedCompletion *time.Time           `json:"estimated_completion" db:"estimated_completion"`
	CreatedAt           time.Time            `json:"created_at" db:"created_at"`
	UpdatedAt           time.Time            `json:"updated_at" db:"updated_at"`
}

// CyberwareSynergy represents synergies between different implants
type CyberwareSynergy struct {
	ID              string              `json:"id" db:"id"`
	PrimaryImplant  string              `json:"primary_implant" db:"primary_implant"`
	SecondaryImplants []string          `json:"secondary_implants" db:"secondary_implants"`
	SynergyType     string              `json:"synergy_type" db:"synergy_type"`     // "amplification", "stabilization", "emergent"
	BonusEffects    []SynergyEffect     `json:"bonus_effects" db:"bonus_effects"`
	RiskEffects     []SynergyEffect     `json:"risk_effects" db:"risk_effects"`
	Compatibility   float64             `json:"compatibility" db:"compatibility"`   // 0-1 synergy strength
	ActivationReq   map[string]interface{} `json:"activation_req" db:"activation_req"` // Requirements to activate
	Status          string              `json:"status" db:"status"`                 // "potential", "active", "conflicting"
	DiscoveredAt    time.Time           `json:"discovered_at" db:"discovered_at"`
	CreatedAt       time.Time           `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time           `json:"updated_at" db:"updated_at"`
}

// SynergyEffect represents an effect of cyberware synergy
type SynergyEffect struct {
	Type        string      `json:"type"`
	Description string      `json:"description"`
	Value       interface{} `json:"value"`
	Condition   string      `json:"condition,omitempty"` // When effect applies
}

// CyberwareMarket represents available cyberware in the market
type CyberwareMarket struct {
	CyberwareID    string    `json:"cyberware_id" db:"cyberware_id"`
	VendorID       string    `json:"vendor_id" db:"vendor_id"`
	Location       string    `json:"location" db:"location"`
	BasePrice      int       `json:"base_price" db:"base_price"`
	Discount       float64   `json:"discount" db:"discount"`       // 0-1 discount factor
	Stock          int       `json:"stock" db:"stock"`             // Available quantity
	ReputationReq  int       `json:"reputation_req" db:"reputation_req"` // Required vendor reputation
	BlackMarket    bool      `json:"black_market" db:"black_market"`     // Is this black market?
	AvailableFrom  time.Time `json:"available_from" db:"available_from"`
	AvailableUntil *time.Time `json:"available_until" db:"available_until"`
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
}

// CyberwareQuest represents quest-related cyberware unlocks
type CyberwareQuest struct {
	ID           string   `json:"id" db:"id"`
	CyberwareID  string   `json:"cyberware_id" db:"cyberware_id"`
	QuestID      string   `json:"quest_id" db:"quest_id"`
	UnlockStage  string   `json:"unlock_stage" db:"unlock_stage"` // "start", "middle", "end", "reward"
	Description  string   `json:"description" db:"description"`
	RewardType   string   `json:"reward_type" db:"reward_type"`   // "permanent", "temporary", "conditional"
	Requirements []string `json:"requirements" db:"requirements"` // Quest requirements
	CreatedAt    time.Time `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time `json:"updated_at" db:"updated_at"`
}

// CyberwareAnalytics provides analytics for cyberware usage
type CyberwareAnalytics struct {
	CyberwareID      string             `json:"cyberware_id"`
	TotalInstalls    int64              `json:"total_installs"`
	SuccessRate      float64            `json:"success_rate"`      // 0-1
	AvgLifespan      int64              `json:"avg_lifespan"`      // Days
	MalfunctionRate  float64            `json:"malfunction_rate"`  // Per month
	PopularUpgrades  []string           `json:"popular_upgrades"`
	RiskFactors      map[string]float64 `json:"risk_factors"`      // Risk type -> probability
	RevenueGenerated int64              `json:"revenue_generated"` // Total eddies
	LastUpdated      time.Time          `json:"last_updated"`
}

// InstallationRequest represents a cyberware installation request
type InstallationRequest struct {
	PlayerID    string            `json:"player_id"`
	CyberwareID string            `json:"cyberware_id"`
	VendorID    string            `json:"vendor_id,omitempty"`
	Priority    string            `json:"priority,omitempty"` // "standard", "rush", "emergency"
	Customizations map[string]interface{} `json:"customizations,omitempty"`
	Budget       int               `json:"budget,omitempty"` // Max eddies willing to spend
}

// InstallationResponse represents the response to an installation request
type InstallationResponse struct {
	Success         bool                   `json:"success"`
	RequestID       string                 `json:"request_id,omitempty"`
	EstimatedCost   int                    `json:"estimated_cost,omitempty"`
	EstimatedTime   int                    `json:"estimated_time,omitempty"` // minutes
	Risks           []CyberwareRisk        `json:"risks,omitempty"`
	Alternatives    []CyberwareAlternative `json:"alternatives,omitempty"`
	Error           string                 `json:"error,omitempty"`
}

// CyberwareAlternative represents an alternative cyberware option
type CyberwareAlternative struct {
	CyberwareID     string  `json:"cyberware_id"`
	Name            string  `json:"name"`
	Reason          string  `json:"reason"`          // Why this is suggested
	Compatibility   float64 `json:"compatibility"`   // 0-1 compatibility score
	CostDifference  int     `json:"cost_difference"` // Eddies difference from requested
}

// CyberwareOptimization represents optimization settings for cyberware
type CyberwareOptimization struct {
	PlayerID       string                 `json:"player_id"`
	CyberwareID    string                 `json:"cyberware_id"`
	OptimizationType string               `json:"optimization_type"` // "performance", "stability", "efficiency"
	Settings       map[string]interface{} `json:"settings"`
	PerformanceGain float64               `json:"performance_gain"`  // 0-1 improvement
	RiskIncrease    float64               `json:"risk_increase"`     // 0-1 risk increase
	Cost            int                   `json:"cost"`              // Eddies
	AppliedAt       time.Time             `json:"applied_at"`
	ExpiresAt       *time.Time            `json:"expires_at,omitempty"`
}
