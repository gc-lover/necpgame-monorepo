package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/gc-lover/necpgame/services/party-core-service-go/pkg/models"
)

// PartyRepository интерфейс для работы с группами в БД
type PartyRepository interface {
	CreateParty(ctx context.Context, party *models.Party) error
	GetPartyByID(ctx context.Context, id uuid.UUID) (*models.Party, error)
	GetPartyByLeaderID(ctx context.Context, leaderID uuid.UUID) (*models.Party, error)
	GetPartyByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.Party, error)
	UpdateParty(ctx context.Context, party *models.Party) error
	DeleteParty(ctx context.Context, id uuid.UUID) error

	AddPartyMember(ctx context.Context, member *models.PartyMember) error
	UpdatePartyMember(ctx context.Context, member *models.PartyMember) error
	RemovePartyMember(ctx context.Context, partyID, characterID uuid.UUID) error
	GetPartyMembers(ctx context.Context, partyID uuid.UUID) ([]models.PartyMember, error)
	GetPartyMember(ctx context.Context, partyID, characterID uuid.UUID) (*models.PartyMember, error)
}

// PostgresPartyRepository реализация PartyRepository для PostgreSQL
type PostgresPartyRepository struct {
	db *pgxpool.Pool
}

// NewPostgresPartyRepository создает новый экземпляр репозитория
func NewPostgresPartyRepository(db *pgxpool.Pool) PartyRepository {
	return &PostgresPartyRepository{db: db}
}

// CreateParty создает новую группу
func (r *PostgresPartyRepository) CreateParty(ctx context.Context, party *models.Party) error {
	query := `
		INSERT INTO parties (id, leader_id, name, max_size, loot_mode, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)`

	now := time.Now()
	party.CreatedAt = now
	party.UpdatedAt = now

	_, err := r.db.Exec(ctx, query,
		party.ID, party.LeaderID, party.Name, party.MaxSize, party.LootMode,
		party.CreatedAt, party.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to create party: %w", err)
	}

	// Добавляем лидера как первого члена
	leaderMember := &models.PartyMember{
		ID:          uuid.New(),
		PartyID:     party.ID,
		CharacterID: party.LeaderID,
		AccountID:   party.LeaderID, // Предполагаем, что account_id совпадает с character_id для простоты
		Role:        models.PartyRoleLeader,
		JoinedAt:    now,
	}

	return r.AddPartyMember(ctx, leaderMember)
}

// GetPartyByID получает группу по ID
func (r *PostgresPartyRepository) GetPartyByID(ctx context.Context, id uuid.UUID) (*models.Party, error) {
	query := `
		SELECT id, leader_id, name, max_size, loot_mode, created_at, updated_at
		FROM parties
		WHERE id = $1`

	var party models.Party
	err := r.db.QueryRow(ctx, query, id).Scan(
		&party.ID, &party.LeaderID, &party.Name, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get party by ID: %w", err)
	}

	// Получаем членов группы
	members, err := r.GetPartyMembers(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}

	party.Members = members
	return &party, nil
}

// GetPartyByLeaderID получает группу по ID лидера
func (r *PostgresPartyRepository) GetPartyByLeaderID(ctx context.Context, leaderID uuid.UUID) (*models.Party, error) {
	query := `
		SELECT id, leader_id, name, max_size, loot_mode, created_at, updated_at
		FROM parties
		WHERE leader_id = $1`

	var party models.Party
	err := r.db.QueryRow(ctx, query, leaderID).Scan(
		&party.ID, &party.LeaderID, &party.Name, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get party by leader ID: %w", err)
	}

	members, err := r.GetPartyMembers(ctx, party.ID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}

	party.Members = members
	return &party, nil
}

// GetPartyByCharacterID получает группу по ID персонажа
func (r *PostgresPartyRepository) GetPartyByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.Party, error) {
	query := `
		SELECT p.id, p.leader_id, p.name, p.max_size, p.loot_mode, p.created_at, p.updated_at
		FROM parties p
		JOIN party_members pm ON p.id = pm.party_id
		WHERE pm.character_id = $1`

	var party models.Party
	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&party.ID, &party.LeaderID, &party.Name, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get party by character ID: %w", err)
	}

	members, err := r.GetPartyMembers(ctx, party.ID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}

	party.Members = members
	return &party, nil
}

