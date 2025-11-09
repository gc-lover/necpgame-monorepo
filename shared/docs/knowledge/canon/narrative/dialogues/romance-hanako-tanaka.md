# Романтические сцены — Hanako Tanaka (Этапы 1-2)

**ID диалога:** `dialogue-romance-hanako`  
**Тип:** romance  
**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 20:43  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/hanako-arasaka.md`, `../dialogues/npc-viktor-vektor.md`, `../../02-gameplay/social/romance-system.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/romance  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:43
**api-readiness-notes:** «Этапы 1-2 романса с Ханако оформлены: чайная сцена, небесный сад, проверки, YAML и обновлённая API-интеграция.»

---

---

## 1. Контекст и цели

- **Этап 1:** Приватная чайная комната Arasaka Tower после успешного корпоративного задания.
- **Этап 2:** Закрытый небесный сад Киото-Сити (орбитальная ветвь), где Arasaka хранили архивы 2050-х, с AR-симуляцией ханами.
- **Цели:** Проверить честь игрока, совместить личное с корпоративным долгом, предложить выбор между служением Arasaka и личной независимостью.
- **Интеграции:** `rep.romance.hanako`, `flag.romance.hanako.stage*`, `rep.corp.arasaka`, `flag.arasaka.clearanceA`, `world.event.corporate_war_escalation`, `world.event.blackwall_breach`.

## 2. Состояния и условия

| Этап | Состояние | Описание | Триггеры | Флаги |
|------|-----------|----------|----------|-------|
| Stage1 | opening | Приветствие | `rep.corp.arasaka ≥ 20`, `flag.arasaka.clearanceA == true` | `flag.romance.hanako.stage0` |
| Stage1 | trust-test | Интервью о мотивации | После `opening` | `flag.romance.hanako.stage0` |
| Stage1 | etiquette | Чайная церемония | После `trust-test` | `flag.romance.hanako.ceremony` |
| Stage1 | branch-choice | Решение пути | Завершение сцены | `flag.romance.hanako.choice1` |
| Stage2 | garden-entry | Вход в небесный сад | `flag.romance.hanako.path_*` | `flag.romance.hanako.stage1-complete` |
| Stage2 | memory-bridge | Откровенный обмен воспоминаниями | После `garden-entry` | `flag.romance.hanako.memories` |
| Stage2 | oath-selection | Финальный выбор | После `memory-bridge` | `flag.romance.hanako.choice2` |

- **Проверки:** Stage1 — Persuasion, Willpower, Insight; Stage2 — Etiquette, Strategy, Technical, Willpower.
- **Пасхалки:** Упоминание Олимпиады 1964 и 2020, легенды о синтоистских дронах, сравнение кризиса 2020 с корпоративной войной.

## 3. Структура диалога

### 3.1 Этап 1 — Чайная комната Arasaka Tower

