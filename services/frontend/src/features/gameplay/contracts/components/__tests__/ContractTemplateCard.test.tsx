import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ContractTemplateCard } from '../ContractTemplateCard'

describe('ContractTemplateCard', () => {
  it('shows template info', () => {
    render(
      <ContractTemplateCard
        template={{
          templateId: 'tpl-9988',
          name: 'Night Market Delivery',
          type: 'COURIER',
          recommendedCollateral: 3000,
          autoExecution: true,
          tags: ['24h', 'fragile'],
        }}
      />,
    )

    expect(screen.getByText(/Night Market Delivery/i)).toBeInTheDocument()
    expect(screen.getByText(/COURIER/i)).toBeInTheDocument()
  })
})


