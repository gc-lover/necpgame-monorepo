package com.necpgame.worldservice.model;

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
 * StageObjectiveProgress
 */

@JsonTypeName("StageObjective_progress")

public class StageObjectiveProgress {

  private @Nullable BigDecimal current;

  private @Nullable BigDecimal total;

  private @Nullable BigDecimal percentage;

  public StageObjectiveProgress current(@Nullable BigDecimal current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable BigDecimal getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable BigDecimal current) {
    this.current = current;
  }

  public StageObjectiveProgress total(@Nullable BigDecimal total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  @Valid 
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable BigDecimal getTotal() {
    return total;
  }

  public void setTotal(@Nullable BigDecimal total) {
    this.total = total;
  }

  public StageObjectiveProgress percentage(@Nullable BigDecimal percentage) {
    this.percentage = percentage;
    return this;
  }

  /**
   * Get percentage
   * minimum: 0
   * maximum: 100
   * @return percentage
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentage")
  public @Nullable BigDecimal getPercentage() {
    return percentage;
  }

  public void setPercentage(@Nullable BigDecimal percentage) {
    this.percentage = percentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StageObjectiveProgress stageObjectiveProgress = (StageObjectiveProgress) o;
    return Objects.equals(this.current, stageObjectiveProgress.current) &&
        Objects.equals(this.total, stageObjectiveProgress.total) &&
        Objects.equals(this.percentage, stageObjectiveProgress.percentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, total, percentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StageObjectiveProgress {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
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