```yaml
nodes:
  - id: opening
    label: «Приветствие»
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Добро пожаловать. Эта встреча — редкость. Надеюсь, вы понимаете её значение."
      - speaker: Player
        options:
          - id: greet-formal
            text: "Честь быть приглашённым"
            response:
              trigger-check: { node: "N-10", stat: "Persuasion", dc: 18, modifiers: [{ source: "item.romance-hanako-tip", value: +2 }] }
              outcomes:
                success: { set-flag: "flag.romance.hanako.stage0", reputation: { romance_hanako: +5 } }
                failure: { effect: "misstep", reputation: { romance_hanako: -3 } }
          - id: greet-direct
            text: "Давайте сразу к сути"
            response:
              speaker: Hanako
              text: "Терпение — достоинство. Пожалуйста, соблюдайте церемонию."
              outcomes: { default: { reputation: { romance_hanako: -5 } } }

  - id: trust-test
    label: «Проверка доверия»
    entry-condition: flag.romance.hanako.stage0 == true
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Почему вы служите Arasaka? Ответ определит наши шаги."
      - speaker: Player
        options:
          - id: answer-duty
            text: "Честь и долг"
            response:
              trigger-check: { node: "N-3", stat: "Willpower", dc: 19, modifiers: [{ source: "rep.corp.arasaka", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.hanako.loyal", reputation: { romance_hanako: +6 } }
                failure: { effect: "doubt", reputation: { romance_hanako: -4 } }
          - id: answer-honest
            text: "Хочу изменить систему изнутри"
            response:
              trigger-check: { node: "N-3", stat: "Insight", dc: 18, modifiers: [{ source: "rep.romance.hanako", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.hanako.truth", reputation: { romance_hanako: +5 } }
                failure: { effect: "misunderstanding", reputation: { romance_hanako: -3 } }

  - id: etiquette
    label: «Чайная церемония»
    entry-condition: flag.romance.hanako.stage0 == true
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Следуйте моим движениям. Это традиция семейства с 2020-х."
      - speaker: Player
        options:
          - id: ceremony-follow
            text: "Повторять точно"
            response:
              trigger-check: { node: "N-5", stat: "Insight", dc: 17, modifiers: [{ source: "flag.viktor.loyal", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.hanako.ceremony", reputation: { romance_hanako: +6 } }
                failure: { effect: "spill", penalty: "minor_shame", reputation: { romance_hanako: -4 } }
          - id: ceremony-improvise
            text: "Добавить нотку 2077"
            response:
              trigger-check: { node: "N-11", stat: "Persuasion", dc: 20 }
              outcomes:
                success: { effect: "impress", reputation: { romance_hanako: +7 } }
                failure: { effect: "offense", reputation: { romance_hanako: -6 } }

  - id: branch-choice
    label: «Решение встречи»
    entry-condition: flag.romance.hanako.stage0 == true
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Какой путь вы видите между Arasaka и нами?"
      - speaker: Player
        options:
          - id: choice-loyal
            text: "Служить без вопросов"
            response:
              condition: { flag.romance.hanako.loyal: true }
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_loyal", reputation: { romance_hanako: +8 } }
          - id: choice-equal
            text: "Партнёрство равных"
            response:
              condition: { flag.romance.hanako.truth: true }
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_equal", reputation: { romance_hanako: +8 } }
          - id: choice-respect
            text: "Разделяем уважение"
            response:
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_respect", reputation: { romance_hanako: +4 } }
```

### 3.2 Этап 2 — Небесный сад Киото-Сити

```yaml
nodes:
  - id: garden-entry
    label: «Небесный сад»
    entry-condition: flag.romance.hanako.path_loyal or flag.romance.hanako.path_equal or flag.romance.hanako.path_respect
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Здесь воссоздан ханами 2054 года. Орбитальный купол видит звёзды и Токио одновременно."
      - speaker: Player
        options:
          - id: pledge-duty
            text: "Я охраню этот сад от войны"
            response:
              trigger-check: { node: "N-14", stat: "Etiquette", dc: 19, modifiers: [{ source: "flag.romance.hanako.path_loyal", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.hanako.stage1-complete", reputation: { romance_hanako: +6 } }
                failure: { effect: "protocol_violation", reputation: { romance_hanako: -4 } }
          - id: recall-reality
            text: "2020-й тоже был хаосом, но люди выстояли"
            response:
              trigger-check: { node: "N-14", stat: "Willpower", dc: 18 }
              outcomes:
                success: { set-flag: "flag.romance.hanako.stage1-complete", reputation: { romance_hanako: +5 } }
                failure: { effect: "dissonance", reputation: { romance_hanako: -3 } }

  - id: memory-bridge
    label: «Мост воспоминаний»
    entry-condition: flag.romance.hanako.stage1-complete == true
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Я потеряла много близких, ещё до Кибервойны. Как вы справляетесь с утратами?"
      - speaker: Player
        options:
          - id: strategy-balance
            text: "Строю планы, чтобы не повторять прошлое"
            response:
              trigger-check: { node: "N-16", stat: "Strategy", dc: 20 }
              outcomes:
                success: { set-flag: "flag.romance.hanako.memories", reputation: { romance_hanako: +7 } }
                failure: { effect: "cold_response", reputation: { romance_hanako: -4 } }
          - id: open-heart
            text: "Позволяю боли быть частью силы"
            response:
              trigger-check: { node: "N-16", stat: "Insight", dc: 18 }
              outcomes:
                success: { set-flag: "flag.romance.hanako.memories", reputation: { romance_hanako: +6 } }
                failure: { effect: "guard_up", reputation: { romance_hanako: -3 } }

  - id: oath-selection
    label: «Клятва под куполом»
    entry-condition: flag.romance.hanako.memories == true
    speaker-order: ["Hanako", "Player"]
    dialogue:
      - speaker: Hanako
        text: "Сад будет свидетелем любой клятвы. Какой путь выберете?"
      - speaker: Player
        options:
          - id: oath-arasaka
            text: "Стану щитом Arasaka и твоим защитником"
            response:
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_guardian", reputation: { romance_hanako: +9 }, reputationBonus: { rep.corp.arasaka: +5 } }
          - id: oath-alliance
            text: "Я выберу равенство и честность"
            response:
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_alliance", reputation: { romance_hanako: +8 } }
          - id: oath-liberation
            text: "Мы найдём путь вне корпоративных цепей"
            response:
              outcomes:
                default: { set-flag: "flag.romance.hanako.path_liberation", reputation: { romance_hanako: +10 }, grant_contract: "arasaka-liberation-pact" }
```

