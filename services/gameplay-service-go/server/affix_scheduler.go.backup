package server

import (
	"context"
	"time"

	"github.com/sirupsen/logrus"
)

type AffixScheduler struct {
	service AffixServiceInterface
	logger  *logrus.Logger
	stopCh  chan struct{}
}

func NewAffixScheduler(service AffixServiceInterface) *AffixScheduler {
	return &AffixScheduler{
		service: service,
		logger:  GetLogger(),
		stopCh:  make(chan struct{}),
	}
}

func (s *AffixScheduler) Start() {
	go s.run()
	s.logger.Info("Affix rotation scheduler started")
}

func (s *AffixScheduler) Stop() {
	close(s.stopCh)
	s.logger.Info("Affix rotation scheduler stopped")
}

func (s *AffixScheduler) run() {
	now := time.Now()
	nextMonday := GetNextMonday(now)
	duration := nextMonday.Sub(now)

	s.logger.WithField("next_rotation", nextMonday).Info("Scheduled next affix rotation")

	ticker := time.NewTicker(24 * time.Hour)
	defer ticker.Stop()

	initialTimer := time.NewTimer(duration)
	defer initialTimer.Stop()

	select {
	case <-initialTimer.C:
		s.triggerRotation()
	case <-s.stopCh:
		return
	}

	for {
		select {
		case <-ticker.C:
			if isMonday(time.Now()) {
				s.triggerRotation()
			}
		case <-s.stopCh:
			return
		}
	}
}

func (s *AffixScheduler) triggerRotation() {
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	s.logger.Info("Triggering weekly affix rotation")
	_, err := s.service.TriggerRotation(ctx, false, nil)
	if err != nil {
		if err.Error() == "rotation already exists for this week" {
			s.logger.Info("Rotation already exists for this week, skipping")
			return
		}
		s.logger.WithError(err).Error("Failed to trigger affix rotation")
		return
	}

	s.logger.Info("Affix rotation completed successfully")
}

func GetNextMonday(t time.Time) time.Time {
	weekday := int(t.Weekday())
	if weekday == 0 {
		weekday = 7
	}
	daysUntilMonday := 8 - weekday
	if daysUntilMonday == 7 {
		daysUntilMonday = 0
	}
	nextMonday := t.AddDate(0, 0, daysUntilMonday)
	nextMonday = time.Date(nextMonday.Year(), nextMonday.Month(), nextMonday.Day(), 0, 0, 0, 0, time.UTC)
	return nextMonday
}

func isMonday(t time.Time) bool {
	return t.Weekday() == time.Monday
}

