// Package algorithms содержит алгоритмы динамического ценообразования
// Issue: #2175 - Dynamic Pricing Auction House mechanics
// PERFORMANCE: Оптимизированные алгоритмы для MMOFPS с минимальными аллокациями
package algorithms

import (
	"math"
	"time"

	"github.com/go-faster/errors"
	"gonum.org/v1/gonum/stat"

	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/models"
)

// LinearPricingAlgorithm реализует линейное ценообразование
type LinearPricingAlgorithm struct {
	BaseRate     float64       // Базовая скорость изменения цены (% в час)
	TimeWeight   float64       // Вес времени
	DemandWeight float64       // Вес спроса
	RarityWeight float64       // Вес редкости
}

// NewLinearPricingAlgorithm создает новый линейный алгоритм
func NewLinearPricingAlgorithm() *LinearPricingAlgorithm {
	return &LinearPricingAlgorithm{
		BaseRate:     0.02, // 2% в час
		TimeWeight:   0.6,
		DemandWeight: 0.3,
		RarityWeight: 0.1,
	}
}

// CalculatePrice рассчитывает цену с использованием линейного алгоритма
func (l *LinearPricingAlgorithm) CalculatePrice(auction *models.Auction, marketData *models.MarketData) (float64, error) {
	if auction == nil || auction.Item.EndTime.IsZero() {
		return auction.CurrentPrice, errors.New("invalid auction data")
	}

	timeRemaining := auction.GetTimeRemaining()
	totalDuration := auction.Item.EndTime.Sub(auction.Item.CreatedAt)
	timeProgress := 1.0 - (timeRemaining.Seconds() / totalDuration.Seconds())

	// Ограничиваем прогресс времени
	if timeProgress < 0 {
		timeProgress = 0
	}
	if timeProgress > 1 {
		timeProgress = 1
	}

	// Расчет множителя времени (цена растет к концу аукциона)
	timeMultiplier := 1.0 + (l.BaseRate * l.TimeWeight * timeProgress)

	// Расчет множителя спроса (больше ставок = выше цена)
	demandMultiplier := 1.0
	if len(auction.BidHistory) > 0 {
		demandMultiplier = 1.0 + (l.BaseRate * l.DemandWeight * float64(len(auction.BidHistory)))
	}

	// Расчет множителя редкости
	rarityMultiplier := l.getRarityMultiplier(auction.Item.Rarity)

	// Общий множитель
	totalMultiplier := timeMultiplier * demandMultiplier * rarityMultiplier

	newPrice := auction.StartPrice * totalMultiplier

	// Применяем ограничения на изменение цены
	maxChange := auction.CurrentPrice * 0.15 // Максимум 15% изменение за раз
	if newPrice > auction.CurrentPrice+maxChange {
		newPrice = auction.CurrentPrice + maxChange
	}
	if newPrice < auction.CurrentPrice*0.95 { // Минимум -5%
		newPrice = auction.CurrentPrice * 0.95
	}

	return newPrice, nil
}

// UpdateParameters обновляет параметры алгоритма на основе результатов
func (l *LinearPricingAlgorithm) UpdateParameters(auction *models.Auction, actualPrice float64) error {
	// Для линейного алгоритма параметры фиксированы
	return nil
}

// GetAlgorithmType возвращает тип алгоритма
func (l *LinearPricingAlgorithm) GetAlgorithmType() string {
	return "linear"
}

// getRarityMultiplier возвращает множитель редкости
func (l *LinearPricingAlgorithm) getRarityMultiplier(rarity string) float64 {
	switch rarity {
	case "common":
		return 1.0
	case "uncommon":
		return 1.1
	case "rare":
		return 1.25
	case "epic":
		return 1.5
	case "legendary":
		return 2.0
	default:
		return 1.0
	}
}

// ExponentialPricingAlgorithm реализует экспоненциальное ценообразование
type ExponentialPricingAlgorithm struct {
	GrowthRate   float64       // Скорость роста
	TimeDecay    float64       // Затухание со временем
	BidBoost     float64       // Увеличение за ставку
}

// NewExponentialPricingAlgorithm создает новый экспоненциальный алгоритм
func NewExponentialPricingAlgorithm() *ExponentialPricingAlgorithm {
	return &ExponentialPricingAlgorithm{
		GrowthRate: 0.001, // Очень медленный рост
		TimeDecay:  0.9,   // Затухание 10% в час
		BidBoost:   0.05,  // 5% за ставку
	}
}

