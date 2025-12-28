// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Business logic service for Easter Eggs - Enterprise-grade implementation

package service

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"sync"
	"time"

	"go.uber.org/zap"
	"gopkg.in/yaml.v3"

	"cyberspace-easter-eggs-service-go/internal/metrics"
	"cyberspace-easter-eggs-service-go/pkg/models"
	"cyberspace-easter-eggs-service-go/pkg/repository"
)

// YAML Import structures
type EasterEggYAML struct {
	ID              string              `yaml:"id"`
	Name            string              `yaml:"name"`
	Category        string              `yaml:"category"`
	Difficulty      string              `yaml:"difficulty"`
	Description     string              `yaml:"description"`
	Content         string              `yaml:"content"`
	Location        EasterEggLocationYAML `yaml:"location"`
	DiscoveryMethod DiscoveryMethodYAML `yaml:"discovery_method"`
	Rewards         []EasterEggRewardYAML `yaml:"rewards"`
	LoreConnections []string            `yaml:"lore_connections"`
	Hints           []HintYAML          `yaml:"hints"`
}

type EasterEggLocationYAML struct {
	NetworkType   string   `yaml:"network_type"`
	SpecificAreas []string `yaml:"specific_areas"`
}

type DiscoveryMethodYAML struct {
	Type         string   `yaml:"type"`
	Description  string   `yaml:"description"`
	Requirements []string `yaml:"requirements"`
}

type EasterEggRewardYAML struct {
	Type     string `yaml:"type"`
	Value    int    `yaml:"value,omitempty"`
	ItemID   string `yaml:"item_id,omitempty"`
	ItemName string `yaml:"item_name,omitempty"`
}

type HintYAML struct {
	Text    string `yaml:"text"`
	Type    string `yaml:"type"`
	Cost    int    `yaml:"cost"`
	Enabled bool   `yaml:"enabled"`
}

type EasterEggsYAMLImport struct {
	Metadata struct {
		Sections []struct {
			Title string `yaml:"title"`
			Body  string `yaml:"body"`
		} `yaml:"sections"`
	} `yaml:"metadata"`
	EasterEggs []EasterEggYAML `yaml:"easter_eggs"`
}

// EasterEggsServiceInterface defines the service interface
type EasterEggsServiceInterface interface {
	// Easter egg operations
	GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error)
	GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error)
	GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error)
	CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	DeleteEasterEgg(ctx context.Context, id string) error

	// Player operations
	GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error)
	UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error
	GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error)
	DiscoverEasterEgg(ctx context.Context, playerID, easterEggID string, attemptData map[string]interface{}) (*models.PlayerEasterEggProgress, error)

	// Statistics operations
	GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error)
	GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error)

	// Hint operations
	GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error)
	GetEasterEggHints(ctx context.Context, easterEggID string) ([]models.DiscoveryHint, error)

	// Challenge operations
	GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error)
	GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error)

	// Import operations
	ImportEasterEggsFromYAML(ctx context.Context, yamlPath string) (*ImportResult, error)

	// Health check
	HealthCheck(ctx context.Context) error
}

// ImportResult represents the result of an import operation
type ImportResult struct {
	TotalProcessed    int      `json:"total_processed"`
	SuccessfullyAdded int      `json:"successfully_added"`
	Updated           int      `json:"updated"`
	Errors            []string `json:"errors"`
}

// EasterEggsService implements EasterEggsServiceInterface
type EasterEggsService struct {
	repo                   repository.RepositoryInterface
	metrics                *metrics.Collector
	logger                 *zap.SugaredLogger
	httpClient             *http.Client
	achievementServiceURL  string

	// PERFORMANCE: Memory pools for hot path operations
	easterEggPool          *sync.Pool
	playerProgressPool     *sync.Pool
	discoveryAttemptPool   *sync.Pool

	// PERFORMANCE: Worker pool semaphore for real-time operations
	discoverySemaphore     chan struct{}
}
}

