package server

import (
	"time"

	"github.com/google/uuid"
	cosmeticapi "github.com/necpgame/cosmetic-service-go/pkg/api"
	"github.com/necpgame/cosmetic-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertCosmeticItemToAPI(item *models.CosmeticItem) cosmeticapi.CosmeticItem {
	result := cosmeticapi.CosmeticItem{
		Id:            (*openapi_types.UUID)(&item.ID),
		Code:          &item.Code,
		Name:          &item.Name,
		Description:   &item.Description,
		Cost:          &item.Cost,
		CurrencyType:  &item.CurrencyType,
		IsExclusive:   &item.IsExclusive,
		Source:        &item.Source,
		VisualEffects: &item.VisualEffects,
		Assets:        &item.Assets,
		IsActive:      &item.IsActive,
		CreatedAt:     &item.CreatedAt,
		UpdatedAt:     &item.UpdatedAt,
	}

	category := convertCosmeticCategoryToAPI(item.Category)
	result.Category = &category

	cosmeticType := convertCosmeticTypeToAPI(item.CosmeticType)
	result.CosmeticType = &cosmeticType

	rarity := convertCosmeticRarityToAPI(item.Rarity)
	result.Rarity = &rarity

	return result
}

func convertCosmeticItemFromAPI(item *cosmeticapi.CosmeticItem) *models.CosmeticItem {
	if item == nil {
		return nil
	}

	result := &models.CosmeticItem{
		Code:          "",
		Name:          "",
		Description:   "",
		Cost:          0,
		CurrencyType:  "",
		IsExclusive:   false,
		Source:        "",
		VisualEffects: make(map[string]interface{}),
		Assets:        make(map[string]interface{}),
		IsActive:      false,
	}

	if item.Id != nil {
		result.ID = uuid.UUID(*item.Id)
	}
	if item.Code != nil {
		result.Code = *item.Code
	}
	if item.Name != nil {
		result.Name = *item.Name
	}
	if item.Description != nil {
		result.Description = *item.Description
	}
	if item.Cost != nil {
		result.Cost = *item.Cost
	}
	if item.CurrencyType != nil {
		result.CurrencyType = *item.CurrencyType
	}
	if item.IsExclusive != nil {
		result.IsExclusive = *item.IsExclusive
	}
	if item.Source != nil {
		result.Source = *item.Source
	}
	if item.VisualEffects != nil {
		result.VisualEffects = *item.VisualEffects
	}
	if item.Assets != nil {
		result.Assets = *item.Assets
	}
	if item.IsActive != nil {
		result.IsActive = *item.IsActive
	}
	if item.CreatedAt != nil {
		result.CreatedAt = *item.CreatedAt
	}
	if item.UpdatedAt != nil {
		result.UpdatedAt = *item.UpdatedAt
	}

	if item.Category != nil {
		result.Category = convertCosmeticCategoryFromAPI(*item.Category)
	}
	if item.CosmeticType != nil {
		result.CosmeticType = convertCosmeticTypeFromAPI(*item.CosmeticType)
	}
	if item.Rarity != nil {
		result.Rarity = convertCosmeticRarityFromAPI(*item.Rarity)
	}

	return result
}

func convertCosmeticCategoryToAPI(category models.CosmeticCategory) cosmeticapi.CosmeticItemCategory {
	switch category {
	case models.CosmeticCategoryCharacterSkin:
		return cosmeticapi.CosmeticItemCategoryCHARACTERSKIN
	case models.CosmeticCategoryWeaponSkin:
		return cosmeticapi.CosmeticItemCategoryWEAPONSKIN
	case models.CosmeticCategoryEmote:
		return cosmeticapi.CosmeticItemCategoryEMOTE
	case models.CosmeticCategoryTitle:
		return cosmeticapi.CosmeticItemCategoryTITLE
	case models.CosmeticCategoryNamePlate:
		return cosmeticapi.CosmeticItemCategoryNAMEPLATE
	default:
		return cosmeticapi.CosmeticItemCategoryCHARACTERSKIN
	}
}

