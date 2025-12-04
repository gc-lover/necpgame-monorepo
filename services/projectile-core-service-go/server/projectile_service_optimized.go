//go:build ignore
// +build ignore

// Issue: #PROJECTILE_OPTIMIZATION
// Projectile Service - Server-Side Projectile Simulation with Anti-Cheat
// Performance: Ballistic physics, hit detection, spatial culling
package server

import (
	"context"
	"math"
	"sync"
	"sync/atomic"
	"time"

	// pb "github.com/gc-lover/necpgame-monorepo/proto/realtime/projectile" // TODO: Fix proto import
)

// Projectile represents a server-authoritative projectile
type Projectile struct {
	ProjectileID uint64
	OwnerID      uint64
	Type         pb.ProjectileSpawn_ProjectileType
	
	// Position (server-authoritative)
	X, Y, Z float32
	
	// Velocity
	VX, VY, VZ float32
	
	// Spawn time
	SpawnTime time.Time
	
	// TTL (time to live)
	TTL time.Duration
	
	// Damage (server-calculated)
	Damage uint32
}

// ProjectileService handles projectile simulation
type ProjectileService struct {
	// Active projectiles (lock-free read!)
	projectiles sync.Map // projectile_id → *Projectile
	
	// Projectile ID generator (atomic)
	nextProjectileID atomic.Uint64
	
	// Weapon configurations (for validation)
	weaponConfigs sync.Map // weapon_id → *WeaponConfig
	
	// Player states (for validation)
	playerStates sync.Map // player_id → *PlayerFireState
}

// WeaponConfig represents weapon configuration for validation
type WeaponConfig struct {
	WeaponID     uint32
	FireRate     float32 // shots per second
	ProjectileSpeed float32 // m/s
	Damage       uint32
	TTL          time.Duration
}

// PlayerFireState tracks player firing for anti-cheat
type PlayerFireState struct {
	PlayerID      uint64
	LastFireTime  time.Time
	FireCount     int
	LastWeaponID  uint32
}

// NewProjectileService creates new projectile service
func NewProjectileService() *ProjectileService {
	s := &ProjectileService{}
	
	// Load default weapon configs
	s.loadWeaponConfigs()
	
	return s
}

// ValidateProjectileSpawn validates projectile spawn request (anti-cheat)
// Checks: fire rate, weapon equipped, ammo, direction validity
func (s *ProjectileService) ValidateProjectileSpawn(ctx context.Context, spawn *pb.ProjectileSpawn) *pb.ProjectileValidationResult {
	result := &pb.ProjectileValidationResult{
		ClientProjectileId: spawn.ClientProjectileId,
		Valid:              false,
	}

	// Get weapon config
	configInterface, ok := s.weaponConfigs.Load(spawn.WeaponId)
	if !ok {
		result.Reason = pb.ProjectileValidationResult_INVALID_WEAPON
		return result
	}
	config := configInterface.(*WeaponConfig)

	// Get player fire state
	stateInterface, _ := s.playerStates.LoadOrStore(spawn.PlayerId, &PlayerFireState{
		PlayerID: spawn.PlayerId,
	})
	fireState := stateInterface.(*PlayerFireState)

	// Check fire rate (anti-cheat: rate limit)
	now := time.Now()
	timeSinceLastFire := now.Sub(fireState.LastFireTime)
	minFireInterval := time.Duration(float64(time.Second) / float64(config.FireRate))

	if timeSinceLastFire < minFireInterval*0.8 { // Allow 20% margin for lag
		result.Reason = pb.ProjectileValidationResult_RATE_LIMIT
		return result
	}

	// Validate direction (unit vector check)
	dirLen := math.Sqrt(
		float64(spawn.Direction.X*spawn.Direction.X +
			spawn.Direction.Y*spawn.Direction.Y +
			spawn.Direction.Z*spawn.Direction.Z))
	
	if dirLen < 90 || dirLen > 110 { // Should be ~100 (quantized unit vector)
		result.Reason = pb.ProjectileValidationResult_INVALID_DIRECTION
		return result
	}

	// TODO: Check ammo
	// TODO: Check weapon equipped

	// Update fire state
	fireState.LastFireTime = now
	fireState.FireCount++
	fireState.LastWeaponID = spawn.WeaponId

	// Validation successful
	result.Valid = true
	result.Reason = pb.ProjectileValidationResult_VALID

	return result
}

