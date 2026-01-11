// Package service содержит бизнес-логику аукционного дома
// Issue: #2175 - Dynamic Pricing Auction House mechanics
package service

import (
	"fmt"
	"math"
	"math/rand"
	"sort"
	"sync"
	"time"

	"necpgame/services/auction-house-service-go/internal/models"
)

// PricingEngine реализует динамические алгоритмы ценообразования
type PricingEngine struct {
	mu                   sync.RWMutex
	priceBeliefs         map[string]*PriceBelief        // item_id -> belief state
	supplyDemandHistory  map[string][]*SupplyDemandPoint // item_id -> historical data
	marketClearingPrices map[string]float64             // item_id -> clearing prices
	random               *rand.Rand
}

// PriceBelief представляет веру в цену для BazaarBot алгоритма
type PriceBelief struct {
	ItemID           string    `json:"item_id"`
	CurrentBelief    float64   `json:"current_belief"`     // Текущая оценка цены
	BeliefVariance   float64   `json:"belief_variance"`    // Дисперсия веры
	LastUpdated      time.Time `json:"last_updated"`
	TradesCount      int64     `json:"trades_count"`       // Количество сделок
	LearningRate     float64   `json:"learning_rate"`      // Скорость обучения (0.01-0.1)
	Confidence       float64   `json:"confidence"`         // Уверенность (0-1)
}

// SupplyDemandPoint представляет точку данных спроса/предложения
type SupplyDemandPoint struct {
	Timestamp   time.Time `json:"timestamp"`
	Supply      int64     `json:"supply"`       // Количество предметов на продажу
	Demand      int64     `json:"demand"`       // Количество запросов на покупку
	ClearingPrice float64 `json:"clearing_price"` // Цена клиринга
	Volume       int64    `json:"volume"`       // Объем торгов
}

// DoubleAuctionOrder представляет ордер в двойном аукционе
type DoubleAuctionOrder struct {
	ID          string    `json:"id"`
	ItemID      string    `json:"item_id"`
	PlayerID    string    `json:"player_id"`
	Type        string    `json:"type"`        // "bid" или "ask"
	Price       float64   `json:"price"`
	Quantity    int       `json:"quantity"`
	Timestamp   time.Time `json:"timestamp"`
	IsActive    bool      `json:"is_active"`
}

// MarketClearingResult результат клиринга рынка
type MarketClearingResult struct {
	ItemID          string                    `json:"item_id"`
	ClearingPrice   float64                   `json:"clearing_price"`
	ExecutedTrades  []*ExecutedTrade          `json:"executed_trades"`
	RemainingBids   []*DoubleAuctionOrder    `json:"remaining_bids"`
	RemainingAsks   []*DoubleAuctionOrder    `json:"remaining_asks"`
	Volume          int64                     `json:"volume"`
	MarketEfficiency float64                  `json:"market_efficiency"` // 0-1
}

// ExecutedTrade представляет исполненную сделку
type ExecutedTrade struct {
	BuyerID    string  `json:"buyer_id"`
	SellerID   string  `json:"seller_id"`
	Price      float64 `json:"price"`
	Quantity   int     `json:"quantity"`
	Timestamp  time.Time `json:"timestamp"`
}

// NewPricingEngine создает новый движок ценообразования
func NewPricingEngine() *PricingEngine {
	return &PricingEngine{
		priceBeliefs:         make(map[string]*PriceBelief),
		supplyDemandHistory:  make(map[string][]*SupplyDemandPoint),
		marketClearingPrices: make(map[string]float64),
		random:               rand.New(rand.NewSource(time.Now().UnixNano())),
	}
}

// InitializePriceBelief инициализирует веру в цену для нового предмета
func (pe *PricingEngine) InitializePriceBelief(itemID string, initialPrice float64) {
	pe.mu.Lock()
	defer pe.mu.Unlock()

	pe.priceBeliefs[itemID] = &PriceBelief{
		ItemID:         itemID,
		CurrentBelief:  initialPrice,
		BeliefVariance: initialPrice * 0.5, // Начальная дисперсия 50%
		LastUpdated:    time.Now(),
		TradesCount:    0,
		LearningRate:   0.05, // 5% learning rate
		Confidence:     0.1,  // Низкая начальная уверенность
	}
}

