import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ChainStagesCard } from '../ChainStagesCard'

describe('ChainStagesCard', () => {
  it('renders chain stages info', () => {
    render(
      <ChainStagesCard
        finalProduct="Legendary Weapon"
        stages=[
          {
            stageId: 'stage-1',
            name: 'Smelt Ingots',
            duration: '30m',
            inputs: 'Ore x10',
            outputs: 'Ingots x5',
          },
        ]
      />,
    )

    expect(screen.getByText(/Smelt Ingots/i)).toBeInTheDocument()
    expect(screen.getByText(/Ore x10/i)).toBeInTheDocument()
  })
})


