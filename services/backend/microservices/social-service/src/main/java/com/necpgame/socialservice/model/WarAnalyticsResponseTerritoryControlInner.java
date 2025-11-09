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
 * WarAnalyticsResponseTerritoryControlInner
 */

@JsonTypeName("WarAnalyticsResponse_territoryControl_inner")

public class WarAnalyticsResponseTerritoryControlInner {

  private @Nullable String territoryId;

  private @Nullable String ownerClanId;

  private @Nullable BigDecimal controlPercent;

  public WarAnalyticsResponseTerritoryControlInner territoryId(@Nullable String territoryId) {
    this.territoryId = territoryId;
    return this;
  }

  /**
   * Get territoryId
   * @return territoryId
   */
  
  @Schema(name = "territoryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoryId")
  public @Nullable String getTerritoryId() {
    return territoryId;
  }

  public void setTerritoryId(@Nullable String territoryId) {
    this.territoryId = territoryId;
  }

  public WarAnalyticsResponseTerritoryControlInner ownerClanId(@Nullable String ownerClanId) {
    this.ownerClanId = ownerClanId;
    return this;
  }

  /**
   * Get ownerClanId
   * @return ownerClanId
   */
  
  @Schema(name = "ownerClanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerClanId")
  public @Nullable String getOwnerClanId() {
    return ownerClanId;
  }

  public void setOwnerClanId(@Nullable String ownerClanId) {
    this.ownerClanId = ownerClanId;
  }

  public WarAnalyticsResponseTerritoryControlInner controlPercent(@Nullable BigDecimal controlPercent) {
    this.controlPercent = controlPercent;
    return this;
  }

  /**
   * Get controlPercent
   * @return controlPercent
   */
  @Valid 
  @Schema(name = "controlPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlPercent")
  public @Nullable BigDecimal getControlPercent() {
    return controlPercent;
  }

  public void setControlPercent(@Nullable BigDecimal controlPercent) {
    this.controlPercent = controlPercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarAnalyticsResponseTerritoryControlInner warAnalyticsResponseTerritoryControlInner = (WarAnalyticsResponseTerritoryControlInner) o;
    return Objects.equals(this.territoryId, warAnalyticsResponseTerritoryControlInner.territoryId) &&
        Objects.equals(this.ownerClanId, warAnalyticsResponseTerritoryControlInner.ownerClanId) &&
        Objects.equals(this.controlPercent, warAnalyticsResponseTerritoryControlInner.controlPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territoryId, ownerClanId, controlPercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarAnalyticsResponseTerritoryControlInner {\n");
    sb.append("    territoryId: ").append(toIndentedString(territoryId)).append("\n");
    sb.append("    ownerClanId: ").append(toIndentedString(ownerClanId)).append("\n");
    sb.append("    controlPercent: ").append(toIndentedString(controlPercent)).append("\n");
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

