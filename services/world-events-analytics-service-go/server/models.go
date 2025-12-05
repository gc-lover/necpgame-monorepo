// Issue: #44
package server

import (
	"time"

	"github.com/google/uuid"
)

// EventMetrics - метрики события
type EventMetrics struct {
	EventID           uuid.UUID
	ParticipantCount  int
	CompletionRate    float64
	AverageDuration   int64
	TotalRewards      int64
	PlayerEngagement  float64
	RecordedAt        time.Time
}

// EventAnalytics - аналитика события
type EventAnalytics struct {
	EventID      uuid.UUID
	Type         string
	Scale        string
	StartTime    time.Time
	EndTime      *time.Time
	Metrics      *EventMetrics
	Aggregations map[string]interface{}
}















