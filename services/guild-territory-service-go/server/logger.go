// Package server Issue: #1856
// Structured logging with logrus for guild-territory-service
package server

import (
	"os"

	"github.com/sirupsen/logrus"
)

var logger *logrus.Logger

// GetLogger returns configured logger instance
func GetLogger() *logrus.Logger {
	if logger == nil {
		logger = logrus.New()

		// Set log level from environment
		level := os.Getenv("LOG_LEVEL")
		if level == "" {
			level = "info"
		}

		logLevel, err := logrus.ParseLevel(level)
		if err != nil {
			logLevel = logrus.InfoLevel
		}
		logger.SetLevel(logLevel)

		// Structured JSON logging for production
		logger.SetFormatter(&logrus.JSONFormatter{
			TimestampFormat: "2006-01-02T15:04:05Z07:00",
		})

		// Output to stdout
		logger.SetOutput(os.Stdout)
	}

	return logger
}