// CalculatePrice рассчитывает цену с использованием экспоненциального алгоритма
func (e *ExponentialPricingAlgorithm) CalculatePrice(auction *models.Auction, marketData *models.MarketData) (float64, error) {
	timeRemaining := auction.GetTimeRemaining()
	hoursRemaining := timeRemaining.Hours()

	// Экспоненциальный рост к концу аукциона
	timeFactor := math.Exp(e.GrowthRate * (24.0 - hoursRemaining)) // Предполагаем 24-часовой аукцион

	// Затухание со временем
	decayFactor := math.Pow(e.TimeDecay, 24.0-hoursRemaining)

	// Фактор ставок
	bidFactor := 1.0 + (float64(len(auction.BidHistory)) * e.BidBoost)

	newPrice := auction.StartPrice * timeFactor * decayFactor * bidFactor

	// Ограничиваем рост
	maxPrice := auction.StartPrice * 5.0 // Максимум 5x от стартовой цены
	if newPrice > maxPrice {
		newPrice = maxPrice
	}

	return newPrice, nil
}

// UpdateParameters обновляет параметры алгоритма
func (e *ExponentialPricingAlgorithm) UpdateParameters(auction *models.Auction, actualPrice float64) error {
	return nil
}

// GetAlgorithmType возвращает тип алгоритма
func (e *ExponentialPricingAlgorithm) GetAlgorithmType() string {
	return "exponential"
}

// AdaptivePricingAlgorithm реализует адаптивное ценообразование с машинным обучением
type AdaptivePricingAlgorithm struct {
	LearningRate      float64
	MemorySize        int
	FeatureWeights    map[string]float64
	PriceHistory      []float64
	TimeWeights       []float64
	AccuracyHistory   []float64
}

// NewAdaptivePricingAlgorithm создает новый адаптивный алгоритм
func NewAdaptivePricingAlgorithm() *AdaptivePricingAlgorithm {
	return &AdaptivePricingAlgorithm{
		LearningRate:   0.01,
		MemorySize:     100,
		FeatureWeights: make(map[string]float64),
		PriceHistory:   make([]float64, 0),
		TimeWeights:    make([]float64, 0),
		AccuracyHistory: make([]float64, 0),
	}
}

// CalculatePrice рассчитывает цену с использованием адаптивного алгоритма
func (a *AdaptivePricingAlgorithm) CalculatePrice(auction *models.Auction, marketData *models.MarketData) (float64, error) {
	features := a.extractFeatures(auction, marketData)

	// Линейная комбинация признаков
	prediction := 0.0
	for feature, value := range features {
		weight, exists := a.FeatureWeights[feature]
		if !exists {
			weight = 0.1 // Начальный вес
			a.FeatureWeights[feature] = weight
		}
		prediction += weight * value
	}

	// Преобразуем в цену
	newPrice := auction.StartPrice * (1.0 + prediction)

	// Ограничиваем изменения
	minPrice := auction.CurrentPrice * 0.9
	maxPrice := auction.CurrentPrice * 1.1

	if newPrice < minPrice {
		newPrice = minPrice
	}
	if newPrice > maxPrice {
		newPrice = maxPrice
	}

	return newPrice, nil
}

// UpdateParameters обновляет параметры алгоритма на основе обратной связи
func (a *AdaptivePricingAlgorithm) UpdateParameters(auction *models.Auction, actualPrice float64) error {
	if len(a.PriceHistory) >= a.MemorySize {
		// Удаляем старые данные
		a.PriceHistory = a.PriceHistory[1:]
		a.AccuracyHistory = a.AccuracyHistory[1:]
	}

	// Вычисляем ошибку предсказания
	predictedPrice := auction.CurrentPrice // Последнее предсказание
	actualChange := (actualPrice - auction.StartPrice) / auction.StartPrice
	predictedChange := (predictedPrice - auction.StartPrice) / auction.StartPrice

	error := actualChange - predictedChange

	// Обновляем веса признаков
	features := a.extractFeatures(auction, auction.MarketData)
	for feature, value := range features {
		weight := a.FeatureWeights[feature]
		weight += a.LearningRate * error * value
		a.FeatureWeights[feature] = weight
	}

	// Сохраняем в историю
	a.PriceHistory = append(a.PriceHistory, actualPrice)
	a.AccuracyHistory = append(a.AccuracyHistory, math.Abs(error))

	return nil
}

// GetAlgorithmType возвращает тип алгоритма
func (a *AdaptivePricingAlgorithm) GetAlgorithmType() string {
	return "adaptive"
}

