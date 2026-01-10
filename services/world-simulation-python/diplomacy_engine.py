#!/usr/bin/env python3
"""
Diplomacy Engine - FreeCiv Logic Implementation
Love/Fear based diplomatic relations between factions (#2279)

Implements:
- Love metric (reputation/historical relations)
- Fear metric (military power differences)
- Dynamic relation evaluation (WAR, PEACE, ALLIANCE, COLD_WAR)
- ConfederateAI-inspired logic adaptation
"""

import random
import logging
from typing import Dict, List, Tuple, Optional
from datetime import datetime
from enum import Enum

logger = logging.getLogger(__name__)

class DiplomaticState(Enum):
    """Diplomatic relationship states"""
    WAR = "WAR"
    COLD_WAR = "COLD_WAR"
    PEACE = "PEACE"
    ALLIANCE = "ALLIANCE"

class FactionState:
    """Represents the state of a faction in diplomatic relations"""

    def __init__(self, faction_id: str, military_power: float = 0.0, name: str = ""):
        self.faction_id = faction_id
        self.name = name or faction_id
        self.military_power = military_power
        self.economic_strength = 0.0
        self.territory_size = 0.0
        self.technology_level = 0.0
        self.morale = 1.0  # 0.0 to 1.0
        self.resources = {}  # Resource inventory
        self.last_updated = datetime.now()

    def update_power(self, new_power: float):
        """Update military power"""
        self.military_power = max(0.0, new_power)
        self.last_updated = datetime.now()

    def get_total_power_score(self) -> float:
        """Calculate total power score combining all factors"""
        # Weighted combination of different power aspects
        weights = {
            'military': 0.4,
            'economic': 0.25,
            'territory': 0.15,
            'technology': 0.15,
            'morale': 0.05
        }

        return (
            self.military_power * weights['military'] +
            self.economic_strength * weights['economic'] +
            self.territory_size * weights['territory'] +
            self.technology_level * weights['technology'] +
            self.morale * weights['morale']
        )

class DiplomaticRelation:
    """Represents diplomatic relation between two factions"""

    def __init__(self, faction_a: str, faction_b: str):
        self.faction_a = faction_a
        self.faction_b = faction_b
        self.love_score = 0.0  # -1.0 to 1.0 (hate to love)
        self.fear_score = 0.0  # 0.0 to 1.0 (no fear to terror)
        self.trust_level = 0.0  # -1.0 to 1.0 (distrust to trust)
        self.last_interaction = None
        self.interaction_history: List[Dict] = []
        self.treaties: List[Dict] = []
        self.state = DiplomaticState.PEACE
        self.state_changed_at = datetime.now()

    def update_scores(self, love_delta: float, fear_delta: float, trust_delta: float = 0.0):
        """Update diplomatic scores with bounds checking"""
        self.love_score = max(-1.0, min(1.0, self.love_score + love_delta))
        self.fear_score = max(0.0, min(1.0, self.fear_score + fear_delta))
        self.trust_level = max(-1.0, min(1.0, self.trust_level + trust_delta))

        # Record interaction
        self.interaction_history.append({
            'timestamp': datetime.now(),
            'love_delta': love_delta,
            'fear_delta': fear_delta,
            'trust_delta': trust_delta,
            'total_love': self.love_score,
            'total_fear': self.fear_score,
            'total_trust': self.trust_level
        })

        # Keep only last 20 interactions
        if len(self.interaction_history) > 20:
            self.interaction_history = self.interaction_history[-20:]

        self.last_interaction = datetime.now()

    def add_treaty(self, treaty_type: str, duration_days: int, terms: Dict):
        """Add a diplomatic treaty"""
        from datetime import timedelta
        treaty = {
            'type': treaty_type,
            'signed_at': datetime.now(),
            'expires_at': datetime.now() + timedelta(days=duration_days),
            'terms': terms,
            'active': True
        }
        self.treaties.append(treaty)

    def get_active_treaties(self) -> List[Dict]:
        """Get currently active treaties"""
        now = datetime.now()
        active = [t for t in self.treaties if t['active'] and t['expires_at'] > now]

        # Mark expired treaties as inactive
        for treaty in self.treaties:
            if treaty['active'] and treaty['expires_at'] <= now:
                treaty['active'] = False

        return active

