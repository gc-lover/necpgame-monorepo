package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *Service
	logger  *logrus.Logger
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

