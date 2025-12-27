// Issue: #2264
// Analytics Dashboard Service Models
// PERFORMANCE: Structs optimized for memory alignment and JSON serialization

package models

import (
	"time"
)

// HealthResponse represents service health status
// PERFORMANCE: Fields ordered for struct alignment (large → small)
type HealthResponse struct {
	Service               string    `json:"service" yaml:"service"`
	Status                string    `json:"status" yaml:"status"`
	Timestamp             time.Time `json:"timestamp" yaml:"timestamp"`
	Version               string    `json:"version,omitempty" yaml:"version,omitempty"`
	UptimeSeconds         int       `json:"uptime_seconds,omitempty" yaml:"uptime_seconds,omitempty"`
	ActiveConnections     int       `json:"active_connections,omitempty" yaml:"active_connections,omitempty"`
	DataFreshnessSeconds  int       `json:"data_freshness_seconds,omitempty" yaml:"data_freshness_seconds,omitempty"`
}

// GameAnalyticsOverview represents comprehensive game analytics data
type GameAnalyticsOverview struct {
	Summary         *DashboardSummary       `json:"summary"`
	PlayerMetrics   *PlayerMetricsSummary  `json:"player_metrics"`
	EconomicIndicators *EconomicIndicators `json:"economic_indicators"`
	CombatStats     *CombatStatsSummary    `json:"combat_stats"`
	SocialMetrics   *SocialMetricsSummary  `json:"social_metrics"`
	SystemHealth    *SystemHealthSummary   `json:"system_health"`
	Timestamp       time.Time              `json:"timestamp"`
	Period          string                 `json:"period"`
}

// DashboardSummary contains key performance indicators
type DashboardSummary struct {
	ActiveUsers          int     `json:"active_users"`
	NewRegistrations     int     `json:"new_registrations"`
	TotalRevenue         float64 `json:"total_revenue"`
	AverageSessionTime   float64 `json:"average_session_time"`
	ServerHealthScore    float64 `json:"server_health_score"`
	AlertsCount          int     `json:"alerts_count"`
}

// PlayerMetricsSummary contains player engagement metrics
type PlayerMetricsSummary struct {
	ActiveUsers              int                `json:"active_users"`
	NewUsers                 int                `json:"new_users"`
	RetentionRate            map[string]float64 `json:"retention_rate"`
	AverageSessionDuration   float64            `json:"average_session_duration"`
	ChurnRate                float64            `json:"churn_rate"`
	PlayerSegments           map[string]int     `json:"player_segments"`
}

// EconomicIndicators contains in-game economy health data
type EconomicIndicators struct {
	TotalCurrencyCirculation float64                      `json:"total_currency_circulation"`
	InflationRate           float64                      `json:"inflation_rate"`
	TradingVolume           float64                      `json:"trading_volume"`
	MarketStabilityIndex    float64                      `json:"market_stability_index"`
	TopTradedItems          []TradedItemInfo             `json:"top_traded_items"`
}

// TradedItemInfo represents trading data for an item
type TradedItemInfo struct {
	ItemID       string  `json:"item_id"`
	Volume       int     `json:"volume"`
	AveragePrice float64 `json:"average_price"`
}

// CombatStatsSummary contains combat performance data
type CombatStatsSummary struct {
	TotalMatches              int                   `json:"total_matches"`
	AverageMatchDuration      float64              `json:"average_match_duration"`
	WinRate                   float64              `json:"win_rate"`
	PopularGameModes          []GameModeStats      `json:"popular_game_modes"`
	RegionalPerformance       map[string]float64   `json:"regional_performance"`
}

// GameModeStats represents statistics for a game mode
type GameModeStats struct {
	Mode           string  `json:"mode"`
	Matches        int     `json:"matches"`
	AveragePlayers int     `json:"average_players"`
}

// SocialMetricsSummary contains social interaction data
type SocialMetricsSummary struct {
	ActiveGuilds          int               `json:"active_guilds"`
	AverageGuildSize      float64          `json:"average_guild_size"`
	SocialConnections     int              `json:"social_connections"`
	GuildActivityScore    float64          `json:"guild_activity_score"`
	TopGuilds             []GuildInfo       `json:"top_guilds"`
}

