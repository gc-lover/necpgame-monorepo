// Issue: #2217
// PERFORMANCE: Optimized command bus for high-throughput command processing
package commands

import (
	"context"
	"fmt"
	"reflect"
	"sync"
	"time"

	"go.uber.org/zap"
)

// Command defines the interface for commands
type Command interface {
	// CommandType returns the command type string
	CommandType() string

	// AggregateID returns the target aggregate ID
	AggregateID() string
}

// CommandHandler defines the interface for command handlers
type CommandHandler interface {
	Handle(ctx context.Context, command Command) error
}

// CommandHandlerFunc is a function type for command handlers
type CommandHandlerFunc func(ctx context.Context, command Command) error

// Handle implements CommandHandler interface for functions
func (f CommandHandlerFunc) Handle(ctx context.Context, command Command) error {
	return f(ctx, command)
}

// Bus manages command routing and handling
type Bus struct {
	handlers map[string]CommandHandler
	logger   *zap.Logger
	mu       sync.RWMutex
}

// NewBus creates a new command bus
func NewBus(logger *zap.Logger) *Bus {
	return &Bus{
		handlers: make(map[string]CommandHandler),
		logger:   logger,
	}
}

// RegisterHandler registers a command handler
func (b *Bus) RegisterHandler(commandType string, handler CommandHandler) error {
	b.mu.Lock()
	defer b.mu.Unlock()

	if _, exists := b.handlers[commandType]; exists {
		return fmt.Errorf("handler for command type %s already registered", commandType)
	}

	b.handlers[commandType] = handler
	b.logger.Info("Command handler registered",
		zap.String("command_type", commandType),
		zap.String("handler_type", reflect.TypeOf(handler).String()))

	return nil
}

// Send sends a command to its handler
func (b *Bus) Send(ctx context.Context, command Command) error {
	commandType := command.CommandType()

	b.mu.RLock()
	handler, exists := b.handlers[commandType]
	b.mu.RUnlock()

	if !exists {
		b.logger.Error("No handler registered for command type",
			zap.String("command_type", commandType))
		return fmt.Errorf("no handler registered for command type: %s", commandType)
	}

	// Execute command with timing
	startTime := time.Now()

	err := handler.Handle(ctx, command)

	duration := time.Since(startTime)

	if err != nil {
		b.logger.Error("Command execution failed",
			zap.String("command_type", commandType),
			zap.String("aggregate_id", command.AggregateID()),
			zap.Error(err),
			zap.Duration("duration", duration))
		return err
	}

	b.logger.Info("Command executed successfully",
		zap.String("command_type", commandType),
		zap.String("aggregate_id", command.AggregateID()),
		zap.Duration("duration", duration))

	return nil
}

// SendAsync sends a command asynchronously
func (b *Bus) SendAsync(ctx context.Context, command Command) chan error {
	errChan := make(chan error, 1)

	go func() {
		defer close(errChan)
		errChan <- b.Send(ctx, command)
	}()

	return errChan
}

// GetRegisteredCommands returns all registered command types
func (b *Bus) GetRegisteredCommands() []string {
	b.mu.RLock()
	defer b.mu.RUnlock()

	commands := make([]string, 0, len(b.handlers))
	for commandType := range b.handlers {
		commands = append(commands, commandType)
	}

	return commands
}

// UnregisterHandler removes a command handler
func (b *Bus) UnregisterHandler(commandType string) {
	b.mu.Lock()
	defer b.mu.Unlock()

	if _, exists := b.handlers[commandType]; exists {
		delete(b.handlers, commandType)
		b.logger.Info("Command handler unregistered",
			zap.String("command_type", commandType))
	}
}
