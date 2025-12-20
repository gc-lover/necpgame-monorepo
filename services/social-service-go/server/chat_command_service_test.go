// Issue: #1490
package server

import (
	"context"
	"testing"

	"github.com/necpgame/social-service-go/models"
	"github.com/stretchr/testify/assert"
)

func TestChatCommandService_ExecuteCommand_Help(t *testing.T) {
	service := NewChatCommandService(GetLogger())
	ctx := context.Background()

	req := &models.ExecuteCommandRequest{
		Command: "/help",
	}

	response, err := service.ExecuteCommand(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "/help", response.Command)
	assert.NotNil(t, response.Result)
	assert.Nil(t, response.Error)
}

func TestChatCommandService_ExecuteCommand_Time(t *testing.T) {
	service := NewChatCommandService(GetLogger())
	ctx := context.Background()

	req := &models.ExecuteCommandRequest{
		Command: "/time",
	}

	response, err := service.ExecuteCommand(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "/time", response.Command)
	assert.NotNil(t, response.Result)
	assert.Nil(t, response.Error)
}

func TestChatCommandService_ExecuteCommand_Ping(t *testing.T) {
	service := NewChatCommandService(GetLogger())
	ctx := context.Background()

	req := &models.ExecuteCommandRequest{
		Command: "/ping",
	}

	response, err := service.ExecuteCommand(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.True(t, response.Success)
	assert.Equal(t, "/ping", response.Command)
	assert.NotNil(t, response.Result)
	assert.Equal(t, "pong", *response.Result)
	assert.Nil(t, response.Error)
}

func TestChatCommandService_ExecuteCommand_Unknown(t *testing.T) {
	service := NewChatCommandService(GetLogger())
	ctx := context.Background()

	req := &models.ExecuteCommandRequest{
		Command: "/unknown",
	}

	response, err := service.ExecuteCommand(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.False(t, response.Success)
	assert.Equal(t, "/unknown", response.Command)
	assert.Nil(t, response.Result)
	assert.NotNil(t, response.Error)
}

func TestChatCommandService_ExecuteCommand_Empty(t *testing.T) {
	service := NewChatCommandService(GetLogger())
	ctx := context.Background()

	req := &models.ExecuteCommandRequest{
		Command: "",
	}

	response, err := service.ExecuteCommand(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.False(t, response.Success)
	assert.Equal(t, "", response.Command)
	assert.Nil(t, response.Result)
	assert.NotNil(t, response.Error)
}