// GuildInfo represents guild performance data
type GuildInfo struct {
	GuildID         string  `json:"guild_id"`
	Name            string  `json:"name"`
	MemberCount     int     `json:"member_count"`
	ActivityScore   float64 `json:"activity_score"`
}

// SystemHealthSummary contains system performance data
type SystemHealthSummary struct {
	OverallHealthScore        float64            `json:"overall_health_score"`
	ResponseTimeAvg          float64            `json:"response_time_avg"`
	ErrorRate                float64            `json:"error_rate"`
	ActiveServices           int                `json:"active_services"`
	CriticalAlerts           int                `json:"critical_alerts"`
	InfrastructureStatus     map[string]string  `json:"infrastructure_status"`
}

// PlayerBehaviorAnalytics contains detailed player behavior data
type PlayerBehaviorAnalytics struct {
	Period           string              `json:"period"`
	Metrics          *PlayerMetricsSummary `json:"metrics"`
	Segments         []PlayerSegment     `json:"segments"`
	RetentionCurves  interface{}         `json:"retention_curves,omitempty"`
	EngagementPatterns *EngagementPatterns `json:"engagement_patterns"`
	Timestamp        time.Time           `json:"timestamp"`
}

// PlayerSegment represents a player behavior segment
type PlayerSegment struct {
	SegmentName   string             `json:"segment_name"`
	PlayerCount   int                `json:"player_count"`
	Characteristics map[string]interface{} `json:"characteristics"`
	RetentionRate float64            `json:"retention_rate"`
	LifetimeValue float64            `json:"lifetime_value"`
}

// EngagementPatterns contains player engagement data
type EngagementPatterns struct {
	PeakHours           []string `json:"peak_hours"`
	PreferredGameModes  []string `json:"preferred_game_modes"`
}

// EconomicAnalytics contains comprehensive economic data
type EconomicAnalytics struct {
	Period          string          `json:"period"`
	CurrencyFlow    *CurrencyFlow   `json:"currency_flow"`
	MarketTrends    []MarketTrend   `json:"market_trends"`
	PlayerWealth    *PlayerWealth   `json:"player_wealth"`
	TradingActivity *TradingActivity `json:"trading_activity"`
	Timestamp       time.Time       `json:"timestamp"`
}

// CurrencyFlow represents currency circulation data
type CurrencyFlow struct {
	TotalCirculation float64 `json:"total_circulation"`
	DailyVolume      float64 `json:"daily_volume"`
	InflationRate    float64 `json:"inflation_rate"`
}

// MarketTrend represents market trend data
type MarketTrend struct {
	ItemCategory   string  `json:"item_category"`
	PriceTrend     string  `json:"price_trend"`
	VolumeChange   float64 `json:"volume_change"`
}

// PlayerWealth represents player wealth distribution
type PlayerWealth struct {
	AverageBalance      float64            `json:"average_balance"`
	WealthDistribution  map[string]float64 `json:"wealth_distribution"`
}

// TradingActivity represents trading statistics
type TradingActivity struct {
	ActiveTraders        int     `json:"active_traders"`
	SuccessfulTrades     int     `json:"successful_trades"`
	AverageTradeValue    float64 `json:"average_trade_value"`
}

// CombatAnalytics contains detailed combat performance data
type CombatAnalytics struct {
	Period         string                 `json:"period"`
	OverallStats   *CombatOverallStats    `json:"overall_stats"`
	WeaponPerformance []WeaponPerformance `json:"weapon_performance"`
	ClassBalance   []ClassBalance        `json:"class_balance"`
	RegionalStats  map[string]*RegionalCombatStats `json:"regional_stats"`
	Timestamp      time.Time              `json:"timestamp"`
}

// CombatOverallStats contains overall combat statistics
type CombatOverallStats struct {
	TotalMatches      int     `json:"total_matches"`
	AverageDuration   float64 `json:"average_duration"`
	OverallWinRate    float64 `json:"overall_win_rate"`
}

// WeaponPerformance represents weapon usage statistics
type WeaponPerformance struct {
	WeaponID     string  `json:"weapon_id"`
	UsageRate    float64 `json:"usage_rate"`
	WinRate      float64 `json:"win_rate"`
	AverageKills float64 `json:"average_kills"`
}

