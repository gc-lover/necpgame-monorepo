import axios, { AxiosInstance, AxiosRequestConfig } from 'axios'

// Конфигурация API клиента
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

// Создание экземпляра axios
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    // Здесь можно добавить токен авторизации
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`
    // }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    // Обработка ошибок
    if (error.response) {
      // Сервер ответил с кодом ошибки
      console.error('API Error:', error.response.data)
    } else if (error.request) {
      // Запрос был отправлен, но ответ не получен
      console.error('Network Error:', error.request)
    } else {
      // Ошибка при настройке запроса
      console.error('Error:', error.message)
    }
    return Promise.reject(error)
  }
)

// API методы
export const api = {
  // Health check - используем реальный эндпоинт для проверки доступности
  health: () => apiClient.get('/characters/classes'),
  
  // Gameplay endpoints (пока не реализованы в бекенде)
  gameplay: {
    health: () => apiClient.get('/gameplay/health'),
    status: () => apiClient.get('/gameplay/status'),
  },
  
  // Social endpoints (пока не реализованы в бекенде)
  social: {
    health: () => apiClient.get('/social/health'),
  },
}

export default apiClient





