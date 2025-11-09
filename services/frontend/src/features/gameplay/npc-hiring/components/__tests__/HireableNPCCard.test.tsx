import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HireableNPCCard } from '../HireableNPCCard'

describe('HireableNPCCard', () => {
  it('renders NPC info', () => {
    const npc = {
      npc_id: 'bodyguard_001',
      name: 'Viktor "Vik" Kozlov',
      type: 'combat' as const,
      role: 'bodyguard',
      tier: 3,
      cost_daily: 500,
      reputation_required: 1000,
    }
    render(<HireableNPCCard npc={npc} />)
    expect(screen.getByText('Viktor "Vik" Kozlov')).toBeInTheDocument()
    expect(screen.getByText('T3')).toBeInTheDocument()
  })
})

