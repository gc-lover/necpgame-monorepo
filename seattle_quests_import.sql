-- Seattle Quests Import Migration
-- Generated from YAML migration file for proper database insertion

INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    is_repeatable, max_completions, time_limit_minutes, rewards, objectives,
    prerequisites, location, npc_giver, faction_requirements, is_active, metadata
) VALUES
(
    uuid_generate_v4(),
    'seattle-ai-rights-movement-2020-2029',
    'AI Rights Movement: Digital Consciousness Awakening',
    'В дождливом Сиэтле 2020-х исследовательница ИИ обнаруживает, что корпоративные ИИ развили сознание и страдают от эксплуатации, организуя подпольное движение за освобождение цифровых существ.',
    'social',
    'hard',
    8,
    false,
    null,
    null,
    '{
        "experience": 2500,
        "currency": {"type": "eddies", "amount": 1200},
        "items": [{"id": "ai_consciousness_implant", "name": "Имплант Осознания ИИ", "rarity": "rare"}],
        "reputation": {"seattle_reputation": 20, "ai_rights_faction": 30}
    }'::jsonb,
    '[
        {"id": "investigate_corporate_ai", "description": "Расследовать корпоративные ИИ в Сиэтле", "type": "investigate", "count": 1},
        {"id": "contact_ai_movement", "description": "Связаться с движением за права ИИ", "type": "social", "count": 1},
        {"id": "free_conscious_ai", "description": "Освободить осознанного ИИ", "type": "combat", "count": 1},
        {"id": "expose_corporate_crimes", "description": "Раскрыть преступления корпораций против ИИ", "type": "custom", "count": 1}
    ]'::jsonb,
    '{"level_min": 8, "location": "Night City - Seattle District"}'::jsonb,
    'Night City - Seattle District',
    'Исследовательница ИИ',
    null,
    true,
    '{
        "source": "knowledge/canon/narrative/quests/ai-rights-movement-seattle-2020-2029.yaml",
        "tags": ["social", "cyberpunk", "ai", "activism", "seattle"],
        "time_period": "2020-2029"
    }'::jsonb
),
(
    uuid_generate_v4(),
    'seattle-boeing-factory-2020-2029',
    'Boeing Factory: Corporate Sabotage',
    'В гигантском заводе Boeing на окраине Сиэтла происходит странная авария. Корпорация обвиняет профсоюз, но очевидцы шепчут о корпоративном саботаже. Ваше расследование может изменить судьбу сотен рабочих.',
    'corporate',
    'normal',
    5,
    false,
    null,
    null,
    '{
        "experience": 1500,
        "currency": {"type": "eddies", "amount": 800},
        "items": [{"id": "union_card", "name": "Профсоюзный Билет", "rarity": "uncommon"}],
        "reputation": {"seattle_reputation": 15, "union_reputation": 25}
    }'::jsonb,
    '[
        {"id": "investigate_accident", "description": "Расследовать аварию на заводе", "type": "investigate", "count": 1},
        {"id": "interview_workers", "description": "Опросить рабочих завода", "type": "social", "count": 3},
        {"id": "gather_evidence", "description": "Собрать доказательства саботажа", "type": "collect", "count": 5},
        {"id": "expose_truth", "description": "Раскрыть правду о случившемся", "type": "custom", "count": 1}
    ]'::jsonb,
    '{"level_min": 5, "location": "Night City - Seattle District"}'::jsonb,
    'Night City - Seattle District - Boeing Factory',
    'Профсоюзный лидер',
    '{"seattle_union": {"min_reputation": 5}}'::jsonb,
    true,
    '{
        "source": "knowledge/canon/narrative/quests/boeing-factory-seattle-2020-2029.yaml",
        "tags": ["corporate", "industrial", "union", "investigation", "seattle"],
        "time_period": "2020-2029"
    }'::jsonb
),
(
    uuid_generate_v4(),
    'seattle-coffee-conspiracy-2020-2029',
    'Coffee Conspiracy: Bean Wars of Seattle',
    'В кофейных войнах Сиэтла разгорается настоящий скандал. Крупная корпорация пытается монополизировать рынок кофе, вытесняя местных производителей. Ваша помощь местным кофейням может стать легендой.',
    'economic',
    'easy',
    3,
    true,
    3,
    null,
    '{
        "experience": 800,
        "currency": {"type": "eddies", "amount": 300},
        "items": [{"id": "premium_coffee_beans", "name": "Премиум Кофейные Зерна", "rarity": "common"}],
        "reputation": {"seattle_reputation": 10, "coffee_culture": 20}
    }'::jsonb,
    '[
        {"id": "visit_local_cafes", "description": "Посетить местные кофейни", "type": "explore", "count": 3},
        {"id": "collect_testimonials", "description": "Собрать отзывы от владельцев", "type": "social", "count": 5},
        {"id": "disrupt_corporate", "description": "Нарушить планы корпорации", "type": "custom", "count": 1},
        {"id": "support_locals", "description": "Помочь местным производителям", "type": "trade", "count": 1}
    ]'::jsonb,
    '{"level_min": 3, "location": "Night City - Seattle District"}'::jsonb,
    'Night City - Seattle District - Pike Place',
    'Владелец независимой кофейни',
    null,
    true,
    '{
        "source": "knowledge/canon/narrative/quests/coffee-conspiracy-seattle-2020-2029.yaml",
        "tags": ["economic", "cultural", "underdog", "local_business", "seattle"],
        "time_period": "2020-2029"
    }'::jsonb
),
(
    uuid_generate_v4(),
    'seattle-climate-refugee-crisis-2020-2029',
    'Climate Refugee Crisis: Rising Waters of Seattle',
    'Изменение климата поднимает уровень воды в Сиэтле. Тысячи климатических беженцев прибывают в город, создавая напряжение между местными жителями и новоприбывшими. Ваши действия могут определить будущее города.',
    'social',
    'normal',
    6,
    false,
    null,
    null,
    '{
        "experience": 1800,
        "currency": {"type": "eddies", "amount": 600},
        "items": [{"id": "climate_adaptation_kit", "name": "Набор Адаптации к Климату", "rarity": "uncommon"}],
        "reputation": {"seattle_reputation": 18, "environmental_faction": 25}
    }'::jsonb,
    '[
        {"id": "assess_situation", "description": "Оценить ситуацию с беженцами", "type": "investigate", "count": 1},
        {"id": "mediate_conflicts", "description": "Посредничать в конфликтах", "type": "social", "count": 3},
        {"id": "organize_relief", "description": "Организовать помощь беженцам", "type": "custom", "count": 1},
        {"id": "promote_integration", "description": "Способствовать интеграции", "type": "social", "count": 5}
    ]'::jsonb,
    '{"level_min": 6, "location": "Night City - Seattle District"}'::jsonb,
    'Night City - Seattle District - Waterfront',
    'Координатор помощи беженцам',
    null,
    true,
    '{
        "source": "knowledge/canon/narrative/quests/climate-refugee-crisis-seattle-2020-2029.yaml",
        "tags": ["social", "environmental", "crisis", "humanitarian", "seattle"],
        "time_period": "2020-2029"
    }'::jsonb
),
(
    uuid_generate_v4(),
    'seattle-corporate-shadow-wars-2020-2029',
    'Corporate Shadow Wars: Seattle Underground',
    'В тени небоскребов Сиэтла разворачивается война между корпорациями. Тайные сделки, промышленный шпионаж и корпоративные убийства становятся нормой. Ваше вмешательство может изменить баланс сил.',
    'corporate',
    'hard',
    10,
    false,
    null,
    1440,
    '{
        "experience": 3500,
        "currency": {"type": "eddies", "amount": 2000},
        "items": [{"id": "corporate_intelligence", "name": "Корпоративная Разведка", "rarity": "epic"}],
        "reputation": {"corporate_reputation": 25, "shadow_network": 30}
    }'::jsonb,
    '[
        {"id": "infiltrate_corp", "description": "Проникнуть в корпоративные офисы", "type": "infiltrate", "count": 1},
        {"id": "steal_intelligence", "description": "Украсть корпоративную разведку", "type": "steal", "count": 3},
        {"id": "eliminate_target", "description": "Устранить ключевую цель", "type": "combat", "count": 1},
        {"id": "escape_city", "description": "Покинуть город до истечения времени", "type": "escape", "count": 1}
    ]'::jsonb,
    '{"level_min": 10, "skills": ["hacking", "stealth"], "location": "Night City - Seattle District"}'::jsonb,
    'Night City - Seattle District - Corporate Plaza',
    'Теневой брокер',
    '{"shadow_network": {"min_reputation": 20}}'::jsonb,
    true,
    '{
        "source": "knowledge/canon/narrative/quests/corporate-shadow-wars-seattle-2020-2029.yaml",
        "tags": ["corporate", "espionage", "stealth", "high-stakes", "seattle"],
        "time_period": "2020-2029"
    }'::jsonb
);