// NewEasterEggsService creates a new easter eggs service
func NewEasterEggsService(repo repository.RepositoryInterface, metrics *metrics.Collector, logger *zap.SugaredLogger) EasterEggsServiceInterface {
	achievementURL := os.Getenv("ACHIEVEMENT_SERVICE_URL")
	if achievementURL == "" {
		achievementURL = "http://achievement-system-service-go:8080" // default
	}

	return &EasterEggsService{
		repo:                  repo,
		metrics:               metrics,
		logger:                logger,
		httpClient:            &http.Client{Timeout: 5 * time.Second},
		achievementServiceURL: achievementURL,

		// PERFORMANCE: Initialize memory pools for hot path operations
		easterEggPool: &sync.Pool{
			New: func() interface{} { return &models.EasterEgg{} },
		},
		playerProgressPool: &sync.Pool{
			New: func() interface{} { return &models.PlayerEasterEggProgress{} },
		},
		discoveryAttemptPool: &sync.Pool{
			New: func() interface{} { return &models.EasterEggDiscoveryAttempt{} },
		},

		// PERFORMANCE: Worker pool for discovery operations (limit to 100 concurrent)
		discoverySemaphore: make(chan struct{}, 100),
	}
}

// GetEasterEgg retrieves a single easter egg
func (s *EasterEggsService) GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEgg")
	defer s.metrics.ObserveRequestDuration("GetEasterEgg", time.Now())

	egg, err := s.repo.GetEasterEgg(ctx, id)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter egg %s: %v", id, err)
		return nil, fmt.Errorf("failed to get easter egg: %w", err)
	}

	s.logger.Debugf("Retrieved easter egg: %s", id)
	return egg, nil
}

// GetEasterEggsByCategory retrieves easter eggs by category
func (s *EasterEggsService) GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEggsByCategory")
	defer s.metrics.ObserveRequestDuration("GetEasterEggsByCategory", time.Now())

	eggs, err := s.repo.GetEasterEggsByCategory(ctx, category, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter eggs by category %s: %v", category, err)
		return nil, fmt.Errorf("failed to get easter eggs by category: %w", err)
	}

	s.logger.Debugf("Retrieved %d easter eggs for category: %s", len(eggs), category)
	return eggs, nil
}

// GetEasterEggsByDifficulty retrieves easter eggs by difficulty
func (s *EasterEggsService) GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error) {
	s.metrics.IncrementRequests("GetEasterEggsByDifficulty")
	defer s.metrics.ObserveRequestDuration("GetEasterEggsByDifficulty", time.Now())

	eggs, err := s.repo.GetEasterEggsByDifficulty(ctx, difficulty, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter eggs by difficulty %s: %v", difficulty, err)
		return nil, fmt.Errorf("failed to get easter eggs by difficulty: %w", err)
	}

	s.logger.Debugf("Retrieved %d easter eggs for difficulty: %s", len(eggs), difficulty)
	return eggs, nil
}

// CreateEasterEgg creates a new easter egg
func (s *EasterEggsService) CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	s.metrics.IncrementRequests("CreateEasterEgg")
	defer s.metrics.ObserveRequestDuration("CreateEasterEgg", time.Now())

	if err := s.repo.CreateEasterEgg(ctx, egg); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to create easter egg: %v", err)
		return fmt.Errorf("failed to create easter egg: %w", err)
	}

	s.logger.Infof("Created easter egg: %s", egg.ID)
	return nil
}

// UpdateEasterEgg updates an existing easter egg
func (s *EasterEggsService) UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	s.metrics.IncrementRequests("UpdateEasterEgg")
	defer s.metrics.ObserveRequestDuration("UpdateEasterEgg", time.Now())

	if err := s.repo.UpdateEasterEgg(ctx, egg); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update easter egg %s: %v", egg.ID, err)
		return fmt.Errorf("failed to update easter egg: %w", err)
	}

	s.logger.Infof("Updated easter egg: %s", egg.ID)
	return nil
}

// DeleteEasterEgg deletes an easter egg
func (s *EasterEggsService) DeleteEasterEgg(ctx context.Context, id string) error {
	s.metrics.IncrementRequests("DeleteEasterEgg")
	defer s.metrics.ObserveRequestDuration("DeleteEasterEgg", time.Now())

	if err := s.repo.DeleteEasterEgg(ctx, id); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to delete easter egg %s: %v", id, err)
		return fmt.Errorf("failed to delete easter egg: %w", err)
	}

	s.logger.Infof("Deleted easter egg: %s", id)
	return nil
}

