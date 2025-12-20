// Package repository Issue: #1841-#1844 - In-memory repository for testing interactive objects
package repository

import (
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/models"
)

// InMemoryInteractiveRepository implements InteractiveRepository for testing
type InMemoryInteractiveRepository struct {
	mu           sync.RWMutex
	interactives map[string]*models.Interactive
}

// NewInMemoryInteractiveRepository creates a new in-memory repository
func NewInMemoryInteractiveRepository() *InMemoryInteractiveRepository {
	return &InMemoryInteractiveRepository{
		interactives: make(map[string]*models.Interactive),
	}
}

// SaveInteractive saves an interactive object
func (r *InMemoryInteractiveRepository) SaveInteractive(interactiveID string, version int, name, description, location string, interactiveType models.InteractiveType, status models.InteractiveStatus, contentData map[string]interface{}) (*models.Interactive, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	now := time.Now()
	interactive := &models.Interactive{
		InteractiveID: interactiveID,
		Version:       version,
		Name:          name,
		Description:   description,
		Location:      location,
		Type:          interactiveType,
		Status:        status,
		ContentData:   contentData,
		CreatedAt:     now,
		UpdatedAt:     now,
	}

	r.interactives[interactiveID] = interactive
	return interactive, nil
}

// GetInteractives retrieves interactives with filtering
func (r *InMemoryInteractiveRepository) GetInteractives(filter *models.ListInteractivesRequest) ([]models.Interactive, int, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var result []models.Interactive
	total := 0

	for _, interactive := range r.interactives {
		// Apply filters
		if filter != nil {
			if filter.Type != nil && interactive.Type != *filter.Type {
				continue
			}
			if filter.Status != nil && interactive.Status != *filter.Status {
				continue
			}
			if filter.Location != nil && interactive.Location != *filter.Location {
				continue
			}
		}

		result = append(result, *interactive)
		total++
	}

	// Apply pagination if specified
	if filter != nil && filter.Limit > 0 {
		offset := filter.Offset
		limit := filter.Limit

		if offset >= len(result) {
			return []models.Interactive{}, total, nil
		}

		end := offset + limit
		if end > len(result) {
			end = len(result)
		}

		result = result[offset:end]
	}

	return result, total, nil
}

// GetInteractive retrieves a single interactive
func (r *InMemoryInteractiveRepository) GetInteractive(interactiveID string) (*models.Interactive, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	interactive, exists := r.interactives[interactiveID]
	if !exists {
		return nil, fmt.Errorf("interactive not found: %s", interactiveID)
	}

	return interactive, nil
}

// UpdateInteractive updates an interactive
func (r *InMemoryInteractiveRepository) UpdateInteractive(interactiveID string, updates map[string]interface{}) (*models.Interactive, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	interactive, exists := r.interactives[interactiveID]
	if !exists {
		return nil, fmt.Errorf("interactive not found: %s", interactiveID)
	}

	// Apply updates (simplified)
	if name, ok := updates["name"].(string); ok {
		interactive.Name = name
	}
	if description, ok := updates["description"].(string); ok {
		interactive.Description = description
	}
	if status, ok := updates["status"].(models.InteractiveStatus); ok {
		interactive.Status = status
	}

	interactive.UpdatedAt = time.Now()
	return interactive, nil
}

// DeleteInteractive deletes an interactive
func (r *InMemoryInteractiveRepository) DeleteInteractive(interactiveID string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.interactives[interactiveID]; !exists {
		return fmt.Errorf("interactive not found: %s", interactiveID)
	}

	delete(r.interactives, interactiveID)
	return nil
}
