package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type EngramTransfer struct {
	ID                     uuid.UUID   `json:"id"`
	TransferID             uuid.UUID   `json:"transfer_id"`
	EngramID               uuid.UUID   `json:"engram_id"`
	FromCharacterID        uuid.UUID   `json:"from_character_id"`
	ToCharacterID          uuid.UUID   `json:"to_character_id"`
	TransferType           string      `json:"transfer_type"`
	IsCopy                 bool        `json:"is_copy"`
	NewAttitudeType        *string     `json:"new_attitude_type,omitempty"`
	TransferPrice          *float64    `json:"transfer_price,omitempty"`
	Status                 string      `json:"status"`
	LoanReturnDate         *time.Time  `json:"loan_return_date,omitempty"`
	ExtractionRiskPercent  *float64    `json:"extraction_risk_percent,omitempty"`
	EngramDamaged          bool        `json:"engram_damaged"`
	DamagePercent          *float64    `json:"damage_percent,omitempty"`
	TargetCharacterDied    bool        `json:"target_character_died"`
	NewEngramID            *uuid.UUID  `json:"new_engram_id,omitempty"`
	TransferredAt          *time.Time  `json:"transferred_at,omitempty"`
	CreatedAt              time.Time   `json:"created_at"`
	UpdatedAt              time.Time   `json:"updated_at"`
}

type EngramTransferRepositoryInterface interface {
	CreateTransfer(ctx context.Context, transfer *EngramTransfer) error
	GetTransferByID(ctx context.Context, transferID uuid.UUID) (*EngramTransfer, error)
	UpdateTransferStatus(ctx context.Context, transferID uuid.UUID, status string, transferredAt *time.Time) error
	GetActiveLoans(ctx context.Context, characterID uuid.UUID) ([]*EngramTransfer, error)
	GetPendingReturns(ctx context.Context) ([]*EngramTransfer, error)
}

type EngramTransferRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramTransferRepository(db *pgxpool.Pool) *EngramTransferRepository {
	return &EngramTransferRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramTransferRepository) CreateTransfer(ctx context.Context, transfer *EngramTransfer) error {
	transfer.ID = uuid.New()
	if transfer.TransferID == uuid.Nil {
		transfer.TransferID = uuid.New()
	}
	transfer.CreatedAt = time.Now()
	transfer.UpdatedAt = time.Now()

	_, err := r.db.Exec(ctx,
		`INSERT INTO economy.engram_transfers 
		 (id, transfer_id, engram_id, from_character_id, to_character_id, transfer_type,
		  is_copy, new_attitude_type, transfer_price, status, loan_return_date,
		  extraction_risk_percent, engram_damaged, damage_percent, target_character_died,
		  new_engram_id, transferred_at, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19)`,
		transfer.ID, transfer.TransferID, transfer.EngramID, transfer.FromCharacterID,
		transfer.ToCharacterID, transfer.TransferType, transfer.IsCopy, transfer.NewAttitudeType,
		transfer.TransferPrice, transfer.Status, transfer.LoanReturnDate, transfer.ExtractionRiskPercent,
		transfer.EngramDamaged, transfer.DamagePercent, transfer.TargetCharacterDied,
		transfer.NewEngramID, transfer.TransferredAt, transfer.CreatedAt, transfer.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create engram transfer")
		return err
	}

	return nil
}

