// Issue: #2237
// PERFORMANCE: Enterprise-grade Kafka event-driven architecture
// BACKEND: Core event processing service for NECPGAME MMOFPS RPG

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/IBM/sarama"
	"go.uber.org/zap"

	"kafka-event-driven-core/internal/config"
	"kafka-event-driven-core/internal/consumers"
	"kafka-event-driven-core/internal/events"
	"kafka-event-driven-core/internal/metrics"
	"kafka-event-driven-core/internal/producers"
)

func main() {
	// PERFORMANCE: Optimize GC for high-throughput event processing
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "200") // Higher GC threshold for event processing
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Kafka Event-Driven Core Service",
		zap.String("version", "1.0.0"),
		zap.String("gogc", os.Getenv("GOGC")))

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize metrics
	metricsCollector := metrics.NewCollector(logger)
	defer metricsCollector.Shutdown()

	// Initialize Kafka admin client for topic management
	adminClient, err := sarama.NewClusterAdmin(cfg.Kafka.Brokers, cfg.Kafka.SaramaConfig())
	if err != nil {
		logger.Fatal("Failed to create Kafka admin client", zap.Error(err))
	}
	defer adminClient.Close()

	// Ensure topics exist
	if err := ensureTopicsExist(adminClient, cfg, logger); err != nil {
		logger.Fatal("Failed to ensure topics exist", zap.Error(err))
	}

	// Initialize event registry
	eventRegistry := events.NewRegistry(logger)

	// Register event schemas
	if err := registerEventSchemas(eventRegistry); err != nil {
		logger.Fatal("Failed to register event schemas", zap.Error(err))
	}

	// Initialize event producers
	eventProducers := make(map[string]*producers.EventProducer)

	// Create producers for each domain
	domains := []string{"combat", "economy", "social", "system"}
	for _, domain := range domains {
		producer, err := producers.NewEventProducer(
			cfg.Kafka.Brokers,
			fmt.Sprintf("game.%s.events", domain),
			eventRegistry,
			logger,
			metricsCollector,
		)
		if err != nil {
			logger.Fatal("Failed to create event producer",
				zap.String("domain", domain), zap.Error(err))
		}
		eventProducers[domain] = producer
		defer producer.Close()
	}

	// Initialize consumer manager
	consumerManager := consumers.NewManager(cfg, eventRegistry, logger, metricsCollector)

	// Register domain-specific consumers
	if err := registerDomainConsumers(consumerManager, cfg, eventRegistry, logger, metricsCollector); err != nil {
		logger.Fatal("Failed to register domain consumers", zap.Error(err))
	}

	// Start consumer manager
	if err := consumerManager.Start(context.Background()); err != nil {
		logger.Fatal("Failed to start consumer manager", zap.Error(err))
	}
	defer consumerManager.Stop()

	// Start health check server
	healthServer := startHealthServer(cfg, logger, metricsCollector)
	defer healthServer.Shutdown(context.Background())

	// Start metrics server
	metricsServer := startMetricsServer(cfg, logger, metricsCollector)
	defer metricsServer.Shutdown(context.Background())

	// Graceful shutdown handling
	shutdownCh := make(chan os.Signal, 1)
	signal.Notify(shutdownCh, syscall.SIGINT, syscall.SIGTERM)

	logger.Info("Kafka Event-Driven Core Service started successfully",
		zap.Strings("brokers", cfg.Kafka.Brokers),
		zap.Int("domains", len(domains)))

	// Wait for shutdown signal
	<-shutdownCh
	logger.Info("Received shutdown signal, initiating graceful shutdown...")

	// Force GC before exit
	runtime.GC()
	logger.Info("Kafka Event-Driven Core Service shutdown complete")
}

// ensureTopicsExist creates required Kafka topics if they don't exist
func ensureTopicsExist(adminClient sarama.ClusterAdmin, cfg *config.Config, logger *zap.Logger) error {
	// Topic configurations based on proto/kafka/topics/topic-config.yaml
	topicConfigs := []struct {
		name string
		detail sarama.TopicDetail
	}{
		{
			name: "game.combat.events",
			detail: sarama.TopicDetail{
				NumPartitions:     24,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("604800000"), // 7 days
					"compression.type": stringPtr("snappy"),
				},
			},
		},
		{
			name: "game.combat.damage.validation",
			detail: sarama.TopicDetail{
				NumPartitions:     12,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("259200000"), // 3 days
					"compression.type": stringPtr("snappy"),
				},
			},
		},
		{
			name: "game.economy.events",
			detail: sarama.TopicDetail{
				NumPartitions:     12,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("2592000000"), // 30 days
					"compression.type": stringPtr("lz4"),
				},
			},
		},
		{
			name: "game.social.events",
			detail: sarama.TopicDetail{
				NumPartitions:     8,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("2592000000"), // 30 days
					"compression.type": stringPtr("gzip"),
				},
			},
		},
		{
			name: "game.system.events",
			detail: sarama.TopicDetail{
				NumPartitions:     6,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("7776000000"), // 90 days
					"compression.type": stringPtr("gzip"),
				},
			},
		},
		{
			name: "game.processing.dead.letter",
			detail: sarama.TopicDetail{
				NumPartitions:     4,
				ReplicationFactor: 3,
				ConfigEntries: map[string]*string{
					"retention.ms": stringPtr("604800000"), // 7 days
					"compression.type": stringPtr("gzip"),
				},
			},
		},
	}

	for _, topicConfig := range topicConfigs {
		if err := adminClient.CreateTopic(topicConfig.name, &topicConfig.detail, false); err != nil {
			if err != sarama.ErrTopicAlreadyExists {
				logger.Error("Failed to create topic",
					zap.String("topic", topicConfig.name), zap.Error(err))
				return fmt.Errorf("failed to create topic %s: %w", topicConfig.name, err)
			}
			logger.Info("Topic already exists", zap.String("topic", topicConfig.name))
		} else {
			logger.Info("Created topic", zap.String("topic", topicConfig.name))
		}
	}

	return nil
}