func convertCosmeticCategoryFromAPI(category cosmeticapi.CosmeticItemCategory) models.CosmeticCategory {
	switch category {
	case cosmeticapi.CosmeticItemCategoryCHARACTERSKIN:
		return models.CosmeticCategoryCharacterSkin
	case cosmeticapi.CosmeticItemCategoryWEAPONSKIN:
		return models.CosmeticCategoryWeaponSkin
	case cosmeticapi.CosmeticItemCategoryEMOTE:
		return models.CosmeticCategoryEmote
	case cosmeticapi.CosmeticItemCategoryTITLE:
		return models.CosmeticCategoryTitle
	case cosmeticapi.CosmeticItemCategoryNAMEPLATE:
		return models.CosmeticCategoryNamePlate
	default:
		return models.CosmeticCategoryCharacterSkin
	}
}

func convertCosmeticTypeToAPI(cosmeticType models.CosmeticType) cosmeticapi.CosmeticItemCosmeticType {
	switch cosmeticType {
	case models.CosmeticTypeSkin:
		return cosmeticapi.CosmeticItemCosmeticTypeSKIN
	case models.CosmeticTypeEmote:
		return cosmeticapi.CosmeticItemCosmeticTypeEMOTE
	case models.CosmeticTypeTitle:
		return cosmeticapi.CosmeticItemCosmeticTypeTITLE
	case models.CosmeticTypeNamePlate:
		return cosmeticapi.CosmeticItemCosmeticTypeNAMEPLATE
	default:
		return cosmeticapi.CosmeticItemCosmeticTypeSKIN
	}
}

func convertCosmeticTypeFromAPI(cosmeticType cosmeticapi.CosmeticItemCosmeticType) models.CosmeticType {
	switch cosmeticType {
	case cosmeticapi.CosmeticItemCosmeticTypeSKIN:
		return models.CosmeticTypeSkin
	case cosmeticapi.CosmeticItemCosmeticTypeEMOTE:
		return models.CosmeticTypeEmote
	case cosmeticapi.CosmeticItemCosmeticTypeTITLE:
		return models.CosmeticTypeTitle
	case cosmeticapi.CosmeticItemCosmeticTypeNAMEPLATE:
		return models.CosmeticTypeNamePlate
	default:
		return models.CosmeticTypeSkin
	}
}

func convertCosmeticRarityToAPI(rarity models.CosmeticRarity) cosmeticapi.CosmeticItemRarity {
	switch rarity {
	case models.CosmeticRarityCommon:
		return cosmeticapi.CosmeticItemRarityCOMMON
	case models.CosmeticRarityUncommon:
		return cosmeticapi.CosmeticItemRarityUNCOMMON
	case models.CosmeticRarityRare:
		return cosmeticapi.CosmeticItemRarityRARE
	case models.CosmeticRarityEpic:
		return cosmeticapi.CosmeticItemRarityEPIC
	case models.CosmeticRarityLegendary:
		return cosmeticapi.CosmeticItemRarityLEGENDARY
	case models.CosmeticRarityExotic:
		return cosmeticapi.CosmeticItemRarityEXOTIC
	default:
		return cosmeticapi.CosmeticItemRarityCOMMON
	}
}

func convertCosmeticRarityFromAPI(rarity cosmeticapi.CosmeticItemRarity) models.CosmeticRarity {
	switch rarity {
	case cosmeticapi.CosmeticItemRarityCOMMON:
		return models.CosmeticRarityCommon
	case cosmeticapi.CosmeticItemRarityUNCOMMON:
		return models.CosmeticRarityUncommon
	case cosmeticapi.CosmeticItemRarityRARE:
		return models.CosmeticRarityRare
	case cosmeticapi.CosmeticItemRarityEPIC:
		return models.CosmeticRarityEpic
	case cosmeticapi.CosmeticItemRarityLEGENDARY:
		return models.CosmeticRarityLegendary
	case cosmeticapi.CosmeticItemRarityEXOTIC:
		return models.CosmeticRarityExotic
	default:
		return models.CosmeticRarityCommon
	}
}

