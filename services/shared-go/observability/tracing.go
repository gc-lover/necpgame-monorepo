// Distributed Tracing Configuration Library
// Issue: #2152
// PERFORMANCE: Jaeger, OpenTelemetry, service mesh metrics
// Enterprise-grade distributed tracing for all Go services

package observability

import (
	"context"
	"fmt"
	"time"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/resource"
	"go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.21.0"
	"go.uber.org/zap"
)

// TracingConfig holds distributed tracing configuration
type TracingConfig struct {
	// Service identification
	ServiceName    string
	ServiceVersion string
	ServiceType    string
	ServiceCategory string

	// Jaeger configuration
	JaegerEndpoint string
	JaegerEnabled  bool

	// OpenTelemetry Collector configuration
	OTLPEndpoint string
	OTLPEnabled  bool

	// Sampling configuration
	SamplingRate float64

	// Batch configuration
	BatchTimeout      time.Duration
	MaxExportBatchSize int
	MaxQueueSize      int

	// Resource attributes
	Environment string
	Deployment  string
}

// DefaultTracingConfig returns default tracing configuration
func DefaultTracingConfig() TracingConfig {
	return TracingConfig{
		ServiceName:        "necpgame-service",
		ServiceVersion:     "1.0.0",
		ServiceType:        "gameplay",
		ServiceCategory:    "general",
		JaegerEndpoint:     "http://jaeger.observability.svc.cluster.local:14268/api/traces",
		JaegerEnabled:      true,
		OTLPEndpoint:       "otel-collector.observability.svc.cluster.local:4317",
		OTLPEnabled:        true,
		SamplingRate:       1.0, // 100% sampling in dev, adjust for prod
		BatchTimeout:       100 * time.Millisecond,
		MaxExportBatchSize: 512,
		MaxQueueSize:       2048,
		Environment:        "development",
		Deployment:         "default",
	}
}

// SetupTracing initializes distributed tracing with Jaeger and OpenTelemetry
func SetupTracing(config TracingConfig, logger *zap.Logger) (*trace.TracerProvider, error) {
	// Create resource
	res, err := resource.New(
		context.Background(),
		resource.WithAttributes(
			semconv.ServiceNameKey.String(config.ServiceName),
			semconv.ServiceVersionKey.String(config.ServiceVersion),
			attribute.String("service.type", config.ServiceType),
			attribute.String("service.category", config.ServiceCategory),
			attribute.String("deployment.environment", config.Environment),
			attribute.String("deployment.name", config.Deployment),
		),
	)
	if err != nil {
		return nil, fmt.Errorf("failed to create resource: %w", err)
	}

	var exporter trace.SpanExporter

	// Use OTLP if enabled (preferred)
	if config.OTLPEnabled {
		ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
		defer cancel()

		otlpExporter, err := otlptracegrpc.New(
			ctx,
			otlptracegrpc.WithEndpoint(config.OTLPEndpoint),
			otlptracegrpc.WithInsecure(),
		)
		if err != nil {
			logger.Warn("Failed to create OTLP exporter, falling back to Jaeger",
				zap.Error(err))
		} else {
			exporter = otlpExporter
			logger.Info("OTLP exporter created",
				zap.String("endpoint", config.OTLPEndpoint))
		}
	}

	// Fallback to Jaeger if OTLP not available
	if exporter == nil && config.JaegerEnabled {
		jaegerExporter, err := jaeger.New(
			jaeger.WithCollectorEndpoint(
				jaeger.WithEndpoint(config.JaegerEndpoint),
			),
		)
		if err != nil {
			return nil, fmt.Errorf("failed to create Jaeger exporter: %w", err)
		}
		exporter = jaegerExporter
		logger.Info("Jaeger exporter created",
			zap.String("endpoint", config.JaegerEndpoint))
	}

	if exporter == nil {
		return nil, fmt.Errorf("no tracing exporter configured")
	}

	// Create sampler based on sampling rate
	var sampler trace.Sampler
	if config.SamplingRate >= 1.0 {
		sampler = trace.AlwaysSample()
	} else if config.SamplingRate <= 0.0 {
		sampler = trace.NeverSample()
	} else {
		sampler = trace.TraceIDRatioBased(config.SamplingRate)
	}

	// Create tracer provider with optimizations
	tp := trace.NewTracerProvider(
		trace.WithBatcher(
			exporter,
			trace.WithBatchTimeout(config.BatchTimeout),
			trace.WithMaxExportBatchSize(config.MaxExportBatchSize),
			trace.WithMaxQueueSize(config.MaxQueueSize),
		),
		trace.WithResource(res),
		trace.WithSampler(sampler),
	)

	// Set global tracer provider
	otel.SetTracerProvider(tp)

	// Set global propagator
	otel.SetTextMapPropagator(
		propagation.NewCompositeTextMapPropagator(
			propagation.TraceContext{},
			propagation.Baggage{},
		),
	)

	logger.Info("Distributed tracing initialized",
		zap.String("service", config.ServiceName),
		zap.Float64("sampling_rate", config.SamplingRate))

	return tp, nil
}

// ShutdownTracing gracefully shuts down tracing
func ShutdownTracing(ctx context.Context, tp *trace.TracerProvider, logger *zap.Logger) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := tp.Shutdown(ctx); err != nil {
		logger.Error("Failed to shutdown tracer provider", zap.Error(err))
		return err
	}

	logger.Info("Tracing shutdown complete")
	return nil
}
