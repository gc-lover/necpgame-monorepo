package server

import (
	"encoding/binary"
	"fmt"
	"math"
)

type PlayerInputData struct {
	PlayerID string
	Tick     int64
	MoveX    float32
	MoveY    float32
	Shoot    bool
	AimX     float32
	AimY     float32
}

type EntityState struct {
	ID string
	X  float32
	Y  float32
	VX float32
	VY float32
}

type GameStateData struct {
	Tick     int64
	Entities []EntityState
}

func readVarInt(data []byte, offset *int) (uint64, error) {
	var result uint64
	var shift uint
	for *offset < len(data) {
		b := data[*offset]
		*offset++
		result |= uint64(b&0x7F) << shift
		if (b & 0x80) == 0 {
			return result, nil
		}
		shift += 7
		if shift >= 64 {
			return 0, fmt.Errorf("varint too long")
		}
	}
	return 0, fmt.Errorf("unexpected end of data")
}

func readString(data []byte, offset *int) (string, error) {
	length, err := readVarInt(data, offset)
	if err != nil {
		return "", err
	}
	if *offset+int(length) > len(data) {
		return "", fmt.Errorf("string length exceeds data")
	}
	result := string(data[*offset : *offset+int(length)])
	*offset += int(length)
	return result, nil
}

func readFloat32(data []byte, offset *int) (float32, error) {
	if *offset+4 > len(data) {
		return 0, fmt.Errorf("not enough data for float32")
	}
	bits := binary.LittleEndian.Uint32(data[*offset:])
	*offset += 4
	return math.Float32frombits(bits), nil
}

func readInt64(data []byte, offset *int) (int64, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return 0, err
	}
	return int64(val), nil
}

func readBool(data []byte, offset *int) (bool, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return false, err
	}
	return val != 0, nil
}

