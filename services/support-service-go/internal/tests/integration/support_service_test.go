package integration

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame/services/support-service-go/api"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/config"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/handlers"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/service"
	"go.uber.org/zap"
	"go.uber.org/zap/zaptest"
)

type TestSuite struct {
	server   *httptest.Server
	client   *http.Client
	baseURL  string
	logger   *zap.Logger
	cleanup  func()
}

func setupTestSuite(t *testing.T) *TestSuite {
	t.Helper()

	// Setup test logger
	logger := zaptest.NewLogger(t)

	// Setup mock repository for testing
	mockRepo := newMockSupportRepository()
	repo := repository.SupportRepository(mockRepo)

	// Setup service
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host:     "localhost",
			Port:     5432,
			User:     "test",
			Password: "test",
			DBName:   "test_support",
		},
		Server: config.ServerConfig{
			Port: 8080,
		},
	}
	svc := service.NewSupportService(repo, logger, testConfig)

	// Setup handlers
	httpHandlers := handlers.NewSupportHandlers(svc, logger)

	// Setup mock security handler for tests
	securityHandler := &mockSecurityHandler{}

	// Setup server
	apiSrv, err := api.NewServer(httpHandlers, securityHandler)
	if err != nil {
		t.Fatalf("Failed to create API server: %v", err)
	}

	server := httptest.NewServer(apiSrv)

	return &TestSuite{
		server:  server,
		client:  &http.Client{Timeout: 10 * time.Second},
		baseURL: server.URL,
		logger:  logger,
		cleanup: func() {
			server.Close()
		},
	}
}


// Helper functions for making HTTP requests
func (ts *TestSuite) makeRequest(t *testing.T, method, path string, body interface{}) *http.Response {
	t.Helper()

	var reqBody *bytes.Buffer
	if body != nil {
		jsonBody, err := json.Marshal(body)
		if err != nil {
			t.Fatalf("Failed to marshal request body: %v", err)
		}
		reqBody = bytes.NewBuffer(jsonBody)
	} else {
		reqBody = bytes.NewBuffer(nil)
	}

	req, err := http.NewRequest(method, ts.baseURL+path, reqBody)
	if err != nil {
		t.Fatalf("Failed to create request: %v", err)
	}

	req.Header.Set("Content-Type", "application/json")

	resp, err := ts.client.Do(req)
	if err != nil {
		t.Fatalf("Failed to make request: %v", err)
	}

	return resp
}

func (ts *TestSuite) parseResponse(t *testing.T, resp *http.Response, target interface{}) {
	t.Helper()
	defer resp.Body.Close()

	if err := json.NewDecoder(resp.Body).Decode(target); err != nil {
		t.Fatalf("Failed to decode response: %v", err)
	}
}

// Test data
var testCharacterID = uuid.New()
var testAgentID = uuid.New()

// TestHealthCheck tests the health check handler directly
func TestHealthCheck(t *testing.T) {
	// Setup test logger
	logger := zaptest.NewLogger(t)

	// Setup mock repository
	repo := repository.SupportRepository(&mockSupportRepository{})

	// Setup service
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host:     "localhost",
			Port:     5432,
			User:     "test",
			Password: "test",
			DBName:   "test_support",
		},
		Server: config.ServerConfig{
			Port: 8080,
		},
	}
	svc := service.NewSupportService(repo, logger, testConfig)

	// Setup handlers
	handlers := handlers.NewSupportHandlers(svc, logger)

	// Test health check directly
	ctx := context.Background()
	resp, err := handlers.HealthCheck(ctx)
	if err != nil {
		t.Fatalf("Health check failed: %v", err)
	}

	if resp.Status != api.HealthResponseStatusOk {
		t.Errorf("Expected status 'ok', got '%s'", resp.Status)
	}

	t.Log("✓ Health check handler works correctly")
}

