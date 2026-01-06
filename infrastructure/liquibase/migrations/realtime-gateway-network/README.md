# Realtime Gateway Network Database Schema

Полная схема базы данных для сетевых метрик, телеметрии и мониторинга системы realtime-gateway в NECPGAME.

## 📡 Обзор

Realtime Gateway Network Database обеспечивает комплексный мониторинг и аналитику сетевой инфраструктуры MMOFPS игры с поддержкой UDP протокола, пространственного разделения, дельта-компрессии и адаптивных систем. База данных предназначена для обработки миллионов сетевых событий в реальном времени с минимальной задержкой.

## 🏗️ Архитектура базы данных

### Основные компоненты

1. **Сетевые сессии** (`network_sessions`) - Отслеживание подключений игроков
2. **Телеметрия сети** (`network_telemetry`) - Детальные метрики производительности
3. **Пространственное разделение** (`spatial_*`) - Метрики spatial grid и cell performance
4. **Компрессия данных** (`delta_compression_stats`) - Эффективность алгоритмов компрессии
5. **Протоколы UDP/WebSocket** (`udp_packet_stats`, `websocket_session_stats`) - Статистика пакетов
6. **Производительность** (`network_performance_metrics`) - Системные метрики в реальном времени
7. **Качество соединений** (`connection_quality_stats`) - Агрегированная статистика игроков
8. **Адаптивные системы** (`tick_rate_adaptation_metrics`) - Метрики адаптивного управления
9. **Ошибки и диагностика** (`network_error_logs`) - Логирование и анализ ошибок

## 📊 Схема таблиц

### Сетевые сессии

```sql
-- Активные и завершенные сетевые сессии
network_sessions
├── id (UUID PRIMARY KEY)
├── player_id (BIGINT) - Игрок
├── session_type ('udp_game', 'websocket_lobby', 'admin')
├── ip_address, user_agent (INET, TEXT) - Информация о клиенте
├── session_start, session_end (TIMESTAMP) - Временные метки
├── connection_quality (DECIMAL 0.0-1.0) - Качество соединения
├── bytes_sent, bytes_received (BIGINT) - Статистика трафика
├── disconnect_reason ('client', 'server', 'timeout', 'error')
└── session_metadata (JSONB) - Дополнительные данные

-- Детальная телеметрия производительности
network_telemetry
├── id (BIGSERIAL PRIMARY KEY)
├── session_id (UUID FOREIGN KEY)
├── player_id, telemetry_timestamp (BIGINT, TIMESTAMP)
├── rtt_ms, jitter_ms (INTEGER) - Задержка и джиттер
├── packet_loss_percentage (DECIMAL) - Потери пакетов
├── bandwidth_up_kbps, bandwidth_down_kbps (INTEGER) - Пропускная способность
├── connection_type ('wired', 'wifi', 'mobile') - Тип соединения
├── network_quality_score (DECIMAL 0.0-1.0) - Общий скор качества
└── telemetry_metadata (JSONB) - Дополнительные метрики
```

### Пространственное разделение

```sql
-- Метрики пространственных клеток
spatial_cell_metrics
├── id (BIGSERIAL PRIMARY KEY)
├── metric_timestamp (TIMESTAMP)
├── grid_cell_x, grid_cell_y (INTEGER) - Координаты клетки
├── active_players, max_players (INTEGER) - Загрузка игроками
├── cell_load_percentage (DECIMAL) - Процент загрузки
├── updates_per_second, packets_sent_per_second (DECIMAL) - Производительность
├── processing_time_us (INTEGER) - Время обработки
├── migration_events, boundary_crossings (INTEGER) - Миграции
└── cell_metadata (JSONB) - Метаданные клетки

-- Глобальные метрики spatial grid
spatial_grid_global_metrics
├── id (BIGSERIAL PRIMARY KEY)
├── metric_timestamp (TIMESTAMP)
├── total_active_cells, total_players (INTEGER)
├── average_cell_processing_time_us (INTEGER)
├── total_packets_sent_per_second (DECIMAL)
├── load_balance_score (DECIMAL 0-100) - Баланс нагрузки
├── migration_events_per_second (DECIMAL)
└── grid_metadata (JSONB)
```

### Компрессия и оптимизации

```sql
-- Статистика дельта-компрессии
delta_compression_stats
├── id (BIGSERIAL PRIMARY KEY)
├── compression_timestamp (TIMESTAMP)
├── original_bytes, compressed_bytes (BIGINT)
├── compression_ratio (DECIMAL) - Коэффициент сжатия
├── compression_time_us (INTEGER) - Время компрессии
├── algorithm_used ('coordinate_quantization', 'delta_encoding')
├── position_changed, rotation_changed, health_changed (BOOLEAN) - Измененные поля
├── batch_size (INTEGER) - Размер пакета
├── spatial_cell_x, spatial_cell_y (INTEGER) - Локация
└── compression_metadata (JSONB)

-- Статистика UDP пакетов
udp_packet_stats
├── id (BIGSERIAL PRIMARY KEY)
├── packet_timestamp (TIMESTAMP)
├── packet_type ('player_update', 'combat_action', 'spatial_update')
├── sequence_number, ack_sequence_number (INTEGER)
├── packet_size_bytes (INTEGER <= 1500)
├── send_attempts, delivery_confirmed (INTEGER, BOOLEAN)
├── source_player_id, target_player_count (BIGINT, INTEGER)
├── packet_priority ('low', 'normal', 'high', 'critical')
└── packet_metadata (JSONB)
```

