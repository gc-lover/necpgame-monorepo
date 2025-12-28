// Package playeranalytics provides comprehensive player behavior analytics for MMOFPS games
package playeranalytics

import (
	"context"
	"encoding/json"
	"fmt"
	"math"
	"sort"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// PlayerAnalytics provides comprehensive player behavior and engagement analytics
type PlayerAnalytics struct {
	config         *AnalyticsConfig
	logger         *errorhandling.Logger
	playerData     map[string]*PlayerProfile
	sessionData    map[string][]*GameSession
	eventBuffer    chan *PlayerEvent
	metrics        *AnalyticsMetrics

	mu sync.RWMutex

	// Background processing
	shutdownChan chan struct{}
	wg           sync.WaitGroup
}

// AnalyticsConfig holds analytics configuration
type AnalyticsConfig struct {
	RetentionDays       int           `json:"retention_days"`
	SessionTimeout      time.Duration `json:"session_timeout"`
	EventBufferSize     int           `json:"event_buffer_size"`
	ProcessingInterval  time.Duration `json:"processing_interval"`
	EnableRealTime      bool          `json:"enable_real_time"`
	EnablePredictions   bool          `json:"enable_predictions"`
	CohortAnalysisDays  int           `json:"cohort_analysis_days"`
	ChurnThresholdDays  int           `json:"churn_threshold_days"`
}

// PlayerProfile contains comprehensive player information and analytics
type PlayerProfile struct {
	PlayerID       string                 `json:"player_id"`
	CreatedAt      time.Time              `json:"created_at"`
	LastSeen       time.Time              `json:"last_seen"`
	TotalSessions  int                    `json:"total_sessions"`
	TotalPlayTime  time.Duration          `json:"total_play_time"`
	Level          int                    `json:"level"`
	Experience     int64                  `json:"experience"`
	Region         string                 `json:"region"`

	// Engagement metrics
	SessionFrequency   float64              `json:"session_frequency"`   // sessions per day
	AvgSessionLength   time.Duration        `json:"avg_session_length"`
	RetentionRate      map[string]float64  `json:"retention_rate"`      // day -> retention %
	ChurnRisk          float64              `json:"churn_risk"`          // 0-1 scale

	// Gameplay metrics
	FavoriteGameMode   string               `json:"favorite_game_mode"`
	SkillRating        float64              `json:"skill_rating"`
	WinRate            float64              `json:"win_rate"`
	KDRatio            float64              `json:"kd_ratio"`
	PlayStyle          string               `json:"play_style"`          // aggressive, defensive, supportive

	// Social metrics
	FriendsCount       int                  `json:"friends_count"`
	GuildID            string               `json:"guild_id"`
	SocialEngagement   float64              `json:"social_engagement"`   // 0-1 scale

	// Economic metrics
	TotalSpent         float64              `json:"total_spent"`
	PurchaseFrequency  float64              `json:"purchase_frequency"`
	FavoriteItems      []string             `json:"favorite_items"`

	// Behavioral patterns
	PeakPlayHours      []int                `json:"peak_play_hours"`     // hours of day
	WeeklyPattern      map[string]float64   `json:"weekly_pattern"`      // day -> activity %
	DeviceType         string               `json:"device_type"`
	ConnectionQuality  string               `json:"connection_quality"`

	// Advanced analytics
	EngagementScore    float64              `json:"engagement_score"`    // 0-100 scale
	LoyaltyTier        string               `json:"loyalty_tier"`        // bronze, silver, gold, platinum
	PredictedChurnDate *time.Time           `json:"predicted_churn_date"`
	CustomMetrics      map[string]interface{} `json:"custom_metrics"`

	// Raw data for analysis
	SessionHistory     []*GameSession        `json:"session_history"`
	EventHistory       []*PlayerEvent        `json:"event_history"`
}

// GameSession represents a player's game session
type GameSession struct {
	SessionID     string        `json:"session_id"`
	PlayerID      string        `json:"player_id"`
	StartTime     time.Time     `json:"start_time"`
	EndTime       *time.Time    `json:"end_time"`
	Duration      time.Duration `json:"duration"`
	GameMode      string        `json:"game_mode"`
	MapName       string        `json:"map_name"`
	Result        string        `json:"result"`        // win, loss, draw
	Score         int           `json:"score"`
	Kills         int           `json:"kills"`
	Deaths        int           `json:"deaths"`
	Assists       int           `json:"assists"`
	DamageDealt   int           `json:"damage_dealt"`
	DamageTaken   int           `json:"damage_taken"`
	Region        string        `json:"region"`
	Ping          int           `json:"ping"`
	FPS           int           `json:"fps"`
	DeviceInfo    DeviceInfo    `json:"device_info"`
}

// DeviceInfo contains device and connection information
type DeviceInfo struct {
	Platform    string `json:"platform"`    // pc, console, mobile
	OS          string `json:"os"`
	DeviceModel string `json:"device_model"`
	ScreenRes   string `json:"screen_resolution"`
	NetworkType string `json:"network_type"` // wifi, cellular, ethernet
	ConnectionQuality string `json:"connection_quality"` // excellent, good, poor
}

// PlayerEvent represents a player action or event
type PlayerEvent struct {
	EventID     string                 `json:"event_id"`
	PlayerID    string                 `json:"player_id"`
	EventType   string                 `json:"event_type"`
	EventData   map[string]interface{} `json:"event_data"`
	Timestamp   time.Time              `json:"timestamp"`
	SessionID   string                 `json:"session_id"`
	Location    Location               `json:"location"`
	Context     map[string]interface{} `json:"context"`
}

// Location represents in-game location
type Location struct {
	X         float64 `json:"x"`
	Y         float64 `json:"y"`
	Z         float64 `json:"z"`
	Zone      string  `json:"zone"`
	SubZone   string  `json:"sub_zone"`
}

// AnalyticsMetrics contains aggregated analytics metrics
type AnalyticsMetrics struct {
	TotalPlayers       int64                `json:"total_players"`
	ActivePlayers      int64                `json:"active_players"`
	NewPlayersToday    int64                `json:"new_players_today"`
	ReturningPlayers   int64                `json:"returning_players"`
	AvgSessionLength   time.Duration        `json:"avg_session_length"`
	RetentionRates     map[string]float64   `json:"retention_rates"`     // day -> rate
	ChurnRate          float64              `json:"churn_rate"`
	EngagementScore    float64              `json:"engagement_score"`
	RevenueMetrics     RevenueMetrics       `json:"revenue_metrics"`
	GameplayMetrics    GameplayMetrics      `json:"gameplay_metrics"`
	SocialMetrics      SocialMetrics        `json:"social_metrics"`
}

// RevenueMetrics contains revenue analytics
type RevenueMetrics struct {
	TotalRevenue     float64            `json:"total_revenue"`
	ARPU             float64            `json:"arpu"`             // Average Revenue Per User
	ARPPU            float64            `json:"arppu"`            // Average Revenue Per Paying User
	ConversionRate   float64            `json:"conversion_rate"`
	TopItems         map[string]int     `json:"top_items"`
	PurchasePatterns map[string]float64 `json:"purchase_patterns"`
}

// GameplayMetrics contains gameplay analytics
type GameplayMetrics struct {
	AvgMatchDuration  time.Duration                `json:"avg_match_duration"`
	PopularGameModes  map[string]int               `json:"popular_game_modes"`
	SkillDistribution map[string]int               `json:"skill_distribution"`
	WinRateByTier     map[string]float64           `json:"win_rate_by_tier"`
	PopularMaps       map[string]int               `json:"popular_maps"`
	QuitRate          float64                      `json:"quit_rate"`
}

// SocialMetrics contains social engagement analytics
type SocialMetrics struct {
	TotalGuilds       int64             `json:"total_guilds"`
	AvgGuildSize      float64           `json:"avg_guild_size"`
	FriendConnections int64             `json:"friend_connections"`
	SocialEvents      int64             `json:"social_events"`
	CommunicationVolume map[string]int `json:"communication_volume"`
}

// CohortAnalysis represents cohort analysis results
type CohortAnalysis struct {
	CohortDate    time.Time           `json:"cohort_date"`
	CohortSize    int                 `json:"cohort_size"`
	RetentionData map[int]float64     `json:"retention_data"` // day -> retention %
	LifetimeValue float64             `json:"lifetime_value"`
	EngagementTrend []EngagementPoint `json:"engagement_trend"`
}

// EngagementPoint represents a point in engagement trend
type EngagementPoint struct {
	Date             time.Time `json:"date"`
	ActiveUsers      int       `json:"active_users"`
	SessionCount     int       `json:"session_count"`
	AvgSessionLength float64   `json:"avg_session_length"`
	EngagementScore  float64   `json:"engagement_score"`
}

// NewPlayerAnalytics creates a new player analytics system
func NewPlayerAnalytics(config *AnalyticsConfig, logger *errorhandling.Logger) (*PlayerAnalytics, error) {
	if config == nil {
		config = &AnalyticsConfig{
			RetentionDays:      90,
			SessionTimeout:     30 * time.Minute,
			EventBufferSize:    10000,
			ProcessingInterval: 5 * time.Minute,
			EnableRealTime:     true,
			EnablePredictions:  true,
			CohortAnalysisDays: 30,
			ChurnThresholdDays: 7,
		}
	}

	pa := &PlayerAnalytics{
		config:      config,
		logger:      logger,
		playerData:  make(map[string]*PlayerProfile),
		sessionData: make(map[string][]*GameSession),
		eventBuffer: make(chan *PlayerEvent, config.EventBufferSize),
		metrics:     &AnalyticsMetrics{},
		shutdownChan: make(chan struct{}),
	}

	// Start background processing
	pa.startBackgroundProcessing()

	logger.Infow("Player analytics system initialized",
		"retention_days", config.RetentionDays,
		"buffer_size", config.EventBufferSize)

	return pa, nil
}

// RecordPlayerEvent records a player event for analytics
func (pa *PlayerAnalytics) RecordPlayerEvent(event *PlayerEvent) error {
	select {
	case pa.eventBuffer <- event:
		return nil
	default:
		return errorhandling.NewValidationError("EVENT_BUFFER_FULL", "Event buffer is full")
	}
}

// RecordGameSession records a game session
func (pa *PlayerAnalytics) RecordGameSession(session *GameSession) error {
	pa.mu.Lock()
	defer pa.mu.Unlock()

	// Store session
	if pa.sessionData[session.PlayerID] == nil {
		pa.sessionData[session.PlayerID] = make([]*GameSession, 0)
	}
	pa.sessionData[session.PlayerID] = append(pa.sessionData[session.PlayerID], session)

	// Update player profile
	profile := pa.getOrCreatePlayerProfile(session.PlayerID)
	profile.LastSeen = time.Now()
	profile.TotalSessions++
	profile.TotalPlayTime += session.Duration
	profile.Region = session.Region

	// Update session statistics
	sessionCount := len(pa.sessionData[session.PlayerID])
	if sessionCount > 1 {
		totalDuration := time.Duration(0)
		for _, s := range pa.sessionData[session.PlayerID] {
			totalDuration += s.Duration
		}
		profile.AvgSessionLength = totalDuration / time.Duration(sessionCount)
	}

	// Calculate session frequency (sessions per day)
	daysSinceFirst := time.Since(profile.CreatedAt).Hours() / 24
	if daysSinceFirst > 0 {
		profile.SessionFrequency = float64(sessionCount) / daysSinceFirst
	}

	// Update gameplay metrics
	pa.updateGameplayMetrics(profile, session)

	pa.logger.Debugw("Game session recorded",
		"player_id", session.PlayerID,
		"session_id", session.SessionID,
		"duration", session.Duration)

	return nil
}

// GetPlayerProfile retrieves a player's profile
func (pa *PlayerAnalytics) GetPlayerProfile(playerID string) (*PlayerProfile, error) {
	pa.mu.RLock()
	defer pa.mu.RUnlock()

	profile, exists := pa.playerData[playerID]
	if !exists {
		return nil, errorhandling.NewNotFoundError("PLAYER_NOT_FOUND", "Player profile not found")
	}

	return profile, nil
}

// GetAnalyticsMetrics returns current analytics metrics
func (pa *PlayerAnalytics) GetAnalyticsMetrics() *AnalyticsMetrics {
	pa.mu.RLock()
	defer pa.mu.RUnlock()

	// Calculate real-time metrics
	metrics := &AnalyticsMetrics{
		TotalPlayers:     int64(len(pa.playerData)),
		RetentionRates:   make(map[string]float64),
		RevenueMetrics:   RevenueMetrics{},
		GameplayMetrics:  GameplayMetrics{},
		SocialMetrics:    SocialMetrics{},
	}

	// Calculate active players (seen in last 24 hours)
	activeThreshold := time.Now().Add(-24 * time.Hour)
	for _, profile := range pa.playerData {
		if profile.LastSeen.After(activeThreshold) {
			metrics.ActivePlayers++
		}
	}

	// Calculate retention rates
	metrics.RetentionRates = pa.calculateRetentionRates()

	// Calculate average session length
	totalSessions := 0
	totalDuration := time.Duration(0)
	for _, sessions := range pa.sessionData {
		for _, session := range sessions {
			totalSessions++
			totalDuration += session.Duration
		}
	}
	if totalSessions > 0 {
		metrics.AvgSessionLength = totalDuration / time.Duration(totalSessions)
	}

	return metrics
}

// GetCohortAnalysis performs cohort analysis
func (pa *PlayerAnalytics) GetCohortAnalysis(cohortDate time.Time, days int) (*CohortAnalysis, error) {
	pa.mu.RLock()
	defer pa.mu.RUnlock()

	cohort := &CohortAnalysis{
		CohortDate:    cohortDate,
		RetentionData: make(map[int]float64),
		EngagementTrend: make([]EngagementPoint, 0),
	}

	cohortStart := cohortDate
	cohortEnd := cohortDate.AddDate(0, 0, days)

	// Find players in this cohort
	cohortPlayers := make([]*PlayerProfile, 0)
	for _, profile := range pa.playerData {
		if profile.CreatedAt.After(cohortStart) && profile.CreatedAt.Before(cohortEnd) {
			cohortPlayers = append(cohortPlayers, profile)
		}
	}

	cohort.CohortSize = len(cohortPlayers)

	// Calculate retention for each day
	for day := 1; day <= days; day++ {
		dayStart := cohortDate.AddDate(0, 0, day-1)
		dayEnd := cohortDate.AddDate(0, 0, day)

		activePlayers := 0
		for _, profile := range cohortPlayers {
			if profile.LastSeen.After(dayStart) && profile.LastSeen.Before(dayEnd) {
				activePlayers++
			}
		}

		if cohort.CohortSize > 0 {
			cohort.RetentionData[day] = float64(activePlayers) / float64(cohort.CohortSize) * 100
		}
	}

	return cohort, nil
}

// PredictChurn predicts churn risk for players
func (pa *PlayerAnalytics) PredictChurn(playerID string) (float64, error) {
	pa.mu.RLock()
	profile, exists := pa.playerData[playerID]
	pa.mu.RUnlock()

	if !exists {
		return 0, errorhandling.NewNotFoundError("PLAYER_NOT_FOUND", "Player not found")
	}

	// Simple churn prediction based on activity patterns
	daysSinceLastSeen := time.Since(profile.LastSeen).Hours() / 24

	// Risk factors
	riskScore := 0.0

	// Days since last seen
	if daysSinceLastSeen > pa.config.ChurnThresholdDays {
		riskScore += 0.3
	}

	// Session frequency
	if profile.SessionFrequency < 0.1 { // Less than 1 session per 10 days
		riskScore += 0.2
	}

	// Total sessions (new players more likely to churn)
	if profile.TotalSessions < 5 {
		riskScore += 0.2
	}

	// Time since registration
	daysSinceRegistration := time.Since(profile.CreatedAt).Hours() / 24
	if daysSinceRegistration < 7 {
		riskScore += 0.1 // New players have higher initial churn risk
	}

	// Social engagement
	if profile.SocialEngagement < 0.3 {
		riskScore += 0.2
	}

	// Clamp risk score
	if riskScore > 1.0 {
		riskScore = 1.0
	}

	profile.ChurnRisk = riskScore

	// Predict churn date if risk is high
	if riskScore > 0.7 {
		daysToChurn := int((1.0 - riskScore) * 30) // Estimate based on risk
		predictedDate := time.Now().AddDate(0, 0, daysToChurn)
		profile.PredictedChurnDate = &predictedDate
	}

	return riskScore, nil
}

// GetEngagementInsights provides engagement insights and recommendations
func (pa *PlayerAnalytics) GetEngagementInsights() map[string]interface{} {
	pa.mu.RLock()
	defer pa.mu.RUnlock()

	insights := map[string]interface{}{
		"total_players": len(pa.playerData),
		"insights":      []string{},
		"recommendations": []string{},
	}

	// Analyze engagement patterns
	totalEngagement := 0.0
	playerCount := 0

	for _, profile := range pa.playerData {
		if profile.EngagementScore > 0 {
			totalEngagement += profile.EngagementScore
			playerCount++
		}
	}

	if playerCount > 0 {
		avgEngagement := totalEngagement / float64(playerCount)
		insights["average_engagement"] = avgEngagement

		if avgEngagement < 30 {
			insights["insights"] = append(insights["insights"].([]string), "Low overall engagement detected")
			insights["recommendations"] = append(insights["recommendations"].([]string),
				"Implement engagement campaigns",
				"Add daily rewards system",
				"Improve onboarding experience")
		}
	}

	// Analyze churn risk
	highRiskPlayers := 0
	for _, profile := range pa.playerData {
		if profile.ChurnRisk > 0.7 {
			highRiskPlayers++
		}
	}

	if highRiskPlayers > 0 {
		insights["high_risk_players"] = highRiskPlayers
		insights["insights"] = append(insights["insights"].([]string),
			fmt.Sprintf("%d players at high churn risk", highRiskPlayers))
		insights["recommendations"] = append(insights["recommendations"].([]string),
			"Implement retention campaigns",
			"Send personalized offers to at-risk players",
			"Improve customer support response time")
	}

	return insights
}

// Helper methods

func (pa *PlayerAnalytics) getOrCreatePlayerProfile(playerID string) *PlayerProfile {
	profile, exists := pa.playerData[playerID]
	if !exists {
		profile = &PlayerProfile{
			PlayerID:     playerID,
			CreatedAt:    time.Now(),
			LastSeen:     time.Now(),
			RetentionRate: make(map[string]float64),
			CustomMetrics: make(map[string]interface{}),
			SessionHistory: make([]*GameSession, 0),
			EventHistory:   make([]*PlayerEvent, 0),
		}
		pa.playerData[playerID] = profile
	}
	return profile
}

func (pa *PlayerAnalytics) updateGameplayMetrics(profile *PlayerProfile, session *GameSession) {
	// Update win rate
	if session.Result == "win" {
		wins := int(profile.WinRate * float64(profile.TotalSessions))
		profile.WinRate = float64(wins + 1) / float64(profile.TotalSessions + 1)
	}

	// Update K/D ratio
	totalKills := 0
	totalDeaths := 0
	for _, s := range pa.sessionData[profile.PlayerID] {
		totalKills += s.Kills
		totalDeaths += s.Deaths
	}
	totalDeaths += session.Deaths
	if totalDeaths > 0 {
		profile.KDRatio = float64(totalKills) / float64(totalDeaths)
	}

	// Update favorite game mode
	modeCount := make(map[string]int)
	for _, s := range pa.sessionData[profile.PlayerID] {
		modeCount[s.GameMode]++
	}
	modeCount[session.GameMode]++

	maxMode := ""
	maxCount := 0
	for mode, count := range modeCount {
		if count > maxCount {
			maxCount = count
			maxMode = mode
		}
	}
	profile.FavoriteGameMode = maxMode

	// Calculate engagement score (simplified)
	engagementFactors := []float64{
		profile.SessionFrequency * 10,     // Frequency factor
		float64(profile.TotalSessions),     // Session count factor
		profile.WinRate * 100,             // Performance factor
		float64(profile.Level),            // Progression factor
		profile.SocialEngagement * 100,    // Social factor
	}

	avgEngagement := 0.0
	for _, factor := range engagementFactors {
		avgEngagement += factor
	}
	avgEngagement /= float64(len(engagementFactors))

	profile.EngagementScore = math.Min(avgEngagement, 100.0)

	// Determine loyalty tier
	switch {
	case profile.EngagementScore >= 80:
		profile.LoyaltyTier = "platinum"
	case profile.EngagementScore >= 60:
		profile.LoyaltyTier = "gold"
	case profile.EngagementScore >= 40:
		profile.LoyaltyTier = "silver"
	default:
		profile.LoyaltyTier = "bronze"
	}
}

func (pa *PlayerAnalytics) calculateRetentionRates() map[string]float64 {
	rates := make(map[string]float64)

	// Calculate retention for days 1, 3, 7, 14, 30
	days := []int{1, 3, 7, 14, 30}

	for _, day := range days {
		retained := 0
		total := 0

		for _, profile := range pa.playerData {
			// Only consider players who have been registered long enough
			daysSinceRegistration := int(time.Since(profile.CreatedAt).Hours() / 24)
			if daysSinceRegistration >= day {
				total++
				// Check if player was active within the retention period
				retentionThreshold := profile.CreatedAt.AddDate(0, 0, day)
				if profile.LastSeen.After(retentionThreshold) {
					retained++
				}
			}
		}

		if total > 0 {
			rates[fmt.Sprintf("day_%d", day)] = float64(retained) / float64(total) * 100
		}
	}

	return rates
}

func (pa *PlayerAnalytics) startBackgroundProcessing() {
	// Event processing worker
	pa.wg.Add(1)
	go func() {
		defer pa.wg.Done()
		for {
			select {
			case event := <-pa.eventBuffer:
				pa.processPlayerEvent(event)
			case <-pa.shutdownChan:
				return
			}
		}
	}()

	// Metrics calculation worker
	pa.wg.Add(1)
	go func() {
		defer pa.wg.Done()
		ticker := time.NewTicker(pa.config.ProcessingInterval)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				pa.updateAnalyticsMetrics()
			case <-pa.shutdownChan:
				return
			}
		}
	}()
}

