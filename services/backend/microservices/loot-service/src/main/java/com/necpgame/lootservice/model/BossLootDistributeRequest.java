package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BossLootDistributeRequest
 */


public class BossLootDistributeRequest {

  private String partyId;

  private @Nullable String overrideWinnerId;

  private @Nullable Boolean skipSmartLoot;

  public BossLootDistributeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BossLootDistributeRequest(String partyId) {
    this.partyId = partyId;
  }

  public BossLootDistributeRequest partyId(String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @NotNull 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("partyId")
  public String getPartyId() {
    return partyId;
  }

  public void setPartyId(String partyId) {
    this.partyId = partyId;
  }

  public BossLootDistributeRequest overrideWinnerId(@Nullable String overrideWinnerId) {
    this.overrideWinnerId = overrideWinnerId;
    return this;
  }

  /**
   * Get overrideWinnerId
   * @return overrideWinnerId
   */
  
  @Schema(name = "overrideWinnerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrideWinnerId")
  public @Nullable String getOverrideWinnerId() {
    return overrideWinnerId;
  }

  public void setOverrideWinnerId(@Nullable String overrideWinnerId) {
    this.overrideWinnerId = overrideWinnerId;
  }

  public BossLootDistributeRequest skipSmartLoot(@Nullable Boolean skipSmartLoot) {
    this.skipSmartLoot = skipSmartLoot;
    return this;
  }

  /**
   * Get skipSmartLoot
   * @return skipSmartLoot
   */
  
  @Schema(name = "skipSmartLoot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skipSmartLoot")
  public @Nullable Boolean getSkipSmartLoot() {
    return skipSmartLoot;
  }

  public void setSkipSmartLoot(@Nullable Boolean skipSmartLoot) {
    this.skipSmartLoot = skipSmartLoot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BossLootDistributeRequest bossLootDistributeRequest = (BossLootDistributeRequest) o;
    return Objects.equals(this.partyId, bossLootDistributeRequest.partyId) &&
        Objects.equals(this.overrideWinnerId, bossLootDistributeRequest.overrideWinnerId) &&
        Objects.equals(this.skipSmartLoot, bossLootDistributeRequest.skipSmartLoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, overrideWinnerId, skipSmartLoot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BossLootDistributeRequest {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    overrideWinnerId: ").append(toIndentedString(overrideWinnerId)).append("\n");
    sb.append("    skipSmartLoot: ").append(toIndentedString(skipSmartLoot)).append("\n");
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

