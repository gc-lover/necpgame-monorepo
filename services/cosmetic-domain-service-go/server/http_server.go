// Issue: #backend-cosmetic_domain

package server

import (
	"net/http"

	"cosmetic-domain-service-go/pkg/api"
)

type CosmeticdomainService struct {
	api *api.Server
}

func NewCosmeticdomainService() *CosmeticdomainService {
	return &CosmeticdomainService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *CosmeticdomainService) Handler() http.Handler {
	return s.api
}
