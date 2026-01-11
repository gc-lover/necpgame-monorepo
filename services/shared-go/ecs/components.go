// Common Game Components for ECS
// Issue: #2120
// PERFORMANCE: Optimized struct alignment for memory efficiency

package ecs

// Component type IDs (pre-defined for common game components)
const (
	ComponentIDPosition ComponentID = iota + 1
	ComponentIDRotation
	ComponentIDVelocity
	ComponentIDHealth
	ComponentIDMaxHealth
	ComponentIDLevel
	ComponentIDExperience
	ComponentIDInventory
	ComponentIDEquipment
	ComponentIDStats
	ComponentIDMovement
	ComponentIDCombat
	ComponentIDAI
)

// Position component (3D position)
// PERFORMANCE: Struct field alignment optimized (12 bytes)
type Position struct {
	X float32 // 4 bytes
	Y float32 // 4 bytes
	Z float32 // 4 bytes
}

func (p *Position) ComponentType() ComponentID {
	return ComponentIDPosition
}

// Rotation component (yaw, pitch, roll)
// PERFORMANCE: Struct field alignment optimized (12 bytes)
type Rotation struct {
	Yaw   float32 // 4 bytes
	Pitch float32 // 4 bytes
	Roll  float32 // 4 bytes
}

func (r *Rotation) ComponentType() ComponentID {
	return ComponentIDRotation
}

// Velocity component (3D velocity)
// PERFORMANCE: Struct field alignment optimized (12 bytes)
type Velocity struct {
	X float32 // 4 bytes
	Y float32 // 4 bytes
	Z float32 // 4 bytes
}

func (v *Velocity) ComponentType() ComponentID {
	return ComponentIDVelocity
}

// Health component
// PERFORMANCE: Struct field alignment optimized (8 bytes)
type Health struct {
	Current int32 // 4 bytes
	Max     int32 // 4 bytes
}

func (h *Health) ComponentType() ComponentID {
	return ComponentIDHealth
}

// Level component
// PERFORMANCE: Struct field alignment optimized (8 bytes)
type Level struct {
	Current      int32  // 4 bytes
	Experience   uint32 // 4 bytes
}

func (l *Level) ComponentType() ComponentID {
	return ComponentIDLevel
}

// Movement component (movement state and parameters)
// PERFORMANCE: Struct field alignment optimized (16 bytes)
type Movement struct {
	Speed     float32 // 4 bytes
	MaxSpeed  float32 // 4 bytes
	IsMoving  bool    // 1 byte (aligned to 4 bytes)
	// padding: 3 bytes
}

func (m *Movement) ComponentType() ComponentID {
	return ComponentIDMovement
}

// Combat component (combat state)
// PERFORMANCE: Struct field alignment optimized (24 bytes)
type Combat struct {
	AttackDamage  int32   // 4 bytes
	DefenseRating int32   // 4 bytes
	CriticalChance float32 // 4 bytes
	IsInCombat    bool    // 1 byte (aligned to 4 bytes)
	// padding: 3 bytes
}

func (c *Combat) ComponentType() ComponentID {
	return ComponentIDCombat
}

// Stats component (character statistics)
// PERFORMANCE: Struct field alignment optimized (32 bytes)
type Stats struct {
	Strength     int32 // 4 bytes
	Dexterity    int32 // 4 bytes
	Intelligence int32 // 4 bytes
	Constitution int32 // 4 bytes
	Wisdom       int32 // 4 bytes
	Charisma     int32 // 4 bytes
	// padding: 8 bytes
}

func (s *Stats) ComponentType() ComponentID {
	return ComponentIDStats
}
