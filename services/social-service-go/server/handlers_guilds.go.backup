//go:build ignore
// +build ignore

package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/pkg/api/guilds"
	"github.com/sirupsen/logrus"
)

type GuildsServiceInterface interface {
	SearchGuilds(ctx context.Context, params guilds.SearchGuildsParams) (*guilds.GuildListResponse, error)
	CreateGuild(ctx context.Context, req *guilds.CreateGuildRequest) (*guilds.Guild, error)
	GetGuild(ctx context.Context, guildID guilds.GuildId) (*guilds.Guild, error)
	UpdateGuild(ctx context.Context, guildID guilds.GuildId, req *guilds.UpdateGuildRequest) (*guilds.Guild, error)
}

type GuildsHandlers struct {
	service GuildsServiceInterface
	logger  *logrus.Logger
}

func NewGuildsHandlers(service GuildsServiceInterface) *GuildsHandlers {
	return &GuildsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *GuildsHandlers) SearchGuilds(w http.ResponseWriter, r *http.Request, params guilds.SearchGuildsParams) {
	ctx := r.Context()
	
	response, err := h.service.SearchGuilds(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to search guilds")
		h.respondError(w, http.StatusInternalServerError, "failed to search guilds")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *GuildsHandlers) CreateGuild(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req guilds.CreateGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	guild, err := h.service.CreateGuild(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create guild")
		h.respondError(w, http.StatusInternalServerError, "failed to create guild")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, guild)
}

func (h *GuildsHandlers) GetGuild(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId) {
	ctx := r.Context()
	
	guild, err := h.service.GetGuild(ctx, guildId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get guild")
		h.respondError(w, http.StatusInternalServerError, "failed to get guild")
		return
	}
	
	if guild == nil {
		h.respondError(w, http.StatusNotFound, "guild not found")
		return
	}
	
	h.respondJSON(w, http.StatusOK, guild)
}

func (h *GuildsHandlers) UpdateGuild(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId) {
	ctx := r.Context()
	
	var req guilds.UpdateGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	guild, err := h.service.UpdateGuild(ctx, guildId, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update guild")
		h.respondError(w, http.StatusInternalServerError, "failed to update guild")
		return
	}
	
	h.respondJSON(w, http.StatusOK, guild)
}

func (h *GuildsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")

	buf := &bytes.Buffer{}
	if err := json.NewEncoder(buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *GuildsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := guilds.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}
