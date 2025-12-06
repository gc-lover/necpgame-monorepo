// Issue: #1585
package server

import (
	"github.com/sirupsen/logrus"
)

var (
	logger *logrus.Logger
)

func init() {
	logger = logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)
}

// GetLogger returns the logger instance
func GetLogger() *logrus.Logger {
	return logger
}

