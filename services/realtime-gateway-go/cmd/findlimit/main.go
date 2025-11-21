package main

import (
	"context"
	"encoding/binary"
	"flag"
	"fmt"
	"math"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gorilla/websocket"
)

type ClientMetrics struct {
	PlayerID          string
	Connections       int64
	ConnectionsFailed int64
	PlayerInputSent   int64
	GameStateReceived int64
	TotalLatency      int64
	TotalLatencyCount int64
	TotalBytesSent    int64
	TotalBytesReceived int64
	Errors            int64
	LastGameStateTick int64
}

type TestResult struct {
	NumClients        int
	Duration          time.Duration
	Success           bool
	Connections       int64
	ConnectionsFailed int64
	PlayerInputSent   int64
	PlayerInputRate   float64
	GameStateReceived int64
	TotalErrors       int64
	ErrorRate         float64
	BytesSent         int64
	BytesReceived     int64
	AvgLatency        float64
	MaxLatency        float64
	MinLatency        float64
}

type FindLimitConfig struct {
	ServerURL      string
	StartClients   int
	MaxClients     int
	StepSize       int
	TestDuration   time.Duration
	PlayerInputHz  int
	ErrorThreshold float64 // Процент ошибок, при котором считаем тест провальным
	CooldownTime   time.Duration
}

