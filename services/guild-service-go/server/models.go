// Issue: #140889771
// PERFORMANCE: Optimized struct alignment and memory layout for guild operations

package server

// Re-export models from pkg/models to maintain backward compatibility
import "github.com/gc-lover/necpgame-monorepo/services/guild-service-go/pkg/models"

// Guild represents a player guild/clan
type Guild = models.Guild

// GuildMember represents a guild member
type GuildMember = models.GuildMember

// GuildAnnouncement represents a guild announcement
type GuildAnnouncement = models.GuildAnnouncement

// GuildVoiceChannel represents a guild voice channel integrated with WebRTC signaling
type GuildVoiceChannel = models.GuildVoiceChannel

// GuildVoiceParticipant represents a participant in a guild voice channel
type GuildVoiceParticipant = models.GuildVoiceParticipant
