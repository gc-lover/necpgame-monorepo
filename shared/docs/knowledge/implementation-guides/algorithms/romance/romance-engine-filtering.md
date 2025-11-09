---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:46
**api-readiness-notes:** Romance Event Engine - Filtering & Weighting. Фильтрация и взвешивание романтических событий. ~360 строк.
---

# Romance Event Engine - Part 1: Filtering & Weighting

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:46  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Romance event filtering & weighting  
**Размер:** ~360 строк ✅

**Родительский документ:** romance-event-engine.md (разбит на 3 части)  
**Связанные микрофичи:**
- [Part 2: Scoring & Selection](./romance-engine-scoring.md)
- [Part 3: Execution & Memory](./romance-engine-execution.md)

---

## Краткое описание

Алгоритм выбора подходящих романтических событий на основе AI и множества факторов.

---

## Архитектура Engine

```
Player Context + NPC Profile + Current Relationship State
                    ↓
            Event Selection Engine
                    ↓
    Filter → Weight → Score → Select → Adapt
                    ↓
            Recommended Event(s)
```

---

## 1. Event Filtering (Фильтрация)

### Phase 1: Hard Filters (Обязательные условия)

```python
def filter_events_hard(events, player, npc, relationship):
    """Убираем события, которые точно не подходят"""
    filtered = []
    
    for event in events:
        # Check relationship range
        if not (event.relationship_min <= relationship.score <= event.relationship_max):
            continue
        
        # Check if already completed (for unique events)
        if event.unique and event.id in relationship.completed_events:
            continue
        
        # Check location compatibility
        if event.triggers.location_required:
            if player.location not in event.triggers.locations:
                continue
        
        # Check time compatibility
        if event.triggers.time_required:
            if current_time() not in event.triggers.time:
                continue
        
        # Check season (if required)
        if event.triggers.season:
            if current_season() != event.triggers.season:
                continue
        
        # Check chemistry minimum
        if event.triggers.chemistry_min:
            if relationship.chemistry < event.triggers.chemistry_min:
                continue
        
        # Check cultural compatibility
        if event.culture_specific:
            if not is_culturally_compatible(player, npc, event):
                continue
        
        filtered.append(event)
    
    return filtered
```

### Phase 2: Soft Filters (Предпочтения)

```python
def filter_events_soft(events, player, npc, relationship, history):
    """Приоритизируем события по предпочтениям"""
    scored_events = []
    
    for event in events:
        score = 0
        
        # Avoid repetition (недавно использованные события)
        if event.category in history.recent_categories(last=5):
            score -= 20
        
        # Prefer events matching current arc phase
        if event.category == get_expected_category(relationship.score):
            score += 30
        
        # Prefer regional events if in NPC's region
        if event.region == npc.home_region and player.location == npc.home_city:
            score += 25
        
        # Prefer events matching player preferences
        if event.category in player.preferences.favorite_event_types:
            score += 15
        
        # Avoid conflict events if relationship fragile
        if event.category == 'conflict' and relationship.health < 50:
            score -= 30
        
        scored_events.append((event, score))
    
    # Sort by score
    scored_events.sort(key=lambda x: x[1], reverse=True)
    
    return [event for event, score in scored_events if score > 0]
```

---

## 2. Event Weighting (Взвешивание)

### Факторы веса события