// TestCreateTicketHandler tests ticket creation handler
func TestCreateTicketHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := newMockSupportRepository()
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	ctx := context.Background()
	req := &api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Ticket",
		Description: "Test description",
		Category:    api.OptTicketCategory{Value: api.TicketCategoryOther, Set: true},
		Priority:    api.OptTicketPriority{Value: api.TicketPriorityNormal, Set: true},
		Tags:       []string{"test", "unit"},
	}

	resp, err := h.CreateTicket(ctx, req)
	if err != nil {
		t.Fatalf("Create ticket failed: %v", err)
	}

	// Type assertion to get the actual ticket response
	ticketResp, ok := resp.(*api.TicketResponse)
	if !ok {
		t.Fatalf("Expected TicketResponse, got %T", resp)
	}

	if ticketResp.Title != req.Title {
		t.Errorf("Expected title '%s', got '%s'", req.Title, ticketResp.Title)
	}

	if ticketResp.CharacterID != testCharacterID {
		t.Errorf("Expected character ID '%s', got '%s'", testCharacterID, ticketResp.CharacterID)
	}

	t.Logf("✓ Create ticket handler works correctly: %s", ticketResp.ID)
}

// TestGetTicketHandler tests ticket retrieval handler
func TestGetTicketHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create a ticket first
	ctx := context.Background()
	testTicket := &models.Ticket{
		ID:          uuid.New(),
		CharacterID: testCharacterID,
		Title:       "Test Get Ticket",
		Description: "Test description",
		Category:    models.TicketCategoryOther,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Tags:        []string{"test"},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ResponseCount: 0,
	}
	mockRepo.tickets[testTicket.ID] = testTicket

	// Test get ticket
	params := api.GetTicketParams{TicketId: testTicket.ID}
	resp, err := h.GetTicket(ctx, params)
	if err != nil {
		t.Fatalf("Get ticket failed: %v", err)
	}

	// Type assertion to get the actual ticket response
	ticketResp, ok := resp.(*api.TicketResponse)
	if !ok {
		t.Fatalf("Expected TicketResponse, got %T", resp)
	}

	if ticketResp.ID != testTicket.ID {
		t.Errorf("Expected ticket ID '%s', got '%s'", testTicket.ID, ticketResp.ID)
	}

	if ticketResp.Title != testTicket.Title {
		t.Errorf("Expected title '%s', got '%s'", testTicket.Title, ticketResp.Title)
	}

	t.Logf("✓ Get ticket handler works correctly: %s", ticketResp.ID)
}

// TestUpdateTicketHandler tests ticket update handler
func TestUpdateTicketHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create a ticket first
	ctx := context.Background()
	testTicket := &models.Ticket{
		ID:          uuid.New(),
		CharacterID: testCharacterID,
		Title:       "Original Title",
		Description: "Original description",
		Category:    models.TicketCategoryOther,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Tags:        []string{"test"},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ResponseCount: 0,
	}
	mockRepo.tickets[testTicket.ID] = testTicket

	// Test update ticket
	req := &api.UpdateTicketRequest{
		Title:       api.OptString{Value: "Updated Title", Set: true},
		Description: api.OptString{Value: "Updated description", Set: true},
		Tags:        []string{"updated", "test"},
	}
	params := api.UpdateTicketParams{TicketId: testTicket.ID}

	resp, err := h.UpdateTicket(ctx, req, params)
	if err != nil {
		t.Fatalf("Update ticket failed: %v", err)
	}

	// Type assertion to get the actual ticket response
	ticketResp, ok := resp.(*api.TicketResponse)
	if !ok {
		t.Fatalf("Expected TicketResponse, got %T", resp)
	}

	if ticketResp.Title != "Updated Title" {
		t.Errorf("Expected updated title 'Updated Title', got '%s'", ticketResp.Title)
	}

	t.Logf("✓ Update ticket handler works correctly: %s", ticketResp.ID)
}

// TestDeleteTicketHandler tests ticket deletion handler
func TestDeleteTicketHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create a ticket first
	ctx := context.Background()
	testTicket := &models.Ticket{
		ID:          uuid.New(),
		CharacterID: testCharacterID,
		Title:       "Test Delete Ticket",
		Description: "Test description",
		Category:    models.TicketCategoryOther,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Tags:        []string{"test"},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ResponseCount: 0,
	}
	mockRepo.tickets[testTicket.ID] = testTicket

	// Test delete ticket
	params := api.DeleteTicketParams{TicketId: testTicket.ID}
	resp, err := h.DeleteTicket(ctx, params)
	if err != nil {
		t.Fatalf("Delete ticket failed: %v", err)
	}

	// Check that response is of correct type (no content response)
	if _, ok := resp.(*api.DeleteTicketNoContent); !ok {
		t.Errorf("Expected no content response, got %T", resp)
	}

	// Verify ticket is deleted
	if _, exists := mockRepo.tickets[testTicket.ID]; exists {
		t.Errorf("Ticket should be deleted but still exists")
	}

	t.Logf("✓ Delete ticket handler works correctly: %s", testTicket.ID)
}

