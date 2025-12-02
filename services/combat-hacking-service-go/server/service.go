// Issue: #57
package server

import (
	"context"
	"errors"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/combat-hacking-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

const (
	MaxHeat         = 100.0
	DefaultCoolingRate = 1.0
	OverheatThreshold = 80.0
)

type HackingService interface {
	HackTarget(ctx context.Context, playerID uuid.UUID, req api.HackTargetRequest) (*api.HackResult, error)
	ActivateCountermeasures(ctx context.Context, playerID uuid.UUID, req api.CountermeasureRequest) (*api.CountermeasureResult, error)
	GetDemons(ctx context.Context, playerID uuid.UUID) ([]api.Demon, error)
	ActivateDemon(ctx context.Context, playerID uuid.UUID, demonID uuid.UUID, req api.ActivateDemonRequest) (*api.DemonActivationResult, error)
	GetICELevel(ctx context.Context, targetID uuid.UUID) (*api.ICEInfo, error)
	GetNetworkInfo(ctx context.Context, networkID uuid.UUID) (*api.NetworkInfo, error)
	AccessNetwork(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID, req api.NetworkAccessRequest) (*api.NetworkAccessResult, error)
	GetOverheatStatus(ctx context.Context, playerID uuid.UUID) (*api.OverheatStatus, error)
}

type hackingService struct {
	repo   Repository
	logger *logrus.Logger
}

func NewHackingService(repo Repository, logger *logrus.Logger) HackingService {
	return &hackingService{
		repo:   repo,
		logger: logger,
	}
}

func (s *hackingService) HackTarget(ctx context.Context, playerID uuid.UUID, req api.HackTargetRequest) (*api.HackResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"target_id": req.TargetId,
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
	overheatIncrease := s.calculateOverheatIncrease(req.HackType, req.DemonId != nil)

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
		TargetID:        req.TargetId,
		HackType:        string(req.HackType),
		DemonID:         req.DemonId,
		Success:         success,
		Detected:        detected,
		OverheatIncrease: overheatIncrease,
	}

	if err := s.repo.SaveHackingExecution(ctx, execution); err != nil {
		return nil, err
	}

	effects := s.generateEffects(req.HackType, success)

	result := &api.HackResult{
		Success:         &success,
		Detected:        &detected,
		Effects:         &effects,
		OverheatIncrease: &overheatIncrease,
	}

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

func (s *hackingService) ActivateCountermeasures(ctx context.Context, playerID uuid.UUID, req api.CountermeasureRequest) (*api.CountermeasureResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"attacker_id": req.AttackerId,
		"type": req.CountermeasureType,
	}).Info("Activating countermeasures")

	activated := true
	countermeasureType := "unknown"
	if req.CountermeasureType != nil {
		countermeasureType = string(*req.CountermeasureType)
	}
	effects := []map[string]interface{}{
		{
			"type":        countermeasureType,
			"description": "Countermeasure activated",
		},
	}

	result := &api.CountermeasureResult{
		Activated: &activated,
		Effects:   &effects,
	}

	return result, nil
}

func (s *hackingService) GetDemons(ctx context.Context, playerID uuid.UUID) ([]api.Demon, error) {
	demon1ID := openapi_types.UUID(uuid.New())
	demon2ID := openapi_types.UUID(uuid.New())
	name1 := "Ping"
	name2 := "Disable"
	demonType1 := api.DemonTypeControl
	demonType2 := api.DemonTypeDisable
	heatCost1 := float32(5.0)
	heatCost2 := float32(10.0)

	demons := []api.Demon{
		{
			Id:              &demon1ID,
			Name:            &name1,
			Type:            &demonType1,
			HeatCost:        &heatCost1,
		},
		{
			Id:              &demon2ID,
			Name:            &name2,
			Type:            &demonType2,
			HeatCost:        &heatCost2,
		},
	}

	return demons, nil
}

func (s *hackingService) ActivateDemon(ctx context.Context, playerID uuid.UUID, demonID uuid.UUID, req api.ActivateDemonRequest) (*api.DemonActivationResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"demon_id":  demonID,
		"target_id": req.TargetId,
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
	effects := []map[string]interface{}{
		{
			"type":        "demon_activated",
			"description": "Demon activated successfully",
		},
	}

	result := &api.DemonActivationResult{
		Activated:       &activated,
		OverheatIncrease: &overheatIncrease,
		Effects:         &effects,
	}

	return result, nil
}

func (s *hackingService) GetICELevel(ctx context.Context, targetID uuid.UUID) (*api.ICEInfo, error) {
	iceLevel := 5
	iceType := api.ICEInfoIceType("basic")

	info := &api.ICEInfo{
		TargetId: &targetID,
		IceLevel: &iceLevel,
		IceType:  &iceType,
	}

	return info, nil
}

func (s *hackingService) GetNetworkInfo(ctx context.Context, networkID uuid.UUID) (*api.NetworkInfo, error) {
	networkType := api.NetworkInfoType("simple")
	securityLevel := api.NetworkInfoSecurityLevel("low")
	nodes := []map[string]interface{}{}

	info := &api.NetworkInfo{
		Id:            &networkID,
		Type:          &networkType,
		SecurityLevel: &securityLevel,
		Nodes:         &nodes,
	}

	return info, nil
}

func (s *hackingService) AccessNetwork(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID, req api.NetworkAccessRequest) (*api.NetworkAccessResult, error) {
	s.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"network_id": networkID,
		"method":     req.Method,
	}).Info("Accessing network")

	accessGranted := true
	accessLevel := "read"
	availableTargets := []openapi_types.UUID{}

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

	result := &api.NetworkAccessResult{
		AccessGranted:   &accessGranted,
		AccessLevel:     &accessLevel,
		AvailableTargets: &availableTargets,
	}

	return result, nil
}

func (s *hackingService) GetOverheatStatus(ctx context.Context, playerID uuid.UUID) (*api.OverheatStatus, error) {
	state, err := s.repo.GetOverheatState(ctx, playerID)
	if err != nil {
		return nil, err
	}

	status := &api.OverheatStatus{
		CurrentHeat: &state.CurrentHeat,
		MaxHeat:     &state.MaxHeat,
		Overheated:  &state.Overheated,
		CoolingRate: &state.CoolingRate,
	}

	return status, nil
}

