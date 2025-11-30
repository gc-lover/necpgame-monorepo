package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/necpgame/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            *mux.Router
	worldService      WorldService
	worldEventsService WorldEventsServiceInterface
	worldStateService  WorldStateServiceInterface
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, worldService WorldService, worldEventsService WorldEventsServiceInterface, worldStateService WorldStateServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
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

	worldAPI.HandleFunc("/reset/daily/execute", server.executeDailyReset).Methods("POST")
	worldAPI.HandleFunc("/reset/daily/status", server.getDailyResetStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/daily/next", server.getNextDailyReset).Methods("GET")
	worldAPI.HandleFunc("/reset/weekly/execute", server.executeWeeklyReset).Methods("POST")
	worldAPI.HandleFunc("/reset/weekly/status", server.getWeeklyResetStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/weekly/next", server.getNextWeeklyReset).Methods("GET")

	worldAPI.HandleFunc("/reset/quests/pool", server.getQuestPool).Methods("GET")
	worldAPI.HandleFunc("/reset/quests/assign", server.assignQuestToPlayer).Methods("POST")
	worldAPI.HandleFunc("/reset/quests/player/{player_id}", server.getPlayerQuests).Methods("GET")

	worldAPI.HandleFunc("/reset/rewards/login/{player_id}", server.getPlayerLoginRewards).Methods("GET")
	worldAPI.HandleFunc("/reset/rewards/login/claim", server.claimLoginReward).Methods("POST")
	worldAPI.HandleFunc("/reset/rewards/login/streak/{player_id}", server.getPlayerLoginStreak).Methods("GET")

	worldAPI.HandleFunc("/reset/schedule", server.getResetSchedule).Methods("GET")
	worldAPI.HandleFunc("/reset/schedule/update", server.updateResetSchedule).Methods("POST")

	worldAPI.HandleFunc("/reset/execute/daily", server.executeDailyResetBatch).Methods("POST")
	worldAPI.HandleFunc("/reset/execute/weekly", server.executeWeeklyResetBatch).Methods("POST")
	worldAPI.HandleFunc("/reset/execute/status", server.getResetExecutionStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/events", server.getResetEvents).Methods("GET")

	worldAPI.HandleFunc("/travel-events/trigger", server.triggerTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/available", server.getAvailableTravelEvents).Methods("GET")
	worldAPI.HandleFunc("/travel-events/{eventId}", server.getTravelEvent).Methods("GET")
	worldAPI.HandleFunc("/travel-events/{eventId}/start", server.startTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/skill-check", server.performTravelEventSkillCheck).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/complete", server.completeTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/cancel", server.cancelTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/epoch/{epochId}", server.getEpochTravelEvents).Methods("GET")
	worldAPI.HandleFunc("/travel-events/cooldown/{characterId}", server.getCharacterTravelEventCooldowns).Methods("GET")
	worldAPI.HandleFunc("/travel-events/probability", server.calculateTravelEventProbability).Methods("GET")
	worldAPI.HandleFunc("/travel-events/rewards/{eventId}", server.getTravelEventRewards).Methods("GET")
	worldAPI.HandleFunc("/travel-events/penalties/{eventId}", server.getTravelEventPenalties).Methods("GET")

	// World State endpoints
	worldAPI.HandleFunc("/state/{key}", server.getStateByKey).Methods("GET")
	worldAPI.HandleFunc("/state/{key}", server.updateState).Methods("PUT")
	worldAPI.HandleFunc("/state/{key}", server.deleteState).Methods("DELETE")
	worldAPI.HandleFunc("/state/category/{category}", server.getStateByCategory).Methods("GET")
	worldAPI.HandleFunc("/state/batch", server.batchUpdateState).Methods("POST")

	worldEventsAPI := router.PathPrefix("/api/v1").Subrouter()
	if authEnabled {
		worldEventsAPI.Use(server.authMiddleware)
	}
	worldEventsHandlers := NewWorldEventsHandlers(worldEventsService)
	worldEventsHandler := api.HandlerFromMux(worldEventsHandlers, worldEventsAPI)
	router.PathPrefix("/api/v1").Handler(worldEventsHandler)

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

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


