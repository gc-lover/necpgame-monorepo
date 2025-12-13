// Issue: #44
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
	// Events CRUD
	CreateEvent(ctx context.Context, name, description, eventType, scale, frequency string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	UpdateEvent(ctx context.Context, id uuid.UUID, name, description, eventType, scale, frequency, status *string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	DeleteEvent(ctx context.Context, id uuid.UUID) error
	ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error)

	// Event state
	GetActiveEvents(ctx context.Context) ([]*WorldEvent, error)
	GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error)

	// Event actions
	ActivateEvent(ctx context.Context, id uuid.UUID, activatedBy string) error
	DeactivateEvent(ctx context.Context, id uuid.UUID) error
	AnnounceEvent(ctx context.Context, id uuid.UUID, announcedBy, message string, channels []string) error

	// Era-based event generation
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
func NewService(repo Repository, redis *redis.Client, kafkaWriter *kafka.Writer, logger *zap.Logger) Service {
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

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.updated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:list")

	return event, nil
}

func (s *service) DeleteEvent(ctx context.Context, id uuid.UUID) error {
	err := s.repo.DeleteEvent(ctx, id)
	if err != nil {
		return err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.deleted", map[string]interface{}{"event_id": id.String()})

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:list")

	return nil
}

func (s *service) ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error) {
	return s.repo.ListEvents(ctx, filter)
}

func (s *service) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	// Try cache first
	cacheKey := "world:events:active"
	cached, err := s.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var events []*WorldEvent
		if err := json.Unmarshal([]byte(cached), &events); err == nil {
			return events, nil
		}
	}

	// Get from DB
	events, err := s.repo.GetActiveEvents(ctx)
	if err != nil {
		return nil, err
	}

	// Cache for 1 minute
	eventsJSON, _ := json.Marshal(events)
	s.redis.Set(ctx, cacheKey, eventsJSON, 1*time.Minute)

	return events, nil
}

func (s *service) GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error) {
	return s.repo.GetPlannedEvents(ctx)
}

func (s *service) ActivateEvent(ctx context.Context, id uuid.UUID, activatedBy string) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	if event.Status != "planned" {
		return fmt.Errorf("event is not in planned state")
	}

	// Update status
	event.Status = "active"
	now := time.Now()
	event.StartTime = &now
	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return err
	}

	// Record activation
	activation := &EventActivation{
		EventID:     id,
		ActivatedAt: now,
		ActivatedBy: activatedBy,
		Reason:      "manual activation",
	}
	err = s.repo.RecordActivation(ctx, activation)
	if err != nil {
		s.logger.Error("Failed to record activation", zap.Error(err))
		// Don't fail the activation if recording fails
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.activated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:active")

	return nil
}

func (s *service) DeactivateEvent(ctx context.Context, id uuid.UUID) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	if event.Status != "active" {
		return fmt.Errorf("event is not active")
	}

	// Update status
	event.Status = "completed"
	now := time.Now()
	event.EndTime = &now
	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.deactivated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:active")

	return nil
}

func (s *service) AnnounceEvent(ctx context.Context, id uuid.UUID, announcedBy, message string, channels []string) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	// Record announcement
	announcement := &EventAnnouncement{
		EventID:     id,
		AnnouncedAt: time.Now(),
		AnnouncedBy: announcedBy,
		Message:     message,
		Channels:    channels,
	}
	err = s.repo.RecordAnnouncement(ctx, announcement)
	if err != nil {
		s.logger.Error("Failed to record announcement", zap.Error(err))
		// Don't fail the announcement if recording fails
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.announced", map[string]interface{}{
		"event_id":     id.String(),
		"message":      message,
		"channels":     channels,
		"announced_by": announcedBy,
	})

	return nil
}

// Helper methods

func (s *service) publishKafkaEvent(ctx context.Context, eventType string, data interface{}) {
	payload, err := json.Marshal(map[string]interface{}{
		"event_type": eventType,
		"timestamp":  time.Now().Unix(),
		"data":       data,
	})
	if err != nil {
		s.logger.Error("Failed to marshal Kafka event", zap.Error(err))
		return
	}

	err = s.kafkaWriter.WriteMessages(ctx, kafka.Message{
		Key:   []byte(eventType),
		Value: payload,
	})
	if err != nil {
		s.logger.Error("Failed to publish Kafka event", zap.Error(err))
	}
}

func (s *service) invalidateCache(ctx context.Context, key string) {
	err := s.redis.Del(ctx, key).Err()
	if err != nil {
		s.logger.Error("Failed to invalidate cache", zap.String("key", key), zap.Error(err))
	}
}

// Era-based methods

func (s *service) GetEraConfig(ctx context.Context, eraID string) (*EraConfig, error) {
	config, exists := s.eraConfigs[eraID]
	if !exists {
		return nil, fmt.Errorf("era config not found: %s", eraID)
	}
	return config, nil
}

func (s *service) ListEraConfigs(ctx context.Context) ([]*EraConfig, error) {
	configs := make([]*EraConfig, 0, len(s.eraConfigs))
	for _, config := range s.eraConfigs {
		configs = append(configs, config)
	}
	return configs, nil
}

func (s *service) GenerateEventFromEra(ctx context.Context, eraID string, roll int) (*WorldEvent, error) {
	config, exists := s.eraConfigs[eraID]
	if !exists {
		return nil, fmt.Errorf("era config not found: %s", eraID)
	}

	// Find event template by roll
	var template *EraEventTemplate
	for _, tmpl := range config.EventTable {
		if s.isRollInRange(roll, tmpl.RollRange) {
			template = &tmpl
			break
		}
	}

	if template == nil {
		return nil, fmt.Errorf("no event template found for roll %d in era %s", roll, eraID)
	}

	// Create event from template
	event := &WorldEvent{
		ID:          uuid.New(),
		Name:        template.Name,
		Description: template.Description,
		Type:        template.Type,
		Scale:       template.Scale,
		Frequency:   template.Frequency,
		Status:      "planned",
		StartTime:   nil, // Will be set when activated
		EndTime:     nil,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	// Marshal effects, triggers, constraints
	if effectsJSON, err := json.Marshal(template.Effects); err == nil {
		event.Effects = effectsJSON
	}

	if triggersJSON, err := json.Marshal(template.Triggers); err == nil {
		event.Triggers = triggersJSON
	}

	if constraintsJSON, err := json.Marshal(template.Constraints); err == nil {
		event.Constraints = constraintsJSON
	}

	// Add era metadata
	metadata := map[string]interface{}{
		"era_id":        eraID,
		"era_name":      config.Name,
		"roll":          roll,
		"dc_difficulty": config.DCDifficulty,
		"faction_ai":    config.FactionAI,
		"economy":       config.Economy,
	}

	if metadataJSON, err := json.Marshal(metadata); err == nil {
		event.Metadata = metadataJSON
	}

	// Save to database
	if err := s.repo.CreateEvent(ctx, event); err != nil {
		return nil, fmt.Errorf("failed to save generated event: %w", err)
	}

	s.logger.Info("Generated event from era",
		zap.String("era_id", eraID),
		zap.Int("roll", roll),
		zap.String("event_name", event.Name),
		zap.String("event_id", event.ID.String()))

	return event, nil
}

// isRollInRange checks if roll value falls within the template's range
func (s *service) isRollInRange(roll int, rollRange string) bool {
	// Parse range like "01-10", "11-20", etc.
	var min, max int
	if _, err := fmt.Sscanf(rollRange, "%d-%d", &min, &max); err != nil {
		return false
	}
	return roll >= min && roll <= max
}
