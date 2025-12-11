package main

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
	"sync"
	"syscall"
	"time"

	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type ModeState string

const (
	ModeActive    ModeState = "ACTIVE"
	ModeCompleted ModeState = "COMPLETED"
	ModeFailed    ModeState = "FAILED"
	ModeInactive  ModeState = "INACTIVE"
)

type RestrictedModeType string

const (
	ModeIronman  RestrictedModeType = "IRONMAN"
	ModeHardcore RestrictedModeType = "HARDCORE"
	ModeSolo     RestrictedModeType = "SOLO"
	ModeNoDeath  RestrictedModeType = "NODEATH"
)

type modeStatus struct {
	SessionID   uuid.UUID          `json:"sessionId"`
	CharacterID uuid.UUID          `json:"characterId"`
	Mode        RestrictedModeType `json:"mode"`
	State       ModeState          `json:"state"`
	StartedAt   time.Time          `json:"startedAt"`
	UpdatedAt   time.Time          `json:"updatedAt"`
	CompletedAt *time.Time         `json:"completedAt,omitempty"`
}

type modeLimits struct {
	Permadeath   bool             `json:"permadeath"`
	NoDeath      bool             `json:"noDeath"`
	MaxDeaths    int              `json:"maxDeaths"`
	ResourceCaps map[string]int64 `json:"resourceCaps"`
}

type store struct {
	mu       sync.RWMutex
	statuses map[uuid.UUID]modeStatus
	deaths   map[uuid.UUID]int
	limits   map[RestrictedModeType]modeLimits
	usage    map[string]int64
}

func newStore() *store {
	return &store{
		statuses: make(map[uuid.UUID]modeStatus),
		deaths:   make(map[uuid.UUID]int),
		limits: map[RestrictedModeType]modeLimits{
			ModeIronman:  {Permadeath: true, NoDeath: true, MaxDeaths: 0, ResourceCaps: map[string]int64{"HEALTH_POTION": 3, "MANA_POTION": 5, "AMMO": 500}},
			ModeHardcore: {Permadeath: false, NoDeath: false, MaxDeaths: 2, ResourceCaps: map[string]int64{"HEALTH_POTION": 5, "AMMO": 800}},
			ModeSolo:     {Permadeath: false, NoDeath: false, MaxDeaths: 1, ResourceCaps: map[string]int64{"HEALTH_POTION": 4, "AMMO": 600}},
			ModeNoDeath:  {Permadeath: false, NoDeath: true, MaxDeaths: 0, ResourceCaps: map[string]int64{"HEALTH_POTION": 6}},
		},
		usage: make(map[string]int64),
	}
}

func (s *store) setStatus(m modeStatus) modeStatus {
	s.mu.Lock()
	defer s.mu.Unlock()
	s.statuses[m.CharacterID] = m
	return m
}

func (s *store) getStatus(characterID uuid.UUID) (modeStatus, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()
	v, ok := s.statuses[characterID]
	return v, ok
}

func (s *store) incUsage(sessionID uuid.UUID, resource string, amount int64) (int64, map[string]int64) {
	s.mu.Lock()
	defer s.mu.Unlock()
	key := sessionID.String() + ":" + resource
	cur := s.usage[key]
	cur += amount
	s.usage[key] = cur
	return cur, s.usage
}

type server struct {
	log   *logrus.Logger
	r     *http.ServeMux
	h     http.Handler
	store *store
}

func newServer() *server {
	l := logrus.New()
	l.SetFormatter(&logrus.JSONFormatter{})

	s := &server{
		log:   l,
		r:     http.NewServeMux(),
		store: newStore(),
	}

	s.routes()
	s.h = recoveryMiddleware(timeoutMiddleware(s.r, 5*time.Second), s.log)
	return s
}

func (s *server) routes() {
	s.r.HandleFunc("/health", s.health)
	s.r.Handle("/metrics", promhttp.Handler())
	s.r.HandleFunc("/restricted-modes/select/", s.selectMode)
	s.r.HandleFunc("/restricted-modes/status/", s.status)
	s.r.HandleFunc("/restricted-modes/complete/", s.complete)
	s.r.HandleFunc("/restricted-modes/fail/", s.fail)
	s.r.HandleFunc("/restricted-modes/permadeath/", s.permadeath)
	s.r.HandleFunc("/restricted-modes/death/", s.registerDeath)
	s.r.HandleFunc("/restricted-modes/resource/", s.consumeResource)
}

