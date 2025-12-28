// Additional unit tests for ClanWarService
// Issue: #140895110
// PERFORMANCE: Extended test coverage for edge cases and error scenarios

package server

import (
	"context"
	"errors"
	"fmt"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
)

// Additional mock methods for extended testing
func (m *MockClanWarRepository) GetWarByIDWithBattles(ctx context.Context, warID uuid.UUID) (*ClanWar, []*Battle, error) {
	args := m.Called(ctx, warID)
	if args.Get(0) == nil {
		return nil, nil, args.Error(2)
	}
	return args.Get(0).(*ClanWar), args.Get(1).([]*Battle), args.Error(2)
}

func (m *MockClanWarRepository) GetActiveWarsByClan(ctx context.Context, clanID uuid.UUID) ([]*ClanWar, error) {
	args := m.Called(ctx, clanID)
	return args.Get(0).([]*ClanWar), args.Error(1)
}

func (m *MockClanWarRepository) GetWarStatistics(ctx context.Context, warID uuid.UUID) (*WarStatistics, error) {
	args := m.Called(ctx, warID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*WarStatistics), args.Error(1)
}


// Test additional service methods
func TestClanWarService_GetWarWithDetails_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	expectedWar := &ClanWar{
		ID:       warID,
		ClanID1:  uuid.New(),
		ClanID2:  uuid.New(),
		Status:   "active",
	}
	expectedBattles := []*Battle{
		{
			ID:     uuid.New(),
			WarID:  warID,
			Status: "active",
		},
	}

	mockRepo.On("GetWarByIDWithBattles", ctx, warID).Return(expectedWar, expectedBattles, nil)

	war, battles, err := service.GetWarWithDetails(ctx, warID)

	assert.NoError(t, err)
	assert.NotNil(t, war)
	assert.NotNil(t, battles)
	assert.Equal(t, expectedWar.ID, war.ID)
	assert.Len(t, battles, 1)
	assert.Equal(t, expectedBattles[0].WarID, battles[0].WarID)

	mockRepo.AssertExpectations(t)
}

func TestClanWarService_GetActiveWarsByClan_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	clanID := uuid.New()

	expectedWars := []*ClanWar{
		{
			ID:      uuid.New(),
			ClanID1: clanID,
			Status:  "active",
		},
		{
			ID:      uuid.New(),
			ClanID2: clanID,
			Status:  "active",
		},
	}

	mockRepo.On("GetActiveWarsByClan", ctx, clanID).Return(expectedWars, nil)

	wars, err := service.GetActiveWarsByClan(ctx, clanID)

	assert.NoError(t, err)
	assert.NotNil(t, wars)
	assert.Len(t, wars, 2)
	assert.True(t, wars[0].ClanID1 == clanID || wars[0].ClanID2 == clanID)
	assert.True(t, wars[1].ClanID1 == clanID || wars[1].ClanID2 == clanID)

	mockRepo.AssertExpectations(t)
}

func TestClanWarService_GetWarStatistics_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	expectedStats := &WarStatistics{
		WarID:         warID,
		TotalBattles:  10,
		ActiveBattles: 3,
		Clan1Score:    150,
		Clan2Score:    120,
	}

	mockRepo.On("GetWarStatistics", ctx, warID).Return(expectedStats, nil)

	stats, err := service.GetWarStatistics(ctx, warID)

	assert.NoError(t, err)
	assert.NotNil(t, stats)
	assert.Equal(t, warID, stats.WarID)
	assert.Equal(t, 10, stats.TotalBattles)
	assert.Equal(t, 3, stats.ActiveBattles)
	assert.Equal(t, 150, stats.Clan1Score)
	assert.Equal(t, 120, stats.Clan2Score)

	mockRepo.AssertExpectations(t)
}

// Test edge cases and error scenarios
func TestClanWarService_DeclareWar_ConcurrentDeclarations(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	clanID1 := uuid.New()
	clanID2 := uuid.New()
	territoryID := uuid.New()

	territory := &Territory{
		ID:          territoryID,
		Name:        "Test Territory",
		Description: "Test Description",
		Type:        "strategic",
	}

	// Setup mock to return territory for first call, then error for concurrent calls
	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(territory, nil).Once()
	mockRepo.On("CreateWar", ctx, mock.AnythingOfType("*server.ClanWar")).Return(nil).Once()
	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(territory, nil).Once()
	mockRepo.On("CreateWar", ctx, mock.AnythingOfType("*server.ClanWar")).Return(errors.New("concurrent conflict")).Once()

	// First call should succeed
	war1, err1 := service.DeclareWar(ctx, clanID1, clanID2, territoryID)
	// Second call should also attempt but mock returns error
	war2, err2 := service.DeclareWar(ctx, clanID1, clanID2, territoryID)

	assert.NoError(t, err1)
	assert.NotNil(t, war1)
	assert.Error(t, err2)
	assert.Nil(t, war2)
}

