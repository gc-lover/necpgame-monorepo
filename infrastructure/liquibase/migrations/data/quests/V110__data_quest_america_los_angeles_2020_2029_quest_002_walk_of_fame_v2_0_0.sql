-- Issue: #140890241
-- Import quest from: america\\los-angeles\\2020-2029\\quest-002-walk-of-fame.yaml
-- Generated: 2025-12-23T22:55:00.000000

BEGIN;

-- Quest: canon-quest-los-angeles-walk-of-fame-2020-2029
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-los-angeles-walk-of-fame-2020-2029', 'Лос-Анджелес: Голливудская Аллея Славы', 'Игрок проходит путь от неизвестного артиста к обладателю собственной звезды на Hollywood Walk of Fame, демонстрируя цену славы и бюрократию шоу-бизнеса.',
        'side', 8, NULL,
        '{\"required_quests\": [], \"required_flags\": [], \"required_reputation\": {\"hollywood_scene\": 100}, \"required_items\": []}'::jsonb,
        '[{\"id\": \"achieve_fame\", \"text\": \"Достичь славы и признания в индустрии\", \"type\": \"interact\", \"target\": \"fame_achievement\", \"count\": 1, \"optional\": false}, {\"id\": \"pay_fee\", \"text\": \"Оплатить взнос в $50 000 за звезду\", \"type\": \"interact\", \"target\": \"walk_of_fame_fee\", \"count\": 1, \"optional\": false}, {\"id\": \"run_pr_campaign\", \"text\": \"Провести PR-кампанию и собрать подписи знаменитостей\", \"type\": \"interact\", \"target\": \"pr_campaign\", \"count\": 1, \"optional\": false}, {\"id\": \"attend_ceremony\", \"text\": \"Присутствовать на церемонии открытия звезды\", \"type\": \"interact\", \"target\": \"star_ceremony\", \"count\": 1, \"optional\": false}]'::jsonb,
        '{\"xp\": 2200, \"currency\": 0, \"attributes\": {\"charisma\": 20}, \"achievements\": [{\"id\": \"walk_of_fame_star\", \"name\": \"Звезда Аллеи Славы\"}], \"reputation\": {\"hollywood_scene\": 50}, \"items\": []}'::jsonb,
        '[{\"condition\": \"Успешная церемония\", \"outcome\": \"VIP-доступ к голливудским событиям\", \"next_quests\": []}, {\"condition\": \"Недостаточная репутация\", \"outcome\": \"Отказ в заявке на звезду\", \"next_quests\": []}]'::jsonb,
        '{\"sections\": [{\"id\": \"overview\", \"title\": \"Описание\", \"body\": \"Игрок проходит путь от неизвестного артиста к обладателю собственной звезды на Hollywood Walk of Fame.\"}, {\"id\": \"fame_cost\", \"title\": \"Цена славы\", \"body\": \"Звезда на Walk of Fame стоит $50,000 и требует одобрения Hollywood Chamber of Commerce.\"}, {\"id\": \"ceremony\", \"title\": \"Церемония\", \"body\": \"Открытие звезды сопровождается речью мэра, выступлениями знаменитостей и вниманием папарацци.\"}]}'::jsonb,
        '2.0.0', true);

COMMIT;
