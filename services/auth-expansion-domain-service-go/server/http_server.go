// Issue: #backend-auth_expansion_domain

package server

import (
	"net/http"

	"auth-expansion-domain-service-go/pkg/api"
)

type AuthexpansiondomainService struct {
	api *api.Server
}

func NewAuthexpansiondomainService() *AuthexpansiondomainService {
	return &AuthexpansiondomainService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *AuthexpansiondomainService) Handler() http.Handler {
	return s.api
}
