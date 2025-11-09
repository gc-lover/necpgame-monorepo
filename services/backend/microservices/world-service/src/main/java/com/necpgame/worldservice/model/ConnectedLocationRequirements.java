package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Требования для доступа (если accessible&#x3D;false)
 */

@Schema(name = "ConnectedLocation_requirements", description = "Требования для доступа (если accessible=false)")
@JsonTypeName("ConnectedLocation_requirements")

public class ConnectedLocationRequirements {

  private @Nullable Integer minLevel;

  @Valid
  private List<String> requiredQuests = new ArrayList<>();

  public ConnectedLocationRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Минимальный уровень
   * @return minLevel
   */
  
  @Schema(name = "minLevel", description = "Минимальный уровень", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public ConnectedLocationRequirements requiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
    return this;
  }

  public ConnectedLocationRequirements addRequiredQuestsItem(String requiredQuestsItem) {
    if (this.requiredQuests == null) {
      this.requiredQuests = new ArrayList<>();
    }
    this.requiredQuests.add(requiredQuestsItem);
    return this;
  }

  /**
   * Требуемые завершенные квесты
   * @return requiredQuests
   */
  
  @Schema(name = "requiredQuests", description = "Требуемые завершенные квесты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredQuests")
  public List<String> getRequiredQuests() {
    return requiredQuests;
  }

  public void setRequiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectedLocationRequirements connectedLocationRequirements = (ConnectedLocationRequirements) o;
    return Objects.equals(this.minLevel, connectedLocationRequirements.minLevel) &&
        Objects.equals(this.requiredQuests, connectedLocationRequirements.requiredQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, requiredQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectedLocationRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    requiredQuests: ").append(toIndentedString(requiredQuests)).append("\n");
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