func main() {
	var (
		serverURL      = flag.String("url", "ws://127.0.0.1:18080/ws?token=test", "WebSocket server URL")
		startClients   = flag.Int("start", 10, "Starting number of clients")
		maxClients     = flag.Int("max", 500, "Maximum number of clients to test")
		stepSize       = flag.Int("step", 20, "Number of clients to add per iteration")
		testDuration   = flag.Duration("duration", 20*time.Second, "Duration per test iteration")
		playerInputHz  = flag.Int("hz", 60, "PlayerInput frequency (Hz)")
		errorThreshold = flag.Float64("error-threshold", 1.0, "Error rate threshold (percent) to consider test failed")
		cooldownTime   = flag.Duration("cooldown", 5*time.Second, "Cooldown time between tests")
	)
	flag.Parse()

	config := FindLimitConfig{
		ServerURL:      *serverURL,
		StartClients:   *startClients,
		MaxClients:     *maxClients,
		StepSize:       *stepSize,
		TestDuration:   *testDuration,
		PlayerInputHz:  *playerInputHz,
		ErrorThreshold: *errorThreshold,
		CooldownTime:   *cooldownTime,
	}

	fmt.Printf("=== Gateway Limit Finder ===\n")
	fmt.Printf("Server URL: %s\n", config.ServerURL)
	fmt.Printf("Starting clients: %d\n", config.StartClients)
	fmt.Printf("Maximum clients: %d\n", config.MaxClients)
	fmt.Printf("Step size: %d\n", config.StepSize)
	fmt.Printf("Test duration per iteration: %v\n", config.TestDuration)
	fmt.Printf("PlayerInput Hz: %d\n", config.PlayerInputHz)
	fmt.Printf("Error threshold: %.2f%%\n", config.ErrorThreshold)
	fmt.Printf("Cooldown between tests: %v\n", config.CooldownTime)
	fmt.Printf("\n")
	fmt.Printf("Starting limit search...\n\n")

	results := []TestResult{}
	maxLimit := 0
	maxSuccessfulClients := 0

	for numClients := config.StartClients; numClients <= config.MaxClients; numClients += config.StepSize {
		fmt.Printf("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
		fmt.Printf("Testing with %d clients...\n", numClients)

		result := runTest(config, numClients)

		// Ожидаемая пропускная способность (клиенты * частота)
		expectedRate := float64(numClients) * float64(config.PlayerInputHz)
		
		// Проверяем, что реальная пропускная способность составляет минимум 95% от ожидаемой
		throughputRatio := float64(0)
		if expectedRate > 0 {
			throughputRatio = result.PlayerInputRate / expectedRate * 100.0
		}
		
		// Проверяем, прошел ли тест
		// Тест считается успешным, если:
		// 1. Процент ошибок меньше порога
		// 2. Нет неудачных подключений
		// 3. Пропускная способность составляет минимум 95% от ожидаемой
		success := result.ErrorRate < config.ErrorThreshold && 
		           result.ConnectionsFailed == 0 &&
		           throughputRatio >= 95.0

		if success {
			result.Success = true
			maxSuccessfulClients = numClients
			fmt.Printf("✅ PASSED: %d clients - Error rate: %.2f%%, Throughput: %.2f msg/s (%.1f%% of expected %.0f msg/s)\n",
				numClients, result.ErrorRate, result.PlayerInputRate, throughputRatio, expectedRate)
		} else {
			result.Success = false
			failureReason := ""
			if result.ErrorRate >= config.ErrorThreshold {
				failureReason += fmt.Sprintf("Error rate too high (%.2f%%), ", result.ErrorRate)
			}
			if result.ConnectionsFailed > 0 {
				failureReason += fmt.Sprintf("Failed connections: %d, ", result.ConnectionsFailed)
			}
			if throughputRatio < 95.0 {
				failureReason += fmt.Sprintf("Throughput too low (%.1f%% of expected %.0f msg/s), ", throughputRatio, expectedRate)
			}
			fmt.Printf("❌ FAILED: %d clients - %sThroughput: %.2f msg/s\n",
				numClients, failureReason, result.PlayerInputRate)

			// Если процент ошибок слишком высок или пропускная способность слишком низкая, останавливаемся
			if result.ErrorRate >= config.ErrorThreshold*2 || throughputRatio < 80.0 {
				if result.ErrorRate >= config.ErrorThreshold*2 {
					fmt.Printf("⚠️  Error rate too high (%.2f%%), stopping limit search.\n", result.ErrorRate)
				} else {
					fmt.Printf("⚠️  Throughput too low (%.1f%% of expected), stopping limit search.\n", throughputRatio)
				}
				break
			}
		}

		results = append(results, result)
		maxLimit = numClients

		// Выводим детальную статистику
		printDetailedResult(result)

		// Если это не последний тест, делаем паузу
		if numClients < config.MaxClients {
			fmt.Printf("\nCooldown: waiting %v before next test...\n\n", config.CooldownTime)
			time.Sleep(config.CooldownTime)
		}
	}

	fmt.Printf("\n")
	fmt.Printf("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
	fmt.Printf("=== LIMIT SEARCH RESULTS ===\n\n")

	fmt.Printf("Maximum tested: %d clients\n", maxLimit)
	fmt.Printf("Maximum successful: %d clients\n", maxSuccessfulClients)

	if maxSuccessfulClients > 0 {
		fmt.Printf("\n✅ RECOMMENDED LIMIT: %d clients\n", maxSuccessfulClients)
		fmt.Printf("   (with error threshold: %.2f%%)\n", config.ErrorThreshold)
	} else {
		fmt.Printf("\n❌ No successful tests found. Gateway may have issues at %d+ clients.\n", config.StartClients)
	}

	fmt.Printf("\n=== Detailed Results ===\n\n")
	printSummaryTable(results)
}

func runTest(config FindLimitConfig, numClients int) TestResult {
	ctx, cancel := context.WithTimeout(context.Background(), config.TestDuration)
	defer cancel()

	clientMetrics := make([]*ClientMetrics, numClients)
	for i := 0; i < numClients; i++ {
		clientMetrics[i] = &ClientMetrics{
			PlayerID: fmt.Sprintf("p%08x", i),
		}
	}

	var clientsWg sync.WaitGroup
	startTime := time.Now()

	// Запускаем всех клиентов
	for i := 0; i < numClients; i++ {
		clientsWg.Add(1)
		go runClientTest(ctx, config, clientMetrics[i], &clientsWg)
	}

	// Ждем завершения всех клиентов или истечения времени
	done := make(chan struct{})
	go func() {
		clientsWg.Wait()
		close(done)
	}()

	select {
	case <-ctx.Done():
		// Время истекло
	case <-done:
		// Все клиенты завершились
	}

	testDuration := time.Since(startTime)

	// Собираем метрики
	var totalMetrics ClientMetrics
	var minLatency = float64(math.MaxFloat64)
	var maxLatency = float64(0)

	for _, cm := range clientMetrics {
		totalMetrics.Connections += atomic.LoadInt64(&cm.Connections)
		totalMetrics.ConnectionsFailed += atomic.LoadInt64(&cm.ConnectionsFailed)
		totalMetrics.PlayerInputSent += atomic.LoadInt64(&cm.PlayerInputSent)
		totalMetrics.GameStateReceived += atomic.LoadInt64(&cm.GameStateReceived)
		totalMetrics.TotalBytesSent += atomic.LoadInt64(&cm.TotalBytesSent)
		totalMetrics.TotalBytesReceived += atomic.LoadInt64(&cm.TotalBytesReceived)
		totalMetrics.Errors += atomic.LoadInt64(&cm.Errors)

		latencyCount := atomic.LoadInt64(&cm.TotalLatencyCount)
		if latencyCount > 0 {
			avgLat := float64(atomic.LoadInt64(&cm.TotalLatency)) / float64(latencyCount) / float64(time.Millisecond)
			if avgLat < minLatency {
				minLatency = avgLat
			}
			if avgLat > maxLatency {
				maxLatency = avgLat
			}
		}
	}

	totalRequests := totalMetrics.PlayerInputSent + totalMetrics.Errors
	errorRate := float64(0)
	if totalRequests > 0 {
		errorRate = float64(totalMetrics.Errors) / float64(totalRequests) * 100.0
	}

	avgLatency := float64(0)
	if totalMetrics.TotalLatencyCount > 0 {
		avgLatency = float64(totalMetrics.TotalLatency) / float64(totalMetrics.TotalLatencyCount) / float64(time.Millisecond)
	}

	playerInputRate := float64(totalMetrics.PlayerInputSent) / testDuration.Seconds()

	result := TestResult{
		NumClients:        numClients,
		Duration:          testDuration,
		Connections:       totalMetrics.Connections,
		ConnectionsFailed: totalMetrics.ConnectionsFailed,
		PlayerInputSent:   totalMetrics.PlayerInputSent,
		PlayerInputRate:   playerInputRate,
		GameStateReceived: totalMetrics.GameStateReceived,
		TotalErrors:       totalMetrics.Errors,
		ErrorRate:         errorRate,
		BytesSent:         totalMetrics.TotalBytesSent,
		BytesReceived:     totalMetrics.TotalBytesReceived,
		AvgLatency:        avgLatency,
		MaxLatency:        maxLatency,
		MinLatency:        minLatency,
	}

	return result
}

func runClientTest(ctx context.Context, config FindLimitConfig, metrics *ClientMetrics, wg *sync.WaitGroup) {
	defer wg.Done()

	// Подключаемся к серверу
	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
	}

	conn, _, err := dialer.DialContext(ctx, config.ServerURL, nil)
	if err != nil {
		atomic.AddInt64(&metrics.ConnectionsFailed, 1)
		return
	}
	defer conn.Close()

	atomic.AddInt64(&metrics.Connections, 1)

	// Запускаем горутину для чтения сообщений
	var readWg sync.WaitGroup
	readWg.Add(1)
	go func() {
		defer readWg.Done()
		for {
			select {
			case <-ctx.Done():
				return
			default:
				conn.SetReadDeadline(time.Now().Add(1 * time.Second))
				_, data, err := conn.ReadMessage()
				if err != nil {
					if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
						atomic.AddInt64(&metrics.Errors, 1)
					}
					return
				}
				atomic.AddInt64(&metrics.GameStateReceived, 1)
				atomic.AddInt64(&metrics.TotalBytesReceived, int64(len(data)))
			}
		}
	}()

	// Отправляем PlayerInput с заданной частотой
	ticker := time.NewTicker(time.Duration(1000/config.PlayerInputHz) * time.Millisecond)
	defer ticker.Stop()

	tick := int64(0)
	playerID := metrics.PlayerID

	for {
		select {
		case <-ctx.Done():
			readWg.Wait()
			return
		case <-ticker.C:
			tick++
			startTime := time.Now()

			// Формируем PlayerInput сообщение
			message := buildPlayerInputMessage(playerID, tick, 0.5, 0.3, false, 0.0, 0.0)

			// Отправляем сообщение
			conn.SetWriteDeadline(time.Now().Add(5 * time.Second))
			err := conn.WriteMessage(websocket.BinaryMessage, message)
			if err != nil {
				atomic.AddInt64(&metrics.Errors, 1)
				continue
			}

			latency := time.Since(startTime)
			atomic.AddInt64(&metrics.PlayerInputSent, 1)
			atomic.AddInt64(&metrics.TotalLatency, int64(latency))
			atomic.AddInt64(&metrics.TotalLatencyCount, 1)
			atomic.AddInt64(&metrics.TotalBytesSent, int64(len(message)))
		}
	}
}