### 3.3 Таблица проверок

| Этап | Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|------|--------------|----|--------------|-------|--------|-------------|--------------|
| Stage1 | opening.greet-formal | Persuasion | 18 | `+2` при `item.romance-hanako-tip` | Флаг stage0, +5 | −3 | Доп. флаг `flag.romance.hanako.grace` | Потеря допуска |
| Stage1 | trust-test.answer-duty | Willpower | 19 | `+1` при `rep.corp.arasaka ≥ 30` | Флаг loyal, +6 | −4 | Доступ к «guardian protocol» | Проверка безопасности |
| Stage1 | trust-test.answer-honest | Insight | 18 | `+1` при `rep.romance.hanako ≥ 8` | Флаг truth | −3 | Пасхалка «Arasaka 1964 oath» | Снижение clearance |
| Stage1 | etiquette.ceremony-follow | Insight | 17 | `+1` при `flag.viktor.loyal` | Флаг ceremony | −4 | Сцена «Tea Harmony» | Репутация −6 |
| Stage1 | etiquette.ceremony-improvise | Persuasion | 20 | — | +7 | −6 | Доступ к меню «Neo Matcha» | Блок на 12 ч |
| Stage2 | garden-entry.pledge-duty | Etiquette | 19 | `+1` при `path_loyal` | Флаг stage1-complete | −4 | Бонус к репутации Arasaka | Разрыв встречи |
| Stage2 | garden-entry.recall-reality | Willpower | 18 | — | Флаг stage1-complete | −3 | Доп. баф «resolve-aura» | Потеря +5 репутации |
| Stage2 | memory-bridge.strategy-balance | Strategy | 20 | `+1` при `rep.romance.hanako ≥ 12` | Флаг memories | −4 | Разблокировка тактики «Kyoto Gambit» | Контрпроверка охраны |
| Stage2 | memory-bridge.open-heart | Insight | 18 | `+1` при `flag.romance.hanako.path_equal` | Флаг memories | −3 | Ветка «Hanami Promise» | Потеря доверия |

### 3.4 Реакции на события

- **`world.event.corporate_war_escalation`:** +1 DC ко всем проверкам Stage2, новая реплика о переговорах с Militech.
- **`world.event.blackwall_breach`:** добавляет скрытую опцию `oath-liberation` с бонусом `lagrange-signal`, но повышает риск провала `strategy-balance`.

## 4. Экспорт (YAML)

