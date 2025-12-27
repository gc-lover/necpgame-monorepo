// Issue: #2217
package service

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/repository"
	"event-sourcing-aggregates-go/internal/metrics"
)

// EventSourcingService handles event sourcing business logic
type EventSourcingService struct {
	repo         RepositoryInterface
	metrics      *metrics.Collector
	logger       *zap.SugaredLogger
	eventProcessors map[string]EventProcessor
	processingWG   sync.WaitGroup
}

// EventProcessor defines the interface for event processors
type EventProcessor interface {
	ProcessEvent(ctx context.Context, event *repository.DomainEvent) error
	GetAggregateType() string
}

// RepositoryInterface defines the repository interface
type RepositoryInterface interface {
	AppendEvent(ctx context.Context, event *repository.DomainEvent) error
	GetEvents(ctx context.Context, aggregateID uuid.UUID, fromVersion int) ([]*repository.DomainEvent, error)
	GetSnapshot(ctx context.Context, aggregateID uuid.UUID) (*repository.AggregateSnapshot, error)
	SaveSnapshot(ctx context.Context, snapshot *repository.AggregateSnapshot) error
	HealthCheck(ctx context.Context) error
}

// NewEventSourcingService creates a new event sourcing service
func NewEventSourcingService(repo RepositoryInterface, metrics *metrics.Collector, logger *zap.SugaredLogger) *EventSourcingService {
	return &EventSourcingService{
		repo:            repo,
		metrics:         metrics,
		logger:          logger,
		eventProcessors: make(map[string]EventProcessor),
	}
}

// RegisterEventProcessor registers an event processor for an aggregate type
func (s *EventSourcingService) RegisterEventProcessor(processor EventProcessor) {
	s.eventProcessors[processor.GetAggregateType()] = processor
	s.logger.Infof("Registered event processor for aggregate type: %s", processor.GetAggregateType())
}

// AppendEvent appends a new event to the event store
func (s *EventSourcingService) AppendEvent(ctx context.Context, aggregateID uuid.UUID, aggregateType string, eventType string, payload map[string]interface{}, metadata map[string]interface{}, causationID, correlationID *uuid.UUID) (*repository.DomainEvent, error) {

	// Get current aggregate version
	currentVersion, err := s.getCurrentAggregateVersion(ctx, aggregateID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get current aggregate version: %w", err)
	}

	eventID := uuid.New()
	event := &repository.DomainEvent{
		EventID:          eventID,
		AggregateID:      aggregateID,
		AggregateType:    aggregateType,
		AggregateVersion: currentVersion + 1,
		EventType:        eventType,
		EventVersion:     1,
		OccurredAt:       time.Now(),
		Payload:          payload,
		Metadata:         metadata,
		CausationID:      causationID,
		CorrelationID:    correlationID,
		ProcessingStatus: "pending",
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}

	if err := s.repo.AppendEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to append event: %w", err)
	}

	s.metrics.IncrementEventsAppended()
	s.logger.Infof("Appended event: %s for aggregate %s", eventType, aggregateID.String())

	return event, nil
}

// GetEventStream retrieves all events for an aggregate
func (s *EventSourcingService) GetEventStream(ctx context.Context, aggregateID uuid.UUID, fromVersion int64) ([]*repository.DomainEvent, error) {
	events, err := s.repo.GetEventStream(ctx, aggregateID, fromVersion)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get event stream: %w", err)
	}

	return events, nil
}

// RebuildAggregate rebuilds an aggregate from its event stream
func (s *EventSourcingService) RebuildAggregate(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (map[string]interface{}, error) {
	// Get all events for the aggregate
	events, err := s.repo.GetEventStream(ctx, aggregateID, 0)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get event stream for rebuild: %w", err)
	}

	// Check if we have an event processor for this aggregate type
	processor, exists := s.eventProcessors[aggregateType]
	if !exists {
		return nil, fmt.Errorf("no event processor registered for aggregate type: %s", aggregateType)
	}

	// Rebuild aggregate state from events
	aggregateState := make(map[string]interface{})

	for _, event := range events {
		if err := processor.ProcessEvent(ctx, event); err != nil {
			s.logger.Errorf("Failed to process event %s during rebuild: %v", event.EventID.String(), err)
			continue
		}

		// Update aggregate state (simplified - in practice, each processor would maintain its own state)
		if event.Payload != nil {
			for k, v := range event.Payload {
				aggregateState[k] = v
			}
		}
	}

	s.logger.Infof("Rebuilt aggregate %s with %d events", aggregateID.String(), len(events))
	return aggregateState, nil
}

