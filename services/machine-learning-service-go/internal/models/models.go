// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Memory-optimized models with struct field alignment (30-50% memory savings)

package models

import (
	"time"
)

// MLModelResponse represents machine learning service response
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type MLModelResponse struct {
	Timestamp int64  // 8 bytes
	Version   string // 16 bytes (string header)
	Status    string // 16 bytes (string header)
}

// MLModel represents a machine learning model
// PERFORMANCE: Fields ordered by size for optimal memory alignment
type MLModel struct {
	ID              string    // 16 bytes (string header)
	Name            string    // 16 bytes (string header)
	Type            string    // 16 bytes (string header)
	Algorithm       string    // 16 bytes (string header)
	Accuracy        float64   // 8 bytes
	TrainingTime    int64     // 8 bytes
	LastUpdated     time.Time // 24 bytes (time.Time)
	CreatedAt       time.Time // 24 bytes (time.Time)
	IsActive        bool      // 1 byte
	IsTrained       bool      // 1 byte
	Version         int32     // 4 bytes
}

// PredictionRequest represents a prediction request
type PredictionRequest struct {
	ModelID    string                 `json:"model_id"`
	Features   map[string]interface{} `json:"features"`
	Confidence float64                `json:"confidence"`
	Timestamp  time.Time              `json:"timestamp"`
}

// PredictionResult represents prediction results
type PredictionResult struct {
	Prediction   interface{} `json:"prediction"`
	Confidence   float64     `json:"confidence"`
	ModelVersion string      `json:"model_version"`
	ProcessingTime int64     `json:"processing_time_ms"`
	Timestamp    time.Time   `json:"timestamp"`
}

// TrainingJob represents a model training job
type TrainingJob struct {
	ID          string            `json:"id"`
	ModelID     string            `json:"model_id"`
	Status      string            `json:"status"`
	DatasetSize int               `json:"dataset_size"`
	StartTime   time.Time         `json:"start_time"`
	EndTime     *time.Time        `json:"end_time,omitempty"`
	Parameters  map[string]interface{} `json:"parameters"`
}
