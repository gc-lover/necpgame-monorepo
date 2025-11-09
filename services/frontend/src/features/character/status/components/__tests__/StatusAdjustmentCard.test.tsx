import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { StatusAdjustmentCard } from '../StatusAdjustmentCard'

const mockUseUpdateCharacterStatus = vi.fn()

vi.mock('@/api/generated/character-status/characters/characters', () => ({
  useUpdateCharacterStatus: (...args: unknown[]) => mockUseUpdateCharacterStatus(...args),
}))

describe('StatusAdjustmentCard', () => {
  let queryClient: QueryClient
  const mutate = vi.fn()
  const onUpdated = vi.fn()

  const renderComponent = () =>
    render(
      <QueryClientProvider client={queryClient}>
        <StatusAdjustmentCard characterId="char-001" onUpdated={onUpdated} />
      </QueryClientProvider>
    )

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
      },
    })
    mutate.mockReset()
    onUpdated.mockReset()
    mockUseUpdateCharacterStatus.mockReturnValue({ mutate, isPending: false })
  })

  it('отправляет дельты статуса через мутацию', () => {
    mutate.mockImplementation((_payload, options) => {
      options?.onSuccess?.()
    })

    renderComponent()

    fireEvent.change(screen.getByLabelText('Здоровье Δ'), { target: { value: '10' } })
    fireEvent.change(screen.getByLabelText('Опыт Δ'), { target: { value: '50' } })
    fireEvent.click(screen.getByRole('button', { name: /применить/i }))

    expect(mutate).toHaveBeenCalledWith(
      {
        characterId: 'char-001',
        data: {
          healthDelta: 10,
          experienceDelta: 50,
        },
      },
      expect.any(Object)
    )
    expect(onUpdated).toHaveBeenCalled()
    expect(screen.getByText(/Статус обновлён/i)).toBeInTheDocument()
  })

  it('не отправляет запрос без заполненных полей', () => {
    renderComponent()
    fireEvent.click(screen.getByRole('button', { name: /применить/i }))
    expect(mutate).not.toHaveBeenCalled()
    expect(screen.getByText(/Укажите изменения перед применением/i)).toBeInTheDocument()
  })
})