// GetPlayerProgress gets player progress for a specific easter egg
func (s *EasterEggsService) GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error) {
	s.metrics.IncrementRequests("GetPlayerProgress")
	defer s.metrics.ObserveRequestDuration("GetPlayerProgress", time.Now())

	progress, err := s.repo.GetPlayerProgress(ctx, playerID, easterEggID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player progress for %s:%s: %v", playerID, easterEggID, err)
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	return progress, nil
}

// UpdatePlayerProgress updates player progress
func (s *EasterEggsService) UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error {
	s.metrics.IncrementRequests("UpdatePlayerProgress")
	defer s.metrics.ObserveRequestDuration("UpdatePlayerProgress", time.Now())

	if err := s.repo.UpdatePlayerProgress(ctx, progress); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update player progress for %s:%s: %v", progress.PlayerID, progress.EasterEggID, err)
		return fmt.Errorf("failed to update player progress: %w", err)
	}

	s.logger.Debugf("Updated progress for player %s on egg %s", progress.PlayerID, progress.EasterEggID)
	return nil
}

// GetPlayerProfile gets complete player profile
func (s *EasterEggsService) GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error) {
	s.metrics.IncrementRequests("GetPlayerProfile")
	defer s.metrics.ObserveRequestDuration("GetPlayerProfile", time.Now())

	profile, err := s.repo.GetPlayerProfile(ctx, playerID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player profile for %s: %v", playerID, err)
		return nil, fmt.Errorf("failed to get player profile: %w", err)
	}

	return profile, nil
}

