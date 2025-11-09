package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CreateCombatSessionRequestSettings
 */

@JsonTypeName("CreateCombatSessionRequest_settings")

public class CreateCombatSessionRequestSettings {

  private Boolean turnBased = false;

  private JsonNullable<Integer> timeLimitSeconds = JsonNullable.<Integer>undefined();

  public CreateCombatSessionRequestSettings turnBased(Boolean turnBased) {
    this.turnBased = turnBased;
    return this;
  }

  /**
   * Get turnBased
   * @return turnBased
   */
  
  @Schema(name = "turn_based", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("turn_based")
  public Boolean getTurnBased() {
    return turnBased;
  }

  public void setTurnBased(Boolean turnBased) {
    this.turnBased = turnBased;
  }

  public CreateCombatSessionRequestSettings timeLimitSeconds(Integer timeLimitSeconds) {
    this.timeLimitSeconds = JsonNullable.of(timeLimitSeconds);
    return this;
  }

  /**
   * Get timeLimitSeconds
   * @return timeLimitSeconds
   */
  
  @Schema(name = "time_limit_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_limit_seconds")
  public JsonNullable<Integer> getTimeLimitSeconds() {
    return timeLimitSeconds;
  }

  public void setTimeLimitSeconds(JsonNullable<Integer> timeLimitSeconds) {
    this.timeLimitSeconds = timeLimitSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCombatSessionRequestSettings createCombatSessionRequestSettings = (CreateCombatSessionRequestSettings) o;
    return Objects.equals(this.turnBased, createCombatSessionRequestSettings.turnBased) &&
        equalsNullable(this.timeLimitSeconds, createCombatSessionRequestSettings.timeLimitSeconds);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(turnBased, hashCodeNullable(timeLimitSeconds));
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
    sb.append("class CreateCombatSessionRequestSettings {\n");
    sb.append("    turnBased: ").append(toIndentedString(turnBased)).append("\n");
    sb.append("    timeLimitSeconds: ").append(toIndentedString(timeLimitSeconds)).append("\n");
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

