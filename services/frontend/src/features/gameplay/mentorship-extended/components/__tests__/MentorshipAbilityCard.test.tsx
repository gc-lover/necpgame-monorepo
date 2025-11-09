import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MentorshipAbilityCard } from '../MentorshipAbilityCard'

describe('MentorshipAbilityCard', () => {
  it('renders ability information', () => {
    render(
      <MentorshipAbilityCard
        ability={{
          abilityId: 'abl-1',
          name: 'Neural Overclock',
          description: 'Instantly refreshes quickhack cooldowns.',
          rarity: 'LEGENDARY',
          activationCost: '40 RAM',
          cooldown: '90s',
        }}
      />,
    )

    expect(screen.getByText(/Neural Overclock/i)).toBeInTheDocument()
    expect(screen.getByText(/Instantly refreshes/i)).toBeInTheDocument()
  })
})


