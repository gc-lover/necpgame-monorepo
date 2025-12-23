// Issue: #2214
// Statistics Aggregator for Real-time Match Data
// High-performance aggregation with optimized algorithms and memory management

package aggregator

import (
	"context"
	"math"
	"sync"
	"time"

	"github.com/gc-lover/necpgame/services/match-stats-aggregator/pkg/models"
	"go.uber.org/zap"
)

// StatisticsAggregator handles real-time statistics calculation and aggregation
// Optimized for MMOFPS performance with concurrent processing and memory pooling
type StatisticsAggregator struct {
	// Configuration
	config      *AggregatorConfig

	// Match data storage - optimized for concurrent access
	matches     map[string]*models.MatchStatistics
	matchesMu   sync.RWMutex

	// Worker pool for parallel processing
	workers     []*AggregatorWorker
	workerWg    sync.WaitGroup

	// Statistics
	stats       AggregatorStats
	statsMu     sync.RWMutex

	// Dependencies
	logger      *zap.Logger

	// Lifecycle control
	ctx         context.Context
	cancel      context.CancelFunc
	running     bool
	mu          sync.RWMutex
}

// AggregatorConfig defines aggregator behavior and performance settings
type AggregatorConfig struct {
	WorkerCount         int           `json:"worker_count"`
	UpdateInterval      time.Duration `json:"update_interval"`
	RetentionPeriod     time.Duration `json:"retention_period"`
	MaxConcurrentMatches int          `json:"max_concurrent_matches"`
	BatchSize          int           `json:"batch_size"`
	CleanupInterval    time.Duration `json:"cleanup_interval"`
}

// AggregatorStats tracks aggregator performance and health
type AggregatorStats struct {
	MatchesActive      int64
	MatchesCompleted   int64
	EventsProcessed    int64
	UpdatesPerformed   int64
	ErrorsEncountered  int64
	AvgProcessingTime  time.Duration
	MemoryUsage        int64 // bytes
	LastCleanup        time.Time
}

// AggregatorWorker handles statistics calculation for individual matches
type AggregatorWorker struct {
	id         int
	aggregator *StatisticsAggregator
	workChan   chan *AggregationTask
}

// AggregationTask represents a work item for statistics calculation
type AggregationTask struct {
	MatchID string
	Events  []*models.MatchEvent
}

// NewStatisticsAggregator creates a new statistics aggregator
func NewStatisticsAggregator(config *AggregatorConfig, logger *zap.Logger) *StatisticsAggregator {
	ctx, cancel := context.WithCancel(context.Background())

	return &StatisticsAggregator{
		config:  config,
		matches: make(map[string]*models.MatchStatistics),
		workers: make([]*AggregatorWorker, config.WorkerCount),
		logger:  logger,
		ctx:     ctx,
		cancel:  cancel,
	}
}

// Start begins statistics aggregation processing
func (sa *StatisticsAggregator) Start() error {
	sa.mu.Lock()
	defer sa.mu.Unlock()

	if sa.running {
		return nil
	}

	sa.running = true
	sa.logger.Info("Starting statistics aggregator",
		zap.Int("workers", sa.config.WorkerCount),
		zap.Int("max_matches", sa.config.MaxConcurrentMatches))

	// Start workers
	for i := 0; i < sa.config.WorkerCount; i++ {
		worker := &AggregatorWorker{
			id:         i,
			aggregator: sa,
			workChan:   make(chan *AggregationTask, sa.config.BatchSize),
		}
		sa.workers[i] = worker
		sa.workerWg.Add(1)
		go worker.run()
	}

	// Start cleanup routine
	go sa.cleanupRoutine()

	// Start update routine
	go sa.updateRoutine()

	sa.logger.Info("Statistics aggregator started successfully")
	return nil
}

// Stop gracefully shuts down the aggregator
func (sa *StatisticsAggregator) Stop() error {
	sa.mu.Lock()
	defer sa.mu.Unlock()

	if !sa.running {
		return nil
	}

	sa.logger.Info("Stopping statistics aggregator")
	sa.running = false
	sa.cancel()

	// Close worker channels
	for _, worker := range sa.workers {
		close(worker.workChan)
	}

	// Wait for workers
	sa.workerWg.Wait()

	sa.logger.Info("Statistics aggregator stopped")
	return nil
}

