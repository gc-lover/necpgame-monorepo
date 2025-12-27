// NPC Hiring World Impact Service
// Issue: #140894831

package npchiringimpact

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Service provides analysis of NPC hiring impacts on the game world
type Service struct {
	logger *zap.Logger
	// TODO: Add database connections, external service clients
}

// NewService creates a new NPC hiring world impact service
func NewService(logger *zap.Logger) (*Service, error) {
	return &Service{
		logger: logger,
	}, nil
}

// Data structures

type NPCHireWorldImpact struct {
	HireID            uuid.UUID         `json:"hire_id"`
	NPCID             uuid.UUID         `json:"npc_id"`
	NPCName           string            `json:"npc_name"`
	EconomicImpact    EconomicImpact    `json:"economic_impact"`
	SocialImpact      SocialImpact      `json:"social_impact"`
	PoliticalImpact   PoliticalImpact   `json:"political_impact"`
	RegionalDevelopment RegionalDevelopment `json:"regional_development"`
	Timestamp         time.Time         `json:"timestamp"`
}

type EconomicImpact struct {
	WageDistribution WageDistribution `json:"wage_distribution"`
	MarketEffects    MarketEffects    `json:"market_effects"`
	TaxRevenue       int              `json:"tax_revenue"`
}

type WageDistribution struct {
	TotalPaid         int     `json:"total_paid"`
	AverageHourlyRate float64 `json:"average_hourly_rate"`
	Currency          string  `json:"currency"`
}

type MarketEffects struct {
	ServicePriceChanges map[string]float64 `json:"service_price_changes"`
	CompetitionLevel    float64            `json:"competition_level"`
}

type SocialImpact struct {
	CommunityTrust     float64         `json:"community_trust"`
	SecurityPerception float64         `json:"security_perception"`
	NPCIntegrationLevel float64        `json:"npc_integration_level"`
	ReputationEffects  []ReputationEffect `json:"reputation_effects"`
}

type ReputationEffect struct {
	FactionID        uuid.UUID `json:"faction_id"`
	ReputationChange int       `json:"reputation_change"`
	Reason           string    `json:"reason"`
}

type PoliticalImpact struct {
	FactionRelations    []FactionRelation `json:"faction_relations"`
	PowerBalanceShift   PowerBalanceShift `json:"power_balance_shift"`
	PoliticalEvents     []PoliticalEvent  `json:"political_events"`
}

type FactionRelation struct {
	FactionID      uuid.UUID `json:"faction_id"`
	RelationChange int       `json:"relation_change"`
	InfluenceLevel string    `json:"influence_level"`
}

type PowerBalanceShift struct {
	RegionStability map[string]float64 `json:"region_stability"`
	FactionDominance map[string]float64 `json:"faction_dominance"`
}

type PoliticalEvent struct {
	EventType   string  `json:"event_type"`
	Probability float64 `json:"probability"`
	Description string  `json:"description"`
}

type RegionalDevelopment struct {
	InfrastructureImprovements []string `json:"infrastructure_improvements"`
	EconomicGrowth            float64  `json:"economic_growth"`
	PopulationChanges         PopulationChanges `json:"population_changes"`
	QualityOfLife            float64 `json:"quality_of_life"`
}

type PopulationChanges struct {
	ImmigrationRate float64 `json:"immigration_rate"`
	EmigrationRate  float64 `json:"emigration_rate"`
}

type WorldImpactsSummary struct {
	TotalHiredNPCs    int               `json:"total_hired_npcs"`
	ActiveRegions     int               `json:"active_regions"`
	EconomicSummary   EconomicImpact    `json:"economic_summary"`
	SocialSummary     SocialImpact      `json:"social_summary"`
	PoliticalSummary  PoliticalImpact   `json:"political_summary"`
	TopImpactedRegions []RegionImpact   `json:"top_impacted_regions"`
	TimePeriod       string            `json:"time_period"`
}

type RegionImpact struct {
	RegionID    uuid.UUID `json:"region_id"`
	RegionName  string    `json:"region_name"`
	ImpactScore float64   `json:"impact_score"`
}

type NPCHiringPredictionRequest struct {
	NPCID             uuid.UUID `json:"npc_id"`
	HireDurationHours int       `json:"hire_duration_hours"`
	ActivityLevel     string    `json:"activity_level"`
	RegionID          *uuid.UUID `json:"region_id,omitempty"`
	SpecialConditions []string  `json:"special_conditions,omitempty"`
}

