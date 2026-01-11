// Memory-efficient Entity Component System (ECS) Implementation
// Issue: #2120
// PERFORMANCE: Optimized for 10k+ game entities with <50ms latency

package ecs

import (
	"sync"
	"sync/atomic"
	"unsafe"
)

// EntityID represents a unique entity identifier
type EntityID uint64

// ComponentID represents a unique component type identifier
type ComponentID uint16

// Component represents a game component (e.g., Position, Health, Movement)
// Components should be small structs with optimal field alignment
type Component interface {
	// ComponentType returns the component type ID
	ComponentType() ComponentID
}

// System processes entities with specific component combinations
type System interface {
	// Update processes entities matching the system's component requirements
	Update(deltaTime float64, entities []Entity)
	// RequiredComponents returns the component IDs required by this system
	RequiredComponents() []ComponentID
}

// World manages all entities, components, and systems
type World struct {
	// Entity storage
	entities      map[EntityID]*Entity
	entityMutex   sync.RWMutex
	nextEntityID  atomic.Uint64

	// Component storage (per-component-type arrays for cache efficiency)
	components    map[ComponentID]*ComponentArray
	componentMutex sync.RWMutex

	// System storage
	systems       []System
	systemMutex   sync.RWMutex

	// Entity-component relationships (sparse set for fast lookup)
	entityComponents map[EntityID]*ComponentSet
	relationshipMutex sync.RWMutex

	// Statistics
	entityCount    atomic.Int64
	componentCount atomic.Int64
}

// Entity represents a game entity (just an ID)
// In ECS, entities are just identifiers - all data is in components
type Entity struct {
	ID EntityID
}

// ComponentSet tracks which components an entity has (bitmask for fast checks)
type ComponentSet struct {
	bits   []uint64 // Bit array: bit[i] = 1 if entity has component type i
	count  int      // Number of components
}

// HasComponent checks if entity has a specific component type
func (cs *ComponentSet) HasComponent(componentID ComponentID) bool {
	index := int(componentID) / 64
	bit := int(componentID) % 64
	if index >= len(cs.bits) {
		return false
	}
	return (cs.bits[index] & (1 << bit)) != 0
}

// AddComponent adds a component type to the set
func (cs *ComponentSet) AddComponent(componentID ComponentID) {
	index := int(componentID) / 64
	bit := int(componentID) % 64
	if index >= len(cs.bits) {
		// Grow bits array
		newBits := make([]uint64, index+1)
		copy(newBits, cs.bits)
		cs.bits = newBits
	}
	if (cs.bits[index] & (1 << bit)) == 0 {
		cs.bits[index] |= (1 << bit)
		cs.count++
	}
}

// RemoveComponent removes a component type from the set
func (cs *ComponentSet) RemoveComponent(componentID ComponentID) {
	index := int(componentID) / 64
	bit := int(componentID) % 64
	if index < len(cs.bits) && (cs.bits[index] & (1 << bit)) != 0 {
		cs.bits[index] &^= (1 << bit)
		cs.count--
	}
}

// Matches checks if this component set matches the required components
func (cs *ComponentSet) Matches(required []ComponentID) bool {
	for _, compID := range required {
		if !cs.HasComponent(compID) {
			return false
		}
	}
	return true
}

// ComponentArray stores components of the same type in a dense array
// This provides excellent cache locality (SoA - Structure of Arrays)
type ComponentArray struct {
	components []Component
	entityMap  map[EntityID]int // Maps entity ID to component index
	mutex      sync.RWMutex
}

// NewComponentArray creates a new component array
func NewComponentArray() *ComponentArray {
	return &ComponentArray{
		components: make([]Component, 0, 1024), // Pre-allocate for 1024 components
		entityMap:  make(map[EntityID]int, 1024),
	}
}

// Add adds a component for an entity
func (ca *ComponentArray) Add(entityID EntityID, component Component) {
	ca.mutex.Lock()
	defer ca.mutex.Unlock()

	if index, exists := ca.entityMap[entityID]; exists {
		// Update existing component
		ca.components[index] = component
	} else {
		// Add new component
		index = len(ca.components)
		ca.components = append(ca.components, component)
		ca.entityMap[entityID] = index
	}
}

// Get gets a component for an entity
func (ca *ComponentArray) Get(entityID EntityID) (Component, bool) {
	ca.mutex.RLock()
	defer ca.mutex.RUnlock()

	index, exists := ca.entityMap[entityID]
	if !exists {
		return nil, false
	}
	return ca.components[index], true
}

// Remove removes a component for an entity
func (ca *ComponentArray) Remove(entityID EntityID) bool {
	ca.mutex.Lock()
	defer ca.mutex.Unlock()

	index, exists := ca.entityMap[entityID]
	if !exists {
		return false
	}

	// Swap with last element for O(1) removal
	lastIndex := len(ca.components) - 1
	lastComponent := ca.components[lastIndex]
	lastEntityID := EntityID(0)
	for eid, idx := range ca.entityMap {
		if idx == lastIndex {
			lastEntityID = eid
			break
		}
	}

	if index != lastIndex {
		ca.components[index] = lastComponent
		ca.entityMap[lastEntityID] = index
	}

	ca.components = ca.components[:lastIndex]
	delete(ca.entityMap, entityID)
	return true
}

// Size returns the number of components in the array
func (ca *ComponentArray) Size() int {
	ca.mutex.RLock()
	defer ca.mutex.RUnlock()
	return len(ca.components)
}

