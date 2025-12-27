// Issue: #2214 - Real-time Match Statistics Aggregation
// Models for Match Stats Aggregator - Real-time competitive statistics system

package models

import (
	"time"
)

// MatchStatistics represents comprehensive match statistics
type MatchStatistics struct {
	MatchID         string                 `json:"match_id" db:"match_id"`
	GameMode        string                 `json:"game_mode" db:"game_mode"`
	MapName         string                 `json:"map_name" db:"map_name"`
	StartTime       time.Time              `json:"start_time" db:"start_time"`
	EndTime         *time.Time             `json:"end_time" db:"end_time"`
	Duration        int                    `json:"duration" db:"duration"`         // seconds
	Status          string                 `json:"status" db:"status"`             // "active", "completed", "abandoned"
	Winner          string                 `json:"winner" db:"winner"`             // team/player ID
	TotalPlayers    int                    `json:"total_players" db:"total_players"`
	Teams           []TeamStatistics       `json:"teams" db:"teams"`               // JSON array
	Players         []PlayerMatchStats     `json:"players" db:"players"`           // JSON array
	Events          []MatchEvent           `json:"events" db:"events"`             // JSON array
	AggregatedStats AggregatedMatchStats   `json:"aggregated_stats" db:"aggregated_stats"` // JSON
	Metadata        map[string]interface{} `json:"metadata" db:"metadata"`         // JSON
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// TeamStatistics represents team-level statistics
type TeamStatistics struct {
	TeamID       string  `json:"team_id"`
	TeamName     string  `json:"team_name"`
	Score        int     `json:"score"`
	Placement    int     `json:"placement"`
	Kills        int     `json:"kills"`
	Deaths       int     `json:"deaths"`
	Assists      int     `json:"assists"`
	ObjectiveScore int   `json:"objective_score"`
	Players      []string `json:"players"` // player IDs
}

// PlayerMatchStats represents individual player statistics for a match
type PlayerMatchStats struct {
	PlayerID      string  `json:"player_id"`
	PlayerName    string  `json:"player_name"`
	TeamID        string  `json:"team_id"`
	Score         int     `json:"score"`
	Kills         int     `json:"kills"`
	Deaths        int     `json:"deaths"`
	Assists       int     `json:"assists"`
	Headshots     int     `json:"headshots"`
	Accuracy      float64 `json:"accuracy"`      // 0.0-1.0
	DamageDealt   int     `json:"damage_dealt"`
	DamageTaken   int     `json:"damage_taken"`
	Healing       int     `json:"healing"`
	ObjectiveTime int     `json:"objective_time"` // seconds
	MovementStats MovementStats `json:"movement_stats"`
	WeaponStats   []WeaponStats `json:"weapon_stats"`
	Placement     int     `json:"placement"`
	SurvivalTime  int     `json:"survival_time"` // seconds
	Ping          int     `json:"ping"`          // milliseconds
	PacketLoss    float64 `json:"packet_loss"`   // 0.0-1.0
}

// MovementStats represents player movement statistics
type MovementStats struct {
	DistanceTraveled float64 `json:"distance_traveled"` // units
	AverageSpeed     float64 `json:"average_speed"`     // units per second
	MaxSpeed         float64 `json:"max_speed"`         // units per second
	Jumps            int     `json:"jumps"`
	CrouchTime       int     `json:"crouch_time"`       // seconds
	SprintTime       int     `json:"sprint_time"`       // seconds
	FallDamage       int     `json:"fall_damage"`
}

// WeaponStats represents weapon usage statistics
type WeaponStats struct {
	WeaponName    string  `json:"weapon_name"`
	WeaponType    string  `json:"weapon_type"`    // "rifle", "pistol", "shotgun", "sniper", "melee"
	ShotsFired    int     `json:"shots_fired"`
	ShotsHit      int     `json:"shots_hit"`
	DamageDealt   int     `json:"damage_dealt"`
	Kills         int     `json:"kills"`
	Headshots     int     `json:"headshots"`
	Accuracy      float64 `json:"accuracy"`       // 0.0-1.0
	TimeEquipped  int     `json:"time_equipped"`  // seconds
	Reloads       int     `json:"reloads"`
}

// MatchEvent represents significant events during a match
type MatchEvent struct {
	EventID     string                 `json:"event_id"`
	EventType   string                 `json:"event_type"`   // "kill", "death", "objective", "round_win", "match_end"
	PlayerID    string                 `json:"player_id,omitempty"`
	TargetID    string                 `json:"target_id,omitempty"`
	Weapon      string                 `json:"weapon,omitempty"`
	Location    [3]float64             `json:"location,omitempty"` // [x,y,z]
	Timestamp   time.Time              `json:"timestamp"`
	Data        map[string]interface{} `json:"data,omitempty"`
	Description string                 `json:"description"`
}

// AggregatedMatchStats contains aggregated statistics for the entire match
type AggregatedMatchStats struct {
	TotalKills       int     `json:"total_kills"`
	TotalDeaths      int     `json:"total_deaths"`
	TotalAssists     int     `json:"total_assists"`
	TotalDamage      int     `json:"total_damage"`
	AverageAccuracy  float64 `json:"average_accuracy"`  // 0.0-1.0
	LongestKill      int     `json:"longest_kill"`      // units
	MostValuablePlayer string `json:"most_valuable_player"`
	ClutchMoments    int     `json:"clutch_moments"`    // number of 1vX wins
	ComebackCount    int     `json:"comeback_count"`    // teams that came back from deficit
	AverageMatchDuration int  `json:"average_match_duration"` // seconds
}

// PlayerProfile represents aggregated player statistics across matches
type PlayerProfile struct {
	PlayerID          string              `json:"player_id" db:"player_id"`
	PlayerName        string              `json:"player_name" db:"player_name"`
	TotalMatches      int64               `json:"total_matches" db:"total_matches"`
	TotalWins         int64               `json:"total_wins" db:"total_wins"`
	TotalLosses       int64               `json:"total_losses" db:"total_losses"`
	WinRate           float64             `json:"win_rate" db:"win_rate"`           // 0.0-1.0
	AveragePlacement  float64             `json:"average_placement" db:"average_placement"`
	TotalKills        int64               `json:"total_kills" db:"total_kills"`
	TotalDeaths       int64               `json:"total_deaths" db:"total_deaths"`
	KillDeathRatio    float64             `json:"kill_death_ratio" db:"kill_death_ratio"`
	TotalScore        int64               `json:"total_score" db:"total_score"`
	AverageScore      float64             `json:"average_score" db:"average_score"`
	TotalPlayTime     int64               `json:"total_play_time" db:"total_play_time"` // seconds
	AverageAccuracy   float64             `json:"average_accuracy" db:"average_accuracy"`
	FavoriteWeapon    string              `json:"favorite_weapon" db:"favorite_weapon"`
	FavoriteMap       string              `json:"favorite_map" db:"favorite_map"`
	Rank              int                 `json:"rank" db:"rank"`
	SkillRating       float64             `json:"skill_rating" db:"skill_rating"`
	PeakRank          int                 `json:"peak_rank" db:"peak_rank"`
	SeasonStats       []SeasonStats       `json:"season_stats" db:"season_stats"`     // JSON array
	Achievements      []string            `json:"achievements" db:"achievements"`      // JSON array
	LastMatchTime     time.Time           `json:"last_match_time" db:"last_match_time"`
	CreatedAt         time.Time           `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time           `json:"updated_at" db:"updated_at"`
}

// SeasonStats represents statistics for a specific season
type SeasonStats struct {
	SeasonID    string    `json:"season_id"`
	SeasonName  string    `json:"season_name"`
	StartDate   time.Time `json:"start_date"`
	EndDate     time.Time `json:"end_date"`
	Matches     int       `json:"matches"`
	Wins        int       `json:"wins"`
	Losses      int       `json:"losses"`
	WinRate     float64   `json:"win_rate"`
	PeakRank    int       `json:"peak_rank"`
	FinalRank   int       `json:"final_rank"`
	Reward      string    `json:"reward,omitempty"`
}

// Leaderboard represents a competitive leaderboard
type Leaderboard struct {
	LeaderboardID   string             `json:"leaderboard_id" db:"leaderboard_id"`
	Name            string             `json:"name" db:"name"`
	Description     string             `json:"description" db:"description"`
	Type            string             `json:"type" db:"type"`             // "global", "regional", "seasonal", "tournament"
	GameMode        string             `json:"game_mode" db:"game_mode"`
	Region          string             `json:"region" db:"region"`
	SeasonID        string             `json:"season_id" db:"season_id,omitempty"`
	MaxEntries      int                `json:"max_entries" db:"max_entries"`
	ResetFrequency  string             `json:"reset_frequency" db:"reset_frequency"` // "daily", "weekly", "monthly", "seasonal"
	LastReset       time.Time          `json:"last_reset" db:"last_reset"`
	NextReset       time.Time          `json:"next_reset" db:"next_reset"`
	Entries         []LeaderboardEntry `json:"entries" db:"entries"`       // JSON array (top entries only)
	TotalEntries    int64              `json:"total_entries" db:"total_entries"`
	CreatedAt       time.Time          `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time          `json:"updated_at" db:"updated_at"`
}

// LeaderboardEntry represents a player's entry in a leaderboard
type LeaderboardEntry struct {
	Rank          int       `json:"rank"`
	PreviousRank  int       `json:"previous_rank"`
	PlayerID      string    `json:"player_id"`
	PlayerName    string    `json:"player_name"`
	Value         float64   `json:"value"`         // score/rating value
	Change        int       `json:"change"`        // rank change from previous period
	MatchesPlayed int       `json:"matches_played"`
	WinRate       float64   `json:"win_rate"`
	LastUpdated   time.Time `json:"last_updated"`
}

// TournamentStatistics represents tournament-level statistics
type TournamentStatistics struct {
	TournamentID       string                  `json:"tournament_id" db:"tournament_id"`
	TournamentName     string                  `json:"tournament_name" db:"tournament_name"`
	StartDate          time.Time               `json:"start_date" db:"start_date"`
	EndDate            *time.Time              `json:"end_date" db:"end_date"`
	Status             string                  `json:"status" db:"status"`             // "upcoming", "active", "completed"
	TotalParticipants  int                     `json:"total_participants" db:"total_participants"`
	TotalMatches       int                     `json:"total_matches" db:"total_matches"`
	TotalPrizePool     int64                   `json:"total_prize_pool" db:"total_prize_pool"`
	CurrentRound       int                     `json:"current_round" db:"current_round"`
	MatchesByRound     []RoundStats            `json:"matches_by_round" db:"matches_by_round"` // JSON array
	TopPerformers      []TournamentPerformer   `json:"top_performers" db:"top_performers"`     // JSON array
	ViewershipStats    ViewershipStats         `json:"viewership_stats" db:"viewership_stats"` // JSON
	SpectatorPeak      int                     `json:"spectator_peak" db:"spectator_peak"`
	AggregatedStats    TournamentAggregatedStats `json:"aggregated_stats" db:"aggregated_stats"` // JSON
	Metadata           map[string]interface{}  `json:"metadata" db:"metadata"`         // JSON
	CreatedAt          time.Time               `json:"created_at" db:"created_at"`
	UpdatedAt          time.Time               `json:"updated_at" db:"updated_at"`
}

// RoundStats represents statistics for a tournament round
type RoundStats struct {
	RoundNumber    int       `json:"round_number"`
	RoundName      string    `json:"round_name"`
	TotalMatches   int       `json:"total_matches"`
	CompletedMatches int     `json:"completed_matches"`
	AverageDuration int      `json:"average_duration"` // seconds
	Winners        []string  `json:"winners"`          // player IDs
	StartTime      time.Time `json:"start_time"`
	EndTime        *time.Time `json:"end_time,omitempty"`
}

// TournamentPerformer represents a top performer in a tournament
type TournamentPerformer struct {
	PlayerID    string  `json:"player_id"`
	PlayerName  string  `json:"player_name"`
	Kills       int     `json:"kills"`
	Deaths      int     `json:"deaths"`
	Assists     int     `json:"assists"`
	Score       int     `json:"score"`
	Placement   int     `json:"placement"`
	WinRate     float64 `json:"win_rate"`
	MVPCount    int     `json:"mvp_count"`
}

// ViewershipStats represents tournament viewership statistics
type ViewershipStats struct {
	TotalViewTime     int64   `json:"total_view_time"`     // minutes
	AverageViewers    float64 `json:"average_viewers"`
	PeakViewers       int     `json:"peak_viewers"`
	UniqueViewers     int64   `json:"unique_viewers"`
	ViewTimeByRegion  map[string]int64 `json:"view_time_by_region"`
	PopularStreamers  []string `json:"popular_streamers"`
}

// TournamentAggregatedStats contains aggregated tournament statistics
type TournamentAggregatedStats struct {
	TotalKills       int64   `json:"total_kills"`
	TotalDeaths      int64   `json:"total_deaths"`
	TotalAssists     int64   `json:"total_assists"`
	TotalDamage      int64   `json:"total_damage"`
	AverageAccuracy  float64 `json:"average_accuracy"`
	LongestStreak    int     `json:"longest_streak"`
	MostValuablePlayer string `json:"most_valuable_player"`
	ClutchPlays      int     `json:"clutch_plays"`
	Comebacks        int     `json:"comebacks"`
}

// RealTimeStats represents real-time statistics update
type RealTimeStats struct {
	MatchID       string                 `json:"match_id"`
	UpdateType    string                 `json:"update_type"`    // "player", "team", "match", "event"
	PlayerID      string                 `json:"player_id,omitempty"`
	TeamID        string                 `json:"team_id,omitempty"`
	StatType      string                 `json:"stat_type"`      // "kill", "death", "score", "objective"
	Value         interface{}            `json:"value"`
	Timestamp     time.Time              `json:"timestamp"`
	Metadata      map[string]interface{} `json:"metadata,omitempty"`
}

// StatisticsQuery represents a query for statistics
type StatisticsQuery struct {
	QueryType     string                 `json:"query_type"`     // "player", "match", "leaderboard", "tournament"
	PlayerID      string                 `json:"player_id,omitempty"`
	MatchID       string                 `json:"match_id,omitempty"`
	TournamentID  string                 `json:"tournament_id,omitempty"`
	LeaderboardID string                 `json:"leaderboard_id,omitempty"`
	TimeRange     TimeRange              `json:"time_range,omitempty"`
	Filters       map[string]interface{} `json:"filters,omitempty"`
	Limit         int                    `json:"limit,omitempty"`
	Offset        int                    `json:"offset,omitempty"`
}

// TimeRange represents a time range for queries
type TimeRange struct {
	Start time.Time `json:"start,omitempty"`
	End   time.Time `json:"end,omitempty"`
}

// StatisticsResponse represents a statistics query response
type StatisticsResponse struct {
	Query      StatisticsQuery `json:"query"`
	Data       interface{}     `json:"data"`
	TotalCount int64           `json:"total_count,omitempty"`
	Timestamp  time.Time       `json:"timestamp"`
	CacheHit   bool            `json:"cache_hit,omitempty"`
}

// AnalyticsReport represents a comprehensive analytics report
type AnalyticsReport struct {
	ReportID     string                 `json:"report_id" db:"report_id"`
	ReportType   string                 `json:"report_type" db:"report_type"` // "daily", "weekly", "monthly", "season"
	TimeRange    TimeRange              `json:"time_range" db:"time_range"`  // JSON
	GeneratedAt  time.Time              `json:"generated_at" db:"generated_at"`
	Data         map[string]interface{} `json:"data" db:"data"`             // JSON report data
	Summary      ReportSummary          `json:"summary" db:"summary"`       // JSON
	Trends       []TrendData            `json:"trends" db:"trends"`         // JSON array
	Insights     []string               `json:"insights" db:"insights"`     // JSON array
	Status       string                 `json:"status" db:"status"`         // "generating", "completed", "failed"
}

// ReportSummary contains key metrics for the report
type ReportSummary struct {
	TotalMatches     int64   `json:"total_matches"`
	TotalPlayers     int64   `json:"total_players"`
	AverageMatchTime float64 `json:"average_match_time"` // minutes
	PeakConcurrency  int64   `json:"peak_concurrency"`
	PopularGameMode  string  `json:"popular_game_mode"`
	ServerUptime     float64 `json:"server_uptime"`       // 0.0-1.0
	ErrorRate        float64 `json:"error_rate"`         // 0.0-1.0
}

// TrendData represents trending statistics over time
type TrendData struct {
	Metric    string      `json:"metric"`
	TimePoints []TimePoint `json:"time_points"`
	Trend     string      `json:"trend"` // "increasing", "decreasing", "stable"
	ChangePercent float64 `json:"change_percent"`
}

// TimePoint represents a data point at a specific time
type TimePoint struct {
	Timestamp time.Time `json:"timestamp"`
	Value     float64   `json:"value"`
}

// PerformanceMetrics represents system performance metrics
type PerformanceMetrics struct {
	ServiceName     string    `json:"service_name"`
	Timestamp       time.Time `json:"timestamp"`
	ResponseTime    int64     `json:"response_time"`    // milliseconds
	Throughput      int64     `json:"throughput"`       // requests per second
	ErrorRate       float64   `json:"error_rate"`       // 0.0-1.0
	MemoryUsage     int64     `json:"memory_usage"`     // bytes
	CPUUsage        float64   `json:"cpu_usage"`        // 0.0-1.0
	ActiveConnections int     `json:"active_connections"`
	QueueDepth      int       `json:"queue_depth"`
	CacheHitRate    float64   `json:"cache_hit_rate"`   // 0.0-1.0
}
