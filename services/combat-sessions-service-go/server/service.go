package server

import (
	"github.com/sirupsen/logrus"
)

type Service struct {
	repo   *Repository
	logger *logrus.Logger
}

func NewService(repo *Repository) *Service {
	return &Service{
		repo:   repo,
		logger: GetLogger(),
	}
}

