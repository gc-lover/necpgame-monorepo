package com.necpgame.workqueue.domain.process;

import com.necpgame.workqueue.domain.AgentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "template_instances")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateInstanceEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private ProcessTemplateEntity template;

    @Column(name = "entity_type", nullable = false, length = 64)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "data_json", columnDefinition = "JSONB", nullable = false)
    private String dataJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filled_by_agent_id")
    private AgentEntity filledBy;

    @Column(name = "filled_at")
    private OffsetDateTime filledAt;
}


