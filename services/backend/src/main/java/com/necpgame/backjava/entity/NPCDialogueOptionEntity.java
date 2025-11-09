package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity РґР»СЏ РѕРїС†РёР№ РґРёР°Р»РѕРіР° NPC (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: npc_dialogue_options
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "npc_dialogue_options",
    indexes = {
        @Index(name = "idx_npc_dialogue_options_dialogue", columnList = "dialogue_id"),
        @Index(name = "idx_npc_dialogue_options_next_dialogue", columnList = "next_dialogue_id")
    }
)
public class NPCDialogueOptionEntity {
    
    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dialogue_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_npc_dialogue_options_dialogue"))
    private NPCDialogueEntity dialogue;
    
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_dialogue_id",
                foreignKey = @ForeignKey(name = "fk_npc_dialogue_options_next_dialogue"))
    private NPCDialogueEntity nextDialogue;
    
    @Column(name = "action_type", length = 50)
    private String actionType;
    
    @Column(name = "action_data", columnDefinition = "JSONB")
    private String actionData;
    
    @Column(name = "requirement", columnDefinition = "JSONB")
    private String requirement;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

