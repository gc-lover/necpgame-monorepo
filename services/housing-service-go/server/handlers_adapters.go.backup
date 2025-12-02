package server

import (
	"time"

	"github.com/necpgame/housing-service-go/models"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func toAPIApartment(apt *models.Apartment) api.Apartment {
	if apt == nil {
		return api.Apartment{}
	}

	apiID := openapi_types.UUID(apt.ID)
	apiOwnerID := openapi_types.UUID(apt.OwnerID)

	accessSetting := api.ApartmentAccessSettingPRIVATE
	if apt.IsPublic {
		accessSetting = api.ApartmentAccessSettingPUBLIC
	}

	result := api.Apartment{
		Id:             &apiID,
		OwnerId:        &apiOwnerID,
		ApartmentType:  toAPIApartmentType(apt.ApartmentType),
		LocationId:     stringPtr(apt.Location),
		Price:          int64Ptr(apt.Price),
		FurnitureSlots: intPtr(apt.FurnitureSlots),
		PrestigeScore:  intPtr(apt.PrestigeScore),
		AccessSetting:  &accessSetting,
		CreatedAt:      timePtr(apt.CreatedAt),
		UpdatedAt:      timePtr(apt.UpdatedAt),
	}

	if apt.Guests != nil && len(apt.Guests) > 0 {
		guests := make([]openapi_types.UUID, len(apt.Guests))
		for i, g := range apt.Guests {
			guests[i] = openapi_types.UUID(g)
		}
		result.Guests = &guests
	}

	return result
}

func toAPIPlacedFurniture(pf *models.PlacedFurniture) api.PlacedFurniture {
	if pf == nil {
		return api.PlacedFurniture{}
	}

	apiID := openapi_types.UUID(pf.ID)
	apiApartmentID := openapi_types.UUID(pf.ApartmentID)

	var posX, posY, posZ float32
	if pf.Position != nil {
		if x, ok := pf.Position["x"].(float64); ok {
			posX = float32(x)
		}
		if y, ok := pf.Position["y"].(float64); ok {
			posY = float32(y)
		}
		if z, ok := pf.Position["z"].(float64); ok {
			posZ = float32(z)
		}
	}

	var rotYaw float32
	if pf.Rotation != nil {
		if yaw, ok := pf.Rotation["yaw"].(float64); ok {
			rotYaw = float32(yaw)
		}
	}

	var scale float32 = 1.0
	if pf.Scale != nil {
		if s, ok := pf.Scale["uniform"].(float64); ok {
			scale = float32(s)
		}
	}

	result := api.PlacedFurniture{
		Id:              &apiID,
		ApartmentId:     &apiApartmentID,
		FurnitureItemId: stringPtr(pf.FurnitureItemID),
		PositionX:       float32Ptr(posX),
		PositionY:       float32Ptr(posY),
		PositionZ:       float32Ptr(posZ),
		RotationYaw:     float32Ptr(rotYaw),
		Scale:           float32Ptr(scale),
		PlacedAt:        timePtr(pf.CreatedAt),
	}

	return result
}

func toAPIPrestigeEntry(pe *models.PrestigeLeaderboardEntry) api.PrestigeEntry {
	if pe == nil {
		return api.PrestigeEntry{}
	}

	apiApartmentID := openapi_types.UUID(pe.ApartmentID)
	apiOwnerID := openapi_types.UUID(pe.OwnerID)

	result := api.PrestigeEntry{
		ApartmentId:   &apiApartmentID,
		OwnerId:       &apiOwnerID,
		OwnerName:     stringPtr(pe.OwnerName),
		PrestigeScore: intPtr(pe.PrestigeScore),
		ApartmentType: stringPtr(string(pe.ApartmentType)),
	}

	return result
}

func toAPIApartmentType(at models.ApartmentType) *api.ApartmentApartmentType {
	var result api.ApartmentApartmentType
	switch at {
	case models.ApartmentTypeStudio:
		result = api.STUDIO
	case models.ApartmentTypeStandard:
		result = api.STANDARD
	case models.ApartmentTypePenthouse:
		result = api.PENTHOUSE
	case models.ApartmentTypeGuildHall:
		result = api.GUILDHALL
	default:
		result = api.STANDARD
	}
	return &result
}

func stringPtr(s string) *string {
	return &s
}

func intPtr(i int) *int {
	return &i
}

func int64Ptr(i int64) *int64 {
	return &i
}

func float32Ptr(f float32) *float32 {
	return &f
}

func timePtr(t time.Time) *time.Time {
	return &t
}