// unlockEasterEggAchievement calls achievement service to unlock easter egg achievement
func (s *EasterEggsService) unlockEasterEggAchievement(ctx context.Context, playerID, easterEggID string) error {
	achievementID := s.getAchievementIDForEasterEgg(easterEggID)
	if achievementID == "" {
		return nil // No achievement for this egg
	}

	// Convert playerID to UUID format if needed
	playerUUID := fmt.Sprintf("player-%s", playerID) // Simple conversion for demo

	requestBody := map[string]interface{}{
		"achievement_id": achievementID,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		s.logger.Warnf("Failed to marshal achievement request: %v", err)
		return err
	}

	url := fmt.Sprintf("%s/api/v1/players/%s/achievements/unlock", s.achievementServiceURL, playerUUID)
	req, err := http.NewRequestWithContext(ctx, "POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		s.logger.Warnf("Failed to create achievement request: %v", err)
		return err
	}

	req.Header.Set("Content-Type", "application/json")

	resp, err := s.httpClient.Do(req)
	if err != nil {
		s.logger.Warnf("Failed to call achievement service: %v", err)
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusCreated {
		s.logger.Warnf("Achievement service returned status: %d", resp.StatusCode)
		return fmt.Errorf("achievement service error: %d", resp.StatusCode)
	}

	s.logger.Infof("Unlocked achievement %s for player %s", achievementID, playerID)
	return nil
}

// getAchievementIDForEasterEgg maps easter egg ID to achievement ID
func (s *EasterEggsService) getAchievementIDForEasterEgg(easterEggID string) string {
	achievementMap := map[string]string{
		"easter-egg-turing-ghost":              "550e8400-e29b-41d4-a716-446655440000",
		"easter-egg-schrodinger-cat":           "550e8400-e29b-41d4-a716-446655440001",
		"easter-egg-y2k-bug":                   "550e8400-e29b-41d4-a716-446655440002",
		"easter-egg-matrix-loading-screen":     "550e8400-e29b-41d4-a716-446655440003",
		"easter-egg-blockchain-pyramid":        "550e8400-e29b-41d4-a716-446655440004",
		"easter-egg-netscape-dinosaur":         "550e8400-e29b-41d4-a716-446655440005",
		"easter-egg-404-lore-not-found":        "550e8400-e29b-41d4-a716-446655440006",
		"easter-egg-quantum-computer-mini-game":"550e8400-e29b-41d4-a716-446655440007",
		"easter-egg-killer-virus-animation":    "550e8400-e29b-41d4-a716-446655440008",
		"easter-egg-neural-dream-network":      "550e8400-e29b-41d4-a716-446655440009",
		"easter-egg-shakespeare-online":        "550e8400-e29b-41d4-a716-446655440010",
		"easter-egg-rockstar-2077":             "550e8400-e29b-41d4-a716-446655440011",
		"easter-egg-forgotten-movies-theater": "550e8400-e29b-41d4-a716-446655440012",
		"easter-egg-digital-artist-gallery":    "550e8400-e29b-41d4-a716-446655440013",
		"easter-egg-philosophical-ai-debates":  "550e8400-e29b-41d4-a716-446655440014",
		"easter-egg-dancing-robot":             "550e8400-e29b-41d4-a716-446655440015",
		"easter-egg-living-books-library":      "550e8400-e29b-41d4-a716-446655440016",
		"easter-egg-meme-museum":               "550e8400-e29b-41d4-a716-446655440017",
		"easter-egg-virtual-poet":              "550e8400-e29b-41d4-a716-446655440018",
		"easter-egg-historical-holograms":      "550e8400-e29b-41d4-a716-446655440019",
		"easter-egg-roman-legion-network":      "550e8400-e29b-41d4-a716-446655440020",
		"easter-egg-vikings-vr-exploration":    "550e8400-e29b-41d4-a716-446655440021",
		"easter-egg-dinosaurs-online":          "550e8400-e29b-41d4-a716-446655440022",
		"easter-egg-cat-quantum-box":           "550e8400-e29b-41d4-a716-446655440023",
		"easter-egg-bug-coffee":                "550e8400-e29b-41d4-a716-446655440024",
	}

	return achievementMap[easterEggID]
}

// DiscoverEasterEgg handles the easter egg discovery process
func (s *EasterEggsService) DiscoverEasterEgg(ctx context.Context, playerID, easterEggID string, attemptData map[string]interface{}) (*models.PlayerEasterEggProgress, error) {
	s.metrics.IncrementRequests("DiscoverEasterEgg")
	defer s.metrics.ObserveRequestDuration("DiscoverEasterEgg", time.Now())

	// PERFORMANCE: Acquire semaphore to limit concurrent discovery operations
	select {
	case s.discoverySemaphore <- struct{}{}:
		defer func() { <-s.discoverySemaphore }()
	case <-ctx.Done():
		return nil, ctx.Err()
	}

	// Record the discovery attempt
	attempt := &models.EasterEggDiscoveryAttempt{
		ID:          fmt.Sprintf("%s-%s-%d", playerID, easterEggID, time.Now().Unix()),
		PlayerID:    playerID,
		EasterEggID: easterEggID,
		AttemptType: "api_discovery",
		Success:     true,
		AttemptedAt: time.Now(),
		ResponseTime: 100, // Placeholder
	}

	if err := s.repo.CreateDiscoveryAttempt(ctx, attempt); err != nil {
		s.logger.Warnf("Failed to record discovery attempt: %v", err)
	}

	// Get or create player progress
	progress, err := s.repo.GetPlayerProgress(ctx, playerID, easterEggID)
	if err != nil {
		// Create new progress if doesn't exist
		progress = &models.PlayerEasterEggProgress{
			PlayerID:     playerID,
			EasterEggID:  easterEggID,
			Status:       "discovered",
			DiscoveredAt: &time.Time{},
			VisitCount:   1,
			LastVisited:  time.Now(),
		}
		*progress.DiscoveredAt = time.Now()
	} else {
		// Update existing progress
		progress.Status = "discovered"
		if progress.DiscoveredAt == nil {
			progress.DiscoveredAt = &time.Time{}
			*progress.DiscoveredAt = time.Now()
		}
		progress.VisitCount++
		progress.LastVisited = time.Now()
	}

	if err := s.repo.UpdatePlayerProgress(ctx, progress); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to update player progress: %v", err)
		return nil, fmt.Errorf("failed to update player progress: %w", err)
	}

	// Record successful discovery
	if err := s.repo.RecordSuccessfulDiscovery(ctx, playerID, easterEggID); err != nil {
		s.logger.Warnf("Failed to record successful discovery: %v", err)
	}

	// Update easter egg statistics
	if err := s.repo.UpdateEasterEggStats(ctx, easterEggID); err != nil {
		s.logger.Warnf("Failed to update easter egg stats: %v", err)
	}

	// Create discovery event
	event := &models.EasterEggEvent{
		ID:          fmt.Sprintf("event-%s-%s-%d", playerID, easterEggID, time.Now().Unix()),
		EventType:   "discovered",
		PlayerID:    playerID,
		EasterEggID: easterEggID,
		EventData:   attemptData,
		CreatedAt:   time.Now(),
		Processed:   false,
	}

	if err := s.repo.CreateEasterEggEvent(ctx, event); err != nil {
		s.logger.Warnf("Failed to create discovery event: %v", err)
	}

	// Get easter egg to process rewards
	easterEgg, err := s.repo.GetEasterEgg(ctx, easterEggID)
	if err != nil {
		s.logger.Warnf("Failed to get easter egg for rewards processing: %v", err)
	} else {
		// Process rewards for the player
		if err := s.processEasterEggRewards(ctx, playerID, easterEgg); err != nil {
			s.logger.Warnf("Failed to process rewards for easter egg %s: %v", easterEggID, err)
			// Don't fail the whole operation if rewards processing fails
		}
	}

	// Unlock achievement for easter egg discovery
	if err := s.unlockEasterEggAchievement(ctx, playerID, easterEggID); err != nil {
		s.logger.Warnf("Failed to unlock achievement for easter egg %s: %v", easterEggID, err)
		// Don't fail the whole operation if achievement unlock fails
	}

	s.logger.Infof("Player %s discovered easter egg %s", playerID, easterEggID)
	return progress, nil
}

