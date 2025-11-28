package server

func BuildGameStateMessage(gameState *GameStateData) ([]byte, error) {
	result := make([]byte, 0, 200)

	gameStateBytes := make([]byte, 0, 100)

	gameStateBytes = writeVarInt(gameStateBytes, (1<<3)|0)
	gameStateBytes = writeInt64(gameStateBytes, gameState.Tick)

	for _, entity := range gameState.Entities {
		entityBytes := make([]byte, 0, 50)

		if entity.ID != "" {
			entityBytes = writeVarInt(entityBytes, (1<<3)|2)
			entityBytes = writeString(entityBytes, entity.ID)
		}

		if entity.X != 0 {
			entityBytes = writeVarInt(entityBytes, (2<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.X)
		}

		if entity.Y != 0 {
			entityBytes = writeVarInt(entityBytes, (3<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.Y)
		}

		if entity.Z != 0 {
			entityBytes = writeVarInt(entityBytes, (4<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.Z)
		}

		if entity.VX != 0 {
			entityBytes = writeVarInt(entityBytes, (5<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.VX)
		}

		if entity.VY != 0 {
			entityBytes = writeVarInt(entityBytes, (6<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.VY)
		}

		if entity.VZ != 0 {
			entityBytes = writeVarInt(entityBytes, (7<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.VZ)
		}

		if entity.Yaw != 0 {
			entityBytes = writeVarInt(entityBytes, (8<<3)|0)
			entityBytes = writeVarIntZigZag(entityBytes, entity.Yaw)
		}

		gameStateBytes = writeVarInt(gameStateBytes, (2<<3)|2)
		gameStateBytes = writeVarInt(gameStateBytes, uint64(len(entityBytes)))
		gameStateBytes = append(gameStateBytes, entityBytes...)
	}

	result = writeVarInt(result, (12<<3)|2)
	result = writeVarInt(result, uint64(len(gameStateBytes)))
	result = append(result, gameStateBytes...)

	return result, nil
}

