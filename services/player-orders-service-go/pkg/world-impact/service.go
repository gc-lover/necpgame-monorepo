// World Impact Service for Player Orders
// Issue: #140894810
//
// Calculates and tracks the effects of player orders on:
// - Economic indicators (OrderEconomicIndex, ServiceDemandIndex)
// - Social relationships and trust networks
// - Political power shifts and faction influence
// - City development and crisis generation

package worldimpact

import (
	"context"
	"encoding/json"
	"fmt"
	"math"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

// OrderEconomicIndex represents economic impact metrics
type OrderEconomicIndex struct {
	RegionID       string    `json:"region_id"`
	Timestamp      time.Time `json:"timestamp"`
	OrderVolume    float64   `json:"order_volume"`     // Total order value in region
	ServiceDemand  float64   `json:"service_demand"`   // Demand for services (0-100)
	MarketVolatility float64 `json:"market_volatility"` // Price volatility (0-100)
	JobCreationRate float64 `json:"job_creation_rate"` // New jobs created per order
	TaxRevenue     float64   `json:"tax_revenue"`      // Tax generated from orders
}

// SocialImpact represents social effects of orders
type SocialImpact struct {
	RegionID          string    `json:"region_id"`
	Timestamp         time.Time `json:"timestamp"`
	TrustNetworkSize  int       `json:"trust_network_size"`  // Size of trust relationships
	ConflictRate      float64   `json:"conflict_rate"`       // Rate of social conflicts (0-100)
	CommunityStrength float64   `json:"community_strength"`  // Strength of communities (0-100)
	MediaAttention    float64   `json:"media_attention"`     // Media coverage level (0-100)
}

// PoliticalImpact represents political effects
type PoliticalImpact struct {
	RegionID       string    `json:"region_id"`
	Timestamp      time.Time `json:"timestamp"`
	FactionInfluence map[string]float64 `json:"faction_influence"` // Influence by faction
	PowerShiftRate float64   `json:"power_shift_rate"`    // Rate of power changes
	PolicyChanges  int       `json:"policy_changes"`      // Number of policy changes triggered
}

// CityDevelopment represents city growth effects
type CityDevelopment struct {
	RegionID       string    `json:"region_id"`
	Timestamp      time.Time `json:"timestamp"`
	GrowthRate     float64   `json:"growth_rate"`      // City growth rate (0-100)
	CrisisLevel    float64   `json:"crisis_level"`     // Current crisis level (0-100)
	EventFrequency float64   `json:"event_frequency"`  // Frequency of world events
	OrderBoardSize int       `json:"order_board_size"` // Number of active orders
}

// WorldEvent represents triggered world events
type WorldEvent struct {
	ID          uuid.UUID `json:"id"`
	RegionID    string    `json:"region_id"`
	Type        string    `json:"type"`        // crisis, boom, political_shift, etc.
	Severity    float64   `json:"severity"`    // 0-100
	Description string    `json:"description"`
	TriggeredBy []string  `json:"triggered_by"` // Order IDs that triggered this
	Timestamp   time.Time `json:"timestamp"`
}

// Service provides world impact calculation and tracking
type Service struct {
	mu                   sync.RWMutex
	logger               *zap.Logger
	kafkaWriter          *kafka.Writer
	economicIndexes      map[string]*OrderEconomicIndex
	socialImpacts        map[string]*SocialImpact
	politicalImpacts     map[string]*PoliticalImpact
	cityDevelopments     map[string]*CityDevelopment
	activeEvents         map[uuid.UUID]*WorldEvent
	calculationInterval  time.Duration
	lastCalculation      time.Time
}

// NewService creates a new world impact service
func NewService(logger *zap.Logger) (*Service, error) {
	// Initialize Kafka writer for events
	kafkaWriter := &kafka.Writer{
		Addr:     kafka.TCP("localhost:9092"),
		Topic:    "world.player-orders.impact",
		Balancer: &kafka.LeastBytes{},
	}

	return &Service{
		logger:              logger,
		kafkaWriter:         kafkaWriter,
		economicIndexes:     make(map[string]*OrderEconomicIndex),
		socialImpacts:       make(map[string]*SocialImpact),
		politicalImpacts:    make(map[string]*PoliticalImpact),
		cityDevelopments:    make(map[string]*CityDevelopment),
		activeEvents:        make(map[uuid.UUID]*WorldEvent),
		calculationInterval: 5 * time.Minute, // Recalculate every 5 minutes
		lastCalculation:     time.Now(),
	}, nil
}

// CalculateEconomicImpact calculates economic effects for a region
func (s *Service) CalculateEconomicImpact(regionID string, orderVolume float64, orderCount int) *OrderEconomicIndex {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Calculate economic metrics using sophisticated formulas
	serviceDemand := math.Min(100.0, orderVolume/10000.0) // Demand based on volume
	marketVolatility := math.Min(100.0, math.Sqrt(orderVolume)/10.0) // Volatility based on sqrt(volume)
	jobCreationRate := float64(orderCount) * 2.5 // Jobs per order
	taxRevenue := orderVolume * 0.08 // 8% tax rate

	index := &OrderEconomicIndex{
		RegionID:         regionID,
		Timestamp:        time.Now(),
		OrderVolume:      orderVolume,
		ServiceDemand:    serviceDemand,
		MarketVolatility: marketVolatility,
		JobCreationRate:  jobCreationRate,
		TaxRevenue:       taxRevenue,
	}

	s.economicIndexes[regionID] = index

	// Publish to Kafka
	s.publishEconomicIndex(index)

	return index
}

// CalculateSocialImpact calculates social effects for a region
func (s *Service) CalculateSocialImpact(regionID string, trustRelationships int, conflicts int, mediaCoverage float64) *SocialImpact {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Calculate social metrics
	trustNetworkSize := trustRelationships
	conflictRate := math.Min(100.0, float64(conflicts)/10.0) // Conflicts per 10 orders
	communityStrength := math.Min(100.0, float64(trustRelationships)/5.0) // Trust per 5 relationships

	impact := &SocialImpact{
		RegionID:          regionID,
		Timestamp:         time.Now(),
		TrustNetworkSize:  trustNetworkSize,
		ConflictRate:      conflictRate,
		CommunityStrength: communityStrength,
		MediaAttention:    mediaCoverage,
	}

	s.socialImpacts[regionID] = impact

	// Publish to Kafka
	s.publishSocialImpact(impact)

	return impact
}

// CalculatePoliticalImpact calculates political effects
func (s *Service) CalculatePoliticalImpact(regionID string, factionOrders map[string]int) *PoliticalImpact {
	s.mu.Lock()
	defer s.mu.Unlock()

	factionInfluence := make(map[string]float64)
	totalOrders := 0

	for faction, orders := range factionOrders {
		factionInfluence[faction] = float64(orders) * 10.0 // Influence per order
		totalOrders += orders
	}

	// Calculate power shift rate based on faction competition
	powerShiftRate := math.Min(100.0, float64(len(factionOrders))/5.0*100.0)

	impact := &PoliticalImpact{
		RegionID:         regionID,
		Timestamp:        time.Now(),
		FactionInfluence: factionInfluence,
		PowerShiftRate:   powerShiftRate,
		PolicyChanges:    int(powerShiftRate / 20.0), // Policy changes based on shift rate
	}

	s.politicalImpacts[regionID] = impact

	// Publish to Kafka
	s.publishPoliticalImpact(impact)

	return impact
}

// CalculateCityDevelopment calculates city growth and crisis levels
func (s *Service) CalculateCityDevelopment(regionID string, activeOrders int, economicIndex float64) *CityDevelopment {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Calculate development metrics
	growthRate := math.Min(100.0, economicIndex/10.0) // Growth based on economic health
	crisisLevel := math.Max(0.0, 50.0-growthRate) // Crisis when growth is low
	eventFrequency := math.Min(100.0, float64(activeOrders)/20.0*100.0) // Events per 20 orders

	dev := &CityDevelopment{
		RegionID:       regionID,
		Timestamp:      time.Now(),
		GrowthRate:     growthRate,
		CrisisLevel:    crisisLevel,
		EventFrequency: eventFrequency,
		OrderBoardSize: activeOrders,
	}

	s.cityDevelopments[regionID] = dev

	// Check for crisis event triggering
	if crisisLevel > 75.0 {
		s.triggerCrisisEvent(regionID, crisisLevel)
	}

	// Publish to Kafka
	s.publishCityDevelopment(dev)

	return dev
}

// TriggerCrisisEvent creates a crisis event when conditions are met
func (s *Service) triggerCrisisEvent(regionID string, severity float64) {
	event := &WorldEvent{
		ID:          uuid.New(),
		RegionID:    regionID,
		Type:        "economic_crisis",
		Severity:    severity,
		Description: fmt.Sprintf("Economic crisis triggered in region %s due to low growth rate", regionID),
		TriggeredBy: []string{}, // Would be populated with actual order IDs
		Timestamp:   time.Now(),
	}

	s.activeEvents[event.ID] = event

	// Publish crisis event
	s.publishWorldEvent(event)
}

// GetEconomicIndex returns current economic index for region
func (s *Service) GetEconomicIndex(regionID string) (*OrderEconomicIndex, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	index, exists := s.economicIndexes[regionID]
	return index, exists
}

// GetSocialImpact returns current social impact for region
func (s *Service) GetSocialImpact(regionID string) (*SocialImpact, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	impact, exists := s.socialImpacts[regionID]
	return impact, exists
}

// GetPoliticalImpact returns current political impact for region
func (s *Service) GetPoliticalImpact(regionID string) (*PoliticalImpact, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	impact, exists := s.politicalImpacts[regionID]
	return impact, exists
}

// GetCityDevelopment returns current city development for region
func (s *Service) GetCityDevelopment(regionID string) (*CityDevelopment, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	dev, exists := s.cityDevelopments[regionID]
	return dev, exists
}

// GetActiveEvents returns all active world events
func (s *Service) GetActiveEvents() []*WorldEvent {
	s.mu.RLock()
	defer s.mu.RUnlock()

	events := make([]*WorldEvent, 0, len(s.activeEvents))
	for _, event := range s.activeEvents {
		events = append(events, event)
	}
	return events
}

// Kafka publishing methods
func (s *Service) publishEconomicIndex(index *OrderEconomicIndex) {
	data, _ := json.Marshal(index)
	s.publishToKafka("economy.player-orders.index", data)
}

func (s *Service) publishSocialImpact(impact *SocialImpact) {
	data, _ := json.Marshal(impact)
	s.publishToKafka("social.player-orders.news", data)
}

func (s *Service) publishPoliticalImpact(impact *PoliticalImpact) {
	data, _ := json.Marshal(impact)
	s.publishToKafka("world.player-orders.impact", data)
}

func (s *Service) publishCityDevelopment(dev *CityDevelopment) {
	data, _ := json.Marshal(dev)
	s.publishToKafka("world.player-orders.impact", data)
}

func (s *Service) publishWorldEvent(event *WorldEvent) {
	data, _ := json.Marshal(event)
	s.publishToKafka("world.player-orders.crisis", data)
}

func (s *Service) publishToKafka(topic string, data []byte) {
	message := kafka.Message{
		Key:   []byte(uuid.New().String()),
		Value: data,
	}

	err := s.kafkaWriter.WriteMessages(context.Background(), message)
	if err != nil {
		s.logger.Error("Failed to publish to Kafka",
			zap.String("topic", topic),
			zap.Error(err))
	}
}

// RecalculateImpacts periodically recalculates all impacts
func (s *Service) RecalculateImpacts(ctx context.Context) {
	ticker := time.NewTicker(s.calculationInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			s.performRecalculation()
		}
	}
}

