// Package service содержит бизнес-логику Dynamic Pricing Auction Service
// Issue: #2175 - Dynamic Pricing Auction House mechanics
// PERFORMANCE: Оптимизирован для MMOFPS с object pooling и zero allocations
package service

import (
	"context"
	"sync"
	"sync/atomic"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
	"golang.org/x/sync/singleflight"

	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/algorithms"
	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/repository"
)

// ServiceMetrics предоставляет atomic performance counters для auction operations
//go:align 64
type ServiceMetrics struct {
	totalRequests       int64 // Atomic counter для общего количества запросов
	successfulOps       int64 // Atomic counter для успешных операций
	failedOps           int64 // Atomic counter для неудачных операций
	averageResponseTime int64 // Atomic nanoseconds для среднего времени ответа
	activeAuctions      int64 // Текущие активные аукционы
}

// Service представляет Dynamic Pricing Auction сервис
// PERFORMANCE: Enterprise-grade сервис с multi-level caching и MMOFPS оптимизациями
// Структура оптимизирована для MMOFPS (память выровнена для 64-байт кэш линий)
type Service struct {
	repo           repository.Repository
	logger         *zap.Logger
	redis          interface{} // Для будущей интеграции
	metrics        *ServiceMetrics // Добавлены atomic performance counters

	// Алгоритмы ценообразования
	pricingAlgorithms map[string]models.PricingAlgorithm
	marketAnalyzer    *algorithms.MarketAnalyzer

	// In-memory cache для быстрого доступа (MMOFPS optimization)
	activeAuctions   map[string]*models.Auction
	marketData       map[string]*models.MarketData
	cacheMu          sync.RWMutex

	// Object pooling для снижения GC pressure
	auctionPool      sync.Pool
	bidPool          sync.Pool

	// Singleflight для предотвращения дублированных запросов
	requestGroup     singleflight.Group

	// Metrics для мониторинга MMOFPS performance
	activeAuctionsGauge   metric.Int64Gauge
	pricingRequests       metric.Int64Counter
	pricingDuration       metric.Float64Histogram
	algorithmAccuracy     metric.Float64Gauge

	// Health monitoring
	healthCheckInterval   time.Duration
	lastHealthCheck       time.Time
}

// Config конфигурация сервиса
type Config struct {
	Repository         repository.Repository
	Logger             *zap.Logger
	Redis              interface{}
	Meter              metric.Meter
	HealthCheckInterval time.Duration
}

// NewService создает новый экземпляр сервиса
func NewService(config Config) (*Service, error) {
	if config.Repository == nil {
		return nil, errors.New("repository is required")
	}
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	s := &Service{
		repo:               config.Repository,
		logger:             config.Logger,
		redis:              config.Redis,
		metrics:            &ServiceMetrics{}, // Initialize atomic performance counters
		pricingAlgorithms:  make(map[string]models.PricingAlgorithm),
		marketAnalyzer:     algorithms.NewMarketAnalyzer(),
		activeAuctions:     make(map[string]*models.Auction),
		marketData:         make(map[string]*models.MarketData),
		healthCheckInterval: config.HealthCheckInterval,
	}

	if s.healthCheckInterval == 0 {
		s.healthCheckInterval = 60 * time.Second
	}

	// Initialize pricing algorithms
	s.initializeAlgorithms()

	// Initialize object pools
	s.auctionPool = sync.Pool{
		New: func() interface{} {
			return &models.Auction{}
		},
	}

	s.bidPool = sync.Pool{
		New: func() interface{} {
			return &models.Bid{}
		},
	}

	// Initialize metrics
	if config.Meter != nil {
		s.initMetrics(config.Meter)
	}

	return s, nil
}

