// Issue: #backend-reset_service_go

package server

import (
	"net/http"

	"reset-service-go-service-go/pkg/api"
)

type ResetservicegoService struct {
	api *api.Server
}

func NewResetservicegoService() *ResetservicegoService {
	return &ResetservicegoService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *ResetservicegoService) Handler() http.Handler {
	return s.api
}
