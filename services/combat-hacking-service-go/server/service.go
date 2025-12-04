// Issue: #57, #1607
package server

import (
	"context"
	"errors"
	"math/rand"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/combat-hacking-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	MaxHeat         = 100.0
	DefaultCoolingRate = 1.0
	OverheatThreshold = 80.0
)

type HackingService interface {
	HackTarget(ctx context.Context, playerID uuid.UUID, req *api.HackTargetRequest) (*api.HackResult, error)
	ActivateCountermeasures(ctx context.Context, playerID uuid.UUID, req *api.CountermeasureRequest) (*api.CountermeasureResult, error)
	GetDemons(ctx context.Context, playerID uuid.UUID) ([]api.Demon, error)
	ActivateDemon(ctx context.Context, playerID uuid.UUID, demonID uuid.UUID, req *api.ActivateDemonRequest) (*api.DemonActivationResult, error)
	GetICELevel(ctx context.Context, targetID uuid.UUID) (*api.ICEInfo, error)
	GetNetworkInfo(ctx context.Context, networkID uuid.UUID) (*api.NetworkInfo, error)
	AccessNetwork(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID, req *api.NetworkAccessRequest) (*api.NetworkAccessResult, error)
	GetOverheatStatus(ctx context.Context, playerID uuid.UUID) (*api.OverheatStatus, error)
}

type hackingService struct {
	repo   Repository
	logger *logrus.Logger

	// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
	hackResultPool sync.Pool
	countermeasureResultPool sync.Pool
	demonActivationResultPool sync.Pool
	iceInfoPool sync.Pool
	networkInfoPool sync.Pool
	networkAccessResultPool sync.Pool
	overheatStatusPool sync.Pool
}

func NewHackingService(repo Repository, logger *logrus.Logger) HackingService {
	s := &hackingService{
		repo:   repo,
		logger: logger,
	}

	// Initialize memory pools (zero allocations target!)
	s.hackResultPool = sync.Pool{
		New: func() interface{} {
			return &api.HackResult{}
		},
	}
	s.countermeasureResultPool = sync.Pool{
		New: func() interface{} {
			return &api.CountermeasureResult{}
		},
	}
	s.demonActivationResultPool = sync.Pool{
		New: func() interface{} {
			return &api.DemonActivationResult{}
		},
	}
	s.iceInfoPool = sync.Pool{
		New: func() interface{} {
			return &api.ICEInfo{}
		},
	}
	s.networkInfoPool = sync.Pool{
		New: func() interface{} {
			return &api.NetworkInfo{}
		},
	}
	s.networkAccessResultPool = sync.Pool{
		New: func() interface{} {
			return &api.NetworkAccessResult{}
		},
	}
	s.overheatStatusPool = sync.Pool{
		New: func() interface{} {
			return &api.OverheatStatus{}
		},
	}

	return s
}

func (s *hackingService) HackTarget(ctx context.Context, playerID uuid.UUID, req *api.HackTargetRequest) (*api.HackResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"target_id": req.TargetID,
		"hack_type": req.HackType,
	}).Info("Hacking target")

	overheatState, err := s.repo.GetOverheatState(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if overheatState.Overheated {
		return nil, errors.New("system overheated, cannot hack")
	}

	success := s.calculateHackSuccess(req.HackType)
	detected := s.calculateDetection(req.HackType, success)
	overheatIncrease := s.calculateOverheatIncrease(req.HackType, req.DemonID.IsSet())

	newHeat := overheatState.CurrentHeat + overheatIncrease
	if newHeat > MaxHeat {
		newHeat = MaxHeat
	}

	overheated := newHeat >= OverheatThreshold
	overheatState.CurrentHeat = newHeat
	overheatState.Overheated = overheated

	if err := s.repo.UpdateOverheatState(ctx, overheatState); err != nil {
		return nil, err
	}

	execution := &HackingExecution{
		ID:              uuid.New(),
		PlayerID:        playerID,
		TargetID:        req.TargetID,
		HackType:        string(req.HackType),
		DemonID:         func() *uuid.UUID { if req.DemonID.IsSet() { d := req.DemonID.Value; return &d }; return nil }(),
		Success:         success,
		Detected:        detected,
		OverheatIncrease: overheatIncrease,
	}

	if err := s.repo.SaveHackingExecution(ctx, execution); err != nil {
		return nil, err
	}

	effects := s.generateEffects(req.HackType, success)
	effectsItems := make([]api.HackResultEffectsItem, len(effects))
	for i := range effects {
		effectsItems[i] = api.HackResultEffectsItem{}
	}

	// Issue: #1607 - Use memory pooling
	result := s.hackResultPool.Get().(*api.HackResult)
	// Note: Not returning to pool - struct is returned to caller

	result.Success = api.NewOptBool(success)
	result.Detected = api.NewOptBool(detected)
	result.Effects = effectsItems
	result.OverheatIncrease = api.NewOptFloat32(overheatIncrease)

	return result, nil
}