// registerEventSchemas loads and registers JSON schemas for event validation
func registerEventSchemas(registry *events.Registry) error {
	// Register base event schema
	if err := registry.RegisterSchema("base-event", "proto/kafka/schemas/core/base-event.json"); err != nil {
		return fmt.Errorf("failed to register base event schema: %w", err)
	}

	// Register domain-specific schemas
	domainSchemas := map[string][]string{
		"combat":  {"combat-session-events.json", "combat-action-events.json"},
		"economy": {"economy-trade-events.json"},
		"social":  {"social-guild-events.json"},
		"system":  {"system-events.json"},
	}

	for domain, schemas := range domainSchemas {
		for _, schema := range schemas {
			schemaPath := fmt.Sprintf("proto/kafka/schemas/%s/%s", domain, schema)
			if err := registry.RegisterSchema(fmt.Sprintf("%s-%s", domain, schema), schemaPath); err != nil {
				return fmt.Errorf("failed to register %s schema: %w", schema, err)
			}
		}
	}

	return nil
}

// registerDomainConsumers creates and registers consumers for each domain
func registerDomainConsumers(manager *consumers.Manager, cfg *config.Config, eventRegistry *events.Registry, logger *zap.Logger, metrics *metrics.Collector) error {
	// Combat domain consumers
	combatConsumer := consumers.NewCombatConsumer(cfg, eventRegistry, logger, metrics)
	if err := manager.RegisterConsumer("combat_processor", combatConsumer, []string{"game.combat.events"}); err != nil {
		return fmt.Errorf("failed to register combat consumer: %w", err)
	}

	damageValidator := consumers.NewDamageValidator(cfg, eventRegistry, logger, metrics)
	if err := manager.RegisterConsumer("damage_validator", damageValidator, []string{"game.combat.damage.validation"}); err != nil {
		return fmt.Errorf("failed to register damage validator: %w", err)
	}

	// Economy domain consumers
	economyConsumer := consumers.NewEconomyConsumer(cfg, eventRegistry, logger, metrics)
	if err := manager.RegisterConsumer("economy_processor", economyConsumer, []string{"game.economy.events"}); err != nil {
		return fmt.Errorf("failed to register economy consumer: %w", err)
	}

	// Social domain consumers
	socialConsumer := consumers.NewSocialConsumer(cfg, eventRegistry, logger, metrics)
	if err := manager.RegisterConsumer("social_processor", socialConsumer, []string{"game.social.events"}); err != nil {
		return fmt.Errorf("failed to register social consumer: %w", err)
	}

	// System domain consumers
	systemConsumer := consumers.NewSystemConsumer(cfg, eventRegistry, logger, metrics)
	if err := manager.RegisterConsumer("system_monitor", systemConsumer, []string{"game.system.events"}); err != nil {
		return fmt.Errorf("failed to register system consumer: %w", err)
	}

	return nil
}

// startHealthServer starts a simple health check HTTP server
func startHealthServer(cfg *config.Config, logger *zap.Logger, metrics *metrics.Collector) *http.Server {
	mux := http.NewServeMux()
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
	})

	server := &http.Server{
		Addr:         ":8081",
		Handler:      mux,
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 5 * time.Second,
	}

	go func() {
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Error("Health server failed", zap.Error(err))
		}
	}()

	return server
}

// startMetricsServer starts a metrics HTTP server
func startMetricsServer(cfg *config.Config, logger *zap.Logger, metrics *metrics.Collector) *http.Server {
	mux := http.NewServeMux()
	mux.Handle("/metrics", metrics.Handler())

	server := &http.Server{
		Addr:         ":9090",
		Handler:      mux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	go func() {
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Error("Metrics server failed", zap.Error(err))
		}
	}()

	return server
}

// stringPtr returns a pointer to the given string
func stringPtr(s string) *string {
	return &s
}
