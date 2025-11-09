import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './styles/index.css'

/**
 * Точка входа в приложение
 * 
 * Монтирует React приложение в DOM элемент с id="root"
 * 
 * Примечание: ThemeProvider и другие провайдеры теперь в AppProviders
 */
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
)
