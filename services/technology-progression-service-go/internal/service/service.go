package service

import (
	"context"
	"fmt"
	"log"
	"sync"
	"time"
)

// TechnologyProgressionService implements the core technology progression business logic
type TechnologyProgressionService struct {
	// In-memory storage for demo purposes
	// In production, this would be database/Redis
	technologies    map[string]*Technology
	characterUnlocks map[string][]*TechnologyUnlock // character_id -> unlocks
	notifications   map[string][]*TechnologyNotification // character_id -> notifications
	mu              sync.RWMutex
}

// Technology represents a technology in the progression system
type Technology struct {
	ID                string
	Name              string
	Description       string
	Category          string
	UnlockYear        int
	UnlockPhase       string
	AvailabilityStatus string
	UnlockConditions  []UnlockCondition
	Availability      TechnologyAvailability
	Examples          []string
	LoreContext       string
}

// UnlockCondition represents a condition for unlocking technology
type UnlockCondition struct {
	Type        string // time, quest, reputation, event
	Condition   string
	Description string
}

// TechnologyAvailability represents where technology is available
type TechnologyAvailability struct {
	Vendors       string
	Corporations  []string
	BlackMarket   bool
	Restrictions  []string
}

// TechnologyUnlock represents when a character unlocked a technology
type TechnologyUnlock struct {
	TechnologyID string
	UnlockedAt   time.Time
	UnlockReason string // time_progression, quest_completion, reputation_achieved, event_triggered
}

// TechnologyNotification represents a notification about technology unlock
type TechnologyNotification struct {
	ID               string
	TechnologyID     string
	TechnologyName   string
	UnlockedAt       time.Time
	UnlockReason     string
	CharacterID      string
	AdditionalInfo   map[string]interface{}
}

// NewTechnologyProgressionService creates a new instance of TechnologyProgressionService
func NewTechnologyProgressionService() *TechnologyProgressionService {
	service := &TechnologyProgressionService{
		technologies:     make(map[string]*Technology),
		characterUnlocks: make(map[string][]*TechnologyUnlock),
		notifications:    make(map[string][]*TechnologyNotification),
	}

	// Initialize with technology data from 2020-2093
	service.initializeTechnologies()

	return service
}

// initializeTechnologies loads all technologies from the timeline
func (s *TechnologyProgressionService) initializeTechnologies() {
	// 2020-2030 technologies
	s.technologies["cyberware-basic"] = &Technology{
		ID:                "cyberware-basic",
		Name:              "Кибернетические имплантаты (базовые)",
		Description:       "Киберпротезы, базовые нейроинтерфейсы, простые кибернетические модификации",
		Category:          "cyberware",
		UnlockYear:        2020,
		UnlockPhase:       "Start",
		AvailabilityStatus: "available",
		UnlockConditions: []UnlockCondition{
			{Type: "time", Condition: "league_year >= 2020", Description: "Достигнуть 2020 года в лиге"},
			{Type: "automatic", Condition: "true", Description: "Автоматическая разблокировка"},
		},
		Availability: TechnologyAvailability{
			Vendors:      "all",
			Corporations: []string{"Arasaka", "Militech", "Biotechnica"},
			BlackMarket:  false,
		},
		Examples:    []string{"Базовые киберруки", "Простые нейроинтерфейсы"},
		LoreContext: "Первое поколение кибернетики, доступное широкой публике после корпорационных войн",
	}

	s.technologies["ai-basic"] = &Technology{
		ID:          "ai-basic",
		Name:        "Искусственный интеллект (базовый)",
		Description: "Простые ИИ-системы для управления инфраструктурой, базовые алгоритмы",
		Category:    "ai",
		UnlockYear:  2020,
		UnlockPhase: "Start",
		UnlockConditions: []UnlockCondition{
			{Type: "time", Condition: "league_year >= 2020", Description: "Достигнуть 2020 года в лиге"},
			{Type: "automatic", Condition: "true", Description: "Автоматическая разблокировка"},
		},
		Availability: TechnologyAvailability{
			Vendors:      "corporate_only",
			Corporations: []string{"Arasaka", "Militech", "Biotechnica"},
			BlackMarket:  false,
		},
		Examples:    []string{"Базовые ИИ для управления зданиями", "Простые алгоритмы для транспорта"},
		LoreContext: "Корпорации используют ИИ для автоматизации процессов",
	}

	s.technologies["neurointerface-advanced"] = &Technology{
		ID:          "neurointerface-advanced",
		Name:        "Нейроинтерфейсы (продвинутые)",
		Description: "Прямая связь между мозгом и компьютером. Продвинутые нейроинтерфейсы позволяют управлять техникой силой мысли",
		Category:    "neurointerface",
		UnlockYear:  2030,
		UnlockPhase: "Rise",
		UnlockConditions: []UnlockCondition{
			{Type: "time", Condition: "league_year >= 2030", Description: "Достигнуть 2030 года в лиге"},
			{Type: "quest", Condition: "complete_quest 'Neurointerface Revolution'", Description: "Завершить квест 'Neurointerface Revolution'"},
			{Type: "reputation", Condition: "corporate_reputation >= 50", Description: "Репутация корпораций >= 50"},
		},
		Availability: TechnologyAvailability{
			Vendors:      "elite_vendors",
			Corporations: []string{"Arasaka", "Biotechnica"},
			BlackMarket:  true,
		},
		Examples:    []string{"Продвинутые нейроинтерфейсы", "Прямое подключение к NET"},
		LoreContext: "Революция в интерфейсах человек-машина",
	}

	// Add more technologies here...
	log.Printf("Initialized %d technologies", len(s.technologies))
}

