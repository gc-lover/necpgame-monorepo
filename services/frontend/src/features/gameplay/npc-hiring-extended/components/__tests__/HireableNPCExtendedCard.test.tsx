import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import { HireableNPCExtendedCard } from '../HireableNPCExtendedCard'

describe('HireableNPCExtendedCard', () => {
  it('renders hireable npc info', () => {
    const onHire = vi.fn()
    render(
      <HireableNPCExtendedCard
        npc={{
          npcId: 'npc-alpha',
          name: 'Alpha Ghost',
          role: 'HACKER',
          specialization: 'Quickhack assassin',
          squadAffiliation: 'Neon Phantoms',
          tier: 'S',
          legendary: true,
          skillLevel: 19,
          loyalty: 72,
          effectiveness: 88,
          salaryPerDay: 4200,
          riskLevel: 'MEDIUM',
          traits: ['Stealth deploy', 'ICE neutralizer', 'Combat quickhacks'],
        }}
        onHire={onHire}
      />,
    )

    expect(screen.getByText(/Alpha Ghost/i)).toBeInTheDocument()
    expect(screen.getByText(/Quickhack assassin/i)).toBeInTheDocument()
    expect(screen.getByText(/Нанять/i)).toBeInTheDocument()
  })
})

