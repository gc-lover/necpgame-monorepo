export interface IncidentSummary {
  id: string
  title: string
  severity: 'critical' | 'high' | 'medium' | 'low'
  status: 'detected' | 'acknowledged' | 'mitigated' | 'resolved'
  detectedAt: string
  commander: string
  affectedServices: string[]
}

export interface EscalationStatus {
  level: string
  target: string
  triggeredAt: string
  channel: string
  status: 'pending' | 'engaged' | 'completed'
}

export interface TimelineEvent {
  timestamp: string
  actor: string
  description: string
  category: 'detection' | 'mitigation' | 'communication' | 'recovery'
}

export interface RcaSummary {
  rootCause: string
  contributingFactors: string[]
  correctiveActions: Array<{ description: string; owner: string; dueDate: string; status: 'pending' | 'in_progress' | 'done' }>
  lessonsLearned: string[]
}

export interface OnCallInfo {
  currentResponder: string
  rotation: string
  timeRemaining: string
  nextUp: string
}


