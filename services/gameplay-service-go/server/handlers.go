// Issue: #1599, #1604, #1607, #387, #388
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger               *logrus.Logger
	comboService         ComboServiceInterface
	combatSessionService CombatSessionServiceInterface
	affixService         AffixServiceInterface
	abilityService       AbilityServiceInterface
	questRepository      QuestRepositoryInterface

	// Memory pooling for hot path structs (zero allocations target!)
	sessionListResponsePool   sync.Pool
	combatSessionResponsePool sync.Pool
	sessionEndResponsePool    sync.Pool
	getStealthStatusOKPool    sync.Pool
	internalServerErrorPool   sync.Pool
	badRequestPool            sync.Pool
}

// NewHandlers creates new handlers with memory pooling
// Issue: #1525 - Initialize services if db is provided
func NewHandlers(logger *logrus.Logger, db *pgxpool.Pool) *Handlers {
	h := &Handlers{logger: logger}

	// Initialize memory pools (zero allocations target!)
	h.sessionListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SessionListResponse{}
		},
	}
	h.combatSessionResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CombatSessionResponse{}
		},
	}
	h.sessionEndResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SessionEndResponse{}
		},
	}
	h.getStealthStatusOKPool = sync.Pool{
		New: func() interface{} {
			return &api.GetStealthStatusOK{}
		},
	}
	h.internalServerErrorPool = sync.Pool{
		New: func() interface{} {
			return &api.ActivateAbilityInternalServerError{}
		},
	}
	h.badRequestPool = sync.Pool{
		New: func() interface{} {
			return &api.CreateCombatSessionBadRequest{}
		},
	}

	if db != nil {
		h.comboService = NewComboService(db)
		h.combatSessionService = NewCombatSessionService(db)
		h.affixService = NewAffixService(db)
		h.abilityService = NewAbilityService(db)
		h.questRepository = NewQuestRepository(db)
	}

	return h
}