// UpdatePriceBelief обновляет веру в цену на основе новой сделки (BazaarBot algorithm)
func (pe *PricingEngine) UpdatePriceBelief(itemID string, tradePrice float64, tradeVolume int64) {
	pe.mu.Lock()
	defer pe.mu.Unlock()

	belief, exists := pe.priceBeliefs[itemID]
	if !exists {
		pe.InitializePriceBelief(itemID, tradePrice)
		return
	}

	// Kalman filter-like update для веры в цену
	measurementError := tradePrice * 0.1 // Предполагаем 10% ошибку измерения
	kalmanGain := belief.BeliefVariance / (belief.BeliefVariance + measurementError)

	// Обновляем веру
	oldBelief := belief.CurrentBelief
	belief.CurrentBelief = oldBelief + kalmanGain*(tradePrice-oldBelief)

	// Обновляем дисперсию веры
	belief.BeliefVariance = (1 - kalmanGain) * belief.BeliefVariance

	// Обновляем статистику
	belief.TradesCount++
	belief.LastUpdated = time.Now()

	// Обновляем уверенность на основе количества сделок
	belief.Confidence = math.Min(1.0, float64(belief.TradesCount)/100.0)

	// Адаптивная скорость обучения
	if belief.TradesCount > 10 {
		belief.LearningRate = 0.02 // Уменьшаем learning rate с опытом
	}
}

// GetMarketPrice возвращает текущую рыночную цену на основе веры
func (pe *PricingEngine) GetMarketPrice(itemID string) (float64, float64, error) {
	pe.mu.RLock()
	defer pe.mu.RUnlock()

	belief, exists := pe.priceBeliefs[itemID]
	if !exists {
		return 0, 0, fmt.Errorf("price belief not found for item %s", itemID)
	}

	// Добавляем шум на основе дисперсии веры
	priceNoise := pe.random.NormFloat64() * math.Sqrt(belief.BeliefVariance) * 0.1
	marketPrice := belief.CurrentBelief * (1 + priceNoise)

	// Убеждаемся что цена положительная
	marketPrice = math.Max(marketPrice, 0.01)

	return marketPrice, belief.Confidence, nil
}

// PredictPrice предсказывает будущую цену
func (pe *PricingEngine) PredictPrice(itemID string, hoursAhead int) (float64, float64) {
	pe.mu.RLock()
	defer pe.mu.RUnlock()

	belief, exists := pe.priceBeliefs[itemID]
	if !exists {
		return 0, 0
	}

	history := pe.supplyDemandHistory[itemID]
	if len(history) < 5 {
		// Недостаточно данных для прогноза
		return belief.CurrentBelief, belief.Confidence * 0.5
	}

	// Простой трендовый анализ
	recentPrices := make([]float64, 0, len(history))
	for _, point := range history {
		recentPrices = append(recentPrices, point.ClearingPrice)
	}

	// Линейная регрессия для предсказания тренда
	slope := pe.calculateTrendSlope(recentPrices)
	predictedPrice := belief.CurrentBelief + slope*float64(hoursAhead)

	// Уменьшаем уверенность с увеличением горизонта прогноза
	predictionConfidence := belief.Confidence * math.Exp(-0.1*float64(hoursAhead))

	return predictedPrice, predictionConfidence
}

// calculateTrendSlope рассчитывает наклон тренда
func (pe *PricingEngine) calculateTrendSlope(prices []float64) float64 {
	if len(prices) < 2 {
		return 0
	}

	n := float64(len(prices))
	sumX, sumY, sumXY, sumXX := 0.0, 0.0, 0.0, 0.0

	for i, price := range prices {
		x := float64(i)
		sumX += x
		sumY += price
		sumXY += x * price
		sumXX += x * x
	}

	// slope = (n*sumXY - sumX*sumY) / (n*sumXX - sumX^2)
	numerator := n*sumXY - sumX*sumY
	denominator := n*sumXX - sumX*sumX

	if denominator == 0 {
		return 0
	}

	return numerator / denominator
}

