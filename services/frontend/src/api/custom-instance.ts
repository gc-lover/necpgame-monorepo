import Axios, { AxiosError, AxiosRequestConfig } from 'axios'

/**
 * Базовый URL API
 * Можно переопределить через переменную окружения VITE_API_BASE_URL
 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

/**
 * Кастомный экземпляр Axios для Orval
 * Используется в качестве mutator для всех сгенерированных запросов
 */
export const AXIOS_INSTANCE = Axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

/**
 * Request interceptor - добавление токена авторизации
 */
AXIOS_INSTANCE.interceptors.request.use(
  (config) => {
    // Получаем токен из localStorage (или другого хранилища)
    const token = localStorage.getItem('auth_token')
    
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }

    // Логирование запросов в dev режиме
    if (import.meta.env.DEV) {
      console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`, {
        params: config.params,
        data: config.data,
      })
    }

    return config
  },
  (error) => {
    console.error('[API Request Error]', error)
    return Promise.reject(error)
  }
)

/**
 * Response interceptor - обработка ошибок
 */
AXIOS_INSTANCE.interceptors.response.use(
  (response) => {
    // Логирование успешных ответов в dev режиме
    if (import.meta.env.DEV) {
      console.log(`[API Response] ${response.status} ${response.config.url}`, response.data)
    }
    return response
  },
  (error: AxiosError) => {
    // Обработка различных типов ошибок
    if (error.response) {
      // Сервер ответил с кодом ошибки
      const { status, data } = error.response

      console.error(`[API Error] ${status}`, {
        url: error.config?.url,
        data,
      })

      // Обработка 401 - неавторизован
      if (status === 401) {
        // Удаляем токен и перенаправляем на страницу входа
        localStorage.removeItem('auth_token')
        // TODO: Добавить редирект на /login через router
        console.warn('Unauthorized - redirecting to login')
      }

      // Обработка 403 - доступ запрещен
      if (status === 403) {
        console.warn('Forbidden - insufficient permissions')
      }

      // Обработка 404 - не найдено
      if (status === 404) {
        console.warn('Resource not found')
      }

      // Обработка 500 - внутренняя ошибка сервера
      if (status === 500) {
        console.error('Internal server error')
      }
    } else if (error.request) {
      // Запрос был отправлен, но ответ не получен (проблемы с сетью)
      console.error('[Network Error]', error.request)
    } else {
      // Ошибка при настройке запроса
      console.error('[Request Setup Error]', error.message)
    }

    return Promise.reject(error)
  }
)

/**
 * Кастомная функция для выполнения запросов (используется Orval как mutator)
 * 
 * @param config - конфигурация axios запроса
 * @returns Promise с данными ответа
 */
export const customInstance = <T>(config: AxiosRequestConfig): Promise<T> => {
  const source = Axios.CancelToken.source()

  const promise = AXIOS_INSTANCE({
    ...config,
    cancelToken: source.token,
  }).then(({ data }) => data)

  // @ts-expect-error - добавляем метод cancel для возможности отмены запроса
  promise.cancel = () => {
    source.cancel('Query was cancelled')
  }

  return promise
}

/**
 * Тип для ошибок API
 */
export type ErrorType<T> = AxiosError<T>

/**
 * Тип для тела ответа
 */
export type BodyType<T> = T

export default customInstance
