```python
def calculate_event_weight(event, player, npc, relationship, chemistry):
    """Рассчитать вес события (0-100)"""
    
    weight = 50  # Base weight
    
    # === Personality Match (0-25 points) ===
    personality_alignment = calculate_personality_alignment(
        player.personality, 
        npc.personality, 
        event.personality_requirements
    )
    weight += (personality_alignment / 100) * 25
    
    # === Chemistry Boost (0-20 points) ===
    if chemistry.total > 80:
        weight += 20
    elif chemistry.total > 60:
        weight += 15
    elif chemistry.total > 40:
        weight += 10
    
    # === Timing Appropriateness (0-15 points) ===
    if is_perfect_timing(event, relationship, history):
        weight += 15
    elif is_good_timing(event, relationship):
        weight += 10
    
    # === Cultural Appropriateness (0-15 points) ===
    cultural_fit = calculate_cultural_fit(event, npc.culture, player.cultural_knowledge)
    weight += (cultural_fit / 100) * 15
    
    # === Narrative Coherence (0-15 points) ===
    if fits_narrative_arc(event, relationship.arc):
        weight += 15
    
    # === Player Preferences (0-10 points) ===
    if event.category in player.preferences.preferred_categories:
        weight += 10
    
    # === Penalties ===
    
    # Repetition penalty
    if event.category in history.recent_categories(last=3):
        weight -= 15
    
    # Incompatibility penalty
    if event.requires_trait and not npc.has_trait(event.requires_trait):
        weight -= 20
    
    # Risk penalty (if relationship fragile)
    if event.category == 'conflict' and relationship.health < 60:
        weight -= 25
    
    # Out of region penalty
    if event.region and event.region != player.current_region:
        weight -= 10
    
    return max(0, min(100, weight))
```

### Personality Alignment Calculation

```python
def calculate_personality_alignment(player_personality, npc_personality, event_requirements):
    """
    Рассчитать насколько личности игрока и NPC подходят для события
    Returns: 0-100
    """
    
    if not event_requirements:
        return 75  # Neutral if no requirements
    
    alignment = 0
    factors = 0
    
    # Check each personality trait
    for trait, required_range in event_requirements.items():
        if trait in npc_personality:
            npc_value = npc_personality[trait]
            min_req, max_req = required_range
            
            if min_req <= npc_value <= max_req:
                alignment += 100  # Perfect match
            else:
                # Calculate distance from range
                if npc_value < min_req:
                    distance = min_req - npc_value
                else:
                    distance = npc_value - max_req
                
                # Penalty proportional to distance
                alignment += max(0, 100 - (distance * 2))
            
            factors += 1
    
    return alignment / factors if factors > 0 else 50
```

---

## Chemistry Calculator

### Comprehensive Chemistry System

