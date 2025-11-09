package com.necpgame.backjava.model;

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
 * ChallengeObjective
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeObjective {

  private String metric;

  private Integer target;

  private @Nullable Integer current;

  public ChallengeObjective() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChallengeObjective(String metric, Integer target) {
    this.metric = metric;
    this.target = target;
  }

  public ChallengeObjective metric(String metric) {
    this.metric = metric;
    return this;
  }

  /**
   * Get metric
   * @return metric
   */
  @NotNull 
  @Schema(name = "metric", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metric")
  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public ChallengeObjective target(Integer target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  @NotNull 
  @Schema(name = "target", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target")
  public Integer getTarget() {
    return target;
  }

  public void setTarget(Integer target) {
    this.target = target;
  }

  public ChallengeObjective current(@Nullable Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable Integer getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable Integer current) {
    this.current = current;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeObjective challengeObjective = (ChallengeObjective) o;
    return Objects.equals(this.metric, challengeObjective.metric) &&
        Objects.equals(this.target, challengeObjective.target) &&
        Objects.equals(this.current, challengeObjective.current);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metric, target, current);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeObjective {\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
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

