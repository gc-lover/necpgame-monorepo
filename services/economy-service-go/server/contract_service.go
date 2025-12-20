// Issue: #140890166 - Contract system extension
package server

import (
	"context"
	"fmt"
	"math/rand"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// ContractService управляет бизнес-логикой контрактов
type ContractService struct {
	repo        *ContractRepository
	redis       *redis.Client
	logger      *logrus.Logger
	eventBus    *EventBus
}

// NewContractService создает новый сервис контрактов
func NewContractService(repo *ContractRepository, redis *redis.Client) *ContractService {
	return &ContractService{
		repo:     repo,
		redis:    redis,
		logger:   GetLogger(),
		eventBus: NewEventBus(redis),
	}
}

// CreateContract создает новый контракт в состоянии DRAFT
func (s *ContractService) CreateContract(ctx context.Context, buyerID uuid.UUID, req models.CreateContractRequest) (*models.TradeContract, error) {
	contract := &models.TradeContract{
		ID:          uuid.New(),
		Type:        req.Type,
		BuyerID:     buyerID,
		SellerID:    req.SellerID,
		Title:       req.Title,
		Description: req.Description,
		Terms:       req.Terms,
		Status:      models.ContractStatusDraft,
		ZoneID:      req.ZoneID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		Deadline:    req.Deadline,
	}

	if err := s.repo.CreateContract(ctx, contract); err != nil {
		s.logger.WithError(err).Error("Failed to create contract")
		return nil, fmt.Errorf("failed to create contract: %w", err)
	}

	// Создаем событие создания контракта
	s.createContractEvent(ctx, contract.ID, "contract_created", buyerID, map[string]interface{}{
		"type":        contract.Type,
		"seller_id":   contract.SellerID,
		"title":       contract.Title,
	})

	s.logger.WithFields(logrus.Fields{
		"contract_id": contract.ID,
		"type":        contract.Type,
		"buyer_id":    buyerID,
		"seller_id":   contract.SellerID,
	}).Info("Contract created successfully")

	return contract, nil
}

// StartNegotiation переводит контракт в состояние NEGOTIATION
func (s *ContractService) StartNegotiation(ctx context.Context, contractID, initiatorID uuid.UUID) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	// Проверяем права доступа
	if contract.BuyerID != initiatorID && contract.SellerID != initiatorID {
		return fmt.Errorf("unauthorized: only contract participants can start negotiation")
	}

	if contract.Status != models.ContractStatusDraft {
		return fmt.Errorf("invalid status: can only start negotiation from draft status")
	}

	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusNegotiation, nil); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	s.createContractEvent(ctx, contractID, "negotiation_started", initiatorID, nil)

	s.logger.WithField("contract_id", contractID).Info("Contract negotiation started")
	return nil
}

// UpdateContractTerms обновляет условия контракта во время переговоров
func (s *ContractService) UpdateContractTerms(ctx context.Context, contractID, updaterID uuid.UUID, req models.UpdateContractTermsRequest) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusNegotiation {
		return fmt.Errorf("invalid status: can only update terms during negotiation")
	}

	if contract.BuyerID != updaterID && contract.SellerID != updaterID {
		return fmt.Errorf("unauthorized: only contract participants can update terms")
	}

	if err := s.repo.UpdateContractTerms(ctx, contractID, req.Terms, req.Deadline); err != nil {
		return fmt.Errorf("failed to update contract terms: %w", err)
	}

	s.createContractEvent(ctx, contractID, "terms_updated", updaterID, map[string]interface{}{
		"terms":    req.Terms,
		"deadline": req.Deadline,
	})

	return nil
}

// AcceptContract принимает контракт и переводит в ESCROW_PENDING
func (s *ContractService) AcceptContract(ctx context.Context, contractID, acceptorID uuid.UUID) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusNegotiation {
		return fmt.Errorf("invalid status: can only accept during negotiation")
	}

	if contract.BuyerID != acceptorID && contract.SellerID != acceptorID {
		return fmt.Errorf("unauthorized: only contract participants can accept")
	}

	// Определяем залог на основе типа контракта
	collateral := s.calculateCollateral(contract.Type, contract.Terms)

	escrow := &models.ContractEscrow{
		ContractID: contract.ID,
		Collateral: collateral,
		TotalValue: 0, // Будет рассчитан при внесении депозитов
	}

	if err := s.repo.CreateEscrow(ctx, escrow); err != nil {
		return fmt.Errorf("failed to create escrow: %w", err)
	}

	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusEscrowPending, nil); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	s.createContractEvent(ctx, contractID, "contract_accepted", acceptorID, map[string]interface{}{
		"collateral": collateral,
	})

	s.logger.WithField("contract_id", contractID).Info("Contract accepted, escrow created")
	return nil
}

