// Package server Issue: #140890170 - Crafting mechanics implementation
package server

import (
	"context"
	"fmt"
	"math"
	"math/rand"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// CraftingService управляет логикой крафта
type CraftingService struct {
	repo     *CraftingRepository
	redis    *redis.Client
	logger   *logrus.Logger
	eventBus *EventBus
}

// NewCraftingService создает новый сервис крафта
func NewCraftingService(repo *CraftingRepository, redis *redis.Client) *CraftingService {
	return &CraftingService{
		repo:   repo,
		redis:  redis,
		logger: GetLogger(),
		// TODO: Implement event bus
		// eventBus: NewEventBus(redis),
	}
}

// StartCrafting начинает процесс крафта
func (s *CraftingService) StartCrafting(ctx context.Context, playerID, recipeID, stationID string, materials []models.UsedMaterial) (*models.CraftingOrder, error) {
	// Проверяем рецепт
	recipe, err := s.repo.GetRecipeByID(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	// Проверяем станцию
	station, err := s.repo.GetStationByID(ctx, stationID)
	if err != nil {
		return nil, fmt.Errorf("failed to get station: %w", err)
	}

	if !station.IsActive {
		return nil, fmt.Errorf("station is not active")
	}

	// Проверяем требования рецепта
	if err := s.checkRecipeRequirements(playerID, recipe, station); err != nil {
		return nil, fmt.Errorf("recipe requirements not met: %w", err)
	}

	// Проверяем и резервируем материалы
	if err := s.validateAndReserveMaterials(playerID, materials, recipe.Materials); err != nil {
		return nil, fmt.Errorf("material validation failed: %w", err)
	}

	// Создаем заказ
	order := &models.CraftingOrder{
		ID:            uuid.New().String(),
		PlayerID:      playerID,
		RecipeID:      recipeID,
		StationID:     stationID,
		Status:        "pending",
		Quality:       0, // будет рассчитано позже
		CreatedAt:     time.Now(),
		UsedMaterials: materials,
	}

	// Сохраняем заказ
	if err := s.repo.CreateOrder(ctx, order); err != nil {
		// Откатываем резервирование материалов при ошибке
		s.releaseReservedMaterials(playerID, materials)
		return nil, fmt.Errorf("failed to create order: %w", err)
	}

	// Запускаем асинхронный процесс крафта
	go s.processCrafting(order.ID)

	s.logger.WithFields(map[string]interface{}{
		"order_id":   order.ID,
		"player_id":  playerID,
		"recipe_id":  recipeID,
		"station_id": stationID,
	}).Info("Crafting order created and started")

	return order, nil
}

// GetRecipe получает рецепт с полной информацией
func (s *CraftingService) GetRecipe(ctx context.Context, recipeID string) (*models.CraftingRecipe, error) {
	return s.repo.GetRecipeByID(ctx, recipeID)
}

// GetRecipesByCategory получает рецепты по категории
func (s *CraftingService) GetRecipesByCategory(ctx context.Context, category string, limit, offset int) ([]*models.CraftingRecipe, error) {
	return s.repo.GetRecipesByCategory(ctx, category, limit, offset)
}

// GetPlayerOrders получает заказы игрока
func (s *CraftingService) GetPlayerOrders(ctx context.Context, playerID string, limit, offset int) ([]*models.CraftingOrder, error) {
	return s.repo.GetPlayerOrders(ctx, playerID, limit, offset)
}

// GetOrder получает заказ по ID
func (s *CraftingService) GetOrder(ctx context.Context, orderID string) (*models.CraftingOrder, error) {
	return s.repo.GetOrderByID(ctx, orderID)
}

// CancelOrder отменяет заказ
func (s *CraftingService) CancelOrder(ctx context.Context, orderID, playerID string) error {
	order, err := s.repo.GetOrderByID(ctx, orderID)
	if err != nil {
		return fmt.Errorf("failed to get order: %w", err)
	}

	if order.PlayerID != playerID {
		return fmt.Errorf("access denied: order belongs to another player")
	}

	if order.Status != "pending" && order.Status != "crafting" {
		return fmt.Errorf("cannot cancel order with status: %s", order.Status)
	}

	// Возвращаем материалы
	if err := s.releaseReservedMaterials(playerID, order.UsedMaterials); err != nil {
		s.logger.WithError(err).Error("Failed to release materials on order cancellation")
	}

	// Обновляем статус
	now := time.Now()
	if err := s.repo.UpdateOrderStatus(ctx, orderID, "cancelled", &now); err != nil {
		return fmt.Errorf("failed to update order status: %w", err)
	}

	s.logger.WithFields(map[string]interface{}{
		"order_id":  orderID,
		"player_id": playerID,
	}).Info("Crafting order cancelled")

	return nil
}

// CreateContract создает контракт на крафт
func (s *CraftingService) CreateContract(ctx context.Context, contract *models.CraftingContract) error {
	contract.ID = uuid.New().String()
	contract.Status = "open"
	contract.CreatedAt = time.Now()

	if err := s.repo.CreateContract(ctx, contract); err != nil {
		return fmt.Errorf("failed to create contract: %w", err)
	}

	s.logger.WithFields(map[string]interface{}{
		"contract_id": contract.ID,
		"client_id":   contract.ClientID,
		"recipe_id":   contract.RecipeID,
	}).Info("Crafting contract created")

	return nil
}

// GetContractsByStatus получает контракты по статусу
func (s *CraftingService) GetContractsByStatus(ctx context.Context, status string, limit, offset int) ([]*models.CraftingContract, error) {
	return s.repo.GetContractsByStatus(ctx, status, limit, offset)
}

// CalculateCraftingCost рассчитывает стоимость крафта
func (s *CraftingService) CalculateCraftingCost(ctx context.Context, recipeID string) (map[string]float64, error) {
	recipe, err := s.repo.GetRecipeByID(ctx, recipeID)
	if err != nil {
		return nil, fmt.Errorf("failed to get recipe: %w", err)
	}

	costs := make(map[string]float64)

	// Стоимость материалов (рыночные цены)
	for _, material := range recipe.Materials {
		// Получаем рыночную цену ресурса
		price, err := s.getResourceMarketPrice()
		if err != nil {
			s.logger.WithError(err).Warn("Failed to get market price for resource")
			continue
		}
		costs[material.ResourceID] = price * float64(material.Quantity)
	}

	// Стоимость времени (экономическая ценность)
	timeCost := s.calculateTimeCost(recipe.Duration, recipe.Tier)
	costs["time"] = timeCost

	// Стоимость станции (амортизация)
	stationCost := s.calculateStationCost(recipe.Tier)
	costs["station"] = stationCost

	// Риск провала
	failureRisk := (1.0 - recipe.SuccessRate) * s.calculateFailurePenalty(recipe.Tier)
	costs["risk"] = failureRisk

	// Общая стоимость
	totalCost := 0.0
	for _, cost := range costs {
		totalCost += cost
	}
	costs["total"] = totalCost

	return costs, nil
}

// processCrafting обрабатывает процесс крафта асинхронно
func (s *CraftingService) processCrafting(orderID string) {
	ctx := context.Background()

	// Обновляем статус на "crafting"
	now := time.Now()
	if err := s.repo.UpdateOrderStatus(ctx, orderID, "crafting", nil); err != nil {
		s.logger.WithError(err).Error("Failed to update order status to crafting")
		return
	}

	order, err := s.repo.GetOrderByID(ctx, orderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get order for processing")
		return
	}

	recipe, err := s.repo.GetRecipeByID(ctx, order.RecipeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get recipe for processing")
		return
	}

	station, err := s.repo.GetStationByID(ctx, order.StationID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get station for processing")
		return
	}

	// Имитируем время крафта
	time.Sleep(recipe.Duration)

	// Рассчитываем результат крафта
	result, success := s.calculateCraftingResult(order, recipe, station)

	// Обновляем статус заказа
	status := "completed"
	if !success {
		status = "failed"
	}

	if err := s.repo.UpdateOrderStatus(ctx, orderID, status, &now); err != nil {
		s.logger.WithError(err).Error("Failed to update order status to completed")
		return
	}

	// Обновляем статистику станции
	if err := s.repo.UpdateStationStats(ctx, order.StationID, success, float64(result.Quality)); err != nil {
		s.logger.WithError(err).Error("Failed to update station stats")
	}

	// TODO: Implement event publishing
	// Публикуем событие
	// event := map[string]interface{}{
	// 	"type":       "crafting_completed",
	// 	"order_id":   orderID,
	// 	"player_id":  order.PlayerID,
	// 	"recipe_id":  order.RecipeID,
	// 	"success":    success,
	// 	"quality":    result.Quality,
	// 	"item_id":    result.ItemID,
	// 	"quantity":   result.Quantity,
	// 	"timestamp":  now.Unix(),
	// }

	// TODO: Implement event bus publishing
	// if err := s.eventBus.Publish("crafting", event); err != nil {
	// 	s.logger.WithError(err).Error("Failed to publish crafting event")
	// }

	s.logger.WithFields(map[string]interface{}{
		"order_id": orderID,
		"success":  success,
		"quality":  result.Quality,
	}).Info("Crafting process completed")
}

// calculateCraftingResult рассчитывает результат крафта
func (s *CraftingService) calculateCraftingResult(order *models.CraftingOrder, recipe *models.CraftingRecipe, station *models.CraftingStation) (*models.CraftingResult, bool) {
	// Базовый шанс успеха
	successRate := recipe.SuccessRate

	// Модификаторы качества станции
	successRate *= station.Efficiency

	// Модификаторы от материалов (высококачественные материалы повышают шанс)
	materialBonus := 0.0
	for _, material := range order.UsedMaterials {
		if material.Quality > recipe.Quality {
			materialBonus += 0.05 // +5% за каждый уровень качества выше требуемого
		}
	}
	successRate += materialBonus

	// Случайный фактор
	randomFactor := rand.Float64()
	success := randomFactor <= successRate

	result := &models.CraftingResult{
		ItemID:   recipe.Output.ItemID,
		Quantity: recipe.Output.Quantity,
		Success:  success,
	}

	if success {
		// Рассчитываем качество результата
		baseQuality := recipe.Output.Quality

		// Модификаторы качества
		qualityModifiers := []models.CraftingQualityModifier{
			{Type: "station", Value: station.Efficiency - 1.0, Source: "station_efficiency"},
			{Type: "materials", Value: materialBonus, Source: "material_quality"},
			{Type: "luck", Value: (rand.Float64() - 0.5) * 0.2, Source: "random_luck"}, // ±10% luck
		}

		qualityMultiplier := 1.0
		for _, modifier := range qualityModifiers {
			qualityMultiplier += modifier.Value
		}

		result.Quality = int(math.Round(float64(baseQuality) * qualityMultiplier))

		// Ограничиваем качество разумными пределами
		if result.Quality < 1 {
			result.Quality = 1
		}
		if result.Quality > 100 {
			result.Quality = 100
		}
	} else {
		// При провале качество = 0, но можно вернуть некоторые материалы
		result.Quality = 0
		result.Quantity = 0
	}

	return result, success
}

// checkRecipeRequirements проверяет требования рецепта
func (s *CraftingService) checkRecipeRequirements(playerID string, recipe *models.CraftingRecipe, station *models.CraftingStation) error {
	// Проверяем тип станции
	if recipe.Requirements.StationType != "" && recipe.Requirements.StationType != station.Type {
		return fmt.Errorf("station type mismatch: required %s, got %s", recipe.Requirements.StationType, station.Type)
	}

	// Проверяем уровень станции
	if station.Tier < recipe.Tier {
		return fmt.Errorf("station tier too low: required %d, got %d", recipe.Tier, station.Tier)
	}

	// Проверяем навыки игрока (заглушка - нужно реализовать систему навыков)
	if recipe.Requirements.SkillLevel > 0 {
		// TODO: проверить уровень навыка игрока
		s.logger.WithFields(map[string]interface{}{
			"player_id":      playerID,
			"required_skill": recipe.Requirements.SkillLevel,
		}).Warn("Skill check not implemented yet")
	}

	return nil
}

// validateAndReserveMaterials проверяет и резервирует материалы
func (s *CraftingService) validateAndReserveMaterials(playerID string, usedMaterials []models.UsedMaterial, requiredMaterials []models.RecipeMaterial) error {
	// Создаем карту требуемых материалов
	requiredMap := make(map[string]models.RecipeMaterial)
	for _, req := range requiredMaterials {
		requiredMap[req.ResourceID] = req
	}

	// Проверяем предоставленные материалы
	for _, used := range usedMaterials {
		req, exists := requiredMap[used.ResourceID]
		if !exists && !req.IsOptional {
			return fmt.Errorf("unexpected material: %s", used.ResourceID)
		}

		if used.Quantity < req.Quantity {
			return fmt.Errorf("insufficient quantity for %s: required %d, got %d", used.ResourceID, req.Quantity, used.Quantity)
		}

		if used.Quality < req.Quality {
			return fmt.Errorf("insufficient quality for %s: required %d, got %d", used.ResourceID, req.Quality, used.Quality)
		}
	}

	// Проверяем обязательные материалы
	for resourceID, req := range requiredMap {
		found := false
		for _, used := range usedMaterials {
			if used.ResourceID == resourceID {
				found = true
				break
			}
		}
		if !found && !req.IsOptional {
			return fmt.Errorf("missing required material: %s", resourceID)
		}
	}

	// TODO: резервировать материалы в инвентаре игрока
	// Пока просто логируем
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"materials": usedMaterials,
	}).Info("Materials validated (reservation not implemented yet)")

	return nil
}

