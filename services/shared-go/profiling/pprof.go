// pprof Integration for Advanced Profiling
// Issue: #2076
// PERFORMANCE: HTTP endpoints for pprof profiling

package profiling

import (
	"context"
	"fmt"
	"net/http"
	"runtime"
	"time"

	_ "net/http/pprof" // Register pprof handlers

	"github.com/go-faster/errors"
	"go.uber.org/zap"
)

// PprofServer provides HTTP endpoints for pprof profiling
type PprofServer struct {
	addr   string
	server *http.Server
	logger *zap.Logger
}

// PprofConfig holds configuration for pprof server
type PprofConfig struct {
	Addr   string // Address to listen on (default: :6060)
	Logger *zap.Logger
}

// NewPprofServer creates a new pprof server
func NewPprofServer(config PprofConfig) (*PprofServer, error) {
	if config.Addr == "" {
		config.Addr = ":6060"
	}
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	mux := http.NewServeMux()
	mux.HandleFunc("/debug/pprof/", pprofIndex)
	mux.HandleFunc("/debug/pprof/cmdline", pprofCmdline)
	mux.HandleFunc("/debug/pprof/profile", pprofProfile)
	mux.HandleFunc("/debug/pprof/symbol", pprofSymbol)
	mux.HandleFunc("/debug/pprof/trace", pprofTrace)
	mux.HandleFunc("/debug/pprof/heap", pprofHeap)
	mux.HandleFunc("/debug/pprof/goroutine", pprofGoroutine)
	mux.HandleFunc("/debug/pprof/allocs", pprofAllocs)
	mux.HandleFunc("/debug/pprof/block", pprofBlock)
	mux.HandleFunc("/debug/pprof/mutex", pprofMutex)

	server := &http.Server{
		Addr:         config.Addr,
		Handler:      mux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
		IdleTimeout:  30 * time.Second,
	}

	return &PprofServer{
		addr:   config.Addr,
		server: server,
		logger: config.Logger,
	}, nil
}

// Start starts the pprof server
func (ps *PprofServer) Start(ctx context.Context) error {
	ps.logger.Info("Starting pprof profiling server", zap.String("addr", ps.addr))

	go func() {
		if err := ps.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			ps.logger.Error("Pprof server failed", zap.Error(err))
		}
	}()

	// Wait for context cancellation
	<-ctx.Done()

	return ps.Shutdown(context.Background())
}

// Shutdown gracefully shuts down the pprof server
func (ps *PprofServer) Shutdown(ctx context.Context) error {
	ps.logger.Info("Shutting down pprof profiling server")

	if err := ps.server.Shutdown(ctx); err != nil {
		return errors.Wrap(err, "failed to shutdown pprof server")
	}

	return nil
}

// pprofIndex serves the pprof index page
func pprofIndex(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html")
	fmt.Fprintf(w, `<html>
<head><title>Go Profiling Index</title></head>
<body>
<h1>Profiling Endpoints</h1>
<ul>
<li><a href="/debug/pprof/heap">Heap Profile</a></li>
<li><a href="/debug/pprof/goroutine">Goroutine Profile</a></li>
<li><a href="/debug/pprof/allocs">Allocation Profile</a></li>
<li><a href="/debug/pprof/block">Block Profile</a></li>
<li><a href="/debug/pprof/mutex">Mutex Profile</a></li>
<li><a href="/debug/pprof/profile?seconds=30">CPU Profile (30s)</a></li>
<li><a href="/debug/pprof/trace?seconds=5">Execution Trace (5s)</a></li>
</ul>
</body>
</html>`)
}

// pprofCmdline serves command line arguments
func pprofCmdline(w http.ResponseWriter, r *http.Request) {
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofProfile serves CPU profile
func pprofProfile(w http.ResponseWriter, r *http.Request) {
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofSymbol serves symbol information
func pprofSymbol(w http.ResponseWriter, r *http.Request) {
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofTrace serves execution trace
func pprofTrace(w http.ResponseWriter, r *http.Request) {
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofHeap serves heap profile
func pprofHeap(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=heap_%d.prof", time.Now().Unix()))
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofGoroutine serves goroutine profile
func pprofGoroutine(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=goroutine_%d.prof", time.Now().Unix()))
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofAllocs serves allocation profile
func pprofAllocs(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=allocs_%d.prof", time.Now().Unix()))
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofBlock serves block profile
func pprofBlock(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=block_%d.prof", time.Now().Unix()))
	http.DefaultServeMux.ServeHTTP(w, r)
}

// pprofMutex serves mutex profile
func pprofMutex(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=mutex_%d.prof", time.Now().Unix()))
	http.DefaultServeMux.ServeHTTP(w, r)
}

// GetGoroutineStack returns current goroutine stack trace
func GetGoroutineStack() []byte {
	buf := make([]byte, 1024*1024) // 1 MB buffer
	n := runtime.Stack(buf, true)
	return buf[:n]
}

// GetHeapProfile returns current heap profile
func GetHeapProfile() ([]byte, error) {
	// Note: This is a placeholder. Actual implementation would use:
	// import "runtime/pprof"
	// var buf bytes.Buffer
	// err := pprof.WriteHeapProfile(&buf)
	// return buf.Bytes(), err
	return nil, errors.New("heap profile not implemented (requires runtime/pprof)")
}
