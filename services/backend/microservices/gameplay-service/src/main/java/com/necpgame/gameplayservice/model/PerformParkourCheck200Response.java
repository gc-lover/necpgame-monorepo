package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * PerformParkourCheck200Response
 */

@JsonTypeName("performParkourCheck_200_response")

public class PerformParkourCheck200Response {

  private @Nullable Boolean success;

  private @Nullable Integer rollResult;

  private @Nullable Integer dc;

  /**
   * Gets or Sets consequences
   */
  public enum ConsequencesEnum {
    SUCCESS("success"),
    
    PARTIAL_SUCCESS("partial_success"),
    
    FAILURE("failure"),
    
    CRITICAL_FAILURE("critical_failure");

    private final String value;

    ConsequencesEnum(String value) {
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
    public static ConsequencesEnum fromValue(String value) {
      for (ConsequencesEnum b : ConsequencesEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConsequencesEnum consequences;

  public PerformParkourCheck200Response success(@Nullable Boolean success) {
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

  public PerformParkourCheck200Response rollResult(@Nullable Integer rollResult) {
    this.rollResult = rollResult;
    return this;
  }

  /**
   * Get rollResult
   * @return rollResult
   */
  
  @Schema(name = "roll_result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_result")
  public @Nullable Integer getRollResult() {
    return rollResult;
  }

  public void setRollResult(@Nullable Integer rollResult) {
    this.rollResult = rollResult;
  }

  public PerformParkourCheck200Response dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  public PerformParkourCheck200Response consequences(@Nullable ConsequencesEnum consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable ConsequencesEnum getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable ConsequencesEnum consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformParkourCheck200Response performParkourCheck200Response = (PerformParkourCheck200Response) o;
    return Objects.equals(this.success, performParkourCheck200Response.success) &&
        Objects.equals(this.rollResult, performParkourCheck200Response.rollResult) &&
        Objects.equals(this.dc, performParkourCheck200Response.dc) &&
        Objects.equals(this.consequences, performParkourCheck200Response.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, rollResult, dc, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformParkourCheck200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    rollResult: ").append(toIndentedString(rollResult)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

