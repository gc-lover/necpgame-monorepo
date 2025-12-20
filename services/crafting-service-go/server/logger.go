package server

import (
	"github.com/sirupsen/logrus"
)

// GetLogger returns configured logger instance
func GetLogger() *logrus.Logger {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: "2006-01-02T15:04:05Z07:00",
	})
	logger.SetLevel(logrus.InfoLevel)

	// PERFORMANCE: Async logging for high-throughput operations
	// logger.SetOutput() - keep default for now

	return logger
}