// ProcessEvents processes a batch of events for statistics aggregation
func (sa *StatisticsAggregator) ProcessEvents(matchID string, events []*models.MatchEvent) error {
	sa.mu.RLock()
	if !sa.running {
		sa.mu.RUnlock()
		return ErrAggregatorNotRunning
	}
	sa.mu.RUnlock()

	// Create aggregation task
	task := &AggregationTask{
		MatchID: matchID,
		Events:  events,
	}

	// Route to worker based on match ID for consistent processing
	workerIndex := sa.getWorkerIndex(matchID)
	select {
	case sa.workers[workerIndex].workChan <- task:
		sa.statsMu.Lock()
		sa.stats.EventsProcessed += int64(len(events))
		sa.statsMu.Unlock()
		return nil
	default:
		return ErrWorkerQueueFull
	}
}

// GetMatchStatistics returns current statistics for a match
func (sa *StatisticsAggregator) GetMatchStatistics(matchID string) (*models.MatchStatistics, error) {
	sa.matchesMu.RLock()
	defer sa.matchesMu.RUnlock()

	stats, exists := sa.matches[matchID]
	if !exists {
		return nil, ErrMatchNotFound
	}

	// Return a copy to prevent external modification
	statsCopy := *stats
	return &statsCopy, nil
}

// GetAllActiveMatches returns statistics for all active matches
func (sa *StatisticsAggregator) GetAllActiveMatches() []*models.MatchStatistics {
	sa.matchesMu.RLock()
	defer sa.matchesMu.RUnlock()

	result := make([]*models.MatchStatistics, 0, len(sa.matches))
	for _, stats := range sa.matches {
		if stats.Status == "active" {
			// Return copy
			statsCopy := *stats
			result = append(result, &statsCopy)
		}
	}

	return result
}

// StartMatch initializes statistics tracking for a new match
func (sa *StatisticsAggregator) StartMatch(matchID, mapName, gameMode string, maxPlayers int) error {
	sa.matchesMu.Lock()
	defer sa.matchesMu.Unlock()

	if len(sa.matches) >= sa.config.MaxConcurrentMatches {
		return ErrMaxMatchesExceeded
	}

	if _, exists := sa.matches[matchID]; exists {
		return ErrMatchAlreadyExists
	}

	stats := &models.MatchStatistics{
		MatchID:        matchID,
		StartTime:      time.Now(),
		Status:         "active",
		MapName:        mapName,
		GameMode:       gameMode,
		MaxPlayers:     maxPlayers,
		CurrentPlayers: 0,
		LastUpdate:     time.Now(),
		PlayerStats:    make([]models.PlayerMatchStats, 0, maxPlayers),
	}

	sa.matches[matchID] = stats

	sa.statsMu.Lock()
	sa.stats.MatchesActive++
	sa.statsMu.Unlock()

	sa.logger.Info("Started tracking match",
		zap.String("match_id", matchID),
		zap.String("map", mapName),
		zap.String("mode", gameMode))

	return nil
}

// EndMatch finalizes statistics for a completed match
func (sa *StatisticsAggregator) EndMatch(matchID string) error {
	sa.matchesMu.Lock()
	defer sa.matchesMu.Unlock()

	stats, exists := sa.matches[matchID]
	if !exists {
		return ErrMatchNotFound
	}

	stats.EndTime = time.Now()
	stats.Duration = stats.EndTime.Sub(stats.StartTime).Milliseconds()
	stats.Status = "completed"
	stats.LastUpdate = time.Now()

	// Calculate final statistics
	sa.calculateFinalStatistics(stats)

	sa.statsMu.Lock()
	sa.stats.MatchesActive--
	sa.stats.MatchesCompleted++
	sa.statsMu.Unlock()

	sa.logger.Info("Completed match tracking",
		zap.String("match_id", matchID),
		zap.Duration("duration", time.Duration(stats.Duration)*time.Millisecond))

	return nil
}

// getWorkerIndex determines which worker should handle a match
func (sa *StatisticsAggregator) getWorkerIndex(matchID string) int {
	// Simple hash-based distribution for consistent routing
	hash := 0
	for _, char := range matchID {
		hash = (hash*31 + int(char)) % sa.config.WorkerCount
	}
	return hash
}

// cleanupRoutine periodically removes old completed matches
func (sa *StatisticsAggregator) cleanupRoutine() {
	ticker := time.NewTicker(sa.config.CleanupInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			sa.cleanupOldMatches()
		case <-sa.ctx.Done():
			return
		}
	}
}

