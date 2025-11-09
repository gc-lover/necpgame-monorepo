import { useState, useEffect } from 'react'
import { api } from '../api/client'
import { AxiosError } from 'axios'

interface ApiState<T> {
  data: T | null
  loading: boolean
  error: string | null
}

export function useApi<T>(apiCall: () => Promise<{ data: T }>, deps: unknown[] = []) {
  const [state, setState] = useState<ApiState<T>>({
    data: null,
    loading: true,
    error: null,
  })

  useEffect(() => {
    let cancelled = false

    const fetchData = async () => {
      try {
        setState(prev => ({ ...prev, loading: true, error: null }))
        const response = await apiCall()
        
        if (!cancelled) {
          setState({ data: response.data, loading: false, error: null })
        }
      } catch (error) {
        if (!cancelled) {
          const axiosError = error as AxiosError<{ message?: string }>
          const errorMessage = 
            axiosError.response?.data?.message || 
            axiosError.message || 
            'Unknown error'
          setState({
            data: null,
            loading: false,
            error: errorMessage,
          })
        }
      }
    }

    fetchData()

    return () => {
      cancelled = true
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps)

  return state
}

