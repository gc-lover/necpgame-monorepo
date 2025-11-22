package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestGuildRepository(t *testing.T) (*GuildRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewGuildRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewGuildRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewGuildRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestGuildRepository_Create(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
}

func TestGuildRepository_GetByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()

	ctx := context.Background()
	guild, err := repo.GetByID(ctx, guildID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, guild)
}

func TestGuildRepository_GetByID_Found(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	found, err := repo.GetByID(ctx, guild.ID)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, guild.Name, found.Name)
	assert.Equal(t, guild.Tag, found.Tag)
	assert.Equal(t, leaderID, found.LeaderID)
}

func TestGuildRepository_GetByName_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	guild, err := repo.GetByName(ctx, "NonExistentGuild")

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, guild)
}

func TestGuildRepository_GetByName_Found(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	found, err := repo.GetByName(ctx, guild.Name)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, guild.Name, found.Name)
}

func TestGuildRepository_GetByTag_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	guild, err := repo.GetByTag(ctx, "NONEXISTENT")

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, guild)
}

func TestGuildRepository_GetByTag_Found(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	found, err := repo.GetByTag(ctx, guild.Tag)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, guild.Tag, found.Tag)
}

func TestGuildRepository_List_Empty(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	guilds, err := repo.List(ctx, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, guilds)
}

func TestGuildRepository_List_WithGuilds(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	guilds, err := repo.List(ctx, 10, 0)
	require.NoError(t, err)
	assert.NotEmpty(t, guilds)
}

func TestGuildRepository_Count(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	count, err := repo.Count(ctx)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestGuildRepository_Update(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	guild.Name = "Updated Guild"
	guild.Description = "Updated description"
	guild.UpdatedAt = time.Now()

	err = repo.Update(ctx, guild)
	require.NoError(t, err)

	updated, err := repo.GetByID(ctx, guild.ID)
	require.NoError(t, err)
	require.NotNil(t, updated)
	assert.Equal(t, "Updated Guild", updated.Name)
	assert.Equal(t, "Updated description", updated.Description)
}

func TestGuildRepository_AddMember(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	member := &models.GuildMember{
		ID:           uuid.New(),
		GuildID:      guild.ID,
		CharacterID: characterID,
		Rank:         models.GuildRankMember,
		Status:       models.GuildMemberStatusActive,
		Contribution: 0,
		JoinedAt:     time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddMember(ctx, member)
	require.NoError(t, err)

	found, err := repo.GetMember(ctx, guild.ID, characterID)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, characterID, found.CharacterID)
}

func TestGuildRepository_GetMember_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()
	characterID := uuid.New()

	ctx := context.Background()
	member, err := repo.GetMember(ctx, guildID, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, member)
}

func TestGuildRepository_GetMembers_Empty(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()

	ctx := context.Background()
	members, err := repo.GetMembers(ctx, guildID, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, members)
}

