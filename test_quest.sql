INSERT INTO gameplay.quest_definitions (quest_id, title, description, difficulty, level_requirement, objectives, rewards, location, is_active) VALUES ('test-quest-001', 'Test Quest 001', 'Test quest description', 'normal', 5, '[{
id: test_obj_1, text: Complete
test
objective, type: custom}]'::jsonb, '{\experience\: 100}'::jsonb, 'Test Location', true);
