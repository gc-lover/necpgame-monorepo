package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateCompanionRequestCosmetics
 */

@JsonTypeName("UpdateCompanionRequest_cosmetics")

public class UpdateCompanionRequestCosmetics {

  private @Nullable String skinId;

  private @Nullable String trailColor;

  private @Nullable String voicePackId;

  public UpdateCompanionRequestCosmetics skinId(@Nullable String skinId) {
    this.skinId = skinId;
    return this;
  }

  /**
   * Get skinId
   * @return skinId
   */
  
  @Schema(name = "skinId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skinId")
  public @Nullable String getSkinId() {
    return skinId;
  }

  public void setSkinId(@Nullable String skinId) {
    this.skinId = skinId;
  }

  public UpdateCompanionRequestCosmetics trailColor(@Nullable String trailColor) {
    this.trailColor = trailColor;
    return this;
  }

  /**
   * Get trailColor
   * @return trailColor
   */
  
  @Schema(name = "trailColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trailColor")
  public @Nullable String getTrailColor() {
    return trailColor;
  }

  public void setTrailColor(@Nullable String trailColor) {
    this.trailColor = trailColor;
  }

  public UpdateCompanionRequestCosmetics voicePackId(@Nullable String voicePackId) {
    this.voicePackId = voicePackId;
    return this;
  }

  /**
   * Get voicePackId
   * @return voicePackId
   */
  
  @Schema(name = "voicePackId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voicePackId")
  public @Nullable String getVoicePackId() {
    return voicePackId;
  }

  public void setVoicePackId(@Nullable String voicePackId) {
    this.voicePackId = voicePackId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateCompanionRequestCosmetics updateCompanionRequestCosmetics = (UpdateCompanionRequestCosmetics) o;
    return Objects.equals(this.skinId, updateCompanionRequestCosmetics.skinId) &&
        Objects.equals(this.trailColor, updateCompanionRequestCosmetics.trailColor) &&
        Objects.equals(this.voicePackId, updateCompanionRequestCosmetics.voicePackId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skinId, trailColor, voicePackId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateCompanionRequestCosmetics {\n");
    sb.append("    skinId: ").append(toIndentedString(skinId)).append("\n");
    sb.append("    trailColor: ").append(toIndentedString(trailColor)).append("\n");
    sb.append("    voicePackId: ").append(toIndentedString(voicePackId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

