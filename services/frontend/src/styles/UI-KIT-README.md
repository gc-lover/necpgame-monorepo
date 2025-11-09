# Киберпанк UI Kit для NECPGAME

Кастомная система дизайна в стиле Cyberpunk 2077 для текстовой версии игры.

## 🎨 Цветовая палитра

### Основные цвета
```css
bg-cyber-dark      #0a0e27  - Темный фон
bg-cyber-darker    #050812  - Самый темный фон
```

### Неоновые акценты
```css
text-cyber-neon-cyan    #00f7ff  - Основной неоновый (голубой)
text-cyber-neon-pink    #ff2a6d  - Розовый неон
text-cyber-neon-purple  #d817ff  - Фиолетовый неон
text-cyber-neon-green   #05ffa1  - Зеленый неон
text-cyber-neon-yellow  #fef86c  - Желтый неон
```

### UI элементы
```css
bg-cyber-surface        #1a1f3a  - Поверхность карточек
bg-cyber-surface-hover  #252b4a  - Hover состояние
border-cyber-border     #00f7ff40 - Граница
```

### Текст
```css
text-cyber-text-primary   #e4f0ff  - Основной текст
text-cyber-text-secondary #8b9dc3  - Вторичный текст
text-cyber-text-muted     #4a5a7a  - Приглушенный текст
```

## 📝 Типографика

### Шрифты
- **cyber**: Orbitron, Rajdhani - для заголовков и UI
- **mono-cyber**: Share Tech Mono - для монопространственного текста

### Использование
```html
<h1 class="font-cyber">Заголовок</h1>
<code class="font-mono-cyber">Код</code>
```

## 🔘 Компоненты

### Кнопки

```html
<!-- Основная кнопка -->
<button class="btn-primary">Действие</button>

<!-- Опасное действие -->
<button class="btn-danger">Удалить</button>

<!-- Успех -->
<button class="btn-success">Играть</button>

<!-- Призрачная кнопка -->
<button class="btn-ghost">Отмена</button>
```

### Карточки

```html
<div class="card">
  <div class="card-header">
    <h3 class="card-title">Заголовок</h3>
  </div>
  <p>Содержимое карточки</p>
</div>
```

### Инпуты

```html
<label class="input-label">Имя персонажа</label>
<input type="text" class="input" placeholder="Введите имя...">

<textarea class="textarea" rows="3"></textarea>
```

### Бейджи

```html
<span class="badge-primary">Уровень 5</span>
<span class="badge-danger">Критический</span>
<span class="badge-warning">Внимание</span>
```

### Алерты

```html
<div class="alert-info">
  <p>Информационное сообщение</p>
</div>

<div class="alert-warning">
  <p>Предупреждение</p>
</div>

<div class="alert-error">
  <p>Ошибка</p>
</div>

<div class="alert-success">
  <p>Успех</p>
</div>
```

## ✨ Эффекты

### Неоновое свечение

```html
<!-- Текст с неоновым эффектом -->
<h1 class="neon-text">NECPGAME</h1>

<!-- Hover с сиянием -->
<div class="hover-glow">...</div>
```

### Тени

```css
shadow-neon-cyan    - Голубое свечение
shadow-neon-pink    - Розовое свечение
shadow-neon-purple  - Фиолетовое свечение
shadow-neon-green   - Зеленое свечение
```

### Анимации

```html
<!-- Медленный пульс -->
<div class="animate-pulse-slow">...</div>

<!-- Свечение -->
<div class="animate-glow">...</div>

<!-- Эффект сканирования -->
<div class="scan-line"></div>
```

## 🎯 Утилиты

### Разделитель

```html
<div class="divider"></div>
```

### Сетка фона

```html
<div class="grid-background">
  <!-- Контент с сеткой на фоне -->
</div>
```

### Спиннер загрузки

```html
<div class="loading-spinner"></div>
```

## 📐 Layout классы Tailwind

### Сетка

```html
<!-- Адаптивная сетка карточек -->
<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
  <div class="card">...</div>
</div>
```

### Flexbox

```html
<div class="flex items-center justify-between gap-4">
  ...
</div>
```

### Spacing

```css
space-y-6  - Вертикальные отступы
gap-3      - Отступы в grid/flex
p-6        - Padding
m-4        - Margin
```

## 🎨 Примеры использования

### Карточка персонажа

```html
<div class="card hover-glow group">
  <div class="flex items-center justify-between mb-4">
    <h3 class="text-xl font-bold text-cyber-neon-cyan">
      Имя персонажа
    </h3>
    <span class="badge-primary">Ур. 5</span>
  </div>
  
  <div class="divider"></div>
  
  <div class="space-y-3">
    <div class="flex items-center gap-3">
      <span class="text-cyber-neon-cyan">▸</span>
      <span class="text-cyber-text-muted">Класс:</span>
      <span class="text-cyber-text-primary">Solo</span>
    </div>
  </div>
  
  <div class="flex gap-3 mt-6">
    <button class="btn-success flex-1">▸ Играть</button>
    <button class="btn-danger">✕</button>
  </div>
</div>
```

### Форма

```html
<form class="card max-w-md">
  <div class="card-header">
    <h2 class="card-title">Создание персонажа</h2>
  </div>
  
  <div class="space-y-4">
    <div>
      <label class="input-label">Имя</label>
      <input type="text" class="input" />
    </div>
    
    <div>
      <label class="input-label">Описание</label>
      <textarea class="textarea" rows="4"></textarea>
    </div>
    
    <div class="flex gap-3">
      <button type="submit" class="btn-primary flex-1">
        Создать
      </button>
      <button type="button" class="btn-ghost">
        Отмена
      </button>
    </div>
  </div>
</form>
```

## 🚀 Best Practices

1. **Используйте семантику**: `<button>` для кнопок, `<label>` для лейблов
2. **Accessibility**: Добавляйте `aria-label` для иконочных кнопок
3. **Responsive**: Используйте `md:` и `lg:` префиксы для адаптивности
4. **Консистентность**: Придерживайтесь единого стиля во всем приложении
5. **Анимации**: Используйте анимации умеренно для лучшего UX

## 🔧 Кастомизация

### Изменение цветов

Отредактируйте `tailwind.config.js`:

```js
colors: {
  cyber: {
    neon: {
      cyan: '#YOUR_COLOR',
      // ...
    }
  }
}
```

### Добавление новых компонентов

Добавьте в `cyberpunk-ui.css`:

```css
.your-component {
  @apply bg-cyber-surface border-2 border-cyber-border;
  /* ... */
}
```

## 📚 Ресурсы

- [Tailwind CSS](https://tailwindcss.com/)
- [Cyberpunk 2077 Color Palette](https://www.color-hex.com/color-palette/110771)
- [Google Fonts](https://fonts.google.com/)

---

**Версия UI Kit**: 1.0.0  
**Дата создания**: Ноябрь 2025  
**Статус**: Active Development

