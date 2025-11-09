import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SyncStatusCard } from '../SyncStatusCard'

describe('SyncStatusCard', () => {
  it('displays sync status and nodes', () => {
    render(
      <SyncStatusCard
        syncStatus="DEGRADED"
        syncQueueSize={12}
        successRate={78}
        nodes={[
          { node: 'shard-eu', latencyMs: 42, driftMs: 12, lastAck: '2077-11-08 00:05' },
        ]}
      />,
    )

    expect(screen.getByText(/Sync Status/i)).toBeInTheDocument()
    expect(screen.getByText(/shard-eu/i)).toBeInTheDocument()
  })
})


