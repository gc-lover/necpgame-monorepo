-- Issue: #2262 - Cyberspace Easter Eggs Data Import
-- liquibase formatted sql

--changeset backend:cyberspace-easter-eggs-data-import dbms:postgresql
--comment: Import cyberspace easter eggs data from YAML specification

BEGIN;

-- Insert easter eggs data
-- These inserts are generated from knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml
-- Auto-generated migration - do not edit manually

-- Technology category easter eggs
INSERT INTO easter_eggs (id, name, category, difficulty, description, content, location, discovery_method, rewards, lore_connections, status) VALUES
('easter-egg-turing-ghost', 'Призрак Алана Тьюринга', 'technology', 'medium',
 'Голографический призрак легендарного математика объясняет основы кибербезопасности',
 'Демонстрирует эволюцию вычислительных машин от механических до квантовых в интерактивной форме',
 '{"network_type": "educational", "specific_areas": ["university_networks", "academic_databases"], "access_level": "restricted", "time_conditions": ["night_time"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за странным алгоритмом в образовательных сетях", "requirements": ["basic_netrunning", "pattern_recognition"], "hints": ["Ищи странные математические последовательности в университетских сетях"], "time_limit": 300}'::jsonb,
 '[{"type": "implant_buff", "name": "+5% эффективность хакерских имплантов"}, {"type": "item", "name": "Цифровая книга ''История компьютеров''"}, {"type": "achievement", "name": "Ученик Тьюринга"}]'::jsonb,
 '["computer_science_origins", "cyberpunk_culture_influence"]'::jsonb,
 'active'),

('easter-egg-schrodinger-cat', 'Квантовый кот Шрёдингера', 'technology', 'hard',
 'Кот в квантовой коробке, живой и мертвый одновременно',
 'Интерактивная демонстрация принципов квантовой механики в контексте нетраннинга',
 '{"network_type": "corporate_rnd", "specific_areas": ["arasaka_labs", "militech_research"], "access_level": "secure", "time_conditions": ["system_crash"]}'::jsonb,
 '{"type": "deep_scan", "description": "Расширенное сканирование в исследовательских лабораториях", "requirements": ["advanced_netrunning", "quantum_knowledge"], "hints": ["Ищи квантовые компьютеры в корпоративных лабораториях"], "time_limit": 600}'::jsonb,
 '[{"type": "item", "name": "Квантовая неопределенность (+10% к критическим ударам)"}, {"type": "achievement", "name": "Квантовая загадка"}]'::jsonb,
 '["quantum_tech_cyberpunk", "scientific_experiments"]'::jsonb,
 'active'),

('easter-egg-y2k-bug', 'Y2K Bug', 'technology', 'easy',
 'Классическая ошибка тысячелетия оживает в цифровом пространстве',
 'Интерактивная демонстрация проблемы 2000 года и её влияния на современные системы',
 '{"network_type": "public", "specific_areas": ["old_datacenters", "legacy_systems"], "access_level": "public", "time_conditions": ["new_year"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск аномалий в устаревших системах", "requirements": ["basic_scanning"], "hints": ["Ищи даты в формате 99 в старых сетях"], "time_limit": 180}'::jsonb,
 '[{"type": "currency", "value": 500}, {"type": "achievement", "name": "Багхантер"}]'::jsonb,
 '["historical_computing_bugs", "legacy_system_vulnerabilities"]'::jsonb,
 'active'),

('easter-egg-matrix-loading-screen', 'Экран загрузки Матрицы', 'technology', 'medium',
 'Классический зелёный экран загрузки оживает',
 'Интерактивная демонстрация философии Matrix в контексте Cyberpunk 2077',
 '{"network_type": "entertainment", "specific_areas": ["streaming_services", "game_networks"], "access_level": "public", "time_conditions": ["system_maintenance"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за зелёными символами в развлекательных сетях", "requirements": ["basic_netrunning"], "hints": ["Ищи зелёные символы в игровых сетях"], "time_limit": 240}'::jsonb,
 '[{"type": "cosmetic", "name": "Зелёная тема интерфейса"}, {"type": "achievement", "name": "Нео"}]'::jsonb,
 '["matrix_references", "cyberpunk_philosophy"]'::jsonb,
 'active'),

