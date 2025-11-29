package server

import (
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
)

func convertFeedbackStatusFromAPI(s feedbackapi.GetPlayerFeedbackParamsStatus) models.FeedbackStatus {
	switch s {
	case feedbackapi.Pending:
		return models.FeedbackStatusPending
	case feedbackapi.InReview:
		return models.FeedbackStatusInReview
	case feedbackapi.Approved:
		return models.FeedbackStatusApproved
	case feedbackapi.Rejected:
		return models.FeedbackStatusRejected
	case feedbackapi.Merged:
		return models.FeedbackStatusMerged
	case feedbackapi.Closed:
		return models.FeedbackStatusClosed
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
		result = feedbackapi.FeedbackStatusApproved
	case models.FeedbackStatusRejected:
		result = feedbackapi.FeedbackStatusRejected
	case models.FeedbackStatusMerged:
		result = feedbackapi.FeedbackStatusMerged
	case models.FeedbackStatusClosed:
		result = feedbackapi.FeedbackStatusClosed
	default:
		result = feedbackapi.FeedbackStatusPending
	}
	return &result
}

func convertFeedbackStatusToSubmitFeedbackResponseStatus(s models.FeedbackStatus) feedbackapi.SubmitFeedbackResponseStatus {
	switch s {
	case models.FeedbackStatusPending:
		return feedbackapi.SubmitFeedbackResponseStatusPending
	case models.FeedbackStatusInReview:
		return feedbackapi.SubmitFeedbackResponseStatusInReview
	case models.FeedbackStatusApproved:
		return feedbackapi.SubmitFeedbackResponseStatusApproved
	case models.FeedbackStatusRejected:
		return feedbackapi.SubmitFeedbackResponseStatusRejected
	case models.FeedbackStatusMerged:
		return feedbackapi.SubmitFeedbackResponseStatusMerged
	case models.FeedbackStatusClosed:
		return feedbackapi.SubmitFeedbackResponseStatusClosed
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
	case feedbackapi.UpdateStatusRequestStatusApproved:
		return models.FeedbackStatusApproved
	case feedbackapi.UpdateStatusRequestStatusRejected:
		return models.FeedbackStatusRejected
	case feedbackapi.UpdateStatusRequestStatusMerged:
		return models.FeedbackStatusMerged
	case feedbackapi.UpdateStatusRequestStatusClosed:
		return models.FeedbackStatusClosed
	default:
		return models.FeedbackStatusPending
	}
}

func convertFeedbackStatusToFeedbackBoardItemStatus(s models.FeedbackStatus) feedbackapi.FeedbackBoardItemStatus {
	switch s {
	case models.FeedbackStatusPending:
		return feedbackapi.FeedbackBoardItemStatusPending
	case models.FeedbackStatusInReview:
		return feedbackapi.FeedbackBoardItemStatusInReview
	case models.FeedbackStatusApproved:
		return feedbackapi.FeedbackBoardItemStatusApproved
	default:
		return feedbackapi.FeedbackBoardItemStatusPending
	}
}

func convertGetFeedbackBoardParamsStatusFromAPI(s feedbackapi.GetFeedbackBoardParamsStatus) models.FeedbackStatus {
	switch s {
	case feedbackapi.GetFeedbackBoardParamsStatusPending:
		return models.FeedbackStatusPending
	case feedbackapi.GetFeedbackBoardParamsStatusInReview:
		return models.FeedbackStatusInReview
	case feedbackapi.GetFeedbackBoardParamsStatusApproved:
		return models.FeedbackStatusApproved
	default:
		return models.FeedbackStatusPending
	}
}

func convertModerationStatusToAPI(s models.ModerationStatus) feedbackapi.FeedbackModerationStatus {
	switch s {
	case models.ModerationStatusPending:
		return feedbackapi.FeedbackModerationStatusPending
	case models.ModerationStatusApproved:
		return feedbackapi.FeedbackModerationStatusApproved
	case models.ModerationStatusRejected:
		return feedbackapi.FeedbackModerationStatusRejected
	default:
		return feedbackapi.FeedbackModerationStatusPending
	}
}

