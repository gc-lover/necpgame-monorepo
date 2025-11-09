package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LevelSkipResponse
 */


public class LevelSkipResponse {

  private @Nullable Integer newLevel;

  private @Nullable Integer totalLevelsSkipped;

  private @Nullable Integer remainingCurrency;

  public LevelSkipResponse newLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * Get newLevel
   * @return newLevel
   */
  
  @Schema(name = "newLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newLevel")
  public @Nullable Integer getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(@Nullable Integer newLevel) {
    this.newLevel = newLevel;
  }

  public LevelSkipResponse totalLevelsSkipped(@Nullable Integer totalLevelsSkipped) {
    this.totalLevelsSkipped = totalLevelsSkipped;
    return this;
  }

  /**
   * Get totalLevelsSkipped
   * @return totalLevelsSkipped
   */
  
  @Schema(name = "totalLevelsSkipped", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalLevelsSkipped")
  public @Nullable Integer getTotalLevelsSkipped() {
    return totalLevelsSkipped;
  }

  public void setTotalLevelsSkipped(@Nullable Integer totalLevelsSkipped) {
    this.totalLevelsSkipped = totalLevelsSkipped;
  }

  public LevelSkipResponse remainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
    return this;
  }

  /**
   * Get remainingCurrency
   * @return remainingCurrency
   */
  
  @Schema(name = "remainingCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingCurrency")
  public @Nullable Integer getRemainingCurrency() {
    return remainingCurrency;
  }

  public void setRemainingCurrency(@Nullable Integer remainingCurrency) {
    this.remainingCurrency = remainingCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelSkipResponse levelSkipResponse = (LevelSkipResponse) o;
    return Objects.equals(this.newLevel, levelSkipResponse.newLevel) &&
        Objects.equals(this.totalLevelsSkipped, levelSkipResponse.totalLevelsSkipped) &&
        Objects.equals(this.remainingCurrency, levelSkipResponse.remainingCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newLevel, totalLevelsSkipped, remainingCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelSkipResponse {\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    totalLevelsSkipped: ").append(toIndentedString(totalLevelsSkipped)).append("\n");
    sb.append("    remainingCurrency: ").append(toIndentedString(remainingCurrency)).append("\n");
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