// TestListTicketsHandler tests ticket listing handler
func TestListTicketsHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create some tickets
	ctx := context.Background()
	for i := 1; i <= 3; i++ {
		ticket := &models.Ticket{
			ID:          uuid.New(),
			CharacterID: testCharacterID,
			Title:       fmt.Sprintf("Test Ticket %d", i),
			Description: "Test description",
			Category:    models.TicketCategoryOther,
			Priority:    models.TicketPriorityNormal,
			Status:      models.TicketStatusOpen,
			Tags:        []string{"test"},
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
			ResponseCount: 0,
		}
		mockRepo.tickets[ticket.ID] = ticket
	}

	// Test list tickets
	params := api.ListTicketsParams{
		Page:  api.OptInt{Value: 1, Set: true},
		Limit: api.OptInt{Value: 10, Set: true},
	}
	resp, err := h.ListTickets(ctx, params)
	if err != nil {
		t.Fatalf("List tickets failed: %v", err)
	}

	// Type assertion to get the actual ticket list response
	listResp, ok := resp.(*api.TicketListResponse)
	if !ok {
		t.Fatalf("Expected TicketListResponse, got %T", resp)
	}

	if len(listResp.Tickets) < 3 {
		t.Errorf("Expected at least 3 tickets, got %d", len(listResp.Tickets))
	}

	t.Logf("✓ List tickets handler works correctly: %d tickets returned", len(listResp.Tickets))
}

// TestGetCharacterTicketsHandler tests character tickets retrieval
func TestGetCharacterTicketsHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create tickets for the test character
	ctx := context.Background()
	for i := 1; i <= 2; i++ {
		ticket := &models.Ticket{
			ID:          uuid.New(),
			CharacterID: testCharacterID,
			Title:       fmt.Sprintf("Character Ticket %d", i),
			Description: "Test description",
			Category:    models.TicketCategoryOther,
			Priority:    models.TicketPriorityNormal,
			Status:      models.TicketStatusOpen,
			Tags:        []string{"test"},
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
			ResponseCount: 0,
		}
		mockRepo.tickets[ticket.ID] = ticket
	}

	// Test get character tickets
	params := api.GetCharacterTicketsParams{
		CharacterId: testCharacterID,
		Page:        api.OptInt{Value: 1, Set: true},
		Limit:       api.OptInt{Value: 10, Set: true},
	}
	resp, err := h.GetCharacterTickets(ctx, params)
	if err != nil {
		t.Fatalf("Get character tickets failed: %v", err)
	}

	// Type assertion to get the actual ticket list response
	listResp, ok := resp.(*api.TicketListResponse)
	if !ok {
		t.Fatalf("Expected TicketListResponse, got %T", resp)
	}

	if len(listResp.Tickets) < 2 {
		t.Errorf("Expected at least 2 character tickets, got %d", len(listResp.Tickets))
	}

	t.Logf("✓ Get character tickets handler works correctly: %d tickets returned", len(listResp.Tickets))
}

// TestUpdateTicketStatusHandler tests status update handler
func TestUpdateTicketStatusHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	// Create a ticket first
	ctx := context.Background()
	testTicket := &models.Ticket{
		ID:          uuid.New(),
		CharacterID: testCharacterID,
		Title:       "Test Status Update",
		Description: "Test description",
		Category:    models.TicketCategoryOther,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Tags:        []string{"test"},
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ResponseCount: 0,
	}
	mockRepo.tickets[testTicket.ID] = testTicket

	// Test update status
	req := &api.UpdateStatusRequest{
		Status:  api.TicketStatusInProgress,
		Comment: api.OptString{Value: "Starting work on this ticket", Set: true},
	}
	params := api.UpdateTicketStatusParams{TicketId: testTicket.ID}

	resp, err := h.UpdateTicketStatus(ctx, req, params)
	if err != nil {
		t.Fatalf("Update ticket status failed: %v", err)
	}

	// Type assertion to get the actual ticket response
	ticketResp, ok := resp.(*api.TicketResponse)
	if !ok {
		t.Fatalf("Expected TicketResponse, got %T", resp)
	}

	if ticketResp.Status != api.TicketStatusInProgress {
		t.Errorf("Expected status 'in_progress', got '%s'", ticketResp.Status)
	}

	t.Logf("✓ Update ticket status handler works correctly: %s", ticketResp.ID)
}

