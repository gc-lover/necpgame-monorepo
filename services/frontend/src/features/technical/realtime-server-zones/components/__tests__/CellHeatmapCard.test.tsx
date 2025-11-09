import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CellHeatmapCard } from '../../components/CellHeatmapCard'

describe('CellHeatmapCard', () => {
  it('renders cell metrics', () => {
    render(
      <CellHeatmapCard
        cells={[
          { cellKey: 'A1', playerCount: 64, npcCount: 32, latencyMs: 35 },
        ]}
      />,
    )

    expect(screen.getByText(/A1/i)).toBeInTheDocument()
    expect(screen.getByText(/players/i)).toBeInTheDocument()
  })
})