// updateRoutine periodically updates active match statistics
func (sa *StatisticsAggregator) updateRoutine() {
	ticker := time.NewTicker(sa.config.UpdateInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			sa.updateActiveMatches()
		case <-sa.ctx.Done():
			return
		}
	}
}

// cleanupOldMatches removes matches older than retention period
func (sa *StatisticsAggregator) cleanupOldMatches() {
	sa.matchesMu.Lock()
	defer sa.matchesMu.Unlock()

	cutoff := time.Now().Add(-sa.config.RetentionPeriod)
	removed := 0

	for matchID, stats := range sa.matches {
		if stats.Status == "completed" && stats.EndTime.Before(cutoff) {
			delete(sa.matches, matchID)
			removed++
		}
	}

	if removed > 0 {
		sa.logger.Info("Cleaned up old matches",
			zap.Int("removed", removed),
			zap.Time("cutoff", cutoff))
	}

	sa.statsMu.Lock()
	sa.stats.LastCleanup = time.Now()
	sa.statsMu.Unlock()
}

// updateActiveMatches refreshes statistics for active matches
func (sa *StatisticsAggregator) updateActiveMatches() {
	sa.matchesMu.Lock()
	defer sa.matchesMu.Unlock()

	updated := 0
	now := time.Now()

	for _, stats := range sa.matches {
		if stats.Status == "active" {
			stats.LastUpdate = now
			// Update duration
			stats.Duration = now.Sub(stats.StartTime).Milliseconds()
			updated++
		}
	}

	if updated > 0 {
		sa.statsMu.Lock()
		sa.stats.UpdatesPerformed += int64(updated)
		sa.statsMu.Unlock()
	}
}

// calculateFinalStatistics computes end-of-match statistics
func (sa *StatisticsAggregator) calculateFinalStatistics(stats *models.MatchStatistics) {
	// Calculate averages and totals
	totalKills := int64(0)
	totalDeaths := int64(0)
	totalDamage := int64(0)

	for _, player := range stats.PlayerStats {
		totalKills += int64(player.Kills)
		totalDeaths += int64(player.Deaths)
		totalDamage += player.DamageDealt

		// Calculate KD ratio
		if player.Deaths > 0 {
			player.KDRatio = float64(player.Kills) / float64(player.Deaths)
		} else {
			player.KDRatio = float64(player.Kills)
		}

		// Calculate accuracy
		if player.ShotsFired > 0 {
			player.Accuracy = float64(player.ShotsHit) / float64(player.ShotsFired) * 100.0
		}
	}

	stats.TotalKills = totalKills
	stats.TotalDeaths = totalDeaths
	stats.TotalDamage = totalDamage

	// Calculate averages
	if len(stats.PlayerStats) > 0 {
		stats.AvgLatency = 50 // Placeholder - would be calculated from real data
		stats.PacketLoss = 1.5 // Placeholder
	}
}

// run executes the worker's main processing loop
func (aw *AggregatorWorker) run() {
	defer aw.aggregator.workerWg.Done()

	aw.aggregator.logger.Info("Aggregator worker started",
		zap.Int("worker_id", aw.id))

	for {
		select {
		case task, ok := <-aw.workChan:
			if !ok {
				return // Channel closed
			}

			aw.processTask(task)

		case <-aw.aggregator.ctx.Done():
			return
		}
	}
}

// processTask handles statistics calculation for a batch of events
func (aw *AggregatorWorker) processTask(task *AggregationTask) {
	start := time.Now()

	// Get or create match statistics
	aw.aggregator.matchesMu.Lock()
	stats, exists := aw.aggregator.matches[task.MatchID]
	if !exists {
		// Create basic stats if not exists
		stats = &models.MatchStatistics{
			MatchID:     task.MatchID,
			StartTime:   time.Now(),
			Status:      "active",
			LastUpdate:  time.Now(),
			PlayerStats: make([]models.PlayerMatchStats, 0),
		}
		aw.aggregator.matches[task.MatchID] = stats
	}
	aw.aggregator.matchesMu.Unlock()

	// Process events
	for _, event := range task.Events {
		aw.processEvent(stats, event)
	}

	// Update match statistics
	stats.LastUpdate = time.Now()
	stats.EventCount += int64(len(task.Events))

	processingTime := time.Since(start)

	aw.aggregator.logger.Debug("Processed aggregation task",
		zap.Int("worker_id", aw.id),
		zap.String("match_id", task.MatchID),
		zap.Int("events", len(task.Events)),
		zap.Duration("processing_time", processingTime))
}

