// Issue: #2217
package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// DomainEvent represents a domain event in the event store
type DomainEvent struct {
	EventID         uuid.UUID              `json:"event_id" db:"event_id"`
	AggregateID     uuid.UUID              `json:"aggregate_id" db:"aggregate_id"`
	AggregateType   string                 `json:"aggregate_type" db:"aggregate_type"`
	AggregateVersion int64                  `json:"aggregate_version" db:"aggregate_version"`
	EventType       string                 `json:"event_type" db:"event_type"`
	EventVersion    int                    `json:"event_version" db:"event_version"`
	OccurredAt      time.Time              `json:"occurred_at" db:"occurred_at"`
	Payload         map[string]interface{} `json:"payload" db:"payload"`
	Metadata        map[string]interface{} `json:"metadata" db:"metadata"`
	CausationID     *uuid.UUID             `json:"causation_id" db:"causation_id"`
	CorrelationID   *uuid.UUID             `json:"correlation_id" db:"correlation_id"`
	ProcessedAt     *time.Time             `json:"processed_at" db:"processed_at"`
	ProcessingStatus string                 `json:"processing_status" db:"processing_status"`
	ProcessingError string                 `json:"processing_error" db:"processing_error"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// AggregateSnapshot represents a snapshot of an aggregate
type AggregateSnapshot struct {
	AggregateID   uuid.UUID              `json:"aggregate_id" db:"aggregate_id"`
	AggregateType string                 `json:"aggregate_type" db:"aggregate_type"`
	Version       int64                  `json:"version" db:"version"`
	State         map[string]interface{} `json:"state" db:"state"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
}

// ReadModel represents a CQRS read model
type ReadModel struct {
	ID        string                 `json:"id"`
	ModelName string                 `json:"model_name"`
	Data      map[string]interface{} `json:"data"`
	Version   int64                  `json:"version"`
	UpdatedAt time.Time              `json:"updated_at"`
}

