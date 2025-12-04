// Issue: #44
package server

import (
	"time"

	"github.com/google/uuid"
)

// ScheduledEvent - запланированное событие
type ScheduledEvent struct {
	ID          uuid.UUID
	EventID     uuid.UUID
	ScheduledAt time.Time
	CronPattern string
	TriggerType string
	Enabled     bool
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

// TriggerRequest - запрос на ручной запуск события
type TriggerRequest struct {
	EventID     uuid.UUID
	TriggeredBy string
	Reason      string
}









