export interface SessionInfo {
  sessionId: string
  playerId: string
  characterId: string
  status: 'ACTIVE' | 'AFK' | 'DISCONNECTED' | 'TERMINATED'
  createdAt: string
  lastHeartbeatAt: string
  expiresAt: string
}

export interface HeartbeatMetric {
  timestamp: string
  latencyMs: number
  activity: 'active' | 'idle' | 'menu'
  warning?: 'LATE_HEARTBEAT' | 'NEAR_TIMEOUT'
}

export interface AfkWarning {
  triggeredAt: string
  timeoutSeconds: number
  reason: string
}

export interface ForceLogoutPlan {
  playerId: string
  accountId?: string
  notify: boolean
  scheduledFor: string
}

export interface SessionPolicy {
  name: string
  value: string
  description: string
}

export interface DiagnosticsEntry {
  label: string
  value: string
  status: 'ok' | 'warn' | 'error'
}


