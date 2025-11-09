import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi } from 'vitest'
import { EventDialog } from '../EventDialog'
import { RandomEvent, EventResult } from '@/api/generated/events/models'

describe('EventDialog', () => {
  const mockEvent: RandomEvent = {
    id: 'event-1',
    name: 'Встреча с бандитом',
    description: 'Вы встретили вооруженного бандита',
    options: [
      {
        id: 'option-1',
        text: 'Сразиться',
        available: true,
        requirements: {
          minStrength: 5,
        },
      },
      {
        id: 'option-2',
        text: 'Убежать',
        available: true,
      },
      {
        id: 'option-3',
        text: 'Переговорить',
        available: false,
        requirements: {
          requiredSkill: 'Убеждение',
        },
      },
    ],
    dangerLevel: 'HIGH',
    timeLimit: 30,
  }

  const mockResult: EventResult = {
    success: true,
    outcome: 'Вы успешно справились с ситуацией',
    rewards: {
      experience: 100,
      money: 500,
      reputation: {
        'NCPD': 10,
      },
    },
  }

  it('не рендерится когда open=false', () => {
    render(
      <EventDialog
        open={false}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.queryByText('Встреча с бандитом')).not.toBeInTheDocument()
  })

  it('рендерит название события', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Встреча с бандитом')).toBeInTheDocument()
  })

  it('рендерит описание события', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Вы встретили вооруженного бандита')).toBeInTheDocument()
  })

  it('отображает предупреждение об ограничении времени', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText(/У вас есть 30 секунд/)).toBeInTheDocument()
  })

  it('рендерит все варианты действий', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Сразиться')).toBeInTheDocument()
    expect(screen.getByText('Убежать')).toBeInTheDocument()
    expect(screen.getByText('Переговорить')).toBeInTheDocument()
  })

  it('показывает требования для доступных опций', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Сила 5+')).toBeInTheDocument()
  })

  it('показывает сообщение о невыполненных требованиях', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Требования не выполнены')).toBeInTheDocument()
  })

  it('выбирает вариант при клике', async () => {
    const user = userEvent.setup()
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )

    const option1Button = screen.getByText('Сразиться').closest('button')
    if (option1Button) {
      await user.click(option1Button)
      // Кнопка должна быть выбрана (contained variant)
      expect(option1Button).toHaveClass('MuiButton-contained')
    }
  })

  it('вызывает onSelectOption при подтверждении', async () => {
    const user = userEvent.setup()
    const handleSelectOption = vi.fn()
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={handleSelectOption}
      />
    )

    // Выбираем вариант
    const option1Button = screen.getByText('Сразиться').closest('button')
    if (option1Button) {
      await user.click(option1Button)
    }

    // Подтверждаем
    const confirmButton = screen.getByText('Подтвердить')
    await user.click(confirmButton)

    expect(handleSelectOption).toHaveBeenCalledWith('option-1')
  })

  it('вызывает onClose при отмене', async () => {
    const user = userEvent.setup()
    const handleClose = vi.fn()
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={handleClose}
        onSelectOption={vi.fn()}
      />
    )

    const cancelButton = screen.getByText('Отмена')
    await user.click(cancelButton)

    expect(handleClose).toHaveBeenCalledTimes(1)
  })

  it('отображает результат успеха', () => {
    render(
      <EventDialog
        open={true}
        event={null}
        result={mockResult}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Успех!')).toBeInTheDocument()
    expect(screen.getByText('Вы успешно справились с ситуацией')).toBeInTheDocument()
  })

  it('отображает награды', () => {
    render(
      <EventDialog
        open={true}
        event={null}
        result={mockResult}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('+100 опыта')).toBeInTheDocument()
    expect(screen.getByText('+500 эдди')).toBeInTheDocument()
    expect(screen.getByText(/NCPD.*\+10 репутации/)).toBeInTheDocument()
  })

  it('отображает штрафы при неудаче', () => {
    const failResult: EventResult = {
      success: false,
      outcome: 'Вы проиграли',
      penalties: {
        healthLoss: 50,
        moneyLoss: 100,
        humanityLoss: 5,
      },
    }

    render(
      <EventDialog
        open={true}
        event={null}
        result={failResult}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
      />
    )
    expect(screen.getByText('Неудача')).toBeInTheDocument()
    expect(screen.getByText('-50 здоровья')).toBeInTheDocument()
    expect(screen.getByText('-100 эдди')).toBeInTheDocument()
    expect(screen.getByText('-5 человечности')).toBeInTheDocument()
  })

  it('блокирует кнопку подтверждения когда идет ответ', () => {
    render(
      <EventDialog
        open={true}
        event={mockEvent}
        result={null}
        onClose={vi.fn()}
        onSelectOption={vi.fn()}
        isResponding={true}
      />
    )
    expect(screen.getByText('Загрузка...')).toBeInTheDocument()
  })
})

