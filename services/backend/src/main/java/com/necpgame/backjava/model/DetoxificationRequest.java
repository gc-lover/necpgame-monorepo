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
 * Р—Р°РїСЂРѕСЃ РЅР° РґРµС‚РѕРєСЃРёРєР°С†РёСЋ
 */

@Schema(name = "DetoxificationRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РґРµС‚РѕРєСЃРёРєР°С†РёСЋ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class DetoxificationRequest {

  private UUID npcId;

  /**
   * РЈСЂРѕРІРµРЅСЊ РґРµС‚РѕРєСЃРёРєР°С†РёРё
   */
  public enum LevelEnum {
    BASIC("basic"),
    
    ADVANCED("advanced"),
    
    CRITICAL("critical");

    private final String value;

    LevelEnum(String value) {
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
    public static LevelEnum fromValue(String value) {
      for (LevelEnum b : LevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LevelEnum level;

  public DetoxificationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DetoxificationRequest(UUID npcId, LevelEnum level) {
    this.npcId = npcId;
    this.level = level;
  }

  public DetoxificationRequest npcId(UUID npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC РґР»СЏ РґРµС‚РѕРєСЃРёРєР°С†РёРё
   * @return npcId
   */
  @NotNull @Valid 
  @Schema(name = "npc_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC РґР»СЏ РґРµС‚РѕРєСЃРёРєР°С†РёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_id")
  public UUID getNpcId() {
    return npcId;
  }

  public void setNpcId(UUID npcId) {
    this.npcId = npcId;
  }

  public DetoxificationRequest level(LevelEnum level) {
    this.level = level;
    return this;
  }

  /**
   * РЈСЂРѕРІРµРЅСЊ РґРµС‚РѕРєСЃРёРєР°С†РёРё
   * @return level
   */
  @NotNull 
  @Schema(name = "level", description = "РЈСЂРѕРІРµРЅСЊ РґРµС‚РѕРєСЃРёРєР°С†РёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public LevelEnum getLevel() {
    return level;
  }

  public void setLevel(LevelEnum level) {
    this.level = level;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetoxificationRequest detoxificationRequest = (DetoxificationRequest) o;
    return Objects.equals(this.npcId, detoxificationRequest.npcId) &&
        Objects.equals(this.level, detoxificationRequest.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, level);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetoxificationRequest {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
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

