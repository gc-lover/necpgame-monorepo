package repository

import (
	"context"
	"sync"

	"github.com/google/uuid"
	"combat-abilities-service-go/pkg/models"
)

type Repository interface {
	// Abilities
	CreateAbility(ctx context.Context, ability *models.Ability) error
	GetAbility(ctx context.Context, id uuid.UUID) (*models.Ability, error)
	UpdateAbility(ctx context.Context, ability *models.Ability) error
	DeleteAbility(ctx context.Context, id uuid.UUID) error
	ListAbilities(ctx context.Context, limit, offset int) ([]models.Ability, error)

	// Character Abilities
	CreateCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error
	GetCharacterAbility(ctx context.Context, id uuid.UUID) (*models.CharacterAbility, error)
	GetCharacterAbilities(ctx context.Context, characterID uuid.UUID) ([]models.CharacterAbility, error)
	UpdateCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error
	DeleteCharacterAbility(ctx context.Context, id uuid.UUID) error

	// Ability Activations
	CreateActivation(ctx context.Context, activation *models.AbilityActivation) error
	GetActivation(ctx context.Context, id uuid.UUID) (*models.AbilityActivation, error)
	UpdateActivation(ctx context.Context, activation *models.AbilityActivation) error
	GetCharacterCooldowns(ctx context.Context, characterID uuid.UUID) (map[uuid.UUID]int, error)

	// Validation
	ValidateAbilityActivation(ctx context.Context, characterID, abilityID uuid.UUID) error
}

type InMemoryRepository struct {
	mu                sync.RWMutex
	abilities         map[uuid.UUID]*models.Ability
	characterAbilities map[uuid.UUID]*models.CharacterAbility
	activations       map[uuid.UUID]*models.AbilityActivation
}

func NewRepository() *InMemoryRepository {
	return &InMemoryRepository{
		abilities:         make(map[uuid.UUID]*models.Ability),
		characterAbilities: make(map[uuid.UUID]*models.CharacterAbility),
		activations:       make(map[uuid.UUID]*models.AbilityActivation),
	}
}

// Implementations for Abilities
func (r *InMemoryRepository) CreateAbility(ctx context.Context, ability *models.Ability) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if ability.ID == uuid.Nil {
		ability.ID = uuid.New()
	}
	r.abilities[ability.ID] = ability
	return nil
}

func (r *InMemoryRepository) GetAbility(ctx context.Context, id uuid.UUID) (*models.Ability, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	ability, exists := r.abilities[id]
	if !exists {
		return nil, nil
	}
	return ability, nil
}

func (r *InMemoryRepository) UpdateAbility(ctx context.Context, ability *models.Ability) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if _, exists := r.abilities[ability.ID]; !exists {
		return nil
	}
	r.abilities[ability.ID] = ability
	return nil
}

func (r *InMemoryRepository) DeleteAbility(ctx context.Context, id uuid.UUID) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	delete(r.abilities, id)
	return nil
}

func (r *InMemoryRepository) ListAbilities(ctx context.Context, limit, offset int) ([]models.Ability, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	var abilities []models.Ability
	count := 0
	for _, ability := range r.abilities {
		if count >= offset && (limit == 0 || len(abilities) < limit) {
			abilities = append(abilities, *ability)
		}
		count++
	}
	return abilities, nil
}

// Implementations for Character Abilities
func (r *InMemoryRepository) CreateCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if ca.ID == uuid.Nil {
		ca.ID = uuid.New()
	}
	r.characterAbilities[ca.ID] = ca
	return nil
}

func (r *InMemoryRepository) GetCharacterAbility(ctx context.Context, id uuid.UUID) (*models.CharacterAbility, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	ca, exists := r.characterAbilities[id]
	if !exists {
		return nil, nil
	}
	return ca, nil
}

func (r *InMemoryRepository) GetCharacterAbilities(ctx context.Context, characterID uuid.UUID) ([]models.CharacterAbility, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	var abilities []models.CharacterAbility
	for _, ca := range r.characterAbilities {
		if ca.CharacterID == characterID {
			abilities = append(abilities, *ca)
		}
	}
	return abilities, nil
}

func (r *InMemoryRepository) UpdateCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if _, exists := r.characterAbilities[ca.ID]; !exists {
		return nil
	}
	r.characterAbilities[ca.ID] = ca
	return nil
}

func (r *InMemoryRepository) DeleteCharacterAbility(ctx context.Context, id uuid.UUID) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	delete(r.characterAbilities, id)
	return nil
}

// Implementations for Ability Activations
func (r *InMemoryRepository) CreateActivation(ctx context.Context, activation *models.AbilityActivation) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if activation.ID == uuid.Nil {
		activation.ID = uuid.New()
	}
	r.activations[activation.ID] = activation
	return nil
}

func (r *InMemoryRepository) GetActivation(ctx context.Context, id uuid.UUID) (*models.AbilityActivation, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	activation, exists := r.activations[id]
	if !exists {
		return nil, nil
	}
	return activation, nil
}

func (r *InMemoryRepository) UpdateActivation(ctx context.Context, activation *models.AbilityActivation) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if _, exists := r.activations[activation.ID]; !exists {
		return nil
	}
	r.activations[activation.ID] = activation
	return nil
}

func (r *InMemoryRepository) GetCharacterCooldowns(ctx context.Context, characterID uuid.UUID) (map[uuid.UUID]int, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	cooldowns := make(map[uuid.UUID]int)
	for _, ca := range r.characterAbilities {
		if ca.CharacterID == characterID && ca.CooldownRemaining > 0 {
			cooldowns[ca.AbilityID] = ca.CooldownRemaining
		}
	}
	return cooldowns, nil
}

func (r *InMemoryRepository) ValidateAbilityActivation(ctx context.Context, characterID, abilityID uuid.UUID) error {
	// Basic validation - check if character has the ability and it's not on cooldown
	r.mu.RLock()
	defer r.mu.RUnlock()

	for _, ca := range r.characterAbilities {
		if ca.CharacterID == characterID && ca.AbilityID == abilityID {
			if !ca.IsUnlocked {
				return nil // Ability not unlocked
			}
			if ca.CooldownRemaining > 0 {
				return nil // On cooldown
			}
			return nil // Valid
		}
	}
	return nil // Ability not found for character
}