class DiplomacyEngine:
    """Main diplomacy engine implementing FreeCiv-style Love/Fear logic"""

    def __init__(self):
        self.factions: Dict[str, FactionState] = {}
        self.relations: Dict[str, Dict[str, DiplomaticRelation]] = {}
        self.global_events: List[Dict] = []
        self.diplomacy_log: List[Dict] = []

    def add_faction(self, faction_id: str, military_power: float = 0.0, name: str = ""):
        """Add a faction to the diplomatic system"""
        if faction_id in self.factions:
            logger.warning(f"Faction {faction_id} already exists")
            return

        faction = FactionState(faction_id, military_power, name)
        self.factions[faction_id] = faction

        # Initialize relations with all existing factions
        self.relations[faction_id] = {}
        for existing_id, existing_faction in self.factions.items():
            if existing_id != faction_id:
                relation = DiplomaticRelation(faction_id, existing_id)
                self.relations[faction_id][existing_id] = relation
                # Also add reverse relation for easy lookup
                if existing_id not in self.relations:
                    self.relations[existing_id] = {}
                self.relations[existing_id][faction_id] = relation

        logger.info(f"Added faction {faction_id} ({name}) to diplomacy system")

    def get_relation(self, faction_a: str, faction_b: str) -> Optional[DiplomaticRelation]:
        """Get diplomatic relation between two factions"""
        if faction_a in self.relations and faction_b in self.relations[faction_a]:
            return self.relations[faction_a][faction_b]
        return None

    def evaluate_relation(self, faction_a: str, faction_b: str) -> DiplomaticState:
        """
        Evaluate diplomatic relation using FreeCiv Love/Fear logic
        Returns the current diplomatic state
        """
        relation = self.get_relation(faction_a, faction_b)
        if not relation:
            return DiplomaticState.PEACE

        faction_a_state = self.factions.get(faction_a)
        faction_b_state = self.factions.get(faction_b)

        if not faction_a_state or not faction_b_state:
            return DiplomaticState.PEACE

        # Calculate Love factor (reputation + trust + treaties)
        love_factor = relation.love_score + (relation.trust_level * 0.5)

        # Add treaty bonuses
        active_treaties = relation.get_active_treaties()
        for treaty in active_treaties:
            if treaty['type'] == 'alliance':
                love_factor += 0.3
            elif treaty['type'] == 'trade_agreement':
                love_factor += 0.2
            elif treaty['type'] == 'non_aggression_pact':
                love_factor += 0.1

        # Normalize love factor
        love_factor = max(-1.0, min(1.0, love_factor))

        # Calculate Fear factor (military power difference)
        power_a = faction_a_state.get_total_power_score()
        power_b = faction_b_state.get_total_power_score()

        if power_a > 0 and power_b > 0:
            power_ratio = power_a / power_b
            if power_ratio > 1.5:  # A is significantly stronger
                fear_factor = min(1.0, (power_ratio - 1.0) * 0.3)
            elif power_ratio < 0.67:  # B is significantly stronger
                fear_factor = min(1.0, (1.0/power_ratio - 1.0) * 0.3)
            else:
                fear_factor = 0.0
        else:
            fear_factor = 0.0

        # Update relation scores
        relation.update_scores(0.0, fear_factor * 0.1)  # Gradual fear adjustment

        # Calculate diplomatic score using FreeCiv formula
        # Score = Love + Fear_Impact - Hate_Impact + Random_Noise
        hate_impact = max(0, -love_factor) * 0.5  # Hate reduces diplomacy more than love increases
        fear_impact = fear_factor * 0.8  # Fear can override some hate

        diplomatic_score = love_factor + fear_impact - hate_impact

        # Add small random noise for unpredictability
        noise = random.uniform(-0.1, 0.1)
        diplomatic_score += noise

        # Determine diplomatic state
        old_state = relation.state
        new_state = self._score_to_state(diplomatic_score)

        # Log state changes
        if new_state != old_state:
            self._log_state_change(faction_a, faction_b, old_state, new_state, diplomatic_score)
            relation.state = new_state
            relation.state_changed_at = datetime.now()

        return new_state

    def evaluate_all_relations(self) -> Dict[str, DiplomaticState]:
        """
        Evaluate diplomatic relations for all faction pairs
        Returns dict of relation keys to diplomatic states
        """
        results = {}

        factions = list(self.factions.keys())
        for i in range(len(factions)):
            for j in range(i + 1, len(factions)):
                faction_a = factions[i]
                faction_b = factions[j]

                state = self.evaluate_relation(faction_a, faction_b)
                relation_key = f"{faction_a}_{faction_b}"
                results[relation_key] = state

        return results

    def trigger_diplomatic_event(self, event_type: str, factions_involved: List[str], impact: Dict):
        """
        Trigger a diplomatic event that affects relations
        """
        event = {
            'type': event_type,
            'factions': factions_involved,
            'impact': impact,
            'timestamp': datetime.now(),
            'processed': False
        }

        self.global_events.append(event)

        # Apply immediate impacts
        if event_type == 'war_declaration':
            self._apply_war_impacts(factions_involved, impact)
        elif event_type == 'alliance_formed':
            self._apply_alliance_impacts(factions_involved, impact)
        elif event_type == 'betrayal':
            self._apply_betrayal_impacts(factions_involved, impact)

        logger.info(f"Diplomatic event triggered: {event_type} involving {factions_involved}")

    def _apply_war_impacts(self, factions: List[str], impact: Dict):
        """Apply impacts when war is declared"""
        if len(factions) >= 2:
            attacker, defender = factions[0], factions[1]

            relation = self.get_relation(attacker, defender)
            if relation:
                # War dramatically reduces love and trust
                relation.update_scores(-0.5, 0.0, -0.8)

                # Create war treaty
                relation.add_treaty('war', 365, {'aggressor': attacker, 'defender': defender})

    def _apply_alliance_impacts(self, factions: List[str], impact: Dict):
        """Apply impacts when alliance is formed"""
        if len(factions) >= 2:
            faction_a, faction_b = factions[0], factions[1]

            relation = self.get_relation(faction_a, faction_b)
            if relation:
                # Alliance increases love and trust
                relation.update_scores(0.4, 0.0, 0.6)

                # Create alliance treaty
                duration = impact.get('duration_days', 365)
                relation.add_treaty('alliance', duration, {'members': factions})

    def _apply_betrayal_impacts(self, factions: List[str], impact: Dict):
        """Apply impacts when betrayal occurs"""
        if len(factions) >= 2:
            betrayer, betrayed = factions[0], factions[1]

            relation = self.get_relation(betrayer, betrayed)
            if relation:
                # Betrayal destroys trust and reduces love dramatically
                relation.update_scores(-0.8, 0.0, -1.0)

    def _score_to_state(self, score: float) -> DiplomaticState:
        """Convert diplomatic score to state"""
        if score < -0.7:
            return DiplomaticState.WAR
        elif score < -0.3:
            return DiplomaticState.COLD_WAR
        elif score > 0.5:
            return DiplomaticState.ALLIANCE
        else:
            return DiplomaticState.PEACE

    def _log_state_change(self, faction_a: str, faction_b: str,
                         old_state: DiplomaticState, new_state: DiplomaticState, score: float):
        """Log diplomatic state changes"""
        log_entry = {
            'timestamp': datetime.now(),
            'faction_a': faction_a,
            'faction_b': faction_b,
            'old_state': old_state.value,
            'new_state': new_state.value,
            'diplomatic_score': score,
            'reason': 'evaluation'
        }

        self.diplomacy_log.append(log_entry)

        logger.info(f"Diplomatic state change: {faction_a} vs {faction_b}: {old_state.value} -> {new_state.value} (score: {score:.2f})")

    def get_diplomacy_summary(self) -> Dict:
        """Get summary of current diplomatic situation"""
        summary = {
            'total_factions': len(self.factions),
            'relations_count': sum(len(relations) for relations in self.relations.values()) // 2,  # Divide by 2 to avoid double counting
            'state_distribution': {},
            'recent_events': len(self.global_events),
            'active_treaties': 0
        }

        # Count diplomatic states
        states = {}
        for faction_relations in self.relations.values():
            for relation in faction_relations.values():
                state = relation.state.value
                states[state] = states.get(state, 0) + 1
                summary['active_treaties'] += len(relation.get_active_treaties())

        summary['state_distribution'] = states
        summary['active_treaties'] //= 2  # Avoid double counting

        return summary

    def simulate_time_step(self, days: int = 1):
        """
        Simulate passage of time and gradual diplomatic changes
        """
        # Gradual trust recovery for peaceful relations
        for faction_relations in self.relations.values():
            for relation in faction_relations.values():
                if relation.state in [DiplomaticState.PEACE, DiplomaticState.ALLIANCE]:
                    # Small trust recovery over time
                    trust_recovery = 0.01 * days  # 1% per day for peaceful relations
                    relation.update_scores(0.0, 0.0, trust_recovery)

                elif relation.state == DiplomaticState.COLD_WAR:
                    # Cold war slowly erodes trust
                    trust_erosion = -0.005 * days  # -0.5% per day
                    relation.update_scores(0.0, 0.0, trust_erosion)

        logger.debug(f"Simulated {days} days of diplomatic time passage")