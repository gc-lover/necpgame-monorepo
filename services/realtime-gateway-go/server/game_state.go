package server

import (
	"sync"
	"time"
)

type PlayerState struct {
	ID      string
	X       float32
	Y       float32
	VX      float32
	VY      float32
	LastUpdate time.Time
}

type GameStateManager struct {
	mu       sync.RWMutex
	players  map[string]*PlayerState
	tick     int64
	tickRate int
}

func NewGameStateManager(tickRate int) *GameStateManager {
	return &GameStateManager{
		players:  make(map[string]*PlayerState),
		tick:     0,
		tickRate: tickRate,
	}
}

func (gsm *GameStateManager) UpdatePlayerInput(input *PlayerInputData) {
	gsm.mu.Lock()
	defer gsm.mu.Unlock()

	player, exists := gsm.players[input.PlayerID]
	if !exists {
		player = &PlayerState{
			ID:         input.PlayerID,
			X:          0,
			Y:          0,
			VX:         0,
			VY:         0,
			LastUpdate: time.Now(),
		}
		gsm.players[input.PlayerID] = player
	}

	speed := float32(5.0)
	dt := float32(1.0 / float32(gsm.tickRate))

	player.VX = input.MoveX * speed
	player.VY = input.MoveY * speed

	player.X += player.VX * dt
	player.Y += player.VY * dt
	player.LastUpdate = time.Now()
}

func (gsm *GameStateManager) GetGameState() *GameStateData {
	gsm.mu.RLock()
	defer gsm.mu.RUnlock()

	gsm.tick++

	entities := make([]EntityState, 0, len(gsm.players))
	for _, player := range gsm.players {
		if time.Since(player.LastUpdate) < 5*time.Second {
			entities = append(entities, EntityState{
				ID: player.ID,
				X:  player.X,
				Y:  player.Y,
				VX: player.VX,
				VY: player.VY,
			})
		}
	}

	return &GameStateData{
		Tick:     gsm.tick,
		Entities: entities,
	}
}

func (gsm *GameStateManager) RemovePlayer(playerID string) {
	gsm.mu.Lock()
	defer gsm.mu.Unlock()
	delete(gsm.players, playerID)
}

