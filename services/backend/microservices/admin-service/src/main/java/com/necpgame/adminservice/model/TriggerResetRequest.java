package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerResetRequest
 */


public class TriggerResetRequest {

  /**
   * Gets or Sets resetType
   */
  public enum ResetTypeEnum {
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    MONTHLY("MONTHLY");

    private final String value;

    ResetTypeEnum(String value) {
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
    public static ResetTypeEnum fromValue(String value) {
      for (ResetTypeEnum b : ResetTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResetTypeEnum resetType;

  /**
   * Gets or Sets target
   */
  public enum TargetEnum {
    ALL_PLAYERS("ALL_PLAYERS"),
    
    SINGLE_PLAYER("SINGLE_PLAYER");

    private final String value;

    TargetEnum(String value) {
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
    public static TargetEnum fromValue(String value) {
      for (TargetEnum b : TargetEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TargetEnum target = TargetEnum.ALL_PLAYERS;

  private @Nullable UUID playerId;

  @Valid
  private List<String> items = new ArrayList<>();

  private @Nullable String reason;

  public TriggerResetRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerResetRequest(ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public TriggerResetRequest resetType(ResetTypeEnum resetType) {
    this.resetType = resetType;
    return this;
  }

  /**
   * Get resetType
   * @return resetType
   */
  @NotNull 
  @Schema(name = "reset_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reset_type")
  public ResetTypeEnum getResetType() {
    return resetType;
  }

  public void setResetType(ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public TriggerResetRequest target(TargetEnum target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public TargetEnum getTarget() {
    return target;
  }

  public void setTarget(TargetEnum target) {
    this.target = target;
  }

  public TriggerResetRequest playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Обязательно если target=SINGLE_PLAYER
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", description = "Обязательно если target=SINGLE_PLAYER", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public TriggerResetRequest items(List<String> items) {
    this.items = items;
    return this;
  }

  public TriggerResetRequest addItemsItem(String itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Что именно сбросить (если не указано - всё)
   * @return items
   */
  
  @Schema(name = "items", example = "[\"quests\",\"limits\"]", description = "Что именно сбросить (если не указано - всё)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  public TriggerResetRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина ручного сброса
   * @return reason
   */
  
  @Schema(name = "reason", example = "Bug fix compensation", description = "Причина ручного сброса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerResetRequest triggerResetRequest = (TriggerResetRequest) o;
    return Objects.equals(this.resetType, triggerResetRequest.resetType) &&
        Objects.equals(this.target, triggerResetRequest.target) &&
        Objects.equals(this.playerId, triggerResetRequest.playerId) &&
        Objects.equals(this.items, triggerResetRequest.items) &&
        Objects.equals(this.reason, triggerResetRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetType, target, playerId, items, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerResetRequest {\n");
    sb.append("    resetType: ").append(toIndentedString(resetType)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

