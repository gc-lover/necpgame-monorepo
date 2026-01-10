// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
// Agent: DevOps/Backend
package main

import (
	"context"
	"encoding/json"
	"flag"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

// TickEvent represents the complete tick event structure following base-event.json schema
// Issue: #2281
type TickEvent struct {
	// Base event fields (ordered for struct alignment: large → small)
	EventID      string    `json:"event_id"`
	EventType    string    `json:"event_type"`
	Timestamp    string    `json:"timestamp"` // RFC3339 format
	Version      string    `json:"version"`
	Source       string    `json:"source"`
	CorrelationID string   `json:"correlation_id,omitempty"`
	SessionID    string    `json:"session_id,omitempty"`
	PlayerID     string    `json:"player_id,omitempty"`
	GameID       string    `json:"game_id,omitempty"`
	Tags         []string  `json:"tags,omitempty"`
	TraceID      string    `json:"trace_id,omitempty"`
	
	// Tick-specific data
	Data         TickData  `json:"data"`
	Metadata     EventMetadata `json:"metadata,omitempty"`
}

// TickData represents tick-specific data payload
// Issue: #2281
type TickData struct {
	// Hourly tick fields
	TickID        string    `json:"tick_id"`
	TickType      string    `json:"tick_type"`
	GameHour      *int      `json:"game_hour,omitempty"` // 0-23
	GameDay       *int      `json:"game_day,omitempty"`  // Day number
	GameTime      string    `json:"game_time"`           // RFC3339
	TickTimestamp string    `json:"tick_timestamp"`      // RFC3339
	TriggeredBy   string    `json:"triggered_by"`
	Consumers     []string  `json:"consumers,omitempty"`
	TickMetadata  *TickMetadata `json:"metadata,omitempty"`
}

// TickMetadata contains additional simulation metadata
// Issue: #2281
type TickMetadata struct {
	SimulationDay    *int    `json:"simulation_day,omitempty"`
	SimulationWeek   *int    `json:"simulation_week,omitempty"`
	SimulationMonth  *int    `json:"simulation_month,omitempty"`
	SimulationYear   *int    `json:"simulation_year,omitempty"`
	Season           string  `json:"season,omitempty"`
}

// EventMetadata follows base-event.json schema
// Issue: #2281
type EventMetadata struct {
	Priority    string `json:"priority,omitempty"`
	TTL         string `json:"ttl,omitempty"`
	RetryCount  int    `json:"retry_count,omitempty"`
	Compression string `json:"compression,omitempty"`
	SizeBytes   int    `json:"size_bytes,omitempty"`
}

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to create logger: %v", err)
	}
	defer logger.Sync()

	// Parse command line flags
	tickType := flag.String("type", "hourly", "Type of tick to send (hourly|daily)")
	flag.Parse()

	// Validate tick type
	if *tickType != "hourly" && *tickType != "daily" {
		logger.Fatal("Invalid tick type", zap.String("type", *tickType), zap.String("valid_options", "hourly, daily"))
	}

	// Get environment variables
	kafkaBrokers := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if kafkaBrokers == "" {
		kafkaBrokers = "localhost:9092"
		logger.Warn("KAFKA_BOOTSTRAP_SERVERS not set, using default", zap.String("default", kafkaBrokers))
	}

	// SASL authentication if configured
	if saslUser := os.Getenv("KAFKA_SASL_USERNAME"); saslUser != "" {
		logger.Info("SASL authentication enabled")
	}

	// TLS if configured
	if protocol := os.Getenv("KAFKA_SECURITY_PROTOCOL"); protocol == "SASL_SSL" || protocol == "SSL" {
		logger.Info("TLS enabled for Kafka connection")
	}
	if err != nil {
		logger.Fatal("Failed to create Kafka producer", zap.Error(err), zap.String("brokers", kafkaBrokers))
	}
	defer func() {
		if err := writer.Close(); err != nil {
			logger.Error("Failed to close Kafka writer", zap.Error(err))
		}
	}()

	// Create tick event with proper schema structure
	now := time.Now().UTC()
	tickID := uuid.New().String()
	eventID := uuid.New().String()

	var topic string
	var tickData TickData
	var consumers []string

	switch *tickType {
	case "hourly":
		topic = "world.tick.hourly"
		gameHour := now.Hour()
		tickData = TickData{
			TickID:        tickID,
			TickType:      "hourly",
			GameHour:      &gameHour,
			GameTime:      now.Format(time.RFC3339),
			TickTimestamp: now.Format(time.RFC3339),
			TriggeredBy:   "scheduler",
			Consumers:     []string{"economy-service", "analytics-service"},
		}
		consumers = []string{"economy-service", "analytics-service"}
		logger.Info("Sending hourly tick", 
			zap.Int("game_hour", gameHour),
			zap.String("tick_id", tickID),
			zap.Strings("consumers", consumers))

	case "daily":
		topic = "world.tick.daily"
		// Calculate game day since simulation start (using Unix epoch as reference)
		simulationStart := time.Date(2020, 1, 1, 0, 0, 0, 0, time.UTC)
		daysSinceStart := int(now.Sub(simulationStart).Hours() / 24)
		gameDay := daysSinceStart
		
		simulationWeek := gameDay / 7
		simulationMonth := gameDay / 30
		simulationYear := 2020 + (gameDay / 365)
		
		tickData = TickData{
			TickID:        tickID,
			TickType:      "daily",
			GameDay:       &gameDay,
			GameTime:      now.Format(time.RFC3339),
			TickTimestamp: now.Format(time.RFC3339),
			TriggeredBy:   "scheduler",
			Consumers:     []string{"world-simulation-python", "social-service", "analytics-service"},
			TickMetadata: &TickMetadata{
				SimulationDay:   &gameDay,
				SimulationWeek:  &simulationWeek,
				SimulationMonth: &simulationMonth,
				SimulationYear:  &simulationYear,
			},
		}
		consumers = []string{"world-simulation-python", "social-service", "analytics-service"}
		logger.Info("Sending daily tick",
			zap.Int("game_day", gameDay),
			zap.Int("simulation_week", simulationWeek),
			zap.Int("simulation_year", simulationYear),
			zap.String("tick_id", tickID),
			zap.Strings("consumers", consumers))
	}

	// Create complete tick event following base-event.json schema
	tickEvent := TickEvent{
		EventID:   eventID,
		EventType: fmt.Sprintf("world.tick.%s", *tickType),
		Timestamp: now.Format(time.RFC3339),
		Version:   "1.0.0",
		Source:    "simulation.ticker",
		Data:      tickData,
		Metadata: EventMetadata{
			Priority:    "normal",
			TTL:         "30d",
			RetryCount:  0,
			Compression: "lz4",
		},
		Tags: []string{"simulation", "tick", *tickType},
	}

	// Serialize message
	messageBytes, err := json.Marshal(tickEvent)
	if err != nil {
		logger.Fatal("Failed to marshal tick event", zap.Error(err))
	}

	// Calculate message size for metadata
	tickEvent.Metadata.SizeBytes = len(messageBytes)

	// Update metadata in serialized message
	messageBytes, err = json.Marshal(tickEvent)
	if err != nil {
		logger.Fatal("Failed to remarshal tick event with size", zap.Error(err))
	}

	// Create Kafka message with proper headers
	msg := &sarama.ProducerMessage{
		Topic: topic,
		Key:   sarama.StringEncoder(tickID),
		Value: sarama.ByteEncoder(messageBytes),
		Headers: []sarama.RecordHeader{
			{Key: []byte("event_type"), Value: []byte(tickEvent.EventType)},
			{Key: []byte("source"), Value: []byte(tickEvent.Source)},
			{Key: []byte("version"), Value: []byte(tickEvent.Version)},
			{Key: []byte("tick_type"), Value: []byte(*tickType)},
		},
		Timestamp: now,
	}

	// Send message with context timeout
	done := make(chan error, 1)
	go func() {
		partition, offset, err := producer.SendMessage(msg)
		if err != nil {
			done <- err
			return
		}
		logger.Info("Successfully sent simulation tick",
			zap.String("topic", topic),
			zap.String("event_id", eventID),
			zap.String("tick_id", tickID),
			zap.Int32("partition", partition),
			zap.Int64("offset", offset),
			zap.String("type", *tickType),
			zap.Int("message_size_bytes", len(messageBytes)),
			zap.Strings("expected_consumers", consumers))
		done <- nil
	}()

	// Wait for send with timeout
	select {
	case err := <-done:
		if err != nil {
			logger.Fatal("Failed to send tick message", zap.Error(err))
		}
	case <-ctx.Done():
		logger.Fatal("Timeout sending tick message", zap.Error(ctx.Err()))
	}

	logger.Info("Tick event sent successfully",
		zap.String("topic", topic),
		zap.String("event_id", eventID),
		zap.String("tick_id", tickID))
}