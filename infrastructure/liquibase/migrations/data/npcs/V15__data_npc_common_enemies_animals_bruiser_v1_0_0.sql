-- Issue: #50
-- Import NPC from: common\enemies\animals-bruiser.yaml
-- Version: 1.0.0
-- Generated: 2025-12-21T02:15:37.827144
-- [WARNING]  WARNING: Requires 'npc_definitions' table (create via Database agent)

BEGIN;

-- NPC: npc-common-enemy-animals-bruiser-001
INSERT INTO narrative.npc_definitions (npc_id, title, content_data, version, is_active)
VALUES ('npc-common-enemy-animals-bruiser-001', 'Бугай Animals (Bruiser)',
        '{"metadata": {"id": "npc-common-enemy-animals-bruiser-001", "title": "Бугай Animals (Bruiser)", "document_type": "canon", "category": "narrative", "status": "draft", "version": "1.0.0", "last_updated": "2025-11-13T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "concept_director", "contact": "concept@necp.game"}], "tags": ["npc", "animals", "enemy"], "topics": ["gang-combat", "close-quarters"], "related_systems": ["narrative-service"], "related_documents": [], "source": "shared/docs/knowledge/canon/narrative/npc-lore/common/enemies/animals-bruiser.md", "visibility": "internal", "audience": ["concept", "narrative"], "risk_level": "medium"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Нужен базовый профиль силового бойца банды Animals для боевых сценариев.", "goal": "Зафиксировать боевые характеристики, слабости и типичную локацию появления.", "essence": "Тяжёлый боец ближнего боя с силовыми имплантами, опасный в открытом столкновении и восприимчивый к электрическому урону.", "key_points": ["Основной стиль — агрессивный ближний бой с нокдаунами.", "Использует импланты силы и прорывает щиты.", "Чувствителен к электрическим эффектам."]}, "content": {"sections": [{"id": "profile", "title": "Профиль", "body": "Представитель банды Animals, натренированный на силовые поединки. Имеет заметные хром-усиления и выступает фронтовой единицей при налётах на склады, клубы и тренировочные полигоны банды. Универсален в рукопашной схватке и способен удерживать агро на себе.\\n", "mechanics_links": [], "assets": []}, {"id": "combat-mechanics", "title": "Боевые механики", "body": "• Сильные нокдауны и залповые удары, пробивающие щиты.\\n• Может разгоняться и ломать укрытия низкого класса.\\n• Уязвим к электричеству и импульсным гранатам, которые сбивают усилители мышц.\\n", "mechanics_links": [], "assets": []}, {"id": "deployment", "title": "Локации", "body": "Чаще всего встречается в промзонах и тренировочных комплексах Animals. Может сопровождать караваны банды или выступать телохранителем у лидеров.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 133, "needs_task": false, "queue_reference": [], "blockers": []}, "history": [{"version": "1.0.0", "date": "2025-11-13", "author": "concept_director", "changes": "Конвертация профиля бугая Animals из MD в YAML."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (npc_id) DO
UPDATE
    SET title = EXCLUDED.title, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 NPC