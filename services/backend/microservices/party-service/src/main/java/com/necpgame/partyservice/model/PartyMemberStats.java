package com.necpgame.partyservice.model;

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
 * PartyMemberStats
 */

@JsonTypeName("PartyMember_stats")

public class PartyMemberStats {

  private @Nullable Integer gearScore;

  private @Nullable BigDecimal averageDps;

  public PartyMemberStats gearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
    return this;
  }

  /**
   * Get gearScore
   * @return gearScore
   */
  
  @Schema(name = "gearScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gearScore")
  public @Nullable Integer getGearScore() {
    return gearScore;
  }

  public void setGearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
  }

  public PartyMemberStats averageDps(@Nullable BigDecimal averageDps) {
    this.averageDps = averageDps;
    return this;
  }

  /**
   * Get averageDps
   * @return averageDps
   */
  @Valid 
  @Schema(name = "averageDps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageDps")
  public @Nullable BigDecimal getAverageDps() {
    return averageDps;
  }

  public void setAverageDps(@Nullable BigDecimal averageDps) {
    this.averageDps = averageDps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyMemberStats partyMemberStats = (PartyMemberStats) o;
    return Objects.equals(this.gearScore, partyMemberStats.gearScore) &&
        Objects.equals(this.averageDps, partyMemberStats.averageDps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gearScore, averageDps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyMemberStats {\n");
    sb.append("    gearScore: ").append(toIndentedString(gearScore)).append("\n");
    sb.append("    averageDps: ").append(toIndentedString(averageDps)).append("\n");
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