func (r *EngramTransferRepository) GetTransferByID(ctx context.Context, transferID uuid.UUID) (*EngramTransfer, error) {
	var transfer EngramTransfer
	var newAttitudeType *string
	var transferPrice *float64
	var loanReturnDate *time.Time
	var extractionRiskPercent *float64
	var damagePercent *float64
	var newEngramID *uuid.UUID
	var transferredAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT id, transfer_id, engram_id, from_character_id, to_character_id, transfer_type,
		 is_copy, new_attitude_type, transfer_price, status, loan_return_date,
		 extraction_risk_percent, engram_damaged, damage_percent, target_character_died,
		 new_engram_id, transferred_at, created_at, updated_at
		 FROM economy.engram_transfers
		 WHERE transfer_id = $1`,
		transferID,
	).Scan(
		&transfer.ID, &transfer.TransferID, &transfer.EngramID, &transfer.FromCharacterID,
		&transfer.ToCharacterID, &transfer.TransferType, &transfer.IsCopy, &newAttitudeType,
		&transferPrice, &transfer.Status, &loanReturnDate, &extractionRiskPercent,
		&transfer.EngramDamaged, &damagePercent, &transfer.TargetCharacterDied,
		&newEngramID, &transferredAt, &transfer.CreatedAt, &transfer.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get engram transfer")
		return nil, err
	}

	transfer.NewAttitudeType = newAttitudeType
	transfer.TransferPrice = transferPrice
	transfer.LoanReturnDate = loanReturnDate
	transfer.ExtractionRiskPercent = extractionRiskPercent
	transfer.DamagePercent = damagePercent
	transfer.NewEngramID = newEngramID
	transfer.TransferredAt = transferredAt

	return &transfer, nil
}

func (r *EngramTransferRepository) UpdateTransferStatus(ctx context.Context, transferID uuid.UUID, status string, transferredAt *time.Time) error {
	_, err := r.db.Exec(ctx,
		`UPDATE economy.engram_transfers 
		 SET status = $1, transferred_at = $2, updated_at = NOW()
		 WHERE transfer_id = $3`,
		status, transferredAt, transferID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update transfer status")
		return err
	}

	return nil
}

func (r *EngramTransferRepository) GetActiveLoans(ctx context.Context, characterID uuid.UUID) ([]*EngramTransfer, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, transfer_id, engram_id, from_character_id, to_character_id, transfer_type,
		 is_copy, new_attitude_type, transfer_price, status, loan_return_date,
		 extraction_risk_percent, engram_damaged, damage_percent, target_character_died,
		 new_engram_id, transferred_at, created_at, updated_at
		 FROM economy.engram_transfers
		 WHERE transfer_type = 'loan' AND status = 'completed' AND 
		 (from_character_id = $1 OR to_character_id = $1) AND 
		 loan_return_date > NOW()`,
		characterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get active loans")
		return nil, err
	}
	defer rows.Close()

	var transfers []*EngramTransfer
	for rows.Next() {
		transfer := &EngramTransfer{}
		var newAttitudeType *string
		var transferPrice *float64
		var loanReturnDate *time.Time
		var extractionRiskPercent *float64
		var damagePercent *float64
		var newEngramID *uuid.UUID
		var transferredAt *time.Time

		err := rows.Scan(
			&transfer.ID, &transfer.TransferID, &transfer.EngramID, &transfer.FromCharacterID,
			&transfer.ToCharacterID, &transfer.TransferType, &transfer.IsCopy, &newAttitudeType,
			&transferPrice, &transfer.Status, &loanReturnDate, &extractionRiskPercent,
			&transfer.EngramDamaged, &damagePercent, &transfer.TargetCharacterDied,
			&newEngramID, &transferredAt, &transfer.CreatedAt, &transfer.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan engram transfer")
			continue
		}

		transfer.NewAttitudeType = newAttitudeType
		transfer.TransferPrice = transferPrice
		transfer.LoanReturnDate = loanReturnDate
		transfer.ExtractionRiskPercent = extractionRiskPercent
		transfer.DamagePercent = damagePercent
		transfer.NewEngramID = newEngramID
		transfer.TransferredAt = transferredAt

		transfers = append(transfers, transfer)
	}

	return transfers, nil
}

func (r *EngramTransferRepository) GetPendingReturns(ctx context.Context) ([]*EngramTransfer, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, transfer_id, engram_id, from_character_id, to_character_id, transfer_type,
		 is_copy, new_attitude_type, transfer_price, status, loan_return_date,
		 extraction_risk_percent, engram_damaged, damage_percent, target_character_died,
		 new_engram_id, transferred_at, created_at, updated_at
		 FROM economy.engram_transfers
		 WHERE transfer_type = 'loan' AND status = 'completed' AND loan_return_date <= NOW()`,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get pending returns")
		return nil, err
	}
	defer rows.Close()

	var transfers []*EngramTransfer
	for rows.Next() {
		transfer := &EngramTransfer{}
		var newAttitudeType *string
		var transferPrice *float64
		var loanReturnDate *time.Time
		var extractionRiskPercent *float64
		var damagePercent *float64
		var newEngramID *uuid.UUID
		var transferredAt *time.Time

		err := rows.Scan(
			&transfer.ID, &transfer.TransferID, &transfer.EngramID, &transfer.FromCharacterID,
			&transfer.ToCharacterID, &transfer.TransferType, &transfer.IsCopy, &newAttitudeType,
			&transferPrice, &transfer.Status, &loanReturnDate, &extractionRiskPercent,
			&transfer.EngramDamaged, &damagePercent, &transfer.TargetCharacterDied,
			&newEngramID, &transferredAt, &transfer.CreatedAt, &transfer.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan engram transfer")
			continue
		}

		transfer.NewAttitudeType = newAttitudeType
		transfer.TransferPrice = transferPrice
		transfer.LoanReturnDate = loanReturnDate
		transfer.ExtractionRiskPercent = extractionRiskPercent
		transfer.DamagePercent = damagePercent
		transfer.NewEngramID = newEngramID
		transfer.TransferredAt = transferredAt

		transfers = append(transfers, transfer)
	}

	return transfers, nil
}



