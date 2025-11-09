package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FatigueResetRequest
 */


public class FatigueResetRequest {

  private UUID characterId;

  private String skillId;

  private @Nullable String consumableId;

  private @Nullable String reason;

  private String requestedBy;

  private @Nullable String notes;

  public FatigueResetRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FatigueResetRequest(UUID characterId, String skillId, String requestedBy) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.requestedBy = requestedBy;
  }

  public FatigueResetRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public FatigueResetRequest skillId(String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  @NotNull 
  @Schema(name = "skillId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skillId")
  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public FatigueResetRequest consumableId(@Nullable String consumableId) {
    this.consumableId = consumableId;
    return this;
  }

  /**
   * Get consumableId
   * @return consumableId
   */
  
  @Schema(name = "consumableId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consumableId")
  public @Nullable String getConsumableId() {
    return consumableId;
  }

  public void setConsumableId(@Nullable String consumableId) {
    this.consumableId = consumableId;
  }

  public FatigueResetRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public FatigueResetRequest requestedBy(String requestedBy) {
    this.requestedBy = requestedBy;
    return this;
  }

  /**
   * Get requestedBy
   * @return requestedBy
   */
  @NotNull 
  @Schema(name = "requestedBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestedBy")
  public String getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(String requestedBy) {
    this.requestedBy = requestedBy;
  }

  public FatigueResetRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FatigueResetRequest fatigueResetRequest = (FatigueResetRequest) o;
    return Objects.equals(this.characterId, fatigueResetRequest.characterId) &&
        Objects.equals(this.skillId, fatigueResetRequest.skillId) &&
        Objects.equals(this.consumableId, fatigueResetRequest.consumableId) &&
        Objects.equals(this.reason, fatigueResetRequest.reason) &&
        Objects.equals(this.requestedBy, fatigueResetRequest.requestedBy) &&
        Objects.equals(this.notes, fatigueResetRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, consumableId, reason, requestedBy, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FatigueResetRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    consumableId: ").append(toIndentedString(consumableId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

