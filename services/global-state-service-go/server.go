// Issue: #53
package main

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type server struct {
	log         *logrus.Logger
	router      *http.ServeMux
	stateStore  stateStore
	eventStore  eventStore
	defaultAddr string
}

func newServer() *server {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})

	state := newInMemoryStateStore()
	events := newInMemoryEventStore(1000)

	r := http.NewServeMux()

	s := &server{
		log:         logger,
		router:      r,
		stateStore:  state,
		eventStore:  events,
		defaultAddr: ":8082",
	}
	s.routes()
	return s
}

func (s *server) routes() {
	s.router.Handle("/health", s.wrap(s.health))
	s.router.Handle("/metrics", promhttp.Handler())
	s.router.Handle("/api/v1/state/batch", s.wrap(s.setStateBatch))
	s.router.Handle("/api/v1/state", s.wrap(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/api/v1/state" {
			s.getState(w, r)
			return
		}
		switch r.Method {
		case http.MethodGet:
			s.listState(w, r)
		case http.MethodPost:
			s.setState(w, r)
		default:
			http.NotFound(w, r)
		}
	}))
	s.router.Handle("/api/v1/state/", s.wrap(s.getState))
	s.router.Handle("/api/v1/events", s.wrap(func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			s.listEvents(w, r)
		case http.MethodPost:
			s.appendEvent(w, r)
		default:
			http.NotFound(w, r)
		}
	}))
}

func (s *server) health(w http.ResponseWriter, _ *http.Request) {
	writeJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (s *server) setState(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	var req stateMutationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, http.StatusBadRequest, "invalid json")
		return
	}
	if req.Key == "" || req.Category == "" || len(req.Value) == 0 {
		writeError(w, http.StatusBadRequest, "key, category and value are required")
		return
	}

	entry, err := s.stateStore.upsert(ctx, req)
	if err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		if errors.Is(err, errConflict) {
			writeError(w, http.StatusConflict, "version conflict")
			return
		}
		s.log.WithError(err).Error("failed to upsert state")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	writeJSON(w, http.StatusOK, entry)
}

func (s *server) setStateBatch(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	var req batchMutationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, http.StatusBadRequest, "invalid json")
		return
	}
	if len(req.Mutations) == 0 {
		writeError(w, http.StatusBadRequest, "mutations are required")
		return
	}
	for _, m := range req.Mutations {
		if m.Key == "" || m.Category == "" || len(m.Value) == 0 {
			writeError(w, http.StatusBadRequest, "key, category and value are required")
			return
		}
	}

	entries, err := s.stateStore.upsertBatch(ctx, req.Mutations)
	if err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		if errors.Is(err, errConflict) {
			writeError(w, http.StatusConflict, "version conflict")
			return
		}
		s.log.WithError(err).Error("failed to batch upsert state")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	writeJSON(w, http.StatusOK, entries)
}

func (s *server) getState(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()

	key := strings.TrimPrefix(r.URL.Path, "/api/v1/state/")
	if key == "" {
		writeError(w, http.StatusBadRequest, "empty key")
		return
	}
	entry, ok, err := s.stateStore.get(ctx, key)
	if err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		s.log.WithError(err).Error("failed to get state")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	if !ok {
		writeError(w, http.StatusNotFound, "not found")
		return
	}
	writeJSON(w, http.StatusOK, entry)
}

func (s *server) wrap(next http.HandlerFunc) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				s.log.WithField("panic", rec).Error("recovered panic")
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
		}()
		if r.Header.Get("X-Request-ID") == "" {
			r.Header.Set("X-Request-ID", uuid.New().String())
		}
		next.ServeHTTP(w, r)
	})
}

func (s *server) listState(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()

	categoryQuery := r.URL.Query().Get("category")
	var categories []string
	if categoryQuery != "" {
		for _, c := range strings.Split(categoryQuery, ",") {
			if trimmed := strings.TrimSpace(c); trimmed != "" {
				categories = append(categories, trimmed)
			}
		}
	}
	entries, err := s.stateStore.list(ctx, categories)
	if err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		s.log.WithError(err).Error("failed to list state")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	writeJSON(w, http.StatusOK, entries)
}

func (s *server) appendEvent(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()

	var req stateEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, http.StatusBadRequest, "invalid json")
		return
	}
	if req.EventType == "" || req.AggregateID == "" || len(req.Payload) == 0 {
		writeError(w, http.StatusBadRequest, "eventType, aggregateId and payload are required")
		return
	}
	event := stateEvent{
		ID:            uuid.NewString(),
		EventType:     req.EventType,
		AggregateID:   req.AggregateID,
		Payload:       req.Payload,
		CreatedAt:     time.Now().UTC(),
		CorrelationID: req.CorrelationID,
	}
	if err := s.eventStore.save(ctx, event); err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		s.log.WithError(err).Error("failed to store event")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	writeJSON(w, http.StatusAccepted, event)
}

func (s *server) listEvents(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()

	limit := parseLimit(r.URL.Query().Get("limit"), 50)
	events, err := s.eventStore.list(ctx, limit)
	if err != nil {
		if errors.Is(err, context.DeadlineExceeded) || errors.Is(err, context.Canceled) {
			writeError(w, http.StatusGatewayTimeout, "context deadline exceeded")
			return
		}
		s.log.WithError(err).Error("failed to list events")
		writeError(w, http.StatusInternalServerError, "internal error")
		return
	}
	writeJSON(w, http.StatusOK, events)
}

func parseLimit(raw string, def int) int {
	if raw == "" {
		return def
	}
	val, err := strconv.Atoi(raw)
	if err != nil || val <= 0 {
		return def
	}
	return val
}

func writeJSON(w http.ResponseWriter, status int, v interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(v)
}

func writeError(w http.ResponseWriter, status int, msg string) {
	writeJSON(w, status, map[string]string{"error": msg})
}






