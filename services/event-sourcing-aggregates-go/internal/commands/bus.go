// Command Bus for CQRS pattern implementation
// Issue: #2217
// Agent: Backend Agent
package commands

import (
	"context"
	"fmt"
	"reflect"
	"sync"
	"time"

	"go.uber.org/zap"
)

// Command represents a command in CQRS
type Command interface {
	// GetCommandID returns unique command ID
	GetCommandID() string

	// GetCommandType returns command type name
	GetCommandType() string

	// GetAggregateID returns target aggregate ID
	GetAggregateID() string

	// GetTimestamp returns command timestamp
	GetTimestamp() time.Time
}

// BaseCommand provides common command functionality
type BaseCommand struct {
	CommandID   string    `json:"command_id"`
	CommandType string    `json:"command_type"`
	AggregateID string    `json:"aggregate_id"`
	Timestamp   time.Time `json:"timestamp"`
	UserID      string    `json:"user_id,omitempty"`
	SessionID   string    `json:"session_id,omitempty"`
}

// GetCommandID returns unique command ID
func (c *BaseCommand) GetCommandID() string {
	return c.CommandID
}

// GetCommandType returns command type name
func (c *BaseCommand) GetCommandType() string {
	return c.CommandType
}

// GetAggregateID returns target aggregate ID
func (c *BaseCommand) GetAggregateID() string {
	return c.AggregateID
}

// GetTimestamp returns command timestamp
func (c *BaseCommand) GetTimestamp() time.Time {
	return c.Timestamp
}

// CommandHandler defines interface for command handlers
type CommandHandler interface {
	Handle(ctx context.Context, command Command) error
}

// CommandBus manages command routing and execution
type CommandBus struct {
	handlers map[string]CommandHandler
	logger   *zap.Logger
	mu       sync.RWMutex
}

// NewCommandBus creates a new command bus
func NewCommandBus() *CommandBus {
	logger, _ := zap.NewProduction()
	return &CommandBus{
		handlers: make(map[string]CommandHandler),
		logger:   logger,
	}
}

// RegisterHandler registers a command handler for a command type
func (b *CommandBus) RegisterHandler(commandType string, handler CommandHandler) {
	b.mu.Lock()
	defer b.mu.Unlock()

	b.handlers[commandType] = handler
	b.logger.Info("Registered command handler",
		zap.String("command_type", commandType),
		zap.String("handler_type", reflect.TypeOf(handler).String()))
}

// Send sends a command to its handler
func (b *CommandBus) Send(ctx context.Context, command Command) error {
	commandType := command.GetCommandType()

	b.mu.RLock()
	handler, exists := b.handlers[commandType]
	b.mu.RUnlock()

	if !exists {
		b.logger.Error("No handler registered for command",
			zap.String("command_type", commandType),
			zap.String("command_id", command.GetCommandID()))
		return fmt.Errorf("no handler registered for command type: %s", commandType)
	}

	// Add timeout if not already set
	sendCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	start := time.Now()
	defer func() {
		duration := time.Since(start)
		b.logger.Info("Command executed",
			zap.String("command_type", commandType),
			zap.String("command_id", command.GetCommandID()),
			zap.String("aggregate_id", command.GetAggregateID()),
			zap.Duration("duration", duration))
	}()

	err := handler.Handle(sendCtx, command)
	if err != nil {
		b.logger.Error("Command execution failed",
			zap.Error(err),
			zap.String("command_type", commandType),
			zap.String("command_id", command.GetCommandID()),
			zap.String("aggregate_id", command.GetAggregateID()))
		return fmt.Errorf("command execution failed: %w", err)
	}

	return nil
}

// SendAsync sends a command asynchronously
func (b *CommandBus) SendAsync(ctx context.Context, command Command) error {
	go func() {
		if err := b.Send(ctx, command); err != nil {
			b.logger.Error("Async command failed",
				zap.Error(err),
				zap.String("command_type", command.GetCommandType()),
				zap.String("command_id", command.GetCommandID()))
		}
	}()
	return nil
}

// GetRegisteredHandlers returns list of registered command types
func (b *CommandBus) GetRegisteredHandlers() []string {
	b.mu.RLock()
	defer b.mu.RUnlock()

	types := make([]string, 0, len(b.handlers))
	for commandType := range b.handlers {
		types = append(types, commandType)
	}
	return types
}

// Shutdown gracefully shuts down the command bus
func (b *CommandBus) Shutdown(ctx context.Context) error {
	b.logger.Info("Command bus shutting down")
	// Wait for any pending commands to complete
	time.Sleep(100 * time.Millisecond)
	return nil
}

// Player command implementations

// CreatePlayerCommand represents player creation command
type CreatePlayerCommand struct {
	BaseCommand
	Username string `json:"username"`
	Email    string `json:"email"`
}

// NewCreatePlayerCommand creates a new player creation command
func NewCreatePlayerCommand(aggregateID, username, email, userID string) *CreatePlayerCommand {
	return &CreatePlayerCommand{
		BaseCommand: BaseCommand{
			CommandID:   fmt.Sprintf("create-player-%s-%d", aggregateID, time.Now().UnixNano()),
			CommandType: "CreatePlayer",
			AggregateID: aggregateID,
			Timestamp:   time.Now().UTC(),
			UserID:      userID,
		},
		Username: username,
		Email:    email,
	}
}

// GainPlayerLevelCommand represents level gain command
type GainPlayerLevelCommand struct {
	BaseCommand
	NewLevel    int   `json:"new_level"`
	Experience  int64 `json:"experience"`
}

// NewGainPlayerLevelCommand creates a new level gain command
func NewGainPlayerLevelCommand(aggregateID string, newLevel int, experience int64, userID string) *GainPlayerLevelCommand {
	return &GainPlayerLevelCommand{
		BaseCommand: BaseCommand{
			CommandID:   fmt.Sprintf("gain-level-%s-%d", aggregateID, time.Now().UnixNano()),
			CommandType: "GainPlayerLevel",
			AggregateID: aggregateID,
			Timestamp:   time.Now().UTC(),
			UserID:      userID,
		},
		NewLevel:   newLevel,
		Experience: experience,
	}
}

// EquipPlayerItemCommand represents item equipping command
type EquipPlayerItemCommand struct {
	BaseCommand
	ItemID string `json:"item_id"`
	Slot   string `json:"slot"`
}

// NewEquipPlayerItemCommand creates a new item equip command
func NewEquipPlayerItemCommand(aggregateID, itemID, slot, userID string) *EquipPlayerItemCommand {
	return &EquipPlayerItemCommand{
		BaseCommand: BaseCommand{
			CommandID:   fmt.Sprintf("equip-item-%s-%d", aggregateID, time.Now().UnixNano()),
			CommandType: "EquipPlayerItem",
			AggregateID: aggregateID,
			Timestamp:   time.Now().UTC(),
			UserID:      userID,
		},
		ItemID: itemID,
		Slot:   slot,
	}
}