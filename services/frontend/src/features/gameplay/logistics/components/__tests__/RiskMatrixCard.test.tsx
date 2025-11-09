import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RiskMatrixCard } from '../RiskMatrixCard'

describe('RiskMatrixCard', () => {
  it('renders risk mitigation', () => {
    render(
      <RiskMatrixCard
        risks=[
          {
            name: 'Ambush',
            probability: 'HIGH',
            mitigation: 'Hire escort',
          },
        ]
      />,
    )

    expect(screen.getByText(/Ambush/i)).toBeInTheDocument()
    expect(screen.getByText(/Hire escort/i)).toBeInTheDocument()
  })
})


