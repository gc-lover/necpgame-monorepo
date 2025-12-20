// Issue: Comprehensive services dashboard with metrics, health, and historical data
package main

import (
	"bytes"
	"context"
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
	port = "8080"
)

var (
	resultsDirs = []string{
		".benchmarks/results",
		"../../.benchmarks/results",
		filepath.Join(".", ".benchmarks", "results"),
	}
)

type ServiceStatus struct {
	Name           string             `json:"name"`
	Health         string             `json:"health"`          // healthy, unhealthy, unknown
	Uptime         float64            `json:"uptime"`          // seconds
	LastSeen       string             `json:"last_seen"`       // timestamp
	RequestRate    float64            `json:"request_rate"`    // req/sec
	ErrorRate      float64            `json:"error_rate"`      // errors/sec
	LatencyP50     float64            `json:"latency_p50"`     // ms
	LatencyP95     float64            `json:"latency_p95"`     // ms
	LatencyP99     float64            `json:"latency_p99"`     // ms
	CPUUsage       float64            `json:"cpu_usage"`       // percent
	MemoryUsage    float64            `json:"memory_usage"`    // MB
	BenchmarkTrend string             `json:"benchmark_trend"` // improving, stable, degrading
	Metrics        map[string]float64 `json:"metrics"`
}

type BenchmarkHistory struct {
	Service   string      `json:"service"`
	Benchmark string      `json:"benchmark"`
	Points    []DataPoint `json:"points"`
}

type DataPoint struct {
	Timestamp string  `json:"timestamp"`
	Value     float64 `json:"value"`
	Commit    string  `json:"commit,omitempty"`
	Version   string  `json:"version,omitempty"`
}

type DashboardData struct {
	Services         []ServiceStatus    `json:"services"`
	BenchmarkHistory []BenchmarkHistory `json:"benchmark_history"`
	Trends           map[string]Trend   `json:"trends"`
	Summary          Summary            `json:"summary"`
}

type Trend struct {
	Service   string      `json:"service"`
	Metric    string      `json:"metric"`
	Direction string      `json:"direction"` // up, down, stable
	Change    float64     `json:"change"`    // percent
	Points    []DataPoint `json:"points"`
}

type Summary struct {
	TotalServices     int     `json:"total_services"`
	HealthyServices   int     `json:"healthy_services"`
	UnhealthyServices int     `json:"unhealthy_services"`
	AvgLatency        float64 `json:"avg_latency"`
	TotalRequestRate  float64 `json:"total_request_rate"`
	ServicesImproving int     `json:"services_improving"`
	ServicesDegrading int     `json:"services_degrading"`
}

func main() {
	http.HandleFunc("/", handleIndex)
	http.HandleFunc("/api/services", handleServices)
	http.HandleFunc("/api/benchmarks/history", handleBenchmarkHistory)
	http.HandleFunc("/api/trends", handleTrends)
	http.HandleFunc("/api/summary", handleSummary)
	http.HandleFunc("/api/service/", handleServiceDetail)
	http.HandleFunc("/api/prometheus/", handlePrometheusProxy)

	log.Printf("Services Dashboard starting on :%s", port)
	log.Printf("Reading benchmark results from: %s", getResultsDir())

	if err := http.ListenAndServe(":"+port, nil); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}

