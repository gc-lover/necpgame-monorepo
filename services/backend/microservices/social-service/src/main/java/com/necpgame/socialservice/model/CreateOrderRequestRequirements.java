package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * CreateOrderRequestRequirements
 */

@JsonTypeName("CreateOrderRequest_requirements")

public class CreateOrderRequestRequirements {

  private @Nullable Integer minLevel;

  @Valid
  private Map<String, Integer> requiredSkills = new HashMap<>();

  @Valid
  private List<String> requiredEquipment = new ArrayList<>();

  public CreateOrderRequestRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * @return minLevel
   */
  
  @Schema(name = "min_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_level")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public CreateOrderRequestRequirements requiredSkills(Map<String, Integer> requiredSkills) {
    this.requiredSkills = requiredSkills;
    return this;
  }

  public CreateOrderRequestRequirements putRequiredSkillsItem(String key, Integer requiredSkillsItem) {
    if (this.requiredSkills == null) {
      this.requiredSkills = new HashMap<>();
    }
    this.requiredSkills.put(key, requiredSkillsItem);
    return this;
  }

  /**
   * Get requiredSkills
   * @return requiredSkills
   */
  
  @Schema(name = "required_skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_skills")
  public Map<String, Integer> getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(Map<String, Integer> requiredSkills) {
    this.requiredSkills = requiredSkills;
  }

  public CreateOrderRequestRequirements requiredEquipment(List<String> requiredEquipment) {
    this.requiredEquipment = requiredEquipment;
    return this;
  }

  public CreateOrderRequestRequirements addRequiredEquipmentItem(String requiredEquipmentItem) {
    if (this.requiredEquipment == null) {
      this.requiredEquipment = new ArrayList<>();
    }
    this.requiredEquipment.add(requiredEquipmentItem);
    return this;
  }

  /**
   * Get requiredEquipment
   * @return requiredEquipment
   */
  
  @Schema(name = "required_equipment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_equipment")
  public List<String> getRequiredEquipment() {
    return requiredEquipment;
  }

  public void setRequiredEquipment(List<String> requiredEquipment) {
    this.requiredEquipment = requiredEquipment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateOrderRequestRequirements createOrderRequestRequirements = (CreateOrderRequestRequirements) o;
    return Objects.equals(this.minLevel, createOrderRequestRequirements.minLevel) &&
        Objects.equals(this.requiredSkills, createOrderRequestRequirements.requiredSkills) &&
        Objects.equals(this.requiredEquipment, createOrderRequestRequirements.requiredEquipment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, requiredSkills, requiredEquipment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateOrderRequestRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    requiredSkills: ").append(toIndentedString(requiredSkills)).append("\n");
    sb.append("    requiredEquipment: ").append(toIndentedString(requiredEquipment)).append("\n");
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

