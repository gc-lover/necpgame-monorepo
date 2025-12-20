// Package server Issue: #44
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

// Service - интерфейс бизнес-логики
type Service interface {
	// CreateEvent Events CRUD
	CreateEvent(ctx context.Context, name, description, eventType, scale, frequency string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	UpdateEvent(ctx context.Context, id uuid.UUID, name, description, eventType, scale, frequency, status *string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	DeleteEvent(ctx context.Context, id uuid.UUID) error
	ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error)

	// GetActiveEvents Event state
	GetActiveEvents(ctx context.Context) ([]*WorldEvent, error)
	GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error)

	// ActivateEvent Event actions
	ActivateEvent(ctx context.Context, id uuid.UUID, activatedBy string) error
	DeactivateEvent(ctx context.Context, id uuid.UUID) error
	AnnounceEvent(ctx context.Context, id uuid.UUID, announcedBy, message string, channels []string) error

	// GetEraConfig Era-based event generation
	GetEraConfig(ctx context.Context, eraID string) (*EraConfig, error)
	ListEraConfigs(ctx context.Context) ([]*EraConfig, error)
	GenerateEventFromEra(ctx context.Context, eraID string, roll int) (*WorldEvent, error)
}

type service struct {
	repo        Repository
	redis       *redis.Client
	kafkaWriter *kafka.Writer
	logger      *zap.Logger
	eraConfigs  map[string]*EraConfig // In-memory cache of era configurations
}

// NewService создает новый сервис
func NewService(repo Repository, redis *redis.Client, kafkaWriter *kafka.Writer, logger *zap.Logger) *service {
	svc := &service{
		repo:        repo,
		redis:       redis,
		kafkaWriter: kafkaWriter,
		logger:      logger,
		eraConfigs:  make(map[string]*EraConfig),
	}

	// Initialize era configurations
	svc.initializeEraConfigs()

	return svc
}

// initializeEraConfigs загружает конфигурации эпох из документов
func (s *service) initializeEraConfigs() {
	// Era 1990-2000: Early Corporate Influence
	s.eraConfigs["1990-2000"] = &EraConfig{
		EraID:       "1990-2000",
		Name:        "Early Corporate Influence",
		StartYear:   1990,
		EndYear:     2000,
		Description: "Ранняя корпоративная экспансия и строительство Coronado City",
		DCDifficulty: map[string]int{
			"social":    12,
			"technical": 15,
			"combat":    13,
			"stealth":   14,
		},
		FactionAI: map[string]interface{}{
			"corpo_aggression": 0.3,
			"corpo_espionage":  0.4,
			"resistance_level": 0.2,
		},
		Economy: map[string]interface{}{
			"market_volatility": 0.6,
			"corpo_dominance":   0.8,
			"black_market":      0.3,
		},
		EventTable: []EraEventTemplate{
			{
				RollRange:   "01-20",
				Name:        "Corporate Merger",
				Description: "Две корпорации объявляют о слиянии",
				Type:        "economic",
				Scale:       "regional",
				Frequency:   "rare",
				Effects: map[string]interface{}{
					"market_impact": -0.2,
					"stock_changes": map[string]float64{
						"acquiring":   0.15,
						"acquired":    -0.1,
						"competitors": -0.05,
					},
				},
			},
			{
				RollRange:   "21-40",
				Name:        "Construction Accident",
				Description: "Инцидент на строительстве Coronado City",
				Type:        "disaster",
				Scale:       "local",
				Frequency:   "common",
				Effects: map[string]interface{}{
					"casualties":        5,
					"economic_impact":   -0.1,
					"reputation_damage": -0.15,
				},
			},
			// Add more events...
		},
	}

	// Era 2060-2077: Night City Independence
	s.eraConfigs["2060-2077"] = &EraConfig{
		EraID:       "2060-2077",
		Name:        "Night City Independence",
		StartYear:   2060,
		EndYear:     2077,
		Description: "Период независимости Night City и пика корпоративного влияния",
		DCDifficulty: map[string]int{
			"social":    16,
			"technical": 18,
			"combat":    17,
			"stealth":   19,
			"hacking":   20,
		},
		FactionAI: map[string]interface{}{
			"corpo_aggression": 0.7,
			"corpo_espionage":  0.8,
			"resistance_level": 0.6,
			"gang_activity":    0.9,
		},
		Economy: map[string]interface{}{
			"market_volatility": 0.8,
			"corpo_dominance":   0.9,
			"black_market":      0.7,
			"crypto_economy":    0.5,
		},
		EventTable: []EraEventTemplate{
			{
				RollRange:   "01-15",
				Name:        "Corporate Espionage",
				Description: "Крупная утечка корпоративных секретов",
				Type:        "espionage",
				Scale:       "city",
				Frequency:   "uncommon",
				Effects: map[string]interface{}{
					"data_breach": true,
					"stock_changes": map[string]float64{
						"victim": -0.25,
					},
					"black_market_boost": 0.3,
				},
			},
			{
				RollRange:   "16-30",
				Name:        "Gang War",
				Description: "Конфликт между уличными бандами",
				Type:        "conflict",
				Scale:       "district",
				Frequency:   "common",
				Effects: map[string]interface{}{
					"casualties":        15,
					"territory_changes": true,
					"mercenary_work":    0.4,
				},
			},
			// Add more events...
		},
	}

	// Era 2077: Corporate War and Aftermath
	s.eraConfigs["2077"] = &EraConfig{
		EraID:       "2077",
		Name:        "Corporate War and Aftermath",
		StartYear:   2077,
		EndYear:     2077,
		Description: "Кульминация корпоративных заговоров и Phantom Liberty",
		DCDifficulty: map[string]int{
			"social":     20,
			"technical":  22,
			"combat":     21,
			"stealth":    23,
			"hacking":    24,
			"leadership": 25,
		},
		FactionAI: map[string]interface{}{
			"corpo_aggression":  1.0,
			"corpo_espionage":   1.0,
			"resistance_level":  0.8,
			"gang_activity":     1.0,
			"military_presence": 0.9,
		},
		Economy: map[string]interface{}{
			"market_volatility": 1.0,
			"corpo_dominance":   0.95,
			"black_market":      0.9,
			"crypto_economy":    0.8,
			"war_economy":       0.7,
		},
		EventTable: []EraEventTemplate{
			{
				RollRange:   "01-10",
				Name:        "Corporate Takeover",
				Description: "Крупная корпорация захватывает контроль над районом",
				Type:        "hostile_takeover",
				Scale:       "district",
				Frequency:   "rare",
				Effects: map[string]interface{}{
					"territory_control":       true,
					"population_displacement": 0.3,
					"resistance_movements":    0.6,
				},
			},
			{
				RollRange:   "11-25",
				Name:        "Phantom Liberty Incident",
				Description: "Событие связанное с Phantom Liberty и президентскими заговорами",
				Type:        "political_intrigue",
				Scale:       "city",
				Frequency:   "legendary",
				Effects: map[string]interface{}{
					"political_instability":   0.8,
					"international_attention": true,
					"mercenary_contracts":     0.9,
				},
			},
			{
				RollRange:   "26-40",
				Name:        "Dogtown Escalation",
				Description: "Эскалация конфликта в Dogtown",
				Type:        "gang_warfare",
				Scale:       "district",
				Frequency:   "uncommon",
				Effects: map[string]interface{}{
					"dogtown_instability":        0.7,
					"nomad_migration":            0.4,
					"black_market_opportunities": 0.8,
				},
			},
			// Add more events...
		},
	}
}

