import { api } from '../client'

/**
 * Утилиты для тестирования API
 */

/**
 * Проверяет доступность бекенда
 * @param timeout Таймаут в миллисекундах
 * @returns true если бекенд доступен, false если нет
 */
export async function checkBackendAvailability(timeout: number = 5000): Promise<boolean> {
  try {
    const controller = new AbortController()
    const timeoutId = setTimeout(() => controller.abort(), timeout)
    
    await api.health()
    clearTimeout(timeoutId)
    return true
  } catch (error) {
    return false
  }
}

/**
 * Ожидает доступности бекенда
 * @param maxRetries Максимальное количество попыток
 * @param retryDelay Задержка между попытками в миллисекундах
 * @returns true если бекенд стал доступен, false если превышено количество попыток
 */
export async function waitForBackend(
  maxRetries: number = 10,
  retryDelay: number = 1000
): Promise<boolean> {
  for (let i = 0; i < maxRetries; i++) {
    const isAvailable = await checkBackendAvailability()
    if (isAvailable) {
      return true
    }
    await new Promise(resolve => setTimeout(resolve, retryDelay))
  }
  return false
}

/**
 * Создает мок токена для тестирования
 * @param token Токен для установки
 */
export function setMockAuthToken(token: string): void {
  localStorage.setItem('token', token)
}

/**
 * Удаляет мок токен
 */
export function clearMockAuthToken(): void {
  localStorage.removeItem('token')
}

/**
 * Проверяет, является ли ошибка ошибкой сети
 * @param error Объект ошибки
 */
export function isNetworkError(error: any): boolean {
  return (
    error.code === 'ERR_NETWORK' ||
    error.code === 'ECONNREFUSED' ||
    error.message?.includes('Network Error') ||
    error.message?.includes('timeout') ||
    error.message?.includes('ECONNREFUSED')
  )
}

/**
 * Проверяет, является ли ошибка ошибкой авторизации
 * @param error Объект ошибки
 */
export function isAuthError(error: any): boolean {
  return error.response?.status === 401 || error.response?.status === 403
}

/**
 * Получает информацию о подключении к бекенду
 */
export async function getBackendInfo(): Promise<{
  isAvailable: boolean
  baseURL: string
  responseTime?: number
}> {
  const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'
  const startTime = Date.now()
  
  try {
    await api.health()
    const responseTime = Date.now() - startTime
    
    return {
      isAvailable: true,
      baseURL,
      responseTime,
    }
  } catch (error) {
    return {
      isAvailable: false,
      baseURL,
    }
  }
}

/**
 * Выводит информацию о подключении в консоль
 */
export async function printBackendInfo(): Promise<void> {
  const info = await getBackendInfo()
  
  console.log('\n' + '='.repeat(50))
  console.log('Backend Connection Info')
  console.log('='.repeat(50))
  console.log(`Base URL: ${info.baseURL}`)
  console.log(`Status: ${info.isAvailable ? '✓ Available' : '✗ Unavailable'}`)
  if (info.responseTime) {
    console.log(`Response Time: ${info.responseTime}ms`)
  }
  console.log('='.repeat(50) + '\n')
}

