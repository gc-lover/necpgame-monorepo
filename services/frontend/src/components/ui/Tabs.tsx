import { ReactNode, useState } from 'react'

/**
 * Tab item
 */
export interface Tab {
  id: string
  label: string
  content: ReactNode
  disabled?: boolean
  icon?: ReactNode
}

/**
 * Пропсы компонента Tabs
 */
export interface TabsProps {
  /** Список табов */
  tabs: Tab[]
  /** Активный таб по умолчанию */
  defaultTab?: string
  /** Callback при смене таба */
  onChange?: (tabId: string) => void
}

/**
 * Компонент табов в киберпанк стиле
 */
export function Tabs({ tabs, defaultTab, onChange }: TabsProps) {
  const [activeTab, setActiveTab] = useState(defaultTab || tabs[0]?.id)
  
  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId)
    onChange?.(tabId)
  }
  
  const activeTabContent = tabs.find(tab => tab.id === activeTab)?.content
  
  return (
    <div className="w-full">
      {/* Tab Headers */}
      <div className="flex gap-2 border-b-2 border-cyber-border overflow-x-auto">
        {tabs.map((tab) => {
          const isActive = tab.id === activeTab
          
          return (
            <button
              key={tab.id}
              onClick={() => !tab.disabled && handleTabChange(tab.id)}
              disabled={tab.disabled}
              className={`
                px-6 py-3 font-semibold uppercase tracking-wider text-sm
                border-b-2 transition-all duration-300
                flex items-center gap-2 whitespace-nowrap
                ${isActive 
                  ? 'border-cyber-neon-cyan text-cyber-neon-cyan' 
                  : 'border-transparent text-white/70 hover:text-white hover:border-cyber-border-hover'
                }
                ${tab.disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
              `}
            >
              {tab.icon && <span>{tab.icon}</span>}
              <span>{tab.label}</span>
            </button>
          )
        })}
      </div>
      
      {/* Tab Content */}
      <div className="py-6">
        {activeTabContent}
      </div>
    </div>
  )
}

