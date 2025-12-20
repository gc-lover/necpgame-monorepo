// Package server Issue: #1600 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/character-engram-security-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

type EngramSecurityHandlers struct{}

func NewEngramSecurityHandlers() *EngramSecurityHandlers {
	return &EngramSecurityHandlers{}
}

func (h *EngramSecurityHandlers) EncodeEngram(ctx context.Context, req *api.EncodeEngramRequest, params api.EncodeEngramParams) (api.EncodeEngramRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	protectionTierName := getProtectionTierName(req.ProtectionTier)
	requiredNetrunnerLevel := getRequiredNetrunnerLevel(req.ProtectionTier)

	copyProtection := true
	hackProtection := true
	installProtection := false
	var boundCharacterID api.OptNilUUID

	if req.ProtectionSettings.IsSet() {
		settings := req.ProtectionSettings.Value
		if settings.CopyProtection.IsSet() {
			copyProtection = settings.CopyProtection.Value
		}
		if settings.HackProtection.IsSet() {
			hackProtection = settings.HackProtection.Value
		}
		if settings.InstallProtection.IsSet() {
			installProtection = settings.InstallProtection.Value
		}
		if settings.BoundCharacterID.IsSet() {
			boundCharacterID = settings.BoundCharacterID
		}
	}

	now := time.Now()
	encodedBy := uuid.UUID{}

	response := &api.EngramProtection{
		EngramID:               params.EngramID,
		ProtectionTier:         req.ProtectionTier,
		ProtectionTierName:     api.NewOptEngramProtectionProtectionTierName(protectionTierName),
		RequiredNetrunnerLevel: api.NewOptInt(requiredNetrunnerLevel),
		ProtectionSettings: api.NewOptEngramProtectionProtectionSettings(api.EngramProtectionProtectionSettings{
			CopyProtection:    api.NewOptBool(copyProtection),
			HackProtection:    api.NewOptBool(hackProtection),
			InstallProtection: api.NewOptBool(installProtection),
			BoundCharacterID:  boundCharacterID,
		}),
		EncodedAt: api.NewOptDateTime(now),
		EncodedBy: api.NewOptUUID(encodedBy),
	}

	return response, nil
}

func (h *EngramSecurityHandlers) GetEngramProtection(ctx context.Context, params api.GetEngramProtectionParams) (api.GetEngramProtectionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	protectionTier := 3
	protectionTierName := api.EngramProtectionProtectionTierNameAdvanced
	requiredNetrunnerLevel := 75
	copyProtection := true
	hackProtection := true
	installProtection := false
	now := time.Now()
	encodedBy := uuid.UUID{}

	response := &api.EngramProtection{
		EngramID:               params.EngramID,
		ProtectionTier:         protectionTier,
		ProtectionTierName:     api.NewOptEngramProtectionProtectionTierName(protectionTierName),
		RequiredNetrunnerLevel: api.NewOptInt(requiredNetrunnerLevel),
		ProtectionSettings: api.NewOptEngramProtectionProtectionSettings(api.EngramProtectionProtectionSettings{
			CopyProtection:    api.NewOptBool(true),
			HackProtection:    api.NewOptBool(true),
			InstallProtection: api.NewOptBool(false),
			BoundCharacterID:  api.OptNilUUID{},
		}),
		EncodedAt: api.NewOptDateTime(now),
		EncodedBy: api.NewOptUUID(encodedBy),
	}

	return response, nil
}

func getProtectionTierName(tier int) api.EngramProtectionProtectionTierName {
	switch tier {
	case 1:
		return api.EngramProtectionProtectionTierNameBasic
	case 2:
		return api.EngramProtectionProtectionTierNameStandard
	case 3:
		return api.EngramProtectionProtectionTierNameAdvanced
	case 4:
		return api.EngramProtectionProtectionTierNameCorporate
	case 5:
		return api.EngramProtectionProtectionTierNameMilitary
	default:
		return api.EngramProtectionProtectionTierNameBasic
	}
}

func getRequiredNetrunnerLevel(tier int) int {
	switch tier {
	case 1:
		return 20
	case 2:
		return 50
	case 3:
		return 75
	case 4:
		return 90
	case 5:
		return 95
	default:
		return 20
	}
}
