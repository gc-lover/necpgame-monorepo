package server

// API Request/Response models for Tournament Service

// Tournament related models
type TournamentSummary struct {
	TournamentID      string `json:"tournament_id"`
	Name              string `json:"name"`
	GameMode          string `json:"game_mode"`
	Status            string `json:"status"`
	ParticipantCount  int    `json:"participant_count"`
	MaxParticipants   int    `json:"max_participants"`
	StartTime         int64  `json:"start_time"`
	EndTime           int64  `json:"end_time"`
	RegionRestrictions []string `json:"region_restrictions"`
	CreatedAt         int64  `json:"created_at"`
}

type TournamentDetails struct {
	TournamentID           string              `json:"tournament_id"`
	Name                   string              `json:"name"`
	Description            string              `json:"description"`
	GameMode               string              `json:"game_mode"`
	TournamentFormat       string              `json:"tournament_format"`
	Status                 string              `json:"status"`
	ParticipantCount       int                 `json:"participant_count"`
	MaxParticipants        int                 `json:"max_participants"`
	MinParticipants        int                 `json:"min_participants"`
	RegistrationStartTime  int64               `json:"registration_start_time"`
	RegistrationEndTime    int64               `json:"registration_end_time"`
	StartTime              int64               `json:"start_time"`
	EndTime                int64               `json:"end_time"`
	Rules                  map[string]string   `json:"rules"`
	Prizes                 []Prize             `json:"prizes"`
	EntryFee               *EntryFee           `json:"entry_fee"`
	Visibility             string              `json:"visibility"`
	RegionRestrictions     []string            `json:"region_restrictions"`
	SkillRequirements      *SkillRequirements `json:"skill_requirements"`
	AutoProgression        bool                `json:"auto_progression"`
	MatchTimeout           int                 `json:"match_timeout"`
	AllowSpectators        bool                `json:"allow_spectators"`
	StreamingEnabled       bool                `json:"streaming_enabled"`
	CurrentRound           int                 `json:"current_round"`
	TotalRounds            int                 `json:"total_rounds"`
	CreatedAt              int64               `json:"created_at"`
	UpdatedAt              int64               `json:"updated_at"`
}

// Request models
type CreateTournamentRequest struct {
	TournamentID        string              `json:"tournament_id,omitempty"`
	Name                string              `json:"name"`
	Description         string              `json:"description,omitempty"`
	GameMode            string              `json:"game_mode"`
	TournamentFormat    string              `json:"tournament_format"`
	MaxParticipants     int                 `json:"max_participants"`
	MinParticipants     int                 `json:"min_participants,omitempty"`
	RegistrationStartTime int64             `json:"registration_start_time"`
	RegistrationEndTime int64               `json:"registration_end_time"`
	StartTime           int64               `json:"start_time"`
	EndTime             int64               `json:"end_time"`
	Rules               map[string]string   `json:"rules,omitempty"`
	Prizes              []Prize             `json:"prizes,omitempty"`
	EntryFee            EntryFee            `json:"entry_fee,omitempty"`
	Visibility          string              `json:"visibility,omitempty"`
	RegionRestrictions  []string            `json:"region_restrictions,omitempty"`
	SkillRequirements   SkillRequirements   `json:"skill_requirements,omitempty"`
	AutoProgression     bool                `json:"auto_progression,omitempty"`
	MatchTimeout        int                 `json:"match_timeout,omitempty"`
	AllowSpectators     bool                `json:"allow_spectators,omitempty"`
	StreamingEnabled    bool                `json:"streaming_enabled,omitempty"`
}

type UpdateTournamentRequest struct {
	Name          string              `json:"name,omitempty"`
	Description   string              `json:"description,omitempty"`
	Status        string              `json:"status,omitempty"`
	Rules         map[string]string   `json:"rules,omitempty"`
	Prizes        []Prize             `json:"prizes,omitempty"`
}

type RegisterTournamentRequest struct {
	PlayerID string `json:"player_id"`
	TeamID   string `json:"team_id,omitempty"`
	Message  string `json:"message,omitempty"`
}

