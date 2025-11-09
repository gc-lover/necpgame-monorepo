package com.necpgame.socialservice.model;

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
 * ModifyReputationRequest
 */

@JsonTypeName("modifyReputation_request")

public class ModifyReputationRequest {

  private String characterId;

  private String factionId;

  private BigDecimal change;

  private @Nullable String reason;

  public ModifyReputationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModifyReputationRequest(String characterId, String factionId, BigDecimal change) {
    this.characterId = characterId;
    this.factionId = factionId;
    this.change = change;
  }

  public ModifyReputationRequest characterId(String characterId) {
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

  public ModifyReputationRequest factionId(String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull 
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("faction_id")
  public String getFactionId() {
    return factionId;
  }

  public void setFactionId(String factionId) {
    this.factionId = factionId;
  }

  public ModifyReputationRequest change(BigDecimal change) {
    this.change = change;
    return this;
  }

  /**
   * Get change
   * @return change
   */
  @NotNull @Valid 
  @Schema(name = "change", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("change")
  public BigDecimal getChange() {
    return change;
  }

  public void setChange(BigDecimal change) {
    this.change = change;
  }

  public ModifyReputationRequest reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModifyReputationRequest modifyReputationRequest = (ModifyReputationRequest) o;
    return Objects.equals(this.characterId, modifyReputationRequest.characterId) &&
        Objects.equals(this.factionId, modifyReputationRequest.factionId) &&
        Objects.equals(this.change, modifyReputationRequest.change) &&
        Objects.equals(this.reason, modifyReputationRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, factionId, change, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModifyReputationRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    change: ").append(toIndentedString(change)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