// DepositEscrow вносит депозит в эскроу
func (s *ContractService) DepositEscrow(ctx context.Context, contractID, depositorID uuid.UUID, req models.DepositEscrowRequest) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusEscrowPending && contract.Status != models.ContractStatusActive {
		return fmt.Errorf("invalid status: escrow deposits only allowed in escrow_pending or active status")
	}

	isBuyer := contract.BuyerID == depositorID
	isSeller := contract.SellerID == depositorID

	if !isBuyer && !isSeller {
		return fmt.Errorf("unauthorized: only contract participants can deposit escrow")
	}

	deposit := &models.EscrowDeposit{
		DepositorID: depositorID,
		Items:       req.Items,
		Currency:    req.Currency,
		DepositedAt: time.Now(),
	}

	if err := s.repo.UpdateEscrowDeposit(ctx, contractID, isBuyer, deposit); err != nil {
		return fmt.Errorf("failed to update escrow deposit: %w", err)
	}

	// Проверяем, внесены ли все необходимые депозиты
	if s.checkEscrowComplete(contract) {
		if err := s.activateContract(ctx, contractID); err != nil {
			return fmt.Errorf("failed to activate contract: %w", err)
		}
	}

	eventType := "buyer_escrow_deposited"
	if !isBuyer {
		eventType = "seller_escrow_deposited"
	}

	s.createContractEvent(ctx, contractID, eventType, depositorID, map[string]interface{}{
		"items":    req.Items,
		"currency": req.Currency,
	})

	return nil
}

// CompleteContract завершает контракт и освобождает эскроу
func (s *ContractService) CompleteContract(ctx context.Context, contractID, completerID uuid.UUID) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusActive {
		return fmt.Errorf("invalid status: can only complete active contracts")
	}

	if contract.BuyerID != completerID && contract.SellerID != completerID {
		return fmt.Errorf("unauthorized: only contract participants can complete")
	}

	now := time.Now()
	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusCompleted, &now); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	if err := s.repo.ReleaseEscrow(ctx, contractID); err != nil {
		return fmt.Errorf("failed to release escrow: %w", err)
	}

	s.createContractEvent(ctx, contractID, "contract_completed", completerID, nil)

	// Отправляем событие в event bus для синхронизации с другими сервисами
	s.eventBus.PublishEvent(ctx, "contract.completed", map[string]interface{}{
		"contract_id": contractID,
		"buyer_id":    contract.BuyerID,
		"seller_id":   contract.SellerID,
		"type":        contract.Type,
	})

	s.logger.WithField("contract_id", contractID).Info("Contract completed successfully")
	return nil
}

// CancelContract отменяет контракт
func (s *ContractService) CancelContract(ctx context.Context, contractID, cancellerID uuid.UUID, reason string) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status == models.ContractStatusCompleted || contract.Status == models.ContractStatusDisputed {
		return fmt.Errorf("invalid status: cannot cancel completed or disputed contracts")
	}

	if contract.BuyerID != cancellerID && contract.SellerID != cancellerID {
		return fmt.Errorf("unauthorized: only contract participants can cancel")
	}

	now := time.Now()
	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusCancelled, &now); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	// Возвращаем эскроу участникам
	if err := s.repo.ReleaseEscrow(ctx, contractID); err != nil {
		s.logger.WithError(err).Warn("Failed to release escrow on cancellation")
	}

	s.createContractEvent(ctx, contractID, "contract_cancelled", cancellerID, map[string]interface{}{
		"reason": reason,
	})

	s.logger.WithFields(logrus.Fields{
		"contract_id": contractID,
		"canceller":   cancellerID,
		"reason":      reason,
	}).Info("Contract cancelled")

	return nil
}

// CreateDispute создает диспут для контракта
func (s *ContractService) CreateDispute(ctx context.Context, contractID, initiatorID uuid.UUID, req models.ContractDisputeRequest) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusActive {
		return fmt.Errorf("invalid status: disputes can only be created for active contracts")
	}

	if contract.BuyerID != initiatorID && contract.SellerID != initiatorID {
		return fmt.Errorf("unauthorized: only contract participants can create disputes")
	}

	dispute := &models.ContractDispute{
		ContractID:  contractID,
		InitiatorID: initiatorID,
		Reason:      req.Reason,
		Evidence:    req.Evidence,
	}

	if err := s.repo.CreateDispute(ctx, dispute); err != nil {
		return fmt.Errorf("failed to create dispute: %w", err)
	}

	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusDisputed, nil); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	// Назначаем арбитратора (упрощенная логика - случайный выбор)
	arbitratorID := s.assignArbitrator(contract.BuyerID, contract.SellerID)
	dispute.ArbitratorID = &arbitratorID

	s.createContractEvent(ctx, contractID, "dispute_created", initiatorID, map[string]interface{}{
		"reason":       req.Reason,
		"evidence":     req.Evidence,
		"arbitrator_id": arbitratorID,
	})

	s.logger.WithFields(logrus.Fields{
		"contract_id": contractID,
		"initiator":   initiatorID,
		"reason":      req.Reason,
	}).Info("Contract dispute created")

	return nil
}

