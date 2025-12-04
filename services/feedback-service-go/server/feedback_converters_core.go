// Issue: ogen migration
package server

import (
	"net/url"

	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
)

func convertSubmitFeedbackRequestFromAPI(req *feedbackapi.SubmitFeedbackRequest) *models.SubmitFeedbackRequest {
	result := &models.SubmitFeedbackRequest{
		Type:        convertFeedbackTypeFromAPI(req.Type),
		Category:    convertFeedbackCategoryFromAPI(req.Category),
		Title:       req.Title,
		Description: req.Description,
	}

	if req.Priority.Set {
		p := convertFeedbackPriorityFromAPI(req.Priority.Value)
		result.Priority = &p
	}

	if req.GameContext.Set {
		result.GameContext = convertGameContextFromAPI(&req.GameContext.Value)
	}

	if len(req.Screenshots) > 0 {
		screenshots := make([]string, len(req.Screenshots))
		for i, url := range req.Screenshots {
			screenshots[i] = url.String()
		}
		result.Screenshots = screenshots
	}

	return result
}

func convertSubmitFeedbackResponseToAPI(resp *models.SubmitFeedbackResponse) feedbackapi.SubmitFeedbackResponse {
	status := convertFeedbackStatusToSubmitFeedbackResponseStatus(resp.Status)
	result := feedbackapi.SubmitFeedbackResponse{
		ID:        feedbackapi.NewOptUUID(resp.ID),
		Status:    feedbackapi.NewOptSubmitFeedbackResponseStatus(status),
		CreatedAt: feedbackapi.NewOptDateTime(resp.CreatedAt),
	}

	if resp.GithubIssueNumber != nil {
		result.GithubIssueNumber = feedbackapi.NewOptNilInt(*resp.GithubIssueNumber)
	}

	if resp.GithubIssueURL != nil {
		url, _ := url.Parse(*resp.GithubIssueURL)
		if url != nil {
			result.GithubIssueURL = feedbackapi.NewOptNilURI(*url)
		}
	}

	return result
}

func convertFeedbackToAPI(feedback *models.Feedback) feedbackapi.Feedback {
	result := feedbackapi.Feedback{
		ID:          feedbackapi.NewOptUUID(feedback.ID),
		PlayerID:    feedbackapi.NewOptUUID(feedback.PlayerID),
		Type:        feedbackapi.NewOptFeedbackType(convertFeedbackTypeToAPIValue(feedback.Type)),
		Category:    feedbackapi.NewOptFeedbackCategory(convertFeedbackCategoryToAPIValue(feedback.Category)),
		Title:       feedbackapi.NewOptString(feedback.Title),
		Description: feedbackapi.NewOptString(feedback.Description),
		Status:      feedbackapi.NewOptFeedbackStatus(convertFeedbackStatusToAPIValue(feedback.Status)),
		CreatedAt:   feedbackapi.NewOptDateTime(feedback.CreatedAt),
		UpdatedAt:   feedbackapi.NewOptDateTime(feedback.UpdatedAt),
	}

	if feedback.GithubIssueNumber != nil {
		result.GithubIssueNumber = feedbackapi.NewOptNilInt(*feedback.GithubIssueNumber)
	}

	if feedback.GithubIssueURL != nil {
		u, _ := url.Parse(*feedback.GithubIssueURL)
		if u != nil {
			result.GithubIssueURL = feedbackapi.NewOptNilURI(*u)
		}
	}

	return result
}

func convertFeedbackListToAPI(list *models.FeedbackList) feedbackapi.FeedbackListResponse {
	items := make([]feedbackapi.Feedback, len(list.Items))
	for i, item := range list.Items {
		items[i] = convertFeedbackToAPI(&item)
	}

	result := feedbackapi.FeedbackListResponse{
		Items: items,
		Total: feedbackapi.NewOptInt(list.Total),
	}

	return result
}

func convertUpdateStatusRequestFromAPI(req *feedbackapi.UpdateStatusRequest) *models.UpdateStatusRequest {
	result := &models.UpdateStatusRequest{
		Status: convertUpdateStatusRequestStatusFromAPI(req.Status),
	}

	if req.Comment.Set {
		result.Comment = &req.Comment.Value
	}

	return result
}

func convertGameContextToAPI(ctx *models.GameContext) *feedbackapi.GameContext {
	result := &feedbackapi.GameContext{
		GameVersion: feedbackapi.NewOptString(ctx.Version),
		ActiveQuests: ctx.ActiveQuests,
	}

	if ctx.Location != "" {
		result.Location = feedbackapi.NewOptString(ctx.Location)
	}

	if ctx.CharacterLevel != nil {
		result.Level = feedbackapi.NewOptInt(*ctx.CharacterLevel)
	}

	if ctx.PlaytimeHours != nil {
		seconds := int(*ctx.PlaytimeHours * 3600)
		result.Playtime = feedbackapi.NewOptInt(seconds)
	}

	return result
}

func convertGameContextFromAPI(ctx *feedbackapi.GameContext) *models.GameContext {
	result := &models.GameContext{}

	if ctx.GameVersion.Set {
		result.Version = ctx.GameVersion.Value
	}

	if ctx.Location.Set {
		result.Location = ctx.Location.Value
	}

	if ctx.Level.Set {
		result.CharacterLevel = &ctx.Level.Value
	}

	if len(ctx.ActiveQuests) > 0 {
		result.ActiveQuests = ctx.ActiveQuests
	}

	if ctx.Playtime.Set {
		hours := float64(ctx.Playtime.Value) / 3600.0
		result.PlaytimeHours = &hours
	}

	return result
}





