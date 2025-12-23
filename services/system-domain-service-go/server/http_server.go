// Issue: #backend-system_domain

package server

import (
	"net/http"

	"system-domain-service-go/pkg/api"
)

type SystemdomainService struct {
	api *api.Server
}

func NewSystemdomainService() *SystemdomainService {
	return &SystemdomainService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *SystemdomainService) Handler() http.Handler {
	return s.api
}
