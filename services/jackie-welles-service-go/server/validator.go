// Request validation for Jackie Welles NPC service
// Issue: #1905
// PERFORMANCE: Fast validation with minimal allocations

package server

import (
	"context"
	"errors"
	"fmt"
	"regexp"
	"strings"

	"github.com/gc-lover/necpgame-monorepo/services/jackie-welles-service-go/pkg/api"
	"github.com/google/uuid"
)

// Validator handles request validation
type Validator struct{}

// NewValidator creates a new validator instance
func NewValidator() *Validator {
	return &Validator{}
}

// ValidateQuestAcceptance validates quest acceptance request
func (v *Validator) ValidateQuestAcceptance(ctx context.Context, questID uuid.UUID, playerID uuid.UUID) error {
	if questID == uuid.Nil {
		return errors.New("quest_id cannot be empty")
	}

	if playerID == uuid.Nil {
		return errors.New("player_id cannot be empty")
	}

	// Additional validation could check if quest exists, if player can accept it, etc.
	return nil
}

// ValidateTradeRequest validates trade request
func (v *Validator) ValidateTradeRequest(ctx context.Context, req *api.TradeRequest) error {
	if req == nil {
		return errors.New("trade request cannot be nil")
	}

	items := req.Items
	if len(items) == 0 {
		return errors.New("trade request must include at least one item")
	}

	totalAmount := req.TotalAmount.GetOrZero()
	if totalAmount < 0 {
		return errors.New("total amount cannot be negative")
	}

	tradeType := req.TradeType.GetOrZero()
	if tradeType != "buy" && tradeType != "sell" {
		return errors.New("trade_type must be 'buy' or 'sell'")
	}

	// Validate item IDs are valid UUIDs
	for _, itemID := range items {
		if _, err := uuid.Parse(itemID); err != nil {
			return fmt.Errorf("invalid item ID format: %s", itemID)
		}
	}

	return nil
}

// ValidateDialogueResponse validates dialogue response
func (v *Validator) ValidateDialogueResponse(ctx context.Context, req *api.DialogueResponseRequest, dialogueID uuid.UUID) error {
	if req == nil {
		return errors.New("dialogue response request cannot be nil")
	}

	if dialogueID == uuid.Nil {
		return errors.New("dialogue_id cannot be empty")
	}

	response := req.Response.GetOrZero()
	if response == "" {
		return errors.New("response cannot be empty")
	}

	responseType := req.ResponseType.GetOrZero()
	validTypes := []string{"positive", "negative", "neutral", "question"}
	if !v.isValidResponseType(responseType, validTypes) {
		return fmt.Errorf("invalid response_type: %s", responseType)
	}

	return nil
}

// ValidateDialogueStart validates dialogue start request
func (v *Validator) ValidateDialogueStart(ctx context.Context, req *api.DialogueStartRequest) error {
	if req == nil {
		return errors.New("dialogue start request cannot be nil")
	}

	playerID := req.PlayerID.GetOrZero()
	if playerID == "" {
		return errors.New("player_id cannot be empty")
	}

	if _, err := uuid.Parse(playerID); err != nil {
		return errors.New("invalid player_id format")
	}

	context := req.Context.GetOrZero()
	if len(context) > 1000 {
		return errors.New("context cannot exceed 1000 characters")
	}

	return nil
}

// ValidateInteractionRequest validates interaction request
func (v *Validator) ValidateInteractionRequest(ctx context.Context, req *api.InteractionRequest) error {
	if req == nil {
		return errors.New("interaction request cannot be nil")
	}

	interactionType := req.InteractionType.GetOrZero()
	validTypes := []string{"dialogue", "trade", "quest", "help"}
	if !v.isValidResponseType(interactionType, validTypes) {
		return fmt.Errorf("invalid interaction_type: %s", interactionType)
	}

	playerID := req.PlayerID.GetOrZero()
	if playerID == "" {
		return errors.New("player_id cannot be empty")
	}

	if _, err := uuid.Parse(playerID); err != nil {
		return errors.New("invalid player_id format")
	}

	return nil
}

