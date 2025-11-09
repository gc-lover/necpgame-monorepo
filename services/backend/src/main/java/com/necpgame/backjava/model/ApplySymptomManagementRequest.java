package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё
 */

@Schema(name = "ApplySymptomManagementRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ApplySymptomManagementRequest {

  /**
   * РњРµС‚РѕРґ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё
   */
  public enum MethodEnum {
    MEDICATION("medication"),
    
    THERAPY("therapy"),
    
    IMPLANT("implant");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MethodEnum method;

  @Valid
  private List<UUID> symptomIds = new ArrayList<>();

  public ApplySymptomManagementRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplySymptomManagementRequest(MethodEnum method) {
    this.method = method;
  }

  public ApplySymptomManagementRequest method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * РњРµС‚РѕРґ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё
   * @return method
   */
  @NotNull 
  @Schema(name = "method", description = "РњРµС‚РѕРґ СѓРїСЂР°РІР»РµРЅРёСЏ СЃРёРјРїС‚РѕРјР°РјРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("method")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public ApplySymptomManagementRequest symptomIds(List<UUID> symptomIds) {
    this.symptomIds = symptomIds;
    return this;
  }

  public ApplySymptomManagementRequest addSymptomIdsItem(UUID symptomIdsItem) {
    if (this.symptomIds == null) {
      this.symptomIds = new ArrayList<>();
    }
    this.symptomIds.add(symptomIdsItem);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ СЃРёРјРїС‚РѕРјРѕРІ РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ
   * @return symptomIds
   */
  @Valid 
  @Schema(name = "symptom_ids", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ СЃРёРјРїС‚РѕРјРѕРІ РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symptom_ids")
  public List<UUID> getSymptomIds() {
    return symptomIds;
  }

  public void setSymptomIds(List<UUID> symptomIds) {
    this.symptomIds = symptomIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplySymptomManagementRequest applySymptomManagementRequest = (ApplySymptomManagementRequest) o;
    return Objects.equals(this.method, applySymptomManagementRequest.method) &&
        Objects.equals(this.symptomIds, applySymptomManagementRequest.symptomIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, symptomIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplySymptomManagementRequest {\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    symptomIds: ").append(toIndentedString(symptomIds)).append("\n");
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

