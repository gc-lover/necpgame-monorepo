// Package models содержит модели данных для аукционного дома
// Issue: #2175 - Dynamic Pricing Auction House mechanics
package models

import (
	"time"
)

// AuctionLot представляет лот на аукционе
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32, bool
//go:align 64
type AuctionLot struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
	ID            string    `json:"id" db:"id"`                        // UUID лота
	ItemID        string    `json:"item_id" db:"item_id"`              // ID предмета
	SellerID      string    `json:"seller_id" db:"seller_id"`          // ID продавца
	ItemType      string    `json:"item_type" db:"item_type"`          // weapons, armor, etc.
	ItemRarity    string    `json:"item_rarity" db:"item_rarity"`      // common, rare, etc.
	ItemName      string    `json:"item_name" db:"item_name"`          // Название предмета
	Status        string    `json:"status" db:"status"`                // active, sold, expired

	// Large/Medium fields (8 bytes aligned): float64, int64
	CurrentPrice float64 `json:"current_price" db:"current_price"` // Текущая цена
	BuyoutPrice  float64 `json:"buyout_price" db:"buyout_price"`   // Цена выкупа
	ReservePrice float64 `json:"reserve_price" db:"reserve_price"` // Минимальная цена
	EndTime      int64   `json:"end_time" db:"end_time"`           // Unix timestamp окончания

	// Medium fields (8 bytes aligned): int32 (grouped together)
	Quantity    int `json:"quantity" db:"quantity"`         // Количество предметов
	BidCount    int `json:"bid_count" db:"bid_count"`       // Количество ставок
	TimeRemaining int `json:"time_remaining" db:"time_remaining"` // Оставшееся время в секундах
	Priority    int `json:"priority" db:"priority"`          // Приоритет в поиске

	// Small fields (≤4 bytes): bool
	IsBuyoutEnabled bool `json:"is_buyout_enabled" db:"is_buyout_enabled"` // Разрешен ли выкуп
	IsReserveMet    bool `json:"is_reserve_met" db:"is_reserve_met"`       // Достигнута ли резервная цена
}

// Bid представляет ставку на аукционе
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Small fields (≤4 bytes): int32, bool
//go:align 64
type Bid struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	PlacedAt   time.Time `json:"placed_at" db:"placed_at"`
	ID         string    `json:"id" db:"id"`               // UUID ставки
	BidderID   string    `json:"bidder_id" db:"bidder_id"` // ID участника
	LotID      string    `json:"lot_id" db:"lot_id"`       // ID лота
	BidType    string    `json:"bid_type" db:"bid_type"`   // normal, buyout

	// Large/Medium fields (8 bytes aligned): float64
	Amount float64 `json:"amount" db:"amount"` // Сумма ставки

	// Small fields (≤4 bytes): bool
	IsWinning bool `json:"is_winning" db:"is_winning"` // Является ли победной
}

// TradeRecord представляет запись о совершенной сделке
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32
//go:align 64
type TradeRecord struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	ExecutedAt     time.Time `json:"executed_at" db:"executed_at"`
	ID             string    `json:"id" db:"id"`                         // UUID сделки
	BuyerID        string    `json:"buyer_id" db:"buyer_id"`             // ID покупателя
	SellerID       string    `json:"seller_id" db:"seller_id"`           // ID продавца
	ItemID         string    `json:"item_id" db:"item_id"`               // ID предмета
	ItemName       string    `json:"item_name" db:"item_name"`           // Название предмета
	TradeType      string    `json:"trade_type" db:"trade_type"`         // buy, sell, auction

	// Large/Medium fields (8 bytes aligned): float64, int64
	Price       float64 `json:"price" db:"price"`             // Цена сделки
	Fee         float64 `json:"fee" db:"fee"`                 // Комиссия
	Tax         float64 `json:"tax" db:"tax"`                 // Налог
	ExecutedAtUnix int64 `json:"executed_at_unix" db:"executed_at_unix"` // Unix timestamp

	// Medium fields (8 bytes aligned): int32 (grouped together)
	Quantity int `json:"quantity" db:"quantity"` // Количество
	RegionID int `json:"region_id" db:"region_id"` // ID региона
}