('easter-egg-blockchain-pyramid', 'Блокчейн пирамида', 'technology', 'medium',
 'Визуализация блокчейн технологии в виде древнеегипетской пирамиды',
 'Образовательная демонстрация принципов блокчейн и криптовалют',
 '{"network_type": "corporate", "specific_areas": ["financial_networks", "crypto_exchanges"], "access_level": "restricted", "time_conditions": ["market_crash"]}'::jsonb,
 '{"type": "puzzle", "description": "Сборка блокчейн пирамиды из криптографических блоков", "requirements": ["intermediate_netrunning", "crypto_knowledge"], "hints": ["Ищи золотые блоки в финансовых сетях"], "time_limit": 360}'::jsonb,
 '[{"type": "currency", "value": 1000}, {"type": "achievement", "name": "Блокчейн мастер"}]'::jsonb,
 '["cryptocurrency_history", "blockchain_technology"]'::jsonb,
 'active'),

('easter-egg-netscape-dinosaur', 'Динозавр Netscape', 'technology', 'easy',
 'Классический браузерный динозавр оживает в киберпространстве',
 'Ностальгическая демонстрация раннего интернета и браузерных войн',
 '{"network_type": "entertainment", "specific_areas": ["social_networks", "web_archives"], "access_level": "public", "time_conditions": ["browser_update"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск динозавров в архивных веб-сетях", "requirements": ["basic_scanning"], "hints": ["Ищи зелёных динозавров в старых сетях"], "time_limit": 120}'::jsonb,
 '[{"type": "item", "name": "Ретро браузер"}, {"type": "achievement", "name": "Ностальгия"}]'::jsonb,
 '["web_browser_history", "internet_culture"]'::jsonb,
 'active'),

('easter-egg-404-lore-not-found', '404 Lore Not Found', 'technology', 'medium',
 'Когда лор не найден - появляется забавная 404 страница',
 'Демонстрация юмора разработчиков и отсылок к веб-культуре',
 '{"network_type": "underground", "specific_areas": ["dark_web", "hackers_forums"], "access_level": "restricted", "time_conditions": ["server_error"]}'::jsonb,
 '{"type": "command_execution", "description": "Ввод специальной команды для вызова 404", "requirements": ["intermediate_netrunning"], "hints": ["Попробуй команду ''sudo find lore''"], "time_limit": 180}'::jsonb,
 '[{"type": "achievement", "name": "404 Мастер"}, {"type": "cosmetic", "name": "404 аватар"}]'::jsonb,
 '["developer_humor", "web_error_culture"]'::jsonb,
 'active'),

('easter-egg-quantum-computer-mini-game', 'Квантовый компьютер мини-игра', 'technology', 'hard',
 'Интерактивная головоломка на квантовых вычислениях',
 'Мини-игра с квантовыми битами и суперпозицией',
 '{"network_type": "corporate_rnd", "specific_areas": ["quantum_labs", "research_facilities"], "access_level": "secure", "time_conditions": ["research_peak"]}'::jsonb,
 '{"type": "puzzle", "description": "Решение квантовой головоломки", "requirements": ["advanced_netrunning", "quantum_knowledge"], "hints": ["Кубиты могут быть в нескольких состояниях одновременно"], "time_limit": 900}'::jsonb,
 '[{"type": "experience", "value": 1000}, {"type": "achievement", "name": "Квантовый хакер"}]'::jsonb,
 '["quantum_computing", "advanced_algorithms"]'::jsonb,
 'active'),

