// Issue: #2241
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// LegendTemplatesHandler implements the generated ServerInterface
type LegendTemplatesHandler struct {
	service *LegendTemplatesService
}

// NewLegendTemplatesHandler creates a new handler instance
func NewLegendTemplatesHandler(service *LegendTemplatesService) *LegendTemplatesHandler {
	return &LegendTemplatesHandler{
		service: service,
	}
}

// GetHealth implements health check endpoint
func (h *LegendTemplatesHandler) GetHealth(ctx context.Context) (api.GetHealthRes, error) {
	return h.service.HealthCheck(ctx)
}

// GetTemplates implements GET /templates
func (h *LegendTemplatesHandler) GetTemplates(ctx context.Context, params api.GetTemplatesParams) (api.GetTemplatesRes, error) {
	response, err := h.service.repo.GetTemplates(ctx, params)
	if err != nil {
		return &api.BadRequest{
			Error:   api.NewOptString("DATABASE_ERROR"),
			Message: api.NewOptString("Failed to retrieve templates"),
		}, nil
	}
	return response, nil
}

// CreateTemplate implements POST /templates
func (h *LegendTemplatesHandler) CreateTemplate(ctx context.Context, req *api.CreateTemplateRequest) (api.CreateTemplateRes, error) {
	template, err := h.service.repo.CreateTemplate(ctx, req)
	if err != nil {
		return &api.BadRequest{
			Error:   api.NewOptString("VALIDATION_ERROR"),
			Message: api.NewOptString("Invalid template data"),
		}, nil
	}

	// Invalidate cache after template creation
	h.service.cache.InvalidateCache()

	return &api.TemplateResponse{Template: api.NewOptStoryTemplate(*template)}, nil
}

// GetTemplate implements GET /templates/{template_id}
func (h *LegendTemplatesHandler) GetTemplate(ctx context.Context, params api.GetTemplateParams) (api.GetTemplateRes, error) {
	template, err := h.service.repo.GetTemplateByID(ctx, params.TemplateID.String())
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}
	return &api.TemplateResponse{Template: api.NewOptStoryTemplate(*template)}, nil
}

// UpdateTemplate implements PUT /templates/{template_id}
func (h *LegendTemplatesHandler) UpdateTemplate(ctx context.Context, req *api.UpdateTemplateRequest, params api.UpdateTemplateParams) (api.UpdateTemplateRes, error) {
	template, err := h.service.repo.UpdateTemplate(ctx, params.TemplateID.String(), req)
	if err != nil {
		return &api.BadRequest{
			Error:   api.NewOptString("VALIDATION_ERROR"),
			Message: api.NewOptString("Invalid update data"),
		}, nil
	}

	// Invalidate cache after template update
	h.service.cache.InvalidateCache()

	return &api.TemplateResponse{Template: api.NewOptStoryTemplate(*template)}, nil
}

// DeleteTemplate implements DELETE /templates/{template_id}
func (h *LegendTemplatesHandler) DeleteTemplate(ctx context.Context, params api.DeleteTemplateParams) (api.DeleteTemplateRes, error) {
	err := h.service.repo.DeleteTemplate(ctx, params.TemplateID.String())
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}

	// Invalidate cache after template deletion
	h.service.cache.InvalidateCache()

	return &api.DeleteTemplateNoContent{}, nil
}

// GetTemplateVariants implements GET /templates/{template_id}/variants
func (h *LegendTemplatesHandler) GetTemplateVariants(ctx context.Context, params api.GetTemplateVariantsParams) (api.GetTemplateVariantsRes, error) {
	templateID := params.TemplateID.String()

	// Get template first to check existence
	template, err := h.service.repo.GetTemplateByID(ctx, templateID)
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}

	// Convert variants to proper response format
	var variants []api.TemplateVariant
	for i, variantText := range template.Variants {
		variants = append(variants, api.TemplateVariant{
			ID:          uuid.New(), // Generate ID for response
			TemplateID:  params.TemplateID,
			VariantText: variantText,
			Weight:      api.NewOptInt(1),
			Active:      api.NewOptBool(true),
			CreatedAt:   api.NewOptDateTime(time.Now()),
		})
		_ = i // Avoid unused variable warning
	}

	response := &api.TemplateVariantsResponse{
		Variants:   variants,
		TemplateID: api.NewOptUUID(params.TemplateID),
	}

	return response, nil
}

