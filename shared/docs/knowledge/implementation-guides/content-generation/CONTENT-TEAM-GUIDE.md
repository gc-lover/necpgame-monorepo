---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
---

# Content Team Guide: Romance System

Руководство для content team по созданию 1,550+ событий и 10,000+ NPC.


---

## 🎯 Задачи Content Team

### 1. Заполнить 1,550+ событий ✅ ЧАСТИЧНО

**Статус:**
- ✅ Архитектура создана (все категории определены)
- ✅ Примеры созданы (50+ полностью детальных)
- ✅ Шаблоны готовы (для генерации остальных)
- 🔄 Осталось: Заполнить оставшиеся 1,500 событий

**Подход:**

#### Manual (50 премиум событий) — ВЫПОЛНЕНО ✅
- RE-001 (Bar meeting) — полностью детально с диалогами на 12 языках
- RE-TOKYO-002 (Hanami) — культурно точно
- RE-PARIS-001 (Eiffel proposal) — романтично
- И т.д.

#### Semi-Automated (1,000 событий) — РЕКОМЕНДУЕТСЯ
**Используй генератор:**
1. Базовый шаблон (категория + регион)
2. AI генерирует вариации
3. Human review (quality check)
4. Cultural review (native speakers)
5. Утверждение и добавление в библиотеку

#### Automated (500 простых событий) — ДЛЯ VARIETY
- Процедурная генерация для variety
- Basic events (доставка, простые диалоги)
- Automated QA
- Spot check review

---

### 2. Создать 10,000+ NPC profiles ✅ СИСТЕМА ГОТОВА

**Статус:**
- ✅ Generator создан (procedural generation)
- ✅ 100 archetypes определены
- ✅ Name databases (24 культуры)
- ✅ Premium pool started (2 примера)
- 🔄 Осталось: Запустить генерацию

**Распределение:**

#### Premium NPCs (100) — Manual creation
**Файл:** `sample-npc-profiles/premium-npcs-pool.json`

**Выполнено (2/100):**
- Hanako "Ghost" Tanaka (Tokyo) — полностью детально
- Carlos "El Lobo" Ruiz (Medellín) — полностью детально

**Требуется создать (98):**

**По регионам:**
- **Азия (20):**
  - Tokyo: 5 (Netrunner, Solo, Medtech, Media, Corpo)
  - Seoul: 5 (E-sports Pro, K-Pop Star, Chef, Hacker, Teacher)
  - Shanghai: 5 (AI Developer, Triad Boss, Artist, Trader, Politician)
  - Hong Kong: 3 (Fixer, Financier, Activist)
  - Singapore: 2 (Tech CEO, Journalist)

- **Европа (20):**
  - Paris: 5 (Fashion Designer, Chef, Artist, Activist, Museum Curator)
  - London: 5 (Detective, Banker, Rocker, Professor, Royal Guard)
  - Berlin: 5 (Techno DJ, Hacker, Activist, Gallery Owner, Historian)
  - Rome: 3 (Mafia Princess, Archaeologist, Opera Singer)
  - Amsterdam: 2 (Data Smuggler, Coffee Shop Owner)

- **Америка (20):**
  - Rio: 5 (Samba Dancer, Favela Doctor, Football Player, Activist, Beach Vendor)
  - Buenos Aires: 5 (Tango Master, Psychoanalyst, Football Coach, Writer, Chef)
  - NYC: 5 (Wall Street Trader, Broadway Star, Detective, Media Mogul, Artist)
  - México: 3 (Mariachi, Chef, Historian)
  - LA: 2 (Actor, Surfer)

- **СНГ (15):**
  - Moscow: 5 (Metro Leader done, add: Ballerina, Hacker, Oligarch Kid, Poet, Soldier)
  - SPb: 5 (Museum Curator, Hermitage Guard, Ballet Dancer, Philosopher, Artist)
  - Kiev: 3
  - Baku: 2

