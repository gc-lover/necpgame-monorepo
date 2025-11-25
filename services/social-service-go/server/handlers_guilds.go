package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/guilds"
	"github.com/sirupsen/logrus"
)

type GuildsServiceInterface interface {
	GetGuild(ctx context.Context, guildID uuid.UUID) (*guilds.Guild, error)
	ListGuilds(ctx context.Context, params guilds.ListGuildsParams) ([]guilds.Guild, error)
	CreateGuild(ctx context.Context, req *guilds.CreateGuildRequest) (*guilds.Guild, error)
	UpdateGuild(ctx context.Context, guildID uuid.UUID, req *guilds.UpdateGuildRequest) (*guilds.Guild, error)
	DeleteGuild(ctx context.Context, guildID uuid.UUID) error
	GetGuildMembers(ctx context.Context, guildID uuid.UUID, params guilds.GetGuildMembersParams) ([]guilds.GuildMember, error)
	AddGuildMember(ctx context.Context, guildID uuid.UUID, req *guilds.AddGuildMemberRequest) (*guilds.GuildMember, error)
	RemoveGuildMember(ctx context.Context, guildID, memberID uuid.UUID) error
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

func (h *GuildsHandlers) ListGuilds(w http.ResponseWriter, r *http.Request, params guilds.ListGuildsParams) {
	ctx := r.Context()
	
	guildsList, err := h.service.ListGuilds(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list guilds")
		h.respondError(w, http.StatusInternalServerError, "failed to list guilds")
		return
	}
	
	response := guilds.GuildListResponse{
		Guilds: &guildsList,
		Total:  new(int),
	}
	*response.Total = len(guildsList)
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

func (h *GuildsHandlers) DeleteGuild(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId) {
	ctx := r.Context()
	
	if err := h.service.DeleteGuild(ctx, guildId); err != nil {
		h.logger.WithError(err).Error("Failed to delete guild")
		h.respondError(w, http.StatusInternalServerError, "failed to delete guild")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *GuildsHandlers) GetGuildMembers(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId, params guilds.GetGuildMembersParams) {
	ctx := r.Context()
	
	members, err := h.service.GetGuildMembers(ctx, guildId, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get guild members")
		h.respondError(w, http.StatusInternalServerError, "failed to get guild members")
		return
	}
	
	response := guilds.GuildMemberListResponse{
		Members: &members,
		Total:   new(int),
	}
	*response.Total = len(members)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *GuildsHandlers) AddGuildMember(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId) {
	ctx := r.Context()
	
	var req guilds.AddGuildMemberRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	member, err := h.service.AddGuildMember(ctx, guildId, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add guild member")
		h.respondError(w, http.StatusInternalServerError, "failed to add guild member")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, member)
}

func (h *GuildsHandlers) RemoveGuildMember(w http.ResponseWriter, r *http.Request, guildId guilds.GuildId, memberId guilds.MemberId) {
	ctx := r.Context()
	
	if err := h.service.RemoveGuildMember(ctx, guildId, memberId); err != nil {
		h.logger.WithError(err).Error("Failed to remove guild member")
		h.respondError(w, http.StatusInternalServerError, "failed to remove guild member")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *GuildsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *GuildsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := guilds.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

