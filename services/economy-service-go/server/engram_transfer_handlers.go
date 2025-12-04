package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

func (s *HTTPServer) transferEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramTransferService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram transfer service not initialized")
		return
	}

	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		ToCharacterID   uuid.UUID `json:"to_character_id"`
		TransferType    string    `json:"transfer_type"`
		IsCopy          bool      `json:"is_copy"`
		NewAttitudeType *string   `json:"new_attitude_type,omitempty"`
		TransferPrice   *float64  `json:"transfer_price,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.ToCharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "to_character_id is required")
		return
	}

	if req.TransferType == "" {
		s.respondError(w, http.StatusBadRequest, "transfer_type is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}
	fromCharacterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	// Context timeout for DB operations (Issue #1604)
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	result, err := s.engramTransferService.TransferEngram(ctx, engramID, fromCharacterID, req.ToCharacterID, req.TransferType, req.IsCopy, req.NewAttitudeType, req.TransferPrice)
	if err != nil {
		s.logger.WithError(err).Error("Failed to transfer engram")
		s.respondError(w, http.StatusInternalServerError, "Failed to transfer engram")
		return
	}

	response := map[string]interface{}{
		"transfer_id":    result.TransferID.String(),
		"success":        result.Success,
		"transferred_at": result.TransferredAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	if result.NewEngramID != nil {
		response["new_engram_id"] = result.NewEngramID.String()
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) loanEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramTransferService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram transfer service not initialized")
		return
	}

	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		ToCharacterID    uuid.UUID `json:"to_character_id"`
		LoanDurationDays int       `json:"loan_duration_days"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.ToCharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "to_character_id is required")
		return
	}

	if req.LoanDurationDays < 1 || req.LoanDurationDays > 365 {
		s.respondError(w, http.StatusBadRequest, "loan_duration_days must be 1-365")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}
	fromCharacterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	// Context timeout for DB operations (Issue #1604)
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	result, err := s.engramTransferService.LoanEngram(ctx, engramID, fromCharacterID, req.ToCharacterID, req.LoanDurationDays)
	if err != nil {
		s.logger.WithError(err).Error("Failed to loan engram")
		s.respondError(w, http.StatusInternalServerError, "Failed to loan engram")
		return
	}

	response := map[string]interface{}{
		"loan_id":             result.LoanID.String(),
		"success":             result.Success,
		"return_date":         result.ReturnDate.Format("2006-01-02T15:04:05Z07:00"),
		"temporary_engram_id": result.TemporaryEngramID.String(),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) extractEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramTransferService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram transfer service not initialized")
		return
	}

	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		TargetCharacterID uuid.UUID `json:"target_character_id"`
		ExtractionMethod  string    `json:"extraction_method"`
		RiskLevel         float64   `json:"risk_level"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.TargetCharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "target_character_id is required")
		return
	}

	if req.RiskLevel < 20 || req.RiskLevel > 80 {
		s.respondError(w, http.StatusBadRequest, "risk_level must be 20-80%")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}
	extractorCharacterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	// Context timeout for DB operations (Issue #1604)
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	result, err := s.engramTransferService.ExtractEngram(ctx, engramID, extractorCharacterID, req.TargetCharacterID, req.ExtractionMethod, req.RiskLevel)
	if err != nil {
		s.logger.WithError(err).Error("Failed to extract engram")
		s.respondError(w, http.StatusInternalServerError, "Failed to extract engram")
		return
	}

	response := map[string]interface{}{
		"extraction_id":         result.ExtractionID.String(),
		"success":               result.Success,
		"engram_damaged":        result.EngramDamaged,
		"target_character_died": result.TargetCharacterDied,
	}

	if result.DamagePercent != nil {
		response["damage_percent"] = *result.DamagePercent
	}

	if result.ExtractedEngramID != nil {
		response["extracted_engram_id"] = result.ExtractedEngramID.String()
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) tradeEngram(w http.ResponseWriter, r *http.Request) {
	if s.engramTransferService == nil {
		s.respondError(w, http.StatusInternalServerError, "engram transfer service not initialized")
		return
	}

	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		TradeType         string     `json:"trade_type"`
		TargetCharacterID *uuid.UUID `json:"target_character_id,omitempty"`
		Price             *float64   `json:"price,omitempty"`
		ExchangeItemID    *uuid.UUID `json:"exchange_item_id,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.TradeType == "" {
		s.respondError(w, http.StatusBadRequest, "trade_type is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}
	fromCharacterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	// Context timeout for DB operations (Issue #1604)
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	result, err := s.engramTransferService.TradeEngram(ctx, engramID, fromCharacterID, req.TradeType, req.TargetCharacterID, req.Price, req.ExchangeItemID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to trade engram")
		s.respondError(w, http.StatusInternalServerError, "Failed to trade engram")
		return
	}

	response := map[string]interface{}{
		"trade_id":  result.TradeID.String(),
		"success":   result.Success,
		"traded_at": result.TradedAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	if result.NewOwnerID != nil {
		response["new_owner_id"] = result.NewOwnerID.String()
	}

	s.respondJSON(w, http.StatusOK, response)
}
