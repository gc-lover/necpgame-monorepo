package service

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/party-core-service-go/internal/repository"
	"github.com/gc-lover/necpgame/services/party-core-service-go/pkg/models"
)

// PartyService интерфейс для бизнес-логики групп
type PartyService interface {
	CreateParty(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error)
	GetParty(ctx context.Context, partyID uuid.UUID) (*models.Party, error)
	GetPlayerParty(ctx context.Context, accountID uuid.UUID) (*models.Party, error)
	TransferLeadership(ctx context.Context, partyID uuid.UUID, currentLeaderID, newLeaderID uuid.UUID) error
	DissolveParty(ctx context.Context, partyID uuid.UUID, leaderID uuid.UUID) error

	InviteToParty(ctx context.Context, partyID, inviterID, inviteeID uuid.UUID) error
	AcceptInvite(ctx context.Context, partyID, characterID uuid.UUID) error
	DeclineInvite(ctx context.Context, partyID, characterID uuid.UUID) error
	LeaveParty(ctx context.Context, partyID, characterID uuid.UUID) error
	KickFromParty(ctx context.Context, partyID, leaderID, targetID uuid.UUID) error
}

// PartyServiceImpl реализация PartyService
type PartyServiceImpl struct {
	repo   repository.PartyRepository
	logger *zap.Logger
}

// NewPartyService создает новый экземпляр сервиса
func NewPartyService(repo repository.PartyRepository, logger *zap.Logger) PartyService {
	return &PartyServiceImpl{
		repo:   repo,
		logger: logger,
	}
}

// CreateParty создает новую группу
func (s *PartyServiceImpl) CreateParty(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error) {
	s.logger.Info("Creating new party",
		zap.String("leader_id", leaderID.String()),
		zap.String("name", req.Name))

	// Проверяем, не состоит ли лидер уже в группе
	existingParty, err := s.repo.GetPartyByCharacterID(ctx, leaderID)
	if err == nil && existingParty != nil {
		return nil, errors.New("leader is already in a party")
	}

	// Устанавливаем значения по умолчанию
	if req.Name == "" {
		req.Name = fmt.Sprintf("Party of %s", leaderID.String()[:8])
	}
	if req.MaxSize == 0 {
		req.MaxSize = 4 // Максимум 4 члена по умолчанию
	}
	if req.LootMode == "" {
		req.LootMode = models.LootModeFreeForAll
	}

	party := &models.Party{
		ID:       uuid.New(),
		LeaderID: leaderID,
		Name:     req.Name,
		MaxSize:  req.MaxSize,
		LootMode: req.LootMode,
	}

	err = s.repo.CreateParty(ctx, party)
	if err != nil {
		s.logger.Error("Failed to create party", zap.Error(err))
		return nil, fmt.Errorf("failed to create party: %w", err)
	}

	s.logger.Info("Party created successfully",
		zap.String("party_id", party.ID.String()),
		zap.String("leader_id", leaderID.String()))

	return party, nil
}

// GetParty получает информацию о группе
func (s *PartyServiceImpl) GetParty(ctx context.Context, partyID uuid.UUID) (*models.Party, error) {
	s.logger.Info("Getting party", zap.String("party_id", partyID.String()))

	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		s.logger.Error("Failed to get party", zap.Error(err))
		return nil, fmt.Errorf("failed to get party: %w", err)
	}

	return party, nil
}

// GetPlayerParty получает группу игрока
func (s *PartyServiceImpl) GetPlayerParty(ctx context.Context, accountID uuid.UUID) (*models.Party, error) {
	s.logger.Info("Getting player party", zap.String("account_id", accountID.String()))

	party, err := s.repo.GetPartyByCharacterID(ctx, accountID)
	if err != nil {
		s.logger.Error("Failed to get player party", zap.Error(err))
		return nil, fmt.Errorf("failed to get player party: %w", err)
	}

	return party, nil
}

