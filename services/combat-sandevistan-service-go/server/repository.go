// Package server Issue: #39
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
)

type Activation struct {
	ID                     uuid.UUID
	PlayerID               uuid.UUID
	Phase                  string
	StartedAt              time.Time
	ActivePhaseStartedAt   *time.Time
	RecoveryPhaseStartedAt *time.Time
	EndedAt                *time.Time
	ActionBudgetRemaining  int
	ActionBudgetMax        int
	HeatStacks             int
	IsOverstress           bool
	IsActive               bool
	CreatedAt              time.Time
	UpdatedAt              time.Time
}

type TemporalMark struct {
	ID           uuid.UUID
	ActivationID uuid.UUID
	PlayerID     uuid.UUID
	TargetID     uuid.UUID
	MarkedAt     time.Time
	AppliedAt    *time.Time
	CreatedAt    time.Time
}

type Repository interface {
	GetActivation(ctx context.Context, playerID uuid.UUID) (*Activation, error)
	SaveActivation(ctx context.Context, activation *Activation) error
	GetTemporalMarks(ctx context.Context, playerID uuid.UUID) ([]*TemporalMark, error)
	SaveTemporalMarks(ctx context.Context, marks []*TemporalMark) error
	UpdateTemporalMarks(ctx context.Context, marks []*TemporalMark) error
}

type inMemoryRepository struct {
	mu          sync.RWMutex
	activations map[uuid.UUID]*Activation
	marks       map[uuid.UUID][]*TemporalMark
}

func NewInMemoryRepository() Repository {
	return &inMemoryRepository{
		activations: make(map[uuid.UUID]*Activation),
		marks:       make(map[uuid.UUID][]*TemporalMark),
	}
}

func (r *inMemoryRepository) GetActivation(_ context.Context, playerID uuid.UUID) (*Activation, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	activation, exists := r.activations[playerID]
	if !exists {
		return nil, nil
	}

	return activation, nil
}

func (r *inMemoryRepository) SaveActivation(_ context.Context, activation *Activation) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	now := time.Now()
	if activation.CreatedAt.IsZero() {
		activation.CreatedAt = now
	}
	activation.UpdatedAt = now

	r.activations[activation.PlayerID] = activation
	return nil
}

func (r *inMemoryRepository) GetTemporalMarks(_ context.Context, playerID uuid.UUID) ([]*TemporalMark, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	marks, exists := r.marks[playerID]
	if !exists {
		return []*TemporalMark{}, nil
	}

	return marks, nil
}

func (r *inMemoryRepository) SaveTemporalMarks(_ context.Context, marks []*TemporalMark) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if len(marks) == 0 {
		return nil
	}

	playerID := marks[0].PlayerID
	now := time.Now()

	for _, mark := range marks {
		if mark.CreatedAt.IsZero() {
			mark.CreatedAt = now
		}
	}

	r.marks[playerID] = marks
	return nil
}

func (r *inMemoryRepository) UpdateTemporalMarks(_ context.Context, marks []*TemporalMark) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if len(marks) == 0 {
		return nil
	}

	playerID := marks[0].PlayerID
	existingMarks, exists := r.marks[playerID]
	if !exists {
		return nil
	}

	// Update existing marks with applied status
	for _, updateMark := range marks {
		for i, existingMark := range existingMarks {
			if existingMark.ID == updateMark.ID {
				existingMarks[i].AppliedAt = updateMark.AppliedAt
				break
			}
		}
	}

	r.marks[playerID] = existingMarks
	return nil
}