// TestGetSupportStatsHandler tests support stats handler
func TestGetSupportStatsHandler(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &mockSupportRepository{}
	repo := repository.SupportRepository(mockRepo)
	testConfig := &config.Config{
		Database: config.DatabaseConfig{
			Host: "localhost", Port: 5432, User: "test", Password: "test", DBName: "test_support",
		},
		Server: config.ServerConfig{Port: 8080},
	}
	svc := service.NewSupportService(repo, logger, testConfig)
	h := handlers.NewSupportHandlers(svc, logger)

	ctx := context.Background()
	params := api.GetSupportStatsParams{}

	resp, err := h.GetSupportStats(ctx, params)
	if err != nil {
		t.Fatalf("Get support stats failed: %v", err)
	}

	// Type assertion to get the actual stats response
	statsResp, ok := resp.(*api.SupportStatsResponse)
	if !ok {
		t.Fatalf("Expected SupportStatsResponse, got %T", resp)
	}

	// Basic validation - just check that we got a response
	if statsResp == nil {
		t.Errorf("Expected non-nil stats response")
	}

	t.Log("✓ Get support stats handler works correctly")
}

// TestCreateTicket tests ticket creation
func TestCreateTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Ticket",
		Description: "Test description",
		Category:    api.OptTicketCategory{Value: api.TicketCategoryOther, Set: true},
		Priority:    api.OptTicketPriority{Value: api.TicketPriorityNormal, Set: true},
		Tags:       []string{"test", "integration"},
	}

	resp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		t.Errorf("Expected status 201, got %d", resp.StatusCode)
	}

	var ticketResp api.TicketResponse
	ts.parseResponse(t, resp, &ticketResp)

	if ticketResp.Title != createReq.Title {
		t.Errorf("Expected title '%s', got '%s'", createReq.Title, ticketResp.Title)
	}

	if ticketResp.CharacterID != testCharacterID {
		t.Errorf("Expected character ID '%s', got '%s'", testCharacterID, ticketResp.CharacterID)
	}

	t.Logf("✓ Ticket created successfully with ID: %s", ticketResp.ID)
}

// TestGetTicket tests retrieving a ticket
func TestGetTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// First create a ticket
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Get Ticket",
		Description: "Test description for get",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Now get the ticket
	getResp := ts.makeRequest(t, "GET", fmt.Sprintf("/api/v1/tickets/%s", createdTicket.ID), nil)
	defer getResp.Body.Close()

	if getResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", getResp.StatusCode)
	}

	var retrievedTicket api.TicketResponse
	ts.parseResponse(t, getResp, &retrievedTicket)

	if retrievedTicket.ID != createdTicket.ID {
		t.Errorf("Expected ticket ID '%s', got '%s'", createdTicket.ID, retrievedTicket.ID)
	}

	if retrievedTicket.Title != createdTicket.Title {
		t.Errorf("Expected title '%s', got '%s'", createdTicket.Title, retrievedTicket.Title)
	}

	t.Logf("✓ Ticket retrieved successfully: %s", retrievedTicket.ID)
}

// TestUpdateTicket tests ticket update
func TestUpdateTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Original Title",
		Description: "Original description",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for update test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Update the ticket
	updateReq := api.UpdateTicketRequest{
		Title:       api.NewOptString("Updated Title"),
		Description: api.NewOptString("Updated description"),
		Tags:        []string{"updated", "test"},
	}

	updateResp := ts.makeRequest(t, "PUT", fmt.Sprintf("/api/v1/tickets/%s", createdTicket.ID), updateReq)
	defer updateResp.Body.Close()

	if updateResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", updateResp.StatusCode)
	}

	var updatedTicket api.TicketResponse
	ts.parseResponse(t, updateResp, &updatedTicket)

	if updatedTicket.Title != "Updated Title" {
		t.Errorf("Expected updated title 'Updated Title', got '%s'", updatedTicket.Title)
	}

	t.Logf("✓ Ticket updated successfully: %s", updatedTicket.ID)
}

