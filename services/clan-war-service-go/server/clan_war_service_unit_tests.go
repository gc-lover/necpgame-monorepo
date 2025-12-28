// Basic unit tests for ClanWarService - Happy Path Tests
// Issue: #140895110
// PERFORMANCE: Tests validate core service functionality with mocks

package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
)

// TestClanWarService_DeclareWar_Success tests successful war declaration
func TestClanWarService_DeclareWar_Success(t *testing.T) {
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

	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(territory, nil)
	mockRepo.On("CreateWar", ctx, mock.MatchedBy(func(w *ClanWar) bool {
		return w.ClanID1 == clanID1 && w.ClanID2 == clanID2 && w.TerritoryID == territoryID && w.Status == "pending"
	})).Return(nil)

	war, err := service.DeclareWar(ctx, clanID1, clanID2, territoryID)

	assert.NoError(t, err)
	assert.NotNil(t, war)
	assert.Equal(t, clanID1, war.ClanID1)
	assert.Equal(t, clanID2, war.ClanID2)
	assert.Equal(t, territoryID, war.TerritoryID)
	assert.Equal(t, "pending", war.Status)
	assert.Equal(t, 0, war.ScoreClan1)
	assert.Equal(t, 0, war.ScoreClan2)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_GetWar_Success tests successful war retrieval
func TestClanWarService_GetWar_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	expectedWar := &ClanWar{
		ID:          warID,
		ClanID1:     uuid.New(),
		ClanID2:     uuid.New(),
		Status:      "active",
		TerritoryID: uuid.New(),
		ScoreClan1:  100,
		ScoreClan2:  80,
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(expectedWar, nil)

	war, err := service.GetWar(ctx, warID)

	assert.NoError(t, err)
	assert.NotNil(t, war)
	assert.Equal(t, expectedWar.ID, war.ID)
	assert.Equal(t, expectedWar.Status, war.Status)
	assert.Equal(t, expectedWar.ScoreClan1, war.ScoreClan1)
	assert.Equal(t, expectedWar.ScoreClan2, war.ScoreClan2)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListWars_Success tests successful wars listing
func TestClanWarService_ListWars_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	limit := 10
	offset := 0

	expectedWars := []*ClanWar{
		{
			ID:          uuid.New(),
			ClanID1:     uuid.New(),
			ClanID2:     uuid.New(),
			Status:      "pending",
			TerritoryID: uuid.New(),
			ScoreClan1:  0,
			ScoreClan2:  0,
		},
		{
			ID:          uuid.New(),
			ClanID1:     uuid.New(),
			ClanID2:     uuid.New(),
			Status:      "active",
			TerritoryID: uuid.New(),
			ScoreClan1:  50,
			ScoreClan2:  30,
		},
	}

	mockRepo.On("ListWars", ctx, limit, offset).Return(expectedWars, nil)

	wars, err := service.ListWars(ctx, limit, offset)

	assert.NoError(t, err)
	assert.NotNil(t, wars)
	assert.Len(t, wars, 2)
	assert.Equal(t, expectedWars[0].ID, wars[0].ID)
	assert.Equal(t, expectedWars[1].ID, wars[1].ID)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_StartWar_Success tests successful war start
func TestClanWarService_StartWar_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	war := &ClanWar{
		ID:     warID,
		Status: "pending",
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)
	mockRepo.On("UpdateWar", ctx, mock.MatchedBy(func(w *ClanWar) bool {
		return w.Status == "active" && w.StartTime != nil
	})).Return(nil)

	err := service.StartWar(ctx, warID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

// TestClanWarService_CompleteWar_Success tests successful war completion with Clan1 win
func TestClanWarService_CompleteWar_Success(t *testing.T) {
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
		ScoreClan1: 150,
		ScoreClan2: 100,
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)
	mockRepo.On("UpdateWar", ctx, mock.MatchedBy(func(w *ClanWar) bool {
		return w.Status == "completed" && w.EndTime != nil && *w.WinnerClanID == clanID1
	})).Return(nil)

	err := service.CompleteWar(ctx, warID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

// TestClanWarService_CreateBattle_Success tests successful battle creation
func TestClanWarService_CreateBattle_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()
	territoryID := uuid.New()

	war := &ClanWar{
		ID:     warID,
		Status: "active",
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)
	mockRepo.On("CreateBattle", ctx, mock.MatchedBy(func(b *Battle) bool {
		return b.WarID == warID && b.TerritoryID == territoryID && b.Status == "pending"
	})).Return(nil)

	battle, err := service.CreateBattle(ctx, warID, territoryID)

	assert.NoError(t, err)
	assert.NotNil(t, battle)
	assert.Equal(t, warID, battle.WarID)
	assert.Equal(t, territoryID, battle.TerritoryID)
	assert.Equal(t, "pending", battle.Status)
	assert.Equal(t, 0, battle.ScoreClan1)
	assert.Equal(t, 0, battle.ScoreClan2)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_GetBattle_Success tests successful battle retrieval
func TestClanWarService_GetBattle_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	battleID := uuid.New()

	expectedBattle := &Battle{
		ID:          battleID,
		WarID:       uuid.New(),
		TerritoryID: uuid.New(),
		Status:      "active",
		ScoreClan1:  25,
		ScoreClan2:  20,
	}

	mockRepo.On("GetBattleByID", ctx, battleID).Return(expectedBattle, nil)

	battle, err := service.GetBattle(ctx, battleID)

	assert.NoError(t, err)
	assert.NotNil(t, battle)
	assert.Equal(t, expectedBattle.ID, battle.ID)
	assert.Equal(t, expectedBattle.Status, battle.Status)
	assert.Equal(t, expectedBattle.ScoreClan1, battle.ScoreClan1)
	assert.Equal(t, expectedBattle.ScoreClan2, battle.ScoreClan2)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListBattles_Success tests successful battles listing
func TestClanWarService_ListBattles_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()
	limit := 10
	offset := 0

	expectedBattles := []*Battle{
		{
			ID:          uuid.New(),
			WarID:       warID,
			TerritoryID: uuid.New(),
			Status:      "pending",
			ScoreClan1:  0,
			ScoreClan2:  0,
		},
		{
			ID:          uuid.New(),
			WarID:       warID,
			TerritoryID: uuid.New(),
			Status:      "active",
			ScoreClan1:  10,
			ScoreClan2:  15,
		},
	}

	mockRepo.On("ListBattles", ctx, warID, limit, offset).Return(expectedBattles, nil)

	battles, err := service.ListBattles(ctx, warID, limit, offset)

	assert.NoError(t, err)
	assert.NotNil(t, battles)
	assert.Len(t, battles, 2)
	assert.Equal(t, warID, battles[0].WarID)
	assert.Equal(t, warID, battles[1].WarID)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_GetTerritory_Success tests successful territory retrieval
func TestClanWarService_GetTerritory_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	territoryID := uuid.New()

	expectedTerritory := &Territory{
		ID:          territoryID,
		Name:        "Strategic Point Alpha",
		Description: "Critical territory with high strategic value",
		Type:        "strategic",
		OwnerClanID: nil, // Neutral territory
	}

	mockRepo.On("GetTerritoryByID", ctx, territoryID).Return(expectedTerritory, nil)

	territory, err := service.GetTerritory(ctx, territoryID)

	assert.NoError(t, err)
	assert.NotNil(t, territory)
	assert.Equal(t, expectedTerritory.ID, territory.ID)
	assert.Equal(t, expectedTerritory.Name, territory.Name)
	assert.Equal(t, expectedTerritory.Type, territory.Type)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListTerritories_Success tests successful territories listing
func TestClanWarService_ListTerritories_Success(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	limit := 20
	offset := 0

	expectedTerritories := []*Territory{
		{
			ID:          uuid.New(),
			Name:        "Northern Fortress",
			Description: "Heavily defended northern position",
			Type:        "fortress",
		},
		{
			ID:          uuid.New(),
			Name:        "Eastern Plains",
			Description: "Open battlefield area",
			Type:        "plains",
		},
		{
			ID:          uuid.New(),
			Name:        "Southern Mountains",
			Description: "Difficult terrain with strategic passes",
			Type:        "mountains",
		},
	}

	mockRepo.On("ListTerritories", ctx, limit, offset).Return(expectedTerritories, nil)

	territories, err := service.ListTerritories(ctx, limit, offset)

	assert.NoError(t, err)
	assert.NotNil(t, territories)
	assert.Len(t, territories, 3)
	assert.Equal(t, expectedTerritories[0].Name, territories[0].Name)
	assert.Equal(t, expectedTerritories[1].Type, territories[1].Type)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_CompleteWar_Tie tests war completion with tie result
func TestClanWarService_CompleteWar_Tie(t *testing.T) {
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
		ScoreClan1: 100,
		ScoreClan2: 100, // Equal scores = tie
	}

	mockRepo.On("GetWarByID", ctx, warID).Return(war, nil)
	mockRepo.On("UpdateWar", ctx, mock.MatchedBy(func(w *ClanWar) bool {
		return w.Status == "completed" && w.EndTime != nil && w.WinnerClanID == nil
	})).Return(nil)

	err := service.CompleteWar(ctx, warID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListWars_DefaultLimits tests default limit application
func TestClanWarService_ListWars_DefaultLimits(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()

	// Test with invalid limits - should use defaults
	expectedWars := []*ClanWar{}
	mockRepo.On("ListWars", ctx, 20, 0).Return(expectedWars, nil) // Default limit is 20

	wars, err := service.ListWars(ctx, -1, -1) // Invalid limits

	assert.NoError(t, err)
	assert.NotNil(t, wars)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListBattles_DefaultLimits tests default limit application for battles
func TestClanWarService_ListBattles_DefaultLimits(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()
	warID := uuid.New()

	// Test with invalid limits - should use defaults
	expectedBattles := []*Battle{}
	mockRepo.On("ListBattles", ctx, warID, 20, 0).Return(expectedBattles, nil) // Default limit is 20

	battles, err := service.ListBattles(ctx, warID, 150, -5) // Invalid limits

	assert.NoError(t, err)
	assert.NotNil(t, battles)

	mockRepo.AssertExpectations(t)
}

// TestClanWarService_ListTerritories_DefaultLimits tests default limit application for territories
func TestClanWarService_ListTerritories_DefaultLimits(t *testing.T) {
	mockRepo := new(MockClanWarRepository)
	service := NewClanWarService(mockRepo)
	service.logger = zaptest.NewLogger(t)

	ctx := context.Background()

	// Test with invalid limits - should use defaults
	expectedTerritories := []*Territory{}
	mockRepo.On("ListTerritories", ctx, 20, 0).Return(expectedTerritories, nil) // Default limit is 20

	territories, err := service.ListTerritories(ctx, 0, -1) // Invalid limits

	assert.NoError(t, err)
	assert.NotNil(t, territories)

	mockRepo.AssertExpectations(t)
}

// Issue: #140895110
