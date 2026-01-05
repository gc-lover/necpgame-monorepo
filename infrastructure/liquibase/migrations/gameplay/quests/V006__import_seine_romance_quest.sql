--liquibase formatted sql

--changeset backend-agent:V006__import_seine_romance_quest
--comment: Import Seine Romance quest definition

INSERT INTO gameplay.quest_definitions (
    id,
    metadata,
    title,
    description,
    status,
    level_min,
    level_max,
    rewards,
    objectives,
    created_at,
    updated_at
) VALUES (
    'canon-quest-paris-seine-romance',
    '{"id": "canon-quest-paris-seine-romance", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/europe/paris/2020-2029/quest-003-seine-romance.yaml"}',
    'Париж 2020-2029 — Романтика Сены',
    'Квест описывает приватный круиз по Сене во время заката, раскрывая атмосферу «Paris, je t''aime» и ритуалы мостов.',
    'active',
    5,
    50,
    '{"experience": 1000, "money": {"type": "eddies", "value": -200}, "reputation": {"romance": 25}, "unlocks": {"achievements": [{"id": "paris_romantic", "name": "Романтик Парижа"}]}}',
    '[{"id": "rent_boat", "type": "interaction", "description": "Арендовать bateau-mouche для двоих", "required": true}, {"id": "start_cruise", "type": "location", "description": "Стартовать маршрут из центра города", "required": true}, {"id": "pass_bridges", "type": "location", "description": "Проплыть под Pont Neuf, Pont Alexandre III и Pont des Arts", "required": true}, {"id": "see_eiffel", "type": "location", "description": "Встретить вид на Эйфелеву башню с воды", "required": true}, {"id": "kiss_under_bridge", "type": "interaction", "description": "Совериить поцелуй под мостом", "required": true}]',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'canon-quest-paris-seine-romance';