// GetAvailableTechnologies returns technologies available for the character
func (s *TechnologyProgressionService) GetAvailableTechnologies(ctx context.Context, characterID string, includeLocked bool, category *string) ([]*Technology, int, int, *int, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// Mock current league year - in production this would come from league service
	currentYear := 2025

	var available []*Technology
	var locked []*Technology

	for _, tech := range s.technologies {
		if category != nil && tech.Category != *category {
			continue
		}

		if s.isTechnologyAvailable(tech, currentYear, characterID) {
			// Clone and set status
			techCopy := *tech
			techCopy.AvailabilityStatus = "available"
			available = append(available, &techCopy)
		} else if includeLocked {
			techCopy := *tech
			techCopy.AvailabilityStatus = s.getLockReason(tech, currentYear, characterID)
			locked = append(locked, &techCopy)
		}
	}

	allTech := append(available, locked...)

	// Calculate next unlock year
	var nextUnlockYear *int
	for year := currentYear + 1; year <= 2093; year++ {
		if s.hasTechnologiesUnlockingInYear(year) {
			nextUnlockYear = &year
			break
		}
	}

	return allTech, len(available), len(locked), nextUnlockYear, nil
}

// CheckTechnologyAvailability checks if specific technology is available
func (s *TechnologyProgressionService) CheckTechnologyAvailability(ctx context.Context, technologyID, characterID string) (bool, string, string, []UnlockCondition, string, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	tech, exists := s.technologies[technologyID]
	if !exists {
		return false, "locked_unknown", "Технология не найдена", nil, "", fmt.Errorf("technology not found: %s", technologyID)
	}

	currentYear := 2025 // Mock
	available := s.isTechnologyAvailable(tech, currentYear, characterID)
	status := "available"
	reason := ""
	var unlockTime string

	if !available {
		status = s.getLockReason(tech, currentYear, characterID)
		reason = s.getLockDescription(status, tech)
		if status == "locked_time" {
			unlockTime = fmt.Sprintf("%d года %d месяцев", tech.UnlockYear-currentYear, 0)
		}
	}

	return available, status, reason, tech.UnlockConditions, unlockTime, nil
}

// GetTechnologyTimeline returns upcoming technology unlocks
func (s *TechnologyProgressionService) GetTechnologyTimeline(ctx context.Context, characterID string, futureYears int) (int, []TimelineEntry, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	currentYear := 2025 // Mock

	var timeline []TimelineEntry
	for year := currentYear; year <= currentYear+futureYears && year <= 2093; year++ {
		technologies := s.getTechnologiesForYear(year)
		if len(technologies) > 0 {
			entry := TimelineEntry{
				Year:            year,
				Phase:           s.getLeaguePhaseForYear(year),
				Technologies:    technologies,
				TotalTechnologies: len(technologies),
			}
			timeline = append(timeline, entry)
		}
	}

	return currentYear, timeline, nil
}

// GetTechnologyNotifications returns recent unlock notifications
func (s *TechnologyProgressionService) GetTechnologyNotifications(ctx context.Context, characterID string, limit int) ([]*TechnologyNotification, int, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	notifications := s.notifications[characterID]
	if len(notifications) == 0 {
		return []*TechnologyNotification{}, 0, nil
	}

	// Return most recent notifications
	total := len(notifications)
	start := 0
	if total > limit {
		start = total - limit
	}

	return notifications[start:], total, nil
}

// Helper methods

func (s *TechnologyProgressionService) isTechnologyAvailable(tech *Technology, currentYear int, characterID string) bool {
	// Check time condition
	for _, condition := range tech.UnlockConditions {
		if condition.Type == "time" {
			if currentYear < tech.UnlockYear {
				return false
			}
		}
		// In production, check quest completion, reputation, etc.
	}
	return true
}

func (s *TechnologyProgressionService) getLockReason(tech *Technology, currentYear int, characterID string) string {
	if currentYear < tech.UnlockYear {
		return "locked_time"
	}
	// Check other conditions...
	return "locked_quest"
}

func (s *TechnologyProgressionService) getLockDescription(status string, tech *Technology) string {
	switch status {
	case "locked_time":
		return fmt.Sprintf("Требуется достичь %d года в лиге", tech.UnlockYear)
	case "locked_quest":
		return "Требуется завершить соответствующий квест"
	default:
		return "Технология заблокирована"
	}
}

func (s *TechnologyProgressionService) hasTechnologiesUnlockingInYear(year int) bool {
	for _, tech := range s.technologies {
		if tech.UnlockYear == year {
			return true
		}
	}
	return false
}

func (s *TechnologyProgressionService) getTechnologiesForYear(year int) []*Technology {
	var technologies []*Technology
	for _, tech := range s.technologies {
		if tech.UnlockYear == year {
			techCopy := *tech
			techCopy.AvailabilityStatus = "upcoming"
			technologies = append(technologies, &techCopy)
		}
	}
	return technologies
}

func (s *TechnologyProgressionService) getLeaguePhaseForYear(year int) string {
	switch {
	case year >= 2020 && year < 2030:
		return "Start"
	case year >= 2030 && year < 2040:
		return "Rise"
	case year >= 2040 && year < 2050:
		return "Peak"
	case year >= 2050 && year < 2060:
		return "Fall"
	default:
		return "End"
	}
}

// TimelineEntry represents technologies unlocking in a specific year
type TimelineEntry struct {
	Year              int
	Phase             string
	Technologies      []*Technology
	TotalTechnologies int
}