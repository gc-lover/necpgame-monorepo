package com.necpgame.workqueue.domain.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "enum_groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumGroupEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 64, unique = true)
    private String code;

    @Column(length = 512)
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<EnumValueEntity> values = new ArrayList<>();
}


