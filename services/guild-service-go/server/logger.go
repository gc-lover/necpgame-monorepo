// Issue: #1943
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
		logger.SetOutput(os.Stdout)
		logger.SetFormatter(&logrus.JSONFormatter{
			TimestampFormat: "2006-01-02T15:04:05Z07:00",
		})

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
	}
	return logger
}
