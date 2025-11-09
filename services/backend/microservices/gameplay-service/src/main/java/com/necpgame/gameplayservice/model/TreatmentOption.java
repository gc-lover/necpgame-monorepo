package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
 * Опция лечения
 */

@Schema(name = "TreatmentOption", description = "Опция лечения")

public class TreatmentOption {

  /**
   * Тип лечения
   */
  public enum TypeEnum {
    THERAPY("therapy"),
    
    MEDICATION("medication"),
    
    IMPLANT_REMOVAL("implant_removal"),
    
    DETOXIFICATION("detoxification");

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

  private String name;

  private String description;

  private Float cost;

  private Float effectiveness;

  private JsonNullable<@DecimalMin(value = "0") Float> cooldown = JsonNullable.<Float>undefined();

  @Valid
  private JsonNullable<Map<String, Object>> requirements = JsonNullable.<Map<String, Object>>undefined();

  public TreatmentOption() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TreatmentOption(TypeEnum type, String name, String description, Float cost, Float effectiveness) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.cost = cost;
    this.effectiveness = effectiveness;
  }

  public TreatmentOption type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип лечения
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "Тип лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public TreatmentOption name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название метода лечения
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Название метода лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TreatmentOption description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание метода лечения
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "Описание метода лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TreatmentOption cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Стоимость лечения
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "Стоимость лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  public TreatmentOption effectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
    return this;
  }

  /**
   * Эффективность лечения
   * minimum: 0
   * maximum: 100
   * @return effectiveness
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "effectiveness", description = "Эффективность лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectiveness")
  public Float getEffectiveness() {
    return effectiveness;
  }

  public void setEffectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
  }

  public TreatmentOption cooldown(Float cooldown) {
    this.cooldown = JsonNullable.of(cooldown);
    return this;
  }

  /**
   * Кулдаун до следующего использования в секундах
   * minimum: 0
   * @return cooldown
   */
  @DecimalMin(value = "0") 
  @Schema(name = "cooldown", description = "Кулдаун до следующего использования в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public JsonNullable<@DecimalMin(value = "0") Float> getCooldown() {
    return cooldown;
  }

  public void setCooldown(JsonNullable<Float> cooldown) {
    this.cooldown = cooldown;
  }

  public TreatmentOption requirements(Map<String, Object> requirements) {
    this.requirements = JsonNullable.of(requirements);
    return this;
  }

  public TreatmentOption putRequirementsItem(String key, Object requirementsItem) {
    if (this.requirements == null || !this.requirements.isPresent()) {
      this.requirements = JsonNullable.of(new HashMap<>());
    }
    this.requirements.get().put(key, requirementsItem);
    return this;
  }

  /**
   * Требования для использования
   * @return requirements
   */
  
  @Schema(name = "requirements", description = "Требования для использования", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public JsonNullable<Map<String, Object>> getRequirements() {
    return requirements;
  }

  public void setRequirements(JsonNullable<Map<String, Object>> requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreatmentOption treatmentOption = (TreatmentOption) o;
    return Objects.equals(this.type, treatmentOption.type) &&
        Objects.equals(this.name, treatmentOption.name) &&
        Objects.equals(this.description, treatmentOption.description) &&
        Objects.equals(this.cost, treatmentOption.cost) &&
        Objects.equals(this.effectiveness, treatmentOption.effectiveness) &&
        equalsNullable(this.cooldown, treatmentOption.cooldown) &&
        equalsNullable(this.requirements, treatmentOption.requirements);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name, description, cost, effectiveness, hashCodeNullable(cooldown), hashCodeNullable(requirements));
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
    sb.append("class TreatmentOption {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    effectiveness: ").append(toIndentedString(effectiveness)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

