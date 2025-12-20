// Package server Issue: #1943
package server

import (
	"fmt"
	"sync"
	"time"
)

// CreateTournamentRequest Request/Response types for API
type CreateTournamentRequest struct {
	Name              string                 `json:"name"`
	Description       string                 `json:"description"`
	GuildID           string                 `json:"guild_id"`
	Type              string                 `json:"type"`
	StartTime         time.Time              `json:"start_time"`
	DurationMinutes   int                    `json:"duration_minutes"`
	MaxParticipants   int                    `json:"max_participants"`
	EntryFee          int                    `json:"entry_fee"`
	Rules             map[string]interface{} `json:"rules"`
	PrizeDistribution map[string]interface{} `json:"prize_distribution"`
}

type TournamentResponse struct {
	TournamentID        string    `json:"tournament_id"`
	Name                string    `json:"name"`
	Description         string    `json:"description"`
	GuildID             string    `json:"guild_id"`
	Type                string    `json:"type"`
	Status              string    `json:"status"`
	StartTime           time.Time `json:"start_time"`
	EndTime             time.Time `json:"end_time"`
	MaxParticipants     int       `json:"max_participants"`
	CurrentParticipants int       `json:"current_participants"`
	EntryFee            int       `json:"entry_fee"`
	PrizePool           int       `json:"prize_pool"`
}

type RegisterParticipantRequest struct {
	PlayerID string `json:"player_id"`
}

type MatchResponse struct {
	MatchID       string    `json:"match_id"`
	TournamentID  string    `json:"tournament_id"`
	Round         int       `json:"round"`
	Player1ID     string    `json:"player1_id"`
	Player2ID     string    `json:"player2_id"`
	WinnerID      string    `json:"winner_id,omitempty"`
	Status        string    `json:"status"`
	ScheduledTime time.Time `json:"scheduled_time"`
	StartTime     time.Time `json:"start_time,omitempty"`
	EndTime       time.Time `json:"end_time,omitempty"`
}

type TournamentParticipant struct {
	TournamentID string    `json:"tournament_id"`
	PlayerID     string    `json:"player_id"`
	RegisteredAt time.Time `json:"registered_at"`
	Status       string    `json:"status"`
	Score        int       `json:"score"`
	Rank         int       `json:"rank"`
}

type LeaderboardEntry struct {
	PlayerID string `json:"player_id"`
	Username string `json:"username"`
	Score    int    `json:"score"`
	Rank     int    `json:"rank"`
	Wins     int    `json:"wins"`
	Losses   int    `json:"losses"`
}

type Match struct {
	MatchID       string
	TournamentID  string
	Round         int
	Player1ID     string
	Player2ID     string
	WinnerID      string
	Status        string
	ScheduledTime time.Time
	StartTime     time.Time
	EndTime       time.Time
}

// TournamentCache implements 3-tier caching
type TournamentCache struct {
	redisClient *redis.Client
	repo        *TournamentRepository
	memoryCache sync.Map
}

func (c *TournamentCache) GetTournament(tournamentID string) (*TournamentResponse, error) {
	// L1: Check memory cache
	if cached, ok := c.memoryCache.Load(tournamentID); ok {
		if response, ok := cached.(*TournamentResponse); ok {
			return response, nil
		}
	}

	return nil, fmt.Errorf("tournament not in cache")
}

func (c *TournamentCache) storeInMemoryTournament(tournamentID string, response *TournamentResponse) {
	c.memoryCache.Store(tournamentID, response)
}

func (c *TournamentCache) InvalidateTournament(tournamentID string) {
	c.memoryCache.Delete(tournamentID)
}

func (c *TournamentCache) InvalidateTournamentList() {
	// Clear all tournament list caches from memory
	c.memoryCache.Range(func(key, value interface{}) bool {
		if keyStr, ok := key.(string); ok && len(keyStr) > 12 && keyStr[:12] == "tournaments:" {
			c.memoryCache.Delete(key)
		}
		return true
	})
}
