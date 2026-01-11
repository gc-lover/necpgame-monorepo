// Package repository содержит репозиторий для работы с данными аукционов
// Issue: #2175 - Dynamic Pricing Auction House mechanics
// PERFORMANCE: Использует pgxpool для оптимальной работы с PostgreSQL в MMOFPS
package repository

import (
	"context"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/models"
)

// Repository интерфейс для работы с данными аукционов
type Repository interface {
	// Items
	GetItem(ctx context.Context, id string) (*models.Item, error)
	GetItemsByCategory(ctx context.Context, category string) ([]*models.Item, error)
	GetActiveItems(ctx context.Context) ([]*models.Item, error)
	CreateItem(ctx context.Context, item *models.Item) error
	UpdateItem(ctx context.Context, item *models.Item) error
	DeleteItem(ctx context.Context, id string) error

	// Bids
	GetItemBids(ctx context.Context, itemID string) ([]*models.Bid, error)
	CreateBid(ctx context.Context, bid *models.Bid) error
	GetWinningBid(ctx context.Context, itemID string) (*models.Bid, error)

	// Market Data
	GetMarketData(ctx context.Context, category string) (*models.MarketData, error)
	UpdateMarketData(ctx context.Context, marketData *models.MarketData) error
	GetPriceHistory(ctx context.Context, category string, timeFrame time.Duration) ([]*models.PricePoint, error)

	// Auction Results
	CreateAuctionResult(ctx context.Context, result *models.AuctionResult) error
	GetAuctionResults(ctx context.Context, category string, limit int) ([]*models.AuctionResult, error)
}

// PostgresRepository реализация репозитория для PostgreSQL
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPostgresRepository создает новый репозиторий
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) *PostgresRepository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// GetItem получает товар по ID
func (r *PostgresRepository) GetItem(ctx context.Context, id string) (*models.Item, error) {
	query := `
		SELECT id, name, category, rarity, base_price, current_bid, buyout_price,
			   seller_id, status, end_time, created_at, updated_at
		FROM auction.items
		WHERE id = $1
	`

	var item models.Item
	err := r.db.QueryRow(ctx, query, id).Scan(
		&item.ID, &item.Name, &item.Category, &item.Rarity, &item.BasePrice,
		&item.CurrentBid, &item.BuyoutPrice, &item.SellerID, &item.Status,
		&item.EndTime, &item.CreatedAt, &item.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get item", zap.String("id", id), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get item")
	}

	return &item, nil
}

// GetItemsByCategory получает товары по категории
func (r *PostgresRepository) GetItemsByCategory(ctx context.Context, category string) ([]*models.Item, error) {
	query := `
		SELECT id, name, category, rarity, base_price, current_bid, buyout_price,
			   seller_id, status, end_time, created_at, updated_at
		FROM auction.items
		WHERE category = $1 AND status = 'active'
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, category)
	if err != nil {
		r.logger.Error("Failed to get items by category", zap.String("category", category), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get items by category")
	}
	defer rows.Close()

	var items []*models.Item
	for rows.Next() {
		var item models.Item
		err := rows.Scan(
			&item.ID, &item.Name, &item.Category, &item.Rarity, &item.BasePrice,
			&item.CurrentBid, &item.BuyoutPrice, &item.SellerID, &item.Status,
			&item.EndTime, &item.CreatedAt, &item.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan item", zap.Error(err))
			continue
		}
		items = append(items, &item)
	}

	return items, nil
}

// GetActiveItems получает все активные товары
func (r *PostgresRepository) GetActiveItems(ctx context.Context) ([]*models.Item, error) {
	query := `
		SELECT id, name, category, rarity, base_price, current_bid, buyout_price,
			   seller_id, status, end_time, created_at, updated_at
		FROM auction.items
		WHERE status = 'active' AND end_time > NOW()
		ORDER BY end_time ASC
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get active items", zap.Error(err))
		return nil, errors.Wrap(err, "failed to get active items")
	}
	defer rows.Close()

	var items []*models.Item
	for rows.Next() {
		var item models.Item
		err := rows.Scan(
			&item.ID, &item.Name, &item.Category, &item.Rarity, &item.BasePrice,
			&item.CurrentBid, &item.BuyoutPrice, &item.SellerID, &item.Status,
			&item.EndTime, &item.CreatedAt, &item.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan item", zap.Error(err))
			continue
		}
		items = append(items, &item)
	}

	return items, nil
}

