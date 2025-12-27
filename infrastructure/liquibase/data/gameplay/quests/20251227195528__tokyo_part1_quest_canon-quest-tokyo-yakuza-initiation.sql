--liquibase formatted sql

--changeset tokyo-part1-quests:canon-quest-tokyo-yakuza-initiation runOnChange:true

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
    '83f5d912-5c79-412d-9b41-969e884aa1ab',
    'canon-quest-tokyo-yakuza-initiation',
    'Токио 2020-2029 — Инициация в Якудза',
    'Токио 2020-2029 — Инициация в Якудза',
    'faction',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    15,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "faction",
  "level_min": 15,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "prove_loyalty",
      "text": "Доказать лояльность якудза через испытания"
    },
    {
      "id": "collect_debt",
      "text": "Взыскать долг у должника (дипломатически или силой)"
    },
    {
      "id": "undergo_ceremony",
      "text": "Пройти церемонию yubitsume и получить татуировку"
    },
    {
      "id": "swear_oath",
      "text": "Принести клятву верности клану якудза"
    }
  ],
  "rewards": {
    "experience": 3000,
    "money": {
      "min": 2000,
      "max": 5000
    },
    "items": [
      "yakuza_membership",
      "faction_protection",
      "trading_discounts"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Токио 2020-2029 — Инициация в Якудза",
    "body": "Этот faction квест \"Токио 2020-2029 — Инициация в Якудза\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Доказать лояльность якудза через испытания\n2. Взыскать долг у должника (дипломатически или силой)\n3. Пройти церемонию yubitsume и получить татуировку\n4. Принести клятву верности клану якудза\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_overview",
    "title": "Описание",
    "body": "Клан якудза модернизировал вековые традиции под киберпанк-реалии: цифровые татами-голограммы, биометрический контроль гостей и смешение ритуалов с нейросетевыми клятвами.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "quest_flow",
    "title": "Этапы",
    "body": "1. Познакомиться с вакагасира в ночном кабаке и получить приглашение на испытание.\n2. Выбрать подход к должнику: переговоры с упором на долг, запугивание или быстрый рейд.\n3. Вернуться в офис клана, пройти церемонию yubitsume или предложить равноценную кибер-жертву.\n4. Пережить процесс нанесения полноформатной кибер-татуировки и вступительной клятвы перед кумитё.\n5. Получить клановые протоколы и доступ к закрытой сети заказов.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "regional_details",
    "title": "Региональные детали",
    "body": "Интерьер сочетает татами, токонома с семейными реликвиями и скрытую технику: стены проецируют хронику клана, охрана использует смарт-кимоно и биометрические сенсоры.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды и последствия",
    "body": "Игрок получает 2 500 XP, 5 000 едди и уникальную татуировку, которая активирует ветку из 20+ квестов. Репутация с якудза растёт, полиция и конкуренты становятся враждебнее, выход из фракции недоступен.\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    '25a15110ce1f1ec8b045aa61fa19d9ced4ccf3e4da6b943040a85770784b3660',
    'b31ae4a45fe8b594ce49a78eb66b4093e358ff6959e1470fd9cf19731b8f5eea',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\asia\tokyo\2020-2029\quest-002-yakuza-initiation.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-tokyo-yakuza-initiation';

