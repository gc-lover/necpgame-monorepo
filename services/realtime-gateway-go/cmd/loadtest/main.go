package main

import (
	"context"
	"encoding/binary"
	"flag"
	"fmt"
	"log"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gorilla/websocket"
)

type ClientMetrics struct {
	PlayerID           string
	Connections        int64
	ConnectionsFailed  int64
	PlayerInputSent    int64
	GameStateReceived  int64
	TotalLatency       int64
	TotalLatencyCount  int64
	TotalBytesSent     int64
	TotalBytesReceived int64
	Errors             int64
	LastGameStateTick  int64
}

type LoadTestConfig struct {
	ServerURL      string
	NumClients     int
	Duration       time.Duration
	PlayerInputHz  int
	ReportInterval time.Duration
}

func main() {
	var (
		serverURL      = flag.String("url", "ws://127.0.0.1:18080/ws?token=test", "WebSocket server URL")
		numClients     = flag.Int("clients", 10, "Number of concurrent clients")
		duration       = flag.Duration("duration", 60*time.Second, "Test duration")
		playerInputHz  = flag.Int("hz", 60, "PlayerInput frequency (Hz)")
		reportInterval = flag.Duration("report", 10*time.Second, "Report interval")
	)
	flag.Parse()

	config := LoadTestConfig{
		ServerURL:      *serverURL,
		NumClients:     *numClients,
		Duration:       *duration,
		PlayerInputHz:  *playerInputHz,
		ReportInterval: *reportInterval,
	}

	fmt.Printf("=== WebSocket Load Test ===\n")
	fmt.Printf("Server URL: %s\n", config.ServerURL)
	fmt.Printf("Clients: %d\n", config.NumClients)
	fmt.Printf("Duration: %v\n", config.Duration)
	fmt.Printf("PlayerInput Hz: %d\n", config.PlayerInputHz)
	fmt.Printf("Report Interval: %v\n", config.ReportInterval)
	fmt.Printf("\n")

	ctx, cancel := context.WithTimeout(context.Background(), config.Duration)
	defer cancel()

	var totalMetrics ClientMetrics
	totalMetrics.PlayerID = "total"

	var clientsWg sync.WaitGroup
	clientMetrics := make([]*ClientMetrics, config.NumClients)

	startTime := time.Now()

	for i := 0; i < config.NumClients; i++ {
		clientMetrics[i] = &ClientMetrics{
			PlayerID: fmt.Sprintf("p%08x", i),
		}
		clientsWg.Add(1)
		go runClient(ctx, config, clientMetrics[i], &clientsWg)
	}

	var reportWg sync.WaitGroup
	reportWg.Add(1)
	go func() {
		defer reportWg.Done()
		ticker := time.NewTicker(config.ReportInterval)
		defer ticker.Stop()

		for {
			select {
			case <-ctx.Done():
				return
			case <-ticker.C:
				printReport(clientMetrics, time.Since(startTime))
			}
		}
	}()

	clientsWg.Wait()
	cancel()
	reportWg.Wait()

	finalReport(clientMetrics, time.Since(startTime))
}

func runClient(ctx context.Context, config LoadTestConfig, metrics *ClientMetrics, wg *sync.WaitGroup) {
	defer wg.Done()

	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
	}

	conn, _, err := dialer.DialContext(ctx, config.ServerURL, nil)
	if err != nil {
		atomic.AddInt64(&metrics.ConnectionsFailed, 1)
		log.Printf("[%s] Failed to connect: %v", metrics.PlayerID, err)
		return
	}
	defer conn.Close()

	atomic.AddInt64(&metrics.Connections, 1)

	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	errChan := make(chan error, 1)

	var readWg sync.WaitGroup
	readWg.Add(1)
	go func() {
		defer readWg.Done()
		for {
			select {
			case <-ctx.Done():
				return
			default:
				conn.SetReadDeadline(time.Now().Add(5 * time.Second))
				messageType, data, err := conn.ReadMessage()
				if err != nil {
					if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
						atomic.AddInt64(&metrics.Errors, 1)
						errChan <- err
					}
					return
				}

				if messageType == websocket.BinaryMessage {
					atomic.AddInt64(&metrics.TotalBytesReceived, int64(len(data)))
					gameStateTick := parseGameStateTick(data)
					if gameStateTick > 0 {
						atomic.AddInt64(&metrics.GameStateReceived, 1)
						oldTick := atomic.LoadInt64(&metrics.LastGameStateTick)
						if gameStateTick > oldTick {
							atomic.StoreInt64(&metrics.LastGameStateTick, gameStateTick)
						}
					}
				}
			}
		}
	}()

	tickInterval := time.Duration(1000/config.PlayerInputHz) * time.Millisecond
	ticker := time.NewTicker(tickInterval)
	defer ticker.Stop()

	tick := int64(1)

	for {
		select {
		case <-ctx.Done():
			cancel()
			readWg.Wait()
			return
		case err := <-errChan:
			log.Printf("[%s] Connection error: %v", metrics.PlayerID, err)
			cancel()
			readWg.Wait()
			return
		case <-ticker.C:
			playerInput := buildPlayerInput(metrics.PlayerID, tick, 1.0, 0.0, false, 0.0, 0.0)

			conn.SetWriteDeadline(time.Now().Add(5 * time.Second))
			err := conn.WriteMessage(websocket.BinaryMessage, playerInput)
			if err != nil {
				atomic.AddInt64(&metrics.Errors, 1)
				log.Printf("[%s] Failed to send PlayerInput: %v", metrics.PlayerID, err)
				continue
			}

			atomic.AddInt64(&metrics.PlayerInputSent, 1)
			atomic.AddInt64(&metrics.TotalBytesSent, int64(len(playerInput)))
			tick++
		}
	}
}

