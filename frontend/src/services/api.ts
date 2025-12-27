import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import { ApiResponse, ApiError, LoginResponse, AuthTokens } from '@/types';
import { logger } from '@/utils/logger';

// Environment variables
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8000';
const API_TIMEOUT = parseInt(import.meta.env.VITE_API_TIMEOUT || '30000');

// API Error class
export class APIError extends Error {
  constructor(
    message: string,
    public status: number,
    public code: string,
    public details?: any
  ) {
    super(message);
    this.name = 'APIError';
  }
}

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: API_URL,
  timeout: API_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
});

// Request interceptor for authentication
api.interceptors.request.use(
  (config: AxiosRequestConfig): AxiosRequestConfig => {
    // Add authorization header if token exists
    const token = getAccessToken();
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Add request ID for tracking
    if (config.headers) {
      config.headers['X-Request-ID'] = generateRequestId();
    }

    // Log request in development
    if (import.meta.env.DEV) {
      logger.debug('API Request', {
        method: config.method?.toUpperCase(),
        url: config.url,
        headers: config.headers,
        data: config.data
      });
    }

    return config;
  },
  (error: AxiosError) => {
    logger.error('Request interceptor error', { error: error.message });
    return Promise.reject(error);
  }
);

// Response interceptor for error handling and token refresh
api.interceptors.response.use(
  (response: AxiosResponse): AxiosResponse => {
    // Log response in development
    if (import.meta.env.DEV) {
      logger.debug('API Response', {
        status: response.status,
        url: response.config.url,
        data: response.data
      });
    }

    return response;
  },
  async (error: AxiosError): Promise<any> => {
    const originalRequest = error.config;

    // Log error
    logger.error('API Response Error', {
      status: error.response?.status,
      url: originalRequest?.url,
      message: error.message,
      data: error.response?.data
    });

    // Handle 401 Unauthorized - try to refresh token
    if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const newTokens = await refreshAccessToken();
        if (newTokens) {
          // Update stored tokens
          setAuthTokens(newTokens);

          // Retry original request with new token
          if (originalRequest.headers) {
            originalRequest.headers.Authorization = `Bearer ${newTokens.accessToken}`;
          }

          return api(originalRequest);
        }
      } catch (refreshError) {
        // Token refresh failed - logout user
        logger.warn('Token refresh failed, logging out user');
        clearAuthTokens();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    // Handle other errors
    if (error.response?.data) {
      const apiError = error.response.data as ApiError;
      throw new APIError(
        apiError.message || 'An error occurred',
        error.response.status,
        apiError.code || 'UNKNOWN_ERROR',
        apiError.details
      );
    }

    // Network or other errors
    throw new APIError(
      error.message || 'Network error',
      error.response?.status || 0,
      'NETWORK_ERROR'
    );
  }
);

// Token management functions
function getAccessToken(): string | null {
  return localStorage.getItem('accessToken');
}

function getRefreshToken(): string | null {
  return localStorage.getItem('refreshToken');
}

function setAuthTokens(tokens: AuthTokens): void {
  localStorage.setItem('accessToken', tokens.accessToken);
  localStorage.setItem('refreshToken', tokens.refreshToken);
}

function clearAuthTokens(): void {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('user');
}

async function refreshAccessToken(): Promise<AuthTokens | null> {
  const refreshToken = getRefreshToken();
  if (!refreshToken) return null;

  try {
    const response = await axios.post<LoginResponse>(`${API_URL}/api/v1/auth/refresh`, {
      refreshToken
    });

    return {
      accessToken: response.data.accessToken,
      refreshToken: response.data.refreshToken
    };
  } catch (error) {
    logger.error('Token refresh failed', { error });
    return null;
  }
}

// Utility functions
function generateRequestId(): string {
  return `req_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}

// Generic API methods
export const apiClient = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<ApiResponse<T>>> =>
    api.get(url, config),

  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<AxiosResponse<ApiResponse<T>>> =>
    api.post(url, data, config),

  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<AxiosResponse<ApiResponse<T>>> =>
    api.put(url, data, config),

  patch: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<AxiosResponse<ApiResponse<T>>> =>
    api.patch(url, data, config),

  delete: <T = any>(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<ApiResponse<T>>> =>
    api.delete(url, config),

  // File upload method
  upload: (url: string, file: File, onProgress?: (progress: number) => void): Promise<AxiosResponse<ApiResponse<any>>> => {
    const formData = new FormData();
    formData.append('file', file);

    return api.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress(progress);
        }
      },
    });
  }
};

// Export utilities
export {
  getAccessToken,
  getRefreshToken,
  setAuthTokens,
  clearAuthTokens,
  refreshAccessToken
};

export default api;
