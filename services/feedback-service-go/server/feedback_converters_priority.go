package server

import (
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
)

func convertFeedbackPriorityFromAPI(p feedbackapi.SubmitFeedbackRequestPriority) models.FeedbackPriority {
	switch p {
	case feedbackapi.SubmitFeedbackRequestPriorityLow:
		return models.FeedbackPriorityLow
	case feedbackapi.SubmitFeedbackRequestPriorityMedium:
		return models.FeedbackPriorityMedium
	case feedbackapi.SubmitFeedbackRequestPriorityHigh:
		return models.FeedbackPriorityHigh
	case feedbackapi.SubmitFeedbackRequestPriorityCritical:
		return models.FeedbackPriorityCritical
	default:
		return models.FeedbackPriorityLow
	}
}

func convertFeedbackPriorityToAPI(p models.FeedbackPriority) feedbackapi.FeedbackPriority {
	switch p {
	case models.FeedbackPriorityLow:
		return feedbackapi.FeedbackPriorityLow
	case models.FeedbackPriorityMedium:
		return feedbackapi.FeedbackPriorityMedium
	case models.FeedbackPriorityHigh:
		return feedbackapi.FeedbackPriorityHigh
	case models.FeedbackPriorityCritical:
		return feedbackapi.FeedbackPriorityCritical
	default:
		return feedbackapi.FeedbackPriorityLow
	}
}