// GetAggregateSnapshot retrieves the latest snapshot for an aggregate
func (s *EventSourcingService) GetAggregateSnapshot(ctx context.Context, aggregateID uuid.UUID) (*repository.AggregateSnapshot, error) {
	snapshot, err := s.repo.GetAggregateSnapshot(ctx, aggregateID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get aggregate snapshot: %w", err)
	}

	return snapshot, nil
}

// CreateAggregateSnapshot creates a new snapshot for an aggregate
func (s *EventSourcingService) CreateAggregateSnapshot(ctx context.Context, aggregateID uuid.UUID, aggregateType string, version int64, state map[string]interface{}) error {
	snapshot := &repository.AggregateSnapshot{
		AggregateID:   aggregateID,
		AggregateType: aggregateType,
		Version:       version,
		State:         state,
		CreatedAt:     time.Now(),
	}

	if err := s.repo.CreateAggregateSnapshot(ctx, snapshot); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to create aggregate snapshot: %w", err)
	}

	s.metrics.IncrementSnapshotsCreated()
	s.logger.Infof("Created snapshot for aggregate %s at version %d", aggregateID.String(), version)

	return nil
}

// GetReadModel retrieves a read model
func (s *EventSourcingService) GetReadModel(ctx context.Context, modelName, id string) (*repository.ReadModel, error) {
	model, err := s.repo.GetReadModel(ctx, modelName, id)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get read model: %w", err)
	}

	return model, nil
}

// UpdateReadModel updates a read model
func (s *EventSourcingService) UpdateReadModel(ctx context.Context, model *repository.ReadModel) error {
	if err := s.repo.UpdateReadModel(ctx, model); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update read model: %w", err)
	}

	s.metrics.IncrementReadModelsUpdated()
	return nil
}

// StartEventProcessing starts the background event processing
func (s *EventSourcingService) StartEventProcessing(ctx context.Context) {
	// Subscribe to all event topics
	topics := []string{"events.player", "events.quest", "events.combat", "events.tournament"}
	if err := s.repo.(*repository.EventSourcingRepository).GetKafkaConsumer().SubscribeTopics(topics, nil); err != nil {
		s.logger.Fatalf("Failed to subscribe to Kafka topics: %v", err)
	}

	// Start processing workers
	for i := 0; i < 10; i++ { // Configurable number of workers
		s.processingWG.Add(1)
		go s.eventProcessingWorker(ctx, i)
	}

	s.logger.Info("Started event processing with 10 workers")
}

// eventProcessingWorker processes events from Kafka
func (s *EventSourcingService) eventProcessingWorker(ctx context.Context, workerID int) {
	defer s.processingWG.Done()

	consumer := s.repo.(*repository.EventSourcingRepository).GetKafkaConsumer()

	for {
		select {
		case <-ctx.Done():
			s.logger.Infof("Worker %d shutting down", workerID)
			return
		default:
			msg, err := consumer.ReadMessage(100 * time.Millisecond)
			if err != nil {
				s.logger.Errorf("Worker %d: Error reading message: %v", workerID, err)
				continue
			}

			// Process the event
			var event repository.DomainEvent
			if err := json.Unmarshal(msg.Value, &event); err != nil {
				s.logger.Errorf("Worker %d: Failed to unmarshal event: %v", workerID, err)
				continue
			}

			start := time.Now()
			if err := s.processEvent(ctx, &event); err != nil {
				s.logger.Errorf("Worker %d: Failed to process event %s: %v", workerID, event.EventID.String(), err)
				s.metrics.IncrementProcessingErrors()

				// Update event processing status to failed
				s.updateEventProcessingStatus(ctx, event.EventID, "failed", err.Error())
				continue
			}

			// Update event processing status to processed
			s.updateEventProcessingStatus(ctx, event.EventID, "processed", "")

			processingTime := time.Since(start)
			s.metrics.ObserveProcessingTime(processingTime.Seconds())
			s.metrics.IncrementEventsProcessed()

			s.logger.Infof("Worker %d: Processed event %s in %v", workerID, event.EventID.String(), processingTime)

			// Commit offset
			if _, err := consumer.CommitMessage(msg); err != nil {
				s.logger.Errorf("Worker %d: Failed to commit offset: %v", workerID, err)
			}
		}
	}
}

