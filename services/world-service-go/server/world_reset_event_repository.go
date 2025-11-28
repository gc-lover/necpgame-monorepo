package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
)

func (r *worldRepository) CreateResetEvent(ctx context.Context, event *models.ResetEvent) error {
	eventDataJSON, err := json.Marshal(event.EventData)
	if err != nil {
		return err
	}
	query := `
		INSERT INTO reset_events (id, event_type, reset_type, player_id, event_data, created_at)
		VALUES ($1, $2, $3, $4, $5, $6)
	`
	_, err = r.db.ExecContext(ctx, query, event.ID, event.EventType, event.ResetType, event.PlayerID, eventDataJSON, event.CreatedAt)
	return err
}

func (r *worldRepository) GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error) {
	query := `
		SELECT id, event_type, reset_type, player_id, event_data, created_at
		FROM reset_events
		WHERE 1=1
	`
	args := []interface{}{}
	
	if resetType != nil {
		query += ` AND reset_type = $1`
		args = append(args, *resetType)
	}
	
	query += fmt.Sprintf(` ORDER BY created_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()
	
	var events []models.ResetEvent
	for rows.Next() {
		var event models.ResetEvent
		var eventDataJSON []byte
		var resetTypeStr sql.NullString
		var playerIDStr sql.NullString
		
		if err := rows.Scan(&event.ID, &event.EventType, &resetTypeStr, &playerIDStr, &eventDataJSON, &event.CreatedAt); err != nil {
			continue
		}
		
		if resetTypeStr.Valid {
			rt := models.ResetType(resetTypeStr.String)
			event.ResetType = &rt
		}
		if playerIDStr.Valid {
			if pid, err := uuid.Parse(playerIDStr.String); err == nil {
				event.PlayerID = &pid
			}
		}
		if len(eventDataJSON) > 0 {
			if err := json.Unmarshal(eventDataJSON, &event.EventData); err != nil {
				continue
			}
		}
		
		events = append(events, event)
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM reset_events WHERE 1=1`
	if resetType != nil {
		countQuery += ` AND reset_type = $1`
		r.db.GetContext(ctx, &total, countQuery, *resetType)
	} else {
		r.db.GetContext(ctx, &total, countQuery)
	}
	
	return events, total, nil
}

