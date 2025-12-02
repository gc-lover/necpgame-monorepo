package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/necpgame/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            chi.Router
	worldService      WorldService
	worldEventsService WorldEventsServiceInterface
	worldStateService  WorldStateServiceInterface
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, worldService WorldService, worldEventsService WorldEventsServiceInterface, worldStateService WorldStateServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		worldService:      worldService,
		worldEventsService: worldEventsService,
		worldStateService:  worldStateService,
		logger:            GetLogger(),
		jwtValidator:      jwtValidator,
		authEnabled:       authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	worldAPI := router.PathPrefix("/api/v1/world").Subrouter()

	if authEnabled {
		worldAPI.Use(server.authMiddleware)
	}

	worldAPI.Get("/reset/daily/execute", server.executeDailyReset)
	worldAPI.Get("/reset/daily/status", server.getDailyResetStatus)
	worldAPI.Get("/reset/daily/next", server.getNextDailyReset)
	worldAPI.Get("/reset/weekly/execute", server.executeWeeklyReset)
	worldAPI.Get("/reset/weekly/status", server.getWeeklyResetStatus)
	worldAPI.Get("/reset/weekly/next", server.getNextWeeklyReset)

	worldAPI.Get("/reset/quests/pool", server.getQuestPool)
	worldAPI.Get("/reset/quests/assign", server.assignQuestToPlayer)
	worldAPI.Get("/reset/quests/player/{player_id}", server.getPlayerQuests)

	worldAPI.Get("/reset/rewards/login/{player_id}", server.getPlayerLoginRewards)
	worldAPI.Get("/reset/rewards/login/claim", server.claimLoginReward)
	worldAPI.Get("/reset/rewards/login/streak/{player_id}", server.getPlayerLoginStreak)

	worldAPI.Get("/reset/schedule", server.getResetSchedule)
	worldAPI.Get("/reset/schedule/update", server.updateResetSchedule)

	worldAPI.Get("/reset/execute/daily", server.executeDailyResetBatch)
	worldAPI.Get("/reset/execute/weekly", server.executeWeeklyResetBatch)
	worldAPI.Get("/reset/execute/status", server.getResetExecutionStatus)
	worldAPI.Get("/reset/events", server.getResetEvents)

	worldAPI.Get("/travel-events/trigger", server.triggerTravelEvent)
	worldAPI.Get("/travel-events/available", server.getAvailableTravelEvents)
	worldAPI.Get("/travel-events/{eventId}", server.getTravelEvent)
	worldAPI.Get("/travel-events/{eventId}/start", server.startTravelEvent)
	worldAPI.Get("/travel-events/{eventId}/skill-check", server.performTravelEventSkillCheck)
	worldAPI.Get("/travel-events/{eventId}/complete", server.completeTravelEvent)
	worldAPI.Get("/travel-events/{eventId}/cancel", server.cancelTravelEvent)
	worldAPI.Get("/travel-events/epoch/{epochId}", server.getEpochTravelEvents)
	worldAPI.Get("/travel-events/cooldown/{characterId}", server.getCharacterTravelEventCooldowns)
	worldAPI.Get("/travel-events/probability", server.calculateTravelEventProbability)
	worldAPI.Get("/travel-events/rewards/{eventId}", server.getTravelEventRewards)
	worldAPI.Get("/travel-events/penalties/{eventId}", server.getTravelEventPenalties)

	// World State endpoints
	worldAPI.Get("/state/{key}", server.getStateByKey)
	worldAPI.Get("/state/{key}", server.updateState)
	worldAPI.Get("/state/{key}", server.deleteState)
	worldAPI.Get("/state/category/{category}", server.getStateByCategory)
	worldAPI.Get("/state/batch", server.batchUpdateState)

	worldEventsAPI := router.PathPrefix("/api/v1").Subrouter()
	if authEnabled {
		worldEventsAPI.Use(server.authMiddleware)
	}
	worldEventsHandlers := NewWorldEventsHandlers(worldEventsService)
	worldEventsHandler := api.HandlerFromMux(worldEventsHandlers, worldEventsAPI)
	router.PathPrefix("/api/v1").Handler(worldEventsHandler)

	router.Get("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}


func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}