// AddTemplateVariant implements POST /templates/{template_id}/variants
func (h *LegendTemplatesHandler) AddTemplateVariant(ctx context.Context, req *api.CreateVariantRequest, params api.AddTemplateVariantParams) (api.AddTemplateVariantRes, error) {
	templateID := params.TemplateID.String()

	// Verify template exists
	_, err := h.service.repo.GetTemplateByID(ctx, templateID)
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}

	// Create variant response (in production, would save to database)
	variant := &api.TemplateVariant{
		ID:          uuid.New(),
		TemplateID:  params.TemplateID,
		VariantText: req.VariantText, // Already string
		Weight:      req.Weight,
		Active:      api.NewOptBool(true),
		CreatedAt:   api.NewOptDateTime(time.Now()),
	}

	return &api.VariantResponse{Variant: api.NewOptTemplateVariant(*variant)}, nil
}

// UpdateTemplateVariant implements PUT /templates/{template_id}/variants/{variant_id}
func (h *LegendTemplatesHandler) UpdateTemplateVariant(ctx context.Context, req *api.UpdateVariantRequest, params api.UpdateTemplateVariantParams) (api.UpdateTemplateVariantRes, error) {
	templateID := params.TemplateID.String()

	// Verify template exists
	_, err := h.service.repo.GetTemplateByID(ctx, templateID)
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}

	variantText := req.VariantText.Value // Default value
	if req.VariantText.Set {
		variantText = req.VariantText.Value
	}

	// Create updated variant response
	variant := &api.TemplateVariant{
		ID:          params.VariantID,
		TemplateID:  params.TemplateID,
		VariantText: variantText,
		Weight:      req.Weight,
		Active:      req.Active,
		CreatedAt:   api.NewOptDateTime(time.Now()),
	}

	return &api.VariantResponse{Variant: api.NewOptTemplateVariant(*variant)}, nil
}

// DeleteTemplateVariant implements DELETE /templates/{template_id}/variants/{variant_id}
func (h *LegendTemplatesHandler) DeleteTemplateVariant(ctx context.Context, params api.DeleteTemplateVariantParams) (api.DeleteTemplateVariantRes, error) {
	templateID := params.TemplateID.String()

	// Verify template exists
	_, err := h.service.repo.GetTemplateByID(ctx, templateID)
	if err != nil {
		return &api.NotFound{
			Error:   api.NewOptString("not_found"),
			Message: api.NewOptString("Template not found"),
		}, nil
	}

	return &api.DeleteTemplateVariantNoContent{}, nil
}

// GetVariables implements GET /variables
func (h *LegendTemplatesHandler) GetVariables(ctx context.Context, params api.GetVariablesParams) (api.GetVariablesRes, error) {
	response, err := h.service.repo.GetVariables(ctx, params)
	if err != nil {
		return &api.InternalServerError{
			Error:   api.NewOptString("internal_error"),
			Message: api.NewOptString("Failed to retrieve variables"),
		}, nil
	}
	return response, nil
}

// CreateVariable implements POST /variables
func (h *LegendTemplatesHandler) CreateVariable(ctx context.Context, req *api.CreateVariableRequest) (api.CreateVariableRes, error) {
	variable, err := h.service.repo.CreateVariable(ctx, req)
	if err != nil {
		return &api.BadRequest{
			Error:   api.NewOptString("VALIDATION_ERROR"),
			Message: api.NewOptString("Invalid variable data"),
		}, nil
	}
	return &api.VariableResponse{Variable: api.NewOptVariableRule(*variable)}, nil
}

