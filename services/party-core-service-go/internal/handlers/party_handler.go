package handlers

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
	"go.uber.org/zap"
	openapi_types "github.com/oapi-codegen/runtime/types"

	"github.com/gc-lover/necpgame/services/party-core-service-go/api"
	"github.com/gc-lover/necpgame/services/party-core-service-go/internal/service"
	"github.com/gc-lover/necpgame/services/party-core-service-go/pkg/models"
)

// PartyHandler обрабатывает HTTP запросы для групп
type PartyHandler struct {
	service service.PartyService
	logger  *zap.Logger
}

// NewPartyHandler создает новый обработчик
func NewPartyHandler(svc service.PartyService, logger *zap.Logger) *PartyHandler {
	return &PartyHandler{
		service: svc,
		logger:  logger,
	}
}

// GetParty получает информацию о группе
func (h *PartyHandler) GetParty(ctx echo.Context, params api.GetPartyParams) error {
	h.logger.Info("Processing GetParty request")

	// TODO: Реализовать получение группы по параметрам
	// Пока возвращаем заглушку

	return ctx.JSON(http.StatusOK, map[string]string{
		"message": "GetParty endpoint - not implemented yet",
	})
}

// CreateParty создает новую группу
func (h *PartyHandler) CreateParty(ctx echo.Context) error {
	h.logger.Info("Processing CreateParty request")

	// Получаем account_id из JWT токена (предполагаем, что он в контексте)
	accountID, ok := ctx.Get("account_id").(uuid.UUID)
	if !ok {
		return echo.NewHTTPError(http.StatusUnauthorized, "Invalid account ID")
	}

	var req models.CreatePartyRequest
	if err := ctx.Bind(&req); err != nil {
		h.logger.Error("Failed to bind request", zap.Error(err))
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request body")
	}

	party, err := h.service.CreateParty(ctx.Request().Context(), accountID, &req)
	if err != nil {
		h.logger.Error("Failed to create party", zap.Error(err))
		return echo.NewHTTPError(http.StatusInternalServerError, err.Error())
	}

	// Конвертируем в API модель
	apiParty := convertPartyToAPI(party)

	return ctx.JSON(http.StatusCreated, apiParty)
}

// GetPlayerParty получает группу игрока
func (h *PartyHandler) GetPlayerParty(ctx echo.Context, accountId openapi_types.UUID) error {
	h.logger.Info("Processing GetPlayerParty request",
		zap.String("account_id", accountId.String()))

	party, err := h.service.GetPlayerParty(ctx.Request().Context(), uuid.UUID(accountId))
	if err != nil {
		h.logger.Error("Failed to get player party", zap.Error(err))
		return echo.NewHTTPError(http.StatusNotFound, "Party not found")
	}

	apiParty := convertPartyToAPI(party)

	return ctx.JSON(http.StatusOK, apiParty)
}

// GetPartyById получает группу по ID
func (h *PartyHandler) GetPartyById(ctx echo.Context, partyId openapi_types.UUID) error {
	h.logger.Info("Processing GetPartyById request",
		zap.String("party_id", partyId.String()))

	party, err := h.service.GetParty(ctx.Request().Context(), uuid.UUID(partyId))
	if err != nil {
		h.logger.Error("Failed to get party", zap.Error(err))
		return echo.NewHTTPError(http.StatusNotFound, "Party not found")
	}

	apiParty := convertPartyToAPI(party)

	return ctx.JSON(http.StatusOK, apiParty)
}

// GetPartyLeader получает лидера группы
func (h *PartyHandler) GetPartyLeader(ctx echo.Context, partyId openapi_types.UUID) error {
	h.logger.Info("Processing GetPartyLeader request",
		zap.String("party_id", partyId.String()))

	party, err := h.service.GetParty(ctx.Request().Context(), uuid.UUID(partyId))
	if err != nil {
		h.logger.Error("Failed to get party", zap.Error(err))
		return echo.NewHTTPError(http.StatusNotFound, "Party not found")
	}

	// Находим лидера среди членов группы
	var leader *models.PartyMember
	for _, member := range party.Members {
		if member.Role == models.PartyRoleLeader {
			leader = &member
			break
		}
	}

	if leader == nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Party has no leader")
	}

	return ctx.JSON(http.StatusOK, map[string]interface{}{
		"character_id": leader.CharacterID,
		"joined_at":    leader.JoinedAt,
	})
}

// TransferPartyLeadership передает лидерство
func (h *PartyHandler) TransferPartyLeadership(ctx echo.Context, partyId openapi_types.UUID) error {
	h.logger.Info("Processing TransferPartyLeadership request",
		zap.String("party_id", partyId.String()))

	// Получаем account_id из JWT токена
	accountID, ok := ctx.Get("account_id").(uuid.UUID)
	if !ok {
		return echo.NewHTTPError(http.StatusUnauthorized, "Invalid account ID")
	}

	var req models.TransferLeadershipRequest
	if err := ctx.Bind(&req); err != nil {
		h.logger.Error("Failed to bind request", zap.Error(err))
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request body")
	}

	err := h.service.TransferLeadership(ctx.Request().Context(), uuid.UUID(partyId), accountID, req.NewLeaderID)
	if err != nil {
		h.logger.Error("Failed to transfer leadership", zap.Error(err))
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}

	return ctx.JSON(http.StatusOK, map[string]string{
		"message": "Leadership transferred successfully",
	})
}

// convertPartyToAPI конвертирует модель Party в API формат
func convertPartyToAPI(party *models.Party) *api.Party {
	apiParty := &api.Party{
		Id:        (*openapi_types.UUID)(&party.ID),
		LeaderId:  (*openapi_types.UUID)(&party.LeaderID),
		MaxSize:   &party.MaxSize,
		LootMode:  (*api.PartyLootMode)(&party.LootMode),
		CreatedAt: &party.CreatedAt,
		UpdatedAt: &party.UpdatedAt,
	}

	// Конвертируем членов группы
	if len(party.Members) > 0 {
		apiMembers := make([]api.PartyMember, len(party.Members))
		for i, member := range party.Members {
			apiMembers[i] = api.PartyMember{
				CharacterId: (*openapi_types.UUID)(&member.CharacterID),
				JoinedAt:    &member.JoinedAt,
				Role:        (*api.PartyMemberRole)(&member.Role),
			}
		}
		apiParty.Members = &apiMembers
	}

	return apiParty
}
