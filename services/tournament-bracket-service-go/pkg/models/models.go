// Issue: #2210 - Tournament Bracket System Schema
// Models for Tournament Bracket Service - Competitive gameplay system

package models

import (
	"time"
)

// Tournament represents a competitive tournament
type Tournament struct {
	ID                   string                 `json:"id" db:"id"`
	Name                 string                 `json:"name" db:"name"`
	Description          string                 `json:"description" db:"description"`
	GameMode             string                 `json:"game_mode" db:"game_mode"`
	TournamentType       string                 `json:"tournament_type" db:"tournament_type"` // "single_elimination", "double_elimination", "round_robin", "swiss"
	MaxParticipants      int                    `json:"max_participants" db:"max_participants"`
	CurrentParticipants  int                    `json:"current_participants" db:"current_participants"`
	MinSkillLevel        int                    `json:"min_skill_level" db:"min_skill_level"`
	MaxSkillLevel        int                    `json:"max_skill_level" db:"max_skill_level"`
	EntryFee             int                    `json:"entry_fee" db:"entry_fee"`
	PrizePool            map[string]interface{} `json:"prize_pool" db:"prize_pool"` // JSON
	Status               string                 `json:"status" db:"status"`          // "registration", "in_progress", "completed", "cancelled"
	RegistrationStart    *time.Time             `json:"registration_start" db:"registration_start"`
	RegistrationEnd      *time.Time             `json:"registration_end" db:"registration_end"`
	StartTime            *time.Time             `json:"start_time" db:"start_time"`
	EndTime              *time.Time             `json:"end_time" db:"end_time"`
	Rules                map[string]interface{} `json:"rules" db:"rules"`       // JSON
	Metadata             map[string]interface{} `json:"metadata" db:"metadata"` // JSON
	CreatedAt            time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt            time.Time              `json:"updated_at" db:"updated_at"`
}

// Participant represents a tournament participant
type Participant struct {
	ID               string                 `json:"id" db:"id"`
	TournamentID     string                 `json:"tournament_id" db:"tournament_id"`
	PlayerID         string                 `json:"player_id" db:"player_id"`
	PlayerName       string                 `json:"player_name" db:"player_name"`
	SkillRating      int                    `json:"skill_rating" db:"skill_rating"`
	RegistrationTime time.Time              `json:"registration_time" db:"registration_time"`
	Status           string                 `json:"status" db:"status"` // "registered", "confirmed", "disqualified", "withdrawn"
	Seed             *int                   `json:"seed" db:"seed"`
	Division         string                 `json:"division" db:"division"`
	Metadata         map[string]interface{} `json:"metadata" db:"metadata"` // JSON
}

// Bracket represents a tournament bracket (winners, losers, etc.)
type Bracket struct {
	ID           string                 `json:"id" db:"id"`
	TournamentID string                 `json:"tournament_id" db:"tournament_id"`
	BracketName  string                 `json:"bracket_name" db:"bracket_name"`
	RoundNumber  int                    `json:"round_number" db:"round_number"`
	RoundName    string                 `json:"round_name" db:"round_name"`
	Status       string                 `json:"status" db:"status"` // "pending", "in_progress", "completed"
	Metadata     map[string]interface{} `json:"metadata" db:"metadata"` // JSON
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
}

// Match represents a tournament match
type Match struct {
	ID             string                 `json:"id" db:"id"`
	TournamentID   string                 `json:"tournament_id" db:"tournament_id"`
	BracketID      string                 `json:"bracket_id" db:"bracket_id"`
	MatchNumber    int                    `json:"match_number" db:"match_number"`
	Status         string                 `json:"status" db:"status"` // "scheduled", "in_progress", "completed", "cancelled", "forfeit"
	ScheduledTime  *time.Time             `json:"scheduled_time" db:"scheduled_time"`
	StartTime      *time.Time             `json:"start_time" db:"start_time"`
	EndTime        *time.Time             `json:"end_time" db:"end_time"`
	Duration       *time.Duration         `json:"duration" db:"duration"`
	WinnerID       string                 `json:"winner_id" db:"winner_id"`
	WinnerScore    int                    `json:"winner_score" db:"winner_score"`
	LoserID        string                 `json:"loser_id" db:"loser_id"`
	LoserScore     int                    `json:"loser_score" db:"loser_score"`
	MapName        string                 `json:"map_name" db:"map_name"`
	GameMode       string                 `json:"game_mode" db:"game_mode"`
	ServerID       string                 `json:"server_id" db:"server_id"`
	SpectatorCount int                    `json:"spectator_count" db:"spectator_count"`
	ReplayAvailable bool                   `json:"replay_available" db:"replay_available"`
	ReplayURL      string                 `json:"replay_url" db:"replay_url"`
	Statistics     map[string]interface{} `json:"statistics" db:"statistics"` // JSON
	Events         []interface{}          `json:"events" db:"events"`         // JSON array
	Metadata       map[string]interface{} `json:"metadata" db:"metadata"`     // JSON
	CreatedAt      time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time              `json:"updated_at" db:"updated_at"`
}

