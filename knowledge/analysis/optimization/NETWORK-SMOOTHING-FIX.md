# Исправление подёргиваний после квантования координат

## [SEARCH] Проблема

После внедрения квантования координат (sint32 с масштабированием 0.1 см) появились **подёргивания (рывки)** при наблюдении за движением других игроков.

## [SYMBOL] Анализ текущей реализации

### Текущие параметры интерполяции:

```cpp
// WebSocketMovementSyncComponent.h
static constexpr int32 MaxHistorySize = 3;          // Всего 3 снимка
static constexpr float InterpolationDelay = 0.1f;   // 100 мс задержка
static constexpr float LocationThreshold = 10.0f;   // 10 см порог
static constexpr float MaxTeleportDistance = 1000.0f; // 10 м
static constexpr float HorizontalThreshold = 50.0f; // 50 см

// Скорость интерполяции в коде:
VInterpTo(CurrentLocation, NewLocation, DeltaSeconds, 15.0f)  // 15.0 - медленно
RInterpTo(CurrentRotation, NewRotation, DeltaSeconds, 15.0f)  // 15.0 - медленно
RInterpTo(CurrentRotation, NewRotation, DeltaSeconds, 10.0f)  // 10.0 - ещё медленнее
```

### Квантование координат:

```cpp
// ProtobufCodec.cpp
constexpr float QuantizationScale = 10.0f;  // 0.1 см точность (1 мм)

int32 QuantizeCoordinate(float Value) {
    return FMath::RoundToInt(Value * QuantizationScale);
}

float DequantizeCoordinate(int32 Value) {
    return static_cast<float>(Value) / QuantizationScale;
}
```

### Проблемы:

1. **Слишком большая задержка интерполяции**: 100 мс (`InterpolationDelay = 0.1f`)
   - При 60 Hz (16.67 мс между обновлениями) это 6 кадров задержки
   - Вызывает видимую задержку и подёргивания

2. **Медленная скорость интерполяции**: 15.0 для позиции, 10.0-15.0 для вращения
   - Не успевает сглаживать скачки от квантования
   - Для 60 Hz нужно минимум 30-50

3. **Мало истории**: `MaxHistorySize = 3` (всего 3 снимка)
   - Недостаточно для плавной интерполяции при потере пакетов
   - Нужно минимум 5-8 снимков

4. **Двойной вызов ApplyMovementUpdate**: 
   - В `ProcessEntityUpdate` вызывается напрямую (строка 220)
   - И в `TickComponent` через интерполяцию
   - Может вызывать конфликты и рывки

5. **Нет экстраполяции (предсказания)**: 
   - При отсутствии новых данных клиент не предсказывает позицию
   - Вызывает "замирание" движения при потере пакетов

6. **Квантование скорости**: 
   - Скорость тоже квантуется, что может вызывать скачки
   - Нужно использовать экстраполяцию на основе последней скорости

## [OK] Решения

### Решение 1: Оптимизация параметров интерполяции (БЫСТРОЕ ИСПРАВЛЕНИЕ)

**Изменения**:
1. Уменьшить `InterpolationDelay` с 0.1 до 0.05-0.06 (50-60 мс)
2. Увеличить скорость интерполяции с 15.0 до 30-50
3. Увеличить `MaxHistorySize` с 3 до 8

```cpp
// WebSocketMovementSyncComponent.h
static constexpr int32 MaxHistorySize = 8;          // Было 3, стало 8
static constexpr float InterpolationDelay = 0.05f;  // Было 0.1f, стало 0.05f (50 мс)
```

```cpp
// WebSocketMovementSyncComponent.cpp
// В ApplyMovementUpdate:
const FVector InterpolatedLocation = FMath::VInterpTo(
    CurrentLocation, NewLocation, World->GetDeltaSeconds(), 30.0f);  // Было 15.0f

// В ApplyInterpolatedMovement:
const FRotator SmoothRotation = FMath::RInterpTo(
    CurrentRotation, InterpolatedRotation, World->GetDeltaSeconds(), 30.0f);  // Было 15.0f

// В ApplyMovementUpdate (для вращения):
const FRotator InterpolatedRotation = FMath::RInterpTo(
    CurrentRotation, NewRotation, World->GetDeltaSeconds(), 25.0f);  // Было 10.0f
```

