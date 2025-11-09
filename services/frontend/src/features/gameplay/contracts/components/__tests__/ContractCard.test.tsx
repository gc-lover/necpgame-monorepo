import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ContractCard } from '../ContractCard'

describe('ContractCard', () => {
  it('renders contract summary', () => {
    render(
      <ContractCard
        contract={{
          contractId: 'contract-123456',
          type: 'SERVICE',
          status: 'ACTIVE',
          title: 'Security escort',
          collateral: 5000,
          escrowHeld: 12000,
          expiresIn: '3h',
          participants: [
            { characterId: 'char-AAAAAA', role: 'CREATOR', reputation: 72 },
            { characterId: 'char-BBBBBB', role: 'ACCEPTOR', reputation: 64 },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Security escort/i)).toBeInTheDocument()
    expect(screen.getByText(/Collateral/i)).toBeInTheDocument()
  })
})


