package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CalculateLossRequestMitigatingFactors;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculateLossRequest
 */


public class CalculateLossRequest {

  private UUID implantId;

  /**
   * Класс импланта
   */
  public enum ImplantClassEnum {
    COMBAT("combat"),
    
    NEURAL("neural"),
    
    SENSORY("sensory"),
    
    SUPPORT("support"),
    
    UTILITY("utility");

    private final String value;

    ImplantClassEnum(String value) {
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
    public static ImplantClassEnum fromValue(String value) {
      for (ImplantClassEnum b : ImplantClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ImplantClassEnum implantClass;

  /**
   * Редкость импланта, влияющая на базовый процент потери
   */
  public enum ImplantGradeEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    PROTOTYPE("prototype");

    private final String value;

    ImplantGradeEnum(String value) {
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
    public static ImplantGradeEnum fromValue(String value) {
      for (ImplantGradeEnum b : ImplantGradeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ImplantGradeEnum implantGrade;

  /**
   * Слот, в который устанавливается имплант
   */
  public enum SlotTypeEnum {
    NEURAL_CORE("neural_core"),
    
    LIMB("limb"),
    
    CIRCULATORY("circulatory"),
    
    IMMUNE("immune"),
    
    OCULAR("ocular"),
    
    DERMAL("dermal");

    private final String value;

    SlotTypeEnum(String value) {
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
    public static SlotTypeEnum fromValue(String value) {
      for (SlotTypeEnum b : SlotTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SlotTypeEnum slotType;

  private Float humanityCurrent;

  private @Nullable CalculateLossRequestMitigatingFactors mitigatingFactors;

  public CalculateLossRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateLossRequest(UUID implantId, ImplantClassEnum implantClass, ImplantGradeEnum implantGrade, SlotTypeEnum slotType, Float humanityCurrent) {
    this.implantId = implantId;
    this.implantClass = implantClass;
    this.implantGrade = implantGrade;
    this.slotType = slotType;
    this.humanityCurrent = humanityCurrent;
  }

  public CalculateLossRequest implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Идентификатор импланта, вызывающего потенциальную потерю человечности
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", example = "a2b41a5e-6fce-4d68-9eab-62c4e5f9b211", description = "Идентификатор импланта, вызывающего потенциальную потерю человечности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public CalculateLossRequest implantClass(ImplantClassEnum implantClass) {
    this.implantClass = implantClass;
    return this;
  }

  /**
   * Класс импланта
   * @return implantClass
   */
  @NotNull 
  @Schema(name = "implant_class", example = "combat", description = "Класс импланта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_class")
  public ImplantClassEnum getImplantClass() {
    return implantClass;
  }

  public void setImplantClass(ImplantClassEnum implantClass) {
    this.implantClass = implantClass;
  }

  public CalculateLossRequest implantGrade(ImplantGradeEnum implantGrade) {
    this.implantGrade = implantGrade;
    return this;
  }

  /**
   * Редкость импланта, влияющая на базовый процент потери
   * @return implantGrade
   */
  @NotNull 
  @Schema(name = "implant_grade", example = "epic", description = "Редкость импланта, влияющая на базовый процент потери", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_grade")
  public ImplantGradeEnum getImplantGrade() {
    return implantGrade;
  }

  public void setImplantGrade(ImplantGradeEnum implantGrade) {
    this.implantGrade = implantGrade;
  }

  public CalculateLossRequest slotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Слот, в который устанавливается имплант
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slot_type", example = "neural_core", description = "Слот, в который устанавливается имплант", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot_type")
  public SlotTypeEnum getSlotType() {
    return slotType;
  }

  public void setSlotType(SlotTypeEnum slotType) {
    this.slotType = slotType;
  }

  public CalculateLossRequest humanityCurrent(Float humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
    return this;
  }

  /**
   * Текущая человечность игрока
   * minimum: 0
   * maximum: 100
   * @return humanityCurrent
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "humanity_current", example = "72.5", description = "Текущая человечность игрока", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity_current")
  public Float getHumanityCurrent() {
    return humanityCurrent;
  }

  public void setHumanityCurrent(Float humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
  }

  public CalculateLossRequest mitigatingFactors(@Nullable CalculateLossRequestMitigatingFactors mitigatingFactors) {
    this.mitigatingFactors = mitigatingFactors;
    return this;
  }

  /**
   * Get mitigatingFactors
   * @return mitigatingFactors
   */
  @Valid 
  @Schema(name = "mitigating_factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mitigating_factors")
  public @Nullable CalculateLossRequestMitigatingFactors getMitigatingFactors() {
    return mitigatingFactors;
  }

  public void setMitigatingFactors(@Nullable CalculateLossRequestMitigatingFactors mitigatingFactors) {
    this.mitigatingFactors = mitigatingFactors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateLossRequest calculateLossRequest = (CalculateLossRequest) o;
    return Objects.equals(this.implantId, calculateLossRequest.implantId) &&
        Objects.equals(this.implantClass, calculateLossRequest.implantClass) &&
        Objects.equals(this.implantGrade, calculateLossRequest.implantGrade) &&
        Objects.equals(this.slotType, calculateLossRequest.slotType) &&
        Objects.equals(this.humanityCurrent, calculateLossRequest.humanityCurrent) &&
        Objects.equals(this.mitigatingFactors, calculateLossRequest.mitigatingFactors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, implantClass, implantGrade, slotType, humanityCurrent, mitigatingFactors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateLossRequest {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    implantClass: ").append(toIndentedString(implantClass)).append("\n");
    sb.append("    implantGrade: ").append(toIndentedString(implantGrade)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    humanityCurrent: ").append(toIndentedString(humanityCurrent)).append("\n");
    sb.append("    mitigatingFactors: ").append(toIndentedString(mitigatingFactors)).append("\n");
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