func ParseClientMessage(data []byte) (*PlayerInputData, error) {
	offset := 0
	var token string
	var playerInput *PlayerInputData

	for offset < len(data) {
		tag, err := readVarInt(data, &offset)
		if err != nil {
			return nil, err
		}

		fieldNum := tag >> 3
		wireType := tag & 0x7

		switch fieldNum {
		case 0:
			if wireType == 0 {
				_, err = readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
			} else if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				offset += int(length)
			}
		case 1:
			if wireType == 2 {
				token, err = readString(data, &offset)
				if err != nil {
					return nil, err
				}
			}
		case 3:
			if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				offset += int(length)
			}
		case 10:
			if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				offset += int(length)
			}
		case 11:
			if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				offset += int(length)
			}
		case 12:
			if wireType == 2 {
				length, err := readVarInt(data, &offset)
				if err != nil {
					return nil, err
				}
				if offset+int(length) > len(data) {
					return nil, fmt.Errorf("PlayerInput field length exceeds data: offset=%d, length=%d, data_len=%d", offset, length, len(data))
				}
				
				inputData := data[offset : offset+int(length)]
				offset += int(length)

				playerInput = &PlayerInputData{}
				inputOffset := 0

				for inputOffset < len(inputData) {
					oldOffset := inputOffset
					inputTag, err := readVarInt(inputData, &inputOffset)
					if err != nil {
						return nil, fmt.Errorf("failed to read input tag at offset %d: %w", oldOffset, err)
					}

					inputFieldNum := inputTag >> 3
					inputWireType := inputTag & 0x7

					switch inputFieldNum {
					case 1:
						if inputWireType == 2 {
							if inputOffset >= len(inputData) {
								return nil, fmt.Errorf("inputOffset %d >= inputData length %d for PlayerID", inputOffset, len(inputData))
							}
							strLenStart := inputOffset
							hexLen := 20
							if len(inputData) < strLenStart+hexLen {
								hexLen = len(inputData) - strLenStart
							}
							logger := GetLogger()
							logger.WithFields(map[string]interface{}{
								"str_len_start":  strLenStart,
								"input_data_len": len(inputData),
								"hex_preview":    fmt.Sprintf("%x", inputData[strLenStart:strLenStart+hexLen]),
							}).Debug("Reading PlayerID length varint")
							
							strLen, err := readVarInt(inputData, &inputOffset)
							if err != nil {
								return nil, fmt.Errorf("failed to read PlayerID length at offset %d: %w", strLenStart, err)
							}
							
							logger.WithFields(map[string]interface{}{
								"str_len":        strLen,
								"str_len_start":  strLenStart,
								"input_offset":   inputOffset,
								"input_data_len": len(inputData),
							}).Debug("Read PlayerID length varint")
							
							if strLen > 20 {
								logger.WithFields(map[string]interface{}{
									"str_len":        strLen,
									"str_len_start":  strLenStart,
									"input_offset":   inputOffset,
									"input_data_len": len(inputData),
									"hex_preview":    fmt.Sprintf("%x", inputData[strLenStart:strLenStart+20]),
									"full_hex":       fmt.Sprintf("%x", inputData),
								}).Error("PlayerID string length too large - TRACING SOURCE")
								return nil, fmt.Errorf("PlayerID string length %d too large at offset %d", strLen, strLenStart)
							}
							if inputOffset+int(strLen) > len(inputData) {
								return nil, fmt.Errorf("PlayerID string length %d exceeds inputData at offset %d (inputData len=%d, inputOffset=%d)", strLen, inputOffset, len(inputData), inputOffset)
							}
							playerInput.PlayerID = string(inputData[inputOffset : inputOffset+int(strLen)])
							logger.WithFields(map[string]interface{}{
								"player_id":      playerInput.PlayerID,
								"player_id_len":  len(playerInput.PlayerID),
								"str_len":        strLen,
								"input_offset":   inputOffset,
								"input_data_len": len(inputData),
							}).Info("Parsed PlayerID from message")
							if len(playerInput.PlayerID) > 20 {
								return nil, fmt.Errorf("PlayerID parsed but too long: %d bytes (strLen=%d, inputData len=%d, inputOffset before=%d)", len(playerInput.PlayerID), strLen, len(inputData), strLenStart)
							}
							inputOffset += int(strLen)
						} else {
							return nil, fmt.Errorf("PlayerID field 1 has wrong wire type: expected 2 (string), got %d", inputWireType)
						}
					case 2:
						if inputWireType == 0 {
							playerInput.Tick, err = readInt64(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 3:
						if inputWireType == 5 {
							playerInput.MoveX, err = readFloat32(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 4:
						if inputWireType == 5 {
							playerInput.MoveY, err = readFloat32(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 5:
						if inputWireType == 0 {
							playerInput.Shoot, err = readBool(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 6:
						if inputWireType == 5 {
							playerInput.AimX, err = readFloat32(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 7:
						if inputWireType == 5 {
							playerInput.AimY, err = readFloat32(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					default:
						if inputWireType == 0 {
							_, err = readVarInt(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						} else if inputWireType == 1 {
							inputOffset += 8
						} else if inputWireType == 2 {
							length, err := readVarInt(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
							inputOffset += int(length)
						} else if inputWireType == 5 {
							inputOffset += 4
						}
					}
				}
				
				if playerInput != nil {
					logger := GetLogger()
					if playerInput.PlayerID == "" {
						logger.Warn("PlayerInput parsed but PlayerID is empty, using default 'player1'")
						playerInput.PlayerID = "player1"
					}
					if len(playerInput.PlayerID) > 20 {
						return nil, fmt.Errorf("PlayerInput parsed but PlayerID too long: %d bytes", len(playerInput.PlayerID))
					}
					logger.WithFields(map[string]interface{}{
						"player_id":     playerInput.PlayerID,
						"player_id_len": len(playerInput.PlayerID),
						"tick":          playerInput.Tick,
					}).Info("Successfully parsed PlayerInput with PlayerID")
					return playerInput, nil
				}
			}
		default:
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

	if playerInput == nil {
		return nil, fmt.Errorf("no PlayerInput found in message")
	}

	_ = token
	return playerInput, nil
}

func writeVarInt(buf []byte, v uint64) []byte {
	for v >= 0x80 {
		buf = append(buf, byte(v)|0x80)
		v >>= 7
	}
	buf = append(buf, byte(v))
	return buf
}

func writeString(buf []byte, s string) []byte {
	buf = writeVarInt(buf, uint64(len(s)))
	buf = append(buf, []byte(s)...)
	return buf
}

func writeFloat32(buf []byte, f float32) []byte {
	bits := math.Float32bits(f)
	bytes := make([]byte, 4)
	binary.LittleEndian.PutUint32(bytes, bits)
	return append(buf, bytes...)
}

func writeInt64(buf []byte, v int64) []byte {
	return writeVarInt(buf, uint64(v))
}

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
			entityBytes = writeVarInt(entityBytes, (2<<3)|5)
			entityBytes = writeFloat32(entityBytes, entity.X)
		}

		if entity.Y != 0 {
			entityBytes = writeVarInt(entityBytes, (3<<3)|5)
			entityBytes = writeFloat32(entityBytes, entity.Y)
		}

		if entity.VX != 0 {
			entityBytes = writeVarInt(entityBytes, (4<<3)|5)
			entityBytes = writeFloat32(entityBytes, entity.VX)
		}

		if entity.VY != 0 {
			entityBytes = writeVarInt(entityBytes, (5<<3)|5)
			entityBytes = writeFloat32(entityBytes, entity.VY)
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
