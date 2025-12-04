package server

import (
	feedbackapi "github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
)

func convertFeedbackStatusFromAPI(s feedbackapi.GetPlayerFeedbackStatus) models.FeedbackStatus {
	switch s {
	case feedbackapi.GetPlayerFeedbackStatusPending:
		return models.FeedbackStatusPending
	case feedbackapi.GetPlayerFeedbackStatusInReview:
		return models.FeedbackStatusInReview
	case feedbackapi.GetPlayerFeedbackStatusResolved:
		return models.FeedbackStatusApproved
	case feedbackapi.GetPlayerFeedbackStatusRejected:
		return models.FeedbackStatusRejected
	case feedbackapi.GetPlayerFeedbackStatusMerged:
		return models.FeedbackStatusMerged
	default:
		return models.FeedbackStatusPending
	}
}

func convertFeedbackStatusToAPI(s models.FeedbackStatus) *feedbackapi.FeedbackStatus {
	var result feedbackapi.FeedbackStatus
	switch s {
	case models.FeedbackStatusPending:
		result = feedbackapi.FeedbackStatusPending
	case models.FeedbackStatusInReview:
		result = feedbackapi.FeedbackStatusInReview
	case models.FeedbackStatusApproved:
		result = feedbackapi.FeedbackStatusResolved
	case models.FeedbackStatusRejected:
		result = feedbackapi.FeedbackStatusRejected
	case models.FeedbackStatusMerged:
		result = feedbackapi.FeedbackStatusMerged
	case models.FeedbackStatusClosed:
		result = feedbackapi.FeedbackStatusResolved
	default:
		result = feedbackapi.FeedbackStatusPending
	}
	return &result
}

func convertFeedbackStatusToAPIValue(s models.FeedbackStatus) feedbackapi.FeedbackStatus {
	switch s {
	case models.FeedbackStatusPending:
		return feedbackapi.FeedbackStatusPending
	case models.FeedbackStatusInReview:
		return feedbackapi.FeedbackStatusInReview
	case models.FeedbackStatusApproved:
		return feedbackapi.FeedbackStatusResolved
	case models.FeedbackStatusRejected:
		return feedbackapi.FeedbackStatusRejected
	case models.FeedbackStatusMerged:
		return feedbackapi.FeedbackStatusMerged
	case models.FeedbackStatusClosed:
		return feedbackapi.FeedbackStatusResolved
	default:
		return feedbackapi.FeedbackStatusPending
	}
}

func convertFeedbackStatusToSubmitFeedbackResponseStatus(s models.FeedbackStatus) feedbackapi.SubmitFeedbackResponseStatus {
	switch s {
	case models.FeedbackStatusPending:
		return feedbackapi.SubmitFeedbackResponseStatusPending
	case models.FeedbackStatusInReview:
		return feedbackapi.SubmitFeedbackResponseStatusInReview
	case models.FeedbackStatusApproved:
		return feedbackapi.SubmitFeedbackResponseStatusResolved
	case models.FeedbackStatusRejected:
		return feedbackapi.SubmitFeedbackResponseStatusRejected
	case models.FeedbackStatusMerged:
		return feedbackapi.SubmitFeedbackResponseStatusMerged
	case models.FeedbackStatusClosed:
		return feedbackapi.SubmitFeedbackResponseStatusResolved
	default:
		return feedbackapi.SubmitFeedbackResponseStatusPending
	}
}

func convertUpdateStatusRequestStatusFromAPI(s feedbackapi.UpdateStatusRequestStatus) models.FeedbackStatus {
	switch s {
	case feedbackapi.UpdateStatusRequestStatusPending:
		return models.FeedbackStatusPending
	case feedbackapi.UpdateStatusRequestStatusInReview:
		return models.FeedbackStatusInReview
	case feedbackapi.UpdateStatusRequestStatusResolved:
		return models.FeedbackStatusApproved
	case feedbackapi.UpdateStatusRequestStatusRejected:
		return models.FeedbackStatusRejected
	case feedbackapi.UpdateStatusRequestStatusMerged:
		return models.FeedbackStatusMerged
	default:
		return models.FeedbackStatusPending
	}
}























