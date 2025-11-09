export interface DrStatus {
  ready: boolean
  lastBackup: string
  backupFrequency: string
  failoverReady: boolean
  rpoMinutes: number
  rtoMinutes: number
}

export interface BackupPlan {
  name: string
  cadence: string
  retention: string
  lastRun: string
  nextRun: string
}

export interface FailoverTarget {
  datacenter: string
  region: string
  latencyMs: number
  capacityPercent: number
}

export interface IncidentLogEntry {
  timestamp: string
  type: string
  summary: string
  status: string
}


