package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity РґР»СЏ РґРёР°Р»РѕРіРѕРІ NPC (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: npc_dialogues
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "npc_dialogues",
    indexes = {
        @Index(name = "idx_npc_dialogues_npc", columnList = "npc_id")
    }
)
public class NPCDialogueEntity {
    
    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_npc_dialogues_npc"))
    private NPCEntity npc;
    
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;
    
    @Column(name = "condition", columnDefinition = "JSONB")
    private String condition;
    
    @Column(name = "is_initial", nullable = false)
    private Boolean isInitial = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

