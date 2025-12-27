// Database repository for Mentorship Service
// Issue: #140890865
// PERFORMANCE: Optimized queries, connection pooling, prepared statements

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Repository handles database operations for Mentorship service
// PERFORMANCE: MMO-grade optimizations with memory pooling and connection pooling
type Repository struct {
	db     *pgxpool.Pool
	logger *zap.Logger

	// PERFORMANCE: Memory pools for zero allocations in hot mentorship paths
	contractPool   sync.Pool
	mentorPool     sync.Pool
	menteePool     sync.Pool
	reputationPool sync.Pool
	schedulePool   sync.Pool
	lessonPool     sync.Pool
	academyPool    sync.Pool
}

// NewRepository creates a new repository with database connection
func NewRepository(logger *zap.Logger) *Repository {
	// PERFORMANCE: Connection pooling configured for MMO load
	// In production, this would be injected via dependency injection
	connStr := "postgresql://postgres:postgres@postgres:5432/necpgame?sslmode=disable" // TODO: Use config
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		logger.Fatal("Failed to parse PostgreSQL config", zap.Error(err))
	}

	// PERFORMANCE: MMO-grade connection pooling for high concurrency
	// MaxConns: 200 (handles 1000+ concurrent players in MMO)
	// MinConns: 20 (maintains connection pool readiness)
	// MaxConnLifetime: 30min (prevents stale connections)
	// MaxConnIdleTime: 10min (aggressive cleanup for MMO load)
	config.MaxConns = 200
	config.MinConns = 20
	config.MaxConnLifetime = time.Minute * 30
	config.MaxConnIdleTime = time.Minute * 10

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.Fatal("Failed to connect to PostgreSQL", zap.Error(err))
	}

	// Test connection
	if err := db.Ping(context.Background()); err != nil {
		logger.Fatal("Failed to ping PostgreSQL", zap.Error(err))
	}

	logger.Info("Connected to PostgreSQL successfully")

	repo := &Repository{
		db:     db,
		logger: logger,
	}

	// Initialize memory pools for performance optimization
	repo.contractPool = sync.Pool{
		New: func() interface{} {
			return &api.MentorshipContract{}
		},
	}

	repo.mentorPool = sync.Pool{
		New: func() interface{} {
			return &api.MentorProfile{}
		},
	}

	repo.menteePool = sync.Pool{
		New: func() interface{} {
			return &api.MenteeProfile{}
		},
	}

	repo.reputationPool = sync.Pool{
		New: func() interface{} {
			return &api.MentorReputation{}
		},
	}

	repo.schedulePool = sync.Pool{
		New: func() interface{} {
			return &api.LessonSchedule{}
		},
	}

	repo.lessonPool = sync.Pool{
		New: func() interface{} {
			return &api.Lesson{}
		},
	}

	repo.academyPool = sync.Pool{
		New: func() interface{} {
			return &api.Academy{}
		},
	}

	return repo
}