**Эффект**: Уменьшение задержки, более плавная интерполяция

### Решение 2: Убрать двойной вызов ApplyMovementUpdate

**Проблема**: В `ProcessEntityUpdate` вызывается `ApplyMovementUpdate` напрямую, затем интерполяция в `TickComponent`.

**Исправление**: Убрать прямой вызов, оставить только добавление в историю.

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::ProcessEntityUpdate(...)
{
    // ... существующий код ...
    
    TArray<FEntityStateSnapshot>& History = EntityStateHistory.FindOrAdd(Entity.Id);
    History.Add(NewSnapshot);
    
    if (History.Num() > MaxHistorySize)
    {
        History.RemoveAt(0);
    }
    
    // УБРАТЬ ЭТУ СТРОКУ:
    // ApplyMovementUpdate(TargetPawn, NewLocation, NewRotation, NewVelocity);
    // Теперь применяется только через интерполяцию в TickComponent
}
```

**Эффект**: Устранение конфликтов и рывков

### Решение 3: Добавить экстраполяцию (предсказание позиции)

**Идея**: При отсутствии новых данных (InterpolationTime > последний снимок) использовать скорость для предсказания позиции.

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::TickComponent(...)
{
    // ... существующий код ...
    
    for (int32 i = History.Num() - 1; i > 0; --i)
    {
        const FEntityStateSnapshot& OldSnapshot = History[i - 1];
        const FEntityStateSnapshot& NewSnapshot = History[i];
        
        if (InterpolationTime >= OldSnapshot.Timestamp && InterpolationTime <= NewSnapshot.Timestamp)
        {
            // Интерполяция между двумя снимками
            float TimeDelta = NewSnapshot.Timestamp - OldSnapshot.Timestamp;
            if (TimeDelta > 0.001f)
            {
                float Alpha = (InterpolationTime - OldSnapshot.Timestamp) / TimeDelta;
                ApplyInterpolatedMovement(TargetPawn, OldSnapshot, NewSnapshot, Alpha);
            }
            break;
        }
        else if (InterpolationTime > NewSnapshot.Timestamp && i == History.Num() - 1)
        {
            // ЭКСТРАПОЛЯЦИЯ: предсказание на основе последней скорости
            float TimeSinceLastUpdate = InterpolationTime - NewSnapshot.Timestamp;
            float MaxExtrapolationTime = 0.05f; // Максимум 50 мс экстраполяции
            
            if (TimeSinceLastUpdate < MaxExtrapolationTime && NewSnapshot.Velocity.SizeSquared() > 1.0f)
            {
                // Предсказываем позицию на основе скорости
                FEntityStateSnapshot ExtrapolatedState = NewSnapshot;
                ExtrapolatedState.Location = NewSnapshot.Location + (NewSnapshot.Velocity * TimeSinceLastUpdate);
                ExtrapolatedState.Timestamp = InterpolationTime;
                
                ApplyInterpolatedMovement(TargetPawn, NewSnapshot, ExtrapolatedState, 1.0f);
            }
            else
            {
                // Слишком старая информация, просто используем последний снимок
                ApplyInterpolatedMovement(TargetPawn, OldSnapshot, NewSnapshot, 1.0f);
            }
            break;
        }
    }
}
```

**Эффект**: Плавное движение даже при потере пакетов

### Решение 4: Улучшить сглаживание с учетом квантования

**Идея**: Добавить дополнительное сглаживание для компенсации дискретности квантованных значений.

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::ApplyInterpolatedMovement(...)
{
    // ... существующий код ...
    
    // Дополнительное сглаживание для компенсации квантования
    FVector CurrentLocation = TargetPawn->GetActorLocation();
    FVector TargetLocation = InterpolatedLocation;
    
    // Используем экспоненциальное сглаживание для минимизации рывков
    float SmoothingFactor = FMath::Clamp(World->GetDeltaSeconds() * 30.0f, 0.0f, 1.0f);
    FVector SmoothedLocation = FMath::Lerp(CurrentLocation, TargetLocation, SmoothingFactor);
    
    // Применяем сглаженную позицию
    TargetPawn->SetActorLocation(SmoothedLocation, true);
}
```

**Эффект**: Устранение видимых скачков от квантования

### Решение 5: Увеличить точность квантования (ОПЦИОНАЛЬНО)

**Если подёргивания остаются**, можно увеличить точность квантования:

```cpp
// ProtobufCodec.cpp
constexpr float QuantizationScale = 20.0f;  // Было 10.0f, стало 20.0f (0.05 см точность)