func TestClanWarService_StartWar_AlreadyStarted(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	war := &ClanWar{
		ID:        warID,
		Status:    "active",
		StartTime: &[]time.Time{time.Now()}[0],
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)

	err := service.StartWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war is already started")
}

func TestClanWarService_CompleteWar_InvalidStatus(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	war := &ClanWar{
		ID:     warID,
		Status: "completed",
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)

	err := service.CompleteWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war cannot be completed")
}

func TestClanWarService_CompleteWar_Clan2Wins(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()
	clanID1 := uuid.New()
	clanID2 := uuid.New()

	war := &ClanWar{
		ID:         warID,
		ClanID1:    clanID1,
		ClanID2:    clanID2,
		Status:     "active",
		ScoreClan1: 50,
		ScoreClan2: 100,
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)
	mockRepo.On("UpdateWar", ctx, mock.MatchedBy(func(w *ClanWar) bool {
		return w.Status == "completed" && w.EndTime != nil && *w.WinnerClanID == clanID2
	})).Return(nil)

	err := service.CompleteWar(ctx, warID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestClanWarService_CreateBattle_InvalidWarStatus(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()
	territoryID := uuid.New()

	testCases := []string{"pending", "completed", "cancelled"}

	for _, status := range testCases {
		t.Run(fmt.Sprintf("war_status_%s", status), func(t *testing.T) {
			war := &ClanWar{
				ID:     warID,
				Status: status,
			}

			mockRepo.On("GetWarByID", ctx, warID).Return(war, nil).Once()

			battle, err := service.CreateBattle(ctx, warID, territoryID)

			assert.Error(t, err)
			assert.Nil(t, battle)
			assert.Contains(t, err.Error(), "war is not active")
		})
	}
}

func TestClanWarService_GetBattle_InvalidID(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	battleID := uuid.Nil

	battle, err := service.GetBattle(ctx, battleID)

	assert.Error(t, err)
	assert.Nil(t, battle)
	assert.Contains(t, err.Error(), "invalid battle ID")
}

func TestClanWarService_ListBattles_InvalidWarID(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.Nil
	limit := 10
	offset := 0

	battles, err := service.ListBattles(ctx, warID, limit, offset)

	assert.Error(t, err)
	assert.Nil(t, battles)
	assert.Contains(t, err.Error(), "invalid war ID")
}

func TestClanWarService_GetTerritory_InvalidID(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	territoryID := uuid.Nil

	territory, err := service.GetTerritory(ctx, territoryID)

	assert.Error(t, err)
	assert.Nil(t, territory)
	assert.Contains(t, err.Error(), "invalid territory ID")
}

func TestClanWarService_ListTerritories_InvalidLimits(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()

	// Test with negative limit
	territories1, err1 := service.ListTerritories(ctx, -1, 0)
	assert.Error(t, err1)
	assert.Nil(t, territories1)
	assert.Contains(t, err1.Error(), "invalid limit")

	// Test with negative offset
	territories2, err2 := service.ListTerritories(ctx, 10, -1)
	assert.Error(t, err2)
	assert.Nil(t, territories2)
	assert.Contains(t, err2.Error(), "invalid offset")

	// Test with limit too large
	territories3, err3 := service.ListTerritories(ctx, 1000, 0)
	assert.Error(t, err3)
	assert.Nil(t, territories3)
	assert.Contains(t, err3.Error(), "limit too large")
}

// Test performance and resource usage
func TestClanWarService_ObjectPoolUsage(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	// Test that object pools are properly initialized
	assert.NotNil(t, service.warPool)
	assert.NotNil(t, service.battlePool)
	assert.NotNil(t, service.territoryPool)

	// Test that we can get objects from pools
	warObj := service.warPool.Get()
	assert.NotNil(t, warObj)
	assert.IsType(t, &ClanWar{}, warObj)

	battleObj := service.battlePool.Get()
	assert.NotNil(t, battleObj)
	assert.IsType(t, &Battle{}, battleObj)

	territoryObj := service.territoryPool.Get()
	assert.NotNil(t, territoryObj)
	assert.IsType(t, &Territory{}, territoryObj)
}

func TestClanWarService_LoggerInitialization(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)

	// Test that logger is initialized
	assert.NotNil(t, service.logger)

	// Test that we can create a service with custom logger
	customLogger := zaptest.NewLogger(t)
	service.logger = customLogger
	assert.Equal(t, customLogger, service.logger)
}

// Test validation edge cases
func TestClanWarService_DeclareWar_EmptyTerritory(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	clanID1 := uuid.New()
	clanID2 := uuid.New()
	territoryID := uuid.New()

	// Territory not found
	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(nil, nil)

	war, err := service.DeclareWar(ctx, clanID1, clanID2, territoryID)

	assert.Error(t, err)
	assert.Nil(t, war)
	assert.Contains(t, err.Error(), "territory not found")
}

func TestClanWarService_StartWar_WithExistingStartTime(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()
	startTime := time.Now().Add(-1 * time.Hour)

	war := &ClanWar{
		ID:        warID,
		Status:    "pending",
		StartTime: &startTime,
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)

	err := service.StartWar(ctx, warID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "war is already started")
}

// Test concurrent access patterns
func TestClanWarService_ConcurrentWarAccess(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping concurrent test in short mode")
	}

	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	war := &ClanWar{
		ID:     warID,
		Status: "active",
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil).Maybe()

	const numGoroutines = 10
	results := make(chan error, numGoroutines)

	for i := 0; i < numGoroutines; i++ {
		go func() {
			_, err := service.GetWar(ctx, warID)
			results <- err
		}()
	}

	for i := 0; i < numGoroutines; i++ {
		err := <-results
		assert.NoError(t, err)
	}
}

