import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import { EmergencyActionsCard } from '../../components/EmergencyActionsCard'

describe('EmergencyActionsCard', () => {
  it('renders emergency buttons', () => {
    render(<EmergencyActionsCard onBackup={vi.fn()} onRestore={vi.fn()} onFailover={vi.fn()} />)

    expect(screen.getByText(/emergency backup/i)).toBeInTheDocument()
    expect(screen.getByText(/Переключить/i)).toBeInTheDocument()
  })
})


