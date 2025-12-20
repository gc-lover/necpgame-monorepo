// Package server Issue: #61 - Player Market Service Implementation
package server

// PlayerMarketService implements business logic
type PlayerMarketService struct {
	repo *PlayerMarketRepository
}

// NewPlayerMarketService creates new service