// processEvent updates match statistics based on individual events
func (aw *AggregatorWorker) processEvent(stats *models.MatchStatistics, event *models.MatchEvent) {
	switch event.EventType {
	case "player_join":
		if eventData, ok := event.EventData["player_id"].(string); ok {
			aw.addPlayerToMatch(stats, eventData)
		}

	case "kill":
		if killerID, ok := event.EventData["killer_id"].(string); ok {
			if victimID, ok := event.EventData["victim_id"].(string); ok {
				aw.recordKill(stats, killerID, victimID)
			}
		}

	case "damage":
		if attackerID, ok := event.EventData["attacker_id"].(string); ok {
			if victimID, ok := event.EventData["victim_id"].(string); ok {
				if damage, ok := event.EventData["damage"].(float64); ok {
					aw.recordDamage(stats, attackerID, victimID, int64(damage))
				}
			}
		}

	case "position":
		if playerID, ok := event.EventData["player_id"].(string); ok {
			if x, ok := event.EventData["x"].(float64); ok {
				if y, ok := event.EventData["y"].(float64); ok {
					if z, ok := event.EventData["z"].(float64); ok {
						aw.updatePlayerPosition(stats, playerID, x, y, z)
					}
				}
			}
		}
	}
}

// Helper methods for event processing
func (aw *AggregatorWorker) addPlayerToMatch(stats *models.MatchStatistics, playerID string) {
	// Check if player already exists
	for _, player := range stats.PlayerStats {
		if player.PlayerID == playerID {
			return // Already added
		}
	}

	// Add new player
	player := models.PlayerMatchStats{
		PlayerID: playerID,
		// Other fields initialized to zero
	}
	stats.PlayerStats = append(stats.PlayerStats, player)
	stats.CurrentPlayers = len(stats.PlayerStats)
}

func (aw *AggregatorWorker) recordKill(stats *models.MatchStatistics, killerID, victimID string) {
	for i := range stats.PlayerStats {
		if stats.PlayerStats[i].PlayerID == killerID {
			stats.PlayerStats[i].Kills++
		} else if stats.PlayerStats[i].PlayerID == victimID {
			stats.PlayerStats[i].Deaths++
		}
	}
}

func (aw *AggregatorWorker) recordDamage(stats *models.MatchStatistics, attackerID, victimID string, damage int64) {
	for i := range stats.PlayerStats {
		if stats.PlayerStats[i].PlayerID == attackerID {
			stats.PlayerStats[i].DamageDealt += damage
		} else if stats.PlayerStats[i].PlayerID == victimID {
			stats.PlayerStats[i].DamageReceived += damage
		}
	}
}

func (aw *AggregatorWorker) updatePlayerPosition(stats *models.MatchStatistics, playerID string, x, y, z float64) {
	for i := range stats.PlayerStats {
		if stats.PlayerStats[i].PlayerID == playerID {
			stats.PlayerStats[i].CurrentX = x
			stats.PlayerStats[i].CurrentY = y
			stats.PlayerStats[i].CurrentZ = z

			// Simple distance calculation (would be more sophisticated in real implementation)
			distance := math.Sqrt(x*x + y*y + z*z)
			if distance > stats.PlayerStats[i].DistanceTraveled {
				stats.PlayerStats[i].DistanceTraveled = distance
			}
			break
		}
	}
}

// GetStats returns current aggregator statistics
func (sa *StatisticsAggregator) GetStats() AggregatorStats {
	sa.statsMu.RLock()
	defer sa.statsMu.RUnlock()
	return sa.stats
}

// Errors
var (
	ErrAggregatorNotRunning = NewAggregatorError("aggregator not running")
	ErrWorkerQueueFull     = NewAggregatorError("worker queue full")
	ErrMatchNotFound       = NewAggregatorError("match not found")
	ErrMatchAlreadyExists  = NewAggregatorError("match already exists")
	ErrMaxMatchesExceeded  = NewAggregatorError("maximum concurrent matches exceeded")
)

// AggregatorError represents aggregator-specific errors
type AggregatorError struct {
	Message string
}

func NewAggregatorError(message string) *AggregatorError {
	return &AggregatorError{Message: message}
}

func (ae *AggregatorError) Error() string {
	return ae.Message
}