type NPCHiringImpactPrediction struct {
	NPCID            uuid.UUID                      `json:"npc_id"`
	PredictedImpacts PredictedImpacts               `json:"predicted_impacts"`
	RiskAssessment   RiskAssessment                 `json:"risk_assessment"`
	Recommendations  []string                       `json:"recommendations"`
}

type PredictedImpacts struct {
	ImmediateEffects NPCHireWorldImpact `json:"immediate_effects"`
	ShortTermEffects NPCHireWorldImpact `json:"short_term_effects"`
	LongTermEffects  NPCHireWorldImpact `json:"long_term_effects"`
}

type RiskAssessment struct {
	PoliticalRisk float64 `json:"political_risk"`
	EconomicRisk  float64 `json:"economic_risk"`
	SocialRisk    float64 `json:"social_risk"`
}

type NPCLoyaltyEffects struct {
	LoyaltyBreakdown     LoyaltyBreakdown             `json:"loyalty_breakdown"`
	WorldEffectsByLoyalty WorldEffectsByLoyalty       `json:"world_effects_by_loyalty"`
	LoyaltyTrends        LoyaltyTrends                `json:"loyalty_trends"`
}

type LoyaltyBreakdown struct {
	HighLoyaltyNPCs   int `json:"high_loyalty_npcs"`
	MediumLoyaltyNPCs int `json:"medium_loyalty_npcs"`
	LowLoyaltyNPCs    int `json:"low_loyalty_npcs"`
}

type WorldEffectsByLoyalty struct {
	HighLoyaltyBenefits     []string `json:"high_loyalty_benefits"`
	LowLoyaltyConsequences  []string `json:"low_loyalty_consequences"`
}

type LoyaltyTrends struct {
	AverageLoyaltyChange  float64 `json:"average_loyalty_change"`
	LoyaltyStabilityIndex float64 `json:"loyalty_stability_index"`
}

type EconomicImpactAnalysis struct {
	RegionID             uuid.UUID                    `json:"region_id"`
	TimePeriod           string                       `json:"time_period"`
	NPCTypeDistribution  map[string]int               `json:"npc_type_distribution"`
	CostAnalysis         CostAnalysis                 `json:"cost_analysis"`
	MarketDynamics       MarketDynamics               `json:"market_dynamics"`
}

type CostAnalysis struct {
	TotalCosts            int     `json:"total_costs"`
	AverageCostPerHour    float64 `json:"average_cost_per_hour"`
	CostEfficiencyRatio   float64 `json:"cost_efficiency_ratio"`
}

type MarketDynamics struct {
	SupplyDemandRatio float64 `json:"supply_demand_ratio"`
	PriceVolatility   float64 `json:"price_volatility"`
	MarketSaturation  float64 `json:"market_saturation"`
}

type SocialChangesAnalysis struct {
	RegionID             uuid.UUID          `json:"region_id"`
	ChangeMetrics        ChangeMetrics      `json:"change_metrics"`
	NPCSocialIntegration NPCSocialIntegration `json:"npc_social_integration"`
}

type ChangeMetrics struct {
	CrimeRateChange       float64 `json:"crime_rate_change"`
	SecurityIndexChange   float64 `json:"security_index_change"`
	CommunitySatisfaction float64 `json:"community_satisfaction"`
}

type NPCSocialIntegration struct {
	AcceptanceRate       float64 `json:"acceptance_rate"`
	SocialBondsFormed    int     `json:"social_bonds_formed"`
	CulturalExchangeEvents int   `json:"cultural_exchange_events"`
}

type PoliticalConsequencesRequest struct {
	NPCID       uuid.UUID              `json:"npc_id"`
	HireContext HireContext            `json:"hire_context"`
	SpecialFactors []string            `json:"special_factors,omitempty"`
}

type HireContext struct {
	MissionType      string    `json:"mission_type"`
	TargetFaction    *uuid.UUID `json:"target_faction,omitempty"`
	RegionID         *uuid.UUID `json:"region_id,omitempty"`
	ExpectedDuration int       `json:"expected_duration"`
}

type PoliticalConsequences struct {
	ImmediateConsequences []ImmediateConsequence `json:"immediate_consequences"`
	LongTermImplications  LongTermImplications  `json:"long_term_implications"`
	RiskMitigationStrategies []RiskMitigationStrategy `json:"risk_mitigation_strategies"`
}

