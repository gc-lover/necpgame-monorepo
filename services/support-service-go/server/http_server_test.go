package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	"github.com/google/uuid"
)

// testContext creates a context with timeout for HTTP tests
func httpTestContext() (context.Context, context.CancelFunc) {
	return context.WithTimeout(context.Background(), 10*time.Second)
}

type mockTicketService struct {
	tickets      map[uuid.UUID]*models.SupportTicket
	ticketByNum  map[string]*models.SupportTicket
	responses    map[uuid.UUID][]models.TicketResponse
	ticketNumber int
	createErr    error
	getErr       error
}

func (m *mockTicketService) CreateTicket(_ context.Context, playerID uuid.UUID, req *models.CreateTicketRequest) (*models.SupportTicket, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}

	m.ticketNumber++
	priority := models.TicketPriorityNormal
	if req.Priority != nil {
		priority = *req.Priority
	}

	ticket := &models.SupportTicket{
		ID:          uuid.New(),
		Number:      "TICKET-" + string(rune(m.ticketNumber)),
		PlayerID:    playerID,
		Category:    req.Category,
		Priority:    priority,
		Status:      models.TicketStatusOpen,
		Subject:     req.Subject,
		Description: req.Description,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	m.tickets[ticket.ID] = ticket
	m.ticketByNum[ticket.Number] = ticket
	return ticket, nil
}

func (m *mockTicketService) GetTicket(_ context.Context, id uuid.UUID) (*models.SupportTicket, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.tickets[id], nil
}

func (m *mockTicketService) GetTicketByNumber(_ context.Context, number string) (*models.SupportTicket, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.ticketByNum[number], nil
}

