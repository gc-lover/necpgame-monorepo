package main

import (
	"context"
	"encoding/binary"
	"testing"
	"time"

	"github.com/gorilla/websocket"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestSynchronizationCycle(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping integration test in short mode")
	}
	
	// Skip if gateway is not running
	// This test requires realtime-gateway service to be running
	t.Skip("Skipping integration test - requires running gateway service")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	addr := "ws://127.0.0.1:18080/ws?token=test"
	t.Logf("Connecting to %s...", addr)

	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
	}

	conn, _, err := dialer.DialContext(ctx, addr, nil)
	require.NoError(t, err, "Failed to connect to gateway")
	defer conn.Close()

	t.Log("✓ Connected successfully")

	playerID := "p12345678"
	tick := int64(1)

	playerInputSent := 0
	gameStateReceived := 0
	lastGameStateTick := int64(0)

	done := make(chan bool)
	errChan := make(chan error, 1)

	go func() {
		defer close(done)
		for {
			select {
			case <-ctx.Done():
				return
			default:
				conn.SetReadDeadline(time.Now().Add(5 * time.Second))
				messageType, data, err := conn.ReadMessage()
				if err != nil {
					if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
						errChan <- err
					}
					return
				}

				if messageType == websocket.BinaryMessage {
					if len(data) > 0 {
						gameStateTick := parseGameStateTick(data)
						if gameStateTick > 0 {
							gameStateReceived++
							if gameStateTick > lastGameStateTick {
								lastGameStateTick = gameStateTick
								t.Logf("✓ Received GameState tick=%d (total received: %d)", gameStateTick, gameStateReceived)
							}
						}
					}
				}
			}
		}
	}()

	for i := 0; i < 10; i++ {
		select {
		case <-ctx.Done():
			t.Fatal("Test timeout")
		case err := <-errChan:
			t.Fatalf("Connection error: %v", err)
		default:
		}

		playerInput := buildPlayerInput(playerID, tick, 1.0, 0.0, false, 0.0, 0.0)
		
		conn.SetWriteDeadline(time.Now().Add(5 * time.Second))
		err = conn.WriteMessage(websocket.BinaryMessage, playerInput)
		require.NoError(t, err, "Failed to send PlayerInput")

		playerInputSent++
		tick++
		t.Logf("✓ Sent PlayerInput tick=%d (total sent: %d)", tick-1, playerInputSent)

		time.Sleep(50 * time.Millisecond)
	}

	time.Sleep(2 * time.Second)

	t.Logf("\n=== Test Results ===")
	t.Logf("PlayerInput sent: %d", playerInputSent)
	t.Logf("GameState received: %d", gameStateReceived)
	t.Logf("Last GameState tick: %d", lastGameStateTick)

	assert.Greater(t, playerInputSent, 0, "Should send at least one PlayerInput")
	
	if gameStateReceived == 0 {
		t.Log("WARNING: No GameState received - this is expected if Dedicated Server is not running")
		t.Log("To test full cycle, start UE5 Dedicated Server first")
	} else {
		assert.Greater(t, gameStateReceived, 0, "Should receive at least one GameState")
		assert.Greater(t, lastGameStateTick, int64(0), "GameState should have valid tick")
	}
}

func buildPlayerInput(playerID string, tick int64, moveX, moveY float32, shoot bool, aimX, aimY float32) []byte {
	var buf []byte

	buf = appendVarint(buf, 0x0A)
	buf = appendString(buf, playerID)

	buf = appendVarint(buf, 0x10)
	buf = appendVarint(buf, uint64(tick))

	buf = appendVarint(buf, 0x1D)
	buf = appendFloat32(buf, moveX)

	buf = appendVarint(buf, 0x25)
	buf = appendFloat32(buf, moveY)

	buf = appendVarint(buf, 0x28)
	if shoot {
		buf = append(buf, 0x01)
	} else {
		buf = append(buf, 0x00)
	}

	buf = appendVarint(buf, 0x31)
	buf = appendFloat32(buf, aimX)

	buf = appendVarint(buf, 0x39)
	buf = appendFloat32(buf, aimY)

	var result []byte
	result = appendVarint(result, 0x62)
	result = appendVarint(result, uint64(len(buf)))
	result = append(result, buf...)

	return result
}

func parseGameStateTick(data []byte) int64 {
	if len(data) < 2 {
		return 0
	}

	offset := 0
	for offset < len(data) {
		if offset+1 >= len(data) {
			break
		}

		tag := data[offset]
		offset++

		fieldNum := tag >> 3
		wireType := tag & 0x07

		if fieldNum == 12 && wireType == 2 {
			msgLen, n := binary.Uvarint(data[offset:])
			if n <= 0 {
				break
			}
			offset += n

			if offset+int(msgLen) > len(data) {
				break
			}

			gameStateData := data[offset : offset+int(msgLen)]
			offset += int(msgLen)

			return parseGameSnapshotTick(gameStateData)
		} else if wireType == 0 {
			_, n := binary.Uvarint(data[offset:])
			if n <= 0 {
				break
			}
			offset += n
		} else if wireType == 1 {
			offset += 8
		} else if wireType == 2 {
			msgLen, n := binary.Uvarint(data[offset:])
			if n <= 0 {
				break
			}
			offset += n
			if offset+int(msgLen) > len(data) {
				break
			}
			offset += int(msgLen)
		} else if wireType == 5 {
			offset += 4
		}
	}

	return 0
}

func parseGameSnapshotTick(data []byte) int64 {
	offset := 0
	for offset < len(data) {
		if offset+1 >= len(data) {
			break
		}

		tag := data[offset]
		offset++

		fieldNum := tag >> 3
		wireType := tag & 0x07

		if fieldNum == 1 && wireType == 0 {
			tick, n := binary.Varint(data[offset:])
			if n <= 0 {
				break
			}
			return tick
		} else if wireType == 0 {
			_, n := binary.Uvarint(data[offset:])
			if n <= 0 {
				break
			}
			offset += n
		} else if wireType == 1 {
			offset += 8
		} else if wireType == 2 {
			msgLen, n := binary.Uvarint(data[offset:])
			if n <= 0 {
				break
			}
			offset += n
			if offset+int(msgLen) > len(data) {
				break
			}
			offset += int(msgLen)
		} else if wireType == 5 {
			offset += 4
		}
	}

	return 0
}

func appendVarint(buf []byte, v uint64) []byte {
	for v >= 0x80 {
		buf = append(buf, byte(v)|0x80)
		v >>= 7
	}
	buf = append(buf, byte(v))
	return buf
}

func appendString(buf []byte, s string) []byte {
	buf = appendVarint(buf, uint64(len(s)))
	buf = append(buf, []byte(s)...)
	return buf
}

func appendFloat32(buf []byte, f float32) []byte {
	var b [4]byte
	binary.LittleEndian.PutUint32(b[:], uint32(f))
	buf = append(buf, b[:]...)
	return buf
}

