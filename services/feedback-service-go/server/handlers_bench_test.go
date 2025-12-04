// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/google/uuid"
)

// mockFeedbackService implements FeedbackServiceInterface for benchmarks
type mockFeedbackService struct{}

func (m *mockFeedbackService) SubmitFeedback(ctx context.Context, playerID uuid.UUID, req *models.SubmitFeedbackRequest) (*models.SubmitFeedbackResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetFeedback(ctx context.Context, id uuid.UUID) (*models.Feedback, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetPlayerFeedback(ctx context.Context, playerID uuid.UUID, status *models.FeedbackStatus, feedbackType *models.FeedbackType, limit, offset int) (*models.FeedbackList, error) {
	return nil, nil
}

func (m *mockFeedbackService) UpdateStatus(ctx context.Context, id uuid.UUID, req *models.UpdateStatusRequest) (*models.Feedback, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetBoard(ctx context.Context, category *models.FeedbackCategory, status *models.FeedbackStatus, search *string, sort string, limit, offset int) (*models.FeedbackBoardList, error) {
	return nil, nil
}

func (m *mockFeedbackService) Vote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) Unvote(ctx context.Context, feedbackID, playerID uuid.UUID) (*models.VoteResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetStats(ctx context.Context) (*models.FeedbackStats, error) {
	return nil, nil
}

// BenchmarkGetFeedback benchmarks GetFeedback handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetFeedback(b *testing.B) {
	mockService := &mockFeedbackService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetFeedbackParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFeedback(ctx, params)
	}
}

// BenchmarkGetPlayerFeedback benchmarks GetPlayerFeedback handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerFeedback(b *testing.B) {
	mockService := &mockFeedbackService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetPlayerFeedbackParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerFeedback(ctx, params)
	}
}

// BenchmarkSubmitFeedback benchmarks SubmitFeedback handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSubmitFeedback(b *testing.B) {
	mockService := &mockFeedbackService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.SubmitFeedbackRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SubmitFeedback(ctx, req)
	}
}

