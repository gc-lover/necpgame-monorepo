---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
---

# Романтические события: Знакомство (Meeting Events)

20 событий для первых встреч и знакомства с NPC.


---

## RE-001: Случайная встреча в баре

**Категория:** Meeting | **Relationship:** 0-10 | **DC:** 14 (Charisma)

### Триггеры
- Локация: Bar, Club, Pub
- Время: Evening, Night
- NPC присутствует в локации

### Описание
Вы замечаете привлекательного незнакомца/незнакомку за стойкой бара. Ваши взгляды встречаются.

### Выборы
1. **Подойти и заговорить** (Charisma DC 14)
   - Успех: "Привет, я давно тебя не видел здесь. Ты новичок?" +10 relationship
   - Провал: "Э-э-э... Привет?" Awkward conversation +2 relationship

2. **Предложить выпить** (Trading DC 12)
   - Успех: NPC принимает, разговор начинается +12 relationship
   - Провал: "Спасибо, но я сам/сама куплю" +3 relationship

3. **Игнорировать** → End event, 0 relationship

### Следующие события
- Успех → RE-010 (Обмен контактами), RE-015 (Совместная прогулка)
- Провал → RE-005 (Вторая попытка знакомства)

---

## RE-002: Спасение от скавов

**Категория:** Meeting | **Relationship:** 0-15 | **DC:** 18 (Combat)

### Триггеры
- Локация: Alley, Street, Badlands
- Время: Any
- Random encounter (10% шанс)

### Описание
Вы слышите крики из переулка. NPC окружён скавами.

### Выборы
1. **Вмешаться и спасти** (Combat DC 18)
   - Успех: Скавы повержены, NPC спасён +15 relationship, "Спасибо, ты спас/спасла мне жизнь!"
   - Провал: Бой тяжелый, оба ранены, но выжили +8 relationship, "Мы... мы смогли"
   - Крит.успех: Легко справился, NPC впечатлён +20 relationship, "Ты невероятен/невероятна!"

2. **Вызвать подмогу** (Social DC 14)
   - Успех: Подмога приходит вовремя +10 relationship
   - Провал: Приходят поздно, NPC ранен +5 relationship

3. **Пройти мимо** → -10 relationship если NPC узнает

### Следующие события
- Успех → RE-023 (Помощь в беде - благодарность), RE-022 (Совместная миссия)
- Провал → RE-007 (Медицинская помощь)

---

## RE-003: Профессиональная встреча

**Категория:** Meeting | **Relationship:** 0-12 | **DC:** 16 (Social)

### Триггеры
- Локация: Office, Corporate building, Workshop
- Квест: Collaboration quest active
- NPC - коллега по квесту

### Описание
Вы встречаете NPC на деловой встрече. Нужно произвести хорошее впечатление.

### Выборы
1. **Профессиональное представление** (Social DC 16)
   - Успех: "Приятно работать с профессионалом" +12 relationship
   - Провал: "Хм, посмотрим на деле" +4 relationship

2. **Флиртовать сразу** (Charisma DC 20)
   - Успех: NPC заинтригован +15 relationship, но -5 Professional reputation
   - Провал: "Давайте сначала о работе" +0 relationship, awkward

3. **Быть сдержанным** → +6 relationship, Professional respect

### Следующие события
- Успех → RE-022 (Совместная миссия), RE-024 (Общие хобби)
- Провал → RE-010 (Обмен контактами после работы)

---

## RE-004: Нетраннерский поединок

**Категория:** Meeting | **Relationship:** 0-15 | **DC:** 20 (Hacking)

### Триггеры
- Локация: NET Cafe, Hacker Den
- Класс: Netrunner
- NPC: Netrunner

### Описание
В NET cafe проходит неофициальный турнир нетраннеров. NPC — ваш соперник.

### Выборы
1. **Честная дуэль** (Hacking DC 20)
   - Успех: Победа, "Ты силён/сильна! Уважаю!" +15 relationship
   - Провал: Поражение, "Хорошая попытка" +5 relationship
   - Крит.успех: Блестящая победа +18 relationship, Rival status