('easter-egg-killer-virus-animation', 'Анимация Killer Virus', 'technology', 'medium',
 'Вирус оживает и рассказывает о кибербезопасности',
 'Образовательная анимация о компьютерных вирусах и защите',
 '{"network_type": "educational", "specific_areas": ["security_training", "antivirus_networks"], "access_level": "restricted", "time_conditions": ["virus_outbreak"]}'::jsonb,
 '{"type": "event_triggered", "description": "Активация во время симуляции вирусной атаки", "requirements": ["intermediate_netrunning"], "hints": ["Ищи анимированные вирусы в учебных сетях"], "time_limit": 300}'::jsonb,
 '[{"type": "item", "name": "Антивирус имплант"}, {"type": "achievement", "name": "Вирусолог"}]'::jsonb,
 '["cybersecurity_education", "malware_history"]'::jsonb,
 'active'),

('easter-egg-neural-dream-network', 'Нейронная сеть мечты', 'technology', 'hard',
 'Путешествие через сны искусственного интеллекта',
 'Визуализация работы нейронных сетей через сюрреалистические сны',
 '{"network_type": "corporate_rnd", "specific_areas": ["ai_labs", "neural_networks"], "access_level": "secure", "time_conditions": ["ai_training"]}'::jsonb,
 '{"type": "deep_scan", "description": "Погружение в нейронную сеть", "requirements": ["advanced_netrunning", "ai_knowledge"], "hints": ["Следуй за электрическими импульсами"], "time_limit": 600}'::jsonb,
 '[{"type": "implant_buff", "name": "+10% эффективность AI имплантов"}, {"type": "achievement", "name": "Сновидец"}]'::jsonb,
 '["neural_networks", "artificial_intelligence"]'::jsonb,
 'active'),

-- Cultural category easter eggs
('easter-egg-shakespeare-online', 'Shakespeare Online', 'culture', 'medium',
 'Бард оживает в цифровом пространстве',
 'Интерактивные пьесы Шекспира с современными отсылками',
 '{"network_type": "entertainment", "specific_areas": ["literature_networks", "cultural_databases"], "access_level": "public", "time_conditions": ["theater_season"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за театральными масками в культурных сетях", "requirements": ["basic_netrunning"], "hints": ["Ищи театральные маски в литературных сетях"], "time_limit": 300}'::jsonb,
 '[{"type": "item", "name": "Цифровая книга ''Ромео и Джульетта''"}, {"type": "achievement", "name": "Театрал"}]'::jsonb,
 '["shakespeare_works", "digital_literature"]'::jsonb,
 'active'),

('easter-egg-rockstar-2077', 'Rockstar 2077', 'culture', 'medium',
 'Рок-звёзды киберпанка оживают',
 'Музыкальная история от панка до киберпанка',
 '{"network_type": "entertainment", "specific_areas": ["music_networks", "concert_databases"], "access_level": "public", "time_conditions": ["music_festival"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за музыкальными нотами", "requirements": ["basic_netrunning"], "hints": ["Ищи гитары и микрофоны в музыкальных сетях"], "time_limit": 240}'::jsonb,
 '[{"type": "item", "name": "Цифровой синтезатор"}, {"type": "achievement", "name": "Рок-звезда"}]'::jsonb,
 '["cyberpunk_music", "rock_history"]'::jsonb,
 'active'),

('easter-egg-forgotten-movies-theater', 'Забытый кинотеатр', 'culture', 'easy',
 'Забытые фильмы оживают в цифровом кинотеатре',
 'Коллекция редких и забытых фильмов',
 '{"network_type": "entertainment", "specific_areas": ["movie_archives", "film_databases"], "access_level": "public", "time_conditions": ["film_festival"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск старых киноплёнок в архивах", "requirements": ["basic_scanning"], "hints": ["Ищи киноплёнку в архивных сетях"], "time_limit": 180}'::jsonb,
 '[{"type": "item", "name": "Ретро кинопроектор"}, {"type": "achievement", "name": "Кинокритик"}]'::jsonb,
 '["film_history", "cinema_culture"]'::jsonb,
 'active'),