### Производительность и мониторинг

```sql
-- Метрики производительности в реальном времени
network_performance_metrics
├── id (BIGSERIAL PRIMARY KEY)
├── metric_timestamp (TIMESTAMP)
├── active_udp_connections, active_websocket_connections (INTEGER)
├── average_packet_processing_time_us (INTEGER)
├── cpu_usage_percentage, memory_usage_percentage (DECIMAL)
├── network_bytes_in_per_second, network_bytes_out_per_second (DECIMAL)
├── packet_loss_rate_percentage (DECIMAL)
├── current_tick_rate_hz (DECIMAL)
├── tick_rate_adjustments (INTEGER)
└── performance_metadata (JSONB)

-- Агрегированная статистика качества соединений
connection_quality_stats
├── id (BIGSERIAL PRIMARY KEY)
├── stat_date (DATE)
├── player_id (BIGINT)
├── sessions_count, total_session_time_minutes (INTEGER)
├── average_connection_quality, average_rtt_ms (DECIMAL, INTEGER)
├── connection_drops, successful_reconnects (INTEGER)
├── overall_quality_score, network_stability_score (DECIMAL)
├── quality_trend ('improving', 'stable', 'degrading')
└── quality_metadata (JSONB)
```

### Адаптивные системы и ошибки

```sql
-- Метрики адаптивного управления тикрейтом
tick_rate_adaptation_metrics
├── id (BIGSERIAL PRIMARY KEY)
├── adaptation_timestamp (TIMESTAMP)
├── current_tick_rate_hz, target_tick_rate_hz (DECIMAL)
├── adaptation_reason ('player_count', 'network_load', 'cpu_usage')
├── active_players, network_load_percentage (INTEGER, DECIMAL)
├── adaptation_successful (BOOLEAN)
├── latency_change_ms, bandwidth_change_kbps (INTEGER)
└── adaptation_metadata (JSONB)

-- Логи сетевых ошибок
network_error_logs
├── id (BIGSERIAL PRIMARY KEY)
├── error_timestamp (TIMESTAMP)
├── error_type, error_severity ('NET_001', 'low', 'medium', 'high', 'critical')
├── player_id, session_id (BIGINT, UUID)
├── component_name ('udp_server', 'spatial_grid', 'delta_compression')
├── error_message, stack_trace (TEXT)
├── recovery_attempted, recovery_successful (BOOLEAN)
├── recovery_time_ms (INTEGER)
└── error_metadata (JSONB)
```

## 🔍 Оптимизации производительности

### Индексы

- **Временные индексы**: Для данных последних часов/дней с автоматическим удалением старых
- **Составные индексы**: Для комплексных запросов (player + timestamp + metric)
- **Частичные индексы**: Только для активных сессий и недавних метрик
- **JSONB индексы**: GIN индексы для метаданных и конфигураций
- **Партиционирование**: По дням/часам для high-volume таблиц

### Материализованные представления

```sql
-- Производительность игроков
player_network_performance
├── player_id, total_sessions
├── avg_session_duration_minutes, avg_rtt_ms
├── network_tier ('excellent', 'good', 'fair', 'poor')
└── suspicious_activity_count, active_disputes

-- Производительность регионов
network_region_performance
├── region_code, region_name, continent
├── total_players, avg_rtt_ms, avg_packet_loss
├── connection_success_rate, quality_score_avg
└── region_performance_tier ('excellent', 'good', 'fair', 'poor')

-- Эффективность компрессии
compression_algorithm_efficiency
├── date, algorithm_used
├── avg_compression_ratio, avg_compression_time_us
├── total_original_bytes, total_compressed_bytes
├── overall_compression_efficiency_percentage
└── efficiency_tier ('excellent', 'good', 'fair', 'poor')

-- Производительность spatial grid
spatial_grid_performance
├── date, grid_cell_x, grid_cell_y
├── avg_active_players, avg_load_percentage
├── avg_updates_per_second, avg_processing_time_us
├── overload_events, underutilized_events
└── utilization_status ('optimal', 'overloaded', 'underutilized')

-- Паттерны ошибок
network_error_patterns
├── date, error_type, component_name
├── error_count, affected_players
├── recovery_success_rate, most_common_message
└── frequency_severity ('critical', 'high', 'medium', 'low')

-- Dashboard производительности
network_performance_dashboard
├── generated_at (реальное время)
├── active_connections, avg_rtt_last_5min
├── avg_cpu_last_5min, overloaded_cells
├── avg_compression_ratio, high_severity_errors
└── overall_health_score (0-100)
```

