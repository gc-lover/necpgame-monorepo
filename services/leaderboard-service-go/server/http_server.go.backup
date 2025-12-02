package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/go-chi/chi/v5"
	"github.com/necpgame/leaderboard-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            chi.Router
	leaderboardService LeaderboardService
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, leaderboardService LeaderboardService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		leaderboardService: leaderboardService,
		logger:            GetLogger(),
		jwtValidator:      jwtValidator,
		authEnabled:       authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.Get("/leaderboards/global", server.getGlobalLeaderboard)
	api.Get("/leaderboards/seasonal", server.getSeasonalLeaderboard)
	api.Get("/leaderboards/class", server.getClassLeaderboard)
	api.Get("/leaderboards/friends", server.getFriendsLeaderboard)
	api.Get("/leaderboards/guild", server.getGuildLeaderboard)

	api.Get("/leaderboards/rank", server.getPlayerRank)
	api.Get("/leaderboards/rank/neighbors", server.getRankNeighbors)

	api.Get("/world/leaderboards", server.listLeaderboards)
	api.Get("/world/leaderboards/{leaderboardId}", server.getLeaderboard)
	api.Get("/world/leaderboards/{leaderboardId}/top", server.getLeaderboardTop)
	api.Get("/world/leaderboards/{leaderboardId}/rank/{playerId}", server.getLeaderboardPlayerRank)
	api.Get("/world/leaderboards/{leaderboardId}/around/{playerId}", server.getLeaderboardRankAround)

	router.Get("/health", server.healthCheck)

	return server
}


