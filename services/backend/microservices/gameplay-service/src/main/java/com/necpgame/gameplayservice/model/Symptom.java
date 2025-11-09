package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
 * Симптом киберпсихоза
 */

@Schema(name = "Symptom", description = "Симптом киберпсихоза")

public class Symptom {

  private UUID symptomId;

  private String name;

  private String description;

  /**
   * Серьезность симптома
   */
  public enum SeverityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
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

  @Valid
  private Map<String, Object> effects = new HashMap<>();

  private JsonNullable<@DecimalMin(value = "0") Float> duration = JsonNullable.<Float>undefined();

  public Symptom() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Symptom(UUID symptomId, String name, String description, SeverityEnum severity, Map<String, Object> effects) {
    this.symptomId = symptomId;
    this.name = name;
    this.description = description;
    this.severity = severity;
    this.effects = effects;
  }

  public Symptom symptomId(UUID symptomId) {
    this.symptomId = symptomId;
    return this;
  }

  /**
   * Идентификатор симптома
   * @return symptomId
   */
  @NotNull @Valid 
  @Schema(name = "symptom_id", description = "Идентификатор симптома", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("symptom_id")
  public UUID getSymptomId() {
    return symptomId;
  }

  public void setSymptomId(UUID symptomId) {
    this.symptomId = symptomId;
  }

  public Symptom name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название симптома
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Название симптома", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Symptom description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание симптома
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "Описание симптома", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Symptom severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Серьезность симптома
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", description = "Серьезность симптома", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public Symptom effects(Map<String, Object> effects) {
    this.effects = effects;
    return this;
  }

  public Symptom putEffectsItem(String key, Object effectsItem) {
    if (this.effects == null) {
      this.effects = new HashMap<>();
    }
    this.effects.put(key, effectsItem);
    return this;
  }

  /**
   * Эффекты симптома
   * @return effects
   */
  @NotNull 
  @Schema(name = "effects", description = "Эффекты симптома", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects")
  public Map<String, Object> getEffects() {
    return effects;
  }

  public void setEffects(Map<String, Object> effects) {
    this.effects = effects;
  }

  public Symptom duration(Float duration) {
    this.duration = JsonNullable.of(duration);
    return this;
  }

  /**
   * Длительность симптома в секундах
   * minimum: 0
   * @return duration
   */
  @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Длительность симптома в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public JsonNullable<@DecimalMin(value = "0") Float> getDuration() {
    return duration;
  }

  public void setDuration(JsonNullable<Float> duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Symptom symptom = (Symptom) o;
    return Objects.equals(this.symptomId, symptom.symptomId) &&
        Objects.equals(this.name, symptom.name) &&
        Objects.equals(this.description, symptom.description) &&
        Objects.equals(this.severity, symptom.severity) &&
        Objects.equals(this.effects, symptom.effects) &&
        equalsNullable(this.duration, symptom.duration);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(symptomId, name, description, severity, effects, hashCodeNullable(duration));
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
    sb.append("class Symptom {\n");
    sb.append("    symptomId: ").append(toIndentedString(symptomId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

