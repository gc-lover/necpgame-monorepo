import yaml
from pathlib import Path
from datetime import datetime

def create_currency_exchange_system():
    """Create comprehensive currency exchange system document"""
    document = {
        'metadata': {
            'id': 'mechanics-economy-currency-exchange-system',
            'title': 'Mechanics - Economy Currency Exchange System',
            'document_type': 'mechanics',
            'category': 'economy',
            'subcategory': 'currency-exchange',
            'status': 'draft',
            'version': '1.0.0',
            'last_updated': datetime.now().strftime('%Y-%m-%dT%H:%M:%SZ'),
            'concept_approved': False,
            'concept_reviewed_at': '',
            'owners': [
                {
                    'role': 'game_designer',
                    'contact': 'design@necp.game'
                }
            ],
            'tags': [
                'economy',
                'currency',
                'exchange',
                'trading'
            ],
            'topics': [
                'currency-markets',
                'exchange-mechanics',
                'financial-trading',
                'economic-simulation'
            ],
            'related_systems': [
                'economy-service',
                'trading-service',
                'market-service',
                'player-wallet-service'
            ],
            'related_documents': [
                {
                    'id': 'mechanics-economy-overview',
                    'relation': 'references'
                },
                {
                    'id': 'mechanics-economy-trading',
                    'relation': 'implements'
                }
            ],
            'source': 'scripts/generate_economy_currency_exchange.py',
            'visibility': 'internal',
            'audience': [
                'design',
                'backend',
                'qa'
            ],
            'risk_level': 'high'
        },
        'executive_summary': {
            'objective': 'Создать комплексную систему валютной биржи, где игроки могут торговать различными валютами, влияя на экономику и получая прибыль от спекуляций.',
            'scope': 'Полная система обмена валют с ордерами, графиками, анализом и интеграцией с глобальной экономикой.',
            'key_features': [
                'Многовалютная торговая платформа',
                'Реалистичные рыночные механизмы',
                'Аналитика и графики',
                'Экономическое влияние на мир'
            ],
            'economic_impact': 'Валютная биржа становится центром финансовой активности, влияя на инфляцию и экономические циклы.'
        },
        'currency_types': {
            'game_currencies': [
                {
                    'currency': 'EDollar',
                    'type': 'fiat',
                    'description': 'Основная валюта Новой Америки',
                    'volatility': 'низкая',
                    'backing': 'экономика региона'
                },
                {
                    'currency': 'EuroDollar',
                    'type': 'fiat',
                    'description': 'Валюта Европейской конфедерации',
                    'volatility': 'средняя',
                    'backing': 'политическая стабильность'
                },
                {
                    'currency': 'Nuyen',
                    'type': 'corporate',
                    'description': 'Корпоративная валюта Арасак',
                    'volatility': 'высокая',
                    'backing': 'корпоративная мощь'
                },
                {
                    'currency': 'BTC',
                    'type': 'crypto',
                    'description': 'Биткойн - децентрализованная валюта',
                    'volatility': 'очень высокая',
                    'backing': 'математические алгоритмы'
                },
                {
                    'currency': 'Eth',
                    'type': 'crypto',
                    'description': 'Эфир - смарт-контрактная валюта',
                    'volatility': 'высокая',
                    'backing': 'децентрализованные приложения'
                }
            ],
            'exchange_mechanics': {
                'cross_currency_pairs': [
                    'EDollar/EuroDollar',
                    'EDollar/Nuyen',
                    'EuroDollar/Nuyen',
                    'BTC/EDollar',
                    'Eth/BTC'
                ],
                'exchange_rates': {
                    'real_time_updates': 'каждые 30 секунд',
                    'spread_calculation': '0.1-2.0% в зависимости от ликвидности',
                    'commission_structure': '0.05-0.5% от объема'
                }
            }
        },
        'trading_mechanics': {
            'order_types': [
                {
                    'type': 'market_order',
                    'description': 'Немедленное исполнение по текущей цене',
                    'use_case': 'быстрая торговля',
                    'advantages': 'гарантированное исполнение',
                    'disadvantages': 'непредсказуемая цена'
                },
                {
                    'type': 'limit_order',
                    'description': 'Исполнение по заданной или лучшей цене',
                    'use_case': 'контроль цены',
                    'advantages': 'предсказуемость',
                    'disadvantages': 'может не исполниться'
                },
                {
                    'type': 'stop_order',
                    'description': 'Автоматическое исполнение при достижении уровня',
                    'use_case': 'ограничение убытков',
                    'advantages': 'автоматизация',
                    'disadvantages': 'slippage возможен'
                },
                {
                    'type': 'trailing_stop',
                    'description': 'Динамический стоп с отслеживанием цены',
                    'use_case': 'защита прибыли',
                    'advantages': 'адаптивность',
                    'disadvantages': 'сложность настройки'
                }
            ],
            'trading_session': {
                'market_hours': '24/7 с перерывами на обслуживание',
                'liquidity_providers': 'игроки + NPC трейдеры',
                'volume_tracking': 'реальное время',
                'price_discovery': 'order book + matching engine'
            }
        },
        'market_analysis_tools': {
            'technical_indicators': [
                {
                    'indicator': 'moving_averages',
                    'description': 'Скользящие средние для трендов',
                    'parameters': 'SMA(20), EMA(50), WMA(100)',
                    'use_case': 'определение направления тренда'
                },
                {
                    'indicator': 'rsi',
                    'description': 'Индекс относительной силы',
                    'parameters': 'период 14, уровни 30/70',
                    'use_case': 'перекупленность/перепроданность'
                },
                {
                    'indicator': 'macd',
                    'description': 'Схождение/расхождение скользящих средних',
                    'parameters': '12/26/9',
                    'use_case': 'сигналы разворота тренда'
                },
                {
                    'indicator': 'bollinger_bands',
                    'description': 'Полосы Боллинджера',
                    'parameters': 'период 20, отклонение 2',
                    'use_case': 'волатильность и уровни поддержки'
                }
            ],
            'chart_types': [
                {
                    'type': 'candlestick',
                    'description': 'Японские свечи',
                    'data': 'OHLC + объем',
                    'analysis': 'паттерны и формации'
                },
                {
                    'type': 'line_chart',
                    'description': 'Линейный график',
                    'data': 'закрывающие цены',
                    'analysis': 'долгосрочные тренды'
                },
                {
                    'type': 'bar_chart',
                    'description': 'Барный график',
                    'data': 'OHLC',
                    'analysis': 'детальный анализ'
                }
            ],
            'market_data': {
                'real_time_feeds': 'цены, объемы, ордербук',
                'historical_data': 'до 2 лет назад',
                'news_integration': 'экономические новости влияют на цены',
                'social_sentiment': 'мнение сообщества'
            }
        },
        'economic_integration': {
            'currency_influence': {
                'inflation_control': {
                    'mechanism': 'валютные интервенции',
                    'trigger': 'отклонение от таргета',
                    'effect': 'стабилизация цен'
                },
                'economic_events': {
                    'corporate_earnings': 'влияют на корпоративные валюты',
                    'political_events': 'геополитические факторы',
                    'technological_breakthroughs': 'криптовалюты',
                    'market_sentiment': 'общее настроение'
                }
            },
            'player_economy_impact': {
                'wealth_distribution': 'валютная торговля влияет на классы',
                'investment_opportunities': 'новые способы заработка',
                'risk_reward_balance': 'спекуляция vs стабильность',
                'economic_participation': 'активность влияет на экономику'
            }
        },
        'risk_management': {
            'trading_limits': [
                {
                    'limit_type': 'position_size',
                    'purpose': 'ограничение риска',
                    'calculation': 'процент от портфеля',
                    'enforcement': 'автоматический стоп'
                },
                {
                    'limit_type': 'daily_loss_limit',
                    'purpose': 'защита капитала',
                    'calculation': 'максимальный убыток в день',
                    'enforcement': 'принудительное закрытие позиций'
                },
                {
                    'limit_type': 'leverage_limits',
                    'purpose': 'контроль плеча',
                    'calculation': 'на основе опыта трейдера',
                    'enforcement': 'максимальное плечо'
                }
            ],
            'market_protection': [
                {
                    'mechanism': 'circuit_breakers',
                    'trigger': 'большое ценовое движение',
                    'effect': 'временная остановка торгов',
                    'recovery': 'постепенное возобновление'
                },
                {
                    'mechanism': 'volatility_controls',
                    'trigger': 'экстремальная волатильность',
                    'effect': 'расширенные спреды',
                    'recovery': 'нормализация при стабилизации'
                }
            ]
        },
        'advanced_features': {
            'algorithmic_trading': [
                {
                    'strategy': 'arbitrage_bots',
                    'description': 'Арбитраж между рынками',
                    'complexity': 'высокая',
                    'profitability': 'низкая, но стабильная'
                },
                {
                    'strategy': 'trend_following',
                    'description': 'Следование трендам',
                    'complexity': 'средняя',
                    'profitability': 'переменная'
                },
                {
                    'strategy': 'mean_reversion',
                    'description': 'Возвращение к среднему',
                    'complexity': 'высокая',
                    'profitability': 'высокая при правильной настройке'
                }
            ],
            'social_features': [
                {
                    'feature': 'trading_signals',
                    'description': 'Сигналы от опытных трейдеров',
                    'monetization': 'премиум подписка',
                    'community': 'обмен стратегиями'
                },
                {
                    'feature': 'portfolio_sharing',
                    'description': 'Демонстрация портфелей',
                    'monetization': 'реклама стратегий',
                    'community': 'обучение и вдохновение'
                }
            ]
        },
        'technical_architecture': {
            'trading_engine': {
                'order_matching': 'high-frequency matching engine',
                'price_calculation': 'real-time price discovery',
                'settlement_system': 'instant settlement',
                'risk_engine': 'real-time risk monitoring'
            },
            'data_infrastructure': {
                'time_series_database': 'финансовые временные ряды',
                'order_book_storage': 'высокопроизводительное хранение',
                'analytics_engine': 'комплексный анализ данных',
                'reporting_system': 'детальная аналитика'
            },
            'scalability_requirements': {
                'concurrent_users': '10000+ активных трейдеров',
                'orders_per_second': '100000+ ордеров',
                'data_retention': '2+ года истории',
                'latency_target': '<50ms для основных операций'
            }
        },
        'balancing_framework': {
            'player_progression': [
                {
                    'level': 'beginner',
                    'features': 'базовые инструменты, демо-счет',
                    'limits': 'маленькие суммы, низкое плечо',
                    'education': 'обучающие материалы'
                },
                {
                    'level': 'intermediate',
                    'features': 'расширенные инструменты',
                    'limits': 'средние суммы, среднее плечо',
                    'education': 'продвинутые стратегии'
                },
                {
                    'level': 'advanced',
                    'features': 'все инструменты, алготрейдинг',
                    'limits': 'большие суммы, высокое плечо',
                    'education': 'профессиональные инструменты'
                }
            ],
            'economic_stability': [
                {
                    'mechanism': 'liquidity_provision',
                    'purpose': 'обеспечение ликвидности',
                    'implementation': 'market makers',
                    'monitoring': 'объем торгов'
                },
                {
                    'mechanism': 'volatility_controls',
                    'purpose': 'стабилизация рынка',
                    'implementation': 'динамические спреды',
                    'monitoring': 'волатильность индекса'
                },
                {
                    'mechanism': 'participation_incentives',
                    'purpose': 'привлечение трейдеров',
                    'implementation': 'бонусы и награды',
                    'monitoring': 'активность пользователей'
                }
            ]
        },
        'future_expansions': {
            'emerging_features': [
                'decentralized_exchange',
                'nft_integration',
                'cross_game_trading',
                'ai_trading_assistants'
            ],
            'integration_opportunities': [
                'real_world_data_feeds',
                'social_media_sentiment',
                'geopolitical_events',
                'macroeconomic_indicators'
            ],
            'research_directions': [
                'behavioral_finance_models',
                'market_microstructure_analysis',
                'high_frequency_trading_ethics',
                'cryptocurrency_economics'
            ]
        }
    }

    return document

def main():
    """Generate comprehensive currency exchange system document"""
    document = create_currency_exchange_system()

    output_dir = Path('knowledge/mechanics/economy')
    output_file = output_dir / 'economy-currency-exchange-system.yaml'

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(document, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Generated comprehensive currency exchange system document: {output_file}")

if __name__ == '__main__':
    main()