func convertCosmeticCatalogResponseToAPI(response *models.CosmeticCatalogResponse) cosmeticapi.CosmeticCatalogResponse {
	result := cosmeticapi.CosmeticCatalogResponse{
		Total:  &response.Total,
		Limit:  &response.Limit,
		Offset: &response.Offset,
	}

	items := make([]cosmeticapi.CosmeticItem, len(response.Items))
	for i, item := range response.Items {
		items[i] = convertCosmeticItemToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertCosmeticCategoriesResponseToAPI(response *models.CosmeticCategoriesResponse) cosmeticapi.CosmeticCategoriesResponse {
	categories := make([]struct {
		Category *cosmeticapi.CosmeticCategoriesResponseCategoriesCategory `json:"category,omitempty"`
		Count    *int                                                      `json:"count,omitempty"`
	}, len(response.Categories))

	for i, cat := range response.Categories {
		category := convertCosmeticCategoryToAPI(cat.Category)
		catCategory := cosmeticapi.CosmeticCategoriesResponseCategoriesCategory(category)
		categories[i].Category = &catCategory
		categories[i].Count = &cat.Count
	}

	return cosmeticapi.CosmeticCategoriesResponse{
		Categories: &categories,
	}
}

func convertPlayerCosmeticToAPI(pc *models.PlayerCosmetic) cosmeticapi.PlayerCosmetic {
	result := cosmeticapi.PlayerCosmetic{
		Id:           (*openapi_types.UUID)(&pc.ID),
		PlayerId:     (*openapi_types.UUID)(&pc.PlayerID),
		CosmeticItemId: (*openapi_types.UUID)(&pc.CosmeticItemID),
		Source:       &pc.Source,
		ObtainedAt:   &pc.ObtainedAt,
		TimesUsed:    &pc.TimesUsed,
		CreatedAt:    &pc.CreatedAt,
	}

	if pc.LastUsedAt != nil {
		result.LastUsedAt = pc.LastUsedAt
	}

	if pc.CosmeticItem != nil {
		item := convertCosmeticItemToAPI(pc.CosmeticItem)
		result.CosmeticItem = &item
	}

	return result
}

func convertEquippedCosmeticsToAPI(eq *models.EquippedCosmetics) cosmeticapi.EquippedCosmetics {
	result := cosmeticapi.EquippedCosmetics{
		PlayerId:  (*openapi_types.UUID)(&eq.PlayerID),
		UpdatedAt: &eq.UpdatedAt,
	}

	if eq.CharacterSkinID != nil {
		result.CharacterSkinId = (*openapi_types.UUID)(eq.CharacterSkinID)
	}
	if eq.CharacterSkin != nil {
		item := convertCosmeticItemToAPI(eq.CharacterSkin)
		result.CharacterSkin = &item
	}

	if eq.WeaponSkinID != nil {
		result.WeaponSkinId = (*openapi_types.UUID)(eq.WeaponSkinID)
	}
	if eq.WeaponSkin != nil {
		item := convertCosmeticItemToAPI(eq.WeaponSkin)
		result.WeaponSkin = &item
	}

	if eq.TitleID != nil {
		result.TitleId = (*openapi_types.UUID)(eq.TitleID)
	}
	if eq.Title != nil {
		item := convertCosmeticItemToAPI(eq.Title)
		result.Title = &item
	}

	if eq.NamePlateID != nil {
		result.NamePlateId = (*openapi_types.UUID)(eq.NamePlateID)
	}
	if eq.NamePlate != nil {
		item := convertCosmeticItemToAPI(eq.NamePlate)
		result.NamePlate = &item
	}

	if eq.Emote1ID != nil {
		result.Emote1Id = (*openapi_types.UUID)(eq.Emote1ID)
	}
	if eq.Emote1 != nil {
		item := convertCosmeticItemToAPI(eq.Emote1)
		result.Emote1 = &item
	}

	if eq.Emote2ID != nil {
		result.Emote2Id = (*openapi_types.UUID)(eq.Emote2ID)
	}
	if eq.Emote2 != nil {
		item := convertCosmeticItemToAPI(eq.Emote2)
		result.Emote2 = &item
	}

	if eq.Emote3ID != nil {
		result.Emote3Id = (*openapi_types.UUID)(eq.Emote3ID)
	}
	if eq.Emote3 != nil {
		item := convertCosmeticItemToAPI(eq.Emote3)
		result.Emote3 = &item
	}

	if eq.Emote4ID != nil {
		result.Emote4Id = (*openapi_types.UUID)(eq.Emote4ID)
	}
	if eq.Emote4 != nil {
		item := convertCosmeticItemToAPI(eq.Emote4)
		result.Emote4 = &item
	}

	return result
}

func convertPurchaseCosmeticRequestFromAPI(req *cosmeticapi.PurchaseCosmeticRequest) *models.PurchaseCosmeticRequest {
	return &models.PurchaseCosmeticRequest{
		PlayerID:   uuid.UUID(req.PlayerId),
		CosmeticID: uuid.UUID(req.CosmeticId),
	}
}

func convertEquipCosmeticRequestFromAPI(req *cosmeticapi.EquipCosmeticRequest) *models.EquipCosmeticRequest {
	slot := convertCosmeticSlotFromAPI(req.Slot)
	return &models.EquipCosmeticRequest{
		PlayerID: uuid.UUID(req.PlayerId),
		Slot:     slot,
	}
}

func convertUnequipCosmeticRequestFromAPI(req *cosmeticapi.UnequipCosmeticRequest) *models.UnequipCosmeticRequest {
	slot := convertUnequipCosmeticSlotFromAPI(req.Slot)
	return &models.UnequipCosmeticRequest{
		PlayerID: uuid.UUID(req.PlayerId),
		Slot:     slot,
	}
}

func convertUnequipCosmeticSlotFromAPI(slot cosmeticapi.UnequipCosmeticRequestSlot) models.CosmeticSlot {
	return convertCosmeticSlotFromAPI(cosmeticapi.EquipCosmeticRequestSlot(slot))
}

func convertCosmeticSlotToAPI(slot models.CosmeticSlot) cosmeticapi.EquipCosmeticRequestSlot {
	switch slot {
	case models.CosmeticSlotCharacterSkin:
		return cosmeticapi.EquipCosmeticRequestSlotCHARACTERSKIN
	case models.CosmeticSlotWeaponSkin:
		return cosmeticapi.EquipCosmeticRequestSlotWEAPONSKIN
	case models.CosmeticSlotTitle:
		return cosmeticapi.EquipCosmeticRequestSlotTITLE
	case models.CosmeticSlotNamePlate:
		return cosmeticapi.EquipCosmeticRequestSlotNAMEPLATE
	case models.CosmeticSlotEmote1:
		return cosmeticapi.EquipCosmeticRequestSlotEMOTE1
	case models.CosmeticSlotEmote2:
		return cosmeticapi.EquipCosmeticRequestSlotEMOTE2
	case models.CosmeticSlotEmote3:
		return cosmeticapi.EquipCosmeticRequestSlotEMOTE3
	case models.CosmeticSlotEmote4:
		return cosmeticapi.EquipCosmeticRequestSlotEMOTE4
	default:
		return cosmeticapi.EquipCosmeticRequestSlotCHARACTERSKIN
	}
}

func convertCosmeticSlotFromAPI(slot cosmeticapi.EquipCosmeticRequestSlot) models.CosmeticSlot {
	switch slot {
	case cosmeticapi.EquipCosmeticRequestSlotCHARACTERSKIN:
		return models.CosmeticSlotCharacterSkin
	case cosmeticapi.EquipCosmeticRequestSlotWEAPONSKIN:
		return models.CosmeticSlotWeaponSkin
	case cosmeticapi.EquipCosmeticRequestSlotTITLE:
		return models.CosmeticSlotTitle
	case cosmeticapi.EquipCosmeticRequestSlotNAMEPLATE:
		return models.CosmeticSlotNamePlate
	case cosmeticapi.EquipCosmeticRequestSlotEMOTE1:
		return models.CosmeticSlotEmote1
	case cosmeticapi.EquipCosmeticRequestSlotEMOTE2:
		return models.CosmeticSlotEmote2
	case cosmeticapi.EquipCosmeticRequestSlotEMOTE3:
		return models.CosmeticSlotEmote3
	case cosmeticapi.EquipCosmeticRequestSlotEMOTE4:
		return models.CosmeticSlotEmote4
	default:
		return models.CosmeticSlotCharacterSkin
	}
}

func convertDailyShopResponseToAPI(shop *models.DailyShopResponse) cosmeticapi.DailyShopResponse {
	rotationDate := openapi_types.Date{Time: shop.RotationDate}
	result := cosmeticapi.DailyShopResponse{
		RotationId:     (*openapi_types.UUID)(&shop.RotationID),
		RotationDate:   &rotationDate,
		NextRotationAt: &shop.NextRotationAt,
	}

	items := make([]cosmeticapi.CosmeticItem, len(shop.Items))
	for i, item := range shop.Items {
		items[i] = convertCosmeticItemToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertShopHistoryResponseToAPI(response *models.ShopHistoryResponse) cosmeticapi.ShopHistoryResponse {
	result := cosmeticapi.ShopHistoryResponse{
		Total:  &response.Total,
		Limit:  &response.Limit,
		Offset: &response.Offset,
	}

	rotations := make([]struct {
		CreatedAt    *time.Time          `json:"created_at,omitempty"`
		Id           *openapi_types.UUID `json:"id,omitempty"`
		Items        *[]cosmeticapi.CosmeticItem `json:"items,omitempty"`
		RotationDate *openapi_types.Date `json:"rotation_date,omitempty"`
	}, len(response.Rotations))

	for i, rot := range response.Rotations {
		rotations[i].Id = (*openapi_types.UUID)(&rot.ID)
		rotationDate := openapi_types.Date{Time: rot.RotationDate}
		rotations[i].RotationDate = &rotationDate
		rotations[i].CreatedAt = &rot.CreatedAt

		items := make([]cosmeticapi.CosmeticItem, len(rot.Items))
		for j, item := range rot.Items {
			items[j] = convertCosmeticItemToAPI(&item)
		}
		rotations[i].Items = &items
	}
	result.Rotations = &rotations

	return result
}

func convertPurchaseHistoryResponseToAPI(response *models.PurchaseHistoryResponse) cosmeticapi.PurchaseHistoryResponse {
	result := cosmeticapi.PurchaseHistoryResponse{
		Total:  &response.Total,
		Limit:  &response.Limit,
		Offset: &response.Offset,
	}

	purchases := make([]struct {
		CosmeticItem   *cosmeticapi.CosmeticItem       `json:"cosmetic_item,omitempty"`
		CosmeticItemId *openapi_types.UUID `json:"cosmetic_item_id,omitempty"`
		Cost           *int64              `json:"cost,omitempty"`
		CurrencyType   *string             `json:"currency_type,omitempty"`
		Id             *openapi_types.UUID `json:"id,omitempty"`
		PlayerId       *openapi_types.UUID `json:"player_id,omitempty"`
		PurchasedAt    *time.Time          `json:"purchased_at,omitempty"`
	}, len(response.Purchases))

	for i, pr := range response.Purchases {
		purchases[i].Id = (*openapi_types.UUID)(&pr.ID)
		purchases[i].PlayerId = (*openapi_types.UUID)(&pr.PlayerID)
		purchases[i].CosmeticItemId = (*openapi_types.UUID)(&pr.CosmeticItemID)
		purchases[i].Cost = &pr.Cost
		purchases[i].CurrencyType = &pr.CurrencyType
		purchases[i].PurchasedAt = &pr.PurchasedAt

		if pr.CosmeticItem != nil {
			item := convertCosmeticItemToAPI(pr.CosmeticItem)
			purchases[i].CosmeticItem = &item
		}
	}
	result.Purchases = &purchases

	return result
}

func convertCosmeticInventoryResponseToAPI(response *models.CosmeticInventoryResponse) cosmeticapi.CosmeticInventoryResponse {
	result := cosmeticapi.CosmeticInventoryResponse{
		PlayerId: (*openapi_types.UUID)(&response.PlayerID),
		Total:    &response.Total,
		Limit:    &response.Limit,
		Offset:   &response.Offset,
	}

	cosmetics := make([]cosmeticapi.PlayerCosmetic, len(response.Cosmetics))
	for i, pc := range response.Cosmetics {
		cosmetics[i] = convertPlayerCosmeticToAPI(&pc)
	}
	result.Cosmetics = &cosmetics

	return result
}

func convertOwnershipStatusResponseToAPI(response *models.OwnershipStatusResponse) cosmeticapi.OwnershipStatusResponse {
	result := cosmeticapi.OwnershipStatusResponse{
		PlayerId:   (*openapi_types.UUID)(&response.PlayerID),
		CosmeticId: (*openapi_types.UUID)(&response.CosmeticID),
		Owned:      &response.Owned,
	}

	if response.PlayerCosmetic != nil {
		pc := convertPlayerCosmeticToAPI(response.PlayerCosmetic)
		result.PlayerCosmetic = &pc
	}

	return result
}

func convertCosmeticEventToAPI(event *models.CosmeticEvent) cosmeticapi.CosmeticEvent {
	result := cosmeticapi.CosmeticEvent{
		Id:         (*openapi_types.UUID)(&event.ID),
		PlayerId:   (*openapi_types.UUID)(&event.PlayerID),
		EventData:  &event.EventData,
		CreatedAt:  &event.CreatedAt,
	}

	eventType := convertCosmeticEventTypeToAPI(event.EventType)
	result.EventType = &eventType

	if event.CosmeticID != nil {
		result.CosmeticId = (*openapi_types.UUID)(event.CosmeticID)
	}

	return result
}

func convertCosmeticEventTypeToAPI(eventType models.CosmeticEventType) cosmeticapi.CosmeticEventEventType {
	switch eventType {
	case models.CosmeticEventTypePurchased:
		return cosmeticapi.CosmeticEventEventTypeCosmeticPurchased
	case models.CosmeticEventTypeEquipped:
		return cosmeticapi.CosmeticEventEventTypeCosmeticEquipped
	case models.CosmeticEventTypeUnequipped:
		return cosmeticapi.CosmeticEventEventTypeCosmeticUnequipped
	case models.CosmeticEventTypeShopRotated:
		return cosmeticapi.CosmeticEventEventTypeCosmeticShopRotated
	default:
		return cosmeticapi.CosmeticEventEventTypeCosmeticPurchased
	}
}

func convertCosmeticEventsResponseToAPI(response *models.CosmeticEventsResponse) cosmeticapi.CosmeticEventsResponse {
	result := cosmeticapi.CosmeticEventsResponse{
		Total:  &response.Total,
		Limit:  &response.Limit,
		Offset: &response.Offset,
	}

	events := make([]cosmeticapi.CosmeticEvent, len(response.Events))
	for i, event := range response.Events {
		events[i] = convertCosmeticEventToAPI(&event)
	}
	result.Events = &events

	return result
}

