package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Tier;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SeasonResetRequestTiersMappingInner
 */

@JsonTypeName("SeasonResetRequest_tiersMapping_inner")

public class SeasonResetRequestTiersMappingInner {

  private Tier fromTier;

  private Tier toTier;

  public SeasonResetRequestTiersMappingInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SeasonResetRequestTiersMappingInner(Tier fromTier, Tier toTier) {
    this.fromTier = fromTier;
    this.toTier = toTier;
  }

  public SeasonResetRequestTiersMappingInner fromTier(Tier fromTier) {
    this.fromTier = fromTier;
    return this;
  }

  /**
   * Get fromTier
   * @return fromTier
   */
  @NotNull @Valid 
  @Schema(name = "fromTier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fromTier")
  public Tier getFromTier() {
    return fromTier;
  }

  public void setFromTier(Tier fromTier) {
    this.fromTier = fromTier;
  }

  public SeasonResetRequestTiersMappingInner toTier(Tier toTier) {
    this.toTier = toTier;
    return this;
  }

  /**
   * Get toTier
   * @return toTier
   */
  @NotNull @Valid 
  @Schema(name = "toTier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("toTier")
  public Tier getToTier() {
    return toTier;
  }

  public void setToTier(Tier toTier) {
    this.toTier = toTier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeasonResetRequestTiersMappingInner seasonResetRequestTiersMappingInner = (SeasonResetRequestTiersMappingInner) o;
    return Objects.equals(this.fromTier, seasonResetRequestTiersMappingInner.fromTier) &&
        Objects.equals(this.toTier, seasonResetRequestTiersMappingInner.toTier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromTier, toTier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeasonResetRequestTiersMappingInner {\n");
    sb.append("    fromTier: ").append(toIndentedString(fromTier)).append("\n");
    sb.append("    toTier: ").append(toIndentedString(toTier)).append("\n");
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