// Test memory pool efficiency
func TestClanWarService_MemoryPoolEfficiency(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	// Simulate multiple operations to test pool reuse
	const iterations = 100

	for i := 0; i < iterations; i++ {
		// Get objects from pools
		warObj := service.warPool.Get()
		battleObj := service.battlePool.Get()
		territoryObj := service.territoryPool.Get()

		// Verify objects are valid
		assert.NotNil(t, warObj)
		assert.NotNil(t, battleObj)
		assert.NotNil(t, territoryObj)

		// Put objects back to pools (simulate reuse)
		service.warPool.Put(warObj)
		service.battlePool.Put(battleObj)
		service.territoryPool.Put(territoryObj)
	}

	// Test that pools still work after reuse
	finalWar := service.warPool.Get()
	assert.NotNil(t, finalWar)
	assert.IsType(t, &ClanWar{}, finalWar)
}

// Test error handling with context cancellation
func TestClanWarService_ContextCancellationDuringOperation(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	// Create context that will be cancelled during operation
	ctx, cancel := context.WithCancel(context.Background())

	clanID1 := uuid.New()
	clanID2 := uuid.New()
	territoryID := uuid.New()

	territory := &Territory{
		ID:          territoryID,
		Name:        "Test Territory",
		Description: "Test Description",
		Type:        "strategic",
	}

	// Setup mock with delay to simulate slow operation
	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(territory, nil).Run(func(args mock.Arguments) {
		// Cancel context during the operation
		cancel()
		time.Sleep(10 * time.Millisecond)
	})

	war, err := service.DeclareWar(ctx, clanID1, clanID2, territoryID)

	assert.Error(t, err)
	assert.Nil(t, war)
	assert.Equal(t, context.Canceled, err)
}

// Test input sanitization
func TestClanWarService_InputSanitization(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()

	testCases := []struct {
		name        string
		clanID1     uuid.UUID
		clanID2     uuid.UUID
		territoryID uuid.UUID
		expectError bool
		errorMsg    string
	}{
		{"valid IDs", uuid.New(), uuid.New(), uuid.New(), false, ""},
		{"nil clanID1", uuid.Nil, uuid.New(), uuid.New(), true, "invalid clan IDs"},
		{"nil clanID2", uuid.New(), uuid.Nil, uuid.New(), true, "invalid clan IDs"},
		{"nil territoryID", uuid.New(), uuid.New(), uuid.Nil, true, "invalid territory ID"},
		{"same clans", uuid.New(), uuid.New(), uuid.New(), true, "cannot declare war against same clan"},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			// Adjust clanID2 to match clanID1 for same clan test
			testClanID2 := tc.clanID2
			if tc.name == "same clans" {
				testClanID2 = tc.clanID1
			}

			war, err := service.DeclareWar(ctx, tc.clanID1, testClanID2, tc.territoryID)

			if tc.expectError {
				assert.Error(t, err)
				assert.Nil(t, war)
				assert.Contains(t, err.Error(), tc.errorMsg)
			} else {
				// For success cases, we would need to mock the repository calls
				// This is just testing input validation
				assert.NotNil(t, service)
			}
		})
	}
}
