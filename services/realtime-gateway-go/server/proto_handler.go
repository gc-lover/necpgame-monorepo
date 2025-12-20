package server

import (
	"fmt"
	"math"

	pb "github.com/necpgame/realtime-gateway-go/pkg/proto"
	"google.golang.org/protobuf/proto"
)

type PlayerInputData struct {
	PlayerID string
	Tick     int64
	MoveX    int32
	MoveY    int32
	Shoot    bool
	AimX     int32
	AimY     int32
}

type EntityState struct {
	ID  string
	X   int32
	Y   int32
	Z   int32
	VX  int32
	VY  int32
	VZ  int32
	Yaw int32
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

func readInt64(data []byte, offset *int) (int64, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return 0, err
	}
	return int64(val), nil
}

func ParseClientMessage(data []byte) (*PlayerInputData, error) {
	if len(data) == 0 {
		return nil, fmt.Errorf("no PlayerInput found in message")
	}

	var msg pb.ClientMessage
	if err := proto.Unmarshal(data, &msg); err != nil {
		return nil, err
	}

	input := msg.GetPlayerInput()
	if input == nil {
		return nil, fmt.Errorf("no PlayerInput found in message")
	}

	return &PlayerInputData{
		PlayerID: input.PlayerId,
		Tick:     input.Tick,
		MoveX:    int32(math.Round(float64(input.MoveX) * QuantizationScale)),
		MoveY:    int32(math.Round(float64(input.MoveY) * QuantizationScale)),
		Shoot:    input.Shoot,
		AimX:     int32(math.Round(float64(input.AimX) * QuantizationScale)),
		AimY:     int32(math.Round(float64(input.AimY) * QuantizationScale)),
	}, nil
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

func writeInt64(buf []byte, v int64) []byte {
	return writeVarInt(buf, uint64(v))
}

const QuantizationScale = 1000.0

func QuantizeCoordinate(value float32) int32 {
	return int32(math.Round(float64(value) * QuantizationScale))
}

func DequantizeCoordinate(value int32) float32 {
	return float32(value) / QuantizationScale
}

func encodeZigZag(n int32) uint32 {
	return uint32((n << 1) ^ (n >> 31))
}

func decodeZigZag(n uint32) int32 {
	return int32((n >> 1) ^ -(n & 1))
}

func writeVarIntZigZag(buf []byte, v int32) []byte {
	return writeVarInt(buf, uint64(encodeZigZag(v)))
}

func readVarIntZigZag(data []byte, offset *int) (int32, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return 0, err
	}
	return decodeZigZag(uint32(val)), nil
}

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
							return nil, fmt.Errorf("entity field length exceeds data")
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

func BuildGameStateMessage(gameState *GameStateData) ([]byte, error) {
	if gameState == nil {
		return nil, fmt.Errorf("gameState is nil")
	}
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
