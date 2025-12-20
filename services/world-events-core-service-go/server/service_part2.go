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
