// Issue: #1510
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/pkg/api"
)

var (
	ErrNotFound          = errors.New("not found")
	ErrInvalidInput      = errors.New("invalid input")
	ErrWallRunActive     = errors.New("wall run already active")
	ErrNoWallRun         = errors.New("no active wall run")
	ErrInvalidSurface    = errors.New("invalid surface")
	ErrStaminaDepleted   = errors.New("stamina depleted")
)

// WallRunSession represents a wall run session
type WallRunSession struct {
	ID              uuid.UUID
	CharacterID     uuid.UUID
	SurfaceID       uuid.UUID
	StateID         uuid.UUID
	StartedAt       time.Time
	Direction       api.Direction3D
	StartPosition   api.Position3D
	CurrentPosition api.Position3D
	StaminaConsumed int
	IsActive        bool
}

// Service implements business logic for Combat Acrobatics Wall Run
// Issue: #1510 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	surfacesPool   sync.Pool
	statePool      sync.Pool
	wallKickPool   sync.Pool
}

func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.surfacesPool = sync.Pool{
		New: func() interface{} {
			return &api.SurfacesResponse{}
		},
	}
	s.statePool = sync.Pool{
		New: func() interface{} {
			return &api.WallRunStateResponse{}
		},
	}
	s.wallKickPool = sync.Pool{
		New: func() interface{} {
			return &api.WallKickResponse{}
		},
	}

	return s
}

func (s *Service) GetWallRunSurfaces(ctx context.Context, params api.GetWallRunSurfacesParams) (*api.SurfacesResponse, error) {
	// TODO: Get character ID from context/auth
	characterID := uuid.New() // Placeholder

	// TODO: Get zone/area from params
	zoneID := "current_zone" // Placeholder

	surfaces, err := s.repo.GetSurfacesInZone(ctx, characterID, zoneID)
	if err != nil {
		return nil, err
	}

	response := s.surfacesPool.Get().(*api.SurfacesResponse)
	defer s.surfacesPool.Put(response)

	response.Surfaces = surfaces
	response.Total = len(surfaces)

	return response, nil
}

func (s *Service) StartWallRun(ctx context.Context, req api.OptStartWallRunRequest) (*api.WallRunStateResponse, error) {
	// TODO: Get character ID from context/auth
	characterID := uuid.New() // Placeholder

	// Check if character already has active wall run
	activeSession, err := s.repo.GetActiveWallRun(ctx, characterID)
	if err != nil && !errors.Is(err, ErrNotFound) {
		return nil, err
	}
	if activeSession != nil {
		return nil, ErrWallRunActive
	}

	// Find suitable surface
	var surfaceID uuid.UUID
	if req.IsSet() && req.Value.SurfaceID.IsSet() {
		surfaceID = req.Value.SurfaceID.Value
	} else {
		// Auto-detect surface
		surfaceID = uuid.New() // TODO: Implement surface detection logic
	}

	// Validate surface
	surface, err := s.repo.GetSurface(ctx, surfaceID)
	if err != nil {
		return nil, err
	}
	if !surface.IsSuitable {
		return nil, ErrInvalidSurface
	}

	// Create wall run session
	sessionID := uuid.New()
	stateID := uuid.New()

	session := &WallRunSession{
		ID:              sessionID,
		CharacterID:     characterID,
		SurfaceID:       surfaceID,
		StateID:         stateID,
		StartedAt:       time.Now(),
		Direction:       api.Direction3D{X: 0, Y: 0, Z: 1}, // Default forward
		StartPosition:   surface.Position,
		CurrentPosition: surface.Position,
		StaminaConsumed: 0,
		IsActive:        true,
	}

	err = s.repo.CreateWallRunSession(ctx, session)
	if err != nil {
		return nil, err
	}

	response := s.statePool.Get().(*api.WallRunStateResponse)
	defer s.statePool.Put(response)

	response.StateID = stateID
	response.WallSurfaceID = api.NewOptNilUUID(surfaceID)
	response.StartedAt = session.StartedAt
	response.StoppedAt = api.NewOptNilDateTime(api.NilDateTime{})
	response.Direction = session.Direction
	response.StartPosition = session.StartPosition
	response.CurrentPosition = session.CurrentPosition
	response.StaminaConsumed = session.StaminaConsumed
	response.Duration = 0
	response.IsActive = session.IsActive

	return response, nil
}

func (s *Service) StopWallRun(ctx context.Context) (*api.WallRunStateResponse, error) {
	// TODO: Get character ID from context/auth
	characterID := uuid.New() // Placeholder

	// Get active wall run
	session, err := s.repo.GetActiveWallRun(ctx, characterID)
	if err != nil {
		return nil, err
	}

	// Update session as completed
	session.IsActive = false
	session.StaminaConsumed = 25 // Example stamina consumption

	err = s.repo.UpdateWallRunSession(ctx, session)
	if err != nil {
		return nil, err
	}

	response := s.statePool.Get().(*api.WallRunStateResponse)
	defer s.statePool.Put(response)

	response.StateID = session.StateID
	response.WallSurfaceID = api.NewOptNilUUID(session.SurfaceID)
	response.StartedAt = session.StartedAt
	response.StoppedAt = api.NewOptNilDateTime(api.NewNilDateTime(time.Now()))
	response.Direction = session.Direction
	response.StartPosition = session.StartPosition
	response.CurrentPosition = session.CurrentPosition
	response.StaminaConsumed = session.StaminaConsumed
	response.Duration = int(time.Since(session.StartedAt).Seconds())
	response.IsActive = session.IsActive

	return response, nil
}

func (s *Service) WallKick(ctx context.Context, req *api.WallKickRequest) (*api.WallKickResponse, error) {
	// TODO: Get character ID from context/auth
	characterID := uuid.New() // Placeholder

	// Get active wall run
	session, err := s.repo.GetActiveWallRun(ctx, characterID)
	if err != nil {
		return nil, err
	}

	// Calculate new direction after kick
	newDirection := req.Direction
	wallRunEnded := false

	// Check if kick should end wall run (e.g., if direction is away from wall)
	if req.Direction.Z < -0.5 { // Moving away from wall
		wallRunEnded = true
		session.IsActive = false
	}

	// Update session
	session.Direction = newDirection
	session.StaminaConsumed += 8 // Wall kick stamina cost

	err = s.repo.UpdateWallRunSession(ctx, session)
	if err != nil {
		return nil, err
	}

	response := s.wallKickPool.Get().(*api.WallKickResponse)
	defer s.wallKickPool.Put(response)

	response.KickID = uuid.New()
	response.Timestamp = time.Now()
	response.NewDirection = newDirection
	response.StaminaConsumed = 8
	response.WallRunEnded = wallRunEnded

	return response, nil
}