// TestAssignAgent tests agent assignment to ticket
func TestAssignAgent(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Agent Assignment",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for agent assignment test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Assign agent
	assignReq := api.AssignAgentRequest{
		AgentID: testAgentID,
	}

	assignResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/assign", createdTicket.ID), assignReq)
	defer assignResp.Body.Close()

	if assignResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", assignResp.StatusCode)
	}

	var assignedTicket api.TicketResponse
	ts.parseResponse(t, assignResp, &assignedTicket)

	if !assignedTicket.AgentID.Set || assignedTicket.AgentID.Value != testAgentID {
		t.Errorf("Expected agent ID '%s', got '%v'", testAgentID, assignedTicket.AgentID)
	}

	t.Logf("✓ Agent assigned successfully to ticket: %s", assignedTicket.ID)
}

// TestUpdateTicketStatus tests status update
func TestUpdateTicketStatus(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Status Update",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for status update test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Update status to in_progress
	statusReq := api.UpdateStatusRequest{
		Status:  api.TicketStatusInProgress,
		Comment: api.OptString{Value: "Starting work on this ticket", Set: true},
	}

	statusResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/status", createdTicket.ID), statusReq)
	defer statusResp.Body.Close()

	if statusResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", statusResp.StatusCode)
	}

	var updatedTicket api.TicketResponse
	ts.parseResponse(t, statusResp, &updatedTicket)

	if updatedTicket.Status != api.TicketStatusInProgress {
		t.Errorf("Expected status 'in_progress', got '%s'", updatedTicket.Status)
	}

	t.Logf("✓ Ticket status updated successfully: %s", updatedTicket.ID)
}

// TestListTickets tests ticket listing
func TestListTickets(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a few tickets first
	for i := 1; i <= 3; i++ {
		createReq := api.CreateTicketRequest{
			CharacterID: testCharacterID,
			Title:       fmt.Sprintf("Test Ticket %d", i),
		}

		createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
		createResp.Body.Close()

		if createResp.StatusCode != http.StatusCreated {
			t.Errorf("Failed to create ticket %d for list test, status: %d", i, createResp.StatusCode)
		}
	}

	// List tickets
	listResp := ts.makeRequest(t, "GET", "/api/v1/tickets?page=1&limit=10", nil)
	defer listResp.Body.Close()

	if listResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", listResp.StatusCode)
	}

	var ticketList api.TicketListResponse
	ts.parseResponse(t, listResp, &ticketList)

	if len(ticketList.Tickets) < 3 {
		t.Errorf("Expected at least 3 tickets, got %d", len(ticketList.Tickets))
	}

	t.Logf("✓ Retrieved %d tickets successfully", len(ticketList.Tickets))
}

// TestGetCharacterTickets tests getting tickets by character
func TestGetCharacterTickets(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create tickets for the test character
	for i := 1; i <= 2; i++ {
		createReq := api.CreateTicketRequest{
			CharacterID: testCharacterID,
			Title:       fmt.Sprintf("Character Ticket %d", i),
		}

		createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
		createResp.Body.Close()

		if createResp.StatusCode != http.StatusCreated {
			t.Errorf("Failed to create character ticket %d, status: %d", i, createResp.StatusCode)
		}
	}

	// Get character tickets
	charResp := ts.makeRequest(t, "GET", fmt.Sprintf("/api/v1/characters/%s/tickets?page=1&limit=10", testCharacterID), nil)
	defer charResp.Body.Close()

	if charResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", charResp.StatusCode)
	}

	var ticketList api.TicketListResponse
	ts.parseResponse(t, charResp, &ticketList)

	if len(ticketList.Tickets) < 2 {
		t.Errorf("Expected at least 2 character tickets, got %d", len(ticketList.Tickets))
	}

	t.Logf("✓ Retrieved %d tickets for character successfully", len(ticketList.Tickets))
}

// TestAddTicketResponse tests adding response to ticket
func TestAddTicketResponse(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Response Addition",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for response test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Add a response
	responseReq := api.AddResponseRequest{
		Content: "This is a test response to the ticket.",
	}

	respResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/responses", createdTicket.ID), responseReq)
	defer respResp.Body.Close()

	if respResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", respResp.StatusCode)
	}

	var responseItem api.TicketResponseItem
	ts.parseResponse(t, respResp, &responseItem)

	if responseItem.Content != responseReq.Content {
		t.Errorf("Expected response content '%s', got '%s'", responseReq.Content, responseItem.Content)
	}

	if responseItem.TicketID != createdTicket.ID {
		t.Errorf("Expected ticket ID '%s', got '%s'", createdTicket.ID, responseItem.TicketID)
	}

	t.Logf("✓ Response added successfully to ticket: %s", createdTicket.ID)
}

