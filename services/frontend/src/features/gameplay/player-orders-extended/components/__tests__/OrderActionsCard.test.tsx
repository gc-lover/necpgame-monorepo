import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import { OrderActionsCard } from '../OrderActionsCard'

describe('OrderActionsCard', () => {
  it('renders action buttons', () => {
    const createMock = vi.fn()
    render(<OrderActionsCard onCreateOrder={createMock} />)

    expect(screen.getByText(/Создать заказ/i)).toBeInTheDocument()
  })
})


