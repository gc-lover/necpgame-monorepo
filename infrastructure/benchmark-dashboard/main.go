// Issue: Benchmark dashboard
package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"time"
)

const (
	resultsDir = ".benchmarks/results"
	port       = "8080"
)

var (
	// Try multiple paths for results directory
	resultsDirs = []string{
		".benchmarks/results",
		"../../.benchmarks/results",
		filepath.Join(".", ".benchmarks", "results"),
	}
)

type BenchmarkResult struct {
	Timestamp string `json:"timestamp"`
	Services  []struct {
		Service    string `json:"service"`
		Benchmarks []struct {
			Name        string  `json:"name"`
			NsPerOp     float64 `json:"ns_per_op"`
			AllocsPerOp int     `json:"allocs_per_op"`
			BytesPerOp  int     `json:"bytes_per_op"`
		} `json:"benchmarks"`
	} `json:"services"`
}

type DashboardData struct {
	Runs       []RunInfo                   `json:"runs"`
	Services   []string                    `json:"services"`
	Benchmarks map[string][]BenchmarkPoint `json:"benchmarks"`
}

type RunInfo struct {
	Timestamp string `json:"timestamp"`
	Date      string `json:"date"`
	File      string `json:"file"`
}

type BenchmarkPoint struct {
	Timestamp string  `json:"timestamp"`
	Service   string  `json:"service"`
	Benchmark string  `json:"benchmark"`
	NsPerOp   float64 `json:"ns_per_op"`
	Allocs    int     `json:"allocs_per_op"`
	Bytes     int     `json:"bytes_per_op"`
}

func main() {
	// API endpoints first (before catch-all)
	http.HandleFunc("/api/data", handleData)
	http.HandleFunc("/api/runs", handleRuns)
	http.HandleFunc("/api/services", handleServices)
	http.HandleFunc("/api/trends", handleTrends)
	http.HandleFunc("/api/summary", handleSummary)
	http.HandleFunc("/api/run/", handleRun)
	// Catch-all for HTML
	http.HandleFunc("/", handleIndex)

	log.Printf("Benchmark Dashboard starting on :%s", port)
	log.Printf("Reading results from: %s", getResultsDir())

	if err := http.ListenAndServe(":"+port, nil); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}

