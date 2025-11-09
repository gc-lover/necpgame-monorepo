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
 * BudgetWarning
 */


public class BudgetWarning {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BUDGET_OVERESTIMATED("BUDGET_OVERESTIMATED"),
    
    BUDGET_UNDERESTIMATED("BUDGET_UNDERESTIMATED"),
    
    MARKET_SPIKE("MARKET_SPIKE"),
    
    MARKET_DROP("MARKET_DROP"),
    
    RISK_HIGH("RISK_HIGH"),
    
    ESCROW_TOO_LOW("ESCROW_TOO_LOW"),
    
    COMMISSION_TOO_LOW("COMMISSION_TOO_LOW");

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
    
    ERROR("error");

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

  private String messageKey;

  private @Nullable String defaultMessage;

  private @Nullable String suggestedAction;

  private @Nullable Float threshold;

  public BudgetWarning() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetWarning(CodeEnum code, SeverityEnum severity, String messageKey) {
    this.code = code;
    this.severity = severity;
    this.messageKey = messageKey;
  }

  public BudgetWarning code(CodeEnum code) {
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

  public BudgetWarning severity(SeverityEnum severity) {
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

  public BudgetWarning messageKey(String messageKey) {
    this.messageKey = messageKey;
    return this;
  }

  /**
   * Get messageKey
   * @return messageKey
   */
  @NotNull @Size(min = 3, max = 128) 
  @Schema(name = "messageKey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messageKey")
  public String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  public BudgetWarning defaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
    return this;
  }

  /**
   * Get defaultMessage
   * @return defaultMessage
   */
  @Size(min = 3, max = 512) 
  @Schema(name = "defaultMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultMessage")
  public @Nullable String getDefaultMessage() {
    return defaultMessage;
  }

  public void setDefaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }

  public BudgetWarning suggestedAction(@Nullable String suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  /**
   * Get suggestedAction
   * @return suggestedAction
   */
  @Size(min = 3, max = 256) 
  @Schema(name = "suggestedAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedAction")
  public @Nullable String getSuggestedAction() {
    return suggestedAction;
  }

  public void setSuggestedAction(@Nullable String suggestedAction) {
    this.suggestedAction = suggestedAction;
  }

  public BudgetWarning threshold(@Nullable Float threshold) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetWarning budgetWarning = (BudgetWarning) o;
    return Objects.equals(this.code, budgetWarning.code) &&
        Objects.equals(this.severity, budgetWarning.severity) &&
        Objects.equals(this.messageKey, budgetWarning.messageKey) &&
        Objects.equals(this.defaultMessage, budgetWarning.defaultMessage) &&
        Objects.equals(this.suggestedAction, budgetWarning.suggestedAction) &&
        Objects.equals(this.threshold, budgetWarning.threshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, severity, messageKey, defaultMessage, suggestedAction, threshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetWarning {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    messageKey: ").append(toIndentedString(messageKey)).append("\n");
    sb.append("    defaultMessage: ").append(toIndentedString(defaultMessage)).append("\n");
    sb.append("    suggestedAction: ").append(toIndentedString(suggestedAction)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
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

