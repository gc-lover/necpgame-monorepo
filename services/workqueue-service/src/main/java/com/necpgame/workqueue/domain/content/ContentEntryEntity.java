package com.necpgame.workqueue.domain.content;

import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "content_entities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentEntryEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 128, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_type_id", nullable = false)
    private EnumValueEntity entityType;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private EnumValueEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private EnumValueEntity category;

    @Column(name = "owner_role", length = 64)
    private String ownerRole;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB", nullable = false)
    private String tags;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB", nullable = false)
    private String topics;

    @Column(name = "source_document")
    private String sourceDocument;

    @Column(nullable = false, length = 32)
    private String version;

    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visibility_id", nullable = false)
    private EnumValueEntity visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_level_id")
    private EnumValueEntity riskLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_json", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = false)
    @jakarta.persistence.OrderBy("sortOrder ASC")
    @Builder.Default
    private List<ContentEntitySectionEntity> sections = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<ContentEntityAttributeEntity> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = false)
    @jakarta.persistence.OrderBy("locale ASC")
    @Builder.Default
    private List<ContentEntityLocalizationEntity> localizations = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = false)
    @jakarta.persistence.OrderBy("createdAt DESC")
    @Builder.Default
    private List<ContentEntityNoteEntity> notes = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = false)
    @jakarta.persistence.OrderBy("changedAt DESC")
    @Builder.Default
    private List<ContentEntityHistoryEntity> history = new ArrayList<>();

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<ContentEntityLinkEntity> outgoingLinks = new ArrayList<>();
}


