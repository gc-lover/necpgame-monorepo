// World Events Validator - Input validation
// Issue: #2224

package server

import (
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/pkg/api"
)

type Validator struct {
	maxParticipants int
	maxEventDuration int
	allowedRegions   []string
}

func NewValidator() *Validator {
	return &Validator{
		maxParticipants: 1000, // MMO-scale event capacity
		maxEventDuration: 24,  // Hours
		allowedRegions:   []string{"EUROPE", "ASIA", "AMERICA", "AFRICA", "AUSTRALIA"},
	}
}

func (v *Validator) ValidateParticipationRequest(req *api.ParticipateRequest) error {
	if req.PlayerId.String() == "" {
		return fmt.Errorf("player ID cannot be empty")
	}
	return nil
}

func (v *Validator) ValidateCreateEventRequest(req *api.CreateEventRequest) error {
	// Basic validation
	if req.Name == "" {
		return fmt.Errorf("event name is required")
	}
	if req.Region == "" {
		return fmt.Errorf("event region is required")
	}
	return nil
}