// TestGetTicketResponses tests retrieving ticket responses
func TestGetTicketResponses(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket and add responses
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Response Retrieval",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for response retrieval test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Add a couple of responses
	for i := 1; i <= 2; i++ {
		responseReq := api.AddResponseRequest{
			Content: fmt.Sprintf("Test response #%d", i),
		}

		addResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/responses", createdTicket.ID), responseReq)
		addResp.Body.Close()

		if addResp.StatusCode != http.StatusOK {
			t.Errorf("Failed to add response %d, status: %d", i, addResp.StatusCode)
		}
	}

	// Get responses
	getResp := ts.makeRequest(t, "GET", fmt.Sprintf("/api/v1/tickets/%s/responses?page=1&limit=10", createdTicket.ID), nil)
	defer getResp.Body.Close()

	if getResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", getResp.StatusCode)
	}

	var responseList api.TicketResponseListResponse
	ts.parseResponse(t, getResp, &responseList)

	if len(responseList.Responses) < 2 {
		t.Errorf("Expected at least 2 responses, got %d", len(responseList.Responses))
	}

	t.Logf("✓ Retrieved %d responses for ticket successfully", len(responseList.Responses))
}

// TestRateTicket tests ticket rating
func TestRateTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket and resolve it first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Ticket Rating",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for rating test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Resolve the ticket
	resolveReq := api.UpdateStatusRequest{
		Status: api.TicketStatusResolved,
	}

	resolveResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/status", createdTicket.ID), resolveReq)
	resolveResp.Body.Close()

	if resolveResp.StatusCode != http.StatusOK {
		t.Errorf("Failed to resolve ticket for rating test, status: %d", resolveResp.StatusCode)
	}

	// Rate the ticket
	rateReq := api.RateTicketRequest{
		Rating:  5,
		Comment: api.NewOptString("Excellent support!"),
	}

	rateResp := ts.makeRequest(t, "POST", fmt.Sprintf("/api/v1/tickets/%s/rate", createdTicket.ID), rateReq)
	defer rateResp.Body.Close()

	if rateResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", rateResp.StatusCode)
	}

	var ratedTicket api.TicketResponse
	ts.parseResponse(t, rateResp, &ratedTicket)

	t.Logf("✓ Ticket rated successfully: %s", ratedTicket.ID)
}

// TestGetTicketQueue tests ticket queue retrieval
func TestGetTicketQueue(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create some tickets with different priorities
	priorities := []api.TicketPriority{
		api.TicketPriorityUrgent,
		api.TicketPriorityHigh,
		api.TicketPriorityNormal,
	}

	for i, priority := range priorities {
		createReq := api.CreateTicketRequest{
			CharacterID: testCharacterID,
			Title:       fmt.Sprintf("Queue Test Ticket %d", i+1),
			Priority:    api.OptTicketPriority{Value: priority, Set: true},
		}

		createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
		createResp.Body.Close()

		if createResp.StatusCode != http.StatusCreated {
			t.Errorf("Failed to create queue test ticket %d, status: %d", i+1, createResp.StatusCode)
		}
	}

	// Get ticket queue
	queueResp := ts.makeRequest(t, "GET", "/api/v1/tickets/queue?page=1&limit=10", nil)
	defer queueResp.Body.Close()

	if queueResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", queueResp.StatusCode)
	}

	var queueResponse api.TicketQueueResponse
	ts.parseResponse(t, queueResp, &queueResponse)

	if len(queueResponse.Queue) == 0 {
		t.Errorf("Expected at least some tickets in queue, got 0")
	}

	t.Logf("✓ Retrieved ticket queue with %d tickets successfully", len(queueResponse.Queue))
}

// TestNonExistentTicket tests 404 handling
func TestNonExistentTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Try to get a non-existent ticket
	nonExistentID := uuid.New()
	getResp := ts.makeRequest(t, "GET", fmt.Sprintf("/api/v1/tickets/%s", nonExistentID), nil)
	defer getResp.Body.Close()

	if getResp.StatusCode != http.StatusNotFound {
		t.Errorf("Expected status 404 for non-existent ticket, got %d", getResp.StatusCode)
	}

	t.Log("✓ 404 handling works correctly for non-existent tickets")
}

