--liquibase formatted sql

--changeset kiev-part1-quests:canon-quest-kiev-motherland-monument runOnChange:true

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
    'd69d338f-0942-493e-8d82-9f2353eb0150',
    'canon-quest-kiev-motherland-monument',
    'Киев 2020-2029 — «Родина-мать»',
    'Киев 2020-2029 — «Родина-мать»',
    'side',
    'Kiev',
    '2020-2029',
    'easy',
    '30-60 минут',
    5,
    NULL,
    'active',
    '1.0.0',
    '{
  "quest_type": "side",
  "level_min": 5,
  "level_max": null,
  "requirements": {
    "required_quests": [],
    "required_flags": [],
    "required_reputation": {},
    "required_items": []
  },
  "objectives": [
    {
      "id": "visit_museum",
      "text": "Посетить музей комплекса 'Родина-мать'"
    },
    {
      "id": "get_permission",
      "text": "Получить разрешение на подъём в статую"
    },
    {
      "id": "climb_inside",
      "text": "Подняться по внутренним конструкциям статуи"
    },
    {
      "id": "observe_panorama",
      "text": "Насладиться панорамой Киева с высоты 91 метр"
    }
  ],
  "rewards": {
    "experience": 400,
    "money": {
      "min": 0,
      "max": 100
    },
    "items": [
      "panoramic_view_achievement",
      "patriotic_buff",
      "history_knowledge"
    ]
  }
}',
    '[
  {
    "id": "overview",
    "title": "Обзор: Киев 2020-2029 — «Родина-мать»",
    "body": "Этот side квест \"Киев 2020-2029 — «Родина-мать»\" предлагает увлекательное исследование и взаимодействие с миром игры.",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "stages",
    "title": "Этапы выполнения",
    "body": "Этапы квеста:\n1. Посетить музей комплекса 'Родина-мать'\n2. Получить разрешение на подъём в статую\n3. Подняться по внутренним конструкциям статуи\n4. Насладиться панорамой Киева с высоты 91 метр\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "museum",
    "title": "Музейный комплекс",
    "body": "Квест начинается в музее истории Второй мировой войны у подножия. Игрок изучает экспозиции, встречает гида и получает доступ\nк экскурсии внутрь статуи.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "ascent",
    "title": "Подъём внутри статуи",
    "body": "Игрок проходит узкими лестницами и техническими шахтами, преодолевая сотни ступеней. Контроль стрессоустойчивости и физической\nформы определяет скорость подъёма.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "observation",
    "title": "Смотровая площадка",
    "body": "На высоте 91 метр открывается панорама Днепра и Киева. Игрок активирует голографический гид по достопримечательностям,\nсравнивает монумент с мировыми аналогами и фиксирует данные в timeline.\n",
    "mechanics_links": [],
    "assets": []
  },
  {
    "id": "rewards",
    "title": "Награды",
    "body": "2 500 XP, +10 к параметру «Патриотизм» и ачивка «На плече Родины-матери». Событие открывает цепочку «Гигантские статуи».\n",
    "mechanics_links": [],
    "assets": []
  }
]',
    '{}',
    '[]',
    '[]',
    '{}',
    '{}',
    'ae922b6e0b3f8d57be0e1932b3639e0a2b15cac03380a6d054ce0fe14a5c8c5d',
    'cab8b5932f69c4a4d56065afa45eacf25d16c5667372ee60cd14a6d5f9572909',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'knowledge\canon\lore\timeline-author\quests\cis\kiev\2020-2029\quest-005-motherland-monument.yaml'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = 'canon-quest-kiev-motherland-monument';

