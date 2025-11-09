package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StateUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StateUpdateRequest {

  private Integer version;

  @Valid
  private Map<String, Object> updates = new HashMap<>();

  /**
   * Gets or Sets mergeStrategy
   */
  public enum MergeStrategyEnum {
    OVERWRITE("OVERWRITE"),
    
    MERGE("MERGE"),
    
    APPEND("APPEND");

    private final String value;

    MergeStrategyEnum(String value) {
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
    public static MergeStrategyEnum fromValue(String value) {
      for (MergeStrategyEnum b : MergeStrategyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MergeStrategyEnum mergeStrategy = MergeStrategyEnum.MERGE;

  public StateUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StateUpdateRequest(Integer version, Map<String, Object> updates) {
    this.version = version;
    this.updates = updates;
  }

  public StateUpdateRequest version(Integer version) {
    this.version = version;
    return this;
  }

  /**
   * Текущая версия (для optimistic locking)
   * @return version
   */
  @NotNull 
  @Schema(name = "version", description = "Текущая версия (для optimistic locking)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public StateUpdateRequest updates(Map<String, Object> updates) {
    this.updates = updates;
    return this;
  }

  public StateUpdateRequest putUpdatesItem(String key, Object updatesItem) {
    if (this.updates == null) {
      this.updates = new HashMap<>();
    }
    this.updates.put(key, updatesItem);
    return this;
  }

  /**
   * Get updates
   * @return updates
   */
  @NotNull 
  @Schema(name = "updates", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updates")
  public Map<String, Object> getUpdates() {
    return updates;
  }

  public void setUpdates(Map<String, Object> updates) {
    this.updates = updates;
  }

  public StateUpdateRequest mergeStrategy(MergeStrategyEnum mergeStrategy) {
    this.mergeStrategy = mergeStrategy;
    return this;
  }

  /**
   * Get mergeStrategy
   * @return mergeStrategy
   */
  
  @Schema(name = "merge_strategy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("merge_strategy")
  public MergeStrategyEnum getMergeStrategy() {
    return mergeStrategy;
  }

  public void setMergeStrategy(MergeStrategyEnum mergeStrategy) {
    this.mergeStrategy = mergeStrategy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateUpdateRequest stateUpdateRequest = (StateUpdateRequest) o;
    return Objects.equals(this.version, stateUpdateRequest.version) &&
        Objects.equals(this.updates, stateUpdateRequest.updates) &&
        Objects.equals(this.mergeStrategy, stateUpdateRequest.mergeStrategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, updates, mergeStrategy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateUpdateRequest {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updates: ").append(toIndentedString(updates)).append("\n");
    sb.append("    mergeStrategy: ").append(toIndentedString(mergeStrategy)).append("\n");
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