// TestGetSupportStats tests support statistics
func TestGetSupportStats(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Get support stats
	statsResp := ts.makeRequest(t, "GET", "/api/v1/support/stats?period=month", nil)
	defer statsResp.Body.Close()

	if statsResp.StatusCode != http.StatusOK {
		t.Errorf("Expected status 200, got %d", statsResp.StatusCode)
	}

	var stats api.SupportStatsResponse
	ts.parseResponse(t, statsResp, &stats)

	// Basic validation - stats should have period field
	if stats.Period == "" {
		t.Errorf("Expected non-empty period in stats response")
	}

	t.Logf("✓ Support stats retrieved successfully for period: %s", stats.Period)
}

// TestDeleteTicket tests ticket deletion
func TestDeleteTicket(t *testing.T) {
	ts := setupTestSuite(t)
	defer ts.cleanup()

	// Create a ticket first
	createReq := api.CreateTicketRequest{
		CharacterID: testCharacterID,
		Title:       "Test Ticket Deletion",
	}

	createResp := ts.makeRequest(t, "POST", "/api/v1/tickets", createReq)
	defer createResp.Body.Close()

	if createResp.StatusCode != http.StatusCreated {
		t.Fatalf("Failed to create ticket for deletion test, status: %d", createResp.StatusCode)
	}

	var createdTicket api.TicketResponse
	ts.parseResponse(t, createResp, &createdTicket)

	// Delete the ticket
	deleteResp := ts.makeRequest(t, "DELETE", fmt.Sprintf("/api/v1/tickets/%s", createdTicket.ID), nil)
	defer deleteResp.Body.Close()

	if deleteResp.StatusCode != http.StatusNoContent {
		t.Errorf("Expected status 204, got %d", deleteResp.StatusCode)
	}

	// Verify ticket is gone
	getResp := ts.makeRequest(t, "GET", fmt.Sprintf("/api/v1/tickets/%s", createdTicket.ID), nil)
	defer getResp.Body.Close()

	if getResp.StatusCode != http.StatusNotFound {
		t.Errorf("Expected status 404 after deletion, got %d", getResp.StatusCode)
	}

	t.Logf("✓ Ticket deleted successfully: %s", createdTicket.ID)
}

// Mock repository for testing
type mockSupportRepository struct {
	tickets    map[uuid.UUID]*models.Ticket
	responses  map[uuid.UUID][]*models.TicketResponse
	slaInfo    map[uuid.UUID]*models.TicketSLAInfo
}

func newMockSupportRepository() *mockSupportRepository {
	return &mockSupportRepository{
		tickets:   make(map[uuid.UUID]*models.Ticket),
		responses: make(map[uuid.UUID][]*models.TicketResponse),
		slaInfo:   make(map[uuid.UUID]*models.TicketSLAInfo),
	}
}

func (m *mockSupportRepository) CreateTicket(ctx context.Context, ticket *models.Ticket) error {
	m.tickets[ticket.ID] = ticket
	return nil
}

func (m *mockSupportRepository) GetTicket(ctx context.Context, id uuid.UUID) (*models.Ticket, error) {
	ticket, exists := m.tickets[id]
	if !exists {
		return nil, fmt.Errorf("ticket not found")
	}
	return ticket, nil
}

func (m *mockSupportRepository) UpdateTicket(ctx context.Context, ticket *models.Ticket) error {
	m.tickets[ticket.ID] = ticket
	return nil
}

func (m *mockSupportRepository) DeleteTicket(ctx context.Context, id uuid.UUID) error {
	delete(m.tickets, id)
	return nil
}

func (m *mockSupportRepository) ListTickets(ctx context.Context, filter *models.TicketFilter, page, limit int) ([]*models.Ticket, int, error) {
	var tickets []*models.Ticket
	for _, ticket := range m.tickets {
		tickets = append(tickets, ticket)
	}
	return tickets, len(tickets), nil
}

func (m *mockSupportRepository) GetTicketsByCharacter(ctx context.Context, characterID uuid.UUID, page, limit int) ([]*models.Ticket, int, error) {
	var tickets []*models.Ticket
	for _, ticket := range m.tickets {
		if ticket.CharacterID == characterID {
			tickets = append(tickets, ticket)
		}
	}
	return tickets, len(tickets), nil
}

func (m *mockSupportRepository) GetTicketsByAgent(ctx context.Context, agentID uuid.UUID, page, limit int) ([]*models.Ticket, int, error) {
	var tickets []*models.Ticket
	for _, ticket := range m.tickets {
		if ticket.AgentID != nil && *ticket.AgentID == agentID {
			tickets = append(tickets, ticket)
		}
	}
	return tickets, len(tickets), nil
}

