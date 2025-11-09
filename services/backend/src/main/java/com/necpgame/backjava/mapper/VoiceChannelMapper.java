package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.VoiceChannelEntity;
import com.necpgame.backjava.entity.VoiceParticipantEntity;
import com.necpgame.backjava.entity.enums.VoiceChannelOwnerType;
import com.necpgame.backjava.entity.enums.VoiceChannelStatus;
import com.necpgame.backjava.entity.enums.VoiceChannelType;
import com.necpgame.backjava.entity.enums.VoiceParticipantAudioQuality;
import com.necpgame.backjava.entity.enums.VoiceParticipantStatus;
import com.necpgame.backjava.entity.enums.VoiceQualityPreset;
import com.necpgame.backjava.model.ProximitySettings;
import com.necpgame.backjava.model.VoiceChannel;
import com.necpgame.backjava.model.VoiceChannelOwner;
import com.necpgame.backjava.model.VoiceChannelPermissions;
import com.necpgame.backjava.model.VoiceChannelSummary;
import com.necpgame.backjava.model.VoiceParticipant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoiceChannelMapper {

    default VoiceChannel toDto(VoiceChannelEntity entity) {
        throw new UnsupportedOperationException("Voice channel mapper is not implemented yet");
    }

    default VoiceChannelSummary toSummary(VoiceChannelEntity entity) {
        throw new UnsupportedOperationException("Voice channel mapper is not implemented yet");
    }

    default List<VoiceChannelSummary> toSummaryList(List<VoiceChannelEntity> entities) {
        throw new UnsupportedOperationException("Voice channel mapper is not implemented yet");
    }

    default VoiceParticipant toParticipant(VoiceParticipantEntity entity) {
        throw new UnsupportedOperationException("Voice channel mapper is not implemented yet");
    }
}

