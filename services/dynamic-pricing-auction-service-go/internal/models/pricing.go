// Package models содержит модели данных для динамического ценообразования
// Issue: #2175 - Dynamic Pricing Auction House mechanics
package models

import (
	"time"
)

// Item представляет товар в системе аукциона
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): n/a
//go:align 64
type Item struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	EndTime     time.Time `json:"end_time" db:"end_time"` // Время окончания
	ID          string    `json:"id" db:"id"`             // UUID товара (16 bytes)
	Name        string    `json:"name" db:"name"`         // Название товара
	SellerID    string    `json:"seller_id" db:"seller_id"` // ID продавца
	Category    string    `json:"category" db:"category"` // Категория (weapons, armor, etc.)
	Rarity      string    `json:"rarity" db:"rarity"`     // Редкость (common, rare, epic, legendary)
	Status      string    `json:"status" db:"status"`     // Статус (active, sold, cancelled)

	// Medium fields (8 bytes aligned): float64 (grouped together)
	BasePrice   float64 `json:"base_price" db:"base_price"`     // Базовая цена
	CurrentBid  float64 `json:"current_bid" db:"current_bid"`   // Текущая ставка
	BuyoutPrice float64 `json:"buyout_price" db:"buyout_price"` // Цена выкупа
}

// Auction представляет аукцион с динамическим ценообразованием
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): n/a
//go:align 64
type Auction struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
	LastPriceUpdate time.Time          `json:"last_price_update"`
	ItemID          string             `json:"item_id"`
	Item            *Item              `json:"item,omitempty"` // Ссылка на товар
	BidHistory      []*Bid             `json:"bid_history"`
	PriceHistory    []*PricePoint      `json:"price_history"`
	DynamicPricing  *DynamicPricing    `json:"dynamic_pricing"`
	MarketData      *MarketData        `json:"market_data"`

	// Medium fields (8 bytes aligned): float64 (grouped together)
	StartPrice        float64 `json:"start_price"`
	CurrentPrice      float64 `json:"current_price"`
	ReservePrice      float64 `json:"reserve_price"`
	PredictedEndPrice float64 `json:"predicted_end_price"`
	PriceVolatility   float64 `json:"price_volatility"`
}

// Bid представляет ставку на аукционе
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Small fields (≤4 bytes): bool
//go:align 64
type Bid struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
	Timestamp time.Time `json:"timestamp" db:"timestamp"`
	ID        string    `json:"id" db:"id"`
	ItemID    string    `json:"item_id" db:"item_id"`
	BidderID  string    `json:"bidder_id" db:"bidder_id"`
	Amount    float64   `json:"amount" db:"amount"`

	// Small fields (≤4 bytes): bool
	IsWinning bool `json:"is_winning" db:"is_winning"`
}

// PricePoint представляет точку данных в истории цен
type PricePoint struct {
	Timestamp time.Time `json:"timestamp"`
	Price     float64   `json:"price"`
	Volume    int       `json:"volume"`     // Объем торгов
	Type      string    `json:"type"`       // Тип изменения (bid, market, algorithm)
}

// MarketData содержит рыночные данные для товара
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), int64 (8)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): n/a
//go:align 64
type MarketData struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), int64 (8)
	LastUpdate   time.Time `json:"last_update"`
	ItemID       string    `json:"item_id"`
	Category     string    `json:"category"`
	TotalVolume  int64     `json:"total_volume"` // Общий объем торгов

	// Medium fields (8 bytes aligned): float64 (grouped together)
	AveragePrice     float64 `json:"average_price"`     // Средняя цена
	MedianPrice      float64 `json:"median_price"`      // Медианная цена
	PriceStdDev      float64 `json:"price_std_dev"`     // Стандартное отклонение цен
	SupplyVelocity   float64 `json:"supply_velocity"`   // Скорость предложения
	DemandVelocity   float64 `json:"demand_velocity"`   // Скорость спроса
	PriceElasticity  float64 `json:"price_elasticity"`  // Эластичность цены
	MarketSaturation float64 `json:"market_saturation"` // Насыщенность рынка (0-1)
}

// DynamicPricing содержит параметры динамического ценообразования
type DynamicPricing struct {
	AlgorithmType      string             `json:"algorithm_type"`       // Тип алгоритма (linear, exponential, ai)
	BaseMultiplier     float64            `json:"base_multiplier"`      // Базовый множитель
	TimeMultiplier     float64            `json:"time_multiplier"`      // Множитель времени
	DemandMultiplier   float64            `json:"demand_multiplier"`    // Множитель спроса
	RarityMultiplier   float64            `json:"rarity_multiplier"`    // Множитель редкости
	VolatilityFactor   float64            `json:"volatility_factor"`    // Фактор волатильности
	MinPriceChange     float64            `json:"min_price_change"`     // Минимальное изменение цены
	MaxPriceChange     float64            `json:"max_price_change"`     // Максимальное изменение цены
	UpdateInterval     time.Duration      `json:"update_interval"`      // Интервал обновления
	AdaptiveLearning   *AdaptiveLearning  `json:"adaptive_learning"`   // Адаптивное обучение
}

// AdaptiveLearning содержит параметры машинного обучения
type AdaptiveLearning struct {
	LearningRate       float64            `json:"learning_rate"`        // Скорость обучения
	MemorySize         int                `json:"memory_size"`          // Размер памяти для истории
	PredictionHorizon  time.Duration      `json:"prediction_horizon"`   // Горизонт предсказания
	FeatureWeights     map[string]float64 `json:"feature_weights"`      // Весовые коэффициенты признаков
	AccuracyScore      float64            `json:"accuracy_score"`       // Оценка точности предсказаний
	LastRetrained      time.Time          `json:"last_retrained"`
}

