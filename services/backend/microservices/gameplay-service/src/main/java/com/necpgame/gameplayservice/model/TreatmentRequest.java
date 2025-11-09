package com.necpgame.gameplayservice.model;

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
 * TreatmentRequest
 */


public class TreatmentRequest {

  private String characterId;

  /**
   * Gets or Sets treatmentType
   */
  public enum TreatmentTypeEnum {
    THERAPY("therapy"),
    
    MEDICATION("medication"),
    
    IMPLANT_REMOVAL("implant_removal"),
    
    DETOX("detox"),
    
    SYMPTOM_MANAGEMENT("symptom_management");

    private final String value;

    TreatmentTypeEnum(String value) {
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
    public static TreatmentTypeEnum fromValue(String value) {
      for (TreatmentTypeEnum b : TreatmentTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TreatmentTypeEnum treatmentType;

  private @Nullable String npcProviderId;

  private @Nullable String implantToRemove;

  public TreatmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TreatmentRequest(String characterId, TreatmentTypeEnum treatmentType) {
    this.characterId = characterId;
    this.treatmentType = treatmentType;
  }

  public TreatmentRequest characterId(String characterId) {
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

  public TreatmentRequest treatmentType(TreatmentTypeEnum treatmentType) {
    this.treatmentType = treatmentType;
    return this;
  }

  /**
   * Get treatmentType
   * @return treatmentType
   */
  @NotNull 
  @Schema(name = "treatment_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("treatment_type")
  public TreatmentTypeEnum getTreatmentType() {
    return treatmentType;
  }

  public void setTreatmentType(TreatmentTypeEnum treatmentType) {
    this.treatmentType = treatmentType;
  }

  public TreatmentRequest npcProviderId(@Nullable String npcProviderId) {
    this.npcProviderId = npcProviderId;
    return this;
  }

  /**
   * ID NPC (medtech, терапевт, риппердок)
   * @return npcProviderId
   */
  
  @Schema(name = "npc_provider_id", description = "ID NPC (medtech, терапевт, риппердок)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_provider_id")
  public @Nullable String getNpcProviderId() {
    return npcProviderId;
  }

  public void setNpcProviderId(@Nullable String npcProviderId) {
    this.npcProviderId = npcProviderId;
  }

  public TreatmentRequest implantToRemove(@Nullable String implantToRemove) {
    this.implantToRemove = implantToRemove;
    return this;
  }

  /**
   * ID импланта для удаления (если treatment_type = implant_removal)
   * @return implantToRemove
   */
  
  @Schema(name = "implant_to_remove", description = "ID импланта для удаления (если treatment_type = implant_removal)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_to_remove")
  public @Nullable String getImplantToRemove() {
    return implantToRemove;
  }

  public void setImplantToRemove(@Nullable String implantToRemove) {
    this.implantToRemove = implantToRemove;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreatmentRequest treatmentRequest = (TreatmentRequest) o;
    return Objects.equals(this.characterId, treatmentRequest.characterId) &&
        Objects.equals(this.treatmentType, treatmentRequest.treatmentType) &&
        Objects.equals(this.npcProviderId, treatmentRequest.npcProviderId) &&
        Objects.equals(this.implantToRemove, treatmentRequest.implantToRemove);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, treatmentType, npcProviderId, implantToRemove);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TreatmentRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    treatmentType: ").append(toIndentedString(treatmentType)).append("\n");
    sb.append("    npcProviderId: ").append(toIndentedString(npcProviderId)).append("\n");
    sb.append("    implantToRemove: ").append(toIndentedString(implantToRemove)).append("\n");
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

