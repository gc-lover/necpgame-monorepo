package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/feedback-service-go/models"
	"github.com/sirupsen/logrus"
)

type FeedbackRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewFeedbackRepository(db *pgxpool.Pool) *FeedbackRepository {
	return &FeedbackRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *FeedbackRepository) Create(ctx context.Context, feedback *models.Feedback) error {
	gameContextJSON, _ := json.Marshal(feedback.GameContext)
	screenshotsJSON, _ := json.Marshal(feedback.Screenshots)

	query := `
		INSERT INTO feedback.player_feedback (
			id, player_id, type, category, title, description, priority,
			game_context, screenshots, status, votes_count, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13
		)`

	_, err := r.db.Exec(ctx, query,
		feedback.ID, feedback.PlayerID, feedback.Type, feedback.Category,
		feedback.Title, feedback.Description, feedback.Priority,
		gameContextJSON, screenshotsJSON, feedback.Status,
		feedback.VotesCount, feedback.CreatedAt, feedback.UpdatedAt,
	)

	return err
}

func (r *FeedbackRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Feedback, error) {
	var feedback models.Feedback
	var gameContextJSON []byte
	var screenshotsJSON []byte
	var priority *string
	var mergedInto *uuid.UUID
	var moderationStatus *string

	query := `
		SELECT id, player_id, type, category, title, description, priority,
			game_context, screenshots, github_issue_number, github_issue_url,
			status, votes_count, merged_into, moderation_status, moderation_reason,
			created_at, updated_at
		FROM feedback.player_feedback
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&feedback.ID, &feedback.PlayerID, &feedback.Type, &feedback.Category,
		&feedback.Title, &feedback.Description, &priority,
		&gameContextJSON, &screenshotsJSON,
		&feedback.GithubIssueNumber, &feedback.GithubIssueURL,
		&feedback.Status, &feedback.VotesCount, &mergedInto,
		&moderationStatus, &feedback.ModerationReason,
		&feedback.CreatedAt, &feedback.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if priority != nil {
		p := models.FeedbackPriority(*priority)
		feedback.Priority = &p
	}
	if len(gameContextJSON) > 0 {
		var gameContext models.GameContext
		json.Unmarshal(gameContextJSON, &gameContext)
		feedback.GameContext = &gameContext
	}
	if len(screenshotsJSON) > 0 {
		json.Unmarshal(screenshotsJSON, &feedback.Screenshots)
	}
	feedback.MergedInto = mergedInto
	if moderationStatus != nil {
		ms := models.ModerationStatus(*moderationStatus)
		feedback.ModerationStatus = &ms
	}

	return &feedback, nil
}

func (r *FeedbackRepository) GetByPlayerID(ctx context.Context, playerID uuid.UUID, status *models.FeedbackStatus, feedbackType *models.FeedbackType, limit, offset int) ([]models.Feedback, error) {
	query := `
		SELECT id, player_id, type, category, title, description, priority,
			game_context, screenshots, github_issue_number, github_issue_url,
			status, votes_count, merged_into, moderation_status, moderation_reason,
			created_at, updated_at
		FROM feedback.player_feedback
		WHERE player_id = $1`
	args := []interface{}{playerID}
	argPos := 2

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	if feedbackType != nil {
		query += ` AND type = $` + strconv.Itoa(argPos)
		args = append(args, *feedbackType)
		argPos++
	}

	query += ` ORDER BY created_at DESC LIMIT $` + strconv.Itoa(argPos) + ` OFFSET $` + strconv.Itoa(argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var feedbacks []models.Feedback
	for rows.Next() {
		var feedback models.Feedback
		var gameContextJSON []byte
		var screenshotsJSON []byte
		var priority *string
		var mergedInto *uuid.UUID
		var moderationStatus *string

		err := rows.Scan(
			&feedback.ID, &feedback.PlayerID, &feedback.Type, &feedback.Category,
			&feedback.Title, &feedback.Description, &priority,
			&gameContextJSON, &screenshotsJSON,
			&feedback.GithubIssueNumber, &feedback.GithubIssueURL,
			&feedback.Status, &feedback.VotesCount, &mergedInto,
			&moderationStatus, &feedback.ModerationReason,
			&feedback.CreatedAt, &feedback.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if priority != nil {
			p := models.FeedbackPriority(*priority)
			feedback.Priority = &p
		}
		if len(gameContextJSON) > 0 {
			var gameContext models.GameContext
			json.Unmarshal(gameContextJSON, &gameContext)
			feedback.GameContext = &gameContext
		}
		if len(screenshotsJSON) > 0 {
			json.Unmarshal(screenshotsJSON, &feedback.Screenshots)
		}
		feedback.MergedInto = mergedInto
		if moderationStatus != nil {
			ms := models.ModerationStatus(*moderationStatus)
			feedback.ModerationStatus = &ms
		}

		feedbacks = append(feedbacks, feedback)
	}

	return feedbacks, rows.Err()
}

func (r *FeedbackRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.FeedbackStatus, githubIssueNumber *int, githubIssueURL *string) error {
	query := `
		UPDATE feedback.player_feedback
		SET status = $1, github_issue_number = $2, github_issue_url = $3, updated_at = $4
		WHERE id = $5`

	_, err := r.db.Exec(ctx, query, status, githubIssueNumber, githubIssueURL, time.Now(), id)
	return err
}

func (r *FeedbackRepository) ListBoard(ctx context.Context, category *models.FeedbackCategory, status *models.FeedbackStatus, search *string, sort string, limit, offset int) ([]models.FeedbackBoardItem, error) {
	query := `
		SELECT id, type, category, title, description, votes_count, status,
			github_issue_number, github_issue_url, created_at
		FROM feedback.player_feedback
		WHERE moderation_status = 'approved' AND type IN ('feature_request', 'wishlist')`
	args := []interface{}{}
	argPos := 1

	if category != nil {
		query += ` AND category = $` + strconv.Itoa(argPos)
		args = append(args, *category)
		argPos++
	}

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	if search != nil && *search != "" {
		query += ` AND (title ILIKE $` + strconv.Itoa(argPos) + ` OR description ILIKE $` + strconv.Itoa(argPos) + `)`
		searchPattern := "%" + *search + "%"
		args = append(args, searchPattern, searchPattern)
		argPos += 2
	}

	if sort == "votes" {
		query += ` ORDER BY votes_count DESC, created_at DESC`
	} else {
		query += ` ORDER BY created_at DESC`
	}

	query += ` LIMIT $` + strconv.Itoa(argPos) + ` OFFSET $` + strconv.Itoa(argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var items []models.FeedbackBoardItem
	for rows.Next() {
		var item models.FeedbackBoardItem
		err := rows.Scan(
			&item.ID, &item.Type, &item.Category, &item.Title, &item.Description,
			&item.VotesCount, &item.Status,
			&item.GithubIssueNumber, &item.GithubIssueURL,
			&item.CreatedAt,
		)
		if err != nil {
			return nil, err
		}
		items = append(items, item)
	}

	return items, rows.Err()
}

func (r *FeedbackRepository) CountBoard(ctx context.Context, category *models.FeedbackCategory, status *models.FeedbackStatus, search *string) (int, error) {
	query := `
		SELECT COUNT(*)
		FROM feedback.player_feedback
		WHERE moderation_status = 'approved' AND type IN ('feature_request', 'wishlist')`
	args := []interface{}{}
	argPos := 1

	if category != nil {
		query += ` AND category = $` + strconv.Itoa(argPos)
		args = append(args, *category)
		argPos++
	}

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	if search != nil && *search != "" {
		query += ` AND (title ILIKE $` + strconv.Itoa(argPos) + ` OR description ILIKE $` + strconv.Itoa(argPos) + `)`
		searchPattern := "%" + *search + "%"
		args = append(args, searchPattern, searchPattern)
		argPos += 2
	}

	var count int
	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

func (r *FeedbackRepository) CountByPlayerID(ctx context.Context, playerID uuid.UUID, status *models.FeedbackStatus, feedbackType *models.FeedbackType) (int, error) {
	query := `SELECT COUNT(*) FROM feedback.player_feedback WHERE player_id = $1`
	args := []interface{}{playerID}
	argPos := 2

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	if feedbackType != nil {
		query += ` AND type = $` + strconv.Itoa(argPos)
		args = append(args, *feedbackType)
		argPos++
	}

	var count int
	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

func (r *FeedbackRepository) Vote(ctx context.Context, feedbackID, playerID uuid.UUID) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	_, err = tx.Exec(ctx, `
		INSERT INTO feedback.player_feedback_votes (feedback_id, player_id)
		VALUES ($1, $2)
		ON CONFLICT (feedback_id, player_id) DO NOTHING`,
		feedbackID, playerID)
	if err != nil {
		return err
	}

	_, err = tx.Exec(ctx, `
		UPDATE feedback.player_feedback
		SET votes_count = votes_count + 1
		WHERE id = $1 AND NOT EXISTS (
			SELECT 1 FROM feedback.player_feedback_votes
			WHERE feedback_id = $1 AND player_id = $2
		)`,
		feedbackID, playerID)
	if err != nil {
		return err
	}

	return tx.Commit(ctx)
}

func (r *FeedbackRepository) Unvote(ctx context.Context, feedbackID, playerID uuid.UUID) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	_, err = tx.Exec(ctx, `
		DELETE FROM feedback.player_feedback_votes
		WHERE feedback_id = $1 AND player_id = $2`,
		feedbackID, playerID)
	if err != nil {
		return err
	}

	_, err = tx.Exec(ctx, `
		UPDATE feedback.player_feedback
		SET votes_count = GREATEST(votes_count - 1, 0)
		WHERE id = $1`,
		feedbackID)
	if err != nil {
		return err
	}

	return tx.Commit(ctx)
}

func (r *FeedbackRepository) HasVoted(ctx context.Context, feedbackID, playerID uuid.UUID) (bool, error) {
	var exists bool
	err := r.db.QueryRow(ctx, `
		SELECT EXISTS(
			SELECT 1 FROM feedback.player_feedback_votes
			WHERE feedback_id = $1 AND player_id = $2
		)`,
		feedbackID, playerID).Scan(&exists)
	return exists, err
}

func (r *FeedbackRepository) GetStats(ctx context.Context) (*models.FeedbackStats, error) {
	stats := &models.FeedbackStats{}

	err := r.db.QueryRow(ctx, `
		SELECT COUNT(*) FROM feedback.player_feedback
	`).Scan(&stats.Total)
	if err != nil {
		return nil, err
	}

	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'pending'`).Scan(&stats.Pending)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'in_review'`).Scan(&stats.InReview)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'approved'`).Scan(&stats.Approved)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'rejected'`).Scan(&stats.Rejected)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'merged'`).Scan(&stats.Merged)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE status = 'closed'`).Scan(&stats.Closed)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE type = 'feature_request'`).Scan(&stats.FeatureRequests)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE type = 'bug_report'`).Scan(&stats.BugReports)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE type = 'wishlist'`).Scan(&stats.Wishlist)
	r.db.QueryRow(ctx, `SELECT COUNT(*) FROM feedback.player_feedback WHERE type = 'feedback'`).Scan(&stats.Feedback)

	return stats, nil
}








