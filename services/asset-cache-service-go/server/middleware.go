package server

import (
	"log/slog"
	"net/http"
	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

// NewMiddleware creates Echo middleware for the asset cache service
func NewMiddleware() []echo.MiddlewareFunc {
	return []echo.MiddlewareFunc{
		middleware.LoggerWithConfig(middleware.LoggerConfig{
			Format: `{"time":"${time_rfc3339_nano}","level":"INFO","msg":"request","method":"${method}","uri":"${uri}","status":${status},"latency":"${latency_human}","bytes_in":${bytes_in},"bytes_out":${bytes_out}}` + "\n",
			Output: slog.Default().Handler(),
		}),
		middleware.Recover(),
		middleware.CORS(),
		middleware.TimeoutWithConfig(middleware.TimeoutConfig{
			Timeout: 30 * time.Second,
		}),
		middleware.RateLimiterWithConfig(middleware.RateLimiterConfig{
			Store: middleware.NewRateLimiterMemoryStoreWithConfig(
				middleware.RateLimiterMemoryStoreConfig{Rate: 100, Burst: 200},
			),
			IdentifierExtractor: func(ctx echo.Context) (string, error) {
				id := ctx.RealIP()
				return id, nil
			},
			ErrorHandler: func(context echo.Context, err error) error {
				return context.JSON(http.StatusTooManyRequests, map[string]string{
					"error": "rate limit exceeded",
				})
			},
		}),
		requestMetricsMiddleware(),
	}
}

// requestMetricsMiddleware adds request metrics tracking
func requestMetricsMiddleware() echo.MiddlewareFunc {
	return func(next echo.HandlerFunc) echo.HandlerFunc {
		return func(c echo.Context) error {
			start := time.Now()

			err := next(c)

			duration := time.Since(start)

			// Log performance metrics for asset requests
			if c.Path() == "/assets/:id" {
				status := c.Response().Status
				method := c.Request().Method

				slog.Info("Asset request metrics",
					"method", method,
					"path", c.Path(),
					"status", status,
					"duration_ms", duration.Milliseconds(),
					"bytes_out", c.Response().Size,
				)
			}

			return err
		}
	}
}