2. **Попытка взлома** (Hacking DC 24, Stealth DC 22)
   - Успех: Победа, но если раскроют -30 relationship
   - Провал: Раскрыт, "Читер!" -20 relationship

3. **Предложить ничью** (Persuasion DC 18)
   - Успех: Ничья, mutual respect +12 relationship
   - Провал: "Нет, давай честно" → forced duel

### Следующие события
- Успех → RE-024 (Общие хобби - нетраннинг), RE-030 (Соперничество)
- Провал → RE-026 (Реванш)

---

## RE-005: Столкновение на рынке

**Категория:** Meeting | **Relationship:** 0-8 | **DC:** 12 (Social)

### Триггеры
- Локация: Market, Bazaar, Shop
- Random encounter (15% шанс)

### Описание
Вы случайно сталкиваетесь с NPC, роняя их покупки.

### Выборы
1. **Извиниться и помочь собрать** (Social DC 12)
   - Успех: "Ничего страшного, спасибо за помощь" +8 relationship
   - Провал: "Будьте осторожнее!" +0 relationship, annoyed

2. **Пошутить о ситуации** (Charisma DC 14)
   - Успех: Оба смеются, ice broken +10 relationship
   - Провал: NPC не оценил шутку -2 relationship

3. **Быстро уйти** → -5 relationship, NPC remembers rudeness

### Следующие события
- Успех → RE-010 (Обмен контактами), RE-008 (Встреча в культурном месте)
- Провал → RE-012 (Вторая случайная встреча - исправить впечатление)

---

## RE-006: Общий враг

**Категория:** Meeting | **Relationship:** 0-18 | **DC:** 20 (Combat)

### Описание
Вы и NPC независимо атакованы одной и той же бандой/корпорацией. Нужно объединиться.

### Выборы
1. **Предложить союз** (Social DC 16)
   - Успех: "Давай объединимся!" Teamwork bonus +5 Combat DC
   - Провал: "Я справлюсь сам/сама" → No teamwork bonus

2. **Совместный бой** (Combat DC 20)
   - Успех: Победа, "Мы отличная команда!" +18 relationship
   - Провал: Тяжелая победа, оба ранены +10 relationship
   - Крит.успех: Синхронная атака, "Мы как единое целое!" +25 relationship

### Следующие события
- → RE-022 (Совместная миссия), RE-025 (Откровенный разговор о враге)

---

## RE-007: Медицинская помощь

**Категория:** Meeting | **Relationship:** 0-14 | **DC:** 16 (Medtech)

### Триггеры
- Локация: Clinic, Street, Combat zone
- NPC injured

### Описание
NPC ранен и нуждается в медицинской помощи. Вы — единственный, кто может помочь.

### Выборы
1. **Оказать первую помощь** (Medtech DC 16)
   - Успех: Ранение стабилизировано, "Спасибо, ты спас/спасла меня" +14 relationship
   - Провал: Помощь оказана, но грубо +6 relationship, NPC в pain

2. **Отвезти в клинику** (Driving DC 14)
   - Успех: Быстрая доставка, NPC благодарен +12 relationship
   - Провал: Долгая дорога, NPC страдает +5 relationship

3. **Позвать Trauma Team** (Cost 1000 ed)
   - → +8 relationship, NPC indebted

### Следующие события
- → RE-023 (Помощь в беде - благодарность), RE-021 (Разговор о жизни)

---

## RE-008: Культурное событие

**Категория:** Meeting | **Relationship:** 0-10 | **DC:** 14 (Culture)

### Триггеры
- Локация: Museum, Theater, Art Gallery, Concert
- Cultural event active

### Описание
Вы оба посещаете одно и то же культурное событие. Начинается непринуждённая беседа.

### Выборы
1. **Обсудить искусство/музыку** (Culture DC 14)
   - Успех: "У нас схожие вкусы!" +10 relationship, Shared interests discovered
   - Провал: "Интересная точка зрения..." +2 relationship, polite

2. **Спросить мнение NPC** (Social DC 12)
   - Успех: Deep conversation +8 relationship
   - Провал: Short answers +3 relationship

