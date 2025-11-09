package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faction_quest_endings", indexes = {
    @Index(name = "idx_faction_quest_endings_quest", columnList = "quest_id")
})
public class FactionQuestEndingEntity {

    @Id
    @Column(name = "ending_id", length = 120)
    private String endingId;

    @Column(name = "quest_id", length = 120, nullable = false)
    private String questId;

    @Column(name = "ending_payload", columnDefinition = "jsonb", nullable = false)
    private String endingPayload;
}