// TransferLeadership передает лидерство другому игроку
func (s *PartyServiceImpl) TransferLeadership(ctx context.Context, partyID uuid.UUID, currentLeaderID, newLeaderID uuid.UUID) error {
	s.logger.Info("Transferring party leadership",
		zap.String("party_id", partyID.String()),
		zap.String("current_leader", currentLeaderID.String()),
		zap.String("new_leader", newLeaderID.String()))

	// Проверяем, что текущий лидер действительно лидер
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	if party.LeaderID != currentLeaderID {
		return errors.New("only party leader can transfer leadership")
	}

	// Проверяем, что новый лидер состоит в группе
	newLeaderMember, err := s.repo.GetPartyMember(ctx, partyID, newLeaderID)
	if err != nil {
		return errors.New("new leader must be a party member")
	}

	// Обновляем лидера в группе
	party.LeaderID = newLeaderID
	err = s.repo.UpdateParty(ctx, party)
	if err != nil {
		return fmt.Errorf("failed to update party leader: %w", err)
	}

	// Обновляем роли членов группы
	// Старый лидер становится обычным членом
	oldLeaderMember, err := s.repo.GetPartyMember(ctx, partyID, currentLeaderID)
	if err != nil {
		return fmt.Errorf("failed to get old leader member: %w", err)
	}
	oldLeaderMember.Role = models.PartyRoleMember
	err = s.repo.UpdatePartyMember(ctx, oldLeaderMember)
	if err != nil {
		return fmt.Errorf("failed to update old leader role: %w", err)
	}

	// Новый лидер становится лидером
	newLeaderMember.Role = models.PartyRoleLeader
	err = s.repo.UpdatePartyMember(ctx, newLeaderMember)
	if err != nil {
		return fmt.Errorf("failed to update new leader role: %w", err)
	}

	s.logger.Info("Party leadership transferred successfully",
		zap.String("party_id", partyID.String()),
		zap.String("new_leader", newLeaderID.String()))

	return nil
}

// DissolveParty распускает группу (только лидер)
func (s *PartyServiceImpl) DissolveParty(ctx context.Context, partyID uuid.UUID, leaderID uuid.UUID) error {
	s.logger.Info("Dissolving party",
		zap.String("party_id", partyID.String()),
		zap.String("leader_id", leaderID.String()))

	// Проверяем, что лидер действительно лидер группы
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	if party.LeaderID != leaderID {
		return errors.New("only party leader can dissolve the party")
	}

	err = s.repo.DeleteParty(ctx, partyID)
	if err != nil {
		s.logger.Error("Failed to dissolve party", zap.Error(err))
		return fmt.Errorf("failed to dissolve party: %w", err)
	}

	s.logger.Info("Party dissolved successfully", zap.String("party_id", partyID.String()))
	return nil
}

// InviteToParty приглашает игрока в группу
func (s *PartyServiceImpl) InviteToParty(ctx context.Context, partyID, inviterID, inviteeID uuid.UUID) error {
	s.logger.Info("Inviting player to party",
		zap.String("party_id", partyID.String()),
		zap.String("inviter_id", inviterID.String()),
		zap.String("invitee_id", inviteeID.String()))

	// Проверяем, что приглашающий является членом группы
	_, err := s.repo.GetPartyMember(ctx, partyID, inviterID)
	if err != nil {
		return errors.New("inviter must be a party member")
	}

	// Проверяем, что приглашаемый не состоит уже в группе
	existingParty, err := s.repo.GetPartyByCharacterID(ctx, inviteeID)
	if err == nil && existingParty != nil {
		return errors.New("invitee is already in a party")
	}

	// Получаем информацию о группе
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	// Проверяем лимит участников
	if len(party.Members) >= party.MaxSize {
		return errors.New("party is full")
	}

	// TODO: Создать приглашение в БД или кэше
	// Пока просто логируем
	s.logger.Info("Party invitation created",
		zap.String("party_id", partyID.String()),
		zap.String("invitee_id", inviteeID.String()))

	return nil
}