### Следующие события
- → RE-024 (Общие хобби), RE-021 (Разговор за кофе)

---

## RE-009: Спортивное соревнование

**Категория:** Meeting | **Relationship:** 0-12 | **DC:** 18 (Athletics)

### Триггеры
- Локация: Arena, Gym, Race track
- Competition event

### Описание
Вы соревнуетесь с NPC в спортивном состязании.

### Выборы
1. **Честное соревнование** (Athletics DC 18)
   - Успех: Победа, "Ты хорош/хороша!" +12 relationship, Competitive respect
   - Провал: Поражение, "Good game!" +6 relationship, Good sport

2. **Предложить сделать команду** (Social DC 14)
   - → Team event, +10 relationship if success

### Следующие события
- → RE-024 (Общие хобби - спорт), RE-030 (Дружеское соперничество)

---

## RE-010: Обмен контактами

**Категория:** Meeting | **Relationship:** 8-15 | **DC:** 14 (Social)

### Триггеры
- После любого успешного Meeting event
- Relationship 8+

### Описание
Приятный разговор подходит к концу. Предложить обменяться контактами?

### Выборы
1. **"Можно взять твой номер?"** (Social DC 14)
   - Успех: "Конечно!" +10 relationship, Contact acquired
   - Провал: "Я подумаю" +2 relationship, No contact yet

2. **Дать свою визитку** (Charisma DC 12)
   - Успех: NPC сохраняет +8 relationship, Future contact possible
   - Провал: NPC теряет визитку +0 relationship

### Следующие события
- → RE-015 (Первое текстовое сообщение), RE-021 (Звонок NPC)

---

[RE-011 до RE-020 - ещё 10 событий знакомства]

## RE-011: Спасение от киберпсихоза

**DC:** 20 (Medtech/WILL) | +16 relationship

## RE-012: Деловое предложение

**DC:** 16 (Trading) | +10 relationship

## RE-013: Совместная поездка

**DC:** 14 (Driving) | +8 relationship

## RE-014: Музыкальное выступление

**DC:** 18 (Performance) | +12 relationship

## RE-015: Встреча на тренировке

**DC:** 16 (Athletics) | +10 relationship

## RE-016: Техническая поломка

**DC:** 18 (Tech) | +12 relationship (NPC благодарен за помощь)

## RE-017: Философская дискуссия

**DC:** 16 (Intelligence) | +10 relationship

## RE-018: Спасение от корпоратов

**DC:** 22 (Combat/Stealth) | +18 relationship

## RE-019: Совместная рыбалка/охота

**DC:** 14 (Survival) | +10 relationship

## RE-020: Встреча на похоронах

**DC:** 18 (Empathy) | +14 relationship (bonding over grief)

---

## Формула relationship для Meeting Events

```
Base Relationship = Success Bonus + Class Modifier + Personality Match

Success Bonus:
- Critical Success: +18-25
- Success: +10-15
- Failure: +2-5
- Critical Failure: -5 to 0

Class Modifier:
- Same class/similar interests: +20%
- Compatible classes: +10%
- Opposite classes: +0%

Personality Match:
- High compatibility: +20%
- Medium compatibility: +10%
- Low compatibility: +0%
- Conflicting personalities: -10%
```

---

## JSON структура события

```json
{
  "eventId": "RE-001",
  "category": "meeting",
  "name": "Случайная встреча в баре",
  "relationshipRange": [0, 10],
  "triggers": {
    "locations": ["bar", "club", "pub"],
    "time": ["evening", "night"],
    "npcPresent": true
  },
  "choices": [
    {
      "choiceId": 1,
      "text": "Подойти и заговорить",
      "skillCheck": {
        "type": "Charisma",
        "dc": 14
      },
      "outcomes": {
        "success": {
          "relationship": 10,
          "dialogue": "Привет, я давно тебя не видел здесь...",
          "nextEvents": ["RE-010", "RE-015"]
        },
        "failure": {
          "relationship": 2,
          "dialogue": "Э-э-э... Привет?",
          "nextEvents": ["RE-005"]
        }
      }
    }
  ]
}
```

