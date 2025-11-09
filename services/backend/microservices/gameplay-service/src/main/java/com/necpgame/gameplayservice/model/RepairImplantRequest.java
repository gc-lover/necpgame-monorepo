package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * RepairImplantRequest
 */

@JsonTypeName("repairImplant_request")

public class RepairImplantRequest {

  private String characterId;

  /**
   * Gets or Sets repairMethod
   */
  public enum RepairMethodEnum {
    RIPPERDOC("ripperdoc"),
    
    SELF_REPAIR("self_repair");

    private final String value;

    RepairMethodEnum(String value) {
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
    public static RepairMethodEnum fromValue(String value) {
      for (RepairMethodEnum b : RepairMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RepairMethodEnum repairMethod;

  private @Nullable String ripperdocId;

  @Valid
  private List<String> materials = new ArrayList<>();

  public RepairImplantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RepairImplantRequest(String characterId, RepairMethodEnum repairMethod) {
    this.characterId = characterId;
    this.repairMethod = repairMethod;
  }

  public RepairImplantRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public RepairImplantRequest repairMethod(RepairMethodEnum repairMethod) {
    this.repairMethod = repairMethod;
    return this;
  }

  /**
   * Get repairMethod
   * @return repairMethod
   */
  @NotNull 
  @Schema(name = "repair_method", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("repair_method")
  public RepairMethodEnum getRepairMethod() {
    return repairMethod;
  }

  public void setRepairMethod(RepairMethodEnum repairMethod) {
    this.repairMethod = repairMethod;
  }

  public RepairImplantRequest ripperdocId(@Nullable String ripperdocId) {
    this.ripperdocId = ripperdocId;
    return this;
  }

  /**
   * ID риппердока (если repair_method=ripperdoc)
   * @return ripperdocId
   */
  
  @Schema(name = "ripperdoc_id", description = "ID риппердока (если repair_method=ripperdoc)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ripperdoc_id")
  public @Nullable String getRipperdocId() {
    return ripperdocId;
  }

  public void setRipperdocId(@Nullable String ripperdocId) {
    this.ripperdocId = ripperdocId;
  }

  public RepairImplantRequest materials(List<String> materials) {
    this.materials = materials;
    return this;
  }

  public RepairImplantRequest addMaterialsItem(String materialsItem) {
    if (this.materials == null) {
      this.materials = new ArrayList<>();
    }
    this.materials.add(materialsItem);
    return this;
  }

  /**
   * Материалы для ремонта
   * @return materials
   */
  
  @Schema(name = "materials", description = "Материалы для ремонта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("materials")
  public List<String> getMaterials() {
    return materials;
  }

  public void setMaterials(List<String> materials) {
    this.materials = materials;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RepairImplantRequest repairImplantRequest = (RepairImplantRequest) o;
    return Objects.equals(this.characterId, repairImplantRequest.characterId) &&
        Objects.equals(this.repairMethod, repairImplantRequest.repairMethod) &&
        Objects.equals(this.ripperdocId, repairImplantRequest.ripperdocId) &&
        Objects.equals(this.materials, repairImplantRequest.materials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, repairMethod, ripperdocId, materials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RepairImplantRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    repairMethod: ").append(toIndentedString(repairMethod)).append("\n");
    sb.append("    ripperdocId: ").append(toIndentedString(ripperdocId)).append("\n");
    sb.append("    materials: ").append(toIndentedString(materials)).append("\n");
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

