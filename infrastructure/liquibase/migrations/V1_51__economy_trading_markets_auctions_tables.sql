-- Issue: #140875788
-- Economy Trading Markets Auctions Database Schema
-- Создание таблиц для экономической системы:
-- - Игровой рынок (market_listings)
-- - Аукционный дом (auction_lots)
-- - Фондовая биржа (stock_orders, stock_trades)
-- - Лог экономических операций (economy_operations_log)

-- Таблица объявлений на игровом рынке
CREATE TABLE IF NOT EXISTS economy.market_listings (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    item_id UUID NOT NULL,
    price DECIMAL(15, 2) NOT NULL CHECK (price > 0),
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'expired', 'cancelled')),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sold_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для market_listings
CREATE INDEX IF NOT EXISTS idx_market_listings_seller_id ON economy.market_listings(seller_id, status);
CREATE INDEX IF NOT EXISTS idx_market_listings_item_id ON economy.market_listings(item_id, status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_market_listings_status ON economy.market_listings(status, expires_at) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_market_listings_price ON economy.market_listings(price) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_market_listings_expires_at ON economy.market_listings(expires_at) WHERE status = 'active';

-- Таблица лотов аукционного дома
CREATE TABLE IF NOT EXISTS economy.auction_lots (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    seller_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    item_id UUID NOT NULL,
    start_price DECIMAL(15, 2) NOT NULL CHECK (start_price > 0),
    buyout_price DECIMAL(15, 2) CHECK (buyout_price IS NULL OR buyout_price >= start_price),
    current_bid DECIMAL(15, 2) NOT NULL DEFAULT 0,
    bidder_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    bid_count INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'expired', 'cancelled')),
    duration_hours INTEGER NOT NULL DEFAULT 24 CHECK (duration_hours > 0),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для auction_lots
CREATE INDEX IF NOT EXISTS idx_auction_lots_seller_id ON economy.auction_lots(seller_id, status);
CREATE INDEX IF NOT EXISTS idx_auction_lots_bidder_id ON economy.auction_lots(bidder_id) WHERE bidder_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_auction_lots_item_id ON economy.auction_lots(item_id, status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auction_lots_status ON economy.auction_lots(status, expires_at) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auction_lots_expires_at ON economy.auction_lots(expires_at) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auction_lots_current_bid ON economy.auction_lots(current_bid) WHERE status = 'active';

-- Таблица ордеров на бирже
CREATE TABLE IF NOT EXISTS economy.stock_orders (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    stock_symbol VARCHAR(20) NOT NULL,
    order_type VARCHAR(10) NOT NULL CHECK (order_type IN ('buy', 'sell')),
    order_side VARCHAR(10) NOT NULL CHECK (order_side IN ('market', 'limit')),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price DECIMAL(15, 2) CHECK (order_side = 'limit' AND price IS NOT NULL OR order_side = 'market' AND price IS NULL),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'filled', 'partially_filled', 'cancelled')),
    filled_quantity INTEGER NOT NULL DEFAULT 0 CHECK (filled_quantity >= 0 AND filled_quantity <= quantity),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    executed_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для stock_orders
CREATE INDEX IF NOT EXISTS idx_stock_orders_character_id ON economy.stock_orders(character_id, status);
CREATE INDEX IF NOT EXISTS idx_stock_orders_stock_symbol ON economy.stock_orders(stock_symbol, status) WHERE status IN ('pending', 'partially_filled');
CREATE INDEX IF NOT EXISTS idx_stock_orders_order_type ON economy.stock_orders(order_type, stock_symbol, status) WHERE status IN ('pending', 'partially_filled');
CREATE INDEX IF NOT EXISTS idx_stock_orders_price ON economy.stock_orders(price, stock_symbol) WHERE order_side = 'limit' AND status IN ('pending', 'partially_filled');
CREATE INDEX IF NOT EXISTS idx_stock_orders_status ON economy.stock_orders(status, created_at);