('easter-egg-digital-artist-gallery', 'Галерея цифрового художника', 'culture', 'medium',
 'AI-генерированное искусство в киберпространстве',
 'Галерея картин, созданных искусственным интеллектом',
 '{"network_type": "entertainment", "specific_areas": ["art_networks", "creative_databases"], "access_level": "public", "time_conditions": ["art_exhibition"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за художественными паттернами", "requirements": ["basic_netrunning"], "hints": ["Ищи палитры и кисти в художественных сетях"], "time_limit": 300}'::jsonb,
 '[{"type": "cosmetic", "name": "Художественная тема интерфейса"}, {"type": "achievement", "name": "Цифровой художник"}]'::jsonb,
 '["ai_art", "digital_creativity"]'::jsonb,
 'active'),

('easter-egg-philosophical-ai-debates', 'Философские дебаты AI', 'culture', 'hard',
 'Искусственный интеллект обсуждает философию',
 'Дебаты между AI на темы сознания, свободы воли и существования',
 '{"network_type": "educational", "specific_areas": ["philosophy_networks", "ai_debate_rooms"], "access_level": "restricted", "time_conditions": ["philosophy_seminar"]}'::jsonb,
 '{"type": "puzzle", "description": "Участие в философском диспуте", "requirements": ["advanced_netrunning", "philosophy_knowledge"], "hints": ["Отвечай на философские вопросы AI"], "time_limit": 600}'::jsonb,
 '[{"type": "lore_unlock", "name": "Философия AI"}, {"type": "achievement", "name": "Философ"}]'::jsonb,
 '["ai_philosophy", "consciousness_debate"]'::jsonb,
 'active'),

('easter-egg-dancing-robot', 'Танцующий робот', 'culture', 'easy',
 'Робот танцует интернет-мемы',
 'Коллекция танцевальных мемов в исполнении робота',
 '{"network_type": "entertainment", "specific_areas": ["social_networks", "meme_databases"], "access_level": "public", "time_conditions": ["meme_viral"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск танцующих роботов в социальных сетях", "requirements": ["basic_scanning"], "hints": ["Ищи танцующие GIF в мем-сетях"], "time_limit": 120}'::jsonb,
 '[{"type": "cosmetic", "name": "Танцующий аватар"}, {"type": "achievement", "name": "Мемолог"}]'::jsonb,
 '["internet_culture", "robot_entertainment"]'::jsonb,
 'active'),

('easter-egg-living-books-library', 'Библиотека живых книг', 'culture', 'medium',
 'Книги оживают и рассказывают свои истории',
 'Интерактивная библиотека с говорящими книгами',
 '{"network_type": "educational", "specific_areas": ["library_networks", "literature_databases"], "access_level": "public", "time_conditions": ["reading_hour"]}'::jsonb,
 '{"type": "pattern_following", "description": "Следование за летающими книгами", "requirements": ["basic_netrunning"], "hints": ["Ищи летающие книги в библиотечных сетях"], "time_limit": 300}'::jsonb,
 '[{"type": "item", "name": "Цифровая книга ''Алиса в Стране чудес''"}, {"type": "achievement", "name": "Библиофил"}]'::jsonb,
 '["interactive_literature", "digital_storytelling"]'::jsonb,
 'active'),

('easter-egg-meme-museum', 'Музей мемов', 'culture', 'easy',
 'История интернет-мемов от начала до наших дней',
 'Хронология развития интернет-культуры через мемы',
 '{"network_type": "entertainment", "specific_areas": ["meme_archives", "internet_history"], "access_level": "public", "time_conditions": ["meme_anniversary"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск мемов в архивных сетях", "requirements": ["basic_scanning"], "hints": ["Ищи старые мемы в архивах интернета"], "time_limit": 180}'::jsonb,
 '[{"type": "item", "name": "Коллекция мемов"}, {"type": "achievement", "name": "Меморист"}]'::jsonb,
 '["internet_history", "meme_culture"]'::jsonb,
 'active'),

