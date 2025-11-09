export interface QueueStatus {
  mode: string
  population: number
  estimatedWait: string
  inReadyCheck: number
  activeTickets: number
}

export interface MatchTicket {
  ticketId: string
  mode: string
  players: number
  latencyMs: number
  status: 'SEARCHING' | 'READY_CHECK' | 'CONFIRMED'
  createdAt: string
}

export interface ReadyCheckState {
  matchId: string
  expiresInSeconds: number
  accepted: number
  declined: number
  pending: number
}

export interface QualityMetric {
  name: string
  target: string
  current: string
  status: 'OK' | 'WARN' | 'ALERT'
}

export interface TelemetryPoint {
  label: string
  value: number
  percentile: number
}

export interface AnalyticsSnapshot {
  matchesToday: number
  averageWait: string
  cancellations: number
  dodges: number
}


