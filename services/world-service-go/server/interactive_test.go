// Issue: #1841-#1844 - QA testing for world interactives import
package server

import (
	"context"
	"strings"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

func TestInteractiveContentImport(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.DebugLevel)
	interactiveRepo := NewInMemoryInteractiveRepository()
	handlers := NewHandlers(logger, interactiveRepo)

	ctx := context.Background()

	// Test importing corporate interactive content
	t.Run("ImportCorporateInteractive", func(t *testing.T) {
		req := &api.ImportInteractiveContentRequest{
			InteractiveID: "corporate-server-rack-1841",
		}

		resp, err := handlers.ImportInteractiveContent(ctx, req)
		if err != nil {
			t.Fatalf("ImportInteractiveContent failed: %v", err)
		}

		if resp == nil {
			t.Fatal("Expected non-nil response")
		}

		// Type assert to check the concrete type
		if concreteResp, ok := resp.(*api.ImportInteractiveContentResponse); ok {
			if concreteResp.InteractiveID != "corporate-server-rack-1841" {
				t.Errorf("Expected interactive ID 'corporate-server-rack-1841', got '%s'", concreteResp.InteractiveID)
			}
			if concreteResp.Message != "Interactive content imported" {
				t.Errorf("Expected message 'Interactive content imported', got '%s'", concreteResp.Message)
			}
		}
	})

	// Test importing urban interactive content
	t.Run("ImportUrbanInteractive", func(t *testing.T) {
		req := &api.ImportInteractiveContentRequest{
			InteractiveID: "urban-terminal-1839",
		}

		resp, err := handlers.ImportInteractiveContent(ctx, req)
		if err != nil {
			t.Fatalf("ImportInteractiveContent failed: %v", err)
		}

		if resp == nil {
			t.Fatal("Expected non-nil response")
		}
	})

	// Test listing interactives
	t.Run("ListInteractives", func(t *testing.T) {
		params := api.ListInteractivesParams{}

		resp, err := handlers.ListInteractives(ctx, params)
		if err != nil {
			t.Fatalf("ListInteractives failed: %v", err)
		}

		if resp == nil {
			t.Fatal("Expected non-nil response")
		}

		// Type assert to check the concrete type
		if concreteResp, ok := resp.(*api.ListInteractivesResponse); ok {
			if concreteResp.Total != 2 {
				t.Errorf("Expected 2 interactives, got %d", concreteResp.Total)
			}
			if len(concreteResp.Interactives) != 2 {
				t.Errorf("Expected 2 interactive items, got %d", len(concreteResp.Interactives))
			}
		}
	})

	// Test repository functionality
	t.Run("RepositorySaveAndRetrieve", func(t *testing.T) {
		contentData := map[string]interface{}{
			"type": "checkpoint",
			"name": "Test Checkpoint",
			"description": "A test interactive checkpoint",
		}

		saved, err := interactiveRepo.SaveInteractive(ctx, "test-checkpoint", 1, "Test Checkpoint", "A test interactive", "global", "checkpoint", "active", contentData)
		if err != nil {
			t.Fatalf("SaveInteractive failed: %v", err)
		}

		if saved.InteractiveID != "test-checkpoint" {
			t.Errorf("Expected interactive ID 'test-checkpoint', got '%s'", saved.InteractiveID)
		}

		if saved.Name != "Test Checkpoint" {
			t.Errorf("Expected name 'Test Checkpoint', got '%s'", saved.Name)
		}

		// Test retrieval - should find at least our test checkpoint
		filter := &models.ListInteractivesRequest{
			Type: &[]models.InteractiveType{models.InteractiveType("checkpoint")}[0],
		}

		interactives, total, err := interactiveRepo.GetInteractives(ctx, filter)
		if err != nil {
			t.Fatalf("GetInteractives failed: %v", err)
		}

		if total < 1 {
			t.Errorf("Expected at least 1 interactive, got %d", total)
		}

		if len(interactives) < 1 {
			t.Errorf("Expected at least 1 interactive item, got %d", len(interactives))
		}

		// Check that our test checkpoint is in the results
		found := false
		for _, interactive := range interactives {
			if interactive.InteractiveID == "test-checkpoint" {
				found = true
				break
			}
		}

		if !found {
			t.Error("Expected to find test-checkpoint in results")
		}
	})
}

func TestInteractiveContentValidation(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.DebugLevel)
	interactiveRepo := NewInMemoryInteractiveRepository()
	handlers := NewHandlers(logger, interactiveRepo)

	ctx := context.Background()

	// Test validation - empty interactive ID should return error response
	t.Run("EmptyInteractiveID", func(t *testing.T) {
		req := &api.ImportInteractiveContentRequest{
			InteractiveID: "",
		}

		resp, err := handlers.ImportInteractiveContent(ctx, req)
		if err != nil {
			t.Fatalf("ImportInteractiveContent returned unexpected error: %v", err)
		}

		if concreteResp, ok := resp.(*api.ImportInteractiveContentResponse); ok {
			if concreteResp.InteractiveID != "" {
				t.Errorf("Expected empty interactive ID in response, got '%s'", concreteResp.InteractiveID)
			}
			if !strings.Contains(concreteResp.Message, "empty interactive_id") {
				t.Errorf("Expected error message about empty interactive_id, got '%s'", concreteResp.Message)
			}
		}
	})

	// Test with nil request
	t.Run("NilRequest", func(t *testing.T) {
		resp, err := handlers.ImportInteractiveContent(ctx, nil)
		if err != nil {
			t.Fatalf("ImportInteractiveContent returned unexpected error: %v", err)
		}

		if concreteResp, ok := resp.(*api.ImportInteractiveContentResponse); ok {
			if concreteResp.InteractiveID != "" {
				t.Errorf("Expected empty interactive ID in response, got '%s'", concreteResp.InteractiveID)
			}
			if !strings.Contains(concreteResp.Message, "nil request") {
				t.Errorf("Expected error message about nil request, got '%s'", concreteResp.Message)
			}
		}
	})
}

func BenchmarkInteractiveImport(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel) // Reduce log noise during benchmarks
	interactiveRepo := NewInMemoryInteractiveRepository()
	handlers := NewHandlers(logger, interactiveRepo)

	ctx := context.Background()
	req := &api.ImportInteractiveContentRequest{
		InteractiveID: "benchmark-interactive",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		req.InteractiveID = "benchmark-interactive-" + string(rune(i%10+'0'))
		_, _ = handlers.ImportInteractiveContent(ctx, req)
	}
}

func BenchmarkInteractiveList(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)
	interactiveRepo := NewInMemoryInteractiveRepository()
	handlers := NewHandlers(logger, interactiveRepo)

	ctx := context.Background()

	// Pre-populate with some data
	for i := 0; i < 100; i++ {
		req := &api.ImportInteractiveContentRequest{
			InteractiveID: "benchmark-interactive-" + string(rune(i%10+'0')),
		}
		_, _ = handlers.ImportInteractiveContent(ctx, req)
	}

	params := api.ListInteractivesParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListInteractives(ctx, params)
	}
}