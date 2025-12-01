-- Issue: #140876083
-- Player Market Database Schema
-- Создание таблиц для Player Market:
-- - История сделок на рынке (market_trade_history)
-- - Отзывы продавцов (seller_reviews)
-- - Статистика продавцов (seller_statistics)
-- Примечание: market_listings уже создана в V1_51

-- Создание схемы economy, если её нет (уже создана в V1_15, но для безопасности)
CREATE SCHEMA IF NOT EXISTS economy;

-- Таблица истории сделок на рынке
CREATE TABLE IF NOT EXISTS economy.market_trade_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    listing_id UUID NOT NULL REFERENCES economy.market_listings(id) ON DELETE CASCADE,
    buyer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    item_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price_per_unit DECIMAL(15,2) NOT NULL CHECK (price_per_unit > 0),
    total_price DECIMAL(15,2) NOT NULL CHECK (total_price > 0),
    commission DECIMAL(15,2) NOT NULL DEFAULT 0.0 CHECK (commission >= 0),
    seller_received DECIMAL(15,2) NOT NULL CHECK (seller_received >= 0),
    completed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для market_trade_history
CREATE INDEX IF NOT EXISTS idx_market_trade_history_listing_id ON economy.market_trade_history(listing_id);
CREATE INDEX IF NOT EXISTS idx_market_trade_history_buyer_id ON economy.market_trade_history(buyer_id, completed_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_trade_history_seller_id ON economy.market_trade_history(seller_id, completed_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_trade_history_item_id ON economy.market_trade_history(item_id, completed_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_trade_history_completed_at ON economy.market_trade_history(completed_at DESC);

-- Таблица отзывов продавцов
CREATE TABLE IF NOT EXISTS economy.seller_reviews (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    trade_id UUID NOT NULL REFERENCES economy.market_trade_history(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    buyer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_positive BOOLEAN NOT NULL DEFAULT true,
    reported BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(trade_id, buyer_id)
);

-- Индексы для seller_reviews
CREATE INDEX IF NOT EXISTS idx_seller_reviews_seller_id ON economy.seller_reviews(seller_id, rating, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_seller_reviews_buyer_id ON economy.seller_reviews(buyer_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_seller_reviews_trade_id ON economy.seller_reviews(trade_id);
CREATE INDEX IF NOT EXISTS idx_seller_reviews_rating ON economy.seller_reviews(rating, seller_id);
CREATE INDEX IF NOT EXISTS idx_seller_reviews_reported ON economy.seller_reviews(reported) WHERE reported = true;

-- Таблица статистики продавцов
CREATE TABLE IF NOT EXISTS economy.seller_statistics (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    total_sales INTEGER NOT NULL DEFAULT 0 CHECK (total_sales >= 0),
    total_revenue DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (total_revenue >= 0),
    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (average_rating >= 0 AND average_rating <= 5),
    positive_reviews INTEGER NOT NULL DEFAULT 0 CHECK (positive_reviews >= 0),
    negative_reviews INTEGER NOT NULL DEFAULT 0 CHECK (negative_reviews >= 0),
    total_reviews INTEGER NOT NULL DEFAULT 0 CHECK (total_reviews >= 0),
    items_sold INTEGER NOT NULL DEFAULT 0 CHECK (items_sold >= 0),
    last_sale_at TIMESTAMP,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(seller_id)
);

-- Индексы для seller_statistics
CREATE INDEX IF NOT EXISTS idx_seller_statistics_seller_id ON economy.seller_statistics(seller_id);
CREATE INDEX IF NOT EXISTS idx_seller_statistics_average_rating ON economy.seller_statistics(average_rating DESC, total_reviews DESC);
CREATE INDEX IF NOT EXISTS idx_seller_statistics_total_revenue ON economy.seller_statistics(total_revenue DESC);
CREATE INDEX IF NOT EXISTS idx_seller_statistics_total_sales ON economy.seller_statistics(total_sales DESC);

-- Таблица избранных продавцов
CREATE TABLE IF NOT EXISTS economy.seller_favorites (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    buyer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(buyer_id, seller_id)
);

-- Индексы для seller_favorites
CREATE INDEX IF NOT EXISTS idx_seller_favorites_buyer_id ON economy.seller_favorites(buyer_id);
CREATE INDEX IF NOT EXISTS idx_seller_favorites_seller_id ON economy.seller_favorites(seller_id);

-- Таблица подписок на продавцов
CREATE TABLE IF NOT EXISTS economy.seller_subscriptions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    subscriber_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    notify_on_new_listings BOOLEAN NOT NULL DEFAULT true,
    notify_on_price_drops BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(subscriber_id, seller_id)
);

-- Индексы для seller_subscriptions
CREATE INDEX IF NOT EXISTS idx_seller_subscriptions_subscriber_id ON economy.seller_subscriptions(subscriber_id);
CREATE INDEX IF NOT EXISTS idx_seller_subscriptions_seller_id ON economy.seller_subscriptions(seller_id);

-- Комментарии к таблицам
COMMENT ON TABLE economy.market_trade_history IS 'История сделок на игровом рынке';
COMMENT ON TABLE economy.seller_reviews IS 'Отзывы покупателей о продавцах';
COMMENT ON TABLE economy.seller_statistics IS 'Статистика продавцов (продажи, рейтинг, доходы)';
COMMENT ON TABLE economy.seller_favorites IS 'Избранные продавцы покупателей';
COMMENT ON TABLE economy.seller_subscriptions IS 'Подписки на продавцов для уведомлений';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.market_trade_history.listing_id IS 'ID объявления из market_listings';
COMMENT ON COLUMN economy.market_trade_history.price_per_unit IS 'Цена за единицу товара';
COMMENT ON COLUMN economy.market_trade_history.total_price IS 'Общая стоимость сделки';
COMMENT ON COLUMN economy.market_trade_history.commission IS 'Комиссия рынка';
COMMENT ON COLUMN economy.market_trade_history.seller_received IS 'Сумма, полученная продавцом (total_price - commission)';
COMMENT ON COLUMN economy.seller_reviews.rating IS 'Рейтинг продавца (1-5)';
COMMENT ON COLUMN economy.seller_reviews.is_positive IS 'Флаг положительного отзыва';
COMMENT ON COLUMN economy.seller_reviews.reported IS 'Флаг жалобы на отзыв';
COMMENT ON COLUMN economy.seller_statistics.average_rating IS 'Средний рейтинг продавца (0-5)';
COMMENT ON COLUMN economy.seller_statistics.positive_reviews IS 'Количество положительных отзывов';
COMMENT ON COLUMN economy.seller_statistics.negative_reviews IS 'Количество отрицательных отзывов';
COMMENT ON COLUMN economy.seller_statistics.items_sold IS 'Количество проданных предметов';
COMMENT ON COLUMN economy.seller_subscriptions.notify_on_new_listings IS 'Уведомлять о новых объявлениях';
COMMENT ON COLUMN economy.seller_subscriptions.notify_on_price_drops IS 'Уведомлять о снижении цен';


