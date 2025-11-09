package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
 * РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Рµ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёРµ РѕРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Р­РЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ Р»РёРјРёС‚ 
 */

@Schema(name = "IndividualEnergyLimits", description = "РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Рµ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРёРµ РѕРіСЂР°РЅРёС‡РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Р­РЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ Р»РёРјРёС‚ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class IndividualEnergyLimits {

  private UUID implantId;

  private Float individualLimit;

  private Float currentUsage;

  private Boolean canExceed;

  @Valid
  private JsonNullable<Map<String, Object>> penaltyOnExceed = JsonNullable.<Map<String, Object>>undefined();

  public IndividualEnergyLimits() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndividualEnergyLimits(UUID implantId, Float individualLimit, Float currentUsage, Boolean canExceed) {
    this.implantId = implantId;
    this.individualLimit = individualLimit;
    this.currentUsage = currentUsage;
    this.canExceed = canExceed;
  }

  public IndividualEnergyLimits implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р°
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public IndividualEnergyLimits individualLimit(Float individualLimit) {
    this.individualLimit = individualLimit;
    return this;
  }

  /**
   * РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return individualLimit
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "individual_limit", description = "РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("individual_limit")
  public Float getIndividualLimit() {
    return individualLimit;
  }

  public void setIndividualLimit(Float individualLimit) {
    this.individualLimit = individualLimit;
  }

  public IndividualEnergyLimits currentUsage(Float currentUsage) {
    this.currentUsage = currentUsage;
    return this;
  }

  /**
   * РўРµРєСѓС‰РµРµ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return currentUsage
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "current_usage", description = "РўРµРєСѓС‰РµРµ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_usage")
  public Float getCurrentUsage() {
    return currentUsage;
  }

  public void setCurrentUsage(Float currentUsage) {
    this.currentUsage = currentUsage;
  }

  public IndividualEnergyLimits canExceed(Boolean canExceed) {
    this.canExceed = canExceed;
    return this;
  }

  /**
   * РњРѕР¶РЅРѕ Р»Рё РїСЂРµРІС‹СЃРёС‚СЊ РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚
   * @return canExceed
   */
  @NotNull 
  @Schema(name = "can_exceed", description = "РњРѕР¶РЅРѕ Р»Рё РїСЂРµРІС‹СЃРёС‚СЊ РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("can_exceed")
  public Boolean getCanExceed() {
    return canExceed;
  }

  public void setCanExceed(Boolean canExceed) {
    this.canExceed = canExceed;
  }

  public IndividualEnergyLimits penaltyOnExceed(Map<String, Object> penaltyOnExceed) {
    this.penaltyOnExceed = JsonNullable.of(penaltyOnExceed);
    return this;
  }

  public IndividualEnergyLimits putPenaltyOnExceedItem(String key, Object penaltyOnExceedItem) {
    if (this.penaltyOnExceed == null || !this.penaltyOnExceed.isPresent()) {
      this.penaltyOnExceed = JsonNullable.of(new HashMap<>());
    }
    this.penaltyOnExceed.get().put(key, penaltyOnExceedItem);
    return this;
  }

  /**
   * РЁС‚СЂР°С„С‹ РїСЂРё РїСЂРµРІС‹С€РµРЅРёРё Р»РёРјРёС‚Р°
   * @return penaltyOnExceed
   */
  
  @Schema(name = "penalty_on_exceed", description = "РЁС‚СЂР°С„С‹ РїСЂРё РїСЂРµРІС‹С€РµРЅРёРё Р»РёРјРёС‚Р°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalty_on_exceed")
  public JsonNullable<Map<String, Object>> getPenaltyOnExceed() {
    return penaltyOnExceed;
  }

  public void setPenaltyOnExceed(JsonNullable<Map<String, Object>> penaltyOnExceed) {
    this.penaltyOnExceed = penaltyOnExceed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndividualEnergyLimits individualEnergyLimits = (IndividualEnergyLimits) o;
    return Objects.equals(this.implantId, individualEnergyLimits.implantId) &&
        Objects.equals(this.individualLimit, individualEnergyLimits.individualLimit) &&
        Objects.equals(this.currentUsage, individualEnergyLimits.currentUsage) &&
        Objects.equals(this.canExceed, individualEnergyLimits.canExceed) &&
        equalsNullable(this.penaltyOnExceed, individualEnergyLimits.penaltyOnExceed);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, individualLimit, currentUsage, canExceed, hashCodeNullable(penaltyOnExceed));
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
    sb.append("class IndividualEnergyLimits {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    individualLimit: ").append(toIndentedString(individualLimit)).append("\n");
    sb.append("    currentUsage: ").append(toIndentedString(currentUsage)).append("\n");
    sb.append("    canExceed: ").append(toIndentedString(canExceed)).append("\n");
    sb.append("    penaltyOnExceed: ").append(toIndentedString(penaltyOnExceed)).append("\n");
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

