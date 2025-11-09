/**
 * @deprecated Используйте компоненты из @/shared/ui/layout вместо этого файла!
 * 
 * Этот файл сохранен для обратной совместимости.
 * Новый код должен импортировать из:
 * import { GameLayout, MenuItem, StatCard } from '@/shared/ui/layout';
 * 
 * См. src/shared/README.md для документации.
 */

// Реэкспорт из новой библиотеки компонентов
export { 
  GameLayout, 
  MenuPanel, 
  StatsPanel, 
  MenuItem, 
  StatCard 
} from '@/shared/ui/layout';

export type {
  GameLayoutProps,
  MenuPanelProps,
  StatsPanelProps,
  MenuItemProps,
  StatCardProps,
} from '@/shared/ui/layout';