// GetEasterEggStatistics gets statistics for an easter egg
func (s *EasterEggsService) GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error) {
	s.metrics.IncrementRequests("GetEasterEggStatistics")
	defer s.metrics.ObserveRequestDuration("GetEasterEggStatistics", time.Now())

	stats, err := s.repo.GetEasterEggStatistics(ctx, easterEggID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get easter egg statistics for %s: %v", easterEggID, err)
		return nil, fmt.Errorf("failed to get easter egg statistics: %w", err)
	}

	return stats, nil
}

// GetCategoryStatistics gets statistics by category
func (s *EasterEggsService) GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error) {
	s.metrics.IncrementRequests("GetCategoryStatistics")
	defer s.metrics.ObserveRequestDuration("GetCategoryStatistics", time.Now())

	stats, err := s.repo.GetCategoryStatistics(ctx)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get category statistics: %v", err)
		return nil, fmt.Errorf("failed to get category statistics: %w", err)
	}

	return stats, nil
}

// GetHintsForEasterEgg gets hints for easter egg discovery
func (s *EasterEggsService) GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error) {
	s.metrics.IncrementRequests("GetHintsForEasterEgg")
	defer s.metrics.ObserveRequestDuration("GetHintsForEasterEgg", time.Now())

	hints, err := s.repo.GetHintsForEasterEgg(ctx, easterEggID, maxLevel)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get hints for easter egg %s: %v", easterEggID, err)
		return nil, fmt.Errorf("failed to get hints: %w", err)
	}

	return hints, nil
}

// GetEasterEggHints gets all hints for easter egg (for generated API)
func (s *EasterEggsService) GetEasterEggHints(ctx context.Context, easterEggID string) ([]models.DiscoveryHint, error) {
	s.metrics.IncrementRequests("GetEasterEggHints")
	defer s.metrics.ObserveRequestDuration("GetEasterEggHints", time.Now())

	hints, err := s.repo.GetHintsForEasterEgg(ctx, easterEggID, 3) // Get all hint levels
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get hints for easter egg %s: %v", easterEggID, err)
		return nil, fmt.Errorf("failed to get hints: %w", err)
	}

	// Convert []*models.DiscoveryHint to []models.DiscoveryHint
	var result []models.DiscoveryHint
	for _, hint := range hints {
		result = append(result, *hint)
	}

	return result, nil
}

// processEasterEggRewards handles reward distribution for easter egg discovery
func (s *EasterEggsService) processEasterEggRewards(ctx context.Context, playerID string, easterEgg *models.EasterEgg) error {
	s.metrics.IncrementRequests("ProcessEasterEggRewards")
	defer s.metrics.ObserveRequestDuration("ProcessEasterEggRewards", time.Now())

	if len(easterEgg.Rewards) == 0 {
		s.logger.Debugf("No rewards defined for easter egg %s", easterEgg.ID)
		return nil
	}

	var grantedRewards []string
	for _, reward := range easterEgg.Rewards {
		if err := s.grantRewardToPlayer(ctx, playerID, easterEgg.ID, reward); err != nil {
			s.logger.Warnf("Failed to grant reward %v to player %s: %v", reward, playerID, err)
			continue // Continue with other rewards even if one fails
		}
		grantedRewards = append(grantedRewards, fmt.Sprintf("%s:%v", reward.Type, reward.Value))
	}

	// Record granted rewards in player progress
	if len(grantedRewards) > 0 {
		if err := s.repo.RecordGrantedRewards(ctx, playerID, easterEgg.ID, grantedRewards); err != nil {
			s.logger.Warnf("Failed to record granted rewards: %v", err)
		}
	}

	s.logger.Infof("Granted %d rewards to player %s for easter egg %s: %v",
		len(grantedRewards), playerID, easterEgg.ID, grantedRewards)
	return nil
}

