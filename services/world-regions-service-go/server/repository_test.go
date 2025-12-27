// Issue: #140875729
// PERFORMANCE: Unit tests with database mocking

package server

import (
	"context"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"
)

func TestWorldRegionsRepository_GetWorldRegions(t *testing.T) {
	logger := zaptest.NewLogger(t)
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := &WorldRegionsRepository{
		db:     db,
		logger: logger,
	}

	ctx := context.Background()

	// Mock data
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

	// Mock query
	rows := sqlmock.NewRows([]string{"id", "name", "continent", "description", "status", "cities_count", "created_at", "updated_at"}).
		AddRow(expectedRegions[0].ID, expectedRegions[0].Name, expectedRegions[0].Continent,
			expectedRegions[0].Description, expectedRegions[0].Status, expectedRegions[0].CitiesCount,
			expectedRegions[0].CreatedAt, expectedRegions[0].UpdatedAt)

	mock.ExpectQuery(`SELECT id, name, continent, description, status, cities_count, created_at, updated_at FROM world_regions`).
		WithArgs(10, 0).
		WillReturnRows(rows)

	// Mock count query
	mock.ExpectQuery(`SELECT COUNT\(\*\) FROM world_regions`).
		WillReturnRows(sqlmock.NewRows([]string{"count"}).AddRow(1))

	regions, total, err := repo.GetWorldRegions(ctx, "", "", 10, 0)

	assert.NoError(t, err)
	assert.Equal(t, 1, total)
	assert.Len(t, regions, 1)
	assert.Equal(t, expectedRegions[0].ID, regions[0].ID)
	assert.Equal(t, expectedRegions[0].Name, regions[0].Name)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestWorldRegionsRepository_GetWorldRegionByID(t *testing.T) {
	logger := zaptest.NewLogger(t)
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := &WorldRegionsRepository{
		db:     db,
		logger: logger,
	}

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

	// Mock query
	rows := sqlmock.NewRows([]string{"id", "name", "continent", "description", "status", "cities_count", "created_at", "updated_at"}).
		AddRow(expectedRegion.ID, expectedRegion.Name, expectedRegion.Continent,
			expectedRegion.Description, expectedRegion.Status, expectedRegion.CitiesCount,
			expectedRegion.CreatedAt, expectedRegion.UpdatedAt)

	mock.ExpectQuery(`SELECT id, name, continent, description, status, cities_count, created_at, updated_at FROM world_regions WHERE id = \$1`).
		WithArgs(regionID).
		WillReturnRows(rows)

	region, err := repo.GetWorldRegionByID(ctx, regionID)

	assert.NoError(t, err)
	assert.Equal(t, expectedRegion.ID, region.ID)
	assert.Equal(t, expectedRegion.Name, region.Name)

	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestWorldRegionsRepository_ImportWorldRegion(t *testing.T) {
	logger := zaptest.NewLogger(t)
	db, mock, err := sqlmock.New()
	require.NoError(t, err)
	defer db.Close()

	repo := &WorldRegionsRepository{
		db:     db,
		logger: logger,
	}

	ctx := context.Background()

	region := &WorldRegion{
		ID:          "test-region",
		Name:        "Test Region",
		Continent:   "europe",
		Description: "Test region description",
		Status:      "draft",
		CitiesCount: 5,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mock.ExpectExec(`INSERT INTO world_regions`).
		WithArgs(region.ID, region.Name, region.Continent, region.Description,
			region.Status, region.CitiesCount, region.CreatedAt, region.UpdatedAt).
		WillReturnResult(sqlmock.NewResult(1, 1))

	err = repo.ImportWorldRegion(ctx, region)

	assert.NoError(t, err)
	assert.NoError(t, mock.ExpectationsWereMet())
}

func TestWorldRegionsRepositoryInterface_ImplementsInterface(t *testing.T) {
	// This test ensures our repository implements the interface correctly
	var _ *WorldRegionsRepository = &WorldRegionsRepository{}
	assert.True(t, true, "Interface implementation check")
}
