package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling for real-time server
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/realtime-gateway-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Realtime WebSocket Gateway (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:18080")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/0")
	serverID := getEnv("SERVER_ID", "gateway-1")
	tickRate := 60

	sessionMgr, err := server.NewSessionManager(redisURL, serverID)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize session manager")
	}

	handler := server.NewGatewayHandler(tickRate, sessionMgr)
	
	// OPTIMIZATION: Issue #1584 - pprof for real-time performance monitoring
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6064")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// CRITICAL for real-time: monitor CPU, goroutines, allocations
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()
	
	if sessionMgr != nil {
		redisClient := sessionMgr.GetRedisClient()
		if redisClient != nil {
			banNotifier := server.NewBanNotificationSubscriber(redisClient, handler)
			handler.SetBanNotifier(banNotifier)
			
			if err := banNotifier.Start(); err != nil {
				logger.WithError(err).Error("Failed to start ban notification subscriber")
			} else {
				logger.Info("Ban notification subscriber started")
			}

			notificationSubscriber := server.NewNotificationSubscriber(redisClient, handler)
			handler.SetNotificationSubscriber(notificationSubscriber)
			
			if err := notificationSubscriber.Start(); err != nil {
				logger.WithError(err).Error("Failed to start notification subscriber")
			} else {
				logger.Info("Notification subscriber started")
			}
		}
	}
	
	wsServer := server.NewWebSocketServer(addr, handler)

	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())

	metricsServer := &http.Server{
		Addr:         metricsAddr,
		Handler:      metricsMux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	go func() {
		logger.WithField("addr", metricsAddr).Info("Metrics server starting")
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Metrics server failed")
		}
	}()

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	if sessionMgr != nil {
		go func() {
			ticker := time.NewTicker(5 * time.Minute)
			defer ticker.Stop()
			for {
				select {
				case <-ctx.Done():
					return
				case <-ticker.C:
					sessionMgr.CleanupExpiredSessions(context.Background())
				}
			}
		}()
	}

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")
		cancel()

		if handler != nil {
			if handler.GetBanNotifier() != nil {
				if err := handler.GetBanNotifier().Stop(); err != nil {
					logger.WithError(err).Error("Failed to stop ban notification subscriber")
				}
			}
			if handler.GetNotificationSubscriber() != nil {
				if err := handler.GetNotificationSubscriber().Stop(); err != nil {
					logger.WithError(err).Error("Failed to stop notification subscriber")
				}
			}
		}

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		metricsServer.Shutdown(shutdownCtx)
	}()

	if err := wsServer.Start(ctx); err != nil && err != http.ErrServerClosed {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
