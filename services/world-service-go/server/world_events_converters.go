package server

import (
	"github.com/necpgame/world-service-go/pkg/api"
)

func convertEffectsFromRequest(effects *[]struct {
	EffectType   *string                 `json:"effect_type,omitempty"`
	Parameters   *map[string]interface{} `json:"parameters,omitempty"`
	TargetSystem *api.EventTargetSystem  `json:"target_system,omitempty"`
}) *[]api.EventEffect {
	if effects == nil {
		return nil
	}
	result := make([]api.EventEffect, len(*effects))
	for i, e := range *effects {
		result[i] = api.EventEffect{
			EffectType:   "",
			Parameters:   e.Parameters,
			TargetSystem: api.EventTargetSystem(""),
		}
		if e.EffectType != nil {
			result[i].EffectType = *e.EffectType
		}
		if e.TargetSystem != nil {
			result[i].TargetSystem = *e.TargetSystem
		}
	}
	return &result
}