// CreateItem создает новый товар
func (r *PostgresRepository) CreateItem(ctx context.Context, item *models.Item) error {
	query := `
		INSERT INTO auction.items (
			id, name, category, rarity, base_price, current_bid, buyout_price,
			seller_id, status, end_time, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	item.CreatedAt = now
	item.UpdatedAt = now
	item.CurrentBid = item.BasePrice // Начальная ставка равна базовой цене

	_, err := r.db.Exec(ctx, query,
		item.ID, item.Name, item.Category, item.Rarity, item.BasePrice,
		item.CurrentBid, item.BuyoutPrice, item.SellerID, item.Status,
		item.EndTime, item.CreatedAt, item.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create item", zap.String("id", item.ID), zap.Error(err))
		return errors.Wrap(err, "failed to create item")
	}

	r.logger.Info("Item created", zap.String("id", item.ID), zap.String("name", item.Name))
	return nil
}

// UpdateItem обновляет товар
func (r *PostgresRepository) UpdateItem(ctx context.Context, item *models.Item) error {
	query := `
		UPDATE auction.items SET
			name = $2, current_bid = $3, buyout_price = $4, status = $5,
			end_time = $6, updated_at = $7
		WHERE id = $1
	`

	item.UpdatedAt = time.Now()

	result, err := r.db.Exec(ctx, query,
		item.ID, item.Name, item.CurrentBid, item.BuyoutPrice,
		item.Status, item.EndTime, item.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update item", zap.String("id", item.ID), zap.Error(err))
		return errors.Wrap(err, "failed to update item")
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("Item not found for update", zap.String("id", item.ID))
		return errors.New("item not found")
	}

	return nil
}

// DeleteItem удаляет товар
func (r *PostgresRepository) DeleteItem(ctx context.Context, id string) error {
	query := `UPDATE auction.items SET status = 'cancelled', updated_at = NOW() WHERE id = $1`

	result, err := r.db.Exec(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete item", zap.String("id", id), zap.Error(err))
		return errors.Wrap(err, "failed to delete item")
	}

	if result.RowsAffected() == 0 {
		return errors.New("item not found")
	}

	r.logger.Info("Item deleted", zap.String("id", id))
	return nil
}

// GetItemBids получает все ставки на товар
func (r *PostgresRepository) GetItemBids(ctx context.Context, itemID string) ([]*models.Bid, error) {
	query := `
		SELECT id, item_id, bidder_id, amount, timestamp, is_winning
		FROM auction.bids
		WHERE item_id = $1
		ORDER BY timestamp ASC
	`

	rows, err := r.db.Query(ctx, query, itemID)
	if err != nil {
		r.logger.Error("Failed to get item bids", zap.String("item_id", itemID), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get item bids")
	}
	defer rows.Close()

	var bids []*models.Bid
	for rows.Next() {
		var bid models.Bid
		err := rows.Scan(&bid.ID, &bid.ItemID, &bid.BidderID, &bid.Amount, &bid.Timestamp, &bid.IsWinning)
		if err != nil {
			r.logger.Error("Failed to scan bid", zap.Error(err))
			continue
		}
		bids = append(bids, &bid)
	}

	return bids, nil
}

// CreateBid создает новую ставку
func (r *PostgresRepository) CreateBid(ctx context.Context, bid *models.Bid) error {
	query := `
		INSERT INTO auction.bids (id, item_id, bidder_id, amount, timestamp, is_winning)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	bid.Timestamp = time.Now()

	_, err := r.db.Exec(ctx, query, bid.ID, bid.ItemID, bid.BidderID, bid.Amount, bid.Timestamp, bid.IsWinning)
	if err != nil {
		r.logger.Error("Failed to create bid", zap.String("id", bid.ID), zap.Error(err))
		return errors.Wrap(err, "failed to create bid")
	}

	r.logger.Info("Bid created",
		zap.String("id", bid.ID),
		zap.String("item_id", bid.ItemID),
		zap.Float64("amount", bid.Amount))

	return nil
}

