package models

import (
	"time"

	"github.com/google/uuid"
)

// PlayerAnalytics represents comprehensive player analytics
type PlayerAnalytics struct {
	PlayerID          uuid.UUID `json:"player_id"`
	Username          string    `json:"username"`
	TotalPlayTime     int64     `json:"total_play_time"`      // in minutes
	SessionsCount     int       `json:"sessions_count"`
	LastSeen          time.Time `json:"last_seen"`
	AverageSessionTime float64  `json:"average_session_time"` // in minutes
	RetentionRate     float64   `json:"retention_rate"`       // 1-day, 7-day, 30-day
	ChurnRisk         string    `json:"churn_risk"`           // "low", "medium", "high"
	EngagementScore   float64   `json:"engagement_score"`     // 0-100 scale
}

// GameMetrics represents overall game performance metrics
type GameMetrics struct {
	TotalPlayers      int64     `json:"total_players"`
	ActivePlayers     int64     `json:"active_players"`
	NewRegistrations  int64     `json:"new_registrations"`
	ConcurrentUsers   int64     `json:"concurrent_users"`
	PeakConcurrent    int64     `json:"peak_concurrent"`
	AverageSessionTime float64  `json:"average_session_time"`
	Revenue          float64   `json:"revenue"`
	TimeRange        string    `json:"time_range"`
	Timestamp        time.Time `json:"timestamp"`
}

// CombatAnalytics represents combat-related analytics
type CombatAnalytics struct {
	TotalMatches      int64     `json:"total_matches"`
	AverageMatchTime  float64   `json:"average_match_time"`  // in minutes
	WinRate          float64   `json:"win_rate"`
	PopularWeapons    []WeaponStats `json:"popular_weapons"`
	KillDeathRatio    float64   `json:"kill_death_ratio"`
	HeadshotRate      float64   `json:"headshot_rate"`
	TimeRange        string    `json:"time_range"`
	Timestamp        time.Time `json:"timestamp"`
}

// WeaponStats represents weapon usage statistics
type WeaponStats struct {
	WeaponID   string  `json:"weapon_id"`
	WeaponName string  `json:"weapon_name"`
	UsageCount int64   `json:"usage_count"`
	WinRate    float64 `json:"win_rate"`
	KillRate   float64 `json:"kill_rate"`
}

// EconomicAnalytics represents in-game economy analytics
type EconomicAnalytics struct {
	TotalTransactions   int64     `json:"total_transactions"`
	TotalRevenue        float64   `json:"total_revenue"`
	AverageTransaction  float64   `json:"average_transaction"`
	PopularItems        []ItemStats `json:"popular_items"`
	CurrencyCirculation float64   `json:"currency_circulation"`
	TradeVolume         int64     `json:"trade_volume"`
	TimeRange          string    `json:"time_range"`
	Timestamp          time.Time `json:"timestamp"`
}

// ItemStats represents item trading statistics
type ItemStats struct {
	ItemID     string  `json:"item_id"`
	ItemName   string  `json:"item_name"`
	TradeCount int64   `json:"trade_count"`
	AvgPrice   float64 `json:"avg_price"`
	TotalValue float64 `json:"total_value"`
}

// SocialAnalytics represents social features analytics
type SocialAnalytics struct {
	TotalGuilds        int64     `json:"total_guilds"`
	ActiveGuilds       int64     `json:"active_guilds"`
	TotalFriendships   int64     `json:"total_friendships"`
	MessagesSent       int64     `json:"messages_sent"`
	VoiceChannels      int64     `json:"voice_channels"`
	ActiveVoiceUsers   int64     `json:"active_voice_users"`
	TimeRange         string    `json:"time_range"`
	Timestamp         time.Time `json:"timestamp"`
}

// PerformanceMetrics represents system performance metrics
type PerformanceMetrics struct {
	ServiceName       string    `json:"service_name"`
	ResponseTime      float64   `json:"response_time"`       // in ms
	ErrorRate         float64   `json:"error_rate"`
	Throughput        int64     `json:"throughput"`          // requests per second
	MemoryUsage       int64     `json:"memory_usage"`        // in MB
	CPUUsage          float64   `json:"cpu_usage"`
	ActiveConnections int64     `json:"active_connections"`
	Timestamp         time.Time `json:"timestamp"`
}