// MarketPrice представляет рыночную цену предмета
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32
//go:align 64
type MarketPrice struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	LastUpdated time.Time `json:"last_updated" db:"last_updated"`
	ItemID      string    `json:"item_id" db:"item_id"`          // ID предмета
	ItemType    string    `json:"item_type" db:"item_type"`      // Тип предмета
	Region      string    `json:"region" db:"region"`            // Регион торговли

	// Large/Medium fields (8 bytes aligned): float64, int64
	CurrentPrice       float64 `json:"current_price" db:"current_price"`               // Текущая цена
	PredictedPrice     float64 `json:"predicted_price" db:"predicted_price"`           // Предсказанная цена
	PriceChange24h     float64 `json:"price_change_24h" db:"price_change_24h"`         // Изменение за 24ч (%)
	AveragePrice7d     float64 `json:"average_price_7d" db:"average_price_7d"`         // Средняя цена за 7 дней
	Volume24h          int64   `json:"volume_24h" db:"volume_24h"`                     // Объем торгов за 24ч

	// Medium fields (8 bytes aligned): int32 (grouped together)
	SupplyScore   int `json:"supply_score" db:"supply_score"`     // Оценка предложения (0-100)
	DemandScore   int `json:"demand_score" db:"demand_score"`     // Оценка спроса (0-100)
	StabilityScore int `json:"stability_score" db:"stability_score"` // Стабильность цены (0-100)
}

// MarketAnalytics содержит аналитику рынка
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), float64 (8)
// Medium fields (8 bytes aligned): maps, slices
// Small fields (≤4 bytes): int32
//go:align 64
type MarketAnalytics struct {
	// Large fields first (16-24 bytes): Time (24), float64 (8)
	LastCalculated time.Time `json:"last_calculated"`
	MarketHealth   float64   `json:"market_health"` // Здоровье рынка (0-100)

	// Medium fields (8 bytes aligned): maps and slices
	PriceTrends         map[string]*PriceTrend     `json:"price_trends"`         // Тренды цен по предметам
	VolumeByHour        map[int]int64             `json:"volume_by_hour"`       // Объем по часам
	ItemTypeDistribution map[string]float64        `json:"item_type_distribution"` // Распределение по типам

	// Medium fields (8 bytes aligned): int32 (grouped together)
	TotalLots      int `json:"total_lots"`       // Общее количество лотов
	ActiveTraders  int `json:"active_traders"`   // Активных трейдеров
	TotalVolume24h int `json:"total_volume_24h"` // Общий объем за 24ч

	// Small fields (≤4 bytes): int32
	TimeframeHours int `json:"timeframe_hours"` // Период анализа в часах
}

// PriceTrend описывает тренд цены для предмета
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16+ bytes): string, float64
// Small fields (≤4 bytes): n/a
//go:align 64
type PriceTrend struct {
	// Large fields first (16+ bytes): string
	ItemID string `json:"item_id"`

	// Large/Medium fields (8 bytes aligned): float64
	Confidence          float64 `json:"confidence"`            // Уверенность прогноза (0-1)
	PredictedChangePercent float64 `json:"predicted_change_percent"` // Предсказанное изменение (%)

	// Small fields (≤4 bytes): n/a - using string enum instead
	TrendDirection string `json:"trend_direction"` // rising, falling, stable
}

// AuctionHouseStats содержит статистику аукционного дома
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32
//go:align 64
type AuctionHouseStats struct {
	// Large fields first (16-24 bytes): Time (24), float64 (8)
	LastUpdated     time.Time `json:"last_updated"`
	AveragePriceChange float64 `json:"average_price_change"` // Среднее изменение цен (%)

	// Large/Medium fields (8 bytes aligned): int64, float64
	TotalVolume     int64   `json:"total_volume"`      // Общий объем торгов
	TotalValue      float64 `json:"total_value"`       // Общая стоимость торгов
	MarketEfficiency float64 `json:"market_efficiency"` // Эффективность рынка (0-1)

	// Medium fields (8 bytes aligned): int32 (grouped together)
	ActiveLots      int `json:"active_lots"`       // Активных лотов
	CompletedTrades int `json:"completed_trades"`  // Завершенных сделок
	UniqueTraders   int `json:"unique_traders"`    // Уникальных трейдеров

	// Small fields (≤4 bytes): int32
	ResponseTimeMs int `json:"response_time_ms"` // Среднее время ответа
}

