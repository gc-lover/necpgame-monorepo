package server

import (
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
)

func convertFeedbackTypeFromAPI(t feedbackapi.SubmitFeedbackRequestType) models.FeedbackType {
	switch t {
	case feedbackapi.SubmitFeedbackRequestTypeFeatureRequest:
		return models.FeedbackTypeFeatureRequest
	case feedbackapi.SubmitFeedbackRequestTypeBugReport:
		return models.FeedbackTypeBugReport
	case feedbackapi.SubmitFeedbackRequestTypeWishlist:
		return models.FeedbackTypeWishlist
	case feedbackapi.SubmitFeedbackRequestTypeFeedback:
		return models.FeedbackTypeFeedback
	default:
		return models.FeedbackTypeFeedback
	}
}

func convertFeedbackTypeToAPI(t models.FeedbackType) *feedbackapi.FeedbackType {
	var result feedbackapi.FeedbackType
	switch t {
	case models.FeedbackTypeFeatureRequest:
		result = feedbackapi.FeedbackTypeFeatureRequest
	case models.FeedbackTypeBugReport:
		result = feedbackapi.FeedbackTypeBugReport
	case models.FeedbackTypeWishlist:
		result = feedbackapi.FeedbackTypeWishlist
	case models.FeedbackTypeFeedback:
		result = feedbackapi.FeedbackTypeFeedback
	default:
		result = feedbackapi.FeedbackTypeFeedback
	}
	return &result
}

func convertGetPlayerFeedbackParamsTypeFromAPI(t feedbackapi.GetPlayerFeedbackParamsType) models.FeedbackType {
	switch t {
	case feedbackapi.GetPlayerFeedbackParamsTypeFeatureRequest:
		return models.FeedbackTypeFeatureRequest
	case feedbackapi.GetPlayerFeedbackParamsTypeBugReport:
		return models.FeedbackTypeBugReport
	case feedbackapi.GetPlayerFeedbackParamsTypeWishlist:
		return models.FeedbackTypeWishlist
	case feedbackapi.GetPlayerFeedbackParamsTypeFeedback:
		return models.FeedbackTypeFeedback
	default:
		return models.FeedbackTypeFeedback
	}
}

func convertFeedbackTypeToFeedbackBoardItemType(t models.FeedbackType) feedbackapi.FeedbackBoardItemType {
	switch t {
	case models.FeedbackTypeFeatureRequest:
		return feedbackapi.FeedbackBoardItemTypeFeatureRequest
	case models.FeedbackTypeBugReport:
		return feedbackapi.FeedbackBoardItemTypeBugReport
	case models.FeedbackTypeWishlist:
		return feedbackapi.FeedbackBoardItemTypeWishlist
	case models.FeedbackTypeFeedback:
		return feedbackapi.FeedbackBoardItemTypeFeedback
	default:
		return feedbackapi.FeedbackBoardItemTypeFeedback
	}
}