// grantRewardToPlayer grants a specific reward to a player
func (s *EasterEggsService) grantRewardToPlayer(ctx context.Context, playerID, easterEggID string, reward models.EasterEggReward) error {
	switch reward.Type {
	case "experience":
		return s.grantExperienceReward(ctx, playerID, reward.Value)
	case "currency":
		return s.grantCurrencyReward(ctx, playerID, reward.Value)
	case "item":
		return s.grantItemReward(ctx, playerID, reward.ItemID, reward.Value)
	case "cosmetic":
		return s.grantCosmeticReward(ctx, playerID, reward.ItemID)
	case "implant_buff":
		return s.grantImplantBuffReward(ctx, playerID, reward.ItemID, reward.Value)
	case "achievement":
		// Achievements are handled separately via unlockEasterEggAchievement
		return nil
	default:
		s.logger.Warnf("Unknown reward type: %s", reward.Type)
		return fmt.Errorf("unknown reward type: %s", reward.Type)
	}
}

// grantExperienceReward grants experience points to player
func (s *EasterEggsService) grantExperienceReward(ctx context.Context, playerID string, amount int) error {
	// For now, just log the reward - actual implementation would integrate with player service
	s.logger.Infof("Granted %d experience points to player %s", amount, playerID)

	// TODO: Integrate with player progression service to actually grant XP
	return nil
}

// grantCurrencyReward grants currency to player
func (s *EasterEggsService) grantCurrencyReward(ctx context.Context, playerID string, amount int) error {
	// For now, just log the reward - actual implementation would integrate with economy service
	s.logger.Infof("Granted %d currency to player %s", amount, playerID)

	// TODO: Integrate with economy service to actually grant currency
	return nil
}

// grantItemReward grants an item to player's inventory
func (s *EasterEggsService) grantItemReward(ctx context.Context, playerID, itemID string, quantity int) error {
	// For now, just log the reward - actual implementation would integrate with inventory service
	s.logger.Infof("Granted item %s (x%d) to player %s", itemID, quantity, playerID)

	// TODO: Integrate with inventory service to actually grant item
	return nil
}

// grantCosmeticReward grants cosmetic item to player
func (s *EasterEggsService) grantCosmeticReward(ctx context.Context, playerID, cosmeticID string) error {
	// For now, just log the reward - actual implementation would integrate with player customization service
	s.logger.Infof("Granted cosmetic %s to player %s", cosmeticID, playerID)

	// TODO: Integrate with player customization service
	return nil
}

// grantImplantBuffReward grants implant buff to player
func (s *EasterEggsService) grantImplantBuffReward(ctx context.Context, playerID, buffID string, duration int) error {
	// For now, just log the reward - actual implementation would integrate with implant system
	s.logger.Infof("Granted implant buff %s (%d minutes) to player %s", buffID, duration, playerID)

	// TODO: Integrate with implant/buff system
	return nil
}

// GetActiveChallenges gets currently active challenges
func (s *EasterEggsService) GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error) {
	s.metrics.IncrementRequests("GetActiveChallenges")
	defer s.metrics.ObserveRequestDuration("GetActiveChallenges", time.Now())

	challenges, err := s.repo.GetActiveChallenges(ctx)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get active challenges: %v", err)
		return nil, fmt.Errorf("failed to get active challenges: %w", err)
	}

	return challenges, nil
}

// GetPlayerChallengeProgress gets player progress on a challenge
func (s *EasterEggsService) GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error) {
	s.metrics.IncrementRequests("GetPlayerChallengeProgress")
	defer s.metrics.ObserveRequestDuration("GetPlayerChallengeProgress", time.Now())

	progress, err := s.repo.GetPlayerChallengeProgress(ctx, playerID, challengeID)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to get player challenge progress for %s:%s: %v", playerID, challengeID, err)
		return 0, fmt.Errorf("failed to get player challenge progress: %w", err)
	}

	return progress, nil
}

