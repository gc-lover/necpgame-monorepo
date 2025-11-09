import { useApi } from '../hooks/useApi'
import { api } from '../api/client'

interface HealthResponse {
  status: string
  message: string
  version?: string
}

export function ApiStatus() {
  const { data, loading, error } = useApi<HealthResponse>(api.health)

  if (loading) {
    return (
      <div className="bg-gray-800 rounded-lg p-6 mb-6">
        <h3 className="text-xl font-semibold mb-4">Статус подключения к API</h3>
        <div className="flex items-center gap-2">
          <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-cyan-400"></div>
          <span className="text-gray-300">Подключение...</span>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="bg-red-900/20 border border-red-500 rounded-lg p-6 mb-6">
        <h3 className="text-xl font-semibold mb-4 text-red-400">Ошибка подключения к API</h3>
        <p className="text-red-300 mb-2">Не удалось подключиться к бекенду:</p>
        <p className="text-sm text-red-400 font-mono">{error}</p>
        <p className="text-sm text-gray-400 mt-4">
          Убедитесь, что бекенд запущен на http://localhost:8080
        </p>
      </div>
    )
  }

  return (
    <div className="bg-green-900/20 border border-green-500 rounded-lg p-6 mb-6">
      <h3 className="text-xl font-semibold mb-4 text-green-400">OK Подключение к API успешно</h3>
      {data && (
        <div className="space-y-2">
          <p className="text-green-300">
            <span className="font-semibold">Статус:</span> {data.status}
          </p>
          <p className="text-green-300">
            <span className="font-semibold">Сообщение:</span> {data.message}
          </p>
          {data.version && (
            <p className="text-green-300">
              <span className="font-semibold">Версия:</span> {data.version}
            </p>
          )}
        </div>
      )}
    </div>
  )
}



























