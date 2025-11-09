import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { ActionPromptDialog } from '../ActionPromptDialog'

describe('ActionPromptDialog', () => {
  it('отправляет значения для use режима', () => {
    const handleSubmit = vi.fn()
    const handleClose = vi.fn()

    render(
      <ActionPromptDialog
        open
        mode="use"
        defaultLocationId="loc-42"
        onSubmit={handleSubmit}
        onClose={handleClose}
      />
    )

    fireEvent.change(screen.getByLabelText('ID объекта'), { target: { value: 'object-1' } })
    fireEvent.click(screen.getByRole('button', { name: 'Подтвердить' }))

    expect(handleSubmit).toHaveBeenCalledWith({ objectId: 'object-1', locationId: 'loc-42' })
  })

  it('отправляет значения для hack режима', () => {
    const handleSubmit = vi.fn()
    const handleClose = vi.fn()

    render(<ActionPromptDialog open mode="hack" onSubmit={handleSubmit} onClose={handleClose} />)

    fireEvent.change(screen.getByLabelText('ID цели'), { target: { value: 'target-5' } })
    fireEvent.mouseDown(screen.getByLabelText('Метод'))
    fireEvent.click(screen.getByRole('option', { name: 'Quickhack' }))
    fireEvent.click(screen.getByRole('button', { name: 'Подтвердить' }))

    expect(handleSubmit).toHaveBeenCalledWith({ targetId: 'target-5', method: 'quickhack' })
  })
})