// ImportEasterEggsFromYAML imports easter eggs from a YAML file
func (s *EasterEggsService) ImportEasterEggsFromYAML(ctx context.Context, yamlPath string) (*ImportResult, error) {
	s.metrics.IncrementRequests("ImportEasterEggsFromYAML")
	defer s.metrics.ObserveRequestDuration("ImportEasterEggsFromYAML", time.Now())

	s.logger.Infof("Starting import of easter eggs from YAML: %s", yamlPath)

	// Read YAML file
	yamlData, err := os.ReadFile(yamlPath)
	if err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to read YAML file %s: %v", yamlPath, err)
		return nil, fmt.Errorf("failed to read YAML file: %w", err)
	}

	// Parse YAML
	var importData EasterEggsYAMLImport
	if err := yaml.Unmarshal(yamlData, &importData); err != nil {
		s.metrics.IncrementErrors()
		s.logger.Errorf("Failed to parse YAML file %s: %v", yamlPath, err)
		return nil, fmt.Errorf("failed to parse YAML: %w", err)
	}

	result := &ImportResult{
		TotalProcessed: len(importData.EasterEggs),
		Errors:         make([]string, 0),
	}

	s.logger.Infof("Processing %d easter eggs from YAML", result.TotalProcessed)

	// Process each easter egg
	for i, yamlEgg := range importData.EasterEggs {
		if err := s.processEasterEggImport(ctx, yamlEgg); err != nil {
			result.Errors = append(result.Errors, fmt.Sprintf("Easter egg %d (%s): %v", i+1, yamlEgg.ID, err))
			s.logger.Warnf("Failed to import easter egg %s: %v", yamlEgg.ID, err)
		} else {
			result.SuccessfullyAdded++
			s.logger.Debugf("Successfully imported easter egg: %s", yamlEgg.ID)
		}
	}

	s.logger.Infof("Import completed: %d processed, %d successful, %d errors",
		result.TotalProcessed, result.SuccessfullyAdded, len(result.Errors))

	return result, nil
}

// processEasterEggImport processes a single easter egg from YAML
func (s *EasterEggsService) processEasterEggImport(ctx context.Context, yamlEgg EasterEggYAML) error {
	// Convert YAML structures to model structures
	location := models.EasterEggLocation{
		NetworkType:   yamlEgg.Location.NetworkType,
		SpecificAreas: yamlEgg.Location.SpecificAreas,
	}

	discoveryMethod := models.DiscoveryMethod{
		Type: yamlEgg.DiscoveryMethod.Type,
	}

	// Convert rewards
	rewards := make([]models.EasterEggReward, len(yamlEgg.Rewards))
	for i, yamlReward := range yamlEgg.Rewards {
		rewards[i] = models.EasterEggReward{
			Type:     yamlReward.Type,
			Value:    yamlReward.Value,
			ItemID:   yamlReward.ItemID,
			ItemName: yamlReward.ItemName,
		}
	}

	// Create easter egg model
	easterEgg := &models.EasterEgg{
		ID:              yamlEgg.ID,
		Name:            yamlEgg.Name,
		Category:        yamlEgg.Category,
		Difficulty:      yamlEgg.Difficulty,
		Description:     yamlEgg.Description,
		Content:         yamlEgg.Content,
		Location:        location,
		DiscoveryMethod: discoveryMethod,
		Rewards:         rewards,
		LoreConnections: yamlEgg.LoreConnections,
		Status:          "active",
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	// Check if easter egg already exists
	existing, err := s.repo.GetEasterEgg(ctx, yamlEgg.ID)
	if err != nil && err.Error() != "easter egg not found" {
		return fmt.Errorf("failed to check existing easter egg: %w", err)
	}

	if existing != nil {
		// Update existing easter egg
		easterEgg.CreatedAt = existing.CreatedAt // Preserve creation time
		return s.repo.UpdateEasterEgg(ctx, easterEgg)
	} else {
		// Create new easter egg
		return s.repo.CreateEasterEgg(ctx, easterEgg)
	}
}

// HealthCheck performs a health check
func (s *EasterEggsService) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}