func (s *service) CreateEvent(ctx context.Context, name, description, eventType, scale, frequency string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error) {
	event := &WorldEvent{
		ID:          uuid.New(),
		Name:        name,
		Description: description,
		Type:        eventType,
		Scale:       scale,
		Frequency:   frequency,
		Status:      "planned",
		StartTime:   startTime,
		EndTime:     endTime,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	// Marshal effects, triggers, constraints to JSON
	if effects != nil {
		effectsJSON, err := json.Marshal(effects)
		if err != nil {
			return nil, err
		}
		event.Effects = effectsJSON
	}

	if triggers != nil {
		triggersJSON, err := json.Marshal(triggers)
		if err != nil {
			return nil, err
		}
		event.Triggers = triggersJSON
	}

	if constraints != nil {
		constraintsJSON, err := json.Marshal(constraints)
		if err != nil {
			return nil, err
		}
		event.Constraints = constraintsJSON
	}

	err := s.repo.CreateEvent(ctx, event)
	if err != nil {
		return nil, err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.created", event)

	// Invalidate cache
	s.invalidateCache(ctx, "world:events:list")

	return event, nil
}

func (s *service) GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("world:event:%s", id.String())
	cached, err := s.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var event WorldEvent
		if err := json.Unmarshal([]byte(cached), &event); err == nil {
			return &event, nil
		}
	}

	// Get from DB
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return nil, err
	}

	if event == nil {
		return nil, fmt.Errorf("event not found")
	}

	// Cache for 5 minutes
	eventJSON, _ := json.Marshal(event)
	s.redis.Set(ctx, cacheKey, eventJSON, 5*time.Minute)

	return event, nil
}

func (s *service) UpdateEvent(ctx context.Context, id uuid.UUID, name, description, eventType, scale, frequency, status *string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error) {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, fmt.Errorf("event not found")
	}

	// Update fields
	if name != nil {
		event.Name = *name
	}
	if description != nil {
		event.Description = *description
	}
	if eventType != nil {
		event.Type = *eventType
	}
	if scale != nil {
		event.Scale = *scale
	}
	if frequency != nil {
		event.Frequency = *frequency
	}
	if status != nil {
		event.Status = *status
	}
	if startTime != nil {
		event.StartTime = startTime
	}
	if endTime != nil {
		event.EndTime = endTime
	}

	// Update effects, triggers, constraints
	if effects != nil {
		effectsJSON, err := json.Marshal(effects)
		if err != nil {
			return nil, err
		}
		event.Effects = effectsJSON
	}
	if triggers != nil {
		triggersJSON, err := json.Marshal(triggers)
		if err != nil {
			return nil, err
		}
		event.Triggers = triggersJSON
	}
	if constraints != nil {
		constraintsJSON, err := json.Marshal(constraints)
		if err != nil {
			return nil, err
		}
		event.Constraints = constraintsJSON
	}

	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return nil, err
	}
