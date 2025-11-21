# Команды для пересборки проекта с Unity Build

## 🚀 Быстрый старт

### Пересборка Editor (рекомендуется)
```batch
cd C:\NECPGAME
scripts\ue\rebuild_editor_unity.cmd
```

### Пересборка Game
```batch
cd C:\NECPGAME
scripts\ue\rebuild_game_unity.cmd
```

## 📋 Полные команды

### 1. Пересборка Editor с параметрами
```batch
scripts\ue\rebuild_editor_unity.cmd "C:\Program Files\Epic Games\UE_5.7\Engine" "C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" Development
```

### 2. Пересборка Game с параметрами
```batch
scripts\ue\rebuild_game_unity.cmd "C:\Program Files\Epic Games\UE_5.7\Engine" "C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" Development
```

### 3. Прямой вызов через Build.bat
```batch
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\Build.bat" LyraEditor Win64 Development "C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" -waitmutex
```

### 4. Через UnrealBuildTool напрямую
```batch
"C:\Program Files\Epic Games\UE_5.7\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe" -Project="C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" LyraEditor Win64 Development -waitmutex
```

## 🔧 Параметры конфигурации

- **Development** - для разработки (рекомендуется)
- **DebugGame** - для отладки
- **Shipping** - для релиза
- **Test** - для тестирования

## ⚡ Оптимизации Unity Build

После пересборки Unity Build будет:
- OK Объединять несколько `.cpp` файлов в один Unity файл
- OK Использовать PCH (Precompiled Headers) для ускорения
- OK Компилировать меньше файлов при инкрементальной сборке

## 📊 Ожидаемое время сборки

- **Первый билд**: 10-20 минут (генерация Unity файлов)
- **Инкрементальная сборка**: 1-3 минуты (после изменений)
- **Полная пересборка**: 10-20 минут

## 🛠️ Дополнительные опции

### Очистка перед сборкой
```batch
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\Build.bat" LyraEditor Win64 Development "C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" -clean -waitmutex
```

### Параллельная сборка (использует все ядра)
По умолчанию UBT использует все доступные ядра процессора.

### Проверка Unity Build
После сборки проверьте логи - должны быть сообщения о Unity файлах:
```
Creating unity file: ...\Intermediate\Build\Win64\UnrealEditor\Development\LyraGame\Unity\LyraGame_1.cpp
```

## 📝 Созданные скрипты

1. `scripts\ue\rebuild_editor_unity.cmd` - пересборка Editor
2. `scripts\ue\rebuild_game_unity.cmd` - пересборка Game

## WARNING Важные замечания

1. **Первый билд**: Может быть медленнее из-за генерации Unity файлов
2. **Инкрементальная сборка**: Будет значительно быстрее
3. **Путь к UE**: Убедитесь, что путь к UE_5.7 правильный
4. **Visual Studio**: Должен быть установлен Visual Studio с C++ компонентами

## 🎯 Проверка результата

После успешной сборки:
1. Проверьте, что `UnrealEditor.exe` обновлён
2. Запустите проект в редакторе
3. Проверьте, что изменения применены
4. Измерьте время инкрементальной сборки


