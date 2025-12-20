// Issue: #140890166 - Contract system extension
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// ContractRepository управляет данными контрактов в базе данных
type ContractRepository struct {
	db *pgxpool.Pool
}

// NewContractRepository создает новый репозиторий контрактов
func NewContractRepository(db *pgxpool.Pool) *ContractRepository {
	return &ContractRepository{db: db}
}

// CreateContract создает новый контракт
func (r *ContractRepository) CreateContract(ctx context.Context, contract *models.TradeContract) error {
	query := `
		INSERT INTO player_contracts (
			id, type, buyer_id, seller_id, title, description, terms,
			status, zone_id, created_at, updated_at, deadline
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)`

	termsJSON, err := json.Marshal(contract.Terms)
	if err != nil {
		return fmt.Errorf("failed to marshal contract terms: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		contract.ID, contract.Type, contract.BuyerID, contract.SellerID,
		contract.Title, contract.Description, termsJSON, contract.Status,
		contract.ZoneID, contract.CreatedAt, contract.UpdatedAt, contract.Deadline)

	return err
}

// GetContractByID получает контракт по ID с эскроу и диспутом
func (r *ContractRepository) GetContractByID(ctx context.Context, contractID uuid.UUID) (*models.TradeContract, error) {
	query := `
		SELECT c.id, c.type, c.buyer_id, c.seller_id, c.title, c.description, c.terms,
			   c.status, c.zone_id, c.created_at, c.updated_at, c.deadline, c.completed_at,
			   e.buyer_deposit, e.seller_deposit, e.collateral, e.total_value, e.released_at,
			   d.reason, d.evidence, d.arbitrator_id, d.decision, d.penalty, d.resolved_at
		FROM player_contracts c
		LEFT JOIN contract_escrows e ON c.id = e.contract_id
		LEFT JOIN contract_disputes d ON c.id = d.contract_id
		WHERE c.id = $1`

	var contract models.TradeContract
	var termsJSON []byte
	var buyerDeposit, sellerDeposit []byte
	var collateral []byte
	var penalty []byte
	var evidence []byte

	err := r.db.QueryRow(ctx, query, contractID).Scan(
		&contract.ID, &contract.Type, &contract.BuyerID, &contract.SellerID,
		&contract.Title, &contract.Description, &termsJSON, &contract.Status,
		&contract.ZoneID, &contract.CreatedAt, &contract.UpdatedAt,
		&contract.Deadline, &contract.CompletedAt,
		&buyerDeposit, &sellerDeposit, &collateral, &contract.Escrow.TotalValue,
		&contract.Escrow.ReleasedAt, &contract.Dispute.Reason, &evidence,
		&contract.Dispute.ArbitratorID, &contract.Dispute.Decision, &penalty,
		&contract.Dispute.ResolvedAt)

	if err != nil {
		return nil, err
	}

	// Разбираем JSON поля
	if err := json.Unmarshal(termsJSON, &contract.Terms); err != nil {
		return nil, fmt.Errorf("failed to unmarshal contract terms: %w", err)
	}

	if buyerDeposit != nil {
		contract.Escrow.BuyerDeposit = &models.EscrowDeposit{}
		if err := json.Unmarshal(buyerDeposit, contract.Escrow.BuyerDeposit); err != nil {
			return nil, fmt.Errorf("failed to unmarshal buyer deposit: %w", err)
		}
	}

	if sellerDeposit != nil {
		contract.Escrow.SellerDeposit = &models.EscrowDeposit{}
		if err := json.Unmarshal(sellerDeposit, contract.Escrow.SellerDeposit); err != nil {
			return nil, fmt.Errorf("failed to unmarshal seller deposit: %w", err)
		}
	}

	if collateral != nil {
		if err := json.Unmarshal(collateral, &contract.Escrow.Collateral); err != nil {
			return nil, fmt.Errorf("failed to unmarshal collateral: %w", err)
		}
	}

	if evidence != nil {
		if err := json.Unmarshal(evidence, &contract.Dispute.Evidence); err != nil {
			return nil, fmt.Errorf("failed to unmarshal dispute evidence: %w", err)
		}
	}

	if penalty != nil {
		if err := json.Unmarshal(penalty, &contract.Dispute.Penalty); err != nil {
			return nil, fmt.Errorf("failed to unmarshal dispute penalty: %w", err)
		}
	}

	return &contract, nil
}

// GetContractsByParticipant получает контракты участника с пагинацией
func (r *ContractRepository) GetContractsByParticipant(ctx context.Context, participantID uuid.UUID, status *models.ContractStatus, limit, offset int) ([]models.TradeContract, int, error) {
	baseQuery := `
		SELECT c.id, c.type, c.buyer_id, c.seller_id, c.title, c.description, c.terms,
			   c.status, c.zone_id, c.created_at, c.updated_at, c.deadline, c.completed_at
		FROM player_contracts c
		WHERE (c.buyer_id = $1 OR c.seller_id = $1)`

	countQuery := `
		SELECT COUNT(*) FROM player_contracts c
		WHERE (c.buyer_id = $1 OR c.seller_id = $1)`

	args := []interface{}{participantID}
	argIndex := 2

	if status != nil {
		baseQuery += fmt.Sprintf(" AND c.status = $%d", argIndex)
		countQuery += fmt.Sprintf(" AND c.status = $%d", argIndex)
		args = append(args, *status)
		argIndex++
	}

	baseQuery += fmt.Sprintf(" ORDER BY c.updated_at DESC LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	// Получаем общее количество
	var total int
	err := r.db.QueryRow(ctx, countQuery, args[:argIndex-1]...).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	// Получаем контракты
	rows, err := r.db.Query(ctx, baseQuery, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var contracts []models.TradeContract
	for rows.Next() {
		var contract models.TradeContract
		var termsJSON []byte

		err := rows.Scan(
			&contract.ID, &contract.Type, &contract.BuyerID, &contract.SellerID,
			&contract.Title, &contract.Description, &termsJSON, &contract.Status,
			&contract.ZoneID, &contract.CreatedAt, &contract.UpdatedAt,
			&contract.Deadline, &contract.CompletedAt)

		if err != nil {
			return nil, 0, err
		}

		if err := json.Unmarshal(termsJSON, &contract.Terms); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal contract terms: %w", err)
		}

		contracts = append(contracts, contract)
	}

	return contracts, total, nil
}

// UpdateContractStatus обновляет статус контракта
func (r *ContractRepository) UpdateContractStatus(ctx context.Context, contractID uuid.UUID, status models.ContractStatus, completedAt *time.Time) error {
	query := `
		UPDATE player_contracts
		SET status = $1, updated_at = $2, completed_at = $3
		WHERE id = $4`

	_, err := r.db.Exec(ctx, query, status, time.Now(), completedAt, contractID)
	return err
}

// UpdateContractTerms обновляет условия контракта
func (r *ContractRepository) UpdateContractTerms(ctx context.Context, contractID uuid.UUID, terms map[string]interface{}, deadline *time.Time) error {
	termsJSON, err := json.Marshal(terms)
	if err != nil {
		return fmt.Errorf("failed to marshal contract terms: %w", err)
	}

	query := `
		UPDATE player_contracts
		SET terms = $1, deadline = $2, updated_at = $3
		WHERE id = $4`

	_, err = r.db.Exec(ctx, query, termsJSON, deadline, time.Now(), contractID)
	return err
}

// CreateEscrow создает эскроу для контракта
func (r *ContractRepository) CreateEscrow(ctx context.Context, escrow *models.ContractEscrow) error {
	query := `
		INSERT INTO contract_escrows (
			contract_id, buyer_deposit, seller_deposit, collateral, total_value, released_at
		) VALUES ($1, $2, $3, $4, $5, $6)`

	var buyerDeposit, sellerDeposit, collateral []byte
	var err error

	if escrow.BuyerDeposit != nil {
		buyerDeposit, err = json.Marshal(escrow.BuyerDeposit)
		if err != nil {
			return fmt.Errorf("failed to marshal buyer deposit: %w", err)
		}
	}

	if escrow.SellerDeposit != nil {
		sellerDeposit, err = json.Marshal(escrow.SellerDeposit)
		if err != nil {
			return fmt.Errorf("failed to marshal seller deposit: %w", err)
		}
	}

	if escrow.Collateral != nil {
		collateral, err = json.Marshal(escrow.Collateral)
		if err != nil {
			return fmt.Errorf("failed to marshal collateral: %w", err)
		}
	}

	_, err = r.db.Exec(ctx, query,
		escrow.ContractID, buyerDeposit, sellerDeposit, collateral,
		escrow.TotalValue, escrow.ReleasedAt)

	return err
}

// UpdateEscrowDeposit обновляет депозит в эскроу
func (r *ContractRepository) UpdateEscrowDeposit(ctx context.Context, contractID uuid.UUID, isBuyer bool, deposit *models.EscrowDeposit) error {
	depositJSON, err := json.Marshal(deposit)
	if err != nil {
		return fmt.Errorf("failed to marshal deposit: %w", err)
	}

	var query string
	if isBuyer {
		query = `UPDATE contract_escrows SET buyer_deposit = $1 WHERE contract_id = $2`
	} else {
		query = `UPDATE contract_escrows SET seller_deposit = $1 WHERE contract_id = $2`
	}

	_, err = r.db.Exec(ctx, query, depositJSON, contractID)
	return err
}

// ReleaseEscrow освобождает эскроу
func (r *ContractRepository) ReleaseEscrow(ctx context.Context, contractID uuid.UUID) error {
	query := `UPDATE contract_escrows SET released_at = $1 WHERE contract_id = $2`
	_, err := r.db.Exec(ctx, query, time.Now(), contractID)
	return err
}

// CreateDispute создает диспут для контракта
func (r *ContractRepository) CreateDispute(ctx context.Context, dispute *models.ContractDispute) error {
	evidenceJSON, err := json.Marshal(dispute.Evidence)
	if err != nil {
		return fmt.Errorf("failed to marshal dispute evidence: %w", err)
	}

	penaltyJSON, err := json.Marshal(dispute.Penalty)
	if err != nil {
		return fmt.Errorf("failed to marshal dispute penalty: %w", err)
	}

	query := `
		INSERT INTO contract_disputes (
			contract_id, initiator_id, reason, evidence, arbitrator_id, decision, penalty, resolved_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err = r.db.Exec(ctx, query,
		dispute.ContractID, dispute.InitiatorID, dispute.Reason, evidenceJSON,
		dispute.ArbitratorID, dispute.Decision, penaltyJSON, dispute.ResolvedAt)

	return err
}

// UpdateDisputeResolution обновляет разрешение диспута
func (r *ContractRepository) UpdateDisputeResolution(ctx context.Context, contractID uuid.UUID, decision string, penalty map[string]int) error {
	penaltyJSON, err := json.Marshal(penalty)
	if err != nil {
		return fmt.Errorf("failed to marshal dispute penalty: %w", err)
	}

	query := `
		UPDATE contract_disputes
		SET decision = $1, penalty = $2, resolved_at = $3
		WHERE contract_id = $4`

	_, err = r.db.Exec(ctx, query, decision, penaltyJSON, time.Now(), contractID)
	return err
}

// CreateContractEvent создает событие контракта для истории
func (r *ContractRepository) CreateContractEvent(ctx context.Context, event *models.ContractEvent) error {
	dataJSON, err := json.Marshal(event.Data)
	if err != nil {
		return fmt.Errorf("failed to marshal event data: %w", err)
	}

	query := `
		INSERT INTO contract_events (id, contract_id, type, actor_id, data, created_at)
		VALUES ($1, $2, $3, $4, $5, $6)`

	_, err = r.db.Exec(ctx, query,
		event.ID, event.ContractID, event.Type, event.ActorID, dataJSON, event.CreatedAt)

	return err
}

// GetContractEvents получает события контракта
func (r *ContractRepository) GetContractEvents(ctx context.Context, contractID uuid.UUID, limit, offset int) ([]models.ContractEvent, error) {
	query := `
		SELECT id, contract_id, type, actor_id, data, created_at
		FROM contract_events
		WHERE contract_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, contractID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var events []models.ContractEvent
	for rows.Next() {
		var event models.ContractEvent
		var dataJSON []byte

		err := rows.Scan(&event.ID, &event.ContractID, &event.Type, &event.ActorID, &dataJSON, &event.CreatedAt)
		if err != nil {
			return nil, err
		}

		if err := json.Unmarshal(dataJSON, &event.Data); err != nil {
			return nil, fmt.Errorf("failed to unmarshal event data: %w", err)
		}

		events = append(events, event)
	}

	return events, nil
}
