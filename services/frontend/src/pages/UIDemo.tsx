import { useState } from 'react'
import {
  Button,
  Card, CardBody, CardHeader, CardFooter,
  Input,
  Select,
  Textarea,
  Modal, ModalFooter,
  Tabs,
  Badge,
  Alert,
  Progress,
} from '../components/ui'
import { MUIButton } from '../components/ui/MUIButton'
import { MUICard, MUICardContent, MUICardHeader, MUICardActions } from '../components/ui/MUICard'
import { DaisyButton } from '../components/ui/DaisyButton'
import { DaisyCard, DaisyCardBody, DaisyCardTitle, DaisyCardActions } from '../components/ui/DaisyCard'
import { MantineButton } from '../components/ui/MantineButton'

/**
 * Демонстрация всех UI компонентов
 * 
 * Эта страница показывает все доступные компоненты и их варианты
 */
export function UIDemo() {
  const [showModal, setShowModal] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [selectValue, setSelectValue] = useState('')
  const [textareaValue, setTextareaValue] = useState('')
  const [progress, setProgress] = useState(65)

  return (
    <div className="min-h-screen bg-cyber-darker p-8">
      <div className="container mx-auto max-w-7xl space-y-12">
        {/* Header */}
        <div className="text-center space-y-4">
          <h1 className="neon-text">UI KIT DEMO</h1>
          <p className="text-white text-lg uppercase tracking-widest">
            Библиотека компонентов NECPGAME в стиле Cyberpunk 2077
          </p>
          <div className="divider"></div>
        </div>

        {/* Tabs Demo */}
        <MUICard>
          <MUICardHeader title="Компоненты по категориям" />
          <MUICardContent>
            <Tabs
              tabs={[
                {
                  id: 'buttons',
                  label: 'Кнопки',
                  content: <ButtonsDemo setShowModal={setShowModal} />,
                },
                {
                  id: 'forms',
                  label: 'Формы',
                  content: (
                    <FormsDemo
                      inputValue={inputValue}
                      setInputValue={setInputValue}
                      selectValue={selectValue}
                      setSelectValue={setSelectValue}
                      textareaValue={textareaValue}
                      setTextareaValue={setTextareaValue}
                    />
                  ),
                },
                {
                  id: 'cards',
                  label: 'Карточки',
                  content: <CardsDemo />,
                },
                {
                  id: 'feedback',
                  label: 'Обратная связь',
                  content: <FeedbackDemo progress={progress} setProgress={setProgress} />,
              },
            ]}
          />
          </MUICardContent>
        </MUICard>

        {/* Modal Demo */}
        <Modal
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          title="Демо модального окна"
          size="md"
        >
          <div className="space-y-4">
            <p className="text-white text-base">
              Это модальное окно в киберпанк стиле с неоновыми эффектами.
            </p>
            <Alert type="info">
              Закройте модальное окно нажав ESC, кнопку ✕ или кликнув вне окна
            </Alert>
          </div>
          
          <ModalFooter>
            <Button variant="outlined" color="secondary" onClick={() => setShowModal(false)}>
              Отмена
            </Button>
            <Button variant="contained" color="primary" onClick={() => setShowModal(false)}>
              Понятно
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    </div>
  )
}

/* === Секции демо === */

