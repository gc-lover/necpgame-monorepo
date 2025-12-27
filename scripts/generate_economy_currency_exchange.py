import yaml
from pathlib import Path
from datetime import datetime

def create_currency_exchange_system():
    """Create comprehensive currency exchange system"""
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
                },
                {
                    'role': 'backend_lead',
                    'contact': 'backend@necp.game'
                }
            ],
            'tags': [
                'economy',
                'currency',
                'exchange',
                'trading',
                'finance'
            ],
            'topics': [
                'gameplay',
                'economy',
                'currency-management',
                'player-trading'
            ],
            'related_systems': [
                'economy-service',
                'market-service',
                'player-inventory-service',
                'banking-service'
            ],
            'related_documents': [
                {
                    'id': 'mechanics-economy-overview',
                    'relation': 'references'
                },
                {
                    'id': 'mechanics-economy-stock-exchange',
                    'relation': 'integrates'
                }
            ],
            'source': 'scripts/generate_economy_currency_exchange.py',
            'visibility': 'internal',
            'audience': [
                'design',
                'systems',
                'backend',
                'qa'
            ],
            'risk_level': 'high'
        },
        'review': {
            'chain': [
                {
                    'role': 'game_designer',
                    'reviewer': '',
                    'reviewed_at': '',
                    'status': 'pending'
                },
                {
                    'role': 'backend_lead',
                    'reviewer': '',
                    'reviewed_at': '',
                    'status': 'pending'
                }
            ],
            'next_actions': []
        },
        'summary': {
            'problem': 'Необходимо разработать комплексную систему обмена валюты, которая позволит игрокам конвертировать между различными валютами игры, влияя на экономику и создавая новые возможности для торговли.',
            'goal': 'Создать глубокую и динамичную систему обмена валюты, которая добавит новый слой экономической стратегии и интеграции в игру.',
            'essence': 'Игроки могут обменивать различные игровые валюты на биржах, через NPC-трейдеров или peer-to-peer, с учетом рыночных колебаний, комиссий и глобальных экономических событий.',
            'key_points': [
                'Множественные валюты с различными свойствами',
                'Динамические обменные курсы',
                'Различные методы обмена',
                'Интеграция с глобальной экономикой'
            ]
        },
        'details': {
            'description': 'Подробное описание системы обмена валюты.',
            'currency_types': [
                {
                    'currency': 'Credits',
                    'description': 'Основная универсальная валюта для повседневных транзакций',
                    'properties': {
                        'stability': 'medium',
                        'liquidity': 'high',
                        'acceptance': 'universal',
                        'inflation_rate': 'controlled'
                    },
                    'use_cases': [
                        'general_purchases',
                        'service_payments',
                        'basic_trading'
                    ]
                },
                {
                    'currency': 'CryptoCoins',
                    'description': 'Цифровая валюта с высокой волатильностью',
                    'properties': {
                        'stability': 'low',
                        'liquidity': 'variable',
                        'acceptance': 'tech_focused',
                        'inflation_rate': 'deflationary'
                    },
                    'use_cases': [
                        'speculative_trading',
                        'black_market_deals',
                        'tech_upgrades'
                    ]
                },
                {
                    'currency': 'FactionTokens',
                    'description': 'Валюты, привязанные к конкретным фракциям',
                    'properties': {
                        'stability': 'high',
                        'liquidity': 'faction_limited',
                        'acceptance': 'faction_specific',
                        'inflation_rate': 'faction_controlled'
                    },
                    'use_cases': [
                        'faction_services',
                        'reputation_boosts',
                        'exclusive_access'
                    ]
                },
                {
                    'currency': 'RareMetals',
                    'description': 'Физические ценные металлы для долгосрочных инвестиций',
                    'properties': {
                        'stability': 'high',
                        'liquidity': 'low',
                        'acceptance': 'industrial',
                        'inflation_rate': 'stable'
                    },
                    'use_cases': [
                        'industrial_crafting',
                        'wealth_preservation',
                        'large_investments'
                    ]
                },
                {
                    'currency': 'EventCurrency',
                    'description': 'Специальная валюта для временных событий',
                    'properties': {
                        'stability': 'variable',
                        'liquidity': 'event_limited',
                        'acceptance': 'event_specific',
                        'inflation_rate': 'event_driven'
                    },
                    'use_cases': [
                        'event_rewards',
                        'seasonal_trading',
                        'limited_time_offers'
                    ]
                }
            ],
            'exchange_mechanisms': [
                {
                    'mechanism': 'CentralExchange',
                    'description': 'Централизованная биржа с фиксированными курсами',
                    'advantages': 'predictable_rates, high_liquidity',
                    'disadvantages': 'fixed_rates, exchange_fees',
                    'accessibility': 'all_players',
                    'fee_structure': '0.5% per_transaction'
                },
                {
                    'mechanism': 'PeerToPeerTrading',
                    'description': 'Прямой обмен между игроками',
                    'advantages': 'flexible_rates, no_fees',
                    'disadvantages': 'finding_partners, trust_issues',
                    'accessibility': 'social_players',
                    'fee_structure': 'none'
                },
                {
                    'mechanism': 'NPCMerchants',
                    'description': 'Обмен через NPC-трейдеров',
                    'advantages': 'immediate_exchange, guaranteed_rates',
                    'disadvantages': 'unfavorable_rates, limited_amounts',
                    'accessibility': 'all_players',
                    'fee_structure': '2-5% markup'
                },
                {
                    'mechanism': 'AutomatedTradingBots',
                    'description': 'AI-управляемые торговые боты',
                    'advantages': '24/7_availability, algorithmic_efficiency',
                    'disadvantages': 'market_manipulation_risk, complex_interface',
                    'accessibility': 'advanced_players',
                    'fee_structure': '0.1% per_transaction'
                },
                {
                    'mechanism': 'AuctionHouseCurrency',
                    'description': 'Специализированные аукционы валюты',
                    'advantages': 'market_price_discovery, bulk_trading',
                    'disadvantages': 'auction_delays, minimum_lots',
                    'accessibility': 'serious_traders',
                    'fee_structure': '1% listing_fee + 2% success_fee'
                }
            ],
            'exchange_rate_dynamics': [
                {
                    'factor': 'SupplyDemandBalance',
                    'description': 'Классическая экономика предложения и спроса',
                    'impact': 'direct_rate_adjustment',
                    'update_frequency': 'real_time',
                    'volatility': 'medium'
                },
                {
                    'factor': 'EconomicEvents',
                    'description': 'Глобальные события влияют на курсы',
                    'impact': 'sudden_rate_changes',
                    'update_frequency': 'event_based',
                    'volatility': 'high'
                },
                {
                    'factor': 'FactionRelations',
                    'description': 'Отношения фракций влияют на фракционные валюты',
                    'impact': 'regional_rate_modifiers',
                    'update_frequency': 'daily',
                    'volatility': 'medium'
                },
                {
                    'factor': 'PlayerSpeculation',
                    'description': 'Действия игроков влияют на волатильность',
                    'impact': 'short_term_fluctuations',
                    'update_frequency': 'continuous',
                    'volatility': 'variable'
                },
                {
                    'factor': 'SeasonalVariations',
                    'description': 'Сезонные колебания спроса',
                    'impact': 'predictable_cycles',
                    'update_frequency': 'weekly',
                    'volatility': 'low'
                }
            ],
            'risk_management': [
                {
                    'risk': 'ExchangeRateVolatility',
                    'mitigation': 'hedging_instruments, stop_loss_orders',
                    'insurance': 'rate_guarantee_options',
                    'education': 'market_trading_tutorials'
                },
                {
                    'risk': 'CounterpartyRisk',
                    'mitigation': 'escrow_services, reputation_systems',
                    'insurance': 'exchange_insurance_fund',
                    'education': 'safe_trading_practices'
                },
                {
                    'risk': 'LiquidityRisk',
                    'mitigation': 'multiple_exchange_options, market_makers',
                    'insurance': 'central_bank_interventions',
                    'education': 'liquidity_management'
                },
                {
                    'risk': 'RegulatoryRisk',
                    'mitigation': 'compliance_monitoring, fair_play_enforcement',
                    'insurance': 'regulatory_fines_fund',
                    'education': 'trading_regulations'
                }
            ],
            'advanced_features': [
                {
                    'feature': 'CurrencyDerivatives',
                    'description': 'Фьючерсы, опционы и другие деривативы',
                    'benefits': 'risk_hedging, speculation_opportunities',
                    'complexity': 'high',
                    'accessibility': 'expert_traders'
                },
                {
                    'feature': 'CurrencyArbitrage',
                    'description': 'Эксплуатация разницы курсов между биржами',
                    'benefits': 'guaranteed_profits, market_efficiency',
                    'complexity': 'medium',
                    'accessibility': 'skilled_traders'
                },
                {
                    'feature': 'CurrencySwaps',
                    'description': 'Обмен валютой на определенный срок',
                    'benefits': 'temporary_access, interest_opportunities',
                    'complexity': 'medium',
                    'accessibility': 'institutional_traders'
                },
                {
                    'feature': 'CurrencyPegging',
                    'description': 'Привязка валют к реальным активам',
                    'benefits': 'stability, predictability',
                    'complexity': 'low',
                    'accessibility': 'conservative_investors'
                }
            ],
            'technical_implementation': {
                'microservices': [
                    'currency-exchange-service',
                    'market-data-service',
                    'trading-engine-service',
                    'risk-management-service'
                ],
                'database_schemas': [
                    'currency_rates',
                    'exchange_orders',
                    'trading_history',
                    'market_indicators'
                ],
                'api_endpoints': [
                    '/exchange/rates',
                    '/exchange/order',
                    '/exchange/history',
                    '/market/analysis'
                ],
                'performance_requirements': {
                    'order_processing': '<100ms',
                    'rate_updates': '<1s',
                    'concurrent_trades': '1000+/s',
                    'data_consistency': 'strong_consistency'
                }
            }
        }
    }

    return document

def main():
    """Generate comprehensive currency exchange system"""
    document = create_currency_exchange_system()

    output_dir = Path('knowledge/mechanics/economy')
    output_file = output_dir / 'currency-exchange-system.yaml'

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(document, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Generated comprehensive currency exchange system: {output_file}")

if __name__ == '__main__':
    main()