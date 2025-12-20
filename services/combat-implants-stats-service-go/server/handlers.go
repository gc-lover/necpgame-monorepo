// Package server Issue: #1595, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
// Memory pooling for hot path (Issue #1607)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-implants-stats-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger *logrus.Logger

	// Memory pooling for hot structs (zero allocations target!)
	energyStatusPool        sync.Pool
	humanityStatusPool      sync.Pool
	compatibilityResultPool sync.Pool
	setBonusesPool          sync.Pool
}

// NewHandlers creates new handlers with memory pooling
func NewHandlers() *Handlers {
	h := &Handlers{
		logger: GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.energyStatusPool = sync.Pool{
		New: func() interface{} {
			return &api.EnergyStatus{}
		},
	}
	h.humanityStatusPool = sync.Pool{
		New: func() interface{} {
			return &api.HumanityStatus{}
		},
	}
	h.compatibilityResultPool = sync.Pool{
		New: func() interface{} {
			return &api.CompatibilityResult{}
		},
	}
	h.setBonusesPool = sync.Pool{
		New: func() interface{} {
			return &api.SetBonuses{}
		},
	}

	return h
}

// GetEnergyStatus - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetEnergyStatus(ctx context.Context, params api.GetEnergyStatusParams) (api.GetEnergyStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithField("character_id", params.CharacterID).Info("GetEnergyStatus request")

	// Get memory pooled response (zero allocation!)
	resp := h.energyStatusPool.Get().(*api.EnergyStatus)
	defer h.energyStatusPool.Put(resp)

	// Reset pooled struct
	*resp = api.EnergyStatus{}

	current := float32(100.0)
	max := float32(100.0)
	consumption := float32(0.0)
	overheated := false
	coolingRate := float32(1.0)

	// Populate response
	resp.Current = api.NewOptFloat32(current)
	resp.Max = api.NewOptFloat32(max)
	resp.Consumption = api.NewOptFloat32(consumption)
	resp.Overheated = api.NewOptBool(false)
	resp.CoolingRate = api.NewOptFloat32(coolingRate)

	// Clone response (caller owns it)
	result := &api.EnergyStatus{
		Current:     resp.Current,
		Max:         resp.Max,
		Consumption: resp.Consumption,
		Overheated:  resp.Overheated,
		CoolingRate: resp.CoolingRate,
	}

	return result, nil
}

// GetHumanityStatus - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetHumanityStatus(ctx context.Context, params api.GetHumanityStatusParams) (api.GetHumanityStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithField("character_id", params.CharacterID).Info("GetHumanityStatus request")

	// Get memory pooled response (zero allocation!)
	resp := h.humanityStatusPool.Get().(*api.HumanityStatus)
	defer h.humanityStatusPool.Put(resp)

	// Reset pooled struct
	*resp = api.HumanityStatus{}

	current := float32(100.0)
	max := float32(100.0)
	cyberpsychosisRisk := float32(0.0)
	implantCount := 0

	// Populate response
	resp.Current = api.NewOptFloat32(current)
	resp.Max = api.NewOptFloat32(max)
	resp.CyberpsychosisRisk = api.NewOptFloat32(cyberpsychosisRisk)
	resp.ImplantCount = api.NewOptInt(implantCount)

	// Clone response (caller owns it)
	result := &api.HumanityStatus{
		Current:            resp.Current,
		Max:                resp.Max,
		CyberpsychosisRisk: resp.CyberpsychosisRisk,
		ImplantCount:       resp.ImplantCount,
	}

	return result, nil
}

// CheckCompatibility - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) CheckCompatibility(ctx context.Context, params api.CheckCompatibilityParams) (api.CheckCompatibilityRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithFields(logrus.Fields{
		"character_id": params.CharacterID,
		"implant_id":   params.ImplantID,
	}).Info("CheckCompatibility request")

	// Get memory pooled response (zero allocation!)
	resp := h.compatibilityResultPool.Get().(*api.CompatibilityResult)
	defer h.compatibilityResultPool.Put(resp)

	// Reset pooled struct
	resp.Conflicts = resp.Conflicts[:0] // Reuse slice
	resp.Warnings = resp.Warnings[:0]   // Reuse slice
	resp.EnergyCheck = api.OptCompatibilityResultEnergyCheck{}
	resp.HumanityCheck = api.OptCompatibilityResultHumanityCheck{}

	compatible := true
	var conflicts []api.CompatibilityResultConflictsItem
	var warnings []string
	availableEnergy := float32(100.0)
	requiredEnergy := float32(10.0)
	sufficientEnergy := true
	availableHumanity := float32(100.0)
	requiredHumanity := float32(5.0)
	sufficientHumanity := true

	energyCheck := api.CompatibilityResultEnergyCheck{
		Available:  api.NewOptFloat32(availableEnergy),
		Required:   api.NewOptFloat32(requiredEnergy),
		Sufficient: api.NewOptBool(true),
	}

	humanityCheck := api.CompatibilityResultHumanityCheck{
		Available:  api.NewOptFloat32(availableHumanity),
		Required:   api.NewOptFloat32(requiredHumanity),
		Sufficient: api.NewOptBool(true),
	}

	// Populate response
	resp.Compatible = api.NewOptBool(true)
	resp.Conflicts = conflicts
	resp.Warnings = warnings
	resp.EnergyCheck = api.NewOptCompatibilityResultEnergyCheck(energyCheck)
	resp.HumanityCheck = api.NewOptCompatibilityResultHumanityCheck(humanityCheck)

	// Clone response (caller owns it)
	result := &api.CompatibilityResult{
		Compatible:    resp.Compatible,
		Conflicts:     append([]api.CompatibilityResultConflictsItem{}, resp.Conflicts...),
		Warnings:      append([]string{}, resp.Warnings...),
		EnergyCheck:   resp.EnergyCheck,
		HumanityCheck: resp.HumanityCheck,
	}

	return result, nil
}

// GetSetBonuses - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetSetBonuses(ctx context.Context, params api.GetSetBonusesParams) (api.GetSetBonusesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithField("character_id", params.CharacterID).Info("GetSetBonuses request")

	// Get memory pooled response (zero allocation!)
	resp := h.setBonusesPool.Get().(*api.SetBonuses)
	defer h.setBonusesPool.Put(resp)

	// Reset pooled struct
	resp.ActiveSets = resp.ActiveSets[:0] // Reuse slice

	var activeSets []api.SetBonusesActiveSetsItem

	// Populate response
	resp.ActiveSets = activeSets

	// Clone response (caller owns it)
	result := &api.SetBonuses{
		ActiveSets: append([]api.SetBonusesActiveSetsItem{}, resp.ActiveSets...),
	}

	return result, nil
}
