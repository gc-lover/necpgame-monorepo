package server

import "fmt"

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
						if inputWireType == 0 {
							playerInput.MoveX, err = readVarIntZigZag(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 4:
						if inputWireType == 0 {
							playerInput.MoveY, err = readVarIntZigZag(inputData, &inputOffset)
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
						if inputWireType == 0 {
							playerInput.AimX, err = readVarIntZigZag(inputData, &inputOffset)
							if err != nil {
								return nil, err
							}
						}
					case 7:
						if inputWireType == 0 {
							playerInput.AimY, err = readVarIntZigZag(inputData, &inputOffset)
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

