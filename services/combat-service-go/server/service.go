package server

import (
	"net/http"
	"sync"

	"github.com/sirupsen/logrus"
)

// CombatService OPTIMIZATION: Issue #1936 - Memory-aligned struct for performance
type CombatService struct {
	logger        *logrus.Logger
	metrics       *CombatMetrics
	activeCombats sync.Map // OPTIMIZATION: Thread-safe map for concurrent access

	// OPTIMIZATION: Issue #1607 - Memory pooling for hot path structs (zero allocations target!)
	initiateCombatResponsePool    sync.Pool
	combatStatusResponsePool      sync.Pool
	combatActionResponsePool      sync.Pool
	endCombatResponsePool         sync.Pool
	damageCalculationResponsePool sync.Pool
	statusEffectsResponsePool     sync.Pool
}

// HealthCheck handler for health check endpoint
func (s *CombatService) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"combat-service","version":"1.0.0"}`))
}

func NewCombatService(logger *logrus.Logger, metrics *CombatMetrics) *CombatService {
	s := &CombatService{
		logger:  logger,
		metrics: metrics,
	}

	// OPTIMIZATION: Issue #1607 - Initialize memory pools (zero allocations target!)
	s.initiateCombatResponsePool = sync.Pool{
		New: func() interface{} {
			return &InitiateCombatResponse{}
		},
	}
	s.combatStatusResponsePool = sync.Pool{
		New: func() interface{} {
			return &CombatStatusResponse{}
		},
	}
	s.combatActionResponsePool = sync.Pool{
		New: func() interface{} {
			return &CombatActionResponse{}
		},
	}
	s.endCombatResponsePool = sync.Pool{
		New: func() interface{} {
			return &EndCombatResponse{}
		},
	}
	s.damageCalculationResponsePool = sync.Pool{
		New: func() interface{} {
			return &DamageCalculationResponse{}
		},
	}
	s.statusEffectsResponsePool = sync.Pool{
		New: func() interface{} {
			return &StatusEffectsResponse{}
		},
	}

	return s
}
