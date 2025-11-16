package com.necpgame.workqueue.domain.npc;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "npc_schedule_entries")
@Getter
@Setter
@NoArgsConstructor
public class NpcScheduleEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_entity_id")
    private ContentEntryEntity npc;

    @Column(name = "day_time_range", length = 64)
    private String dayTimeRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_entity_id")
    private ContentEntryEntity location;

    @Column(name = "schedule_payload", columnDefinition = "JSONB", nullable = false)
    private String schedulePayloadJson;
}

