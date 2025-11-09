package com.necpgame.backjava.entity.narrative;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "narrative_npc_templates",
    indexes = {
        @Index(name = "idx_narrative_npc_templates_code", columnList = "template_code"),
        @Index(name = "idx_narrative_npc_templates_filters", columnList = "faction, region, role")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NarrativeNpcTemplateEntity implements NarrativeToolsWeightedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "template_code", nullable = false, length = 120, unique = true)
    private String templateCode;

    @Column(name = "display_name", length = 160)
    private String displayName;

    @Column(name = "faction", length = 120)
    private String faction;

    @Column(name = "region", length = 120)
    private String region;

    @Column(name = "role", length = 120)
    private String role;

    @Column(name = "personality_json", columnDefinition = "jsonb", nullable = false)
    private String personalityJson;

    @Column(name = "backstory_template", columnDefinition = "text", nullable = false)
    private String backstoryTemplate;

    @Column(name = "tags_json", columnDefinition = "jsonb", nullable = false)
    private String tagsJson;

    @Column(name = "weight", nullable = false)
    private int weight;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Override
    public int getWeight() {
        return weight;
    }
}



