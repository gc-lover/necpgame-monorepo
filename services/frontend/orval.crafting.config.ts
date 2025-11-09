import { defineConfig } from 'orval'

const defaultOverride = {
  mutator: {
    path: './src/api/custom-instance.ts',
    name: 'customInstance',
  },
  query: {
    useQuery: true,
    useMutation: true,
    signal: true,
  },
}

export default defineConfig({
  'quest-catalog-api': {
    input: {
      target: '../openapi/api/v1/narrative/quest-catalog.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative/quest-catalog',
      schemas: './src/api/generated/narrative/quest-catalog/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },

  'faction-quests-api': {
    input: {
      target: '../openapi/api/v1/narrative/faction-quests.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative/faction-quests',
      schemas: './src/api/generated/narrative/faction-quests/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },

  'crafting-system-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/crafting-system/crafting-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/crafting-system',
      schemas: './src/api/generated/crafting-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },

  'combat-session-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/combat-session.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/gameplay/combat-session',
      schemas: './src/api/generated/gameplay/combat-session/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },

  'progression-backend-api': {
    input: {
      target: '../openapi/api/v1/progression/progression-backend.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/progression/backend',
      schemas: './src/api/generated/progression/backend/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },

  'lore-reference-api': {
    input: {
      target: '../openapi/api/v1/lore/lore-reference.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/lore/reference',
      schemas: './src/api/generated/lore/reference/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: defaultOverride,
    },
  },
})