func (s *hackingService) calculateHackSuccess(hackType api.HackTargetRequestHackType) bool {
	rand.Seed(time.Now().UnixNano())
	baseChance := 0.7
	switch hackType {
	case api.HackTargetRequestHackTypeControl:
		baseChance = 0.6
	case api.HackTargetRequestHackTypeDisable:
		baseChance = 0.65
	case api.HackTargetRequestHackTypeScan:
		baseChance = 0.8
	case api.HackTargetRequestHackTypeExtract:
		baseChance = 0.55
	}
	return rand.Float64() < baseChance
}

func (s *hackingService) calculateDetection(hackType api.HackTargetRequestHackType, success bool) bool {
	if !success {
		return true
	}
	rand.Seed(time.Now().UnixNano())
	detectionChance := 0.3
	switch hackType {
	case api.HackTargetRequestHackTypeScan:
		detectionChance = 0.1
	case api.HackTargetRequestHackTypeExtract:
		detectionChance = 0.4
	}
	return rand.Float64() < detectionChance
}

func (s *hackingService) calculateOverheatIncrease(hackType api.HackTargetRequestHackType, hasDemon bool) float32 {
	baseHeat := float32(10.0)
	switch hackType {
	case api.HackTargetRequestHackTypeControl:
		baseHeat = 15.0
	case api.HackTargetRequestHackTypeDisable:
		baseHeat = 20.0
	case api.HackTargetRequestHackTypeScan:
		baseHeat = 5.0
	case api.HackTargetRequestHackTypeExtract:
		baseHeat = 25.0
	}
	if hasDemon {
		baseHeat *= 1.5
	}
	return baseHeat
}

func (s *hackingService) generateEffects(hackType api.HackTargetRequestHackType, success bool) []map[string]interface{} {
	if !success {
		return []map[string]interface{}{}
	}

	effects := []map[string]interface{}{}
	switch hackType {
	case api.HackTargetRequestHackTypeControl:
		effects = append(effects, map[string]interface{}{
			"type":        "control",
			"duration":    5.0,
			"description": "Target controlled",
		})
	case api.HackTargetRequestHackTypeDisable:
		effects = append(effects, map[string]interface{}{
			"type":        "disable",
			"duration":    10.0,
			"description": "Target disabled",
		})
	case api.HackTargetRequestHackTypeScan:
		effects = append(effects, map[string]interface{}{
			"type":        "scan",
			"description": "Target scanned",
		})
	case api.HackTargetRequestHackTypeExtract:
		effects = append(effects, map[string]interface{}{
			"type":        "extract",
			"description": "Data extracted",
		})
	}

	return effects
}

func (s *hackingService) ActivateCountermeasures(ctx context.Context, playerID uuid.UUID, req *api.CountermeasureRequest) (*api.CountermeasureResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"attacker_id": req.AttackerID,
		"type": req.CountermeasureType,
	}).Info("Activating countermeasures")

	activated := true
	effectsItems := []api.CountermeasureResultEffectsItem{
		{},
	}

	// Issue: #1607 - Use memory pooling
	result := s.countermeasureResultPool.Get().(*api.CountermeasureResult)
	// Note: Not returning to pool - struct is returned to caller

	result.Activated = api.NewOptBool(activated)
	result.Effects = effectsItems

	return result, nil
}

func (s *hackingService) GetDemons(ctx context.Context, playerID uuid.UUID) ([]api.Demon, error) {
	demon1ID := uuid.New()
	demon2ID := uuid.New()
	name1 := "Ping"
	name2 := "Disable"
	demonType1 := api.DemonTypeControl
	demonType2 := api.DemonTypeDisable
	heatCost1 := float32(5.0)
	heatCost2 := float32(10.0)

	demons := []api.Demon{
		{
			ID:              api.NewOptUUID(demon1ID),
			Name:            api.NewOptString(name1),
			Type:            api.NewOptDemonType(demonType1),
			HeatCost:        api.NewOptFloat32(heatCost1),
		},
		{
			ID:              api.NewOptUUID(demon2ID),
			Name:            api.NewOptString(name2),
			Type:            api.NewOptDemonType(demonType2),
			HeatCost:        api.NewOptFloat32(heatCost2),
		},
	}

	return demons, nil
}

