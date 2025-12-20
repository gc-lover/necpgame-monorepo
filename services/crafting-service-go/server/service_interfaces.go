// Package server Issue: #2203 - Service interfaces for crafting system
package server

import (
	"context"

	"github.com/google/uuid"
)

// RecipeServiceInterface defines recipe service contract
type RecipeServiceInterface interface {
	GetRecipe(ctx context.Context, recipeID uuid.UUID) (*Recipe, error)
	ListRecipes(ctx context.Context, category *string, tier *int, limit, offset int) ([]Recipe, int, error)
	CreateRecipe(ctx context.Context, recipe *Recipe) error
	UpdateRecipe(ctx context.Context, recipe *Recipe) error
	DeleteRecipe(ctx context.Context, recipeID uuid.UUID) error
}

// OrderServiceInterface defines order service contract
type OrderServiceInterface interface {
	GetOrder(ctx context.Context, orderID uuid.UUID) (*Order, error)
	ListOrders(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]Order, int, error)
	CreateOrder(ctx context.Context, playerID uuid.UUID, recipeID uuid.UUID, stationID *uuid.UUID, qualityModifier float64) (*Order, error)
	UpdateOrder(ctx context.Context, order *Order) error
	CancelOrder(ctx context.Context, orderID uuid.UUID) error
	StartOrder(ctx context.Context, orderID uuid.UUID) error
	CompleteOrder(ctx context.Context, orderID uuid.UUID) error
}

// StationServiceInterface defines station service contract
type StationServiceInterface interface {
	GetStation(ctx context.Context, stationID uuid.UUID) (*Station, error)
	ListStations(ctx context.Context, zoneID *uuid.UUID, stationType *string, available *bool, limit, offset int) ([]Station, int, error)
	UpdateStation(ctx context.Context, station *Station) error
	BookStation(ctx context.Context, stationID uuid.UUID, playerID uuid.UUID, duration int, priority int) (*StationBooking, error)
	IsStationAvailable(ctx context.Context, stationID uuid.UUID) (bool, error)
}

// ChainServiceInterface defines production chain service contract
type ChainServiceInterface interface {
	GetProductionChain(ctx context.Context, chainID uuid.UUID) (*ProductionChain, error)
	ListProductionChains(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]ProductionChain, int, error)
	CreateProductionChain(ctx context.Context, chain *ProductionChain) error
	UpdateProductionChain(ctx context.Context, chain *ProductionChain) error
	DeleteProductionChain(ctx context.Context, chainID uuid.UUID) error
	StartChain(ctx context.Context, chainID uuid.UUID) error
	PauseChain(ctx context.Context, chainID uuid.UUID) error
	ResumeChain(ctx context.Context, chainID uuid.UUID) error
}
