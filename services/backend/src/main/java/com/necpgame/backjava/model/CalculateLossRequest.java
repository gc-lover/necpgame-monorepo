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
 * Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
 */

@Schema(name = "CalculateLossRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CalculateLossRequest {

  private UUID implantId;

  /**
   * РўРёРї РёРјРїР»Р°РЅС‚Р°
   */
  public enum ImplantTypeEnum {
    COMBAT("combat"),
    
    TACTICAL("tactical"),
    
    DEFENSIVE("defensive"),
    
    MOBILITY("mobility"),
    
    OS("os");

    private final String value;

    ImplantTypeEnum(String value) {
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
    public static ImplantTypeEnum fromValue(String value) {
      for (ImplantTypeEnum b : ImplantTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ImplantTypeEnum implantType;

  /**
   * РљР°С‡РµСЃС‚РІРѕ РёРјРїР»Р°РЅС‚Р°
   */
  public enum QualityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary");

    private final String value;

    QualityEnum(String value) {
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
    public static QualityEnum fromValue(String value) {
      for (QualityEnum b : QualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  private JsonNullable<QualityEnum> quality = JsonNullable.<QualityEnum>undefined();

  private JsonNullable<String> installer = JsonNullable.<String>undefined();

  private JsonNullable<Boolean> compatibility = JsonNullable.<Boolean>undefined();

  private JsonNullable<@DecimalMin(value = "0") Float> intensity = JsonNullable.<Float>undefined();

  public CalculateLossRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateLossRequest(UUID implantId, ImplantTypeEnum implantType) {
    this.implantId = implantId;
    this.implantType = implantType;
  }

  public CalculateLossRequest implantId(UUID implantId) {
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

  public CalculateLossRequest implantType(ImplantTypeEnum implantType) {
    this.implantType = implantType;
    return this;
  }

  /**
   * РўРёРї РёРјРїР»Р°РЅС‚Р°
   * @return implantType
   */
  @NotNull 
  @Schema(name = "implant_type", description = "РўРёРї РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_type")
  public ImplantTypeEnum getImplantType() {
    return implantType;
  }

  public void setImplantType(ImplantTypeEnum implantType) {
    this.implantType = implantType;
  }

  public CalculateLossRequest quality(QualityEnum quality) {
    this.quality = JsonNullable.of(quality);
    return this;
  }

  /**
   * РљР°С‡РµСЃС‚РІРѕ РёРјРїР»Р°РЅС‚Р°
   * @return quality
   */
  
  @Schema(name = "quality", description = "РљР°С‡РµСЃС‚РІРѕ РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public JsonNullable<QualityEnum> getQuality() {
    return quality;
  }

  public void setQuality(JsonNullable<QualityEnum> quality) {
    this.quality = quality;
  }

  public CalculateLossRequest installer(String installer) {
    this.installer = JsonNullable.of(installer);
    return this;
  }

  /**
   * РЈСЃС‚Р°РЅРѕРІС‰РёРє РёРјРїР»Р°РЅС‚Р° (NPC РёР»Рё РёРіСЂРѕРє)
   * @return installer
   */
  
  @Schema(name = "installer", description = "РЈСЃС‚Р°РЅРѕРІС‰РёРє РёРјРїР»Р°РЅС‚Р° (NPC РёР»Рё РёРіСЂРѕРє)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installer")
  public JsonNullable<String> getInstaller() {
    return installer;
  }

  public void setInstaller(JsonNullable<String> installer) {
    this.installer = installer;
  }

  public CalculateLossRequest compatibility(Boolean compatibility) {
    this.compatibility = JsonNullable.of(compatibility);
    return this;
  }

  /**
   * РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё
   * @return compatibility
   */
  
  @Schema(name = "compatibility", description = "РЎРѕРІРјРµСЃС‚РёРј Р»Рё РёРјРїР»Р°РЅС‚ СЃ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatibility")
  public JsonNullable<Boolean> getCompatibility() {
    return compatibility;
  }

  public void setCompatibility(JsonNullable<Boolean> compatibility) {
    this.compatibility = compatibility;
  }

  public CalculateLossRequest intensity(Float intensity) {
    this.intensity = JsonNullable.of(intensity);
    return this;
  }

  /**
   * РРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ РёРјРїР»Р°РЅС‚Р°
   * minimum: 0
   * @return intensity
   */
  @DecimalMin(value = "0") 
  @Schema(name = "intensity", description = "РРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intensity")
  public JsonNullable<@DecimalMin(value = "0") Float> getIntensity() {
    return intensity;
  }

  public void setIntensity(JsonNullable<Float> intensity) {
    this.intensity = intensity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateLossRequest calculateLossRequest = (CalculateLossRequest) o;
    return Objects.equals(this.implantId, calculateLossRequest.implantId) &&
        Objects.equals(this.implantType, calculateLossRequest.implantType) &&
        equalsNullable(this.quality, calculateLossRequest.quality) &&
        equalsNullable(this.installer, calculateLossRequest.installer) &&
        equalsNullable(this.compatibility, calculateLossRequest.compatibility) &&
        equalsNullable(this.intensity, calculateLossRequest.intensity);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, implantType, hashCodeNullable(quality), hashCodeNullable(installer), hashCodeNullable(compatibility), hashCodeNullable(intensity));
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
    sb.append("class CalculateLossRequest {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    implantType: ").append(toIndentedString(implantType)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    installer: ").append(toIndentedString(installer)).append("\n");
    sb.append("    compatibility: ").append(toIndentedString(compatibility)).append("\n");
    sb.append("    intensity: ").append(toIndentedString(intensity)).append("\n");
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

