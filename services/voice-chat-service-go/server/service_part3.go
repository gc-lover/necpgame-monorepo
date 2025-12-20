// Background cleanup routines
func (s *VoiceChatService) cleanupInactiveChannels() {
	ticker := time.NewTicker(s.config.ChannelCleanupInterval)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()
		s.channels.Range(func(key, value interface{}) bool {
			channel := value.(*VoiceChannel)

			// Remove channels with no activity for extended period
			if now.Sub(channel.LastActivity) > 24*time.Hour && channel.ParticipantCount == 0 {
				s.channels.Delete(key)
				s.metrics.ActiveChannels.Dec()
				s.logger.WithField("channel_id", channel.ChannelID).Info("inactive channel cleaned up")
			}
			return true
		})
	}
}

func (s *VoiceChatService) cleanupStaleConnections() {
	ticker := time.NewTicker(s.config.ConnectionCleanupInterval)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()
		s.connections.Range(func(key, value interface{}) bool {
			conn := value.(*WSVoiceConnection)

			// Remove stale connections (no heartbeat for 5 minutes)
			if now.Sub(conn.LastHeartbeat) > 5*time.Minute {
				if conn.Conn != nil {
					conn.Conn.Close()
				}
				s.connections.Delete(key)
				s.metrics.ActiveConnections.Dec()
				s.logger.WithField("connection_id", conn.ConnectionID).Info("stale connection cleaned up")
			}
			return true
		})
	}
}

func (s *VoiceChatService) updateProximityCalculations() {
	ticker := time.NewTicker(s.config.ProximityUpdateInterval)
	defer ticker.Stop()

	for range ticker.C {
		// Update proximity calculations for all channels
		s.channels.Range(func(key, value interface{}) bool {
			channel := value.(*VoiceChannel)

			// Skip non-proximity channels
			if channel.Type != "proximity" {
				return true
			}

			// Update proximity zones
			channel.Participants.Range(func(key, value interface{}) bool {
				conn := value.(*WSVoiceConnection)
				if conn.Location != nil {
					s.updateUserProximity(channel, conn)
				}
				return true
			})
			return true
		})
	}
}

// Helper methods
func (s *VoiceChatService) validateChannelRequest(req *CreateChannelRequest) error {
	if req.Name == "" {
		return fmt.Errorf("channel name is required")
	}
	if len(req.Name) < 1 || len(req.Name) > 100 {
		return fmt.Errorf("channel name must be between 1 and 100 characters")
	}
	validTypes := map[string]bool{"global": true, "guild": true, "group": true, "proximity": true, "private": true}
	if !validTypes[req.Type] {
		return fmt.Errorf("invalid channel type")
	}
	return nil
}

func (s *VoiceChatService) channelExists(name string) bool {
	found := false
	s.channels.Range(func(key, value interface{}) bool {
		channel := value.(*VoiceChannel)
		if channel.Name == name {
			found = true
			return false
		}
		return true
	})
	return found
}

func (s *VoiceChatService) calculateDistance(pos1, pos2 PlayerLocation) float64 {
	dx := pos1.X - pos2.X
	dy := pos1.Y - pos2.Y
	dz := pos1.Z - pos2.Z
	return math.Sqrt(dx*dx + dy*dy + dz*dz)
}

func (s *VoiceChatService) calculateVolume(distance, maxDistance float64) float64 {
	if distance >= maxDistance {
		return 0.0
	}
	// Linear falloff
	return 1.0 - (distance / maxDistance)
}

func (s *VoiceChatService) updateUserProximity(channel *VoiceChannel, userConn *WSVoiceConnection) {
	// Update proximity calculations for the user
	// This would involve spatial partitioning and efficient neighbor finding
	// For now, it's a placeholder
}

// Missing imports
import (
	"golang.org/x/time/rate"
)
