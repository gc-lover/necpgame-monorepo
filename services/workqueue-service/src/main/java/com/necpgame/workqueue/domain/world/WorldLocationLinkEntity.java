package com.necpgame.workqueue.domain.world;

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
@Table(name = "world_location_links")
@Getter
@Setter
@NoArgsConstructor
public class WorldLocationLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_location_id")
    private ContentEntryEntity fromLocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_location_id")
    private ContentEntryEntity toLocation;

    @Column(name = "link_type", length = 64)
    private String linkType;

    @Column(name = "travel_time_minutes")
    private Integer travelTimeMinutes;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadataJson;
}

