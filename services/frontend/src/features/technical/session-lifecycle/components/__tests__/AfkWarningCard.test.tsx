import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AfkWarningCard } from '../../components/AfkWarningCard'

describe('AfkWarningCard', () => {
  it('renders AFK warning', () => {
    render(
      <AfkWarningCard
        warning={{
          triggeredAt: '2025-11-08 04:14',
          timeoutSeconds: 45,
          reason: 'No movement',
        }}
      />,
    )

    expect(screen.getByText(/AFK Warning/i)).toBeInTheDocument()
    expect(screen.getByText(/No movement/i)).toBeInTheDocument()
  })
})


