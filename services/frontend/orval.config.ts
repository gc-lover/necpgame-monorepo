import { defineConfig } from 'orval'

/**
 * Конфигурация Orval для генерации TypeScript клиента и React Query хуков
 * из OpenAPI спецификаций
 * 
 * Документация: https://orval.dev/
 */
export default defineConfig({
  // API для аутентификации и создания персонажей
  'auth-api': {
    input: {
      target: '../openapi/api/v1/auth/character-creation.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auth',
      schemas: './src/api/generated/auth/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для аутентификации и авторизации
  'auth-authentication-api': {
    input: {
      target: '../openapi/api/v1/auth/authentication.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auth/authentication',
      schemas: './src/api/generated/auth/authentication/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для импланты и лимиты
  'implants-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/implants-limits.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/gameplay/combat',
      schemas: './src/api/generated/gameplay/combat/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для киберпсихоза
  'cyberpsychosis-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/cyberpsychosis.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/gameplay/cyberpsychosis',
      schemas: './src/api/generated/gameplay/cyberpsychosis/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для запуска игры
  'game-start-api': {
    input: {
      target: '../openapi/api/v1/game/start.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/game',
      schemas: './src/api/generated/game/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для начального состояния игры
  'game-initial-state-api': {
    input: {
      target: '../openapi/api/v1/game/initial-state.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/game',
      schemas: './src/api/generated/game/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для квестов
  'quests-api': {
    input: {
      target: '../openapi/api/v1/quests/quests.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/quests',
      schemas: './src/api/generated/quests/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для инвентаря
  'inventory-api': {
    input: {
      target: '../openapi/api/v1/economy/inventory/inventory.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/inventory',
      schemas: './src/api/generated/inventory/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'auction-house-mechanics-api': {
    input: {
      target: '../openapi/api/v1/economy/auction-house/auction-mechanics.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/economy/auction-house',
      schemas: './src/api/generated/economy/auction-house/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для NPCs и диалогов
  'npcs-api': {
    input: {
      target: '../openapi/api/v1/npcs/npcs.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/npcs',
      schemas: './src/api/generated/npcs/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'personal-npc-scenarios-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/personal-npc-scenarios.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/social/personal-npc-scenarios',
      schemas: './src/api/generated/social/personal-npc-scenarios/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'starter-content-api': {
    input: {
      target: '../openapi/api/v1/narrative/starter-content.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative/starter-content',
      schemas: './src/api/generated/narrative/starter-content/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'regional-quests-api': {
    input: {
      target: '../openapi/api/v1/narrative/regional-quests.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative/regional-quests',
      schemas: './src/api/generated/narrative/regional-quests/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'disaster-recovery-api': {
    input: {
      target: '../openapi/api/v1/technical/disaster-recovery.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/disaster-recovery',
      schemas: './src/api/generated/technical/disaster-recovery/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'incident-response-api': {
    input: {
      target: '../openapi/api/v1/technical/incident-response.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/incident-response',
      schemas: './src/api/generated/technical/incident-response/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'voice-chat-api': {
    input: {
      target: '../openapi/api/v1/social/voice/voice-chat.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/social/voice-chat',
      schemas: './src/api/generated/social/voice-chat/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'realtime-server-zones-api': {
    input: {
      target: '../openapi/api/v1/technical/realtime/server-zones.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/realtime-server-zones',
      schemas: './src/api/generated/technical/realtime-server-zones/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для матчмейкинга (алгоритм)
  'matchmaking-algorithm-api': {
    input: {
      target: '../openapi/api/v1/matchmaking/matchmaking-algorithm.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/gameplay/matchmaking-algorithm',
      schemas: './src/api/generated/gameplay/matchmaking-algorithm/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для локаций
  'locations-api': {
    input: {
      target: '../openapi/api/v1/locations/locations.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/locations',
      schemas: './src/api/generated/locations/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для торговли
  'trading-api': {
    input: {
      target: '../openapi/api/v1/trading/trading.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/trading',
      schemas: './src/api/generated/trading/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для игровых действий
  'actions-api': {
    input: {
      target: '../openapi/api/v1/gameplay/actions/actions.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/actions',
      schemas: './src/api/generated/actions/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для статуса персонажа
  'character-status-api': {
    input: {
      target: '../openapi/api/v1/characters/status.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/character-status',
      schemas: './src/api/generated/character-status/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для случайных событий
  'events-api': {
    input: {
      target: '../openapi/api/v1/events/random-events.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/events',
      schemas: './src/api/generated/events/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы оружия
  'weapons-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/weapons.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/weapons',
      schemas: './src/api/generated/weapons/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы способностей
  'abilities-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/abilities.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/abilities',
      schemas: './src/api/generated/abilities/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для каталога способностей
  'abilities-catalog-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/abilities-catalog.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/abilities-catalog',
      schemas: './src/api/generated/abilities-catalog/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для боевых ролей
  'combat-roles-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/combat-roles.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/combat-roles',
      schemas: './src/api/generated/combat-roles/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для AI врагов
  'ai-enemies-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/ai-enemies.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/ai-enemies',
      schemas: './src/api/generated/ai-enemies/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для комбо и синергий
  'combos-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/combos-synergies.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/combos',
      schemas: './src/api/generated/combos/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для экстракт-шутера
  'extraction-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/extraction.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/extraction',
      schemas: './src/api/generated/extraction/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для каталога имплантов
  'implants-catalog-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/implants.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/implants-catalog',
      schemas: './src/api/generated/implants-catalog/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для боевой системы стрельбы
  'shooting-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/shooting.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/shooting',
      schemas: './src/api/generated/shooting/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для текстовой боевой системы
  'combat-system-api': {
    input: {
      target: '../openapi/api/v1/combat/combat.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/combat-system',
      schemas: './src/api/generated/combat-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы классов и подклассов
  'classes-progression-api': {
    input: {
      target: '../openapi/api/v1/gameplay/progression/classes.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/classes-progression',
      schemas: './src/api/generated/classes-progression/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // Детальная система прогрессии
  'progression-detailed-api': {
    input: {
      target: '../openapi/api/v1/gameplay/progression/progression-detailed.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/progression-detailed',
      schemas: './src/api/generated/progression-detailed/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для Event Sourcing глобального состояния
  'global-state-api': {
    input: {
      target: '../openapi/api/v1/technical/global-state.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/global-state',
      schemas: './src/api/generated/global-state/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для репутационных тиров
  'reputation-tiers-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/reputation-tiers.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/reputation-tiers',
      schemas: './src/api/generated/reputation-tiers/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для киберпространства
  'cyberspace-api': {
    input: {
      target: '../openapi/api/v1/gameplay/cyberspace/cyberspace-core.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/cyberspace',
      schemas: './src/api/generated/cyberspace/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для хакерства
  'hacking-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/hacking-types.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/hacking',
      schemas: './src/api/generated/hacking/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для скрытности
  'stealth-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/stealth.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/stealth',
      schemas: './src/api/generated/stealth/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для паркура
  'freerun-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/freerun.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/freerun',
      schemas: './src/api/generated/freerun/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для биржи акций
  'stock-exchange-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/stock-exchange-core.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/stock-exchange',
      schemas: './src/api/generated/stock-exchange/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для D&D проверок
  'dnd-checks-api': {
    input: {
      target: '../openapi/api/v1/gameplay/mechanics/dnd-checks.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/dnd-checks',
      schemas: './src/api/generated/dnd-checks/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для найма NPC
  'npc-hiring-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/npc-hiring.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/npc-hiring',
      schemas: './src/api/generated/npc-hiring/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для глобальных событий
  'global-events-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/global-events.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/global-events',
      schemas: './src/api/generated/global-events/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для рынка игроков
  'player-market-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/player-market-core.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/player-market',
      schemas: './src/api/generated/player-market/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы лута
  'loot-system-api': {
    input: {
      target: '../openapi/api/v1/loot/loot-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/loot-system',
      schemas: './src/api/generated/loot-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для лут-таблиц
  'loot-tables-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/loot-tables.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/loot-tables',
      schemas: './src/api/generated/loot-tables/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для взлома сетей
  'hacking-networks-api': {
    input: {
      target: '../openapi/api/v1/gameplay/combat/hacking-networks.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/hacking-networks',
      schemas: './src/api/generated/hacking-networks/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для событий эпохи 2020-2040
  'events-2020-2040-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/events-2020-2040.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/events-2020-2040',
      schemas: './src/api/generated/events-2020-2040/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для событий эпохи 2040-2060
  'events-2040-2060-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/events-2040-2060.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/events-2040-2060',
      schemas: './src/api/generated/events-2040-2060/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для событий эпохи 2060-2077
  'events-2060-2077-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/events-2060-2077.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/events-2060-2077',
      schemas: './src/api/generated/events-2060-2077/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для торговых гильдий
  'trading-guilds-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/trading-guilds.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/trading-guilds',
      schemas: './src/api/generated/trading-guilds/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для торговых маршрутов
  'trading-routes-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/trading-routes.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/trading-routes',
      schemas: './src/api/generated/trading-routes/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для валютной биржи
  'currency-exchange-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/currency-exchange.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/currency-exchange',
      schemas: './src/api/generated/currency-exchange/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для каталога ресурсов
  'resources-catalog-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/resources-catalog.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/resources-catalog',
      schemas: './src/api/generated/resources-catalog/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы наставничества
  'mentorship-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/mentorship.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/mentorship',
      schemas: './src/api/generated/mentorship/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для семейных отношений
  'family-relationships-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/family-relationships.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/family-relationships',
      schemas: './src/api/generated/family-relationships/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для экономической аналитики
  'economy-analytics-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/analytics.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/economy-analytics',
      schemas: './src/api/generated/economy-analytics/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для логистики
  'logistics-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/logistics.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/logistics',
      schemas: './src/api/generated/logistics/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для инвестиций
  'investments-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/investments.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/investments',
      schemas: './src/api/generated/investments/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для событий перемещения
  'travel-events-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/travel-events.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/travel-events',
      schemas: './src/api/generated/travel-events/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для расширенной системы заказов
  'player-orders-extended-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/player-orders-extended.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/player-orders-extended',
      schemas: './src/api/generated/player-orders-extended/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для расширенной системы наставничества
  'mentorship-extended-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/mentorship-extended.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/mentorship-extended',
      schemas: './src/api/generated/mentorship-extended/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для расширенной системы найма NPC
  'npc-hiring-extended-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/npc-hiring-extended.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/npc-hiring-extended',
      schemas: './src/api/generated/npc-hiring-extended/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // Narrative coherence monitoring
  'narrative-coherence-api': {
    input: {
      target: '../openapi/api/v1/narrative/narrative-coherence.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative-coherence',
      schemas: './src/api/generated/narrative-coherence/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для расширенной системы случайных событий
  'random-events-extended-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/random-events-extended/random-events.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/random-events-extended',
      schemas: './src/api/generated/random-events-extended/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для фреймворка мировых событий
  'world-events-framework-api': {
    input: {
      target: '../openapi/api/v1/gameplay/world/world-events-framework.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/world-events-framework',
      schemas: './src/api/generated/world-events-framework/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для аукцион дома (core)
  'auction-house-core-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/auction-house-core.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auction-house-core',
      schemas: './src/api/generated/auction-house-core/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы ордеров аукцион дома
  'auction-house-orders-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/auction-house-orders.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auction-house-orders',
      schemas: './src/api/generated/auction-house-orders/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для поиска и фильтрации аукцион дома
  'auction-house-search-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/auction-house-search.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auction-house-search',
      schemas: './src/api/generated/auction-house-search/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для истории цен и статистики аукцион дома
  'auction-house-history-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/auction-house-history.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/auction-house-history',
      schemas: './src/api/generated/auction-house-history/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для рынка игроков (core)
  'player-market-core-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/player-market-core.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/player-market-core',
      schemas: './src/api/generated/player-market-core/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы ордеров рынка игроков
  'player-market-orders-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/player-market-orders.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/player-market-orders',
      schemas: './src/api/generated/player-market-orders/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для исполнения ордеров рынка игроков
  'player-market-execution-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/player-market-execution.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/player-market-execution',
      schemas: './src/api/generated/player-market-execution/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для системы ценообразования
  'pricing-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/pricing.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/pricing',
      schemas: './src/api/generated/pricing/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для экономической аналитики
  'analytics-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/analytics.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/economy-analytics',
      schemas: './src/api/generated/economy-analytics/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для производственных цепочек
  'production-chains-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/production-chains.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/production-chains',
      schemas: './src/api/generated/production-chains/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для контрактов
  'contracts-api': {
    input: {
      target: '../openapi/api/v1/gameplay/economy/contracts.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/contracts',
      schemas: './src/api/generated/contracts/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API анти-чита
  'anti-cheat-api': {
    input: {
      target: '../openapi/api/v1/admin/anti-cheat.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/anti-cheat',
      schemas: './src/api/generated/anti-cheat/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API модерации
  'moderation-api': {
    input: {
      target: '../openapi/api/v1/admin/moderation.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/moderation',
      schemas: './src/api/generated/moderation/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API романтической системы
  'romance-system-api': {
    input: {
      target: '../openapi/api/v1/gameplay/social/romance-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/romance-system',
      schemas: './src/api/generated/romance-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API базы данных лора
  'lore-database-api': {
    input: {
      target: '../openapi/api/v1/lore/lore-database.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/lore/database',
      schemas: './src/api/generated/lore/database/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API для романтических алгоритмов и AI
  'ai-algorithms-api': {
    input: {
      target: '../openapi/api/v1/internal/ai-algorithms.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/internal/ai-algorithms',
      schemas: './src/api/generated/internal/ai-algorithms/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API MVP контента
  'ui-systems-api': {
    input: {
      target: '../openapi/api/v1/technical/ui-systems.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/ui-systems',
      schemas: './src/api/generated/technical/ui-systems/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  // API MVP контента
  'mvp-api': {
    input: {
      target: '../openapi/api/v1/mvp/mvp-content.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/mvp',
      schemas: './src/api/generated/mvp/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'trade-system-api': {
    input: {
      target: '../openapi/api/v1/trade/trade-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/trade-system',
      schemas: './src/api/generated/trade-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'mail-system-api': {
    input: {
      target: './openapi-overrides/mail-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/mail-system',
      schemas: './src/api/generated/mail-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'party-system-api': {
    input: {
      target: '../openapi/api/v1/social/party-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/social/party-system',
      schemas: './src/api/generated/social/party-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'friend-system-api': {
    input: {
      target: '../openapi/api/v1/social/friend-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/social/friend-system',
      schemas: './src/api/generated/social/friend-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'guild-system-api': {
    input: {
      target: '../openapi/api/v1/social/guild-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/social/guild-system',
      schemas: './src/api/generated/social/guild-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'notification-system-api': {
    input: {
      target: '../openapi/api/v1/technical/notification-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/notification-system',
      schemas: './src/api/generated/technical/notification-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'achievement-system-api': {
    input: {
      target: '../openapi/api/v1/progression/achievement-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/progression/achievement-system',
      schemas: './src/api/generated/progression/achievement-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'leaderboard-system-api': {
    input: {
      target: '../openapi/api/v1/progression/leaderboard-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/progression/leaderboard-system',
      schemas: './src/api/generated/progression/leaderboard-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'reset-system-api': {
    input: {
      target: '../openapi/api/v1/technical/daily-weekly-reset-system.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/technical/reset-system',
      schemas: './src/api/generated/technical/reset-system/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },

  'quest-engine-api': {
    input: {
      target: '../openapi/api/v1/narrative/quest-engine.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/api/generated/narrative/quest-engine',
      schemas: './src/api/generated/narrative/quest-engine/models',
      client: 'react-query',
      mock: true,
      prettier: true,
      override: {
        mutator: {
          path: './src/api/custom-instance.ts',
          name: 'customInstance',
        },
        query: {
          useQuery: true,
          useMutation: true,
          signal: true,
        },
      },
    },
  },
})












