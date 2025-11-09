package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "character_milestones", indexes = {
    @Index(name = "idx_character_milestones_character", columnList = "character_id")
})
public class CharacterMilestoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "milestone_id", nullable = false)
    private UUID milestoneId;

    @CreationTimestamp
    @Column(name = "completed_at", nullable = false, updatable = false)
    private OffsetDateTime completedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(optional = false)
    @JoinColumn(name = "milestone_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProgressionMilestoneEntity milestone;
}



