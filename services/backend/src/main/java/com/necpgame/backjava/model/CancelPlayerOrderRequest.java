package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("cancelPlayerOrder_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CancelPlayerOrderRequest {

    @NotNull
    @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("character_id")
    private UUID characterId;

    @Schema(name = "reason")
    @JsonProperty("reason")
    private String reason;

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CancelPlayerOrderRequest that = (CancelPlayerOrderRequest) o;
        return Objects.equals(characterId, that.characterId)
            && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, reason);
    }

    @Override
    public String toString() {
        return "CancelPlayerOrderRequest{" +
            "characterId=" + characterId +
            ", reason='" + reason + '\'' +
            '}';
    }
}