// ClassBalance represents class performance data
type ClassBalance struct {
	ClassName    string  `json:"class_name"`
	PickRate     float64 `json:"pick_rate"`
	WinRate      float64 `json:"win_rate"`
	AverageScore float64 `json:"average_score"`
}

// RegionalCombatStats contains regional combat data
type RegionalCombatStats struct {
	TotalMatches   int     `json:"total_matches"`
	AveragePing    float64 `json:"average_ping"`
	WinRate        float64 `json:"win_rate"`
	PopularWeapons []string `json:"popular_weapons"`
}

// SocialAnalytics contains social network data
type SocialAnalytics struct {
	Period             string        `json:"period"`
	GuildMetrics       *GuildMetrics `json:"guild_metrics"`
	SocialConnections  *SocialConnections `json:"social_connections"`
	CommunityHealth    *CommunityHealth `json:"community_health"`
	TopGuilds          []GuildAnalytics `json:"top_guilds"`
	Timestamp          time.Time     `json:"timestamp"`
}

// GuildMetrics contains guild statistics
type GuildMetrics struct {
	TotalGuilds         int     `json:"total_guilds"`
	ActiveGuilds        int     `json:"active_guilds"`
	AverageGuildSize    float64 `json:"average_guild_size"`
	GuildRetentionRate  float64 `json:"guild_retention_rate"`
}

// SocialConnections contains social connection data
type SocialConnections struct {
	TotalConnections          int     `json:"total_connections"`
	AverageConnectionsPerPlayer float64 `json:"average_connections_per_player"`
	ConnectionGrowthRate      float64 `json:"connection_growth_rate"`
}

// CommunityHealth contains community health metrics
type CommunityHealth struct {
	EngagementScore     float64 `json:"engagement_score"`
	ToxicityLevel       float64 `json:"toxicity_level"`
	PositiveInteractions float64 `json:"positive_interactions"`
}

// GuildAnalytics contains individual guild data
type GuildAnalytics struct {
	GuildID           string  `json:"guild_id"`
	Name              string  `json:"name"`
	MemberCount       int     `json:"member_count"`
	ActivityScore     float64 `json:"activity_score"`
	AverageMemberLevel float64 `json:"average_member_level"`
	WeeklyActiveMembers int   `json:"weekly_active_members"`
	Achievements      []string `json:"achievements"`
}

// RevenueAnalytics contains revenue and monetization data
type RevenueAnalytics struct {
	Period                string              `json:"period"`
	RevenueMetrics        *RevenueMetrics     `json:"revenue_metrics"`
	PlayerSpending        *PlayerSpending     `json:"player_spending"`
	MonetizationEfficiency *MonetizationEfficiency `json:"monetization_efficiency"`
	TopRevenueSources     []RevenueSource     `json:"top_revenue_sources"`
	Timestamp             time.Time           `json:"timestamp"`
}

// RevenueMetrics contains revenue statistics
type RevenueMetrics struct {
	TotalRevenue    float64 `json:"total_revenue"`
	DailyAverage    float64 `json:"daily_average"`
	MonthlyRecurring float64 `json:"monthly_recurring"`
	ARPU            float64 `json:"arpu"`
	ARPPU           float64 `json:"arppu"`
}

// PlayerSpending contains player spending patterns
type PlayerSpending struct {
	PayingUsersPercentage  float64 `json:"paying_users_percentage"`
	AveragePurchaseValue   float64 `json:"average_purchase_value"`
	PurchaseFrequency      float64 `json:"purchase_frequency"`
}

// MonetizationEfficiency contains monetization metrics
type MonetizationEfficiency struct {
	ConversionRate    float64 `json:"conversion_rate"`
	RetentionLTV      float64 `json:"retention_ltv"`
	ChurnImpact       float64 `json:"churn_impact"`
}

// RevenueSource represents a revenue source
type RevenueSource struct {
	Source    string  `json:"source"`
	Revenue   float64 `json:"revenue"`
	Percentage float64 `json:"percentage"`
}