// MatchParticipant represents a participant in a specific match
type MatchParticipant struct {
	ID            string                 `json:"id" db:"id"`
	MatchID       string                 `json:"match_id" db:"match_id"`
	ParticipantID string                 `json:"participant_id" db:"participant_id"`
	Team          string                 `json:"team" db:"team"`
	PlayerSlot    int                    `json:"player_slot" db:"player_slot"`
	Status        string                 `json:"status" db:"status"` // "confirmed", "ready", "playing", "disconnected", "forfeit"
	JoinedAt      *time.Time             `json:"joined_at" db:"joined_at"`
	LeftAt        *time.Time             `json:"left_at" db:"left_at"`
	Score         int                    `json:"score" db:"score"`
	Kills         int                    `json:"kills" db:"kills"`
	Deaths        int                    `json:"deaths" db:"deaths"`
	Assists       int                    `json:"assists" db:"assists"`
	Statistics    map[string]interface{} `json:"statistics" db:"statistics"` // JSON
}

// Spectator represents a spectator watching a match
type Spectator struct {
	ID             string                 `json:"id" db:"id"`
	MatchID        string                 `json:"match_id" db:"match_id"`
	SpectatorID    string                 `json:"spectator_id" db:"spectator_id"`
	JoinedAt       time.Time              `json:"joined_at" db:"joined_at"`
	LeftAt         *time.Time             `json:"left_at" db:"left_at"`
	SessionDuration *time.Duration         `json:"session_duration" db:"session_duration"`
	Metadata       map[string]interface{} `json:"metadata" db:"metadata"` // JSON
}

// TournamentResult represents final tournament results for a participant
type TournamentResult struct {
	ID                 string                 `json:"id" db:"id"`
	TournamentID       string                 `json:"tournament_id" db:"tournament_id"`
	ParticipantID      string                 `json:"participant_id" db:"participant_id"`
	FinalPosition      int                    `json:"final_position" db:"final_position"`
	TotalScore         int                    `json:"total_score" db:"total_score"`
	TotalKills         int                    `json:"total_kills" db:"total_kills"`
	TotalDeaths        int                    `json:"total_deaths" db:"total_deaths"`
	TotalAssists       int                    `json:"total_assists" db:"total_assists"`
	MatchesPlayed      int                    `json:"matches_played" db:"matches_played"`
	MatchesWon         int                    `json:"matches_won" db:"matches_won"`
	MatchesLost        int                    `json:"matches_lost" db:"matches_lost"`
	AverageMatchDuration *time.Duration       `json:"average_match_duration" db:"average_match_duration"`
	SkillRatingChange  int                    `json:"skill_rating_change" db:"skill_rating_change"`
	Rewards            map[string]interface{} `json:"rewards" db:"rewards"`         // JSON
	Achievements       []interface{}          `json:"achievements" db:"achievements"` // JSON array
	Statistics         map[string]interface{} `json:"statistics" db:"statistics"`     // JSON
	CreatedAt          time.Time              `json:"created_at" db:"created_at"`
}

// TournamentAnalytics provides analytics for tournament performance
type TournamentAnalytics struct {
	TournamentID       string             `json:"tournament_id"`
	TotalParticipants  int64              `json:"total_participants"`
	TotalMatches       int64              `json:"total_matches"`
	AverageMatchDuration time.Duration     `json:"average_match_duration"`
	CompletionRate     float64            `json:"completion_rate"`      // 0-1
	PopularGameModes   map[string]int64   `json:"popular_game_modes"`
	SpectatorStats     SpectatorStats     `json:"spectator_stats"`
	SkillRatingChanges map[string]float64 `json:"skill_rating_changes"` // distribution of rating changes
	RevenueGenerated   int64              `json:"revenue_generated"`
	IssuesReported     int64              `json:"issues_reported"`
	LastUpdated        time.Time          `json:"last_updated"`
}

// SpectatorStats contains spectator analytics
type SpectatorStats struct {
	TotalSpectators    int64         `json:"total_spectators"`
	AverageSessionTime time.Duration `json:"average_session_time"`
	PeakConcurrent     int64         `json:"peak_concurrent"`
	RetentionRate      float64       `json:"retention_rate"` // percentage of spectators who watch multiple matches
}