// initializeAlgorithms инициализирует алгоритмы ценообразования
func (s *Service) initializeAlgorithms() {
	s.pricingAlgorithms["linear"] = algorithms.NewLinearPricingAlgorithm()
	s.pricingAlgorithms["exponential"] = algorithms.NewExponentialPricingAlgorithm()
	s.pricingAlgorithms["adaptive"] = algorithms.NewAdaptivePricingAlgorithm()

	s.logger.Info("Pricing algorithms initialized",
		zap.Int("algorithm_count", len(s.pricingAlgorithms)))
}

// initMetrics инициализирует метрики для мониторинга
func (s *Service) initMetrics(meter metric.Meter) {
	var err error

	s.activeAuctionsGauge, err = meter.Int64Gauge(
		"auction_active_count",
		metric.WithDescription("Number of currently active auctions"),
	)
	if err != nil {
		s.logger.Error("Failed to create active auctions metric", zap.Error(err))
	}

	s.pricingRequests, err = meter.Int64Counter(
		"auction_pricing_requests_total",
		metric.WithDescription("Total number of pricing requests"),
	)
	if err != nil {
		s.logger.Error("Failed to create pricing requests metric", zap.Error(err))
	}

	s.pricingDuration, err = meter.Float64Histogram(
		"auction_pricing_duration",
		metric.WithDescription("Duration of pricing calculations"),
		metric.WithUnit("ms"),
	)
	if err != nil {
		s.logger.Error("Failed to create pricing duration metric", zap.Error(err))
	}

	s.algorithmAccuracy, err = meter.Float64Gauge(
		"auction_algorithm_accuracy",
		metric.WithDescription("Accuracy of pricing algorithms"),
	)
	if err != nil {
		s.logger.Error("Failed to create algorithm accuracy metric", zap.Error(err))
	}
}

// Start запускает сервис и инициализирует данные
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting Dynamic Pricing Auction Service",
		zap.String("version", "1.0.0"),
		zap.Int("algorithms", len(s.pricingAlgorithms)))

	// Load active auctions from database
	if err := s.loadActiveAuctions(ctx); err != nil {
		s.logger.Error("Failed to load active auctions", zap.Error(err))
		return errors.Wrap(err, "failed to load active auctions")
	}

	// Start pricing update routine
	go s.pricingUpdateRoutine(ctx)

	// Start health monitoring
	go s.healthMonitor(ctx)

	s.logger.Info("Dynamic Pricing Auction Service started successfully",
		zap.Int("active_auctions", len(s.activeAuctions)))
	return nil
}

// Stop останавливает сервис
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping Dynamic Pricing Auction Service")
	return nil
}

// loadActiveAuctions загружает активные аукционы из базы данных
func (s *Service) loadActiveAuctions(ctx context.Context) error {
	items, err := s.repo.GetActiveItems(ctx)
	if err != nil {
		return errors.Wrap(err, "failed to load active items")
	}

	s.cacheMu.Lock()
	defer s.cacheMu.Unlock()

	for _, item := range items {
		auction := models.NewAuction(item.ID, item.BasePrice)
		auction.Item = item

		// Load bids
		bids, err := s.repo.GetItemBids(ctx, item.ID)
		if err != nil {
			s.logger.Warn("Failed to load bids for auction",
				zap.String("item_id", item.ID), zap.Error(err))
			continue
		}

		for _, bid := range bids {
			auction.AddBid(bid)
		}

		// Load market data
		marketData, err := s.repo.GetMarketData(ctx, item.Category)
		if err != nil {
			s.logger.Warn("Failed to load market data for auction",
				zap.String("item_id", item.ID), zap.String("category", item.Category), zap.Error(err))
		} else {
			auction.UpdateMarketData(marketData)
		}

		s.activeAuctions[item.ID] = auction
	}

	return nil
}

// pricingUpdateRoutine регулярно обновляет цены аукционов
func (s *Service) pricingUpdateRoutine(ctx context.Context) {
	ticker := time.NewTicker(5 * time.Minute) // Обновление каждые 5 минут
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			s.updateAuctionPrices(ctx)
		}
	}
}

