package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class VoiceProximityEmbeddable {

    @Column(name = "proximity_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "proximity_falloff_start")
    private Double falloffStartMeters;

    @Column(name = "proximity_falloff_end")
    private Double falloffEndMeters;

    @Column(name = "proximity_spatial_audio", nullable = false)
    private boolean spatialAudio;
}




