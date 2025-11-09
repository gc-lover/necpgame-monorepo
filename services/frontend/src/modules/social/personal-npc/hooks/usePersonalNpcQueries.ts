import { useCallback, useMemo } from 'react'
import { useQueryClient } from '@tanstack/react-query'
import {
  getGetGameplaySocialPersonalNpcScenariosBlueprintIdInstancesQueryKey,
  getGetGameplaySocialPersonalNpcScenariosBlueprintIdQueryKey,
  getGetGameplaySocialPersonalNpcScenariosQueryKey,
  useDeleteGameplaySocialPersonalNpcScenariosBlueprintId,
  useGetGameplaySocialPersonalNpcScenarios,
  useGetGameplaySocialPersonalNpcScenariosBlueprintId,
  useGetGameplaySocialPersonalNpcScenariosBlueprintIdInstances,
  usePostGameplaySocialPersonalNpcScenarios,
  usePostGameplaySocialPersonalNpcScenariosBlueprintIdPublish,
  usePostGameplaySocialPersonalNpcsNpcIdExecuteScenario,
  usePutGameplaySocialPersonalNpcScenariosBlueprintId,
} from '@/api/generated/social/personal-npc-scenarios/npcscenarios/npcscenarios'
import type {
  ExecuteScenarioRequest,
  GetGameplaySocialPersonalNpcScenariosBlueprintIdInstancesParams,
  GetGameplaySocialPersonalNpcScenariosParams,
  ScenarioBlueprintCreateRequest,
  ScenarioBlueprintPublishRequest,
  ScenarioBlueprintUpdateRequest,
} from '@/api/generated/social/personal-npc-scenarios/models'
import { usePersonalNpcStore } from '../state/usePersonalNpcStore'

export const usePersonalNpcQueries = () => {
  const queryClient = useQueryClient()
  const { filters, pagination, selectedBlueprintId } = usePersonalNpcStore((state) => ({
    filters: state.filters,
    pagination: state.pagination,
    selectedBlueprintId: state.selectedBlueprintId,
  }))

  const listParams = useMemo<GetGameplaySocialPersonalNpcScenariosParams>(() => {
    const params: GetGameplaySocialPersonalNpcScenariosParams = {
      owner_id: filters.ownerId,
      category: filters.category,
      scenario_status: filters.scenarioStatus,
      license_tier: filters.licenseTier,
      page: pagination.page,
      page_size: pagination.pageSize,
    }

    if (typeof filters.isPublic === 'boolean') {
      params.is_public = filters.isPublic
    }

    return params
  }, [
    filters.ownerId,
    filters.category,
    filters.scenarioStatus,
    filters.licenseTier,
    filters.isPublic,
    pagination.page,
    pagination.pageSize,
  ])

  const listQuery = useGetGameplaySocialPersonalNpcScenarios(
    listParams,
    {
      query: {
        keepPreviousData: true,
      },
    },
    queryClient
  )

  const detailQuery = useGetGameplaySocialPersonalNpcScenariosBlueprintId(
    selectedBlueprintId ?? '',
    {
      query: {
        enabled: Boolean(selectedBlueprintId),
      },
    },
    queryClient
  )

  const instancesParams = useMemo<GetGameplaySocialPersonalNpcScenariosBlueprintIdInstancesParams>(() => {
    const params: GetGameplaySocialPersonalNpcScenariosBlueprintIdInstancesParams = {
      scenario_status: filters.scenarioStatus,
      page: pagination.page,
      page_size: pagination.pageSize,
    }

    return params
  }, [filters.scenarioStatus, pagination.page, pagination.pageSize])

  const instancesQuery = useGetGameplaySocialPersonalNpcScenariosBlueprintIdInstances(
    selectedBlueprintId ?? '',
    instancesParams,
    {
      query: {
        enabled: Boolean(selectedBlueprintId),
        keepPreviousData: true,
      },
    },
    queryClient
  )

  const invalidateList = useCallback(() => {
    queryClient.invalidateQueries({
      queryKey: getGetGameplaySocialPersonalNpcScenariosQueryKey(listParams),
    })
  }, [listParams, queryClient])

  const invalidateDetail = useCallback(
    (blueprintId?: string) => {
      if (!blueprintId) {
        return
      }
      queryClient.invalidateQueries({
        queryKey: getGetGameplaySocialPersonalNpcScenariosBlueprintIdQueryKey(blueprintId),
      })
      queryClient.invalidateQueries({
        queryKey: getGetGameplaySocialPersonalNpcScenariosBlueprintIdInstancesQueryKey(
          blueprintId,
          instancesParams
        ),
      })
    },
    [instancesParams, queryClient]
  )

  const createBlueprint = usePostGameplaySocialPersonalNpcScenarios(
    {
      mutation: {
        onSuccess: () => {
          invalidateList()
        },
      },
    },
    queryClient
  )

  const updateBlueprint = usePutGameplaySocialPersonalNpcScenariosBlueprintId(
    {
      mutation: {
        onSuccess: (_, variables) => {
          invalidateList()
          invalidateDetail(variables?.blueprintId)
        },
      },
    },
    queryClient
  )

  const deleteBlueprint = useDeleteGameplaySocialPersonalNpcScenariosBlueprintId(
    {
      mutation: {
        onSuccess: (_, variables) => {
          invalidateList()
          invalidateDetail(variables?.blueprintId)
        },
      },
    },
    queryClient
  )

  const publishBlueprint = usePostGameplaySocialPersonalNpcScenariosBlueprintIdPublish(
    {
      mutation: {
        onSuccess: (_, variables) => {
          invalidateList()
          invalidateDetail(variables?.blueprintId)
        },
      },
    },
    queryClient
  )

  const executeScenario = usePostGameplaySocialPersonalNpcsNpcIdExecuteScenario(
    {
      mutation: {
        onSuccess: () => {
          invalidateDetail(selectedBlueprintId)
        },
      },
    },
    queryClient
  )

  return {
    listQuery,
    detailQuery,
    instancesQuery,
    createBlueprint,
    updateBlueprint,
    deleteBlueprint,
    publishBlueprint,
    executeScenario,
  }
}