func buildPlayerInputMessage(playerID string, tick int64, moveX, moveY float32, shoot bool, aimX, aimY float32) []byte {
	var buf []byte

	buf = appendVarInt(buf, 0x0A)
	buf = appendString(buf, playerID)

	buf = appendVarInt(buf, 0x10)
	buf = appendVarInt(buf, uint64(tick))

	buf = appendVarInt(buf, 0x1D)
	buf = appendFloat32(buf, moveX)

	buf = appendVarInt(buf, 0x25)
	buf = appendFloat32(buf, moveY)

	buf = appendVarInt(buf, 0x28)
	if shoot {
		buf = append(buf, 0x01)
	} else {
		buf = append(buf, 0x00)
	}

	buf = appendVarInt(buf, 0x31)
	buf = appendFloat32(buf, aimX)

	buf = appendVarInt(buf, 0x39)
	buf = appendFloat32(buf, aimY)

	var result []byte
	result = appendVarInt(result, 0x62)
	result = appendVarInt(result, uint64(len(buf)))
	result = append(result, buf...)

	return result
}

func appendVarInt(buf []byte, v uint64) []byte {
	for v >= 0x80 {
		buf = append(buf, byte(v)|0x80)
		v >>= 7
	}
	buf = append(buf, byte(v))
	return buf
}

