-- Import Miami, Detroit, Mexico City quests to database
-- Issue: #51

INSERT INTO gameplay.quest_definitions (id, title, document_type, category, status, version, tags, topics, yaml_content,
                                        created_at, updated_at)
VALUES ('quest-miami-south-beach-neon', 'South Beach Neon Nights', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['miami', 'neon', 'beach', '2020-2029'], ARRAY['cyberpunk', 'nightlife', 'tourism'],
        '{"metadata":{"id":"quest-miami-south-beach-neon","title":"South Beach Neon Nights","description":"Experience the vibrant neon-lit nightlife of South Beach","quest_type":"side","level_min":1,"level_max":20},"objectives":[{"id":"explore-beach","description":"Explore the neon-lit beachfront","type":"explore","target":"South Beach District"}],"rewards":{"experience":500,"currency":200,"items":["neon_beach_souvenir"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('quest-miami-cuban-havana', 'Cuban Heritage in Little Havana', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['miami', 'cuban', 'havana', '2020-2029'], ARRAY['culture', 'heritage', 'food'],
        '{"metadata":{"id":"quest-miami-cuban-havana","title":"Cuban Heritage in Little Havana","description":"Discover the rich Cuban culture in Little Havana","quest_type":"cultural","level_min":1,"level_max":15},"objectives":[{"id":"visit-museum","description":"Visit the Cuban Memorial Museum","type":"visit","target":"Cuban Memorial Museum"}],"rewards":{"experience":300,"currency":150,"items":["cuban_cigar"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('quest-detroit-motor-city', 'Motor City Revival', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['detroit', 'motor', 'city', '2020-2029'], ARRAY['technology', 'revival', 'automotive'],
        '{"metadata":{"id":"quest-detroit-motor-city","title":"Motor City Revival","description":"Witness the technological revival of Detroit","quest_type":"main","level_min":5,"level_max":25},"objectives":[{"id":"visit-innovation-district","description":"Explore the Innovation District","type":"explore","target":"Detroit Innovation District"}],"rewards":{"experience":800,"currency":400,"items":["automotive_chip"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('quest-detroit-motown-music', 'Motown Music Legacy', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['detroit', 'motown', 'music', '2020-2029'], ARRAY['music', 'legacy', 'culture'],
        '{"metadata":{"id":"quest-detroit-motown-music","title":"Motown Music Legacy","description":"Explore the birthplace of Motown music","quest_type":"cultural","level_min":3,"level_max":18},"objectives":[{"id":"visit-hitsville","description":"Visit Hitsville USA recording studio","type":"visit","target":"Hitsville USA"}],"rewards":{"experience":400,"currency":200,"items":["motown_vinyl"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('quest-mexico-zocalo-square', 'Zocalo Square Mysteries', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['mexico', 'zocalo', 'square', '2020-2029'], ARRAY['history', 'mystery', 'central'],
        '{"metadata":{"id":"quest-mexico-zocalo-square","title":"Zocalo Square Mysteries","description":"Uncover the secrets of Mexico Citys central square","quest_type":"mystery","level_min":10,"level_max":30},"objectives":[{"id":"investigate-statue","description":"Investigate the ancient statue","type":"investigate","target":"Ancient Statue"}],"rewards":{"experience":1000,"currency":500,"items":["ancient_artifact"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('quest-mexico-tacos-pastor', 'Tacos al Pastor Quest', 'canon', 'quest', 'draft', '1.0.0',
        ARRAY['mexico', 'tacos', 'pastor', '2020-2029'], ARRAY['food', 'culture', 'street-food'],
        '{"metadata":{"id":"quest-mexico-tacos-pastor","title":"Tacos al Pastor Quest","description":"Master the art of authentic tacos al pastor","quest_type":"culinary","level_min":1,"level_max":10},"objectives":[{"id":"find-vendor","description":"Find the best tacos al pastor vendor","type":"locate","target":"Street Food Vendor"}],"rewards":{"experience":200,"currency":100,"items":["tacos_al_pastor"]}}'::jsonb,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
