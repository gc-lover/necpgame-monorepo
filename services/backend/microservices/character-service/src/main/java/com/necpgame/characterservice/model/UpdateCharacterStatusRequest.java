package com.necpgame.characterservice.model;

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
 * UpdateCharacterStatusRequest
 */

@JsonTypeName("updateCharacterStatus_request")

public class UpdateCharacterStatusRequest {

  private JsonNullable<Integer> healthDelta = JsonNullable.<Integer>undefined();

  private JsonNullable<Integer> energyDelta = JsonNullable.<Integer>undefined();

  private JsonNullable<Integer> humanityDelta = JsonNullable.<Integer>undefined();

  private JsonNullable<Integer> experienceDelta = JsonNullable.<Integer>undefined();

  public UpdateCharacterStatusRequest healthDelta(Integer healthDelta) {
    this.healthDelta = JsonNullable.of(healthDelta);
    return this;
  }

  /**
   * Get healthDelta
   * @return healthDelta
   */
  
  @Schema(name = "healthDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("healthDelta")
  public JsonNullable<Integer> getHealthDelta() {
    return healthDelta;
  }

  public void setHealthDelta(JsonNullable<Integer> healthDelta) {
    this.healthDelta = healthDelta;
  }

  public UpdateCharacterStatusRequest energyDelta(Integer energyDelta) {
    this.energyDelta = JsonNullable.of(energyDelta);
    return this;
  }

  /**
   * Get energyDelta
   * @return energyDelta
   */
  
  @Schema(name = "energyDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyDelta")
  public JsonNullable<Integer> getEnergyDelta() {
    return energyDelta;
  }

  public void setEnergyDelta(JsonNullable<Integer> energyDelta) {
    this.energyDelta = energyDelta;
  }

  public UpdateCharacterStatusRequest humanityDelta(Integer humanityDelta) {
    this.humanityDelta = JsonNullable.of(humanityDelta);
    return this;
  }

  /**
   * Get humanityDelta
   * @return humanityDelta
   */
  
  @Schema(name = "humanityDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanityDelta")
  public JsonNullable<Integer> getHumanityDelta() {
    return humanityDelta;
  }

  public void setHumanityDelta(JsonNullable<Integer> humanityDelta) {
    this.humanityDelta = humanityDelta;
  }

  public UpdateCharacterStatusRequest experienceDelta(Integer experienceDelta) {
    this.experienceDelta = JsonNullable.of(experienceDelta);
    return this;
  }

  /**
   * Get experienceDelta
   * @return experienceDelta
   */
  
  @Schema(name = "experienceDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experienceDelta")
  public JsonNullable<Integer> getExperienceDelta() {
    return experienceDelta;
  }

  public void setExperienceDelta(JsonNullable<Integer> experienceDelta) {
    this.experienceDelta = experienceDelta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateCharacterStatusRequest updateCharacterStatusRequest = (UpdateCharacterStatusRequest) o;
    return equalsNullable(this.healthDelta, updateCharacterStatusRequest.healthDelta) &&
        equalsNullable(this.energyDelta, updateCharacterStatusRequest.energyDelta) &&
        equalsNullable(this.humanityDelta, updateCharacterStatusRequest.humanityDelta) &&
        equalsNullable(this.experienceDelta, updateCharacterStatusRequest.experienceDelta);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(healthDelta), hashCodeNullable(energyDelta), hashCodeNullable(humanityDelta), hashCodeNullable(experienceDelta));
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
    sb.append("class UpdateCharacterStatusRequest {\n");
    sb.append("    healthDelta: ").append(toIndentedString(healthDelta)).append("\n");
    sb.append("    energyDelta: ").append(toIndentedString(energyDelta)).append("\n");
    sb.append("    humanityDelta: ").append(toIndentedString(humanityDelta)).append("\n");
    sb.append("    experienceDelta: ").append(toIndentedString(experienceDelta)).append("\n");
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

