package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ СЌРЅРµСЂРіРёРё 
 */

@Schema(name = "EnergyRestoreResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ СЌРЅРµСЂРіРёРё ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EnergyRestoreResult {

  private Float newLevel;

  private Float restoredAmount;

  private JsonNullable<@DecimalMin(value = "0") Float> cooldown = JsonNullable.<Float>undefined();

  public EnergyRestoreResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnergyRestoreResult(Float newLevel, Float restoredAmount) {
    this.newLevel = newLevel;
    this.restoredAmount = restoredAmount;
  }

  public EnergyRestoreResult newLevel(Float newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return newLevel
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "new_level", description = "РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("new_level")
  public Float getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(Float newLevel) {
    this.newLevel = newLevel;
  }

  public EnergyRestoreResult restoredAmount(Float restoredAmount) {
    this.restoredAmount = restoredAmount;
    return this;
  }

  /**
   * Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅРѕРµ РєРѕР»РёС‡РµСЃС‚РІРѕ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return restoredAmount
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "restored_amount", description = "Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅРѕРµ РєРѕР»РёС‡РµСЃС‚РІРѕ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("restored_amount")
  public Float getRestoredAmount() {
    return restoredAmount;
  }

  public void setRestoredAmount(Float restoredAmount) {
    this.restoredAmount = restoredAmount;
  }

  public EnergyRestoreResult cooldown(Float cooldown) {
    this.cooldown = JsonNullable.of(cooldown);
    return this;
  }

  /**
   * РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµРіРѕ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return cooldown
   */
  @DecimalMin(value = "0") 
  @Schema(name = "cooldown", description = "РљСѓР»РґР°СѓРЅ РґРѕ СЃР»РµРґСѓСЋС‰РµРіРѕ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public JsonNullable<@DecimalMin(value = "0") Float> getCooldown() {
    return cooldown;
  }

  public void setCooldown(JsonNullable<Float> cooldown) {
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
    EnergyRestoreResult energyRestoreResult = (EnergyRestoreResult) o;
    return Objects.equals(this.newLevel, energyRestoreResult.newLevel) &&
        Objects.equals(this.restoredAmount, energyRestoreResult.restoredAmount) &&
        equalsNullable(this.cooldown, energyRestoreResult.cooldown);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(newLevel, restoredAmount, hashCodeNullable(cooldown));
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
    sb.append("class EnergyRestoreResult {\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    restoredAmount: ").append(toIndentedString(restoredAmount)).append("\n");
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

