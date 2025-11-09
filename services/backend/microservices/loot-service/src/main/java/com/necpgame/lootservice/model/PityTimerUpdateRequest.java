package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PityTimerUpdateRequest
 */


public class PityTimerUpdateRequest {

  private UUID playerId;

  private String tableId;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    INCREMENT("INCREMENT"),
    
    RESET("RESET"),
    
    FORCE_REWARD("FORCE_REWARD");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActionEnum action;

  private Integer amount = 1;

  private @Nullable String rewardTemplateId;

  public PityTimerUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PityTimerUpdateRequest(UUID playerId, String tableId) {
    this.playerId = playerId;
    this.tableId = tableId;
  }

  public PityTimerUpdateRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PityTimerUpdateRequest tableId(String tableId) {
    this.tableId = tableId;
    return this;
  }

  /**
   * Get tableId
   * @return tableId
   */
  @NotNull 
  @Schema(name = "tableId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tableId")
  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public PityTimerUpdateRequest action(@Nullable ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable ActionEnum getAction() {
    return action;
  }

  public void setAction(@Nullable ActionEnum action) {
    this.action = action;
  }

  public PityTimerUpdateRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public PityTimerUpdateRequest rewardTemplateId(@Nullable String rewardTemplateId) {
    this.rewardTemplateId = rewardTemplateId;
    return this;
  }

  /**
   * Get rewardTemplateId
   * @return rewardTemplateId
   */
  
  @Schema(name = "rewardTemplateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardTemplateId")
  public @Nullable String getRewardTemplateId() {
    return rewardTemplateId;
  }

  public void setRewardTemplateId(@Nullable String rewardTemplateId) {
    this.rewardTemplateId = rewardTemplateId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PityTimerUpdateRequest pityTimerUpdateRequest = (PityTimerUpdateRequest) o;
    return Objects.equals(this.playerId, pityTimerUpdateRequest.playerId) &&
        Objects.equals(this.tableId, pityTimerUpdateRequest.tableId) &&
        Objects.equals(this.action, pityTimerUpdateRequest.action) &&
        Objects.equals(this.amount, pityTimerUpdateRequest.amount) &&
        Objects.equals(this.rewardTemplateId, pityTimerUpdateRequest.rewardTemplateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, tableId, action, amount, rewardTemplateId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PityTimerUpdateRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    rewardTemplateId: ").append(toIndentedString(rewardTemplateId)).append("\n");
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

