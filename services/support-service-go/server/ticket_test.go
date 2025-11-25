package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/support-service-go/models"
)

type MockTicketService struct{}

func (m *MockTicketService) CreateTicket(ctx context.Context, playerID uuid.UUID, req *models.CreateTicketRequest) (*models.SupportTicket, error) {
	return &models.SupportTicket{
		ID:       uuid.New(),
		PlayerID: playerID,
		Subject:  req.Subject,
		Status:   models.TicketStatusOpen,
	}, nil
}

func (m *MockTicketService) GetTicket(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error) {
	return &models.SupportTicket{
		ID:     id,
		Status: models.TicketStatusOpen,
	}, nil
}

func (m *MockTicketService) GetTicketByNumber(ctx context.Context, number string) (*models.SupportTicket, error) {
	return &models.SupportTicket{
		Number: number,
		Status: models.TicketStatusOpen,
	}, nil
}

func (m *MockTicketService) GetTicketsByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.TicketListResponse, error) {
	return &models.TicketListResponse{
		Tickets: []models.SupportTicket{},
		Total:   0,
	}, nil
}

func (m *MockTicketService) GetTicketsByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) (*models.TicketListResponse, error) {
	return &models.TicketListResponse{
		Tickets: []models.SupportTicket{},
		Total:   0,
	}, nil
}

func (m *MockTicketService) GetTicketsByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) (*models.TicketListResponse, error) {
	return &models.TicketListResponse{
		Tickets: []models.SupportTicket{},
		Total:   0,
	}, nil
}

func (m *MockTicketService) UpdateTicket(ctx context.Context, id uuid.UUID, req *models.UpdateTicketRequest) (*models.SupportTicket, error) {
	status := models.TicketStatusResolved
	if req.Status != nil {
		status = *req.Status
	}
	return &models.SupportTicket{
		ID:     id,
		Status: status,
	}, nil
}

func (m *MockTicketService) AssignTicket(ctx context.Context, id uuid.UUID, agentID uuid.UUID) (*models.SupportTicket, error) {
	return &models.SupportTicket{
		ID:              id,
		AssignedAgentID: &agentID,
	}, nil
}

func (m *MockTicketService) AddResponse(ctx context.Context, ticketID uuid.UUID, authorID uuid.UUID, isAgent bool, req *models.AddResponseRequest) (*models.TicketResponse, error) {
	return &models.TicketResponse{
		ID:       uuid.New(),
		TicketID: ticketID,
		AuthorID: authorID,
		IsAgent:  isAgent,
	}, nil
}

func (m *MockTicketService) GetTicketDetail(ctx context.Context, id uuid.UUID) (*models.TicketDetailResponse, error) {
	return &models.TicketDetailResponse{
		Ticket:    models.SupportTicket{ID: id},
		Responses: []models.TicketResponse{},
	}, nil
}

func (m *MockTicketService) RateTicket(ctx context.Context, id uuid.UUID, rating int) error {
	return nil
}

func TestNewHTTPServer(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	if server == nil {
		t.Fatal("Expected server to be created")
	}
}

func TestHealthCheck(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCreateTicket(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	reqBody := models.CreateTicketRequest{
		Subject: "Test Ticket",
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusCreated {
		t.Errorf("Expected status 201, got %d", w.Code)
	}
	
	var ticket models.SupportTicket
	if err := json.NewDecoder(w.Body).Decode(&ticket); err != nil {
		t.Fatalf("Failed to decode response: %v", err)
	}
	
	if ticket.Status != models.TicketStatusOpen {
		t.Errorf("Expected status Open, got %v", ticket.Status)
	}
}

func TestGetTicket(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/"+ticketID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestGetTicketByNumber(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketNumber := "TICKET-123"
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/number/"+ticketNumber, nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestListPlayerTickets(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	playerID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/player/"+playerID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestListAgentTickets(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	agentID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/agent/"+agentID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestListTicketsByStatus(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/status/open", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestUpdateTicket(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketID := uuid.New()
	status := models.TicketStatusResolved
	reqBody := models.UpdateTicketRequest{
		Status: &status,
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/support/tickets/"+ticketID.String(), bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestAssignTicket(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketID := uuid.New()
	agentID := uuid.New()
	
	reqBody := map[string]string{"agent_id": agentID.String()}
	body, _ := json.Marshal(reqBody)
	
	req := httptest.NewRequest("POST", "/api/v1/support/tickets/"+ticketID.String()+"/assign", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestAddResponse(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketID := uuid.New()
	reqBody := models.AddResponseRequest{
		Message: "Test response",
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/support/tickets/"+ticketID.String()+"/responses", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusCreated {
		t.Errorf("Expected status 201, got %d", w.Code)
	}
}

func TestGetTicketDetail(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	ticketID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/support/tickets/"+ticketID.String()+"/detail", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCORSHeaders(t *testing.T) {
	service := &MockTicketService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Header().Get("Access-Control-Allow-Origin") != "*" {
		t.Error("Expected CORS headers to be set")
	}
}

