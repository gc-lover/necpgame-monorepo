package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё
 */

@Schema(name = "ApplySocialSupportRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ApplySocialSupportRequest {

  /**
   * РўРёРї СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё
   */
  public enum SupportTypeEnum {
    FRIEND("friend"),
    
    FACTION("faction"),
    
    NPC("npc");

    private final String value;

    SupportTypeEnum(String value) {
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
    public static SupportTypeEnum fromValue(String value) {
      for (SupportTypeEnum b : SupportTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SupportTypeEnum supportType;

  private UUID sourceId;

  public ApplySocialSupportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplySocialSupportRequest(SupportTypeEnum supportType, UUID sourceId) {
    this.supportType = supportType;
    this.sourceId = sourceId;
  }

  public ApplySocialSupportRequest supportType(SupportTypeEnum supportType) {
    this.supportType = supportType;
    return this;
  }

  /**
   * РўРёРї СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё
   * @return supportType
   */
  @NotNull 
  @Schema(name = "support_type", description = "РўРёРї СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("support_type")
  public SupportTypeEnum getSupportType() {
    return supportType;
  }

  public void setSupportType(SupportTypeEnum supportType) {
    this.supportType = supportType;
  }

  public ApplySocialSupportRequest sourceId(UUID sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёСЃС‚РѕС‡РЅРёРєР° РїРѕРґРґРµСЂР¶РєРё (РґСЂСѓРі, С„СЂР°РєС†РёСЏ, NPC)
   * @return sourceId
   */
  @NotNull @Valid 
  @Schema(name = "source_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёСЃС‚РѕС‡РЅРёРєР° РїРѕРґРґРµСЂР¶РєРё (РґСЂСѓРі, С„СЂР°РєС†РёСЏ, NPC)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_id")
  public UUID getSourceId() {
    return sourceId;
  }

  public void setSourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplySocialSupportRequest applySocialSupportRequest = (ApplySocialSupportRequest) o;
    return Objects.equals(this.supportType, applySocialSupportRequest.supportType) &&
        Objects.equals(this.sourceId, applySocialSupportRequest.sourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supportType, sourceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplySocialSupportRequest {\n");
    sb.append("    supportType: ").append(toIndentedString(supportType)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
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

