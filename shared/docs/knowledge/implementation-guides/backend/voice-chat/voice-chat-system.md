---

- **Status:** queued
- **Last Updated:** 2025-11-07 16:20
---

---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:40
**api-readiness-notes:** Voice Chat System. Голосовое общение, каналы, proximity chat, quality settings. ~380 строк.
---

# Voice Chat System - Голосовой чат

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:40  
**Приоритет:** HIGH (Team Communication!)  
**Автор:** AI Brain Manager

**Микрофича:** Voice chat & audio communication  
**Размер:** ~380 строк ✅

---

## Краткое описание

**Voice Chat System** - система голосового общения для команд и кланов.

**Ключевые возможности:**
- ✅ Party Voice Chat (голос в группе)
- ✅ Guild Voice Chat (голос в клане)
- ✅ Proximity Voice Chat (пространственный звук)
- ✅ Push-to-Talk / Voice Activation
- ✅ Mute/Deafen Controls
- ✅ Voice Quality Settings

---

## Архитектура системы

```
Client (Microphone)
    ↓
WebRTC Connection
    ↓
Voice Server (Agora/Twilio/Self-hosted)
    ↓
Mixer & Router
    ↓
Deliver to channel participants
    ↓
Client (Speakers/Headphones)
```

---

## Voice Channel Types

### 1. Party Channel (Группа)

```
Participants: 2-5 players
Activation: Auto-join when in party
Quality: High (48kHz, 128kbps)
Latency: <50ms
```

### 2. Guild Channel (Клан)

```
Participants: Up to 100 players
Sub-channels: Multiple rooms
Quality: Medium (44.1kHz, 96kbps)
Permissions: Role-based access
```

### 3. Proximity Chat (Пространственный)

```
Range: 20 meters
Falloff: Linear distance
Quality: Medium (44.1kHz, 64kbps)
3D Audio: Directional sound
```

### 4. Raid Channel (Рейд)

```
Participants: 10-25 players
Sub-channels: By role (tanks, healers, DPS)
Quality: High (48kHz, 128kbps)
Commander mode: Raid leader can broadcast
```

---

## Database Schema

### Таблица `voice_channels`

```sql
CREATE TABLE voice_channels (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Channel info
    channel_type VARCHAR(50) NOT NULL,
    name VARCHAR(200),
    
    -- Owner
    owner_type VARCHAR(20) NOT NULL,
    owner_id UUID NOT NULL,
    
    -- Settings
    max_participants INTEGER DEFAULT 100,
    quality_preset VARCHAR(20) DEFAULT 'MEDIUM',
    
    -- Permissions
    is_public BOOLEAN DEFAULT FALSE,
    allowed_roles VARCHAR(50)[],
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    participant_count INTEGER DEFAULT 0,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_voice_channels_owner ON voice_channels(owner_type, owner_id);
CREATE INDEX idx_voice_channels_active ON voice_channels(is_active) 
    WHERE is_active = TRUE;
```

### Таблица `voice_participants`

```sql
CREATE TABLE voice_participants (
    channel_id UUID NOT NULL,
    player_id UUID NOT NULL,
    
    -- Connection
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    connection_id VARCHAR(255),
    
    -- Status
    is_speaking BOOLEAN DEFAULT FALSE,
    is_muted BOOLEAN DEFAULT FALSE,
    is_deafened BOOLEAN DEFAULT FALSE,
    
    -- Quality
    audio_quality VARCHAR(20) DEFAULT 'AUTO',
    
    -- Stats
    total_speak_time INTEGER DEFAULT 0,
    
    PRIMARY KEY (channel_id, player_id),
    
    CONSTRAINT fk_voice_participant_channel FOREIGN KEY (channel_id) 
        REFERENCES voice_channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_voice_participant_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_voice_participants_player ON voice_participants(player_id);
```

---

## WebRTC Integration

### Establish Connection

```java
@Service
public class VoiceConnectionService {
    
    public VoiceConnectionToken joinVoiceChannel(UUID playerId, UUID channelId) {
        VoiceChannel channel = channelRepository.findById(channelId)
            .orElseThrow();
        
        // Validate access
        if (!canJoinChannel(playerId, channel)) {
            throw new AccessDeniedException();
        }
        
        // Check participant limit
        if (channel.getParticipantCount() >= channel.getMaxParticipants()) {
            throw new ChannelFullException();
        }
        
        // Create participant entry
        VoiceParticipant participant = new VoiceParticipant();
        participant.setChannelId(channelId);
        participant.setPlayerId(playerId);
        participant.setConnectionId(generateConnectionId());
        
        participantRepository.save(participant);
        
        // Update channel count
        channel.setParticipantCount(channel.getParticipantCount() + 1);
        channelRepository.save(channel);
        
        // Generate WebRTC token (using Agora/Twilio SDK)
        VoiceConnectionToken token = voiceProvider.generateToken(
            channelId.toString(),
            playerId.toString(),
            channel.getQualityPreset()
        );
        
        // Notify other participants
        notifyParticipantJoined(channelId, playerId);
        
        log.info("Player joined voice channel: player={}, channel={}", 
            playerId, channelId);
        
        return token;
    }
    
    public void leaveVoiceChannel(UUID playerId, UUID channelId) {
        participantRepository.deleteByChannelAndPlayer(channelId, playerId);
        
        // Update count
        VoiceChannel channel = channelRepository.findById(channelId)
            .orElseThrow();
        channel.setParticipantCount(
            Math.max(0, channel.getParticipantCount() - 1)
        );
        channelRepository.save(channel);
        
        // Notify
        notifyParticipantLeft(channelId, playerId);
    }
}
```

