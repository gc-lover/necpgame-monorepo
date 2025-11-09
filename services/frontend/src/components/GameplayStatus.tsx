import { useApi } from '../hooks/useApi'
import { api } from '../api/client'

interface GameplayStatusResponse {
  message: string
  version?: string
}

export function GameplayStatus() {
  const { data, loading, error } = useApi<GameplayStatusResponse>(api.gameplay.status)

  if (loading) {
    return (
      <div className="bg-gray-800 rounded-lg p-4">
        <p className="text-gray-300">Загрузка статуса игровых данных...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="bg-red-900/20 border border-red-500 rounded-lg p-4">
        <p className="text-red-300">Ошибка: {error}</p>
      </div>
    )
  }

  return (
    <div className="bg-gray-800 rounded-lg p-4">
      <p className="text-gray-300">
        <span className="font-semibold text-cyan-400">Игровые данные:</span> {data?.message}
      </p>
      {data?.version && (
        <p className="text-sm text-gray-400 mt-1">Версия: {data.version}</p>
      )}
    </div>
  )
}



























