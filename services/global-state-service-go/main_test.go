// Issue: #53
package main

import (
	"bytes"
	"encoding/json"
	"io"
	"net/http"
	"net/http/httptest"
	"testing"
)

type httpResponse struct {
	Status int
	Body   []byte
}

func doRequest(t *testing.T, method, url string, payload any) httpResponse {
	t.Helper()

	var body []byte
	if payload != nil {
		var err error
		body, err = json.Marshal(payload)
		if err != nil {
			t.Fatalf("marshal payload: %v", err)
		}
	}

	req, err := http.NewRequest(method, url, bytes.NewReader(body))
	if err != nil {
		t.Fatalf("new request: %v", err)
	}
	if payload != nil {
		req.Header.Set("Content-Type", "application/json")
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatalf("do request: %v", err)
	}
	defer resp.Body.Close()

	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		t.Fatalf("read body: %v", err)
	}

	return httpResponse{Status: resp.StatusCode, Body: respBody}
}

func startTestServer() *httptest.Server {
	s := newServer()
	return httptest.NewServer(s.router)
}

func TestSetAndGetState(t *testing.T) {
	ts := startTestServer()
	defer ts.Close()

	setResp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/state", map[string]any{
		"key":      "player:1",
		"category": "session",
		"value":    map[string]any{"hp": 100},
	})
	if setResp.Status != http.StatusOK {
		t.Fatalf("unexpected status: %d body=%s", setResp.Status, string(setResp.Body))
	}

	getResp := doRequest(t, http.MethodGet, ts.URL+"/api/v1/state/player:1", nil)
	if getResp.Status != http.StatusOK {
		t.Fatalf("unexpected get status: %d body=%s", getResp.Status, string(getResp.Body))
	}
	var state map[string]any
	_ = json.Unmarshal(getResp.Body, &state)
	if state["category"] != "session" {
		t.Fatalf("unexpected category: %v", state["category"])
	}
}

func TestVersionConflict(t *testing.T) {
	ts := startTestServer()
	defer ts.Close()

	initial := map[string]any{"key": "player:2", "category": "session", "value": map[string]any{"hp": 90}}
	if resp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/state", initial); resp.Status != http.StatusOK {
		t.Fatalf("initial set failed: %d body=%s", resp.Status, string(resp.Body))
	}

	update := map[string]any{
		"key":             "player:2",
		"category":        "session",
		"value":           map[string]any{"hp": 80},
		"expectedVersion": 1,
	}
	if resp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/state", update); resp.Status != http.StatusOK {
		t.Fatalf("expected success update: %d body=%s", resp.Status, string(resp.Body))
	}

	conflict := map[string]any{
		"key":             "player:2",
		"category":        "session",
		"value":           map[string]any{"hp": 70},
		"expectedVersion": 1,
	}
	if resp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/state", conflict); resp.Status != http.StatusConflict {
		t.Fatalf("expected conflict: got %d body=%s", resp.Status, string(resp.Body))
	}
}

func TestBatchUpsert(t *testing.T) {
	ts := startTestServer()
	defer ts.Close()

	batch := map[string]any{
		"mutations": []map[string]any{
			{"key": "player:3", "category": "session", "value": map[string]any{"hp": 75}},
			{"key": "player:4", "category": "session", "value": map[string]any{"hp": 65}},
		},
	}
	resp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/state/batch", batch)
	if resp.Status != http.StatusOK {
		t.Fatalf("batch failed: %d body=%s", resp.Status, string(resp.Body))
	}

	listResp := doRequest(t, http.MethodGet, ts.URL+"/api/v1/state?category=session", nil)
	if listResp.Status != http.StatusOK {
		t.Fatalf("list failed: %d body=%s", listResp.Status, string(listResp.Body))
	}
}

func TestEventsFlow(t *testing.T) {
	ts := startTestServer()
	defer ts.Close()

	event := map[string]any{
		"eventType":   "STATE_UPDATED",
		"aggregateId": "player:5",
		"payload":     map[string]any{"status": "online"},
	}
	if resp := doRequest(t, http.MethodPost, ts.URL+"/api/v1/events", event); resp.Status != http.StatusAccepted {
		t.Fatalf("append event failed: %d body=%s", resp.Status, string(resp.Body))
	}

	list := doRequest(t, http.MethodGet, ts.URL+"/api/v1/events?limit=1", nil)
	if list.Status != http.StatusOK {
		t.Fatalf("list events failed: %d body=%s", list.Status, string(list.Body))
	}
}


