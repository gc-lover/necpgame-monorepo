import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ReputationTierCard } from '../ReputationTierCard'

describe('ReputationTierCard', () => {
  it('renders', () => {
    render(<ReputationTierCard faction="NCPD" tier="friendly" points={1000} />)
    expect(screen.getByText('NCPD')).toBeInTheDocument()
  })
})

