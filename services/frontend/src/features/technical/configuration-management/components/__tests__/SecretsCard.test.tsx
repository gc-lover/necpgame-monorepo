import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SecretsCard } from '../../components/SecretsCard'

describe('SecretsCard', () => {
  it('renders secret metadata', () => {
    render(
      <SecretsCard
        secrets={[
          { secretName: 'mm-redis', createdAt: '2025-10-01', updatedAt: '2025-11-05' },
        ]}
      />,
    )

    expect(screen.getByText(/mm-redis/i)).toBeInTheDocument()
    expect(screen.getByText(/created/i)).toBeInTheDocument()
  })
})


