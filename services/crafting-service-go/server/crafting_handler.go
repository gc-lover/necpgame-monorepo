// Issue: #2203 - Ogen API handlers implementation
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/crafting-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// CraftingHandler implements ogen StrictHandler
type CraftingHandler struct {
	recipeService   RecipeServiceInterface
	orderService    OrderServiceInterface
	stationService  StationServiceInterface
	chainService    ChainServiceInterface
	logger          *logrus.Logger
	jwtValidator    *JwtValidator
	authEnabled     bool
}

// NewCraftingHandler creates new crafting handler
func NewCraftingHandler(recipeSvc RecipeServiceInterface, orderSvc OrderServiceInterface, stationSvc StationServiceInterface, chainSvc ChainServiceInterface, logger *logrus.Logger, jwtValidator *JwtValidator, authEnabled bool) *CraftingHandler {
	return &CraftingHandler{
		recipeService:  recipeSvc,
		orderService:   orderSvc,
		stationService: stationSvc,
		chainService:   chainSvc,
		logger:         logger,
		jwtValidator:   jwtValidator,
		authEnabled:    authEnabled,
	}
}

// extractPlayerID extracts player ID from request context
func (h *CraftingHandler) extractPlayerID(ctx context.Context) (uuid.UUID, error) {
	if !h.authEnabled {
		// Return mock player ID for testing
		return uuid.New(), nil
	}

	return h.jwtValidator.ExtractPlayerID(ctx)
}

// RECIPE HANDLERS

func (h *CraftingHandler) ListRecipes(ctx context.Context, params api.ListRecipesParams) (api.ListRecipesRes, error) {
	var category *string
	var tier *int

	if params.Category.IsSet() {
		cat := string(params.Category.Value)
		category = &cat
	}

	if params.Tier.IsSet() {
		tierVal := int(params.Tier.Value)
		tier = &tierVal
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = int(params.Limit.Value)
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = int(params.Offset.Value)
	}

	recipes, total, err := h.recipeService.ListRecipes(ctx, category, tier, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list recipes")
		return &api.ListRecipesInternalServerError{}, nil
	}

	// Convert to API format
	apiRecipes := make([]api.Recipe, len(recipes))
	for i, recipe := range recipes {
		apiRecipes[i] = h.convertRecipeToAPI(recipe)
	}

	var limitOpt api.OptInt
	limitOpt.SetTo(limit)

	var offsetOpt api.OptInt
	offsetOpt.SetTo(offset)

	var totalCount api.OptInt
	totalCount.SetTo(total)

	return &api.ListRecipesOK{
		Recipes: apiRecipes,
		Total:   totalCount,
		Limit:   limitOpt,
		Offset:  offsetOpt,
	}, nil
}

func (h *CraftingHandler) CreateRecipe(ctx context.Context, req *api.CreateRecipeRequest) (api.CreateRecipeRes, error) {
	_, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.CreateRecipeUnauthorized{}, nil
	}

	// Convert API request to internal format
	recipe := h.convertCreateRecipeReqToInternal(*req)

	if err := h.recipeService.CreateRecipe(ctx, recipe); err != nil {
		h.logger.WithError(err).Error("Failed to create recipe")
		return &api.CreateRecipeInternalServerError{}, nil
	}

	recipeResponse := h.convertRecipeToAPI(*recipe)
	return &recipeResponse, nil
}

func (h *CraftingHandler) GetRecipe(ctx context.Context, params api.GetRecipeParams) (api.GetRecipeRes, error) {
	recipeID := params.RecipeId

	recipe, err := h.recipeService.GetRecipe(ctx, recipeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get recipe")
		return &api.GetRecipeInternalServerError{}, nil
	}

	recipeResponse := h.convertRecipeToAPI(*recipe)
	return &recipeResponse, nil
}

func (h *CraftingHandler) UpdateRecipe(ctx context.Context, req *api.UpdateRecipeRequest, params api.UpdateRecipeParams) (api.UpdateRecipeRes, error) {
	recipeID := params.RecipeId

	_, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.UpdateRecipeUnauthorized{}, nil
	}

	recipe := h.convertUpdateRecipeReqToInternal(*req)
	recipe.ID = recipeID

	if err := h.recipeService.UpdateRecipe(ctx, recipe); err != nil {
		h.logger.WithError(err).Error("Failed to update recipe")
		return &api.UpdateRecipeInternalServerError{}, nil
	}

	recipeResponse := h.convertRecipeToAPI(*recipe)
	return &recipeResponse, nil
}

func (h *CraftingHandler) DeleteRecipe(ctx context.Context, params api.DeleteRecipeParams) (api.DeleteRecipeRes, error) {
	recipeID := params.RecipeId

	if err := h.recipeService.DeleteRecipe(ctx, recipeID); err != nil {
		h.logger.WithError(err).Error("Failed to delete recipe")
		return &api.DeleteRecipeInternalServerError{}, nil
	}

	return &api.DeleteRecipeNoContent{}, nil
}

