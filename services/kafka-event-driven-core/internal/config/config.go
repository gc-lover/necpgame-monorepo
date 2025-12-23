// Issue: #2237
// PERFORMANCE: Optimized configuration for high-throughput Kafka processing
package config

import (
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"

	"github.com/IBM/sarama"
	"gopkg.in/yaml.v3"
)

// Config holds the complete configuration for the Kafka event-driven service
type Config struct {
	Kafka     KafkaConfig     `yaml:"kafka"`
	Consumers ConsumersConfig `yaml:"consumers"`
	Producers ProducersConfig `yaml:"producers"`
	Metrics   MetricsConfig   `yaml:"metrics"`
	Events    EventsConfig    `yaml:"events"`
}

// KafkaConfig holds Kafka-specific configuration
type KafkaConfig struct {
	Brokers         []string      `yaml:"brokers"`
	ClientID        string        `yaml:"client_id"`
	GroupID         string        `yaml:"group_id"`
	SessionTimeout  time.Duration `yaml:"session_timeout"`
	HeartbeatInterval time.Duration `yaml:"heartbeat_interval"`
	RebalanceTimeout time.Duration `yaml:"rebalance_timeout"`

	// Producer settings
	ProducerFlushFrequency time.Duration `yaml:"producer_flush_frequency"`
	ProducerFlushMessages  int           `yaml:"producer_flush_messages"`
	ProducerFlushBytes     int           `yaml:"producer_flush_bytes"`
	ProducerRetryMax       int           `yaml:"producer_retry_max"`
	ProducerRetryBackoff   time.Duration `yaml:"producer_retry_backoff"`

	// Consumer settings
	ConsumerFetchMin     int           `yaml:"consumer_fetch_min"`
	ConsumerFetchDefault int           `yaml:"consumer_fetch_default"`
	ConsumerFetchMax     int           `yaml:"consumer_fetch_max"`
	ConsumerMaxWait      time.Duration `yaml:"consumer_max_wait"`
	ConsumerMaxBytes     int           `yaml:"consumer_max_bytes"`

	// Security settings
	SecurityProtocol string `yaml:"security_protocol"`
	SASLUsername     string `yaml:"sasl_username"`
	SASLPassword     string `yaml:"sasl_password"`
	SASLMechanism    string `yaml:"sasl_mechanism"`
	TLSCertFile      string `yaml:"tls_cert_file"`
	TLSKeyFile       string `yaml:"tls_key_file"`
	TLSCAFile        string `yaml:"tls_ca_file"`
}

// ConsumersConfig holds consumer-specific configuration
type ConsumersConfig struct {
	CombatProcessor ConsumersGroupConfig `yaml:"combat_processor"`
	DamageValidator ConsumersGroupConfig `yaml:"damage_validator"`
	EconomyProcessor ConsumersGroupConfig `yaml:"economy_processor"`
	SocialProcessor ConsumersGroupConfig `yaml:"social_processor"`
	SystemMonitor   ConsumersGroupConfig `yaml:"system_monitor"`
	AnalyticsProcessor ConsumersGroupConfig `yaml:"analytics_processor"`
}

// ConsumersGroupConfig holds configuration for a consumer group
type ConsumersGroupConfig struct {
	PartitionsPerConsumer int           `yaml:"partitions_per_consumer"`
	SessionTimeout        time.Duration `yaml:"session_timeout"`
	HeartbeatInterval     time.Duration `yaml:"heartbeat_interval"`
	MaxProcessingTime     time.Duration `yaml:"max_processing_time"`
	BufferSize            int           `yaml:"buffer_size"`
	WorkerCount           int           `yaml:"worker_count"`
	RetryAttempts         int           `yaml:"retry_attempts"`
	RetryBackoff          time.Duration `yaml:"retry_backoff"`
	DLQTopic              string        `yaml:"dlq_topic"`
}

// ProducersConfig holds producer-specific configuration
type ProducersConfig struct {
	BatchSize    int           `yaml:"batch_size"`
	BatchTimeout time.Duration `yaml:"batch_timeout"`
	Compression  string        `yaml:"compression"`
	Acks         string        `yaml:"acks"`
	Timeout      time.Duration `yaml:"timeout"`
}

