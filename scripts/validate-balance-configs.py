#!/usr/bin/env python3
"""
Balance Configuration Validation Script
Issue: #2227 - Game Balance validation for quest, combat, and economy balance systems
Agent: GameBalance - Automated validation of balance configurations for consistency and mathematical correctness

Usage:
    python scripts/validate-balance-configs.py
    python scripts/validate-balance-configs.py --config quest
    python scripts/validate-balance-configs.py --config combat
    python scripts/validate-balance-configs.py --config economy
"""

import yaml
import json
import sys
import os
from typing import Dict, List, Any, Tuple
from dataclasses import dataclass
from pathlib import Path

@dataclass
class ValidationResult:
    is_valid: bool
    errors: List[str]
    warnings: List[str]
    metrics: Dict[str, Any]

class BalanceValidator:
    def __init__(self):
        self.config_dir = Path("config/balance")
        self.errors = []
        self.warnings = []
        self.metrics = {}

    def validate_all_configs(self) -> ValidationResult:
        """Validate all balance configuration files"""
        configs = {
            'quest': 'quest-balance-system.yaml',
            'combat': 'combat-balance-system.yaml',
            'economy': 'economy-balance-system.yaml'
        }

        all_valid = True
        all_errors = []
        all_warnings = []
        all_metrics = {}

        for config_type, filename in configs.items():
            print(f"Validating {config_type} balance configuration...")

            result = self.validate_config(config_type, filename)
            all_valid &= result.is_valid
            all_errors.extend(result.errors)
            all_warnings.extend(result.warnings)
            all_metrics.update({f"{config_type}_{k}": v for k, v in result.metrics.items()})

        return ValidationResult(all_valid, all_errors, all_warnings, all_metrics)

    def validate_config(self, config_type: str, filename: str) -> ValidationResult:
        """Validate a specific configuration file"""
        self.errors = []
        self.warnings = []
        self.metrics = {}

        config_path = self.config_dir / filename
        if not config_path.exists():
            self.errors.append(f"Configuration file not found: {config_path}")
            return ValidationResult(False, self.errors, self.warnings, self.metrics)

        try:
            with open(config_path, 'r', encoding='utf-8') as f:
                config = yaml.safe_load(f)
        except Exception as e:
            self.errors.append(f"Failed to parse {filename}: {str(e)}")
            return ValidationResult(False, self.errors, self.warnings, self.metrics)

        # Validate based on configuration type
        if config_type == 'quest':
            self.validate_quest_config(config)
        elif config_type == 'combat':
            self.validate_combat_config(config)
        elif config_type == 'economy':
            self.validate_economy_config(config)

        is_valid = len(self.errors) == 0
        return ValidationResult(is_valid, self.errors.copy(), self.warnings.copy(), self.metrics.copy())

    def validate_quest_config(self, config: Dict[str, Any]) -> None:
        """Validate quest balance configuration"""
        # Check quest difficulty tiers
        quest_tiers = config.get('quest_difficulty_tiers', {})
        for tier_name, tier_data in quest_tiers.items():
            level_range = tier_data.get('level_range', [])

            # Validate level ranges
            if len(level_range) != 2 or level_range[0] >= level_range[1]:
                self.errors.append(f"Invalid level range for {tier_name}: {level_range}")

            # Check reward progression
            base_exp = tier_data.get('base_experience', 0)
            base_eddies = tier_data.get('base_eddies', 0)

            if base_exp <= 0 or base_eddies <= 0:
                self.errors.append(f"Invalid rewards for {tier_name}: exp={base_exp}, eddies={base_eddies}")

        # Validate dynamic quest formulas
        exp_formula = config.get('dynamic_quest_balance', {}).get('experience_formula', {})
        if 'final' not in exp_formula:
            self.errors.append("Missing final experience formula")

        # Check reputation ranges
        reputation = config.get('reputation_system', {}).get('factions', {})
        for faction_name, faction_data in reputation.items():
            rep_range = faction_data.get('quest_modifier_range', [])
            if len(rep_range) != 2 or rep_range[0] >= rep_range[1]:
                self.errors.append(f"Invalid reputation range for {faction_name}: {rep_range}")

        # Calculate metrics
        self.metrics['total_quest_tiers'] = len(quest_tiers)
        self.metrics['reputation_factions'] = len(reputation)
        self.metrics['exp_to_eddies_ratio'] = self.calculate_exp_eddies_ratio(quest_tiers)

    def validate_combat_config(self, config: Dict[str, Any]) -> None:
        """Validate combat balance configuration"""
        # Check weapon balance
        weapons = config.get('weapon_balance', {})
        ttk_ranges = config.get('combat_constants', {}).get('ttk_targets', {})

        for weapon_name, weapon_data in weapons.items():
            ttk = weapon_data.get('ttk_at_25m', 0)
            if ttk < ttk_ranges.get('min_ttk', 0) or ttk > ttk_ranges.get('max_ttk', 100):
                self.warnings.append(f"Weapon {weapon_name} TTK ({ttk}s) outside optimal range")

        # Validate armor balance
        armor_types = config.get('armor_balance', {})
        for armor_name, armor_data in armor_types.items():
            damage_reduction = armor_data.get('damage_reduction', 0)
            if damage_reduction < 0 or damage_reduction > 1:
                self.errors.append(f"Invalid damage reduction for {armor_name}: {damage_reduction}")

        # Check skill progression
        skills = config.get('skill_progression', {})
        for skill_name, skill_data in skills.items():
            levels = skill_data.get('levels', [])
            if not levels:
                self.errors.append(f"No progression levels for skill {skill_name}")

        # Calculate metrics
        self.metrics['total_weapons'] = len(weapons)
        self.metrics['armor_types'] = len(armor_types)
        self.metrics['combat_skills'] = len(skills)
        self.metrics['average_weapon_ttk'] = self.calculate_average_ttk(weapons)

    def validate_economy_config(self, config: Dict[str, Any]) -> None:
        """Validate economy balance configuration"""
        # Check income sources
        income_sources = config.get('income_sources', {})
        total_income_sources = sum(1 for source in income_sources.values() if isinstance(source, dict))

        # Validate expenditure categories
        expenditure_categories = config.get('expenditure_categories', {})
        total_expenditure_categories = len(expenditure_categories)

        # Check wealth distribution targets
        wealth_dist = config.get('wealth_distribution', {})
        gini_target = wealth_dist.get('gini_targets', {}).get('optimal', 0)
        if gini_target < 0 or gini_target > 1:
            self.errors.append(f"Invalid Gini target: {gini_target}")

        # Validate economic constants
        constants = config.get('economic_constants', {})
        inflation_target = constants.get('currencies', {}).get('eddies', {}).get('inflation_target', 0)
        if inflation_target < 0 or inflation_target > 0.1:
            self.warnings.append(f"Inflation target {inflation_target} may be too extreme")

        # Check economic classes
        economic_classes = config.get('economic_classes', {})
        for class_name, class_data in economic_classes.items():
            wealth_range = class_data.get('wealth_range', [])
            if len(wealth_range) != 2 or wealth_range[0] >= wealth_range[1]:
                self.errors.append(f"Invalid wealth range for {class_name}: {wealth_range}")

        # Calculate metrics
        self.metrics['income_sources'] = total_income_sources
        self.metrics['expenditure_categories'] = total_expenditure_categories
        self.metrics['economic_classes'] = len(economic_classes)
        self.metrics['wealth_inequality_target'] = gini_target

    def calculate_exp_eddies_ratio(self, quest_tiers: Dict[str, Any]) -> float:
        """Calculate average experience to eddies ratio"""
        ratios = []
        for tier_data in quest_tiers.values():
            exp = tier_data.get('base_experience', 0)
            eddies = tier_data.get('base_eddies', 0)
            if exp > 0 and eddies > 0:
                ratios.append(eddies / exp)

        return sum(ratios) / len(ratios) if ratios else 0

    def calculate_average_ttk(self, weapons: Dict[str, Any]) -> float:
        """Calculate average TTK across all weapons"""
        ttks = []
        for weapon_data in weapons.values():
            ttk = weapon_data.get('ttk_at_25m', 0)
            if ttk > 0:
                ttks.append(ttk)

        return sum(ttks) / len(ttks) if ttks else 0

