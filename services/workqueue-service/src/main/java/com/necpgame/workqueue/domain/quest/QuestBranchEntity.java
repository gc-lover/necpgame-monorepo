package com.necpgame.workqueue.domain.quest;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
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

import java.util.UUID;

@Entity
@Table(name = "quest_branches")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestBranchEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quest_entity_id", nullable = false)
    private ContentEntryEntity questEntity;

    @Column(name = "branch_key", nullable = false, length = 128)
    private String branchKey;

    @Column(name = "from_stage_index", nullable = false)
    private Integer fromStageIndex;

    @Column(name = "leads_to_stage_index")
    private Integer leadsToStageIndex;

    @Column(name = "trigger_conditions", columnDefinition = "JSONB")
    private String triggerConditionsJson;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}


