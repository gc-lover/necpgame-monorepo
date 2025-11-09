package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderCheckpoint
 */


public class PlayerOrderCheckpoint {

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime dueAt;

  @Valid
  private List<String> deliverables = new ArrayList<>();

  public PlayerOrderCheckpoint() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderCheckpoint(String name, OffsetDateTime dueAt) {
    this.name = name;
    this.dueAt = dueAt;
  }

  public PlayerOrderCheckpoint name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название контрольной точки.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Название контрольной точки.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerOrderCheckpoint dueAt(OffsetDateTime dueAt) {
    this.dueAt = dueAt;
    return this;
  }

  /**
   * Дедлайн контрольной точки.
   * @return dueAt
   */
  @NotNull @Valid 
  @Schema(name = "dueAt", description = "Дедлайн контрольной точки.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dueAt")
  public OffsetDateTime getDueAt() {
    return dueAt;
  }

  public void setDueAt(OffsetDateTime dueAt) {
    this.dueAt = dueAt;
  }

  public PlayerOrderCheckpoint deliverables(List<String> deliverables) {
    this.deliverables = deliverables;
    return this;
  }

  public PlayerOrderCheckpoint addDeliverablesItem(String deliverablesItem) {
    if (this.deliverables == null) {
      this.deliverables = new ArrayList<>();
    }
    this.deliverables.add(deliverablesItem);
    return this;
  }

  /**
   * Ожидаемые артефакты или отчёты.
   * @return deliverables
   */
  
  @Schema(name = "deliverables", description = "Ожидаемые артефакты или отчёты.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliverables")
  public List<String> getDeliverables() {
    return deliverables;
  }

  public void setDeliverables(List<String> deliverables) {
    this.deliverables = deliverables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderCheckpoint playerOrderCheckpoint = (PlayerOrderCheckpoint) o;
    return Objects.equals(this.name, playerOrderCheckpoint.name) &&
        Objects.equals(this.dueAt, playerOrderCheckpoint.dueAt) &&
        Objects.equals(this.deliverables, playerOrderCheckpoint.deliverables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dueAt, deliverables);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderCheckpoint {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dueAt: ").append(toIndentedString(dueAt)).append("\n");
    sb.append("    deliverables: ").append(toIndentedString(deliverables)).append("\n");
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

