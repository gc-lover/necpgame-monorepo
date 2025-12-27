package service

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/pkg/models"
)

func TestGuildService_ValidateGuild(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name    string
		guild   *models.Guild
		wantErr bool
	}{
		{
			name: "valid guild",
			guild: &models.Guild{
				Name:        "Test Guild",
				Description: "A test guild",
				LeaderID:    uuid.New(),
			},
			wantErr: false,
		},
		{
			name:    "nil guild",
			guild:   nil,
			wantErr: true,
		},
		{
			name: "empty name",
			guild: &models.Guild{
				Name:        "",
				Description: "A test guild",
				LeaderID:    uuid.New(),
			},
			wantErr: true,
		},
		{
			name: "name too long",
			guild: &models.Guild{
				Name:        string(make([]byte, 101)), // 101 characters
				Description: "A test guild",
				LeaderID:    uuid.New(),
			},
			wantErr: true,
		},
		{
			name: "invalid leader ID",
			guild: &models.Guild{
				Name:        "Test Guild",
				Description: "A test guild",
				LeaderID:    uuid.Nil,
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := svc.validateGuild(tt.guild)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestGuildService_ValidateMember(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name   string
		member *models.GuildMember
		wantErr bool
	}{
		{
			name: "valid member",
			member: &models.GuildMember{
				GuildID:  uuid.New(),
				PlayerID: uuid.New(),
				Role:     "member",
			},
			wantErr: false,
		},
		{
			name:    "nil member",
			member:  nil,
			wantErr: true,
		},
		{
			name: "invalid role",
			member: &models.GuildMember{
				GuildID:  uuid.New(),
				PlayerID: uuid.New(),
				Role:     "invalid_role",
			},
			wantErr: true,
		},
		{
			name: "invalid guild ID",
			member: &models.GuildMember{
				GuildID:  uuid.Nil,
				PlayerID: uuid.New(),
				Role:     "member",
			},
			wantErr: true,
		},
		{
			name: "invalid player ID",
			member: &models.GuildMember{
				GuildID:  uuid.New(),
				PlayerID: uuid.Nil,
				Role:     "member",
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := svc.validateMember(tt.member)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestGuildService_CalculateGuildLevel(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name        string
		experience  int64
		expectedLevel int
	}{
		{"level 1", 0, 1},
		{"level 1 with some exp", 500, 1},
		{"level 2", 1000, 2},
		{"level 3", 3000, 3},
		{"level 4", 7000, 4},
		{"high level", 100000, 7},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			level := svc.calculateGuildLevel(tt.experience)
			assert.Equal(t, tt.expectedLevel, level)
		})
	}
}

func TestGuildService_IsValidRole(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	validRoles := []string{"leader", "officer", "member", "recruit"}
	invalidRoles := []string{"", "admin", "moderator", "invalid"}

	for _, role := range validRoles {
		assert.True(t, svc.isValidRole(role), "Role %s should be valid", role)
	}

	for _, role := range invalidRoles {
		assert.False(t, svc.isValidRole(role), "Role %s should be invalid", role)
	}
}

func TestGuildService_IsValidMessageType(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	validTypes := []string{"chat", "announcement", "system"}
	invalidTypes := []string{"", "private", "invalid"}

	for _, msgType := range validTypes {
		assert.True(t, svc.isValidMessageType(msgType), "Message type %s should be valid", msgType)
	}

	for _, msgType := range invalidTypes {
		assert.False(t, svc.isValidMessageType(msgType), "Message type %s should be invalid", msgType)
	}
}

func TestGuildService_CanPromoteMember(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name           string
		currentRole    string
		targetRole     string
		expectedResult bool
	}{
		{"recruit to member", "recruit", "member", true},
		{"member to officer", "member", "officer", true},
		{"officer to leader", "officer", "leader", true},
		{"leader to officer", "leader", "officer", false},
		{"officer to member", "officer", "member", false},
		{"member to leader", "member", "leader", false},
		{"same role", "member", "member", false},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := svc.canPromoteMember(tt.currentRole, tt.targetRole)
			assert.Equal(t, tt.expectedResult, result)
		})
	}
}

func TestGuildService_GetRoleHierarchy(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	hierarchy := svc.getRoleHierarchy()

	expected := map[string]int{
		"recruit":  1,
		"member":   2,
		"officer":  3,
		"leader":   4,
	}

	assert.Equal(t, expected, hierarchy)
}

