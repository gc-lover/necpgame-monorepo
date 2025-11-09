package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RaidStatusSanityLevels
 */

@JsonTypeName("RaidStatus_sanity_levels")

public class RaidStatusSanityLevels {

  private @Nullable BigDecimal average;

  @Valid
  private List<String> critical = new ArrayList<>();

  public RaidStatusSanityLevels average(@Nullable BigDecimal average) {
    this.average = average;
    return this;
  }

  /**
   * Get average
   * @return average
   */
  @Valid 
  @Schema(name = "average", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average")
  public @Nullable BigDecimal getAverage() {
    return average;
  }

  public void setAverage(@Nullable BigDecimal average) {
    this.average = average;
  }

  public RaidStatusSanityLevels critical(List<String> critical) {
    this.critical = critical;
    return this;
  }

  public RaidStatusSanityLevels addCriticalItem(String criticalItem) {
    if (this.critical == null) {
      this.critical = new ArrayList<>();
    }
    this.critical.add(criticalItem);
    return this;
  }

  /**
   * Get critical
   * @return critical
   */
  
  @Schema(name = "critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public List<String> getCritical() {
    return critical;
  }

  public void setCritical(List<String> critical) {
    this.critical = critical;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RaidStatusSanityLevels raidStatusSanityLevels = (RaidStatusSanityLevels) o;
    return Objects.equals(this.average, raidStatusSanityLevels.average) &&
        Objects.equals(this.critical, raidStatusSanityLevels.critical);
  }

  @Override
  public int hashCode() {
    return Objects.hash(average, critical);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidStatusSanityLevels {\n");
    sb.append("    average: ").append(toIndentedString(average)).append("\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
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

