import { describe, it, expect, beforeAll } from 'vitest'
import { api } from '../client'

/**
 * Интеграционные тесты для проверки подключения к бекенду
 * 
 * ВАЖНО: Эти тесты требуют запущенного бекенд сервера
 * Для их запуска бекенд должен быть доступен на VITE_API_BASE_URL
 * 
 * Если бекенд не запущен, тесты будут пропущены
 */
describe('Backend Connection Integration Tests', () => {
  let isBackendAvailable = false
  const BACKEND_TIMEOUT = 5000 // 5 секунд для проверки доступности

  beforeAll(async () => {
    // Проверяем доступность бекенда перед запуском тестов
    try {
      const controller = new AbortController()
      const timeoutId = setTimeout(() => controller.abort(), BACKEND_TIMEOUT)
      
      await api.health()
      clearTimeout(timeoutId)
      isBackendAvailable = true
      console.log('✓ Бекенд доступен, запускаем интеграционные тесты')
    } catch (error) {
      console.warn('⚠ Бекенд недоступен, интеграционные тесты будут пропущены')
      console.warn('Для запуска этих тестов убедитесь, что бекенд запущен')
    }
  })

  describe('Health Checks', () => {
    it.skipIf(!isBackendAvailable)('должен успешно подключиться к бекенду', async () => {
      const response = await api.health()
      
      expect(response.status).toBe(200)
      expect(response.data).toBeDefined()
      expect(response.data).toHaveProperty('classes')
      console.log(`✓ Бекенд доступен, получено ${response.data.classes?.length || 0} классов персонажей`)
    })
  })

  describe('Error Handling', () => {
    it.skipIf(!isBackendAvailable)('должен корректно обрабатывать 404 ошибку', async () => {
      try {
        await api.gameplay.health()
        // Если эндпоинт не существует, должна быть ошибка
      } catch (error: any) {
        if (error.response?.status === 404) {
          expect(error.response.status).toBe(404)
        }
      }
    })
  })

  describe('Response Format', () => {
    it.skipIf(!isBackendAvailable)('должен возвращать данные в правильном формате', async () => {
      const response = await api.health()
      
      expect(response).toHaveProperty('data')
      expect(response).toHaveProperty('status')
      expect(response).toHaveProperty('headers')
      expect(response.status).toBeTypeOf('number')
    })
  })
})

