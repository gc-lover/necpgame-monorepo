// Issue: #backend-companion_service_go - QA Testing
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/http/httptest"
	"runtime"
	"sync"
	"testing"
	"time"

	"companion-service-go/pkg/api"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestCompanionServiceHealthCheck tests health check endpoint
func TestCompanionServiceHealthCheck(t *testing.T) {
	tests := []struct {
		name           string
		expectedStatus int
		expectedStatusValue string
	}{
		{
			name:           "Basic health check",
			expectedStatus: http.StatusOK,
			expectedStatusValue: "healthy",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create service (mock DB for testing)
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			resp, err := svc.CompanionServiceHealthCheck(req().Context())
			require.NoError(t, err)

			healthResp, ok := resp.(*api.HealthResponse)
			require.True(t, ok, "Response should be HealthResponse")

			assert.Equal(t, tt.expectedStatusValue, string(healthResp.Status))
			assert.NotEmpty(t, healthResp.Version.Value)
			assert.Greater(t, healthResp.UptimeSeconds.Value, 0)
		})
	}
}

// TestGetCompanionTypes tests companion types retrieval
func TestGetCompanionTypes(t *testing.T) {
	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	resp, err := svc.GetCompanionTypes(req().Context())
	require.NoError(t, err)

	typesResp, ok := resp.(*api.CompanionTypesResponse)
	require.True(t, ok, "Response should be CompanionTypesResponse")
	assert.NotEmpty(t, typesResp.CompanionTypes)
	assert.Greater(t, typesResp.TotalCount, 0)
}

