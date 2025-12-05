// Issue: #1585 - Logger helper for tests
package server

import (
	"go.uber.org/zap"
)

// GetLogger returns a development logger for testing
func GetLogger() *zap.Logger {
	logger, _ := zap.NewDevelopment()
	return logger
}

