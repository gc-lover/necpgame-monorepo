// Issue: #1515 - Weekly affix rotation scheduler
package server

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// AffixScheduler handles automatic weekly rotation of affixes
type AffixScheduler struct {
	affixService AffixServiceInterface
	logger       *logrus.Logger
	ticker       *time.Ticker
	stopChan     chan bool
}

// NewAffixScheduler creates a new scheduler for affix rotations
func NewAffixScheduler(db *pgxpool.Pool, logger *logrus.Logger) *AffixScheduler {
	if db == nil {
		return nil
	}

	affixService := NewAffixService(db)
	return &AffixScheduler{
		affixService: affixService,
		logger:       logger,
		ticker:       time.NewTicker(1 * time.Hour), // Check every hour
		stopChan:     make(chan bool),
	}
}

// Start begins the scheduler goroutine
func (s *AffixScheduler) Start() {
	if s == nil {
		return
	}

	go func() {
		// Check immediately on start
		s.checkAndRotate(context.Background())

		for {
			select {
			case <-s.ticker.C:
				s.checkAndRotate(context.Background())
			case <-s.stopChan:
				s.ticker.Stop()
				return
			}
		}
	}()

	s.logger.Info("Affix rotation scheduler started (checks every hour, rotates Monday 00:00 UTC)")
}

// Stop stops the scheduler
func (s *AffixScheduler) Stop() {
	if s == nil {
		return
	}
	close(s.stopChan)
	s.logger.Info("Affix rotation scheduler stopped")
}

// checkAndRotate checks if rotation is needed and triggers it
func (s *AffixScheduler) checkAndRotate(ctx context.Context) {
	now := time.Now().UTC()
	
	// Check if it's Monday 00:00-00:59 UTC
	if now.Weekday() != time.Monday || now.Hour() != 0 {
		return
	}

	// Check if there's already an active rotation for this week
	active, err := s.affixService.GetActiveAffixes(ctx)
	if err == nil && active != nil {
		// Check if rotation is for current week
		weekStart := getNextMonday(now.AddDate(0, 0, -7))
		if active.WeekStart.Equal(weekStart) || active.WeekStart.After(weekStart.AddDate(0, 0, -1)) {
			// Rotation already exists for this week
			return
		}
	}

	// Trigger automatic rotation
	s.logger.Info("Triggering automatic affix rotation for new week")
	_, err = s.affixService.TriggerRotation(ctx, false, nil)
	if err != nil {
		s.logger.WithError(err).Error("Failed to trigger automatic affix rotation")
	} else {
		s.logger.Info("Automatic affix rotation completed successfully")
	}
}

