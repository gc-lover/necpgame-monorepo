/**
 * Тесты для VendorCard
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { VendorCard } from '../VendorCard'
import type { Vendor } from '@/api/generated/trading/models'

describe('VendorCard', () => {
  const mockVendor: Vendor = {
    id: 'vendor-001',
    name: 'Тестовый торговец',
    locationId: 'loc-001',
    specialization: 'weapons',
  }

  it('должен отображать торговца из OpenAPI', () => {
    render(<VendorCard vendor={mockVendor} onClick={() => {}} />)
    expect(screen.getByText('Тестовый торговец')).toBeInTheDocument()
    expect(screen.getByText('weapons')).toBeInTheDocument()
  })

  it('должен вызывать onClick', () => {
    const onClick = vi.fn()
    render(<VendorCard vendor={mockVendor} onClick={onClick} />)
    fireEvent.click(screen.getByText('Тестовый торговец'))
    expect(onClick).toHaveBeenCalled()
  })
})

