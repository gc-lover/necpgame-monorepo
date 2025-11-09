package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GuaranteedReward;
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
 * GuaranteedDistributionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuaranteedDistributionRequest {

  private UUID playerId;

  private GuaranteedReward reward;

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    PITY_TIMER("PITY_TIMER"),
    
    MILESTONE("MILESTONE"),
    
    COMPENSATION("COMPENSATION");

    private final String value;

    TriggerEnum(String value) {
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
    public static TriggerEnum fromValue(String value) {
      for (TriggerEnum b : TriggerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TriggerEnum trigger;

  public GuaranteedDistributionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuaranteedDistributionRequest(UUID playerId, GuaranteedReward reward) {
    this.playerId = playerId;
    this.reward = reward;
  }

  public GuaranteedDistributionRequest playerId(UUID playerId) {
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

  public GuaranteedDistributionRequest reward(GuaranteedReward reward) {
    this.reward = reward;
    return this;
  }

  /**
   * Get reward
   * @return reward
   */
  @NotNull @Valid 
  @Schema(name = "reward", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reward")
  public GuaranteedReward getReward() {
    return reward;
  }

  public void setReward(GuaranteedReward reward) {
    this.reward = reward;
  }

  public GuaranteedDistributionRequest trigger(@Nullable TriggerEnum trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger")
  public @Nullable TriggerEnum getTrigger() {
    return trigger;
  }

  public void setTrigger(@Nullable TriggerEnum trigger) {
    this.trigger = trigger;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuaranteedDistributionRequest guaranteedDistributionRequest = (GuaranteedDistributionRequest) o;
    return Objects.equals(this.playerId, guaranteedDistributionRequest.playerId) &&
        Objects.equals(this.reward, guaranteedDistributionRequest.reward) &&
        Objects.equals(this.trigger, guaranteedDistributionRequest.trigger);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, reward, trigger);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuaranteedDistributionRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
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

