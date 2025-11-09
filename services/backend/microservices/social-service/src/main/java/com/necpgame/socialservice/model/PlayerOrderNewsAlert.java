package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * PlayerOrderNewsAlert
 */


public class PlayerOrderNewsAlert {

  private String alertId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CRISIS("crisis"),
    
    SURGE("surge"),
    
    MILESTONE("milestone"),
    
    ECONOMY("economy"),
    
    REPUTATION("reputation");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    CAUTION("caution"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SeverityEnum severity;

  private String description;

  @Valid
  private List<String> actions = new ArrayList<>();

  @Valid
  private List<UUID> cityIds = new ArrayList<>();

  @Valid
  private List<UUID> orderIds = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public PlayerOrderNewsAlert() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsAlert(String alertId, TypeEnum type, SeverityEnum severity, String description, OffsetDateTime createdAt) {
    this.alertId = alertId;
    this.type = type;
    this.severity = severity;
    this.description = description;
    this.createdAt = createdAt;
  }

  public PlayerOrderNewsAlert alertId(String alertId) {
    this.alertId = alertId;
    return this;
  }

  /**
   * Get alertId
   * @return alertId
   */
  @NotNull 
  @Schema(name = "alertId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alertId")
  public String getAlertId() {
    return alertId;
  }

  public void setAlertId(String alertId) {
    this.alertId = alertId;
  }

  public PlayerOrderNewsAlert type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public PlayerOrderNewsAlert severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public PlayerOrderNewsAlert description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PlayerOrderNewsAlert actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public PlayerOrderNewsAlert addActionsItem(String actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * Get actions
   * @return actions
   */
  
  @Schema(name = "actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actions")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }

  public PlayerOrderNewsAlert cityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public PlayerOrderNewsAlert addCityIdsItem(UUID cityIdsItem) {
    if (this.cityIds == null) {
      this.cityIds = new ArrayList<>();
    }
    this.cityIds.add(cityIdsItem);
    return this;
  }

  /**
   * Get cityIds
   * @return cityIds
   */
  @Valid 
  @Schema(name = "cityIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityIds")
  public List<UUID> getCityIds() {
    return cityIds;
  }

  public void setCityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
  }

  public PlayerOrderNewsAlert orderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
    return this;
  }

  public PlayerOrderNewsAlert addOrderIdsItem(UUID orderIdsItem) {
    if (this.orderIds == null) {
      this.orderIds = new ArrayList<>();
    }
    this.orderIds.add(orderIdsItem);
    return this;
  }

  /**
   * Get orderIds
   * @return orderIds
   */
  @Valid 
  @Schema(name = "orderIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderIds")
  public List<UUID> getOrderIds() {
    return orderIds;
  }

  public void setOrderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
  }

  public PlayerOrderNewsAlert createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PlayerOrderNewsAlert expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsAlert playerOrderNewsAlert = (PlayerOrderNewsAlert) o;
    return Objects.equals(this.alertId, playerOrderNewsAlert.alertId) &&
        Objects.equals(this.type, playerOrderNewsAlert.type) &&
        Objects.equals(this.severity, playerOrderNewsAlert.severity) &&
        Objects.equals(this.description, playerOrderNewsAlert.description) &&
        Objects.equals(this.actions, playerOrderNewsAlert.actions) &&
        Objects.equals(this.cityIds, playerOrderNewsAlert.cityIds) &&
        Objects.equals(this.orderIds, playerOrderNewsAlert.orderIds) &&
        Objects.equals(this.createdAt, playerOrderNewsAlert.createdAt) &&
        Objects.equals(this.expiresAt, playerOrderNewsAlert.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, type, severity, description, actions, cityIds, orderIds, createdAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsAlert {\n");
    sb.append("    alertId: ").append(toIndentedString(alertId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    orderIds: ").append(toIndentedString(orderIds)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

