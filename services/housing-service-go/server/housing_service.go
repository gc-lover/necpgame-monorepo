package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type HousingService struct {
	repo   *HousingRepository
	redis  *redis.Client
	logger *logrus.Logger
}

func NewHousingService(repo *HousingRepository, redis *redis.Client, logger *logrus.Logger) *HousingService {
	return &HousingService{
		repo:   repo,
		redis:  redis,
		logger: logger,
	}
}

func (s *HousingService) getApartmentPrice(apartmentType models.ApartmentType) int64 {
	switch apartmentType {
	case models.ApartmentTypeStudio:
		return 50000
	case models.ApartmentTypeStandard:
		return 200000
	case models.ApartmentTypePenthouse:
		return 1000000
	case models.ApartmentTypeGuildHall:
		return 5000000
	default:
		return 50000
	}
}

func (s *HousingService) getFurnitureSlots(apartmentType models.ApartmentType) int {
	switch apartmentType {
	case models.ApartmentTypeStudio:
		return 20
	case models.ApartmentTypeStandard:
		return 40
	case models.ApartmentTypePenthouse:
		return 80
	case models.ApartmentTypeGuildHall:
		return 200
	default:
		return 20
	}
}

func (s *HousingService) PurchaseApartment(ctx context.Context, req *models.PurchaseApartmentRequest) (*models.Apartment, error) {
	price := s.getApartmentPrice(req.ApartmentType)
	furnitureSlots := s.getFurnitureSlots(req.ApartmentType)

	apartment := &models.Apartment{
		ID:            uuid.New(),
		OwnerID:       req.CharacterID,
		OwnerType:     "character",
		ApartmentType: req.ApartmentType,
		Location:      req.Location,
		Price:         price,
		FurnitureSlots: furnitureSlots,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	if err := s.repo.CreateApartment(ctx, apartment); err != nil {
		return nil, fmt.Errorf("failed to create apartment: %w", err)
	}

	s.publishEvent(ctx, "housing:apartment:purchased", map[string]interface{}{
		"apartment_id":   apartment.ID,
		"owner_id":       apartment.OwnerID,
		"apartment_type": apartment.ApartmentType,
		"price":          apartment.Price,
	})

	RecordApartmentCreated(string(apartment.ApartmentType))

	return apartment, nil
}

func (s *HousingService) GetApartment(ctx context.Context, apartmentID uuid.UUID) (*models.Apartment, error) {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to get apartment: %w", err)
	}

	return apartment, nil
}

func (s *HousingService) ListApartments(ctx context.Context, ownerID *uuid.UUID, ownerType *string, isPublic *bool, limit, offset int) ([]models.Apartment, int, error) {
	apartments, total, err := s.repo.ListApartments(ctx, ownerID, ownerType, isPublic, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list apartments: %w", err)
	}

	return apartments, total, nil
}

func (s *HousingService) UpdateApartmentSettings(ctx context.Context, apartmentID uuid.UUID, req *models.UpdateApartmentSettingsRequest) error {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil {
		return fmt.Errorf("failed to get apartment: %w", err)
	}

	if apartment == nil {
		return fmt.Errorf("apartment not found")
	}

	if apartment.OwnerID != req.CharacterID {
		return fmt.Errorf("unauthorized: not the owner")
	}

	if req.IsPublic != nil {
		apartment.IsPublic = *req.IsPublic
	}

	if req.Guests != nil {
		apartment.Guests = req.Guests
	}

	if req.Settings != nil {
		apartment.Settings = req.Settings
	}

	apartment.UpdatedAt = time.Now()

	if err := s.repo.UpdateApartment(ctx, apartment); err != nil {
		return fmt.Errorf("failed to update apartment: %w", err)
	}

	s.publishEvent(ctx, "housing:apartment:updated", map[string]interface{}{
		"apartment_id": apartment.ID,
		"owner_id":     apartment.OwnerID,
	})

	return nil
}

func (s *HousingService) PlaceFurniture(ctx context.Context, apartmentID uuid.UUID, req *models.PlaceFurnitureRequest) (*models.PlacedFurniture, error) {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to get apartment: %w", err)
	}

	if apartment == nil {
		return nil, fmt.Errorf("apartment not found")
	}

	if apartment.OwnerID != req.CharacterID {
		return nil, fmt.Errorf("unauthorized: not the owner")
	}

	count, err := s.repo.CountPlacedFurniture(ctx, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to count furniture: %w", err)
	}

	if count >= apartment.FurnitureSlots {
		return nil, fmt.Errorf("furniture slots limit reached")
	}

	furniture := &models.PlacedFurniture{
		ID:            uuid.New(),
		ApartmentID:   apartmentID,
		FurnitureItemID: req.FurnitureItemID,
		Position:      req.Position,
		Rotation:      req.Rotation,
		Scale:         req.Scale,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	if err := s.repo.CreatePlacedFurniture(ctx, furniture); err != nil {
		return nil, fmt.Errorf("failed to place furniture: %w", err)
	}

	s.recalculatePrestigeScore(ctx, apartmentID)

	s.publishEvent(ctx, "housing:furniture:placed", map[string]interface{}{
		"apartment_id":     apartmentID,
		"furniture_id":     furniture.ID,
		"furniture_item_id": furniture.FurnitureItemID,
	})

	RecordFurniturePlaced()

	return furniture, nil
}

