// Tournament Bracket Domain Models
// Issue: #2210
// Agent: Backend Agent
package models

import (
	"time"

	"github.com/google/uuid"
)

// BracketType represents different tournament bracket formats
type BracketType string

const (
	BracketTypeSingleElimination BracketType = "single_elimination"
	BracketTypeDoubleElimination BracketType = "double_elimination"
	BracketTypeRoundRobin        BracketType = "round_robin"
	BracketTypeSwiss             BracketType = "swiss"
	BracketTypeLadder            BracketType = "ladder"
)

// BracketStatus represents bracket lifecycle status
type BracketStatus string

const (
	BracketStatusPending     BracketStatus = "pending"
	BracketStatusActive      BracketStatus = "active"
	BracketStatusCompleted   BracketStatus = "completed"
	BracketStatusCancelled   BracketStatus = "cancelled"
)

// RoundType represents different round classifications
type RoundType string

const (
	RoundTypeElimination   RoundType = "elimination"
	RoundTypeQualification RoundType = "qualification"
	RoundTypeFinal         RoundType = "final"
)

// RoundStatus represents round execution status
type RoundStatus string

const (
	RoundStatusPending   RoundStatus = "pending"
	RoundStatusActive    RoundStatus = "active"
	RoundStatusCompleted RoundStatus = "completed"
)

// MatchStatus represents individual match status
type MatchStatus string

const (
	MatchStatusPending    MatchStatus = "pending"
	MatchStatusScheduled  MatchStatus = "scheduled"
	MatchStatusInProgress MatchStatus = "in_progress"
	MatchStatusCompleted  MatchStatus = "completed"
	MatchStatusCancelled  MatchStatus = "cancelled"
	MatchStatusBye        MatchStatus = "bye"
)

// ParticipantStatus represents participant status in tournament
type ParticipantStatus string

const (
	ParticipantStatusActive       ParticipantStatus = "active"
	ParticipantStatusEliminated   ParticipantStatus = "eliminated"
	ParticipantStatusWinner       ParticipantStatus = "winner"
	ParticipantStatusForfeit      ParticipantStatus = "forfeit"
	ParticipantStatusDisqualified ParticipantStatus = "disqualified"
	ParticipantStatusBye          ParticipantStatus = "bye"
)

// ParticipantType represents type of tournament participant
type ParticipantType string

const (
	ParticipantTypePlayer       ParticipantType = "player"
	ParticipantTypeTeam         ParticipantType = "team"
	ParticipantTypeRegistration ParticipantType = "registration"
)

// Bracket represents a tournament bracket configuration
type Bracket struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	TournamentID    string                 `json:"tournament_id" db:"tournament_id"`
	Name            string                 `json:"name" db:"name"`
	Description     *string                `json:"description,omitempty" db:"description"`
	BracketType     BracketType            `json:"bracket_type" db:"bracket_type"`
	MaxParticipants int                    `json:"max_participants" db:"max_participants"`
	CurrentRound    int                    `json:"current_round" db:"current_round"`
	TotalRounds     *int                   `json:"total_rounds,omitempty" db:"total_rounds"`
	Status          BracketStatus          `json:"status" db:"status"`
	StartDate       *time.Time             `json:"start_date,omitempty" db:"start_date"`
	EndDate         *time.Time             `json:"end_date,omitempty" db:"end_date"`
	WinnerID        *string                `json:"winner_id,omitempty" db:"winner_id"`
	WinnerName      *string                `json:"winner_name,omitempty" db:"winner_name"`
	PrizePool       map[string]interface{} `json:"prize_pool,omitempty" db:"prize_pool"`
	Rules           map[string]interface{} `json:"rules,omitempty" db:"rules"`
	Metadata        map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// BracketRound represents a round within a bracket
