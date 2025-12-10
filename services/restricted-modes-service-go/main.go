package main

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
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
	r     chi.Router
	store *store
}

func newServer() *server {
	l := logrus.New()
	l.SetFormatter(&logrus.JSONFormatter{})

	s := &server{
		log:   l,
		r:     chi.NewRouter(),
		store: newStore(),
	}

	s.r.Use(middleware.RequestID)
	s.r.Use(middleware.RealIP)
	s.r.Use(middleware.Recoverer)
	s.r.Use(middleware.Timeout(5 * time.Second))

	s.routes()
	return s
}

func (s *server) routes() {
	s.r.Get("/health", s.health)
	s.r.Get("/metrics", func(w http.ResponseWriter, r *http.Request) {
		promhttp.Handler().ServeHTTP(w, r)
	})
	s.r.Post("/restricted-modes/select/{mode}/{characterId}", s.selectMode)
	s.r.Get("/restricted-modes/status/{characterId}", s.status)
	s.r.Post("/restricted-modes/complete/{characterId}", s.complete)
	s.r.Post("/restricted-modes/fail/{characterId}", s.fail)
	s.r.Post("/restricted-modes/permadeath/{characterId}", s.permadeath)
	s.r.Post("/restricted-modes/death/{characterId}", s.registerDeath)
	s.r.Post("/restricted-modes/resource/{characterId}/{resource}/{amount}", s.consumeResource)
}

func (s *server) health(w http.ResponseWriter, r *http.Request) {
	writeJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (s *server) selectMode(w http.ResponseWriter, r *http.Request) {
	modeStr := chi.URLParam(r, "mode")
	charIDStr := chi.URLParam(r, "characterId")
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
	charID, err := uuid.Parse(chi.URLParam(r, "characterId"))
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
	s.updateState(w, r, ModeCompleted)
}

func (s *server) fail(w http.ResponseWriter, r *http.Request) {
	s.updateState(w, r, ModeFailed)
}

func (s *server) permadeath(w http.ResponseWriter, r *http.Request) {
	s.updateState(w, r, ModeFailed)
}

func (s *server) registerDeath(w http.ResponseWriter, r *http.Request) {
	charID, err := uuid.Parse(chi.URLParam(r, "characterId"))
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
	charID, err := uuid.Parse(chi.URLParam(r, "characterId"))
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
	resource := chi.URLParam(r, "resource")
	amountStr := chi.URLParam(r, "amount")
	var amount int64
	if _, err := fmt.Sscan(amountStr, &amount); err != nil || amount <= 0 {
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

func (s *server) updateState(w http.ResponseWriter, r *http.Request, state ModeState) {
	charID, err := uuid.Parse(chi.URLParam(r, "characterId"))
	if err != nil {
		writeError(w, http.StatusBadRequest, "invalid characterId")
		return
	}
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
		Handler:      s.r,
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

func getEnv(key, def string) string {
	if v, ok := os.LookupEnv(key); ok {
		return v
	}
	return def
}

