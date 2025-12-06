package server

import (
	"context"
	"errors"
	"os"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// serviceTestContext creates a context with timeout for service tests
func serviceTestContext(t *testing.T) (context.Context, context.CancelFunc) {
	return context.WithTimeout(context.Background(), 10*time.Second)
}

type mockTicketRepository struct {
	mock.Mock
}

func (m *mockTicketRepository) Create(ctx context.Context, ticket *models.SupportTicket) error {
	args := m.Called(ctx, ticket)
	return args.Error(0)
}

func (m *mockTicketRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.SupportTicket), args.Error(1)
}

func (m *mockTicketRepository) GetByNumber(ctx context.Context, number string) (*models.SupportTicket, error) {
	args := m.Called(ctx, number)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.SupportTicket), args.Error(1)
}

func (m *mockTicketRepository) GetByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.SupportTicket, error) {
	args := m.Called(ctx, playerID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.SupportTicket), args.Error(1)
}

func (m *mockTicketRepository) GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]models.SupportTicket, error) {
	args := m.Called(ctx, agentID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.SupportTicket), args.Error(1)
}

func (m *mockTicketRepository) GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]models.SupportTicket, error) {
	args := m.Called(ctx, status, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.SupportTicket), args.Error(1)
}

func (m *mockTicketRepository) Update(ctx context.Context, ticket *models.SupportTicket) error {
	args := m.Called(ctx, ticket)
	return args.Error(0)
}

func (m *mockTicketRepository) CountByPlayerID(ctx context.Context, playerID uuid.UUID) (int, error) {
	args := m.Called(ctx, playerID)
	return args.Int(0), args.Error(1)
}

func (m *mockTicketRepository) CountByStatus(ctx context.Context, status models.TicketStatus) (int, error) {
	args := m.Called(ctx, status)
	return args.Int(0), args.Error(1)
}

func (m *mockTicketRepository) CreateResponse(ctx context.Context, response *models.TicketResponse) error {
	args := m.Called(ctx, response)
	return args.Error(0)
}

func (m *mockTicketRepository) GetResponsesByTicketID(ctx context.Context, ticketID uuid.UUID) ([]models.TicketResponse, error) {
	args := m.Called(ctx, ticketID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TicketResponse), args.Error(1)
}

func (m *mockTicketRepository) GetNextTicketNumber(ctx context.Context) (string, error) {
	args := m.Called(ctx)
	return args.String(0), args.Error(1)
}

func setupTestService(t *testing.T) (*TicketService, *mockTicketRepository, func()) {
	mockRepo := new(mockTicketRepository)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	// Use test Redis port if available (Docker Compose test)
	redisAddr := os.Getenv("REDIS_ADDR")
	if redisAddr == "" {
		redisAddr = "localhost:6379"
	}

	redisClient := redis.NewClient(&redis.Options{
		Addr:         redisAddr,
		DB:           1,
		DialTimeout:  2 * time.Second,
		ReadTimeout:  2 * time.Second,
		WriteTimeout: 2 * time.Second,
	})
	
	// Try to flush, but don't fail if Redis is unavailable
	_ = redisClient.FlushDB(ctx)

	service := &TicketService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	cleanup := func() {
		ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
		defer cancel()
		_ = redisClient.Close()
		_ = ctx // avoid unused variable
	}

	return service, mockRepo, cleanup
}

