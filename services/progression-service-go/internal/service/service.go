package service

import (
	"context"
	"fmt"
	"log"
	"sync"
	"time"
)

// ProgressionService implements the core endgame progression business logic
type ProgressionService struct {
	// In-memory storage for demo purposes
	// In production, this would be database/Redis
	paragonData  map[string]*ParagonData
	prestigeData map[string]*PrestigeData
	masteryData  map[string]*MasteryData
	mu           sync.RWMutex
}

// ParagonData represents paragon level progression data
type ParagonData struct {
	CharacterID    string
	CurrentLevel   int
	TotalXP        int64
	AvailablePoints int
	// Points distributed to attributes
	Strength     int
	Agility      int
	Intelligence int
	Vitality     int
	Luck         int
	LastUpdated  time.Time
}

// PrestigeData represents prestige reset data
type PrestigeData struct {
	CharacterID   string
	CurrentLevel  int
	TotalResets   int
	BonusMultiplier float32
	LastReset     time.Time
	Bonuses       map[string]float32 // bonus type -> multiplier
}

// MasteryData represents mastery progression data
type MasteryData struct {
	CharacterID string
	Masteries   map[string]*MasteryInfo // mastery type -> info
}

// MasteryInfo represents individual mastery data
type MasteryInfo struct {
	Type         string
	CurrentLevel int
	CurrentXP    int64
	TotalXP      int64
	Rewards      []string
}

// NewProgressionService creates a new instance of ProgressionService
func NewProgressionService() *ProgressionService {
	return &ProgressionService{
		paragonData:  make(map[string]*ParagonData),
		prestigeData: make(map[string]*PrestigeData),
		masteryData:  make(map[string]*MasteryData),
	}
}

// GetParagonLevels returns paragon level information for a character
func (s *ProgressionService) GetParagonLevels(ctx context.Context, characterID string) (*ParagonLevels, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.paragonData[characterID]
	if !exists {
		// Initialize with level 1
		data = &ParagonData{
			CharacterID:     characterID,
			CurrentLevel:    1,
			TotalXP:         0,
			AvailablePoints: 0,
			Strength:        0,
			Agility:         0,
			Intelligence:    0,
			Vitality:        0,
			Luck:            0,
			LastUpdated:     time.Now(),
		}
		s.paragonData[characterID] = data
	}

	// Calculate XP needed for next level
	xpForNext := s.calculateXPForParagonLevel(data.CurrentLevel + 1)
	xpProgress := float32(data.TotalXP) / float32(xpForNext)

	return &ParagonLevels{
		CurrentLevel:    data.CurrentLevel,
		TotalXp:         data.TotalXP,
		AvailablePoints: data.AvailablePoints,
		PointsDistributed: &PointsDistributed{
			Strength:     data.Strength,
			Agility:      data.Agility,
			Intelligence: data.Intelligence,
			Vitality:     data.Vitality,
			Luck:         data.Luck,
		},
		XpToNextLevel: xpForNext - data.TotalXP,
		XpProgress:    xpProgress,
		LastUpdated:   data.LastUpdated,
	}, nil
}

// DistributeParagonPoints distributes available paragon points to attributes
func (s *ProgressionService) DistributeParagonPoints(ctx context.Context, characterID string, distribution *PointsDistribution) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	data, exists := s.paragonData[characterID]
	if !exists {
		return fmt.Errorf("paragon data not found for character %s", characterID)
	}

	totalPoints := distribution.Strength + distribution.Agility + distribution.Intelligence + distribution.Vitality + distribution.Luck
	if totalPoints > data.AvailablePoints {
		return fmt.Errorf("insufficient available points: requested %d, available %d", totalPoints, data.AvailablePoints)
	}

	// Apply distribution
	data.Strength += distribution.Strength
	data.Agility += distribution.Agility
	data.Intelligence += distribution.Intelligence
	data.Vitality += distribution.Vitality
	data.Luck += distribution.Luck
	data.AvailablePoints -= totalPoints
	data.LastUpdated = time.Now()

	log.Printf("Distributed %d paragon points for character %s", totalPoints, characterID)
	return nil
}

