package server

import "fmt"

func ParseGameStateMessage(data []byte) (*GameStateData, error) {
	offset := 0
	var gameState *GameStateData

	for offset < len(data) {
		tag, err := readVarInt(data, &offset)
		if err != nil {
			return nil, err
		}

		fieldNum := tag >> 3
		wireType := tag & 0x7

		if fieldNum == 12 && wireType == 2 {
			length, err := readVarInt(data, &offset)
			if err != nil {
				return nil, err
			}
			if offset+int(length) > len(data) {
				return nil, fmt.Errorf("GameState field length exceeds data")
			}

			gameStateData := data[offset : offset+int(length)]
			offset += int(length)

			gameState = &GameStateData{}
			gsOffset := 0

			for gsOffset < len(gameStateData) {
				gsTag, err := readVarInt(gameStateData, &gsOffset)
				if err != nil {
					return nil, err
				}

				gsFieldNum := gsTag >> 3
				gsWireType := gsTag & 0x7

				switch gsFieldNum {
				case 1:
					if gsWireType == 0 {
						gameState.Tick, err = readInt64(gameStateData, &gsOffset)
						if err != nil {
							return nil, err
						}
					}
				case 2:
					if gsWireType == 2 {
						entityLength, err := readVarInt(gameStateData, &gsOffset)
						if err != nil {
							return nil, err
						}
						if gsOffset+int(entityLength) > len(gameStateData) {
							return nil, fmt.Errorf("Entity field length exceeds data")
						}

						entityData := gameStateData[gsOffset : gsOffset+int(entityLength)]
						gsOffset += int(entityLength)

						entity := EntityState{}
						entityOffset := 0

						for entityOffset < len(entityData) {
							entityTag, err := readVarInt(entityData, &entityOffset)
							if err != nil {
								return nil, err
							}

							entityFieldNum := entityTag >> 3
							entityWireType := entityTag & 0x7

							switch entityFieldNum {
							case 1:
								if entityWireType == 2 {
									entity.ID, err = readString(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 2:
								if entityWireType == 0 {
									entity.X, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 3:
								if entityWireType == 0 {
									entity.Y, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 4:
								if entityWireType == 0 {
									entity.Z, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 5:
								if entityWireType == 0 {
									entity.VX, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 6:
								if entityWireType == 0 {
									entity.VY, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 7:
								if entityWireType == 0 {
									entity.VZ, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							case 8:
								if entityWireType == 0 {
									entity.Yaw, err = readVarIntZigZag(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								}
							default:
								if entityWireType == 0 {
									_, err = readVarInt(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
								} else if entityWireType == 1 {
									entityOffset += 8
								} else if entityWireType == 2 {
									length, err := readVarInt(entityData, &entityOffset)
									if err != nil {
										return nil, err
									}
									entityOffset += int(length)
								} else if entityWireType == 5 {
									entityOffset += 4
								}
							}
						}

						gameState.Entities = append(gameState.Entities, entity)
					}
				default:
					if gsWireType == 0 {
						_, err = readVarInt(gameStateData, &gsOffset)
						if err != nil {
							return nil, err
						}
					} else if gsWireType == 1 {
						gsOffset += 8
					} else if gsWireType == 2 {
						length, err := readVarInt(gameStateData, &gsOffset)
						if err != nil {
							return nil, err
						}
						gsOffset += int(length)
					} else if gsWireType == 5 {
						gsOffset += 4
					}
				}
			}
		} else {
			if wireType == 0 {
				_, err = readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
			} else if wireType == 1 {
				offset += 8
			} else if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				offset += int(length)
			} else if wireType == 5 {
				offset += 4
			}
		}
	}

	if gameState == nil {
		return nil, fmt.Errorf("no GameState found in message")
	}

	return gameState, nil
}

