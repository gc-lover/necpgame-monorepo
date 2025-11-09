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
 * QuestNodeLocationCoordinates
 */

@JsonTypeName("QuestNode_location_coordinates")

public class QuestNodeLocationCoordinates {

  private @Nullable BigDecimal x;

  private @Nullable BigDecimal y;

  public QuestNodeLocationCoordinates x(@Nullable BigDecimal x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
   */
  @Valid 
  @Schema(name = "x", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("x")
  public @Nullable BigDecimal getX() {
    return x;
  }

  public void setX(@Nullable BigDecimal x) {
    this.x = x;
  }

  public QuestNodeLocationCoordinates y(@Nullable BigDecimal y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
   */
  @Valid 
  @Schema(name = "y", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("y")
  public @Nullable BigDecimal getY() {
    return y;
  }

  public void setY(@Nullable BigDecimal y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestNodeLocationCoordinates questNodeLocationCoordinates = (QuestNodeLocationCoordinates) o;
    return Objects.equals(this.x, questNodeLocationCoordinates.x) &&
        Objects.equals(this.y, questNodeLocationCoordinates.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestNodeLocationCoordinates {\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
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

