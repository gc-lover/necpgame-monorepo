package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestConnection
 */


public class QuestConnection {

  private String fromQuestId;

  private String toQuestId;

  /**
   * Gets or Sets connectionType
   */
  public enum ConnectionTypeEnum {
    PREREQUISITE("prerequisite"),
    
    UNLOCKS("unlocks"),
    
    BRANCHES_TO("branches_to"),
    
    ALTERNATIVE_TO("alternative_to"),
    
    PARALLEL_WITH("parallel_with");

    private final String value;

    ConnectionTypeEnum(String value) {
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
    public static ConnectionTypeEnum fromValue(String value) {
      for (ConnectionTypeEnum b : ConnectionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ConnectionTypeEnum connectionType;

  private @Nullable String condition;

  /**
   * Gets or Sets conditionType
   */
  public enum ConditionTypeEnum {
    QUEST_COMPLETED("quest_completed"),
    
    CHOICE_MADE("choice_made"),
    
    REPUTATION_THRESHOLD("reputation_threshold"),
    
    LEVEL_REQUIREMENT("level_requirement");

    private final String value;

    ConditionTypeEnum(String value) {
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
    public static ConditionTypeEnum fromValue(String value) {
      for (ConditionTypeEnum b : ConditionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConditionTypeEnum conditionType;

  public QuestConnection() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestConnection(String fromQuestId, String toQuestId, ConnectionTypeEnum connectionType) {
    this.fromQuestId = fromQuestId;
    this.toQuestId = toQuestId;
    this.connectionType = connectionType;
  }

  public QuestConnection fromQuestId(String fromQuestId) {
    this.fromQuestId = fromQuestId;
    return this;
  }

  /**
   * Get fromQuestId
   * @return fromQuestId
   */
  @NotNull 
  @Schema(name = "from_quest_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from_quest_id")
  public String getFromQuestId() {
    return fromQuestId;
  }

  public void setFromQuestId(String fromQuestId) {
    this.fromQuestId = fromQuestId;
  }

  public QuestConnection toQuestId(String toQuestId) {
    this.toQuestId = toQuestId;
    return this;
  }

  /**
   * Get toQuestId
   * @return toQuestId
   */
  @NotNull 
  @Schema(name = "to_quest_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to_quest_id")
  public String getToQuestId() {
    return toQuestId;
  }

  public void setToQuestId(String toQuestId) {
    this.toQuestId = toQuestId;
  }

  public QuestConnection connectionType(ConnectionTypeEnum connectionType) {
    this.connectionType = connectionType;
    return this;
  }

  /**
   * Get connectionType
   * @return connectionType
   */
  @NotNull 
  @Schema(name = "connection_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("connection_type")
  public ConnectionTypeEnum getConnectionType() {
    return connectionType;
  }

  public void setConnectionType(ConnectionTypeEnum connectionType) {
    this.connectionType = connectionType;
  }

  public QuestConnection condition(@Nullable String condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Условие для активации связи
   * @return condition
   */
  
  @Schema(name = "condition", description = "Условие для активации связи", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition")
  public @Nullable String getCondition() {
    return condition;
  }

  public void setCondition(@Nullable String condition) {
    this.condition = condition;
  }

  public QuestConnection conditionType(@Nullable ConditionTypeEnum conditionType) {
    this.conditionType = conditionType;
    return this;
  }

  /**
   * Get conditionType
   * @return conditionType
   */
  
  @Schema(name = "condition_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition_type")
  public @Nullable ConditionTypeEnum getConditionType() {
    return conditionType;
  }

  public void setConditionType(@Nullable ConditionTypeEnum conditionType) {
    this.conditionType = conditionType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestConnection questConnection = (QuestConnection) o;
    return Objects.equals(this.fromQuestId, questConnection.fromQuestId) &&
        Objects.equals(this.toQuestId, questConnection.toQuestId) &&
        Objects.equals(this.connectionType, questConnection.connectionType) &&
        Objects.equals(this.condition, questConnection.condition) &&
        Objects.equals(this.conditionType, questConnection.conditionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromQuestId, toQuestId, connectionType, condition, conditionType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestConnection {\n");
    sb.append("    fromQuestId: ").append(toIndentedString(fromQuestId)).append("\n");
    sb.append("    toQuestId: ").append(toIndentedString(toQuestId)).append("\n");
    sb.append("    connectionType: ").append(toIndentedString(connectionType)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    conditionType: ").append(toIndentedString(conditionType)).append("\n");
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