// AcceptInvite принимает приглашение в группу
func (s *PartyServiceImpl) AcceptInvite(ctx context.Context, partyID, characterID uuid.UUID) error {
	s.logger.Info("Accepting party invitation",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	// Проверяем, что игрок не состоит в другой группе
	existingParty, err := s.repo.GetPartyByCharacterID(ctx, characterID)
	if err == nil && existingParty != nil {
		return errors.New("character is already in a party")
	}

	// Получаем информацию о группе
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	// Проверяем лимит участников
	if len(party.Members) >= party.MaxSize {
		return errors.New("party is full")
	}

	// Добавляем игрока в группу
	member := &models.PartyMember{
		ID:          uuid.New(),
		PartyID:     partyID,
		CharacterID: characterID,
		AccountID:   characterID, // Предполагаем совпадение для простоты
		Role:        models.PartyRoleMember,
		JoinedAt:    time.Now(),
	}

	err = s.repo.AddPartyMember(ctx, member)
	if err != nil {
		s.logger.Error("Failed to add party member", zap.Error(err))
		return fmt.Errorf("failed to add party member: %w", err)
	}

	s.logger.Info("Player joined party successfully",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	return nil
}

// DeclineInvite отклоняет приглашение в группу
func (s *PartyServiceImpl) DeclineInvite(ctx context.Context, partyID, characterID uuid.UUID) error {
	s.logger.Info("Declining party invitation",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	// TODO: Удалить приглашение из БД или кэша
	// Пока просто логируем
	s.logger.Info("Party invitation declined",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	return nil
}

// LeaveParty позволяет игроку покинуть группу
func (s *PartyServiceImpl) LeaveParty(ctx context.Context, partyID, characterID uuid.UUID) error {
	s.logger.Info("Player leaving party",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	// Получаем информацию о группе и члене
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	member, err := s.repo.GetPartyMember(ctx, partyID, characterID)
	if err != nil {
		return errors.New("character is not a party member")
	}

	// Если лидер покидает группу, передать лидерство другому члену
	if member.Role == models.PartyRoleLeader {
		members := party.Members
		if len(members) <= 1 {
			// Последний член группы - распустить группу
			return s.DissolveParty(ctx, partyID, characterID)
		}

		// Найти следующего члена группы (кроме лидера)
		var newLeader *models.PartyMember
		for _, m := range members {
			if m.CharacterID != characterID {
				newLeader = &m
				break
			}
		}

		if newLeader != nil {
			err = s.TransferLeadership(ctx, partyID, characterID, newLeader.CharacterID)
			if err != nil {
				return fmt.Errorf("failed to transfer leadership: %w", err)
			}
		}
	}

	// Удаляем члена из группы
	err = s.repo.RemovePartyMember(ctx, partyID, characterID)
	if err != nil {
		s.logger.Error("Failed to remove party member", zap.Error(err))
		return fmt.Errorf("failed to remove party member: %w", err)
	}

	s.logger.Info("Player left party successfully",
		zap.String("party_id", partyID.String()),
		zap.String("character_id", characterID.String()))

	return nil
}

// KickFromParty позволяет лидеру выгнать игрока из группы
func (s *PartyServiceImpl) KickFromParty(ctx context.Context, partyID, leaderID, targetID uuid.UUID) error {
	s.logger.Info("Kicking player from party",
		zap.String("party_id", partyID.String()),
		zap.String("leader_id", leaderID.String()),
		zap.String("target_id", targetID.String()))

	// Проверяем, что лидер действительно лидер группы
	party, err := s.repo.GetPartyByID(ctx, partyID)
	if err != nil {
		return fmt.Errorf("failed to get party: %w", err)
	}

	if party.LeaderID != leaderID {
		return errors.New("only party leader can kick members")
	}

	// Проверяем, что цель - член группы
	_, err = s.repo.GetPartyMember(ctx, partyID, targetID)
	if err != nil {
		return errors.New("target is not a party member")
	}

	// Нельзя выгнать самого лидера
	if targetID == leaderID {
		return errors.New("leader cannot kick themselves")
	}

	// Удаляем члена из группы
	err = s.repo.RemovePartyMember(ctx, partyID, targetID)
	if err != nil {
		s.logger.Error("Failed to kick party member", zap.Error(err))
		return fmt.Errorf("failed to kick party member: %w", err)
	}

	s.logger.Info("Player kicked from party successfully",
		zap.String("party_id", partyID.String()),
		zap.String("target_id", targetID.String()))

	return nil
}