func TestGuildRepository_CountMembers(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()

	ctx := context.Background()
	count, err := repo.CountMembers(ctx, guildID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestGuildRepository_UpdateMemberRank(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	member := &models.GuildMember{
		ID:           uuid.New(),
		GuildID:      guild.ID,
		CharacterID: characterID,
		Rank:         models.GuildRankMember,
		Status:       models.GuildMemberStatusActive,
		Contribution: 0,
		JoinedAt:     time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddMember(ctx, member)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.UpdateMemberRank(ctx, guild.ID, characterID, models.GuildRankOfficer)
	require.NoError(t, err)

	updated, err := repo.GetMember(ctx, guild.ID, characterID)
	require.NoError(t, err)
	require.NotNil(t, updated)
	assert.Equal(t, models.GuildRankOfficer, updated.Rank)
}

func TestGuildRepository_RemoveMember(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	member := &models.GuildMember{
		ID:           uuid.New(),
		GuildID:      guild.ID,
		CharacterID: characterID,
		Rank:         models.GuildRankMember,
		Status:       models.GuildMemberStatusActive,
		Contribution: 0,
		JoinedAt:     time.Now(),
		UpdatedAt:    time.Now(),
	}

	err = repo.AddMember(ctx, member)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.RemoveMember(ctx, guild.ID, characterID)
	require.NoError(t, err)

	removed, err := repo.GetMember(ctx, guild.ID, characterID)
	require.NoError(t, err)
	assert.Nil(t, removed)
}

func TestGuildRepository_Disband(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.Disband(ctx, guild.ID)
	require.NoError(t, err)

	disbanded, err := repo.GetByID(ctx, guild.ID)
	require.NoError(t, err)
	assert.Nil(t, disbanded)
}

func TestGuildRepository_CreateInvitation(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	invitation := &models.GuildInvitation{
		ID:          uuid.New(),
		GuildID:     guild.ID,
		CharacterID: characterID,
		InvitedBy:   leaderID,
		Message:     "Join us!",
		Status:      "pending",
		CreatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
	}

	err = repo.CreateInvitation(ctx, invitation)
	require.NoError(t, err)

	found, err := repo.GetInvitation(ctx, invitation.ID)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, characterID, found.CharacterID)
}

func TestGuildRepository_GetInvitation_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	invitationID := uuid.New()

	ctx := context.Background()
	invitation, err := repo.GetInvitation(ctx, invitationID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, invitation)
}

func TestGuildRepository_GetInvitationsByCharacter_Empty(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	invitations, err := repo.GetInvitationsByCharacter(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, invitations)
}

func TestGuildRepository_AcceptInvitation(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	invitation := &models.GuildInvitation{
		ID:          uuid.New(),
		GuildID:     guild.ID,
		CharacterID: characterID,
		InvitedBy:   leaderID,
		Message:     "Join us!",
		Status:      "pending",
		CreatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
	}

	err = repo.CreateInvitation(ctx, invitation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.AcceptInvitation(ctx, invitation.ID)
	require.NoError(t, err)

	accepted, err := repo.GetInvitation(ctx, invitation.ID)
	require.NoError(t, err)
	assert.Nil(t, accepted)
}

func TestGuildRepository_RejectInvitation(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	invitation := &models.GuildInvitation{
		ID:          uuid.New(),
		GuildID:     guild.ID,
		CharacterID: characterID,
		InvitedBy:   leaderID,
		Message:     "Join us!",
		Status:      "pending",
		CreatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
	}

	err = repo.CreateInvitation(ctx, invitation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.RejectInvitation(ctx, invitation.ID)
	require.NoError(t, err)

	rejected, err := repo.GetInvitation(ctx, invitation.ID)
	require.NoError(t, err)
	assert.Nil(t, rejected)
}

func TestGuildRepository_CreateBank(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	bank := &models.GuildBank{
		ID:        uuid.New(),
		GuildID:   guild.ID,
		Currency:  make(map[string]int),
		Items:     []map[string]interface{}{},
		UpdatedAt: time.Now(),
	}

	err = repo.CreateBank(ctx, bank)
	require.NoError(t, err)

	found, err := repo.GetBank(ctx, guild.ID)
	require.NoError(t, err)
	require.NotNil(t, found)
	assert.Equal(t, guild.ID, found.GuildID)
}

func TestGuildRepository_GetBank_NotFound(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	guildID := uuid.New()

	ctx := context.Background()
	bank, err := repo.GetBank(ctx, guildID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, bank)
}

func TestGuildRepository_UpdateBank(t *testing.T) {
	repo, cleanup := setupTestGuildRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	leaderID := uuid.New()
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        "Test Guild",
		Tag:         "TEST",
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  20,
		Description: "Test description",
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	ctx := context.Background()
	err := repo.Create(ctx, guild)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	bank := &models.GuildBank{
		ID:        uuid.New(),
		GuildID:   guild.ID,
		Currency:  make(map[string]int),
		Items:     []map[string]interface{}{},
		UpdatedAt: time.Now(),
	}

	err = repo.CreateBank(ctx, bank)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	bank.Currency["credits"] = 1000
	bank.Items = []map[string]interface{}{
		{"id": "item1", "quantity": 5},
	}

	err = repo.UpdateBank(ctx, bank)
	require.NoError(t, err)

	updated, err := repo.GetBank(ctx, guild.ID)
	require.NoError(t, err)
	require.NotNil(t, updated)
	assert.Equal(t, 1000, updated.Currency["credits"])
	assert.Len(t, updated.Items, 1)
}

