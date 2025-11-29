package server

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type ChatCommandServiceInterface interface {
	ExecuteCommand(ctx context.Context, req *models.ExecuteCommandRequest) (*models.CommandResponse, error)
}

type ChatCommandService struct {
	logger *logrus.Logger
}

func NewChatCommandService() *ChatCommandService {
	return &ChatCommandService{
		logger: GetLogger(),
	}
}

func (s *ChatCommandService) ExecuteCommand(ctx context.Context, req *models.ExecuteCommandRequest) (*models.CommandResponse, error) {
	if req.Command == "" {
		return &models.CommandResponse{
			Success: false,
			Command: req.Command,
			Error:   stringPtr("command is required"),
		}, nil
	}

	command := strings.ToLower(strings.TrimSpace(req.Command))

	switch command {
	case "/help", "/h":
		return s.handleHelpCommand(req), nil
	case "/time", "/t":
		return s.handleTimeCommand(req), nil
	case "/ping", "/p":
		return s.handlePingCommand(req), nil
	case "/whoami", "/who":
		return s.handleWhoAmICommand(ctx, req), nil
	default:
		return &models.CommandResponse{
			Success: false,
			Command: req.Command,
			Error:   stringPtr(fmt.Sprintf("unknown command: %s", req.Command)),
		}, nil
	}
}

func (s *ChatCommandService) handleHelpCommand(req *models.ExecuteCommandRequest) *models.CommandResponse {
	helpText := "Available commands:\n" +
		"/help, /h - Show this help message\n" +
		"/time, /t - Show server time\n" +
		"/ping, /p - Check connection\n" +
		"/whoami, /who - Show your character info"
	return &models.CommandResponse{
		Success: true,
		Command: req.Command,
		Result:  &helpText,
	}
}

func (s *ChatCommandService) handleTimeCommand(req *models.ExecuteCommandRequest) *models.CommandResponse {
	timeText := fmt.Sprintf("Server time: %s", time.Now().Format(time.RFC3339))
	return &models.CommandResponse{
		Success: true,
		Command: req.Command,
		Result:  &timeText,
	}
}

func (s *ChatCommandService) handlePingCommand(req *models.ExecuteCommandRequest) *models.CommandResponse {
	result := "pong"
	return &models.CommandResponse{
		Success: true,
		Command: req.Command,
		Result:  &result,
	}
}

func (s *ChatCommandService) handleWhoAmICommand(ctx context.Context, req *models.ExecuteCommandRequest) *models.CommandResponse {
	result := "Character info not available (JWT token parsing not implemented)"
	return &models.CommandResponse{
		Success: true,
		Command: req.Command,
		Result:  &result,
	}
}

func stringPtr(s string) *string {
	return &s
}

