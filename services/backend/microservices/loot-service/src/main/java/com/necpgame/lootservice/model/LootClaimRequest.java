package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootClaimRequest
 */


public class LootClaimRequest {

  /**
   * Gets or Sets claimMode
   */
  public enum ClaimModeEnum {
    AUTO("AUTO"),
    
    NEED_GREED("NEED_GREED"),
    
    DIRECT_ASSIGN("DIRECT_ASSIGN");

    private final String value;

    ClaimModeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ClaimModeEnum fromValue(String value) {
      for (ClaimModeEnum b : ClaimModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ClaimModeEnum claimMode;

  private @Nullable String partyId;

  private @Nullable String initiatorId;

  private @Nullable Boolean smartLootOverride;

  public LootClaimRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootClaimRequest(ClaimModeEnum claimMode) {
    this.claimMode = claimMode;
  }

  public LootClaimRequest claimMode(ClaimModeEnum claimMode) {
    this.claimMode = claimMode;
    return this;
  }

  /**
   * Get claimMode
   * @return claimMode
   */
  @NotNull 
  @Schema(name = "claimMode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("claimMode")
  public ClaimModeEnum getClaimMode() {
    return claimMode;
  }

  public void setClaimMode(ClaimModeEnum claimMode) {
    this.claimMode = claimMode;
  }

  public LootClaimRequest partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public LootClaimRequest initiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
    return this;
  }

  /**
   * Get initiatorId
   * @return initiatorId
   */
  
  @Schema(name = "initiatorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatorId")
  public @Nullable String getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
  }

  public LootClaimRequest smartLootOverride(@Nullable Boolean smartLootOverride) {
    this.smartLootOverride = smartLootOverride;
    return this;
  }

  /**
   * Get smartLootOverride
   * @return smartLootOverride
   */
  
  @Schema(name = "smartLootOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smartLootOverride")
  public @Nullable Boolean getSmartLootOverride() {
    return smartLootOverride;
  }

  public void setSmartLootOverride(@Nullable Boolean smartLootOverride) {
    this.smartLootOverride = smartLootOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootClaimRequest lootClaimRequest = (LootClaimRequest) o;
    return Objects.equals(this.claimMode, lootClaimRequest.claimMode) &&
        Objects.equals(this.partyId, lootClaimRequest.partyId) &&
        Objects.equals(this.initiatorId, lootClaimRequest.initiatorId) &&
        Objects.equals(this.smartLootOverride, lootClaimRequest.smartLootOverride);
  }

  @Override
  public int hashCode() {
    return Objects.hash(claimMode, partyId, initiatorId, smartLootOverride);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootClaimRequest {\n");
    sb.append("    claimMode: ").append(toIndentedString(claimMode)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    initiatorId: ").append(toIndentedString(initiatorId)).append("\n");
    sb.append("    smartLootOverride: ").append(toIndentedString(smartLootOverride)).append("\n");
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