type ImmediateConsequence struct {
	ConsequenceType  string      `json:"consequence_type"`
	Severity         string      `json:"severity"`
	AffectedFactions []uuid.UUID `json:"affected_factions"`
	Probability      float64     `json:"probability"`
}

type LongTermImplications struct {
	GeopoliticalChanges     []string `json:"geopolitical_changes"`
	PowerBalanceAlterations []string `json:"power_balance_alterations"`
	StrategicOpportunities  []string `json:"strategic_opportunities"`
}

type RiskMitigationStrategy struct {
	Strategy                 string  `json:"strategy"`
	Effectiveness           float64 `json:"effectiveness"`
	ImplementationComplexity string  `json:"implementation_complexity"`
}

// Business logic methods

// GetNPCHireWorldImpact returns world impact for a specific NPC hire
func (s *Service) GetNPCHireWorldImpact(ctx context.Context, hireID uuid.UUID) (*NPCHireWorldImpact, error) {
	s.logger.Info("Getting NPC hire world impact",
		zap.String("hire_id", hireID.String()))

	// TODO: Get actual hire data from database
	// For now, return mock data

	economicImpact := EconomicImpact{
		WageDistribution: WageDistribution{
			TotalPaid:         2500,
			AverageHourlyRate: 125.0,
			Currency:          "eurodollar",
		},
		MarketEffects: MarketEffects{
			ServicePriceChanges: map[string]float64{
				"protection":  5.2,
				"security":    3.1,
				"investigation": -2.8,
			},
			CompetitionLevel: 78.5,
		},
		TaxRevenue: 375,
	}

	socialImpact := SocialImpact{
		CommunityTrust:     82.3,
		SecurityPerception: 88.7,
		NPCIntegrationLevel: 76.4,
		ReputationEffects: []ReputationEffect{
			{
				FactionID:        uuid.New(),
				ReputationChange: 15,
				Reason:           "successful_protection_mission",
			},
		},
	}

	politicalImpact := PoliticalImpact{
		FactionRelations: []FactionRelation{
			{
				FactionID:      uuid.New(),
				RelationChange: 8,
				InfluenceLevel: "minor",
			},
		},
		PowerBalanceShift: PowerBalanceShift{
			RegionStability: map[string]float64{
				"downtown": 85.2,
			},
			FactionDominance: map[string]float64{
				"corporation_a": 12.3,
			},
		},
		PoliticalEvents: []PoliticalEvent{
			{
				EventType:   "alliance_formation",
				Probability: 15.5,
				Description: "Potential alliance between local factions",
			},
		},
	}

	regionalDevelopment := RegionalDevelopment{
		InfrastructureImprovements: []string{
			"Security monitoring systems upgraded",
			"Emergency response protocols enhanced",
		},
		EconomicGrowth: 3.2,
		PopulationChanges: PopulationChanges{
			ImmigrationRate: 1.8,
			EmigrationRate:  0.5,
		},
		QualityOfLife: 7.8,
	}

	return &NPCHireWorldImpact{
		HireID:             hireID,
		NPCID:              uuid.New(),
		NPCName:            "Marcus 'Ghost' Chen",
		EconomicImpact:     economicImpact,
		SocialImpact:       socialImpact,
		PoliticalImpact:    politicalImpact,
		RegionalDevelopment: regionalDevelopment,
		Timestamp:          time.Now(),
	}, nil
}

