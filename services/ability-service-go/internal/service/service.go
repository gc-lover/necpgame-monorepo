package service

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/ability-service-go/config"
	"necpgame/services/ability-service-go/internal/models"
	"necpgame/services/ability-service-go/internal/repository"
	api "necpgame/services/ability-service-go/pkg/api"
)

type Service struct {
	logger  *zap.Logger
	repo    *repository.Repository
	config  *config.Config
	server  *api.Server
	handler *Handler
}

func NewService(logger *zap.Logger, repo *repository.Repository, cfg *config.Config) *Service {
	s := &Service{
		logger: logger,
		repo:   repo,
		config: cfg,
	}

	// Create handler with generated API
	s.handler = &Handler{
		logger: logger,
		repo:   repo,
		config: cfg,
	}

	// Create security handler with JWT validation
	sec := NewSecurityHandler(cfg.JWT.Secret, logger)

	// Create server with generated API
	var err error
	s.server, err = api.NewServer(s.handler, sec)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	return s
}

func (s *Service) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	s.server.ServeHTTP(w, r)
}

// SecurityHandler implements the generated SecurityHandler interface
type SecurityHandler struct {
	jwtSecret []byte
	logger    *zap.Logger
}

func NewSecurityHandler(jwtSecret string, logger *zap.Logger) *SecurityHandler {
	return &SecurityHandler{
		jwtSecret: []byte(jwtSecret),
		logger:    logger,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Validate JWT token
	token, err := jwt.Parse(t.Token, func(token *jwt.Token) (interface{}, error) {
		// Validate signing method
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, jwt.ErrSignatureInvalid
		}
		return s.jwtSecret, nil
	})

	if err != nil {
		s.logger.Warn("Invalid JWT token", zap.Error(err))
		return ctx, err
	}

	if !token.Valid {
		s.logger.Warn("Token validation failed")
		return ctx, jwt.ErrSignatureInvalid
	}

	// Extract claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.Warn("Invalid token claims")
		return ctx, jwt.ErrInvalidKey
	}

	// Extract user information
	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		s.logger.Warn("Missing user_id in token")
		return ctx, jwt.ErrInvalidKey
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.logger.Warn("Invalid user_id format", zap.Error(err))
		return ctx, jwt.ErrInvalidKey
	}

	// Set user type to "player" for abilities
	userType := "player"

	// Add user information to context
	ctx = models.SetUserInContext(ctx, userID, userType)

	s.logger.Info("User authenticated",
		zap.String("user_id", userID.String()),
		zap.String("user_type", userType),
		zap.String("operation", string(operationName)))

	return ctx, nil
}

// ValidateAbilityActivation validates if ability can be activated
func (s *Service) ValidateAbilityActivation(ctx context.Context, playerID, abilityID string) error {
	// PERFORMANCE: Add context timeout for ability validation operations
	timeoutCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Check if ability exists
	ability, err := s.repo.GetAbilityByID(timeoutCtx, abilityID)
	if err != nil {
		return err
	}
	if ability == nil {
		return fmt.Errorf("ability not found")
	}

	// Check cooldown
	cooldown, err := s.repo.GetAbilityCooldown(timeoutCtx, playerID, abilityID)
	if err != nil {
		return err
	}
	if cooldown != nil {
		return fmt.Errorf("ability on cooldown until %s", cooldown.ExpiresAt)
	}

	// Check ability requirements and player resources
	ability, err := s.repo.GetAbilityByID(timeoutCtx, abilityID)
	if err != nil {
		return fmt.Errorf("failed to get ability: %w", err)
	}

	// Check player resources
	playerResources, err := s.repo.GetPlayerResources(timeoutCtx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get player resources: %w", err)
	}

	// Check mana cost
	if ability.ManaCost != nil && playerResources.Mana < *ability.ManaCost {
		return fmt.Errorf("insufficient mana: has %d, needs %d", playerResources.Mana, *ability.ManaCost)
	}

	// Check ability requirements
	if ability.Requirements != nil {
		playerStats, err := s.repo.GetPlayerStats(timeoutCtx, playerID)
		if err != nil {
			return fmt.Errorf("failed to get player stats: %w", err)
		}

		// Check minimum level
		if ability.Requirements.MinLevel != nil && playerStats.Level < *ability.Requirements.MinLevel {
			return fmt.Errorf("insufficient level: has %d, needs %d", playerStats.Level, *ability.Requirements.MinLevel)
		}

		// Check required class
		if ability.Requirements.RequiredClass != nil && playerStats.Class != *ability.Requirements.RequiredClass {
			return fmt.Errorf("wrong class: has %s, needs %s", playerStats.Class, *ability.Requirements.RequiredClass)
		}

		// Check required skills
		if len(ability.Requirements.RequiredSkills) > 0 {
			playerSkillMap := make(map[string]bool)
			for _, skill := range playerStats.Skills {
				playerSkillMap[skill] = true
			}

			for _, requiredSkill := range ability.Requirements.RequiredSkills {
				if !playerSkillMap[requiredSkill] {
					return fmt.Errorf("missing required skill: %s", requiredSkill)
				}
			}
		}
	}

	return nil
}

// ActivateAbility activates an ability for a player
func (s *Service) ActivateAbility(ctx context.Context, playerID, abilityID string) error {
	// PERFORMANCE: Add context timeout for ability activation operations
	timeoutCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Validate activation
	if err := s.ValidateAbilityActivation(timeoutCtx, playerID, abilityID); err != nil {
		return err
	}

	// Get ability details
	ability, err := s.repo.GetAbilityByID(timeoutCtx, abilityID)
	if err != nil {
		return err
	}

	// Set cooldown
	expiresAt := time.Now().Add(time.Duration(ability.Cooldown) * time.Millisecond)
	cooldown := &repository.AbilityCooldown{
		PlayerID:  playerID,
		AbilityID: abilityID,
		ExpiresAt: expiresAt.Format(time.RFC3339),
	}

	if err := s.repo.SetAbilityCooldown(timeoutCtx, cooldown); err != nil {
		return err
	}

	// Deduct player resources
	manaCost := 0
	if ability.ManaCost != nil {
		manaCost = *ability.ManaCost
	}

	if err := s.repo.UpdatePlayerResources(timeoutCtx, playerID, manaCost, 0); err != nil {
		s.logger.Error("Failed to deduct player resources after ability activation",
			zap.String("player_id", playerID),
			zap.String("ability_id", abilityID),
			zap.Error(err))
		// Don't fail the activation if resource update fails (to avoid inconsistent state)
		// But log the error for monitoring
	}

	s.logger.Info("Ability activated successfully",
		zap.String("player_id", playerID),
		zap.String("ability_id", abilityID),
		zap.Int("mana_cost", manaCost))

	return nil
}