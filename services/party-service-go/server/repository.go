// Issue: #139
package server

import (
	"context"
	"fmt"
	"sync"
)

type PartyRepository struct {
	mu      sync.RWMutex
	parties map[string]*Party
}

func NewPartyRepository() *PartyRepository {
	return &PartyRepository{
		parties: make(map[string]*Party),
	}
}

// CreateParty stores a new party
func (r *PartyRepository) CreateParty(ctx context.Context, party *Party) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	r.parties[party.ID] = party
	return nil
}

// GetParty retrieves a party by ID
func (r *PartyRepository) GetParty(ctx context.Context, partyID string) (*Party, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	party, exists := r.parties[partyID]
	if !exists {
		return nil, fmt.Errorf("party not found")
	}

	return party, nil
}

// UpdateParty updates a party
func (r *PartyRepository) UpdateParty(ctx context.Context, party *Party) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.parties[party.ID]; !exists {
		return fmt.Errorf("party not found")
	}

	r.parties[party.ID] = party
	return nil
}

// DeleteParty removes a party
func (r *PartyRepository) DeleteParty(ctx context.Context, partyID string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.parties[partyID]; !exists {
		return fmt.Errorf("party not found")
	}

	delete(r.parties, partyID)
	return nil
}
