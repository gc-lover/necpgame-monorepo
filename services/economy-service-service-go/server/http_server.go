// Issue: #backend-economy_service

package server

import (
	"net/http"

	"economy-service-service-go/pkg/api"
)

type EconomyserviceService struct {
	api *api.Server
}

func NewEconomyserviceService() *EconomyserviceService {
	return &EconomyserviceService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *EconomyserviceService) Handler() http.Handler {
	return s.api
}
