package com.necpgame.worldservice.model;

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
 * EventResolutionResultSkillCheckResult
 */

@JsonTypeName("EventResolutionResult_skill_check_result")

public class EventResolutionResultSkillCheckResult {

  private @Nullable Boolean success;

  private @Nullable Integer roll;

  private @Nullable Integer modifier;

  public EventResolutionResultSkillCheckResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public EventResolutionResultSkillCheckResult roll(@Nullable Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Get roll
   * @return roll
   */
  
  @Schema(name = "roll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable Integer getRoll() {
    return roll;
  }

  public void setRoll(@Nullable Integer roll) {
    this.roll = roll;
  }

  public EventResolutionResultSkillCheckResult modifier(@Nullable Integer modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Get modifier
   * @return modifier
   */
  
  @Schema(name = "modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable Integer getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable Integer modifier) {
    this.modifier = modifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventResolutionResultSkillCheckResult eventResolutionResultSkillCheckResult = (EventResolutionResultSkillCheckResult) o;
    return Objects.equals(this.success, eventResolutionResultSkillCheckResult.success) &&
        Objects.equals(this.roll, eventResolutionResultSkillCheckResult.roll) &&
        Objects.equals(this.modifier, eventResolutionResultSkillCheckResult.modifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, roll, modifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventResolutionResultSkillCheckResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
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