### Функции производительности

```sql
-- Метрики здоровья сети в реальном времени
get_network_health_metrics(observation_window_minutes)

-- Анализ трендов соединений игрока
analyze_player_connection_trends(player_id, days_back)

-- Рекомендации оптимизации spatial grid
get_spatial_optimization_recommendations()

-- Обновление всех аналитических представлений
refresh_network_analytics()

-- Очистка старых данных телеметрии
cleanup_old_network_telemetry()

-- Агрегация ежедневной статистики
aggregate_daily_network_stats(target_date)

-- Валидация целостности данных
validate_network_data_integrity()

-- Выполнение планового обслуживания
perform_network_maintenance()
```

## 🚀 Масштабируемость

### Партиционирование

- **network_telemetry**: Партиционирование по дням (90 дней хранения)
- **delta_compression_stats**: Партиционирование по дням (60 дней хранения)
- **udp_packet_stats**: Партиционирование по часам (7 дней хранения)
- **spatial_cell_metrics**: Партиционирование по дням (30 дней хранения)

### Репликация и кеширование

- **Read replicas**: Для аналитических запросов и исторических данных
- **Redis caching**: Для real-time метрик и сессионных данных
- **Time-series optimization**: Специфические оптимизации для временных рядов
- **Compression**: Автоматическое сжатие старых партиций

### Производительность

- **Real-time queries**: <10ms для текущих метрик
- **Historical analytics**: <100ms для агрегированных данных
- **Bulk operations**: Поддержка 100k+ вставок в секунду
- **Concurrent connections**: 10k+ одновременных сессий телеметрии

## 🔒 Безопасность и приватность

### Защита данных

- **IP маскировка**: Анонимизация IP адресов для аналитики
- **PII minimization**: Минимизация персональных данных в логах
- **Access controls**: Ролевая модель доступа к метрикам
- **Encryption**: Шифрование чувствительных сетевых данных

### Аудит и compliance

- **Complete audit trail**: Все изменения сетевых настроек логируются
- **Data retention policies**: Автоматическое удаление старых данных
- **Anomaly detection**: AI-powered обнаружение необычных паттернов
- **Privacy compliance**: Соответствие GDPR и другим стандартам

## 📈 Мониторинг и алертинг

### Метрики здоровья системы

- **Connection metrics**: Активные соединения, качество подключений
- **Performance metrics**: CPU, память, сеть, задержки
- **Business metrics**: Успешность доставки, пользовательский опыт
- **Security metrics**: Обнаружение атак, аномалий трафика

### Автоматизированные алерты

- **Threshold-based**: Предупреждения при превышении порогов
- **Trend-based**: Обнаружение негативных трендов
- **Predictive**: Предупреждения о потенциальных проблемах
- **Auto-remediation**: Автоматическое исправление известных проблем

## 🔧 Техническое обслуживание

### Автоматизированные процедуры

```sql
-- Ежедневная очистка (утро)
SELECT cleanup_old_network_telemetry();
SELECT archive_old_compression_stats();

-- Ежечасная агрегация
SELECT aggregate_daily_network_stats();

-- Ежеминутное обновление
SELECT refresh_network_analytics();

-- Еженедельная валидация
SELECT validate_network_data_integrity();
```

### Производительность обслуживания

- **Zero-downtime maintenance**: Все операции без остановки сервиса
- **Online index rebuilds**: Перестройка индексов без блокировки
- **Partition rotation**: Автоматическая ротация партиций
- **Automated optimization**: Самонастройка на основе нагрузки

## 📊 Аналитика и BI

### Real-time dashboards

- **Network health dashboard**: Общее состояние сетевой инфраструктуры
- **Player experience dashboard**: Метрики качества подключения игроков
- **Geographic performance**: Производительность по регионам
- **Compression analytics**: Эффективность алгоритмов компрессии

### Business intelligence

- **Player segmentation**: Кластеризация игроков по сетевым характеристикам
- **Churn prediction**: Предсказание оттока на основе сетевых проблем
- **Capacity planning**: Планирование ресурсов на основе трендов
- **A/B testing**: Сравнение сетевых конфигураций

## 🌍 Глобальная инфраструктура

### Географическое распределение

- **Regional databases**: Локальные реплики для низкой задержки
- **Global aggregation**: Централизованная аналитика всех регионов
- **Cross-region failover**: Автоматическое переключение при сбоях
- **Latency optimization**: Геораспределенная доставка контента

### Многорегиональная архитектура

- **Active-active**: Все регионы активны одновременно
- **Data consistency**: Eventual consistency для метрик
- **Regional autonomy**: Независимое масштабирование регионов
- **Global coordination**: Централизованное управление политиками

Эта схема обеспечивает enterprise-grade monitoring и analytics для global real-time gaming infrastructure с поддержкой миллионов одновременных игроков и комплексным анализом сетевой производительности.
