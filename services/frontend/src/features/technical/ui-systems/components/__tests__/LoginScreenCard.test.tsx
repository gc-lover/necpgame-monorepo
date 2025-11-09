import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { LoginScreenCard } from '../../components/LoginScreenCard'

describe('LoginScreenCard', () => {
  it('renders login screen data', () => {
    render(
      <LoginScreenCard
        data={{
          title: 'Night City Login',
          subtitle: 'Connect to the grid',
          callToAction: 'Start Session',
          background: 'neon-rain-01',
          rotatingTips: ['Tip 1', 'Tip 2'],
        }}
      />,
    )

    expect(screen.getByText(/Night City Login/i)).toBeInTheDocument()
    expect(screen.getByText(/Start Session/i)).toBeInTheDocument()
  })
})


