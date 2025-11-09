package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformSnapshotCheckRequest
 */

@JsonTypeName("performSnapshotCheck_request")

public class PerformSnapshotCheckRequest {

  private String characterId;

  private String targetId;

  private @Nullable BigDecimal aimingTime;

  public PerformSnapshotCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformSnapshotCheckRequest(String characterId, String targetId) {
    this.characterId = characterId;
    this.targetId = targetId;
  }

  public PerformSnapshotCheckRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public PerformSnapshotCheckRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public PerformSnapshotCheckRequest aimingTime(@Nullable BigDecimal aimingTime) {
    this.aimingTime = aimingTime;
    return this;
  }

  /**
   * Время прицеливания (секунды)
   * @return aimingTime
   */
  @Valid 
  @Schema(name = "aiming_time", description = "Время прицеливания (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiming_time")
  public @Nullable BigDecimal getAimingTime() {
    return aimingTime;
  }

  public void setAimingTime(@Nullable BigDecimal aimingTime) {
    this.aimingTime = aimingTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformSnapshotCheckRequest performSnapshotCheckRequest = (PerformSnapshotCheckRequest) o;
    return Objects.equals(this.characterId, performSnapshotCheckRequest.characterId) &&
        Objects.equals(this.targetId, performSnapshotCheckRequest.targetId) &&
        Objects.equals(this.aimingTime, performSnapshotCheckRequest.aimingTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, aimingTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformSnapshotCheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    aimingTime: ").append(toIndentedString(aimingTime)).append("\n");
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

