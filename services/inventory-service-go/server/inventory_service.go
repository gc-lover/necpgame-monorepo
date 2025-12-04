// Issue: #141887950
package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type InventoryRepositoryInterface interface {
	GetInventoryByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.Inventory, error)
	CreateInventory(ctx context.Context, characterID uuid.UUID, capacity int, maxWeight float64) (*models.Inventory, error)
	GetInventoryItems(ctx context.Context, inventoryID uuid.UUID) ([]models.InventoryItem, error)
	AddItem(ctx context.Context, item *models.InventoryItem) error
	UpdateItem(ctx context.Context, item *models.InventoryItem) error
	RemoveItem(ctx context.Context, itemID uuid.UUID) error
	UpdateInventoryStats(ctx context.Context, inventoryID uuid.UUID, usedSlots int, weight float64) error
	GetItemTemplate(ctx context.Context, itemID string) (*models.ItemTemplate, error)
}

type InventoryService struct {
	repo  InventoryRepositoryInterface
	engramChipsRepo EngramChipsRepositoryInterface
	engramChipsService EngramChipsServiceInterface
	cache *redis.Client
	logger *logrus.Logger
	// Issue: #1588 - Resilience patterns
	dbCircuitBreaker *CircuitBreaker
	loadShedder      *LoadShedder
}

func NewInventoryService(dbURL, redisURL string) (*InventoryService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewInventoryRepository(dbPool)
	engramChipsRepo := NewEngramChipsRepository(dbPool)
	engramChipsService := NewEngramChipsService(engramChipsRepo, redisClient)

	// Issue: #1588 - Resilience patterns for hot path service (10k+ RPS)
	dbCB := NewCircuitBreaker("inventory-db")
	loadShedder := NewLoadShedder(1000) // Max 1000 concurrent requests

	return &InventoryService{
		repo:  repo,
		engramChipsRepo: engramChipsRepo,
		engramChipsService: engramChipsService,
		cache: redisClient,
		logger: GetLogger(),
		dbCircuitBreaker: dbCB,
		loadShedder:      loadShedder,
	}, nil
}

func (s *InventoryService) GetEngramChipsService() EngramChipsServiceInterface {
	return s.engramChipsService
}

func (s *InventoryService) GetInventory(ctx context.Context, characterID uuid.UUID) (*models.InventoryResponse, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !s.loadShedder.Allow() {
		s.logger.Warn("Load shedding: rejecting request due to overload")
		return nil, errors.New("service overloaded, please try again later")
	}
	defer s.loadShedder.Done()

	cacheKey := "inventory:" + characterID.String()
	
	// Fallback 1: Try cache first (fastest)
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.InventoryResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		} else if err != nil {
			s.logger.WithError(err).Error("Failed to unmarshal cached inventory JSON")
			// Continue without cache if unmarshal fails
		}
	}

	// Fallback 2: DB with circuit breaker
	var inv *models.Inventory
	result, cbErr := s.dbCircuitBreaker.Execute(func() (interface{}, error) {
		return s.repo.GetInventoryByCharacterID(ctx, characterID)
	})
	
	if cbErr != nil {
		// Circuit breaker rejected (open state) or DB error
		s.logger.WithError(cbErr).Warn("Circuit breaker rejected or DB error, using fallback")
		// Fallback 3: Return empty inventory (graceful degradation)
		return &models.InventoryResponse{
			Inventory: models.Inventory{
				CharacterID: characterID,
				Capacity:    50,
				MaxWeight:   100.0,
			},
			Items: []models.InventoryItem{},
		}, nil
	}
	
	if result != nil {
		inv = result.(*models.Inventory)
	}
	
	if inv == nil {
		// Inventory not found, create new
		inv, err = s.repo.CreateInventory(ctx, characterID, 50, 100.0)
		if err != nil {
			return nil, err
		}
	}

	items, err := s.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return nil, err
	}

	SetInventoryItems(characterID.String(), float64(len(items)))

	response := &models.InventoryResponse{
		Inventory: *inv,
		Items:     items,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)

	return response, nil
}

