package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ScheduleConfig
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ScheduleConfig {

  private @Nullable String cronExpression;

  private @Nullable String timezone;

  private @Nullable Boolean enabled;

  @Valid
  private List<String> itemsToReset = new ArrayList<>();

  public ScheduleConfig cronExpression(@Nullable String cronExpression) {
    this.cronExpression = cronExpression;
    return this;
  }

  /**
   * Cron выражение для расписания
   * @return cronExpression
   */
  
  @Schema(name = "cron_expression", example = "0 0 * * *", description = "Cron выражение для расписания", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cron_expression")
  public @Nullable String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(@Nullable String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public ScheduleConfig timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", example = "UTC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  public ScheduleConfig enabled(@Nullable Boolean enabled) {
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

  public ScheduleConfig itemsToReset(List<String> itemsToReset) {
    this.itemsToReset = itemsToReset;
    return this;
  }

  public ScheduleConfig addItemsToResetItem(String itemsToResetItem) {
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
  
  @Schema(name = "items_to_reset", example = "[\"quests\",\"limits\",\"bonuses\",\"vendors\",\"instances\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    ScheduleConfig scheduleConfig = (ScheduleConfig) o;
    return Objects.equals(this.cronExpression, scheduleConfig.cronExpression) &&
        Objects.equals(this.timezone, scheduleConfig.timezone) &&
        Objects.equals(this.enabled, scheduleConfig.enabled) &&
        Objects.equals(this.itemsToReset, scheduleConfig.itemsToReset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cronExpression, timezone, enabled, itemsToReset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleConfig {\n");
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

