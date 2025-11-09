package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RatingCategoriesResponseDecayDefaults
 */

@JsonTypeName("RatingCategoriesResponse_decayDefaults")

public class RatingCategoriesResponseDecayDefaults {

  private @Nullable Integer inactivityGraceDays;

  private @Nullable Float minimumFloor;

  public RatingCategoriesResponseDecayDefaults inactivityGraceDays(@Nullable Integer inactivityGraceDays) {
    this.inactivityGraceDays = inactivityGraceDays;
    return this;
  }

  /**
   * Get inactivityGraceDays
   * minimum: 0
   * @return inactivityGraceDays
   */
  @Min(value = 0) 
  @Schema(name = "inactivityGraceDays", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inactivityGraceDays")
  public @Nullable Integer getInactivityGraceDays() {
    return inactivityGraceDays;
  }

  public void setInactivityGraceDays(@Nullable Integer inactivityGraceDays) {
    this.inactivityGraceDays = inactivityGraceDays;
  }

  public RatingCategoriesResponseDecayDefaults minimumFloor(@Nullable Float minimumFloor) {
    this.minimumFloor = minimumFloor;
    return this;
  }

  /**
   * Get minimumFloor
   * minimum: 0
   * maximum: 100
   * @return minimumFloor
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "minimumFloor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minimumFloor")
  public @Nullable Float getMinimumFloor() {
    return minimumFloor;
  }

  public void setMinimumFloor(@Nullable Float minimumFloor) {
    this.minimumFloor = minimumFloor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingCategoriesResponseDecayDefaults ratingCategoriesResponseDecayDefaults = (RatingCategoriesResponseDecayDefaults) o;
    return Objects.equals(this.inactivityGraceDays, ratingCategoriesResponseDecayDefaults.inactivityGraceDays) &&
        Objects.equals(this.minimumFloor, ratingCategoriesResponseDecayDefaults.minimumFloor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inactivityGraceDays, minimumFloor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingCategoriesResponseDecayDefaults {\n");
    sb.append("    inactivityGraceDays: ").append(toIndentedString(inactivityGraceDays)).append("\n");
    sb.append("    minimumFloor: ").append(toIndentedString(minimumFloor)).append("\n");
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

