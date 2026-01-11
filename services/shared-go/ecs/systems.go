// Common Game Systems for ECS
// Issue: #2120
// PERFORMANCE: Optimized for batch processing

package ecs

import (
	"sync"
)

// MovementSystem processes entities with Position, Velocity, and Movement components
type MovementSystem struct {
	world *World
}

// NewMovementSystem creates a new movement system
func NewMovementSystem(world *World) *MovementSystem {
	return &MovementSystem{world: world}
}

// RequiredComponents returns required component types
func (ms *MovementSystem) RequiredComponents() []ComponentID {
	return []ComponentID{ComponentIDPosition, ComponentIDVelocity, ComponentIDMovement}
}

// Update processes movement for all matching entities
func (ms *MovementSystem) Update(deltaTime float64, entities []Entity) {
	for _, entity := range entities {
		pos, _ := ms.world.GetComponent(entity.ID, ComponentIDPosition).(*Position)
		vel, _ := ms.world.GetComponent(entity.ID, ComponentIDVelocity).(*Velocity)
		mov, _ := ms.world.GetComponent(entity.ID, ComponentIDMovement).(*Movement)

		if pos == nil || vel == nil || mov == nil {
			continue
		}

		// Update position based on velocity
		dt := float32(deltaTime)
		pos.X += vel.X * dt
		pos.Y += vel.Y * dt
		pos.Z += vel.Z * dt

		// Apply movement constraints
		if mov.IsMoving {
			speedSq := vel.X*vel.X + vel.Y*vel.Y + vel.Z*vel.Z
			maxSpeedSq := mov.MaxSpeed * mov.MaxSpeed
			if speedSq > maxSpeedSq {
				scale := mov.MaxSpeed / float32(sqrt32(speedSq))
				vel.X *= scale
				vel.Y *= scale
				vel.Z *= scale
			}
		}
	}
}

// HealthSystem processes entities with Health component
type HealthSystem struct {
	world *World
}

// NewHealthSystem creates a new health system
func NewHealthSystem(world *World) *HealthSystem {
	return &HealthSystem{world: world}
}

// RequiredComponents returns required component types
func (hs *HealthSystem) RequiredComponents() []ComponentID {
	return []ComponentID{ComponentIDHealth}
}

// Update processes health for all matching entities
func (hs *HealthSystem) Update(deltaTime float64, entities []Entity) {
	for _, entity := range entities {
		health, _ := hs.world.GetComponent(entity.ID, ComponentIDHealth).(*Health)
		if health == nil {
			continue
		}

		// Ensure health doesn't exceed max
		if health.Current > health.Max {
			health.Current = health.Max
		}

		// Ensure health doesn't go below 0
		if health.Current < 0 {
			health.Current = 0
		}
	}
}

// CombatSystem processes entities with Combat and Health components
type CombatSystem struct {
	world *World
}

// NewCombatSystem creates a new combat system
func NewCombatSystem(world *World) *CombatSystem {
	return &CombatSystem{world: world}
}

// RequiredComponents returns required component types
func (cs *CombatSystem) RequiredComponents() []ComponentID {
	return []ComponentID{ComponentIDCombat, ComponentIDHealth}
}

// Update processes combat for all matching entities
func (cs *CombatSystem) Update(deltaTime float64, entities []Entity) {
	// Combat logic (simplified)
	for _, entity := range entities {
		combat, _ := cs.world.GetComponent(entity.ID, ComponentIDCombat).(*Combat)
		health, _ := cs.world.GetComponent(entity.ID, ComponentIDHealth).(*Health)

		if combat == nil || health == nil {
			continue
		}

		// Combat processing logic here
		// This is a placeholder - actual combat would be more complex
	}
}

// ParallelSystem processes entities in parallel using worker pools
type ParallelSystem struct {
	world      *World
	workerPool chan func()
	wg         sync.WaitGroup
	workers    int
}

// NewParallelSystem creates a new parallel system
func NewParallelSystem(world *World, workers int) *ParallelSystem {
	ps := &ParallelSystem{
		world:      world,
		workerPool: make(chan func(), workers*2),
		workers:    workers,
	}

	// Start worker pool
	for i := 0; i < workers; i++ {
		ps.wg.Add(1)
		go func() {
			defer ps.wg.Done()
			for task := range ps.workerPool {
				task()
			}
		}()
	}

	return ps
}

// RequiredComponents returns required component types (override in embedded systems)
func (ps *ParallelSystem) RequiredComponents() []ComponentID {
	return []ComponentID{}
}

// Update processes entities in parallel
func (ps *ParallelSystem) Update(deltaTime float64, entities []Entity) {
	// Override in embedded systems
}

// Close shuts down the worker pool
func (ps *ParallelSystem) Close() {
	close(ps.workerPool)
	ps.wg.Wait()
}

// Helper function for fast sqrt
func sqrt32(x float32) float32 {
	// Fast approximation using Newton's method
	// For production, use math.Sqrt or lookup table for accuracy
	if x <= 0 {
		return 0
	}
	// Newton's method for sqrt
	guess := x
	for i := 0; i < 5; i++ {
		guess = 0.5 * (guess + x/guess)
	}
	return guess
}