// MetricsConfig holds metrics configuration
type MetricsConfig struct {
	Enabled       bool          `yaml:"enabled"`
	Interval      time.Duration `yaml:"interval"`
	PushGateway   string        `yaml:"push_gateway"`
	JobName       string        `yaml:"job_name"`
	InstanceLabel string        `yaml:"instance_label"`
}

// EventsConfig holds event processing configuration
type EventsConfig struct {
	SchemaPath         string        `yaml:"schema_path"`
	ValidationEnabled  bool          `yaml:"validation_enabled"`
	DeadLetterEnabled  bool          `yaml:"dead_letter_enabled"`
	ProcessingTimeout  time.Duration `yaml:"processing_timeout"`
	MaxRetries         int           `yaml:"max_retries"`
	RetryBackoff       time.Duration `yaml:"retry_backoff"`
	CorrelationEnabled bool          `yaml:"correlation_enabled"`
	TracingEnabled     bool          `yaml:"tracing_enabled"`
}

// Load loads configuration from environment variables and config file
func Load() (*Config, error) {
	config := &Config{}

	// Load from environment variables first
	if err := loadFromEnv(config); err != nil {
		return nil, fmt.Errorf("failed to load config from environment: %w", err)
	}

	// Load from config file if it exists
	configFile := os.Getenv("CONFIG_FILE")
	if configFile == "" {
		configFile = "config.yaml"
	}

	if _, err := os.Stat(configFile); err == nil {
		if err := loadFromFile(config, configFile); err != nil {
			return nil, fmt.Errorf("failed to load config from file: %w", err)
		}
	}

	// Set defaults for missing values
	setDefaults(config)

	// Validate configuration
	if err := validateConfig(config); err != nil {
		return nil, fmt.Errorf("invalid configuration: %w", err)
	}

	return config, nil
}

// SaramaConfig creates a Sarama configuration from the Kafka config
func (k *KafkaConfig) SaramaConfig() *sarama.Config {
	config := sarama.NewConfig()

	// Client configuration
	config.ClientID = k.ClientID
	config.Version = sarama.V3_6_0_0 // Kafka 3.6+

	// Producer configuration
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Flush.Frequency = k.ProducerFlushFrequency
	config.Producer.Flush.Messages = k.ProducerFlushMessages
	config.Producer.Flush.Bytes = k.ProducerFlushBytes
	config.Producer.Retry.Max = k.ProducerRetryMax
	config.Producer.Retry.Backoff = k.ProducerRetryBackoff
	config.Producer.Return.Successes = true

	// Consumer configuration
	config.Consumer.Fetch.Min = int32(k.ConsumerFetchMin)
	config.Consumer.Fetch.Default = int32(k.ConsumerFetchDefault)
	config.Consumer.Fetch.Max = int32(k.ConsumerFetchMax)
	config.Consumer.MaxWaitTime = k.ConsumerMaxWait
	config.Consumer.MaxProcessingTime = 0 // Disable auto-commit
	config.Consumer.Return.Errors = true

	// Consumer group configuration
	config.Consumer.Group.Session.Timeout = k.SessionTimeout
	config.Consumer.Group.Heartbeat.Interval = k.HeartbeatInterval
	config.Consumer.Group.Rebalance.Timeout = k.RebalanceTimeout
	config.Consumer.Group.Rebalance.Retry.Max = 4
	config.Consumer.Group.Rebalance.Retry.Backoff = 2 * time.Second

	// Admin client configuration
	config.Admin.Timeout = 30 * time.Second

	// Compression
	config.Producer.Compression = sarama.CompressionSnappy

	// Security configuration
	if k.SecurityProtocol != "" {
		switch strings.ToLower(k.SecurityProtocol) {
		case "sasl_plaintext":
			config.Net.SASL.Enable = true
			config.Net.SASL.User = k.SASLUsername
			config.Net.SASL.Password = k.SASLPassword
			config.Net.SASL.Mechanism = sarama.SASLMechanism(k.SASLMechanism)
		case "sasl_ssl":
			config.Net.SASL.Enable = true
			config.Net.SASL.User = k.SASLUsername
			config.Net.SASL.Password = k.SASLPassword
			config.Net.SASL.Mechanism = sarama.SASLMechanism(k.SASLMechanism)
			config.Net.TLS.Enable = true
		case "ssl":
			config.Net.TLS.Enable = true
			if k.TLSCertFile != "" && k.TLSKeyFile != "" {
				// For sarama, TLS config is handled through config.Net.TLS
				// The actual TLS config is managed by the sarama library
				// We just enable TLS here
			}
		}
	}

	return config
}

