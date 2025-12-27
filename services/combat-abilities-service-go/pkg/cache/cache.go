package cache

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
	"combat-abilities-service-go/pkg/models"
)

type Cache interface {
	// Abilities
	SetAbility(ctx context.Context, ability *models.Ability) error
	GetAbility(ctx context.Context, id uuid.UUID) (*models.Ability, error)
	DeleteAbility(ctx context.Context, id uuid.UUID) error

	// Character Abilities
	SetCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error
	GetCharacterAbility(ctx context.Context, id uuid.UUID) (*models.CharacterAbility, error)
	GetCharacterAbilities(ctx context.Context, characterID uuid.UUID) ([]models.CharacterAbility, error)
	DeleteCharacterAbility(ctx context.Context, id uuid.UUID) error

	// Cooldowns
	SetCooldown(ctx context.Context, characterID, abilityID uuid.UUID, remainingMs int, ttl time.Duration) error
	GetCooldown(ctx context.Context, characterID, abilityID uuid.UUID) (int, error)
	DeleteCooldown(ctx context.Context, characterID, abilityID uuid.UUID) error

	// Activations
	SetActivation(ctx context.Context, activation *models.AbilityActivation) error
	GetActivation(ctx context.Context, id uuid.UUID) (*models.AbilityActivation, error)
	DeleteActivation(ctx context.Context, id uuid.UUID) error
}

type InMemoryCache struct {
	mu                sync.RWMutex
	abilities         map[uuid.UUID]*models.Ability
	characterAbilities map[uuid.UUID]*models.CharacterAbility
	characterAbilitiesByChar map[uuid.UUID][]models.CharacterAbility
	cooldowns         map[string]int // key: "characterID:abilityID"
	activations       map[uuid.UUID]*models.AbilityActivation
	ttl               time.Duration
}

func NewCache() *InMemoryCache {
	return &InMemoryCache{
		abilities:         make(map[uuid.UUID]*models.Ability),
		characterAbilities: make(map[uuid.UUID]*models.CharacterAbility),
		characterAbilitiesByChar: make(map[uuid.UUID][]models.CharacterAbility),
		cooldowns:         make(map[string]int),
		activations:       make(map[uuid.UUID]*models.AbilityActivation),
		ttl:               5 * time.Minute, // Default TTL
	}
}

// Implementations for Abilities
func (c *InMemoryCache) SetAbility(ctx context.Context, ability *models.Ability) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.abilities[ability.ID] = ability
	return nil
}

func (c *InMemoryCache) GetAbility(ctx context.Context, id uuid.UUID) (*models.Ability, error) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	ability, exists := c.abilities[id]
	if !exists {
		return nil, nil
	}
	return ability, nil
}

func (c *InMemoryCache) DeleteAbility(ctx context.Context, id uuid.UUID) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.abilities, id)
	return nil
}

// Implementations for Character Abilities
func (c *InMemoryCache) SetCharacterAbility(ctx context.Context, ca *models.CharacterAbility) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.characterAbilities[ca.ID] = ca

	// Update by character index
	charAbs := c.characterAbilitiesByChar[ca.CharacterID]
	// Remove old entry if exists
	for i, existing := range charAbs {
		if existing.ID == ca.ID {
			charAbs = append(charAbs[:i], charAbs[i+1:]...)
			break
		}
	}
	charAbs = append(charAbs, *ca)
	c.characterAbilitiesByChar[ca.CharacterID] = charAbs

	return nil
}

func (c *InMemoryCache) GetCharacterAbility(ctx context.Context, id uuid.UUID) (*models.CharacterAbility, error) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	ca, exists := c.characterAbilities[id]
	if !exists {
		return nil, nil
	}
	return ca, nil
}

func (c *InMemoryCache) GetCharacterAbilities(ctx context.Context, characterID uuid.UUID) ([]models.CharacterAbility, error) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	abilities, exists := c.characterAbilitiesByChar[characterID]
	if !exists {
		return []models.CharacterAbility{}, nil
	}
	return abilities, nil
}

func (c *InMemoryCache) DeleteCharacterAbility(ctx context.Context, id uuid.UUID) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	ca, exists := c.characterAbilities[id]
	if exists {
		delete(c.characterAbilities, id)
		// Remove from character index
		charAbs := c.characterAbilitiesByChar[ca.CharacterID]
		for i, existing := range charAbs {
			if existing.ID == id {
				c.characterAbilitiesByChar[ca.CharacterID] = append(charAbs[:i], charAbs[i+1:]...)
				break
			}
		}
	}
	return nil
}

// Implementations for Cooldowns
func (c *InMemoryCache) SetCooldown(ctx context.Context, characterID, abilityID uuid.UUID, remainingMs int, ttl time.Duration) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	key := characterID.String() + ":" + abilityID.String()
	c.cooldowns[key] = remainingMs

	// TODO: Implement TTL expiration
	go func() {
		time.Sleep(ttl)
		c.mu.Lock()
		delete(c.cooldowns, key)
		c.mu.Unlock()
	}()

	return nil
}

func (c *InMemoryCache) GetCooldown(ctx context.Context, characterID, abilityID uuid.UUID) (int, error) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	key := characterID.String() + ":" + abilityID.String()
	remaining, exists := c.cooldowns[key]
	if !exists {
		return 0, nil
	}
	return remaining, nil
}

func (c *InMemoryCache) DeleteCooldown(ctx context.Context, characterID, abilityID uuid.UUID) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	key := characterID.String() + ":" + abilityID.String()
	delete(c.cooldowns, key)
	return nil
}

// Implementations for Activations
func (c *InMemoryCache) SetActivation(ctx context.Context, activation *models.AbilityActivation) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.activations[activation.ID] = activation
	return nil
}

func (c *InMemoryCache) GetActivation(ctx context.Context, id uuid.UUID) (*models.AbilityActivation, error) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	activation, exists := c.activations[id]
	if !exists {
		return nil, nil
	}
	return activation, nil
}

func (c *InMemoryCache) DeleteActivation(ctx context.Context, id uuid.UUID) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.activations, id)
	return nil
}
