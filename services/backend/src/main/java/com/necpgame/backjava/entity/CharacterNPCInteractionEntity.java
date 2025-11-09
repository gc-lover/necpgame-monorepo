package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity РґР»СЏ РёСЃС‚РѕСЂРёРё РІР·Р°РёРјРѕРґРµР№СЃС‚РІРёР№ РїРµСЂСЃРѕРЅР°Р¶Р° СЃ NPC.
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: character_npc_interactions
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "character_npc_interactions",
    indexes = {
        @Index(name = "idx_character_npc_interactions_character", columnList = "character_id"),
        @Index(name = "idx_character_npc_interactions_npc", columnList = "npc_id"),
        @Index(name = "idx_character_npc_interactions_dialogue", columnList = "current_dialogue_id")
    }
)
public class CharacterNPCInteractionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_character_npc_interactions_character"))
    private CharacterEntity character;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_character_npc_interactions_npc"))
    private NPCEntity npc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_dialogue_id",
                foreignKey = @ForeignKey(name = "fk_character_npc_interactions_dialogue"))
    private NPCDialogueEntity currentDialogue;
    
    @Column(name = "interaction_count", nullable = false)
    private Integer interactionCount = 0;
    
    @Column(name = "relationship_level", nullable = false)
    private Integer relationshipLevel = 0;
    
    @CreationTimestamp
    @Column(name = "first_interaction_at", nullable = false, updatable = false)
    private LocalDateTime firstInteractionAt;
    
    @Column(name = "last_interaction_at")
    private LocalDateTime lastInteractionAt;
}

