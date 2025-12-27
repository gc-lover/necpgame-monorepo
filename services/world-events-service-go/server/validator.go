// World Events Validator - Input validation
// Issue: #2224

package server

import (
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/pkg/api"
)

type Validator struct {
	// TODO: Add validation rules
}

func NewValidator() *Validator {
	return &Validator{}
}

func (v *Validator) ValidateParticipationRequest(req *api.ParticipateRequest) error {
	// TODO: Implement validation logic
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
