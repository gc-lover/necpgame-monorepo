import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { InitialStateSidebar } from '../InitialStateSidebar'
import type { GameQuest, TutorialStepsResponse } from '@/api/generated/game/models'

describe('InitialStateSidebar', () => {
  const quest: GameQuest = {
    id: 'quest-1',
    name: 'Доставка груза',
    description: 'Нужно доставить посылку в Watson.',
    type: 'side',
    level: 1,
    giverNpcId: 'npc-1',
    rewards: {
      experience: 100,
      money: 200,
      items: [],
      reputation: {
        faction: 'ncpd',
        amount: 5,
      },
    },
  }

  const tutorial: TutorialStepsResponse = {
    steps: [
      {
        id: 'step-1',
        title: 'Добро пожаловать',
        description: 'Осмотритесь вокруг.',
        hint: 'Используйте действия.',
      },
    ],
    currentStep: 0,
    totalSteps: 1,
    canSkip: true,
  }

  it('отображает карточку квеста', () => {
    render(<InitialStateSidebar quest={quest} tutorial={tutorial} />)

    expect(screen.getByText('Доставка груза')).toBeInTheDocument()
  })

  it('показывает заглушку при отсутствии туториала', () => {
    render(<InitialStateSidebar quest={quest} />)

    expect(screen.getByText(/туториал недоступен/i)).toBeInTheDocument()
  })

  it('вызывает обработчики завершения и пропуска туториала', () => {
    const onComplete = vi.fn()
    const onSkip = vi.fn()

    render(
      <InitialStateSidebar
        quest={quest}
        tutorial={tutorial}
        onCompleteTutorial={onComplete}
        onSkipTutorial={onSkip}
      />
    )

    fireEvent.click(screen.getByText('Пропустить'))
    expect(onSkip).toHaveBeenCalled()

    fireEvent.click(screen.getByText('Завершить'))
    expect(onComplete).toHaveBeenCalled()
  })
})