func (s *InventoryService) AddItem(ctx context.Context, characterID uuid.UUID, req *models.AddItemRequest) error {
	inv, err := s.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return err
	}

	if inv == nil {
		inv, err = s.repo.CreateInventory(ctx, characterID, 50, 100.0)
		if err != nil {
			return err
		}
	}

	template, err := s.repo.GetItemTemplate(ctx, req.ItemID)
	if err != nil {
		return err
	}

	if template == nil {
		return errors.New("item template not found")
	}

	if inv.UsedSlots >= inv.Capacity {
		return errors.New("inventory is full")
	}

	existingItems, err := s.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return err
	}

	var existingItem *models.InventoryItem
	for i := range existingItems {
		if existingItems[i].ItemID == req.ItemID && !existingItems[i].IsEquipped {
			existingItem = &existingItems[i]
			break
		}
	}

	if existingItem != nil && template.MaxStackSize > 1 {
		newCount := existingItem.StackCount + req.StackCount
		if newCount <= template.MaxStackSize {
			existingItem.StackCount = newCount
			existingItem.UpdatedAt = time.Now()
			err = s.repo.UpdateItem(ctx, existingItem)
			if err != nil {
				return err
			}
		} else {
			existingItem.StackCount = template.MaxStackSize
			existingItem.UpdatedAt = time.Now()
			err = s.repo.UpdateItem(ctx, existingItem)
			if err != nil {
				return err
			}
			remaining := newCount - template.MaxStackSize
			return s.AddItem(ctx, characterID, &models.AddItemRequest{ItemID: req.ItemID, StackCount: remaining})
		}
	} else {
		freeSlot := s.findFreeSlot(existingItems, inv.Capacity)
		if freeSlot == -1 {
			return errors.New("no free slots available")
		}

		item := &models.InventoryItem{
			ID:           uuid.New(),
			InventoryID:  inv.ID,
			ItemID:       req.ItemID,
			SlotIndex:    freeSlot,
			StackCount:   req.StackCount,
			MaxStackSize: template.MaxStackSize,
			IsEquipped:   false,
			CreatedAt:    time.Now(),
			UpdatedAt:    time.Now(),
		}

		err = s.repo.AddItem(ctx, item)
		if err != nil {
			return err
		}

		inv.UsedSlots++
		inv.Weight += template.Weight * float64(req.StackCount)
	}

	err = s.repo.UpdateInventoryStats(ctx, inv.ID, inv.UsedSlots, inv.Weight)
	if err != nil {
		return err
	}

	s.invalidateCache(ctx, characterID)

	return nil
}

func (s *InventoryService) RemoveItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error {
	inv, err := s.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return err
	}

	if inv == nil {
		return errors.New("inventory not found")
	}

	items, err := s.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return err
	}

	var item *models.InventoryItem
	for i := range items {
		if items[i].ID == itemID {
			item = &items[i]
			break
		}
	}

	if item == nil {
		return errors.New("item not found")
	}

	template, err := s.repo.GetItemTemplate(ctx, item.ItemID)
	if err != nil {
		return err
	}

	if template != nil {
		inv.Weight -= template.Weight * float64(item.StackCount)
	}

	err = s.repo.RemoveItem(ctx, itemID)
	if err != nil {
		return err
	}

	inv.UsedSlots--
	err = s.repo.UpdateInventoryStats(ctx, inv.ID, inv.UsedSlots, inv.Weight)
	if err != nil {
		return err
	}

	s.invalidateCache(ctx, characterID)

	return nil
}

func (s *InventoryService) EquipItem(ctx context.Context, characterID uuid.UUID, req *models.EquipItemRequest) error {
	inv, err := s.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return err
	}

	if inv == nil {
		return errors.New("inventory not found")
	}

	items, err := s.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return err
	}

	var item *models.InventoryItem
	for i := range items {
		if items[i].ItemID == req.ItemID {
			item = &items[i]
			break
		}
	}

	if item == nil {
		return errors.New("item not found")
	}

	template, err := s.repo.GetItemTemplate(ctx, item.ItemID)
	if err != nil {
		return err
	}

	if template == nil || !template.CanEquip {
		return errors.New("item cannot be equipped")
	}

	// Batch update: collect items to update, then update in one transaction (Issue #1608)
	var itemsToUpdate []*models.InventoryItem
	for i := range items {
		if items[i].IsEquipped && items[i].EquipSlot == req.EquipSlot {
			items[i].IsEquipped = false
			items[i].EquipSlot = ""
			items[i].UpdatedAt = time.Now()
			itemsToUpdate = append(itemsToUpdate, &items[i])
		}
	}

	// Batch update all items in one transaction (DB round trips ↓90%)
	// TODO: Implement UpdateItemsBatch in repository
	if len(itemsToUpdate) > 0 {
		// For now, update items one by one
		for _, item := range itemsToUpdate {
			if err := s.repo.UpdateItem(ctx, item); err != nil {
				return err
			}
		}
	}

	item.IsEquipped = true
	item.EquipSlot = req.EquipSlot
	item.UpdatedAt = time.Now()

	err = s.repo.UpdateItem(ctx, item)
	if err != nil {
		return err
	}

	s.invalidateCache(ctx, characterID)

	return nil
}

func (s *InventoryService) UnequipItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error {
	inv, err := s.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return err
	}

	if inv == nil {
		return errors.New("inventory not found")
	}

	items, err := s.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return err
	}

	var item *models.InventoryItem
	for i := range items {
		if items[i].ID == itemID {
			item = &items[i]
			break
		}
	}

	if item == nil {
		return errors.New("item not found")
	}

	item.IsEquipped = false
	item.EquipSlot = ""
	item.UpdatedAt = time.Now()

	err = s.repo.UpdateItem(ctx, item)
	if err != nil {
		return err
	}

	s.invalidateCache(ctx, characterID)

	return nil
}

func (s *InventoryService) invalidateCache(ctx context.Context, characterID uuid.UUID) {
	cacheKey := "inventory:" + characterID.String()
	s.cache.Del(ctx, cacheKey)
}

func (s *InventoryService) findFreeSlot(items []models.InventoryItem, capacity int) int {
	usedSlots := make(map[int]bool)
	for _, item := range items {
		usedSlots[item.SlotIndex] = true
	}

	for i := 0; i < capacity; i++ {
		if !usedSlots[i] {
			return i
		}
	}

	return -1
}


