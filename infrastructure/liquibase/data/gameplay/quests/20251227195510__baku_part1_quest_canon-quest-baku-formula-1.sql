--liquibase formatted sql

--changeset baku-part1-quests:canon-quest-baku-formula-1 runOnChange:true

INSERT INTO gameplay.quests (
    id,
    metadata_id,
    title,
    english_title,
    type,
    location,
    time_period,
    difficulty,
    estimated_duration,
    player_level_min,
    player_level_max,
    status,
    version,
    quest_definition,
    narrative_context,
    gameplay_mechanics,
    additional_npcs,
    environmental_challenges,
    visual_design,
    cultural_elements,
    metadata_hash,
    content_hash,
    created_at,
    updated_at,
    source_file
) VALUES (
    '4698361e-5c4e-427a-bbae-9d3c75317306',
    'canon-quest-baku-formula-1',
    'Баку 2020-2029 — «Гран-при Азербайджана Формулы 1»',
    'Баку 2020-2029 — «Гран-при Азербайджана Формулы 1»',
    'side',
    'Baku',
    '2020-2029',
    'easy',
    '30-60 минут',
    1,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 1,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "buy_ticket",
      "text": "Купить билет на Гран-при Азербайджана"
    },
    {
      "id": "arrive_at_circuit",
      "text": "Прибыть на Baku City Circuit"
    },
    {
      "id": "watch_qualifying",
      "text": "Посмотреть квалификацию и почувствовать атмосферу"
    },
    {
      "id": "experience_race",
      "text": "Насладиться гонкой и финалом с видом на город"
    }
  ],
  "rewards": {
    "experience": 450,
    "money": {
      "min": 50,
      "max": 200
    },
    "items": [
      "adrenaline_boost",
      "f1_experience",
      "sports_fan_badge"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Баку 2020-2029 — «Гран-при Азербайджана Формулы 1»",
    "body": "Этот side квест \"Баку 2020-2029 — «Гран-при Азербайджана Формулы 1»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Купить билет на Гран-при Азербайджана\n2. Прибыть на Baku City Circuit\n3. Посмотреть квалификацию и почувствовать атмосферу\n4. Насладиться гонкой и финалом с видом на город\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "arrival",
    "title": "Вход и инфраструктура",
    "body": "Гонка проходит на уличной трассе Baku City Circuit с 2016 года. Игрок покупает билет, проходит через зону безопасности\nи поднимается на трибуны, где открывается вид на стены старого города и Flame Towers.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "race",
    "title": "Ход события",
    "body": "- 51 круг по узким улицам и длинному прямому участку (2,2 км).\n- Рев болидов, pit-stop стратегии и интерактивные зоны фанатов.\n- Возможность участвовать в мини-игре прогнозов, влияющей на социальные очки с поклонниками автоспорта.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и эффекты",
    "body": "- 1 500 XP, -200 едди (стоимость билета) и +50 к параметру «Адреналин».\n- Ачивка «Гонщик Баку» и доступ к спортивным мероприятиям региона.\n- Дополнительные сувениры и репутация среди фан-клубов Формулы 1.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '6bbf19c50608397d5083782e6738dc45d521793829c4201a6d80187866bcf510',
    '09a3d68be9a125261aae9b6ffe18289d670ddb63abe4e799e79d60026de0c359',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\baku\2020-2029\quest-006-formula-1-baku.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-baku-formula-1';