// ResolveDispute разрешает диспут (вызывается арбитратором)
func (s *ContractService) ResolveDispute(ctx context.Context, contractID, arbitratorID uuid.UUID, decision string, penalty map[string]int) error {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return fmt.Errorf("failed to get contract: %w", err)
	}

	if contract.Status != models.ContractStatusDisputed {
		return fmt.Errorf("invalid status: can only resolve disputed contracts")
	}

	if contract.Dispute.ArbitratorID == nil || *contract.Dispute.ArbitratorID != arbitratorID {
		return fmt.Errorf("unauthorized: only assigned arbitrator can resolve dispute")
	}

	if err := s.repo.UpdateDisputeResolution(ctx, contractID, decision, penalty); err != nil {
		return fmt.Errorf("failed to update dispute resolution: %w", err)
	}

	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusArbitrated, nil); err != nil {
		return fmt.Errorf("failed to update contract status: %w", err)
	}

	// Освобождаем эскроу с учетом решения арбитража
	if err := s.repo.ReleaseEscrow(ctx, contractID); err != nil {
		return fmt.Errorf("failed to release escrow: %w", err)
	}

	s.createContractEvent(ctx, contractID, "dispute_resolved", arbitratorID, map[string]interface{}{
		"decision": decision,
		"penalty":  penalty,
	})

	s.logger.WithFields(logrus.Fields{
		"contract_id": contractID,
		"decision":    decision,
	}).Info("Contract dispute resolved")

	return nil
}

// GetContract получает контракт с полной информацией
func (s *ContractService) GetContract(ctx context.Context, contractID, requesterID uuid.UUID) (*models.TradeContract, error) {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return nil, err
	}

	// Проверяем права доступа
	if contract.BuyerID != requesterID && contract.SellerID != requesterID {
		return nil, fmt.Errorf("unauthorized: only contract participants can view details")
	}

	return contract, nil
}

// GetContractsByParticipant получает контракты участника
func (s *ContractService) GetContractsByParticipant(ctx context.Context, participantID uuid.UUID, status *models.ContractStatus, limit, offset int) ([]models.TradeContract, int, error) {
	return s.repo.GetContractsByParticipant(ctx, participantID, status, limit, offset)
}

// GetContractHistory получает историю событий контракта
func (s *ContractService) GetContractHistory(ctx context.Context, contractID, requesterID uuid.UUID, limit, offset int) ([]models.ContractEvent, error) {
	contract, err := s.repo.GetContractByID(ctx, contractID)
	if err != nil {
		return nil, err
	}

	if contract.BuyerID != requesterID && contract.SellerID != requesterID {
		return nil, fmt.Errorf("unauthorized: only contract participants can view history")
	}

	return s.repo.GetContractEvents(ctx, contractID, limit, offset)
}

// Вспомогательные методы

func (s *ContractService) calculateCollateral(contractType models.ContractType, terms map[string]interface{}) map[string]int {
	// Упрощенная логика расчета залога на основе типа контракта
	switch contractType {
	case models.ContractTypeExchange:
		return map[string]int{"currency": 100}
	case models.ContractTypeDelivery:
		return map[string]int{"currency": 200}
	case models.ContractTypeCrafting:
		return map[string]int{"currency": 300}
	case models.ContractTypeService:
		return map[string]int{"currency": 150}
	default:
		return map[string]int{"currency": 50}
	}
}

func (s *ContractService) checkEscrowComplete(contract *models.TradeContract) bool {
	// Проверяем, что оба участника внесли депозиты
	hasBuyerDeposit := contract.Escrow != nil && contract.Escrow.BuyerDeposit != nil
	hasSellerDeposit := contract.Escrow != nil && contract.Escrow.SellerDeposit != nil

	return hasBuyerDeposit && hasSellerDeposit
}

func (s *ContractService) activateContract(ctx context.Context, contractID uuid.UUID) error {
	if err := s.repo.UpdateContractStatus(ctx, contractID, models.ContractStatusActive, nil); err != nil {
		return err
	}

	s.createContractEvent(ctx, contractID, "contract_activated", uuid.Nil, nil)
	return nil
}

func (s *ContractService) assignArbitrator(buyerID, sellerID uuid.UUID) uuid.UUID {
	// Упрощенная логика: случайный выбор между системными арбитраторами
	arbitrators := []uuid.UUID{
		uuid.MustParse("00000000-0000-0000-0000-000000000001"), // System Arbitrator 1
		uuid.MustParse("00000000-0000-0000-0000-000000000002"), // System Arbitrator 2
	}

	return arbitrators[rand.Intn(len(arbitrators))]
}

func (s *ContractService) createContractEvent(ctx context.Context, contractID uuid.UUID, eventType string, actorID uuid.UUID, data map[string]interface{}) {
	event := &models.ContractEvent{
		ID:         uuid.New(),
		ContractID: contractID,
		Type:       eventType,
		ActorID:    actorID,
		Data:       data,
		CreatedAt:  time.Now(),
	}

	if err := s.repo.CreateContractEvent(ctx, event); err != nil {
		s.logger.WithError(err).WithField("event_type", eventType).Warn("Failed to create contract event")
	}
}
