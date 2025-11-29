package server

import (
	"github.com/google/uuid"
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertFeedbackBoardListToAPI(list *models.FeedbackBoardList) feedbackapi.FeedbackBoardList {
	result := feedbackapi.FeedbackBoardList{
		Total:  &list.Total,
		Limit:  &list.Limit,
		Offset: &list.Offset,
	}

	items := make([]feedbackapi.FeedbackBoardItem, len(list.Items))
	for i, item := range list.Items {
		items[i] = convertFeedbackBoardItemToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertFeedbackBoardItemToAPI(item *models.FeedbackBoardItem) feedbackapi.FeedbackBoardItem {
	itemType := convertFeedbackTypeToFeedbackBoardItemType(item.Type)
	category := convertFeedbackCategoryToFeedbackBoardItemCategory(item.Category)
	status := convertFeedbackStatusToFeedbackBoardItemStatus(item.Status)
	result := feedbackapi.FeedbackBoardItem{
		Id:         (*openapi_types.UUID)(&item.ID),
		Type:       &itemType,
		Category:   &category,
		Title:      &item.Title,
		Description: &item.Description,
		Status:     &status,
		VotesCount: &item.VotesCount,
		CreatedAt:  &item.CreatedAt,
	}

	if item.GithubIssueNumber != nil {
		result.GithubIssueNumber = item.GithubIssueNumber
	}

	if item.GithubIssueURL != nil {
		result.GithubIssueUrl = item.GithubIssueURL
	}

	return result
}

func convertVoteResponseToAPI(resp *models.VoteResponse, feedbackID uuid.UUID) feedbackapi.VoteResponse {
	result := feedbackapi.VoteResponse{
		FeedbackId: (*openapi_types.UUID)(&feedbackID),
		VotesCount: &resp.VotesCount,
		HasVoted:   &resp.HasVoted,
	}

	return result
}

func convertFeedbackStatsToAPI(stats *models.FeedbackStats) feedbackapi.FeedbackStats {
	result := feedbackapi.FeedbackStats{
		Total: &stats.Total,
	}

	result.ByStatus = &struct {
		Approved *int `json:"approved,omitempty"`
		Closed   *int `json:"closed,omitempty"`
		InReview *int `json:"in_review,omitempty"`
		Merged   *int `json:"merged,omitempty"`
		Pending  *int `json:"pending,omitempty"`
		Rejected *int `json:"rejected,omitempty"`
	}{
		Approved: &stats.Approved,
		Closed:   &stats.Closed,
		InReview: &stats.InReview,
		Merged:   &stats.Merged,
		Pending:  &stats.Pending,
		Rejected: &stats.Rejected,
	}

	result.ByType = &struct {
		BugReport      *int `json:"bug_report,omitempty"`
		FeatureRequest *int `json:"feature_request,omitempty"`
		Feedback       *int `json:"feedback,omitempty"`
		Wishlist       *int `json:"wishlist,omitempty"`
	}{
		BugReport:      &stats.BugReports,
		FeatureRequest: &stats.FeatureRequests,
		Feedback:       &stats.Feedback,
		Wishlist:       &stats.Wishlist,
	}

	result.ByCategory = &struct {
		Balance   *int `json:"balance,omitempty"`
		Content   *int `json:"content,omitempty"`
		Gameplay  *int `json:"gameplay,omitempty"`
		Lore      *int `json:"lore,omitempty"`
		Other     *int `json:"other,omitempty"`
		Technical *int `json:"technical,omitempty"`
		UiUx      *int `json:"ui_ux,omitempty"`
	}{}

	totalVotes := 0
	if stats.Total > 0 {
		avgVotes := float32(totalVotes) / float32(stats.Total)
		result.AverageVotesPerFeedback = &avgVotes
	}

	return result
}

func convertSortFromAPI(s feedbackapi.GetFeedbackBoardParamsSort) string {
	switch s {
	case feedbackapi.CreatedAsc:
		return "created_asc"
	case feedbackapi.CreatedDesc:
		return "created_desc"
	case feedbackapi.VotesAsc:
		return "votes_asc"
	case feedbackapi.VotesDesc:
		return "votes_desc"
	default:
		return "created"
	}
}

