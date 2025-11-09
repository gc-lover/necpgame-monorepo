package com.necpgame.backjava.entity.mvp;

import com.necpgame.backjava.entity.GameLocationEntity;
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
@Table(name = "mvp_text_nearby_npcs", indexes = {
    @Index(name = "idx_mvp_text_nearby_npcs_location", columnList = "location_id")
})
public class MvpTextNearbyNpcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private GameLocationEntity location;

    @Column(name = "npc_name", nullable = false, length = 120)
    private String npcName;

    @Column(name = "can_interact", nullable = false)
    private boolean canInteract;
}