// ValidateRelationshipUpdate validates relationship update request
func (v *Validator) ValidateRelationshipUpdate(ctx context.Context, req *api.UpdateRelationshipRequest) error {
	if req == nil {
		return errors.New("relationship update request cannot be nil")
	}

	playerID := req.PlayerID.GetOrZero()
	if playerID == "" {
		return errors.New("player_id cannot be empty")
	}

	if _, err := uuid.Parse(playerID); err != nil {
		return errors.New("invalid player_id format")
	}

	actionType := req.ActionType.GetOrZero()
	validActions := []string{"quest_completed", "trade", "dialogue_positive", "dialogue_negative", "help_received"}
	if !v.isValidResponseType(actionType, validActions) {
		return fmt.Errorf("invalid action_type: %s", actionType)
	}

	changeAmount := req.ChangeAmount.GetOrZero()
	if changeAmount < -100 || changeAmount > 100 {
		return errors.New("change_amount must be between -100 and 100")
	}

	return nil
}

// ValidatePlayerID validates player ID format
func (v *Validator) ValidatePlayerID(playerID string) error {
	if playerID == "" {
		return errors.New("player_id cannot be empty")
	}

	if _, err := uuid.Parse(playerID); err != nil {
		return errors.New("invalid player_id format")
	}

	return nil
}

// ValidateItemID validates item ID format
func (v *Validator) ValidateItemID(itemID string) error {
	if itemID == "" {
		return errors.New("item_id cannot be empty")
	}

	if _, err := uuid.Parse(itemID); err != nil {
		return errors.New("invalid item_id format")
	}

	return nil
}

// ValidateTextInput validates text input for XSS and length
func (v *Validator) ValidateTextInput(text string, maxLength int) error {
	if len(text) > maxLength {
		return fmt.Errorf("text exceeds maximum length of %d characters", maxLength)
	}

	// Simple XSS check - look for script tags
	if strings.Contains(strings.ToLower(text), "<script") {
		return errors.New("text contains potentially malicious content")
	}

	// Check for suspicious patterns
	suspiciousPatterns := []string{"javascript:", "onload=", "onerror="}
	for _, pattern := range suspiciousPatterns {
		if strings.Contains(strings.ToLower(text), pattern) {
			return errors.New("text contains potentially malicious content")
		}
	}

	return nil
}

// ValidateEnum validates that value is in allowed enum values
func (v *Validator) ValidateEnum(value string, allowed []string) error {
	for _, allowedValue := range allowed {
		if value == allowedValue {
			return nil
		}
	}
	return fmt.Errorf("value '%s' not in allowed values: %v", value, allowed)
}

// isValidResponseType checks if response type is valid
func (v *Validator) isValidResponseType(responseType string, validTypes []string) bool {
	for _, validType := range validTypes {
		if responseType == validType {
			return true
		}
	}
	return false
}

// ValidateEmail validates email format
func (v *Validator) ValidateEmail(email string) error {
	emailRegex := regexp.MustCompile(`^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$`)
	if !emailRegex.MatchString(email) {
		return errors.New("invalid email format")
	}
	return nil
}

// ValidatePhoneNumber validates phone number format
func (v *Validator) ValidatePhoneNumber(phone string) error {
	phoneRegex := regexp.MustCompile(`^\+?[1-9]\d{1,14}$`)
	if !phoneRegex.MatchString(phone) {
		return errors.New("invalid phone number format")
	}
	return nil
}

// SanitizeInput sanitizes user input
func (v *Validator) SanitizeInput(input string) string {
	// Remove potentially dangerous characters
	input = strings.ReplaceAll(input, "<", "&lt;")
	input = strings.ReplaceAll(input, ">", "&gt;")
	input = strings.ReplaceAll(input, "\"", "&quot;")
	input = strings.ReplaceAll(input, "'", "&apos;")

	return strings.TrimSpace(input)
}
