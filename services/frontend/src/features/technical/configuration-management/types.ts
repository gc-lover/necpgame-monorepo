export interface ServiceConfigInfo {
  serviceName: string
  environment: string
  version: string
  configuration: Record<string, unknown>
}

export interface SecretMetadata {
  secretName: string
  createdAt: string
  updatedAt: string
}

export interface ReloadStatus {
  lastReload: string
  triggeredBy: string
  status: 'IDLE' | 'IN_PROGRESS' | 'SUCCESS' | 'FAILED'
}

export interface EnvironmentSummary {
  name: string
  services: number
  overrides: number
  driftAlerts: number
}