func (m *mockSupportRepository) GetTicketQueue(ctx context.Context, filter *models.QueueFilter, page, limit int) ([]*models.Ticket, *models.QueueStats, error) {
	var tickets []*models.Ticket
	for _, ticket := range m.tickets {
		if ticket.Status == models.TicketStatusOpen {
			tickets = append(tickets, ticket)
		}
	}
	stats := &models.QueueStats{
		TotalWaiting: len(tickets),
		UrgentCount:  0,
		HighCount:    0,
		NormalCount:  0,
		LowCount:     0,
	}
	return tickets, stats, nil
}

func (m *mockSupportRepository) AssignAgent(ctx context.Context, ticketID, agentID uuid.UUID) (*models.Ticket, error) {
	ticket, exists := m.tickets[ticketID]
	if !exists {
		return nil, fmt.Errorf("ticket not found")
	}
	ticket.AgentID = &agentID
	ticket.Status = models.TicketStatusAssigned
	return ticket, nil
}

func (m *mockSupportRepository) UpdateTicketStatus(ctx context.Context, ticketID uuid.UUID, status models.TicketStatus) (*models.Ticket, error) {
	ticket, exists := m.tickets[ticketID]
	if !exists {
		return nil, fmt.Errorf("ticket not found")
	}
	ticket.Status = status
	return ticket, nil
}

func (m *mockSupportRepository) UpdateTicketPriority(ctx context.Context, ticketID uuid.UUID, priority models.TicketPriority) (*models.Ticket, error) {
	ticket, exists := m.tickets[ticketID]
	if !exists {
		return nil, fmt.Errorf("ticket not found")
	}
	ticket.Priority = priority
	return ticket, nil
}

func (m *mockSupportRepository) CreateTicketResponse(ctx context.Context, response *models.TicketResponse) error {
	m.responses[response.TicketID] = append(m.responses[response.TicketID], response)
	return nil
}

func (m *mockSupportRepository) GetTicketResponses(ctx context.Context, ticketID uuid.UUID, page, limit int) ([]*models.TicketResponse, int, error) {
	responses, exists := m.responses[ticketID]
	if !exists {
		return []*models.TicketResponse{}, 0, nil
	}
	return responses, len(responses), nil
}

func (m *mockSupportRepository) UpdateTicketRating(ctx context.Context, ticketID uuid.UUID, rating int, comment string) error {
	// Mock implementation - do nothing
	return nil
}

// Mock security handler for tests
type mockSecurityHandler struct{}

func (m *mockSecurityHandler) HandleApiKeyAuth(ctx context.Context, operationName api.OperationName, apiKey api.ApiKeyAuth) (context.Context, error) {
	// Allow all API key auth for tests
	return ctx, nil
}

func (m *mockSecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, bearer api.BearerAuth) (context.Context, error) {
	// Allow all bearer auth for tests
	return ctx, nil
}

func (m *mockSupportRepository) GetTicketSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	sla, exists := m.slaInfo[ticketID]
	if !exists {
		return &models.TicketSLAInfo{
			TicketID:           ticketID,
			Priority:           models.TicketPriorityNormal,
			CreatedAt:          time.Now(),
			SLADueDate:         time.Now().Add(24 * time.Hour),
			SLAStatus:          models.SLAStatusCompliant,
			TimeToFirstResponse: &[]string{"2h"}[0],
			TimeToResolution:    &[]string{"24h"}[0],
		}, nil
	}
	return sla, nil
}

func (m *mockSupportRepository) GetSupportStats(ctx context.Context, period, category string) (*models.SupportStatsResponse, error) {
	return &models.SupportStatsResponse{
		Period:          period,
		TotalTickets:    len(m.tickets),
		ResolvedTickets: 0,
		AgentPerformance: []models.AgentPerformance{},
		TicketsByStatus:  make(map[string]int),
		TicketsByPriority: make(map[string]int),
		TicketsByCategory: make(map[string]int),
	}, nil
}

func (m *mockSupportRepository) IncrementTicketResponseCount(ctx context.Context, ticketID uuid.UUID) error {
	ticket, exists := m.tickets[ticketID]
	if !exists {
		return fmt.Errorf("ticket not found")
	}
	ticket.ResponseCount++
	return nil
}