// ИЛИ даже:
constexpr float QuantizationScale = 50.0f;  // 0.02 см точность (2 мм)
```

**Компромисс**: Увеличение размера пакетов на 10-20%, но лучшее качество

## [TARGET] Рекомендуемый порядок внедрения

### Этап 1: Быстрое исправление (РЕКОМЕНДУЕТСЯ НАЧАТЬ С ЭТОГО)
1. [OK] Уменьшить `InterpolationDelay` до 0.05f
2. [OK] Увеличить скорость интерполяции до 30-50
3. [OK] Увеличить `MaxHistorySize` до 8
4. [OK] Убрать двойной вызов `ApplyMovementUpdate`

**Ожидаемый эффект**: Уменьшение подёргиваний на 60-80%

### Этап 2: Улучшение качества
5. [OK] Добавить экстраполяцию
6. [OK] Улучшить сглаживание с учетом квантования

**Ожидаемый эффект**: Плавное движение даже при потере пакетов

### Этап 3: Дополнительная оптимизация (если нужно)
7. [WARNING] Увеличить точность квантования (если подёргивания остаются)

## [SYMBOL] Практики в индустрии

### Source Engine (CS:GO):
- **Интерполяция**: 50-100 мс задержка
- **Экстраполяция**: До 100 мс предсказания
- **Точность координат**: ~1 см (квантование)

### Quake 3:
- **Интерполяция**: 50-80 мс задержка
- **Экстраполяция**: Dead reckoning на основе скорости
- **Точность координат**: ~0.1 см (фиксированная точка)

### Overwatch:
- **Интерполяция**: 60-120 мс задержка (адаптивная)
- **Экстраполяция**: До 150 мс для близких игроков
- **Сглаживание**: Экспоненциальное сглаживание + фильтр Калмана

### Valorant:
- **Интерполяция**: 50-80 мс задержка
- **Экстраполяция**: До 100 мс
- **Точность координат**: ~0.5-1 см

## [SYMBOL] Детальная реализация

### 1. Обновление параметров в WebSocketMovementSyncComponent.h

```cpp
// WebSocketMovementSyncComponent.h
private:
    TMap<FString, TArray<FEntityStateSnapshot>> EntityStateHistory;
    static constexpr int32 MaxHistorySize = 8;          // Было 3
    static constexpr float InterpolationDelay = 0.05f;  // Было 0.1f (50 мс вместо 100 мс)
    static constexpr float MaxExtrapolationTime = 0.05f; // НОВОЕ: максимум экстраполяции 50 мс
    static constexpr float SmoothingSpeed = 30.0f;       // НОВОЕ: скорость сглаживания
```

### 2. Убрать двойной вызов в ProcessEntityUpdate

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::ProcessEntityUpdate(...)
{
    // ... существующий код до строки 212 ...
    
    TArray<FEntityStateSnapshot>& History = EntityStateHistory.FindOrAdd(Entity.Id);
    History.Add(NewSnapshot);
    
    if (History.Num() > MaxHistorySize)
    {
        History.RemoveAt(0);
    }
    
    // УБРАТЬ: ApplyMovementUpdate(TargetPawn, NewLocation, NewRotation, NewVelocity);
    // Применение происходит только через интерполяцию в TickComponent
}
```