// GetWorldImpactsFromNPCHiring returns overall world impacts from NPC hiring
func (s *Service) GetWorldImpactsFromNPCHiring(ctx context.Context, regionID *uuid.UUID, timePeriod string) (*WorldImpactsSummary, error) {
	s.logger.Info("Getting world impacts from NPC hiring",
		zap.String("time_period", timePeriod))

	// TODO: Aggregate data from multiple hires
	// For now, return mock aggregated data

	economicSummary := EconomicImpact{
		WageDistribution: WageDistribution{
			TotalPaid:         15000,
			AverageHourlyRate: 110.0,
			Currency:          "eurodollar",
		},
		MarketEffects: MarketEffects{
			ServicePriceChanges: map[string]float64{
				"security": 8.5,
				"espionage": 12.3,
			},
			CompetitionLevel: 65.8,
		},
		TaxRevenue: 2250,
	}

	socialSummary := SocialImpact{
		CommunityTrust:     78.9,
		SecurityPerception: 84.2,
		NPCIntegrationLevel: 72.1,
		ReputationEffects:  []ReputationEffect{}, // Would be aggregated
	}

	politicalSummary := PoliticalImpact{
		FactionRelations:   []FactionRelation{}, // Would be aggregated
		PowerBalanceShift: PowerBalanceShift{
			RegionStability:  map[string]float64{"global": 79.5},
			FactionDominance: map[string]float64{},
		},
		PoliticalEvents: []PoliticalEvent{},
	}

	topRegions := []RegionImpact{
		{
			RegionID:    uuid.New(),
			RegionName:  "Night City Downtown",
			ImpactScore: 85.7,
		},
		{
			RegionID:    uuid.New(),
			RegionName:  "Badlands Border",
			ImpactScore: 72.3,
		},
	}

	return &WorldImpactsSummary{
		TotalHiredNPCs:     23,
		ActiveRegions:      8,
		EconomicSummary:    economicSummary,
		SocialSummary:      socialSummary,
		PoliticalSummary:   politicalSummary,
		TopImpactedRegions: topRegions,
		TimePeriod:         timePeriod,
	}, nil
}

// PredictNPCHiringImpact predicts impact of NPC hiring
func (s *Service) PredictNPCHiringImpact(ctx context.Context, req NPCHiringPredictionRequest) (*NPCHiringImpactPrediction, error) {
	s.logger.Info("Predicting NPC hiring impact",
		zap.String("npc_id", req.NPCID.String()),
		zap.Int("duration", req.HireDurationHours))

	// TODO: Implement sophisticated prediction algorithms
	// For now, return mock predictions based on input parameters

	activityMultiplier := s.getActivityMultiplier(req.ActivityLevel)
	durationMultiplier := math.Min(float64(req.HireDurationHours)/24.0, 5.0) // Cap at 5 days equivalent

	// Calculate risk assessment
	politicalRisk := s.calculatePoliticalRisk(req) * activityMultiplier
	economicRisk := s.calculateEconomicRisk(req) * durationMultiplier
	socialRisk := s.calculateSocialRisk(req) * activityMultiplier

	// Generate recommendations
	recommendations := s.generateRecommendations(req, politicalRisk, economicRisk, socialRisk)

	// Create mock impact predictions
	immediateEffects := s.createMockImpact(req, 1.0)
	shortTermEffects := s.createMockImpact(req, activityMultiplier*0.7)
	longTermEffects := s.createMockImpact(req, durationMultiplier*0.3)

	return &NPCHiringImpactPrediction{
		NPCID: req.NPCID,
		PredictedImpacts: PredictedImpacts{
			ImmediateEffects: *immediateEffects,
			ShortTermEffects: *shortTermEffects,
			LongTermEffects:  *longTermEffects,
		},
		RiskAssessment: RiskAssessment{
			PoliticalRisk: politicalRisk,
			EconomicRisk:  economicRisk,
			SocialRisk:    socialRisk,
		},
		Recommendations: recommendations,
	}, nil
}

// GetNPCLoyaltyEffects returns loyalty effects on the world
func (s *Service) GetNPCLoyaltyEffects(ctx context.Context, npcID *uuid.UUID, loyaltyLevel *int, regionID *uuid.UUID) (*NPCLoyaltyEffects, error) {
	s.logger.Info("Getting NPC loyalty effects")

	// TODO: Get actual loyalty data
	// For now, return mock loyalty effects

	loyaltyBreakdown := LoyaltyBreakdown{
		HighLoyaltyNPCs:   12,
		MediumLoyaltyNPCs: 8,
		LowLoyaltyNPCs:    3,
	}

	highLoyaltyBenefits := []string{
		"Increased regional stability",
		"Better faction cooperation",
		"Enhanced economic growth",
		"Improved community trust",
		"Advanced infrastructure development",
	}

	lowLoyaltyConsequences := []string{
		"Decreased regional security",
		"Faction conflicts escalation",
		"Economic downturns",
		"Community distrust",
		"Infrastructure neglect",
	}

	loyaltyTrends := LoyaltyTrends{
		AverageLoyaltyChange:  0.3,
		LoyaltyStabilityIndex: 78.5,
	}

	return &NPCLoyaltyEffects{
		LoyaltyBreakdown:     loyaltyBreakdown,
		WorldEffectsByLoyalty: WorldEffectsByLoyalty{
			HighLoyaltyBenefits:    highLoyaltyBenefits,
			LowLoyaltyConsequences: lowLoyaltyConsequences,
		},
		LoyaltyTrends: loyaltyTrends,
	}, nil
}

