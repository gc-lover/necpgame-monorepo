export interface VoiceChannelSummary {
  channelId: string
  channelName: string
  channelType: 'party' | 'guild' | 'raid' | 'proximity' | 'custom'
  owner: string
  isActive: boolean
  participants: number
  maxParticipants: number
}

export interface VoiceParticipant {
  playerId: string
  displayName: string
  role: 'leader' | 'moderator' | 'member'
  muted: boolean
  deafened: boolean
  speaking: boolean
  latencyMs: number
}

export interface VoiceControlsState {
  inputDevice: string
  outputDevice: string
  noiseSuppression: boolean
  echoCancellation: boolean
  spatialAudio: boolean
}

export interface SpatialAudioMetric {
  participantId: string
  angle: number
  distance: number
  volume: number
}

export interface ChannelSettings {
  qualityPreset: 'low' | 'medium' | 'high'
  autoCloseMinutes: number
  allowedRoles: string[]
  proximityEnabled: boolean
}

export interface QualityProfile {
  bitrateKbps: number
  packetLoss: number
  jitter: number
  status: 'excellent' | 'good' | 'degraded' | 'critical'
}