// BracketGenerationRequest represents a request to generate tournament brackets
type BracketGenerationRequest struct {
	TournamentID   string `json:"tournament_id"`
	GenerationType string `json:"generation_type"` // "single_elimination", "double_elimination", "round_robin"
	SeedingMethod  string `json:"seeding_method"`  // "skill_rating", "random", "manual"
	ByesAllowed    bool   `json:"byes_allowed"`
}

// BracketGenerationResponse contains the generated bracket structure
type BracketGenerationResponse struct {
	TournamentID string                `json:"tournament_id"`
	Brackets     []GeneratedBracket    `json:"brackets"`
	TotalRounds  int                   `json:"total_rounds"`
	TotalMatches int                   `json:"total_matches"`
	EstimatedDuration time.Duration     `json:"estimated_duration"`
}

// GeneratedBracket represents a generated bracket structure
type GeneratedBracket struct {
	Name     string            `json:"name"`
	Rounds   []GeneratedRound  `json:"rounds"`
	Metadata map[string]interface{} `json:"metadata"`
}

// GeneratedRound represents a round in the bracket
type GeneratedRound struct {
	RoundNumber int               `json:"round_number"`
	RoundName   string            `json:"round_name"`
	Matches     []GeneratedMatch  `json:"matches"`
}

// GeneratedMatch represents a match in the bracket
type GeneratedMatch struct {
	MatchNumber     int      `json:"match_number"`
	Participant1ID  string   `json:"participant_1_id,omitempty"`
	Participant2ID  string   `json:"participant_2_id,omitempty"`
	IsBye          bool     `json:"is_bye"`
	EstimatedTime  *time.Time `json:"estimated_time"`
}

// MatchStartRequest represents a request to start a match
type MatchStartRequest struct {
	MatchID    string                 `json:"match_id"`
	ServerID   string                 `json:"server_id"`
	MapName    string                 `json:"map_name"`
	GameMode   string                 `json:"game_mode"`
	Metadata   map[string]interface{} `json:"metadata"`
}

// MatchUpdateRequest represents a match status update
type MatchUpdateRequest struct {
	MatchID      string                 `json:"match_id"`
	Status       string                 `json:"status"`
	ScoreUpdates []ScoreUpdate          `json:"score_updates,omitempty"`
	Event        *MatchEvent            `json:"event,omitempty"`
	Metadata     map[string]interface{} `json:"metadata,omitempty"`
}

// ScoreUpdate represents a score change in a match
type ScoreUpdate struct {
	ParticipantID string `json:"participant_id"`
	Score         int    `json:"score"`
	Kills         int    `json:"kills"`
	Deaths        int    `json:"deaths"`
	Assists       int    `json:"assists"`
}

// MatchEvent represents a significant event during a match
type MatchEvent struct {
	EventType   string                 `json:"event_type"`   // "kill", "objective", "special"
	Description string                 `json:"description"`
	Timestamp   time.Time              `json:"timestamp"`
	Data        map[string]interface{} `json:"data"`
}

// TournamentSpectatorRequest represents a spectator joining a tournament match
type TournamentSpectatorRequest struct {
	MatchID     string `json:"match_id"`
	SpectatorID string `json:"spectator_id"`
	StreamType  string `json:"stream_type"` // "live", "replay", "highlights"
}

// TournamentSpectatorResponse contains spectator access information
type TournamentSpectatorResponse struct {
	SessionID   string `json:"session_id"`
	StreamURL   string `json:"stream_url"`
	AccessToken string `json:"access_token"`
	ExpiresAt   time.Time `json:"expires_at"`
}

// TournamentRegistrationRequest represents a player registering for a tournament
type TournamentRegistrationRequest struct {
	TournamentID string `json:"tournament_id"`
	PlayerID     string `json:"player_id"`
	PlayerName   string `json:"player_name"`
}

// TournamentRegistrationResponse contains registration confirmation
type TournamentRegistrationResponse struct {
	RegistrationID string `json:"registration_id"`
	Status         string `json:"status"`
	QueuePosition  int    `json:"queue_position,omitempty"`
	EstimatedStart *time.Time `json:"estimated_start,omitempty"`
}

// TournamentLeaderboard represents tournament ranking
type TournamentLeaderboard struct {
	TournamentID string              `json:"tournament_id"`
	Rankings     []LeaderboardEntry  `json:"rankings"`
	LastUpdated  time.Time           `json:"last_updated"`
}

// LeaderboardEntry represents a player's position in tournament
type LeaderboardEntry struct {
	Rank            int    `json:"rank"`
	ParticipantID   string `json:"participant_id"`
	PlayerName      string `json:"player_name"`
	Score           int    `json:"score"`
	Wins            int    `json:"wins"`
	Losses          int    `json:"losses"`
	WinRate         float64 `json:"win_rate"`
	SkillRating     int    `json:"skill_rating"`
	Change          int    `json:"change"` // rating change
}
