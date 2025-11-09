import { AppProviders } from './app/providers'
import './styles/index.css'

/**
 * Главный компонент приложения
 * 
 * Использует SPA архитектуру с React Router
 * Все провайдеры и роутинг настроены в AppProviders
 */
function App() {
  return <AppProviders />
}

export default App
