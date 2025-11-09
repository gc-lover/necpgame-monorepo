package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
 */

@Schema(name = "ApplyLossRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ApplyLossRequest {

  private Float lossAmount;

  private String reason;

  private JsonNullable<UUID> implantId = JsonNullable.<UUID>undefined();

  public ApplyLossRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyLossRequest(Float lossAmount, String reason) {
    this.lossAmount = lossAmount;
    this.reason = reason;
  }

  public ApplyLossRequest lossAmount(Float lossAmount) {
    this.lossAmount = lossAmount;
    return this;
  }

  /**
   * РљРѕР»РёС‡РµСЃС‚РІРѕ РїРѕС‚РµСЂСЏРЅРЅРѕР№ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
   * minimum: 0
   * @return lossAmount
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "loss_amount", description = "РљРѕР»РёС‡РµСЃС‚РІРѕ РїРѕС‚РµСЂСЏРЅРЅРѕР№ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loss_amount")
  public Float getLossAmount() {
    return lossAmount;
  }

  public void setLossAmount(Float lossAmount) {
    this.lossAmount = lossAmount;
  }

  public ApplyLossRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * РџСЂРёС‡РёРЅР° РїРѕС‚РµСЂРё (implant_install, progression, trigger)
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", description = "РџСЂРёС‡РёРЅР° РїРѕС‚РµСЂРё (implant_install, progression, trigger)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ApplyLossRequest implantId(UUID implantId) {
    this.implantId = JsonNullable.of(implantId);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° (РµСЃР»Рё РїСЂРёС‡РёРЅР° = implant_install)
   * @return implantId
   */
  @Valid 
  @Schema(name = "implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° (РµСЃР»Рё РїСЂРёС‡РёРЅР° = implant_install)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public JsonNullable<UUID> getImplantId() {
    return implantId;
  }

  public void setImplantId(JsonNullable<UUID> implantId) {
    this.implantId = implantId;
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
        equalsNullable(this.implantId, applyLossRequest.implantId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(lossAmount, reason, hashCodeNullable(implantId));
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

