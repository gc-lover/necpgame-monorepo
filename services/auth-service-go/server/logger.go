package server

import (
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1998 - Structured JSON logging for auth performance monitoring
func NewLogger() *logrus.Logger {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: "2006-01-02T15:04:05.000Z07:00",
		FieldMap: logrus.FieldMap{
			logrus.FieldKeyTime:  "timestamp",
			logrus.FieldKeyLevel: "level",
			logrus.FieldKeyMsg:   "message",
			logrus.FieldKeyFunc:  "caller",
		},
	})

	// OPTIMIZATION: Issue #1998 - Info level for production, can be configured
	logger.SetLevel(logrus.InfoLevel)

	// OPTIMIZATION: Issue #1998 - Enable caller info for auth debugging
	logger.SetReportCaller(true)

	return logger
}
