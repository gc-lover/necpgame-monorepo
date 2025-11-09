package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quest_template_definitions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestTemplateDefinitionEntity {

    @Id
    @Column(name = "quest_template_id", nullable = false, length = 100)
    private String questTemplateId;

    @Column(name = "definition", columnDefinition = "jsonb", nullable = false)
    private String definition;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}



