package repository

import (
	"errors"
	"sync"
	"time"

	"combat-hacking-service-go/pkg/models"
)

type Repository struct {
	mu           sync.RWMutex
	blindZones   map[string]*models.BlindZone
	phantomEntities map[string]*models.PhantomEntity
}

func NewRepository() *Repository {
	return &Repository{
		blindZones:      make(map[string]*models.BlindZone),
		phantomEntities: make(map[string]*models.PhantomEntity),
	}
}

// CreateBlindZone creates a new blind zone from screen hack skill
// Issue: #143875347
func (r *Repository) CreateBlindZone(req models.ScreenHackBlindRequest) (*models.BlindZone, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Calculate zone parameters based on skill level
	var radius float64
	var duration int
	var detectionRisk float64

	switch req.SkillLevel {
	case 1:
		radius = 8.0
		duration = 4
		detectionRisk = 0.15
	case 2:
		radius = 12.0
		duration = 6
		detectionRisk = 0.20
	case 3:
		radius = 15.0
		duration = 8
		detectionRisk = 0.25
	default:
		return nil, errors.New("invalid skill level")
	}

	// Generate zone ID (in real implementation, use UUID)
	zoneID := generateID()

	zone := &models.BlindZone{
		ID:             zoneID,
		Position:       req.ScreenPosition,
		Radius:         radius,
		Duration:       duration,
		DetectionRisk:  detectionRisk,
		AffectedEnemies: 0, // Will be calculated based on enemy positions
		CreatedAt:      time.Now().Unix(),
	}

	r.blindZones[zoneID] = zone

	// Start cleanup goroutine
	go func() {
		time.Sleep(time.Duration(duration) * time.Second)
		r.mu.Lock()
		delete(r.blindZones, zoneID)
		r.mu.Unlock()
	}()

	return zone, nil
}

// CreateGlitchDoubles creates phantom entities from glitch doubles skill
// Issue: #143875814
func (r *Repository) CreateGlitchDoubles(req models.GlitchDoublesRequest) ([]*models.PhantomEntity, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Calculate phantom parameters based on skill level
	var phantomCount int
	var duration int
	var phantomRange float64
	var detectionRisk float64

	switch req.SkillLevel {
	case 1:
		phantomCount = 2
		duration = 6
		phantomRange = 15.0
		detectionRisk = 0.20
	case 2:
		phantomCount = 3
		duration = 9
		phantomRange = 20.0
		detectionRisk = 0.25
	case 3:
		phantomCount = 4
		duration = 12
		phantomRange = 25.0
		detectionRisk = 0.30
	default:
		return nil, errors.New("invalid skill level")
	}

	phantoms := make([]*models.PhantomEntity, phantomCount)

	for i := 0; i < phantomCount; i++ {
		phantomID := generateID()

		phantom := &models.PhantomEntity{
			ID:            phantomID,
			PlayerID:      req.PlayerID,
			Position:      models.Vector3{X: 0, Y: 0, Z: 0}, // Will be set to player position
			Duration:      duration,
			Range:         phantomRange,
			DetectionRisk: detectionRisk,
			CreatedAt:     time.Now().Unix(),
		}

		r.phantomEntities[phantomID] = phantom
		phantoms[i] = phantom

		// Start cleanup goroutine
		go func(id string) {
			time.Sleep(time.Duration(duration) * time.Second)
			r.mu.Lock()
			delete(r.phantomEntities, id)
			r.mu.Unlock()
		}(phantomID)
	}

	return phantoms, nil
}

// GetActiveBlindZones returns all currently active blind zones
func (r *Repository) GetActiveBlindZones() map[string]*models.BlindZone {
	r.mu.RLock()
	defer r.mu.RUnlock()

	zones := make(map[string]*models.BlindZone)
	for id, zone := range r.blindZones {
		zones[id] = zone
	}
	return zones
}

// GetActivePhantoms returns all currently active phantom entities
func (r *Repository) GetActivePhantoms() map[string]*models.PhantomEntity {
	r.mu.RLock()
	defer r.mu.RUnlock()

	phantoms := make(map[string]*models.PhantomEntity)
	for id, phantom := range r.phantomEntities {
		phantoms[id] = phantom
	}
	return phantoms
}

// generateID generates a simple ID (in production, use proper UUID)
func generateID() string {
	return time.Now().Format("20060102150405") + string(rune(time.Now().Nanosecond()))
}

// BACKEND NOTE: Repository uses RWMutex for concurrent access.
// Blind zones and phantoms are cleaned up automatically after duration expires.

// Issue: #143875347, #143875814