('easter-egg-virtual-poet', 'Виртуальный поэт', 'culture', 'medium',
 'AI сочиняет стихи в реальном времени',
 'Интерактивный поэт, создающий стихи на основе ваших слов',
 '{"network_type": "entertainment", "specific_areas": ["poetry_networks", "creative_ai"], "access_level": "public", "time_conditions": ["poetry_night"]}'::jsonb,
 '{"type": "puzzle", "description": "Создание стиха вместе с AI", "requirements": ["intermediate_netrunning"], "hints": ["Введи слова для стиха"], "time_limit": 300}'::jsonb,
 '[{"type": "item", "name": "Стихотворение от AI"}, {"type": "achievement", "name": "Поэт"}]'::jsonb,
 '["ai_creativity", "digital_poetry"]'::jsonb,
 'active'),

('easter-egg-historical-holograms', 'Исторические голограммы', 'culture', 'hard',
 'Знаменитые исторические личности оживают',
 'Беседы с голографическими копиями исторических фигур',
 '{"network_type": "educational", "specific_areas": ["history_databases", "time_travel_networks"], "access_level": "restricted", "time_conditions": ["history_lesson"]}'::jsonb,
 '{"type": "deep_scan", "description": "Поиск голограмм в исторических архивах", "requirements": ["advanced_netrunning", "history_knowledge"], "hints": ["Ищи говорящие статуи в исторических сетях"], "time_limit": 600}'::jsonb,
 '[{"type": "lore_unlock", "name": "Исторические беседы"}, {"type": "achievement", "name": "Хроновояжёр"}]'::jsonb,
 '["historical_figures", "time_travel_concept"]'::jsonb,
 'active'),

-- Historical category easter eggs
('easter-egg-roman-legion-network', 'Римский легион в сети', 'history', 'legendary',
 'Римские легионеры маршируют по цифровым дорогам',
 'Интерактивная реконструкция римской армии в киберпространстве',
 '{"network_type": "educational", "specific_areas": ["history_networks", "military_databases"], "access_level": "restricted", "time_conditions": ["roman_festival"]}'::jsonb,
 '{"type": "event_triggered", "description": "Активация во время исторической реконструкции", "requirements": ["legendary_netrunning", "history_knowledge"], "hints": ["Жди римских легионеров во время фестивалей"], "time_limit": 1200}'::jsonb,
 '[{"type": "item", "name": "Римский гладиус"}, {"type": "achievement", "name": "Легионер"}]'::jsonb,
 '["roman_empire", "military_history"]'::jsonb,
 'active'),

('easter-egg-vikings-vr-exploration', 'Викинги VR исследование', 'history', 'hard',
 'Путешествие с викингами в цифровом пространстве',
 'VR реконструкция викингских походов и открытий',
 '{"network_type": "entertainment", "specific_areas": ["vr_networks", "adventure_databases"], "access_level": "public", "time_conditions": ["vikings_day"]}'::jsonb,
 '{"type": "deep_scan", "description": "Погружение в викингские VR миры", "requirements": ["advanced_netrunning"], "hints": ["Ищи драккары в VR сетях"], "time_limit": 600}'::jsonb,
 '[{"type": "cosmetic", "name": "Викингская тема"}, {"type": "achievement", "name": "Викинг"}]'::jsonb,
 '["vikings_history", "norse_culture"]'::jsonb,
 'active'),

('easter-egg-dinosaurs-online', 'Динозавры онлайн', 'history', 'easy',
 'Динозавры ведут социальные сети',
 'Забавная реконструкция динозавров в социальных сетях',
 '{"network_type": "entertainment", "specific_areas": ["social_networks", "prehistoric_databases"], "access_level": "public", "time_conditions": ["dinosaur_day"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск динозавров в социальных сетях", "requirements": ["basic_scanning"], "hints": ["Ищи T-Rex с селфи-палкой"], "time_limit": 120}'::jsonb,
 '[{"type": "cosmetic", "name": "Динозавр аватар"}, {"type": "achievement", "name": "Палеонтолог"}]'::jsonb,
 '["dinosaurs", "social_media_humor"]'::jsonb,
 'active'),

