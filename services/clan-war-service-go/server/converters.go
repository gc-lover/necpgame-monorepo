// Issue: ogen migration
package server

import (
	"time"

	"github.com/necpgame/clan-war-service-go/models"
	clanwarapi "github.com/necpgame/clan-war-service-go/pkg/api"
)

func convertDeclareWarRequestFromAPI(req *clanwarapi.DeclareWarRequest) *models.DeclareWarRequest {
	result := &models.DeclareWarRequest{
		AttackerGuildID: req.AttackerClanID,
		DefenderGuildID: req.DefenderClanID,
	}

	if len(req.AllyGuildIds) > 0 {
		result.Allies = req.AllyGuildIds
	}

	// TerritoryID not in API schema, set to nil
	result.TerritoryID = nil

	return result
}

func convertClanWarToAPI(war *models.ClanWar) clanwarapi.ClanWar {
	result := clanwarapi.ClanWar{
		ID:             clanwarapi.NewOptUUID(war.ID),
		AttackerClanID: clanwarapi.NewOptUUID(war.AttackerGuildID),
		DefenderClanID: clanwarapi.NewOptUUID(war.DefenderGuildID),
		Status:         clanwarapi.NewOptClanWarStatus(convertWarStatusToAPI(war.Status)),
		AttackerScore:  clanwarapi.NewOptInt(war.AttackerScore),
		DefenderScore:  clanwarapi.NewOptInt(war.DefenderScore),
		DeclaredAt:     clanwarapi.NewOptDateTime(war.CreatedAt),
		CreatedAt:      clanwarapi.NewOptDateTime(war.CreatedAt),
		UpdatedAt:      clanwarapi.NewOptDateTime(war.UpdatedAt),
	}

	if len(war.Allies) > 0 {
		result.AllyGuildIds = war.Allies
		result.AlliesAttacker = war.Allies
	}

	if war.TerritoryID != nil {
		// TerritoryID not in API schema, skip
	}

	if war.WinnerGuildID != nil {
		result.WinnerClanID = clanwarapi.NewOptNilUUID(*war.WinnerGuildID)
	}

	if war.StartTime != (time.Time{}) {
		result.ActiveStartedAt = clanwarapi.NewOptNilDateTime(war.StartTime)
	}

	if war.EndTime != nil {
		result.EndedAt = clanwarapi.NewOptNilDateTime(*war.EndTime)
	}

	phase := convertWarPhaseToAPI(war.Phase)
	result.Phase = clanwarapi.NewOptNilClanWarPhase(phase)

	return result
}

func convertWarListToAPI(wars []models.ClanWar, total int) clanwarapi.ActiveWarsResponse {
	items := make([]clanwarapi.ClanWar, len(wars))
	for i, war := range wars {
		items[i] = convertClanWarToAPI(&war)
	}

	return clanwarapi.ActiveWarsResponse{
		Wars:   items,
		Total:  clanwarapi.NewOptInt(total),
		Limit:  clanwarapi.NewOptInt(len(wars)),
		Offset: clanwarapi.NewOptInt(0),
	}
}

func convertWarStatusToAPI(status models.WarStatus) clanwarapi.ClanWarStatus {
	switch status {
	case models.WarStatusDeclared:
		return clanwarapi.ClanWarStatusDECLARED
	case models.WarStatusOngoing:
		return clanwarapi.ClanWarStatusACTIVE
	case models.WarStatusCompleted:
		return clanwarapi.ClanWarStatusENDED
	case models.WarStatusCancelled:
		return clanwarapi.ClanWarStatusENDED
	default:
		return clanwarapi.ClanWarStatusDECLARED
	}
}

func convertWarPhaseToAPI(phase models.WarPhase) clanwarapi.ClanWarPhase {
	switch phase {
	case models.WarPhasePreparation:
		return clanwarapi.ClanWarPhasePREPARATION
	case models.WarPhaseActive:
		return clanwarapi.ClanWarPhaseACTIVE
	case models.WarPhaseCompleted:
		return clanwarapi.ClanWarPhaseENDING
	case models.WarPhaseCancelled:
		return clanwarapi.ClanWarPhaseENDING
	default:
		return clanwarapi.ClanWarPhasePREPARATION
	}
}

