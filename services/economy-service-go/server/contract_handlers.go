// Package server Issue: #140890166 - Contract system extension
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

// ContractHandlers содержит обработчики для контрактов
type ContractHandlers struct {
	contractService *ContractService
	logger          *logrus.Logger
}

// NewContractHandlers создает новые обработчики контрактов
func NewContractHandlers(contractService *ContractService) *ContractHandlers {
	return &ContractHandlers{
		contractService: contractService,
		logger:          GetLogger(),
	}
}

// CreateContractHandler создает новый контракт
func (h *ContractHandlers) CreateContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	// Извлекаем buyerID из JWT токена (предполагаем, что middleware уже проверил токен)
	buyerID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req models.CreateContractRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	contract, err := h.contractService.CreateContract(ctx, buyerID, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create contract")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, contract, http.StatusCreated)
}

// StartNegotiationHandler начинает переговоры по контракту
func (h *ContractHandlers) StartNegotiationHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	if err := h.contractService.StartNegotiation(ctx, contractID, userID); err != nil {
		h.logger.WithError(err).Error("Failed to start negotiation")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "negotiation_started"}, http.StatusOK)
}

// UpdateContractTermsHandler обновляет условия контракта
func (h *ContractHandlers) UpdateContractTermsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req models.UpdateContractTermsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := h.contractService.UpdateContractTerms(ctx, contractID, userID, req); err != nil {
		h.logger.WithError(err).Error("Failed to update contract terms")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "terms_updated"}, http.StatusOK)
}

// AcceptContractHandler принимает контракт
func (h *ContractHandlers) AcceptContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	if err := h.contractService.AcceptContract(ctx, contractID, userID); err != nil {
		h.logger.WithError(err).Error("Failed to accept contract")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "contract_accepted"}, http.StatusOK)
}

// DepositEscrowHandler вносит депозит в эскроу
func (h *ContractHandlers) DepositEscrowHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req models.DepositEscrowRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := h.contractService.DepositEscrow(ctx, contractID, userID, req); err != nil {
		h.logger.WithError(err).Error("Failed to deposit escrow")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "escrow_deposited"}, http.StatusOK)
}

// CompleteContractHandler завершает контракт
func (h *ContractHandlers) CompleteContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	if err := h.contractService.CompleteContract(ctx, contractID, userID); err != nil {
		h.logger.WithError(err).Error("Failed to complete contract")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "contract_completed"}, http.StatusOK)
}

// CancelContractHandler отменяет контракт
func (h *ContractHandlers) CancelContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req struct {
		Reason string `json:"reason,omitempty"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := h.contractService.CancelContract(ctx, contractID, userID, req.Reason); err != nil {
		h.logger.WithError(err).Error("Failed to cancel contract")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "contract_cancelled"}, http.StatusOK)
}

// CreateDisputeHandler создает диспут для контракта
func (h *ContractHandlers) CreateDisputeHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req models.ContractDisputeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := h.contractService.CreateDispute(ctx, contractID, userID, req); err != nil {
		h.logger.WithError(err).Error("Failed to create dispute")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "dispute_created"}, http.StatusOK)
}

// ResolveDisputeHandler разрешает диспут (для арбитраторов)
func (h *ContractHandlers) ResolveDisputeHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var req struct {
		Decision string         `json:"decision"`
		Penalty  map[string]int `json:"penalty,omitempty"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := h.contractService.ResolveDispute(ctx, contractID, userID, req.Decision, req.Penalty); err != nil {
		h.logger.WithError(err).Error("Failed to resolve dispute")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]string{"status": "dispute_resolved"}, http.StatusOK)
}

// GetContractHandler получает контракт по ID
func (h *ContractHandlers) GetContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	contract, err := h.contractService.GetContract(ctx, contractID, userID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get contract")
		h.writeError(w, err.Error(), http.StatusNotFound)
		return
	}

	h.writeJSON(w, contract, http.StatusOK)
}

// GetUserContractsHandler получает контракты пользователя
func (h *ContractHandlers) GetUserContractsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	// Парсим query параметры
	statusStr := r.URL.Query().Get("status")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	var status *models.ContractStatus
	if statusStr != "" {
		s := models.ContractStatus(statusStr)
		status = &s
	}

	limit := 20 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	contracts, total, err := h.contractService.GetContractsByParticipant(ctx, userID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get user contracts")
		h.writeError(w, "Failed to get contracts", http.StatusInternalServerError)
		return
	}

	response := models.ContractListResponse{
		Contracts: contracts,
		Total:     total,
	}

	h.writeJSON(w, response, http.StatusOK)
}

// GetContractHistoryHandler получает историю контракта
func (h *ContractHandlers) GetContractHistoryHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	contractIDStr := vars["contractId"]

	contractID, err := uuid.Parse(contractIDStr)
	if err != nil {
		h.writeError(w, "Invalid contract ID", http.StatusBadRequest)
		return
	}

	userID, ok := getUserIDFromContext(ctx)
	if !ok {
		h.writeError(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 200 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	events, err := h.contractService.GetContractHistory(ctx, contractID, userID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get contract history")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	response := models.ContractHistoryResponse{
		ContractID: contractID,
		Events:     events,
	}

	h.writeJSON(w, response, http.StatusOK)
}

// Вспомогательные методы

func (h *ContractHandlers) writeJSON(w http.ResponseWriter, data interface{}, status int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ContractHandlers) writeError(w http.ResponseWriter, message string, status int) {
	h.writeJSON(w, map[string]string{"error": message}, status)
}

// getUserIDFromContext извлекает userID из контекста (реализуется в middleware)
func getUserIDFromContext(ctx context.Context) (uuid.UUID, bool) {
	// Это заглушка - в реальном коде userID извлекается из JWT токена
	// Предполагаем, что middleware уже добавил userID в контекст
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return uuid.Nil, false
	}

	userID, ok := userIDVal.(uuid.UUID)
	return userID, ok
}
