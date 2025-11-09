import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TradingGuildCard } from '../TradingGuildCard'

describe('TradingGuildCard', () => {
  it('renders guild info', () => {
    const guild = {
      guild_id: 'guild_001',
      name: 'Night Market Traders',
      type: 'MERCHANT',
      level: 3,
      member_count: 25,
      treasury_balance: 500000,
      reputation_score: 85,
    }
    render(<TradingGuildCard guild={guild} />)
    expect(screen.getByText('Night Market Traders')).toBeInTheDocument()
    expect(screen.getByText('MERCHANT')).toBeInTheDocument()
    expect(screen.getByText('LVL 3')).toBeInTheDocument()
  })
})