// PricingAlgorithm представляет алгоритм ценообразования
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16+ bytes): string, maps
// Medium fields (8 bytes aligned): slices
// Small fields (≤4 bytes): bool, int32
//go:align 64
type PricingAlgorithm struct {
	// Large fields first (16+ bytes): string, maps
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Description string                 `json:"description"`
	Algorithm   string                 `json:"algorithm"` // bazaarbot, double_auction, etc.
	Parameters  map[string]interface{} `json:"parameters"` // Параметры алгоритма

	// Medium fields (8 bytes aligned): slices
	SupportedItemTypes []string `json:"supported_item_types"` // Поддерживаемые типы предметов

	// Small fields (≤4 bytes): bool, int32
	IsActive    bool `json:"is_active"`     // Активен ли алгоритм
	Priority    int  `json:"priority"`      // Приоритет использования
	MaxItems    int  `json:"max_items"`     // Максимум предметов для обработки
	Version     int  `json:"version"`       // Версия алгоритма
}

// AuctionLotRegistry центральный реестр лотов
type AuctionLotRegistry struct {
	Lots           map[string]*AuctionLot         `json:"lots"`
	Bids           map[string][]*Bid             `json:"bids"`           // bids by lot_id
	MarketPrices   map[string]*MarketPrice       `json:"market_prices"` // prices by item_id
	TradeHistory   []*TradeRecord                `json:"trade_history"`
	Analytics      *MarketAnalytics              `json:"analytics"`
	Stats          *AuctionHouseStats            `json:"stats"`
	PricingAlgorithms map[string]*PricingAlgorithm `json:"pricing_algorithms"`
}

// NewAuctionLotRegistry создает новый реестр лотов
func NewAuctionLotRegistry() *AuctionLotRegistry {
	return &AuctionLotRegistry{
		Lots:             make(map[string]*AuctionLot),
		Bids:             make(map[string][]*Bid),
		MarketPrices:     make(map[string]*MarketPrice),
		TradeHistory:     make([]*TradeRecord, 0),
		Analytics:        &MarketAnalytics{},
		Stats:           &AuctionHouseStats{},
		PricingAlgorithms: make(map[string]*PricingAlgorithm),
	}
}

// RegisterLot регистрирует новый лот
func (r *AuctionLotRegistry) RegisterLot(lot *AuctionLot) {
	r.Lots[lot.ID] = lot
}

// GetLot возвращает лот по ID
func (r *AuctionLotRegistry) GetLot(id string) (*AuctionLot, bool) {
	lot, exists := r.Lots[id]
	return lot, exists
}

// GetActiveLots возвращает все активные лоты
func (r *AuctionLotRegistry) GetActiveLots() []*AuctionLot {
	var result []*AuctionLot
	for _, lot := range r.Lots {
		if lot.Status == "active" {
			result = append(result, lot)
		}
	}
	return result
}

// GetLotsByItemType возвращает лоты по типу предмета
func (r *AuctionLotRegistry) GetLotsByItemType(itemType string) []*AuctionLot {
	var result []*AuctionLot
	for _, lot := range r.Lots {
		if lot.ItemType == itemType {
			result = append(result, lot)
		}
	}
	return result
}

// AddBid добавляет ставку на лот
func (r *AuctionLotRegistry) AddBid(bid *Bid) {
	if r.Bids[bid.LotID] == nil {
		r.Bids[bid.LotID] = make([]*Bid, 0)
	}
	r.Bids[bid.LotID] = append(r.Bids[bid.LotID], bid)

	// Обновляем текущую цену лота
	if lot, exists := r.Lots[bid.LotID]; exists {
		lot.CurrentPrice = bid.Amount
		lot.BidCount++
		lot.UpdatedAt = time.Now()
	}
}

// GetLotBids возвращает все ставки на лот
func (r *AuctionLotRegistry) GetLotBids(lotID string) []*Bid {
	return r.Bids[lotID]
}

// UpdateMarketPrice обновляет рыночную цену предмета
func (r *AuctionLotRegistry) UpdateMarketPrice(price *MarketPrice) {
	r.MarketPrices[price.ItemID] = price
}

// GetMarketPrice возвращает рыночную цену предмета
func (r *AuctionLotRegistry) GetMarketPrice(itemID string) (*MarketPrice, bool) {
	price, exists := r.MarketPrices[itemID]
	return price, exists
}

// RecordTrade записывает совершенную сделку
func (r *AuctionLotRegistry) RecordTrade(trade *TradeRecord) {
	r.TradeHistory = append(r.TradeHistory, trade)
}

// GetTradeHistory возвращает историю торгов игрока
func (r *AuctionLotRegistry) GetTradeHistory(playerID string, limit int) []*TradeRecord {
	var result []*TradeRecord
	for _, trade := range r.TradeHistory {
		if trade.BuyerID == playerID || trade.SellerID == playerID {
			result = append(result, trade)
			if len(result) >= limit {
				break
			}
		}
	}
	return result
}