// ExecuteDoubleAuction выполняет двойной аукцион для предмета
func (pe *PricingEngine) ExecuteDoubleAuction(itemID string, bids, asks []*DoubleAuctionOrder) *MarketClearingResult {
	pe.mu.Lock()
	defer pe.mu.Unlock()

	result := &MarketClearingResult{
		ItemID:         itemID,
		ExecutedTrades: make([]*ExecutedTrade, 0),
		RemainingBids:  make([]*DoubleAuctionOrder, 0),
		RemainingAsks:  make([]*DoubleAuctionOrder, 0),
	}

	// Сортируем bids по убыванию цены (высшая цена первой)
	sort.Slice(bids, func(i, j int) bool {
		return bids[i].Price > bids[j].Price
	})

	// Сортируем asks по возрастанию цены (низшая цена первой)
	sort.Slice(asks, func(i, j int) bool {
		return asks[i].Price < asks[j].Price
	})

	bidIndex, askIndex := 0, 0
	totalVolume := int64(0)

	// Алгоритм двойного аукциона - matching engine
	for bidIndex < len(bids) && askIndex < len(asks) {
		bid := bids[bidIndex]
		ask := asks[askIndex]

		// Если bid price >= ask price, можем совершить сделку
		if bid.Price >= ask.Price && bid.IsActive && ask.IsActive {
			// Определяем цену исполнения (midpoint или другой механизм)
			clearingPrice := (bid.Price + ask.Price) / 2.0

			// Определяем объем сделки
			tradeQuantity := int(math.Min(float64(bid.Quantity), float64(ask.Quantity)))

			// Создаем сделку
			trade := &ExecutedTrade{
				BuyerID:   bid.PlayerID,
				SellerID:  ask.PlayerID,
				Price:     clearingPrice,
				Quantity:  tradeQuantity,
				Timestamp: time.Now(),
			}

			result.ExecutedTrades = append(result.ExecutedTrades, trade)
			totalVolume += int64(tradeQuantity)

			// Обновляем ордера
			bid.Quantity -= tradeQuantity
			ask.Quantity -= tradeQuantity

			// Если ордер полностью исполнен, переходим к следующему
			if bid.Quantity == 0 {
				bidIndex++
			}
			if ask.Quantity == 0 {
				askIndex++
			}
		} else {
			// Нет пересечения - добавляем в оставшиеся
			if bid.IsActive {
				result.RemainingBids = append(result.RemainingBids, bid)
			}
			if ask.IsActive {
				result.RemainingAsks = append(result.RemainingAsks, ask)
			}
			break // Выходим из цикла, так как дальнейшие ордера не пересекутся
		}
	}

	// Добавляем оставшиеся активные ордера
	for i := bidIndex; i < len(bids); i++ {
		if bids[i].IsActive {
			result.RemainingBids = append(result.RemainingBids, bids[i])
		}
	}
	for i := askIndex; i < len(asks); i++ {
		if asks[i].IsActive {
			result.RemainingAsks = append(result.RemainingAsks, asks[i])
		}
	}

	// Рассчитываем среднюю цену клиринга
	if len(result.ExecutedTrades) > 0 {
		totalValue := 0.0
		for _, trade := range result.ExecutedTrades {
			totalValue += trade.Price * float64(trade.Quantity)
		}
		result.ClearingPrice = totalValue / float64(totalVolume)

		// Сохраняем цену клиринга
		pe.marketClearingPrices[itemID] = result.ClearingPrice

		// Обновляем веру в цену на основе клиринга
		pe.UpdatePriceBelief(itemID, result.ClearingPrice, totalVolume)
	}

	// Рассчитываем эффективность рынка
	result.Volume = totalVolume
	if len(bids) > 0 && len(asks) > 0 {
		maxPossibleVolume := int64(0)
		for _, bid := range bids {
			maxPossibleVolume += int64(bid.Quantity)
		}
		for _, ask := range asks {
			maxPossibleVolume += int64(ask.Quantity)
		}
		maxPossibleVolume /= 2 // Среднее между bid и ask объемами

		if maxPossibleVolume > 0 {
			result.MarketEfficiency = float64(totalVolume) / float64(maxPossibleVolume)
		}
	}

	return result
}

