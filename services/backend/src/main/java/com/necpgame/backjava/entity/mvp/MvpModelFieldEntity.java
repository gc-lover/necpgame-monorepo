package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_model_fields", indexes = {
    @Index(name = "idx_mvp_model_fields_model", columnList = "model_id")
})
public class MvpModelFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private MvpModelEntity model;

    @Column(name = "field_name", nullable = false, length = 120)
    private String fieldName;

    @Column(name = "field_type", nullable = false, length = 80)
    private String fieldType;

    @Column(name = "is_required", nullable = false)
    private boolean required;

    @Column(name = "field_description", nullable = false, columnDefinition = "TEXT")
    private String description;
}

