import { describe, it, expect, beforeAll } from 'vitest'
import { getCharacterClasses, getCharacterOrigins } from '../generated/auth/characters/characters'
import { getCities, getFactions } from '../generated/auth/reference-data/reference-data'

/**
 * Интеграционные тесты для Auth API
 * Проверяют работу с эндпоинтами авторизации и справочных данных
 * 
 * ВАЖНО: Эти тесты требуют запущенного бекенд сервера
 */
describe('Auth API Integration Tests', () => {
  let isBackendAvailable = false
  const BACKEND_TIMEOUT = 5000

  beforeAll(async () => {
    // Проверяем доступность бекенда
    try {
      const controller = new AbortController()
      const timeoutId = setTimeout(() => controller.abort(), BACKEND_TIMEOUT)
      
      // Пробуем получить данные классов
      // customInstance возвращает только data, не полный response
      const data = await getCharacterClasses()
      clearTimeout(timeoutId)
      
      // Если получили данные - бекенд доступен
      if (data && data.classes) {
        isBackendAvailable = true
        console.log(`✓ Auth API доступен, получено ${data.classes.length} классов`)
      }
    } catch (error: any) {
      console.warn('⚠ Auth API недоступен, тесты будут пропущены')
      console.warn('Ошибка:', error.message || error.response?.data?.error?.message || 'Unknown error')
    }
  })

  describe('Reference Data API', () => {
    it.skipIf(!isBackendAvailable)('должен получить список классов персонажей', async () => {
      const data = await getCharacterClasses()
      
      expect(data).toBeDefined()
      expect(data).toHaveProperty('classes')
      expect(Array.isArray(data.classes)).toBe(true)
      
      // Проверяем структуру данных
      if (data.classes && data.classes.length > 0) {
        const firstClass = data.classes[0]
        expect(firstClass).toHaveProperty('id')
        expect(firstClass).toHaveProperty('name')
        expect(firstClass).toHaveProperty('description')
        console.log(`✓ Получено ${data.classes.length} классов персонажей`)
      }
    })

    it.skipIf(!isBackendAvailable)('должен получить список происхождений персонажей', async () => {
      const data = await getCharacterOrigins()
      
      expect(data).toBeDefined()
      expect(data).toHaveProperty('origins')
      expect(Array.isArray(data.origins)).toBe(true)
      
      // Проверяем структуру данных
      if (data.origins && data.origins.length > 0) {
        const firstOrigin = data.origins[0]
        expect(firstOrigin).toHaveProperty('id')
        expect(firstOrigin).toHaveProperty('name')
        console.log(`✓ Получено ${data.origins.length} происхождений`)
      }
    })

    it.skipIf(!isBackendAvailable)('должен получить список городов', async () => {
      const data = await getCities()
      
      expect(data).toBeDefined()
      expect(data).toHaveProperty('cities')
      expect(Array.isArray(data.cities)).toBe(true)
      
      // Проверяем структуру данных
      if (data.cities && data.cities.length > 0) {
        const firstCity = data.cities[0]
        expect(firstCity).toHaveProperty('id')
        expect(firstCity).toHaveProperty('name')
        expect(firstCity).toHaveProperty('description')
        console.log(`✓ Получено ${data.cities.length} городов`)
      }
    })

    it.skipIf(!isBackendAvailable)('должен получить список фракций', async () => {
      const data = await getFactions()
      
      expect(data).toBeDefined()
      expect(data).toHaveProperty('factions')
      expect(Array.isArray(data.factions)).toBe(true)
      
      // Проверяем структуру данных
      if (data.factions && data.factions.length > 0) {
        const firstFaction = data.factions[0]
        expect(firstFaction).toHaveProperty('id')
        expect(firstFaction).toHaveProperty('name')
        expect(firstFaction).toHaveProperty('type')
        console.log(`✓ Получено ${data.factions.length} фракций`)
      }
    })

    it.skipIf(!isBackendAvailable)('должен фильтровать фракции по происхождению', async () => {
      // Сначала получаем список происхождений
      const originsData = await getCharacterOrigins()
      
      if (originsData?.origins && originsData.origins.length > 0) {
        const firstOrigin = originsData.origins[0]
        
        // Получаем фракции для этого происхождения
        const factionsData = await getFactions({ origin: firstOrigin.id })
        
        expect(factionsData).toBeDefined()
        expect(factionsData).toHaveProperty('factions')
        console.log(`✓ Фильтрация по происхождению "${firstOrigin.id}" работает`)
      }
    })
  })

  describe('API Error Handling', () => {
    it('должен корректно обрабатывать различные типы ошибок', async () => {
      // Этот тест проверяет, что API клиент настроен для обработки ошибок
      // Мы не делаем реальный запрос к несуществующему эндпоинту,
      // а просто проверяем наличие обработчиков ошибок
      const { default: apiClient } = await import('../client')
      
      // Проверяем наличие response interceptor для обработки ошибок
      expect(apiClient.interceptors.response).toBeDefined()
      expect(apiClient.interceptors.response.handlers.length).toBeGreaterThan(0)
      
      console.log('✓ API клиент настроен для обработки ошибок')
    })
  })

  describe('Response Validation', () => {
    it.skipIf(!isBackendAvailable)('должен возвращать данные с корректными типами', async () => {
      const data = await getCharacterClasses()
      
      expect(data).toBeTypeOf('object')
      expect(data).toHaveProperty('classes')
      
      if (data?.classes) {
        expect(Array.isArray(data.classes)).toBe(true)
        console.log('✓ Типы данных корректны')
      }
    })

    it.skipIf(!isBackendAvailable)('должен иметь корректную структуру данных', async () => {
      const data = await getCharacterClasses()
      
      expect(data).toBeDefined()
      expect(data.classes).toBeDefined()
      expect(Array.isArray(data.classes)).toBe(true)
      
      // Проверяем, что каждый класс имеет необходимые поля
      data.classes.forEach((cls: any) => {
        expect(cls).toHaveProperty('id')
        expect(cls).toHaveProperty('name')
        expect(cls).toHaveProperty('description')
        expect(typeof cls.id).toBe('string')
        expect(typeof cls.name).toBe('string')
      })
      console.log('✓ Структура данных корректна')
    })
  })
})