type BracketRound struct {
	ID            uuid.UUID              `json:"id" db:"id"`
	BracketID     uuid.UUID              `json:"bracket_id" db:"bracket_id"`
	RoundNumber   int                    `json:"round_number" db:"round_number"`
	RoundName     *string                `json:"round_name,omitempty" db:"round_name"`
	RoundType     RoundType              `json:"round_type" db:"round_type"`
	Status        RoundStatus            `json:"status" db:"status"`
	StartDate     *time.Time             `json:"start_date,omitempty" db:"start_date"`
	EndDate       *time.Time             `json:"end_date,omitempty" db:"end_date"`
	TotalMatches  int                    `json:"total_matches" db:"total_matches"`
	CompletedMatches int                 `json:"completed_matches" db:"completed_matches"`
	ByeCount      int                    `json:"bye_count" db:"bye_count"`
	Metadata      map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`
}

// BracketMatch represents an individual match in a bracket round
type BracketMatch struct {
	ID                  uuid.UUID              `json:"id" db:"id"`
	BracketID           uuid.UUID              `json:"bracket_id" db:"bracket_id"`
	RoundID             uuid.UUID              `json:"round_id" db:"round_id"`
	MatchNumber         int                    `json:"match_number" db:"match_number"`
	Participant1ID      *string                `json:"participant1_id,omitempty" db:"participant1_id"`
	Participant1Name    *string                `json:"participant1_name,omitempty" db:"participant1_name"`
	Participant1Seed    *int                   `json:"participant1_seed,omitempty" db:"participant1_seed"`
	Participant1Score   int                    `json:"participant1_score" db:"participant1_score"`
	Participant1Status  string                 `json:"participant1_status" db:"participant1_status"`
	Participant2ID      *string                `json:"participant2_id,omitempty" db:"participant2_id"`
	Participant2Name    *string                `json:"participant2_name,omitempty" db:"participant2_name"`
	Participant2Seed    *int                   `json:"participant2_seed,omitempty" db:"participant2_seed"`
	Participant2Score   int                    `json:"participant2_score" db:"participant2_score"`
	Participant2Status  string                 `json:"participant2_status" db:"participant2_status"`
	WinnerID            *string                `json:"winner_id,omitempty" db:"winner_id"`
	WinnerName          *string                `json:"winner_name,omitempty" db:"winner_name"`
	LoserID             *string                `json:"loser_id,omitempty" db:"loser_id"`
	LoserName           *string                `json:"loser_name,omitempty" db:"loser_name"`
	Status              MatchStatus            `json:"status" db:"status"`
	ScheduledStart      *time.Time             `json:"scheduled_start,omitempty" db:"scheduled_start"`
	ActualStart         *time.Time             `json:"actual_start,omitempty" db:"actual_start"`
	CompletedAt         *time.Time             `json:"completed_at,omitempty" db:"completed_at"`
	Duration            time.Duration         `json:"duration" db:"duration"`
	MapName             *string                `json:"map_name,omitempty" db:"map_name"`
	GameMode            *string                `json:"game_mode,omitempty" db:"game_mode"`
	SpectatorCount      int                    `json:"spectator_count" db:"spectator_count"`
	StreamURL           *string                `json:"stream_url,omitempty" db:"stream_url"`
	ReplayURL           *string                `json:"replay_url,omitempty" db:"replay_url"`
	ScoreDetails        map[string]interface{} `json:"score_details,omitempty" db:"score_details"`
	MatchStats          map[string]interface{} `json:"match_stats,omitempty" db:"match_stats"`
	Metadata            map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt           time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt           time.Time              `json:"updated_at" db:"updated_at"`
}

// BracketParticipant represents a participant in a tournament bracket
type BracketParticipant struct {
	ID                uuid.UUID              `json:"id" db:"id"`
	BracketID         uuid.UUID              `json:"bracket_id" db:"bracket_id"`
	ParticipantID     string                 `json:"participant_id" db:"participant_id"`
	ParticipantName   string                 `json:"participant_name" db:"participant_name"`
	ParticipantType   ParticipantType        `json:"participant_type" db:"participant_type"`
	SeedNumber        *int                   `json:"seed_number,omitempty" db:"seed_number"`
	CurrentRound      int                    `json:"current_round" db:"current_round"`
	Status            ParticipantStatus      `json:"status" db:"status"`
	JoinedAt          time.Time              `json:"joined_at" db:"joined_at"`
	EliminatedAt      *time.Time             `json:"eliminated_at,omitempty" db:"eliminated_at"`
	EliminatedRound   *int                   `json:"eliminated_round,omitempty" db:"eliminated_round"`
	FinalRank         *int                   `json:"final_rank,omitempty" db:"final_rank"`
	TotalScore        int                    `json:"total_score" db:"total_score"`
	TotalWins         int                    `json:"total_wins" db:"total_wins"`
	TotalLosses       int                    `json:"total_losses" db:"total_losses"`
	TotalDraws        int                    `json:"total_draws" db:"total_draws"`
	AverageScore      float64                `json:"average_score" db:"average_score"`
	PerformanceStats  map[string]interface{} `json:"performance_stats,omitempty" db:"performance_stats"`
	Metadata          map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt         time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time              `json:"updated_at" db:"updated_at"`
}

// CreateBracketRequest represents request to create a new bracket
type CreateBracketRequest struct {
	TournamentID    string                 `json:"tournament_id"`
	Name            string                 `json:"name"`
	Description     *string                `json:"description,omitempty"`
	BracketType     BracketType            `json:"bracket_type"`
	MaxParticipants int                    `json:"max_participants"`
	PrizePool       map[string]interface{} `json:"prize_pool,omitempty"`
	Rules           map[string]interface{} `json:"rules,omitempty"`
	StartDate       *time.Time             `json:"start_date,omitempty"`
}

// UpdateBracketRequest represents request to update a bracket
type UpdateBracketRequest struct {
	Name        *string                `json:"name,omitempty"`
	Description *string                `json:"description,omitempty"`
	Status      *BracketStatus         `json:"status,omitempty"`
	StartDate   *time.Time             `json:"start_date,omitempty"`
	EndDate     *time.Time             `json:"end_date,omitempty"`
	PrizePool   map[string]interface{} `json:"prize_pool,omitempty"`
	Rules       map[string]interface{} `json:"rules,omitempty"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// CreateMatchRequest represents request to create a new match
type CreateMatchRequest struct {
	BracketID         uuid.UUID  `json:"bracket_id"`
	RoundID           uuid.UUID  `json:"round_id"`
	MatchNumber       int        `json:"match_number"`
	Participant1ID    *string    `json:"participant1_id,omitempty"`
	Participant1Name  *string    `json:"participant1_name,omitempty"`
	Participant1Seed  *int       `json:"participant1_seed,omitempty"`
	Participant2ID    *string    `json:"participant2_id,omitempty"`
	Participant2Name  *string    `json:"participant2_name,omitempty"`
	Participant2Seed  *int       `json:"participant2_seed,omitempty"`
	ScheduledStart    *time.Time `json:"scheduled_start,omitempty"`
	MapName           *string    `json:"map_name,omitempty"`
	GameMode          *string    `json:"game_mode,omitempty"`
}

// UpdateMatchRequest represents request to update a match
type UpdateMatchRequest struct {
	Status            *MatchStatus           `json:"status,omitempty"`
	Participant1Score *int                   `json:"participant1_score,omitempty"`
	Participant2Score *int                   `json:"participant2_score,omitempty"`
	WinnerID          *string                `json:"winner_id,omitempty"`
	ActualStart       *time.Time             `json:"actual_start,omitempty"`
	CompletedAt       *time.Time             `json:"completed_at,omitempty"`
	SpectatorCount    *int                   `json:"spectator_count,omitempty"`
	StreamURL         *string                `json:"stream_url,omitempty"`
	ReplayURL         *string                `json:"replay_url,omitempty"`
	ScoreDetails      map[string]interface{} `json:"score_details,omitempty"`
	MatchStats        map[string]interface{} `json:"match_stats,omitempty"`
	Metadata          map[string]interface{} `json:"metadata,omitempty"`
}

// CreateParticipantRequest represents request to add a participant
type CreateParticipantRequest struct {
	BracketID       uuid.UUID        `json:"bracket_id"`
	ParticipantID   string           `json:"participant_id"`
	ParticipantName string           `json:"participant_name"`
	ParticipantType ParticipantType  `json:"participant_type"`
	SeedNumber      *int             `json:"seed_number,omitempty"`
	Metadata        map[string]interface{} `json:"metadata,omitempty"`
}

// UpdateParticipantRequest represents request to update a participant
type UpdateParticipantRequest struct {
	Status           *ParticipantStatus     `json:"status,omitempty"`
	CurrentRound     *int                   `json:"current_round,omitempty"`
	FinalRank        *int                   `json:"final_rank,omitempty"`
	TotalScore       *int                   `json:"total_score,omitempty"`
	TotalWins        *int                   `json:"total_wins,omitempty"`
	TotalLosses      *int                   `json:"total_losses,omitempty"`
	TotalDraws       *int                   `json:"total_draws,omitempty"`
	PerformanceStats map[string]interface{} `json:"performance_stats,omitempty"`
	Metadata         map[string]interface{} `json:"metadata,omitempty"`
}

type UpdateRoundRequest struct {
	Name      *string     `json:"name,omitempty"`
	Status    *RoundStatus `json:"status,omitempty"`
	StartDate *time.Time  `json:"start_date,omitempty"`
	EndDate   *time.Time  `json:"end_date,omitempty"`
}

type ReportMatchResultRequest struct {
	WinnerID    *string `json:"winner_id,omitempty"`
	WinnerScore int     `json:"winner_score"`
	LoserScore  int     `json:"loser_score"`
	IsWalkover  bool    `json:"is_walkover"`
	IsForfeit   bool    `json:"is_forfeit"`
}

type AddParticipantRequest struct {
	ParticipantID   string  `json:"participant_id"`
	ParticipantName string  `json:"participant_name"`
	ParticipantType ParticipantType `json:"participant_type"`
	SeedNumber      *int    `json:"seed_number,omitempty"`
}

// BracketProgress represents overall bracket progress
type BracketProgress struct {
	BracketID         uuid.UUID `json:"bracket_id"`
	TotalRounds       int       `json:"total_rounds"`
	CurrentRound      int       `json:"current_round"`
	TotalMatches      int       `json:"total_matches"`
	CompletedMatches  int       `json:"completed_matches"`
	ActiveMatches     int       `json:"active_matches"`
	TotalParticipants int       `json:"total_participants"`
	ActiveParticipants int      `json:"active_participants"`
	EliminatedParticipants int  `json:"eliminated_participants"`
	ProgressPercent   float64   `json:"progress_percent"`
}

// MatchResult represents the result of a completed match
type MatchResult struct {
	MatchID            uuid.UUID `json:"match_id"`
	WinnerID           *string   `json:"winner_id,omitempty"`
	WinnerName         *string   `json:"winner_name,omitempty"`
	LoserID            *string   `json:"loser_id,omitempty"`
	LoserName          *string   `json:"loser_name,omitempty"`
	WinnerScore        int       `json:"winner_score"`
	LoserScore         int       `json:"loser_score"`
	Duration           time.Duration `json:"duration"`
	CompletedAt        time.Time `json:"completed_at"`
	IsWalkover         bool      `json:"is_walkover"`
	IsForfeit          bool      `json:"is_forfeit"`
}