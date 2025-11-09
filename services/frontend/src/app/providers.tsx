import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from 'react-router-dom'
import { ThemeProvider } from '@mui/material/styles'
import { CssBaseline } from '@mui/material'
import { router } from './router'
import { cyberpunkTheme } from '../theme/cyberpunkTheme'

/**
 * QueryClient для React Query
 */
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
      refetchOnMount: false,
      staleTime: 5 * 60 * 1000, // 5 минут - данные считаются свежими
      gcTime: 10 * 60 * 1000, // 10 минут - время кэширования
    },
  },
})

/**
 * Провайдеры приложения
 * 
 * Объединяет все провайдеры:
 * - QueryClientProvider (React Query)
 * - ThemeProvider (MUI Theme)
 * - RouterProvider (React Router)
 */
export function AppProviders() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={cyberpunkTheme}>
        <CssBaseline />
        <RouterProvider router={router} />
      </ThemeProvider>
    </QueryClientProvider>
  )
}

