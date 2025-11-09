package com.necpgame.gameplayservice.model;

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
 * ApplyLossRequest
 */


public class ApplyLossRequest {

  private Float lossAmount;

  /**
   * Причина потери человечности
   */
  public enum ReasonEnum {
    IMPLANT_INSTALLATION("implant_installation"),
    
    TRAUMA("trauma"),
    
    CYBERDRUGS("cyberdrugs"),
    
    OVERCLOCK("overclock"),
    
    EXPERIMENTAL_PROCEDURE("experimental_procedure");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReasonEnum reason;

  private JsonNullable<UUID> implantId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> appliedBy = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 500) String> notes = JsonNullable.<String>undefined();

  public ApplyLossRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyLossRequest(Float lossAmount, ReasonEnum reason) {
    this.lossAmount = lossAmount;
    this.reason = reason;
  }

  public ApplyLossRequest lossAmount(Float lossAmount) {
    this.lossAmount = lossAmount;
    return this;
  }

  /**
   * Фактическая потеря человечности, которая будет применена
   * minimum: 0
   * maximum: 100
   * @return lossAmount
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "loss_amount", example = "8.75", description = "Фактическая потеря человечности, которая будет применена", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loss_amount")
  public Float getLossAmount() {
    return lossAmount;
  }

  public void setLossAmount(Float lossAmount) {
    this.lossAmount = lossAmount;
  }

  public ApplyLossRequest reason(ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина потери человечности
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", example = "implant_installation", description = "Причина потери человечности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public ReasonEnum getReason() {
    return reason;
  }

  public void setReason(ReasonEnum reason) {
    this.reason = reason;
  }

  public ApplyLossRequest implantId(UUID implantId) {
    this.implantId = JsonNullable.of(implantId);
    return this;
  }

  /**
   * Идентификатор импланта, если потеря вызвана его установкой
   * @return implantId
   */
  @Valid 
  @Schema(name = "implant_id", example = "a2b41a5e-6fce-4d68-9eab-62c4e5f9b211", description = "Идентификатор импланта, если потеря вызвана его установкой", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public JsonNullable<UUID> getImplantId() {
    return implantId;
  }

  public void setImplantId(JsonNullable<UUID> implantId) {
    this.implantId = implantId;
  }

  public ApplyLossRequest appliedBy(String appliedBy) {
    this.appliedBy = JsonNullable.of(appliedBy);
    return this;
  }

  /**
   * Идентификатор врача/техника, подтвердившего потерю (если применимо)
   * @return appliedBy
   */
  
  @Schema(name = "applied_by", example = "npc-ripperdoc-viktor", description = "Идентификатор врача/техника, подтвердившего потерю (если применимо)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applied_by")
  public JsonNullable<String> getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(JsonNullable<String> appliedBy) {
    this.appliedBy = appliedBy;
  }

  public ApplyLossRequest notes(String notes) {
    this.notes = JsonNullable.of(notes);
    return this;
  }

  /**
   * Дополнительные комментарии по событию
   * @return notes
   */
  @Size(max = 500) 
  @Schema(name = "notes", description = "Дополнительные комментарии по событию", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public JsonNullable<@Size(max = 500) String> getNotes() {
    return notes;
  }

  public void setNotes(JsonNullable<String> notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyLossRequest applyLossRequest = (ApplyLossRequest) o;
    return Objects.equals(this.lossAmount, applyLossRequest.lossAmount) &&
        Objects.equals(this.reason, applyLossRequest.reason) &&
        equalsNullable(this.implantId, applyLossRequest.implantId) &&
        equalsNullable(this.appliedBy, applyLossRequest.appliedBy) &&
        equalsNullable(this.notes, applyLossRequest.notes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(lossAmount, reason, hashCodeNullable(implantId), hashCodeNullable(appliedBy), hashCodeNullable(notes));
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
    sb.append("class ApplyLossRequest {\n");
    sb.append("    lossAmount: ").append(toIndentedString(lossAmount)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    appliedBy: ").append(toIndentedString(appliedBy)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

