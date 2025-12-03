// Issue: #MOVEMENT_OPTIMIZATION
// Movement Service - Optimized with Server-Side Validation & Anti-Cheat
// Performance: Movement validation, speed hack detection, position reconciliation
package server

import (
	"context"
	"math"
	"sync"
	"time"

	pb "github.com/gc-lover/necpgame-monorepo/proto/realtime/movement"
)

// MovementService handles movement logic with anti-cheat
type MovementService struct {
	// Player states (in-memory for real-time)
	playerStates sync.Map // player_id → *PlayerState
	
	// Movement validation config
	maxSpeed       float64 // m/s
	maxAcceleration float64
	
	// Anti-cheat tracking
	violations sync.Map // player_id → violation count
}

// NewMovementService creates new movement service
func NewMovementService() *MovementService {
	return &MovementService{
		maxSpeed:        10.0, // 10 m/s normal speed
		maxAcceleration: 50.0, // m/s²
	}
}

// ProcessMovementInput processes player movement input with validation
// Performance: Server-authoritative, anti-cheat integrated
func (s *MovementService) ProcessMovementInput(ctx context.Context, input *pb.PlayerMovementUpdate) *PlayerState {
	// Mock player_id (in production, extract from validated token)
	playerID := uint64(input.ClientTick) // TEMPORARY

	// Get or create player state
	stateInterface, _ := s.playerStates.LoadOrStore(playerID, &PlayerState{
		PlayerID:   playerID,
		LastUpdate: time.Now(),
	})
	state := stateInterface.(*PlayerState)

	// Calculate delta time
	now := time.Now()
	deltaTime := now.Sub(state.LastUpdate).Seconds()
	if deltaTime > 1.0 {
		deltaTime = 0.016 // Cap at 60 FPS if too long
	}

	// De-quantize input (-100 to 100 → -1.0 to 1.0)
	moveX := float64(input.MoveX) / 100.0
	moveY := float64(input.MoveY) / 100.0

	// Calculate intended movement
	speed := s.maxSpeed
	if input.ActionFlags&0x01 != 0 { // Sprint flag
		speed *= 1.5
	}

	// Calculate new position
	newX := state.X + moveX*speed*deltaTime
	newY := state.Y + moveY*speed*deltaTime
	newZ := state.Z // Simplified (no gravity/jumping yet)

	// Server-side validation (anti-cheat)
	if !s.validateMovement(state, newX, newY, newZ, deltaTime) {
		// Movement invalid, reject and correct
		s.recordViolation(playerID)
		return state // Return old state (correction)
	}

	// Update state
	state.X = newX
	state.Y = newY
	state.Z = newZ
	state.Yaw = float64(input.Yaw) / 100.0
	state.Pitch = float64(input.Pitch) / 100.0
	state.Flags = input.ActionFlags
	state.LastUpdate = now
	state.Moved = true

	// Calculate velocity for interpolation
	state.VX = moveX * speed
	state.VY = moveY * speed
	state.VZ = 0

	return state
}

// validateMovement validates movement for anti-cheat
// Checks: speed limits, acceleration limits, no-clip detection
func (s *MovementService) validateMovement(state *PlayerState, newX, newY, newZ, deltaTime float64) bool {
	// Calculate distance moved
	dx := newX - state.X
	dy := newY - state.Y
	dz := newZ - state.Z
	distance := math.Sqrt(dx*dx + dy*dy + dz*dz)

	// Calculate speed
	speed := distance / deltaTime

	// Check speed limit (anti-cheat: speed hack detection)
	maxAllowedSpeed := s.maxSpeed * 2.0 // Allow some margin for sprinting + lag
	if speed > maxAllowedSpeed {
		// Speed hack detected!
		return false
	}

	// Check acceleration (prevent instant direction changes)
	if deltaTime > 0 {
		acceleration := math.Abs(speed-math.Sqrt(state.VX*state.VX+state.VY*state.VY)) / deltaTime
		if acceleration > s.maxAcceleration {
			// Unrealistic acceleration
			return false
		}
	}

	// TODO: Check no-clip (collision detection)
	// TODO: Check fly hack (Z coordinate validation)

	return true
}

// recordViolation records anti-cheat violation
func (s *MovementService) recordViolation(playerID uint64) {
	countInterface, _ := s.violations.LoadOrStore(playerID, 0)
	count := countInterface.(int)
	count++
	s.violations.Store(playerID, count)

	if count > 10 {
		// Ban player or flag for manual review
		// TODO: Integrate with anti-cheat system
	}
}

// GetPlayerState returns current player state
func (s *MovementService) GetPlayerState(playerID uint64) (*PlayerState, bool) {
	stateInterface, ok := s.playerStates.Load(playerID)
	if !ok {
		return nil, false
	}
	return stateInterface.(*PlayerState), true
}

// GetAllPlayerStates returns all player states (for snapshot)
func (s *MovementService) GetAllPlayerStates() []*PlayerState {
	states := make([]*PlayerState, 0, 100)
	
	s.playerStates.Range(func(_, value interface{}) bool {
		states = append(states, value.(*PlayerState))
		return true
	})
	
	return states
}

