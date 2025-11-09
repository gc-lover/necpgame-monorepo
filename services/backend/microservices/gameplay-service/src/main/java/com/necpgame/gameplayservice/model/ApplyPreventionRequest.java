package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ApplyPreventionRequest
 */


public class ApplyPreventionRequest {

  /**
   * Метод профилактики киберпсихоза
   */
  public enum MethodEnum {
    THERAPY_SESSION("therapy_session"),
    
    MEDITATION_PROTOCOL("meditation_protocol"),
    
    NEUROFEEDBACK("neurofeedback"),
    
    MEDICAL_SEDATION("medical_sedation"),
    
    COMMUNITY_SUPPORT("community_support");

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

  private Integer durationHours;

  private JsonNullable<String> facilitatorId = JsonNullable.<String>undefined();

  @Valid
  private List<String> resourcesUsed = new ArrayList<>();

  public ApplyPreventionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyPreventionRequest(MethodEnum method, Integer durationHours) {
    this.method = method;
    this.durationHours = durationHours;
  }

  public ApplyPreventionRequest method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * Метод профилактики киберпсихоза
   * @return method
   */
  @NotNull 
  @Schema(name = "method", example = "neurofeedback", description = "Метод профилактики киберпсихоза", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("method")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public ApplyPreventionRequest durationHours(Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Продолжительность профилактики в часах
   * minimum: 1
   * maximum: 168
   * @return durationHours
   */
  @NotNull @Min(value = 1) @Max(value = 168) 
  @Schema(name = "duration_hours", example = "6", description = "Продолжительность профилактики в часах", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration_hours")
  public Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(Integer durationHours) {
    this.durationHours = durationHours;
  }

  public ApplyPreventionRequest facilitatorId(String facilitatorId) {
    this.facilitatorId = JsonNullable.of(facilitatorId);
    return this;
  }

  /**
   * Идентификатор специалиста, проводящего процедуру
   * @return facilitatorId
   */
  
  @Schema(name = "facilitator_id", example = "npc-therapist-river", description = "Идентификатор специалиста, проводящего процедуру", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facilitator_id")
  public JsonNullable<String> getFacilitatorId() {
    return facilitatorId;
  }

  public void setFacilitatorId(JsonNullable<String> facilitatorId) {
    this.facilitatorId = facilitatorId;
  }

  public ApplyPreventionRequest resourcesUsed(List<String> resourcesUsed) {
    this.resourcesUsed = resourcesUsed;
    return this;
  }

  public ApplyPreventionRequest addResourcesUsedItem(String resourcesUsedItem) {
    if (this.resourcesUsed == null) {
      this.resourcesUsed = new ArrayList<>();
    }
    this.resourcesUsed.add(resourcesUsedItem);
    return this;
  }

  /**
   * Идентификаторы ресурсов, использованных в процедуре
   * @return resourcesUsed
   */
  
  @Schema(name = "resources_used", description = "Идентификаторы ресурсов, использованных в процедуре", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resources_used")
  public List<String> getResourcesUsed() {
    return resourcesUsed;
  }

  public void setResourcesUsed(List<String> resourcesUsed) {
    this.resourcesUsed = resourcesUsed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyPreventionRequest applyPreventionRequest = (ApplyPreventionRequest) o;
    return Objects.equals(this.method, applyPreventionRequest.method) &&
        Objects.equals(this.durationHours, applyPreventionRequest.durationHours) &&
        equalsNullable(this.facilitatorId, applyPreventionRequest.facilitatorId) &&
        Objects.equals(this.resourcesUsed, applyPreventionRequest.resourcesUsed);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, durationHours, hashCodeNullable(facilitatorId), resourcesUsed);
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
    sb.append("class ApplyPreventionRequest {\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    facilitatorId: ").append(toIndentedString(facilitatorId)).append("\n");
    sb.append("    resourcesUsed: ").append(toIndentedString(resourcesUsed)).append("\n");
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

