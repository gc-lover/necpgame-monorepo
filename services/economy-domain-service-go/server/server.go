package server

import (
    "net/http"

    "economy-domain-service-go/pkg/api"
)

type EconomyService struct {
    server *api.Server
}

func NewEconomyService() *EconomyService {
    handler := NewHandler()

    // Create server with generated API
    server, err := api.NewServer(handler, nil)
    if err != nil {
        panic("Failed to create API server: " + err.Error())
    }

    return &EconomyService{
        server: server,
    }
}

func (s *EconomyService) Handler() http.Handler {
    return s.server
}
