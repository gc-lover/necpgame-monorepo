package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.LocationActionRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LocationAction
 */


public class LocationAction {

  private String id;

  private String label;

  private String description;

  private Boolean enabled;

  /**
   * Тип действия
   */
  public enum ActionTypeEnum {
    EXPLORATION("exploration"),
    
    INTERACTION("interaction"),
    
    COMBAT("combat"),
    
    TRADE("trade"),
    
    QUEST("quest"),
    
    TRAVEL("travel");

    private final String value;

    ActionTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionTypeEnum fromValue(String value) {
      for (ActionTypeEnum b : ActionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionTypeEnum actionType;

  private @Nullable LocationActionRequirements requirements;

  private @Nullable String disabledReason;

  public LocationAction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LocationAction(String id, String label, String description, Boolean enabled, ActionTypeEnum actionType) {
    this.id = id;
    this.label = label;
    this.description = description;
    this.enabled = enabled;
    this.actionType = actionType;
  }

  public LocationAction id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID действия
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "explore_market", description = "ID действия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocationAction label(String label) {
    this.label = label;
    return this;
  }

  /**
   * Название действия для отображения
   * @return label
   */
  @NotNull 
  @Schema(name = "label", example = "Исследовать рынок", description = "Название действия для отображения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public LocationAction description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание действия
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Посетить местный рынок и посмотреть товары", description = "Описание действия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocationAction enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Доступно ли действие для персонажа
   * @return enabled
   */
  @NotNull 
  @Schema(name = "enabled", example = "true", description = "Доступно ли действие для персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public LocationAction actionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
    return this;
  }

  /**
   * Тип действия
   * @return actionType
   */
  @NotNull 
  @Schema(name = "actionType", example = "exploration", description = "Тип действия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actionType")
  public ActionTypeEnum getActionType() {
    return actionType;
  }

  public void setActionType(ActionTypeEnum actionType) {
    this.actionType = actionType;
  }

  public LocationAction requirements(@Nullable LocationActionRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable LocationActionRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable LocationActionRequirements requirements) {
    this.requirements = requirements;
  }

  public LocationAction disabledReason(@Nullable String disabledReason) {
    this.disabledReason = disabledReason;
    return this;
  }

  /**
   * Причина недоступности (если enabled=false)
   * @return disabledReason
   */
  
  @Schema(name = "disabledReason", example = "Требуется уровень 5", description = "Причина недоступности (если enabled=false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disabledReason")
  public @Nullable String getDisabledReason() {
    return disabledReason;
  }

  public void setDisabledReason(@Nullable String disabledReason) {
    this.disabledReason = disabledReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationAction locationAction = (LocationAction) o;
    return Objects.equals(this.id, locationAction.id) &&
        Objects.equals(this.label, locationAction.label) &&
        Objects.equals(this.description, locationAction.description) &&
        Objects.equals(this.enabled, locationAction.enabled) &&
        Objects.equals(this.actionType, locationAction.actionType) &&
        Objects.equals(this.requirements, locationAction.requirements) &&
        Objects.equals(this.disabledReason, locationAction.disabledReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label, description, enabled, actionType, requirements, disabledReason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationAction {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    disabledReason: ").append(toIndentedString(disabledReason)).append("\n");
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

