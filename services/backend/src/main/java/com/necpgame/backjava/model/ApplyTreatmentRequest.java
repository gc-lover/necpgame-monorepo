package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
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
 * Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ Р»РµС‡РµРЅРёСЏ
 */

@Schema(name = "ApplyTreatmentRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ Р»РµС‡РµРЅРёСЏ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ApplyTreatmentRequest {

  /**
   * РўРёРї Р»РµС‡РµРЅРёСЏ
   */
  public enum TreatmentTypeEnum {
    THERAPY("therapy"),
    
    MEDICATION("medication"),
    
    IMPLANT_REMOVAL("implant_removal"),
    
    DETOXIFICATION("detoxification");

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

  private JsonNullable<UUID> npcId = JsonNullable.<UUID>undefined();

  public ApplyTreatmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyTreatmentRequest(TreatmentTypeEnum treatmentType) {
    this.treatmentType = treatmentType;
  }

  public ApplyTreatmentRequest treatmentType(TreatmentTypeEnum treatmentType) {
    this.treatmentType = treatmentType;
    return this;
  }

  /**
   * РўРёРї Р»РµС‡РµРЅРёСЏ
   * @return treatmentType
   */
  @NotNull 
  @Schema(name = "treatment_type", description = "РўРёРї Р»РµС‡РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("treatment_type")
  public TreatmentTypeEnum getTreatmentType() {
    return treatmentType;
  }

  public void setTreatmentType(TreatmentTypeEnum treatmentType) {
    this.treatmentType = treatmentType;
  }

  public ApplyTreatmentRequest npcId(UUID npcId) {
    this.npcId = JsonNullable.of(npcId);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC РґР»СЏ Р»РµС‡РµРЅРёСЏ
   * @return npcId
   */
  @Valid 
  @Schema(name = "npc_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC РґР»СЏ Р»РµС‡РµРЅРёСЏ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public JsonNullable<UUID> getNpcId() {
    return npcId;
  }

  public void setNpcId(JsonNullable<UUID> npcId) {
    this.npcId = npcId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyTreatmentRequest applyTreatmentRequest = (ApplyTreatmentRequest) o;
    return Objects.equals(this.treatmentType, applyTreatmentRequest.treatmentType) &&
        equalsNullable(this.npcId, applyTreatmentRequest.npcId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(treatmentType, hashCodeNullable(npcId));
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
    sb.append("class ApplyTreatmentRequest {\n");
    sb.append("    treatmentType: ").append(toIndentedString(treatmentType)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
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

