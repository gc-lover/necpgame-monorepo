-- Issue: #2262 - Easter Eggs Achievement System Integration
-- liquibase formatted sql

--changeset backend:easter-eggs-achievements dbms:postgresql
--comment: Create achievements for cyberspace easter eggs system

BEGIN;

-- Insert easter egg achievements
INSERT INTO achievements (id, name, description, category, icon_url, points, rarity, is_hidden, is_active, created_at, updated_at) VALUES
-- Individual easter egg discoveries
('550e8400-e29b-41d4-a716-446655440000', 'Призрак Тьюринга', 'Обнаружить Призрака Алана Тьюринга в университетских сетях', 'exploration', '/icons/achievements/turing_ghost.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440001', 'Квантовый Загадка', 'Найти Квантового кота Шрёдингера', 'exploration', '/icons/achievements/schrodinger_cat.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', 'Багхантер', 'Обнаружить Y2K Bug в устаревших системах', 'exploration', '/icons/achievements/y2k_bug.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', 'Нео', 'Найти Экран загрузки Матрицы', 'exploration', '/icons/achievements/matrix_screen.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440004', 'Блокчейн Пионер', 'Обнаружить Блокчейн пирамиду', 'exploration', '/icons/achievements/blockchain.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440005', 'Динозавр Сети', 'Найти Динозавра Netscape', 'exploration', '/icons/achievements/netscape_dino.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440006', '404 Лор', 'Обнаружить 404 Lore Not Found', 'exploration', '/icons/achievements/404_lore.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440007', 'Квантовый Игрок', 'Завершить Квантовый компьютер мини-игру', 'exploration', '/icons/achievements/quantum_game.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440008', 'Вирусолог', 'Посмотреть анимацию Killer Virus', 'exploration', '/icons/achievements/killer_virus.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440009', 'Нейронный Сон', 'Исследовать Нейронную сеть мечты', 'exploration', '/icons/achievements/neural_dream.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Cultural easter eggs
('550e8400-e29b-41d4-a716-446655440010', 'Шекспир Онлайн', 'Найти Shakespeare Online', 'culture', '/icons/achievements/shakespeare.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440011', 'Рокстар 2077', 'Обнаружить Rockstar 2077', 'culture', '/icons/achievements/rockstar.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440012', 'Кинофил', 'Посетить Забытый кинотеатр', 'culture', '/icons/achievements/movies.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440013', 'Цифровой Художник', 'Найти Галерею цифрового художника', 'culture', '/icons/achievements/digital_art.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440014', 'Философ AI', 'Принять участие в Философских дебатах AI', 'culture', '/icons/achievements/ai_debates.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440015', 'Танцор', 'Посмотреть танцующего робота', 'culture', '/icons/achievements/dancing_robot.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440016', 'Книголюб', 'Посетить Библиотеку живых книг', 'culture', '/icons/achievements/living_books.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440017', 'Мемолог', 'Исследовать Музей мемов', 'culture', '/icons/achievements/meme_museum.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440018', 'Поэт', 'Встретить Виртуального поэта', 'culture', '/icons/achievements/virtual_poet.png', 50, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440019', 'Историк', 'Посмотреть Исторические голограммы', 'culture', '/icons/achievements/holograms.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Historical easter eggs
('550e8400-e29b-41d4-a716-446655440020', 'Римский Легионер', 'Встретить Римский легион в сети', 'history', '/icons/achievements/roman_legion.png', 100, 'legendary', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440021', 'Викинг', 'Исследовать Викингов VR', 'history', '/icons/achievements/vikings.png', 75, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440022', 'Динозавролог', 'Найти Динозавров онлайн', 'history', '/icons/achievements/dinosaurs.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Humorous easter eggs
('550e8400-e29b-41d4-a716-446655440023', 'Квантовая Кошка', 'Найти Кота в квантовой коробке', 'humor', '/icons/achievements/quantum_cat.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440024', 'Кофейный Баг', 'Обнаружить Баг в кофе', 'humor', '/icons/achievements/bug_coffee.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Collection achievements
('550e8400-e29b-41d4-a716-446655440025', 'Исследователь Сети', 'Обнаружить 5 пасхальных яиц', 'exploration', '/icons/achievements/explorer.png', 100, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440026', 'Охотник за Сокровищами', 'Обнаружить 10 пасхальных яиц', 'exploration', '/icons/achievements/hunter.png', 200, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440027', 'Мастер Сети', 'Обнаружить 15 пасхальных яиц', 'exploration', '/icons/achievements/master.png', 300, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440028', 'Легенда Киберпространства', 'Обнаружить все 25 пасхальных яиц', 'exploration', '/icons/achievements/legend.png', 500, 'legendary', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Category completion achievements
('550e8400-e29b-41d4-a716-446655440029', 'Технофил', 'Обнаружить все 10 технологических пасхальных яиц', 'exploration', '/icons/achievements/technophile.png', 150, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440030', 'Культуролог', 'Обнаружить все 10 культурных пасхальных яиц', 'culture', '/icons/achievements/culturalist.png', 150, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440031', 'Историк Сети', 'Обнаружить все 3 исторических пасхальных яйца', 'history', '/icons/achievements/historian.png', 100, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440032', 'Юморист', 'Обнаружить все 2 юмористических пасхальных яйца', 'humor', '/icons/achievements/humorist.png', 50, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Difficulty-based achievements
('550e8400-e29b-41d4-a716-446655440033', 'Новичок Сети', 'Обнаружить 5 легких пасхальных яиц', 'exploration', '/icons/achievements/novice.png', 50, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440034', 'Опытный Нетраннер', 'Обнаружить 3 средних пасхальных яйца', 'exploration', '/icons/achievements/experienced.png', 100, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440035', 'Хакер Элиты', 'Обнаружить 2 сложных пасхальных яйца', 'exploration', '/icons/achievements/elite_hacker.png', 200, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440036', 'Легендарный Нетраннер', 'Обнаружить легендарное пасхальное яйцо', 'exploration', '/icons/achievements/legendary_netrunner.png', 300, 'legendary', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert achievement criteria for progress tracking
INSERT INTO achievement_criteria (achievement_id, type, target, value, operator) VALUES
-- Collection achievements
('550e8400-e29b-41d4-a716-446655440025', 'stat', 'easter_eggs_discovered', '{"count": 5}', 'gte'),
('550e8400-e29b-41d4-a716-446655440026', 'stat', 'easter_eggs_discovered', '{"count": 10}', 'gte'),
('550e8400-e29b-41d4-a716-446655440027', 'stat', 'easter_eggs_discovered', '{"count": 15}', 'gte'),
('550e8400-e29b-41d4-a716-446655440028', 'stat', 'easter_eggs_discovered', '{"count": 25}', 'gte'),

-- Category completion
('550e8400-e29b-41d4-a716-446655440029', 'stat', 'technology_easter_eggs', '{"count": 10}', 'gte'),
('550e8400-e29b-41d4-a716-446655440030', 'stat', 'cultural_easter_eggs', '{"count": 10}', 'gte'),
('550e8400-e29b-41d4-a716-446655440031', 'stat', 'historical_easter_eggs', '{"count": 3}', 'gte'),
('550e8400-e29b-41d4-a716-446655440032', 'stat', 'humorous_easter_eggs', '{"count": 2}', 'gte'),

-- Difficulty-based
('550e8400-e29b-41d4-a716-446655440033', 'stat', 'easy_easter_eggs', '{"count": 5}', 'gte'),
('550e8400-e29b-41d4-a716-446655440034', 'stat', 'medium_easter_eggs', '{"count": 3}', 'gte'),
('550e8400-e29b-41d4-a716-446655440035', 'stat', 'hard_easter_eggs', '{"count": 2}', 'gte'),
('550e8400-e29b-41d4-a716-446655440036', 'stat', 'legendary_easter_eggs', '{"count": 1}', 'gte');

COMMIT;
