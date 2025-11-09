package com.necpgame.gameplayservice.model;

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
 * DetoxificationRequest
 */


public class DetoxificationRequest {

  /**
   * Протокол детоксикации
   */
  public enum ProtocolEnum {
    CHEMICAL_FLUSH("chemical_flush"),
    
    NEURAL_COOLING("neural_cooling"),
    
    NANOBOT_CLEANUP("nanobot_cleanup"),
    
    CYBERDRUG_PURGE("cyberdrug_purge");

    private final String value;

    ProtocolEnum(String value) {
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
    public static ProtocolEnum fromValue(String value) {
      for (ProtocolEnum b : ProtocolEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ProtocolEnum protocol;

  private Integer durationHours;

  /**
   * Требуемый уровень наблюдения
   */
  public enum SupervisionLevelEnum {
    SELF("self"),
    
    MEDIC("medic"),
    
    SPECIALIST("specialist"),
    
    CORPORATE_TEAM("corporate_team");

    private final String value;

    SupervisionLevelEnum(String value) {
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
    public static SupervisionLevelEnum fromValue(String value) {
      for (SupervisionLevelEnum b : SupervisionLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SupervisionLevelEnum supervisionLevel;

  @Valid
  private List<String> supportiveMedication = new ArrayList<>();

  public DetoxificationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DetoxificationRequest(ProtocolEnum protocol, Integer durationHours) {
    this.protocol = protocol;
    this.durationHours = durationHours;
  }

  public DetoxificationRequest protocol(ProtocolEnum protocol) {
    this.protocol = protocol;
    return this;
  }

  /**
   * Протокол детоксикации
   * @return protocol
   */
  @NotNull 
  @Schema(name = "protocol", example = "neural_cooling", description = "Протокол детоксикации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("protocol")
  public ProtocolEnum getProtocol() {
    return protocol;
  }

  public void setProtocol(ProtocolEnum protocol) {
    this.protocol = protocol;
  }

  public DetoxificationRequest durationHours(Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Продолжительность детоксикации
   * minimum: 1
   * maximum: 168
   * @return durationHours
   */
  @NotNull @Min(value = 1) @Max(value = 168) 
  @Schema(name = "duration_hours", example = "24", description = "Продолжительность детоксикации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration_hours")
  public Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(Integer durationHours) {
    this.durationHours = durationHours;
  }

  public DetoxificationRequest supervisionLevel(@Nullable SupervisionLevelEnum supervisionLevel) {
    this.supervisionLevel = supervisionLevel;
    return this;
  }

  /**
   * Требуемый уровень наблюдения
   * @return supervisionLevel
   */
  
  @Schema(name = "supervision_level", example = "specialist", description = "Требуемый уровень наблюдения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supervision_level")
  public @Nullable SupervisionLevelEnum getSupervisionLevel() {
    return supervisionLevel;
  }

  public void setSupervisionLevel(@Nullable SupervisionLevelEnum supervisionLevel) {
    this.supervisionLevel = supervisionLevel;
  }

  public DetoxificationRequest supportiveMedication(List<String> supportiveMedication) {
    this.supportiveMedication = supportiveMedication;
    return this;
  }

  public DetoxificationRequest addSupportiveMedicationItem(String supportiveMedicationItem) {
    if (this.supportiveMedication == null) {
      this.supportiveMedication = new ArrayList<>();
    }
    this.supportiveMedication.add(supportiveMedicationItem);
    return this;
  }

  /**
   * Применяемые поддерживающие препараты
   * @return supportiveMedication
   */
  
  @Schema(name = "supportive_medication", description = "Применяемые поддерживающие препараты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportive_medication")
  public List<String> getSupportiveMedication() {
    return supportiveMedication;
  }

  public void setSupportiveMedication(List<String> supportiveMedication) {
    this.supportiveMedication = supportiveMedication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetoxificationRequest detoxificationRequest = (DetoxificationRequest) o;
    return Objects.equals(this.protocol, detoxificationRequest.protocol) &&
        Objects.equals(this.durationHours, detoxificationRequest.durationHours) &&
        Objects.equals(this.supervisionLevel, detoxificationRequest.supervisionLevel) &&
        Objects.equals(this.supportiveMedication, detoxificationRequest.supportiveMedication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(protocol, durationHours, supervisionLevel, supportiveMedication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetoxificationRequest {\n");
    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    supervisionLevel: ").append(toIndentedString(supervisionLevel)).append("\n");
    sb.append("    supportiveMedication: ").append(toIndentedString(supportiveMedication)).append("\n");
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

