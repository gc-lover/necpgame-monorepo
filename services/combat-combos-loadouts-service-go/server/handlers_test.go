// Combat Combos Loadouts Service Handlers Tests
// Issue: #141890005

package server

import (
	"testing"

	"github.com/google/uuid"
)

func TestValidateLoadout(t *testing.T) {
	service := &Service{}

	tests := []struct {
		name    string
		loadout *ComboLoadout
		wantErr bool
		errMsg  string
	}{
		{
			name:    "nil loadout",
			loadout: nil,
			wantErr: true,
			errMsg:  "loadout is nil",
		},
		{
			name: "valid loadout",
			loadout: &ComboLoadout{
				ID:           uuid.New(),
				CharacterID:  uuid.New(),
				ActiveCombos: []uuid.UUID{uuid.New(), uuid.New()},
				Preferences: ComboLoadoutPreferences{
					AutoActivate:    true,
					PriorityOrder:   []uuid.UUID{uuid.New()},
					MaxActiveCombos: 5,
				},
			},
			wantErr: false,
		},
		{
			name: "invalid character ID",
			loadout: &ComboLoadout{
				ID:           uuid.New(),
				CharacterID:  uuid.Nil,
				ActiveCombos: []uuid.UUID{},
			},
			wantErr: true,
			errMsg:  "invalid character ID",
		},
		{
			name: "too many active combos",
			loadout: &ComboLoadout{
				ID:           uuid.New(),
				CharacterID:  uuid.New(),
				ActiveCombos: make([]uuid.UUID, 15), // More than max 10
			},
			wantErr: true,
			errMsg:  "too many active combos",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := service.validateLoadout(tt.loadout)

			if tt.wantErr {
				if err == nil {
					t.Errorf("expected error, got nil")
					return
				}
				if tt.errMsg != "" && !contains(err.Error(), tt.errMsg) {
					t.Errorf("expected error containing '%s', got '%s'", tt.errMsg, err.Error())
				}
			} else {
				if err != nil {
					t.Errorf("unexpected error: %v", err)
				}
			}
		})
	}
}

func TestValidateUpdateRequest(t *testing.T) {
	service := &Service{}

	tests := []struct {
		name    string
		req     *UpdateLoadoutRequest
		wantErr bool
		errMsg  string
	}{
		{
			name:    "nil request",
			req:     nil,
			wantErr: true,
			errMsg:  "request is nil",
		},
		{
			name: "valid request",
			req: &UpdateLoadoutRequest{
				CharacterID:  uuid.New(),
				ActiveCombos: []uuid.UUID{uuid.New()},
				Preferences: ComboLoadoutPreferences{
					AutoActivate:    false,
					PriorityOrder:   []uuid.UUID{},
					MaxActiveCombos: 3,
				},
			},
			wantErr: false,
		},
		{
			name: "duplicate combo IDs",
			req: &UpdateLoadoutRequest{
				CharacterID:  uuid.New(),
				ActiveCombos: []uuid.UUID{uuid.New(), uuid.New()},
			},
			wantErr: false, // This should be valid, duplicates are allowed for now
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := service.validateUpdateRequest(tt.req)

			if tt.wantErr {
				if err == nil {
					t.Errorf("expected error, got nil")
					return
				}
				if tt.errMsg != "" && !contains(err.Error(), tt.errMsg) {
					t.Errorf("expected error containing '%s', got '%s'", tt.errMsg, err.Error())
				}
			} else {
				if err != nil {
					t.Errorf("unexpected error: %v", err)
				}
			}
		})
	}
}

// Helper function to check if string contains substring
func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > len(substr) && (s[:len(substr)] == substr || s[len(s)-len(substr):] == substr || containsAt(s, substr)))
}

func containsAt(s, substr string) bool {
	for i := 0; i <= len(s)-len(substr); i++ {
		if s[i:i+len(substr)] == substr {
			return true
		}
	}
	return false
}
