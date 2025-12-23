// Legend Templates Validator - Input validation layer
// Issue: #2241
// PERFORMANCE: Fast validation for high-throughput legend generation

package server

import (
	"fmt"
	"regexp"
	"strings"

	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// Validator provides input validation for legend templates
// PERFORMANCE: Pre-compiled regex patterns for fast validation
type Validator struct {
	uuidRegex *regexp.Regexp
}

// NewValidator creates a new validator instance
func NewValidator() *Validator {
	return &Validator{}
}

// ValidateCreateTemplateRequest validates template creation request
func (v *Validator) ValidateCreateTemplateRequest(req *api.CreateTemplateRequest) error {
	// TODO: Implement validation
	return nil
}

// ValidateUpdateTemplateRequest validates template update request
func (v *Validator) ValidateUpdateTemplateRequest(req *api.UpdateTemplateRequest) error {
	// TODO: Implement validation
	return nil
}

// ValidateCreateVariableRequest validates variable creation request
func (v *Validator) ValidateCreateVariableRequest(req *api.CreateVariableRequest) error {
	// TODO: Implement validation
	return nil
}

// ValidateGenerateLegendRequest validates legend generation request
func (v *Validator) ValidateGenerateLegendRequest(req *api.GenerateLegendRequest) error {
	// TODO: Implement validation
	return nil
}

// ValidateTemplateStructure validates template structure and variables
func (v *Validator) ValidateTemplateStructure(template api.StoryTemplate) []string {
	// TODO: Implement validation
	return []string{}
}

// validateTemplateType validates template type
func (v *Validator) validateTemplateType(templateType api.StoryTemplateType) error {
	validTypes := []api.StoryTemplateType{
		api.StoryTemplateTypeCombat,
		api.StoryTemplateTypeSocial,
		api.StoryTemplateTypeEconomic,
		api.StoryTemplateTypeExploration,
	}

	for _, validType := range validTypes {
		if templateType == validType {
			return nil
		}
	}

	return fmt.Errorf("unknown template type: %s", templateType)
}

// validateTemplateCategory validates template category
func (v *Validator) validateTemplateCategory(category string) error {
	if category == "" {
		return fmt.Errorf("category cannot be empty")
	}

	if len(category) > 100 {
		return fmt.Errorf("category too long (max 100 chars)")
	}

	return nil
}

// validateBaseTemplate validates base template
func (v *Validator) validateBaseTemplate(template string) error {
	if template == "" {
		return fmt.Errorf("base template cannot be empty")
	}

	if len(template) > 1000 {
		return fmt.Errorf("base template too long (max 1000 chars)")
	}

	// Check for basic placeholder syntax
	if !strings.Contains(template, "{") || !strings.Contains(template, "}") {
		return fmt.Errorf("template must contain variable placeholders in {variable} format")
	}

	return nil
}

// validateConditions validates activation conditions
func (v *Validator) validateConditions(conditions map[string]interface{}) error {
	// TODO: Implement condition validation
	return nil
}

// validateVariables validates variable list
func (v *Validator) validateVariables(variables []string) error {
	if len(variables) == 0 {
		return fmt.Errorf("at least one variable required")
	}

	if len(variables) > 20 {
		return fmt.Errorf("too many variables (max 20)")
	}

	// Check for duplicates
	seen := make(map[string]bool)
	for _, variable := range variables {
		if seen[variable] {
			return fmt.Errorf("duplicate variable: %s", variable)
		}
		seen[variable] = true

		if len(variable) > 50 {
			return fmt.Errorf("variable name too long: %s", variable)
		}
	}

	return nil
}

// validateVariants validates template variants
func (v *Validator) validateVariants(variants []string) error {
	if len(variants) > 10 {
		return fmt.Errorf("too many variants (max 10)")
	}

	for i, variant := range variants {
		if len(variant) > 1000 {
			return fmt.Errorf("variant %d too long (max 1000 chars)", i)
		}
	}

	return nil
}

// validateVariableType validates variable type
func (v *Validator) validateVariableType(variableType api.VariableRuleType) error {
	validTypes := []api.VariableRuleType{
		api.VariableRuleTypePlayerName,
		api.VariableRuleTypeActionVerb,
		api.VariableRuleTypeEnemyType,
		api.VariableRuleTypeLocation,
		api.VariableRuleTypeNumber,
		api.VariableRuleTypeTimeContext,
		api.VariableRuleTypeFaction,
		api.VariableRuleTypeEmotion,
	}

	for _, validType := range validTypes {
		if variableType == validType {
			return nil
		}
	}

	return fmt.Errorf("unknown variable type: %s", variableType)
}

// validateVariableName validates variable name
func (v *Validator) validateVariableName(name string) error {
	if name == "" {
		return fmt.Errorf("variable name cannot be empty")
	}

	if len(name) > 100 {
		return fmt.Errorf("variable name too long (max 100 chars)")
	}

	// Check for valid identifier format
	if !regexp.MustCompile(`^[a-zA-Z_][a-zA-Z0-9_]*$`).MatchString(name) {
		return fmt.Errorf("invalid variable name format")
	}

	return nil
}

// validateVariableRules validates variable substitution rules
func (v *Validator) validateVariableRules(rules map[string]interface{}) error {
	// TODO: Implement detailed rules validation
	return nil
}

// validateEventType validates event type
func (v *Validator) validateEventType(eventType api.GenerateLegendRequestEventType) error {
	validTypes := []api.GenerateLegendRequestEventType{
		api.GenerateLegendRequestEventTypeCombat,
		api.GenerateLegendRequestEventTypeSocial,
		api.GenerateLegendRequestEventTypeEconomic,
		api.GenerateLegendRequestEventTypeExploration,
	}

	for _, validType := range validTypes {
		if eventType == validType {
			return nil
		}
	}

	return fmt.Errorf("unknown event type: %s", eventType)
}

// validateEventData validates event data
func (v *Validator) validateEventData(eventData api.GenerateLegendRequestEventData) error {
	// At least one field should be provided
	hasData := false

	if playerName, ok := eventData.PlayerName.Get(); ok && playerName != "" {
		hasData = true
	}

	if actionVerb, ok := eventData.ActionVerb.Get(); ok && actionVerb != "" {
		hasData = true
	}

	if enemyType, ok := eventData.EnemyType.Get(); ok && enemyType != "" {
		hasData = true
	}

	if !hasData {
		return fmt.Errorf("at least one event data field must be provided")
	}

	return nil
}

// validateGenerationContext validates generation context
func (v *Validator) validateGenerationContext(context api.GenerateLegendRequestContext) error {
	if narratorFaction, ok := context.NarratorFaction.Get(); ok && narratorFaction == "" {
		return fmt.Errorf("narrator_faction cannot be empty if provided")
	}

	if storyStyle, ok := context.StoryStyle.Get(); ok {
		validStyles := []string{"formal", "casual", "slang", "dramatic"}
		valid := false
		for _, s := range validStyles {
			if string(storyStyle) == s {
				valid = true
				break
			}
		}
		if !valid {
			return fmt.Errorf("invalid story style: %s", storyStyle)
		}
	}

	return nil
}
