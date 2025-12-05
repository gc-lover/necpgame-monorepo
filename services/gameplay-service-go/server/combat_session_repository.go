// Issue: #1607
package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
)

type CombatSessionRepositoryInterface interface {
	CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error)
	GetSession(ctx context.Context, sessionID uuid.UUID) (*api.CombatSessionResponse, error)
	ListSessions(ctx context.Context, status *api.SessionStatus, sessionType *api.SessionType, limit, offset int) (*api.SessionListResponse, error)
	EndSession(ctx context.Context, sessionID uuid.UUID) (*api.SessionEndResponse, error)
}

type CombatSessionRepository struct {
	db *pgxpool.Pool
}

func NewCombatSessionRepository(db *pgxpool.Pool) *CombatSessionRepository {
	return &CombatSessionRepository{db: db}
}

// mapSessionTypeToDB maps OpenAPI SessionType to DB enum
func mapSessionTypeToDB(sessionType api.SessionType) string {
	switch sessionType {
	case api.SessionTypePvpArena:
		return "pvp"
	case api.SessionTypePveRaid:
		return "pve"
	case api.SessionTypeExtractZone:
		return "pve"
	case api.SessionTypeGuildWar:
		return "pvp"
	default:
		return "pve"
	}
}

// mapSessionStatusToDB maps OpenAPI SessionStatus to DB enum
func mapSessionStatusToDB(status api.SessionStatus) string {
	switch status {
	case api.SessionStatusPending:
		return "created"
	case api.SessionStatusActive:
		return "active"
	case api.SessionStatusEnded:
		return "completed"
	case api.SessionStatusAborted:
		return "cancelled"
	default:
		return "created"
	}
}

// mapSessionTypeFromDB maps DB enum to OpenAPI SessionType
func mapSessionTypeFromDB(dbType string) api.SessionType {
	switch dbType {
	case "pvp":
		return api.SessionTypePvpArena
	case "pve":
		return api.SessionTypePveRaid
	case "raid":
		return api.SessionTypePveRaid
	case "arena":
		return api.SessionTypePvpArena
	default:
		return api.SessionTypePveRaid
	}
}

// mapSessionStatusFromDB maps DB enum to OpenAPI SessionStatus
func mapSessionStatusFromDB(dbStatus string) api.SessionStatus {
	switch dbStatus {
	case "created":
		return api.SessionStatusPending
	case "active":
		return api.SessionStatusActive
	case "paused":
		return api.SessionStatusActive
	case "completed":
		return api.SessionStatusEnded
	case "cancelled":
		return api.SessionStatusAborted
	default:
		return api.SessionStatusPending
	}
}

