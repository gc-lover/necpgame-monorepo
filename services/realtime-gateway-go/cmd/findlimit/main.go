package main

import (
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
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
	// Пороги для киберспортивных игр (CS:GO, VALORANT стандарты)
	MaxLatencyMs   float64 // Максимальная допустимая latency (мс)
	CriticalLatencyMs float64 // Критическая latency, при которой тест останавливается (мс)
}

func main() {
	var (
		serverURL        = flag.String("url", "ws://127.0.0.1:18080/ws?token=test", "WebSocket server URL")
		startClients     = flag.Int("start", 10, "Starting number of clients")
		maxClients       = flag.Int("max", 500, "Maximum number of clients to test")
		stepSize         = flag.Int("step", 20, "Number of clients to add per iteration")
		testDuration     = flag.Duration("duration", 20*time.Second, "Duration per test iteration")
		playerInputHz    = flag.Int("hz", 60, "PlayerInput frequency (Hz)")
		errorThreshold   = flag.Float64("error-threshold", 0.5, "Error rate threshold (percent) to consider test failed (competitive gaming standard: 0.5%)")
		cooldownTime     = flag.Duration("cooldown", 5*time.Second, "Cooldown time between tests")
		maxLatencyMs     = flag.Float64("max-latency", 50.0, "Maximum acceptable latency in milliseconds (competitive gaming: 50ms for good, 100ms acceptable)")
		criticalLatencyMs = flag.Float64("critical-latency", 150.0, "Critical latency threshold in milliseconds - test stops if exceeded (competitive gaming: 150ms is unplayable)")
	)
	flag.Parse()

	config := FindLimitConfig{
		ServerURL:        *serverURL,
		StartClients:     *startClients,
		MaxClients:       *maxClients,
		StepSize:         *stepSize,
		TestDuration:     *testDuration,
		PlayerInputHz:    *playerInputHz,
		ErrorThreshold:   *errorThreshold,
		CooldownTime:     *cooldownTime,
		MaxLatencyMs:     *maxLatencyMs,
		CriticalLatencyMs: *criticalLatencyMs,
	}

	fmt.Printf("=== Gateway Limit Finder ===\n")
	fmt.Printf("Server URL: %s\n", config.ServerURL)
	fmt.Printf("Starting clients: %d\n", config.StartClients)
	fmt.Printf("Maximum clients: %d\n", config.MaxClients)
	fmt.Printf("Step size: %d\n", config.StepSize)
	fmt.Printf("Test duration per iteration: %v\n", config.TestDuration)
	fmt.Printf("PlayerInput Hz: %d\n", config.PlayerInputHz)
	fmt.Printf("Error threshold: %.2f%% (competitive gaming standard: <0.5%%)\n", config.ErrorThreshold)
	fmt.Printf("Max latency: %.1f ms (competitive gaming: 50ms good, 100ms acceptable)\n", config.MaxLatencyMs)
	fmt.Printf("Critical latency: %.1f ms (test stops if exceeded)\n", config.CriticalLatencyMs)
	fmt.Printf("Cooldown between tests: %v\n", config.CooldownTime)
	fmt.Printf("\n")
	fmt.Printf("Starting limit search...\n\n")

	results := []TestResult{}
	maxLimit := 0
	maxSuccessfulClients := 0
	currentStep := config.StepSize
	previousLatency := float64(0)
	previousErrorRate := float64(0)

	for numClients := config.StartClients; numClients <= config.MaxClients; numClients += currentStep {
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
		// 1. Процент ошибок меньше порога (competitive gaming: <0.5%)
		// 2. Нет неудачных подключений
		// 3. Пропускная способность составляет минимум 95% от ожидаемой
		// 4. Latency не превышает максимально допустимую (competitive gaming: <50ms хорошая, <100ms приемлемая)
		latencyOK := result.AvgLatency == 0 || result.AvgLatency <= config.MaxLatencyMs
		success := result.ErrorRate < config.ErrorThreshold && 
		           result.ConnectionsFailed == 0 &&
		           throughputRatio >= 95.0 &&
		           latencyOK

		// Адаптивный шаг: уменьшаем шаг, если начинается рост latency или error rate
		if len(results) > 0 {
			latencyIncrease := result.AvgLatency > 0 && previousLatency > 0 && result.AvgLatency > previousLatency*1.2
			errorRateIncrease := result.ErrorRate > previousErrorRate*1.5 && previousErrorRate > 0
			latencyNearLimit := result.AvgLatency > 0 && result.AvgLatency > config.MaxLatencyMs*0.7
			
			if latencyIncrease || errorRateIncrease || latencyNearLimit {
				// Уменьшаем шаг для более точного поиска предела
				newStep := currentStep / 2
				if newStep < 5 {
					newStep = 5 // Минимальный шаг 5 клиентов
				}
				if newStep < currentStep {
					currentStep = newStep
					fmt.Printf("  📉 Adaptive step: reducing step size to %d (latency/error rate increasing)\n", currentStep)
				}
			} else if result.AvgLatency > 0 && result.AvgLatency < config.MaxLatencyMs*0.5 && currentStep < config.StepSize {
				// Если latency низкая, можно увеличить шаг обратно (но не больше исходного)
				newStep := currentStep * 2
				if newStep > config.StepSize {
					newStep = config.StepSize
				}
				if newStep > currentStep {
					currentStep = newStep
					fmt.Printf("  📈 Adaptive step: increasing step size to %d (latency stable)\n", currentStep)
				}
			}
		}
		
		previousLatency = result.AvgLatency
		previousErrorRate = result.ErrorRate

		if success {
			result.Success = true
			maxSuccessfulClients = numClients
			latencyInfo := ""
			if result.AvgLatency > 0 {
				latencyInfo = fmt.Sprintf(", Latency: %.2f ms", result.AvgLatency)
			}
			fmt.Printf("OK PASSED: %d clients - Error rate: %.2f%%, Throughput: %.2f msg/s (%.1f%% of expected %.0f msg/s)%s\n",
				numClients, result.ErrorRate, result.PlayerInputRate, throughputRatio, expectedRate, latencyInfo)
		} else {
			result.Success = false
			failureReason := ""
			if result.ErrorRate >= config.ErrorThreshold {
				failureReason += fmt.Sprintf("Error rate too high (%.2f%% >= %.2f%%), ", result.ErrorRate, config.ErrorThreshold)
			}
			if result.ConnectionsFailed > 0 {
				failureReason += fmt.Sprintf("Failed connections: %d, ", result.ConnectionsFailed)
			}
			if throughputRatio < 95.0 {
				failureReason += fmt.Sprintf("Throughput too low (%.1f%% of expected %.0f msg/s), ", throughputRatio, expectedRate)
			}
			if result.AvgLatency > 0 && result.AvgLatency > config.MaxLatencyMs {
				failureReason += fmt.Sprintf("Latency too high (%.2f ms > %.1f ms), ", result.AvgLatency, config.MaxLatencyMs)
			}
			fmt.Printf("❌ FAILED: %d clients - %sThroughput: %.2f msg/s\n",
				numClients, failureReason, result.PlayerInputRate)

			// Критерии остановки теста (competitive gaming standards):
			// 1. Процент ошибок превышает двойной порог (критично)
			// 2. Latency превышает критический порог (150ms - неиграбельно для киберспорта)
			// 3. Пропускная способность слишком низкая (<80% от ожидаемой)
			shouldStop := false
			stopReason := ""
			
			if result.ErrorRate >= config.ErrorThreshold*2 {
				shouldStop = true
				stopReason = fmt.Sprintf("Error rate critically high (%.2f%% >= %.2f%%)", result.ErrorRate, config.ErrorThreshold*2)
			} else if result.AvgLatency > 0 && result.AvgLatency > config.CriticalLatencyMs {
				shouldStop = true
				stopReason = fmt.Sprintf("Latency critically high (%.2f ms > %.1f ms) - unplayable for competitive gaming", result.AvgLatency, config.CriticalLatencyMs)
			} else if throughputRatio < 80.0 {
				shouldStop = true
				stopReason = fmt.Sprintf("Throughput critically low (%.1f%% of expected)", throughputRatio)
			}
			
			if shouldStop {
				fmt.Printf("WARNING  %s - stopping limit search.\n", stopReason)
				break
			}
		}

		results = append(results, result)
		maxLimit = numClients

		// Выводим детальную статистику
		printDetailedResult(result)

		// Если это не последний тест, делаем паузу
		if numClients+currentStep <= config.MaxClients {
			fmt.Printf("\nCooldown: waiting %v before next test (next step: %d clients)...\n\n", config.CooldownTime, currentStep)
			time.Sleep(config.CooldownTime)
		} else {
			break // Не можем добавить еще один шаг
		}
	}

	fmt.Printf("\n")
	fmt.Printf("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
	fmt.Printf("=== LIMIT SEARCH RESULTS ===\n\n")

	fmt.Printf("Maximum tested: %d clients\n", maxLimit)
	fmt.Printf("Maximum successful: %d clients\n", maxSuccessfulClients)

	if maxSuccessfulClients > 0 {
		fmt.Printf("\nOK RECOMMENDED LIMIT: %d clients\n", maxSuccessfulClients)
		fmt.Printf("   (with error threshold: %.2f%%)\n", config.ErrorThreshold)
	} else {
		fmt.Printf("\n❌ No successful tests found. Gateway may have issues at %d+ clients.\n", config.StartClients)
	}

	fmt.Printf("\n=== Detailed Results ===\n\n")
	printSummaryTable(results)
}

func runTest(config FindLimitConfig, numClients int) TestResult {
	ctx, cancel := context.WithTimeout(context.Background(), config.TestDuration+30*time.Second)
	defer cancel()

	clientMetrics := make([]*ClientMetrics, numClients)
	for i := 0; i < numClients; i++ {
		clientMetrics[i] = &ClientMetrics{
			PlayerID: fmt.Sprintf("p%08x", i),
		}
	}

	var clientsWg sync.WaitGroup
	var connectedWg sync.WaitGroup
	connectedWg.Add(numClients)
	startTestChan := make(chan struct{})
	testCtxChan := make(chan context.Context, numClients)

	// Создаем контекст для теста заранее (но таймер запустится после подключения всех)
	testCtx, testCancel := context.WithCancel(context.Background())
	defer testCancel()

	// Запускаем всех клиентов
	for i := 0; i < numClients; i++ {
		clientsWg.Add(1)
		testCtxChan <- testCtx
		go runClientTest(ctx, config, clientMetrics[i], &clientsWg, &connectedWg, startTestChan, testCtxChan)
	}

	// Ждем подключения всех клиентов (с таймаутом 30 секунд)
	connectedChan := make(chan struct{})
	go func() {
		connectedWg.Wait()
		close(connectedChan)
	}()

	connectionTimeout := 30 * time.Second
	select {
	case <-connectedChan:
		// Все клиенты подключились
	case <-time.After(connectionTimeout):
		// Таймаут подключения
		fmt.Printf("  WARNING  Warning: Not all clients connected within %v\n", connectionTimeout)
	}

	// Только после подключения всех клиентов начинаем измерение времени и тест
	startTime := time.Now()
	
	// Запускаем таймер для testCtx
	time.AfterFunc(config.TestDuration, func() {
		testCancel()
	})
	
	close(startTestChan) // Сигнализируем клиентам, что можно начинать тест

	// Ждем завершения теста или истечения времени
	done := make(chan struct{})
	go func() {
		// Ждем завершения всех клиентов
		clientsWg.Wait()
		close(done)
	}()

	select {
	case <-testCtx.Done():
		// Время теста истекло
		testCancel()
	case <-done:
		// Все клиенты завершились
		testCancel()
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
		
		// Суммируем latency метрики
		cmLatency := atomic.LoadInt64(&cm.TotalLatency)
		cmLatencyCount := atomic.LoadInt64(&cm.TotalLatencyCount)
		totalMetrics.TotalLatency += cmLatency
		totalMetrics.TotalLatencyCount += cmLatencyCount

		// Находим min/max latency для каждого клиента
		if cmLatencyCount > 0 {
			avgLat := float64(cmLatency) / float64(cmLatencyCount) / float64(time.Millisecond)
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

func runClientTest(ctx context.Context, config FindLimitConfig, metrics *ClientMetrics, wg *sync.WaitGroup, connectedWg *sync.WaitGroup, startTestChan <-chan struct{}, testCtxChan <-chan context.Context) {
	defer wg.Done()

	// Подключаемся к серверу с отдельным контекстом для подключения
	connectCtx, connectCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer connectCancel()

	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
	}

	conn, _, err := dialer.DialContext(connectCtx, config.ServerURL, nil)
	if err != nil {
		atomic.AddInt64(&metrics.ConnectionsFailed, 1)
		connectedWg.Done()
		return
	}
	defer conn.Close()

	atomic.AddInt64(&metrics.Connections, 1)
	connectedWg.Done() // Сигнализируем, что клиент подключился

	// Ждем сигнала начала теста (все клиенты подключились)
	<-startTestChan

	// Получаем контекст для теста
	testCtx := <-testCtxChan

	// Запускаем горутину для чтения сообщений
	var readWg sync.WaitGroup
	readWg.Add(1)
	go func() {
		defer readWg.Done()
		for {
			select {
			case <-testCtx.Done():
				return
			default:
				conn.SetReadDeadline(time.Now().Add(1 * time.Second))
				_, data, err := conn.ReadMessage()
				if err != nil {
					if websocket.IsUnexpectedCloseError(err, websocket.CloseNormalClosure, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
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
	tickerInterval := time.Duration(1000.0/float64(config.PlayerInputHz)) * time.Millisecond
	ticker := time.NewTicker(tickerInterval)
	defer ticker.Stop()

	tick := int64(0)
	playerID := metrics.PlayerID
	testStartTime := time.Now()

	for {
		select {
		case <-testCtx.Done():
			ticker.Stop()
			conn.SetWriteDeadline(time.Now().Add(1 * time.Second))
			conn.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseNormalClosure, ""))
			readWg.Wait()
			return
		case <-ticker.C:
			tick++
			startTime := time.Now()

			// Симулируем движение и поворот для постоянных изменений в GameState
			elapsed := time.Since(testStartTime).Seconds()
			
			// Каждый клиент имеет свой offset для уникальности движения
			clientOffset := float64(tick % 100) * 0.1
			moveX := float32(math.Sin(elapsed*0.5 + clientOffset))
			moveY := float32(math.Cos(elapsed*0.5 + clientOffset))
			
			// Симулируем поворот камеры
			aimX := float32(math.Sin(elapsed*0.3 + clientOffset*2))
			aimY := float32(math.Cos(elapsed*0.3 + clientOffset*2))

			// Формируем PlayerInput сообщение с изменяющимися данными
			message := buildPlayerInputMessage(playerID, tick, moveX, moveY, false, aimX, aimY)

			// Отправляем сообщение
			conn.SetWriteDeadline(time.Now().Add(5 * time.Second))
			err := conn.WriteMessage(websocket.BinaryMessage, message)
			if err != nil {
				if !websocket.IsCloseError(err, websocket.CloseNormalClosure, websocket.CloseGoingAway) {
					atomic.AddInt64(&metrics.Errors, 1)
				}
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
		success := "OK PASS"
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

