# Исправление вскидывания оружия и дёргания персонажа

## [SEARCH] Проблема

При наблюдении за движением других игроков возникали:
1. **Дёргание персонажа** при движении
2. **Вскидывание оружия вверх** - оружие периодически поднималось вверх

## [SYMBOL] Анализ причины

### Основная проблема

В функции `ApplyInterpolatedMovement` использовался `FMath::Lerp` для всего ротатора:

```cpp
FRotator InterpolatedRotation = FMath::Lerp(OldState.Rotation, NewState.Rotation, Alpha);
```

**Проблема**: При интерполяции между двумя снимками состояния (`OldState` и `NewState`) интерполировался не только Yaw, но и Pitch. Это вызывало:
- Конфликт с локальными анимациями оружия, которые управляют Pitch
- Вскидывание оружия вверх при интерполяции между разными значениями Pitch
- Дёргание персонажа из-за неправильной интерполяции ротации

### Дополнительная проблема

В обработке первого снимка при `World == nullptr` передавался полный `Snapshot.Rotation`, что могло содержать неправильный Pitch.

## [OK] Решение

### 1. Исправление интерполяции в `ApplyInterpolatedMovement`

**Было**:
```cpp
FRotator InterpolatedRotation = FMath::Lerp(OldState.Rotation, NewState.Rotation, Alpha);
```

**Стало**:
```cpp
float InterpolatedYaw = FMath::Lerp(OldState.Rotation.Yaw, NewState.Rotation.Yaw, Alpha);
```

Теперь интерполируется только Yaw, а Pitch и Roll всегда берутся из текущего состояния персонажа:

```cpp
const FRotator CurrentRotation = TargetPawn->GetActorRotation();
float SmoothYaw = FMath::FInterpTo(CurrentRotation.Yaw, InterpolatedYaw, World->GetDeltaSeconds(), 18.0f);
FRotator SmoothRotation(CurrentRotation.Pitch, SmoothYaw, CurrentRotation.Roll);
```

### 2. Исправление обработки первого снимка

**Было**:
```cpp
else
{
    ApplyMovementUpdate(TargetPawn, Snapshot.Location, Snapshot.Rotation, Snapshot.Velocity);
}
```

**Стало**:
```cpp
FRotator SafeRotation = FRotator(CurrentRotation.Pitch, Snapshot.Rotation.Yaw, CurrentRotation.Roll);
// ...
else
{
    ApplyMovementUpdate(TargetPawn, Snapshot.Location, SafeRotation, Snapshot.Velocity);
}
```

## [TARGET] Результат

После исправления:
- [OK] Pitch и Roll всегда сохраняются из текущего состояния персонажа
- [OK] Интерполируется только Yaw для плавного поворота
- [OK] Нет конфликтов с локальными анимациями оружия
- [OK] Оружие не вскидывается вверх
- [OK] Плавное движение других игроков без дёргания

## [NOTE] Технические детали

### Принцип работы

1. **Синхронизация только Yaw**: В `ProcessEntityUpdate` создаётся `NewRotation` с текущим Pitch и Roll:
   ```cpp
   FRotator NewRotation(CurrentRotation.Pitch, NewYaw, CurrentRotation.Roll);
   ```

2. **Интерполяция только Yaw**: В `ApplyInterpolatedMovement` интерполируется только Yaw между снимками:
   ```cpp
   float InterpolatedYaw = FMath::Lerp(OldState.Rotation.Yaw, NewState.Rotation.Yaw, Alpha);
   ```

3. **Сохранение локальных значений**: Pitch и Roll всегда берутся из текущего состояния персонажа, что позволяет локальным анимациям работать без конфликтов.

### Почему это важно

- **Pitch управляется камерой**: В UE5 Pitch обычно управляется камерой игрока и не должен синхронизироваться для других игроков
- **Анимации оружия**: Локальные анимации оружия (стрельба, перезарядка) управляют Pitch персонажа
- **Визуальная согласованность**: Синхронизация Pitch может вызывать визуальные артефакты, когда локальные анимации конфликтуют с сетевыми данными

## [SYMBOL] Файлы изменены

- `client/UE5/NECPGAME/Source/LyraGame/Net/WebSocketMovementSyncComponent.cpp`
  - Функция `ApplyInterpolatedMovement`: исправлена интерполяция ротации
  - Обработка первого снимка: исправлена передача ротации

## [WARNING] Важные замечания

1. **Только Yaw синхронизируется**: Pitch и Roll остаются локальными для каждого клиента
2. **Совместимость с анимациями**: Исправление не влияет на локальные анимации оружия
3. **Производительность**: Изменения не влияют на производительность, только на корректность интерполяции

