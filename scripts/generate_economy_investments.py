import yaml
from pathlib import Path
from datetime import datetime

def create_investment_system():
    """Create comprehensive investment system document"""
    document = {
        'metadata': {
            'id': 'mechanics-economy-investments-system',
            'title': 'Mechanics - Economy Investments System',
            'document_type': 'mechanics',
            'category': 'economy',
            'subcategory': 'investments',
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
                'investments',
                'portfolio',
                'financial-planning'
            ],
            'topics': [
                'investment-mechanics',
                'portfolio-management',
                'risk-assessment',
                'financial-planning'
            ],
            'related_systems': [
                'economy-service',
                'portfolio-service',
                'market-service',
                'analytics-service'
            ],
            'related_documents': [
                {
                    'id': 'mechanics-economy-overview',
                    'relation': 'references'
                },
                {
                    'id': 'mechanics-economy-currency-exchange',
                    'relation': 'integrates'
                }
            ],
            'source': 'scripts/generate_economy_investments.py',
            'visibility': 'internal',
            'audience': [
                'design',
                'backend',
                'qa'
            ],
            'risk_level': 'high'
        },
        'executive_summary': {
            'objective': 'Создать комплексную систему инвестиций, позволяющую игрокам управлять портфелями, оценивать риски и получать доход от долгосрочных вложений в различные активы игры.',
            'scope': 'Полная система управления инвестициями с портфельным анализом, диверсификацией, оценкой рисков и интеграцией с глобальной экономикой.',
            'key_features': [
                'Многоуровневая система инвестиционных инструментов',
                'Портфельный анализ и оптимизация',
                'Оценка рисков и доходности',
                'Экономическая интеграция и влияние на мир'
            ],
            'economic_impact': 'Инвестиционная система становится фундаментом долгосрочной финансовой стратегии, влияя на экономические циклы и поведение игроков.'
        },
        'investment_instruments': {
            'traditional_assets': [
                {
                    'asset_class': 'stocks',
                    'description': 'Акции корпораций',
                    'instruments': ['Arasaka Corp', 'Biotechnica', 'Trauma Team', 'Maelstrom'],
                    'risk_level': 'medium',
                    'expected_return': '8-15% annually',
                    'liquidity': 'high'
                },
                {
                    'asset_class': 'bonds',
                    'description': 'Облигации корпораций и правительств',
                    'instruments': ['NUSA Treasury', 'Eurodollar Bonds', 'Corporate Debt'],
                    'risk_level': 'low',
                    'expected_return': '3-7% annually',
                    'liquidity': 'medium'
                },
                {
                    'asset_class': 'real_estate',
                    'description': 'Инвестиции в недвижимость',
                    'instruments': ['Urban Properties', 'Industrial Complexes', 'Luxury Apartments'],
                    'risk_level': 'medium',
                    'expected_return': '6-12% annually',
                    'liquidity': 'low'
                },
                {
                    'asset_class': 'commodities',
                    'description': 'Товарные активы',
                    'instruments': ['Chrome', 'Tech Components', 'Rare Metals'],
                    'risk_level': 'high',
                    'expected_return': '10-25% annually',
                    'liquidity': 'medium'
                }
            ],
            'alternative_assets': [
                {
                    'asset_class': 'venture_capital',
                    'description': 'Инвестиции в стартапы',
                    'instruments': ['Tech Startups', 'Biotech Ventures', 'Cyberware Companies'],
                    'risk_level': 'very_high',
                    'expected_return': '20-50% annually',
                    'liquidity': 'very_low'
                },
                {
                    'asset_class': 'derivatives',
                    'description': 'Производные финансовые инструменты',
                    'instruments': ['Options', 'Futures', 'Swaps'],
                    'risk_level': 'high',
                    'expected_return': 'variable',
                    'liquidity': 'high'
                },
                {
                    'asset_class': 'cryptocurrencies',
                    'description': 'Цифровые валюты',
                    'instruments': ['BTC', 'ETH', 'CyberCoin', 'Arasaka Token'],
                    'risk_level': 'very_high',
                    'expected_return': '15-40% annually',
                    'liquidity': 'high'
                },
                {
                    'asset_class': 'collectibles',
                    'description': 'Коллекционные предметы',
                    'instruments': ['Rare Cyberware', 'Vintage Tech', 'Art Pieces'],
                    'risk_level': 'medium',
                    'expected_return': '5-20% annually',
                    'liquidity': 'low'
                }
            ]
        },
        'portfolio_management': {
            'portfolio_construction': {
                'asset_allocation': [
                    {
                        'strategy': 'conservative',
                        'allocation': '60% bonds, 30% stocks, 10% cash',
                        'risk_level': 'low',
                        'expected_return': '4-6%'
                    },
                    {
                        'strategy': 'balanced',
                        'allocation': '50% stocks, 30% bonds, 20% alternatives',
                        'risk_level': 'medium',
                        'expected_return': '7-10%'
                    },
                    {
                        'strategy': 'aggressive',
                        'allocation': '70% stocks, 20% alternatives, 10% cash',
                        'risk_level': 'high',
                        'expected_return': '10-15%'
                    },
                    {
                        'strategy': 'speculative',
                        'allocation': '50% alternatives, 30% crypto, 20% high-risk stocks',
                        'risk_level': 'very_high',
                        'expected_return': '15-30%'
                    }
                ],
                'diversification_principles': [
                    'asset_class_diversity',
                    'geographic_spread',
                    'sector_diversification',
                    'time_horizon_matching'
                ]
            },
            'portfolio_analytics': {
                'performance_metrics': [
                    {
                        'metric': 'total_return',
                        'calculation': '(ending_value - beginning_value + income) / beginning_value',
                        'frequency': 'daily/weekly/monthly',
                        'benchmark': 'market_index'
                    },
                    {
                        'metric': 'sharpe_ratio',
                        'calculation': '(portfolio_return - risk_free_rate) / portfolio_volatility',
                        'frequency': 'monthly',
                        'interpretation': 'risk-adjusted return'
                    },
                    {
                        'metric': 'maximum_drawdown',
                        'calculation': 'peak_to_trough_decline',
                        'frequency': 'continuous',
                        'risk_measure': 'worst_case_scenario'
                    },
                    {
                        'metric': 'alpha',
                        'calculation': 'excess_return_over_benchmark',
                        'frequency': 'monthly',
                        'skill_measure': 'manager_skill'
                    }
                ],
                'risk_assessment': [
                    {
                        'risk_type': 'volatility_risk',
                        'measurement': 'standard_deviation',
                        'management': 'diversification'
                    },
                    {
                        'risk_type': 'market_risk',
                        'measurement': 'beta_coefficient',
                        'management': 'hedging_strategies'
                    },
                    {
                        'risk_type': 'liquidity_risk',
                        'measurement': 'bid_ask_spread',
                        'management': 'position_sizing'
                    },
                    {
                        'risk_type': 'credit_risk',
                        'measurement': 'credit_rating',
                        'management': 'quality_focus'
                    }
                ]
            }
        },
        'investment_strategies': {
            'active_strategies': [
                {
                    'strategy': 'value_investing',
                    'description': 'Инвестиции в недооцененные активы',
                    'approach': 'fundamental_analysis',
                    'time_horizon': 'long_term',
                    'risk_level': 'medium'
                },
                {
                    'strategy': 'growth_investing',
                    'description': 'Инвестиции в быстрорастущие компании',
                    'approach': 'earnings_potential',
                    'time_horizon': 'long_term',
                    'risk_level': 'high'
                },
                {
                    'strategy': 'momentum_investing',
                    'description': 'Следование рыночным трендам',
                    'approach': 'technical_analysis',
                    'time_horizon': 'medium_term',
                    'risk_level': 'high'
                },
                {
                    'strategy': 'contrarian_investing',
                    'description': 'Инвестиции против рынка',
                    'approach': 'sentiment_analysis',
                    'time_horizon': 'long_term',
                    'risk_level': 'very_high'
                }
            ],
            'passive_strategies': [
                {
                    'strategy': 'index_fund_investing',
                    'description': 'Следование рыночным индексам',
                    'approach': 'market_cap_weighting',
                    'time_horizon': 'long_term',
                    'risk_level': 'market_risk'
                },
                {
                    'strategy': 'buy_and_hold',
                    'description': 'Долгосрочное удержание активов',
                    'approach': 'minimal_trading',
                    'time_horizon': 'very_long_term',
                    'risk_level': 'low_medium'
                },
                {
                    'strategy': 'dollar_cost_averaging',
                    'description': 'Регулярные инвестиции фиксированных сумм',
                    'approach': 'systematic_investing',
                    'time_horizon': 'long_term',
                    'risk_level': 'medium'
                }
            ]
        },
        'investment_planning': {
            'goal_setting': [
                {
                    'goal_type': 'retirement_planning',
                    'time_horizon': '20-40 years',
                    'risk_tolerance': 'moderate',
                    'strategy_focus': 'long_term_growth'
                },
                {
                    'goal_type': 'wealth_accumulation',
                    'time_horizon': '5-15 years',
                    'risk_tolerance': 'moderate_high',
                    'strategy_focus': 'balanced_growth'
                },
                {
                    'goal_type': 'income_generation',
                    'time_horizon': 'ongoing',
                    'risk_tolerance': 'low_moderate',
                    'strategy_focus': 'dividend_yield'
                },
                {
                    'goal_type': 'speculative_gains',
                    'time_horizon': '1-3 years',
                    'risk_tolerance': 'high',
                    'strategy_focus': 'high_risk_high_reward'
                }
            ],
            'tax_optimization': [
                {
                    'strategy': 'tax_loss_harvesting',
                    'description': 'Продажа убыточных активов для компенсации прибыли',
                    'benefit': 'налоговые вычеты',
                    'timing': 'year_end_optimization'
                },
                {
                    'strategy': 'tax_advantaged_accounts',
                    'description': 'Инвестирование в защищенные от налогов счета',
                    'benefit': 'отсрочка налогов',
                    'types': ['retirement_accounts', 'education_savings']
                },
                {
                    'strategy': 'municipal_bonds',
                    'description': 'Инвестиции в муниципальные облигации',
                    'benefit': 'освобождение от налогов',
                    'considerations': 'низкая доходность'
                }
            ]
        },
        'market_integration': {
            'economic_events_impact': [
                {
                    'event_type': 'corporate_earnings',
                    'impact': 'direct_price_movement',
                    'timing': 'quarterly_reports',
                    'strategy': 'earnings_season_trading'
                },
                {
                    'event_type': 'economic_indicators',
                    'impact': 'sector_wide_effects',
                    'timing': 'monthly_quarterly',
                    'strategy': 'macro_economic_positioning'
                },
                {
                    'event_type': 'geopolitical_events',
                    'impact': 'market_volatility',
                    'timing': 'unpredictable',
                    'strategy': 'crisis_alpha_strategies'
                },
                {
                    'event_type': 'technological_breakthroughs',
                    'impact': 'disruptive_innovation',
                    'timing': 'major_announcements',
                    'strategy': 'innovation_investing'
                }
            ],
            'player_market_influence': [
                {
                    'influence_type': 'large_investor_impact',
                    'mechanism': 'price_movement_from_large_trades',
                    'considerations': 'market_manipulation_detection',
                    'balancing': 'position_limits'
                },
                {
                    'influence_type': 'investment_clubs',
                    'mechanism': 'coordinated_investment_groups',
                    'benefits': 'collective_intelligence',
                    'challenges': 'coordination_complexity'
                },
                {
                    'influence_type': 'investment_advice_market',
                    'mechanism': 'paid_analyst_services',
                    'quality_control': 'rating_systems',
                    'monetization': 'subscription_models'
                }
            ]
        },
        'advanced_features': {
            'algorithmic_investing': [
                {
                    'feature': 'robo_advisors',
                    'description': 'Автоматизированные инвестиционные советники',
                    'algorithms': 'modern_portfolio_theory',
                    'personalization': 'risk_assessment_based'
                },
                {
                    'feature': 'quantitative_strategies',
                    'description': 'Математические модели инвестирования',
                    'approaches': 'statistical_arbitrage',
                    'edge': 'speed_and_precision'
                },
                {
                    'feature': 'machine_learning_models',
                    'description': 'ИИ для предсказания рынков',
                    'techniques': 'deep_learning_forecasting',
                    'data_sources': 'multi_modal_market_data'
                }
            ],
            'social_investing': [
                {
                    'feature': 'investment_networks',
                    'description': 'Социальные сети инвесторов',
                    'benefits': 'information_sharing',
                    'risks': 'herd_behavior'
                },
                {
                    'feature': 'crowdfunding_platforms',
                    'description': 'Коллективное инвестирование',
                    'models': 'equity_crowdfunding',
                    'regulation': 'securities_law_compliance'
                },
                {
                    'feature': 'investment_competitions',
                    'description': 'Конкурсы портфельного управления',
                    'prizes': 'real_money_rewards',
                    'education': 'learning_opportunities'
                }
            ]
        },
        'technical_architecture': {
            'portfolio_engine': {
                'core_components': [
                    'portfolio_calculator',
                    'risk_assessment_engine',
                    'performance_analyzer',
                    'rebalancing_automator'
                ],
                'data_structures': [
                    'investment_portfolios',
                    'transaction_histories',
                    'market_data_cache',
                    'user_preferences'
                ],
                'processing_pipeline': [
                    'order_processing',
                    'settlement_engine',
                    'reporting_system',
                    'compliance_monitoring'
                ]
            },
            'analytics_platform': {
                'data_collection': [
                    'market_data_feeds',
                    'economic_indicators',
                    'social_sentiment',
                    'alternative_data'
                ],
                'analysis_engines': [
                    'technical_analysis',
                    'fundamental_analysis',
                    'quantitative_models',
                    'machine_learning'
                ],
                'reporting_system': [
                    'real_time_dashboards',
                    'periodic_reports',
                    'performance_attribution',
                    'risk_reports'
                ]
            },
            'scalability_considerations': {
                'performance_requirements': {
                    'portfolio_updates': '<100ms for real-time positions',
                    'analytics_queries': '<500ms for complex calculations',
                    'report_generation': '<2s for standard reports',
                    'concurrent_users': '10000+ simultaneous sessions'
                },
                'data_management': {
                    'time_series_storage': 'optimized for financial data',
                    'portfolio_optimization': 'real-time rebalancing algorithms',
                    'risk_calculations': 'parallel_processing_capable',
                    'cache_strategies': 'multi-level_caching'
                }
            }
        },
        'regulatory_compliance': {
            'investment_regulations': [
                {
                    'regulation': 'know_your_customer',
                    'requirement': 'investor_verification',
                    'implementation': 'identity_validation',
                    'monitoring': 'ongoing_compliance'
                },
                {
                    'regulation': 'suitability_rules',
                    'requirement': 'appropriate_investments',
                    'implementation': 'risk_assessment_questionnaires',
                    'enforcement': 'automated_screening'
                },
                {
                    'regulation': 'disclosure_requirements',
                    'requirement': 'transparent_reporting',
                    'implementation': 'detailed_prospectuses',
                    'frequency': 'regular_updates'
                },
                {
                    'regulation': 'anti_money_laundering',
                    'requirement': 'transaction_monitoring',
                    'implementation': 'behavioral_analytics',
                    'reporting': 'suspicious_activity_reports'
                }
            ],
            'game_specific_compliance': [
                {
                    'consideration': 'virtual_economy_balance',
                    'mechanism': 'inflation_controls',
                    'monitoring': 'economic_indicators',
                    'adjustment': 'dynamic_economic_policy'
                },
                {
                    'consideration': 'fair_play_principles',
                    'mechanism': 'wealth_distribution_limits',
                    'monitoring': 'gini_coefficient',
                    'adjustment': 'progressive_taxation'
                },
                {
                    'consideration': 'addiction_prevention',
                    'mechanism': 'investment_limits',
                    'monitoring': 'player_behavior_patterns',
                    'intervention': 'responsible_gaming_features'
                }
            ]
        },
        'future_expansions': {
            'emerging_trends': [
                'decentralized_finance',
                'tokenized_assets',
                'ai_powered_portfolio_management',
                'social_impact_investing'
            ],
            'technological_advancements': [
                'quantum_computing_optimization',
                'blockchain_settlement_systems',
                'real_time_market_microstructure',
                'predictive_behavioral_analytics'
            ],
            'market_evolution': [
                'global_market_integration',
                'alternative_asset_classes',
                'personalized_investment_products',
                'regulatory_technology_innovation'
            ]
        }
    }

    return document

def main():
    """Generate comprehensive investment system document"""
    document = create_investment_system()

    output_dir = Path('knowledge/mechanics/economy')
    output_file = output_dir / 'economy-investments-system.yaml'

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(document, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Generated comprehensive investment system document: {output_file}")

if __name__ == '__main__':
    main()