// EventSourcingRepository handles event store operations
type EventSourcingRepository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewConnection creates a new database connection
func NewConnection(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(100) // Higher for event sourcing
	db.SetMaxIdleConns(20)
	db.SetConnMaxLifetime(time.Hour)

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(redisURL string) (*redis.Client, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx := context.Background()
	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// NewKafkaProducer creates a new Kafka producer
func NewKafkaProducer(brokers []string) (*kafka.Producer, error) {
	config := &kafka.ConfigMap{
		"bootstrap.servers": brokers[0], // Use first broker for simplicity
		"acks":              "all",
		"retries":           5,
		"max.in.flight.requests.per.connection": 1,
	}

	producer, err := kafka.NewProducer(config)
	if err != nil {
		return nil, fmt.Errorf("failed to create Kafka producer: %w", err)
	}

	return producer, nil
}

// NewKafkaConsumer creates a new Kafka consumer
func NewKafkaConsumer(brokers []string, groupID string) (*kafka.Consumer, error) {
	config := &kafka.ConfigMap{
		"bootstrap.servers":        brokers[0], // Use first broker for simplicity
		"group.id":                 groupID,
		"auto.offset.reset":        "earliest",
		"enable.auto.commit":       false,
		"isolation.level":          "read_committed",
		"max.poll.interval.ms":     300000,
		"session.timeout.ms":       30000,
		"heartbeat.interval.ms":    3000,
	}

	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		return nil, fmt.Errorf("failed to create Kafka consumer: %w", err)
	}

	return consumer, nil
}

// NewEventSourcingRepository creates a new event sourcing repository
func NewEventSourcingRepository(db *sql.DB, redis *redis.Client, producer *kafka.Producer, consumer *kafka.Consumer, logger *zap.SugaredLogger) *EventSourcingRepository {
	return &EventSourcingRepository{
		db:             db,
		redis:          redis,
		kafkaProducer:  producer,
		kafkaConsumer:  consumer,
		logger:         logger,
	}
}

// AppendEvent appends a new event to the event store
func (r *EventSourcingRepository) AppendEvent(ctx context.Context, event *DomainEvent) error {
	payloadJSON, _ := json.Marshal(event.Payload)
	metadataJSON, _ := json.Marshal(event.Metadata)

	query := `
		INSERT INTO event_store.events (
			event_id, aggregate_id, aggregate_type, aggregate_version, event_type,
			event_version, occurred_at, payload, metadata, causation_id, correlation_id
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.ExecContext(ctx, query,
		event.EventID, event.AggregateID, event.AggregateType, event.AggregateVersion,
		event.EventType, event.EventVersion, event.OccurredAt, payloadJSON, metadataJSON,
		event.CausationID, event.CorrelationID)

	if err != nil {
		r.logger.Errorf("Failed to append event: %v", err)
		return fmt.Errorf("failed to append event: %w", err)
	}

	// Publish to Kafka for async processing
	eventJSON, _ := json.Marshal(event)
	topic := fmt.Sprintf("events.%s", event.AggregateType)

	kafkaMsg := &kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Key:            []byte(event.AggregateID.String()),
		Value:          eventJSON,
		Headers: []kafka.Header{
			{Key: "event_type", Value: []byte(event.EventType)},
			{Key: "aggregate_type", Value: []byte(event.AggregateType)},
		},
	}

	deliveryChan := make(chan kafka.Event)
	defer close(deliveryChan)

	if err := r.kafkaProducer.Produce(kafkaMsg, deliveryChan); err != nil {
		r.logger.Errorf("Failed to produce Kafka message: %v", err)
		return fmt.Errorf("failed to produce Kafka message: %w", err)
	}

	// Wait for delivery confirmation
	select {
	case e := <-deliveryChan:
		if ev, ok := e.(*kafka.Message); ok {
			if ev.TopicPartition.Error != nil {
				r.logger.Errorf("Kafka delivery failed: %v", ev.TopicPartition.Error)
				return fmt.Errorf("kafka delivery failed: %w", ev.TopicPartition.Error)
			}
		}
	case <-time.After(5 * time.Second):
		r.logger.Error("Kafka delivery timeout")
		return fmt.Errorf("kafka delivery timeout")
	}

	r.logger.Infof("Appended event: %s for aggregate %s", event.EventType, event.AggregateID.String())
	return nil
}

// GetEventStream retrieves all events for an aggregate
func (r *EventSourcingRepository) GetEventStream(ctx context.Context, aggregateID uuid.UUID, fromVersion int64) ([]*DomainEvent, error) {
	query := `
		SELECT event_id, aggregate_id, aggregate_type, aggregate_version, event_type,
			   event_version, occurred_at, payload, metadata, causation_id, correlation_id,
			   processed_at, processing_status, processing_error, created_at, updated_at
		FROM event_store.events
		WHERE aggregate_id = $1 AND aggregate_version >= $2
		ORDER BY aggregate_version ASC
	`

	rows, err := r.db.QueryContext(ctx, query, aggregateID, fromVersion)
	if err != nil {
		r.logger.Errorf("Failed to get event stream: %v", err)
		return nil, fmt.Errorf("failed to get event stream: %w", err)
	}
	defer rows.Close()

	var events []*DomainEvent
	for rows.Next() {
		var e DomainEvent
		var payloadJSON, metadataJSON []byte

		err := rows.Scan(
			&e.EventID, &e.AggregateID, &e.AggregateType, &e.AggregateVersion, &e.EventType,
			&e.EventVersion, &e.OccurredAt, &payloadJSON, &metadataJSON, &e.CausationID,
			&e.CorrelationID, &e.ProcessedAt, &e.ProcessingStatus, &e.ProcessingError,
			&e.CreatedAt, &e.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan event: %v", err)
			continue
		}

		json.Unmarshal(payloadJSON, &e.Payload)
		json.Unmarshal(metadataJSON, &e.Metadata)

		events = append(events, &e)
	}

	return events, nil
}

// GetAggregateSnapshot retrieves the latest snapshot for an aggregate
func (r *EventSourcingRepository) GetAggregateSnapshot(ctx context.Context, aggregateID uuid.UUID) (*AggregateSnapshot, error) {
	cacheKey := fmt.Sprintf("snapshot:%s", aggregateID.String())
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var snapshot AggregateSnapshot
		if err := json.Unmarshal([]byte(cached), &snapshot); err == nil {
			return &snapshot, nil
		}
	}

	query := `
		SELECT aggregate_id, aggregate_type, version, state, created_at
		FROM event_store.aggregate_snapshots
		WHERE aggregate_id = $1
		ORDER BY version DESC
		LIMIT 1
	`

	var s AggregateSnapshot
	var stateJSON []byte

	err = r.db.QueryRowContext(ctx, query, aggregateID).Scan(
		&s.AggregateID, &s.AggregateType, &s.Version, &stateJSON, &s.CreatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("snapshot not found")
		}
		r.logger.Errorf("Failed to get snapshot: %v", err)
		return nil, fmt.Errorf("failed to get snapshot: %w", err)
	}

	json.Unmarshal(stateJSON, &s.State)

	// Cache result
	snapshotJSON, _ := json.Marshal(s)
	r.redis.Set(ctx, cacheKey, snapshotJSON, 30*time.Minute)

	return &s, nil
}

// CreateAggregateSnapshot creates a new snapshot for an aggregate
func (r *EventSourcingRepository) CreateAggregateSnapshot(ctx context.Context, snapshot *AggregateSnapshot) error {
	stateJSON, _ := json.Marshal(snapshot.State)

	query := `
		INSERT INTO event_store.aggregate_snapshots (
			aggregate_id, aggregate_type, version, state
		) VALUES ($1, $2, $3, $4)
		ON CONFLICT (aggregate_id) DO UPDATE SET
			version = EXCLUDED.version,
			state = EXCLUDED.state,
			created_at = CURRENT_TIMESTAMP
	`

	_, err := r.db.ExecContext(ctx, query,
		snapshot.AggregateID, snapshot.AggregateType, snapshot.Version, stateJSON)

	if err != nil {
		r.logger.Errorf("Failed to create snapshot: %v", err)
		return fmt.Errorf("failed to create snapshot: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("snapshot:%s", snapshot.AggregateID.String())
	r.redis.Del(ctx, cacheKey)

	r.logger.Infof("Created snapshot for aggregate %s at version %d", snapshot.AggregateID.String(), snapshot.Version)
	return nil
}

// GetReadModel retrieves a read model by ID
func (r *EventSourcingRepository) GetReadModel(ctx context.Context, modelName, id string) (*ReadModel, error) {
	cacheKey := fmt.Sprintf("readmodel:%s:%s", modelName, id)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var model ReadModel
		if err := json.Unmarshal([]byte(cached), &model); err == nil {
			return &model, nil
		}
	}

	query := `
		SELECT id, model_name, data, version, updated_at
		FROM event_store.read_models
		WHERE model_name = $1 AND id = $2
	`

	var m ReadModel
	var dataJSON []byte

	err = r.db.QueryRowContext(ctx, query, modelName, id).Scan(
		&m.ID, &m.ModelName, &dataJSON, &m.Version, &m.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("read model not found")
		}
		r.logger.Errorf("Failed to get read model: %v", err)
		return nil, fmt.Errorf("failed to get read model: %w", err)
	}

	json.Unmarshal(dataJSON, &m.Data)

	// Cache result
	modelJSON, _ := json.Marshal(m)
	r.redis.Set(ctx, cacheKey, modelJSON, 15*time.Minute)

	return &m, nil
}

// UpdateReadModel updates or creates a read model
func (r *EventSourcingRepository) UpdateReadModel(ctx context.Context, model *ReadModel) error {
	dataJSON, _ := json.Marshal(model.Data)

	query := `
		INSERT INTO event_store.read_models (
			id, model_name, data, version, updated_at
		) VALUES ($1, $2, $3, $4, CURRENT_TIMESTAMP)
		ON CONFLICT (model_name, id) DO UPDATE SET
			data = EXCLUDED.data,
			version = EXCLUDED.version,
			updated_at = CURRENT_TIMESTAMP
	`

	_, err := r.db.ExecContext(ctx, query,
		model.ID, model.ModelName, dataJSON, model.Version)

	if err != nil {
		r.logger.Errorf("Failed to update read model: %v", err)
		return fmt.Errorf("failed to update read model: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("readmodel:%s:%s", model.ModelName, model.ID)
	r.redis.Del(ctx, cacheKey)

	return nil
}

// GetEventsByType retrieves events by type with pagination
func (r *EventSourcingRepository) GetEventsByType(ctx context.Context, eventType string, limit, offset int) ([]*DomainEvent, error) {
	query := `
		SELECT event_id, aggregate_id, aggregate_type, aggregate_version, event_type,
			   event_version, occurred_at, payload, metadata, causation_id, correlation_id,
			   processed_at, processing_status, processing_error, created_at, updated_at
		FROM event_store.events
		WHERE event_type = $1
		ORDER BY occurred_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, eventType, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get events by type: %v", err)
		return nil, fmt.Errorf("failed to get events by type: %w", err)
	}
	defer rows.Close()

	var events []*DomainEvent
	for rows.Next() {
		var e DomainEvent
		var payloadJSON, metadataJSON []byte

		err := rows.Scan(
			&e.EventID, &e.AggregateID, &e.AggregateType, &e.AggregateVersion, &e.EventType,
			&e.EventVersion, &e.OccurredAt, &payloadJSON, &metadataJSON, &e.CausationID,
			&e.CorrelationID, &e.ProcessedAt, &e.ProcessingStatus, &e.ProcessingError,
			&e.CreatedAt, &e.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan event: %v", err)
			continue
		}

		json.Unmarshal(payloadJSON, &e.Payload)
		json.Unmarshal(metadataJSON, &e.Metadata)

		events = append(events, &e)
	}

	return events, nil
}