// SystemPerformanceAnalytics contains system performance data
type SystemPerformanceAnalytics struct {
	Period              string            `json:"period"`
	APIPerformance      *APIPerformance   `json:"api_performance"`
	DatabasePerformance *DatabasePerformance `json:"database_performance"`
	InfrastructureHealth *InfrastructureHealth `json:"infrastructure_health"`
	ActiveAlerts        []SystemAlert     `json:"active_alerts"`
	Timestamp           time.Time         `json:"timestamp"`
}

// APIPerformance contains API performance metrics
type APIPerformance struct {
	AverageResponseTime float64 `json:"average_response_time"`
	P95ResponseTime     float64 `json:"p95_response_time"`
	P99ResponseTime     float64 `json:"p99_response_time"`
	ErrorRate           float64 `json:"error_rate"`
	Throughput          int     `json:"throughput"`
}

// DatabasePerformance contains database performance metrics
type DatabasePerformance struct {
	ConnectionPoolUtilization float64 `json:"connection_pool_utilization"`
	AverageQueryTime          float64 `json:"average_query_time"`
	SlowQueriesCount          int     `json:"slow_queries_count"`
	CacheHitRate              float64 `json:"cache_hit_rate"`
}

// InfrastructureHealth contains infrastructure health data
type InfrastructureHealth struct {
	CPUUtilization     float64 `json:"cpu_utilization"`
	MemoryUtilization  float64 `json:"memory_utilization"`
	DiskUtilization    float64 `json:"disk_utilization"`
	NetworkBandwidth   float64 `json:"network_bandwidth"`
}

// SystemAlert represents a system performance alert
type SystemAlert struct {
	Severity    string    `json:"severity"`
	Message     string    `json:"message"`
	Component   string    `json:"component,omitempty"`
	Value       float64   `json:"value,omitempty"`
	Threshold   float64   `json:"threshold,omitempty"`
	Timestamp   time.Time `json:"timestamp"`
}

// AnalyticsAlerts contains analytics alerts data
type AnalyticsAlerts struct {
	Alerts   []AnalyticsAlert `json:"alerts"`
	Summary  *AlertSummary    `json:"summary"`
	Timestamp time.Time       `json:"timestamp"`
}

// AnalyticsAlert represents an analytics alert
type AnalyticsAlert struct {
	ID                string    `json:"id"`
	Severity          string    `json:"severity"`
	Title             string    `json:"title"`
	Message           string    `json:"message"`
	Category          string    `json:"category"`
	Metric            string    `json:"metric,omitempty"`
	CurrentValue      float64   `json:"current_value,omitempty"`
	ThresholdValue    float64   `json:"threshold_value,omitempty"`
	ChangePercentage  float64   `json:"change_percentage,omitempty"`
	Acknowledged      bool      `json:"acknowledged"`
	Timestamp         time.Time `json:"timestamp"`
	AcknowledgedAt    *time.Time `json:"acknowledged_at,omitempty"`
	AcknowledgedBy    string    `json:"acknowledged_by,omitempty"`
}

// AlertSummary contains alert summary statistics
type AlertSummary struct {
	CriticalCount int `json:"critical_count"`
	HighCount     int `json:"high_count"`
	MediumCount   int `json:"medium_count"`
	LowCount      int `json:"low_count"`
}

// AnalyticsReport represents a generated analytics report
type AnalyticsReport struct {
	ReportType    string                 `json:"report_type"`
	Period        *ReportPeriod          `json:"period"`
	Data          interface{}            `json:"data"`
	Metadata      *ReportMetadata       `json:"metadata,omitempty"`
	GeneratedAt   time.Time              `json:"generated_at"`
	GeneratedBy   string                 `json:"generated_by"`
	Format        string                 `json:"format"`
}

// ReportPeriod represents report time period
type ReportPeriod struct {
	StartDate string `json:"start_date"`
	EndDate   string `json:"end_date"`
}

// ReportMetadata contains report generation metadata
type ReportMetadata struct {
	TotalRecords         int     `json:"total_records"`
	DataFreshness       string  `json:"data_freshness"`
	GenerationTimeSeconds float64 `json:"generation_time_seconds"`
}

// Error represents an API error response
type Error struct {
	Message  string      `json:"message"`
	Domain   string      `json:"domain,omitempty"`
	Details  interface{} `json:"details,omitempty"`
	Code     int         `json:"code"`
}