func (pa *PlayerAnalytics) processPlayerEvent(event *PlayerEvent) {
	pa.mu.Lock()
	defer pa.mu.Unlock()

	profile := pa.getOrCreatePlayerProfile(event.PlayerID)

	// Add to event history
	profile.EventHistory = append(profile.EventHistory, event)

	// Process event based on type
	switch event.EventType {
	case "login":
		profile.LastSeen = event.Timestamp
	case "logout":
		profile.LastSeen = event.Timestamp
	case "purchase":
		if amount, ok := event.EventData["amount"].(float64); ok {
			profile.TotalSpent += amount
		}
	case "friend_added":
		profile.FriendsCount++
	case "guild_joined":
		if guildID, ok := event.EventData["guild_id"].(string); ok {
			profile.GuildID = guildID
		}
	}

	// Keep only recent events (last 1000)
	if len(profile.EventHistory) > 1000 {
		profile.EventHistory = profile.EventHistory[len(profile.EventHistory)-1000:]
	}
}

func (pa *PlayerAnalytics) updateAnalyticsMetrics() {
	pa.mu.Lock()
	defer pa.mu.Unlock()

	// Update global metrics
	metrics := pa.metrics
	metrics.TotalPlayers = int64(len(pa.playerData))

	// Calculate active players (last 24h)
	activeThreshold := time.Now().Add(-24 * time.Hour)
	activeCount := int64(0)
	newToday := int64(0)
	returning := int64(0)

	today := time.Now().Truncate(24 * time.Hour)

	for _, profile := range pa.playerData {
		if profile.LastSeen.After(activeThreshold) {
			activeCount++
		}

		if profile.CreatedAt.After(today) {
			newToday++
		}

		// Simple returning player logic (played yesterday and today)
		yesterday := today.AddDate(0, 0, -1)
		if profile.LastSeen.After(today) && pa.hasPlayedInRange(profile, yesterday, today) {
			returning++
		}
	}

	metrics.ActivePlayers = activeCount
	metrics.NewPlayersToday = newToday
	metrics.ReturningPlayers = returning
}

// hasPlayedInRange checks if player was active in a date range
func (pa *PlayerAnalytics) hasPlayedInRange(profile *PlayerProfile, start, end time.Time) bool {
	for _, session := range pa.sessionData[profile.PlayerID] {
		if session.StartTime.After(start) && session.StartTime.Before(end) {
			return true
		}
	}
	return false
}

// Shutdown gracefully shuts down the analytics system
func (pa *PlayerAnalytics) Shutdown(ctx context.Context) error {
	close(pa.shutdownChan)

	done := make(chan struct{})
	go func() {
		pa.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		pa.logger.Info("Player analytics system shut down gracefully")
		return nil
	case <-ctx.Done():
		pa.logger.Warn("Player analytics system shutdown timed out")
		return ctx.Err()
	}
}
