package server

import (
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
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
	case feedbackapi.SubmitFeedbackRequestCategoryUiUx:
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
		result = feedbackapi.FeedbackCategoryUiUx
	case models.FeedbackCategoryOther:
		result = feedbackapi.FeedbackCategoryOther
	default:
		result = feedbackapi.FeedbackCategoryOther
	}
	return &result
}

func convertGetFeedbackBoardParamsCategoryFromAPI(c feedbackapi.GetFeedbackBoardParamsCategory) models.FeedbackCategory {
	switch c {
	case feedbackapi.GetFeedbackBoardParamsCategoryGameplay:
		return models.FeedbackCategoryGameplay
	case feedbackapi.GetFeedbackBoardParamsCategoryBalance:
		return models.FeedbackCategoryBalance
	case feedbackapi.GetFeedbackBoardParamsCategoryContent:
		return models.FeedbackCategoryContent
	case feedbackapi.GetFeedbackBoardParamsCategoryTechnical:
		return models.FeedbackCategoryTechnical
	case feedbackapi.GetFeedbackBoardParamsCategoryLore:
		return models.FeedbackCategoryLore
	case feedbackapi.GetFeedbackBoardParamsCategoryUiUx:
		return models.FeedbackCategoryUIUX
	case feedbackapi.GetFeedbackBoardParamsCategoryOther:
		return models.FeedbackCategoryOther
	default:
		return models.FeedbackCategoryOther
	}
}

func convertFeedbackCategoryToFeedbackBoardItemCategory(c models.FeedbackCategory) feedbackapi.FeedbackBoardItemCategory {
	switch c {
	case models.FeedbackCategoryGameplay:
		return feedbackapi.FeedbackBoardItemCategoryGameplay
	case models.FeedbackCategoryBalance:
		return feedbackapi.FeedbackBoardItemCategoryBalance
	case models.FeedbackCategoryContent:
		return feedbackapi.FeedbackBoardItemCategoryContent
	case models.FeedbackCategoryTechnical:
		return feedbackapi.FeedbackBoardItemCategoryTechnical
	case models.FeedbackCategoryLore:
		return feedbackapi.FeedbackBoardItemCategoryLore
	case models.FeedbackCategoryUIUX:
		return feedbackapi.FeedbackBoardItemCategoryUiUx
	case models.FeedbackCategoryOther:
		return feedbackapi.FeedbackBoardItemCategoryOther
	default:
		return feedbackapi.FeedbackBoardItemCategoryOther
	}
}