type UnregisterTournamentRequest struct {
	PlayerID string `json:"player_id"`
	Reason   string `json:"reason,omitempty"`
}

// Response models
type CreateTournamentResponse struct {
	TournamentID  string `json:"tournament_id"`
	Name          string `json:"name"`
	LeaderID      string `json:"leader_id,omitempty"`
	Status        string `json:"status"`
	MemberCount   int    `json:"member_count"`
	CreatedAt     int64  `json:"created_at"`
}

type ListTournamentsResponse struct {
	Tournaments []*TournamentSummary `json:"tournaments"`
	TotalCount  int                  `json:"total_count"`
}

type GetTournamentResponse struct {
	Tournament *TournamentDetails `json:"tournament"`
}

type UpdateTournamentResponse struct {
	TournamentID  string   `json:"tournament_id"`
	UpdatedFields []string `json:"updated_fields"`
	UpdatedAt     int64    `json:"updated_at"`
}

type RegisterTournamentResponse struct {
	TournamentID string `json:"tournament_id"`
	PlayerID     string `json:"player_id"`
	Status       string `json:"status"`
	JoinedAt     int64  `json:"joined_at"`
	Message      string `json:"message,omitempty"`
}

type UnregisterTournamentResponse struct {
	TournamentID   string `json:"tournament_id"`
	PlayerID       string `json:"player_id"`
	UnregisteredAt int64  `json:"unregistered_at"`
	RefundProcessed bool   `json:"refund_processed"`
}

type GetTournamentBracketResponse struct {
	TournamentID string  `json:"tournament_id"`
	CurrentRound int     `json:"current_round"`
	TotalRounds  int     `json:"total_rounds"`
	Matches      []Match `json:"matches"`
}

// Match related models
type MatchDetail struct {
	MatchID        string                 `json:"match_id"`
	TournamentID   string                 `json:"tournament_id"`
	Round          int                    `json:"round"`
	Player1        *PlayerInfo            `json:"player1"`
	Player2        *PlayerInfo            `json:"player2"`
	Winner         *PlayerInfo            `json:"winner"`
	Status         string                 `json:"status"`
	ScheduledTime  int64                  `json:"scheduled_time"`
	StartedTime    int64                  `json:"started_time"`
	CompletedTime  int64                  `json:"completed_time"`
	Score          *MatchScore            `json:"score"`
	SpectatorsCount int                   `json:"spectators_count"`
	StreamingURL   string                 `json:"streaming_url"`
	Metadata       map[string]interface{} `json:"metadata"`
}

type PlayerInfo struct {
	PlayerID    string `json:"player_id"`
	DisplayName string `json:"display_name"`
	Level       int    `json:"level,omitempty"`
	Rating      int    `json:"rating,omitempty"`
}

type ListMatchesResponse struct {
	Matches    []*Match `json:"matches"`
	TotalCount int      `json:"total_count"`
}

type GetMatchResponse struct {
	Match *MatchDetail `json:"match"`
}

type UpdateMatchResultRequest struct {
	Score    MatchScore `json:"score"`
	WinnerID string     `json:"winner_id"`
}

type UpdateMatchResultResponse struct {
	MatchID                   string `json:"match_id"`
	TournamentID              string `json:"tournament_id"`
	WinnerID                  string `json:"winner_id"`
	LoserID                   string `json:"loser_id"`
	UpdatedAt                 int64  `json:"updated_at"`
	NextMatchScheduled        bool   `json:"next_match_scheduled"`
	TournamentProgressUpdated bool   `json:"tournament_progress_updated"`
}

// Ranking related models
type GetRankingsResponse struct {
	LeaderboardType string          `json:"leaderboard_type"`
	Rankings        []*PlayerRanking `json:"rankings"`
	TotalCount      int             `json:"total_count"`
	GeneratedAt     int64           `json:"generated_at"`
}

