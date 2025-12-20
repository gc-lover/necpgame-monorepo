// Package server Issue: #42 - economy-player-market ogen typed handlers with business logic
package server

import (
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type MarketHandlersOgen struct {
	db         *pgxpool.Pool
	repository *PlayerMarketRepository
}

func NewMarketHandlersOgen(db *pgxpool.Pool) *MarketHandlersOgen {
	return &MarketHandlersOgen{
		db:         db,
		repository: NewPlayerMarketRepository(db),
	}
}
