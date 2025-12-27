package server

import (
    "context"
    "sync"

    "economy-domain-service-go/pkg/api"
    "go.uber.org/zap"
)

type Service struct {
    repo   *Repository
    logger *zap.Logger
    pool   *sync.Pool
}

func NewService() *Service {
    logger, _ := zap.NewProduction()
    return &Service{
        repo:   NewRepository(),
        logger: logger,
        pool: &sync.Pool{
            New: func() interface{} {
                return &api.HealthResponse{}
            },
        },
    }
}

func (s *Service) HealthCheck(ctx context.Context) error {
    return s.repo.HealthCheck(ctx)
}
