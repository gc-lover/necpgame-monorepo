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
	return &SpecializeddomainService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *SpecializeddomainService) Handler() http.Handler {
	return s.api
}
