// Issue: #backend-reset_service_go

package server

import (
	"context"
	"net/http"

	"reset-service-go-service-go/pkg/api"
)

type ResetservicegoService struct {
	api *api.Server
}

func NewResetservicegoService() *ResetservicegoService {
	server, err := api.NewServer(NewHandler(), nil) // nil for security handler
	if err != nil {
		panic(err) // In production, handle this properly
	}

	return &ResetservicegoService{
		api: server,
	}
}

func (s *ResetservicegoService) Handler() http.Handler {
	return s.api
}
