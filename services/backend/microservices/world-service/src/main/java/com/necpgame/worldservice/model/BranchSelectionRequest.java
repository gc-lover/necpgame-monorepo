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
 * BranchSelectionRequest
 */


public class BranchSelectionRequest {

  private UUID playerId;

  private String branchId;

  private @Nullable String rationale;

  public BranchSelectionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BranchSelectionRequest(UUID playerId, String branchId) {
    this.playerId = playerId;
    this.branchId = branchId;
  }

  public BranchSelectionRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public BranchSelectionRequest branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

  /**
   * Get branchId
   * @return branchId
   */
  @NotNull 
  @Schema(name = "branchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("branchId")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public BranchSelectionRequest rationale(@Nullable String rationale) {
    this.rationale = rationale;
    return this;
  }

  /**
   * Get rationale
   * @return rationale
   */
  
  @Schema(name = "rationale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rationale")
  public @Nullable String getRationale() {
    return rationale;
  }

  public void setRationale(@Nullable String rationale) {
    this.rationale = rationale;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchSelectionRequest branchSelectionRequest = (BranchSelectionRequest) o;
    return Objects.equals(this.playerId, branchSelectionRequest.playerId) &&
        Objects.equals(this.branchId, branchSelectionRequest.branchId) &&
        Objects.equals(this.rationale, branchSelectionRequest.rationale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, branchId, rationale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchSelectionRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    rationale: ").append(toIndentedString(rationale)).append("\n");
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

