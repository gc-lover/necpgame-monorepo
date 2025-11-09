package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * XpGrantResponseLevelUpsInner
 */

@JsonTypeName("XpGrantResponse_levelUps_inner")

public class XpGrantResponseLevelUpsInner {

  private @Nullable Integer level;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime achievedAt;

  public XpGrantResponseLevelUpsInner level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public XpGrantResponseLevelUpsInner achievedAt(@Nullable OffsetDateTime achievedAt) {
    this.achievedAt = achievedAt;
    return this;
  }

  /**
   * Get achievedAt
   * @return achievedAt
   */
  @Valid 
  @Schema(name = "achievedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievedAt")
  public @Nullable OffsetDateTime getAchievedAt() {
    return achievedAt;
  }

  public void setAchievedAt(@Nullable OffsetDateTime achievedAt) {
    this.achievedAt = achievedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpGrantResponseLevelUpsInner xpGrantResponseLevelUpsInner = (XpGrantResponseLevelUpsInner) o;
    return Objects.equals(this.level, xpGrantResponseLevelUpsInner.level) &&
        Objects.equals(this.achievedAt, xpGrantResponseLevelUpsInner.achievedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, achievedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpGrantResponseLevelUpsInner {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    achievedAt: ").append(toIndentedString(achievedAt)).append("\n");
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

