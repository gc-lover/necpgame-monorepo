-- Issue: #140876094
-- Player Orders System Database Schema Enhancement
-- Дополнение схемы системы заказов игроков:
-- - Создание ENUM типов для соответствия архитектуре
-- - Обновление существующих таблиц для использования ENUM (опционально)
-- - Убедиться, что все поля из архитектуры присутствуют
-- Примечание: Базовые таблицы уже созданы в V1_27 и V1_53

-- Создание схемы social, если её нет (уже создана в V1_27, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов для соответствия архитектуре
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_order_type') THEN
        CREATE TYPE player_order_type AS ENUM ('combat', 'hacking', 'trade', 'political', 'research', 'social');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_order_risk_level') THEN
        CREATE TYPE player_order_risk_level AS ENUM ('low', 'medium', 'high');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_order_payment_model') THEN
        CREATE TYPE player_order_payment_model AS ENUM ('fixed', 'hourly', 'percentage', 'hybrid');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_order_status') THEN
        CREATE TYPE player_order_status AS ENUM ('created', 'published', 'accepted', 'in-progress', 'completed', 'cancelled', 'failed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'player_order_format') THEN
        CREATE TYPE player_order_format AS ENUM ('public', 'selective', 'private', 'auction');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'auction_type') THEN
        CREATE TYPE auction_type AS ENUM ('ascending', 'descending', 'sealed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'auction_status') THEN
        CREATE TYPE auction_status AS ENUM ('open', 'closed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'bid_status') THEN
        CREATE TYPE bid_status AS ENUM ('active', 'outbid', 'winning', 'lost');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_option_type') THEN
        CREATE TYPE order_option_type AS ENUM ('customer_cancellation', 'executor_cancellation', 'mutual_cancellation');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_option_status') THEN
        CREATE TYPE order_option_status AS ENUM ('active', 'exercised', 'expired');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'arbitration_status') THEN
        CREATE TYPE arbitration_status AS ENUM ('pending', 'in-review', 'resolved', 'dismissed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'insurance_type') THEN
        CREATE TYPE insurance_type AS ENUM ('mission_failure', 'cargo', 'delay', 'comprehensive');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'insurance_status') THEN
        CREATE TYPE insurance_status AS ENUM ('active', 'claimed', 'expired');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'rating_type') THEN
        CREATE TYPE rating_type AS ENUM ('customer', 'executor');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'reputation_type') THEN
        CREATE TYPE reputation_type AS ENUM ('customer', 'executor');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transaction_type') THEN
        CREATE TYPE transaction_type AS ENUM ('deposit', 'payment', 'commission', 'refund', 'penalty');
    END IF;
END $$;

-- Обновление player_orders для добавления недостающих полей (если их нет)
DO $$ 
BEGIN
    -- Проверяем и добавляем недостающие поля
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'player_orders' 
                   AND column_name = 'complexity') THEN
        ALTER TABLE social.player_orders ADD COLUMN complexity INTEGER DEFAULT 0;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'player_orders' 
                   AND column_name = 'risk_level') THEN
        ALTER TABLE social.player_orders ADD COLUMN risk_level VARCHAR(20) CHECK (risk_level IN ('low', 'medium', 'high'));
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'player_orders' 
                   AND column_name = 'reward_amount') THEN
        ALTER TABLE social.player_orders ADD COLUMN reward_amount DECIMAL(10, 2);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'player_orders' 
                   AND column_name = 'payment_model') THEN
        ALTER TABLE social.player_orders ADD COLUMN payment_model VARCHAR(20) CHECK (payment_model IN ('fixed', 'hourly', 'percentage', 'hybrid'));
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'player_orders' 
                   AND column_name = 'format') THEN
        ALTER TABLE social.player_orders ADD COLUMN format VARCHAR(20) CHECK (format IN ('public', 'selective', 'private', 'auction'));
    END IF;
END $$;

-- Обновление индексов для player_orders (если их нет)
CREATE INDEX IF NOT EXISTS idx_player_orders_complexity ON social.player_orders(complexity, status);
CREATE INDEX IF NOT EXISTS idx_player_orders_risk_level ON social.player_orders(risk_level, status);
CREATE INDEX IF NOT EXISTS idx_player_orders_format ON social.player_orders(format, status);
CREATE INDEX IF NOT EXISTS idx_player_orders_deadline ON social.player_orders(deadline) WHERE deadline IS NOT NULL;

-- Комментарии к таблицам (обновление существующих)
COMMENT ON TABLE social.player_orders IS 'Заказы от игроков (основная таблица)';
COMMENT ON TABLE social.multi_executor_orders IS 'Мульти-исполнительные заказы (команды исполнителей)';
COMMENT ON TABLE social.order_auctions IS 'Аукционы заказов (ascending, descending, sealed)';
COMMENT ON TABLE social.auction_bids IS 'Ставки на аукционах заказов';
COMMENT ON TABLE social.order_options IS 'Опционы на заказы (отмена, компенсация)';
COMMENT ON TABLE social.order_arbitration IS 'Арбитраж заказов (споры, решения)';
COMMENT ON TABLE social.order_insurance IS 'Страхование заказов (провал миссии, груз, задержка)';
COMMENT ON TABLE social.order_ratings IS 'Рейтинги заказов (качество, своевременность, коммуникация)';
COMMENT ON TABLE social.order_reputation IS 'Репутация в заказах (заказчик/исполнитель)';
COMMENT ON TABLE social.order_economy IS 'Экономика заказов (депозиты, выплаты, комиссии)';
COMMENT ON TABLE social.order_telemetry IS 'Телеметрия заказов (события, аналитика)';

-- Комментарии к колонкам player_orders
COMMENT ON COLUMN social.player_orders.complexity IS 'Сложность заказа (число)';
COMMENT ON COLUMN social.player_orders.risk_level IS 'Уровень риска: low, medium, high';
COMMENT ON COLUMN social.player_orders.reward_amount IS 'Сумма награды';
COMMENT ON COLUMN social.player_orders.payment_model IS 'Модель оплаты: fixed, hourly, percentage, hybrid';
COMMENT ON COLUMN social.player_orders.format IS 'Формат заказа: public, selective, private, auction';
COMMENT ON COLUMN social.player_orders.deadline IS 'Срок выполнения заказа';


