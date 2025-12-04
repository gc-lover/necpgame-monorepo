// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
)

// mockMailService implements Service interface for benchmarks
type mockMailService struct{}

func (m *mockMailService) GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error) {
	return nil, nil
}

func (m *mockMailService) GetMail(ctx context.Context, mailID string) (*api.MailDetailResponse, error) {
	return nil, nil
}

func (m *mockMailService) DeleteMail(ctx context.Context, mailID string) error {
	return nil
}

func (m *mockMailService) SendMail(ctx context.Context, req *api.SendMailRequest) (*api.SendMailResponse, error) {
	return nil, nil
}

func (m *mockMailService) ClaimAttachments(ctx context.Context, mailID string) (*api.ClaimAttachmentsResponse, error) {
	return nil, nil
}

// BenchmarkGetInbox benchmarks GetInbox handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetInbox(b *testing.B) {
	mockService := &mockMailService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetInboxParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetInbox(ctx, params)
	}
}

// BenchmarkGetMail benchmarks GetMail handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetMail(b *testing.B) {
	mockService := &mockMailService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetMailParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetMail(ctx, params)
	}
}

// BenchmarkDeleteMail benchmarks DeleteMail handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDeleteMail(b *testing.B) {
	mockService := &mockMailService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.DeleteMailParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeleteMail(ctx, params)
	}
}

