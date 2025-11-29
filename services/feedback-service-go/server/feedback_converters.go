package server

import (
	"github.com/google/uuid"
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertSubmitFeedbackRequestFromAPI(req *feedbackapi.SubmitFeedbackRequest) *models.SubmitFeedbackRequest {
	result := &models.SubmitFeedbackRequest{
		Type:        convertFeedbackTypeFromAPI(req.Type),
		Category:    convertFeedbackCategoryFromAPI(req.Category),
		Title:       req.Title,
		Description: req.Description,
	}

	if req.Priority != nil {
		p := convertFeedbackPriorityFromAPI(*req.Priority)
		result.Priority = &p
	}

	if req.GameContext != nil {
		result.GameContext = convertGameContextFromAPI(req.GameContext)
	}

	if req.Screenshots != nil {
		result.Screenshots = *req.Screenshots
	}

	return result
}

func convertSubmitFeedbackResponseToAPI(resp *models.SubmitFeedbackResponse) feedbackapi.SubmitFeedbackResponse {
	status := convertFeedbackStatusToSubmitFeedbackResponseStatus(resp.Status)
	result := feedbackapi.SubmitFeedbackResponse{
		Id:                (*openapi_types.UUID)(&resp.ID),
		Status:            &status,
		CreatedAt:         &resp.CreatedAt,
	}

	if resp.GithubIssueNumber != nil {
		result.GithubIssueNumber = resp.GithubIssueNumber
	}

	if resp.GithubIssueURL != nil {
		result.GithubIssueUrl = resp.GithubIssueURL
	}

	return result
}

func convertFeedbackToAPI(feedback *models.Feedback) feedbackapi.Feedback {
	result := feedbackapi.Feedback{
		Id:                (*openapi_types.UUID)(&feedback.ID),
		PlayerId:          (*openapi_types.UUID)(&feedback.PlayerID),
		Type:              convertFeedbackTypeToAPI(feedback.Type),
		Category:          convertFeedbackCategoryToAPI(feedback.Category),
		Title:             &feedback.Title,
		Description:      &feedback.Description,
		Status:            convertFeedbackStatusToAPI(feedback.Status),
		CreatedAt:         &feedback.CreatedAt,
		UpdatedAt:         &feedback.UpdatedAt,
		VotesCount:        &feedback.VotesCount,
	}

	if feedback.Priority != nil {
		p := convertFeedbackPriorityToAPI(*feedback.Priority)
		result.Priority = &p
	}

	if feedback.GameContext != nil {
		result.GameContext = convertGameContextToAPI(feedback.GameContext)
	}

	if len(feedback.Screenshots) > 0 {
		result.Screenshots = &feedback.Screenshots
	}

	if feedback.GithubIssueNumber != nil {
		result.GithubIssueNumber = feedback.GithubIssueNumber
	}

	if feedback.GithubIssueURL != nil {
		result.GithubIssueUrl = feedback.GithubIssueURL
	}

	if feedback.MergedInto != nil {
		result.MergedInto = (*openapi_types.UUID)(feedback.MergedInto)
	}

	if feedback.ModerationStatus != nil {
		ms := convertModerationStatusToAPI(*feedback.ModerationStatus)
		result.ModerationStatus = &ms
	}

	if feedback.ModerationReason != nil {
		result.ModerationReason = feedback.ModerationReason
	}

	return result
}

func convertFeedbackListToAPI(list *models.FeedbackList) feedbackapi.FeedbackList {
	result := feedbackapi.FeedbackList{
		Total:  &list.Total,
		Limit:  &list.Limit,
		Offset: &list.Offset,
	}

	items := make([]feedbackapi.Feedback, len(list.Items))
	for i, item := range list.Items {
		items[i] = convertFeedbackToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertUpdateStatusRequestFromAPI(req *feedbackapi.UpdateStatusRequest) *models.UpdateStatusRequest {
	result := &models.UpdateStatusRequest{
		Status: convertUpdateStatusRequestStatusFromAPI(req.Status),
	}

	if req.GithubIssueNumber != nil {
		result.GithubIssueNumber = req.GithubIssueNumber
	}

	if req.GithubIssueUrl != nil {
		result.GithubIssueURL = req.GithubIssueUrl
	}

	return result
}

func convertFeedbackBoardListToAPI(list *models.FeedbackBoardList) feedbackapi.FeedbackBoardList {
	result := feedbackapi.FeedbackBoardList{
		Total:  &list.Total,
		Limit:  &list.Limit,
		Offset: &list.Offset,
	}

	items := make([]feedbackapi.FeedbackBoardItem, len(list.Items))
	for i, item := range list.Items {
		items[i] = convertFeedbackBoardItemToAPI(&item)
	}
	result.Items = &items

	return result
}

func convertFeedbackBoardItemToAPI(item *models.FeedbackBoardItem) feedbackapi.FeedbackBoardItem {
	itemType := convertFeedbackTypeToFeedbackBoardItemType(item.Type)
	category := convertFeedbackCategoryToFeedbackBoardItemCategory(item.Category)
	status := convertFeedbackStatusToFeedbackBoardItemStatus(item.Status)
	result := feedbackapi.FeedbackBoardItem{
		Id:         (*openapi_types.UUID)(&item.ID),
		Type:       &itemType,
		Category:   &category,
		Title:      &item.Title,
		Description: &item.Description,
		Status:     &status,
		VotesCount: &item.VotesCount,
		CreatedAt:  &item.CreatedAt,
	}

	if item.GithubIssueNumber != nil {
		result.GithubIssueNumber = item.GithubIssueNumber
	}

	if item.GithubIssueURL != nil {
		result.GithubIssueUrl = item.GithubIssueURL
	}

	return result
}

func convertVoteResponseToAPI(resp *models.VoteResponse, feedbackID uuid.UUID) feedbackapi.VoteResponse {
	result := feedbackapi.VoteResponse{
		FeedbackId: (*openapi_types.UUID)(&feedbackID),
		VotesCount: &resp.VotesCount,
		HasVoted:   &resp.HasVoted,
	}

	return result
}

func convertFeedbackStatsToAPI(stats *models.FeedbackStats) feedbackapi.FeedbackStats {
	result := feedbackapi.FeedbackStats{
		Total: &stats.Total,
	}

	result.ByStatus = &struct {
		Approved *int `json:"approved,omitempty"`
		Closed   *int `json:"closed,omitempty"`
		InReview *int `json:"in_review,omitempty"`
		Merged   *int `json:"merged,omitempty"`
		Pending  *int `json:"pending,omitempty"`
		Rejected *int `json:"rejected,omitempty"`
	}{
		Approved: &stats.Approved,
		Closed:   &stats.Closed,
		InReview: &stats.InReview,
		Merged:   &stats.Merged,
		Pending:  &stats.Pending,
		Rejected: &stats.Rejected,
	}

	result.ByType = &struct {
		BugReport      *int `json:"bug_report,omitempty"`
		FeatureRequest *int `json:"feature_request,omitempty"`
		Feedback       *int `json:"feedback,omitempty"`
		Wishlist       *int `json:"wishlist,omitempty"`
	}{
		BugReport:      &stats.BugReports,
		FeatureRequest: &stats.FeatureRequests,
		Feedback:       &stats.Feedback,
		Wishlist:       &stats.Wishlist,
	}

	result.ByCategory = &struct {
		Balance   *int `json:"balance,omitempty"`
		Content   *int `json:"content,omitempty"`
		Gameplay  *int `json:"gameplay,omitempty"`
		Lore      *int `json:"lore,omitempty"`
		Other     *int `json:"other,omitempty"`
		Technical *int `json:"technical,omitempty"`
		UiUx      *int `json:"ui_ux,omitempty"`
	}{}

	totalVotes := 0
	if stats.Total > 0 {
		avgVotes := float32(totalVotes) / float32(stats.Total)
		result.AverageVotesPerFeedback = &avgVotes
	}

	return result
}

func convertGameContextToAPI(ctx *models.GameContext) *feedbackapi.GameContext {
	result := &feedbackapi.GameContext{
		Version: &ctx.Version,
	}

	if ctx.Location != "" {
		result.Location = &ctx.Location
	}

	if ctx.CharacterLevel != nil {
		result.CharacterLevel = ctx.CharacterLevel
	}

	if len(ctx.ActiveQuests) > 0 {
		result.ActiveQuests = &ctx.ActiveQuests
	}

	if ctx.PlaytimeHours != nil {
		hours := float32(*ctx.PlaytimeHours)
		result.PlaytimeHours = &hours
	}

	return result
}

func convertGameContextFromAPI(ctx *feedbackapi.GameContext) *models.GameContext {
	result := &models.GameContext{}

	if ctx.Version != nil {
		result.Version = *ctx.Version
	}

	if ctx.Location != nil {
		result.Location = *ctx.Location
	}

	if ctx.CharacterLevel != nil {
		result.CharacterLevel = ctx.CharacterLevel
	}

	if ctx.ActiveQuests != nil {
		result.ActiveQuests = *ctx.ActiveQuests
	}

	if ctx.PlaytimeHours != nil {
		hours := float64(*ctx.PlaytimeHours)
		result.PlaytimeHours = &hours
	}

	return result
}

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

func convertSortFromAPI(s feedbackapi.GetFeedbackBoardParamsSort) string {
	switch s {
	case feedbackapi.CreatedAsc:
		return "created_asc"
	case feedbackapi.CreatedDesc:
		return "created_desc"
	case feedbackapi.VotesAsc:
		return "votes_asc"
	case feedbackapi.VotesDesc:
		return "votes_desc"
	default:
		return "created"
	}
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

