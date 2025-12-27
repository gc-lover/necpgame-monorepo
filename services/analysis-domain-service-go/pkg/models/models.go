// Issue: #implement-analysis-domain-service
// Models for Analysis Domain Service - Enterprise-grade data structures

package models

import (
	"time"
)

// NetworkMetrics represents network performance metrics
type NetworkMetrics struct {
	Region            string    `json:"region" db:"region"`
	AverageLatencyMs  float64   `json:"average_latency_ms" db:"average_latency_ms"`
	PeakLatencyMs     float64   `json:"peak_latency_ms" db:"peak_latency_ms"`
	PacketLossPercent float64   `json:"packet_loss_percent" db:"packet_loss_percent"`
	BandwidthMbps     float64   `json:"bandwidth_mbps" db:"bandwidth_mbps"`
	Timestamp         time.Time `json:"timestamp" db:"timestamp"`
}

// PlayerBehaviorMetrics represents aggregated player behavior data
type PlayerBehaviorMetrics struct {
	ActiveUsers       int     `json:"active_users" db:"active_users"`
	SessionDuration   float64 `json:"session_duration" db:"session_duration"`   // minutes
	RetentionRate     float64 `json:"retention_rate" db:"retention_rate"`       // percentage
	ChurnRate         float64 `json:"churn_rate" db:"churn_rate"`               // percentage
	EngagementScore   float64 `json:"engagement_score" db:"engagement_score"`   // 0-100
	ConversionRate    float64 `json:"conversion_rate" db:"conversion_rate"`     // percentage
	Period            string  `json:"period" db:"period"`                       // "daily", "weekly", "monthly"
	Timestamp         time.Time `json:"timestamp" db:"timestamp"`
}

// SystemPerformance represents system performance metrics
type SystemPerformance struct {
	ServiceName     string    `json:"service_name" db:"service_name"`
	CPUUsage        float64   `json:"cpu_usage" db:"cpu_usage"`               // percentage
	MemoryUsage     float64   `json:"memory_usage" db:"memory_usage"`         // percentage
	DiskUsage       float64   `json:"disk_usage" db:"disk_usage"`             // percentage
	NetworkIO       float64   `json:"network_io" db:"network_io"`             // Mbps
	ResponseTime    float64   `json:"response_time" db:"response_time"`       // milliseconds
	ErrorRate       float64   `json:"error_rate" db:"error_rate"`             // percentage
	ActiveRequests  int       `json:"active_requests" db:"active_requests"`
	Timestamp       time.Time `json:"timestamp" db:"timestamp"`
}

// ResearchInsight represents research findings and insights
type ResearchInsight struct {
	ID              string    `json:"id" db:"id"`
	Topic           string    `json:"topic" db:"topic"`
	Category        string    `json:"category" db:"category"`
	Insight         string    `json:"insight" db:"insight"`
	Confidence      float64   `json:"confidence" db:"confidence"`           // 0-1
	DataPoints      int       `json:"data_points" db:"data_points"`
	Impact          string    `json:"impact" db:"impact"`
	Recommendations []string  `json:"recommendations" db:"recommendations"` // JSON array
	Status          string    `json:"status" db:"status"`                   // "draft", "validated", "implemented"
	CreatedAt       time.Time `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time `json:"updated_at" db:"updated_at"`
}

// SecurityThreat represents security threat analysis
type SecurityThreat struct {
	ID              string    `json:"id" db:"id"`
	Type            string    `json:"type" db:"type"`                       // "ddos", "injection", "unauthorized_access"
	Severity        string    `json:"severity" db:"severity"`               // "low", "medium", "high", "critical"
	Description     string    `json:"description" db:"description"`
	Status          string    `json:"status" db:"status"`                   // "detected", "investigating", "mitigated", "resolved"
	SourceIP        string    `json:"source_ip" db:"source_ip"`
	AffectedSystems []string  `json:"affected_systems" db:"affected_systems"` // JSON array
	DetectedAt      time.Time `json:"detected_at" db:"detected_at"`
	ResolvedAt      *time.Time `json:"resolved_at" db:"resolved_at"`
}

// NetworkBottleneck represents network performance bottleneck
type NetworkBottleneck struct {
	ID              string    `json:"id" db:"id"`
	Component       string    `json:"component" db:"component"`
	Severity        string    `json:"severity" db:"severity"`               // "low", "medium", "high"
	Description     string    `json:"description" db:"description"`
	Impact          string    `json:"impact" db:"impact"`
	CurrentValue    float64   `json:"current_value" db:"current_value"`
	ThresholdValue  float64   `json:"threshold_value" db:"threshold_value"`
	Recommendations []string  `json:"recommendations" db:"recommendations"` // JSON array
	Status          string    `json:"status" db:"status"`                   // "active", "resolved", "monitoring"
	DetectedAt      time.Time `json:"detected_at" db:"detected_at"`
	ResolvedAt      *time.Time `json:"resolved_at" db:"resolved_at"`
}

// ScalabilityAnalysis represents system scalability analysis
type ScalabilityAnalysis struct {
	ServiceName      string    `json:"service_name" db:"service_name"`
	CurrentLoad      float64   `json:"current_load" db:"current_load"`           // percentage
	MaxCapacity      float64   `json:"max_capacity" db:"max_capacity"`
	BottleneckPoint  string    `json:"bottleneck_point" db:"bottleneck_point"`
	ScalingFactor    float64   `json:"scaling_factor" db:"scaling_factor"`
	Recommendations  []string  `json:"recommendations" db:"recommendations"`     // JSON array
	RiskLevel        string    `json:"risk_level" db:"risk_level"`               // "low", "medium", "high"
	Timestamp        time.Time `json:"timestamp" db:"timestamp"`
}

// HypothesisTest represents A/B test or hypothesis validation
type HypothesisTest struct {
	ID           string                 `json:"id" db:"id"`
	Hypothesis   string                 `json:"hypothesis" db:"hypothesis"`
	Type         string                 `json:"type" db:"type"`                     // "ab_test", "feature_flag", "cohort_analysis"
	Status       string                 `json:"status" db:"status"`                 // "planned", "running", "completed", "failed"
	TestData     map[string]interface{} `json:"test_data" db:"test_data"`           // JSON object
	Results      map[string]interface{} `json:"results" db:"results"`               // JSON object
	Confidence   float64                `json:"confidence" db:"confidence"`         // 0-1
	PValue       float64                `json:"p_value" db:"p_value"`
	Conclusion   string                 `json:"conclusion" db:"conclusion"`
	StartedAt    time.Time              `json:"started_at" db:"started_at"`
	CompletedAt  *time.Time             `json:"completed_at" db:"completed_at"`
}
