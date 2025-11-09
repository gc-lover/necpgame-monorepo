package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Р РµР·СѓР»СЊС‚Р°С‚ РґРµС‚РѕРєСЃРёРєР°С†РёРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; Р”РµС‚РѕРєСЃРёРєР°С†РёСЏ 
 */

@Schema(name = "DetoxificationResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РґРµС‚РѕРєСЃРёРєР°С†РёРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> Р”РµС‚РѕРєСЃРёРєР°С†РёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class DetoxificationResult {

  private Float humanityRestored;

  private Float cost;

  private Float duration;

  private Float cooldown;

  public DetoxificationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DetoxificationResult(Float humanityRestored, Float cost, Float duration, Float cooldown) {
    this.humanityRestored = humanityRestored;
    this.cost = cost;
    this.duration = duration;
    this.cooldown = cooldown;
  }

  public DetoxificationResult humanityRestored(Float humanityRestored) {
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

  public DetoxificationResult cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * РЎС‚РѕРёРјРѕСЃС‚СЊ РґРµС‚РѕРєСЃРёРєР°С†РёРё
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "РЎС‚РѕРёРјРѕСЃС‚СЊ РґРµС‚РѕРєСЃРёРєР°С†РёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  public DetoxificationResult duration(Float duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ РїСЂРѕС†РµРґСѓСЂС‹ РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return duration
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ РїСЂРѕС†РµРґСѓСЂС‹ РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public DetoxificationResult cooldown(Float cooldown) {
    this.cooldown = cooldown;
    return this;
  }

  /**
   * РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµР№ РґРµС‚РѕРєСЃРёРєР°С†РёРё РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return cooldown
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cooldown", description = "РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµР№ РґРµС‚РѕРєСЃРёРєР°С†РёРё РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cooldown")
  public Float getCooldown() {
    return cooldown;
  }

  public void setCooldown(Float cooldown) {
    this.cooldown = cooldown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetoxificationResult detoxificationResult = (DetoxificationResult) o;
    return Objects.equals(this.humanityRestored, detoxificationResult.humanityRestored) &&
        Objects.equals(this.cost, detoxificationResult.cost) &&
        Objects.equals(this.duration, detoxificationResult.duration) &&
        Objects.equals(this.cooldown, detoxificationResult.cooldown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(humanityRestored, cost, duration, cooldown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetoxificationResult {\n");
    sb.append("    humanityRestored: ").append(toIndentedString(humanityRestored)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
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

