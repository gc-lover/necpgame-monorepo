// Issue: #44
package server

import (
	"database/sql"
	"time"

	"github.com/google/uuid"
)

// WorldEvent - внутренняя модель мирового события
type WorldEvent struct {
	ID          uuid.UUID
	Name        string
	Description string
	Type        string
	Scale       string
	Frequency   string
	Status      string
	StartTime   *time.Time
	EndTime     *time.Time
	Effects     []byte // JSON
	Triggers    []byte // JSON
	Constraints []byte // JSON
	Metadata    []byte // JSON
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

// EventActivation - внутренняя модель активации события
type EventActivation struct {
	EventID     uuid.UUID
	ActivatedAt time.Time
	ActivatedBy string
	Reason      string
}

// EventAnnouncement - внутренняя модель анонса события
type EventAnnouncement struct {
	EventID      uuid.UUID
	AnnouncedAt  time.Time
	AnnouncedBy  string
	Message      string
	Channels     []string
}

// EventFilter - фильтр для поиска событий
type EventFilter struct {
	Status    *string
	Type      *string
	Scale     *string
	Frequency *string
	Limit     int
	Offset    int
}

// NullTime - обертка для nullable time
type NullTime struct {
	sql.NullTime
}

func (nt *NullTime) MarshalJSON() ([]byte, error) {
	if !nt.Valid {
		return []byte("null"), nil
	}
	return nt.Time.MarshalJSON()
}

func (nt *NullTime) UnmarshalJSON(data []byte) error {
	if string(data) == "null" {
		nt.Valid = false
		return nil
	}
	nt.Valid = true
	return nt.Time.UnmarshalJSON(data)
}


