function ButtonsDemo({ setShowModal }: { setShowModal: (show: boolean) => void }) {
  return (
    <div className="space-y-8">
      {/* Варианты */}
        <div>
          <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider text-lg" style={{ textShadow: '0 0 10px currentColor', filter: 'brightness(1.5)' }}>
            ▸ Варианты кнопок
          </h3>
        <div className="space-y-4">
          <div>
            <p className="text-white/70 mb-2 text-sm">Material UI кнопки (готовые, проверенные):</p>
            <div className="flex flex-wrap gap-4">
              <MUIButton variant="contained" color="primary">Primary</MUIButton>
              <MUIButton variant="contained" color="secondary">Secondary</MUIButton>
              <MUIButton variant="contained" color="info">Info</MUIButton>
              <MUIButton variant="contained" color="success">Success</MUIButton>
              <MUIButton variant="contained" color="warning">Warning</MUIButton>
              <MUIButton variant="contained" color="error">Error</MUIButton>
              <MUIButton variant="outlined" color="primary">Outlined</MUIButton>
              <MUIButton variant="text" color="primary">Text</MUIButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">Mantine кнопки:</p>
            <div className="flex flex-wrap gap-4">
              <MantineButton color="cyan">Primary</MantineButton>
              <MantineButton color="pink">Secondary</MantineButton>
              <MantineButton color="violet">Accent</MantineButton>
              <MantineButton color="green">Success</MantineButton>
              <MantineButton color="yellow">Warning</MantineButton>
              <MantineButton color="red">Error</MantineButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">DaisyUI кнопки:</p>
            <div className="flex flex-wrap gap-4">
              <DaisyButton variant="primary">Primary</DaisyButton>
              <DaisyButton variant="secondary">Secondary</DaisyButton>
              <DaisyButton variant="accent">Accent</DaisyButton>
              <DaisyButton variant="success">Success</DaisyButton>
              <DaisyButton variant="warning">Warning</DaisyButton>
              <DaisyButton variant="error">Error</DaisyButton>
              <DaisyButton variant="ghost">Ghost</DaisyButton>
              <DaisyButton variant="link">Link</DaisyButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">Outline варианты:</p>
            <div className="flex flex-wrap gap-4">
              <DaisyButton variant="primary" outline>Primary</DaisyButton>
              <DaisyButton variant="secondary" outline>Secondary</DaisyButton>
              <DaisyButton variant="accent" outline>Accent</DaisyButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">Material UI Button (из index.ts):</p>
            <div className="flex flex-wrap gap-4">
              <Button variant="contained" color="primary">Primary (MUI)</Button>
              <Button variant="contained" color="secondary">Secondary (MUI)</Button>
              <Button variant="contained" color="success">Success (MUI)</Button>
              <Button variant="contained" color="error">Error (MUI)</Button>
              <Button variant="outlined" color="primary">Outlined (MUI)</Button>
              <Button variant="text" color="primary">Text (MUI)</Button>
            </div>
          </div>
        </div>
      </div>

      {/* Размеры */}
        <div>
          <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider text-lg" style={{ textShadow: '0 0 10px currentColor', filter: 'brightness(1.5)' }}>
            ▸ Размеры
          </h3>
        <div className="space-y-4">
          <div>
            <p className="text-white/70 mb-2 text-sm">DaisyUI размеры:</p>
            <div className="flex flex-wrap items-center gap-4">
              <DaisyButton size="xs">Extra Small</DaisyButton>
              <DaisyButton size="sm">Small</DaisyButton>
              <DaisyButton size="md">Medium</DaisyButton>
              <DaisyButton size="lg">Large</DaisyButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">Material UI размеры:</p>
            <div className="flex flex-wrap items-center gap-4">
              <MUIButton variant="contained" size="small" color="primary">Small</MUIButton>
              <MUIButton variant="contained" size="medium" color="primary">Medium</MUIButton>
              <MUIButton variant="contained" size="large" color="primary">Large</MUIButton>
            </div>
          </div>
          <div>
            <p className="text-white/70 mb-2 text-sm">Mantine размеры:</p>
            <div className="flex flex-wrap items-center gap-4">
              <MantineButton size="xs" color="cyan">Extra Small</MantineButton>
              <MantineButton size="sm" color="cyan">Small</MantineButton>
              <MantineButton size="md" color="cyan">Medium</MantineButton>
              <MantineButton size="lg" color="cyan">Large</MantineButton>
            </div>
          </div>
        </div>
      </div>

      {/* Состояния */}
        <div>
          <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider text-lg" style={{ textShadow: '0 0 10px currentColor', filter: 'brightness(1.5)' }}>
            ▸ Состояния
          </h3>
        <div className="flex flex-wrap gap-4">
          <Button disabled>Disabled</Button>
          <Button loading>Loading...</Button>
          <Button fullWidth>Full Width</Button>
        </div>
      </div>

      {/* С иконками */}
        <div>
          <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider text-lg" style={{ textShadow: '0 0 10px currentColor', filter: 'brightness(1.5)' }}>
            ▸ С иконками
          </h3>
        <div className="flex flex-wrap gap-4">
          <Button leftIcon={<span>▸</span>}>Играть</Button>
          <Button rightIcon={<span>→</span>}>Далее</Button>
          <Button variant="contained" color="error" leftIcon={<span>✕</span>}>
            Удалить
          </Button>
        </div>
      </div>

      {/* Открыть модалку */}
        <div>
          <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider text-lg" style={{ textShadow: '0 0 10px currentColor', filter: 'brightness(1.5)' }}>
            ▸ Модальное окно
          </h3>
        <Button onClick={() => setShowModal(true)}>Открыть модалку</Button>
      </div>
    </div>
  )
}

interface FormsDemoProps {
  inputValue: string
  setInputValue: (value: string) => void
  selectValue: string
  setSelectValue: (value: string) => void
  textareaValue: string
  setTextareaValue: (value: string) => void
}

