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

func (m *mockFeedbackService) SubmitFeedback(_ context.Context, _ uuid.UUID, _ *models.SubmitFeedbackRequest) (*models.SubmitFeedbackResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetFeedback(_ context.Context, _ uuid.UUID) (*models.Feedback, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetPlayerFeedback(_ context.Context, _ uuid.UUID, _ *models.FeedbackStatus, _ *models.FeedbackType, _, _ int) (*models.FeedbackList, error) {
	return nil, nil
}

func (m *mockFeedbackService) UpdateStatus(_ context.Context, _ uuid.UUID, _ *models.UpdateStatusRequest) (*models.Feedback, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetBoard(_ context.Context, _ *models.FeedbackCategory, _ *models.FeedbackStatus, _ *string, _ string, _, _ int) (*models.FeedbackBoardList, error) {
	return nil, nil
}

func (m *mockFeedbackService) Vote(_ context.Context, _, _ uuid.UUID) (*models.VoteResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) Unvote(_ context.Context, _, _ uuid.UUID) (*models.VoteResponse, error) {
	return nil, nil
}

func (m *mockFeedbackService) GetStats(_ context.Context) (*models.FeedbackStats, error) {
	return nil, nil
}

// BenchmarkGetFeedback benchmarks GetFeedback handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetFeedback(b *testing.B) {
	mockService := &mockFeedbackService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetFeedbackParams{}

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
