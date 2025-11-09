---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:46
**api-readiness-notes:** Romance Event Engine - Execution & Memory. Выполнение событий и система памяти. ~320 строк.
---

# Romance Event Engine - Part 3: Execution & Memory

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:46  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Romance event execution & memory  
**Размер:** ~320 строк ✅

**Родительский документ:** romance-event-engine.md (разбит на 3 части)  
**Связанные микрофичи:**
- [Part 1: Filtering & Weighting](./romance-engine-filtering.md)
- [Part 2: Scoring & Selection](./romance-engine-scoring.md)

---

## Smart Recommendations

### AI-Driven Recommendation System

```python
def get_smart_recommendations(player, npc, relationship, count=3):
    """
    AI-powered умные рекомендации событий
    """
    
    context = gather_romance_context(player, npc)
    
    # === Analyze Relationship State ===
    analysis = analyze_relationship_state(relationship, history)
    
    recommendations = []
    
    # === Strategy 1: Progress Romance ===
    if analysis.ready_for_next_stage:
        # Suggest events that advance relationship
        next_stage_events = get_events_for_next_stage(relationship.stage)
        recommendations.extend(next_stage_events[:2])
    
    # === Strategy 2: Resolve Conflicts ===
    if relationship.conflicts_unresolved > 0:
        # Priority: reconciliation!
        reconciliation_events = get_reconciliation_events(context)
        recommendations.insert(0, reconciliation_events[0])  # Top priority
    
    # === Strategy 3: Add Drama (if too smooth) ===
    if analysis.too_smooth and relationship.score > 50:
        # Inject conflict for narrative interest
        mild_conflict = get_mild_conflict_event(relationship)
        recommendations.append(mild_conflict)
    
    # === Strategy 4: Cultural Experience ===
    if player.location == npc.home_city:
        # In NPC's home city: suggest regional events
        regional_event = get_regional_event(npc.home_region, relationship.score)
        recommendations.append(regional_event)
    
    # === Strategy 5: Milestone Events ===
    if analysis.approaching_milestone:
        # Big moments: first kiss, meeting family, proposal
        milestone_event = get_milestone_event(analysis.next_milestone, relationship)
        recommendations.insert(0, milestone_event)
    
    # === Strategy 6: Variety ===
    # Ensure recommendations are varied (not all same category)
    recommendations = ensure_category_variety(recommendations)
    
    # === Strategy 7: Player Preferences ===
    # Consider what player has enjoyed before
    if player.romance_history.favorite_categories:
        bonus_event = get_event_from_category(
            player.romance_history.favorite_categories[0],
            relationship.score
        )
        recommendations.append(bonus_event)
    
    # Deduplicate and return top N
    recommendations = list(dict.fromkeys(recommendations))[:count]
    
    return recommendations


def analyze_relationship_state(relationship, history):
    """
    Анализ текущего состояния отношений
    """
    
    return {
        'ready_for_next_stage': (
            relationship.score >= get_threshold_for_next_stage(relationship.stage) - 5
        ),
        'too_smooth': (
            relationship.events_since_conflict > 10 and 
            relationship.conflicts_total == 0
        ),
        'approaching_milestone': detect_approaching_milestone(relationship),
        'next_milestone': get_next_expected_milestone(relationship),
        'health_status': classify_relationship_health(relationship.health),
        'breakup_risk': relationship.breakup_risk,
        'momentum': calculate_relationship_momentum(history.last_10_events),
        'stagnation': days_since_progress(history) > 7
    }
```

---

## Memory System

### Relationship Memory Engine

```python
class RelationshipMemory:
    """Система памяти отношений"""
    
    def remember_event(self, relationship_id, event, choices, outcome):
        """
        Запомнить событие в историю
        """
        
        memory = {
            'event_id': event.id,
            'event_name': event.name,
            'category': event.category,
            'timestamp': current_timestamp(),
            'location': current_location(),
            'choices_made': choices,
            'outcome': outcome,
            'relationship_before': relationship.score,
            'relationship_after': relationship.score + outcome.relationship_change,
            'memorable_dialogue': outcome.dialogue,
            'emotional_impact': classify_emotional_impact(outcome)
        }
        
        # Store in database
        store_event_history(relationship_id, memory)
        
        # Update relationship flags
        if outcome.flags:
            add_flags_to_relationship(relationship_id, outcome.flags)
        
        # Check for milestones
        check_and_record_milestones(relationship_id, event, outcome)
    
    def recall_memory(self, relationship_id, event_type=None, limit=10):
        """
        Вспомнить прошлые события
        """
        
        query = """
            SELECT * FROM relationship_event_history
            WHERE relationship_id = %s
        """
        
        if event_type:
            query += " AND event_id LIKE %s"
            params = (relationship_id, f"{event_type}%")
        else:
            params = (relationship_id,)
        
        query += " ORDER BY triggered_at DESC LIMIT %s"
        params += (limit,)
        
        return execute_query(query, params)
    
    def get_significant_memories(self, relationship_id):
        """
        Получить значимые воспоминания (для диалогов)
        """
        
        # Get milestones
        milestones = get_relationship_milestones(relationship_id)
        
        # Get high-impact events
        high_impact = get_high_impact_events(relationship_id, min_impact=20)
        
        # Get conflicts and resolutions
        conflicts = get_conflict_memories(relationship_id)
        
        return {
            'milestones': milestones,
            'high_impact_moments': high_impact,
            'conflicts': conflicts,
            'first_meeting': get_first_event(relationship_id),
            'most_recent': get_recent_events(relationship_id, count=5)
        }
    
    def reference_past_event_in_dialogue(self, current_event, relationship_id):
        """
        Вставить отсылку к прошлому событию в диалог
        """
        
        significant = self.get_significant_memories(relationship_id)
        
        # NPC может сказать:
        # "Remember when we first met at that bar?" (RE-001)
        # "That first kiss under sakura... I'll never forget" (RE-TOKYO-002)
        # "We've been through so much together" (if many conflicts resolved)
        
        past_ref = select_relevant_past_event(
            current_event.category, 
            significant
        )
        
        if past_ref:
            dialogue_addition = generate_nostalgic_dialogue(past_ref, npc.personality)
            current_event.dialogue.add_reference(dialogue_addition)
        
        return current_event
```