func TestGuildService_ValidateGuildName(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name    string
		guildName string
		wantErr bool
	}{
		{"valid name", "Test Guild", false},
		{"valid name with numbers", "Guild 2077", false},
		{"valid unicode name", "Гильдия Тестирования", false},
		{"empty name", "", true},
		{"name too short", "A", true},
		{"name too long", string(make([]byte, 101)), true},
		{"name with invalid chars", "Guild@#$%", true},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := svc.validateGuildName(tt.guildName)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

func TestGuildService_ValidateApplication(t *testing.T) {
	logger := zaptest.NewLogger(t)
	svc := NewGuildService(nil, logger)

	tests := []struct {
		name       string
		application *models.GuildApplication
		wantErr    bool
	}{
		{
			name: "valid application",
			application: &models.GuildApplication{
				GuildID:       uuid.New(),
				ApplicantID:   uuid.New(),
				ApplicationText: "I want to join your guild",
			},
			wantErr: false,
		},
		{
			name: "nil application",
			application: nil,
			wantErr: true,
		},
		{
			name: "invalid guild ID",
			application: &models.GuildApplication{
				GuildID:       uuid.Nil,
				ApplicantID:   uuid.New(),
				ApplicationText: "Please accept me",
			},
			wantErr: true,
		},
		{
			name: "invalid applicant ID",
			application: &models.GuildApplication{
				GuildID:       uuid.New(),
				ApplicantID:   uuid.Nil,
				ApplicationText: "Let me in",
			},
			wantErr: true,
		},
		{
			name: "empty application text",
			application: &models.GuildApplication{
				GuildID:       uuid.New(),
				ApplicantID:   uuid.New(),
				ApplicationText: "",
			},
			wantErr: false, // Empty text is allowed
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := svc.validateApplication(tt.application)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

// Benchmark tests for performance validation

func BenchmarkGuildLevelCalculation(b *testing.B) {
	logger := zaptest.NewLogger(b)
	svc := NewGuildService(nil, logger)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = svc.calculateGuildLevel(int64(i * 1000))
	}
}

func BenchmarkRoleValidation(b *testing.B) {
	logger := zaptest.NewLogger(b)
	svc := NewGuildService(nil, logger)

	roles := []string{"leader", "officer", "member", "recruit"}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = svc.isValidRole(roles[i%len(roles)])
	}
}

func BenchmarkGuildNameValidation(b *testing.B) {
	logger := zaptest.NewLogger(b)
	svc := NewGuildService(nil, logger)

	names := []string{
		"Test Guild",
		"Cyber Nomads",
		"Neon Reapers",
		"Data Pirates",
	}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = svc.validateGuildName(names[i%len(names)])
	}
}

// Mock repository for testing
type mockRepository struct{}

func (m *mockRepository) CreateGuild(ctx context.Context, guild *models.Guild) error {
	return nil
}

func (m *mockRepository) GetGuild(ctx context.Context, id uuid.UUID) (*models.Guild, error) {
	return &models.Guild{
		ID:          id,
		Name:        "Test Guild",
		Description: "A test guild",
		LeaderID:    uuid.New(),
		Level:       1,
		Experience:  0,
	}, nil
}

func (m *mockRepository) UpdateGuild(ctx context.Context, guild *models.Guild) error {
	return nil
}

func (m *mockRepository) DeleteGuild(ctx context.Context, id uuid.UUID) error {
	return nil
}

func (m *mockRepository) ListGuilds(ctx context.Context, limit, offset int) ([]*models.Guild, error) {
	return []*models.Guild{}, nil
}

func (m *mockRepository) AddMember(ctx context.Context, member *models.GuildMember) error {
	return nil
}

func (m *mockRepository) RemoveMember(ctx context.Context, guildID, playerID uuid.UUID) error {
	return nil
}

func (m *mockRepository) UpdateMemberRole(ctx context.Context, guildID, playerID uuid.UUID, role string) error {
	return nil
}

func (m *mockRepository) GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]*models.GuildMember, error) {
	return []*models.GuildMember{}, nil
}

func (m *mockRepository) CreateApplication(ctx context.Context, application *models.GuildApplication) error {
	return nil
}

func (m *mockRepository) GetApplication(ctx context.Context, id uuid.UUID) (*models.GuildApplication, error) {
	return &models.GuildApplication{
		ID:             id,
		Status:         "pending",
		ApplicationText: "Test application",
	}, nil
}

func (m *mockRepository) UpdateApplicationStatus(ctx context.Context, id uuid.UUID, status string, reviewedBy uuid.UUID) error {
	return nil
}

func (m *mockRepository) GetGuildApplications(ctx context.Context, guildID uuid.UUID) ([]*models.GuildApplication, error) {
	return []*models.GuildApplication{}, nil
}
