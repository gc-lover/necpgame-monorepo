package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-hacking-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type HackingHandlers struct {
	logger *logrus.Logger
}

func NewHackingHandlers() *HackingHandlers {
	return &HackingHandlers{
		logger: GetLogger(),
	}
}

func (h *HackingHandlers) HackTarget(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.HackTargetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode HackTarget request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"target_id": req.TargetId,
		"hack_type": req.HackType,
		"demon_id":  req.DemonId,
	}).Info("HackTarget request")

	success := true
	detected := false
	overheatIncrease := float32(10.0)
	effects := []map[string]interface{}{}

	response := api.HackResult{
		Success:         &success,
		Detected:        &detected,
		Effects:         &effects,
		OverheatIncrease: &overheatIncrease,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ActivateCountermeasures(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.CountermeasureRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ActivateCountermeasures request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"attacker_id": req.AttackerId,
		"type":        req.CountermeasureType,
	}).Info("ActivateCountermeasures request")

	activated := true
	effects := []map[string]interface{}{}
	response := api.CountermeasureResult{
		Activated: &activated,
		Effects:   &effects,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetDemons(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetDemons request")

	demons := []api.Demon{}
	response := map[string]interface{}{
		"demons": demons,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ActivateDemon(w http.ResponseWriter, r *http.Request, demonId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.ActivateDemonRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ActivateDemon request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"demon_id":  demonId,
		"target_id": req.TargetId,
	}).Info("ActivateDemon request")

	activated := true
	overheatIncrease := float32(15.0)
	effects := []map[string]interface{}{}
	response := api.DemonActivationResult{
		Activated:       &activated,
		OverheatIncrease: &overheatIncrease,
		Effects:         &effects,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetICELevel(w http.ResponseWriter, r *http.Request, targetId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("target_id", targetId).Info("GetICELevel request")

	iceLevel := 5
	iceType := api.ICEInfoIceType("basic")
	response := api.ICEInfo{
		TargetId: &targetId,
		IceLevel: &iceLevel,
		IceType:  &iceType,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetNetworkInfo(w http.ResponseWriter, r *http.Request, networkId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("network_id", networkId).Info("GetNetworkInfo request")

	networkType := api.NetworkInfoType("simple")
	securityLevel := api.NetworkInfoSecurityLevel("low")
	nodes := []map[string]interface{}{}
	response := api.NetworkInfo{
		Id:            &networkId,
		Type:          &networkType,
		SecurityLevel: &securityLevel,
		Nodes:         &nodes,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) AccessNetwork(w http.ResponseWriter, r *http.Request, networkId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.NetworkAccessRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode AccessNetwork request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"network_id": networkId,
		"method":     req.Method,
	}).Info("AccessNetwork request")

	accessGranted := true
	accessLevel := "read"
	availableTargets := []openapi_types.UUID{}
	response := api.NetworkAccessResult{
		AccessGranted:   &accessGranted,
		AccessLevel:     &accessLevel,
		AvailableTargets: &availableTargets,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetOverheatStatus(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetOverheatStatus request")

	currentHeat := float32(0.0)
	maxHeat := float32(100.0)
	overheated := false
	coolingRate := float32(1.0)
	response := api.OverheatStatus{
		CurrentHeat: &currentHeat,
		MaxHeat:     &maxHeat,
		Overheated:  &overheated,
		CoolingRate: &coolingRate,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *HackingHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}

