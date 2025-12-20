package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
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