// GetParagonStats returns paragon statistics
func (s *ProgressionService) GetParagonStats(ctx context.Context, characterID string) (*ParagonStats, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// Calculate global stats (simplified)
	totalChars := len(s.paragonData)
	avgLevel := 0
	highestLevel := 0

	for _, d := range s.paragonData {
		avgLevel += d.CurrentLevel
		if d.CurrentLevel > highestLevel {
			highestLevel = d.CurrentLevel
		}
	}

	if totalChars > 0 {
		avgLevel = avgLevel / totalChars
	}

	return &ParagonStats{
		TotalCharactersWithParagon: totalChars,
		AverageParagonLevel:        avgLevel,
		HighestParagonLevel:        highestLevel,
	}, nil
}

// GetPrestigeInfo returns prestige information for a character
func (s *ProgressionService) GetPrestigeInfo(ctx context.Context, characterID string) (*PrestigeInfo, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.prestigeData[characterID]
	if !exists {
		// Initialize with level 0
		data = &PrestigeData{
			CharacterID:     characterID,
			CurrentLevel:    0,
			TotalResets:     0,
			BonusMultiplier: 1.0,
			Bonuses:         make(map[string]float32),
			LastReset:       time.Time{},
		}
		s.prestigeData[characterID] = data
	}

	// Calculate requirements for next prestige
	nextLevel := data.CurrentLevel + 1
	xpRequired := s.calculateXPForPrestigeLevel(nextLevel)

	return &PrestigeInfo{
		CurrentLevel:     data.CurrentLevel,
		TotalResets:      data.TotalResets,
		BonusMultiplier:  data.BonusMultiplier,
		NextLevelXpRequired: xpRequired,
		LastReset:        data.LastReset,
	}, nil
}

// ResetPrestige performs prestige reset
func (s *ProgressionService) ResetPrestige(ctx context.Context, characterID string) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	data, exists := s.prestigeData[characterID]
	if !exists {
		return fmt.Errorf("prestige data not found for character %s", characterID)
	}

	// Check if character meets requirements
	requiredLevel := 50
	if data.CurrentLevel < requiredLevel {
		return fmt.Errorf("character must reach level %d before prestige reset", requiredLevel)
	}

	// Perform reset
	data.CurrentLevel = 0
	data.TotalResets++
	data.BonusMultiplier += 0.1 // +10% bonus per reset
	data.LastReset = time.Now()

	// Apply bonuses
	data.Bonuses["xp_multiplier"] = float32(data.BonusMultiplier)
	data.Bonuses["currency_multiplier"] = float32(data.BonusMultiplier)

	log.Printf("Prestige reset performed for character %s, new multiplier: %.2f", characterID, data.BonusMultiplier)
	return nil
}

// GetPrestigeBonuses returns prestige bonuses
func (s *ProgressionService) GetPrestigeBonuses(ctx context.Context, characterID string) (*PrestigeBonuses, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.prestigeData[characterID]
	if !exists {
		return &PrestigeBonuses{
			Bonuses: make(map[string]float32),
		}, nil
	}

	return &PrestigeBonuses{
		Bonuses: data.Bonuses,
	}, nil
}

// GetMasteryLevels returns mastery levels for all types
func (s *ProgressionService) GetMasteryLevels(ctx context.Context, characterID string) (*MasteryLevels, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.masteryData[characterID]
	if !exists {
		// Initialize with default masteries
		data = s.initializeMasteryData(characterID)
		s.masteryData[characterID] = data
	}

	levels := make(map[string]int)
	rewards := make(map[string][]string)

	for masteryType, info := range data.Masteries {
		levels[masteryType] = info.CurrentLevel
		rewards[masteryType] = info.Rewards
	}

	return &MasteryLevels{
		Levels:  levels,
		Rewards: rewards,
	}, nil
}

