package server

import (
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
	"github.com/necpgame/companion-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func toAPICompanionType(ct *models.CompanionType) api.CompanionType {
	if ct == nil {
		return api.CompanionType{}
	}

	apiID := openapi_types.UUID{}
	if ct.ID != "" {
		if id, err := uuid.Parse(ct.ID); err == nil {
			apiID = openapi_types.UUID(id)
		}
	}

	category := api.CompanionTypeCategory(ct.Category)

	result := api.CompanionType{
		Id:          &apiID,
		Name:        stringPtr(ct.Name),
		Description: stringPtr(ct.Description),
		Category:    &category,
		Cost:        int64Ptr(ct.Cost),
		CreatedAt:   timePtr(ct.CreatedAt),
	}

	if ct.Stats != nil {
		result.Stats = &ct.Stats
	}

	return result
}

func toAPIPlayerCompanion(pc *models.PlayerCompanion) api.PlayerCompanion {
	if pc == nil {
		return api.PlayerCompanion{}
	}

	apiID := openapi_types.UUID(pc.ID)
	apiCharID := openapi_types.UUID(pc.CharacterID)
	
	var apiTypeID openapi_types.UUID
	if pc.CompanionTypeID != "" {
		if typeID, err := uuid.Parse(pc.CompanionTypeID); err == nil {
			apiTypeID = openapi_types.UUID(typeID)
		}
	}

	isSummoned := pc.Status == models.CompanionStatusSummoned

	result := api.PlayerCompanion{
		Id:            &apiID,
		PlayerId:      &apiCharID,
		CompanionTypeId: &apiTypeID,
		Level:         intPtr(pc.Level),
		Experience:    intPtr(int(pc.Experience)),
		IsSummoned:    &isSummoned,
		CreatedAt:     timePtr(pc.CreatedAt),
		UpdatedAt:     timePtr(pc.UpdatedAt),
	}

	if pc.CustomName != nil {
		result.Name = pc.CustomName
	}

	if pc.SummonedAt != nil {
		result.SummonedAt = pc.SummonedAt
	}

	if pc.Equipment != nil {
		result.Equipment = &pc.Equipment
	}

	return result
}

func toAPICompanionAbility(ca *models.CompanionAbility) api.CompanionAbility {
	if ca == nil {
		return api.CompanionAbility{}
	}

	apiID := openapi_types.UUID(ca.ID)
	apiCompanionID := openapi_types.UUID(ca.PlayerCompanionID)

	result := api.CompanionAbility{
		Id:              &apiID,
		CompanionTypeId: &apiCompanionID,
		Code:            stringPtr(ca.AbilityID),
		IsActive:        boolPtr(ca.IsActive),
	}

	if ca.CooldownUntil != nil {
		result.CooldownSeconds = intPtr(int(time.Until(*ca.CooldownUntil).Seconds()))
	}

	if ca.LastUsedAt != nil {
		result.CooldownSeconds = intPtr(int(time.Until(*ca.LastUsedAt).Seconds() + 30))
	}

	return result
}

func int64Ptr(i int64) *int64 {
	return &i
}

func timePtr(t time.Time) *time.Time {
	return &t
}

