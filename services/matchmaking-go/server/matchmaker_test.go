package server

import (
	"context"
	"encoding/json"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestMatchmaker(t *testing.T) (*Matchmaker, *redis.Client, func()) {
	config := NewMatchmakerConfig("redis://localhost:6379", "pve8", 8)
	matchmaker := NewMatchmaker(config)
	
	testClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})

	ctx := context.Background()
	testClient.FlushDB(ctx)

	cleanup := func() {
		testClient.FlushDB(ctx)
		testClient.Close()
		matchmaker.Close()
	}

	matchmaker.client = testClient

	return matchmaker, testClient, cleanup
}

func TestNewMatchmaker(t *testing.T) {
	config := NewMatchmakerConfig("redis://localhost:6379", "pve8", 8)
	matchmaker := NewMatchmaker(config)
	defer matchmaker.Close()

	assert.NotNil(t, matchmaker)
	assert.NotNil(t, matchmaker.client)
	assert.Equal(t, config, matchmaker.config)
}

func TestPopTickets_EmptyQueue(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")

	tickets := matchmaker.popTickets(ctx, 8)
	assert.Empty(t, tickets)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)
}

func TestPopTickets_SingleTicket(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	ticketID := uuid.New().String()

	client.RPush(ctx, queueKey, ticketID)

	tickets := matchmaker.popTickets(ctx, 8)
	assert.Len(t, tickets, 1)
	assert.Equal(t, ticketID, tickets[0])

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)
}

func TestPopTickets_MultipleTickets(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	
	var expectedTickets []string
	for i := 0; i < 5; i++ {
		ticketID := uuid.New().String()
		expectedTickets = append(expectedTickets, ticketID)
		client.RPush(ctx, queueKey, ticketID)
	}

	tickets := matchmaker.popTickets(ctx, 8)
	assert.Len(t, tickets, 5)
	assert.Equal(t, expectedTickets[4], tickets[0])

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)
}

func TestPopTickets_PartialCount(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	
	for i := 0; i < 3; i++ {
		client.RPush(ctx, queueKey, uuid.New().String())
	}

	tickets := matchmaker.popTickets(ctx, 8)
	assert.Len(t, tickets, 3)
}

func TestAllocate_MatchCreated(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	allocationsKey := redisKeys.allocations()
	
	tickets := []string{
		uuid.New().String(),
		uuid.New().String(),
		uuid.New().String(),
	}

	matchmaker.allocate(ctx, tickets)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	require.NoError(t, err)
	assert.Greater(t, streamInfo.Length, int64(0))

	messages, err := client.XRead(ctx, &redis.XReadArgs{
		Streams: []string{allocationsKey, "0"},
		Count:   1,
		Block:   time.Second,
	}).Result()
	require.NoError(t, err)
	require.Len(t, messages, 1)
	require.Len(t, messages[0].Messages, 1)

	message := messages[0].Messages[0]
	dataStr, ok := message.Values["data"].(string)
	require.True(t, ok)

	var payload map[string]interface{}
	err = json.Unmarshal([]byte(dataStr), &payload)
	require.NoError(t, err)

	assert.Equal(t, "pve8", payload["mode"])
	assert.NotEmpty(t, payload["instance"])

	var players []string
	err = json.Unmarshal([]byte(payload["players"].(string)), &players)
	require.NoError(t, err)
	assert.Equal(t, tickets, players)
}

func TestLoopOnce_ExactTeamSize(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	allocationsKey := redisKeys.allocations()

	for i := 0; i < 8; i++ {
		client.RPush(ctx, queueKey, uuid.New().String())
	}

	matchmaker.LoopOnce(ctx)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	require.NoError(t, err)
	assert.Equal(t, int64(1), streamInfo.Length)
}

func TestLoopOnce_PartialTeam(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	allocationsKey := redisKeys.allocations()

	for i := 0; i < 3; i++ {
		client.RPush(ctx, queueKey, uuid.New().String())
	}

	matchmaker.LoopOnce(ctx)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(3), queueSize)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	if err == nil {
		assert.Equal(t, int64(0), streamInfo.Length)
	}
}

func TestLoopOnce_EmptyQueue(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	allocationsKey := redisKeys.allocations()

	matchmaker.LoopOnce(ctx)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	if err == nil {
		assert.Equal(t, int64(0), streamInfo.Length)
	}
}

