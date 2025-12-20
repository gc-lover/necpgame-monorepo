package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestAirDashFlow(t *testing.T) {
	svc := NewService()
	router := NewRouter(svc)

	body, _ := json.Marshal(AirDashRequest{
		CharacterID: "char-1",
		Direction:   &Vector3{X: 1, Y: 0, Z: 0},
		StaminaCost: 10,
	})
	req := httptest.NewRequest(http.MethodPost, "/api/v1/gameplay/combat/acrobatics/air-dash", bytes.NewReader(body))
	w := httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("air dash status = %d", w.Code)
	}

	// Charges should decrement to 1 (default 2)
	var state AirDashState
	if err := json.Unmarshal(w.Body.Bytes(), &state); err != nil {
		t.Fatalf("decode: %v", err)
	}
	if state.CurrentCharges != 1 {
		t.Fatalf("expected current_charges=1 got %d", state.CurrentCharges)
	}

	// Availability endpoint
	req2 := httptest.NewRequest(http.MethodGet, "/api/v1/gameplay/combat/acrobatics/air-dash/charges?character_id=char-1", nil)
	w2 := httptest.NewRecorder()
	router.ServeHTTP(w2, req2)
	if w2.Code != http.StatusOK {
		t.Fatalf("charges status = %d", w2.Code)
	}
}

func TestWallKickAndState(t *testing.T) {
	svc := NewService()
	router := NewRouter(svc)

	body, _ := json.Marshal(WallKickRequest{
		CharacterID: "char-2",
		Direction:   &Vector3{X: 0, Y: 1, Z: 0},
		ChainCount:  2,
	})
	req := httptest.NewRequest(http.MethodPost, "/api/v1/gameplay/combat/acrobatics/wall-kick", bytes.NewReader(body))
	w := httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("wall kick status = %d", w.Code)
	}

	req2 := httptest.NewRequest(http.MethodGet, "/api/v1/gameplay/combat/acrobatics/wall-kick/available?character_id=char-2", nil)
	w2 := httptest.NewRecorder()
	router.ServeHTTP(w2, req2)
	if w2.Code != http.StatusOK {
		t.Fatalf("wall kick availability status = %d", w2.Code)
	}
}

func TestVaultAndAdvancedState(t *testing.T) {
	svc := NewService()
	router := NewRouter(svc)

	body, _ := json.Marshal(VaultRequest{
		CharacterID: "char-3",
		ObstacleID:  "obs-1",
		ManualMode:  true,
		Direction:   &Vector3{X: 0, Y: 0, Z: 1},
	})
	req := httptest.NewRequest(http.MethodPost, "/api/v1/gameplay/combat/acrobatics/vault", bytes.NewReader(body))
	w := httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("vault status = %d", w.Code)
	}

	// Advanced state aggregates all parts
	req2 := httptest.NewRequest(http.MethodGet, "/api/v1/gameplay/combat/acrobatics/advanced/state?character_id=char-3", nil)
	w2 := httptest.NewRecorder()
	router.ServeHTTP(w2, req2)
	if w2.Code != http.StatusOK {
		t.Fatalf("advanced state status = %d", w2.Code)
	}
}