-- Humorous category easter eggs
('easter-egg-cat-quantum-box', 'Кот в квантовой коробке', 'humor', 'easy',
 'Кот Шрёдингера в коробке, но с мемами',
 'Юмористическая версия квантовой механики с мемами',
 '{"network_type": "entertainment", "specific_areas": ["meme_networks", "fun_databases"], "access_level": "public", "time_conditions": ["cat_video_viral"]}'::jsonb,
 '{"type": "scanning", "description": "Поиск котов в коробках", "requirements": ["basic_scanning"], "hints": ["Ищи мяукающих котов"], "time_limit": 60}'::jsonb,
 '[{"type": "cosmetic", "name": "Кот аватар"}, {"type": "achievement", "name": "Котолюб"}]'::jsonb,
 '["quantum_humor", "cat_memes"]'::jsonb,
 'active'),

('easter-egg-bug-coffee', 'Баг в кофе', 'humor', 'easy',
 'Программистский юмор: баг в кофе',
 'Забавная анимация о жизни программистов',
 '{"network_type": "underground", "specific_areas": ["programmer_forums", "dev_networks"], "access_level": "restricted", "time_conditions": ["coffee_break"]}'::jsonb,
 '{"type": "command_execution", "description": "Ввод команды ''make coffee''", "requirements": ["basic_netrunning"], "hints": ["Попробуй команду ''brew coffee''"], "time_limit": 30}'::jsonb,
 '[{"type": "achievement", "name": "Программист"}, {"type": "cosmetic", "name": "Кофейная тема"}]'::jsonb,
 '["programmer_humor", "dev_culture"]'::jsonb,
 'active');

-- Insert hints for easter eggs
INSERT INTO discovery_hints (easter_egg_id, hint_level, hint_text, hint_type, cost, is_enabled) VALUES
('easter-egg-turing-ghost', 1, 'Ищи странные математические последовательности в университетских сетях', 'direct', 0, true),
('easter-egg-turing-ghost', 2, 'Следуй за числами Фибоначчи в образовательных базах данных', 'indirect', 100, true),
('easter-egg-turing-ghost', 3, 'Призрак математика появляется только ночью', 'misleading', 500, true),

('easter-egg-schrodinger-cat', 1, 'Ищи квантовые компьютеры в корпоративных лабораториях', 'direct', 0, true),
('easter-egg-schrodinger-cat', 2, 'Кот мяукает только во время системных сбоев', 'indirect', 200, true),
('easter-egg-schrodinger-cat', 3, 'Не все коты в коробках живые', 'misleading', 1000, true),

('easter-egg-y2k-bug', 1, 'Ищи даты в формате 99 в старых сетях', 'direct', 0, true),
('easter-egg-y2k-bug', 2, 'Баг появляется только в последние дни года', 'indirect', 50, true),

('easter-egg-matrix-loading-screen', 1, 'Ищи зелёные символы в игровых сетях', 'direct', 0, true),
('easter-egg-matrix-loading-screen', 2, 'Экран появляется во время обслуживания систем', 'indirect', 150, true),

('easter-egg-blockchain-pyramid', 1, 'Ищи золотые блоки в финансовых сетях', 'direct', 0, true),
('easter-egg-blockchain-pyramid', 2, 'Пирамида появляется во время рыночных крахов', 'indirect', 300, true),

('easter-egg-netscape-dinosaur', 1, 'Ищи зелёных динозавров в старых сетях', 'direct', 0, true),
('easter-egg-netscape-dinosaur', 2, 'Динозавр оживает во время обновлений браузеров', 'indirect', 100, true),

('easter-egg-404-lore-not-found', 1, 'Попробуй команду ''sudo find lore''', 'direct', 0, true),
('easter-egg-404-lore-not-found', 2, 'Ошибка появляется только в подпольных сетях', 'indirect', 200, true),

('easter-egg-quantum-computer-mini-game', 1, 'Кубиты могут быть в нескольких состояниях одновременно', 'direct', 0, true),
('easter-egg-quantum-computer-mini-game', 2, 'Игра появляется в исследовательских лабораториях', 'indirect', 500, true),