// ORDER HANDLERS

func (h *CraftingHandler) ListOrders(ctx context.Context, params api.ListOrdersParams) (api.ListOrdersRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.ListOrdersUnauthorized{}, nil
	}

	var status *string
	if params.Status.IsSet() {
		s := string(params.Status.Value)
		status = &s
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = int(params.Limit.Value)
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = int(params.Offset.Value)
	}

	orders, total, err := h.orderService.ListOrders(ctx, &playerID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list orders")
		return &api.ListOrdersInternalServerError{}, nil
	}

	apiOrders := make([]api.Order, len(orders))
	for i, order := range orders {
		apiOrders[i] = h.convertOrderToAPI(&order)
	}

	var totalCount api.OptInt
	totalCount.SetTo(total)

	return &api.ListOrdersOK{
		Orders: apiOrders,
		Total:  totalCount,
	}, nil
}

func (h *CraftingHandler) CreateOrder(ctx context.Context, req *api.CreateOrderRequest) (api.CreateOrderRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.CreateOrderUnauthorized{}, nil
	}

	recipeID := req.RecipeID

	var stationID *uuid.UUID
	if req.StationID.IsSet() {
		stationID = &req.StationID.Value
	}

	qualityModifier := 1.0
	if req.QualityModifier.IsSet() {
		qualityModifier = float64(req.QualityModifier.Value)
	}

	order, err := h.orderService.CreateOrder(ctx, playerID, recipeID, stationID, qualityModifier)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create order")
		return &api.CreateOrderInternalServerError{}, nil
	}

	orderResponse := h.convertOrderToAPI(order)
	return &orderResponse, nil
}

func (h *CraftingHandler) GetOrder(ctx context.Context, params api.GetOrderParams) (api.GetOrderRes, error) {
	orderID := params.OrderId

	order, err := h.orderService.GetOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get order")
		return &api.GetOrderInternalServerError{}, nil
	}

	orderResponse := h.convertOrderToAPI(order)
	return &orderResponse, nil
}

// STATION HANDLERS

func (h *CraftingHandler) ListStations(ctx context.Context, params api.ListStationsParams) (api.ListStationsRes, error) {
	var zoneID *uuid.UUID
	if params.ZoneID.IsSet() {
		zoneID = &params.ZoneID.Value
	}

	var stationType *string
	if params.Type.IsSet() {
		st := string(params.Type.Value)
		stationType = &st
	}

	var available *bool
	if params.Available.IsSet() {
		available = &params.Available.Value
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = int(params.Limit.Value)
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = int(params.Offset.Value)
	}

	stations, total, err := h.stationService.ListStations(ctx, zoneID, stationType, available, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list stations")
		return &api.ListStationsInternalServerError{}, nil
	}

	apiStations := make([]api.Station, len(stations))
	for i, station := range stations {
		apiStations[i] = h.convertStationToAPI(station)
	}

	var totalCount api.OptInt
	totalCount.SetTo(total)

	return &api.ListStationsOK{
		Stations: apiStations,
		Total:    totalCount,
	}, nil
}

func (h *CraftingHandler) GetStation(ctx context.Context, params api.GetStationParams) (api.GetStationRes, error) {
	stationID := params.StationId

	station, err := h.stationService.GetStation(ctx, stationID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get station")
		return &api.GetStationInternalServerError{}, nil
	}

	stationResponse := h.convertStationToAPI(*station)
	return &stationResponse, nil
}

func (h *CraftingHandler) BookCraftingStation(ctx context.Context, req *api.BookStationRequest, params api.BookCraftingStationParams) (api.BookCraftingStationRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.BookCraftingStationUnauthorized{}, nil
	}

	stationID := params.StationId

	duration := 3600 // Default 1 hour
	if req.Duration != 0 {
		duration = int(req.Duration)
	}

	priority := 5 // Default priority
	if req.Priority.IsSet() {
		priority = int(req.Priority.Value)
	}

	booking, err := h.stationService.BookStation(ctx, stationID, playerID, duration, priority)
	if err != nil {
		h.logger.WithError(err).Error("Failed to book station")
		return &api.BookCraftingStationInternalServerError{}, nil
	}

	bookingResponse := h.convertStationBookingToAPI(*booking)
	return &bookingResponse, nil
}

// PRODUCTION CHAIN HANDLERS

func (h *CraftingHandler) ListProductionChains(ctx context.Context, params api.ListProductionChainsParams) (api.ListProductionChainsRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.ListProductionChainsUnauthorized{}, nil
	}

	var status *string
	if params.Status.IsSet() {
		s := string(params.Status.Value)
		status = &s
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = int(params.Limit.Value)
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = int(params.Offset.Value)
	}

	chains, total, err := h.chainService.ListProductionChains(ctx, &playerID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list production chains")
		return &api.ListProductionChainsInternalServerError{}, nil
	}

	apiChains := make([]api.ProductionChain, len(chains))
	for i, chain := range chains {
		apiChains[i] = h.convertProductionChainToAPI(chain)
	}

	var totalCount api.OptInt
	totalCount.SetTo(total)

	return &api.ListProductionChainsOK{
		Chains: apiChains,
		Total:  totalCount,
	}, nil
}