def print_validation_report(result: ValidationResult) -> None:
    """Print validation report"""
    print("\n" + "="*80)
    print("🎯 BALANCE CONFIGURATION VALIDATION REPORT")
    print("="*80)

    if result.is_valid:
        print("✅ VALIDATION PASSED")
    else:
        print("❌ VALIDATION FAILED")

    print(f"\n📊 Metrics:")
    for key, value in result.metrics.items():
        print(f"  {key}: {value}")

    if result.errors:
        print(f"\n❌ Critical Errors ({len(result.errors)}):")
        for error in result.errors:
            print(f"  • {error}")

    if result.warnings:
        print(f"\n⚠️  Warnings ({len(result.warnings)}):")
        for warning in result.warnings:
            print(f"  • {warning}")

    print("\n" + "="*80)

def main():
    import argparse

    parser = argparse.ArgumentParser(description='Validate balance configuration files')
    parser.add_argument('--config', choices=['quest', 'combat', 'economy'], help='Validate specific configuration')
    parser.add_argument('--json', action='store_true', help='Output results as JSON')

    args = parser.parse_args()

    validator = BalanceValidator()

    if args.config:
        result = validator.validate_config(args.config, f'{args.config}-balance-system.yaml')
        config_name = args.config.title()
    else:
        result = validator.validate_all_configs()
        config_name = "All Balance"

    if args.json:
        output = {
            'config': config_name,
            'valid': result.is_valid,
            'errors': result.errors,
            'warnings': result.warnings,
            'metrics': result.metrics
        }
        print(json.dumps(output, indent=2))
    else:
        print_validation_report(result)

    sys.exit(0 if result.is_valid else 1)

if __name__ == '__main__':
    main()
