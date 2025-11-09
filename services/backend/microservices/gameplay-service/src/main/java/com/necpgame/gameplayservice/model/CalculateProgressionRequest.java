package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CalculateProgressionRequestRecentEventsInner;
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
 * CalculateProgressionRequest
 */


public class CalculateProgressionRequest {

  private Float humanityCurrent;

  /**
   * Текущая стадия киберпсихоза
   */
  public enum StageEnum {
    NONE("none"),
    
    EARLY("early"),
    
    MODERATE("moderate"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StageEnum stage;

  /**
   * Gets or Sets activeTriggers
   */
  public enum ActiveTriggersEnum {
    SUSTAINED_COMBAT("sustained_combat"),
    
    TRAUMA_EVENT("trauma_event"),
    
    IMPLANT_DAMAGE("implant_damage"),
    
    NETRUNNING_OVERLOAD("netrunning_overload"),
    
    BLACKWALL_CONTACT("blackwall_contact"),
    
    CYBERDRUG_ADDICTION("cyberdrug_addiction");

    private final String value;

    ActiveTriggersEnum(String value) {
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
    public static ActiveTriggersEnum fromValue(String value) {
      for (ActiveTriggersEnum b : ActiveTriggersEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ActiveTriggersEnum> activeTriggers = new ArrayList<>();

  @Valid
  private List<@Valid CalculateProgressionRequestRecentEventsInner> recentEvents = new ArrayList<>();

  public CalculateProgressionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateProgressionRequest(Float humanityCurrent, StageEnum stage, List<ActiveTriggersEnum> activeTriggers) {
    this.humanityCurrent = humanityCurrent;
    this.stage = stage;
    this.activeTriggers = activeTriggers;
  }

  public CalculateProgressionRequest humanityCurrent(Float humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
    return this;
  }

  /**
   * Текущая человечность игрока
   * minimum: 0
   * maximum: 100
   * @return humanityCurrent
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "humanity_current", example = "58.2", description = "Текущая человечность игрока", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity_current")
  public Float getHumanityCurrent() {
    return humanityCurrent;
  }

  public void setHumanityCurrent(Float humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
  }

  public CalculateProgressionRequest stage(StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Текущая стадия киберпсихоза
   * @return stage
   */
  @NotNull 
  @Schema(name = "stage", example = "moderate", description = "Текущая стадия киберпсихоза", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage")
  public StageEnum getStage() {
    return stage;
  }

  public void setStage(StageEnum stage) {
    this.stage = stage;
  }

  public CalculateProgressionRequest activeTriggers(List<ActiveTriggersEnum> activeTriggers) {
    this.activeTriggers = activeTriggers;
    return this;
  }

  public CalculateProgressionRequest addActiveTriggersItem(ActiveTriggersEnum activeTriggersItem) {
    if (this.activeTriggers == null) {
      this.activeTriggers = new ArrayList<>();
    }
    this.activeTriggers.add(activeTriggersItem);
    return this;
  }

  /**
   * Активные триггеры, ускоряющие прогрессию
   * @return activeTriggers
   */
  @NotNull @Size(min = 0) 
  @Schema(name = "active_triggers", example = "[\"sustained_combat\",\"implant_damage\"]", description = "Активные триггеры, ускоряющие прогрессию", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("active_triggers")
  public List<ActiveTriggersEnum> getActiveTriggers() {
    return activeTriggers;
  }

  public void setActiveTriggers(List<ActiveTriggersEnum> activeTriggers) {
    this.activeTriggers = activeTriggers;
  }

  public CalculateProgressionRequest recentEvents(List<@Valid CalculateProgressionRequestRecentEventsInner> recentEvents) {
    this.recentEvents = recentEvents;
    return this;
  }

  public CalculateProgressionRequest addRecentEventsItem(CalculateProgressionRequestRecentEventsInner recentEventsItem) {
    if (this.recentEvents == null) {
      this.recentEvents = new ArrayList<>();
    }
    this.recentEvents.add(recentEventsItem);
    return this;
  }

  /**
   * Последние ключевые события с влиянием на прогрессию
   * @return recentEvents
   */
  @Valid 
  @Schema(name = "recent_events", description = "Последние ключевые события с влиянием на прогрессию", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recent_events")
  public List<@Valid CalculateProgressionRequestRecentEventsInner> getRecentEvents() {
    return recentEvents;
  }

  public void setRecentEvents(List<@Valid CalculateProgressionRequestRecentEventsInner> recentEvents) {
    this.recentEvents = recentEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateProgressionRequest calculateProgressionRequest = (CalculateProgressionRequest) o;
    return Objects.equals(this.humanityCurrent, calculateProgressionRequest.humanityCurrent) &&
        Objects.equals(this.stage, calculateProgressionRequest.stage) &&
        Objects.equals(this.activeTriggers, calculateProgressionRequest.activeTriggers) &&
        Objects.equals(this.recentEvents, calculateProgressionRequest.recentEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(humanityCurrent, stage, activeTriggers, recentEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateProgressionRequest {\n");
    sb.append("    humanityCurrent: ").append(toIndentedString(humanityCurrent)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    activeTriggers: ").append(toIndentedString(activeTriggers)).append("\n");
    sb.append("    recentEvents: ").append(toIndentedString(recentEvents)).append("\n");
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