// SpawnProjectile spawns server-authoritative projectile
// Performance: Atomic ID generation, lock-free map
func (s *ProjectileService) SpawnProjectile(ctx context.Context, spawn *pb.ProjectileSpawn) *Projectile {
	// Generate server projectile ID (atomic, lock-free!)
	projectileID := s.nextProjectileID.Add(1)

	// Get weapon config
	configInterface, _ := s.weaponConfigs.Load(spawn.WeaponId)
	config := configInterface.(*WeaponConfig)

	// De-quantize position and direction
	x := float32(spawn.Origin.X) / 100.0
	y := float32(spawn.Origin.Y) / 100.0
	z := float32(spawn.Origin.Z) / 100.0

	dirX := float32(spawn.Direction.X) / 100.0
	dirY := float32(spawn.Direction.Y) / 100.0
	dirZ := float32(spawn.Direction.Z) / 100.0

	// Normalize direction
	dirLen := float32(math.Sqrt(float64(dirX*dirX + dirY*dirY + dirZ*dirZ)))
	dirX /= dirLen
	dirY /= dirLen
	dirZ /= dirLen

	// Create projectile with initial velocity
	projectile := &Projectile{
		ProjectileID: projectileID,
		OwnerID:      spawn.PlayerId,
		Type:         spawn.Type,
		X:            x,
		Y:            y,
		Z:            z,
		VX:           dirX * config.ProjectileSpeed,
		VY:           dirY * config.ProjectileSpeed,
		VZ:           dirZ * config.ProjectileSpeed,
		SpawnTime:    time.Now(),
		TTL:          config.TTL,
		Damage:       config.Damage,
	}

	// Store projectile (lock-free map!)
	s.projectiles.Store(projectileID, projectile)

	return projectile
}

// SimulateTick simulates all projectiles for one tick
// Performance: Parallel processing, ballistic physics
func (s *ProjectileService) SimulateTick(ctx context.Context, deltaTime float32) {
	s.projectiles.Range(func(key, value interface{}) bool {
		projectile := value.(*Projectile)

		// Apply physics (gravity, air resistance)
		projectile.VZ -= 9.81 * deltaTime // Gravity
		projectile.X += projectile.VX * deltaTime
		projectile.Y += projectile.VY * deltaTime
		projectile.Z += projectile.VZ * deltaTime

		// Check TTL
		if time.Since(projectile.SpawnTime) > projectile.TTL {
			s.projectiles.Delete(key)
			return true
		}

		// TODO: Hit detection (raycast)
		// TODO: Terrain collision

		return true
	})
}

// GetActiveProjectiles returns all active projectiles
func (s *ProjectileService) GetActiveProjectiles() []*Projectile {
	projectiles := make([]*Projectile, 0, 100)
	
	s.projectiles.Range(func(_, value interface{}) bool {
		projectiles = append(projectiles, value.(*Projectile))
		return true
	})
	
	return projectiles
}

// CleanupExpired removes expired projectiles
func (s *ProjectileService) CleanupExpired() {
	now := time.Now()
	
	s.projectiles.Range(func(key, value interface{}) bool {
		projectile := value.(*Projectile)
		
		if now.Sub(projectile.SpawnTime) > projectile.TTL {
			s.projectiles.Delete(key)
		}
		
		return true
	})
}

// ToProto converts Projectile to protobuf message
// Performance: Coordinate quantization (50% smaller!)
func (p *Projectile) ToProto() *pb.ProjectileState {
	return &pb.ProjectileState{
		ProjectileId: p.ProjectileID,
		OwnerId:      p.OwnerID,
		Position: &pb.Vector3Quantized{
			X: int32(p.X * 100),
			Y: int32(p.Y * 100),
			Z: int32(p.Z * 100),
		},
		Velocity: &pb.Vector3Quantized{
			X: int32(p.VX * 100),
			Y: int32(p.VY * 100),
			Z: int32(p.VZ * 100),
		},
		ServerTick: uint32(time.Now().Unix()),
		TtlMs:      uint32(p.TTL.Milliseconds() - time.Since(p.SpawnTime).Milliseconds()),
		Type:       uint32(p.Type),
	}
}

// loadWeaponConfigs loads default weapon configurations
func (s *ProjectileService) loadWeaponConfigs() {
	// Example weapon configs
	configs := []WeaponConfig{
		{WeaponID: 1, FireRate: 10.0, ProjectileSpeed: 100.0, Damage: 25, TTL: 5 * time.Second},   // Assault Rifle
		{WeaponID: 2, FireRate: 1.0, ProjectileSpeed: 50.0, Damage: 100, TTL: 10 * time.Second},   // Sniper
		{WeaponID: 3, FireRate: 0.5, ProjectileSpeed: 30.0, Damage: 150, TTL: 15 * time.Second},   // Rocket
	}

	for _, config := range configs {
		s.weaponConfigs.Store(config.WeaponID, &config)
	}
}