type GetPlayerRankingResponse struct {
	PlayerID     string                    `json:"player_id"`
	Rankings     map[string]*PlayerRanking `json:"rankings"`
	OverallStats *PlayerStats             `json:"overall_stats"`
}

// League related models
type LeagueSummary struct {
	LeagueID       string `json:"league_id"`
	Name           string `json:"name"`
	GameMode       string `json:"game_mode"`
	Status         string `json:"status"`
	CurrentSeason  int    `json:"current_season"`
	TeamCount      int    `json:"team_count"`
	MaxTeams       int    `json:"max_teams"`
	SeasonEndTime  int64  `json:"season_end_time"`
	Region         string `json:"region"`
}

type ListLeaguesResponse struct {
	Leagues    []*LeagueSummary `json:"leagues"`
	TotalCount int              `json:"total_count"`
}

type CreateLeagueRequest struct {
	Name            string              `json:"name"`
	Description     string              `json:"description,omitempty"`
	GameMode        string              `json:"game_mode"`
	MaxTeams        int                 `json:"max_teams"`
	SeasonStartTime int64               `json:"season_start_time"`
	SeasonEndTime   int64               `json:"season_end_time"`
	Rules           map[string]string   `json:"rules,omitempty"`
	Prizes          []Prize             `json:"prizes,omitempty"`
	Region          string              `json:"region,omitempty"`
}

type CreateLeagueResponse struct {
	LeagueID      string `json:"league_id"`
	Name          string `json:"name"`
	Status        string `json:"status"`
	CurrentSeason int    `json:"current_season"`
	CreatedAt     int64  `json:"created_at"`
}

type JoinLeagueRequest struct {
	PlayerID string `json:"player_id"`
	TeamName string `json:"team_name,omitempty"`
}

type JoinLeagueResponse struct {
	LeagueID   string `json:"league_id"`
	PlayerID   string `json:"player_id"`
	TeamID     string `json:"team_id"`
	JoinedAt   int64  `json:"joined_at"`
	CurrentRank int   `json:"current_rank"`
}

// Reward related models
type Reward struct {
	RewardID      string `json:"reward_id"`
	Position      int    `json:"position"`
	RewardType    string `json:"reward_type"`
	RewardValue   string `json:"reward_value"`
	Description   string `json:"description"`
	Claimed       bool   `json:"claimed"`
	ClaimDeadline int64  `json:"claim_deadline"`
}

type GetTournamentRewardsResponse struct {
	TournamentID    string   `json:"tournament_id"`
	PlayerID        string   `json:"player_id"`
	AvailableRewards []Reward `json:"available_rewards"`
	ClaimedRewards  []string `json:"claimed_rewards"`
}

type ClaimRewardsRequest struct {
	TournamentID string   `json:"tournament_id"`
	PlayerID     string   `json:"player_id"`
	RewardIDs    []string `json:"reward_ids"`
}

type ClaimRewardsResponse struct {
	TournamentID   string   `json:"tournament_id"`
	PlayerID       string   `json:"player_id"`
	ClaimedRewards []string `json:"claimed_rewards"`
	TotalValue     int      `json:"total_value"`
	ClaimedAt      int64    `json:"claimed_at"`
}

// Statistics related models
type GameModeStats struct {
	GameMode          string `json:"game_mode"`
	TournamentsCount  int    `json:"tournaments_count"`
	ParticipantsCount int    `json:"participants_count"`
}

type GetGlobalStatisticsResponse struct {
	TimeRange           string                   `json:"time_range"`
	TotalTournaments    int                      `json:"total_tournaments"`
	ActiveTournaments   int                      `json:"active_tournaments"`
	TotalParticipants   int                      `json:"total_participants"`
	TotalMatchesPlayed  int                      `json:"total_matches_played"`
	AverageTournamentSize float64                `json:"average_tournament_size"`
	PopularGameModes    []GameModeStats          `json:"popular_game_modes"`
	RegionActivity      map[string]int           `json:"region_activity"`
	GeneratedAt         int64                    `json:"generated_at"`
}

// Error response model
type ErrorResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Details string `json:"details,omitempty"`
}