// releaseReservedMaterials возвращает зарезервированные материалы
func (s *CraftingService) releaseReservedMaterials(playerID string, materials []models.UsedMaterial) error {
	// TODO: вернуть материалы в инвентарь игрока
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"materials": materials,
	}).Info("Materials released (not implemented yet)")

	return nil
}

// getResourceMarketPrice получает рыночную цену ресурса
func (s *CraftingService) getResourceMarketPrice() (float64, error) {
	// TODO: получить цену из торгового сервиса
	// Пока возвращаем заглушку
	return 10.0, nil // 10 кредитов за единицу
}

// calculateTimeCost рассчитывает стоимость времени крафта
func (s *CraftingService) calculateTimeCost(duration time.Duration, tier int) float64 {
	// Стоимость минуты крафта в кредитах (зависит от уровня сложности)
	baseRate := 1.0                           // кредит в минуту
	tierMultiplier := 1.0 + float64(tier)*0.5 // более сложные рецепты дороже
	return baseRate * tierMultiplier * duration.Minutes()
}

// calculateStationCost рассчитывает стоимость использования станции
func (s *CraftingService) calculateStationCost(tier int) float64 {
	// Базовая стоимость использования станции
	baseCost := 5.0
	tierMultiplier := 1.0 + float64(tier)*0.3
	return baseCost * tierMultiplier
}

// calculateFailurePenalty рассчитывает штраф за провал
func (s *CraftingService) calculateFailurePenalty(tier int) float64 {
	// Штраф за провал (потеря материалов)
	basePenalty := 50.0
	tierMultiplier := 1.0 + float64(tier)*0.5
	return basePenalty * tierMultiplier
}
