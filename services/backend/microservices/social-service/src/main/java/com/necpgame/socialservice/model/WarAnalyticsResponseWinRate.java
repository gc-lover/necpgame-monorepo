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
 * WarAnalyticsResponseWinRate
 */

@JsonTypeName("WarAnalyticsResponse_winRate")

public class WarAnalyticsResponseWinRate {

  private @Nullable BigDecimal attacker;

  private @Nullable BigDecimal defender;

  public WarAnalyticsResponseWinRate attacker(@Nullable BigDecimal attacker) {
    this.attacker = attacker;
    return this;
  }

  /**
   * Get attacker
   * @return attacker
   */
  @Valid 
  @Schema(name = "attacker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attacker")
  public @Nullable BigDecimal getAttacker() {
    return attacker;
  }

  public void setAttacker(@Nullable BigDecimal attacker) {
    this.attacker = attacker;
  }

  public WarAnalyticsResponseWinRate defender(@Nullable BigDecimal defender) {
    this.defender = defender;
    return this;
  }

  /**
   * Get defender
   * @return defender
   */
  @Valid 
  @Schema(name = "defender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defender")
  public @Nullable BigDecimal getDefender() {
    return defender;
  }

  public void setDefender(@Nullable BigDecimal defender) {
    this.defender = defender;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarAnalyticsResponseWinRate warAnalyticsResponseWinRate = (WarAnalyticsResponseWinRate) o;
    return Objects.equals(this.attacker, warAnalyticsResponseWinRate.attacker) &&
        Objects.equals(this.defender, warAnalyticsResponseWinRate.defender);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attacker, defender);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarAnalyticsResponseWinRate {\n");
    sb.append("    attacker: ").append(toIndentedString(attacker)).append("\n");
    sb.append("    defender: ").append(toIndentedString(defender)).append("\n");
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

