package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CharacterStatsRecalculateRequest
 */


public class CharacterStatsRecalculateRequest {

  /**
   * Gets or Sets triggers
   */
  public enum TriggersEnum {
    EQUIPMENT_CHANGE("equipmentChange"),
    
    SKILL_CHANGE("skillChange"),
    
    EXTERNAL_MODIFIER("externalModifier"),
    
    MANUAL("manual");

    private final String value;

    TriggersEnum(String value) {
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
    public static TriggersEnum fromValue(String value) {
      for (TriggersEnum b : TriggersEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<TriggersEnum> triggers = new ArrayList<>();

  private Boolean includeInventory = true;

  private Boolean forceSnapshot = false;

  public CharacterStatsRecalculateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterStatsRecalculateRequest(List<TriggersEnum> triggers) {
    this.triggers = triggers;
  }

  public CharacterStatsRecalculateRequest triggers(List<TriggersEnum> triggers) {
    this.triggers = triggers;
    return this;
  }

  public CharacterStatsRecalculateRequest addTriggersItem(TriggersEnum triggersItem) {
    if (this.triggers == null) {
      this.triggers = new ArrayList<>();
    }
    this.triggers.add(triggersItem);
    return this;
  }

  /**
   * Get triggers
   * @return triggers
   */
  @NotNull 
  @Schema(name = "triggers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("triggers")
  public List<TriggersEnum> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<TriggersEnum> triggers) {
    this.triggers = triggers;
  }

  public CharacterStatsRecalculateRequest includeInventory(Boolean includeInventory) {
    this.includeInventory = includeInventory;
    return this;
  }

  /**
   * Get includeInventory
   * @return includeInventory
   */
  
  @Schema(name = "includeInventory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeInventory")
  public Boolean getIncludeInventory() {
    return includeInventory;
  }

  public void setIncludeInventory(Boolean includeInventory) {
    this.includeInventory = includeInventory;
  }

  public CharacterStatsRecalculateRequest forceSnapshot(Boolean forceSnapshot) {
    this.forceSnapshot = forceSnapshot;
    return this;
  }

  /**
   * Get forceSnapshot
   * @return forceSnapshot
   */
  
  @Schema(name = "forceSnapshot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forceSnapshot")
  public Boolean getForceSnapshot() {
    return forceSnapshot;
  }

  public void setForceSnapshot(Boolean forceSnapshot) {
    this.forceSnapshot = forceSnapshot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterStatsRecalculateRequest characterStatsRecalculateRequest = (CharacterStatsRecalculateRequest) o;
    return Objects.equals(this.triggers, characterStatsRecalculateRequest.triggers) &&
        Objects.equals(this.includeInventory, characterStatsRecalculateRequest.includeInventory) &&
        Objects.equals(this.forceSnapshot, characterStatsRecalculateRequest.forceSnapshot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(triggers, includeInventory, forceSnapshot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterStatsRecalculateRequest {\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    includeInventory: ").append(toIndentedString(includeInventory)).append("\n");
    sb.append("    forceSnapshot: ").append(toIndentedString(forceSnapshot)).append("\n");
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