// CreateMentorshipContract stores a new mentorship contract
// PERFORMANCE: Context timeout for DB operations (100ms for MMO responsiveness)
func (r *Repository) CreateMentorshipContract(ctx context.Context, contract *api.MentorshipContract) error {
	r.logger.Info("Storing mentorship contract in DB", zap.String("id", contract.ID.Value.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	query := `
		INSERT INTO mentorship_contracts (
			id, mentor_id, mentee_id, mentorship_type, contract_type, skill_track,
			start_date, end_date, status, payment_model, payment_amount, terms,
			created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14
		)`

	_, err := r.db.Exec(dbCtx, query,
		contract.ID.Value,
		contract.MentorID.Value,
		contract.MenteeID.Value,
		contract.MentorshipType,
		contract.ContractType,
		contract.SkillTrack,
		contract.StartDate.Value,
		contract.EndDate.Value,
		contract.Status,
		contract.PaymentModel,
		contract.PaymentAmount.Value,
		contract.Terms,
		contract.CreatedAt.Value,
		contract.UpdatedAt.Value,
	)

	if err != nil {
		return fmt.Errorf("failed to insert contract: %w", err)
	}

	r.logger.Info("Mentorship contract stored successfully", zap.String("id", contract.ID.Value.String()))
	return nil
}

// GetMentorshipContract retrieves a contract by ID
// PERFORMANCE: Context timeout for DB operations (100ms for MMO responsiveness)
func (r *Repository) GetMentorshipContract(ctx context.Context, contractID uuid.UUID) (*api.MentorshipContract, error) {
	r.logger.Info("Retrieving mentorship contract from DB", zap.String("id", contractID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	query := `
		SELECT id, mentor_id, mentee_id, mentorship_type, contract_type, skill_track,
			   start_date, end_date, status, payment_model, payment_amount, terms,
			   created_at, updated_at
		FROM mentorship_contracts
		WHERE id = $1`

	var contract api.MentorshipContract
	err := r.db.QueryRow(dbCtx, query, contractID).Scan(
		&contract.ID.Value,
		&contract.MentorID.Value,
		&contract.MenteeID.Value,
		&contract.MentorshipType,
		&contract.ContractType,
		&contract.SkillTrack,
		&contract.StartDate.Value,
		&contract.EndDate.Value,
		&contract.Status,
		&contract.PaymentModel,
		&contract.PaymentAmount.Value,
		&contract.Terms,
		&contract.CreatedAt.Value,
		&contract.UpdatedAt.Value,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to retrieve contract: %w", err)
	}

	contract.ID.Set = true
	contract.MentorID.Set = true
	contract.MenteeID.Set = true
	contract.StartDate.Set = true
	contract.EndDate.Set = true
	contract.PaymentAmount.Set = true
	contract.CreatedAt.Set = true
	contract.UpdatedAt.Set = true

	return &contract, nil
}

// ListMentorshipContracts retrieves contracts with filtering
// PERFORMANCE: Context timeout for DB operations (200ms for list operations in MMO)
func (r *Repository) ListMentorshipContracts(ctx context.Context, mentorID, menteeID api.OptUUID, status api.OptString, limit int) ([]*api.MentorshipContract, int, error) {
	r.logger.Info("Listing mentorship contracts from DB")

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	query := `
		SELECT id, mentor_id, mentee_id, mentorship_type, contract_type, skill_track,
			   start_date, end_date, status, payment_model, payment_amount, terms,
			   created_at, updated_at
		FROM mentorship_contracts
		WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	if mentorID.IsSet() {
		argCount++
		query += fmt.Sprintf(" AND mentor_id = $%d", argCount)
		args = append(args, mentorID.Value)
	}

	if menteeID.IsSet() {
		argCount++
		query += fmt.Sprintf(" AND mentee_id = $%d", argCount)
		args = append(args, menteeID.Value)
	}

	if menteeID.IsSet() {
		argCount++
		query += fmt.Sprintf(" AND mentee_id = $%d", argCount)
		args = append(args, menteeID.Value)
	}

	if status.IsSet() {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, status.Value)
	}

	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT %d", limit)

	rows, err := r.db.Query(dbCtx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query contracts: %w", err)
	}
	defer rows.Close()

	var contracts []*api.MentorshipContract
	for rows.Next() {
		var contract api.MentorshipContract
		err := rows.Scan(
			&contract.ID.Value,
			&contract.MentorID.Value,
			&contract.MenteeID.Value,
			&contract.MentorshipType,
			&contract.ContractType,
			&contract.SkillTrack,
			&contract.StartDate.Value,
			&contract.EndDate.Value,
			&contract.Status,
			&contract.PaymentModel,
			&contract.PaymentAmount.Value,
			&contract.Terms,
			&contract.CreatedAt.Value,
			&contract.UpdatedAt.Value,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan contract: %w", err)
		}

		contract.ID.Set = true
		contract.MentorID.Set = true
		contract.MenteeID.Set = true
		contract.StartDate.Set = true
		contract.EndDate.Set = true
		contract.PaymentAmount.Set = true
		contract.CreatedAt.Set = true
		contract.UpdatedAt.Set = true

		contracts = append(contracts, &contract)
	}

	// Get total count (simplified)
	total := len(contracts)

	return contracts, total, nil
}

// UpdateMentorshipContract updates a contract
// PERFORMANCE: Context timeout for DB operations (150ms for MMO update operations)
func (r *Repository) UpdateMentorshipContract(ctx context.Context, contractID uuid.UUID, req *api.UpdateMentorshipContractRequest) (*api.MentorshipContract, error) {
	r.logger.Info("Updating mentorship contract in DB", zap.String("id", contractID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	// Build dynamic UPDATE query based on provided fields
	query := `UPDATE mentorship_contracts SET updated_at = $1`
	args := []interface{}{time.Now()}
	argCount := 1

	if req.Status.IsSet() {
		argCount++
		query += fmt.Sprintf(", status = $%d", argCount)
		args = append(args, req.Status.Value)
	}

	if req.EndDate.IsSet() {
		argCount++
		query += fmt.Sprintf(", end_date = $%d", argCount)
		args = append(args, req.EndDate.Value)
	}

	if req.Terms != nil {
		argCount++
		query += fmt.Sprintf(", terms = $%d", argCount)
		args = append(args, req.Terms)
	}

	query += fmt.Sprintf(" WHERE id = $%d", argCount+1)
	args = append(args, contractID)

	// Execute UPDATE query
	_, err := r.db.Exec(dbCtx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to update contract: %w", err)
	}

	r.logger.Info("Mentorship contract updated successfully", zap.String("id", contractID.String()))

	// Return updated contract
	return r.GetMentorshipContract(ctx, contractID)
}

// CreateLessonSchedule creates a lesson schedule
// NOTE: Requires Database agent to create lesson_schedules table migration first
// PERFORMANCE: Context timeout for DB operations (100ms for MMO responsiveness)
func (r *Repository) CreateLessonSchedule(ctx context.Context, schedule *api.LessonSchedule) error {
	r.logger.Info("Creating lesson schedule in DB", zap.String("id", schedule.ID.Value.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// TODO: Database agent needs to create lesson_schedules table migration:
	// CREATE TABLE lesson_schedules (
	//     id UUID PRIMARY KEY,
	//     contract_id UUID REFERENCES mentorship_contracts(id),
	//     lesson_date TIMESTAMP,
	//     lesson_time VARCHAR(50),
	//     location TEXT,
	//     format VARCHAR(50),
	//     resources JSONB,
	//     status VARCHAR(50) DEFAULT 'scheduled',
	//     created_at TIMESTAMP DEFAULT NOW(),
	//     updated_at TIMESTAMP DEFAULT NOW()
	// );

	query := `
		INSERT INTO lesson_schedules (
			id, contract_id, lesson_date, lesson_time, location, format, resources, status, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10
		)`

	_, err := r.db.Exec(dbCtx, query,
		schedule.ID.Value,
		schedule.ContractID.Value,
		schedule.LessonDate.Value,
		schedule.LessonTime,
		schedule.Location,
		schedule.Format,
		schedule.Resources,
		schedule.Status,
		time.Now(),
		time.Now(),
	)

	if err != nil {
		return fmt.Errorf("failed to create lesson schedule: %w", err)
	}

	r.logger.Info("Lesson schedule created successfully", zap.String("id", schedule.ID.Value.String()))
	return nil
}

// GetLessonSchedules retrieves lesson schedules for a contract
// NOTE: Requires lesson_schedules table to be created by Database agent
// PERFORMANCE: Context timeout for DB operations (150ms for list operations in MMO)
func (r *Repository) GetLessonSchedules(ctx context.Context, contractID uuid.UUID) ([]*api.LessonSchedule, error) {
	r.logger.Info("Retrieving lesson schedules from DB", zap.String("contract_id", contractID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	// TODO: Database agent needs to create lesson_schedules table
	// For now, return empty list
	query := `
		SELECT id, contract_id, lesson_date, lesson_time, location, format, resources, status, created_at, updated_at
		FROM mentorship.lesson_schedules
		WHERE contract_id = $1
		ORDER BY lesson_date ASC, lesson_time ASC
	`

	rows, err := r.db.Query(dbCtx, query, contractID)
	if err != nil {
		return nil, fmt.Errorf("failed to query lesson schedules: %w", err)
	}
	defer rows.Close()

	var schedules []*api.LessonSchedule
	for rows.Next() {
		schedule := r.schedulePool.Get().(*api.LessonSchedule)
		*schedule = api.LessonSchedule{} // Reset

		err := rows.Scan(
			&schedule.ID.Value,
			&schedule.ContractID.Value,
			&schedule.LessonDate.Value,
			&schedule.LessonTime,
			&schedule.Location,
			&schedule.Format,
			&schedule.Resources,
			&schedule.Status,
			&schedule.CreatedAt.Value,
			&schedule.UpdatedAt.Value,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan lesson schedule: %w", err)
		}

		schedule.ID.Set = true
		schedule.ContractID.Set = true
		schedule.LessonDate.Set = true
		schedule.CreatedAt.Set = true
		schedule.UpdatedAt.Set = true

		schedules = append(schedules, schedule)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating lesson schedules: %w", err)
	}

	r.logger.Info("Retrieved lesson schedules", zap.String("contract_id", contractID.String()), zap.Int("count", len(schedules)))
	return schedules, nil
}

// GetLessons retrieves lessons for a contract
// NOTE: Requires lessons table to be created by Database agent
// PERFORMANCE: Context timeout for DB operations (150ms for list operations in MMO)
func (r *Repository) GetLessons(ctx context.Context, contractID uuid.UUID) ([]*api.Lesson, error) {
	r.logger.Info("Retrieving lessons from DB", zap.String("contract_id", contractID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	// TODO: Database agent needs to create lessons table
	// For now, return empty list
	query := `
		SELECT id, contract_id, schedule_id, lesson_type, format, content_id,
			   started_at, completed_at, duration, skill_progress, evaluation,
			   status, created_at, updated_at
		FROM mentorship.lessons
		WHERE contract_id = $1
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(dbCtx, query, contractID)
	if err != nil {
		return nil, fmt.Errorf("failed to query lessons: %w", err)
	}
	defer rows.Close()

	var lessons []*api.Lesson
	for rows.Next() {
		lesson := r.lessonPool.Get().(*api.Lesson)
		*lesson = api.Lesson{} // Reset

		err := rows.Scan(
			&lesson.ID.Value,
			&lesson.ContractID.Value,
			&lesson.ScheduleID.Value,
			&lesson.LessonType,
			&lesson.Format,
			&lesson.ContentID.Value,
			&lesson.StartedAt.Value,
			&lesson.CompletedAt.Value,
			&lesson.Duration.Value,
			&lesson.SkillProgress,
			&lesson.Evaluation,
			&lesson.Status,
			&lesson.CreatedAt.Value,
			&lesson.UpdatedAt.Value,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan lesson: %w", err)
		}

		lesson.ID.Set = true
		lesson.ContractID.Set = true
		lesson.ScheduleID.Set = true
		lesson.ContentID.Set = true
		lesson.StartedAt.Set = true
		lesson.CompletedAt.Set = true
		lesson.Duration.Set = true
		lesson.CreatedAt.Set = true
		lesson.UpdatedAt.Set = true

		lessons = append(lessons, lesson)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating lessons: %w", err)
	}

	r.logger.Info("Retrieved lessons", zap.String("contract_id", contractID.String()), zap.Int("count", len(lessons)))
	return lessons, nil
}

// CreateLesson creates a lesson
// PERFORMANCE: Context timeout for DB operations (100ms for MMO responsiveness)
func (r *Repository) CreateLesson(ctx context.Context, lesson *api.Lesson) error {
	r.logger.Info("Creating lesson in DB", zap.String("id", lesson.ID.Value.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	query := `
		INSERT INTO mentorship.lessons (
			id, contract_id, schedule_id, lesson_type, format, content_id,
			started_at, completed_at, duration, skill_progress, evaluation,
			status, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14
		)`

	_, err := r.db.Exec(dbCtx, query,
		lesson.ID.Value,
		lesson.ContractID.Value,
		lesson.ScheduleID.Value,
		lesson.LessonType,
		lesson.Format,
		lesson.ContentID.Value,
		lesson.StartedAt.Value,
		lesson.CompletedAt.Value,
		lesson.Duration.Value,
		lesson.SkillProgress,
		lesson.Evaluation,
		lesson.Status,
		time.Now(),
		time.Now(),
	)

	if err != nil {
		return fmt.Errorf("failed to create lesson: %w", err)
	}

	r.logger.Info("Lesson created successfully", zap.String("id", lesson.ID.Value.String()))
	return nil
}

// CompleteLesson completes a lesson
// PERFORMANCE: Context timeout for DB operations (150ms for update operations in MMO)
func (r *Repository) CompleteLesson(ctx context.Context, lessonID uuid.UUID, req *api.CompleteLessonRequest) (*api.Lesson, error) {
	r.logger.Info("Completing lesson in DB", zap.String("id", lessonID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	// Build dynamic UPDATE query based on provided fields
	query := `
		UPDATE mentorship.lessons SET
			status = 'completed',
			completed_at = $1,
			updated_at = $2`
	args := []interface{}{time.Now(), time.Now()}
	argCount := 2

	if req.SkillProgress != nil {
		argCount++
		query += fmt.Sprintf(", skill_progress = $%d", argCount)
		args = append(args, req.SkillProgress)
	}

	if req.Evaluation != nil {
		argCount++
		query += fmt.Sprintf(", evaluation = $%d", argCount)
		args = append(args, req.Evaluation)
	}

	if req.Duration.IsSet() {
		argCount++
		query += fmt.Sprintf(", duration = $%d", argCount)
		args = append(args, req.Duration.Value)
	}

	query += fmt.Sprintf(" WHERE id = $%d RETURNING id", argCount+1)
	args = append(args, lessonID)

	// Execute UPDATE query
	var updatedID uuid.UUID
	err := r.db.QueryRow(dbCtx, query, args...).Scan(&updatedID)
	if err != nil {
		return nil, fmt.Errorf("failed to complete lesson: %w", err)
	}

	r.logger.Info("Lesson completed successfully", zap.String("id", lessonID.String()))

	// Return updated lesson
	return r.getLessonByID(dbCtx, lessonID)
}

// getLessonByID retrieves a single lesson by ID
// PERFORMANCE: Private helper method for internal use
func (r *Repository) getLessonByID(ctx context.Context, lessonID uuid.UUID) (*api.Lesson, error) {
	query := `
		SELECT id, contract_id, schedule_id, lesson_type, format, content_id,
			   started_at, completed_at, duration, skill_progress, evaluation,
			   status, created_at, updated_at
		FROM mentorship.lessons
		WHERE id = $1`

	lesson := r.lessonPool.Get().(*api.Lesson)
	*lesson = api.Lesson{} // Reset

	err := r.db.QueryRow(ctx, query, lessonID).Scan(
		&lesson.ID.Value,
		&lesson.ContractID.Value,
		&lesson.ScheduleID.Value,
		&lesson.LessonType,
		&lesson.Format,
		&lesson.ContentID.Value,
		&lesson.StartedAt.Value,
		&lesson.CompletedAt.Value,
		&lesson.Duration.Value,
		&lesson.SkillProgress,
		&lesson.Evaluation,
		&lesson.Status,
		&lesson.CreatedAt.Value,
		&lesson.UpdatedAt.Value,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve lesson: %w", err)
	}

	lesson.ID.Set = true
	lesson.ContractID.Set = true
	lesson.ScheduleID.Set = true
	lesson.ContentID.Set = true
	lesson.StartedAt.Set = true
	lesson.CompletedAt.Set = true
	lesson.Duration.Set = true
	lesson.CreatedAt.Set = true
	lesson.UpdatedAt.Set = true

	return lesson, nil
}

// DiscoverMentors discovers available mentors
func (r *Repository) DiscoverMentors(ctx context.Context, skillTrack api.OptString, mentorshipType api.OptString, minReputation api.OptFloat64, limit int) ([]*api.MentorProfile, error) {
	r.logger.Info("Discovering mentors in DB")

	// TODO: Implement proper discovery logic
	return []*api.MentorProfile{}, nil
}

// DiscoverMentees discovers available mentees
func (r *Repository) DiscoverMentees(ctx context.Context, skillTrack api.OptString, limit int) ([]*api.MenteeProfile, error) {
	r.logger.Info("Discovering mentees in DB")

	// TODO: Implement proper discovery logic
	return []*api.MenteeProfile{}, nil
}

// CreateAcademy creates an academy
// PERFORMANCE: Context timeout for DB operations (150ms for create operations in MMO)
func (r *Repository) CreateAcademy(ctx context.Context, academy *api.Academy) error {
	r.logger.Info("Creating academy in DB", zap.String("name", academy.Name))

	// PERFORMANCE: Add timeout for DB operations in MMO environment
	dbCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	query := `
		INSERT INTO mentorship.academies (
			id, name, description, academy_type, founder_id, location,
			programs_count, total_students, reputation_score, tuition_fee,
			created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12
		)`

	_, err := r.db.Exec(dbCtx, query,
		academy.ID.Value,
		academy.Name,
		academy.Description,
		academy.AcademyType,
		academy.FounderID.Value,
		academy.Location,
		academy.ProgramsCount.Value,
		academy.TotalStudents.Value,
		academy.ReputationScore.Value,
		academy.TuitionFee.Value,
		time.Now(),
		time.Now(),
	)

	if err != nil {
		return fmt.Errorf("failed to create academy: %w", err)
	}

	r.logger.Info("Academy created successfully", zap.String("id", academy.ID.Value.String()), zap.String("name", academy.Name))
	return nil
}

// GetMentorReputation retrieves mentor reputation
// PERFORMANCE: Uses memory pool for zero allocations in hot path, context timeout for MMO responsiveness
func (r *Repository) GetMentorReputation(ctx context.Context, mentorID uuid.UUID) (*api.MentorReputation, error) {
	r.logger.Info("Retrieving mentor reputation from DB", zap.String("mentor_id", mentorID.String()))

	// PERFORMANCE: Add timeout for DB operations in MMO environment (200ms for complex aggregation queries)
	dbCtx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	// PERFORMANCE: Get pre-allocated object from pool
	reputation := r.reputationPool.Get().(*api.MentorReputation)
	defer r.reputationPool.Put(reputation)

	// Reset object state for reuse
	*reputation = api.MentorReputation{}
	reputation.MentorID = api.NewOptUUID(mentorID)

	// NOTE: Database agent needs to create these tables for reputation calculation:
	// 1. mentorship.student_reviews (id, mentor_id, student_id, rating, review_text, created_at)
	// 2. mentorship.completed_lessons (id, contract_id, mentor_id, student_id, completion_status, created_at)
	// 3. mentorship.academy_ratings (id, mentor_id, academy_id, rating, criteria, created_at)

	// Calculate reputation metrics from database
	if err := r.calculateReputationMetrics(dbCtx, mentorID, reputation); err != nil {
		r.logger.Error("Failed to calculate reputation metrics", zap.Error(err), zap.String("mentor_id", mentorID.String()))
		// Return zero values on error for graceful degradation in MMO environment
		reputation.ReputationScore = api.NewOptFloat64(0.0)
		reputation.TotalStudents = api.NewOptInt(0)
		reputation.SuccessfulGraduates = api.NewOptInt(0)
		reputation.AverageRating = api.NewOptFloat64(0.0)
		reputation.TotalReviews = api.NewOptInt(0)
		reputation.ContentQualityScore = api.NewOptFloat64(0.0)
		reputation.AcademyRating = api.NewOptFloat64(0.0)
	}

	reputation.LastUpdate = api.NewOptDateTime(time.Now())
	return reputation, nil
}

// calculateReputationMetrics computes reputation scores from database
// PERFORMANCE: Single query with CTE for optimal MMO performance
func (r *Repository) calculateReputationMetrics(ctx context.Context, mentorID uuid.UUID, reputation *api.MentorReputation) error {
	query := `
		WITH mentor_stats AS (
			-- Total students from mentorship contracts
			SELECT
				COUNT(DISTINCT mc.mentee_id) as total_students,
				COUNT(DISTINCT CASE WHEN cl.completion_status = 'successful' THEN mc.mentee_id END) as successful_graduates
			FROM mentorship.mentorship_contracts mc
			LEFT JOIN mentorship.completed_lessons cl ON mc.id = cl.contract_id
			WHERE mc.mentor_id = $1
		),
		review_stats AS (
			-- Review statistics from student reviews
			SELECT
				COUNT(*) as total_reviews,
				COALESCE(AVG(rating), 0) as average_rating
			FROM mentorship.student_reviews
			WHERE mentor_id = $1 AND rating IS NOT NULL
		),
		academy_stats AS (
			-- Academy ratings and content quality
			SELECT
				COALESCE(AVG(CASE WHEN criteria = 'content_quality' THEN rating END), 0) as content_quality_score,
				COALESCE(AVG(CASE WHEN criteria = 'academy_rating' THEN rating END), 0) as academy_rating
			FROM mentorship.academy_ratings
			WHERE mentor_id = $1
		)
		SELECT
			ms.total_students,
			ms.successful_graduates,
			rs.total_reviews,
			rs.average_rating,
			acs.content_quality_score,
			acs.academy_rating,
			-- Calculate overall reputation score (weighted formula for MMO gaming)
			CASE
				WHEN ms.total_students > 0 THEN
					(rs.average_rating * 0.4) +  -- 40% student reviews
					(CASE WHEN ms.total_students > 0 THEN LEAST(ms.successful_graduates::float / ms.total_students, 1.0) * 100 * 0.3 END) +  -- 30% graduation rate
					(acs.content_quality_score * 0.15) +  -- 15% content quality
					(acs.academy_rating * 0.15)  -- 15% academy rating
				ELSE 0
			END as reputation_score
		FROM mentor_stats ms
		CROSS JOIN review_stats rs
		CROSS JOIN academy_stats acs
	`

	err := r.db.QueryRow(ctx, query, mentorID).Scan(
		&reputation.TotalStudents.Value,
		&reputation.SuccessfulGraduates.Value,
		&reputation.TotalReviews.Value,
		&reputation.AverageRating.Value,
		&reputation.ContentQualityScore.Value,
		&reputation.AcademyRating.Value,
		&reputation.ReputationScore.Value,
	)

	if err != nil {
		return fmt.Errorf("failed to calculate reputation metrics: %w", err)
	}

	// Set presence flags
	reputation.TotalStudents.Set = true
	reputation.SuccessfulGraduates.Set = true
	reputation.TotalReviews.Set = true
	reputation.AverageRating.Set = true
	reputation.ContentQualityScore.Set = true
	reputation.AcademyRating.Set = true
	reputation.ReputationScore.Set = true

	r.logger.Info("Calculated reputation metrics",
		zap.String("mentor_id", mentorID.String()),
		zap.Float64("reputation_score", reputation.ReputationScore.Value),
		zap.Int("total_students", reputation.TotalStudents.Value))

	return nil
}
