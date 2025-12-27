// NPC Hiring models for social system
// Issue: #140875791

package models

import (
	"time"

	"github.com/google/uuid"
)

// NPCHiring represents an NPC hiring contract
type NPCHiring struct {
	ID            uuid.UUID     `json:"id" db:"id"`
	NPCID         uuid.UUID     `json:"npc_id" db:"npc_id"`
	NPCName       string        `json:"npc_name" db:"npc_name"`
	NPCType       NPCType       `json:"npc_type" db:"npc_type"`
	EmployerID    uuid.UUID     `json:"employer_id" db:"employer_id"`
	ServiceType   ServiceType   `json:"service_type" db:"service_type"`
	ContractTerms ContractTerms `json:"contract_terms" db:"contract_terms"`
	Status        HiringStatus  `json:"status" db:"status"`
	HiredAt       time.Time     `json:"hired_at" db:"hired_at"`
	ExpiresAt     *time.Time    `json:"expires_at" db:"expires_at"`
	LastActive    time.Time     `json:"last_active" db:"last_active"`
	CreatedAt     time.Time     `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time     `json:"updated_at" db:"updated_at"`
}

// NPCType defines the type of NPC that can be hired
type NPCType string

const (
	NPCTypeMercenary   NPCType = "mercenary"
	NPCTypeFixer       NPCType = "fixer"
	NPCTypeRipperdoc   NPCType = "ripperdoc"
	NPCTypeNomad       NPCType = "nomad"
	NPCTypeTechie      NPCType = "techie"
	NPCTypeSolo        NPCType = "solo"
	NPCTypeCorporate   NPCType = "corporate"
	NPCTypeStreetKid   NPCType = "street_kid"
)

// ServiceType defines the type of service the NPC provides
type ServiceType string

const (
	ServiceTypeCombat      ServiceType = "combat"
	ServiceTypeEspionage   ServiceType = "espionage"
	ServiceTypeProtection  ServiceType = "protection"
	ServiceTypeTransport   ServiceType = "transport"
	ServiceTypeCrafting    ServiceType = "crafting"
	ServiceTypeInformation ServiceType = "information"
	ServiceTypeMedical     ServiceType = "medical"
	ServiceTypeTechSupport ServiceType = "tech_support"
)

// HiringStatus represents the status of an NPC hiring
type HiringStatus string

const (
	HiringStatusActive      HiringStatus = "active"
	HiringStatusInactive    HiringStatus = "inactive"
	HiringStatusTerminated  HiringStatus = "terminated"
	HiringStatusExpired     HiringStatus = "expired"
	HiringStatusOnLeave     HiringStatus = "on_leave"
)

// ContractTerms represents the terms of the hiring contract
type ContractTerms struct {
	Duration        time.Duration `json:"duration"`
	Payment         PaymentTerms  `json:"payment"`
	WorkingHours    string        `json:"working_hours"`    // e.g., "24/7", "business_hours"
	RiskLevel       string        `json:"risk_level"`       // "low", "medium", "high", "extreme"
	TerminationClauses []string   `json:"termination_clauses"`
	Bonuses         []BonusTerm   `json:"bonuses"`
	Penalties       []PenaltyTerm `json:"penalties"`
}

// PaymentTerms represents payment terms for the contract
type PaymentTerms struct {
	BaseSalary      int         `json:"base_salary"`       // Base payment per period
	PaymentPeriod   string      `json:"payment_period"`    // "hourly", "daily", "weekly", "monthly"
	BonusStructure  []BonusTerm `json:"bonus_structure"`
	Deductions      []Deduction `json:"deductions"`
	Currency        string      `json:"currency"`
}

// BonusTerm represents a bonus in the contract
type BonusTerm struct {
	Type        string `json:"type"`        // "performance", "risk", "loyalty", etc.
	Description string `json:"description"`
	Amount      int    `json:"amount"`
	Conditions  string `json:"conditions"`
}

// PenaltyTerm represents a penalty in the contract
type PenaltyTerm struct {
	Type        string `json:"type"`        // "breach", "poor_performance", etc.
	Description string `json:"description"`
	Amount      int    `json:"amount"`
	Conditions  string `json:"conditions"`
}

// Deduction represents a deduction from payment
type Deduction struct {
	Type        string `json:"type"`        // "tax", "insurance", "equipment", etc.
	Description string `json:"description"`
	Amount      int    `json:"amount"`
	Percentage  float64 `json:"percentage"` // For percentage-based deductions
}

// NPCAvailability represents the availability of an NPC for hiring
type NPCAvailability struct {
	NPCID       uuid.UUID `json:"npc_id"`
	NPCName     string    `json:"npc_name"`
	NPCType     NPCType   `json:"npc_type"`
	Available   bool      `json:"available"`
	CurrentHire *NPCHiring `json:"current_hire,omitempty"`
	Skills      []NPCSkill `json:"skills"`
	Rates       []RateCard `json:"rates"`
	Reputation  int       `json:"reputation"` // Overall reputation score
	LastActive  time.Time `json:"last_active"`
}

// NPCSkill represents a skill that an NPC possesses
type NPCSkill struct {
	SkillName   string  `json:"skill_name"`
	SkillLevel  int     `json:"skill_level"`   // 1-10
	Experience  int     `json:"experience"`    // Years of experience
	Specialization string `json:"specialization,omitempty"`
}

// RateCard represents pricing for NPC services
type RateCard struct {
	ServiceType ServiceType `json:"service_type"`
	BaseRate    int         `json:"base_rate"`    // Base payment per hour/day
	RiskMultiplier float64  `json:"risk_multiplier"` // Multiplier for risky jobs
	ExperienceBonus float64 `json:"experience_bonus"` // Bonus for experienced NPCs
}

// NPCPerformance represents performance metrics for hired NPCs
type NPCPerformance struct {
	HiringID       uuid.UUID `json:"hiring_id"`
	NPCID          uuid.UUID `json:"npc_id"`
	EmployerID     uuid.UUID `json:"employer_id"`
	Period         string    `json:"period"` // "daily", "weekly", "monthly"
	MissionsCompleted int    `json:"missions_completed"`
	SuccessRate     float64  `json:"success_rate"` // 0-100
	ClientSatisfaction float64 `json:"client_satisfaction"` // 0-100
	Earnings        int       `json:"earnings"` // Total earnings for the period
	BonusesEarned   int       `json:"bonuses_earned"`
	PenaltiesApplied int      `json:"penalties_applied"`
	CalculatedAt    time.Time `json:"calculated_at"`
}

// NPCContractNegotiation represents a contract negotiation session
type NPCContractNegotiation struct {
	ID            uuid.UUID      `json:"id"`
	NPCID         uuid.UUID      `json:"npc_id"`
	EmployerID    uuid.UUID      `json:"employer_id"`
	ProposedTerms ContractTerms  `json:"proposed_terms"`
	CounterOffers []CounterOffer `json:"counter_offers"`
	Status        NegotiationStatus `json:"status"`
	CreatedAt     time.Time      `json:"created_at"`
	ExpiresAt     time.Time      `json:"expires_at"`
}

// CounterOffer represents a counter-offer in contract negotiation
type CounterOffer struct {
	ID         uuid.UUID     `json:"id"`
	ProposedBy uuid.UUID     `json:"proposed_by"`
	Terms      ContractTerms `json:"terms"`
	Message    string        `json:"message"`
	ProposedAt time.Time     `json:"proposed_at"`
}

// NegotiationStatus represents the status of contract negotiation
type NegotiationStatus string

const (
	NegotiationStatusOpen     NegotiationStatus = "open"
	NegotiationStatusAccepted NegotiationStatus = "accepted"
	NegotiationStatusRejected NegotiationStatus = "rejected"
	NegotiationStatusExpired  NegotiationStatus = "expired"
)
