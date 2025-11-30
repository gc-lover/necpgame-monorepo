package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) sendFriendRequest(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	var req models.SendFriendRequestRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	friendship, err := s.socialService.SendFriendRequest(r.Context(), characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to send friend request")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusCreated, friendship)
}

func (s *HTTPServer) acceptFriendRequest(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	vars := mux.Vars(r)
	requestIDStr := vars["id"]

	requestID, err := uuid.Parse(requestIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request id")
		return
	}

	friendship, err := s.socialService.AcceptFriendRequest(r.Context(), characterID, requestID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to accept friend request")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, friendship)
}

func (s *HTTPServer) rejectFriendRequest(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	vars := mux.Vars(r)
	requestIDStr := vars["id"]

	requestID, err := uuid.Parse(requestIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request id")
		return
	}

	err = s.socialService.RejectFriendRequest(r.Context(), characterID, requestID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to reject friend request")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusNoContent, nil)
}

func (s *HTTPServer) removeFriend(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	vars := mux.Vars(r)
	friendIDStr := vars["id"]

	friendID, err := uuid.Parse(friendIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid friend id")
		return
	}

	err = s.socialService.RemoveFriend(r.Context(), characterID, friendID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove friend")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusNoContent, nil)
}

func (s *HTTPServer) blockFriend(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	vars := mux.Vars(r)
	targetIDStr := vars["id"]

	targetID, err := uuid.Parse(targetIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid target id")
		return
	}

	friendship, err := s.socialService.BlockFriend(r.Context(), characterID, targetID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to block friend")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, friendship)
}

func (s *HTTPServer) getFriends(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	response, err := s.socialService.GetFriends(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get friends")
		s.respondError(w, http.StatusInternalServerError, "failed to get friends")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getFriendRequests(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterIDFromRequest(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, err.Error())
		return
	}

	requests, err := s.socialService.GetFriendRequests(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get friend requests")
		s.respondError(w, http.StatusInternalServerError, "failed to get friend requests")
		return
	}

	s.respondJSON(w, http.StatusOK, requests)
}

