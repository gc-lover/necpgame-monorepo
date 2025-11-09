package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * HireableNPC
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class HireableNPC {

  private @Nullable String npcId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    COMBAT("COMBAT"),
    
    TECH("TECH"),
    
    HACKER("HACKER"),
    
    MEDIC("MEDIC"),
    
    DRIVER("DRIVER"),
    
    MERCHANT("MERCHANT"),
    
    BODYGUARD("BODYGUARD");

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

  private @Nullable TypeEnum type;

  @Valid
  private Map<String, Integer> skills = new HashMap<>();

  private @Nullable Integer costPerDay;

  private @Nullable Integer loyalty;

  private @Nullable String specialization;

  @Valid
  private List<String> equipment = new ArrayList<>();

  /**
   * Gets or Sets availability
   */
  public enum AvailabilityEnum {
    AVAILABLE("AVAILABLE"),
    
    BUSY("BUSY"),
    
    EXCLUSIVE("EXCLUSIVE");

    private final String value;

    AvailabilityEnum(String value) {
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
    public static AvailabilityEnum fromValue(String value) {
      for (AvailabilityEnum b : AvailabilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AvailabilityEnum availability;

  public HireableNPC npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public HireableNPC name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public HireableNPC type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public HireableNPC skills(Map<String, Integer> skills) {
    this.skills = skills;
    return this;
  }

  public HireableNPC putSkillsItem(String key, Integer skillsItem) {
    if (this.skills == null) {
      this.skills = new HashMap<>();
    }
    this.skills.put(key, skillsItem);
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public Map<String, Integer> getSkills() {
    return skills;
  }

  public void setSkills(Map<String, Integer> skills) {
    this.skills = skills;
  }

  public HireableNPC costPerDay(@Nullable Integer costPerDay) {
    this.costPerDay = costPerDay;
    return this;
  }

  /**
   * Get costPerDay
   * @return costPerDay
   */
  
  @Schema(name = "cost_per_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_per_day")
  public @Nullable Integer getCostPerDay() {
    return costPerDay;
  }

  public void setCostPerDay(@Nullable Integer costPerDay) {
    this.costPerDay = costPerDay;
  }

  public HireableNPC loyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
    return this;
  }

  /**
   * Get loyalty
   * minimum: 0
   * maximum: 100
   * @return loyalty
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "loyalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyalty")
  public @Nullable Integer getLoyalty() {
    return loyalty;
  }

  public void setLoyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
  }

  public HireableNPC specialization(@Nullable String specialization) {
    this.specialization = specialization;
    return this;
  }

  /**
   * Get specialization
   * @return specialization
   */
  
  @Schema(name = "specialization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specialization")
  public @Nullable String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(@Nullable String specialization) {
    this.specialization = specialization;
  }

  public HireableNPC equipment(List<String> equipment) {
    this.equipment = equipment;
    return this;
  }

  public HireableNPC addEquipmentItem(String equipmentItem) {
    if (this.equipment == null) {
      this.equipment = new ArrayList<>();
    }
    this.equipment.add(equipmentItem);
    return this;
  }

  /**
   * Get equipment
   * @return equipment
   */
  
  @Schema(name = "equipment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equipment")
  public List<String> getEquipment() {
    return equipment;
  }

  public void setEquipment(List<String> equipment) {
    this.equipment = equipment;
  }

  public HireableNPC availability(@Nullable AvailabilityEnum availability) {
    this.availability = availability;
    return this;
  }

  /**
   * Get availability
   * @return availability
   */
  
  @Schema(name = "availability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availability")
  public @Nullable AvailabilityEnum getAvailability() {
    return availability;
  }

  public void setAvailability(@Nullable AvailabilityEnum availability) {
    this.availability = availability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HireableNPC hireableNPC = (HireableNPC) o;
    return Objects.equals(this.npcId, hireableNPC.npcId) &&
        Objects.equals(this.name, hireableNPC.name) &&
        Objects.equals(this.type, hireableNPC.type) &&
        Objects.equals(this.skills, hireableNPC.skills) &&
        Objects.equals(this.costPerDay, hireableNPC.costPerDay) &&
        Objects.equals(this.loyalty, hireableNPC.loyalty) &&
        Objects.equals(this.specialization, hireableNPC.specialization) &&
        Objects.equals(this.equipment, hireableNPC.equipment) &&
        Objects.equals(this.availability, hireableNPC.availability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, type, skills, costPerDay, loyalty, specialization, equipment, availability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HireableNPC {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    costPerDay: ").append(toIndentedString(costPerDay)).append("\n");
    sb.append("    loyalty: ").append(toIndentedString(loyalty)).append("\n");
    sb.append("    specialization: ").append(toIndentedString(specialization)).append("\n");
    sb.append("    equipment: ").append(toIndentedString(equipment)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
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

