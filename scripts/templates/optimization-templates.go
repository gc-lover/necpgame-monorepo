// Code templates for automated service optimization
// These templates provide standardized optimizations for different service types

package templates

// GameServiceTemplate provides optimization templates for game-related services
// (combat, ability, weapon services)
const GameServiceTemplate = `// PERFORMANCE: Memory pooling for hot path objects (Level 2 optimization)
// Reduces GC pressure in high-throughput game operations
var (
	{{.EntityName}}Pool = sync.Pool{
		New: func() interface{} {
			return &{{.EntityName}}{}
		},
	}

	{{.EventName}}Pool = sync.Pool{
		New: func() interface{} {
			return &{{.EventName}}{}
		},
	}
)

// PERFORMANCE: Memory pool management functions
func get{{.EntityName}}() *{{.EntityName}} {
	return {{.EntityName}}Pool.Get().(*{{.EntityName}})
}

func put{{.EntityName}}(entity *{{.EntityName}}) {
	// Reset fields for reuse
	{{range .EntityFields}}
	entity.{{.}} = {{.DefaultValue}}
	{{end}}
	{{.EntityName}}Pool.Put(entity)
}

func get{{.EventName}}() *{{.EventName}} {
	return {{.EventName}}Pool.Get().(*{{.EventName}})
}

func put{{.EventName}}(event *{{.EventName}}) {
	// Reset fields for reuse
	event.ID = ""
	event.Type = ""
	event.Timestamp = time.Time{}
	event.Participant = ""
	event.Description = ""
	{{.EventName}}Pool.Put(event)
}
`

// AuthServiceTemplate provides optimization templates for authentication services
const AuthServiceTemplate = `// PERFORMANCE: Memory pooling for hot path objects (Level 2 optimization)
// Reduces GC pressure in high-throughput authentication operations
var (
	jwtClaimsPool = sync.Pool{
		New: func() interface{} {
			return &JWTClaims{}
		},
	}

	userSessionPool = sync.Pool{
		New: func() interface{} {
			return &UserSession{}
		},
	}

	tokenResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.TokenResponse{}
		},
	}
)

// PERFORMANCE: Memory pool management functions
func getJWTClaims() *JWTClaims {
	return jwtClaimsPool.Get().(*JWTClaims)
}

func putJWTClaims(claims *JWTClaims) {
	// Reset fields for reuse
	claims.UserID = ""
	claims.Username = ""
	claims.Email = ""
	claims.RegisteredClaims = jwt.RegisteredClaims{}
	jwtClaimsPool.Put(claims)
}

func getUserSession() *UserSession {
	return userSessionPool.Get().(*UserSession)
}

func putUserSession(session *UserSession) {
	// Reset fields for reuse
	session.UserID = ""
	session.Token = ""
	session.CreatedAt = time.Time{}
	session.ExpiresAt = time.Time{}
	userSessionPool.Put(session)
}
`

// DatabasePoolTemplate provides standardized database pool configuration
const DatabasePoolTemplate = `// initDatabase initializes PostgreSQL connection with performance optimizations
// PERFORMANCE: Optimized connection pool for {{.ServiceType}} operations
func (s *Service) initDatabase(databaseURL string) error {
	// PERFORMANCE: Optimized connection pool for {{.ServiceType}}
	config, err := pgxpool.ParseConfig(databaseURL)
	if err != nil {
		return errors.Wrap(err, "failed to parse database URL")
	}

	// PERFORMANCE: Tune connection pool for {{.ServiceType}} service
	config.MaxConns = {{.MaxConns}}                    // {{.MaxConnsDesc}}
	config.MinConns = {{.MinConns}}                    // {{.MinConnsDesc}}
	config.MaxConnLifetime = {{.MaxConnLifetime}}     // {{.MaxConnLifetimeDesc}}
	config.MaxConnIdleTime = {{.MaxConnIdleTime}}     // {{.MaxConnIdleTimeDesc}}

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return errors.Wrap(err, "failed to create connection pool")
	}

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		return errors.Wrap(err, "failed to ping database")
	}

	s.db = pool
	s.logger.Info("Database connection established with {{.ServiceType}} optimizations",
		zap.Int("max_conns", {{.MaxConns}}),
		zap.Int("min_conns", {{.MinConns}}))
	return nil
}`

