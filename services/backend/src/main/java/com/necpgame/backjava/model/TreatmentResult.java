package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ Р»РµС‡РµРЅРёСЏ. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; Р›РµС‡РµРЅРёРµ 
 */

@Schema(name = "TreatmentResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ Р»РµС‡РµРЅРёСЏ. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> Р›РµС‡РµРЅРёРµ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class TreatmentResult {

  private Float humanityRestored;

  private Float cost;

  private Float duration;

  private JsonNullable<@DecimalMin(value = "0") Float> cooldown = JsonNullable.<Float>undefined();

  @Valid
  private List<String> limitations = new ArrayList<>();

  public TreatmentResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TreatmentResult(Float humanityRestored, Float cost, Float duration) {
    this.humanityRestored = humanityRestored;
    this.cost = cost;
    this.duration = duration;
  }

  public TreatmentResult humanityRestored(Float humanityRestored) {
    this.humanityRestored = humanityRestored;
    return this;
  }

  /**
   * Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅР°СЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ
   * minimum: 0
   * @return humanityRestored
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "humanity_restored", description = "Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅР°СЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity_restored")
  public Float getHumanityRestored() {
    return humanityRestored;
  }

  public void setHumanityRestored(Float humanityRestored) {
    this.humanityRestored = humanityRestored;
  }

  public TreatmentResult cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * РЎС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "РЎС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  public TreatmentResult duration(Float duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return duration
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public TreatmentResult cooldown(Float cooldown) {
    this.cooldown = JsonNullable.of(cooldown);
    return this;
  }

  /**
   * РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµРіРѕ Р»РµС‡РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return cooldown
   */
  @DecimalMin(value = "0") 
  @Schema(name = "cooldown", description = "РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµРіРѕ Р»РµС‡РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public JsonNullable<@DecimalMin(value = "0") Float> getCooldown() {
    return cooldown;
  }

  public void setCooldown(JsonNullable<Float> cooldown) {
    this.cooldown = cooldown;
  }

  public TreatmentResult limitations(List<String> limitations) {
    this.limitations = limitations;
    return this;
  }

  public TreatmentResult addLimitationsItem(String limitationsItem) {
    if (this.limitations == null) {
      this.limitations = new ArrayList<>();
    }
    this.limitations.add(limitationsItem);
    return this;
  }

  /**
   * РћРіСЂР°РЅРёС‡РµРЅРёСЏ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ (РЅР°РїСЂРёРјРµСЂ, РЅРµР»СЊР·СЏ РїРѕР»РЅРѕСЃС‚СЊСЋ РІРѕСЃСЃС‚Р°РЅРѕРІРёС‚СЊ РїРѕСЃР»Рµ 25% РїРѕС‚РµСЂРё)
   * @return limitations
   */
  
  @Schema(name = "limitations", description = "РћРіСЂР°РЅРёС‡РµРЅРёСЏ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ (РЅР°РїСЂРёРјРµСЂ, РЅРµР»СЊР·СЏ РїРѕР»РЅРѕСЃС‚СЊСЋ РІРѕСЃСЃС‚Р°РЅРѕРІРёС‚СЊ РїРѕСЃР»Рµ 25% РїРѕС‚РµСЂРё)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limitations")
  public List<String> getLimitations() {
    return limitations;
  }

  public void setLimitations(List<String> limitations) {
    this.limitations = limitations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreatmentResult treatmentResult = (TreatmentResult) o;
    return Objects.equals(this.humanityRestored, treatmentResult.humanityRestored) &&
        Objects.equals(this.cost, treatmentResult.cost) &&
        Objects.equals(this.duration, treatmentResult.duration) &&
        equalsNullable(this.cooldown, treatmentResult.cooldown) &&
        Objects.equals(this.limitations, treatmentResult.limitations);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(humanityRestored, cost, duration, hashCodeNullable(cooldown), limitations);
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
    sb.append("class TreatmentResult {\n");
    sb.append("    humanityRestored: ").append(toIndentedString(humanityRestored)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
    sb.append("    limitations: ").append(toIndentedString(limitations)).append("\n");
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

