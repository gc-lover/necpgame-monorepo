package handlers

import (
	"context"

	"necpgame/services/tournament-service-go/internal/service"
)

// TournamentHandlers implements the generated Handler interface
type TournamentHandlers struct {
	tournamentSvc *service.TournamentService
}

// NewTournamentHandlers creates a new instance of TournamentHandlers
func NewTournamentHandlers(svc *service.TournamentService) *TournamentHandlers {
	return &TournamentHandlers{
		tournamentSvc: svc,
	}
}
