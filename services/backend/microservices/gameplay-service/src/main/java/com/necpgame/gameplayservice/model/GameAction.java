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
 * GameAction
 */


public class GameAction {

  private String id;

  private String label;

  private @Nullable String description;

  private Boolean enabled = true;

  public GameAction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameAction(String id, String label) {
    this.id = id;
    this.label = label;
  }

  public GameAction id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор действия
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "look-around", description = "Уникальный идентификатор действия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameAction label(String label) {
    this.label = label;
    return this;
  }

  /**
   * Название действия для отображения
   * @return label
   */
  @NotNull @Size(min = 1, max = 100) 
  @Schema(name = "label", example = "Осмотреть окрестности", description = "Название действия для отображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public GameAction description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание действия
   * @return description
   */
  @Size(max = 500) 
  @Schema(name = "description", example = "Осмотрите окрестности, чтобы найти точки интереса", description = "Описание действия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GameAction enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Доступно ли действие в данный момент
   * @return enabled
   */
  
  @Schema(name = "enabled", example = "true", description = "Доступно ли действие в данный момент", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameAction gameAction = (GameAction) o;
    return Objects.equals(this.id, gameAction.id) &&
        Objects.equals(this.label, gameAction.label) &&
        Objects.equals(this.description, gameAction.description) &&
        Objects.equals(this.enabled, gameAction.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label, description, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameAction {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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