function FormsDemo({
  inputValue,
  setInputValue,
  selectValue,
  setSelectValue,
  textareaValue,
  setTextareaValue,
}: FormsDemoProps) {
  return (
    <div className="space-y-8 max-w-2xl">
      {/* Input */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Input поля
        </h3>
        <div className="space-y-4">
          <Input
            label="Имя персонажа"
            placeholder="Введите имя персонажа"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            required
          />
          <Input
            label="Email"
            type="email"
            placeholder="example@necpgame.com"
            hint="Введите ваш email"
          />
          <Input
            label="Пароль с ошибкой"
            type="password"
            placeholder="••••••••"
            error="Пароль должен содержать минимум 8 символов"
          />
          <Input
            label="С иконками"
            placeholder="Поиск..."
            leftIcon={<span>🔍</span>}
            rightIcon={<span>✕</span>}
          />
        </div>
      </div>

      {/* Select */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Select выпадающие списки
        </h3>
        <div className="space-y-4">
          <Select
            label="Выберите класс"
            placeholder="Выберите класс персонажа"
            value={selectValue}
            onChange={(e) => setSelectValue(e.target.value)}
            options={[
              { value: 'netrunner', label: 'Netrunner' },
              { value: 'solo', label: 'Solo' },
              { value: 'techie', label: 'Techie' },
              { value: 'nomad', label: 'Nomad', disabled: true },
            ]}
            required
          />
        </div>
      </div>

      {/* Textarea */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Textarea текстовые области
        </h3>
        <div className="space-y-4">
          <Textarea
            label="Описание персонажа"
            placeholder="Расскажите о вашем персонаже..."
            value={textareaValue}
            onChange={(e) => setTextareaValue(e.target.value)}
            maxLength={500}
            showCount
          />
        </div>
      </div>
    </div>
  )
}

function CardsDemo() {
  return (
    <div className="space-y-8">
      <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
        ▸ Примеры карточек
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Простая карточка MUI */}
        <MUICard>
          <MUICardContent>
            <p className="text-white">Material UI карточка с контентом</p>
          </MUICardContent>
        </MUICard>

        {/* Карточка с header MUI */}
        <MUICard>
          <MUICardHeader title="Заголовок" />
          <MUICardContent>
            <p className="text-white/90">MUI карточка с заголовком</p>
          </MUICardContent>
        </MUICard>

        {/* Карточка с hover MUI */}
        <MUICard>
          <MUICardContent>
            <p className="text-white">Наведите мышку</p>
            <p className="text-white/70 text-sm mt-2">Эффект свечения</p>
          </MUICardContent>
        </MUICard>

        {/* Полная карточка MUI */}
        <MUICard>
          <MUICardHeader 
            title="Персонаж"
            action={<Badge variant="success">Активен</Badge>}
          />
          <MUICardContent>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-white/70">Класс:</span>
                <span className="text-white">Netrunner</span>
              </div>
              <div className="flex justify-between">
                <span className="text-white/70">Уровень:</span>
                <span className="text-white">45</span>
              </div>
            </div>
          </MUICardContent>
          <MUICardActions>
            <MUIButton variant="contained" color="primary" fullWidth>
              Играть
            </MUIButton>
          </MUICardActions>
        </MUICard>
      </div>
    </div>
  )
}

function FeedbackDemo({
  progress,
  setProgress,
}: {
  progress: number
  setProgress: (value: number) => void
}) {
  return (
    <div className="space-y-8">
      {/* Badges */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Badges
        </h3>
        <div className="flex flex-wrap gap-3">
          <Badge variant="primary">Primary</Badge>
          <Badge variant="secondary">Secondary</Badge>
          <Badge variant="success">Success</Badge>
          <Badge variant="danger">Danger</Badge>
          <Badge variant="warning">Warning</Badge>
          <Badge variant="info">Info</Badge>
        </div>
      </div>

      {/* Alerts */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Alerts
        </h3>
        <div className="space-y-4">
          <Alert type="info" title="Информация">
            Это информационное сообщение
          </Alert>
          <Alert type="success" title="Успех">
            Операция выполнена успешно
          </Alert>
          <Alert type="warning" title="Предупреждение">
            Будьте осторожны!
          </Alert>
          <Alert type="error" title="Ошибка" onClose={() => console.log('Closed')}>
            Произошла ошибка. Попробуйте еще раз.
          </Alert>
        </div>
      </div>

      {/* Progress */}
      <div>
        <h3 className="text-cyber-neon-cyan font-bold mb-4 uppercase tracking-wider">
          ▸ Progress Bars
        </h3>
        <div className="space-y-6">
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-white">Прогресс:</span>
              <input
                type="range"
                min="0"
                max="100"
                value={progress}
                onChange={(e) => setProgress(Number(e.target.value))}
                className="w-48"
              />
            </div>
            <Progress value={progress} showPercent color="cyan" />
          </div>

          <div className="space-y-2">
            <span className="text-white">Разные цвета:</span>
            <Progress value={75} color="cyan" size="sm" />
            <Progress value={60} color="pink" size="md" />
            <Progress value={85} color="green" size="lg" />
            <Progress value={40} color="purple" />
            <Progress value={90} color="yellow" />
          </div>
        </div>
      </div>
    </div>
  )
}