```yaml
conversation:
  id: romance-hanako-stage1
  entryNodes: [opening]
  states:
    opening:
      requirements:
        rep.corp.arasaka: ">=20"
        flag.arasaka.clearanceA: true
    trust-test:
      requirements:
        flag.romance.hanako.stage0: true
    etiquette:
      requirements:
        flag.romance.hanako.stage0: true
    branch-choice:
      requirements:
        flag.romance.hanako.stage0: true
  nodes:
    opening:
      options:
        - id: greet-formal
          checks:
            - stat: Persuasion
              dc: 18
              modifiers:
                - source: item.romance-hanako-tip
                  value: 2
          success:
            setFlags: [flag.romance.hanako.stage0]
            reputation:
              romance_hanako: 5
          failure:
            reputation:
              romance_hanako: -3
        - id: greet-direct
          success:
            reputation:
              romance_hanako: -5
    trust-test:
      options:
        - id: answer-duty
          checks:
            - stat: Willpower
              dc: 19
          success:
            setFlags: [flag.romance.hanako.loyal]
            reputation:
              romance_hanako: 6
          failure:
            reputation:
              romance_hanako: -4
        - id: answer-honest
          checks:
            - stat: Insight
              dc: 18
          success:
            setFlags: [flag.romance.hanako.truth]
            reputation:
              romance_hanako: 5
          failure:
            reputation:
              romance_hanako: -3
    etiquette:
      options:
        - id: ceremony-follow
          checks:
            - stat: Insight
              dc: 17
          success:
            setFlags: [flag.romance.hanako.ceremony]
            reputation:
              romance_hanako: 6
          failure:
            penalties: [minor_shame]
            reputation:
              romance_hanako: -4
        - id: ceremony-improvise
          checks:
            - stat: Persuasion
              dc: 20
          success:
            reputation:
              romance_hanako: 7
          failure:
            reputation:
              romance_hanako: -6
    branch-choice:
      options:
        - id: choice-loyal
          conditions:
            - flag.romance.hanako.loyal: true
          success:
            setFlags: [flag.romance.hanako.path_loyal]
            reputation:
              romance_hanako: 8
        - id: choice-equal
          conditions:
            - flag.romance.hanako.truth: true
          success:
            setFlags: [flag.romance.hanako.path_equal]
            reputation:
              romance_hanako: 8
        - id: choice-respect
          success:
            setFlags: [flag.romance.hanako.path_respect]
            reputation:
              romance_hanako: 4
```

