// Issue: #57
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
)

type HackingExecution struct {
	ID              uuid.UUID
	PlayerID        uuid.UUID
	TargetID        uuid.UUID
	HackType        string
	DemonID         *uuid.UUID
	Success         bool
	Detected        bool
	OverheatIncrease float32
	CreatedAt       time.Time
}

type NetworkAccess struct {
	ID              uuid.UUID
	PlayerID        uuid.UUID
	NetworkID       uuid.UUID
	AccessLevel     string
	AccessGranted   bool
	CreatedAt       time.Time
}

type OverheatState struct {
	PlayerID    uuid.UUID
	CurrentHeat float32
	MaxHeat     float32
	Overheated  bool
	CoolingRate float32
	UpdatedAt   time.Time
}

type Repository interface {
	SaveHackingExecution(ctx context.Context, execution *HackingExecution) error
	GetHackingExecutions(ctx context.Context, playerID uuid.UUID) ([]*HackingExecution, error)
	SaveNetworkAccess(ctx context.Context, access *NetworkAccess) error
	GetNetworkAccess(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID) (*NetworkAccess, error)
	GetOverheatState(ctx context.Context, playerID uuid.UUID) (*OverheatState, error)
	UpdateOverheatState(ctx context.Context, state *OverheatState) error
}

type inMemoryRepository struct {
	mu                sync.RWMutex
	executions        map[uuid.UUID][]*HackingExecution
	networkAccesses   map[string]*NetworkAccess
	overheatStates    map[uuid.UUID]*OverheatState
}

func NewInMemoryRepository() Repository {
	return &inMemoryRepository{
		executions:      make(map[uuid.UUID][]*HackingExecution),
		networkAccesses: make(map[string]*NetworkAccess),
		overheatStates:  make(map[uuid.UUID]*OverheatState),
	}
}

func (r *inMemoryRepository) SaveHackingExecution(ctx context.Context, execution *HackingExecution) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if execution.CreatedAt.IsZero() {
		execution.CreatedAt = time.Now()
	}

	r.executions[execution.PlayerID] = append(r.executions[execution.PlayerID], execution)
	return nil
}

func (r *inMemoryRepository) GetHackingExecutions(ctx context.Context, playerID uuid.UUID) ([]*HackingExecution, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	executions, exists := r.executions[playerID]
	if !exists {
		return []*HackingExecution{}, nil
	}

	return executions, nil
}

func (r *inMemoryRepository) SaveNetworkAccess(ctx context.Context, access *NetworkAccess) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if access.CreatedAt.IsZero() {
		access.CreatedAt = time.Now()
	}

	key := access.PlayerID.String() + ":" + access.NetworkID.String()
	r.networkAccesses[key] = access
	return nil
}

func (r *inMemoryRepository) GetNetworkAccess(ctx context.Context, playerID uuid.UUID, networkID uuid.UUID) (*NetworkAccess, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	key := playerID.String() + ":" + networkID.String()
	access, exists := r.networkAccesses[key]
	if !exists {
		return nil, nil
	}

	return access, nil
}

func (r *inMemoryRepository) GetOverheatState(ctx context.Context, playerID uuid.UUID) (*OverheatState, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	state, exists := r.overheatStates[playerID]
	if !exists {
		return &OverheatState{
			PlayerID:    playerID,
			CurrentHeat: 0.0,
			MaxHeat:     100.0,
			Overheated:  false,
			CoolingRate: 1.0,
			UpdatedAt:   time.Now(),
		}, nil
	}

	return state, nil
}

func (r *inMemoryRepository) UpdateOverheatState(ctx context.Context, state *OverheatState) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	state.UpdatedAt = time.Now()
	r.overheatStates[state.PlayerID] = state
	return nil
}

