func appendString(buf []byte, s string) []byte {
	buf = appendVarInt(buf, uint64(len(s)))
	buf = append(buf, []byte(s)...)
	return buf
}

func appendFloat32(buf []byte, f float32) []byte {
	var b [4]byte
	binary.LittleEndian.PutUint32(b[:], math.Float32bits(f))
	buf = append(buf, b[:]...)
	return buf
}

func printDetailedResult(result TestResult) {
	fmt.Printf("  Connections: %d (failed: %d)\n", result.Connections, result.ConnectionsFailed)
	fmt.Printf("  PlayerInput sent: %d (%.2f msg/s)\n", result.PlayerInputSent, result.PlayerInputRate)
	fmt.Printf("  GameState received: %d\n", result.GameStateReceived)
	fmt.Printf("  Errors: %d (%.2f%%)\n", result.TotalErrors, result.ErrorRate)
	fmt.Printf("  Bytes sent: %d (%.2f KB)\n", result.BytesSent, float64(result.BytesSent)/1024)
	fmt.Printf("  Bytes received: %d (%.2f KB)\n", result.BytesReceived, float64(result.BytesReceived)/1024)
	if result.AvgLatency > 0 {
		fmt.Printf("  Latency: avg=%.2f ms, min=%.2f ms, max=%.2f ms\n",
			result.AvgLatency, result.MinLatency, result.MaxLatency)
	}
}

func printSummaryTable(results []TestResult) {
	if len(results) == 0 {
		return
	}

	fmt.Printf("%-10s | %-10s | %-12s | %-12s | %-10s | %-12s | %-12s\n",
		"Clients", "Success", "Input (msg/s)", "Errors", "Error %", "Conn Failed", "Latency (ms)")
	fmt.Printf("%s\n", "---------------------------------------------------------------------------------------")

	for _, r := range results {
		success := "✅ PASS"
		if !r.Success {
			success = "❌ FAIL"
		}

		fmt.Printf("%-10d | %-10s | %-12.2f | %-12d | %-10.2f | %-12d | %-12.2f\n",
			r.NumClients,
			success,
			r.PlayerInputRate,
			r.TotalErrors,
			r.ErrorRate,
			r.ConnectionsFailed,
			r.AvgLatency,
		)
	}
}

