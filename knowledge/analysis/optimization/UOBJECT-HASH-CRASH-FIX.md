# Исправление краша UObject hash table

## [SEARCH] Проблема

Краш при работе с сетевой синхронизацией:
```
Fatal error: [File:D:\build\++UE5\Sync\Engine\Source\Runtime\CoreUObject\Private\UObject\UObjectHash.cpp] [Line: 644] 
Trying to modify UObject map (FindOrAdd) that is currently being iterated.
```

## [SYMBOL] Анализ причины

### Основная проблема

Краш происходил из-за модификации UObject hash tables во время итерации:

1. **Итерация по PlayerController**: В `FindControllerByPlayerId` и `OnGameStateReceived` происходила итерация по `World->GetPlayerControllerIterator()`
2. **Обращение к UObject**: Во время итерации вызывался `GetPlayerIdFromController`, который обращался к `PlayerState` (UObject)
3. **Модификация TMap**: При добавлении в `EntityStateHistory` TMap могла происходить модификация UObject hash tables

### Почему это вызывало краш

- UE5 не позволяет модифицировать UObject hash tables во время итерации по ним
- `GetPlayerIdFromController` обращается к `PlayerState`, который является UObject
- Если в это время происходит итерация по UObject hash tables (например, сборка мусора), возникает краш

## [OK] Решение

### 1. Предварительный сбор контроллеров

**Было**:
```cpp
for (const FProtobufCodec::FEntityState& Entity : ServerMsg.GameState.Snapshot.Entities)
{
    ProcessEntityUpdate(Entity, GameStateTick, LocalPlayerId, World);
    // Внутри ProcessEntityUpdate вызывался FindControllerByPlayerId
}
```

**Стало**:
```cpp
TMap<FString, APlayerController*> ControllerMap;
for (FConstPlayerControllerIterator It = World->GetPlayerControllerIterator(); It; ++It)
{
    if (APlayerController* PC = It->Get())
    {
        FString PCPlayerId = GetPlayerIdFromController(PC);
        if (!PCPlayerId.IsEmpty())
        {
            ControllerMap.Add(PCPlayerId, PC);
        }
    }
}

for (const FProtobufCodec::FEntityState& Entity : ServerMsg.GameState.Snapshot.Entities)
{
    APlayerController* TargetController = ControllerMap.FindRef(EntityId);
    if (TargetController && IsValid(TargetController))
    {
        ProcessEntityUpdate(Entity, GameStateTick, LocalPlayerId, World, TargetController);
    }
}
```

### 2. Изменение сигнатуры ProcessEntityUpdate

**Было**:
```cpp
void ProcessEntityUpdate(const FProtobufCodec::FEntityState& Entity, int64 GameStateTick, const FString& LocalPlayerId, UWorld* World);
```

**Стало**:
```cpp
void ProcessEntityUpdate(const FProtobufCodec::FEntityState& Entity, int64 GameStateTick, const FString& LocalPlayerId, UWorld* World, APlayerController* TargetController);
```

Теперь контроллер передаётся напрямую, избегая поиска во время обработки.

### 3. Безопасная итерация в FindControllerByPlayerId

**Было**:
```cpp
for (FConstPlayerControllerIterator It = World->GetPlayerControllerIterator(); It; ++It)
{
    if (APlayerController* PC = It->Get())
    {
        FString PCPlayerId = GetPlayerIdFromController(PC);
        // Прямое обращение к UObject во время итерации
    }
}
```

**Стало**:
```cpp
TArray<APlayerController*> Controllers;
for (FConstPlayerControllerIterator It = World->GetPlayerControllerIterator(); It; ++It)
{
    if (APlayerController* PC = It->Get())
    {
        Controllers.Add(PC);
    }
}

for (APlayerController* PC : Controllers)
{
    if (!IsValid(PC))
    {
        continue;
    }
    
    FString PCPlayerId = GetPlayerIdFromController(PC);
    // Обращение к UObject после завершения итерации
}
```

### 4. Добавление проверок валидности

Добавлены проверки `IsValid()` перед обращением к UObject:

```cpp
FString UWebSocketMovementSyncComponent::GetPlayerIdFromController(APlayerController* Controller) const
{
    if (!Controller || !IsValid(Controller))
    {
        return FString();
    }

    ALyraPlayerController* LyraPC = Cast<ALyraPlayerController>(Controller);
    if (!LyraPC || !IsValid(LyraPC))
    {
        return FString();
    }

    if (ALyraPlayerState* LyraPS = LyraPC->GetLyraPlayerState())
    {
        if (IsValid(LyraPS))
        {
            // Безопасное обращение к UObject
        }
    }
}
```

## [TARGET] Результат

После исправления:
- [OK] Нет модификации UObject hash tables во время итерации
- [OK] Контроллеры собираются заранее в безопасном месте
- [OK] Обращение к UObject происходит после завершения итерации
- [OK] Добавлены проверки валидности для предотвращения доступа к невалидным объектам
- [OK] Краш устранён

## [NOTE] Технические детали

### Принцип работы

1. **Предварительный сбор**: Все контроллеры собираются в TMap до обработки сущностей
2. **Безопасная итерация**: Итерация по PlayerController завершается до обращения к UObject
3. **Прямая передача**: Контроллер передаётся напрямую в `ProcessEntityUpdate`, избегая повторного поиска
4. **Проверки валидности**: Все обращения к UObject защищены проверками `IsValid()`

### Почему это важно

- **Безопасность потоков**: Избегаем конфликтов при доступе к UObject из разных потоков
- **Стабильность**: Предотвращаем краши при сборке мусора или других операциях с UObject hash tables
- **Производительность**: Предварительный сбор контроллеров более эффективен, чем повторный поиск

## [SYMBOL] Файлы изменены

- `client/UE5/NECPGAME/Source/LyraGame/Net/WebSocketMovementSyncComponent.cpp`
  - Функция `OnGameStateReceived`: предварительный сбор контроллеров
  - Функция `ProcessEntityUpdate`: изменена сигнатура для приёма контроллера
  - Функция `FindControllerByPlayerId`: безопасная итерация с предварительным сбором
  - Функция `GetPlayerIdFromController`: добавлены проверки валидности

- `client/UE5/NECPGAME/Source/LyraGame/Net/WebSocketMovementSyncComponent.h`
  - Изменена сигнатура `ProcessEntityUpdate`

## [WARNING] Важные замечания

1. **Избегайте модификации UObject hash tables во время итерации**: Всегда собирайте данные заранее
2. **Проверяйте валидность**: Используйте `IsValid()` перед обращением к UObject
3. **Безопасная итерация**: Собирайте итераторы в массивы перед обращением к UObject

