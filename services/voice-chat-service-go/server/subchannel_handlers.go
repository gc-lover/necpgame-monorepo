package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/voice-chat-service-go/models"
)

type SubchannelServiceInterface interface {
	CreateSubchannel(ctx context.Context, lobbyID uuid.UUID, req *models.CreateSubchannelRequest) (*models.Subchannel, error)
	GetSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.Subchannel, error)
	ListSubchannels(ctx context.Context, lobbyID uuid.UUID) (*models.SubchannelListResponse, error)
	UpdateSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID, req *models.UpdateSubchannelRequest) (*models.Subchannel, error)
	DeleteSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) error
	MoveToSubchannel(ctx context.Context, lobbyID, subchannelID, characterID uuid.UUID, force bool) (*models.MoveToSubchannelResponse, error)
	GetSubchannelParticipants(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.SubchannelParticipantsResponse, error)
}

func (s *HTTPServer) listSubchannels(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	response, err := s.subchannelService.ListSubchannels(r.Context(), lobbyID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list subchannels")
		s.respondError(w, http.StatusInternalServerError, "failed to list subchannels")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) createSubchannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	var req models.CreateSubchannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	subchannel, err := s.subchannelService.CreateSubchannel(r.Context(), lobbyID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create subchannel")
		if err.Error() == "name must be between 2 and 32 characters" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to create subchannel")
		}
		return
	}

	s.respondJSON(w, http.StatusCreated, subchannel)
}

func (s *HTTPServer) getSubchannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	subchannelID, err := uuid.Parse(vars["subchannel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid subchannel_id")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	subchannel, err := s.subchannelService.GetSubchannel(r.Context(), lobbyID, subchannelID)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "subchannel not found")
		} else {
			s.logger.WithError(err).Error("Failed to get subchannel")
			s.respondError(w, http.StatusInternalServerError, "failed to get subchannel")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, subchannel)
}

func (s *HTTPServer) updateSubchannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	subchannelID, err := uuid.Parse(vars["subchannel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid subchannel_id")
		return
	}

	var req models.UpdateSubchannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	subchannel, err := s.subchannelService.UpdateSubchannel(r.Context(), lobbyID, subchannelID, &req)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "subchannel not found")
		} else {
			s.logger.WithError(err).Error("Failed to update subchannel")
			s.respondError(w, http.StatusInternalServerError, "failed to update subchannel")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, subchannel)
}

func (s *HTTPServer) deleteSubchannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	subchannelID, err := uuid.Parse(vars["subchannel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid subchannel_id")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	err = s.subchannelService.DeleteSubchannel(r.Context(), lobbyID, subchannelID)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "subchannel not found")
		} else if err.Error() == "cannot delete main subchannel" {
			s.respondError(w, http.StatusConflict, err.Error())
		} else {
			s.logger.WithError(err).Error("Failed to delete subchannel")
			s.respondError(w, http.StatusInternalServerError, "failed to delete subchannel")
		}
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func (s *HTTPServer) moveToSubchannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	subchannelID, err := uuid.Parse(vars["subchannel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid subchannel_id")
		return
	}

	var req models.MoveToSubchannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	response, err := s.subchannelService.MoveToSubchannel(r.Context(), lobbyID, subchannelID, req.CharacterID, req.Force)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "subchannel not found")
		} else if err.Error() == "subchannel is locked" || err.Error() == "subchannel is full" {
			s.respondError(w, http.StatusConflict, err.Error())
		} else {
			s.logger.WithError(err).Error("Failed to move to subchannel")
			s.respondError(w, http.StatusInternalServerError, "failed to move to subchannel")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getSubchannelParticipants(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	lobbyID, err := uuid.Parse(vars["lobby_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid lobby_id")
		return
	}

	subchannelID, err := uuid.Parse(vars["subchannel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid subchannel_id")
		return
	}

	if s.subchannelService == nil {
		s.respondError(w, http.StatusNotImplemented, "subchannel service not initialized")
		return
	}

	response, err := s.subchannelService.GetSubchannelParticipants(r.Context(), lobbyID, subchannelID)
	if err != nil {
		if err == pgx.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "subchannel not found")
		} else {
			s.logger.WithError(err).Error("Failed to get subchannel participants")
			s.respondError(w, http.StatusInternalServerError, "failed to get subchannel participants")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

