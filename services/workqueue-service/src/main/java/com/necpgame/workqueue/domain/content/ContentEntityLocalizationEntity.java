package com.necpgame.workqueue.domain.content;

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
@Table(name = "entity_localizations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentEntityLocalizationEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private ContentEntryEntity entity;

    @Column(nullable = false, length = 16)
    private String locale;

    @Column(name = "title_localized", length = 512)
    private String titleLocalized;

    @Column(name = "description_localized", columnDefinition = "TEXT")
    private String descriptionLocalized;

    @Column(name = "flavor_text", columnDefinition = "TEXT")
    private String flavorText;

    @Column(name = "metadata_json", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}