// GetVariable implements GET /variables/{variable_id}
func (h *LegendTemplatesHandler) GetVariable(ctx context.Context, params api.GetVariableParams) (api.GetVariableRes, error) {
	// Mock implementation - in production would query database
	rules := api.VariableRuleRules{
		Synonyms: []string{"character", "mercenary"},
	}

	variable := &api.VariableRule{
		ID:        params.VariableID,
		Type:      "player_name",
		Name:      "player_name",
		Rules:     rules,
		Active:    api.NewOptBool(true),
		CreatedAt: api.NewOptDateTime(time.Now()),
		UpdatedAt: api.NewOptDateTime(time.Now()),
	}

	return &api.VariableResponse{Variable: api.NewOptVariableRule(*variable)}, nil
}

// UpdateVariable implements PUT /variables/{variable_id}
func (h *LegendTemplatesHandler) UpdateVariable(ctx context.Context, req *api.UpdateVariableRequest, params api.UpdateVariableParams) (api.UpdateVariableRes, error) {
	rules := api.VariableRuleRules{
		Synonyms: []string{"character", "mercenary"}, // Default rules
	}

	variable := &api.VariableRule{
		ID:        params.VariableID,
		Type:      "player_name",
		Name:      "player_name",
		Rules:     rules,
		Active:    req.Active,
		CreatedAt: api.NewOptDateTime(time.Now()),
		UpdatedAt: api.NewOptDateTime(time.Now()),
	}

	return &api.VariableResponse{Variable: api.NewOptVariableRule(*variable)}, nil
}

// DeleteVariable implements DELETE /variables/{variable_id}
func (h *LegendTemplatesHandler) DeleteVariable(ctx context.Context, params api.DeleteVariableParams) (api.DeleteVariableRes, error) {
	return &api.DeleteVariableNoContent{}, nil
}

// GenerateLegend implements POST /generate (HOT PATH)
func (h *LegendTemplatesHandler) GenerateLegend(ctx context.Context, req *api.GenerateLegendRequest) (api.GenerateLegendRes, error) {
	response, err := h.service.GenerateLegend(ctx, req)
	if err != nil {
		return &api.BadRequest{
			Error:   api.NewOptString("GENERATION_ERROR"),
			Message: api.NewOptString("Failed to generate legend"),
		}, nil
	}
	return response, nil
}

// GetActiveTemplates implements GET /templates/active (HOT PATH)
func (h *LegendTemplatesHandler) GetActiveTemplates(ctx context.Context, params api.GetActiveTemplatesParams) (api.GetActiveTemplatesRes, error) {
	var eventType *string
	if params.Type.Set {
		eventTypeStr := string(params.Type.Value)
		eventType = &eventTypeStr
	}
	return h.service.GetActiveTemplates(ctx, eventType)
}

// ValidateTemplate implements POST /validate
func (h *LegendTemplatesHandler) ValidateTemplate(ctx context.Context, req *api.ValidateTemplateRequest) (api.ValidateTemplateRes, error) {
	// Basic validation - check required fields
	template := req.Template

	if template.ID == uuid.Nil {
		return &api.ValidationError{
			Valid:  api.NewOptBool(false),
			Errors: []string{"template ID is required"},
		}, nil
	}

	if template.BaseTemplate == "" {
		return &api.ValidationError{
			Valid:  api.NewOptBool(false),
			Errors: []string{"base_template is required"},
		}, nil
	}

	if len(template.Variables) == 0 {
		return &api.ValidationError{
			Valid:  api.NewOptBool(false),
			Errors: []string{"at least one variable is required"},
		}, nil
	}

	return &api.ValidationResponse{
		Valid:  api.NewOptBool(true),
		Errors: []string{},
	}, nil
}