// GetWinningBid получает выигрышную ставку
func (r *PostgresRepository) GetWinningBid(ctx context.Context, itemID string) (*models.Bid, error) {
	query := `
		SELECT id, item_id, bidder_id, amount, timestamp, is_winning
		FROM auction.bids
		WHERE item_id = $1 AND is_winning = true
		ORDER BY timestamp DESC
		LIMIT 1
	`

	var bid models.Bid
	err := r.db.QueryRow(ctx, query, itemID).Scan(
		&bid.ID, &bid.ItemID, &bid.BidderID, &bid.Amount, &bid.Timestamp, &bid.IsWinning,
	)

	if err != nil {
		r.logger.Error("Failed to get winning bid", zap.String("item_id", itemID), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get winning bid")
	}

	return &bid, nil
}

// GetMarketData получает рыночные данные для категории
func (r *PostgresRepository) GetMarketData(ctx context.Context, category string) (*models.MarketData, error) {
	query := `
		SELECT category, total_volume, average_price, median_price, price_std_dev,
			   supply_velocity, demand_velocity, price_elasticity, market_saturation, last_update
		FROM auction.market_data
		WHERE category = $1
		ORDER BY last_update DESC
		LIMIT 1
	`

	var marketData models.MarketData
	marketData.Category = category

	err := r.db.QueryRow(ctx, query, category).Scan(
		&marketData.Category, &marketData.TotalVolume, &marketData.AveragePrice,
		&marketData.MedianPrice, &marketData.PriceStdDev, &marketData.SupplyVelocity,
		&marketData.DemandVelocity, &marketData.PriceElasticity, &marketData.MarketSaturation,
		&marketData.LastUpdate,
	)

	if err != nil {
		r.logger.Error("Failed to get market data", zap.String("category", category), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get market data")
	}

	return &marketData, nil
}

// UpdateMarketData обновляет рыночные данные
func (r *PostgresRepository) UpdateMarketData(ctx context.Context, marketData *models.MarketData) error {
	query := `
		INSERT INTO auction.market_data (
			category, total_volume, average_price, median_price, price_std_dev,
			supply_velocity, demand_velocity, price_elasticity, market_saturation, last_update
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		ON CONFLICT (category) DO UPDATE SET
			total_volume = EXCLUDED.total_volume,
			average_price = EXCLUDED.average_price,
			median_price = EXCLUDED.median_price,
			price_std_dev = EXCLUDED.price_std_dev,
			supply_velocity = EXCLUDED.supply_velocity,
			demand_velocity = EXCLUDED.demand_velocity,
			price_elasticity = EXCLUDED.price_elasticity,
			market_saturation = EXCLUDED.market_saturation,
			last_update = EXCLUDED.last_update
	`

	marketData.LastUpdate = time.Now()

	_, err := r.db.Exec(ctx, query,
		marketData.Category, marketData.TotalVolume, marketData.AveragePrice,
		marketData.MedianPrice, marketData.PriceStdDev, marketData.SupplyVelocity,
		marketData.DemandVelocity, marketData.PriceElasticity, marketData.MarketSaturation,
		marketData.LastUpdate,
	)

	if err != nil {
		r.logger.Error("Failed to update market data", zap.String("category", marketData.Category), zap.Error(err))
		return errors.Wrap(err, "failed to update market data")
	}

	return nil
}

// GetPriceHistory получает историю цен для категории
func (r *PostgresRepository) GetPriceHistory(ctx context.Context, category string, timeFrame time.Duration) ([]*models.PricePoint, error) {
	query := `
		SELECT timestamp, price, volume, type
		FROM auction.price_history
		WHERE category = $1 AND timestamp >= $2
		ORDER BY timestamp DESC
		LIMIT 1000
	`

	cutoffTime := time.Now().Add(-timeFrame)

	rows, err := r.db.Query(ctx, query, category, cutoffTime)
	if err != nil {
		r.logger.Error("Failed to get price history",
			zap.String("category", category), zap.Duration("timeframe", timeFrame), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get price history")
	}
	defer rows.Close()

	var pricePoints []*models.PricePoint
	for rows.Next() {
		var point models.PricePoint
		err := rows.Scan(&point.Timestamp, &point.Price, &point.Volume, &point.Type)
		if err != nil {
			r.logger.Error("Failed to scan price point", zap.Error(err))
			continue
		}
		pricePoints = append(pricePoints, &point)
	}

	return pricePoints, nil
}

// CreateAuctionResult сохраняет результат аукциона
func (r *PostgresRepository) CreateAuctionResult(ctx context.Context, result *models.AuctionResult) error {
	query := `
		INSERT INTO auction.auction_results (
			item_id, final_price, winner_id, seller_id, end_time, duration,
			total_bids, price_efficiency, market_impact
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		result.ItemID, result.FinalPrice, result.WinnerID, result.SellerID,
		result.EndTime, result.Duration, result.TotalBids, result.PriceEfficiency, result.MarketImpact,
	)

	if err != nil {
		r.logger.Error("Failed to create auction result", zap.String("item_id", result.ItemID), zap.Error(err))
		return errors.Wrap(err, "failed to create auction result")
	}

	return nil
}

// GetAuctionResults получает результаты аукционов
func (r *PostgresRepository) GetAuctionResults(ctx context.Context, category string, limit int) ([]*models.AuctionResult, error) {
	query := `
		SELECT ar.item_id, ar.final_price, ar.winner_id, ar.seller_id, ar.end_time,
			   ar.duration, ar.total_bids, ar.price_efficiency, ar.market_impact,
			   i.name as item_name
		FROM auction.auction_results ar
		JOIN auction.items i ON ar.item_id = i.id
		WHERE i.category = $1
		ORDER BY ar.end_time DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, category, limit)
	if err != nil {
		r.logger.Error("Failed to get auction results",
			zap.String("category", category), zap.Int("limit", limit), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get auction results")
	}
	defer rows.Close()

	var results []*models.AuctionResult
	for rows.Next() {
		var result models.AuctionResult
		var itemName string
		err := rows.Scan(
			&result.ItemID, &result.FinalPrice, &result.WinnerID, &result.SellerID,
			&result.EndTime, &result.Duration, &result.TotalBids, &result.PriceEfficiency,
			&result.MarketImpact, &itemName,
		)
		if err != nil {
			r.logger.Error("Failed to scan auction result", zap.Error(err))
			continue
		}
		results = append(results, &result)
	}

	return results, nil
}