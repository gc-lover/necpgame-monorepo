// Issue: #140875729
// PERFORMANCE: Unit tests for business logic

package server

import (
	"context"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
)

// MockWorldRegionsRepository mocks the repository interface
type MockWorldRegionsRepository struct {
	mock.Mock
}

func (m *MockWorldRegionsRepository) GetWorldRegions(ctx context.Context, status, continent string, limit, offset int) ([]*WorldRegion, int, error) {
	args := m.Called(ctx, status, continent, limit, offset)
	return args.Get(0).([]*WorldRegion), args.Int(1), args.Error(2)
}

func (m *MockWorldRegionsRepository) GetWorldRegionByID(ctx context.Context, regionID string) (*WorldRegion, error) {
	args := m.Called(ctx, regionID)
	return args.Get(0).(*WorldRegion), args.Error(1)
}

func (m *MockWorldRegionsRepository) GetRegionTimeline(ctx context.Context, regionID string, periodStart, periodEnd int) ([]*TimelineEvent, error) {
	args := m.Called(ctx, regionID, periodStart, periodEnd)
	return args.Get(0).([]*TimelineEvent), args.Error(1)
}

func (m *MockWorldRegionsRepository) ImportWorldRegion(ctx context.Context, region *WorldRegion) error {
	args := m.Called(ctx, region)
	return args.Error(0)
}

func (m *MockWorldRegionsRepository) Close() {
	// Mock implementation
}

func TestWorldRegionsService_GetWorldRegions_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	ctx := context.Background()

	expectedRegions := []*WorldRegion{
		{
			ID:          "africa-2020-2093",
			Name:        "Africa",
			Continent:   "africa",
			Description: "African continent regions",
			Status:      "approved",
			CitiesCount: 25,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("GetWorldRegions", ctx, "", "", 10, 0).Return(expectedRegions, 1, nil)

	regions, total, err := service.GetWorldRegions(ctx, "", "", 10, 0)

	assert.NoError(t, err)
	assert.Equal(t, 1, total)
	assert.Len(t, regions, 1)
	assert.Equal(t, expectedRegions[0].ID, regions[0].ID)

	mockRepo.AssertExpectations(t)
}

func TestWorldRegionsService_GetWorldRegion_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	ctx := context.Background()
	regionID := "africa-2020-2093"

	expectedRegion := &WorldRegion{
		ID:          regionID,
		Name:        "Africa",
		Continent:   "africa",
		Description: "African continent regions",
		Status:      "approved",
		CitiesCount: 25,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetWorldRegionByID", ctx, regionID).Return(expectedRegion, nil)

	region, err := service.GetWorldRegion(ctx, regionID)

	assert.NoError(t, err)
	assert.Equal(t, expectedRegion.ID, region.ID)
	assert.Equal(t, expectedRegion.Name, region.Name)

	mockRepo.AssertExpectations(t)
}

func TestWorldRegionsService_GetRegionTimeline_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	ctx := context.Background()
	regionID := "africa-2020-2093"

	expectedEvents := []*TimelineEvent{
		{
			RegionID:    regionID,
			Period:      "2020-2029",
			Title:       "Corporate Anclaves",
			Description: "Formation of corporate enclaves in Africa",
		},
	}

	mockRepo.On("GetRegionTimeline", ctx, regionID, 2020, 2030).Return(expectedEvents, nil)

	events, err := service.GetRegionTimeline(ctx, regionID, 2020, 2030)

	assert.NoError(t, err)
	assert.Len(t, events, 1)
	assert.Equal(t, expectedEvents[0].Period, events[0].Period)

	mockRepo.AssertExpectations(t)
}

func TestWorldRegionsService_ValidateRegionData_Valid(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	region := &WorldRegion{
		ID:        "test-region",
		Name:      "Test Region",
		Continent: "europe",
		Status:    "approved",
	}

	err := service.ValidateRegionData(region)

	assert.NoError(t, err)
}

func TestWorldRegionsService_ValidateRegionData_InvalidID(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	region := &WorldRegion{
		ID:        "",
		Name:      "Test Region",
		Continent: "europe",
		Status:    "approved",
	}

	err := service.ValidateRegionData(region)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "region ID is required")
}

func TestWorldRegionsService_ValidateRegionData_InvalidContinent(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	region := &WorldRegion{
		ID:        "test-region",
		Name:      "Test Region",
		Continent: "invalid-continent",
		Status:    "approved",
	}

	err := service.ValidateRegionData(region)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "invalid continent")
}

func TestWorldRegionsService_ValidateRegionData_InvalidStatus(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockWorldRegionsRepository{}
	service := NewWorldRegionsService(mockRepo)

	region := &WorldRegion{
		ID:        "test-region",
		Name:      "Test Region",
		Continent: "europe",
		Status:    "invalid-status",
	}

	err := service.ValidateRegionData(region)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "invalid status")
}
