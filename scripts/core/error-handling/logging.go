// Package logging provides comprehensive logging for MMOFPS game services
package logging

import (
	"context"
	"time"

	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

// Context key for request ID
type contextKey string

const RequestIDKey contextKey = "request_id"

// Logger wraps zap logger with additional functionality
type Logger struct {
	*zap.SugaredLogger
	config *LoggerConfig
}

// LoggerConfig holds logger configuration
type LoggerConfig struct {
	ServiceName string
	Level       zapcore.Level
	Development bool
	AddCaller   bool
}

// NewLogger creates a new structured logger
func NewLogger(config *LoggerConfig) (*Logger, error) {
	zapConfig := zap.NewProductionConfig()

	if config.Development {
		zapConfig = zap.NewDevelopmentConfig()
	}

	zapConfig.Level = zap.NewAtomicLevelAt(config.Level)
	zapConfig.EncoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder
	zapConfig.EncoderConfig.EncodeDuration = zapcore.StringDurationEncoder

	if config.AddCaller {
		zapConfig.EncoderConfig.EncodeCaller = zapcore.ShortCallerEncoder
	}

	// Add service name to all logs
	zapConfig.InitialFields = map[string]interface{}{
		"service": config.ServiceName,
	}

	baseLogger, err := zapConfig.Build(
		zap.AddCallerSkip(1),
		zap.AddStacktrace(zapcore.ErrorLevel),
	)
	if err != nil {
		return nil, err
	}

	logger := &Logger{
		SugaredLogger: baseLogger.Sugar(),
		config:        config,
	}

	return logger, nil
}

// WithRequestID adds request ID to logger context
func (l *Logger) WithRequestID(requestID string) *Logger {
	return &Logger{
		SugaredLogger: l.SugaredLogger.With("request_id", requestID),
		config:        l.config,
	}
}

// WithFields adds multiple fields to logger context
func (l *Logger) WithFields(fields map[string]interface{}) *Logger {
	args := make([]interface{}, 0, len(fields)*2)
	for k, v := range fields {
		args = append(args, k, v)
	}
	return &Logger{
		SugaredLogger: l.SugaredLogger.With(args...),
		config:        l.config,
	}
}

// WithContext extracts request ID from context and adds to logger
func (l *Logger) WithContext(ctx context.Context) *Logger {
	if requestID, ok := ctx.Value(RequestIDKey).(string); ok {
		return l.WithRequestID(requestID)
	}
	return l
}

// LogError logs an error with structured context
func (l *Logger) LogError(err error, message string, fields ...zap.Field) {
	if gameErr, ok := err.(*GameError); ok {
		fields = append(fields,
			zap.String("error_type", string(gameErr.Type)),
			zap.String("error_code", gameErr.Code),
			zap.String("error_message", gameErr.Message),
			zap.String("severity", gameErr.Severity),
			zap.Int("http_status", gameErr.HTTPStatus),
		)

		if gameErr.Details != "" {
			fields = append(fields, zap.String("error_details", gameErr.Details))
		}

		if len(gameErr.Fields) > 0 {
			fields = append(fields, zap.Any("error_fields", gameErr.Fields))
		}

		if gameErr.Cause != nil {
			fields = append(fields, zap.Error(gameErr.Cause))
		}
	}

	l.Errorw(message, fields...)
}

// LogRequest logs HTTP request details
func (l *Logger) LogRequest(method, path, userAgent, remoteAddr string, status int, duration time.Duration) {
	level := zapcore.InfoLevel
	if status >= 500 {
		level = zapcore.ErrorLevel
	} else if status >= 400 {
		level = zapcore.WarnLevel
	}

	l.Logw(level, "HTTP Request",
		"method", method,
		"path", path,
		"user_agent", userAgent,
		"remote_addr", remoteAddr,
		"status", status,
		"duration_ms", duration.Milliseconds(),
	)
}

// LogDatabaseOperation logs database operations
func (l *Logger) LogDatabaseOperation(operation, table string, duration time.Duration, err error) {
	fields := []zap.Field{
		zap.String("operation", operation),
		zap.String("table", table),
		zap.Duration("duration", duration),
	}

	if err != nil {
		l.Errorw("Database operation failed", append(fields, zap.Error(err))...)
	} else {
		l.Debugw("Database operation completed", fields...)
	}
}

// LogCacheOperation logs cache operations
func (l *Logger) LogCacheOperation(operation, key string, hit bool, duration time.Duration) {
	fields := []zap.Field{
		zap.String("operation", operation),
		zap.String("key", key),
		zap.Bool("cache_hit", hit),
		zap.Duration("duration", duration),
	}

	if hit {
		l.Debugw("Cache hit", fields...)
	} else {
		l.Debugw("Cache miss", fields...)
	}
}

// LogExternalServiceCall logs calls to external services
func (l *Logger) LogExternalServiceCall(service, method, url string, status int, duration time.Duration, err error) {
	fields := []zap.Field{
		zap.String("external_service", service),
		zap.String("method", method),
		zap.String("url", url),
		zap.Int("status", status),
		zap.Duration("duration", duration),
	}

	message := "External service call completed"
	if err != nil {
		message = "External service call failed"
		fields = append(fields, zap.Error(err))
		l.Errorw(message, fields...)
	} else if status >= 400 {
		l.Warnw(message, fields...)
	} else {
		l.Debugw(message, fields...)
	}
}

// LogBusinessEvent logs business domain events
func (l *Logger) LogBusinessEvent(eventType, entityType, entityID string, details map[string]interface{}) {
	fields := []zap.Field{
		zap.String("event_type", eventType),
		zap.String("entity_type", entityType),
		zap.String("entity_id", entityID),
	}

	if details != nil {
		fields = append(fields, zap.Any("event_details", details))
	}

	l.Infow("Business event", fields...)
}

// LogPerformanceMetric logs performance metrics
func (l *Logger) LogPerformanceMetric(metric string, value float64, tags map[string]string) {
	fields := []zap.Field{
		zap.String("metric", metric),
		zap.Float64("value", value),
	}

	if tags != nil {
		for k, v := range tags {
			fields = append(fields, zap.String("tag_"+k, v))
		}
	}

	l.Infow("Performance metric", fields...)
}

// Logw is a convenience method for structured logging with level
func (l *Logger) Logw(level zapcore.Level, message string, keysAndValues ...interface{}) {
	switch level {
	case zapcore.DebugLevel:
		l.Debugw(message, keysAndValues...)
	case zapcore.InfoLevel:
		l.Infow(message, keysAndValues...)
	case zapcore.WarnLevel:
		l.Warnw(message, keysAndValues...)
	case zapcore.ErrorLevel:
		l.Errorw(message, keysAndValues...)
	case zapcore.FatalLevel:
		l.Fatalw(message, keysAndValues...)
	}
}

// NewContextWithRequestID creates a new context with request ID
func NewContextWithRequestID(ctx context.Context, requestID string) context.Context {
	return context.WithValue(ctx, RequestIDKey, requestID)
}

// GetRequestIDFromContext extracts request ID from context
func GetRequestIDFromContext(ctx context.Context) (string, bool) {
	requestID, ok := ctx.Value(RequestIDKey).(string)
	return requestID, ok
}
