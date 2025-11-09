package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LeadershipRecord
 */


public class LeadershipRecord {

  private @Nullable String name;

  private @Nullable String role;

  private JsonNullable<Integer> startYear = JsonNullable.<Integer>undefined();

  private JsonNullable<Integer> endYear = JsonNullable.<Integer>undefined();

  public LeadershipRecord name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public LeadershipRecord role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public LeadershipRecord startYear(Integer startYear) {
    this.startYear = JsonNullable.of(startYear);
    return this;
  }

  /**
   * Get startYear
   * @return startYear
   */
  
  @Schema(name = "start_year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_year")
  public JsonNullable<Integer> getStartYear() {
    return startYear;
  }

  public void setStartYear(JsonNullable<Integer> startYear) {
    this.startYear = startYear;
  }

  public LeadershipRecord endYear(Integer endYear) {
    this.endYear = JsonNullable.of(endYear);
    return this;
  }

  /**
   * Get endYear
   * @return endYear
   */
  
  @Schema(name = "end_year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_year")
  public JsonNullable<Integer> getEndYear() {
    return endYear;
  }

  public void setEndYear(JsonNullable<Integer> endYear) {
    this.endYear = endYear;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeadershipRecord leadershipRecord = (LeadershipRecord) o;
    return Objects.equals(this.name, leadershipRecord.name) &&
        Objects.equals(this.role, leadershipRecord.role) &&
        equalsNullable(this.startYear, leadershipRecord.startYear) &&
        equalsNullable(this.endYear, leadershipRecord.endYear);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, role, hashCodeNullable(startYear), hashCodeNullable(endYear));
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
    sb.append("class LeadershipRecord {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    startYear: ").append(toIndentedString(startYear)).append("\n");
    sb.append("    endYear: ").append(toIndentedString(endYear)).append("\n");
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