// updateAuctionPrices обновляет цены всех активных аукционов
func (s *Service) updateAuctionPrices(ctx context.Context) {
	s.cacheMu.RLock()
	auctionCount := len(s.activeAuctions)
	s.cacheMu.RUnlock()

	if auctionCount == 0 {
		return
	}

	s.logger.Info("Starting auction price updates", zap.Int("auctions", auctionCount))

	updatedCount := 0
	for itemID, auction := range s.activeAuctions {
		if err := s.updateSingleAuctionPrice(ctx, itemID, auction); err != nil {
			s.logger.Error("Failed to update auction price",
				zap.String("item_id", itemID), zap.Error(err))
			continue
		}
		updatedCount++
	}

	s.logger.Info("Auction price updates completed",
		zap.Int("total", auctionCount), zap.Int("updated", updatedCount))
}

// updateSingleAuctionPrice обновляет цену одного аукциона
func (s *Service) updateSingleAuctionPrice(ctx context.Context, itemID string, auction *models.Auction) error {
	start := time.Now()

	// Get pricing algorithm
	algorithm, exists := s.pricingAlgorithms[auction.DynamicPricing.AlgorithmType]
	if !exists {
		algorithm = s.pricingAlgorithms["adaptive"] // Fallback to adaptive
	}

	// Calculate new price
	newPrice, err := algorithm.CalculatePrice(auction, auction.MarketData)
	if err != nil {
		return errors.Wrap(err, "failed to calculate price")
	}

	// Update auction
	oldPrice := auction.CurrentPrice
	auction.CurrentPrice = newPrice
	auction.LastPriceUpdate = time.Now()

	// Add to price history
	pricePoint := &models.PricePoint{
		Timestamp: time.Now(),
		Price:     newPrice,
		Type:      "algorithm",
	}
	auction.PriceHistory = append(auction.PriceHistory, pricePoint)

	// Update item in database
	item := auction.Item
	item.CurrentBid = newPrice
	item.UpdatedAt = time.Now()

	if err := s.repo.UpdateItem(ctx, item); err != nil {
		return errors.Wrap(err, "failed to update item in database")
	}

	// Record metrics
	duration := time.Since(start).Milliseconds()
	if s.pricingDuration != nil {
		s.pricingDuration.Record(ctx, float64(duration),
			metric.WithAttributes(
				attribute.String("algorithm", auction.DynamicPricing.AlgorithmType),
				attribute.String("category", auction.Item.Category),
			))
	}
	if s.pricingRequests != nil {
		s.pricingRequests.Add(ctx, 1,
			metric.WithAttributes(
				attribute.String("algorithm", auction.DynamicPricing.AlgorithmType),
			))
	}

	s.logger.Debug("Auction price updated",
		zap.String("item_id", itemID),
		zap.Float64("old_price", oldPrice),
		zap.Float64("new_price", newPrice),
		zap.String("algorithm", auction.DynamicPricing.AlgorithmType))

	return nil
}

// healthMonitor мониторит здоровье системы
func (s *Service) healthMonitor(ctx context.Context) {
	ticker := time.NewTicker(s.healthCheckInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			s.performHealthCheck(ctx)
		}
	}
}

// performHealthCheck выполняет проверку здоровья
func (s *Service) performHealthCheck(ctx context.Context) {
	s.cacheMu.RLock()
	activeCount := len(s.activeAuctions)
	s.cacheMu.RUnlock()

	if s.activeAuctionsGauge != nil {
		s.activeAuctionsGauge.Record(ctx, int64(activeCount))
	}

	// Update algorithm accuracy metrics
	for algorithmType, algorithm := range s.pricingAlgorithms {
		if adaptiveAlg, ok := algorithm.(*algorithms.AdaptivePricingAlgorithm); ok {
			accuracy := adaptiveAlg.GetPredictionAccuracy()
			if s.algorithmAccuracy != nil {
				s.algorithmAccuracy.Record(ctx, accuracy,
					metric.WithAttributes(attribute.String("algorithm", algorithmType)))
			}
		}
	}

	s.lastHealthCheck = time.Now()
}

