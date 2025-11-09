import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ModelsCard } from '../ModelsCard'

describe('ModelsCard', () => {
  it('renders data models', () => {
    render(
      <ModelsCard
        models={[
          {
            modelName: 'Character',
            description: 'Player character data',
            fields: [
              { fieldName: 'name', type: 'string', required: true },
              { fieldName: 'level', type: 'number', required: true },
            ],
          },
        ]}
      />,
    )

    expect(screen.getByText(/Character/i)).toBeInTheDocument()
    expect(screen.getByText(/Player character data/i)).toBeInTheDocument()
  })
})