// NewWorld creates a new ECS world
func NewWorld() *World {
	return &World{
		entities:         make(map[EntityID]*Entity, 1024),
		components:       make(map[ComponentID]*ComponentArray, 64),
		systems:          make([]System, 0, 32),
		entityComponents: make(map[EntityID]*ComponentSet, 1024),
	}
}

// CreateEntity creates a new entity
func (w *World) CreateEntity() EntityID {
	id := EntityID(w.nextEntityID.Add(1))
	w.entityMutex.Lock()
	w.entities[id] = &Entity{ID: id}
	w.entityComponents[id] = &ComponentSet{
		bits: make([]uint64, 1), // Start with 1 uint64 (64 component types)
	}
	w.entityMutex.Unlock()
	w.entityCount.Add(1)
	return id
}

// DestroyEntity destroys an entity and removes all its components
func (w *World) DestroyEntity(entityID EntityID) {
	w.entityMutex.Lock()
	delete(w.entities, entityID)
	componentSet, exists := w.entityComponents[entityID]
	w.entityMutex.Unlock()

	if !exists {
		return
	}

	// Remove all components
	w.componentMutex.Lock()
	for compID, compArray := range w.components {
		if componentSet.HasComponent(compID) {
			compArray.Remove(entityID)
			w.componentCount.Add(-1)
		}
	}
	w.componentMutex.Unlock()

	w.relationshipMutex.Lock()
	delete(w.entityComponents, entityID)
	w.relationshipMutex.Unlock()

	w.entityCount.Add(-1)
}

// AddComponent adds a component to an entity
func (w *World) AddComponent(entityID EntityID, component Component) {
	compID := component.ComponentType()

	// Add to component array
	w.componentMutex.Lock()
	compArray, exists := w.components[compID]
	if !exists {
		compArray = NewComponentArray()
		w.components[compID] = compArray
	}
	compArray.Add(entityID, component)
	w.componentMutex.Unlock()

	// Update entity-component relationship
	w.relationshipMutex.Lock()
	if compSet, exists := w.entityComponents[entityID]; exists {
		compSet.AddComponent(compID)
	}
	w.relationshipMutex.Unlock()

	w.componentCount.Add(1)
}

// GetComponent gets a component from an entity
func (w *World) GetComponent(entityID EntityID, compID ComponentID) (Component, bool) {
	w.componentMutex.RLock()
	compArray, exists := w.components[compID]
	w.componentMutex.RUnlock()

	if !exists {
		return nil, false
	}

	return compArray.Get(entityID)
}

// RemoveComponent removes a component from an entity
func (w *World) RemoveComponent(entityID EntityID, compID ComponentID) {
	w.componentMutex.Lock()
	if compArray, exists := w.components[compID]; exists {
		compArray.Remove(entityID)
		w.componentCount.Add(-1)
	}
	w.componentMutex.Unlock()

	w.relationshipMutex.Lock()
	if compSet, exists := w.entityComponents[entityID]; exists {
		compSet.RemoveComponent(compID)
	}
	w.relationshipMutex.Unlock()
}

// AddSystem adds a system to the world
func (w *World) AddSystem(system System) {
	w.systemMutex.Lock()
	defer w.systemMutex.Unlock()
	w.systems = append(w.systems, system)
}

// Update updates all systems with matching entities
func (w *World) Update(deltaTime float64) {
	w.systemMutex.RLock()
	systems := make([]System, len(w.systems))
	copy(systems, w.systems)
	w.systemMutex.RUnlock()

	for _, system := range systems {
		required := system.RequiredComponents()
		entities := w.getEntitiesWithComponents(required)
		system.Update(deltaTime, entities)
	}
}

// getEntitiesWithComponents finds all entities that have all required components
func (w *World) getEntitiesWithComponents(required []ComponentID) []Entity {
	w.relationshipMutex.RLock()
	defer w.relationshipMutex.RUnlock()

	var result []Entity
	for entityID, compSet := range w.entityComponents {
		if compSet.Matches(required) {
			result = append(result, Entity{ID: entityID})
		}
	}
	return result
}

// GetEntityCount returns the number of entities
func (w *World) GetEntityCount() int64 {
	return w.entityCount.Load()
}

// GetComponentCount returns the total number of components
func (w *World) GetComponentCount() int64 {
	return w.componentCount.Load()
}

// GetStats returns world statistics
func (w *World) GetStats() map[string]interface{} {
	return map[string]interface{}{
		"entities":   w.entityCount.Load(),
		"components": w.componentCount.Load(),
		"systems":    len(w.systems),
		"memory_bytes": w.estimateMemoryUsage(),
	}
}

// estimateMemoryUsage estimates memory usage in bytes
func (w *World) estimateMemoryUsage() int64 {
	var total int64

	// Entity storage
	total += int64(unsafe.Sizeof(Entity{})) * w.entityCount.Load()

	// Component storage
	w.componentMutex.RLock()
	for _, compArray := range w.components {
		compArray.mutex.RLock()
		total += int64(unsafe.Sizeof(Component(nil))) * int64(len(compArray.components))
		total += int64(unsafe.Sizeof(EntityID(0))+unsafe.Sizeof(int(0))) * int64(len(compArray.entityMap))
		compArray.mutex.RUnlock()
	}
	w.componentMutex.RUnlock()

	// Component sets
	w.relationshipMutex.RLock()
	for _, compSet := range w.entityComponents {
		total += int64(unsafe.Sizeof(uint64(0)) * len(compSet.bits))
		total += int64(unsafe.Sizeof(int(0)))
	}
	w.relationshipMutex.RUnlock()

	return total
}
