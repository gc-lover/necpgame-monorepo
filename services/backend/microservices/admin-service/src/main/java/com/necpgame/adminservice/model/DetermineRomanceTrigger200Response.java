package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DetermineRomanceTrigger200Response
 */

@JsonTypeName("determineRomanceTrigger_200_response")

public class DetermineRomanceTrigger200Response {

  private @Nullable Boolean shouldTrigger;

  private JsonNullable<String> eventId = JsonNullable.<String>undefined();

  private @Nullable BigDecimal triggerProbability;

  public DetermineRomanceTrigger200Response shouldTrigger(@Nullable Boolean shouldTrigger) {
    this.shouldTrigger = shouldTrigger;
    return this;
  }

  /**
   * Get shouldTrigger
   * @return shouldTrigger
   */
  
  @Schema(name = "should_trigger", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("should_trigger")
  public @Nullable Boolean getShouldTrigger() {
    return shouldTrigger;
  }

  public void setShouldTrigger(@Nullable Boolean shouldTrigger) {
    this.shouldTrigger = shouldTrigger;
  }

  public DetermineRomanceTrigger200Response eventId(String eventId) {
    this.eventId = JsonNullable.of(eventId);
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public JsonNullable<String> getEventId() {
    return eventId;
  }

  public void setEventId(JsonNullable<String> eventId) {
    this.eventId = eventId;
  }

  public DetermineRomanceTrigger200Response triggerProbability(@Nullable BigDecimal triggerProbability) {
    this.triggerProbability = triggerProbability;
    return this;
  }

  /**
   * Get triggerProbability
   * @return triggerProbability
   */
  @Valid 
  @Schema(name = "trigger_probability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_probability")
  public @Nullable BigDecimal getTriggerProbability() {
    return triggerProbability;
  }

  public void setTriggerProbability(@Nullable BigDecimal triggerProbability) {
    this.triggerProbability = triggerProbability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetermineRomanceTrigger200Response determineRomanceTrigger200Response = (DetermineRomanceTrigger200Response) o;
    return Objects.equals(this.shouldTrigger, determineRomanceTrigger200Response.shouldTrigger) &&
        equalsNullable(this.eventId, determineRomanceTrigger200Response.eventId) &&
        Objects.equals(this.triggerProbability, determineRomanceTrigger200Response.triggerProbability);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(shouldTrigger, hashCodeNullable(eventId), triggerProbability);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetermineRomanceTrigger200Response {\n");
    sb.append("    shouldTrigger: ").append(toIndentedString(shouldTrigger)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    triggerProbability: ").append(toIndentedString(triggerProbability)).append("\n");
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

