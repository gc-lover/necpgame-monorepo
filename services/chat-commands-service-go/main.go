// Issue: #142074390
// [Backend] Реализовать Chat Commands Service: ogen handlers implementation

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"necpgame/services/chat-commands-service-go/pkg/api"
)

// CommandProcessor handles chat command processing logic
type CommandProcessor struct {
	logger         *zap.Logger
	commandConfigs map[string]CommandConfig
	permissions    map[string][]string
	cooldowns      map[string]time.Duration
}

// CommandConfig represents configuration for a chat command
type CommandConfig struct {
	Name             string
	Category         string
	RequiredPerms    []string
	Cooldown         time.Duration
	Description      string
	Syntax           string
	Enabled          bool
}

// ChatCommandsHandler implements the generated Handler interface
type ChatCommandsHandler struct {
	logger           *zap.Logger
	commandProcessor *CommandProcessor
}

// NewChatCommandsHandler creates new chat commands handler
func NewChatCommandsHandler(logger *zap.Logger, processor *CommandProcessor) *ChatCommandsHandler {
	return &ChatCommandsHandler{
		logger:           logger,
		commandProcessor: processor,
	}
}

// Chat Commands Service Health Check
func (h *ChatCommandsHandler) ChatCommandsServiceHealthCheck(ctx context.Context) (api.ChatCommandsServiceHealthCheckRes, error) {
	h.logger.Info("Health check requested")

	return &api.HealthResponseHeaders{
		Response: api.HealthResponse{
			Status: api.HealthResponseStatusHealthy,
			Timestamp: time.Now(),
			Uptime: api.OptString{Value: "1h 30m", Set: true},
			Version: api.OptString{Value: "1.0.0", Set: true},
			CommandProcessingActive: true,
			ActiveCommandsCount: api.OptInt64{Value: 0, Set: true},
			DatabaseConnection: api.HealthResponseDatabaseConnectionConnected,
		},
	}, nil
}

// Execute Chat Command
func (h *ChatCommandsHandler) ExecuteChatCommand(ctx context.Context, req api.CommandExecutionRequest) (api.ExecuteChatCommandRes, error) {
	h.logger.Info("Executing chat command", zap.String("command", req.Command))

	// Parse command
	parts := strings.Fields(req.Command)
	if len(parts) == 0 {
		return &api.CommandExecutionResponseHeaders{
			Response: api.CommandExecutionResponse{
				Success: false,
				CommandId: "error-invalid-command",
				ResultMessage: "Invalid command format",
				ExecutionTimeMs: 5,
			},
		}, nil
	}

	commandName := strings.ToLower(parts[0])
	if !strings.HasPrefix(commandName, "/") {
		return &api.CommandExecutionResponseHeaders{
			Response: api.CommandExecutionResponse{
				Success: false,
				CommandId: "error-invalid-prefix",
				ResultMessage: "Commands must start with /",
				ExecutionTimeMs: 5,
			},
		}, nil
	}

	// Check if command exists and is enabled
	config, exists := h.commandProcessor.commandConfigs[commandName]
	if !exists || !config.Enabled {
		return &api.CommandExecutionResponseHeaders{
			Response: api.CommandExecutionResponse{
				Success: false,
				CommandId: "error-unknown-command",
				ResultMessage: fmt.Sprintf("Unknown or disabled command: %s", commandName),
				ExecutionTimeMs: 10,
			},
		}, nil
	}

	// Mock successful execution
	return &api.CommandExecutionResponseHeaders{
		Response: api.CommandExecutionResponse{
			Success: true,
			CommandId: fmt.Sprintf("cmd-%d", time.Now().Unix()),
			ResultMessage: fmt.Sprintf("Command '%s' executed successfully", commandName),
			ExecutionTimeMs: 25,
			AffectedPlayers: &[]string{req.PlayerId.String()},
			CooldownRemainingMs: &config.Cooldown.Milliseconds(),
		},
	}, nil
}

