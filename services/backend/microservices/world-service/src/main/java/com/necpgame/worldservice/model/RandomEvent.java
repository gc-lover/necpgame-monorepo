package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.EventOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RandomEvent
 */


public class RandomEvent {

  private String id;

  private String name;

  private String description;

  @Valid
  private List<@Valid EventOption> options = new ArrayList<>();

  private JsonNullable<Integer> timeLimit = JsonNullable.<Integer>undefined();

  /**
   * Gets or Sets dangerLevel
   */
  public enum DangerLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high");

    private final String value;

    DangerLevelEnum(String value) {
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
    public static DangerLevelEnum fromValue(String value) {
      for (DangerLevelEnum b : DangerLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DangerLevelEnum dangerLevel;

  public RandomEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RandomEvent(String id, String name, String description, List<@Valid EventOption> options) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.options = options;
  }

  public RandomEvent id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RandomEvent name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RandomEvent description(String description) {
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

  public RandomEvent options(List<@Valid EventOption> options) {
    this.options = options;
    return this;
  }

  public RandomEvent addOptionsItem(EventOption optionsItem) {
    if (this.options == null) {
      this.options = new ArrayList<>();
    }
    this.options.add(optionsItem);
    return this;
  }

  /**
   * Get options
   * @return options
   */
  @NotNull @Valid 
  @Schema(name = "options", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("options")
  public List<@Valid EventOption> getOptions() {
    return options;
  }

  public void setOptions(List<@Valid EventOption> options) {
    this.options = options;
  }

  public RandomEvent timeLimit(Integer timeLimit) {
    this.timeLimit = JsonNullable.of(timeLimit);
    return this;
  }

  /**
   * Get timeLimit
   * @return timeLimit
   */
  
  @Schema(name = "timeLimit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeLimit")
  public JsonNullable<Integer> getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(JsonNullable<Integer> timeLimit) {
    this.timeLimit = timeLimit;
  }

  public RandomEvent dangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Get dangerLevel
   * @return dangerLevel
   */
  
  @Schema(name = "dangerLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dangerLevel")
  public @Nullable DangerLevelEnum getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RandomEvent randomEvent = (RandomEvent) o;
    return Objects.equals(this.id, randomEvent.id) &&
        Objects.equals(this.name, randomEvent.name) &&
        Objects.equals(this.description, randomEvent.description) &&
        Objects.equals(this.options, randomEvent.options) &&
        equalsNullable(this.timeLimit, randomEvent.timeLimit) &&
        Objects.equals(this.dangerLevel, randomEvent.dangerLevel);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, options, hashCodeNullable(timeLimit), dangerLevel);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RandomEvent {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    timeLimit: ").append(toIndentedString(timeLimit)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
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

