package server

import (
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
)

func convertFeedbackCategoryFromAPI(c feedbackapi.SubmitFeedbackRequestCategory) models.FeedbackCategory {
	switch c {
	case feedbackapi.SubmitFeedbackRequestCategoryGameplay:
		return models.FeedbackCategoryGameplay
	case feedbackapi.SubmitFeedbackRequestCategoryBalance:
		return models.FeedbackCategoryBalance
	case feedbackapi.SubmitFeedbackRequestCategoryContent:
		return models.FeedbackCategoryContent
	case feedbackapi.SubmitFeedbackRequestCategoryTechnical:
		return models.FeedbackCategoryTechnical
	case feedbackapi.SubmitFeedbackRequestCategoryLore:
		return models.FeedbackCategoryLore
	case feedbackapi.SubmitFeedbackRequestCategoryUIUx:
		return models.FeedbackCategoryUIUX
	case feedbackapi.SubmitFeedbackRequestCategoryOther:
		return models.FeedbackCategoryOther
	default:
		return models.FeedbackCategoryOther
	}
}

func convertFeedbackCategoryToAPI(c models.FeedbackCategory) *feedbackapi.FeedbackCategory {
	var result feedbackapi.FeedbackCategory
	switch c {
	case models.FeedbackCategoryGameplay:
		result = feedbackapi.FeedbackCategoryGameplay
	case models.FeedbackCategoryBalance:
		result = feedbackapi.FeedbackCategoryBalance
	case models.FeedbackCategoryContent:
		result = feedbackapi.FeedbackCategoryContent
	case models.FeedbackCategoryTechnical:
		result = feedbackapi.FeedbackCategoryTechnical
	case models.FeedbackCategoryLore:
		result = feedbackapi.FeedbackCategoryLore
	case models.FeedbackCategoryUIUX:
		result = feedbackapi.FeedbackCategoryUIUx
	case models.FeedbackCategoryOther:
		result = feedbackapi.FeedbackCategoryOther
	default:
		result = feedbackapi.FeedbackCategoryOther
	}
	return &result
}

func convertFeedbackCategoryToAPIValue(c models.FeedbackCategory) feedbackapi.FeedbackCategory {
	switch c {
	case models.FeedbackCategoryGameplay:
		return feedbackapi.FeedbackCategoryGameplay
	case models.FeedbackCategoryBalance:
		return feedbackapi.FeedbackCategoryBalance
	case models.FeedbackCategoryContent:
		return feedbackapi.FeedbackCategoryContent
	case models.FeedbackCategoryTechnical:
		return feedbackapi.FeedbackCategoryTechnical
	case models.FeedbackCategoryLore:
		return feedbackapi.FeedbackCategoryLore
	case models.FeedbackCategoryUIUX:
		return feedbackapi.FeedbackCategoryUIUx
	case models.FeedbackCategoryOther:
		return feedbackapi.FeedbackCategoryOther
	default:
		return feedbackapi.FeedbackCategoryOther
	}
}


