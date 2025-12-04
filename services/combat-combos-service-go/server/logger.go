// Issue: #1595
package server

import (
	"github.com/sirupsen/logrus"
)

var defaultLogger *logrus.Logger

func init() {
	defaultLogger = logrus.New()
	defaultLogger.SetFormatter(&logrus.JSONFormatter{})
}

func GetLogger() *logrus.Logger {
	return defaultLogger
}