// RedisPoolTemplate provides standardized Redis pool configuration
const RedisPoolTemplate = `// initRedis initializes Redis connection for {{.ServiceType}}
// PERFORMANCE: Optimized for {{.ServiceType}} operations
func (s *Service) initRedis(redisURL string) error {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return errors.Wrap(err, "failed to parse redis URL")
	}

	// PERFORMANCE: Optimize Redis client for {{.ServiceType}} operations
	rdb := redis.NewClient(opt)
	rdb.Options().PoolSize = {{.PoolSize}}           // {{.PoolSizeDesc}}
	rdb.Options().MinIdleConns = {{.MinIdleConns}}   // {{.MinIdleConnsDesc}}
	rdb.Options().ConnMaxLifetime = {{.ConnMaxLifetime}} // {{.ConnMaxLifetimeDesc}}

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := rdb.Ping(ctx).Err(); err != nil {
		return errors.Wrap(err, "failed to ping redis")
	}

	s.redis = rdb
	s.logger.Info("Redis connection established with {{.ServiceType}} optimizations",
		zap.Int("pool_size", {{.PoolSize}}),
		zap.Int("min_idle", {{.MinIdleConns}}))
	return nil
}`

// HTTPServerTemplate provides optimized HTTP server configuration
const HTTPServerTemplate = `	// Create enterprise-grade HTTP server with {{.ServiceType}} optimizations
	// PERFORMANCE: Tuned for {{.ServiceType}} operations
	srv := &http.Server{
		Addr:              addr,
		Handler:           svc,
		ReadTimeout:       {{.ReadTimeout}},       // {{.ReadTimeoutDesc}}
		WriteTimeout:      {{.WriteTimeout}},      // {{.WriteTimeoutDesc}}
		IdleTimeout:       {{.IdleTimeout}},       // {{.IdleTimeoutDesc}}
		ReadHeaderTimeout: {{.ReadHeaderTimeout}}, // {{.ReadHeaderTimeoutDesc}}
		MaxHeaderBytes:    {{.MaxHeaderBytes}},    // {{.MaxHeaderBytesDesc}}
	}`

// PprofTemplate provides pprof endpoint setup
const PprofTemplate = `import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // PERFORMANCE: pprof endpoint for profiling (Level 3 optimization)
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"
	{{.AdditionalImports}}
)

// Main function with pprof endpoint
func main() {
	// PERFORMANCE: GC tuning for {{.ServiceType}} operations (Level 3 optimization)
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		// debug.SetGCPercent({{.GOGCValue}}) // Uncomment for production tuning
	}

	// ... existing logger setup ...

	// PERFORMANCE: Profiling endpoint for real-time performance monitoring
	profilingAddr := os.Getenv("PPROF_ADDR")
	if profilingAddr == "" {
		profilingAddr = "{{.PprofAddr}}" // {{.ServiceName}} profiling port
	}

	// PERFORMANCE: Start pprof profiling server for real-time performance monitoring
	go func() {
		logger.Info("Starting pprof profiling server", zap.String("addr", profilingAddr))
		if err := http.ListenAndServe(profilingAddr, nil); err != nil {
			logger.Error("Pprof server failed", zap.Error(err))
		}
	}()

	// ... existing server setup ...

	// Start server in goroutine
	go func() {
		logger.Info("Starting {{.ServiceName}}",
			zap.String("addr", addr),
			zap.String("pprof_addr", profilingAddr),
			zap.String("performance_target", "{{.PerformanceTarget}}"),
			zap.String("optimization_level", "{{.OptimizationLevel}}"))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed", zap.Error(err))
		}
	}()

	// ... existing graceful shutdown ...
}`

// ContextTimeoutTemplate provides standardized context timeout usage
const ContextTimeoutTemplate = `// withTimeout creates context with timeout for operation safety
// PERFORMANCE: Prevents hanging operations in {{.ServiceType}} sessions
func (h *Handler) withTimeout(ctx context.Context, operation string, timeout time.Duration) (context.Context, context.CancelFunc) {
	return context.WithTimeout(ctx, timeout)
}

// Example usage in handlers:
func (h *Handler) SomeOperation(ctx context.Context, req *api.Request) (api.Response, error) {
	ctx, cancel := h.withTimeout(ctx, "SomeOperation", {{.TimeoutDuration}})
	defer cancel()

	// ... operation logic ...
}`