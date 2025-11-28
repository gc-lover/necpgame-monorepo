package server

import (
	"os"
	"sync"

	"github.com/sirupsen/logrus"
)

var (
	logger *logrus.Logger
	once   sync.Once
)

func GetLogger() *logrus.Logger {
	once.Do(func() {
		logger = logrus.New()
		logger.SetOutput(os.Stdout)
		logger.SetFormatter(&logrus.JSONFormatter{})
		
		level := os.Getenv("LOG_LEVEL")
		switch level {
		case "debug":
			logger.SetLevel(logrus.DebugLevel)
		case "info":
			logger.SetLevel(logrus.InfoLevel)
		case "warn":
			logger.SetLevel(logrus.WarnLevel)
		case "error":
			logger.SetLevel(logrus.ErrorLevel)
		default:
			logger.SetLevel(logrus.InfoLevel)
		}
	})
	return logger
}

