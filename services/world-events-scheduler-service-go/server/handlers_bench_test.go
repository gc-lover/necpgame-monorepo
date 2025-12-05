// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	api "github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkScheduleWorldEvent benchmarks ScheduleWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkScheduleWorldEvent(b *testing.B) {
	logger := GetLogger()
	service := NewService(nil, nil, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()
	req := &api.ScheduleWorldEventRequest{
		// TODO: Fill request fields based on API spec
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ScheduleWorldEvent(ctx, req)
	}
}

// BenchmarkGetScheduledWorldEvents benchmarks GetScheduledWorldEvents handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetScheduledWorldEvents(b *testing.B) {
	logger := GetLogger()
	// Create mock repository to avoid nil pointer dereference
	mockRepo := &mockRepository{}
	service := NewService(mockRepo, nil, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetScheduledWorldEvents(ctx)
	}
}

// mockRepository is a minimal mock for benchmark tests
type mockRepository struct{}

func (m *mockRepository) GetScheduledEvents(ctx context.Context) ([]*ScheduledEvent, error) {
	return []*ScheduledEvent{}, nil
}

func (m *mockRepository) CreateScheduledEvent(ctx context.Context, event *ScheduledEvent) error {
	return nil
}

func (m *mockRepository) GetScheduledEvent(ctx context.Context, id uuid.UUID) (*ScheduledEvent, error) {
	return nil, nil
}

func (m *mockRepository) UpdateScheduledEvent(ctx context.Context, event *ScheduledEvent) error {
	return nil
}

func (m *mockRepository) DeleteScheduledEvent(ctx context.Context, id uuid.UUID) error {
	return nil
}

// BenchmarkTriggerScheduledWorldEvent benchmarks TriggerScheduledWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerScheduledWorldEvent(b *testing.B) {
	logger := GetLogger()
	service := NewService(nil, nil, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()
	params := api.TriggerScheduledWorldEventParams{
		ID: uuid.New(),
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerScheduledWorldEvent(ctx, params)
	}
}

