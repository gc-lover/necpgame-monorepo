import (
	"context"
	"flag"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"sync"
	"syscall"
	"time"

	_ "net/http/pprof"

	"github.com/gc-lover/necpgame-monorepo/services/message-queue-service-go/server"
	"github.com/sirupsen/logrus"
)

// MessageQueueServiceConfig OPTIMIZATION: Issue #2205 - Memory-aligned struct for message queue service performance
type MessageQueueServiceConfig struct {
	StateSyncInterval time.Duration `json:"state_sync_interval"` // 8 bytes
	ReadTimeout       time.Duration `json:"read_timeout"`        // 8 bytes
	WriteTimeout      time.Duration `json:"write_timeout"`       // 8 bytes
	MetricsInterval   time.Duration `json:"metrics_interval"`    // 8 bytes
	DefaultTimeout    time.Duration `json:"default_timeout"`     // 8 bytes
	CleanupInterval   time.Duration `json:"cleanup_interval"`    // 8 bytes
	HTTPAddr          string        `json:"http_addr"`           // 16 bytes
	RedisAddr         string        `json:"redis_addr"`          // 16 bytes
	PprofAddr         string        `json:"pprof_addr"`          // 16 bytes
	HealthAddr        string        `json:"health_addr"`         // 16 bytes
	MaxConnections    int           `json:"max_connections"`     // 8 bytes
	MaxHeaderBytes    int           `json:"max_header_bytes"`    // 8 bytes
	DefaultQueueSize  int           `json:"default_queue_size"`  // 8 bytes
	DefaultMessageTTL time.Duration `json:"default_message_ttl"` // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)

	config := &MessageQueueServiceConfig{
		HTTPAddr:          ":8080",
		RedisAddr:         "localhost:6379",
		PprofAddr:         ":6060",
		HealthAddr:        ":8081",
		ReadTimeout:       30 * time.Second,
		WriteTimeout:      30 * time.Second,
		MetricsInterval:   10 * time.Second,
		StateSyncInterval: 30 * time.Second,
		DefaultTimeout:    5 * time.Second,
		CleanupInterval:   5 * time.Minute,
		MaxConnections:    10000,
		MaxHeaderBytes:    1 << 20, // 1MB
		DefaultQueueSize:  10000,
		DefaultMessageTTL: 24 * time.Hour,
	}

	// Parse command line flags
	flag.StringVar(&config.HTTPAddr, "http-addr", config.HTTPAddr, "HTTP server address")
	flag.StringVar(&config.RedisAddr, "redis-addr", config.RedisAddr, "Redis server address")
	flag.StringVar(&config.PprofAddr, "pprof-addr", config.PprofAddr, "Pprof server address")
	flag.StringVar(&config.HealthAddr, "health-addr", config.HealthAddr, "Health check server address")
	flag.Parse()

	// OPTIMIZATION: Issue #2205 - GC tuning for message queue performance
	runtime.SetGCPercent(50)

	// Start pprof server
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("starting pprof server")
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Start health check server
	go func() {
		http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
			w.WriteHeader(http.StatusOK)
			w.Write([]byte("OK"))
		})
		logger.WithField("addr", config.HealthAddr).Info("starting health server")
		if err := http.ListenAndServe(config.HealthAddr, nil); err != nil {
			logger.WithError(err).Error("health server failed")
		}
	}()

	// Create message queue service
	service := server.NewMessageQueueService(config, logger)
	if err != nil {
		log.Fatal("failed to create message queue service:", err)
	}

	// Start HTTP server
	httpServer, err := server.NewMessageQueueServer(config, service, logger)
	if err != nil {
		log.Fatal("failed to create HTTP server:", err)
	}

	// Start servers
	var wg sync.WaitGroup
	wg.Add(1)

	go func() {
		defer wg.Done()
		logger.WithField("addr", config.HTTPAddr).Info("starting HTTP server")
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Error("HTTP server failed")
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down servers...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("HTTP server shutdown failed")
	}

	service.Shutdown(ctx)

	wg.Wait()
	logger.Info("servers stopped")
}
