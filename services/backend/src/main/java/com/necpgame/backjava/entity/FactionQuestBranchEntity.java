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
@Table(name = "faction_quest_branches", indexes = {
    @Index(name = "idx_faction_quest_branches_quest", columnList = "quest_id")
})
public class FactionQuestBranchEntity {

    @Id
    @Column(name = "branch_id", length = 120)
    private String branchId;

    @Column(name = "quest_id", length = 120, nullable = false)
    private String questId;

    @Column(name = "branch_payload", columnDefinition = "jsonb", nullable = false)
    private String branchPayload;
}