func (s *HousingService) RemoveFurniture(ctx context.Context, apartmentID, furnitureID uuid.UUID, characterID uuid.UUID) error {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil {
		return fmt.Errorf("failed to get apartment: %w", err)
	}

	if apartment == nil {
		return fmt.Errorf("apartment not found")
	}

	if apartment.OwnerID != characterID {
		return fmt.Errorf("unauthorized: not the owner")
	}

	furniture, err := s.repo.GetPlacedFurnitureByID(ctx, furnitureID)
	if err != nil {
		return fmt.Errorf("failed to get furniture: %w", err)
	}

	if furniture == nil || furniture.ApartmentID != apartmentID {
		return fmt.Errorf("furniture not found")
	}

	if err := s.repo.DeletePlacedFurniture(ctx, furnitureID); err != nil {
		return fmt.Errorf("failed to remove furniture: %w", err)
	}

	s.recalculatePrestigeScore(ctx, apartmentID)

	s.publishEvent(ctx, "housing:furniture:removed", map[string]interface{}{
		"apartment_id": apartmentID,
		"furniture_id": furnitureID,
	})

	return nil
}

func (s *HousingService) GetFurnitureItem(ctx context.Context, itemID string) (*models.FurnitureItem, error) {
	item, err := s.repo.GetFurnitureItemByID(ctx, itemID)
	if err != nil {
		return nil, fmt.Errorf("failed to get furniture item: %w", err)
	}

	return item, nil
}

func (s *HousingService) ListFurnitureItems(ctx context.Context, category *models.FurnitureCategory, limit, offset int) ([]models.FurnitureItem, int, error) {
	items, total, err := s.repo.ListFurnitureItems(ctx, category, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list furniture items: %w", err)
	}

	return items, total, nil
}

func (s *HousingService) GetApartmentDetail(ctx context.Context, apartmentID uuid.UUID) (*models.ApartmentDetailResponse, error) {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to get apartment: %w", err)
	}

	if apartment == nil {
		return nil, fmt.Errorf("apartment not found")
	}

	furniture, err := s.repo.ListPlacedFurniture(ctx, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to list furniture: %w", err)
	}

	var itemDetails []models.FurnitureItem
	functionalBonuses := make(map[string]interface{})

	for _, f := range furniture {
		item, err := s.repo.GetFurnitureItemByID(ctx, f.FurnitureItemID)
		if err == nil && item != nil {
			itemDetails = append(itemDetails, *item)

			for key, value := range item.FunctionBonus {
				if existing, ok := functionalBonuses[key]; ok {
					if num, ok := existing.(float64); ok {
						if val, ok := value.(float64); ok {
							functionalBonuses[key] = num + val
						}
					}
				} else {
					functionalBonuses[key] = value
				}
			}
		}
	}

	return &models.ApartmentDetailResponse{
		Apartment:         apartment,
		Furniture:         furniture,
		ItemDetails:       itemDetails,
		FunctionalBonuses: functionalBonuses,
	}, nil
}

func (s *HousingService) VisitApartment(ctx context.Context, req *models.VisitApartmentRequest) error {
	apartment, err := s.repo.GetApartmentByID(ctx, req.ApartmentID)
	if err != nil {
		return fmt.Errorf("failed to get apartment: %w", err)
	}

	if apartment == nil {
		return fmt.Errorf("apartment not found")
	}

	if !apartment.IsPublic && apartment.OwnerID != req.CharacterID {
		hasAccess := false
		for _, guestID := range apartment.Guests {
			if guestID == req.CharacterID {
				hasAccess = true
				break
			}
		}
		if !hasAccess {
			return fmt.Errorf("unauthorized: apartment is private")
		}
	}

	visit := &models.ApartmentVisit{
		ID:          uuid.New(),
		ApartmentID: req.ApartmentID,
		VisitorID:   req.CharacterID,
		VisitedAt:   time.Now(),
	}

	if err := s.repo.CreateVisit(ctx, visit); err != nil {
		return fmt.Errorf("failed to create visit: %w", err)
	}

	s.publishEvent(ctx, "housing:apartment:visited", map[string]interface{}{
		"apartment_id": req.ApartmentID,
		"visitor_id":   req.CharacterID,
	})

	RecordApartmentVisit()

	return nil
}

func (s *HousingService) GetPrestigeLeaderboard(ctx context.Context, limit, offset int) ([]models.PrestigeLeaderboardEntry, int, error) {
	entries, total, err := s.repo.GetPrestigeLeaderboard(ctx, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get leaderboard: %w", err)
	}

	return entries, total, nil
}

func (s *HousingService) recalculatePrestigeScore(ctx context.Context, apartmentID uuid.UUID) {
	apartment, err := s.repo.GetApartmentByID(ctx, apartmentID)
	if err != nil || apartment == nil {
		return
	}

	baseScore := 0
	switch apartment.ApartmentType {
	case models.ApartmentTypeStudio:
		baseScore = 100
	case models.ApartmentTypeStandard:
		baseScore = 250
	case models.ApartmentTypePenthouse:
		baseScore = 500
	case models.ApartmentTypeGuildHall:
		baseScore = 1000
	}

	furniture, err := s.repo.ListPlacedFurniture(ctx, apartmentID)
	if err == nil {
		for _, f := range furniture {
			item, err := s.repo.GetFurnitureItemByID(ctx, f.FurnitureItemID)
			if err == nil && item != nil {
				baseScore += item.PrestigeValue
			}
		}
	}

	apartment.PrestigeScore = baseScore
	apartment.UpdatedAt = time.Now()
	s.repo.UpdateApartment(ctx, apartment)
}

func (s *HousingService) publishEvent(ctx context.Context, eventType string, data map[string]interface{}) {
	event := map[string]interface{}{
		"type":      eventType,
		"timestamp": time.Now().Unix(),
		"data":      data,
	}

	eventJSON, err := json.Marshal(event)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal event")
		return
	}

	if err := s.redis.Publish(ctx, "housing:events", eventJSON).Err(); err != nil {
		s.logger.WithError(err).Error("Failed to publish event")
	}
}