// Validate Chat Command
func (h *ChatCommandsHandler) ValidateChatCommand(ctx context.Context, req api.CommandValidationRequest) (api.ValidateChatCommandRes, error) {
	h.logger.Info("Validating chat command", zap.String("command", req.Command))

	parts := strings.Fields(req.Command)
	if len(parts) == 0 {
		return api.CommandValidationResponse{
			Valid: false,
			CommandType: api.CommandValidationResponseCommandTypeSystem,
			SyntaxHint: "Commands must start with / and contain at least one word",
		}, nil
	}

	commandName := strings.ToLower(parts[0])
	if !strings.HasPrefix(commandName, "/") {
		return api.CommandValidationResponse{
			Valid: false,
			CommandType: api.CommandValidationResponseCommandTypeSystem,
			SyntaxHint: "Commands must start with /",
		}, nil
	}

	config, exists := h.commandProcessor.commandConfigs[commandName]
	if !exists {
		return api.CommandValidationResponse{
			Valid: false,
			CommandType: api.CommandValidationResponseCommandTypeSystem,
			SyntaxHint: fmt.Sprintf("Unknown command: %s. Use /help for available commands", commandName),
		}, nil
	}

	// Mock permission check
	hasPerms := true // In real implementation, check against user's permissions

	return api.CommandValidationResponse{
		Valid: true,
		CommandType: api.CommandValidationResponseCommandType(config.Category),
		RequiredPermissions: &config.RequiredPerms,
		HasPermissions: &hasPerms,
		SyntaxHint: config.Syntax,
		CooldownMs: &config.Cooldown.Milliseconds(),
	}, nil
}

// Execute Kick Command
func (h *ChatCommandsHandler) ExecuteKickCommand(ctx context.Context, req api.AdminCommandRequest) (api.ExecuteKickCommandRes, error) {
	h.logger.Info("Executing kick command", zap.String("target", req.TargetPlayerId.String()))

	return api.AdminCommandResponse{
		Success: true,
		CommandId: fmt.Sprintf("kick-%d", time.Now().Unix()),
		Message: fmt.Sprintf("Player has been kicked from the channel"),
		AffectedChannels: &[]string{req.ChannelId.String()},
	}, nil
}

// Execute Ban Command
func (h *ChatCommandsHandler) ExecuteBanCommand(ctx context.Context, req api.AdminCommandRequest) (api.ExecuteBanCommandRes, error) {
	h.logger.Info("Executing ban command", zap.String("target", req.TargetPlayerId.String()))

	expiresAt := time.Now().Add(time.Duration(req.DurationMinutes) * time.Minute)
	return api.AdminCommandResponse{
		Success: true,
		CommandId: fmt.Sprintf("ban-%d", time.Now().Unix()),
		Message: fmt.Sprintf("Player has been banned for %d minutes", req.DurationMinutes),
		ExpiresAt: &expiresAt,
		AffectedChannels: &[]string{req.ChannelId.String()},
	}, nil
}

// Execute Mute Command
func (h *ChatCommandsHandler) ExecuteMuteCommand(ctx context.Context, req api.AdminCommandRequest) (api.ExecuteMuteCommandRes, error) {
	h.logger.Info("Executing mute command", zap.String("target", req.TargetPlayerId.String()))

	expiresAt := time.Now().Add(time.Duration(req.DurationMinutes) * time.Minute)
	return api.AdminCommandResponse{
		Success: true,
		CommandId: fmt.Sprintf("mute-%d", time.Now().Unix()),
		Message: fmt.Sprintf("Player has been muted for %d minutes", req.DurationMinutes),
		ExpiresAt: &expiresAt,
		AffectedChannels: &[]string{req.ChannelId.String()},
	}, nil
}

// Execute Whisper Command
func (h *ChatCommandsHandler) ExecuteWhisperCommand(ctx context.Context, req api.SocialCommandRequest) (api.ExecuteWhisperCommandRes, error) {
	h.logger.Info("Executing whisper command", zap.String("target", req.TargetPlayerId.String()))

	return api.SocialCommandResponse{
		Success: true,
		MessageId: fmt.Sprintf("whisper-%d", time.Now().Unix()),
		DeliveryStatus: api.SocialCommandResponseDeliveryStatusDelivered,
		RecipientOnline: true,
	}, nil
}

// Get Command Help
func (h *ChatCommandsHandler) GetCommandHelp(ctx context.Context, params api.GetCommandHelpParams) (api.GetCommandHelpRes, error) {
	h.logger.Info("Getting command help")

	var commands []api.CommandHelp
	for _, config := range h.commandProcessor.commandConfigs {
		if !config.Enabled {
			continue
		}

		commands = append(commands, api.CommandHelp{
			Command: config.Name,
			Description: config.Description,
			Syntax: config.Syntax,
			Category: config.Category,
			RequiredPermissions: &config.RequiredPerms,
			CooldownSeconds: int32(config.Cooldown.Seconds()),
			Examples: &[]string{fmt.Sprintf("%s example", config.Name)},
		})
	}

	return &api.CommandHelpResponseHeaders{
		Response: api.CommandHelpResponse{
			Commands: commands,
			TotalCommands: int32(len(commands)),
			PlayerPermissions: []string{"player", "moderator"}, // Mock permissions
			LastUpdated: time.Now(),
		},
	}, nil
}