func (s *hackingService) ActivateDemon(ctx context.Context, playerID uuid.UUID, demonID uuid.UUID, req *api.ActivateDemonRequest) (*api.DemonActivationResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"demon_id":  demonID,
		"target_id": req.TargetID,
	}).Info("Activating demon")

	overheatState, err := s.repo.GetOverheatState(ctx, playerID)
	if err != nil {
		return nil, err
	}

	if overheatState.Overheated {
		return nil, errors.New("system overheated, cannot activate demon")
	}

	overheatIncrease := float32(15.0)
	newHeat := overheatState.CurrentHeat + overheatIncrease
	if newHeat > MaxHeat {
		newHeat = MaxHeat
	}

	overheated := newHeat >= OverheatThreshold
	overheatState.CurrentHeat = newHeat
	overheatState.Overheated = overheated

	if err := s.repo.UpdateOverheatState(ctx, overheatState); err != nil {
		return nil, err
	}

	activated := true
	effectsItems := []api.DemonActivationResultEffectsItem{
		{},
	}

	// Issue: #1607 - Use memory pooling
	result := s.demonActivationResultPool.Get().(*api.DemonActivationResult)
	// Note: Not returning to pool - struct is returned to caller

	result.Activated = api.NewOptBool(activated)
	result.OverheatIncrease = api.NewOptFloat32(overheatIncrease)
	result.Effects = effectsItems

	return result, nil
}

func (s *hackingService) GetICELevel(ctx context.Context, targetID uuid.UUID) (*api.ICEInfo, error) {
	iceLevel := 5
	iceType := api.ICEInfoIceType("basic")

	// Issue: #1607 - Use memory pooling
	info := s.iceInfoPool.Get().(*api.ICEInfo)
	// Note: Not returning to pool - struct is returned to caller

	info.TargetID = api.NewOptUUID(targetID)
	info.IceLevel = api.NewOptInt(iceLevel)
	info.IceType = api.NewOptICEInfoIceType(iceType)
	info.ActiveCountermeasures = []string{}

	return info, nil
}

func (s *hackingService) GetNetworkInfo(ctx context.Context, networkID uuid.UUID) (*api.NetworkInfo, error) {
	networkType := api.NetworkInfoType("simple")
	securityLevel := api.NetworkInfoSecurityLevel("low")

	// Issue: #1607 - Use memory pooling
	info := s.networkInfoPool.Get().(*api.NetworkInfo)
	// Note: Not returning to pool - struct is returned to caller

	info.ID = api.NewOptUUID(networkID)
	info.Type = api.NewOptNetworkInfoType(networkType)
	info.SecurityLevel = api.NewOptNetworkInfoSecurityLevel(securityLevel)
	info.Nodes = []api.NetworkInfoNodesItem{}

	return info, nil
}

func (s *hackingService) AccessNetwork(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID, req *api.NetworkAccessRequest) (*api.NetworkAccessResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"network_id": networkID,
		"method":     req.Method,
	}).Info("Accessing network")

	accessGranted := true
	accessLevel := "read"

	access := &NetworkAccess{
		ID:            uuid.New(),
		PlayerID:      playerID,
		NetworkID:     networkID,
		AccessLevel:   accessLevel,
		AccessGranted: accessGranted,
	}

	if err := s.repo.SaveNetworkAccess(ctx, access); err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	result := s.networkAccessResultPool.Get().(*api.NetworkAccessResult)
	// Note: Not returning to pool - struct is returned to caller

	result.AccessGranted = api.NewOptBool(accessGranted)
	result.AccessLevel = api.NewOptString(accessLevel)
	result.AvailableTargets = []uuid.UUID{}

	return result, nil
}

func (s *hackingService) GetOverheatStatus(ctx context.Context, playerID uuid.UUID) (*api.OverheatStatus, error) {
	state, err := s.repo.GetOverheatState(ctx, playerID)
	if err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	status := s.overheatStatusPool.Get().(*api.OverheatStatus)
	// Note: Not returning to pool - struct is returned to caller

	status.CurrentHeat = api.NewOptFloat32(state.CurrentHeat)
	status.MaxHeat = api.NewOptFloat32(state.MaxHeat)
	status.Overheated = api.NewOptBool(state.Overheated)
	status.CoolingRate = api.NewOptFloat32(state.CoolingRate)

	return status, nil
}

