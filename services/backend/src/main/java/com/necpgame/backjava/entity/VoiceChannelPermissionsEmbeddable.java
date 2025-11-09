package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class VoiceChannelPermissionsEmbeddable {

    @Column(name = "allow_invite", nullable = false)
    private boolean allowInvite;

    @Column(name = "allow_recording", nullable = false)
    private boolean allowRecording;

    @Column(name = "allow_spectators", nullable = false)
    private boolean allowSpectators;
}




