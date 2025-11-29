package models

import (
	"time"

	"github.com/google/uuid"
)

type FeedbackType string

const (
	FeedbackTypeFeatureRequest FeedbackType = "feature_request"
	FeedbackTypeBugReport     FeedbackType = "bug_report"
	FeedbackTypeWishlist      FeedbackType = "wishlist"
	FeedbackTypeFeedback      FeedbackType = "feedback"
)

type FeedbackCategory string

const (
	FeedbackCategoryGameplay FeedbackCategory = "gameplay"
	FeedbackCategoryBalance  FeedbackCategory = "balance"
	FeedbackCategoryContent  FeedbackCategory = "content"
	FeedbackCategoryTechnical FeedbackCategory = "technical"
	FeedbackCategoryLore     FeedbackCategory = "lore"
	FeedbackCategoryUIUX     FeedbackCategory = "ui_ux"
	FeedbackCategoryOther    FeedbackCategory = "other"
)

type FeedbackPriority string

const (
	FeedbackPriorityLow      FeedbackPriority = "low"
	FeedbackPriorityMedium   FeedbackPriority = "medium"
	FeedbackPriorityHigh     FeedbackPriority = "high"
	FeedbackPriorityCritical FeedbackPriority = "critical"
)

type FeedbackStatus string

const (
	FeedbackStatusPending   FeedbackStatus = "pending"
	FeedbackStatusInReview  FeedbackStatus = "in_review"
	FeedbackStatusApproved  FeedbackStatus = "approved"
	FeedbackStatusRejected  FeedbackStatus = "rejected"
	FeedbackStatusMerged    FeedbackStatus = "merged"
	FeedbackStatusClosed    FeedbackStatus = "closed"
)

type ModerationStatus string

const (
	ModerationStatusPending  ModerationStatus = "pending"
	ModerationStatusApproved ModerationStatus = "approved"
	ModerationStatusRejected ModerationStatus = "rejected"
)

type GameContext struct {
	Version        string   `json:"version" db:"version"`
	Location       string   `json:"location,omitempty" db:"location"`
	CharacterLevel *int     `json:"character_level,omitempty" db:"character_level"`
	ActiveQuests   []string `json:"active_quests,omitempty" db:"active_quests"`
	PlaytimeHours  *float64 `json:"playtime_hours,omitempty" db:"playtime_hours"`
}

type Feedback struct {
	ID                uuid.UUID        `json:"id" db:"id"`
	PlayerID          uuid.UUID        `json:"player_id" db:"player_id"`
	Type              FeedbackType      `json:"type" db:"type"`
	Category          FeedbackCategory  `json:"category" db:"category"`
	Title             string           `json:"title" db:"title"`
	Description       string           `json:"description" db:"description"`
	Priority          *FeedbackPriority `json:"priority,omitempty" db:"priority"`
	GameContext       *GameContext     `json:"game_context,omitempty" db:"game_context"`
	Screenshots       []string         `json:"screenshots,omitempty" db:"screenshots"`
	GithubIssueNumber *int             `json:"github_issue_number,omitempty" db:"github_issue_number"`
	GithubIssueURL    *string          `json:"github_issue_url,omitempty" db:"github_issue_url"`
	Status            FeedbackStatus   `json:"status" db:"status"`
	VotesCount        int              `json:"votes_count" db:"votes_count"`
	MergedInto        *uuid.UUID       `json:"merged_into,omitempty" db:"merged_into"`
	ModerationStatus  *ModerationStatus `json:"moderation_status,omitempty" db:"moderation_status"`
	ModerationReason  *string          `json:"moderation_reason,omitempty" db:"moderation_reason"`
	CreatedAt         time.Time        `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time        `json:"updated_at" db:"updated_at"`
}

type SubmitFeedbackRequest struct {
	Type        FeedbackType      `json:"type"`
	Category    FeedbackCategory  `json:"category"`
	Title       string            `json:"title"`
	Description string            `json:"description"`
	Priority    *FeedbackPriority `json:"priority,omitempty"`
	GameContext *GameContext      `json:"game_context,omitempty"`
	Screenshots []string          `json:"screenshots,omitempty"`
}

type SubmitFeedbackResponse struct {
	ID                uuid.UUID      `json:"id"`
	Status            FeedbackStatus `json:"status"`
	GithubIssueNumber *int           `json:"github_issue_number,omitempty"`
	GithubIssueURL    *string        `json:"github_issue_url,omitempty"`
	CreatedAt         time.Time      `json:"created_at"`
}

type FeedbackList struct {
	Items  []Feedback `json:"items"`
	Total  int        `json:"total"`
	Limit  int        `json:"limit"`
	Offset int        `json:"offset"`
}

type UpdateStatusRequest struct {
	Status            FeedbackStatus `json:"status"`
	GithubIssueNumber *int           `json:"github_issue_number,omitempty"`
	GithubIssueURL    *string        `json:"github_issue_url,omitempty"`
	Comment           *string        `json:"comment,omitempty"`
}

type FeedbackBoardItem struct {
	ID                uuid.UUID       `json:"id"`
	Type              FeedbackType    `json:"type"`
	Category          FeedbackCategory `json:"category"`
	Title             string          `json:"title"`
	Description       string          `json:"description"`
	VotesCount        int             `json:"votes_count"`
	Status            FeedbackStatus  `json:"status"`
	GithubIssueNumber *int            `json:"github_issue_number,omitempty"`
	GithubIssueURL    *string         `json:"github_issue_url,omitempty"`
	CreatedAt         time.Time       `json:"created_at"`
}

type FeedbackBoardList struct {
	Items  []FeedbackBoardItem `json:"items"`
	Total  int                 `json:"total"`
	Limit  int                 `json:"limit"`
	Offset int                 `json:"offset"`
}

type VoteResponse struct {
	VotesCount int  `json:"votes_count"`
	HasVoted   bool `json:"has_voted"`
}

type FeedbackStats struct {
	Total           int `json:"total"`
	Pending         int `json:"pending"`
	InReview        int `json:"in_review"`
	Approved        int `json:"approved"`
	Rejected        int `json:"rejected"`
	Merged          int `json:"merged"`
	Closed          int `json:"closed"`
	FeatureRequests int `json:"feature_requests"`
	BugReports      int `json:"bug_reports"`
	Wishlist        int `json:"wishlist"`
	Feedback        int `json:"feedback"`
}









