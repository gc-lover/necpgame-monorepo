package handlers

import (
	"github.com/go-chi/chi/v5"
)

// Routes returns the router with all combat abilities routes
func (h *CombatAbilitiesHandler) Routes() chi.Router {
	r := chi.NewRouter()

	// Health check
	r.Get("/health", h.HealthCheck)

	// Combat abilities routes
	r.Route("/combat/abilities", func(r chi.Router) {
		r.Post("/", h.ActivateAbility)           // POST /combat/abilities
		r.Get("/", h.ListAbilities)               // GET /combat/abilities

		r.Route("/{ability_id}", func(r chi.Router) {
			r.Get("/cooldown", h.GetAbilityCooldown)     // GET /combat/abilities/{ability_id}/cooldown
			r.Get("/synergies", h.GetAbilitySynergies)   // GET /combat/abilities/{ability_id}/synergies
		})

		r.Post("/validate", h.ValidateAbilityActivation) // POST /combat/abilities/validate
	})

	return r
}
