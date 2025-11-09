import { describe, it, expect, beforeEach, vi } from 'vitest'
import axios from 'axios'
import apiClient, { api } from '../client'

/**
 * Тесты для API клиента
 * Проверяют конфигурацию и основные методы клиента
 */
describe('API Client', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('Конфигурация клиента', () => {
    it('должен быть настроен с правильным baseURL', () => {
      expect(apiClient.defaults.baseURL).toBeDefined()
      expect(apiClient.defaults.baseURL).toContain('/api/v1')
    })

    it('должен иметь таймаут 30 секунд', () => {
      expect(apiClient.defaults.timeout).toBe(30000)
    })

    it('должен иметь правильные заголовки по умолчанию', () => {
      expect(apiClient.defaults.headers['Content-Type']).toBe('application/json')
    })

    it('должен быть экземпляром axios', () => {
      expect(apiClient).toBeInstanceOf(Object)
      expect(typeof apiClient.get).toBe('function')
      expect(typeof apiClient.post).toBe('function')
      expect(typeof apiClient.put).toBe('function')
      expect(typeof apiClient.delete).toBe('function')
    })
  })

  describe('API методы', () => {
    it('должен экспортировать api объект с методами', () => {
      expect(api).toBeDefined()
      expect(typeof api.health).toBe('function')
      expect(api.gameplay).toBeDefined()
      expect(typeof api.gameplay.health).toBe('function')
      expect(typeof api.gameplay.status).toBe('function')
      expect(api.social).toBeDefined()
      expect(typeof api.social.health).toBe('function')
    })
  })

  describe('Interceptors', () => {
    it('должен иметь request interceptor', () => {
      expect(apiClient.interceptors.request).toBeDefined()
      expect(apiClient.interceptors.request.handlers.length).toBeGreaterThan(0)
    })

    it('должен иметь response interceptor', () => {
      expect(apiClient.interceptors.response).toBeDefined()
      expect(apiClient.interceptors.response.handlers.length).toBeGreaterThan(0)
    })
  })
})