func (h *CraftingHandler) CreateProductionChain(ctx context.Context, req *api.CreateChainRequest) (api.CreateProductionChainRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.CreateProductionChainUnauthorized{}, nil
	}

	productionChain := h.convertCreateChainReqToInternal(*req)
	productionChain.PlayerID = playerID

	if err := h.chainService.CreateProductionChain(ctx, productionChain); err != nil {
		h.logger.WithError(err).Error("Failed to create production chain")
		return &api.CreateProductionChainInternalServerError{}, nil
	}

	chainResponse := h.convertProductionChainToAPI(*productionChain)
	return &chainResponse, nil
}

func (h *CraftingHandler) GetProductionChain(ctx context.Context, params api.GetProductionChainParams) (api.GetProductionChainRes, error) {
	chainID := params.ChainId

	chain, err := h.chainService.GetProductionChain(ctx, chainID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get production chain")
		return &api.GetProductionChainInternalServerError{}, nil
	}

	chainResponse := h.convertProductionChainToAPI(*chain)
	return &chainResponse, nil
}

// CancelOrder cancels a crafting order
func (h *CraftingHandler) CancelOrder(ctx context.Context, params api.CancelOrderParams) (api.CancelOrderRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.CancelOrderUnauthorized{}, nil
	}

	orderID := params.OrderId

	// Check if order belongs to player
	order, err := h.orderService.GetOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get order")
		return &api.CancelOrderNotFound{}, nil
	}
	if order.PlayerID != playerID {
		return &api.CancelOrderUnauthorized{}, nil
	}

	if err := h.orderService.CancelOrder(ctx, orderID); err != nil {
		h.logger.WithError(err).Error("Failed to cancel order")
		return &api.CancelOrderInternalServerError{}, nil
	}

	return &api.CancelOrderNoContent{}, nil
}

// UpdateOrder updates order status or parameters
func (h *CraftingHandler) UpdateOrder(ctx context.Context, req *api.UpdateOrderRequest, params api.UpdateOrderParams) (api.UpdateOrderRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.UpdateOrderUnauthorized{}, nil
	}

	orderID := params.OrderId

	// Check if order belongs to player
	existingOrder, err := h.orderService.GetOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get order")
		return &api.UpdateOrderNotFound{}, nil
	}
	if existingOrder.PlayerID != playerID {
		return &api.UpdateOrderUnauthorized{}, nil
	}

	order, err := h.orderService.GetOrder(ctx, orderID)
	if err != nil {
		return &api.UpdateOrderInternalServerError{}, nil
	}

	// Update fields from request
	if req.Status.IsSet() {
		order.Status = string(req.Status.Value)
	}

	if req.QualityModifier.IsSet() {
		order.QualityModifier = float64(req.QualityModifier.Value)
	}

	if req.Progress.IsSet() {
		order.Progress = float64(req.Progress.Value)
	}

	if err := h.orderService.UpdateOrder(ctx, order); err != nil {
		h.logger.WithError(err).Error("Failed to update order")
		return &api.UpdateOrderInternalServerError{}, nil
	}

	apiOrder := h.convertOrderToAPI(order)
	return &apiOrder, nil
}

// UpdateProductionChain updates chain status or configuration
func (h *CraftingHandler) UpdateProductionChain(ctx context.Context, req *api.UpdateChainRequest, params api.UpdateProductionChainParams) (api.UpdateProductionChainRes, error) {
	playerID, err := h.extractPlayerID(ctx)
	if err != nil {
		return &api.UpdateProductionChainUnauthorized{}, nil
	}
	_ = playerID // TODO: Use for ownership check

	chainID := params.ChainId

	productionChain, err := h.chainService.GetProductionChain(ctx, chainID)
	if err != nil {
		return &api.UpdateProductionChainInternalServerError{}, nil
	}

	// Check ownership
	if productionChain.PlayerID != playerID {
		return &api.UpdateProductionChainUnauthorized{}, nil
	}

	// Update fields from request
	if req.Name.IsSet() {
		productionChain.Name = req.Name.Value
	}

	if req.Description.IsSet() {
		productionChain.Description = req.Description.Value
	}

	if req.Status.IsSet() {
		productionChain.Status = string(req.Status.Value)
	}

	if err := h.chainService.UpdateProductionChain(ctx, productionChain); err != nil {
		h.logger.WithError(err).Error("Failed to update production chain")
		return &api.UpdateProductionChainInternalServerError{}, nil
	}

	chainResponse := h.convertProductionChainToAPI(*productionChain)
	return &chainResponse, nil
}