func TestTicketService_CreateTicket_Success(t *testing.T) {
	t.Parallel()
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	playerID := uuid.New()
	req := &models.CreateTicketRequest{
		Category:    models.TicketCategoryTechnical,
		Subject:     "Test Subject",
		Description: "Test Description",
	}

	mockRepo.On("GetNextTicketNumber", ctx).Return("TKT-20250101-0001", nil)
	mockRepo.On("Create", ctx, mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	result, err := service.CreateTicket(ctx, playerID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, models.TicketCategoryTechnical, result.Category)
	assert.Equal(t, models.TicketStatusOpen, result.Status)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_CreateTicket_GetNextTicketNumberError(t *testing.T) {
	t.Parallel()
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ctx, cancel := serviceTestContext(t)
	defer cancel()

	playerID := uuid.New()
	req := &models.CreateTicketRequest{
		Category:    models.TicketCategoryTechnical,
		Subject:     "Test Subject",
		Description: "Test Description",
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetNextTicketNumber", ctx).Return("", expectedErr)

	result, err := service.CreateTicket(ctx, playerID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicket_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	expectedTicket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    uuid.New(),
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(expectedTicket, nil)

	result, err := service.GetTicket(context.Background(), ticketID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, ticketID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicket_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	result, err := service.GetTicket(context.Background(), ticketID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByPlayerID_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	playerID := uuid.New()
	tickets := []models.SupportTicket{
		{
			ID:          uuid.New(),
			Number:      "TKT-20250101-0001",
			PlayerID:    playerID,
			Category:    models.TicketCategoryTechnical,
			Status:      models.TicketStatusOpen,
			Subject:     "Test Subject",
			Description: "Test Description",
		},
	}

	mockRepo.On("GetByPlayerID", context.Background(), playerID, 10, 0).Return(tickets, nil)
	mockRepo.On("CountByPlayerID", context.Background(), playerID).Return(1, nil)

	result, err := service.GetTicketsByPlayerID(context.Background(), playerID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_UpdateTicket_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Old Subject",
		Description: "Old Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	newSubject := "New Subject"
	req := &models.UpdateTicketRequest{
		Subject: &newSubject,
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	result, err := service.UpdateTicket(context.Background(), ticketID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, newSubject, result.Subject)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_UpdateTicket_StatusResolved(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	status := models.TicketStatusResolved
	req := &models.UpdateTicketRequest{
		Status: &status,
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	result, err := service.UpdateTicket(context.Background(), ticketID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.TicketStatusResolved, result.Status)
	assert.NotNil(t, result.ResolvedAt)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AssignTicket_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	agentID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	result, err := service.AssignTicket(context.Background(), ticketID, agentID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.AssignedAgentID)
	assert.Equal(t, agentID, *result.AssignedAgentID)
	assert.Equal(t, models.TicketStatusAssigned, result.Status)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AddResponse_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	authorID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	req := &models.AddResponseRequest{
		Message: "Test Response",
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("CreateResponse", context.Background(), mock.AnythingOfType("*models.TicketResponse")).Return(nil)

	result, err := service.AddResponse(context.Background(), ticketID, authorID, false, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, ticketID, result.TicketID)
	assert.Equal(t, authorID, result.AuthorID)
	assert.Equal(t, "Test Response", result.Message)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AddResponse_FirstAgentResponse(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	agentID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Status:      models.TicketStatusAssigned,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	req := &models.AddResponseRequest{
		Message: "Agent Response",
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("CreateResponse", context.Background(), mock.AnythingOfType("*models.TicketResponse")).Return(nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	result, err := service.AddResponse(context.Background(), ticketID, agentID, true, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketDetail_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	responses := []models.TicketResponse{
		{
			ID:       uuid.New(),
			TicketID: ticketID,
			AuthorID: playerID,
			Message:  "Test Response",
			CreatedAt: time.Now(),
		},
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("GetResponsesByTicketID", context.Background(), ticketID).Return(responses, nil)

	result, err := service.GetTicketDetail(context.Background(), ticketID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, ticketID, result.Ticket.ID)
	assert.Len(t, result.Responses, 1)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_RateTicket_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	playerID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TKT-20250101-0001",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Status:      models.TicketStatusResolved,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	rating := 5

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(nil)

	err := service.RateTicket(context.Background(), ticketID, rating)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_RateTicket_InvalidRating(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	rating := 10

	err := service.RateTicket(context.Background(), ticketID, rating)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByAgentID_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	agentID := uuid.New()
	tickets := []models.SupportTicket{
		{
			ID:          uuid.New(),
			Number:      "TKT-20250101-0001",
			Category:    models.TicketCategoryTechnical,
			Status:      models.TicketStatusAssigned,
			Subject:     "Test Subject",
			Description: "Test Description",
		},
	}

	mockRepo.On("GetByAgentID", context.Background(), agentID, 10, 0).Return(tickets, nil)

	result, err := service.GetTicketsByAgentID(context.Background(), agentID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 1)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByStatus_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	status := models.TicketStatusOpen
	tickets := []models.SupportTicket{
		{
			ID:          uuid.New(),
			Number:      "TKT-20250101-0001",
			Category:    models.TicketCategoryTechnical,
			Status:      status,
			Subject:     "Test Subject",
			Description: "Test Description",
		},
	}

	mockRepo.On("GetByStatus", context.Background(), status, 10, 0).Return(tickets, nil)
	mockRepo.On("CountByStatus", context.Background(), status).Return(1, nil)

	result, err := service.GetTicketsByStatus(context.Background(), status, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicket_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, expectedErr)

	result, err := service.GetTicket(context.Background(), ticketID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_UpdateTicket_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	req := &models.UpdateTicketRequest{
		Subject: stringPtr("New Subject"),
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	result, err := service.UpdateTicket(context.Background(), ticketID, req)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_CreateTicket_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	playerID := uuid.New()
	req := &models.CreateTicketRequest{
		Category:    models.TicketCategoryTechnical,
		Subject:     "Test Subject",
		Description: "Test Description",
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetNextTicketNumber", context.Background()).Return("TKT-20250101-0001", nil)
	mockRepo.On("Create", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(expectedErr)

	result, err := service.CreateTicket(context.Background(), playerID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByPlayerID_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	playerID := uuid.New()

	mockRepo.On("GetByPlayerID", context.Background(), playerID, 10, 0).Return([]models.SupportTicket{}, nil)
	mockRepo.On("CountByPlayerID", context.Background(), playerID).Return(0, nil)

	result, err := service.GetTicketsByPlayerID(context.Background(), playerID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 0)
	assert.Equal(t, 0, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByPlayerID_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	playerID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetByPlayerID", context.Background(), playerID, 10, 0).Return(nil, expectedErr)

	result, err := service.GetTicketsByPlayerID(context.Background(), playerID, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByAgentID_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	agentID := uuid.New()

	mockRepo.On("GetByAgentID", context.Background(), agentID, 10, 0).Return([]models.SupportTicket{}, nil)

	result, err := service.GetTicketsByAgentID(context.Background(), agentID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 0)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByAgentID_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	agentID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetByAgentID", context.Background(), agentID, 10, 0).Return(nil, expectedErr)

	result, err := service.GetTicketsByAgentID(context.Background(), agentID, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByStatus_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	mockRepo.On("GetByStatus", context.Background(), models.TicketStatusOpen, 10, 0).Return([]models.SupportTicket{}, nil)
	mockRepo.On("CountByStatus", context.Background(), models.TicketStatusOpen).Return(0, nil)

	result, err := service.GetTicketsByStatus(context.Background(), models.TicketStatusOpen, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Tickets, 0)
	assert.Equal(t, 0, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketsByStatus_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	expectedErr := errors.New("database error")

	mockRepo.On("GetByStatus", context.Background(), models.TicketStatusOpen, 10, 0).Return(nil, expectedErr)

	result, err := service.GetTicketsByStatus(context.Background(), models.TicketStatusOpen, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AssignTicket_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	agentID := uuid.New()

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	result, err := service.AssignTicket(context.Background(), ticketID, agentID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AssignTicket_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	agentID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		PlayerID:    uuid.New(),
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(expectedErr)

	result, err := service.AssignTicket(context.Background(), ticketID, agentID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AddResponse_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	authorID := uuid.New()
	req := &models.AddResponseRequest{
		Message: "Test Response",
	}

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	result, err := service.AddResponse(context.Background(), ticketID, authorID, false, req)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_AddResponse_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	authorID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		PlayerID:    uuid.New(),
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	req := &models.AddResponseRequest{
		Message: "Test Response",
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("CreateResponse", context.Background(), mock.AnythingOfType("*models.TicketResponse")).Return(expectedErr)

	result, err := service.AddResponse(context.Background(), ticketID, authorID, false, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketDetail_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	result, err := service.GetTicketDetail(context.Background(), ticketID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketDetail_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, expectedErr)

	result, err := service.GetTicketDetail(context.Background(), ticketID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_RateTicket_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()

	mockRepo.On("GetByID", context.Background(), ticketID).Return(nil, nil)

	err := service.RateTicket(context.Background(), ticketID, 5)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_RateTicket_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ticketID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		PlayerID:    uuid.New(),
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusResolved,
		Subject:     "Test Subject",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetByID", context.Background(), ticketID).Return(ticket, nil)
	mockRepo.On("Update", context.Background(), mock.AnythingOfType("*models.SupportTicket")).Return(expectedErr)

	err := service.RateTicket(context.Background(), ticketID, 5)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketByNumber_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	number := "TKT-20250101-0001"

	mockRepo.On("GetByNumber", context.Background(), number).Return(nil, nil)

	result, err := service.GetTicketByNumber(context.Background(), number)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestTicketService_GetTicketByNumber_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	number := "TKT-20250101-0001"
	expectedErr := errors.New("database error")

	mockRepo.On("GetByNumber", context.Background(), number).Return(nil, expectedErr)

	result, err := service.GetTicketByNumber(context.Background(), number)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func stringPtr(s string) *string {
	return &s
}