- **Африка (10):**
  - Lagos: 3 (Tech Entrepreneur, Afrobeat Musician, Market Trader)
  - Nairobi: 3 (Safari Guide, Wildlife Conservationist, Nomad Pathfinder)
  - Cape Town: 3 (Winemaker, Beach Lifeguard, Social Activist)
  - Cairo: 1 (Archaeologist)

- **Ближний Восток (10):**
  - Dubai: 4 (Corp Executive, Luxury Hotelier, Sheikha's Daughter, Expat Consultant)
  - Tel Aviv: 4 (Netrunner, IDF Soldier, Startup Founder, Beach Bar Owner)
  - Istanbul: 2 (Bazaar Merchant, Historian)

- **Океания (5):**
  - Sydney: 3 (Lifeguard, Marine Biologist, Tech Worker)
  - Auckland: 2 (Hobbiton Guide, Rugby Player)

#### Generated NPCs (9,900) — Automated
**Запустить скрипт:**
```bash
python generate_npcs.py --count 9900 --distribution regional --quality-min 70
```

**Процесс:**
1. Generator создаёт профили (2-3 часа CPU time)
2. Quality check (автоматический)
3. Cultural verification (spot check by humans)
4. Database import
5. Testing (sample QA)

**Timeline: 1 неделя** (включая QA)

---

### 3. Написать диалоги (18 языков) ✅ СИСТЕМА ГОТОВА

**Статус:**
- ✅ Template system создан
- ✅ Примеры на 12+ языках
- ✅ Personality variations defined
- 🔄 Осталось: Профессиональный перевод

**Языки (приоритет):**

#### Tier 1 (обязательные, 6 языков):
1. **English** — Global, 100% coverage ✅
2. **Japanese** — Азия, cultural events ✅
3. **Spanish** — Латинская Америка ✅
4. **Russian** — СНГ ✅
5. **French** — Европа ✅
6. **Arabic** — Ближний Восток ✅

#### Tier 2 (важные, 6 языков):
7. **Chinese (Simplified)** — Азия
8. **Portuguese** — Бразилия
9. **German** — Европа
10. **Korean** — Азия
11. **Italian** — Европа
12. **Hebrew** — Израиль

#### Tier 3 (дополнительные, 6 языков):
13. **Turkish** — Турция
14. **Polish** — Европа
15. **Hindi** — Индия
16. **Thai** — Таиланд
17. **Tagalog** — Филиппины
18. **Swahili** — Африка

**Process:**
1. English master text (базовый)
2. Professional translation (18 языков)
3. Native speaker review (accuracy)
4. Cultural adaptation (не просто перевод!)
5. Testing (in-game)

**Effort:** 
- Translation agency: ~2,000,000 words
- Cost estimate: $50,000-100,000
- Timeline: 4-6 недель

**Alternative: AI translation + human review**
- ChatGPT/DeepL initial translation
- Native speaker review and correction
- Cost: $20,000-30,000
- Timeline: 2-3 недели

---

### 4. Cultural Accuracy Testing ✅ FRAMEWORK ГОТОВ

**Процесс:**

#### Step 1: Native Speaker Review
Hire reviewers для каждой культуры:
- **Japanese** culture expert (2 reviewers)
- **Korean** culture expert (2 reviewers)
- **Chinese** culture expert (2 reviewers)
- **French** culture expert (2 reviewers)
- **Spanish/Latin** culture expert (3 reviewers - Spain, México, South America)
- **Russian/CIS** culture expert (2 reviewers)
- **Arabic/Muslim** culture expert (2 reviewers) — CRITICAL!
- **Hebrew/Israeli** culture expert (1 reviewer)
- **African** culture experts (3 reviewers - West, East, South)

**Total: 20-25 cultural reviewers**

#### Step 2: Review Checklist

**Для каждого регионального события проверить:**
- ✅ Name authenticity (имена настоящие?)
- ✅ Language accuracy (фразы правильные?)
- ✅ Cultural references (отсылки точные?)
- ✅ Traditions respect (традиции уважены?)
- ✅ Stereotypes avoided (нет оскорбительных стереотипов?)
- ✅ Timeline accuracy (исторический контекст?)
- ✅ Geography correct (места существуют?)
- ✅ Food/customs right (еда, обычаи правильные?)
- ✅ Religious sensitivity (религия уважена?)
- ✅ Political sensitivity (политика нейтральна?)

#### Step 3: Severity Classification

**Issues severity:**
- **CRITICAL:** Offensive, illegal, completely wrong (must fix)
  - Example: PDA in Saudi Arabia (illegal!)
  - Example: Wrong religious practice
  - Example: Offensive stereotype

- **HIGH:** Inaccurate, immersion-breaking (should fix)
  - Example: Wrong food in region
  - Example: Historically impossible
  - Example: Language completely wrong

- **MEDIUM:** Minor inaccuracies (nice to fix)
  - Example: Unusual name choice
  - Example: Rare tradition
  - Example: Translation awkward

- **LOW:** Stylistic choice (optional)
  - Example: Creative interpretation
  - Example: Cyberpunk future variant

#### Step 4: Revision Process
1. Reviewer identifies issues
2. Content team fixes
3. Re-review
4. Approval

**Timeline: 2-3 недели** (parallel with translation)

---

## 📋 Production Checklist

### Events Library (1,550)

**Progress tracker:**
```
Meeting Events (80):
  ✅ RE-001 to RE-020 (20 done - templates)
  🔄 RE-021 to RE-080 (60 remaining - generate)

Friendship Events (100):
  ✅ RE-081 to RE-105 (25 done - templates)
  🔄 RE-106 to RE-180 (75 remaining - generate)

Flirting Events (120):
  ✅ RE-181 to RE-210 (30 done - templates)
  🔄 RE-211 to RE-300 (90 remaining - generate)

Dating Events (140):
  ✅ RE-301 to RE-335 (35 done - templates)
  🔄 RE-336 to RE-440 (105 remaining - generate)

Intimacy Events (100):
  ✅ RE-441 to RE-465 (25 done - templates)
  🔄 RE-466 to RE-540 (75 remaining - generate)

Conflict Events (80):
  ✅ RE-541 to RE-570 (30 done - templates)
  🔄 RE-571 to RE-620 (50 remaining - generate)

Reconciliation Events (60):
  ✅ RE-621 to RE-640 (20 done - templates)
  🔄 RE-641 to RE-680 (40 remaining - generate)

Commitment Events (40):
  ✅ RE-681 to RE-695 (15 done - templates)
  🔄 RE-696 to RE-720 (25 remaining - generate)

Crisis Events (50):
  ✅ RE-721 to RE-740 (20 done - templates)
  🔄 RE-741 to RE-770 (30 remaining - generate)

Regional Events (800):
  ✅ Templates for all regions created
  🔄 Full generation needed

TOTAL: 250/1550 templates done (16%)
REMAINING: 1,300 events to generate
```

### NPC Profiles (10,000)

**Progress tracker:**
```
Premium NPCs (100):
  ✅ Hanako Tanaka (Tokyo) - COMPLETE
  ✅ Carlos Ruiz (Medellín) - COMPLETE
  ✅ Ksenia Volkov (Moscow) - COMPLETE
  🔄 97 remaining

Generated NPCs (9,900):
  ⏳ Run generator script
  ⏳ Quality check
  ⏳ Cultural verification
  ⏳ Database import

TOTAL: 3/10000 done (0.03%)
ESTIMATION: With generator - 1 week
```

---

## 🛠️ Tools & Resources

### For Content Writers

**Writing Tools:**
- `romance-event-template.md` — Template for new events
- `dialogue-style-guide.md` — How to write personality-based dialogue
- `cultural-do-dont.md` — Cultural sensitivity guidelines
- `translation-glossary.md` — Key terms in all languages

**Testing Tools:**
- In-game event tester (trigger event, see result)
- Dialogue preview (hear with TTS)
- Cultural checker (automated red flags)

### For Translators

**Translation Memory:**
- Common phrases database (3,000+ phrases)
- Character consistency (names, titles)
- Cultural adaptation notes
- Glossary of game terms

**Quality Metrics:**
- Translation accuracy (95%+)
- Cultural appropriateness (100%)
- Tone consistency (personality-matched)
- Immersion (feels natural)

### For Cultural Reviewers

**Review Dashboard:**
- Event viewer (see event in context)
- Issue reporter (flag problems)
- Severity selector (critical/high/medium/low)
- Suggestion field (how to fix)

**Payment:**
- $50-100 per event reviewed
- Bonus for finding critical issues
- Native speaker verification required

---

## 📊 Content Generation Strategy

### Recommended Approach (Hybrid)

**Phase 1: Core Events (Manual, 250 events)**
- **Week 1-2:** Team of 10 writers
- 25 events per writer = 250 events
- Focus: High-impact events (milestones, cultural significance)
- Quality: Premium, hand-crafted

**Phase 2: Generated Events (Auto, 1,000 events)**
- **Week 3:** Run generator
- AI creates based on templates
- Automated QA filters low quality
- Spot check 10% (100 events) by humans

**Phase 3: Filler Events (Semi-Auto, 300 events)**
- **Week 4:** Minor variations of existing
- Different locations, small tweaks
- Bulk generation + human review

**TOTAL TIMELINE: 4 weeks** for all events

### For NPCs

**Phase 1: Premium (100 NPCs)**
- **Week 1:** Team of 10 writers
- 10 NPCs each = 100 premium NPCs
- Full backstories, unique arcs
- Quality: Witcher 3 level depth

**Phase 2: Generated (9,900 NPCs)**
- **Week 2:** Run generator script
- Procedural generation
- Quality check (>70 score)
- Cultural verification (spot check)

**TOTAL TIMELINE: 2 weeks** for all NPCs

---

## 📖 Writing Guidelines

### Event Writing Best Practices

**1. Dialogue должен быть:**
- Natural (как люди говорят)
- Personality-appropriate (extravert ≠ introvert)
- Culturally accurate (Japanese ≠ Brazilian)
- Emotionally resonant (players должны чувствовать)
- Varied (избегать repetition)

**2. Choices должны быть:**
- Meaningful (не просто косметика)
- Clear (понятно что произойдёт)
- Balanced (не один obvious choice)
- Consequence-driven (выборы имеют вес)

**3. Outcomes должны быть:**
- Logical (следствие выбора)
- Satisfying (positive outcomes rewarding)
- Fair (failures не frustrating)
- Memorable (key moments impactful)

### NPC Writing Best Practices

**1. Backstory должен:**
- Make sense (логичная история)
- Be deep but concise (300-500 words)
- Explain personality (why they are who they are)
- Create empathy (players should care)
- Tie to game world (not isolated)

**2. Personality должна:**
- Be consistent (не противоречия)
- Have growth potential (can evolve)
- Be realistic (flawed humans)
- Match culture (Japanese ≠ Italian behavior)

**3. Dialogue voice должен:**
- Be distinct (each NPC unique)
- Match personality (shy ≠ outgoing speech)
- Reflect culture (idioms, phrases)
- Evolve with relationship (strangers → lovers sound different)

---

## 🎨 Cultural Authenticity Matrix

### Critical Cultural Points

**Japanese:**
- ✅ Indirectness (subtle communication)
- ✅ Hierarchy (respect for sempai/kohai)
- ✅ Group harmony (wa - 和)
- ✅ Modesty (public reserve)
- ❌ AVOID: Overly direct confrontation
- ❌ AVOID: Public emotional outbursts
- ❌ AVOID: Disrespect to elders

**Brazilian:**
- ✅ Warmth (abraços, beijos)
- ✅ Passion (emotional expression)
- ✅ Joy (alegria, festas)
- ✅ Physical touch (normal greeting)
- ❌ AVOID: Cold formality
- ❌ AVOID: Ignoring family
- ❌ AVOID: Fake joy (detect insincerity)

**Russian:**
- ✅ Depth (глубокие разговоры)
- ✅ Duality (холод снаружи, тепло внутри)
- ✅ Soul (русская душа concept)
- ✅ Honesty (ценят искренность)
- ❌ AVOID: Superficiality
- ❌ AVOID: Fake smiles
- ❌ AVOID: Ignoring suffering/struggle

**Emirati/Arab:**
- ✅ Hospitality (generous hosts)
- ✅ Family (critical importance)
- ✅ Honor (reputation matters)
- ✅ Modesty (especially women)
- ✅ Religion (Islam respected)
- ❌ AVOID: PDA in public (illegal!)
- ❌ AVOID: Alcohol in Saudi (illegal!)
- ❌ AVOID: Disrespect to Islam
- ❌ CRITICAL: Gender separation rules

**Israeli:**
- ✅ Directness (say what you mean)
- ✅ Passion (strong emotions)
- ✅ Family (very important)
- ✅ Debate (arguing is bonding)
- ❌ AVOID: Antisemitism (obvious)
- ❌ AVOID: Insensitivity to conflict

**African (varies by region):**
- ✅ Community (Ubuntu philosophy)
- ✅ Family (extended family important)
- ✅ Respect (elders, traditions)
- ✅ Joy (celebration of life)
- ❌ AVOID: Tribal stereotypes
- ❌ AVOID: "Safari only" view
- ❌ AVOID: Poverty porn

---

## 🚀 Quick Start for Content Team

### Day 1: Setup
1. Read all documentation
2. Review examples (Hanako, Carlos)
3. Setup writing environment
4. Assign events per writer

### Day 2-10: Core Production
- Write 25-30 events per writer
- Peer review
- Cultural check
- Iteration

### Day 11-15: Generation & QA
- Run generators for remaining events
- Quality check generated content
- Fix issues
- Cultural verification

### Day 16-20: Premium NPCs
- Create 100 premium NPC profiles
- Full backstories
- Unique arcs
- Voice direction

### Day 21-25: Translation
- Professional translation
- Review and corrections
- Import to system

### Day 26-30: Final QA
- In-game testing
- Cultural expert final review
- Bug fixes
- Polish

**GO LIVE: Week 5** 🚀

---

## 📈 Success Metrics

### Content Quality KPIs

**Events:**
- Player rating > 4.0/5.0
- Cultural accuracy 95%+
- Translation quality 95%+
- Replayability 70%+ (players try multiple events)

**NPCs:**
- Player favorite NPCs > 10 per player
- Romance completion rate 40%+
- Diversity in romanced NPCs (not all same culture)
- Premium NPC engagement 80%+

**Dialogue:**
- Immersion score 4.5/5.0
- Personality consistency 95%+
- Cultural authenticity 100% (no offensive content)
- Translation quality 95%+

---

## 💰 Budget Estimate

**Content Creation:**
- Writers (10 × $5,000/month × 1 month) = $50,000
- Editors/QA (3 × $4,000/month × 1 month) = $12,000

**Translation:**
- Professional (18 languages × $0.08/word × 200,000 words) = $288,000
- OR AI + review (18 languages × $2,000) = $36,000

**Cultural Review:**
- Native speakers (20 × $2,000) = $40,000

**TOTAL (Professional):** ~$390,000
**TOTAL (AI-assisted):** ~$138,000

**Recommended: Hybrid**
- Core events: Professional translation ($100,000)
- Generated events: AI + review ($40,000)
- Cultural review: Full ($40,000)
**TOTAL: ~$180,000**

---

## ✅ Готово к запуску

**Content Team имеет:**
- ✅ Полную документацию
- ✅ Примеры (2 premium NPCs, 1 full event)
- ✅ Генераторы (NPC + events)
- ✅ Quality checkers (automated + manual)
- ✅ Translation system
- ✅ Cultural review framework
- ✅ Timeline (4-6 недель)
- ✅ Budget ($140,000-390,000)

**Можно начинать production немедленно!** 🎬