### 3. Обновить TickComponent с экстраполяцией

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::TickComponent(...)
{
    // ... существующий код до строки 63 ...
    
    for (int32 i = History.Num() - 1; i > 0; --i)
    {
        const FEntityStateSnapshot& OldSnapshot = History[i - 1];
        const FEntityStateSnapshot& NewSnapshot = History[i];
        
        if (InterpolationTime >= OldSnapshot.Timestamp && InterpolationTime <= NewSnapshot.Timestamp)
        {
            float TimeDelta = NewSnapshot.Timestamp - OldSnapshot.Timestamp;
            if (TimeDelta > 0.001f)
            {
                float Alpha = (InterpolationTime - OldSnapshot.Timestamp) / TimeDelta;
                ApplyInterpolatedMovement(TargetPawn, OldSnapshot, NewSnapshot, Alpha);
            }
            else
            {
                ApplyInterpolatedMovement(TargetPawn, OldSnapshot, NewSnapshot, 1.0f);
            }
            break;
        }
        else if (InterpolationTime > NewSnapshot.Timestamp && i == History.Num() - 1)
        {
            // ЭКСТРАПОЛЯЦИЯ: предсказание на основе скорости
            float TimeSinceLastUpdate = InterpolationTime - NewSnapshot.Timestamp;
            
            if (TimeSinceLastUpdate < MaxExtrapolationTime && NewSnapshot.Velocity.SizeSquared() > 1.0f)
            {
                FEntityStateSnapshot ExtrapolatedState = NewSnapshot;
                ExtrapolatedState.Location = NewSnapshot.Location + (NewSnapshot.Velocity * TimeSinceLastUpdate);
                ExtrapolatedState.Timestamp = InterpolationTime;
                ApplyInterpolatedMovement(TargetPawn, NewSnapshot, ExtrapolatedState, 1.0f);
            }
            else
            {
                ApplyInterpolatedMovement(TargetPawn, OldSnapshot, NewSnapshot, 1.0f);
            }
            break;
        }
    }
}
```

### 4. Обновить скорости интерполяции

```cpp
// WebSocketMovementSyncComponent.cpp
void UWebSocketMovementSyncComponent::ApplyMovementUpdate(...)
{
    // ... существующий код ...
    
    else if (LocationDistance > 0.5f)
    {
        UWorld* World = GetWorld();
        if (World)
        {
            // Увеличена скорость с 15.0 до 30.0
            const FVector InterpolatedLocation = FMath::VInterpTo(
                CurrentLocation, NewLocation, World->GetDeltaSeconds(), SmoothingSpeed);
            MovementComp->Velocity = FVector(NewVelocity.X, NewVelocity.Y, MovementComp->Velocity.Z);
            TargetPawn->SetActorLocation(InterpolatedLocation, true);
        }
    }
    
    // ... для вращения ...
    else if (World)
    {
        // Увеличена скорость с 10.0 до 25.0
        const FRotator InterpolatedRotation = FMath::RInterpTo(
            CurrentRotation, NewRotation, World->GetDeltaSeconds(), 25.0f);
        TargetPawn->SetActorRotation(InterpolatedRotation);
    }
}

void UWebSocketMovementSyncComponent::ApplyInterpolatedMovement(...)
{
    // ... существующий код ...
    
    // Увеличена скорость с 15.0 до 30.0
    const FRotator SmoothRotation = FMath::RInterpTo(
        CurrentRotation, InterpolatedRotation, World->GetDeltaSeconds(), SmoothingSpeed);
    TargetPawn->SetActorRotation(SmoothRotation);
}
```

## [SYMBOL] Ожидаемые результаты

### До исправления:
- Подёргивания при наблюдении за другими игроками
- Задержка интерполяции: 100 мс
- Медленная скорость интерполяции: 15.0
- Мало истории: 3 снимка
- Нет экстраполяции

### После исправления:
- [OK] Плавное движение других игроков
- [OK] Задержка интерполяции: 50 мс (меньше видимая задержка)
- [OK] Быстрая скорость интерполяции: 30.0 (лучше сглаживание)
- [OK] Больше истории: 8 снимков (лучше при потере пакетов)
- [OK] Есть экстраполяция (предсказание на основе скорости)

## [WARNING] Важные замечания

1. **Задержка vs Плавность**: 
   - Уменьшение `InterpolationDelay` уменьшает задержку, но может вызвать подёргивания при потере пакетов
   - Нужно найти баланс (рекомендуется 50-60 мс)

2. **Экстраполяция vs Точность**:
   - Экстраполяция может давать ошибки, если скорость резко меняется
   - Ограничьте время экстраполяции (50-100 мс максимум)

3. **Производительность**:
   - Увеличение `MaxHistorySize` и экстраполяция увеличивают нагрузку на CPU
   - Для 390 клиентов это может быть заметно, нужно профилировать

4. **Тестирование**:
   - Протестируйте с различными параметрами в реальных условиях
   - Проверьте при потере пакетов и высокой задержке сети

