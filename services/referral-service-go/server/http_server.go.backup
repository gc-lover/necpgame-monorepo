package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/go-chi/chi/v5"
	"github.com/necpgame/referral-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          chi.Router
	referralService ReferralService
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, referralService ReferralService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		referralService: referralService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/growth/referral").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.Get("/code", server.getReferralCode)
	api.Get("/code/generate", server.generateReferralCode)
	api.Get("/code/{code}/validate", server.validateReferralCode)

	api.Get("/register", server.registerWithCode)
	api.Get("/status/{player_id}", server.getReferralStatus)

	api.Get("/milestones/{player_id}", server.getMilestones)
	api.Get("/milestones/{milestone_id}/claim", server.claimMilestoneReward)

	api.Get("/rewards/distribute", server.distributeRewards)
	api.Get("/rewards/history/{player_id}", server.getRewardHistory)

	api.Get("/stats/{player_id}", server.getReferralStats)
	api.Get("/stats/public/{code}", server.getPublicReferralStats)

	api.Get("/leaderboard", server.getLeaderboard)
	api.Get("/leaderboard/{player_id}/position", server.getLeaderboardPosition)

	api.Get("/events/{player_id}", server.getEvents)

	router.Get("/health", server.healthCheck)

	return server
}


