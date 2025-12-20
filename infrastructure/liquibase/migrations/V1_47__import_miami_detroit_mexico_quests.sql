-- Issue: #51
-- Import Miami, Detroit, Mexico City 2020-2029 quests to database

-- Function to load YAML content and insert quest
CREATE OR REPLACE FUNCTION import_quest_from_yaml(quest_file_path text)
RETURNS void AS $$
DECLARE
    quest_data jsonb;
    quest_id text;
BEGIN
    -- Load YAML file (this would need to be done via external tool)
    -- For now, we'll manually insert the data

    RAISE NOTICE 'Importing quest from: %', quest_file_path;
END;
$$ LANGUAGE plpgsql;

-- Import Miami quests (first 2 for demo)
INSERT INTO gameplay.quest_definitions (
    id, title, document_type, category, status, version, tags, topics, yaml_content, created_at, updated_at
) VALUES
('quest-miami-south-beach-neon', 'South Beach Neon Nights', 'canon', 'quest', 'draft', '1.0.0',
 '{"miami","neon","beach","2020-2029"}'::text[], '{"cyberpunk","nightlife","tourism"}'::text[],
 '{
   "metadata": {
     "id": "quest-miami-south-beach-neon",
     "title": "South Beach Neon Nights",
     "description": "Experience the vibrant neon-lit nightlife of South Beach",
     "quest_type": "side",
     "level_min": 1,
     "level_max": 20
   },
   "objectives": [
     {
       "id": "explore-beach",
       "description": "Explore the neon-lit beachfront",
       "type": "explore",
       "target": "South Beach District"
     }
   ],
   "rewards": {
     "experience": 500,
     "currency": 200,
     "items": ["neon_beach_souvenir"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),

('quest-miami-cuban-havana', 'Cuban Heritage in Little Havana', 'canon', 'quest', 'draft', '1.0.0',
 '{"miami","cuban","havana","2020-2029"}'::text[], '{"culture","heritage","food"}'::text[],
 '{
   "metadata": {
     "id": "quest-miami-cuban-havana",
     "title": "Cuban Heritage in Little Havana",
     "description": "Discover the rich Cuban culture in Little Havana",
     "quest_type": "cultural",
     "level_min": 1,
     "level_max": 15
   },
   "objectives": [
     {
       "id": "visit-museum",
       "description": "Visit the Cuban Memorial Museum",
       "type": "visit",
       "target": "Cuban Memorial Museum"
     },
     {
       "id": "taste-coffee",
       "description": "Try authentic Cuban coffee",
       "type": "interact",
       "target": "Local Cafe"
     }
   ],
   "rewards": {
     "experience": 300,
     "currency": 150,
     "items": ["cuban_cigar", "coffee_beans"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Import Detroit quests (first 2 for demo)
INSERT INTO gameplay.quest_definitions (
    id, title, document_type, category, status, version, tags, topics, yaml_content, created_at, updated_at
) VALUES
('quest-detroit-motor-city', 'Motor City Revival', 'canon', 'quest', 'draft', '1.0.0',
 '{"detroit","motor","city","2020-2029"}'::text[], '{"technology","revival","automotive"}'::text[],
 '{
   "metadata": {
     "id": "quest-detroit-motor-city",
     "title": "Motor City Revival",
     "description": "Witness the technological revival of Detroit",
     "quest_type": "main",
     "level_min": 5,
     "level_max": 25
   },
   "objectives": [
     {
       "id": "visit-innovation-district",
       "description": "Explore the Innovation District",
       "type": "explore",
       "target": "Detroit Innovation District"
     },
     {
       "id": "hack-terminal",
       "description": "Hack into the automated factory terminal",
       "type": "hack",
       "target": "Factory Control Terminal"
     }
   ],
   "rewards": {
     "experience": 800,
     "currency": 400,
     "items": ["automotive_chip", "factory_pass"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),

('quest-detroit-motown-music', 'Motown Music Legacy', 'canon', 'quest', 'draft', '1.0.0',
 '{"detroit","motown","music","2020-2029"}'::text[], '{"music","legacy","culture"}'::text[],
 '{
   "metadata": {
     "id": "quest-detroit-motown-music",
     "title": "Motown Music Legacy",
     "description": "Explore the birthplace of Motown music",
     "quest_type": "cultural",
     "level_min": 3,
     "level_max": 18
   },
   "objectives": [
     {
       "id": "visit-hitsville",
       "description": "Visit Hitsville USA recording studio",
       "type": "visit",
       "target": "Hitsville USA"
     },
     {
       "id": "attend-concert",
       "description": "Attend a virtual Motown concert",
       "type": "interact",
       "target": "Virtual Concert Hall"
     }
   ],
   "rewards": {
     "experience": 400,
     "currency": 200,
     "items": ["motown_vinyl", "concert_ticket"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Import Mexico City quests (first 2 for demo)
INSERT INTO gameplay.quest_definitions (
    id, title, document_type, category, status, version, tags, topics, yaml_content, created_at, updated_at
) VALUES
('quest-mexico-zocalo-square', 'Zocalo Square Mysteries', 'canon', 'quest', 'draft', '1.0.0',
 '{"mexico","zocalo","square","2020-2029"}'::text[], '{"history","mystery","central"}'::text[],
 '{
   "metadata": {
     "id": "quest-mexico-zocalo-square",
     "title": "Zocalo Square Mysteries",
     "description": "Uncover the secrets of Mexico Citys central square",
     "quest_type": "mystery",
     "level_min": 10,
     "level_max": 30
   },
   "objectives": [
     {
       "id": "investigate-statue",
       "description": "Investigate the ancient statue",
       "type": "investigate",
       "target": "Ancient Statue"
     },
     {
       "id": "decode-message",
       "description": "Decode the hidden message",
       "type": "hack",
       "target": "Encrypted Datashard"
     }
   ],
   "rewards": {
     "experience": 1000,
     "currency": 500,
     "items": ["ancient_artifact", "datashard"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),

('quest-mexico-tacos-pastor', 'Tacos al Pastor Quest', 'canon', 'quest', 'draft', '1.0.0',
 '{"mexico","tacos","pastor","2020-2029"}'::text[], '{"food","culture","street-food"}'::text[],
 '{
   "metadata": {
     "id": "quest-mexico-tacos-pastor",
     "title": "Tacos al Pastor Quest",
     "description": "Master the art of authentic tacos al pastor",
     "quest_type": "culinary",
     "level_min": 1,
     "level_max": 10
   },
   "objectives": [
     {
       "id": "find-vendor",
       "description": "Find the best tacos al pastor vendor",
       "type": "locate",
       "target": "Street Food Vendor"
     },
     {
       "id": "cook-tacos",
       "description": "Help cook authentic tacos al pastor",
       "type": "craft",
       "target": "Taco Stand Kitchen"
     }
   ],
   "rewards": {
     "experience": 200,
     "currency": 100,
     "items": ["tacos_al_pastor", "cooking_recipe"]
   }
 }'::jsonb,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);
