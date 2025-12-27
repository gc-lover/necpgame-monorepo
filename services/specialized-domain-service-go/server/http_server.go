// Issue: #backend-specialized_domain

package server

import (
	"net/http"

	"specialized-domain-service-go/pkg/api"
)

type SpecializeddomainService struct {
	api *api.Server
}

func NewSpecializeddomainService() *SpecializeddomainService {
	server, _ := api.NewServer(&Handler{}, nil) // TODO: Add proper security handler
	return &SpecializeddomainService{
		api: server,
	}
}

func (s *SpecializeddomainService) Handler() http.Handler {
	return s.api
}
