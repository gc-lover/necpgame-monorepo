package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChooseFloorApproach200Response
 */

@JsonTypeName("chooseFloorApproach_200_response")

public class ChooseFloorApproach200Response {

  private @Nullable Integer floorNumber;

  private @Nullable String approach;

  private @Nullable BigDecimal difficultyModifier;

  public ChooseFloorApproach200Response floorNumber(@Nullable Integer floorNumber) {
    this.floorNumber = floorNumber;
    return this;
  }

  /**
   * Get floorNumber
   * @return floorNumber
   */
  
  @Schema(name = "floor_number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("floor_number")
  public @Nullable Integer getFloorNumber() {
    return floorNumber;
  }

  public void setFloorNumber(@Nullable Integer floorNumber) {
    this.floorNumber = floorNumber;
  }

  public ChooseFloorApproach200Response approach(@Nullable String approach) {
    this.approach = approach;
    return this;
  }

  /**
   * Get approach
   * @return approach
   */
  
  @Schema(name = "approach", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approach")
  public @Nullable String getApproach() {
    return approach;
  }

  public void setApproach(@Nullable String approach) {
    this.approach = approach;
  }

  public ChooseFloorApproach200Response difficultyModifier(@Nullable BigDecimal difficultyModifier) {
    this.difficultyModifier = difficultyModifier;
    return this;
  }

  /**
   * Модификатор сложности
   * @return difficultyModifier
   */
  @Valid 
  @Schema(name = "difficulty_modifier", description = "Модификатор сложности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty_modifier")
  public @Nullable BigDecimal getDifficultyModifier() {
    return difficultyModifier;
  }

  public void setDifficultyModifier(@Nullable BigDecimal difficultyModifier) {
    this.difficultyModifier = difficultyModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChooseFloorApproach200Response chooseFloorApproach200Response = (ChooseFloorApproach200Response) o;
    return Objects.equals(this.floorNumber, chooseFloorApproach200Response.floorNumber) &&
        Objects.equals(this.approach, chooseFloorApproach200Response.approach) &&
        Objects.equals(this.difficultyModifier, chooseFloorApproach200Response.difficultyModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(floorNumber, approach, difficultyModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChooseFloorApproach200Response {\n");
    sb.append("    floorNumber: ").append(toIndentedString(floorNumber)).append("\n");
    sb.append("    approach: ").append(toIndentedString(approach)).append("\n");
    sb.append("    difficultyModifier: ").append(toIndentedString(difficultyModifier)).append("\n");
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