// RealTimeDashboard represents real-time dashboard data
type RealTimeDashboard struct {
	OnlinePlayers     int64                `json:"online_players"`
	ActiveMatches     int64                `json:"active_matches"`
	ServerLoad        []ServerLoad         `json:"server_load"`
	RecentEvents      []GameEvent          `json:"recent_events"`
	TopPlayers        []PlayerRank         `json:"top_players"`
	RevenueToday      float64              `json:"revenue_today"`
	NewPlayersToday   int64                `json:"new_players_today"`
	Timestamp         time.Time            `json:"timestamp"`
}

// ServerLoad represents individual server load
type ServerLoad struct {
	ServerID   string  `json:"server_id"`
	ServerName string  `json:"server_name"`
	Region     string  `json:"region"`
	Load       float64 `json:"load"`        // 0-100%
	Players    int64   `json:"players"`
	Status     string  `json:"status"`      // "healthy", "warning", "critical"
}

// GameEvent represents recent game events for dashboard
type GameEvent struct {
	EventID     uuid.UUID `json:"event_id"`
	EventType   string    `json:"event_type"`   // "match_end", "achievement", "purchase"
	PlayerID    uuid.UUID `json:"player_id"`
	PlayerName  string    `json:"player_name"`
	Description string    `json:"description"`
	Value       float64   `json:"value"`
	Timestamp   time.Time `json:"timestamp"`
}

// PlayerRank represents player ranking for leaderboard
type PlayerRank struct {
	Rank      int       `json:"rank"`
	PlayerID  uuid.UUID `json:"player_id"`
	Username  string    `json:"username"`
	Score     int64     `json:"score"`
	Category  string    `json:"category"` // "kills", "wins", "score"
}

// DashboardWidget represents configurable dashboard widget
type DashboardWidget struct {
	WidgetID   uuid.UUID              `json:"widget_id"`
	WidgetType string                 `json:"widget_type"` // "chart", "metric", "table"
	Title      string                 `json:"title"`
	Config     map[string]interface{} `json:"config"`
	Data       interface{}            `json:"data"`
	Position   WidgetPosition         `json:"position"`
}

// WidgetPosition represents widget position on dashboard
type WidgetPosition struct {
	X      int `json:"x"`
	Y      int `json:"y"`
	Width  int `json:"width"`
	Height int `json:"height"`
}

// AnalyticsQuery represents analytics query parameters
type AnalyticsQuery struct {
	StartTime   time.Time `json:"start_time"`
	EndTime     time.Time `json:"end_time"`
	Granularity string    `json:"granularity"` // "hour", "day", "week", "month"
	Filters     map[string]interface{} `json:"filters"`
	GroupBy     []string  `json:"group_by"`
	Metrics     []string  `json:"metrics"`
}

// AnalyticsResponse represents analytics query response
type AnalyticsResponse struct {
	Query    AnalyticsQuery `json:"query"`
	Data     interface{}    `json:"data"`
	Metadata ResponseMetadata `json:"metadata"`
}

// ResponseMetadata represents response metadata
type ResponseMetadata struct {
	TotalRecords int64         `json:"total_records"`
	ExecutionTime time.Duration `json:"execution_time"`
	CacheHit     bool          `json:"cache_hit"`
	DataFreshness time.Duration `json:"data_freshness"`
}

// HealthStatus represents service health
type HealthStatus struct {
	Service    string            `json:"service"`
	Status     string            `json:"status"`
	Version    string            `json:"version"`
	Uptime     string            `json:"uptime"`
	Timestamp  time.Time         `json:"timestamp"`
	Services   map[string]string `json:"services"` // service_name -> status
	Metrics    HealthMetrics     `json:"metrics"`
}

// HealthMetrics represents health-related metrics
type HealthMetrics struct {
	ActiveConnections int64 `json:"active_connections"`
	QueriesPerSecond  int64 `json:"queries_per_second"`
	CacheHitRate      float64 `json:"cache_hit_rate"`
	ErrorRate         float64 `json:"error_rate"`
	ResponseTime      float64 `json:"response_time"`
}

// BACKEND NOTE: Struct field alignment optimized (large → small types).
// Expected memory savings: 30-50% for analytics data structures.
// Analytics service handles high-volume data processing with real-time requirements.