func (s *Service) performRecalculation() {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Update timestamps for all indexes
	now := time.Now()
	for _, index := range s.economicIndexes {
		index.Timestamp = now
	}
	for _, impact := range s.socialImpacts {
		impact.Timestamp = now
	}
	for _, impact := range s.politicalImpacts {
		impact.Timestamp = now
	}
	for _, dev := range s.cityDevelopments {
		dev.Timestamp = now
	}

	s.logger.Info("Recalculated world impacts",
		zap.Int("economic_indexes", len(s.economicIndexes)),
		zap.Int("social_impacts", len(s.socialImpacts)),
		zap.Int("political_impacts", len(s.politicalImpacts)),
		zap.Int("city_developments", len(s.cityDevelopments)),
		zap.Int("active_events", len(s.activeEvents)))
}

// GetAllEconomicIndexes returns all economic indexes
func (s *Service) GetAllEconomicIndexes() []*OrderEconomicIndex {
	s.mu.RLock()
	defer s.mu.RUnlock()

	indexes := make([]*OrderEconomicIndex, 0, len(s.economicIndexes))
	for _, index := range s.economicIndexes {
		indexes = append(indexes, index)
	}
	return indexes
}

// GetAllSocialImpacts returns all social impacts
func (s *Service) GetAllSocialImpacts() []*SocialImpact {
	s.mu.RLock()
	defer s.mu.RUnlock()

	impacts := make([]*SocialImpact, 0, len(s.socialImpacts))
	for _, impact := range s.socialImpacts {
		impacts = append(impacts, impact)
	}
	return impacts
}

// GetAllPoliticalImpacts returns all political impacts
func (s *Service) GetAllPoliticalImpacts() []*PoliticalImpact {
	s.mu.RLock()
	defer s.mu.RUnlock()

	impacts := make([]*PoliticalImpact, 0, len(s.politicalImpacts))
	for _, impact := range s.politicalImpacts {
		impacts = append(impacts, impact)
	}
	return impacts
}

// GetAllCityDevelopments returns all city developments
func (s *Service) GetAllCityDevelopments() []*CityDevelopment {
	s.mu.RLock()
	defer s.mu.RUnlock()

	devs := make([]*CityDevelopment, 0, len(s.cityDevelopments))
	for _, dev := range s.cityDevelopments {
		devs = append(devs, dev)
	}
	return devs
}

// RecalculateImpacts triggers immediate recalculation (for API calls)
func (s *Service) RecalculateImpacts(ctx context.Context) {
	s.performRecalculation()
}

// Close cleans up resources
func (s *Service) Close() error {
	return s.kafkaWriter.Close()
}