```yaml
conversation:
  id: romance-hanako-stage2
  entryNodes: [garden-entry]
  states:
    garden-entry:
      requirements:
        flag.romance.hanako.path_loyal: true
      fallbackRequirements:
        flag.romance.hanako.path_equal: true
      secondaryFallbackRequirements:
        flag.romance.hanako.path_respect: true
    memory-bridge:
      requirements:
        flag.romance.hanako.stage1-complete: true
    oath-selection:
      requirements:
        flag.romance.hanako.memories: true
  nodes:
    garden-entry:
      options:
        - id: pledge-duty
          checks:
            - stat: Etiquette
              dc: 19
          success:
            setFlags: [flag.romance.hanako.stage1-complete]
            reputation:
              romance_hanako: 6
          failure:
            reputation:
              romance_hanako: -4
        - id: recall-reality
          checks:
            - stat: Willpower
              dc: 18
          success:
            setFlags: [flag.romance.hanako.stage1-complete]
            reputation:
              romance_hanako: 5
          failure:
            reputation:
              romance_hanako: -3
    memory-bridge:
      options:
        - id: strategy-balance
          checks:
            - stat: Strategy
              dc: 20
          success:
            setFlags: [flag.romance.hanako.memories]
            reputation:
              romance_hanako: 7
          failure:
            reputation:
              romance_hanako: -4
        - id: open-heart
          checks:
            - stat: Insight
              dc: 18
          success:
            setFlags: [flag.romance.hanako.memories]
            reputation:
              romance_hanako: 6
          failure:
            reputation:
              romance_hanako: -3
    oath-selection:
      options:
        - id: oath-arasaka
          success:
            setFlags: [flag.romance.hanako.path_guardian]
            reputation:
              romance_hanako: 9
            reputationBonus:
              rep.corp.arasaka: 5
        - id: oath-alliance
          success:
            setFlags: [flag.romance.hanako.path_alliance]
            reputation:
              romance_hanako: 8
        - id: oath-liberation
          success:
            setFlags: [flag.romance.hanako.path_liberation]
            grantContract: arasaka-liberation-pact
            reputation:
              romance_hanako: 10
```

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/romance/dialogues/hanako/stage1` | `GET` | Получить чайную сцену |
| `/romance/dialogues/hanako/stage1/run-check` | `POST` | Проверки Persuasion/Willpower/Insight |
| `/romance/dialogues/hanako/stage1/state` | `POST` | Сохранить `flag.romance.hanako.stage0` и ветки |
| `/romance/dialogues/hanako/stage2` | `GET` | Получить сцену небесного сада |
| `/romance/dialogues/hanako/stage2/run-check` | `POST` | Проверки Etiquette/Willpower/Strategy/Insight |
| `/romance/dialogues/hanako/stage2/state` | `POST` | Сохранить флаги `stage1-complete`, `memories`, `path_*`, контракты |
| `/romance/dialogues/hanako/stage3` | `GET` | Получить сцену подземного архива |
| `/romance/dialogues/hanako/stage3/run-check` | `POST` | Проверки Technical/Etiquette/Empathy/Willpower/Hacking/Negotiation |
| `/romance/dialogues/hanako/stage3/state` | `POST` | Сохранить флаги `stage3-unlocked`, `blackwall_*`, `stage3-decision`, выданные награды |
| `/romance/dialogues/hanako/telemetry` | `POST` | Сводная телеметрия по двум этапам |

GraphQL поле `romanceDialogue(id: ID!, stage: Int)` возвращает `RomanceDialogueNode` с `corporateHooks`, `gardenStatus`, `archiveStatus`, `oathSummary`.

## 6. Валидация и телеметрия

- `scripts/validate-romance-flags.ps1` сверяет `flag.romance.hanako.*`, корпоративные флаги, контракты и финальные решения Stage3.
- `scripts/dialogue-simulator.ps1 -Scenario romance-hanako` прогоняет пути `path_loyal`, `path_equal`, `path_respect`, Stage3 ветки `guardian/alliance/liberation`, проверяет выдачу наград и world-state.
- Метрики: `romance-hanako-stage1-success-rate` (цель ≥75%), `romance-hanako-stage2-oath-distribution`, `romance-hanako-stage3-archive-outcome`, `romance-hanako-liberation-uptake`. При провале двух проверок Stage3 подряд запускается миссия `arasaka-loyalty-check`.

## 7. Награды и последствия

- **Репутация:** `rep.romance.hanako` до +48, бонус к `rep.corp.arasaka` при `oath-arasaka` и `vow-guardian`.
- **Контракты/активности:** `arasaka-liberation-pact`, `contract.hanako-shared-future`, `arasaka-archive-leak`, `archivist side-activity` (по флагу stage3-guardian).
- **Предметы/бафы:** `archive.data-shard`, `buff.guardian-aegis`, `program.lagrange-signal`, `contract.hanako-shared-future` (передача) и `lagrange-signal` баф на Blackwall сопротивление.
- **Флаги:** `flag.romance.hanako.stage0`, `flag.romance.hanako.ceremony`, `flag.romance.hanako.path_*`, `flag.romance.hanako.stage1-complete`, `flag.romance.hanako.stage2-complete`, `flag.romance.hanako.stage3-unlocked`, `flag.romance.hanako.blackwall_*`, `flag.romance.hanako.stage3-decision`, `flag.romance.hanako.stage3-{guardian|alliance|liberation}`.

## 8. Связанные материалы

- `../npc-lore/important/hanako-arasaka.md`
- `../dialogues/npc-viktor-vektor.md`
- `../../02-gameplay/social/romance-system.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-2060-2077.md`

## 9. История изменений

- 2025-11-07 21:29 — Версия 1.3.0: добавлен этап 3 (подземный архив), новые проверки, YAML/REST и телеметрия.
- 2025-11-07 19:24 — Добавлен этап 2 (небесный сад), обновлены API и метрики.
- 2025-11-07 18:20 — Подтверждён экспорт и API этапа 1.
- 2025-11-07 17:12 — Создана романтическая сцена Hanako (этап 1).