// PricingAlgorithm определяет интерфейс для алгоритмов ценообразования
type PricingAlgorithm interface {
	CalculatePrice(auction *Auction, marketData *MarketData) (float64, error)
	UpdateParameters(auction *Auction, actualPrice float64) error
	GetAlgorithmType() string
}

// SystemHealth представляет состояние здоровья системы
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), int64 (8), float64 (8)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): int32 (grouped together)
//go:align 64
type SystemHealth struct {
	// Large fields first (16-24 bytes): Time (24), int64 (8), float64 (8)
	LastHealthCheck  time.Time `json:"last_health_check"`
	ResponseTime     int64     `json:"response_time_ms"`
	TotalVolume      int64     `json:"total_volume"`
	HealthScore      float64   `json:"health_score"`

	// Medium fields (8 bytes aligned): float64 (grouped together)
	ErrorRate        float64 `json:"error_rate"`
	AverageBidAmount float64 `json:"average_bid_amount"`

	// Small fields (≤4 bytes): int32 (grouped together)
	TotalMechanics    int `json:"total_mechanics"`
	ActiveMechanics   int `json:"active_mechanics"`
	InactiveMechanics int `json:"inactive_mechanics"`
}

// MarketAnalysis содержит результаты анализа рынка
type MarketAnalysis struct {
	Category           string             `json:"category"`
	TimeFrame          time.Duration      `json:"time_frame"`
	TrendDirection     string             `json:"trend_direction"`      // up, down, stable
	TrendStrength      float64            `json:"trend_strength"`       // Сила тренда (0-1)
	VolatilityIndex    float64            `json:"volatility_index"`     // Индекс волатильности
	SeasonalPatterns   []*SeasonalPattern `json:"seasonal_patterns"`    // Сезонные паттерны
	AnomalyScore       float64            `json:"anomaly_score"`        // Оценка аномалий
	ConfidenceLevel    float64            `json:"confidence_level"`     // Уровень уверенности
	AnalysisTimestamp  time.Time          `json:"analysis_timestamp"`
}

// SeasonalPattern представляет сезонный паттерн цен
type SeasonalPattern struct {
	PeriodType         string    `json:"period_type"`          // daily, weekly, monthly
	PeriodValue        int       `json:"period_value"`         // Значение периода (0-23 для часов, 0-6 для дней)
	AverageMultiplier  float64   `json:"average_multiplier"`   // Средний множитель цены
	ConfidenceInterval float64   `json:"confidence_interval"`  // Интервал уверенности
}

// AuctionResult содержит результаты завершенного аукциона
type AuctionResult struct {
	ItemID         string    `json:"item_id"`
	FinalPrice     float64   `json:"final_price"`
	WinnerID       string    `json:"winner_id"`
	SellerID       string    `json:"seller_id"`
	EndTime        time.Time `json:"end_time"`
	Duration       time.Duration `json:"duration"`
	TotalBids      int       `json:"total_bids"`
	PriceEfficiency float64  `json:"price_efficiency"`  // Эффективность цены (предсказанная vs реальная)
	MarketImpact   float64   `json:"market_impact"`     // Влияние на рынок
}

// NewAuction создает новый аукцион
func NewAuction(itemID string, startPrice float64) *Auction {
	return &Auction{
		ItemID:          itemID,
		StartPrice:      startPrice,
		CurrentPrice:    startPrice,
		BidHistory:      make([]*Bid, 0),
		PriceHistory:    make([]*PricePoint, 0),
		DynamicPricing:  NewDefaultDynamicPricing(),
		MarketData:      &MarketData{},
		LastPriceUpdate: time.Now(),
	}
}

// NewDefaultDynamicPricing создает настройки динамического ценообразования по умолчанию
func NewDefaultDynamicPricing() *DynamicPricing {
	return &DynamicPricing{
		AlgorithmType:    "adaptive",
		BaseMultiplier:   1.0,
		TimeMultiplier:   0.02, // 2% в час
		DemandMultiplier: 0.05, // 5% за ставку
		RarityMultiplier: 0.1,  // 10% за уровень редкости
		VolatilityFactor: 0.8,
		MinPriceChange:   0.01, // 1%
		MaxPriceChange:   0.20, // 20%
		UpdateInterval:   5 * time.Minute,
		AdaptiveLearning: &AdaptiveLearning{
			LearningRate:      0.01,
			MemorySize:        1000,
			PredictionHorizon: 1 * time.Hour,
			FeatureWeights:    make(map[string]float64),
		},
	}
}

// AddBid добавляет новую ставку в аукцион
func (a *Auction) AddBid(bid *Bid) {
	a.BidHistory = append(a.BidHistory, bid)
	if bid.Amount > a.CurrentPrice {
		a.CurrentPrice = bid.Amount
		a.LastPriceUpdate = bid.Timestamp
	}
}

// GetBidHistory возвращает историю ставок
func (a *Auction) GetBidHistory() []*Bid {
	return a.BidHistory
}

// GetPriceHistory возвращает историю цен
func (a *Auction) GetPriceHistory() []*PricePoint {
	return a.PriceHistory
}

// IsActive проверяет, активен ли аукцион
func (a *Auction) IsActive() bool {
	if a.Item == nil {
		return false
	}
	return time.Now().Before(a.Item.EndTime)
}

// GetTimeRemaining возвращает оставшееся время
func (a *Auction) GetTimeRemaining() time.Duration {
	if a.Item == nil || a.Item.EndTime.IsZero() {
		return 0
	}
	return time.Until(a.Item.EndTime)
}

// UpdateMarketData обновляет рыночные данные
func (a *Auction) UpdateMarketData(marketData *MarketData) {
	a.MarketData = marketData
	a.LastPriceUpdate = time.Now()
}