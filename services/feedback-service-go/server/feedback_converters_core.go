package server

import (
	"github.com/google/uuid"
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertSubmitFeedbackRequestFromAPI(req *feedbackapi.SubmitFeedbackRequest) *models.SubmitFeedbackRequest {
	result := &models.SubmitFeedbackRequest{
		Type:        convertFeedbackTypeFromAPI(req.Type),
		Category:    convertFeedbackCategoryFromAPI(req.Category),
		Title:       req.Title,
		Description: req.Description,
	}

	if req.Priority != nil {
		p := convertFeedbackPriorityFromAPI(*req.Priority)
		result.Priority = &p
	}

	if req.GameContext != nil {
		result.GameContext = convertGameContextFromAPI(req.GameContext)
	}

	if req.Screenshots != nil {
		result.Screenshots = *req.Screenshots
	}

	return result
}

func convertSubmitFeedbackResponseToAPI(resp *models.SubmitFeedbackResponse) feedbackapi.SubmitFeedbackResponse {
	status := convertFeedbackStatusToSubmitFeedbackResponseStatus(resp.Status)
	result := feedbackapi.SubmitFeedbackResponse{
		Id:                (*openapi_types.UUID)(&resp.ID),
		Status:            &status,
		CreatedAt:         &resp.CreatedAt,
	}

	if resp.GithubIssueNumber != nil {
		result.GithubIssueNumber = resp.GithubIssueNumber
	}

	if resp.GithubIssueURL != nil {
		result.GithubIssueUrl = resp.GithubIssueURL
	}

	return result
}

func convertFeedbackToAPI(feedback *models.Feedback) feedbackapi.Feedback {
	result := feedbackapi.Feedback{
		Id:                (*openapi_types.UUID)(&feedback.ID),
		PlayerId:          (*openapi_types.UUID)(&feedback.PlayerID),
		Type:              convertFeedbackTypeToAPI(feedback.Type),
		Category:          convertFeedbackCategoryToAPI(feedback.Category),
		Title:             &feedback.Title,
		Description:      &feedback.Description,
		Status:            convertFeedbackStatusToAPI(feedback.Status),
		CreatedAt:         &feedback.CreatedAt,
		UpdatedAt:         &feedback.UpdatedAt,
		VotesCount:        &feedback.VotesCount,
	}

	if feedback.Priority != nil {
		p := convertFeedbackPriorityToAPI(*feedback.Priority)
		result.Priority = &p
	}

	if feedback.GameContext != nil {
		result.GameContext = convertGameContextToAPI(feedback.GameContext)
	}

	if len(feedback.Screenshots) > 0 {
		result.Screenshots = &feedback.Screenshots
	}

	if feedback.GithubIssueNumber != nil {
		result.GithubIssueNumber = feedback.GithubIssueNumber
	}

	if feedback.GithubIssueURL != nil {
		result.GithubIssueUrl = feedback.GithubIssueURL
	}

	if feedback.MergedInto != nil {
		result.MergedInto = (*openapi_types.UUID)(feedback.MergedInto)
	}

	if feedback.ModerationStatus != nil {
		ms := convertModerationStatusToAPI(*feedback.ModerationStatus)
		result.ModerationStatus = &ms
	}

	if feedback.ModerationReason != nil {
		result.ModerationReason = feedback.ModerationReason
	}

	return result
}

func convertFeedbackListToAPI(list *models.FeedbackList) feedbackapi.FeedbackList {
	result := feedbackapi.FeedbackList{
		Total:  &list.Total,
		Limit:  &list.Limit,
		Offset: &list.Offset,
	}

	items := make([]feedbackapi.Feedback, len(list.Items))
	for i, item := range list.Items {
		items[i] = convertFeedbackToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertUpdateStatusRequestFromAPI(req *feedbackapi.UpdateStatusRequest) *models.UpdateStatusRequest {
	result := &models.UpdateStatusRequest{
		Status: convertUpdateStatusRequestStatusFromAPI(req.Status),
	}

	if req.GithubIssueNumber != nil {
		result.GithubIssueNumber = req.GithubIssueNumber
	}

	if req.GithubIssueUrl != nil {
		result.GithubIssueURL = req.GithubIssueUrl
	}

	return result
}

func convertGameContextToAPI(ctx *models.GameContext) *feedbackapi.GameContext {
	result := &feedbackapi.GameContext{
		Version: &ctx.Version,
	}

	if ctx.Location != "" {
		result.Location = &ctx.Location
	}

	if ctx.CharacterLevel != nil {
		result.CharacterLevel = ctx.CharacterLevel
	}

	if len(ctx.ActiveQuests) > 0 {
		result.ActiveQuests = &ctx.ActiveQuests
	}

	if ctx.PlaytimeHours != nil {
		hours := float32(*ctx.PlaytimeHours)
		result.PlaytimeHours = &hours
	}

	return result
}

func convertGameContextFromAPI(ctx *feedbackapi.GameContext) *models.GameContext {
	result := &models.GameContext{}

	if ctx.Version != nil {
		result.Version = *ctx.Version
	}

	if ctx.Location != nil {
		result.Location = *ctx.Location
	}

	if ctx.CharacterLevel != nil {
		result.CharacterLevel = ctx.CharacterLevel
	}

	if ctx.ActiveQuests != nil {
		result.ActiveQuests = *ctx.ActiveQuests
	}

	if ctx.PlaytimeHours != nil {
		hours := float64(*ctx.PlaytimeHours)
		result.PlaytimeHours = &hours
	}

	return result
}