// UpdateSupplyDemand обновляет данные о спросе и предложении
func (pe *PricingEngine) UpdateSupplyDemand(itemID string, supply, demand int64, clearingPrice float64, volume int64) {
	pe.mu.Lock()
	defer pe.mu.Unlock()

	if pe.supplyDemandHistory[itemID] == nil {
		pe.supplyDemandHistory[itemID] = make([]*SupplyDemandPoint, 0)
	}

	point := &SupplyDemandPoint{
		Timestamp:     time.Now(),
		Supply:        supply,
		Demand:        demand,
		ClearingPrice: clearingPrice,
		Volume:        volume,
	}

	// Добавляем новую точку данных
	pe.supplyDemandHistory[itemID] = append(pe.supplyDemandHistory[itemID], point)

	// Ограничиваем историю последними 1000 точками
	if len(pe.supplyDemandHistory[itemID]) > 1000 {
		pe.supplyDemandHistory[itemID] = pe.supplyDemandHistory[itemID][len(pe.supplyDemandHistory[itemID])-1000:]
	}
}

// CalculateMarketAnalytics рассчитывает рыночную аналитику
func (pe *PricingEngine) CalculateMarketAnalytics(itemID string, timeframeHours int) *models.MarketAnalytics {
	pe.mu.RLock()
	defer pe.mu.RUnlock()

	analytics := &models.MarketAnalytics{
		LastCalculated:      time.Now(),
		TimeframeHours:      timeframeHours,
		PriceTrends:         make(map[string]*models.PriceTrend),
		VolumeByHour:        make(map[int]int64),
		ItemTypeDistribution: make(map[string]float64),
	}

	history := pe.supplyDemandHistory[itemID]
	if len(history) == 0 {
		return analytics
	}

	// Фильтруем данные по временному диапазону
	cutoffTime := time.Now().Add(-time.Duration(timeframeHours) * time.Hour)
	filteredHistory := make([]*SupplyDemandPoint, 0)

	for _, point := range history {
		if point.Timestamp.After(cutoffTime) {
			filteredHistory = append(filteredHistory, point)
		}
	}

	if len(filteredHistory) == 0 {
		return analytics
	}

	// Рассчитываем тренды цен
	prices := make([]float64, len(filteredHistory))
	for i, point := range filteredHistory {
		prices[i] = point.ClearingPrice
	}

	slope := pe.calculateTrendSlope(prices)
	trendDirection := "stable"
	if slope > 0.01 {
		trendDirection = "rising"
	} else if slope < -0.01 {
		trendDirection = "falling"
	}

	// Рассчитываем уверенность тренда
	confidence := pe.calculateTrendConfidence(prices)

	trend := &models.PriceTrend{
		ItemID:                 itemID,
		TrendDirection:         trendDirection,
		Confidence:             confidence,
		PredictedChangePercent: slope * 100,
	}

	analytics.PriceTrends[itemID] = trend

	// Рассчитываем объем по часам
	for _, point := range filteredHistory {
		hour := point.Timestamp.Hour()
		analytics.VolumeByHour[hour] += point.Volume
	}

	// Рассчитываем общую статистику
	totalVolume := int64(0)
	totalValue := 0.0

	for _, point := range filteredHistory {
		totalVolume += point.Volume
		totalValue += point.ClearingPrice * float64(point.Volume)
	}

	analytics.TotalVolume24h = int(totalVolume) // Примерное значение

	// Рассчитываем здоровье рынка
	avgVolume := float64(totalVolume) / float64(len(filteredHistory))
	analytics.MarketHealth = math.Min(100.0, avgVolume*10) // Простая формула

	return analytics
}

// calculateTrendConfidence рассчитывает уверенность тренда
func (pe *PricingEngine) calculateTrendConfidence(prices []float64) float64 {
	if len(prices) < 3 {
		return 0.1
	}

	// Рассчитываем R-квадрат для линейной регрессии
	n := float64(len(prices))
	sumY := 0.0
	for _, price := range prices {
		sumY += price
	}
	meanY := sumY / n

	// Рассчитываем total sum of squares
	ssTot := 0.0
	for _, price := range prices {
		ssTot += math.Pow(price-meanY, 2)
	}

	// Рассчитываем residual sum of squares (упрощенная версия)
	ssRes := 0.0
	slope := pe.calculateTrendSlope(prices)
	intercept := meanY - slope*n/2 // Примерное значение

	for i, price := range prices {
		predicted := intercept + slope*float64(i)
		ssRes += math.Pow(price-predicted, 2)
	}

	if ssTot == 0 {
		return 0.1
	}

	rSquared := 1 - (ssRes / ssTot)
	return math.Max(0.1, math.Min(1.0, rSquared))
}