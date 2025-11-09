package com.necpgame.socialservice.model;

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
 * RatingWarning
 */


public class RatingWarning {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    DECAY_HIGH("decay_high"),
    
    DISPUTE_SPIKE("dispute_spike"),
    
    LATE_PAYMENTS("late_payments"),
    
    COMMUNICATION_LOW("communication_low"),
    
    REWARD_UNFAIR("reward_unfair"),
    
    SANCTION_ACTIVE("sanction_active");

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

  private String messageKey;

  private @Nullable String defaultMessage;

  private @Nullable String suggestedAction;

  public RatingWarning() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingWarning(CodeEnum code, SeverityEnum severity, String messageKey) {
    this.code = code;
    this.severity = severity;
    this.messageKey = messageKey;
  }

  public RatingWarning code(CodeEnum code) {
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

  public RatingWarning severity(SeverityEnum severity) {
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

  public RatingWarning messageKey(String messageKey) {
    this.messageKey = messageKey;
    return this;
  }

  /**
   * Get messageKey
   * @return messageKey
   */
  @NotNull 
  @Schema(name = "messageKey", example = "ratings.warning.decay_high", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messageKey")
  public String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  public RatingWarning defaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
    return this;
  }

  /**
   * Get defaultMessage
   * @return defaultMessage
   */
  
  @Schema(name = "defaultMessage", example = "Рейтинг снизился из-за продолжительной неактивности.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultMessage")
  public @Nullable String getDefaultMessage() {
    return defaultMessage;
  }

  public void setDefaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }

  public RatingWarning suggestedAction(@Nullable String suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  /**
   * Get suggestedAction
   * @return suggestedAction
   */
  
  @Schema(name = "suggestedAction", example = "Выполните 3 заказа с высоким рейтингом в течение недели.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedAction")
  public @Nullable String getSuggestedAction() {
    return suggestedAction;
  }

  public void setSuggestedAction(@Nullable String suggestedAction) {
    this.suggestedAction = suggestedAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingWarning ratingWarning = (RatingWarning) o;
    return Objects.equals(this.code, ratingWarning.code) &&
        Objects.equals(this.severity, ratingWarning.severity) &&
        Objects.equals(this.messageKey, ratingWarning.messageKey) &&
        Objects.equals(this.defaultMessage, ratingWarning.defaultMessage) &&
        Objects.equals(this.suggestedAction, ratingWarning.suggestedAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, severity, messageKey, defaultMessage, suggestedAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingWarning {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    messageKey: ").append(toIndentedString(messageKey)).append("\n");
    sb.append("    defaultMessage: ").append(toIndentedString(defaultMessage)).append("\n");
    sb.append("    suggestedAction: ").append(toIndentedString(suggestedAction)).append("\n");
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

