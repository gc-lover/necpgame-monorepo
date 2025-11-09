package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ExternalFlag;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerCondition
 */


public class TriggerCondition {

  private @Nullable ExternalFlag flag;

  private @Nullable Float threshold;

  /**
   * Gets or Sets operator
   */
  public enum OperatorEnum {
    GREATER_OR_EQUAL("GREATER_OR_EQUAL"),
    
    LESS_OR_EQUAL("LESS_OR_EQUAL"),
    
    EQUAL("EQUAL"),
    
    NOT_EQUAL("NOT_EQUAL");

    private final String value;

    OperatorEnum(String value) {
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
    public static OperatorEnum fromValue(String value) {
      for (OperatorEnum b : OperatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OperatorEnum operator;

  public TriggerCondition flag(@Nullable ExternalFlag flag) {
    this.flag = flag;
    return this;
  }

  /**
   * Get flag
   * @return flag
   */
  @Valid 
  @Schema(name = "flag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flag")
  public @Nullable ExternalFlag getFlag() {
    return flag;
  }

  public void setFlag(@Nullable ExternalFlag flag) {
    this.flag = flag;
  }

  public TriggerCondition threshold(@Nullable Float threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable Float getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable Float threshold) {
    this.threshold = threshold;
  }

  public TriggerCondition operator(@Nullable OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

  /**
   * Get operator
   * @return operator
   */
  
  @Schema(name = "operator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operator")
  public @Nullable OperatorEnum getOperator() {
    return operator;
  }

  public void setOperator(@Nullable OperatorEnum operator) {
    this.operator = operator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerCondition triggerCondition = (TriggerCondition) o;
    return Objects.equals(this.flag, triggerCondition.flag) &&
        Objects.equals(this.threshold, triggerCondition.threshold) &&
        Objects.equals(this.operator, triggerCondition.operator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flag, threshold, operator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerCondition {\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
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