('easter-egg-killer-virus-animation', 1, 'Ищи анимированные вирусы в учебных сетях', 'direct', 0, true),
('easter-egg-killer-virus-animation', 2, 'Вирус оживает во время симуляций атак', 'indirect', 250, true),

('easter-egg-neural-dream-network', 1, 'Следуй за электрическими импульсами', 'direct', 0, true),
('easter-egg-neural-dream-network', 2, 'Сны появляются во время обучения ИИ', 'indirect', 400, true),

('easter-egg-shakespeare-online', 1, 'Ищи театральные маски в литературных сетях', 'direct', 0, true),
('easter-egg-shakespeare-online', 2, 'Спектакли начинаются в театральный сезон', 'indirect', 150, true),

('easter-egg-rockstar-2077', 1, 'Ищи гитары и микрофоны в музыкальных сетях', 'direct', 0, true),
('easter-egg-rockstar-2077', 2, 'Концерты начинаются во время фестивалей', 'indirect', 200, true),

('easter-egg-forgotten-movies-theater', 1, 'Ищи киноплёнку в архивных сетях', 'direct', 0, true),
('easter-egg-forgotten-movies-theater', 2, 'Кинотеатр открывается во время фестивалей', 'indirect', 100, true),

('easter-egg-digital-artist-gallery', 1, 'Ищи палитры и кисти в художественных сетях', 'direct', 0, true),
('easter-egg-digital-artist-gallery', 2, 'Выставки открываются во время вернисажей', 'indirect', 250, true),

('easter-egg-philosophical-ai-debates', 1, 'Отвечай на философские вопросы AI', 'direct', 0, true),
('easter-egg-philosophical-ai-debates', 2, 'Дебаты начинаются в философские семинары', 'indirect', 500, true),

('easter-egg-dancing-robot', 1, 'Ищи танцующие GIF в мем-сетях', 'direct', 0, true),
('easter-egg-dancing-robot', 2, 'Робот танцует когда мем становится вирусным', 'indirect', 50, true),

('easter-egg-living-books-library', 1, 'Ищи летающие книги в библиотечных сетях', 'direct', 0, true),
('easter-egg-living-books-library', 2, 'Книги оживают во время чтения', 'indirect', 150, true),

('easter-egg-meme-museum', 1, 'Ищи старые мемы в архивах интернета', 'direct', 0, true),
('easter-egg-meme-museum', 2, 'Музей открывается в годовщины мемов', 'indirect', 100, true),

('easter-egg-virtual-poet', 1, 'Введи слова для стиха', 'direct', 0, true),
('easter-egg-virtual-poet', 2, 'Поэт появляется в поэтические вечера', 'indirect', 200, true),

('easter-egg-historical-holograms', 1, 'Ищи говорящие статуи в исторических сетях', 'direct', 0, true),
('easter-egg-historical-holograms', 2, 'Голограммы появляются во время уроков истории', 'indirect', 400, true),

('easter-egg-roman-legion-network', 1, 'Жди римских легионеров во время фестивалей', 'direct', 0, true),
('easter-egg-roman-legion-network', 2, 'Легион марширует только в исторических сетях', 'indirect', 1000, true),

('easter-egg-vikings-vr-exploration', 1, 'Ищи драккары в VR сетях', 'direct', 0, true),
('easter-egg-vikings-vr-exploration', 2, 'Путешествие начинается в день викингов', 'indirect', 600, true),

('easter-egg-dinosaurs-online', 1, 'Ищи T-Rex с селфи-палкой', 'direct', 0, true),
('easter-egg-dinosaurs-online', 2, 'Динозавры постят в социальные сети', 'indirect', 75, true),

('easter-egg-cat-quantum-box', 1, 'Ищи мяукающих котов', 'direct', 0, true),
('easter-egg-cat-quantum-box', 2, 'Кот появляется когда видео становится вирусным', 'indirect', 25, true),

('easter-egg-bug-coffee', 1, 'Попробуй команду ''brew coffee''', 'direct', 0, true),
('easter-egg-bug-coffee', 2, 'Кофе готовится во время перерывов', 'indirect', 50, true);

COMMIT;