// extractFeatures извлекает признаки для машинного обучения
func (a *AdaptivePricingAlgorithm) extractFeatures(auction *models.Auction, marketData *models.MarketData) map[string]float64 {
	features := make(map[string]float64)

	// Временные признаки
	timeRemaining := auction.GetTimeRemaining()
	features["time_remaining_hours"] = timeRemaining.Hours()
	features["time_progress"] = 1.0 - (timeRemaining.Seconds() / (24 * 3600)) // Предполагаем 24 часа

	// Признаки спроса
	features["bid_count"] = float64(len(auction.BidHistory))
	features["bid_density"] = float64(len(auction.BidHistory)) / math.Max(1, 24-timeRemaining.Hours())

	// Рыночные признаки
	if marketData != nil {
		features["market_average"] = marketData.AveragePrice
		features["market_volatility"] = marketData.PriceStdDev
		features["supply_velocity"] = marketData.SupplyVelocity
		features["demand_velocity"] = marketData.DemandVelocity
		features["market_saturation"] = marketData.MarketSaturation
	}

	// Признаки товара
	rarityValue := a.getRarityValue(auction.Item.Rarity)
	features["rarity"] = rarityValue
	features["start_price_ratio"] = auction.CurrentPrice / auction.StartPrice

	return features
}

// getRarityValue возвращает числовое значение редкости
func (a *AdaptivePricingAlgorithm) getRarityValue(rarity string) float64 {
	switch rarity {
	case "common":
		return 1.0
	case "uncommon":
		return 2.0
	case "rare":
		return 3.0
	case "epic":
		return 4.0
	case "legendary":
		return 5.0
	default:
		return 1.0
	}
}

// GetPredictionAccuracy возвращает точность предсказаний
func (a *AdaptivePricingAlgorithm) GetPredictionAccuracy() float64 {
	if len(a.AccuracyHistory) == 0 {
		return 0.0
	}

	// Средняя абсолютная ошибка
	sum := 0.0
	for _, accuracy := range a.AccuracyHistory {
		sum += accuracy
	}

	return 1.0 - (sum / float64(len(a.AccuracyHistory))) // Преобразуем в точность
}

// MarketAnalyzer анализирует рыночные тенденции
type MarketAnalyzer struct {
	WindowSize time.Duration
}

// NewMarketAnalyzer создает новый анализатор рынка
func NewMarketAnalyzer() *MarketAnalyzer {
	return &MarketAnalyzer{
		WindowSize: 24 * time.Hour, // 24 часа
	}
}

// AnalyzeMarket анализирует рынок для категории товаров
func (ma *MarketAnalyzer) AnalyzeMarket(category string, priceHistory []*models.PricePoint) *models.MarketAnalysis {
	if len(priceHistory) < 2 {
		return &models.MarketAnalysis{
			Category:          category,
			TrendDirection:    "stable",
			TrendStrength:     0.0,
			AnalysisTimestamp: time.Now(),
		}
	}

	// Извлекаем цены и времена
	prices := make([]float64, len(priceHistory))
	times := make([]float64, len(priceHistory))

	for i, point := range priceHistory {
		prices[i] = point.Price
		times[i] = float64(point.Timestamp.Unix())
	}

	// Расчет тренда
	slope, _ := stat.LinearRegression(times, prices, nil, false)

	trendDirection := "stable"
	trendStrength := math.Abs(slope) / stat.Mean(prices, nil) // Относительная сила тренда

	if slope > 0.001 {
		trendDirection = "up"
	} else if slope < -0.001 {
		trendDirection = "down"
	}

	// Расчет волатильности
	volatility := stat.StdDev(prices, nil) / stat.Mean(prices, nil)

	return &models.MarketAnalysis{
		Category:          category,
		TimeFrame:         ma.WindowSize,
		TrendDirection:    trendDirection,
		TrendStrength:     trendStrength,
		VolatilityIndex:   volatility,
		ConfidenceLevel:   ma.calculateConfidence(prices),
		AnalysisTimestamp: time.Now(),
	}
}

// calculateConfidence рассчитывает уровень уверенности анализа
func (ma *MarketAnalyzer) calculateConfidence(prices []float64) float64 {
	if len(prices) < 10 {
		return 0.5 // Низкая уверенность при малом объеме данных
	}

	// Простая метрика уверенности на основе размера выборки и стабильности
	sampleSize := float64(len(prices))
	volatility := stat.StdDev(prices, nil) / stat.Mean(prices, nil)

	confidence := math.Min(sampleSize/100.0, 1.0) * (1.0 - math.Min(volatility, 1.0))

	return math.Max(0.1, math.Min(confidence, 0.95)) // Ограничиваем диапазон
}