func handleIndex(w http.ResponseWriter, r *http.Request) {
	// Try multiple paths for HTML file
	htmlPaths := []string{
		"dashboard.html",
		"infrastructure/benchmark-dashboard/dashboard.html",
		filepath.Join(".", "dashboard.html"),
		filepath.Join("infrastructure", "benchmark-dashboard", "dashboard.html"),
	}

	var data []byte
	var err error
	for _, htmlPath := range htmlPaths {
		data, err = os.ReadFile(htmlPath)
		if err == nil {
			break
		}
	}

	if err != nil {
		http.Error(w, "Dashboard HTML not found: "+err.Error(), http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	w.Write(data)
}

func handleData(w http.ResponseWriter, r *http.Request) {
	data, err := loadAllData()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(data)
}

func handleRuns(w http.ResponseWriter, r *http.Request) {
	runs, err := listRuns()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(runs)
}

func handleRun(w http.ResponseWriter, r *http.Request) {
	filename := strings.TrimPrefix(r.URL.Path, "/api/run/")
	if filename == "" {
		http.Error(w, "filename required", http.StatusBadRequest)
		return
	}

	dir := getResultsDir()
	fullPath := filepath.Join(dir, filename)
	data, err := os.ReadFile(fullPath)
	if err != nil {
		http.Error(w, err.Error(), http.StatusNotFound)
		return
	}

	// Remove BOM if present
	data = bytes.TrimPrefix(data, []byte{0xEF, 0xBB, 0xBF})

	var result BenchmarkResult
	if err := json.Unmarshal(data, &result); err != nil {
		http.Error(w, fmt.Sprintf("JSON parse error: %v (file: %s)", err, filename), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func getResultsDir() string {
	for _, dir := range resultsDirs {
		if info, err := os.Stat(dir); err == nil && info.IsDir() {
			log.Printf("Using results directory: %s", dir)
			return dir
		}
	}
	log.Printf("Warning: results directory not found, using default: %s", resultsDir)
	return resultsDir
}

func listRuns() ([]RunInfo, error) {
	dir := getResultsDir()
	entries, err := os.ReadDir(dir)
	if err != nil {
		return nil, err
	}

	var runs []RunInfo
	for _, entry := range entries {
		if entry.IsDir() || !strings.HasSuffix(entry.Name(), ".json") {
			continue
		}

		if !strings.HasPrefix(entry.Name(), "benchmarks_") {
			continue
		}

		timestamp := strings.TrimPrefix(strings.TrimSuffix(entry.Name(), ".json"), "benchmarks_")
		date, _ := parseTimestamp(timestamp)

		runs = append(runs, RunInfo{
			Timestamp: timestamp,
			Date:      date,
			File:      entry.Name(),
		})
	}

	sort.Slice(runs, func(i, j int) bool {
		return runs[i].Timestamp > runs[j].Timestamp
	})

	return runs, nil
}

func loadAllData() (*DashboardData, error) {
	runs, err := listRuns()
	if err != nil {
		log.Printf("Failed to list runs: %v", err)
		return nil, err
	}

	log.Printf("Found %d runs", len(runs))

	serviceMap := make(map[string]bool)
	benchmarkMap := make(map[string][]BenchmarkPoint)

	dir := getResultsDir()
	log.Printf("Loading data from directory: %s", dir)

	for _, run := range runs {
		fullPath := filepath.Join(dir, run.File)
		log.Printf("Reading file: %s", fullPath)

		data, err := os.ReadFile(fullPath)
		if err != nil {
			log.Printf("Failed to read %s: %v", fullPath, err)
			continue
		}

		// Remove BOM if present
		data = bytes.TrimPrefix(data, []byte{0xEF, 0xBB, 0xBF})

		var result BenchmarkResult
		if err := json.Unmarshal(data, &result); err != nil {
			log.Printf("Failed to parse JSON %s: %v", run.File, err)
			continue
		}

		log.Printf("Loaded run %s: %d services", run.File, len(result.Services))

		for _, svc := range result.Services {
			serviceMap[svc.Service] = true
			log.Printf("  Service: %s, benchmarks: %d", svc.Service, len(svc.Benchmarks))

			for _, bench := range svc.Benchmarks {
				key := fmt.Sprintf("%s/%s", svc.Service, bench.Name)
				benchmarkMap[key] = append(benchmarkMap[key], BenchmarkPoint{
					Timestamp: run.Timestamp,
					Service:   svc.Service,
					Benchmark: bench.Name,
					NsPerOp:   bench.NsPerOp,
					Allocs:    bench.AllocsPerOp,
					Bytes:     bench.BytesPerOp,
				})
			}
		}
	}

	services := make([]string, 0, len(serviceMap))
	for svc := range serviceMap {
		services = append(services, svc)
	}
	sort.Strings(services)

	for key := range benchmarkMap {
		sort.Slice(benchmarkMap[key], func(i, j int) bool {
			return benchmarkMap[key][i].Timestamp < benchmarkMap[key][j].Timestamp
		})
	}

	return &DashboardData{
		Runs:       runs,
		Services:   services,
		Benchmarks: benchmarkMap,
	}, nil
}

func parseTimestamp(ts string) (string, error) {
	if len(ts) < 8 {
		return ts, nil
	}

	t, err := time.Parse("20060102_150405", ts)
	if err != nil {
		return ts, err
	}

	return t.Format("2006-01-02 15:04:05"), nil
}

// New endpoints for services dashboard

type ServiceStatus struct {
	Name           string             `json:"name"`
	Health         string             `json:"health"`
	BenchmarkTrend string             `json:"benchmark_trend"`
	LastBenchmark  float64            `json:"last_benchmark"`
	Metrics        map[string]float64 `json:"metrics"`
}

type Trend struct {
	Service   string          `json:"service"`
	Benchmark string          `json:"benchmark"`
	Direction string          `json:"direction"` // improving, degrading, stable
	Change    float64         `json:"change"`    // percent
	Points    []BenchmarkPoint `json:"points"`
}

type Summary struct {
	TotalServices     int     `json:"total_services"`
	TotalRuns         int     `json:"total_runs"`
	ServicesImproving int     `json:"services_improving"`
	ServicesDegrading int     `json:"services_degrading"`
	AvgPerformance    float64 `json:"avg_performance"`
}

func handleServices(w http.ResponseWriter, r *http.Request) {
	data, err := loadAllData()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	services := make(map[string]*ServiceStatus)
	for _, svc := range data.Services {
		if services[svc] == nil {
			services[svc] = &ServiceStatus{
				Name:    svc,
				Health:  "healthy",
				Metrics: make(map[string]float64),
			}
		}
	}

	// Calculate trends for each service
	trends := calculateTrends(data)
	for key, trend := range trends {
		parts := strings.Split(key, "/")
		if len(parts) >= 2 {
			svcName := parts[0]
			if services[svcName] != nil {
				services[svcName].BenchmarkTrend = trend.Direction
				if len(trend.Points) > 0 {
					services[svcName].LastBenchmark = trend.Points[len(trend.Points)-1].NsPerOp
				}
			}
		}
	}

	result := make([]ServiceStatus, 0, len(services))
	for _, svc := range services {
		result = append(result, *svc)
	}

	sort.Slice(result, func(i, j int) bool {
		return result[i].Name < result[j].Name
	})

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func handleTrends(w http.ResponseWriter, r *http.Request) {
	data, err := loadAllData()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	trends := calculateTrends(data)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(trends)
}

func handleSummary(w http.ResponseWriter, r *http.Request) {
	data, err := loadAllData()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	trends := calculateTrends(data)

	summary := Summary{
		TotalServices: len(data.Services),
		TotalRuns:     len(data.Runs),
	}

	var totalPerf float64
	var perfCount int

	for _, trend := range trends {
		if trend.Direction == "improving" {
			summary.ServicesImproving++
		} else if trend.Direction == "degrading" {
			summary.ServicesDegrading++
		}

		if len(trend.Points) > 0 {
			totalPerf += trend.Points[len(trend.Points)-1].NsPerOp
			perfCount++
		}
	}

	if perfCount > 0 {
		summary.AvgPerformance = totalPerf / float64(perfCount)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(summary)
}

func calculateTrends(data *DashboardData) map[string]Trend {
	trends := make(map[string]Trend)

	for key, points := range data.Benchmarks {
		if len(points) < 2 {
			continue
		}

		first := points[0].NsPerOp
		last := points[len(points)-1].NsPerOp

		change := ((last - first) / first) * 100
		direction := "stable"
		if change > 5 {
			direction = "degrading" // Higher ns/op = worse
		} else if change < -5 {
			direction = "improving" // Lower ns/op = better
		}

		parts := strings.Split(key, "/")
		service := ""
		benchmark := key
		if len(parts) >= 2 {
			service = parts[0]
			benchmark = strings.Join(parts[1:], "/")
		}

		trends[key] = Trend{
			Service:   service,
			Benchmark: benchmark,
			Direction: direction,
			Change:    change,
			Points:    points,
		}
	}

	return trends
}
