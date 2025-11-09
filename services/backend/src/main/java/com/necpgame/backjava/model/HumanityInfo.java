package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РРЅС„РѕСЂРјР°С†РёСЏ Рѕ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё 
 */

@Schema(name = "HumanityInfo", description = "РРЅС„РѕСЂРјР°С†РёСЏ Рѕ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class HumanityInfo {

  private Float current;

  private Float max;

  private Float lossPercentage;

  /**
   * РўРµРєСѓС‰Р°СЏ СЃС‚Р°РґРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°
   */
  public enum StageEnum {
    EARLY("early"),
    
    MIDDLE("middle"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StageEnum stage;

  public HumanityInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HumanityInfo(Float current, Float max, Float lossPercentage, StageEnum stage) {
    this.current = current;
    this.max = max;
    this.lossPercentage = lossPercentage;
    this.stage = stage;
  }

  public HumanityInfo current(Float current) {
    this.current = current;
    return this;
  }

  /**
   * РўРµРєСѓС‰РёР№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100)
   * minimum: 0
   * maximum: 100
   * @return current
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "current", description = "РўРµРєСѓС‰РёР№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current")
  public Float getCurrent() {
    return current;
  }

  public void setCurrent(Float current) {
    this.current = current;
  }

  public HumanityInfo max(Float max) {
    this.max = max;
    return this;
  }

  /**
   * РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (РјРѕР¶РµС‚ СЃРЅРёР¶Р°С‚СЊСЃСЏ РїСЂРё РЅР°РєРѕРїР»РµРЅРёРё СЃС‚СЂРµСЃСЃР°)
   * minimum: 0
   * maximum: 100
   * @return max
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "max", description = "РњР°РєСЃРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (РјРѕР¶РµС‚ СЃРЅРёР¶Р°С‚СЊСЃСЏ РїСЂРё РЅР°РєРѕРїР»РµРЅРёРё СЃС‚СЂРµСЃСЃР°)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("max")
  public Float getMax() {
    return max;
  }

  public void setMax(Float max) {
    this.max = max;
  }

  public HumanityInfo lossPercentage(Float lossPercentage) {
    this.lossPercentage = lossPercentage;
    return this;
  }

  /**
   * РџСЂРѕС†РµРЅС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100%)
   * minimum: 0
   * maximum: 100
   * @return lossPercentage
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "loss_percentage", description = "РџСЂРѕС†РµРЅС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loss_percentage")
  public Float getLossPercentage() {
    return lossPercentage;
  }

  public void setLossPercentage(Float lossPercentage) {
    this.lossPercentage = lossPercentage;
  }

  public HumanityInfo stage(StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * РўРµРєСѓС‰Р°СЏ СЃС‚Р°РґРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°
   * @return stage
   */
  @NotNull 
  @Schema(name = "stage", description = "РўРµРєСѓС‰Р°СЏ СЃС‚Р°РґРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage")
  public StageEnum getStage() {
    return stage;
  }

  public void setStage(StageEnum stage) {
    this.stage = stage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanityInfo humanityInfo = (HumanityInfo) o;
    return Objects.equals(this.current, humanityInfo.current) &&
        Objects.equals(this.max, humanityInfo.max) &&
        Objects.equals(this.lossPercentage, humanityInfo.lossPercentage) &&
        Objects.equals(this.stage, humanityInfo.stage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, max, lossPercentage, stage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HumanityInfo {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("    lossPercentage: ").append(toIndentedString(lossPercentage)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
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