// GetMasteryProgress returns progress for specific mastery type
func (s *ProgressionService) GetMasteryProgress(ctx context.Context, characterID, masteryType string) (*MasteryProgress, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.masteryData[characterID]
	if !exists {
		data = s.initializeMasteryData(characterID)
		s.masteryData[characterID] = data
	}

	info, exists := data.Masteries[masteryType]
	if !exists {
		return nil, fmt.Errorf("mastery type %s not found", masteryType)
	}

	nextLevel := info.CurrentLevel + 1
	xpForNext := s.calculateXPForMasteryLevel(masteryType, nextLevel)

	return &MasteryProgress{
		MasteryType:     masteryType,
		CurrentLevel:    info.CurrentLevel,
		CurrentXp:       info.CurrentXP,
		XpToNextLevel:   xpForNext - info.CurrentXP,
		ProgressPercent: float32(info.CurrentXP) / float32(xpForNext),
		TotalXpEarned:   info.TotalXP,
	}, nil
}

// GetMasteryRewards returns rewards for specific mastery type
func (s *ProgressionService) GetMasteryRewards(ctx context.Context, characterID, masteryType string) (*MasteryRewards, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	data, exists := s.masteryData[characterID]
	if !exists {
		data = s.initializeMasteryData(characterID)
		s.masteryData[characterID] = data
	}

	info, exists := data.Masteries[masteryType]
	if !exists {
		return nil, fmt.Errorf("mastery type %s not found", masteryType)
	}

	return &MasteryRewards{
		MasteryType: masteryType,
		Rewards:     info.Rewards,
	}, nil
}

// Helper methods

func (s *ProgressionService) calculateXPForParagonLevel(level int) int64 {
	// Exponential XP curve: base XP * level^2
	baseXP := int64(1000)
	return baseXP * int64(level*level)
}

func (s *ProgressionService) calculateXPForPrestigeLevel(level int) int64 {
	// Prestige requires reaching max level multiple times
	return int64(50 * level * 10000) // level 50 * 10k XP per prestige level
}

func (s *ProgressionService) calculateXPForMasteryLevel(masteryType string, level int) int64 {
	// Different XP requirements based on mastery type
	baseXP := int64(5000)

	switch masteryType {
	case "raid":
		baseXP = 8000
	case "dungeon":
		baseXP = 6000
	case "world_boss":
		baseXP = 10000
	case "pvp":
		baseXP = 7000
	case "exploration":
		baseXP = 4000
	}

	return baseXP * int64(level)
}

func (s *ProgressionService) initializeMasteryData(characterID string) *MasteryData {
	return &MasteryData{
		CharacterID: characterID,
		Masteries: map[string]*MasteryInfo{
			"raid": {
				Type:         "raid",
				CurrentLevel: 1,
				CurrentXP:    0,
				TotalXP:      0,
				Rewards:      []string{"raid_damage_bonus", "raid_health_bonus"},
			},
			"dungeon": {
				Type:         "dungeon",
				CurrentLevel: 1,
				CurrentXP:    0,
				TotalXP:      0,
				Rewards:      []string{"dungeon_speed_bonus", "dungeon_loot_bonus"},
			},
			"world_boss": {
				Type:         "world_boss",
				CurrentLevel: 1,
				CurrentXP:    0,
				TotalXP:      0,
				Rewards:      []string{"world_boss_damage_bonus", "world_boss_crit_bonus"},
			},
			"pvp": {
				Type:         "pvp",
				CurrentLevel: 1,
				CurrentXP:    0,
				TotalXP:      0,
				Rewards:      []string{"pvp_damage_bonus", "pvp_defense_bonus"},
			},
			"exploration": {
				Type:         "exploration",
				CurrentLevel: 1,
				CurrentXP:    0,
				TotalXP:      0,
				Rewards:      []string{"exploration_speed_bonus", "exploration_discovery_bonus"},
			},
		},
	}
}