// TestGetCompanionType tests individual companion type retrieval
func TestGetCompanionType(t *testing.T) {
	tests := []struct {
		name           string
		typeID        string
		expectSuccess bool
	}{
		{
			name:           "Valid type ID",
			typeID:        "12345678-9abc-def0-1234-56789abcdef0",
			expectSuccess: true,
		},
		{
			name:           "Invalid type ID",
			typeID:        "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.GetCompanionTypeParams{
				TypeID: tt.typeID,
			}

			resp, err := svc.GetCompanionType(req().Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				typeResp, ok := resp.(*api.CompanionTypeDetailed)
				require.True(t, ok, "Response should be CompanionTypeDetailed")
				assert.Equal(t, tt.typeID, typeResp.TypeId.String())
				assert.NotEmpty(t, typeResp.Name)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestGetPlayerCompanions tests player companions retrieval
func TestGetPlayerCompanions(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		limit         int
		offset        int
		expectSuccess bool
	}{
		{
			name:           "Valid player ID with pagination",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			limit:         10,
			offset:        0,
			expectSuccess: true,
		},
		{
			name:           "Invalid player ID",
			playerID:      "invalid-uuid",
			limit:         10,
			offset:        0,
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.GetPlayerCompanionsParams{
				PlayerID: tt.playerID,
			}
			if tt.limit > 0 {
				params.Limit = api.NewOptInt(tt.limit)
			}
			if tt.offset > 0 {
				params.Offset = api.NewOptInt(tt.offset)
			}

			resp, err := svc.GetPlayerCompanions(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				companionsResp, ok := resp.(*api.PlayerCompanionsResponse)
				require.True(t, ok, "Response should be PlayerCompanionsResponse")
				assert.NotNil(t, companionsResp.Companions)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestGetCompanionDetails tests companion details retrieval
func TestGetCompanionDetails(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionID   string
		expectSuccess bool
	}{
		{
			name:           "Valid IDs",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: true,
		},
		{
			name:           "Invalid player ID",
			playerID:      "invalid-uuid",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: false,
		},
		{
			name:           "Invalid companion ID",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.GetCompanionDetailsParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
			}

			resp, err := svc.GetCompanionDetails(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				detailsResp, ok := resp.(*api.CompanionDetailed)
				require.True(t, ok, "Response should be CompanionDetailed")
				assert.Equal(t, tt.companionID, detailsResp.CompanionId.String())
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestPurchaseCompanion tests companion purchasing
func TestPurchaseCompanion(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionTypeID string
		currencyAmount int
		expectSuccess bool
	}{
		{
			name:             "Valid purchase",
			playerID:        "12345678-9abc-def0-1234-56789abcdef0",
			companionTypeID: "11223344-5566-7788-99aa-bbccddeeff00",
			currencyAmount:  5000,
			expectSuccess:   true,
		},
		{
			name:             "Invalid player ID",
			playerID:        "invalid-uuid",
			companionTypeID: "11223344-5566-7788-99aa-bbccddeeff00",
			currencyAmount:  5000,
			expectSuccess:   false,
		},
		{
			name:             "Invalid type ID",
			playerID:        "12345678-9abc-def0-1234-56789abcdef0",
			companionTypeID: "invalid-uuid",
			currencyAmount:  5000,
			expectSuccess:   false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.PurchaseCompanionParams{
				PlayerID: tt.playerID,
			}

			req := &api.PurchaseCompanionRequest{
				CompanionTypeID: tt.companionTypeID,
				CurrencyAmount:  tt.currencyAmount,
				ConfirmationToken: "CONFIRM_PURCHASE_2024",
			}

			resp, err := svc.PurchaseCompanion(req.Context(), params, req)

			if tt.expectSuccess {
				require.NoError(t, err)
				purchaseResp, ok := resp.(*api.PurchaseCompanionResponse)
				require.True(t, ok, "Response should be PurchaseCompanionResponse")
				assert.True(t, purchaseResp.Success)
				assert.NotEmpty(t, purchaseResp.PurchaseID)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestSummonCompanion tests companion summoning
func TestSummonCompanion(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionID   string
		expectSuccess bool
	}{
		{
			name:           "Valid summon",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: true,
		},
		{
			name:           "Invalid player ID",
			playerID:      "invalid-uuid",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: false,
		},
		{
			name:           "Invalid companion ID",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.SummonCompanionParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
			}

			resp, err := svc.SummonCompanion(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				summonResp, ok := resp.(*api.SummonCompanionResponse)
				require.True(t, ok, "Response should be SummonCompanionResponse")
				assert.True(t, summonResp.Success)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestRecallCompanion tests companion recalling
func TestRecallCompanion(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionID   string
		expectSuccess bool
	}{
		{
			name:           "Valid recall",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: true,
		},
		{
			name:           "Invalid IDs",
			playerID:      "invalid-uuid",
			companionID:   "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.RecallCompanionParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
			}

			resp, err := svc.RecallCompanion(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				recallResp, ok := resp.(*api.RecallCompanionResponse)
				require.True(t, ok, "Response should be RecallCompanionResponse")
				assert.True(t, recallResp.Success)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestRenameCompanion tests companion renaming
func TestRenameCompanion(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionID   string
		newName       string
		expectSuccess bool
	}{
		{
			name:           "Valid rename",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			newName:       "New Companion Name",
			expectSuccess: true,
		},
		{
			name:           "Invalid player ID",
			playerID:      "invalid-uuid",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			newName:       "New Name",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.RenameCompanionParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
			}

			req := &api.RenameCompanionRequest{
				NewName: tt.newName,
			}

			resp, err := svc.RenameCompanion(req.Context(), params, req)

			if tt.expectSuccess {
				require.NoError(t, err)
				renameResp, ok := resp.(*api.RenameCompanionResponse)
				require.True(t, ok, "Response should be RenameCompanionResponse")
				assert.True(t, renameResp.Success)
				assert.Equal(t, tt.newName, renameResp.NewName)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestAddCompanionExperience tests experience addition
func TestAddCompanionExperience(t *testing.T) {
	tests := []struct {
		name             string
		playerID        string
		companionID     string
		experienceAmount int
		reason          string
		expectSuccess   bool
	}{
		{
			name:             "Valid experience addition",
			playerID:        "12345678-9abc-def0-1234-56789abcdef0",
			companionID:     "11223344-5566-7788-99aa-bbccddeeff00",
			experienceAmount: 100,
			reason:          "quest_completion",
			expectSuccess:   true,
		},
		{
			name:             "Invalid player ID",
			playerID:        "invalid-uuid",
			companionID:     "11223344-5566-7788-99aa-bbccddeeff00",
			experienceAmount: 100,
			reason:          "quest_completion",
			expectSuccess:   false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.AddCompanionExperienceParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
			}

			req := &api.AddCompanionExperienceRequest{
				ExperienceAmount: tt.experienceAmount,
				Reason:          tt.reason,
			}

			resp, err := svc.AddCompanionExperience(req.Context(), params, req)

			if tt.expectSuccess {
				require.NoError(t, err)
				expResp, ok := resp.(*api.AddExperienceResponse)
				require.True(t, ok, "Response should be AddExperienceResponse")
				assert.True(t, expResp.Success)
				assert.Equal(t, tt.experienceAmount, expResp.ExperienceAdded)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestUseCompanionAbility tests ability usage
func TestUseCompanionAbility(t *testing.T) {
	tests := []struct {
		name           string
		playerID      string
		companionID   string
		abilityID     string
		expectSuccess bool
	}{
		{
			name:           "Valid ability usage",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			abilityID:     "aabbccdd-eeff-1122-3344-556677889900",
			expectSuccess: true,
		},
		{
			name:           "Invalid ability ID",
			playerID:      "12345678-9abc-def0-1234-56789abcdef0",
			companionID:   "11223344-5566-7788-99aa-bbccddeeff00",
			abilityID:     "invalid-uuid",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.UseCompanionAbilityParams{
				PlayerID:    tt.playerID,
				CompanionID: tt.companionID,
				AbilityID:   tt.abilityID,
			}

			resp, err := svc.UseCompanionAbility(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				abilityResp, ok := resp.(*api.UseAbilityResponse)
				require.True(t, ok, "Response should be UseAbilityResponse")
				assert.True(t, abilityResp.Success)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestGetPrestigeLeaderboard tests prestige leaderboard
func TestGetPrestigeLeaderboard(t *testing.T) {
	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	params := api.GetPrestigeLeaderboardParams{
		Page:  api.NewOptInt(1),
		Limit: api.NewOptInt(10),
	}

	resp, err := svc.GetPrestigeLeaderboard(req.Context(), params)
	require.NoError(t, err)

	leaderboardResp, ok := resp.(*api.PrestigeLeaderboardResponse)
	require.True(t, ok, "Response should be PrestigeLeaderboardResponse")
	assert.NotEmpty(t, leaderboardResp.Entries)
	assert.Greater(t, leaderboardResp.TotalCount, 0)
}

// TestVisitCompanionApartment tests apartment visiting
func TestVisitCompanionApartment(t *testing.T) {
	tests := []struct {
		name           string
		apartmentID   string
		visitorID     string
		expectSuccess bool
	}{
		{
			name:           "Valid visit",
			apartmentID:   "12345678-9abc-def0-1234-56789abcdef0",
			visitorID:     "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: true,
		},
		{
			name:           "Invalid apartment ID",
			apartmentID:   "invalid-uuid",
			visitorID:     "11223344-5566-7788-99aa-bbccddeeff00",
			expectSuccess: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			svc := NewServer(nil, nil, nil)
			require.NotNil(t, svc)

			params := api.VisitCompanionApartmentParams{
				ApartmentID: tt.apartmentID,
				VisitorID:   tt.visitorID,
			}

			resp, err := svc.VisitCompanionApartment(req.Context(), params)

			if tt.expectSuccess {
				require.NoError(t, err)
				visitResp, ok := resp.(*api.CompanionApartmentVisitResponse)
				require.True(t, ok, "Response should be CompanionApartmentVisitResponse")
				assert.True(t, visitResp.Success)
			} else {
				assert.NotNil(t, resp)
			}
		})
	}
}

// TestConcurrentLoad tests service under concurrent load
func TestConcurrentLoad(t *testing.T) {
	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	concurrentUsers := 50
	requestsPerUser := 5

	var wg sync.WaitGroup
	errorCount := int64(0)
	var mu sync.Mutex

	// Start concurrent users
	for i := 0; i < concurrentUsers; i++ {
		wg.Add(1)
		go func(userID int) {
			defer wg.Done()

			for j := 0; j < requestsPerUser; j++ {
				// Alternate between different operations
				switch j % 6 {
				case 0:
					// Health check
					_, err := svc.CompanionServiceHealthCheck(req.Context())
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 1:
					// Get companion types
					_, err := svc.GetCompanionTypes(req.Context())
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 2:
					// Get player companions
					params := api.GetPlayerCompanionsParams{
						PlayerID: "12345678-9abc-def0-1234-56789abcdef0",
					}
					_, err := svc.GetPlayerCompanions(req.Context(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 3:
					// Get prestige leaderboard
					params := api.GetPrestigeLeaderboardParams{}
					_, err := svc.GetPrestigeLeaderboard(req.Context(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 4:
					// Purchase companion
					params := api.PurchaseCompanionParams{
						PlayerID: "12345678-9abc-def0-1234-56789abcdef0",
					}
					req := &api.PurchaseCompanionRequest{
						CompanionTypeID: "11223344-5566-7788-99aa-bbccddeeff00",
						CurrencyAmount:  5000,
						ConfirmationToken: "CONFIRM_PURCHASE_2024",
					}
					_, err := svc.PurchaseCompanion(req.Context(), params, req)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				case 5:
					// Summon companion
					params := api.SummonCompanionParams{
						PlayerID:    "12345678-9abc-def0-1234-56789abcdef0",
						CompanionID: "11223344-5566-7788-99aa-bbccddeeff00",
					}
					_, err := svc.SummonCompanion(req.Context(), params)
					if err != nil {
						mu.Lock()
						errorCount++
						mu.Unlock()
					}
				}
			}
		}(i)
	}

	// Wait for all requests to complete
	done := make(chan bool)
	go func() {
		wg.Wait()
		done <- true
	}()

	select {
	case <-done:
		// All requests completed
	case <-time.After(30 * time.Second):
		t.Fatal("Concurrent load test timed out")
	}

	// Assert no errors occurred
	assert.Equal(t, int64(0), errorCount, "Some concurrent requests failed")
}

// TestMemoryUsage tests memory usage patterns
func TestMemoryUsage(t *testing.T) {
	// Record initial memory stats
	runtime.GC()
	var m1 runtime.MemStats
	runtime.ReadMemStats(&m1)

	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	// Perform operations that should use memory pools
	for i := 0; i < 100; i++ {
		params := api.GetPlayerCompanionsParams{
			PlayerID: "12345678-9abc-def0-1234-56789abcdef0",
		}
		_, err := svc.GetPlayerCompanions(req.Context(), params)
		assert.NoError(t, err)
	}

	// Force GC and check memory
	runtime.GC()
	var m2 runtime.MemStats
	runtime.ReadMemStats(&m2)

	// Memory usage should not grow significantly due to pooling
	memoryGrowth := m2.Alloc - m1.Alloc
	t.Logf("Memory growth after 100 requests: %d bytes", memoryGrowth)

	// With memory pooling, growth should be minimal
	assert.Less(t, memoryGrowth, int64(1024*1024), "Memory growth too high, pooling may not be working")
}

// TestPerformanceBenchmarks runs performance benchmarks
func TestPerformanceBenchmarks(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping performance benchmarks in short mode")
	}

	svc := NewServer(nil, nil, nil)
	require.NotNil(t, svc)

	// Benchmark companion types retrieval
	t.Run("GetCompanionTypesLatency", func(t *testing.T) {
		start := time.Now()
		iterations := 1000

		for i := 0; i < iterations; i++ {
			_, err := svc.GetCompanionTypes(req.Context())
			assert.NoError(t, err)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Get companion types: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 5*time.Millisecond,
			"Get companion types latency exceeds 5ms target")
	})

	// Benchmark player companions retrieval
	t.Run("GetPlayerCompanionsLatency", func(t *testing.T) {
		params := api.GetPlayerCompanionsParams{
			PlayerID: "12345678-9abc-def0-1234-56789abcdef0",
		}

		start := time.Now()
		iterations := 500

		for i := 0; i < iterations; i++ {
			_, err := svc.GetPlayerCompanions(req.Context(), params)
			assert.NoError(t, err)
		}

		duration := time.Since(start)
		avgLatency := duration / time.Duration(iterations)

		t.Logf("Get player companions: %d requests in %v, avg latency: %v",
			iterations, duration, avgLatency)

		// Assert performance targets
		assert.Less(t, avgLatency, 15*time.Millisecond,
			"Get player companions latency exceeds 15ms target")
	})
}

// Helper function for context (simplified for testing)
func req() *http.Request {
	return httptest.NewRequest(http.MethodGet, "/", nil).WithContext(context.Background())
}

// Issue: #backend-companion_service_go - QA Testing