// GetNPCHiringEconomicImpact returns economic impact analysis
func (s *Service) GetNPCHiringEconomicImpact(ctx context.Context, regionID uuid.UUID, npcType, timeRange string) (*EconomicImpactAnalysis, error) {
	s.logger.Info("Getting NPC hiring economic impact",
		zap.String("region_id", regionID.String()),
		zap.String("npc_type", npcType),
		zap.String("time_range", timeRange))

	// TODO: Get actual economic data
	// For now, return mock analysis

	npcTypeDistribution := map[string]int{
		"mercenary":   8,
		"fixer":       5,
		"bodyguard":   6,
		"hacker":      3,
		"informant":   4,
	}

	costAnalysis := CostAnalysis{
		TotalCosts:         45000,
		AverageCostPerHour: 95.0,
		CostEfficiencyRatio: 2.3,
	}

	marketDynamics := MarketDynamics{
		SupplyDemandRatio: 1.4,
		PriceVolatility:   12.5,
		MarketSaturation:  67.8,
	}

	return &EconomicImpactAnalysis{
		RegionID:            regionID,
		TimePeriod:          timeRange,
		NPCTypeDistribution: npcTypeDistribution,
		CostAnalysis:        costAnalysis,
		MarketDynamics:      marketDynamics,
	}, nil
}

// GetNPCHiringSocialChanges returns social changes analysis
func (s *Service) GetNPCHiringSocialChanges(ctx context.Context, regionID uuid.UUID, changeType string) (*SocialChangesAnalysis, error) {
	s.logger.Info("Getting NPC hiring social changes",
		zap.String("region_id", regionID.String()),
		zap.String("change_type", changeType))

	// TODO: Get actual social data
	// For now, return mock analysis

	changeMetrics := ChangeMetrics{
		CrimeRateChange:       -8.5,
		SecurityIndexChange:   12.3,
		CommunitySatisfaction: 15.7,
	}

	npcSocialIntegration := NPCSocialIntegration{
		AcceptanceRate:         78.9,
		SocialBondsFormed:      45,
		CulturalExchangeEvents: 12,
	}

	return &SocialChangesAnalysis{
		RegionID:             regionID,
		ChangeMetrics:        changeMetrics,
		NPCSocialIntegration: npcSocialIntegration,
	}, nil
}

// CalculatePoliticalConsequences calculates political consequences of NPC hiring
func (s *Service) CalculatePoliticalConsequences(ctx context.Context, req PoliticalConsequencesRequest) (*PoliticalConsequences, error) {
	s.logger.Info("Calculating political consequences",
		zap.String("npc_id", req.NPCID.String()),
		zap.String("mission_type", req.HireContext.MissionType))

	// TODO: Implement political consequence calculations
	// For now, return mock consequences

	immediateConsequences := []ImmediateConsequence{
		{
			ConsequenceType:  "diplomatic_tension",
			Severity:         "moderate",
			AffectedFactions: []uuid.UUID{uuid.New(), uuid.New()},
			Probability:      35.5,
		},
		{
			ConsequenceType:  "alliance_strain",
			Severity:         "minor",
			AffectedFactions: []uuid.UUID{uuid.New()},
			Probability:      22.1,
		},
	}

	longTermImplications := LongTermImplications{
		GeopoliticalChanges: []string{
			"Shift in regional power balance",
			"Changes in faction alliances",
		},
		PowerBalanceAlterations: []string{
			"Strengthened corporate influence",
			"Decreased nomad presence",
		},
		StrategicOpportunities: []string{
			"New trade routes opened",
			"Intelligence network expansion",
		},
	}

	riskMitigationStrategies := []RiskMitigationStrategy{
		{
			Strategy:                 "Maintain neutrality in faction conflicts",
			Effectiveness:           75.0,
			ImplementationComplexity: "medium",
		},
		{
			Strategy:                 "Build diplomatic bridges",
			Effectiveness:           60.0,
			ImplementationComplexity: "high",
		},
	}

	return &PoliticalConsequences{
		ImmediateConsequences:    immediateConsequences,
		LongTermImplications:     longTermImplications,
		RiskMitigationStrategies: riskMitigationStrategies,
	}, nil
}