// AddXPToParagon adds XP to paragon progression
func (s *ProgressionService) AddXPToParagon(ctx context.Context, characterID string, xp int64) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	data, exists := s.paragonData[characterID]
	if !exists {
		data = &ParagonData{
			CharacterID:     characterID,
			CurrentLevel:    1,
			TotalXP:         0,
			AvailablePoints: 0,
			LastUpdated:     time.Now(),
		}
		s.paragonData[characterID] = data
	}

	data.TotalXP += xp
	data.LastUpdated = time.Now()

	// Check for level ups
	for {
		xpForNext := s.calculateXPForParagonLevel(data.CurrentLevel + 1)
		if data.TotalXP >= xpForNext {
			data.CurrentLevel++
			data.AvailablePoints += 5 // 5 points per level
			log.Printf("Character %s reached Paragon level %d", characterID, data.CurrentLevel)
		} else {
			break
		}
	}

	return nil
}

// AddXPToMastery adds XP to specific mastery type
func (s *ProgressionService) AddXPToMastery(ctx context.Context, characterID, masteryType string, xp int64) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	data, exists := s.masteryData[characterID]
	if !exists {
		data = s.initializeMasteryData(characterID)
		s.masteryData[characterID] = data
	}

	info, exists := data.Masteries[masteryType]
	if !exists {
		return fmt.Errorf("mastery type %s not found", masteryType)
	}

	info.CurrentXP += xp
	info.TotalXP += xp

	// Check for level ups
	for {
		xpForNext := s.calculateXPForMasteryLevel(masteryType, info.CurrentLevel + 1)
		if info.CurrentXP >= xpForNext {
			info.CurrentLevel++

			// Add new rewards
			switch info.CurrentLevel {
			case 5:
				info.Rewards = append(info.Rewards, masteryType+"_elite_bonus")
			case 10:
				info.Rewards = append(info.Rewards, masteryType+"_legendary_bonus")
			case 25:
				info.Rewards = append(info.Rewards, masteryType+"_mythic_bonus")
			}

			log.Printf("Character %s reached %s mastery level %d", characterID, masteryType, info.CurrentLevel)
		} else {
			break
		}
	}

	return nil
}

// Types for API responses (matching ogen generated types)
type ParagonLevels struct {
	CurrentLevel       int                 `json:"current_level"`
	TotalXp            int64               `json:"total_xp"`
	AvailablePoints    int                 `json:"available_points"`
	PointsDistributed  *PointsDistributed  `json:"points_distributed"`
	XpToNextLevel      int64               `json:"xp_to_next_level"`
	XpProgress         float32             `json:"xp_progress"`
	LastUpdated        time.Time           `json:"last_updated"`
}

type PointsDistributed struct {
	Strength     int `json:"strength"`
	Agility      int `json:"agility"`
	Intelligence int `json:"intelligence"`
	Vitality     int `json:"vitality"`
	Luck         int `json:"luck"`
}

type PointsDistribution struct {
	Strength     int `json:"strength"`
	Agility      int `json:"agility"`
	Intelligence int `json:"intelligence"`
	Vitality     int `json:"vitality"`
	Luck         int `json:"luck"`
}

type ParagonStats struct {
	TotalCharactersWithParagon int `json:"total_characters_with_paragon"`
	AverageParagonLevel        int `json:"average_paragon_level"`
	HighestParagonLevel        int `json:"highest_paragon_level"`
}

type PrestigeInfo struct {
	CurrentLevel        int       `json:"current_level"`
	TotalResets         int       `json:"total_resets"`
	BonusMultiplier     float32   `json:"bonus_multiplier"`
	NextLevelXpRequired int64     `json:"next_level_xp_required"`
	LastReset           time.Time `json:"last_reset"`
}

type PrestigeBonuses struct {
	Bonuses map[string]float32 `json:"bonuses"`
}

type MasteryLevels struct {
	Levels  map[string]int      `json:"levels"`
	Rewards map[string][]string `json:"rewards"`
}

type MasteryProgress struct {
	MasteryType     string  `json:"mastery_type"`
	CurrentLevel    int     `json:"current_level"`
	CurrentXp       int64   `json:"current_xp"`
	XpToNextLevel   int64   `json:"xp_to_next_level"`
	ProgressPercent float32 `json:"progress_percent"`
	TotalXpEarned   int64   `json:"total_xp_earned"`
}

type MasteryRewards struct {
	MasteryType string   `json:"mastery_type"`
	Rewards     []string `json:"rewards"`
}