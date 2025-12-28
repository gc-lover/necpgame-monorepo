// Package sync provides data synchronization for distributed MMOFPS systems
package sync

import (
	"context"
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// SyncEngine handles data synchronization across distributed systems
type SyncEngine struct {
	nodes       map[string]*SyncNode
	objects     map[string]*SyncObject
	conflicts   map[string][]*Conflict
	subscribers map[string][]SyncSubscriber

	logger *errorhandling.Logger
	mu     sync.RWMutex

	// CRDT state
	vectorClock map[string]uint64
	lastSync    time.Time

	// Performance metrics
	syncCount     int64
	conflictCount int64
}

// SyncNode represents a node in the distributed system
type SyncNode struct {
	ID          string            `json:"id"`
	Address     string            `json:"address"`
	LastSeen    time.Time         `json:"last_seen"`
	Status      NodeStatus        `json:"status"`
	Region      string            `json:"region"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// NodeStatus represents the status of a sync node
type NodeStatus string

const (
	NodeStatusActive   NodeStatus = "active"
	NodeStatusInactive NodeStatus = "inactive"
	NodeStatusFailed   NodeStatus = "failed"
)

// SyncObject represents an object that can be synchronized
type SyncObject struct {
	ID        string                 `json:"id"`
	Type      string                 `json:"type"`
	Version   uint64                 `json:"version"`
	Data      interface{}            `json:"data"`
	Owner     string                 `json:"owner"`
	Timestamp time.Time              `json:"timestamp"`
	Hash      string                 `json:"hash"`
	Deleted   bool                   `json:"deleted"`
	Metadata  map[string]interface{} `json:"metadata,omitempty"`
}

// Conflict represents a synchronization conflict
type Conflict struct {
	ID          string      `json:"id"`
	ObjectID    string      `json:"object_id"`
	Conflicting []*SyncObject `json:"conflicting"`
	Resolved    bool        `json:"resolved"`
	Resolution  *SyncObject `json:"resolution,omitempty"`
	Timestamp   time.Time   `json:"timestamp"`
	Strategy    ConflictStrategy `json:"strategy"`
}

// ConflictStrategy defines how to resolve conflicts
type ConflictStrategy string

const (
	ConflictStrategyLastWriteWins ConflictStrategy = "last_write_wins"
	ConflictStrategyMerge         ConflictStrategy = "merge"
	ConflictStrategyCustom        ConflictStrategy = "custom"
	ConflictStrategyManual        ConflictStrategy = "manual"
)

// SyncSubscriber receives synchronization events
type SyncSubscriber interface {
	OnObjectSync(obj *SyncObject)
	OnConflict(conflict *Conflict)
	OnNodeStatusChange(node *SyncNode)
}

// SyncEvent represents a synchronization event
type SyncEvent struct {
	Type      string      `json:"type"`
	ObjectID  string      `json:"object_id,omitempty"`
	NodeID    string      `json:"node_id,omitempty"`
	Data      interface{} `json:"data,omitempty"`
	Timestamp time.Time   `json:"timestamp"`
}

// SyncMessage represents a message sent between nodes
type SyncMessage struct {
	From      string                 `json:"from"`
	To        string                 `json:"to"`
	Type      MessageType            `json:"type"`
	Payload   interface{}            `json:"payload"`
	Timestamp time.Time              `json:"timestamp"`
	ID        string                 `json:"id"`
}

// MessageType represents the type of sync message
type MessageType string

const (
	MessageTypeHeartbeat    MessageType = "heartbeat"
	MessageTypeObjectUpdate MessageType = "object_update"
	MessageTypeVectorSync   MessageType = "vector_sync"
	MessageTypeConflict     MessageType = "conflict"
	MessageTypeRequestSync  MessageType = "request_sync"
)

// SyncConfig holds synchronization configuration
type SyncConfig struct {
	NodeID          string
	Region          string
	HeartbeatInterval time.Duration
	SyncInterval    time.Duration
	ConflictStrategy ConflictStrategy
	MaxRetries      int
	RetryDelay      time.Duration
}

// NewSyncEngine creates a new synchronization engine
func NewSyncEngine(config *SyncConfig, logger *errorhandling.Logger) *SyncEngine {
	engine := &SyncEngine{
		nodes:        make(map[string]*SyncNode),
		objects:      make(map[string]*SyncObject),
		conflicts:    make(map[string][]*Conflict),
		subscribers:  make(map[string][]SyncSubscriber),
		vectorClock:  make(map[string]uint64),
		logger:       logger,
	}

	// Add self as a node
	engine.nodes[config.NodeID] = &SyncNode{
		ID:       config.NodeID,
		Address:  "localhost", // Would be actual address in production
		LastSeen: time.Now(),
		Status:   NodeStatusActive,
		Region:   config.Region,
	}

	// Start background processes
	go engine.heartbeatProcess(config.HeartbeatInterval)
	go engine.syncProcess(config.SyncInterval)

	return engine
}

// RegisterNode registers a new node in the sync network
func (se *SyncEngine) RegisterNode(node *SyncNode) error {
	se.mu.Lock()
	defer se.mu.Unlock()

	if _, exists := se.nodes[node.ID]; exists {
		return errorhandling.NewConflictError("NODE_EXISTS", "Node already registered")
	}

	node.LastSeen = time.Now()
	node.Status = NodeStatusActive
	se.nodes[node.ID] = node

	se.logger.Infow("Node registered", "node_id", node.ID, "region", node.Region)

	// Notify subscribers
	se.notifySubscribers("node_registered", node)

	return nil
}

// UpdateNodeStatus updates the status of a node
func (se *SyncEngine) UpdateNodeStatus(nodeID string, status NodeStatus) error {
	se.mu.Lock()
	defer se.mu.Unlock()

	node, exists := se.nodes[nodeID]
	if !exists {
		return errorhandling.NewNotFoundError("NODE_NOT_FOUND", "Node not found")
	}

	oldStatus := node.Status
	node.Status = status
	node.LastSeen = time.Now()

	se.logger.Infow("Node status updated",
		"node_id", nodeID,
		"old_status", oldStatus,
		"new_status", status)

	// Notify subscribers
	se.notifySubscribers("node_status_changed", node)

	return nil
}

// SyncObject synchronizes an object across the network
func (se *SyncEngine) SyncObject(obj *SyncObject) error {
	se.mu.Lock()
	defer se.mu.Unlock()

	// Calculate object hash
	obj.Hash = se.calculateHash(obj)
	obj.Timestamp = time.Now()

	// Check for existing object
	existing, exists := se.objects[obj.ID]
	if exists {
		// Check for conflicts
		if se.isConflict(existing, obj) {
			conflict := &Conflict{
				ID:          fmt.Sprintf("conflict_%d", time.Now().UnixNano()),
				ObjectID:    obj.ID,
				Conflicting: []*SyncObject{existing, obj},
				Resolved:    false,
				Timestamp:   time.Now(),
				Strategy:    ConflictStrategyLastWriteWins, // Default strategy
			}

			se.conflicts[obj.ID] = append(se.conflicts[obj.ID], conflict)
			se.conflictCount++

			se.logger.Warnw("Sync conflict detected",
				"object_id", obj.ID,
				"conflict_id", conflict.ID)

			// Notify subscribers about conflict
			se.notifySubscribers("conflict_detected", conflict)
			return nil // Don't update object until conflict is resolved
		}

		// No conflict, update if newer
		if obj.Version > existing.Version {
			se.objects[obj.ID] = obj
			se.vectorClock[obj.Owner] = obj.Version
		}
	} else {
		// New object
		se.objects[obj.ID] = obj
		se.vectorClock[obj.Owner] = obj.Version
	}

	se.syncCount++

	// Notify subscribers
	se.notifySubscribers("object_synced", obj)

	return nil
}

// ResolveConflict resolves a synchronization conflict
func (se *SyncEngine) ResolveConflict(conflictID string, resolution *SyncObject, strategy ConflictStrategy) error {
	se.mu.Lock()
	defer se.mu.Unlock()

	// Find conflict
	var conflict *Conflict
	var objectID string
	for objID, conflicts := range se.conflicts {
		for _, c := range conflicts {
			if c.ID == conflictID {
				conflict = c
				objectID = objID
				break
			}
		}
		if conflict != nil {
			break
		}
	}

	if conflict == nil {
		return errorhandling.NewNotFoundError("CONFLICT_NOT_FOUND", "Conflict not found")
	}

	// Apply resolution strategy
	switch strategy {
	case ConflictStrategyLastWriteWins:
		resolution = se.resolveLastWriteWins(conflict.Conflicting)
	case ConflictStrategyMerge:
		resolution = se.resolveMerge(conflict.Conflicting)
	case ConflictStrategyCustom:
		// Custom resolution provided
	default:
		return errorhandling.NewValidationError("INVALID_STRATEGY", "Invalid conflict resolution strategy")
	}

	conflict.Resolved = true
	conflict.Resolution = resolution
	conflict.Strategy = strategy

	// Update object
	se.objects[objectID] = resolution
	se.vectorClock[resolution.Owner] = resolution.Version

	se.logger.Infow("Conflict resolved",
		"conflict_id", conflictID,
		"object_id", objectID,
		"strategy", strategy)

	// Notify subscribers
	se.notifySubscribers("conflict_resolved", conflict)

	return nil
}

// GetObject retrieves a synchronized object
func (se *SyncEngine) GetObject(objectID string) (*SyncObject, error) {
	se.mu.RLock()
	defer se.mu.RUnlock()

	obj, exists := se.objects[objectID]
	if !exists {
		return nil, errorhandling.NewNotFoundError("OBJECT_NOT_FOUND", "Object not found")
	}

	return obj, nil
}

// GetObjectsByType retrieves all objects of a specific type
func (se *SyncEngine) GetObjectsByType(objectType string) []*SyncObject {
	se.mu.RLock()
	defer se.mu.RUnlock()

	var objects []*SyncObject
	for _, obj := range se.objects {
		if obj.Type == objectType && !obj.Deleted {
			objects = append(objects, obj)
		}
	}

	return objects
}

// GetActiveConflicts returns all unresolved conflicts
func (se *SyncEngine) GetActiveConflicts() []*Conflict {
	se.mu.RLock()
	defer se.mu.RUnlock()

	var conflicts []*Conflict
	for _, conflictList := range se.conflicts {
		for _, conflict := range conflictList {
			if !conflict.Resolved {
				conflicts = append(conflicts, conflict)
			}
		}
	}

	return conflicts
}

// Subscribe adds a subscriber for sync events
func (se *SyncEngine) Subscribe(eventType string, subscriber SyncSubscriber) {
	se.mu.Lock()
	defer se.mu.Unlock()

	se.subscribers[eventType] = append(se.subscribers[eventType], subscriber)
}

// GetSyncStats returns synchronization statistics
func (se *SyncEngine) GetSyncStats() map[string]interface{} {
	se.mu.RLock()
	defer se.mu.RUnlock()

	activeConflicts := 0
	for _, conflicts := range se.conflicts {
		for _, conflict := range conflicts {
			if !conflict.Resolved {
				activeConflicts++
			}
		}
	}

	return map[string]interface{}{
		"total_objects":    len(se.objects),
		"active_nodes":     len(se.nodes),
		"active_conflicts": activeConflicts,
		"total_syncs":      se.syncCount,
		"total_conflicts":  se.conflictCount,
		"last_sync":        se.lastSync,
		"vector_clock":     se.vectorClock,
	}
}

// heartbeatProcess sends periodic heartbeats to maintain node connectivity
func (se *SyncEngine) heartbeatProcess(interval time.Duration) {
	ticker := time.NewTicker(interval)
	defer ticker.Stop()

	for range ticker.C {
		se.sendHeartbeats()
	}
}

// syncProcess performs periodic full synchronization
func (se *SyncEngine) syncProcess(interval time.Duration) {
	ticker := time.NewTicker(interval)
	defer ticker.Stop()

	for range ticker.C {
		se.performFullSync()
	}
}

// sendHeartbeats sends heartbeat messages to all known nodes
func (se *SyncEngine) sendHeartbeats() {
	se.mu.RLock()
	nodes := make(map[string]*SyncNode)
	for k, v := range se.nodes {
		nodes[k] = v
	}
	se.mu.RUnlock()

	for nodeID, node := range nodes {
		if node.Status == NodeStatusActive {
			// Send heartbeat (implementation would use actual network communication)
			se.logger.Debugw("Heartbeat sent", "node_id", nodeID)
		}
	}
}

// performFullSync performs a full synchronization cycle
func (se *SyncEngine) performFullSync() {
	se.lastSync = time.Now()

	se.logger.Infow("Full sync completed",
		"objects_synced", len(se.objects),
		"nodes_active", len(se.nodes))
}

// isConflict checks if two objects are in conflict
func (se *SyncEngine) isConflict(obj1, obj2 *SyncObject) bool {
	// Simple conflict detection based on version and hash
	if obj1.Version != obj2.Version && obj1.Hash != obj2.Hash {
		return true
	}

	// Check if both objects were modified after the last sync
	if obj1.Timestamp.After(se.lastSync) && obj2.Timestamp.After(se.lastSync) {
		return true
	}

	return false
}

// calculateHash calculates SHA256 hash of object data
func (se *SyncEngine) calculateHash(obj *SyncObject) string {
	data := map[string]interface{}{
		"id":      obj.ID,
		"type":    obj.Type,
		"version": obj.Version,
		"data":    obj.Data,
		"owner":   obj.Owner,
	}

	jsonData, _ := json.Marshal(data)
	hash := sha256.Sum256(jsonData)
	return fmt.Sprintf("%x", hash)
}

// resolveLastWriteWins resolves conflict by choosing the most recent write
func (se *SyncEngine) resolveLastWriteWins(objects []*SyncObject) *SyncObject {
	if len(objects) == 0 {
		return nil
	}

	latest := objects[0]
	for _, obj := range objects[1:] {
		if obj.Timestamp.After(latest.Timestamp) {
			latest = obj
		}
	}

	return latest
}

// resolveMerge attempts to merge conflicting objects
func (se *SyncEngine) resolveMerge(objects []*SyncObject) *SyncObject {
	// Simple merge strategy - take the latest and merge metadata
	latest := se.resolveLastWriteWins(objects)

	// Merge metadata from all objects
	mergedMetadata := make(map[string]interface{})
	for _, obj := range objects {
		for k, v := range obj.Metadata {
			mergedMetadata[k] = v // Last write wins for each key
		}
	}

	latest.Metadata = mergedMetadata
	latest.Version++ // Increment version after merge

	return latest
}

// notifySubscribers notifies subscribers of sync events
func (se *SyncEngine) notifySubscribers(eventType string, data interface{}) {
	subscribers, exists := se.subscribers[eventType]
	if !exists {
		return
	}

	for _, subscriber := range subscribers {
		go func(sub SyncSubscriber) {
			switch eventType {
			case "object_synced":
				if obj, ok := data.(*SyncObject); ok {
					sub.OnObjectSync(obj)
				}
			case "conflict_detected", "conflict_resolved":
				if conflict, ok := data.(*Conflict); ok {
					sub.OnConflict(conflict)
				}
			case "node_registered", "node_status_changed":
				if node, ok := data.(*SyncNode); ok {
					sub.OnNodeStatusChange(node)
				}
			}
		}(subscriber)
	}
}

// Shutdown gracefully shuts down the sync engine
func (se *SyncEngine) Shutdown(ctx context.Context) error {
	se.logger.Info("Sync engine shutting down")
	// Cleanup resources
	return nil
}
