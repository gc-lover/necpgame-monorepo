/**
 * Тесты для ActionResultDialog
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { ActionResultDialog } from '../ActionResultDialog'

describe('ActionResultDialog', () => {
  it('должен отображать результат из OpenAPI', () => {
    const result = {
      description: 'Тестовое описание',
      healthRestored: 50,
      energyRestored: 30,
    }

    render(
      <ActionResultDialog
        open={true}
        onClose={() => {}}
        title="Тест"
        success={true}
        result={result}
      />
    )

    expect(screen.getByText('Тест')).toBeInTheDocument()
    expect(screen.getByText('Тестовое описание')).toBeInTheDocument()
  })

  it('должен отображать полученные данные после взлома', () => {
    const result = {
      description: 'Взлом выполнен',
      dataAccessed: ['Корпоративные логи', 'Шифрованный архив'],
    }

    render(
      <ActionResultDialog
        open={true}
        onClose={() => {}}
        title="Взлом системы"
        success={true}
        result={result}
      />
    )

    expect(screen.getByText('Взлом выполнен')).toBeInTheDocument()
    expect(screen.getByText('Корпоративные логи')).toBeInTheDocument()
    expect(screen.getByText('Шифрованный архив')).toBeInTheDocument()
  })

  it('должен вызывать onClose', () => {
    const onClose = vi.fn()
    render(
      <ActionResultDialog
        open={true}
        onClose={onClose}
        title="Тест"
        success={true}
      />
    )

    fireEvent.click(screen.getByText('Закрыть'))
    expect(onClose).toHaveBeenCalled()
  })
})

