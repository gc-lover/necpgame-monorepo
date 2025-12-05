// Issue: #1615
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/sirupsen/logrus"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// mockCombatSessionService is a mock implementation of CombatSessionServiceInterface
type mockCombatSessionService struct {
	mock.Mock
}

func (m *mockCombatSessionService) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error) {
	args := m.Called(ctx, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.CombatSessionResponse), args.Error(1)
}

func (m *mockCombatSessionService) GetSession(ctx context.Context, sessionID uuid.UUID) (*api.CombatSessionResponse, error) {
	args := m.Called(ctx, sessionID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.CombatSessionResponse), args.Error(1)
}

func (m *mockCombatSessionService) ListSessions(ctx context.Context, status *api.SessionStatus, sessionType *api.SessionType, limit, offset int) (*api.SessionListResponse, error) {
	args := m.Called(ctx, status, sessionType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.SessionListResponse), args.Error(1)
}

func (m *mockCombatSessionService) EndSession(ctx context.Context, sessionID uuid.UUID) (*api.SessionEndResponse, error) {
	args := m.Called(ctx, sessionID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.SessionEndResponse), args.Error(1)
}

func TestHandlers_CreateCombatSession(t *testing.T) {
	mockService := new(mockCombatSessionService)
	logger := logrus.New()
	handlers := &Handlers{
		combatSessionService: mockService,
		logger:                logger,
	}

	sessionID := uuid.New()
	participantID := uuid.New()
	req := &api.CreateSessionRequest{
		SessionType: api.SessionTypePvpArena,
		Participants: []uuid.UUID{participantID},
	}

	expectedSession := &api.CombatSessionResponse{
		ID:          api.NewOptUUID(sessionID),
		SessionType: api.SessionTypePvpArena,
		Status:      api.SessionStatusPending,
		Participants: []api.Participant{
			{
				PlayerID:    uuid.Nil,
				CharacterID: participantID,
				Team:        api.ParticipantTeamSolo,
				Role:        api.ParticipantRoleAssault,
				Status:      api.ParticipantStatusAlive,
			},
		},
		CreatedAt: api.NewOptDateTime(time.Now()),
	}

	mockService.On("CreateSession", mock.Anything, req).Return(expectedSession, nil)

	ctx := context.Background()
	response, err := handlers.CreateCombatSession(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	mockService.AssertExpectations(t)
}

func TestHandlers_CreateCombatSession_NoService(t *testing.T) {
	logger := logrus.New()
	handlers := &Handlers{
		combatSessionService: nil,
		logger:                logger,
	}

	req := &api.CreateSessionRequest{
		SessionType: api.SessionTypePvpArena,
		Participants: []uuid.UUID{uuid.New()},
	}

	ctx := context.Background()
	response, err := handlers.CreateCombatSession(ctx, req)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	_, ok := response.(*api.CreateCombatSessionBadRequest)
	assert.True(t, ok, "Expected BadRequest when service is nil")
}

func TestHandlers_GetCombatSession(t *testing.T) {
	mockService := new(mockCombatSessionService)
	logger := logrus.New()
	handlers := &Handlers{
		combatSessionService: mockService,
		logger:                logger,
	}

	sessionID := uuid.New()
	participantID := uuid.New()
	expectedSession := &api.CombatSessionResponse{
		ID:          api.NewOptUUID(sessionID),
		SessionType: api.SessionTypePvpArena,
		Status:      api.SessionStatusActive,
		Participants: []api.Participant{
			{
				PlayerID:    uuid.Nil,
				CharacterID: participantID,
				Team:        api.ParticipantTeamSolo,
				Role:        api.ParticipantRoleAssault,
				Status:      api.ParticipantStatusAlive,
			},
		},
		CreatedAt: api.NewOptDateTime(time.Now()),
	}

	mockService.On("GetSession", mock.Anything, sessionID).Return(expectedSession, nil)

	ctx := context.Background()
	params := api.GetCombatSessionParams{
		SessionId: sessionID,
	}
	response, err := handlers.GetCombatSession(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	mockService.AssertExpectations(t)
}

func TestHandlers_ListCombatSessions(t *testing.T) {
	mockService := new(mockCombatSessionService)
	logger := logrus.New()
	handlers := &Handlers{
		combatSessionService: mockService,
		logger:                logger,
	}

	sessionID := uuid.New()
	status := api.SessionStatusActive
	sessionType := api.SessionTypePvpArena
	expectedResponse := &api.SessionListResponse{
		Items: []api.SessionSummary{
			{
				ID:               sessionID,
				SessionType:      sessionType,
				Status:           status,
				CreatedAt:        time.Now(),
				ParticipantCount: api.NewOptInt(1),
			},
		},
		Pagination: api.PaginationResponse{
			Total:  1,
			Limit:  api.NewOptInt(20),
			Offset: api.NewOptInt(0),
			HasMore: api.NewOptBool(false),
		},
	}

	mockService.On("ListSessions", mock.Anything, &status, &sessionType, 20, 0).Return(expectedResponse, nil)

	ctx := context.Background()
	params := api.ListCombatSessionsParams{
		Status:      api.NewOptSessionStatus(status),
		SessionType: api.NewOptSessionType(sessionType),
	}
	response, err := handlers.ListCombatSessions(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	mockService.AssertExpectations(t)
}

func TestHandlers_EndCombatSession(t *testing.T) {
	mockService := new(mockCombatSessionService)
	logger := logrus.New()
	handlers := &Handlers{
		combatSessionService: mockService,
		logger:                logger,
	}

	sessionID := uuid.New()
	expectedResponse := &api.SessionEndResponse{
		SessionID:  sessionID,
		Status:     api.SessionStatusEnded,
		WinnerTeam: api.OptNilString{},
		Rewards:    []api.Reward{},
	}

	mockService.On("EndSession", mock.Anything, sessionID).Return(expectedResponse, nil)

	ctx := context.Background()
	params := api.EndCombatSessionParams{
		SessionId: sessionID,
	}
	response, err := handlers.EndCombatSession(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	mockService.AssertExpectations(t)
}