// loadFromEnv loads configuration from environment variables
func loadFromEnv(config *Config) error {
	// Kafka brokers
	if brokers := os.Getenv("KAFKA_BROKERS"); brokers != "" {
		config.Kafka.Brokers = strings.Split(brokers, ",")
	}

	// Kafka client settings
	config.Kafka.ClientID = getEnvOrDefault("KAFKA_CLIENT_ID", "kafka-event-driven-core")
	config.Kafka.GroupID = getEnvOrDefault("KAFKA_GROUP_ID", "event-driven-consumers")

	// Parse durations
	if val := os.Getenv("KAFKA_SESSION_TIMEOUT"); val != "" {
		if d, err := time.ParseDuration(val); err == nil {
			config.Kafka.SessionTimeout = d
		}
	}

	if val := os.Getenv("KAFKA_HEARTBEAT_INTERVAL"); val != "" {
		if d, err := time.ParseDuration(val); err == nil {
			config.Kafka.HeartbeatInterval = d
		}
	}

	// Security settings
	config.Kafka.SecurityProtocol = os.Getenv("KAFKA_SECURITY_PROTOCOL")
	config.Kafka.SASLUsername = os.Getenv("KAFKA_SASL_USERNAME")
	config.Kafka.SASLPassword = os.Getenv("KAFKA_SASL_PASSWORD")
	config.Kafka.SASLMechanism = getEnvOrDefault("KAFKA_SASL_MECHANISM", "PLAIN")

	return nil
}

// loadFromFile loads configuration from YAML file
func loadFromFile(config *Config, filename string) error {
	data, err := os.ReadFile(filename)
	if err != nil {
		return fmt.Errorf("failed to read config file: %w", err)
	}

	if err := yaml.Unmarshal(data, config); err != nil {
		return fmt.Errorf("failed to parse config file: %w", err)
	}

	return nil
}

