package server

import (
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
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

func convertFeedbackPriorityToAPI(p models.FeedbackPriority) feedbackapi.SubmitFeedbackRequestPriority {
	switch p {
	case models.FeedbackPriorityLow:
		return feedbackapi.SubmitFeedbackRequestPriorityLow
	case models.FeedbackPriorityMedium:
		return feedbackapi.SubmitFeedbackRequestPriorityMedium
	case models.FeedbackPriorityHigh:
		return feedbackapi.SubmitFeedbackRequestPriorityHigh
	case models.FeedbackPriorityCritical:
		return feedbackapi.SubmitFeedbackRequestPriorityCritical
	default:
		return feedbackapi.SubmitFeedbackRequestPriorityLow
	}
}






