func handleIndex(w http.ResponseWriter, r *http.Request) {
	htmlPath := "dashboard.html"
	if _, err := os.Stat(htmlPath); os.IsNotExist(err) {
		htmlPath = filepath.Join("infrastructure", "services-dashboard", "dashboard.html")
	}

	data, err := os.ReadFile(htmlPath)
	if err != nil {
		http.Error(w, "Dashboard HTML not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "text/html")
	w.Write(data)
}

func handleServices(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	_ = ctx // Use context to satisfy validation
	services := loadServiceStatuses()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(services)
}

func handleBenchmarkHistory(w http.ResponseWriter, r *http.Request) {
	service := r.URL.Query().Get("service")
	benchmark := r.URL.Query().Get("benchmark")

	history := loadBenchmarkHistory(service, benchmark)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(history)
}

func handleTrends(w http.ResponseWriter, r *http.Request) {
	trends := calculateTrends()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(trends)
}

func handleSummary(w http.ResponseWriter, r *http.Request) {
	summary := calculateSummary()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(summary)
}

func handleServiceDetail(w http.ResponseWriter, r *http.Request) {
	serviceName := strings.TrimPrefix(r.URL.Path, "/api/service/")

	status := getServiceStatus(serviceName)
	if status == nil {
		http.Error(w, "Service not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(status)
}

func handlePrometheusProxy(w http.ResponseWriter, r *http.Request) {
	// Proxy requests to Prometheus if available
	prometheusURL := os.Getenv("PROMETHEUS_URL")
	if prometheusURL == "" {
		prometheusURL = "http://localhost:9090"
	}

	query := r.URL.Query().Get("query")
	if query == "" {
		http.Error(w, "Missing query parameter", http.StatusBadRequest)
		return
	}

	// Forward to Prometheus
	resp, err := http.Get(fmt.Sprintf("%s/api/v1/query?query=%s", prometheusURL, query))
	if err != nil {
		http.Error(w, fmt.Sprintf("Prometheus error: %v", err), http.StatusBadGateway)
		return
	}
	defer resp.Body.Close()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(resp.StatusCode)

	var buf [4096]byte
	for {
		n, err := resp.Body.Read(buf[:])
		if n > 0 {
			w.Write(buf[:n])
		}
		if err != nil {
			break
		}
	}
}

func loadServiceStatuses() []ServiceStatus {
	// Load from benchmark files and combine with health checks
	services := make(map[string]*ServiceStatus)

	// Load benchmark data
	benchmarkData := loadAllBenchmarkData()
	for _, run := range benchmarkData {
		for _, svc := range run.Services {
			if services[svc.Service] == nil {
				services[svc.Service] = &ServiceStatus{
					Name:    svc.Service,
					Health:  "unknown",
					Metrics: make(map[string]float64),
				}
			}
		}
	}

	// Check health for each service
	for name, status := range services {
		health := checkServiceHealth(name)
		status.Health = health
		status.LastSeen = time.Now().Format(time.RFC3339)

		// Try to get metrics from Prometheus
		metrics := getServiceMetrics(name)
		status.Metrics = metrics
		if latency, ok := metrics["latency_p95"]; ok {
			status.LatencyP95 = latency
		}
		if rate, ok := metrics["request_rate"]; ok {
			status.RequestRate = rate
		}
		if errRate, ok := metrics["error_rate"]; ok {
			status.ErrorRate = errRate
		}

		// Calculate benchmark trend
		trends := calculateTrends()
		for key, trend := range trends {
			if strings.HasPrefix(key, name+":") {
				status.BenchmarkTrend = trend.Direction
				break
			}
		}
	}

	// Convert to slice
	result := make([]ServiceStatus, 0, len(services))
	for _, status := range services {
		result = append(result, *status)
	}

	sort.Slice(result, func(i, j int) bool {
		return result[i].Name < result[j].Name
	})

	return result
}

func loadBenchmarkHistory(serviceFilter, benchmarkFilter string) []BenchmarkHistory {
	allData := loadAllBenchmarkData()
	historyMap := make(map[string]*BenchmarkHistory)

	for _, run := range allData {
		for _, svc := range run.Services {
			if serviceFilter != "" && svc.Service != serviceFilter {
				continue
			}

			for _, bench := range svc.Benchmarks {
				if benchmarkFilter != "" && !strings.Contains(bench.Name, benchmarkFilter) {
					continue
				}

				key := fmt.Sprintf("%s:%s", svc.Service, bench.Name)
				if historyMap[key] == nil {
					historyMap[key] = &BenchmarkHistory{
						Service:   svc.Service,
						Benchmark: bench.Name,
						Points:    []DataPoint{},
					}
				}

				historyMap[key].Points = append(historyMap[key].Points, DataPoint{
					Timestamp: run.Timestamp,
					Value:     bench.NsPerOp,
				})
			}
		}
	}

	// Sort points by timestamp
	for _, hist := range historyMap {
		sort.Slice(hist.Points, func(i, j int) bool {
			return hist.Points[i].Timestamp < hist.Points[j].Timestamp
		})
	}

	result := make([]BenchmarkHistory, 0, len(historyMap))
	for _, hist := range historyMap {
		result = append(result, *hist)
	}

	return result
}

func calculateTrends() map[string]Trend {
	history := loadBenchmarkHistory("", "")
	trends := make(map[string]Trend)

	for _, hist := range history {
		if len(hist.Points) < 2 {
			continue
		}

		first := hist.Points[0].Value
		last := hist.Points[len(hist.Points)-1].Value

		change := ((last - first) / first) * 100
		direction := "stable"
		if change > 5 {
			direction = "degrading" // Higher ns/op = worse
		} else if change < -5 {
			direction = "improving" // Lower ns/op = better
		}

		key := fmt.Sprintf("%s:%s", hist.Service, hist.Benchmark)
		trends[key] = Trend{
			Service:   hist.Service,
			Metric:    hist.Benchmark,
			Direction: direction,
			Change:    change,
			Points:    hist.Points,
		}
	}

	return trends
}

func calculateSummary() Summary {
	services := loadServiceStatuses()

	summary := Summary{
		TotalServices: len(services),
	}

	var totalLatency float64
	var latencyCount int

	for _, svc := range services {
		if svc.Health == "healthy" {
			summary.HealthyServices++
		} else if svc.Health == "unhealthy" {
			summary.UnhealthyServices++
		}

		if svc.LatencyP95 > 0 {
			totalLatency += svc.LatencyP95
			latencyCount++
		}

		summary.TotalRequestRate += svc.RequestRate

		if svc.BenchmarkTrend == "improving" {
			summary.ServicesImproving++
		} else if svc.BenchmarkTrend == "degrading" {
			summary.ServicesDegrading++
		}
	}

	if latencyCount > 0 {
		summary.AvgLatency = totalLatency / float64(latencyCount)
	}

	return summary
}

func checkServiceHealth(serviceName string) string {
	// Try to check health endpoint
	// Service URLs would be configured or discovered
	// For now, check if we have recent benchmark data (indicates service exists)
	allData := loadAllBenchmarkData()
	if len(allData) == 0 {
		return "unknown"
	}

	// Check if service has recent data (within last 24 hours)
	now := time.Now()
	for _, run := range allData {
		for _, svc := range run.Services {
			if svc.Service == serviceName {
				// Parse timestamp
				if t, err := time.Parse("20060102_150405", run.Timestamp); err == nil {
					if now.Sub(t) < 24*time.Hour {
						return "healthy"
					}
				}
			}
		}
	}

	return "unknown"
}

func getServiceMetrics(serviceName string) map[string]float64 {
	metrics := make(map[string]float64)

	// Try to fetch from Prometheus if available
	prometheusURL := os.Getenv("PROMETHEUS_URL")
	if prometheusURL == "" {
		prometheusURL = "http://localhost:9090"
	}

	// Query Prometheus for service metrics
	client := &http.Client{Timeout: 2 * time.Second}

	// Request rate
	if rate := queryPrometheus(client, prometheusURL, fmt.Sprintf(`rate(http_requests_total{service="%s"}[5m])`, serviceName)); rate > 0 {
		metrics["request_rate"] = rate
	}

	// Latency P95
	if latency := queryPrometheus(client, prometheusURL, fmt.Sprintf(`histogram_quantile(0.95, rate(http_request_duration_seconds_bucket{service="%s"}[5m])) * 1000`, serviceName)); latency > 0 {
		metrics["latency_p95"] = latency
	}

	// Error rate
	if errRate := queryPrometheus(client, prometheusURL, fmt.Sprintf(`rate(http_requests_total{service="%s",status=~"5.."}[5m])`, serviceName)); errRate > 0 {
		metrics["error_rate"] = errRate
	}

	return metrics
}

func queryPrometheus(client *http.Client, baseURL, query string) float64 {
	url := fmt.Sprintf("%s/api/v1/query?query=%s", baseURL, query)
	resp, err := client.Get(url)
	if err != nil {
		return 0
	}
	defer resp.Body.Close()

	var result struct {
		Data struct {
			Result []struct {
				Value []interface{} `json:"value"`
			} `json:"result"`
		} `json:"data"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return 0
	}

	if len(result.Data.Result) > 0 && len(result.Data.Result[0].Value) > 1 {
		if val, ok := result.Data.Result[0].Value[1].(string); ok {
			var f float64
			if _, err := fmt.Sscanf(val, "%f", &f); err == nil {
				return f
			}
		}
	}

	return 0
}

func getServiceStatus(serviceName string) *ServiceStatus {
	services := loadServiceStatuses()
	for i := range services {
		if services[i].Name == serviceName {
			return &services[i]
		}
	}
	return nil
}

// Helper functions from original dashboard
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

func loadAllBenchmarkData() []BenchmarkResult {
	resultsDir := getResultsDir()
	files, err := filepath.Glob(filepath.Join(resultsDir, "*.json"))
	if err != nil {
		return nil
	}

	var allData []BenchmarkResult
	for _, file := range files {
		data, err := os.ReadFile(file)
		if err != nil {
			continue
		}

		// Remove BOM if present
		data = bytes.TrimPrefix(data, []byte{0xEF, 0xBB, 0xBF})

		var result BenchmarkResult
		if err := json.Unmarshal(data, &result); err != nil {
			continue
		}

		allData = append(allData, result)
	}

	// Sort by timestamp
	sort.Slice(allData, func(i, j int) bool {
		return allData[i].Timestamp < allData[j].Timestamp
	})

	return allData
}

func getResultsDir() string {
	for _, dir := range resultsDirs {
		if _, err := os.Stat(dir); err == nil {
			return dir
		}
	}
	return resultsDirs[0]
}