```python
class ChemistryCalculator:
    """Расчёт совместимости между игроком и NPC"""
    
    def calculate_total_chemistry(self, player, npc):
        """
        Главная функция расчёта chemistry (0-100)
        """
        
        # Components
        personality_match = self.calculate_personality_match(player, npc)
        shared_interests = self.calculate_shared_interests(player, npc)
        physical_attraction = self.calculate_physical_attraction(player, npc)
        cultural_compatibility = self.calculate_cultural_compatibility(player, npc)
        
        # Weights (configurable)
        weights = {
            'personality': 0.40,
            'interests': 0.30,
            'attraction': 0.20,
            'cultural': 0.10
        }
        
        # Weighted sum
        total = (
            personality_match * weights['personality'] +
            shared_interests * weights['interests'] +
            physical_attraction * weights['attraction'] +
            cultural_compatibility * weights['cultural']
        )
        
        return int(total)
    
    def calculate_personality_match(self, player, npc):
        """
        Big Five personality compatibility (0-100)
        """
        
        # Get personality vectors
        p_personality = player.personality
        n_personality = npc.personality
        
        match_score = 0
        
        # Openness: Similar is good
        openness_diff = abs(p_personality.openness - n_personality.openness)
        match_score += max(0, 100 - openness_diff)
        
        # Conscientiousness: Similar or complementary
        consc_diff = abs(p_personality.conscientiousness - n_personality.conscientiousness)
        if consc_diff < 30:  # Similar
            match_score += 100
        else:  # Very different might work (opposites attract)
            match_score += 60
        
        # Extraversion: Complementary often good (extrovert + introvert)
        extrav_sum = p_personality.extraversion + n_personality.extraversion
        if 80 <= extrav_sum <= 120:  # One intro, one extro
            match_score += 100
        elif extrav_sum > 150 or extrav_sum < 50:  # Both very similar
            match_score += 80
        
        # Agreeableness: Higher is better for relationships
        avg_agree = (p_personality.agreeableness + n_personality.agreeableness) / 2
        match_score += avg_agree
        
        # Neuroticism: Lower combined is better
        avg_neuro = (p_personality.neuroticism + n_personality.neuroticism) / 2
        match_score += (100 - avg_neuro)
        
        # Romance-specific traits
        if hasattr(npc.personality, 'romanticism'):
            # High romanticism NPC needs romantic player
            if p_personality.romanticism and n_personality.romanticism:
                romantic_match = 100 - abs(p_personality.romanticism - n_personality.romanticism)
                match_score += romantic_match
        
        # Average all factors
        return int(match_score / 6)
    
    def calculate_shared_interests(self, player, npc):
        """
        Общие интересы (0-100)
        """
        
        player_interests = set(player.interests + player.hobbies)
        npc_interests = set(npc.interests + npc.hobbies)
        
        # Intersection
        shared = player_interests.intersection(npc_interests)
        
        # Calculate percentage
        total_unique = len(player_interests.union(npc_interests))
        if total_unique == 0:
            return 50
        
        shared_percentage = (len(shared) / total_unique) * 100
        
        # Boost if shared interests in important categories
        important_shared = shared.intersection({'music', 'art', 'sports', 'tech', 'cooking'})
        boost = len(important_shared) * 5
        
        return min(100, int(shared_percentage * 2 + boost))
    
    def calculate_physical_attraction(self, player, npc):
        """
        Физическое влечение (0-100)
        Базируется на предпочтениях + random factor
        """
        
        base_attraction = 50  # Neutral start
        
        # Gender preference
        if player.sexual_orientation == 'heterosexual':
            if player.gender != npc.gender:
                base_attraction += 30
            else:
                return 0  # Not attracted
        elif player.sexual_orientation == 'homosexual':
            if player.gender == npc.gender:
                base_attraction += 30
            else:
                return 0
        else:  # Bisexual, pansexual
            base_attraction += 20
        
        # Age preference
        age_diff = abs(player.age - npc.age)
        if age_diff <= 5:
            base_attraction += 15
        elif age_diff <= 10:
            base_attraction += 10
        elif age_diff > 20:
            base_attraction -= 20
        
        # Physical traits preferences (if system has this)
        if player.preferences.physical_traits:
            matches = count_matching_traits(player.preferences.physical_traits, npc.physical_traits)
            base_attraction += matches * 5
        
        # Random chemistry factor (пресловутая "искра")
        random_chemistry = random.randint(20, 50)
        
        # Charisma influence
        charisma_bonus = (npc.attributes.COOL - 10) * 2  # -10 to +10 bonus
        
        total = base_attraction + random_chemistry + charisma_bonus
        
        return max(0, min(100, total))
    
    def calculate_cultural_compatibility(self, player, npc):
        """
        Культурная совместимость (0-100)
        """
        
        compatibility = 50  # Neutral
        
        # Same culture: bonus
        if player.culture == npc.culture:
            compatibility += 40
        
        # Similar region: moderate bonus
        elif player.region == npc.region:
            compatibility += 20
        
        # Player knows NPC's culture: bonus
        if npc.culture in player.cultural_knowledge:
            compatibility += 30
        
        # Player speaks NPC's language: big bonus
        if npc.primary_language in player.languages:
            compatibility += 25
        
        # Cultural openness
        if player.personality.openness > 70:
            compatibility += 10  # Open to new cultures
        
        # NPC's traditionalism
        if npc.personality.traditionalism > 70:
            # Traditional NPC prefers same culture
            if player.culture != npc.culture:
                compatibility -= 20
        
        return max(0, min(100, compatibility))
```

---

## Связанные документы

- [Romance Event Types](../../../04-narrative/quests/romantic/romance-events-system.md)
- [Romance Events Index](../../../04-narrative/quests/romantic/ROMANCE-EVENTS-INDEX-1000.md)
- [NPC Personality System](../../ai-systems/npc-personality-romance-ai.md)

