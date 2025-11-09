import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SupportAssetCard } from '../SupportAssetCard'

describe('SupportAssetCard', () => {
  it('renders support asset info', () => {
    render(
      <SupportAssetCard
        asset={{
          assetId: 'asset-drone',
          name: 'Aquila Drone Squad',
          type: 'DRONE',
          status: 'READY',
          upkeepCost: 1200,
          capacity: 4,
          utilization: 65,
          bonuses: ['Recon mapping', 'Suppressive fire'],
        }}
      />,
    )

    expect(screen.getByText(/Aquila Drone Squad/i)).toBeInTheDocument()
    expect(screen.getByText(/Recon mapping/i)).toBeInTheDocument()
  })
})


