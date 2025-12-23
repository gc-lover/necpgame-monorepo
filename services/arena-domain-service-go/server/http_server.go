// Issue: #backend-arena_domain

package server

import (
	"net/http"

	"arena-domain-service-go/pkg/api"
)

type ArenadomainService struct {
	api *api.Server
}

func NewArenadomainService() *ArenadomainService {
	return &ArenadomainService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *ArenadomainService) Handler() http.Handler {
	return s.api
}
