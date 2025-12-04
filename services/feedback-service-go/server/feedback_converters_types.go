package server

import (
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
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

func convertFeedbackTypeToAPIValue(t models.FeedbackType) feedbackapi.FeedbackType {
	switch t {
	case models.FeedbackTypeFeatureRequest:
		return feedbackapi.FeedbackTypeFeatureRequest
	case models.FeedbackTypeBugReport:
		return feedbackapi.FeedbackTypeBugReport
	case models.FeedbackTypeWishlist:
		return feedbackapi.FeedbackTypeWishlist
	case models.FeedbackTypeFeedback:
		return feedbackapi.FeedbackTypeFeedback
	default:
		return feedbackapi.FeedbackTypeFeedback
	}
}























