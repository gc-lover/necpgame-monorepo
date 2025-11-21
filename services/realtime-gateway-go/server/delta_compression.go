package server

import (
	"sync"
)

var gameStatePool = sync.Pool{
	New: func() interface{} {
		return &GameStateData{
			Entities: make([]EntityState, 0, 100),
		}
	},
}

func GetGameStateFromPool() *GameStateData {
	return gameStatePool.Get().(*GameStateData)
}

func PutGameStateToPool(gs *GameStateData) {
	if gs == nil {
		return
	}
	gs.Tick = 0
	gs.Entities = gs.Entities[:0]
	gameStatePool.Put(gs)
}

type DeltaState struct {
	LastTick    int64
	LastState   *GameStateData
	ChangedKeys []string
}

type ClientDeltaState struct {
	lastState *GameStateData
	mu        sync.RWMutex
}

func NewClientDeltaState() *ClientDeltaState {
	return &ClientDeltaState{
		lastState: nil,
	}
}

func (cds *ClientDeltaState) GetLastState() *GameStateData {
	cds.mu.RLock()
	defer cds.mu.RUnlock()
	return cds.lastState
}

func (cds *ClientDeltaState) SetLastState(state *GameStateData) {
	cds.mu.Lock()
	defer cds.mu.Unlock()
	cds.lastState = state
}

func CalculateDelta(oldState, newState *GameStateData) *GameStateData {
	if oldState == nil {
		return newState
	}

	if newState == nil {
		return nil
	}

	delta := GetGameStateFromPool()
	delta.Tick = newState.Tick
	delta.Entities = delta.Entities[:0]

	oldEntitiesMap := make(map[string]*EntityState)
	for i := range oldState.Entities {
		oldEntitiesMap[oldState.Entities[i].ID] = &oldState.Entities[i]
	}

	for _, newEntity := range newState.Entities {
		oldEntity, exists := oldEntitiesMap[newEntity.ID]
		if !exists {
			delta.Entities = append(delta.Entities, newEntity)
			continue
		}

		changedEntity := EntityState{ID: newEntity.ID}
		hasChanges := false

		if newEntity.X != oldEntity.X {
			changedEntity.X = newEntity.X
			hasChanges = true
		}

		if newEntity.Y != oldEntity.Y {
			changedEntity.Y = newEntity.Y
			hasChanges = true
		}

		if newEntity.Z != oldEntity.Z {
			changedEntity.Z = newEntity.Z
			hasChanges = true
		}

		if newEntity.VX != oldEntity.VX {
			changedEntity.VX = newEntity.VX
			hasChanges = true
		}

		if newEntity.VY != oldEntity.VY {
			changedEntity.VY = newEntity.VY
			hasChanges = true
		}

		if newEntity.VZ != oldEntity.VZ {
			changedEntity.VZ = newEntity.VZ
			hasChanges = true
		}

		if newEntity.Yaw != oldEntity.Yaw {
			changedEntity.Yaw = newEntity.Yaw
			hasChanges = true
		}

		if hasChanges {
			delta.Entities = append(delta.Entities, changedEntity)
		}
	}

	for oldID := range oldEntitiesMap {
		found := false
		for _, newEntity := range newState.Entities {
			if newEntity.ID == oldID {
				found = true
				break
			}
		}
		if !found {
			delta.Entities = append(delta.Entities, EntityState{ID: oldID})
		}
	}

	// Если Tick изменился, всегда возвращаем дельту (даже пустую) для синхронизации времени
	// Это важно для клиентов, чтобы они знали, что сервер обновился
	if len(delta.Entities) == 0 && delta.Tick == oldState.Tick {
		PutGameStateToPool(delta)
		return nil
	}

	// Если Tick изменился, но entities не изменились, все равно возвращаем дельту
	// Клиент должен получить обновление Tick для правильной синхронизации
	return delta
}

func CopyGameStateData(src *GameStateData) *GameStateData {
	if src == nil {
		return nil
	}

	dst := GetGameStateFromPool()
	dst.Tick = src.Tick
	dst.Entities = dst.Entities[:0]
	if cap(dst.Entities) < len(src.Entities) {
		dst.Entities = make([]EntityState, len(src.Entities))
	} else {
		dst.Entities = dst.Entities[:len(src.Entities)]
	}

	for i := range src.Entities {
		dst.Entities[i] = src.Entities[i]
	}

	return dst
}