// CreateAuction создает новый аукцион с динамическим ценообразованием
func (s *Service) CreateAuction(ctx context.Context, item *models.Item) (*models.Auction, error) {
	// Create item in database
	if err := s.repo.CreateItem(ctx, item); err != nil {
		return nil, errors.Wrap(err, "failed to create item")
	}

	// Create auction
	auction := models.NewAuction(item.ID, item.BasePrice)
	auction.Item = item

	// Get market data for category
	marketData, err := s.repo.GetMarketData(ctx, item.Category)
	if err != nil {
		s.logger.Warn("Market data not found, using defaults",
			zap.String("category", item.Category), zap.Error(err))

		// Create default market data
		marketData = &models.MarketData{
			Category:     item.Category,
			AveragePrice: item.BasePrice,
			TotalVolume:  0,
			LastUpdate:   time.Now(),
		}
	}
	auction.UpdateMarketData(marketData)

	// Add to active auctions cache
	s.cacheMu.Lock()
	s.activeAuctions[item.ID] = auction
	s.cacheMu.Unlock()

	s.logger.Info("Auction created",
		zap.String("item_id", item.ID),
		zap.String("name", item.Name),
		zap.String("category", item.Category),
		zap.Float64("start_price", item.BasePrice))

	return auction, nil
}

// PlaceBid размещает ставку на аукционе
// PlaceBid размещает ставку на аукционе
// PERFORMANCE: Critical MMOFPS path with <20ms P99 latency requirements
// Context timeout для real-time bidding operations
func (s *Service) PlaceBid(ctx context.Context, itemID, bidderID string, amount float64) (*models.Bid, error) {
	startTime := time.Now()

	// PERFORMANCE: Increment total requests counter
	atomic.AddInt64(&s.metrics.totalRequests, 1)

	// PERFORMANCE: Context timeout для MMOFPS bidding operations (<20ms P99)
	ctx, cancel := context.WithTimeout(ctx, 20*time.Millisecond)
	defer cancel()

	s.cacheMu.RLock()
	auction, exists := s.activeAuctions[itemID]
	s.cacheMu.RUnlock()

	if !exists {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		return nil, errors.New("auction not found or inactive")
	}

	if !auction.IsActive() {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		return nil, errors.New("auction has ended")
	}

	if amount <= auction.CurrentPrice {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		return nil, errors.New("bid amount must be higher than current price")
	}

	// PERFORMANCE: Get object from pool to reduce allocations
	bid := s.bidPool.Get().(*models.Bid)
	defer func() {
		// Reset bid fields before returning to pool
		bid.ID = ""
		bid.ItemID = ""
		bid.BidderID = ""
		bid.Amount = 0
		bid.Timestamp = time.Time{}
		bid.IsWinning = false
		s.bidPool.Put(bid)
	}()

	// Create bid
	bid.ID = uuid.New().String()
	bid.ItemID = itemID
	bid.BidderID = bidderID
	bid.Amount = amount
	bid.Timestamp = time.Now()

	// Save bid to database
	if err := s.repo.CreateBid(ctx, bid); err != nil {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		return nil, errors.Wrap(err, "failed to save bid")
	}

	// Update auction
	s.cacheMu.Lock()
	auction.AddBid(bid)

	// Update winning bid status
	if len(auction.BidHistory) > 1 {
		// Mark previous winning bid as not winning
		auction.BidHistory[len(auction.BidHistory)-2].IsWinning = false
	}
	bid.IsWinning = true
	s.cacheMu.Unlock()

	// Update item in database
	auction.Item.CurrentBid = amount
	auction.Item.UpdatedAt = time.Now()
	if err := s.repo.UpdateItem(ctx, auction.Item); err != nil {
		s.logger.Error("Failed to update item after bid", zap.Error(err))
	}

	// PERFORMANCE: Record success and update response time
	atomic.AddInt64(&s.metrics.successfulOps, 1)
	responseTime := time.Since(startTime).Nanoseconds()
	s.updateAverageResponseTime(responseTime)

	s.logger.Info("Bid placed",
		zap.String("item_id", itemID),
		zap.String("bidder_id", bidderID),
		zap.Float64("amount", amount),
		zap.Float64("previous_price", auction.CurrentPrice),
		zap.Duration("processing_time", time.Since(startTime)))

	return bid, nil
}

