// Issue: #1841-#1844 - QA testing for world interactives import
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/pkg/repository"
	"github.com/sirupsen/logrus"
)

func TestInteractiveRepository(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.DebugLevel)
	interactiveRepo := repository.NewInMemoryInteractiveRepository()

	ctx := context.Background()

	// Test saving and retrieving interactives
	t.Run("RepositorySaveAndRetrieve", func(t *testing.T) {
		contentData := map[string]interface{}{
			"type":        "checkpoint",
			"name":        "Test Checkpoint",
			"description": "A test interactive checkpoint",
		}

		saved, err := interactiveRepo.SaveInteractive(ctx, "test-checkpoint", 1, "Test Checkpoint", "A test interactive", "global", models.InteractiveTypeCheckpoint, models.InteractiveStatusActive, contentData)
		if err != nil {
			t.Fatalf("SaveInteractive failed: %v", err)
		}

		if saved.InteractiveID != "test-checkpoint" {
			t.Errorf("Expected interactive ID 'test-checkpoint', got '%s'", saved.InteractiveID)
		}

		if saved.Name != "Test Checkpoint" {
			t.Errorf("Expected name 'Test Checkpoint', got '%s'", saved.Name)
		}

		// Test retrieval - should find our test checkpoint
		filter := &models.ListInteractivesRequest{
			Type: &[]models.InteractiveType{models.InteractiveTypeCheckpoint}[0],
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

	// Test multiple interactives
	t.Run("MultipleInteractives", func(t *testing.T) {
		// Save another interactive
		contentData2 := map[string]interface{}{
			"type":        "terminal",
			"name":        "Test Terminal",
			"description": "A test interactive terminal",
		}

		_, err := interactiveRepo.SaveInteractive(ctx, "test-terminal", 1, "Test Terminal", "A test interactive", "urban", models.InteractiveTypeTerminal, models.InteractiveStatusActive, contentData2)
		if err != nil {
			t.Fatalf("SaveInteractive failed: %v", err)
		}

		// Get all interactives
		filter := &models.ListInteractivesRequest{}
		interactives, total, err := interactiveRepo.GetInteractives(ctx, filter)
		if err != nil {
			t.Fatalf("GetInteractives failed: %v", err)
		}

		if total < 2 {
			t.Errorf("Expected at least 2 interactives, got %d", total)
		}

		// Check both interactives are present
		checkpointFound := false
		terminalFound := false
		for _, interactive := range interactives {
			if interactive.InteractiveID == "test-checkpoint" {
				checkpointFound = true
			}
			if interactive.InteractiveID == "test-terminal" {
				terminalFound = true
			}
		}

		if !checkpointFound {
			t.Error("Expected to find test-checkpoint in results")
		}
		if !terminalFound {
			t.Error("Expected to find test-terminal in results")
		}
	})
}

func BenchmarkInteractiveRepository(b *testing.B) {
	interactiveRepo := repository.NewInMemoryInteractiveRepository()
	ctx := context.Background()

	contentData := map[string]interface{}{
		"type": "benchmark",
		"name": "Benchmark Interactive",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		id := "benchmark-" + string(rune(i%10+'0'))
		_, _ = interactiveRepo.SaveInteractive(ctx, id, 1, "Benchmark", "Benchmark interactive", "global", models.InteractiveTypeCheckpoint, models.InteractiveStatusActive, contentData)
	}
}
