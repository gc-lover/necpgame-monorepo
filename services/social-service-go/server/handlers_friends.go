package server

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/pkg/api/friends"
	"github.com/sirupsen/logrus"
)

type FriendsHandlers struct {
	service FriendsServiceInterface
	logger  *logrus.Logger
}

func NewFriendsHandlers(service FriendsServiceInterface) *FriendsHandlers {
	return &FriendsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *FriendsHandlers) GetFriends(w http.ResponseWriter, r *http.Request, params friends.GetFriendsParams) {
	ctx := r.Context()
	
	limit := 50
	if params.Limit != nil && *params.Limit > 0 {
		limit = *params.Limit
	}
	
	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}
	
	onlineOnly := false
	if params.OnlineOnly != nil {
		onlineOnly = *params.OnlineOnly
	}
	
	friendships, total, err := h.service.GetFriends(ctx, limit, offset, onlineOnly)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get friends")
		h.respondError(w, http.StatusInternalServerError, "failed to get friends")
		return
	}
	
	response := friends.FriendListResponse{
		Friends: &friendships,
		Total:   &total,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *FriendsHandlers) GetFriendsCount(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	count, onlineCount, err := h.service.GetFriendsCount(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get friends count")
		h.respondError(w, http.StatusInternalServerError, "failed to get friends count")
		return
	}
	
	response := friends.FriendsCountResponse{
		Count:       &count,
		OnlineCount: &onlineCount,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *FriendsHandlers) GetOnlineFriends(w http.ResponseWriter, r *http.Request, params friends.GetOnlineFriendsParams) {
	ctx := r.Context()
	
	limit := 50
	if params.Limit != nil && *params.Limit > 0 {
		limit = *params.Limit
	}
	
	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}
	
	friendships, total, err := h.service.GetFriends(ctx, limit, offset, true)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get online friends")
		h.respondError(w, http.StatusInternalServerError, "failed to get online friends")
		return
	}
	
	response := friends.FriendListResponse{
		Friends: &friendships,
		Total:   &total,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *FriendsHandlers) RemoveFriend(w http.ResponseWriter, r *http.Request, friendId friends.FriendId) {
	ctx := r.Context()
	
	err := h.service.RemoveFriend(ctx, friendId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove friend")
		h.respondError(w, http.StatusInternalServerError, "failed to remove friend")
		return
	}
	
	status := "success"
	response := friends.StatusResponse{
		Status: &status,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *FriendsHandlers) GetFriend(w http.ResponseWriter, r *http.Request, friendId friends.FriendId) {
	ctx := r.Context()
	
	friendship, err := h.service.GetFriend(ctx, friendId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get friend")
		h.respondError(w, http.StatusNotFound, "friend not found")
		return
	}
	
	h.respondJSON(w, http.StatusOK, friendship)
}

func (h *FriendsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *FriendsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	code := http.StatusText(status)
	errorResponse := friends.Error{
		Error:   http.StatusText(status),
		Code:    &code,
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func RegisterFriendsHandlers(router *mux.Router, service FriendsServiceInterface) {
	handlers := NewFriendsHandlers(service)
	
	friends.HandlerFromMux(handlers, router)
}

