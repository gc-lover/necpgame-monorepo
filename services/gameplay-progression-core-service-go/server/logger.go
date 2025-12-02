// Issue: #164
package server

import (
	"github.com/sirupsen/logrus"
	"os"
)

var logger = logrus.New()

func init() {
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetOutput(os.Stdout)
	logger.SetLevel(logrus.InfoLevel)
}

func GetLogger() *logrus.Logger {
	return logger
}

