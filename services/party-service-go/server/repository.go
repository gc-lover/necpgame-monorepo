// Package server Issue: #139 - PostgreSQL integration for Party System
// BLOCKER: ACID transactions, context timeouts
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

type PartyRepository struct {
	db *sql.DB
}

type PartyInvite struct {
	ID        uuid.UUID `json:"id"`
	PartyID   string    `json:"party_id"`
	InviterID string    `json:"inviter_id"`
	InviteeID string    `json:"invitee_id"`
	Status    string    `json:"status"`
	ExpiresAt time.Time `json:"expires_at"`
	CreatedAt time.Time `json:"created_at"`
}

func NewPartyRepository() *PartyRepository {
	return &PartyRepository{db: db}
}

// CreateParty stores a new party in PostgreSQL
func (r *PartyRepository) CreateParty(ctx context.Context, party *Party) error {
	query := `
		INSERT INTO social.parties (id, leader_id, name, max_members, loot_mode, status, settings, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	_, err := r.db.ExecContext(ctx, query,
		party.ID,
		party.LeaderID,
		party.Name,
		party.MaxMembers,
		party.LootMode,
		"active",
		"{}",
		party.CreatedAt,
		party.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create party: %w", err)
	}

	return nil
}

// GetParty retrieves a party by ID from PostgreSQL
func (r *PartyRepository) GetParty(ctx context.Context, partyID string) (*Party, error) {
	query := `
		SELECT id, leader_id, name, max_members, loot_mode, status, settings, created_at, updated_at
		FROM social.parties
		WHERE id = $1 AND status = 'active'`

	var party Party
	var settingsJSON string
	var status string

	err := r.db.QueryRowContext(ctx, query, partyID).Scan(
		&party.ID,
		&party.LeaderID,
		&party.Name,
		&party.MaxMembers,
		&party.LootMode,
		&status,
		&settingsJSON,
		&party.CreatedAt,
		&party.UpdatedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("party not found")
		}
		return nil, fmt.Errorf("failed to get party: %w", err)
	}

	// Get members from party_members table
	membersQuery := `
		SELECT character_id
		FROM social.party_members
		WHERE party_id = $1
		ORDER BY joined_at ASC`

	rows, err := r.db.QueryContext(ctx, membersQuery, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	defer rows.Close()

	var members []string
	for rows.Next() {
		var memberID string
		if err := rows.Scan(&memberID); err != nil {
			return nil, fmt.Errorf("failed to scan member: %w", err)
		}
		members = append(members, memberID)
	}

	party.Members = members
	return &party, nil
}

// UpdateParty updates a party in PostgreSQL
func (r *PartyRepository) UpdateParty(ctx context.Context, party *Party) error {
	query := `
		UPDATE social.parties
		SET leader_id = $1, name = $2, max_members = $3, loot_mode = $4,
		    updated_at = $5
		WHERE id = $6`

	_, err := r.db.ExecContext(ctx, query,
		party.LeaderID,
		party.Name,
		party.MaxMembers,
		party.LootMode,
		party.UpdatedAt,
		party.ID,
	)

	if err != nil {
		return fmt.Errorf("failed to update party: %w", err)
	}

	return nil
}

// DeleteParty removes a party from PostgreSQL
func (r *PartyRepository) DeleteParty(ctx context.Context, partyID string) error {
	query := `
		UPDATE social.parties
		SET status = 'disbanded', updated_at = $1
		WHERE id = $2`

	_, err := r.db.ExecContext(ctx, query, time.Now(), partyID)
	if err != nil {
		return fmt.Errorf("failed to disband party: %w", err)
	}

	return nil
}

// CreatePartyInvite creates a new party invitation
func (r *PartyRepository) CreatePartyInvite(ctx context.Context, invite *PartyInvite) error {
	query := `
		INSERT INTO social.party_invitations (id, party_id, inviter_id, invitee_id, status, expires_at, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)`

	_, err := r.db.ExecContext(ctx, query,
		invite.ID,
		invite.PartyID,
		invite.InviterID,
		invite.InviteeID,
		invite.Status,
		invite.ExpiresAt,
		invite.CreatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create party invite: %w", err)
	}

	return nil
}

// GetPartyInvite retrieves a party invitation by ID
func (r *PartyRepository) GetPartyInvite(ctx context.Context, inviteID string) (*PartyInvite, error) {
	query := `
		SELECT id, party_id, inviter_id, invitee_id, status, expires_at, created_at
		FROM social.party_invitations
		WHERE id = $1`

	var invite PartyInvite
	err := r.db.QueryRowContext(ctx, query, inviteID).Scan(
		&invite.ID,
		&invite.PartyID,
		&invite.InviterID,
		&invite.InviteeID,
		&invite.Status,
		&invite.ExpiresAt,
		&invite.CreatedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("party invite not found")
		}
		return nil, fmt.Errorf("failed to get party invite: %w", err)
	}

	return &invite, nil
}

// UpdatePartyInviteStatus updates the status of a party invitation
func (r *PartyRepository) UpdatePartyInviteStatus(ctx context.Context, inviteID, status string) error {
	query := `
		UPDATE social.party_invitations
		SET status = $1, responded_at = $2
		WHERE id = $3`

	_, err := r.db.ExecContext(ctx, query, status, time.Now(), inviteID)
	if err != nil {
		return fmt.Errorf("failed to update party invite status: %w", err)
	}

	return nil
}

// GetPartyInvitesByPlayer retrieves active invitations for a player
func (r *PartyRepository) GetPartyInvitesByPlayer(ctx context.Context, playerID string) ([]*PartyInvite, error) {
	query := `
		SELECT id, party_id, inviter_id, invitee_id, status, expires_at, created_at
		FROM social.party_invitations
		WHERE invitee_id = $1 AND status = 'pending' AND expires_at > $2
		ORDER BY created_at DESC`

	rows, err := r.db.QueryContext(ctx, query, playerID, time.Now())
	if err != nil {
		return nil, fmt.Errorf("failed to get party invites: %w", err)
	}
	defer rows.Close()

	var invites []*PartyInvite
	for rows.Next() {
		var invite PartyInvite
		err := rows.Scan(
			&invite.ID,
			&invite.PartyID,
			&invite.InviterID,
			&invite.InviteeID,
			&invite.Status,
			&invite.ExpiresAt,
			&invite.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan invite: %w", err)
		}
		invites = append(invites, &invite)
	}

	return invites, nil
}