// processEvent processes a single event
func (s *EventSourcingService) processEvent(ctx context.Context, event *repository.DomainEvent) error {
	// Get the appropriate event processor
	processor, exists := s.eventProcessors[event.AggregateType]
	if !exists {
		return fmt.Errorf("no event processor registered for aggregate type: %s", event.AggregateType)
	}

	// Process the event
	if err := processor.ProcessEvent(ctx, event); err != nil {
		return fmt.Errorf("event processor failed: %w", err)
	}

	// Update projections and read models as needed
	// This would involve calling projection updaters

	return nil
}

// updateEventProcessingStatus updates the processing status of an event
func (s *EventSourcingService) updateEventProcessingStatus(ctx context.Context, eventID uuid.UUID, status, errorMsg string) {
	query := `
		UPDATE event_store.events
		SET processing_status = $1, processing_error = $2, processed_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
		WHERE event_id = $3
	`

	_, err := s.repo.(*repository.EventSourcingRepository).GetDB().ExecContext(ctx, query, status, errorMsg, eventID)
	if err != nil {
		s.logger.Errorf("Failed to update event processing status: %v", err)
	}
}

// getCurrentAggregateVersion gets the current version of an aggregate
func (s *EventSourcingService) getCurrentAggregateVersion(ctx context.Context, aggregateID uuid.UUID) (int64, error) {
	query := `
		SELECT COALESCE(MAX(aggregate_version), 0)
		FROM event_store.events
		WHERE aggregate_id = $1
	`

	var version int64
	err := s.repo.(*repository.EventSourcingRepository).GetDB().QueryRowContext(ctx, query, aggregateID).Scan(&version)
	if err != nil {
		return 0, fmt.Errorf("failed to get current aggregate version: %w", err)
	}

	return version, nil
}

// GetEventsAnalytics returns analytics about events
func (s *EventSourcingService) GetEventsAnalytics(ctx context.Context, days int) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) as total_events,
			COUNT(DISTINCT aggregate_id) as unique_aggregates,
			COUNT(DISTINCT aggregate_type) as aggregate_types,
			AVG(EXTRACT(EPOCH FROM (processed_at - occurred_at))) as avg_processing_time
		FROM event_store.events
		WHERE occurred_at >= CURRENT_TIMESTAMP - INTERVAL '%d days'
	`

	var totalEvents, uniqueAggregates, aggregateTypes int64
	var avgProcessingTime *float64

	err := s.repo.(*repository.EventSourcingRepository).GetDB().QueryRowContext(ctx, fmt.Sprintf(query, days)).Scan(
		&totalEvents, &uniqueAggregates, &aggregateTypes, &avgProcessingTime,
	)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get events analytics: %w", err)
	}

	analytics := map[string]interface{}{
		"total_events":       totalEvents,
		"unique_aggregates":  uniqueAggregates,
		"aggregate_types":    aggregateTypes,
		"avg_processing_time": avgProcessingTime,
		"period_days":        days,
		"timestamp":          time.Now(),
	}

	return analytics, nil
}

// GetProcessingStatus returns the current processing status
func (s *EventSourcingService) GetProcessingStatus(ctx context.Context) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) FILTER (WHERE processing_status = 'pending') as pending,
			COUNT(*) FILTER (WHERE processing_status = 'processing') as processing,
			COUNT(*) FILTER (WHERE processing_status = 'processed') as processed,
			COUNT(*) FILTER (WHERE processing_status = 'failed') as failed
		FROM event_store.events
		WHERE occurred_at >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
	`

	var pending, processing, processed, failed int64

	err := s.repo.(*repository.EventSourcingRepository).GetDB().QueryRowContext(ctx, query).Scan(
		&pending, &processing, &processed, &failed,
	)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get processing status: %w", err)
	}

	status := map[string]interface{}{
		"pending":    pending,
		"processing": processing,
		"processed":  processed,
		"failed":     failed,
		"timestamp":  time.Now(),
	}

	return status, nil
}