// UpdateParty обновляет группу
func (r *PostgresPartyRepository) UpdateParty(ctx context.Context, party *models.Party) error {
	query := `
		UPDATE parties
		SET leader_id = $2, name = $3, max_size = $4, loot_mode = $5, updated_at = $6
		WHERE id = $1`

	party.UpdatedAt = time.Now()

	_, err := r.db.Exec(ctx, query,
		party.ID, party.LeaderID, party.Name, party.MaxSize, party.LootMode, party.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to update party: %w", err)
	}

	return nil
}

// DeleteParty удаляет группу
func (r *PostgresPartyRepository) DeleteParty(ctx context.Context, id uuid.UUID) error {
	// Сначала удаляем всех членов группы
	_, err := r.db.Exec(ctx, "DELETE FROM party_members WHERE party_id = $1", id)
	if err != nil {
		return fmt.Errorf("failed to delete party members: %w", err)
	}

	// Затем удаляем саму группу
	_, err = r.db.Exec(ctx, "DELETE FROM parties WHERE id = $1", id)
	if err != nil {
		return fmt.Errorf("failed to delete party: %w", err)
	}

	return nil
}

// AddPartyMember добавляет члена группы
func (r *PostgresPartyRepository) AddPartyMember(ctx context.Context, member *models.PartyMember) error {
	query := `
		INSERT INTO party_members (id, party_id, character_id, account_id, role, joined_at)
		VALUES ($1, $2, $3, $4, $5, $6)`

	_, err := r.db.Exec(ctx, query,
		member.ID, member.PartyID, member.CharacterID, member.AccountID, member.Role, member.JoinedAt)

	if err != nil {
		return fmt.Errorf("failed to add party member: %w", err)
	}

	return nil
}

// UpdatePartyMember обновляет члена группы
func (r *PostgresPartyRepository) UpdatePartyMember(ctx context.Context, member *models.PartyMember) error {
	query := `
		UPDATE party_members
		SET role = $3
		WHERE party_id = $1 AND character_id = $2`

	_, err := r.db.Exec(ctx, query, member.PartyID, member.CharacterID, member.Role)

	if err != nil {
		return fmt.Errorf("failed to update party member: %w", err)
	}

	return nil
}

// RemovePartyMember удаляет члена группы
func (r *PostgresPartyRepository) RemovePartyMember(ctx context.Context, partyID, characterID uuid.UUID) error {
	query := `DELETE FROM party_members WHERE party_id = $1 AND character_id = $2`

	_, err := r.db.Exec(ctx, query, partyID, characterID)

	if err != nil {
		return fmt.Errorf("failed to remove party member: %w", err)
	}

	return nil
}

// GetPartyMembers получает всех членов группы
func (r *PostgresPartyRepository) GetPartyMembers(ctx context.Context, partyID uuid.UUID) ([]models.PartyMember, error) {
	query := `
		SELECT id, party_id, character_id, account_id, role, joined_at
		FROM party_members
		WHERE party_id = $1
		ORDER BY joined_at ASC`

	rows, err := r.db.Query(ctx, query, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	defer rows.Close()

	var members []models.PartyMember
	for rows.Next() {
		var member models.PartyMember
		err := rows.Scan(&member.ID, &member.PartyID, &member.CharacterID, &member.AccountID, &member.Role, &member.JoinedAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan party member: %w", err)
		}
		members = append(members, member)
	}

	return members, nil
}

// GetPartyMember получает конкретного члена группы
func (r *PostgresPartyRepository) GetPartyMember(ctx context.Context, partyID, characterID uuid.UUID) (*models.PartyMember, error) {
	query := `
		SELECT id, party_id, character_id, account_id, role, joined_at
		FROM party_members
		WHERE party_id = $1 AND character_id = $2`

	var member models.PartyMember
	err := r.db.QueryRow(ctx, query, partyID, characterID).Scan(
		&member.ID, &member.PartyID, &member.CharacterID, &member.AccountID, &member.Role, &member.JoinedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get party member: %w", err)
	}

	return &member, nil
}
