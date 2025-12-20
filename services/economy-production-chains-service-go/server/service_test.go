package server

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"net/http/httptest"
	"testing"
)

type chainsResp struct {
	Chains []ProductionChain `json:"chains"`
	Total  int               `json:"total"`
}

type orderResp struct {
	ID string `json:"id"`
}

func TestChainsAndOrdersFlow(t *testing.T) {
	svc := NewService()
	router := NewRouter(svc)

	// List chains
	req := httptest.NewRequest(http.MethodGet, "/api/v1/production/chains", nil)
	w := httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("chains status = %d", w.Code)
	}
	var cr chainsResp
	if err := json.Unmarshal(w.Body.Bytes(), &cr); err != nil {
		t.Fatalf("decode chains: %v", err)
	}
	if cr.Total == 0 || len(cr.Chains) == 0 {
		t.Fatalf("expected seeded chains")
	}
	chainID := cr.Chains[0].ID

	// Start production chain
	startBody := []byte(`{"quantity":2}`)
	req = httptest.NewRequest(http.MethodPost, fmt.Sprintf("/api/v1/production/chains/%s/start", chainID), bytes.NewReader(startBody))
	w = httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusCreated {
		t.Fatalf("start chain status = %d", w.Code)
	}

	// Create order
	createBody := fmt.Sprintf(`{"chain_id":"%s","quantity":1}`, chainID)
	req = httptest.NewRequest(http.MethodPost, "/api/v1/production/orders", bytes.NewReader([]byte(createBody)))
	w = httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusCreated {
		t.Fatalf("create order status = %d", w.Code)
	}
	var ord orderResp
	if err := json.Unmarshal(w.Body.Bytes(), &ord); err != nil {
		t.Fatalf("decode order: %v", err)
	}
	if ord.ID == "" {
		t.Fatalf("order id empty")
	}

	// Get order
	req = httptest.NewRequest(http.MethodGet, "/api/v1/production/orders/"+ord.ID, nil)
	w = httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("get order status = %d", w.Code)
	}

	// Rush order
	rushBody := []byte(`{"time_reduction":5}`)
	req = httptest.NewRequest(http.MethodPost, "/api/v1/production/orders/"+ord.ID+"/rush", bytes.NewReader(rushBody))
	w = httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusOK {
		t.Fatalf("rush status = %d", w.Code)
	}

	// Cancel order
	req = httptest.NewRequest(http.MethodDelete, "/api/v1/production/orders/"+ord.ID, nil)
	w = httptest.NewRecorder()
	router.ServeHTTP(w, req)
	if w.Code != http.StatusNoContent {
		t.Fatalf("cancel status = %d", w.Code)
	}
}