// setDefaults sets default values for missing configuration
func setDefaults(config *Config) {
	// Kafka defaults
	if len(config.Kafka.Brokers) == 0 {
		config.Kafka.Brokers = []string{"localhost:9092"}
	}

	if config.Kafka.ClientID == "" {
		config.Kafka.ClientID = "kafka-event-driven-core"
	}

	if config.Kafka.GroupID == "" {
		config.Kafka.GroupID = "event-driven-consumers"
	}

	if config.Kafka.SessionTimeout == 0 {
		config.Kafka.SessionTimeout = 30 * time.Second
	}

	if config.Kafka.HeartbeatInterval == 0 {
		config.Kafka.HeartbeatInterval = 3 * time.Second
	}

	if config.Kafka.RebalanceTimeout == 0 {
		config.Kafka.RebalanceTimeout = 60 * time.Second
	}

	// Producer defaults
	if config.Kafka.ProducerFlushFrequency == 0 {
		config.Kafka.ProducerFlushFrequency = 500 * time.Millisecond
	}

	if config.Kafka.ProducerFlushMessages == 0 {
		config.Kafka.ProducerFlushMessages = 100
	}

	if config.Kafka.ProducerFlushBytes == 0 {
		config.Kafka.ProducerFlushBytes = 1048576 // 1MB
	}

	if config.Kafka.ProducerRetryMax == 0 {
		config.Kafka.ProducerRetryMax = 5
	}

	if config.Kafka.ProducerRetryBackoff == 0 {
		config.Kafka.ProducerRetryBackoff = 100 * time.Millisecond
	}

	// Consumer defaults
	if config.Kafka.ConsumerFetchMin == 0 {
		config.Kafka.ConsumerFetchMin = 1
	}

	if config.Kafka.ConsumerFetchDefault == 0 {
		config.Kafka.ConsumerFetchDefault = 1048576 // 1MB
	}

	if config.Kafka.ConsumerFetchMax == 0 {
		config.Kafka.ConsumerFetchMax = 5242880 // 5MB
	}

	if config.Kafka.ConsumerMaxWait == 0 {
		config.Kafka.ConsumerMaxWait = 250 * time.Millisecond
	}

	if config.Kafka.ConsumerMaxBytes == 0 {
		config.Kafka.ConsumerMaxBytes = 10485760 // 10MB
	}

	// Consumer group defaults
	setConsumerGroupDefaults(&config.Consumers.CombatProcessor, "combat_processor")
	setConsumerGroupDefaults(&config.Consumers.DamageValidator, "damage_validator")
	setConsumerGroupDefaults(&config.Consumers.EconomyProcessor, "economy_processor")
	setConsumerGroupDefaults(&config.Consumers.SocialProcessor, "social_processor")
	setConsumerGroupDefaults(&config.Consumers.SystemMonitor, "system_monitor")
	setConsumerGroupDefaults(&config.Consumers.AnalyticsProcessor, "analytics_processor")

	// Producer defaults
	if config.Producers.BatchSize == 0 {
		config.Producers.BatchSize = 100
	}

	if config.Producers.BatchTimeout == 0 {
		config.Producers.BatchTimeout = 10 * time.Millisecond
	}

	if config.Producers.Compression == "" {
		config.Producers.Compression = "snappy"
	}

	if config.Producers.Acks == "" {
		config.Producers.Acks = "all"
	}

	if config.Producers.Timeout == 0 {
		config.Producers.Timeout = 30 * time.Second
	}

	// Metrics defaults
	if !config.Metrics.Enabled {
		config.Metrics.Enabled = true
	}

	if config.Metrics.Interval == 0 {
		config.Metrics.Interval = 15 * time.Second
	}

	if config.Metrics.JobName == "" {
		config.Metrics.JobName = "kafka-event-driven-core"
	}

	// Events defaults
	if config.Events.SchemaPath == "" {
		config.Events.SchemaPath = "proto/kafka/schemas"
	}

	if !config.Events.ValidationEnabled {
		config.Events.ValidationEnabled = true
	}

	if !config.Events.DeadLetterEnabled {
		config.Events.DeadLetterEnabled = true
	}

	if config.Events.ProcessingTimeout == 0 {
		config.Events.ProcessingTimeout = 30 * time.Second
	}

	if config.Events.MaxRetries == 0 {
		config.Events.MaxRetries = 3
	}

	if config.Events.RetryBackoff == 0 {
		config.Events.RetryBackoff = 1 * time.Second
	}
}

// setConsumerGroupDefaults sets default values for consumer group config
func setConsumerGroupDefaults(config *ConsumersGroupConfig, name string) {
	if config.PartitionsPerConsumer == 0 {
		config.PartitionsPerConsumer = 4
	}

	if config.SessionTimeout == 0 {
		config.SessionTimeout = 30 * time.Second
	}

	if config.HeartbeatInterval == 0 {
		config.HeartbeatInterval = 3 * time.Second
	}

	if config.MaxProcessingTime == 0 {
		config.MaxProcessingTime = 30 * time.Second
	}

	if config.BufferSize == 0 {
		config.BufferSize = 1000
	}

	if config.WorkerCount == 0 {
		config.WorkerCount = 10
	}

	if config.RetryAttempts == 0 {
		config.RetryAttempts = 3
	}

	if config.RetryBackoff == 0 {
		config.RetryBackoff = 1 * time.Second
	}

	if config.DLQTopic == "" {
		config.DLQTopic = "game.processing.dead.letter"
	}
}

// validateConfig validates the loaded configuration
func validateConfig(config *Config) error {
	if len(config.Kafka.Brokers) == 0 {
		return fmt.Errorf("kafka brokers not configured")
	}

	if config.Kafka.ClientID == "" {
		return fmt.Errorf("kafka client ID not configured")
	}

	for _, broker := range config.Kafka.Brokers {
		if broker == "" {
			return fmt.Errorf("empty kafka broker address")
		}
	}

	return nil
}

// getEnvOrDefault returns environment variable value or default
func getEnvOrDefault(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvIntOrDefault returns environment variable as int or default
func getEnvIntOrDefault(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intVal, err := strconv.Atoi(value); err == nil {
			return intVal
		}
	}
	return defaultValue
}

// getEnvDurationOrDefault returns environment variable as duration or default
func getEnvDurationOrDefault(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}