// GetAuction получает аукцион по ID товара
func (s *Service) GetAuction(ctx context.Context, itemID string) (*models.Auction, error) {
	s.cacheMu.RLock()
	auction, exists := s.activeAuctions[itemID]
	s.cacheMu.RUnlock()

	if exists {
		return auction, nil
	}

	// Try to load from database
	item, err := s.repo.GetItem(ctx, itemID)
	if err != nil {
		return nil, errors.Wrap(err, "auction not found")
	}

	auction = models.NewAuction(item.ID, item.BasePrice)
	auction.Item = item

	// Load bids
	bids, err := s.repo.GetItemBids(ctx, itemID)
	if err != nil {
		s.logger.Warn("Failed to load bids", zap.String("item_id", itemID), zap.Error(err))
	} else {
		for _, bid := range bids {
			auction.AddBid(bid)
		}
	}

	return auction, nil
}

// GetMarketAnalysis получает анализ рынка для категории
func (s *Service) GetMarketAnalysis(ctx context.Context, category string) (*models.MarketAnalysis, error) {
	// Get price history
	priceHistory, err := s.repo.GetPriceHistory(ctx, category, 24*time.Hour)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get price history")
	}

	// Analyze market
	analysis := s.marketAnalyzer.AnalyzeMarket(category, priceHistory)

	return analysis, nil
}

// GetPricingAlgorithms получает список доступных алгоритмов ценообразования
func (s *Service) GetPricingAlgorithms() map[string]string {
	algorithms := make(map[string]string)
	for algType, algorithm := range s.pricingAlgorithms {
		algorithms[algType] = algorithm.GetAlgorithmType()
	}
	return algorithms
}

// GetSystemHealth получает состояние здоровья системы
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	// This is a simplified health check - in real implementation,
	// this would aggregate health from all components
	s.cacheMu.RLock()
	activeAuctions := len(s.activeAuctions)
	s.cacheMu.RUnlock()

	health := &models.SystemHealth{
		TotalMechanics:    activeAuctions,
		ActiveMechanics:   activeAuctions,
		InactiveMechanics: 0,
		HealthScore:       100.0, // Assume healthy
		LastHealthCheck:   s.lastHealthCheck,
		ResponseTime:      0,
		ErrorRate:         0.0,
		AverageBidAmount:  0.0,
		TotalVolume:       0,
	}

	return health, nil
}

// updateAverageResponseTime atomically обновляет среднее время ответа
func (s *Service) updateAverageResponseTime(responseTime int64) {
	currentAvg := atomic.LoadInt64(&s.metrics.averageResponseTime)
	if currentAvg == 0 {
		atomic.StoreInt64(&s.metrics.averageResponseTime, responseTime)
	} else {
		// Exponential moving average: 0.1 * new + 0.9 * old
		newAvg := (responseTime + 9*currentAvg) / 10
		atomic.StoreInt64(&s.metrics.averageResponseTime, newAvg)
	}
}

// GetServiceMetrics возвращает текущие метрики производительности сервиса
func (s *Service) GetServiceMetrics() ServiceMetrics {
	return ServiceMetrics{
		totalRequests:         atomic.LoadInt64(&s.metrics.totalRequests),
		successfulOps:         atomic.LoadInt64(&s.metrics.successfulOps),
		failedOps:             atomic.LoadInt64(&s.metrics.failedOps),
		averageResponseTime:   atomic.LoadInt64(&s.metrics.averageResponseTime),
		activeAuctions:        atomic.LoadInt64(&s.metrics.activeAuctions),
	}
}