func (m *mockTicketService) GetTicketsByPlayerID(_ context.Context, playerID uuid.UUID, limit, offset int) (*models.TicketListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	var tickets []models.SupportTicket
	for _, t := range m.tickets {
		if t.PlayerID == playerID {
			tickets = append(tickets, *t)
		}
	}

	total := len(tickets)
	if offset >= total {
		return &models.TicketListResponse{Tickets: []models.SupportTicket{}, Total: total}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.TicketListResponse{
		Tickets: tickets[offset:end],
		Total:   total,
	}, nil
}

func (m *mockTicketService) GetTicketsByAgentID(_ context.Context, agentID uuid.UUID, _, _ int) (*models.TicketListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	var tickets []models.SupportTicket
	for _, t := range m.tickets {
		if t.AssignedAgentID != nil && *t.AssignedAgentID == agentID {
			tickets = append(tickets, *t)
		}
	}

	return &models.TicketListResponse{
		Tickets: tickets,
		Total:   len(tickets),
	}, nil
}

func (m *mockTicketService) GetTicketsByStatus(_ context.Context, status models.TicketStatus, limit, offset int) (*models.TicketListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	var tickets []models.SupportTicket
	for _, t := range m.tickets {
		if t.Status == status {
			tickets = append(tickets, *t)
		}
	}

	total := len(tickets)
	if offset >= total {
		return &models.TicketListResponse{Tickets: []models.SupportTicket{}, Total: total}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.TicketListResponse{
		Tickets: tickets[offset:end],
		Total:   total,
	}, nil
}

func (m *mockTicketService) UpdateTicket(_ context.Context, id uuid.UUID, req *models.UpdateTicketRequest) (*models.SupportTicket, error) {
	ticket := m.tickets[id]
	if ticket == nil {
		return nil, nil
	}

	if req.Category != nil {
		ticket.Category = *req.Category
	}
	if req.Priority != nil {
		ticket.Priority = *req.Priority
	}
	if req.Status != nil {
		ticket.Status = *req.Status
		now := time.Now()
		if ticket.Status == models.TicketStatusResolved && ticket.ResolvedAt == nil {
			ticket.ResolvedAt = &now
		}
		if ticket.Status == models.TicketStatusClosed && ticket.ClosedAt == nil {
			ticket.ClosedAt = &now
		}
	}
	if req.Subject != nil {
		ticket.Subject = *req.Subject
	}

	ticket.UpdatedAt = time.Now()
	return ticket, nil
}

func (m *mockTicketService) AssignTicket(_ context.Context, id uuid.UUID, agentID uuid.UUID) (*models.SupportTicket, error) {
	ticket := m.tickets[id]
	if ticket == nil {
		return nil, nil
	}

	ticket.AssignedAgentID = &agentID
	if ticket.Status == models.TicketStatusOpen {
		ticket.Status = models.TicketStatusAssigned
	}
	ticket.UpdatedAt = time.Now()
	return ticket, nil
}

func (m *mockTicketService) AddResponse(_ context.Context, ticketID uuid.UUID, authorID uuid.UUID, isAgent bool, req *models.AddResponseRequest) (*models.TicketResponse, error) {
	ticket := m.tickets[ticketID]
	if ticket == nil {
		return nil, nil
	}

	visibility := models.TicketVisibilityPublic
	if req.Visibility != "" {
		visibility = req.Visibility
	}

	response := &models.TicketResponse{
		ID:          uuid.New(),
		TicketID:    ticketID,
		AuthorID:    authorID,
		IsAgent:     isAgent,
		Message:     req.Message,
		Attachments: req.Attachments,
		Visibility:  visibility,
		CreatedAt:   time.Now(),
	}

	m.responses[ticketID] = append(m.responses[ticketID], *response)

	if ticket.FirstResponseAt == nil && isAgent {
		now := time.Now()
		ticket.FirstResponseAt = &now
		if ticket.Status == models.TicketStatusOpen || ticket.Status == models.TicketStatusAssigned {
			ticket.Status = models.TicketStatusInProgress
		}
		ticket.UpdatedAt = now
	}

	return response, nil
}

func (m *mockTicketService) GetTicketDetail(_ context.Context, id uuid.UUID) (*models.TicketDetailResponse, error) {
	ticket := m.tickets[id]
	if ticket == nil {
		return nil, nil
	}

	responses := m.responses[id]
	return &models.TicketDetailResponse{
		Ticket:    *ticket,
		Responses: responses,
	}, nil
}

func (m *mockTicketService) RateTicket(_ context.Context, id uuid.UUID, rating int) error {
	ticket := m.tickets[id]
	if ticket == nil {
		return nil
	}

	if rating >= 1 && rating <= 5 {
		ticket.SatisfactionRating = &rating
		ticket.UpdatedAt = time.Now()
	}
	return nil
}

func TestHTTPServer_CreateTicket(t *testing.T) {
	t.Parallel()
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	playerID := uuid.New()
	// Use API format (uppercase enum values) for ogen
	reqBody := map[string]interface{}{
		"category":    "TECHNICAL",
		"subject":     "Test Ticket",
		"description": "Test Description",
		"priority":    "HIGH",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer test-token")
	req = req.WithContext(context.WithValue(ctx, "user_id", playerID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.SupportTicket
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Subject != "Test Ticket" {
		t.Errorf("Expected subject 'Test Ticket', got %s", response.Subject)
	}
}

func TestHTTPServer_GetTicket(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	playerID := uuid.New()
	ticket := &models.SupportTicket{
		ID:          ticketID,
		Number:      "TICKET-1",
		PlayerID:    playerID,
		Category:    models.TicketCategoryTechnical,
		Priority:    models.TicketPriorityNormal,
		Status:      models.TicketStatusOpen,
		Subject:     "Test Ticket",
		Description: "Test Description",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/support/tickets/"+ticketID.String(), nil)
	req = req.WithContext(ctx)
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.SupportTicket
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != ticketID {
		t.Errorf("Expected ID %s, got %s", ticketID, response.ID)
	}
}

func TestHTTPServer_GetTicketNotFound(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/support/tickets/"+uuid.New().String(), nil)
	req.Header.Set("Authorization", "Bearer test-token")
	req = req.WithContext(context.WithValue(ctx, "user_id", uuid.New().String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_GetTickets(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	playerID := uuid.New()
	ticket1 := &models.SupportTicket{
		ID:        uuid.New(),
		Number:    "TICKET-1",
		PlayerID:  playerID,
		Category:  models.TicketCategoryTechnical,
		Status:    models.TicketStatusOpen,
		Subject:   "Ticket 1",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}
	ticket2 := &models.SupportTicket{
		ID:        uuid.New(),
		Number:    "TICKET-2",
		PlayerID:  playerID,
		Category:  models.TicketCategoryBilling,
		Status:    models.TicketStatusOpen,
		Subject:   "Ticket 2",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService.tickets[ticket1.ID] = ticket1
	mockService.tickets[ticket2.ID] = ticket2
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/support/tickets", nil)
	req.Header.Set("Authorization", "Bearer test-token")
	req = req.WithContext(context.WithValue(ctx, "user_id", playerID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.TicketListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_UpdateTicket(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	ticket := &models.SupportTicket{
		ID:        ticketID,
		Number:    "TICKET-1",
		PlayerID:  uuid.New(),
		Category:  models.TicketCategoryTechnical,
		Status:    models.TicketStatusOpen,
		Subject:   "Original Subject",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	newSubject := "Updated Subject"
	// Use API format (uppercase enum values) for ogen
	statusValue := "IN_PROGRESS"
	reqBody := map[string]interface{}{
		"subject": newSubject,
		"status":  statusValue,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/support/tickets/"+ticketID.String(), bytes.NewBuffer(body))
	req = req.WithContext(ctx)
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.SupportTicket
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Subject != "Updated Subject" {
		t.Errorf("Expected subject 'Updated Subject', got %s", response.Subject)
	}
}

func TestHTTPServer_AssignTicket(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	agentID := uuid.New()
	ticket := &models.SupportTicket{
		ID:        ticketID,
		Number:    "TICKET-1",
		PlayerID:  uuid.New(),
		Status:    models.TicketStatusOpen,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	reqBody := models.AssignTicketRequest{
		AgentID: agentID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets/"+ticketID.String()+"/assign", bytes.NewBuffer(body))
	req = req.WithContext(ctx)
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.SupportTicket
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.AssignedAgentID == nil || *response.AssignedAgentID != agentID {
		t.Error("Ticket was not assigned to agent")
	}
}

func TestHTTPServer_AddResponse(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	authorID := uuid.New()
	ticket := &models.SupportTicket{
		ID:        ticketID,
		Number:    "TICKET-1",
		PlayerID:  authorID,
		Status:    models.TicketStatusOpen,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	reqBody := models.AddResponseRequest{
		Message: "Test response message",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets/"+ticketID.String()+"/responses", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer test-token")
	claims := &Claims{
		RealmAccess: struct {
			Roles []string `json:"roles"`
		}{Roles: []string{"player"}},
	}
	ctx = context.WithValue(ctx, "user_id", authorID.String())
	ctx = context.WithValue(ctx, "claims", claims)
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d", http.StatusCreated, w.Code)
	}

	var response models.TicketResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Message != "Test response message" {
		t.Errorf("Expected message 'Test response message', got %s", response.Message)
	}
}

func TestHTTPServer_GetTicketDetail(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	ticket := &models.SupportTicket{
		ID:        ticketID,
		Number:    "TICKET-1",
		PlayerID:  uuid.New(),
		Status:    models.TicketStatusOpen,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	response := models.TicketResponse{
		ID:        uuid.New(),
		TicketID:  ticketID,
		AuthorID:  ticket.PlayerID,
		Message:   "Test response",
		CreatedAt: time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	mockService.responses[ticketID] = []models.TicketResponse{response}
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/support/tickets/"+ticketID.String()+"/detail", nil)
	req = req.WithContext(ctx)
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var detailResponse models.TicketDetailResponse
	if err := json.Unmarshal(w.Body.Bytes(), &detailResponse); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if len(detailResponse.Responses) != 1 {
		t.Errorf("Expected 1 response, got %d", len(detailResponse.Responses))
	}
}

func TestHTTPServer_RateTicket(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}

	ticketID := uuid.New()
	ticket := &models.SupportTicket{
		ID:        ticketID,
		Number:    "TICKET-1",
		PlayerID:  uuid.New(),
		Status:    models.TicketStatusResolved,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService.tickets[ticketID] = ticket
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	reqBody := models.RateTicketRequest{
		Rating: 5,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets/"+ticketID.String()+"/rate", bytes.NewBuffer(body))
	req = req.WithContext(ctx)
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer test-token")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	t.Parallel()
	ctx, cancel := httpTestContext()
	defer cancel()

	mockService := &mockTicketService{
		tickets:     make(map[uuid.UUID]*models.SupportTicket),
		ticketByNum: make(map[string]*models.SupportTicket),
		responses:   make(map[uuid.UUID][]models.TicketResponse),
	}
	server := NewHTTPServer(":8080", mockService, nil, nil, false)

	req := httptest.NewRequest("GET", "/health", nil)
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["status"] != "healthy" {
		t.Errorf("Expected status 'healthy', got %s", response["status"])
	}
}
