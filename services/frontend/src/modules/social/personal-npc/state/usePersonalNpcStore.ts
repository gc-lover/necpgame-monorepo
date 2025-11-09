import { create } from 'zustand'
import type { ScenarioCategoryParameter } from '@/api/generated/social/personal-npc-scenarios/models/scenarioCategoryParameter'
import type { ScenarioStatusParameter } from '@/api/generated/social/personal-npc-scenarios/models/scenarioStatusParameter'
import type { LicenseTierParameter } from '@/api/generated/social/personal-npc-scenarios/models/licenseTierParameter'

export type PersonalNpcFilters = {
  ownerId?: string
  category?: ScenarioCategoryParameter
  scenarioStatus?: ScenarioStatusParameter
  isPublic?: boolean
  licenseTier?: LicenseTierParameter
  search?: string
}

export type PersonalNpcPagination = {
  page: number
  pageSize: number
}

export type PersonalNpcState = {
  filters: PersonalNpcFilters
  pagination: PersonalNpcPagination
  selectedBlueprintId?: string
  setFilter: <K extends keyof PersonalNpcFilters>(key: K, value: PersonalNpcFilters[K]) => void
  setFilters: (next: PersonalNpcFilters) => void
  resetFilters: () => void
  setPagination: (next: Partial<PersonalNpcPagination>) => void
  selectBlueprint: (blueprintId?: string) => void
}

const initialFilters: PersonalNpcFilters = {}

const initialPagination: PersonalNpcPagination = {
  page: 1,
  pageSize: 20,
}

export const usePersonalNpcStore = create<PersonalNpcState>((set) => ({
  filters: initialFilters,
  pagination: initialPagination,
  selectedBlueprintId: undefined,
  setFilter: (key, value) =>
    set((state) => ({
      filters: {
        ...state.filters,
        [key]: value,
      },
      pagination: {
        ...state.pagination,
        page: 1,
      },
    })),
  setFilters: (next) =>
    set({
      filters: { ...next },
      pagination: { ...initialPagination },
    }),
  resetFilters: () =>
    set({
      filters: { ...initialFilters },
      pagination: { ...initialPagination },
    }),
  setPagination: (next) =>
    set((state) => ({
      pagination: {
        page: next.page ?? state.pagination.page,
        pageSize: next.pageSize ?? state.pagination.pageSize,
      },
    })),
  selectBlueprint: (blueprintId) =>
    set({
      selectedBlueprintId: blueprintId,
    }),
}))

