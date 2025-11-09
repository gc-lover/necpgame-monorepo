package com.necpgame.gameplayservice.model;

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
 * StealthStatusEnemiesAware
 */

@JsonTypeName("StealthStatus_enemies_aware")

public class StealthStatusEnemiesAware {

  private @Nullable Integer total;

  private @Nullable Integer searching;

  public StealthStatusEnemiesAware total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Всего врагов, знающих о персонаже
   * @return total
   */
  
  @Schema(name = "total", description = "Всего врагов, знающих о персонаже", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public StealthStatusEnemiesAware searching(@Nullable Integer searching) {
    this.searching = searching;
    return this;
  }

  /**
   * Врагов, активно ищущих
   * @return searching
   */
  
  @Schema(name = "searching", description = "Врагов, активно ищущих", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("searching")
  public @Nullable Integer getSearching() {
    return searching;
  }

  public void setSearching(@Nullable Integer searching) {
    this.searching = searching;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StealthStatusEnemiesAware stealthStatusEnemiesAware = (StealthStatusEnemiesAware) o;
    return Objects.equals(this.total, stealthStatusEnemiesAware.total) &&
        Objects.equals(this.searching, stealthStatusEnemiesAware.searching);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, searching);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StealthStatusEnemiesAware {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    searching: ").append(toIndentedString(searching)).append("\n");
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

