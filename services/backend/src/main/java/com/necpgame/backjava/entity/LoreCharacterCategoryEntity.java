package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lore_character_categories", indexes = {
        @Index(name = "idx_lore_character_categories_name", columnList = "name")
})
public class LoreCharacterCategoryEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "category_id", nullable = false, unique = true, length = 64)
    private String categoryId;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "role", length = 160)
    private String role;

    @Column(name = "example_characters_json", columnDefinition = "JSONB")
    private String exampleCharactersJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
}