func (s *server) health(w http.ResponseWriter, r *http.Request) {
	writeJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (s *server) selectMode(w http.ResponseWriter, r *http.Request) {
	segments := pathSegments(r.URL.Path)
	if len(segments) != 4 || segments[0] != "restricted-modes" || segments[1] != "select" {
		http.NotFound(w, r)
		return
	}
	modeStr := segments[2]
	charIDStr := segments[3]
	charID, err := uuid.Parse(charIDStr)
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
	mode := RestrictedModeType(modeStr)
	now := time.Now().UTC()
	ms := modeStatus{
		SessionID:   uuid.New(),
		CharacterID: charID,
		Mode:        mode,
		State:       ModeActive,
		StartedAt:   now,
		UpdatedAt:   now,
	}
	s.store.setStatus(ms)
	writeJSON(w, http.StatusOK, ms)
}

func (s *server) status(w http.ResponseWriter, r *http.Request) {
	segments := pathSegments(r.URL.Path)
	if len(segments) != 3 || segments[0] != "restricted-modes" || segments[1] != "status" {
		http.NotFound(w, r)
		return
	}
	charID, err := uuid.Parse(segments[2])
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
	if st, ok := s.store.getStatus(charID); ok {
		writeJSON(w, http.StatusOK, st)
		return
	}
	writeError(w, http.StatusNotFound, "not found")
}

func (s *server) complete(w http.ResponseWriter, r *http.Request) {
	charID, ok := parseCharacterIDWithPrefix(w, r, "complete")
	if !ok {
		return
	}
	s.updateState(w, charID, ModeCompleted)
}

func (s *server) fail(w http.ResponseWriter, r *http.Request) {
	charID, ok := parseCharacterIDWithPrefix(w, r, "fail")
	if !ok {
		return
	}
	s.updateState(w, charID, ModeFailed)
}

func (s *server) permadeath(w http.ResponseWriter, r *http.Request) {
	charID, ok := parseCharacterIDWithPrefix(w, r, "permadeath")
	if !ok {
		return
	}
	s.updateState(w, charID, ModeFailed)
}

func (s *server) registerDeath(w http.ResponseWriter, r *http.Request) {
	segments := pathSegments(r.URL.Path)
	if len(segments) != 3 || segments[0] != "restricted-modes" || segments[1] != "death" {
		http.NotFound(w, r)
		return
	}
	charID, err := uuid.Parse(segments[2])
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
	st, ok := s.store.getStatus(charID)
	if !ok {
		writeError(w, http.StatusNotFound, "not found")
		return
	}
	limits := s.store.limits[st.Mode]
	if limits.Permadeath || limits.NoDeath {
		st.State = ModeFailed
		st.CompletedAt = ptr(time.Now().UTC())
		st.UpdatedAt = *st.CompletedAt
		s.store.setStatus(st)
		writeJSON(w, http.StatusOK, st)
		return
	}
	s.store.deaths[charID]++
	if limits.MaxDeaths > 0 && s.store.deaths[charID] > limits.MaxDeaths {
		st.State = ModeFailed
		now := time.Now().UTC()
		st.CompletedAt = &now
		st.UpdatedAt = now
		s.store.setStatus(st)
		writeJSON(w, http.StatusOK, st)
		return
	}
	st.UpdatedAt = time.Now().UTC()
	s.store.setStatus(st)
	writeJSON(w, http.StatusOK, st)
}

func (s *server) consumeResource(w http.ResponseWriter, r *http.Request) {
	segments := pathSegments(r.URL.Path)
	if len(segments) != 5 || segments[0] != "restricted-modes" || segments[1] != "resource" {
		http.NotFound(w, r)
		return
	}
	charID, err := uuid.Parse(segments[2])
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
	resource := segments[3]
	amount, err := strconv.ParseInt(segments[4], 10, 64)
	if err != nil || amount <= 0 {
		writeError(w, http.StatusBadRequest, "invalid amount")
		return
	}
	st, ok := s.store.getStatus(charID)
	if !ok {
		writeError(w, http.StatusNotFound, "not found")
		return
	}
	limits := s.store.limits[st.Mode]
	capVal, hasCap := limits.ResourceCaps[resource]
	used, _ := s.store.incUsage(st.SessionID, resource, amount)
	if hasCap && used > capVal {
		st.State = ModeFailed
		now := time.Now().UTC()
		st.CompletedAt = &now
		st.UpdatedAt = now
		s.store.setStatus(st)
		writeJSON(w, http.StatusOK, st)
		return
	}
	st.UpdatedAt = time.Now().UTC()
	s.store.setStatus(st)
	writeJSON(w, http.StatusOK, st)
}

func (s *server) updateState(w http.ResponseWriter, charID uuid.UUID, state ModeState) {
	st, ok := s.store.getStatus(charID)
	if !ok {
		writeError(w, http.StatusNotFound, "not found")
		return
	}
	st.State = state
	now := time.Now().UTC()
	st.UpdatedAt = now
	if state == ModeFailed || state == ModeCompleted {
		st.CompletedAt = &now
	}
	s.store.setStatus(st)
	writeJSON(w, http.StatusOK, st)
}

func main() {
	s := newServer()

	addr := getEnv("ADDR", ":8084")
	httpSrv := &http.Server{
		Addr:         addr,
		Handler:      s.h,
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 5 * time.Second,
		IdleTimeout:  30 * time.Second,
	}

	go func() {
		s.log.WithField("addr", addr).Info("restricted-modes go service starting")
		if err := httpSrv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			s.log.WithError(err).Fatal("server failed")
		}
	}()

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)
	<-stop

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		s.log.WithError(err).Error("shutdown failed")
	} else {
		s.log.Info("shutdown complete")
	}
}

func ptr[T any](v T) *T { return &v }

func pathSegments(path string) []string {
	trimmed := strings.Trim(path, "/")
	if trimmed == "" {
		return []string{}
	}
	return strings.Split(trimmed, "/")
}

func parseCharacterIDWithPrefix(w http.ResponseWriter, r *http.Request, action string) (uuid.UUID, bool) {
	segments := pathSegments(r.URL.Path)
	if len(segments) != 3 || segments[0] != "restricted-modes" || segments[1] != action {
		http.NotFound(w, r)
		return uuid.Nil, false
	}
	charID, err := uuid.Parse(segments[2])
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return uuid.Nil, false
	}
	return charID, true
}

func recoveryMiddleware(next http.Handler, logger *logrus.Logger) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				logger.WithField("panic", rec).Error("recovered from panic")
				http.Error(w, "internal server error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

func timeoutMiddleware(next http.Handler, d time.Duration) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), d)
		defer cancel()
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func writeJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(data)
}

func writeError(w http.ResponseWriter, status int, msg string) {
	writeJSON(w, status, map[string]string{"error": msg})
}

func getEnv(key, def string) string {
	if v, ok := os.LookupEnv(key); ok {
		return v
	}
	return def
}

