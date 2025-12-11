-- Issue: #50, #34
-- Import quest from: europe/amsterdam/2020-2029/quest-007-stroopwafel-quest.yaml
-- Version: 1.0.0
-- Generated: 2025-12-12T00:49:14.696898

BEGIN;

-- Quest: quest-amsterdam-2029-stroopwafel-quest
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements, objectives, rewards, branches, content_data, version, is_active) VALUES (
'quest-amsterdam-2029-stroopwafel-quest', 
'Амстердам 2020-2029 — «Квест строопвафлей»', 
'Игрок посещает рынок, учится готовить вафлю и получает бонусы за дегустацию.', 
'side', 
NULL, 
NULL, 
'{}'::jsonb, 
'[]'::jsonb, 
'{}'::jsonb, 
'[]'::jsonb, 
'{"metadata": {"id": "quest-amsterdam-2029-stroopwafel-quest", "title": "Амстердам 2020-2029 — «Квест строопвафлей»", "document_type": "canon", "category": "timeline-quest", "status": "draft", "version": "1.0.0", "last_updated": "2025-11-13T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "concept_director", "contact": "concept@necp.game"}], "tags": ["amsterdam", "cuisine", "dessert"], "topics": ["culinary-tour", "local-traditions"], "related_systems": ["narrative-service", "gameplay-service", "quest-service", "character-service", "economy-service", "wellbeing-service", "social-service"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-007-stroopwafel-quest.md", "visibility": "internal", "audience": ["concept", "narrative", "liveops"], "risk_level": "low"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Кулинарной ветке Амстердама требовался лёгкий десертный квест с узнаваемым продуктом.", "goal": "Познакомить игроков с традицией свежих строопвафлей и правильной подачей к горячему напитку.", "essence": "Игрок посещает рынок, учится готовить вафлю и получает бонусы за дегустацию.", "key_points": ["Подчёркивает отличие свежего и фабричного продукта.", "Включает мини-игру на идеальную карамельную начинку.", "Дополнительно открывает рецепты для кофейных сетов."]}, "content": {"sections": [{"id": "market", "title": "Рынок Albert Cuyp", "body": "Игрок прибывает на рынок, знакомится с мастером и получает задания по выбору ингредиентов.\\n", "mechanics_links": ["mechanics/economy/market-interaction.yaml"], "assets": []}, {"id": "preparation", "title": "Готовим строопвафель", "body": "Мини-игра проверяет температуру пресса и густоту сиропа. Нужно удерживать индикаторы в зелёной зоне.\\n", "mechanics_links": ["mechanics/cooking/temperature-control.yaml"], "assets": []}, {"id": "ritual", "title": "Подача с кофе", "body": "Игрок укладывает вафлю на чашку, ждёт, пока карамель прогреется, и выбирает идеальную подачу.\\n", "mechanics_links": ["mechanics/wellbeing/snack-buff.yaml"], "assets": []}, {"id": "souvenirs", "title": "Покупка для друзей", "body": "Можно упаковать вафли, добавить открытку и отправить их NPC-ally, чтобы улучшить отношения.\\n", "mechanics_links": ["mechanics/social/gift-delivery.yaml"], "assets": []}, {"id": "rewards", "title": "Награды", "body": "- 500 XP, расходы 5 еддис.\\n- Бафф «Сладкое топливо» (+10% Energy на 2 часа), ачивка «Строопвафельный гурман».\\n- Разблокирован рецепт «Сиропные вафли» для игроковых кофеен.\\n", "mechanics_links": ["mechanics/progression/recipe-unlock.yaml"], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 749, "needs_task": false, "queue_reference": [], "blockers": []}, "history": [{"version": "1.0.0", "date": "2025-11-13", "author": "concept_team", "changes": "Квест «Квест строопвафлей» адаптирован под шаблон Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb, 
1, 
true
) ON CONFLICT (quest_id) DO UPDATE SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest
