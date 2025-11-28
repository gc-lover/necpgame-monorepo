package server

import (
	"context"
	"database/sql/driver"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type NotificationPreferencesRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewNotificationPreferencesRepository(db *pgxpool.Pool) *NotificationPreferencesRepository {
	return &NotificationPreferencesRepository{
		db:     db,
		logger: GetLogger(),
	}
}

type DeliveryChannelArray []models.DeliveryChannel

func (a DeliveryChannelArray) Value() (driver.Value, error) {
	if len(a) == 0 {
		return "{}", nil
	}
	channels := make([]string, len(a))
	for i, ch := range a {
		channels[i] = string(ch)
	}
	return json.Marshal(channels)
}

func (a *DeliveryChannelArray) Scan(value interface{}) error {
	if value == nil {
		*a = []models.DeliveryChannel{}
		return nil
	}

	var bytes []byte
	switch v := value.(type) {
	case []byte:
		bytes = v
	case string:
		bytes = []byte(v)
	default:
		return nil
	}

	var channels []string
	if err := json.Unmarshal(bytes, &channels); err != nil {
		return err
	}

	*a = make([]models.DeliveryChannel, len(channels))
	for i, ch := range channels {
		(*a)[i] = models.DeliveryChannel(ch)
	}
	return nil
}

func (r *NotificationPreferencesRepository) GetByAccountID(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	var prefs models.NotificationPreferences
	var channelsJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT account_id, quest_enabled, message_enabled, achievement_enabled, 
		 system_enabled, friend_enabled, guild_enabled, trade_enabled, combat_enabled,
		 preferred_channels, updated_at
		 FROM social.notification_preferences
		 WHERE account_id = $1`,
		accountID,
	).Scan(&prefs.AccountID, &prefs.QuestEnabled, &prefs.MessageEnabled, &prefs.AchievementEnabled,
		&prefs.SystemEnabled, &prefs.FriendEnabled, &prefs.GuildEnabled, &prefs.TradeEnabled,
		&prefs.CombatEnabled, &channelsJSON, &prefs.UpdatedAt)

	if err == pgx.ErrNoRows {
		return r.createDefaultPreferences(ctx, accountID)
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get notification preferences")
		return nil, err
	}

	var channels []string
	if err := json.Unmarshal(channelsJSON, &channels); err == nil {
		prefs.PreferredChannels = make([]models.DeliveryChannel, len(channels))
		for i, ch := range channels {
			prefs.PreferredChannels[i] = models.DeliveryChannel(ch)
		}
	}

	return &prefs, nil
}

func (r *NotificationPreferencesRepository) createDefaultPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	prefs := &models.NotificationPreferences{
		AccountID:          accountID,
		QuestEnabled:       true,
		MessageEnabled:     true,
		AchievementEnabled: true,
		SystemEnabled:      true,
		FriendEnabled:      true,
		GuildEnabled:       true,
		TradeEnabled:       true,
		CombatEnabled:      true,
		PreferredChannels:  []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
		UpdatedAt:          time.Now(),
	}

	channelsJSON, err := json.Marshal([]string{string(models.DeliveryChannelInGame), string(models.DeliveryChannelWebSocket)})
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal channels JSON")
		return prefs, err
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO social.notification_preferences 
		 (account_id, quest_enabled, message_enabled, achievement_enabled, 
		  system_enabled, friend_enabled, guild_enabled, trade_enabled, combat_enabled,
		  preferred_channels, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		 ON CONFLICT (account_id) DO NOTHING`,
		prefs.AccountID, prefs.QuestEnabled, prefs.MessageEnabled, prefs.AchievementEnabled,
		prefs.SystemEnabled, prefs.FriendEnabled, prefs.GuildEnabled, prefs.TradeEnabled,
		prefs.CombatEnabled, channelsJSON, prefs.UpdatedAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create default notification preferences")
		return prefs, err
	}

	return prefs, nil
}

func (r *NotificationPreferencesRepository) Update(ctx context.Context, prefs *models.NotificationPreferences) error {
	channelsJSON, err := json.Marshal(prefs.PreferredChannels)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal preferred channels JSON")
		return err
	}
	prefs.UpdatedAt = time.Now()

	_, err = r.db.Exec(ctx,
		`UPDATE social.notification_preferences
		 SET quest_enabled = $1, message_enabled = $2, achievement_enabled = $3,
		     system_enabled = $4, friend_enabled = $5, guild_enabled = $6,
		     trade_enabled = $7, combat_enabled = $8, preferred_channels = $9,
		     updated_at = $10
		 WHERE account_id = $11`,
		prefs.QuestEnabled, prefs.MessageEnabled, prefs.AchievementEnabled,
		prefs.SystemEnabled, prefs.FriendEnabled, prefs.GuildEnabled,
		prefs.TradeEnabled, prefs.CombatEnabled, channelsJSON, prefs.UpdatedAt,
		prefs.AccountID)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update notification preferences")
		return err
	}

	return nil
}

