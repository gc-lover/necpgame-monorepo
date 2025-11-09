package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RiskAlert
 */


public class RiskAlert {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    FRAUD_SUSPECTED("fraud_suspected"),
    
    ESCROW_REQUIRED("escrow_required"),
    
    MANUAL_REVIEW("manual_review"),
    
    BLACKLIST_MATCH("blacklist_match"),
    
    INSURANCE_REQUIRED("insurance_required");

    private final String value;

    CodeEnum(String value) {
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
    public static CodeEnum fromValue(String value) {
      for (CodeEnum b : CodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CodeEnum code;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SeverityEnum severity;

  private @Nullable String messageKey;

  private @Nullable String description;

  private @Nullable String recommendedAction;

  public RiskAlert() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskAlert(CodeEnum code, SeverityEnum severity) {
    this.code = code;
    this.severity = severity;
  }

  public RiskAlert code(CodeEnum code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public CodeEnum getCode() {
    return code;
  }

  public void setCode(CodeEnum code) {
    this.code = code;
  }

  public RiskAlert severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public RiskAlert messageKey(@Nullable String messageKey) {
    this.messageKey = messageKey;
    return this;
  }

  /**
   * Get messageKey
   * @return messageKey
   */
  
  @Schema(name = "messageKey", example = "risk.alert.manual_review", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageKey")
  public @Nullable String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(@Nullable String messageKey) {
    this.messageKey = messageKey;
  }

  public RiskAlert description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public RiskAlert recommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
    return this;
  }

  /**
   * Get recommendedAction
   * @return recommendedAction
   */
  
  @Schema(name = "recommendedAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedAction")
  public @Nullable String getRecommendedAction() {
    return recommendedAction;
  }

  public void setRecommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskAlert riskAlert = (RiskAlert) o;
    return Objects.equals(this.code, riskAlert.code) &&
        Objects.equals(this.severity, riskAlert.severity) &&
        Objects.equals(this.messageKey, riskAlert.messageKey) &&
        Objects.equals(this.description, riskAlert.description) &&
        Objects.equals(this.recommendedAction, riskAlert.recommendedAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, severity, messageKey, description, recommendedAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskAlert {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    messageKey: ").append(toIndentedString(messageKey)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    recommendedAction: ").append(toIndentedString(recommendedAction)).append("\n");
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

