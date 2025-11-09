export interface RealtimeInstance {
  instanceId: string
  region: string
  status: 'ONLINE' | 'MAINTENANCE' | 'DRAINING' | 'OFFLINE'
  tickRate: 20 | 30 | 60
  maxPlayers: number
  activePlayers: number
  maxZones: number
  supportedZoneTypes: string[]
  metadata: Record<string, string>
}

export interface ZoneSummary {
  zoneId: string
  zoneName: string
  status: 'ONLINE' | 'MAINTENANCE' | 'MIGRATING' | 'OFFLINE'
  assignedServerId: string
  playerCount: number
  npcCount: number
  isPvpEnabled: boolean
}

export interface ZoneTransferPlan {
  targetInstanceId: string
  drainStrategy: 'gradual' | 'immediate'
  priority: 'low' | 'medium' | 'high'
  reason: string
  scheduledFor: string
}

export interface EvacuationPlan {
  targetZoneId: string
  batchSize: number
  intervalMs: number
  notifyPlayers: boolean
  timeoutSeconds: number
}

export interface ZoneCellMetric {
  cellKey: string
  playerCount: number
  npcCount: number
  latencyMs: number
}

export interface TickMetric {
  timestamp: string
  tickDurationMs: number
  warnings: string[]
}

export interface AlertEvent {
  id: string
  level: 'info' | 'warning' | 'critical'
  message: string
  raisedAt: string
  resolvedAt?: string
}


