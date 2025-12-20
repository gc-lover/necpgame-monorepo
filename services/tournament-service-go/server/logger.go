package server

import (
	"github.com/sirupsen/logrus"
)

// NewLogger creates a structured JSON logger for the Tournament Service
func NewLogger() *logrus.Logger {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: "2006-01-02T15:04:05Z07:00",
		FieldMap: logrus.FieldMap{
			logrus.FieldKeyTime:  "@timestamp",
			logrus.FieldKeyLevel: "@level",
			logrus.FieldKeyMsg:   "@message",
		},
	})
	logger.SetLevel(logrus.InfoLevel)
	return logger
}