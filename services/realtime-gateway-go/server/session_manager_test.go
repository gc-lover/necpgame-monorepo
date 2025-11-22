package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

func setupTestRedis() *redis.Client {
	return redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   15,
	})
}

func cleanupTestRedis(client *redis.Client) {
	ctx := context.Background()
	keys, _ := client.Keys(ctx, "*").Result()
	if len(keys) > 0 {
		client.Del(ctx, keys...)
	}
}

func TestSessionManager_CreateSession(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()
	session, err := sm.CreateSession(ctx, "player123", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session: %v", err)
	}

	if session == nil {
		t.Fatal("Session is nil")
	}

	if session.PlayerID != "player123" {
		t.Errorf("Expected player_id 'player123', got %s", session.PlayerID)
	}

	if session.Status != SessionStatusActive {
		t.Errorf("Expected status 'active', got %s", session.Status)
	}

	if session.Token == "" {
		t.Error("Token is empty")
	}

	if session.ReconnectToken == "" {
		t.Error("ReconnectToken is empty")
	}
}

func TestSessionManager_GetSessionByToken(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()
	session, err := sm.CreateSession(ctx, "player123", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session: %v", err)
	}

	retrieved, err := sm.GetSessionByToken(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to get session: %v", err)
	}

	if retrieved == nil {
		t.Fatal("Retrieved session is nil")
	}

	if retrieved.PlayerID != session.PlayerID {
		t.Errorf("Expected player_id %s, got %s", session.PlayerID, retrieved.PlayerID)
	}

	if retrieved.Token != session.Token {
		t.Errorf("Expected token %s, got %s", session.Token, retrieved.Token)
	}
}

func TestSessionManager_UpdateHeartbeat(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()
	session, err := sm.CreateSession(ctx, "player123", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session: %v", err)
	}

	oldHeartbeat := session.LastHeartbeat
	time.Sleep(100 * time.Millisecond)

	err = sm.UpdateHeartbeat(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to update heartbeat: %v", err)
	}

	updated, err := sm.GetSessionByToken(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to get session: %v", err)
	}

	if updated.LastHeartbeat.Before(oldHeartbeat) || updated.LastHeartbeat.Equal(oldHeartbeat) {
		t.Error("Heartbeat was not updated")
	}

	if updated.Status != SessionStatusActive {
		t.Errorf("Expected status 'active', got %s", updated.Status)
	}
}

func TestSessionManager_ReconnectSession(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()
	session, err := sm.CreateSession(ctx, "player123", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session: %v", err)
	}

	err = sm.DisconnectSession(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to disconnect session: %v", err)
	}

	reconnected, err := sm.ReconnectSession(ctx, session.ReconnectToken, "192.168.1.1", "new-agent")
	if err != nil {
		t.Fatalf("Failed to reconnect session: %v", err)
	}

	if reconnected == nil {
		t.Fatal("Reconnected session is nil")
	}

	if reconnected.Status != SessionStatusActive {
		t.Errorf("Expected status 'active', got %s", reconnected.Status)
	}

	if reconnected.DisconnectCount != 1 {
		t.Errorf("Expected disconnect_count 1, got %d", reconnected.DisconnectCount)
	}
}

func TestSessionManager_CloseSession(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()
	session, err := sm.CreateSession(ctx, "player123", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session: %v", err)
	}

	err = sm.CloseSession(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to close session: %v", err)
	}

	closed, err := sm.GetSessionByToken(ctx, session.Token)
	if err != nil {
		t.Fatalf("Failed to get session: %v", err)
	}

	if closed != nil {
		t.Error("Session should be deleted after close")
	}
}

func TestSessionManager_GetActiveSessionsCount(t *testing.T) {
	redisClient := setupTestRedis()
	defer cleanupTestRedis(redisClient)

	sm, err := NewSessionManager("redis://localhost:6379/15", "test-server")
	if err != nil {
		t.Fatalf("Failed to create session manager: %v", err)
	}

	ctx := context.Background()
	characterID := uuid.New()

	session1, err := sm.CreateSession(ctx, "player1", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session 1: %v", err)
	}

	session2, err := sm.CreateSession(ctx, "player2", "127.0.0.1", "test-agent", &characterID)
	if err != nil {
		t.Fatalf("Failed to create session 2: %v", err)
	}

	count, err := sm.GetActiveSessionsCount(ctx)
	if err != nil {
		t.Fatalf("Failed to get active sessions count: %v", err)
	}

	if count != 2 {
		t.Errorf("Expected 2 active sessions, got %d", count)
	}

	sm.CloseSession(ctx, session1.Token)
	count, err = sm.GetActiveSessionsCount(ctx)
	if err != nil {
		t.Fatalf("Failed to get active sessions count: %v", err)
	}

	if count != 1 {
		t.Errorf("Expected 1 active session after closing one, got %d", count)
	}

	sm.CloseSession(ctx, session2.Token)
}

