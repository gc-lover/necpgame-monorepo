package com.necpgame.socialservice.model;

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
 * PlayerOrderDetailedAllOfEscrow
 */

@JsonTypeName("PlayerOrderDetailed_allOf_escrow")

public class PlayerOrderDetailedAllOfEscrow {

  private @Nullable Integer amountHeld;

  private @Nullable String releaseCondition;

  public PlayerOrderDetailedAllOfEscrow amountHeld(@Nullable Integer amountHeld) {
    this.amountHeld = amountHeld;
    return this;
  }

  /**
   * Get amountHeld
   * @return amountHeld
   */
  
  @Schema(name = "amount_held", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_held")
  public @Nullable Integer getAmountHeld() {
    return amountHeld;
  }

  public void setAmountHeld(@Nullable Integer amountHeld) {
    this.amountHeld = amountHeld;
  }

  public PlayerOrderDetailedAllOfEscrow releaseCondition(@Nullable String releaseCondition) {
    this.releaseCondition = releaseCondition;
    return this;
  }

  /**
   * Get releaseCondition
   * @return releaseCondition
   */
  
  @Schema(name = "release_condition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("release_condition")
  public @Nullable String getReleaseCondition() {
    return releaseCondition;
  }

  public void setReleaseCondition(@Nullable String releaseCondition) {
    this.releaseCondition = releaseCondition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderDetailedAllOfEscrow playerOrderDetailedAllOfEscrow = (PlayerOrderDetailedAllOfEscrow) o;
    return Objects.equals(this.amountHeld, playerOrderDetailedAllOfEscrow.amountHeld) &&
        Objects.equals(this.releaseCondition, playerOrderDetailedAllOfEscrow.releaseCondition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amountHeld, releaseCondition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderDetailedAllOfEscrow {\n");
    sb.append("    amountHeld: ").append(toIndentedString(amountHeld)).append("\n");
    sb.append("    releaseCondition: ").append(toIndentedString(releaseCondition)).append("\n");
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