-- Таблица исполненных сделок на бирже
CREATE TABLE IF NOT EXISTS economy.stock_trades (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    buy_order_id UUID NOT NULL REFERENCES economy.stock_orders(id) ON DELETE CASCADE,
    sell_order_id UUID NOT NULL REFERENCES economy.stock_orders(id) ON DELETE CASCADE,
    stock_symbol VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price DECIMAL(15, 2) NOT NULL CHECK (price > 0),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для stock_trades
CREATE INDEX IF NOT EXISTS idx_stock_trades_buy_order_id ON economy.stock_trades(buy_order_id);
CREATE INDEX IF NOT EXISTS idx_stock_trades_sell_order_id ON economy.stock_trades(sell_order_id);
CREATE INDEX IF NOT EXISTS idx_stock_trades_stock_symbol ON economy.stock_trades(stock_symbol, executed_at DESC);
CREATE INDEX IF NOT EXISTS idx_stock_trades_executed_at ON economy.stock_trades(executed_at DESC);

-- Таблица лога всех экономических операций
CREATE TABLE IF NOT EXISTS economy.economy_operations_log (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    operation_type VARCHAR(30) NOT NULL CHECK (operation_type IN ('trade', 'market_purchase', 'auction_bid', 'stock_order')),
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    operation_data JSONB NOT NULL,
    result JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для economy_operations_log
CREATE INDEX IF NOT EXISTS idx_economy_operations_log_character_id ON economy.economy_operations_log(character_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_economy_operations_log_operation_type ON economy.economy_operations_log(operation_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_economy_operations_log_created_at ON economy.economy_operations_log(created_at DESC);

-- Обновление trade_sessions для соответствия архитектуре (если нужно)
-- Проверяем наличие полей и добавляем недостающие
DO $$ 
BEGIN
    -- Добавляем initiator_items и recipient_items, если их нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'economy' 
                   AND table_name = 'trade_sessions' 
                   AND column_name = 'initiator_items') THEN
        ALTER TABLE economy.trade_sessions 
        ADD COLUMN initiator_items JSONB,
        ADD COLUMN recipient_items JSONB,
        ADD COLUMN initiator_currency JSONB,
        ADD COLUMN recipient_currency JSONB;
    END IF;
END $$;

-- Комментарии к таблицам
COMMENT ON TABLE economy.market_listings IS 'Объявления на игровом рынке с фиксированными ценами';
COMMENT ON TABLE economy.auction_lots IS 'Лоты аукционного дома со ставками';
COMMENT ON TABLE economy.stock_orders IS 'Ордера на фондовой бирже (покупка/продажа акций)';
COMMENT ON TABLE economy.stock_trades IS 'Исполненные сделки на фондовой бирже';
COMMENT ON TABLE economy.economy_operations_log IS 'Лог всех экономических операций для аналитики и аудита';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.market_listings.status IS 'Статус объявления: active, sold, expired, cancelled';
COMMENT ON COLUMN economy.auction_lots.buyout_price IS 'Цена мгновенного выкупа (nullable, должна быть >= start_price)';
COMMENT ON COLUMN economy.auction_lots.current_bid IS 'Текущая максимальная ставка';
COMMENT ON COLUMN economy.auction_lots.bidder_id IS 'ID игрока с текущей максимальной ставкой';
COMMENT ON COLUMN economy.stock_orders.order_side IS 'Тип ордера: market (рыночный) или limit (лимитный)';
COMMENT ON COLUMN economy.stock_orders.price IS 'Цена для лимитного ордера (обязательна для limit, NULL для market)';
COMMENT ON COLUMN economy.stock_orders.filled_quantity IS 'Количество исполненных акций';
COMMENT ON COLUMN economy.economy_operations_log.operation_type IS 'Тип операции: trade, market_purchase, auction_bid, stock_order';
COMMENT ON COLUMN economy.economy_operations_log.operation_data IS 'JSONB данные операции для аналитики';
COMMENT ON COLUMN economy.economy_operations_log.result IS 'JSONB результат операции';