---

## Proximity Voice Chat

### Spatial Audio

```typescript
// Client-side: Calculate 3D audio positioning
function updateProximityVoice(localPlayer: Player, remotePlayers: Player[]) {
  for (const remote of remotePlayers) {
    const distance = calculateDistance(localPlayer.position, remote.position);
    
    // Max range: 20 meters
    if (distance > 20) {
      mutePlayer(remote.id);
      continue;
    }
    
    // Calculate volume based on distance
    const volume = 1.0 - (distance / 20); // Linear falloff
    setPlayerVolume(remote.id, volume);
    
    // Calculate 3D position
    const direction = calculateDirection(localPlayer.position, remote.position);
    setPlayer3DPosition(remote.id, direction, distance);
  }
}
```

---

## Voice Controls

### Mute/Deafen

```java
public void mutePlayer(UUID playerId, UUID channelId) {
    VoiceParticipant participant = getParticipant(playerId, channelId);
    
    participant.setIsMuted(true);
    participantRepository.save(participant);
    
    // Send signal to voice server
    voiceProvider.muteUser(channelId.toString(), playerId.toString());
    
    // Notify channel
    notifyMuteStatus(channelId, playerId, true);
}

public void toggleDeafen(UUID playerId, UUID channelId) {
    VoiceParticipant participant = getParticipant(playerId, channelId);
    
    boolean newDeafenState = !participant.isDeafened();
    
    participant.setIsDeafened(newDeafenState);
    participant.setIsMuted(newDeafenState); // Deafen auto-mutes
    
    participantRepository.save(participant);
    
    // Client-side handling (mute all incoming audio)
}
```

---

## Quality Settings

```java
public enum VoiceQuality {
    LOW(22050, 32, "bandwidth_saving"),      // 32kbps
    MEDIUM(44100, 64, "balanced"),           // 64kbps
    HIGH(48000, 128, "high_quality"),        // 128kbps
    ULTRA(48000, 256, "studio_quality");     // 256kbps (premium)
    
    private final int sampleRate;
    private final int bitrate;
    private final String profile;
}
```

---

## Moderation

### Voice Moderation Tools

```java
@Service
public class VoiceModeration {
    
    public void mutePlayerInChannel(UUID moderatorId, 
                                   UUID playerId, 
                                   UUID channelId,
                                   Duration duration) {
        // Check permissions
        if (!hasModeratePermission(moderatorId, channelId)) {
            throw new InsufficientPermissionsException();
        }
        
        // Mute player
        mutePlayer(playerId, channelId);
        
        // Schedule unmute
        if (duration != null) {
            scheduler.schedule(
                () -> unmutePlayer(playerId, channelId),
                duration.toMillis(),
                TimeUnit.MILLISECONDS
            );
        }
        
        // Audit log
        auditLog(moderatorId, "VOICE_MUTE", Map.of(
            "targetPlayer", playerId,
            "channel", channelId,
            "duration", duration
        ));
        
        // Notify player
        notificationService.send(playerId,
            new VoiceMutedNotification(channelId, duration));
    }
}
```

---

## API Endpoints

**POST `/api/v1/voice/join`** - join voice channel

```json
Request:
{
  "channelId": "uuid",
  "audioQuality": "MEDIUM"
}

Response:
{
  "token": "webrtc_token_here",
  "channelId": "uuid",
  "expiresAt": "2025-11-07T06:00:00Z",
  "iceServers": [...],
  "participants": [
    {"playerId": "uuid", "username": "V", "isSpeaking": false}
  ]
}
```

**POST `/api/v1/voice/leave`** - leave channel

**POST `/api/v1/voice/mute`** - mute self

**POST `/api/v1/voice/deafen`** - deafen self

**GET `/api/v1/voice/channels/{id}/participants`** - участники

---

## Связанные документы

- [Party System](../party-system.md)
- [Guild System](../guild-system-backend.md)
- [Chat System](../chat/chat-channels.md)
