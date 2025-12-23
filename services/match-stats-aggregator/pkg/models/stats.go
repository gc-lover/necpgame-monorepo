// Issue: #2214
// Real-time Match Statistics Aggregation System
// Memory-optimized data structures for MMOFPS performance

package models

import (
	"sync"
	"time"
)

// MatchStatistics represents aggregated statistics for a single match
// Memory layout optimized: large fields first for struct alignment
type MatchStatistics struct {
	MatchID         string    `json:"match_id" db:"match_id"`
	StartTime       time.Time `json:"start_time" db:"start_time"`
	EndTime         time.Time `json:"end_time,omitempty" db:"end_time"`
	Duration        int64     `json:"duration_ms" db:"duration_ms"`        // Match duration in milliseconds
	Status          string    `json:"status" db:"status"`                  // active, completed, cancelled
	MapName         string    `json:"map_name" db:"map_name"`
	GameMode        string    `json:"game_mode" db:"game_mode"`
	MaxPlayers      int       `json:"max_players" db:"max_players"`
	CurrentPlayers  int       `json:"current_players" db:"current_players"`

	// Core statistics - optimized for memory alignment
	TotalKills      int64 `json:"total_kills" db:"total_kills"`
	TotalDeaths     int64 `json:"total_deaths" db:"total_deaths"`
	TotalAssists    int64 `json:"total_assists" db:"total_assists"`
	TotalDamage     int64 `json:"total_damage" db:"total_damage"`
	TotalHealing    int64 `json:"total_healing" db:"total_healing"`

	// Performance metrics
	AvgTickRate     float64 `json:"avg_tick_rate" db:"avg_tick_rate"`
	MinTickRate     float64 `json:"min_tick_rate" db:"min_tick_rate"`
	MaxTickRate     float64 `json:"max_tick_rate" db:"max_tick_rate"`
	AvgLatency      int64   `json:"avg_latency_ms" db:"avg_latency_ms"`
	PacketLoss      float64 `json:"packet_loss_pct" db:"packet_loss_pct"`

	// Real-time data (updated every 5 seconds)
	LastUpdate      time.Time `json:"last_update" db:"last_update"`
	EventCount      int64     `json:"event_count" db:"event_count"`

	// Player statistics - slice for dynamic sizing
	PlayerStats     []PlayerMatchStats `json:"player_stats,omitempty" db:"-"`

	// Mutex for concurrent access - placed at end for alignment
	mu              sync.RWMutex `db:"-"`
}

// PlayerMatchStats represents individual player statistics within a match
// Optimized for memory efficiency and cache performance
type PlayerMatchStats struct {
	PlayerID        string  `json:"player_id" db:"player_id"`
	PlayerName      string  `json:"player_name" db:"player_name"`
	TeamID          string  `json:"team_id" db:"team_id"`

	// Combat statistics - core gameplay metrics
	Kills           int     `json:"kills" db:"kills"`
	Deaths          int     `json:"deaths" db:"deaths"`
	Assists         int     `json:"assists" db:"assists"`
	Score           int     `json:"score" db:"score"`

	// Damage metrics
	DamageDealt     int64   `json:"damage_dealt" db:"damage_dealt"`
	DamageReceived  int64   `json:"damage_received" db:"damage_received"`
	HealingDone     int64   `json:"healing_done" db:"healing_done"`

	// Accuracy and efficiency
	ShotsFired      int64   `json:"shots_fired" db:"shots_fired"`
	ShotsHit        int64   `json:"shots_hit" db:"shots_hit"`
	Accuracy        float64 `json:"accuracy_pct" db:"accuracy_pct"`

	// Position and movement
	DistanceTraveled float64 `json:"distance_traveled" db:"distance_traveled"`
	AvgSpeed         float64 `json:"avg_speed" db:"avg_speed"`

	// Special actions
	Headshots       int     `json:"headshots" db:"headshots"`
	Longshots       int     `json:"longshots" db:"longshots"`
	Multikills      int     `json:"multikills" db:"multikills"`

	// Equipment usage
	WeaponUsed      string  `json:"weapon_used" db:"weapon_used"`
	ItemsUsed       []string `json:"items_used,omitempty" db:"-"`

	// Performance rating (calculated)
	KDRatio         float64 `json:"kd_ratio" db:"kd_ratio"`
	Performance     float64 `json:"performance_rating" db:"performance_rating"`

	// Real-time position (for live tracking)
	CurrentX        float64 `json:"current_x,omitempty" db:"-"`
	CurrentY        float64 `json:"current_y,omitempty" db:"-"`
	CurrentZ        float64 `json:"current_z,omitempty" db:"-"`
}

// MatchEvent represents a single event that occurred during a match
// Used for real-time statistics aggregation
type MatchEvent struct {
	EventID         string    `json:"event_id"`
	MatchID         string    `json:"match_id"`
	PlayerID        string    `json:"player_id"`
	EventType       string    `json:"event_type"`       // kill, death, damage, position, item_use
	Timestamp       time.Time `json:"timestamp"`

	// Event-specific data
	EventData       map[string]interface{} `json:"event_data"`

	// Processing metadata
	Processed       bool      `json:"processed"`
	ProcessedAt     time.Time `json:"processed_at,omitempty"`
}

// StatisticsSnapshot represents a point-in-time snapshot of match statistics
// Used for caching and dashboard display
type StatisticsSnapshot struct {
	SnapshotID      string             `json:"snapshot_id"`
	MatchID         string             `json:"match_id"`
	Timestamp       time.Time          `json:"timestamp"`
	Statistics      MatchStatistics    `json:"statistics"`
	PlayerStats     []PlayerMatchStats `json:"player_stats"`

	// Cache metadata
	TTL             time.Duration      `json:"ttl"`
	Compressed      bool               `json:"compressed"`
}

// AggregationConfig defines how statistics should be aggregated
type AggregationConfig struct {
	MatchID         string        `json:"match_id"`
	UpdateInterval  time.Duration `json:"update_interval"`   // How often to update aggregates
	RetentionPeriod time.Duration `json:"retention_period"`  // How long to keep data
	MaxPlayers      int           `json:"max_players"`       // Maximum players per match

	// Performance settings
	BatchSize       int           `json:"batch_size"`        // Batch size for processing
	WorkerCount     int           `json:"worker_count"`      // Number of processing workers
	BufferSize      int           `json:"buffer_size"`       // Event buffer size
}

// Pool for memory reuse - critical for MMOFPS performance
var (
	playerStatsPool = sync.Pool{
		New: func() interface{} {
			return &PlayerMatchStats{}
		},
	}

	matchEventPool = sync.Pool{
		New: func() interface{} {
			return &MatchEvent{
				EventData: make(map[string]interface{}),
			}
		},
	}
)

// GetPlayerStatsFromPool returns a PlayerMatchStats from the pool
func GetPlayerStatsFromPool() *PlayerMatchStats {
	return playerStatsPool.Get().(*PlayerMatchStats)
}

// PutPlayerStatsToPool returns a PlayerMatchStats to the pool
func PutPlayerStatsToPool(ps *PlayerMatchStats) {
	// Reset the struct
	*ps = PlayerMatchStats{}
	playerStatsPool.Put(ps)
}

// GetMatchEventFromPool returns a MatchEvent from the pool
func GetMatchEventFromPool() *MatchEvent {
	return matchEventPool.Get().(*MatchEvent)
}

// PutMatchEventToPool returns a MatchEvent to the pool
func PutMatchEventToPool(me *MatchEvent) {
	// Reset the struct
	*me = MatchEvent{
		EventData: make(map[string]interface{}),
	}
	matchEventPool.Put(me)
}