// Helper methods

func (s *Service) getActivityMultiplier(activityLevel string) float64 {
	switch activityLevel {
	case "low":
		return 0.5
	case "medium":
		return 1.0
	case "high":
		return 1.8
	case "extreme":
		return 3.0
	default:
		return 1.0
	}
}

func (s *Service) calculatePoliticalRisk(req NPCHiringPredictionRequest) float64 {
	baseRisk := 20.0

	// Increase risk based on special conditions
	for _, condition := range req.SpecialConditions {
		switch condition {
		case "high_profile_mission":
			baseRisk += 30.0
		case "faction_sensitive":
			baseRisk += 25.0
		case "international_implications":
			baseRisk += 35.0
		case "media_attention":
			baseRisk += 15.0
		}
	}

	return math.Min(baseRisk, 95.0)
}

func (s *Service) calculateEconomicRisk(req NPCHiringPredictionRequest) float64 {
	baseRisk := 15.0

	// Economic risk increases with duration
	durationRisk := float64(req.HireDurationHours) * 0.1
	baseRisk += durationRisk

	return math.Min(baseRisk, 85.0)
}

func (s *Service) calculateSocialRisk(req NPCHiringPredictionRequest) float64 {
	baseRisk := 10.0

	// Social risk varies with activity level
	activityRisk := s.getActivityMultiplier(req.ActivityLevel) * 5.0
	baseRisk += activityRisk

	return math.Min(baseRisk, 75.0)
}

func (s *Service) generateRecommendations(req NPCHiringPredictionRequest, politicalRisk, economicRisk, socialRisk float64) []string {
	recommendations := []string{}

	if politicalRisk > 50 {
		recommendations = append(recommendations,
			"Consider diplomatic channels to minimize faction tensions")
		recommendations = append(recommendations,
			"Avoid missions that directly target faction leaders")
	}

	if economicRisk > 40 {
		recommendations = append(recommendations,
			"Monitor market volatility and adjust pricing accordingly")
		recommendations = append(recommendations,
			"Consider shorter contract durations to reduce financial exposure")
	}

	if socialRisk > 30 {
		recommendations = append(recommendations,
			"Implement community engagement programs")
		recommendations = append(recommendations,
			"Monitor public opinion and adjust operations accordingly")
	}

	if len(recommendations) == 0 {
		recommendations = append(recommendations,
			"Operation appears low-risk, proceed with standard protocols")
	}

	return recommendations
}

func (s *Service) createMockImpact(req NPCHiringPredictionRequest, multiplier float64) *NPCHireWorldImpact {
	// Create mock impact scaled by multiplier
	return &NPCHireWorldImpact{
		HireID:   uuid.New(),
		NPCID:    req.NPCID,
		NPCName:  "Mock NPC",
		EconomicImpact: EconomicImpact{
			WageDistribution: WageDistribution{
				TotalPaid:         int(2000 * multiplier),
				AverageHourlyRate: 100.0 * multiplier,
				Currency:          "eurodollar",
			},
			MarketEffects: MarketEffects{
				ServicePriceChanges: map[string]float64{
					"security": multiplier * 3.0,
				},
				CompetitionLevel: 70.0,
			},
			TaxRevenue: int(300 * multiplier),
		},
		SocialImpact: SocialImpact{
			CommunityTrust:     80.0 + (multiplier * 5.0),
			SecurityPerception: 85.0 + (multiplier * 3.0),
			NPCIntegrationLevel: 75.0 + (multiplier * 8.0),
			ReputationEffects:  []ReputationEffect{},
		},
		PoliticalImpact: PoliticalImpact{
			FactionRelations:  []FactionRelation{},
			PowerBalanceShift: PowerBalanceShift{
				RegionStability:  map[string]float64{"region": 80.0 + (multiplier * 2.0)},
				FactionDominance: map[string]float64{},
			},
			PoliticalEvents: []PoliticalEvent{},
		},
		RegionalDevelopment: RegionalDevelopment{
			InfrastructureImprovements: []string{"Mock improvement"},
			EconomicGrowth:            multiplier * 2.0,
			PopulationChanges: PopulationChanges{
				ImmigrationRate: multiplier * 1.0,
				EmigrationRate:  multiplier * 0.2,
			},
			QualityOfLife: multiplier * 5.0,
		},
		Timestamp: time.Now(),
	}
}