---

## Полный цикл события

### End-to-End Event Flow

```python
async def execute_romance_event(player_id, npc_id, event_id=None, player_initiated=True):
    """
    Полный цикл выполнения романтического события
    """
    
    # 1. Load data
    player = await get_player(player_id)
    npc = await get_npc(npc_id)
    relationship = await get_relationship(player_id, npc_id)
    context = gather_romance_context(player, npc)
    
    # 2. Select event (if not specified)
    if not event_id:
        recommended = get_smart_recommendations(player, npc, relationship, count=3)
        # Present to player or auto-select based on context
        event = recommended[0]  # or let player choose
    else:
        event = await get_event(event_id)
    
    # 3. Check triggers
    can_trigger, reason = check_event_triggers(event, context)
    if not can_trigger:
        return {"error": reason, "code": "TRIGGER_NOT_MET"}
    
    # 4. Adapt to culture
    adapted_event = adapt_event_to_culture(event, npc.culture, player.cultural_knowledge)
    
    # 5. Add memory references
    adapted_event = reference_past_event_in_dialogue(adapted_event, relationship.id)
    
    # 6. Present event to player
    presentation = {
        'event': adapted_event,
        'npc': npc,
        'context': context,
        'relationship_current': relationship.score,
        'chemistry': relationship.chemistry
    }
    
    # 7. Wait for player choices
    choices = await wait_for_player_choices(presentation)
    
    # 8. Process skill checks (if any)
    if adapted_event.skill_check:
        roll_result = roll_d20()
        total = calculate_skill_check_total(roll_result, player, adapted_event.skill_check)
        success = total >= adapted_event.skill_check.dc
        critical = (roll_result == 20) or (roll_result == 1)
    else:
        success = True
        critical = False
    
    # 9. Determine outcome
    if critical and roll_result == 20:
        outcome = adapted_event.outcomes.critical_success
    elif critical and roll_result == 1:
        outcome = adapted_event.outcomes.critical_failure
    elif success:
        outcome = adapted_event.outcomes.success
    else:
        outcome = adapted_event.outcomes.failure
    
    # 10. Apply outcome
    await apply_event_outcome(relationship.id, outcome)
    
    # 11. Record in memory
    await record_event_history(relationship.id, adapted_event, choices, outcome, roll_result)
    
    # 12. Update relationship scores
    await update_relationship_scores(relationship.id, outcome)
    
    # 13. Check for milestones
    await check_milestones(relationship.id, adapted_event, outcome)
    
    # 14. Check for achievements
    await check_achievements(player_id, relationship.id)
    
    # 15. Suggest next events
    next_suggestions = get_smart_recommendations(player, npc, relationship, count=3)
    await save_next_suggestions(relationship.id, next_suggestions)
    
    # 16. NPC may schedule next interaction
    if outcome.relationship_change > 15:  # Good outcome
        schedule_npc_initiated_event(npc, player, relationship, days=1-3)
    
    return {
        'success': True,
        'outcome': outcome,
        'relationship_new': relationship.score + outcome.relationship_change,
        'next_events': next_suggestions,
        'milestones_achieved': get_new_milestones(relationship.id),
        'achievements_unlocked': get_new_achievements(player_id)
    }
```

---

## Итоговые возможности Engine

- ✅ Фильтрация событий (hard & soft)
- ✅ Взвешивание по множеству факторов
- ✅ Scoring система
- ✅ Умный выбор событий
- ✅ Культурная адаптация
- ✅ Chemistry калькулятор
- ✅ Trigger system
- ✅ Memory system
- ✅ Полный цикл события

**Готово к реализации!**

---

## Связанные документы

- [Part 1: Filtering & Weighting](./romance-engine-filtering.md)
- [Part 2: Scoring & Selection](./romance-engine-scoring.md)
- [Romance Dialogue System](./romance-dialogue.md)

