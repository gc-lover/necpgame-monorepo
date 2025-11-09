import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StateSnapshotCard } from '../StateSnapshotCard'

describe('StateSnapshotCard', () => {
  it('renders snapshot details', () => {
    render(
      <StateSnapshotCard
        snapshot={{
          snapshotId: 'snap-2077-11-07',
          createdAt: '2077-11-07 21:10',
          createdBy: 'System Scheduler',
          sizeMb: 128,
          tags: ['pre-raid', 'night-city'],
          rollbackAvailable: true,
        }}
      />,
    )

    expect(screen.getByText(/Snapshot snap-2077/i)).toBeInTheDocument()
    expect(screen.getByText(/128 MB/i)).toBeInTheDocument()
  })
})