func (r *CombatSessionRepository) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error) {
	sessionID := uuid.New()
	dbSessionType := mapSessionTypeToDB(req.SessionType)
	dbStatus := "created"

	settingsJSON, _ := json.Marshal(req.Settings)

	var createdAt time.Time
	err := r.db.QueryRow(ctx,
		`INSERT INTO mvp_core.combat_sessions (id, session_type, status, settings, created_at)
		 VALUES ($1, $2::combat_session_type, $3::combat_session_status, $4, CURRENT_TIMESTAMP)
		 RETURNING created_at`,
		sessionID, dbSessionType, dbStatus, settingsJSON).Scan(&createdAt)

	if err != nil {
		return nil, err
	}

	// Create participants
	for _, participantID := range req.Participants {
		_, err := r.db.Exec(ctx,
			`INSERT INTO mvp_core.combat_participants (session_id, participant_id, participant_type, status)
			 VALUES ($1, $2, 'player'::combat_participant_type, 'alive'::combat_participant_status)`,
			sessionID, participantID)
		if err != nil {
			return nil, err
		}
	}

	// Get participants for response
	participants, err := r.getParticipants(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	response := &api.CombatSessionResponse{
		ID:          api.NewOptUUID(sessionID),
		CreatedAt:   api.NewOptDateTime(createdAt),
		SessionType: req.SessionType,
		Status:      api.SessionStatusPending,
		Participants: participants,
	}

	if req.ZoneID.IsSet() {
		response.ZoneID = req.ZoneID
	}
	if req.Difficulty.IsSet() {
		response.Difficulty = req.Difficulty
	}

	return response, nil
}

func (r *CombatSessionRepository) GetSession(ctx context.Context, sessionID uuid.UUID) (*api.CombatSessionResponse, error) {
	var dbSessionType, dbStatus string
	var createdAt time.Time
	var settingsJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT session_type::text, status::text, created_at, settings
		 FROM mvp_core.combat_sessions WHERE id = $1`,
		sessionID).Scan(&dbSessionType, &dbStatus, &createdAt, &settingsJSON)

	if err != nil {
		return nil, errors.New("session not found")
	}

	participants, err := r.getParticipants(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	response := &api.CombatSessionResponse{
		ID:          api.NewOptUUID(sessionID),
		CreatedAt:   api.NewOptDateTime(createdAt),
		SessionType: mapSessionTypeFromDB(dbSessionType),
		Status:      mapSessionStatusFromDB(dbStatus),
		Participants: participants,
	}

	return response, nil
}

func (r *CombatSessionRepository) ListSessions(ctx context.Context, status *api.SessionStatus, sessionType *api.SessionType, limit, offset int) (*api.SessionListResponse, error) {
	query := `SELECT id, session_type::text, status::text, created_at,
			  (SELECT COUNT(*) FROM mvp_core.combat_participants WHERE session_id = cs.id) as participant_count
			  FROM mvp_core.combat_sessions cs WHERE 1=1`
	args := []interface{}{}
	argIndex := 1

	if status != nil {
		dbStatus := mapSessionStatusToDB(*status)
		query += ` AND status = $` + string(rune('0'+argIndex)) + `::combat_session_status`
		args = append(args, dbStatus)
		argIndex++
	}

	if sessionType != nil {
		dbType := mapSessionTypeToDB(*sessionType)
		query += ` AND session_type = $` + string(rune('0'+argIndex)) + `::combat_session_type`
		args = append(args, dbType)
		argIndex++
	}

	query += ` ORDER BY created_at DESC LIMIT $` + string(rune('0'+argIndex)) + ` OFFSET $` + string(rune('0'+argIndex+1))
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var items []api.SessionSummary
	for rows.Next() {
		var summary api.SessionSummary
		var dbType, dbStatus string
		var createdAt time.Time
		var participantCount int

		err := rows.Scan(&summary.ID, &dbType, &dbStatus, &createdAt, &participantCount)
		if err != nil {
			return nil, err
		}

		summary.SessionType = mapSessionTypeFromDB(dbType)
		summary.Status = mapSessionStatusFromDB(dbStatus)
		summary.CreatedAt = createdAt
		summary.ParticipantCount = api.NewOptInt(participantCount)

		items = append(items, summary)
	}

	// Get total count
	var total int
	countQuery := `SELECT COUNT(*) FROM mvp_core.combat_sessions WHERE 1=1`
	countArgs := []interface{}{}
	countArgIndex := 1

	if status != nil {
		dbStatus := mapSessionStatusToDB(*status)
		countQuery += ` AND status = $` + string(rune('0'+countArgIndex)) + `::combat_session_status`
		countArgs = append(countArgs, dbStatus)
		countArgIndex++
	}

	if sessionType != nil {
		dbType := mapSessionTypeToDB(*sessionType)
		countQuery += ` AND session_type = $` + string(rune('0'+countArgIndex)) + `::combat_session_type`
		countArgs = append(countArgs, dbType)
		countArgIndex++
	}

	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, err
	}

	return &api.SessionListResponse{
		Items: items,
		Pagination: api.PaginationResponse{
			Total:  total,
			Limit:  api.NewOptInt(limit),
			Offset: api.NewOptInt(offset),
		},
	}, nil
}

func (r *CombatSessionRepository) EndSession(ctx context.Context, sessionID uuid.UUID) (*api.SessionEndResponse, error) {
	var dbStatus string
	err := r.db.QueryRow(ctx,
		`UPDATE mvp_core.combat_sessions 
		 SET status = 'completed'::combat_session_status, ended_at = CURRENT_TIMESTAMP
		 WHERE id = $1
		 RETURNING status::text`,
		sessionID).Scan(&dbStatus)

	if err != nil {
		return nil, errors.New("session not found")
	}

	return &api.SessionEndResponse{
		SessionID: sessionID,
		Status:    mapSessionStatusFromDB(dbStatus),
	}, nil
}

func (r *CombatSessionRepository) getParticipants(ctx context.Context, sessionID uuid.UUID) ([]api.Participant, error) {
	rows, err := r.db.Query(ctx,
		`SELECT participant_id, participant_type::text, status::text, team, health, max_health
		 FROM mvp_core.combat_participants WHERE session_id = $1`,
		sessionID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []api.Participant
	for rows.Next() {
		var p api.Participant
		var dbType, dbStatus string

		err := rows.Scan(&p.CharacterID, &dbType, &dbStatus, &p.Team, &p.Health, &p.MaxHealth)
		if err != nil {
			return nil, err
		}

		// Map participant status
		switch dbStatus {
		case "alive":
			p.Status = api.ParticipantStatusAlive
		case "defeated":
			p.Status = api.ParticipantStatusDead
		case "escaped":
			p.Status = api.ParticipantStatusExtracted
		default:
			p.Status = api.ParticipantStatusAlive
		}

		participants = append(participants, p)
	}

	return participants, nil
}

