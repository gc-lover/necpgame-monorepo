package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * UpdateScheduleRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UpdateScheduleRequest {

  /**
   * Gets or Sets resetType
   */
  public enum ResetTypeEnum {
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    MONTHLY("MONTHLY");

    private final String value;

    ResetTypeEnum(String value) {
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
    public static ResetTypeEnum fromValue(String value) {
      for (ResetTypeEnum b : ResetTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResetTypeEnum resetType;

  private @Nullable String cronExpression;

  private @Nullable String timezone;

  private @Nullable Boolean enabled;

  @Valid
  private List<String> itemsToReset = new ArrayList<>();

  public UpdateScheduleRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateScheduleRequest(ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public UpdateScheduleRequest resetType(ResetTypeEnum resetType) {
    this.resetType = resetType;
    return this;
  }

  /**
   * Get resetType
   * @return resetType
   */
  @NotNull 
  @Schema(name = "reset_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reset_type")
  public ResetTypeEnum getResetType() {
    return resetType;
  }

  public void setResetType(ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public UpdateScheduleRequest cronExpression(@Nullable String cronExpression) {
    this.cronExpression = cronExpression;
    return this;
  }

  /**
   * Get cronExpression
   * @return cronExpression
   */
  
  @Schema(name = "cron_expression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cron_expression")
  public @Nullable String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(@Nullable String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public UpdateScheduleRequest timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  public UpdateScheduleRequest enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public UpdateScheduleRequest itemsToReset(List<String> itemsToReset) {
    this.itemsToReset = itemsToReset;
    return this;
  }

  public UpdateScheduleRequest addItemsToResetItem(String itemsToResetItem) {
    if (this.itemsToReset == null) {
      this.itemsToReset = new ArrayList<>();
    }
    this.itemsToReset.add(itemsToResetItem);
    return this;
  }

  /**
   * Get itemsToReset
   * @return itemsToReset
   */
  
  @Schema(name = "items_to_reset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_to_reset")
  public List<String> getItemsToReset() {
    return itemsToReset;
  }

  public void setItemsToReset(List<String> itemsToReset) {
    this.itemsToReset = itemsToReset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateScheduleRequest updateScheduleRequest = (UpdateScheduleRequest) o;
    return Objects.equals(this.resetType, updateScheduleRequest.resetType) &&
        Objects.equals(this.cronExpression, updateScheduleRequest.cronExpression) &&
        Objects.equals(this.timezone, updateScheduleRequest.timezone) &&
        Objects.equals(this.enabled, updateScheduleRequest.enabled) &&
        Objects.equals(this.itemsToReset, updateScheduleRequest.itemsToReset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetType, cronExpression, timezone, enabled, itemsToReset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateScheduleRequest {\n");
    sb.append("    resetType: ").append(toIndentedString(resetType)).append("\n");
    sb.append("    cronExpression: ").append(toIndentedString(cronExpression)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    itemsToReset: ").append(toIndentedString(itemsToReset)).append("\n");
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

