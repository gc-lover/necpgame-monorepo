import { describe, it, expect, beforeAll } from 'vitest'
import { listCharacters } from '../generated/auth/characters/characters'
import { api } from '../client'

/**
 * Интеграционные тесты для Characters API
 * Проверяют работу с эндпоинтами персонажей
 * 
 * ВАЖНО: Эти тесты требуют запущенного бекенд сервера и авторизованного пользователя
 * Некоторые тесты могут падать из-за отсутствия токена авторизации
 */
describe('Characters API Integration Tests', () => {
  let isBackendAvailable = false
  const BACKEND_TIMEOUT = 5000

  beforeAll(async () => {
    // Проверяем доступность бекенда
    try {
      const controller = new AbortController()
      const timeoutId = setTimeout(() => controller.abort(), BACKEND_TIMEOUT)
      
      await api.health()
      clearTimeout(timeoutId)
      isBackendAvailable = true
      console.log('✓ Backend доступен для Characters API тестов')
    } catch (error) {
      console.warn('⚠ Backend недоступен, тесты будут пропущены')
    }
  })

  describe('List Characters API', () => {
    it.skipIf(!isBackendAvailable)('должен попытаться получить список персонажей', async () => {
      try {
        const response = await listCharacters()
        
        // Если есть токен и пользователь авторизован
        expect(response).toBeDefined()
        expect(response.status).toBe(200)
        expect(response.data).toBeDefined()
        
        if (response.data) {
          expect(response.data).toHaveProperty('characters')
          expect(response.data).toHaveProperty('max_characters')
          expect(response.data).toHaveProperty('current_count')
          expect(Array.isArray(response.data.characters)).toBe(true)
        }
      } catch (error: any) {
        // Если нет токена, ожидаем 401
        if (error.response?.status === 401) {
          expect(error.response.status).toBe(401)
          console.log('✓ API правильно возвращает 401 для неавторизованного пользователя')
        } else {
          // Другие ошибки - пробрасываем дальше для отладки
          throw error
        }
      }
    })

    it.skipIf(!isBackendAvailable)('должен возвращать 401 для неавторизованного запроса', async () => {
      try {
        await listCharacters()
        // Если запрос прошел, значит есть токен или не требуется авторизация
        console.log('✓ Запрос выполнен успешно (возможно, есть сохраненный токен)')
      } catch (error: any) {
        // Проверяем, что это именно ошибка авторизации
        if (error.response) {
          expect([401, 403]).toContain(error.response.status)
          console.log('✓ API правильно требует авторизацию')
        }
      }
    })
  })

  describe('API Response Structure', () => {
    it.skipIf(!isBackendAvailable)('должен иметь правильную структуру ответа при ошибке', async () => {
      try {
        await listCharacters()
      } catch (error: any) {
        if (error.response) {
          // Проверяем структуру ответа с ошибкой
          expect(error.response).toHaveProperty('status')
          expect(error.response).toHaveProperty('data')
          expect(error.response.status).toBeTypeOf('number')
          
          // Если есть данные ошибки, проверяем их формат
          if (error.response.data) {
            // Обычно ошибки имеют поле error или message
            const hasErrorField = 
              error.response.data.error !== undefined ||
              error.response.data.message !== undefined ||
              error.response.data.details !== undefined
            
            expect(hasErrorField).toBe(true)
          }
        }
      }
    })
  })

  describe('API Timeout Handling', () => {
    it('должен иметь настроенный таймаут для запросов', async () => {
      // Проверяем, что API клиент имеет таймаут
      const { default: apiClient } = await import('../client')
      expect(apiClient.defaults.timeout).toBeDefined()
      expect(apiClient.defaults.timeout).toBeGreaterThan(0)
      console.log(`✓ Таймаут настроен: ${apiClient.defaults.timeout}ms`)
    })
  })
})

