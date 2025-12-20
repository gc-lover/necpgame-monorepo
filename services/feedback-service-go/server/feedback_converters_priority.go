package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
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
