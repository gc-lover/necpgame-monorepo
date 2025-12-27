// Issue: #backend-specialized_domain/character_service_go

package server

import (
	"net/http"

	"specialized-domain/character-service-go-service-go/pkg/api"
)

type Specializeddomain/CharacterservicegoService struct {
	api *api.Server
}

func NewSpecializeddomain/CharacterservicegoService() *Specializeddomain/CharacterservicegoService {
	return &Specializeddomain/CharacterservicegoService{
		api: api.NewServer(&Handler{}),
	}
}

func (s *Specializeddomain/CharacterservicegoService) Handler() http.Handler {
	return s.api
}