// Get Command Configuration
func (h *ChatCommandsHandler) GetCommandConfiguration(ctx context.Context) (api.GetCommandConfigurationRes, error) {
	h.logger.Info("Getting command configuration")

	var enabledCommands []string
	var disabledCommands []string
	permissionLevels := make(map[string][]string)

	for name, config := range h.commandProcessor.commandConfigs {
		if config.Enabled {
			enabledCommands = append(enabledCommands, name)
			for _, perm := range config.RequiredPerms {
				permissionLevels[perm] = append(permissionLevels[perm], name)
			}
		} else {
			disabledCommands = append(disabledCommands, name)
		}
	}

	return &api.CommandConfigurationResponseHeaders{
		Response: api.CommandConfigurationResponse{
			Configuration: &api.CommandConfigurationResponseConfiguration{
				EnabledCommands: enabledCommands,
				DisabledCommands: disabledCommands,
				GlobalCooldowns: &map[string]int32{"default": 5},
				PermissionLevels: &permissionLevels,
			},
			Version: "1.0.0",
			LastUpdated: time.Now(),
		},
	}, nil
}

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to initialize logger", err)
	}
	defer logger.Sync()

	// Initialize command processor with sample commands
	commandProcessor := &CommandProcessor{
		logger: logger,
		commandConfigs: map[string]CommandConfig{
			"/help": {
				Name: "/help",
				Category: "informational",
				RequiredPerms: []string{"player"},
				Cooldown: 5 * time.Second,
				Description: "Get help for available commands",
				Syntax: "/help [command]",
				Enabled: true,
			},
			"/whisper": {
				Name: "/whisper",
				Category: "social",
				RequiredPerms: []string{"player"},
				Cooldown: 1 * time.Second,
				Description: "Send private message to another player",
				Syntax: "/whisper <player> <message>",
				Enabled: true,
			},
			"/kick": {
				Name: "/kick",
				Category: "administrative",
				RequiredPerms: []string{"moderator"},
				Cooldown: 30 * time.Second,
				Description: "Remove a player from the current channel",
				Syntax: "/kick <player> [reason] [duration]",
				Enabled: true,
			},
			"/ban": {
				Name: "/ban",
				Category: "administrative",
				RequiredPerms: []string{"admin"},
				Cooldown: 300 * time.Second,
				Description: "Ban a player from the channel",
				Syntax: "/ban <player> <reason> [duration]",
				Enabled: true,
			},
			"/mute": {
				Name: "/mute",
				Category: "administrative",
				RequiredPerms: []string{"moderator"},
				Cooldown: 60 * time.Second,
				Description: "Mute a player in the channel",
				Syntax: "/mute <player> [reason] [duration]",
				Enabled: true,
			},
		},
		permissions: map[string][]string{
			"player":     {"/help", "/whisper"},
			"moderator":  {"/help", "/whisper", "/kick", "/mute"},
			"admin":      {"/help", "/whisper", "/kick", "/ban", "/mute"},
		},
	}

	// Initialize handler
	handler := NewChatCommandsHandler(logger, commandProcessor)

	// Create server
	srv, err := api.NewServer(handler)
	if err != nil {
		logger.Fatal("Failed to create server", zap.Error(err))
	}

	// Setup HTTP server
	httpSrv := &http.Server{
		Addr:    ":8081",
		Handler: srv,
	}

	// Graceful shutdown
	go func() {
		c := make(chan os.Signal, 1)
		signal.Notify(c, os.Interrupt, syscall.SIGTERM)
		<-c
		logger.Info("Shutting down Chat Commands Service...")

		ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
		defer cancel()

		if err := httpSrv.Shutdown(ctx); err != nil {
			logger.Error("Server shutdown failed", zap.Error(err))
		}
	}()

	logger.Info("Starting Chat Commands Service on :8081")
	if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		logger.Fatal("Server failed to start", zap.Error(err))
	}
}