func printReport(metrics []*ClientMetrics, elapsed time.Duration) {
	var total ClientMetrics

	for _, m := range metrics {
		total.Connections += atomic.LoadInt64(&m.Connections)
		total.ConnectionsFailed += atomic.LoadInt64(&m.ConnectionsFailed)
		total.PlayerInputSent += atomic.LoadInt64(&m.PlayerInputSent)
		total.GameStateReceived += atomic.LoadInt64(&m.GameStateReceived)
		total.TotalBytesSent += atomic.LoadInt64(&m.TotalBytesSent)
		total.TotalBytesReceived += atomic.LoadInt64(&m.TotalBytesReceived)
		total.Errors += atomic.LoadInt64(&m.Errors)
	}

	fmt.Printf("\n=== Report (elapsed: %v) ===\n", elapsed.Round(time.Second))
	fmt.Printf("Active Connections: %d\n", total.Connections)
	fmt.Printf("Failed Connections: %d\n", total.ConnectionsFailed)
	fmt.Printf("PlayerInput Sent: %d (%.1f msg/s)\n", total.PlayerInputSent, float64(total.PlayerInputSent)/elapsed.Seconds())
	fmt.Printf("GameState Received: %d (%.1f msg/s)\n", total.GameStateReceived, float64(total.GameStateReceived)/elapsed.Seconds())
	fmt.Printf("Bytes Sent: %d (%.2f KB/s)\n", total.TotalBytesSent, float64(total.TotalBytesSent)/(elapsed.Seconds()*1024))
	fmt.Printf("Bytes Received: %d (%.2f KB/s)\n", total.TotalBytesReceived, float64(total.TotalBytesReceived)/(elapsed.Seconds()*1024))
	fmt.Printf("Errors: %d\n", total.Errors)

	if total.PlayerInputSent > 0 {
		avgBytesPerInput := float64(total.TotalBytesSent) / float64(total.PlayerInputSent)
		fmt.Printf("Avg PlayerInput Size: %.1f bytes\n", avgBytesPerInput)
	}

	if total.GameStateReceived > 0 {
		avgBytesPerState := float64(total.TotalBytesReceived) / float64(total.GameStateReceived)
		fmt.Printf("Avg GameState Size: %.1f bytes\n", avgBytesPerState)
	}
}

func finalReport(metrics []*ClientMetrics, elapsed time.Duration) {
	var total ClientMetrics

	for _, m := range metrics {
		total.Connections += atomic.LoadInt64(&m.Connections)
		total.ConnectionsFailed += atomic.LoadInt64(&m.ConnectionsFailed)
		total.PlayerInputSent += atomic.LoadInt64(&m.PlayerInputSent)
		total.GameStateReceived += atomic.LoadInt64(&m.GameStateReceived)
		total.TotalBytesSent += atomic.LoadInt64(&m.TotalBytesSent)
		total.TotalBytesReceived += atomic.LoadInt64(&m.TotalBytesReceived)
		total.Errors += atomic.LoadInt64(&m.Errors)
	}

	fmt.Printf("\n=== Final Report ===\n")
	fmt.Printf("Test Duration: %v\n", elapsed)
	fmt.Printf("Total Connections: %d\n", total.Connections)
	fmt.Printf("Failed Connections: %d\n", total.ConnectionsFailed)
	fmt.Printf("Total PlayerInput Sent: %d\n", total.PlayerInputSent)
	fmt.Printf("Total GameState Received: %d\n", total.GameStateReceived)
	fmt.Printf("Total Bytes Sent: %d (%.2f KB)\n", total.TotalBytesSent, float64(total.TotalBytesSent)/1024)
	fmt.Printf("Total Bytes Received: %d (%.2f KB)\n", total.TotalBytesReceived, float64(total.TotalBytesReceived)/1024)
	fmt.Printf("Total Errors: %d\n", total.Errors)
	fmt.Printf("\n")
	fmt.Printf("Throughput:\n")
	fmt.Printf("  PlayerInput: %.2f msg/s\n", float64(total.PlayerInputSent)/elapsed.Seconds())
	fmt.Printf("  GameState: %.2f msg/s\n", float64(total.GameStateReceived)/elapsed.Seconds())
	fmt.Printf("  Bytes Sent: %.2f KB/s\n", float64(total.TotalBytesSent)/(elapsed.Seconds()*1024))
	fmt.Printf("  Bytes Received: %.2f KB/s\n", float64(total.TotalBytesReceived)/(elapsed.Seconds()*1024))
	fmt.Printf("\n")

	if total.PlayerInputSent > 0 {
		fmt.Printf("Average Message Sizes:\n")
		fmt.Printf("  PlayerInput: %.1f bytes\n", float64(total.TotalBytesSent)/float64(total.PlayerInputSent))
	}

	if total.GameStateReceived > 0 {
		fmt.Printf("  GameState: %.1f bytes\n", float64(total.TotalBytesReceived)/float64(total.GameStateReceived))
	}

	fmt.Printf("\n")
	fmt.Printf("Per-Client Stats:\n")
	for i, m := range metrics {
		if atomic.LoadInt64(&m.Connections) > 0 {
			inputSent := atomic.LoadInt64(&m.PlayerInputSent)
			stateReceived := atomic.LoadInt64(&m.GameStateReceived)
			errors := atomic.LoadInt64(&m.Errors)
			fmt.Printf("  Client %d (%s): Input=%d, State=%d, Errors=%d\n",
				i, m.PlayerID, inputSent, stateReceived, errors)
		}
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

