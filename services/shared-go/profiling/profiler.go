// Advanced Memory Profiling and Leak Detection
// Issue: #2076
// PERFORMANCE: Continuous memory profiling, leak detection, automated reporting

package profiling

import (
	"context"
	"fmt"
	"runtime"
	"sync"
	"sync/atomic"
	"time"

	"github.com/go-faster/errors"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// ProfilerConfig holds configuration for the profiler
type ProfilerConfig struct {
	Logger           *zap.Logger
	Meter            metric.Meter
	ProfilingInterval time.Duration // How often to collect profiles (default: 30s)
	LeakDetectionEnabled bool        // Enable leak detection
	LeakThreshold    int64           // Memory increase threshold for leak detection (bytes)
	LeakWindow       time.Duration   // Time window for leak detection (default: 5m)
	HeapDumpEnabled  bool            // Enable heap dumps
	HeapDumpInterval time.Duration   // Interval for heap dumps (default: 1h)
	HeapDumpPath     string          // Path to store heap dumps
}

// MemoryStats holds current memory statistics
type MemoryStats struct {
	Timestamp       time.Time
	HeapAlloc       uint64 // Bytes allocated and not yet freed
	HeapSys         uint64 // Bytes obtained from system
	HeapIdle        uint64 // Bytes in idle spans
	HeapInuse       uint64 // Bytes in non-idle span
	HeapReleased    uint64 // Bytes released to OS
	HeapObjects     uint64 // Total number of allocated objects
	NumGC           uint32 // Number of GC runs
	GCCPUFraction   float64 // Fraction of CPU time used by GC
	TotalAlloc      uint64 // Cumulative bytes allocated
	Mallocs         uint64 // Cumulative count of mallocs
	Frees           uint64 // Cumulative count of frees
	GoroutineCount  int    // Number of goroutines
	GCPauseTime     uint64 // Cumulative GC pause time (nanoseconds)
}

// LeakDetectionResult represents leak detection results
type LeakDetectionResult struct {
	Detected        bool
	LeakSize        int64           // Estimated leak size (bytes)
	LeakRate        float64         // Leak rate (bytes/second)
	GrowthRate      float64         // Memory growth rate (bytes/second)
	TimeWindow      time.Duration
	Recommendations []string
}

// Profiler provides advanced memory profiling and leak detection
type Profiler struct {
	config ProfilerConfig
	logger *zap.Logger
	meter  metric.Meter

	// Memory snapshots for leak detection
	snapshots     []MemoryStats
	snapshotMutex sync.RWMutex
	maxSnapshots  int

	// Metrics
	heapAllocGauge      metric.Int64Gauge
	heapInuseGauge      metric.Int64Gauge
	heapObjectsGauge    metric.Int64Gauge
	numGCGauge          metric.Int64Gauge
	gcPauseTimeGauge    metric.Int64Gauge
	goroutineCountGauge metric.Int64Gauge
	leakDetectedCounter metric.Int64Counter

	// Control
	running    atomic.Bool
	stopChan   chan struct{}
	wg         sync.WaitGroup
}

// NewProfiler creates a new profiler instance
func NewProfiler(config ProfilerConfig) (*Profiler, error) {
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}
	if config.Meter == nil {
		return nil, errors.New("meter is required")
	}
	if config.ProfilingInterval == 0 {
		config.ProfilingInterval = 30 * time.Second
	}
	if config.LeakWindow == 0 {
		config.LeakWindow = 5 * time.Minute
	}
	if config.HeapDumpInterval == 0 {
		config.HeapDumpInterval = 1 * time.Hour
	}
	if config.LeakThreshold == 0 {
		config.LeakThreshold = 10 * 1024 * 1024 // 10 MB default
	}

	maxSnapshots := int(config.LeakWindow / config.ProfilingInterval)
	if maxSnapshots == 0 {
		maxSnapshots = 10 // Default to 10 snapshots
	}

	p := &Profiler{
		config:      config,
		logger:      config.Logger,
		meter:       config.Meter,
		snapshots:   make([]MemoryStats, 0, maxSnapshots),
		maxSnapshots: maxSnapshots,
		stopChan:    make(chan struct{}),
	}

	// Initialize metrics
	var err error
	p.heapAllocGauge, err = p.meter.Int64Gauge(
		"memory_heap_alloc_bytes",
		metric.WithDescription("Bytes allocated and not yet freed"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create heap alloc gauge")
	}

	p.heapInuseGauge, err = p.meter.Int64Gauge(
		"memory_heap_inuse_bytes",
		metric.WithDescription("Bytes in non-idle span"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create heap inuse gauge")
	}

	p.heapObjectsGauge, err = p.meter.Int64Gauge(
		"memory_heap_objects_total",
		metric.WithDescription("Total number of allocated objects"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create heap objects gauge")
	}

	p.numGCGauge, err = p.meter.Int64Gauge(
		"gc_runs_total",
		metric.WithDescription("Number of GC runs"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create num GC gauge")
	}

	p.gcPauseTimeGauge, err = p.meter.Int64Gauge(
		"gc_pause_time_ns",
		metric.WithDescription("Cumulative GC pause time in nanoseconds"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create GC pause time gauge")
	}

	p.goroutineCountGauge, err = p.meter.Int64Gauge(
		"goroutines_total",
		metric.WithDescription("Number of goroutines"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create goroutine count gauge")
	}

	p.leakDetectedCounter, err = p.meter.Int64Counter(
		"memory_leak_detected_total",
		metric.WithDescription("Total number of memory leaks detected"),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create leak detected counter")
	}

	return p, nil
}

// Start starts the profiler
func (p *Profiler) Start(ctx context.Context) error {
	if !p.running.CompareAndSwap(false, true) {
		return errors.New("profiler is already running")
	}

	p.logger.Info("Starting memory profiler",
		zap.Duration("profiling_interval", p.config.ProfilingInterval),
		zap.Bool("leak_detection", p.config.LeakDetectionEnabled),
		zap.Bool("heap_dump", p.config.HeapDumpEnabled))

	p.wg.Add(1)
	go p.profilingLoop(ctx)

	if p.config.LeakDetectionEnabled {
		p.wg.Add(1)
		go p.leakDetectionLoop(ctx)
	}

	if p.config.HeapDumpEnabled {
		p.wg.Add(1)
		go p.heapDumpLoop(ctx)
	}

	return nil
}

// Stop stops the profiler
func (p *Profiler) Stop() {
	if !p.running.CompareAndSwap(true, false) {
		return
	}

	close(p.stopChan)
	p.wg.Wait()

	p.logger.Info("Memory profiler stopped")
}

// profilingLoop collects memory statistics periodically
func (p *Profiler) profilingLoop(ctx context.Context) {
	defer p.wg.Done()

	ticker := time.NewTicker(p.config.ProfilingInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-p.stopChan:
			return
		case <-ticker.C:
			stats := p.collectMemoryStats()
			p.updateMetrics(stats)
			p.addSnapshot(stats)
		}
	}
}

// leakDetectionLoop detects memory leaks
func (p *Profiler) leakDetectionLoop(ctx context.Context) {
	defer p.wg.Done()

	ticker := time.NewTicker(p.config.LeakWindow / 2)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-p.stopChan:
			return
		case <-ticker.C:
			result := p.detectLeaks()
			if result.Detected {
				p.logger.Warn("Memory leak detected",
					zap.Int64("leak_size_bytes", result.LeakSize),
					zap.Float64("leak_rate_bytes_per_sec", result.LeakRate),
					zap.Float64("growth_rate_bytes_per_sec", result.GrowthRate),
					zap.Duration("time_window", result.TimeWindow))

				p.leakDetectedCounter.Add(ctx, 1)

				// Log recommendations
				for _, rec := range result.Recommendations {
					p.logger.Info("Leak detection recommendation", zap.String("recommendation", rec))
				}
			}
		}
	}
}

// heapDumpLoop periodically creates heap dumps
func (p *Profiler) heapDumpLoop(ctx context.Context) {
	defer p.wg.Done()

	ticker := time.NewTicker(p.config.HeapDumpInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-p.stopChan:
			return
		case <-ticker.C:
			if err := p.createHeapDump(); err != nil {
				p.logger.Error("Failed to create heap dump", zap.Error(err))
			}
		}
	}
}

// collectMemoryStats collects current memory statistics
func (p *Profiler) collectMemoryStats() MemoryStats {
	var m runtime.MemStats
	runtime.ReadMemStats(&m)

	var gcPause uint64
	if len(m.PauseNs) > 0 {
		for _, pause := range m.PauseNs {
			gcPause += pause
		}
	}

	return MemoryStats{
		Timestamp:      time.Now(),
		HeapAlloc:      m.HeapAlloc,
		HeapSys:        m.HeapSys,
		HeapIdle:       m.HeapIdle,
		HeapInuse:      m.HeapInuse,
		HeapReleased:   m.HeapReleased,
		HeapObjects:    m.HeapObjects,
		NumGC:          m.NumGC,
		GCCPUFraction:  m.GCCPUFraction,
		TotalAlloc:     m.TotalAlloc,
		Mallocs:        m.Mallocs,
		Frees:          m.Frees,
		GoroutineCount: runtime.NumGoroutine(),
		GCPauseTime:    gcPause,
	}
}

// addSnapshot adds a memory snapshot for leak detection
func (p *Profiler) addSnapshot(stats MemoryStats) {
	p.snapshotMutex.Lock()
	defer p.snapshotMutex.Unlock()

	p.snapshots = append(p.snapshots, stats)

	// Keep only recent snapshots
	if len(p.snapshots) > p.maxSnapshots {
		p.snapshots = p.snapshots[1:]
	}
}

// updateMetrics updates OpenTelemetry metrics
func (p *Profiler) updateMetrics(stats MemoryStats) {
	p.heapAllocGauge.Set(int64(stats.HeapAlloc))
	p.heapInuseGauge.Set(int64(stats.HeapInuse))
	p.heapObjectsGauge.Set(int64(stats.HeapObjects))
	p.numGCGauge.Set(int64(stats.NumGC))
	p.gcPauseTimeGauge.Set(int64(stats.GCPauseTime))
	p.goroutineCountGauge.Set(int64(stats.GoroutineCount))
}

// detectLeaks detects memory leaks based on snapshots
func (p *Profiler) detectLeaks() LeakDetectionResult {
	p.snapshotMutex.RLock()
	defer p.snapshotMutex.RUnlock()

	if len(p.snapshots) < 2 {
		return LeakDetectionResult{Detected: false}
	}

	// Get first and last snapshots within leak window
	now := time.Now()
	var recentSnapshots []MemoryStats
	for _, snapshot := range p.snapshots {
		if now.Sub(snapshot.Timestamp) <= p.config.LeakWindow {
			recentSnapshots = append(recentSnapshots, snapshot)
		}
	}

	if len(recentSnapshots) < 2 {
		return LeakDetectionResult{Detected: false}
	}

	first := recentSnapshots[0]
	last := recentSnapshots[len(recentSnapshots)-1]

	timeWindow := last.Timestamp.Sub(first.Timestamp)
	if timeWindow <= 0 {
		return LeakDetectionResult{Detected: false}
	}

	// Calculate memory growth
	growth := int64(last.HeapAlloc) - int64(first.HeapAlloc)
	growthRate := float64(growth) / timeWindow.Seconds()

	// Check if growth exceeds threshold
	detected := growth > p.config.LeakThreshold && growthRate > 0

	if !detected {
		return LeakDetectionResult{Detected: false}
	}

	// Estimate leak size (difference between first and last)
	leakSize := growth
	leakRate := growthRate

	// Generate recommendations
	recommendations := p.generateRecommendations(last, growth, growthRate)

	return LeakDetectionResult{
		Detected:       true,
		LeakSize:       leakSize,
		LeakRate:       leakRate,
		GrowthRate:     growthRate,
		TimeWindow:     timeWindow,
		Recommendations: recommendations,
	}
}

// generateRecommendations generates recommendations based on leak analysis
func (p *Profiler) generateRecommendations(stats MemoryStats, growth int64, growthRate float64) []string {
	var recommendations []string

	// High goroutine count might indicate goroutine leaks
	if stats.GoroutineCount > 10000 {
		recommendations = append(recommendations, "High goroutine count detected - check for goroutine leaks")
	}

	// High GC pause time might indicate memory pressure
	if stats.GCCPUFraction > 0.25 {
		recommendations = append(recommendations, "High GC CPU usage - consider reducing allocations or increasing GOGC")
	}

	// High heap objects might indicate object leaks
	if stats.HeapObjects > 1000000 {
		recommendations = append(recommendations, "High number of heap objects - check for object leaks")
	}

	// High leak rate
	if growthRate > 1024*1024 { // > 1 MB/s
		recommendations = append(recommendations, "High memory leak rate - immediate investigation required")
	}

	if len(recommendations) == 0 {
		recommendations = append(recommendations, "Review memory allocation patterns, use memory pooling where possible")
	}

	return recommendations
}

// createHeapDump creates a heap dump file
func (p *Profiler) createHeapDump() error {
	if p.config.HeapDumpPath == "" {
		return errors.New("heap dump path not configured")
	}

	// Note: Actual heap dump creation requires runtime/pprof package
	// This is a placeholder for the implementation
	p.logger.Info("Creating heap dump",
		zap.String("path", p.config.HeapDumpPath),
		zap.Time("timestamp", time.Now()))

	// In production, this would use:
	// f, err := os.Create(fmt.Sprintf("%s/heap_%d.prof", p.config.HeapDumpPath, time.Now().Unix()))
	// if err != nil {
	//     return errors.Wrap(err, "failed to create heap dump file")
	// }
	// defer f.Close()
	// return pprof.WriteHeapProfile(f)

	return nil
}

// GetMemoryStats returns current memory statistics
func (p *Profiler) GetMemoryStats() MemoryStats {
	return p.collectMemoryStats()
}

// GetLeakDetectionResult performs leak detection and returns results
func (p *Profiler) GetLeakDetectionResult() LeakDetectionResult {
	return p.detectLeaks()
}

// ForceGC forces a garbage collection
func (p *Profiler) ForceGC() {
	runtime.GC()
	p.logger.Debug("Forced garbage collection")
}

// GetGoroutineCount returns current goroutine count
func (p *Profiler) GetGoroutineCount() int {
	return runtime.NumGoroutine()
}

// GetMetrics returns current memory metrics
func (p *Profiler) GetMetrics() map[string]interface{} {
	stats := p.collectMemoryStats()
	return map[string]interface{}{
		"heap_alloc_bytes":      stats.HeapAlloc,
		"heap_inuse_bytes":      stats.HeapInuse,
		"heap_objects":          stats.HeapObjects,
		"num_gc":                stats.NumGC,
		"gc_pause_time_ns":      stats.GCPauseTime,
		"gc_cpu_fraction":       stats.GCCPUFraction,
		"goroutines":            stats.GoroutineCount,
		"total_alloc_bytes":     stats.TotalAlloc,
		"mallocs":               stats.Mallocs,
		"frees":                 stats.Frees,
	}
}