func TestLoopOnce_MoreThanTeamSize(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	allocationsKey := redisKeys.allocations()

	for i := 0; i < 12; i++ {
		client.RPush(ctx, queueKey, uuid.New().String())
	}

	matchmaker.LoopOnce(ctx)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(4), queueSize)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	require.NoError(t, err)
	assert.Equal(t, int64(1), streamInfo.Length)
}

func TestRedisKeys(t *testing.T) {
	assert.Equal(t, "mm:queue:pve8", redisKeys.queue("pve8"))
	assert.Equal(t, "mm:queue:pvp4", redisKeys.queue("pvp4"))
	
	ticketID := uuid.New().String()
	assert.Equal(t, "mm:ticket:"+ticketID, redisKeys.ticket(ticketID))
	
	assert.Equal(t, "mm:allocations", redisKeys.allocations())
}

func TestMatchmaker_Close(t *testing.T) {
	config := NewMatchmakerConfig("redis://localhost:6379", "pve8", 8)
	matchmaker := NewMatchmaker(config)

	err := matchmaker.Close()
	assert.NoError(t, err)
}

func TestAllocate_EmptyTickets(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	allocationsKey := redisKeys.allocations()

	matchmaker.allocate(ctx, []string{})

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	if err == nil {
		assert.Equal(t, int64(1), streamInfo.Length)
	}
}

func TestAllocate_SingleTicket(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	allocationsKey := redisKeys.allocations()
	ticketID := uuid.New().String()

	matchmaker.allocate(ctx, []string{ticketID})

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	require.NoError(t, err)
	assert.Equal(t, int64(1), streamInfo.Length)
}

func TestPopTickets_CountZero(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	
	client.RPush(ctx, queueKey, uuid.New().String())

	tickets := matchmaker.popTickets(ctx, 0)
	assert.Empty(t, tickets)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(1), queueSize)
}

func TestPopTickets_CountNegative(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	
	client.RPush(ctx, queueKey, uuid.New().String())

	tickets := matchmaker.popTickets(ctx, -1)
	assert.Empty(t, tickets)
}

func TestLoopOnce_MultipleIterations(t *testing.T) {
	matchmaker, client, cleanup := setupTestMatchmaker(t)
	defer cleanup()

	ctx := context.Background()
	queueKey := redisKeys.queue("pve8")
	allocationsKey := redisKeys.allocations()

	for i := 0; i < 16; i++ {
		client.RPush(ctx, queueKey, uuid.New().String())
	}

	matchmaker.LoopOnce(ctx)
	matchmaker.LoopOnce(ctx)

	queueSize, _ := client.LLen(ctx, queueKey).Result()
	assert.Equal(t, int64(0), queueSize)

	streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
	require.NoError(t, err)
	assert.Equal(t, int64(2), streamInfo.Length)
}

func TestNewMatchmakerConfig(t *testing.T) {
	config := NewMatchmakerConfig("redis://localhost:6379", "pvp4", 4)
	
	assert.Equal(t, "redis://localhost:6379", config.RedisUrl)
	assert.Equal(t, "pvp4", config.Mode)
	assert.Equal(t, 4, config.TeamSize)
}

func TestAllocate_DifferentModes(t *testing.T) {
	configs := []struct {
		mode     string
		teamSize int
	}{
		{"pve8", 8},
		{"pvp4", 4},
		{"raid12", 12},
	}

	for _, cfg := range configs {
		t.Run(cfg.mode, func(t *testing.T) {
			matchmaker, client, cleanup := setupTestMatchmaker(t)
			defer cleanup()
			matchmaker.config.Mode = cfg.mode
			matchmaker.config.TeamSize = cfg.teamSize

			ctx := context.Background()
			allocationsKey := redisKeys.allocations()
			
			tickets := make([]string, cfg.teamSize)
			for i := 0; i < cfg.teamSize; i++ {
				tickets[i] = uuid.New().String()
			}

			matchmaker.allocate(ctx, tickets)

			streamInfo, err := client.XInfoStream(ctx, allocationsKey).Result()
			require.NoError(t, err)
			assert.Greater(t, streamInfo.Length, int64(0))
		})
	}
}

