package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р—Р°РїСЂРѕСЃ РЅР° СѓРґР°Р»РµРЅРёРµ РёРјРїР»Р°РЅС‚Р°
 */

@Schema(name = "RemoveImplantRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° СѓРґР°Р»РµРЅРёРµ РёРјРїР»Р°РЅС‚Р°")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class RemoveImplantRequest {

  private UUID implantId;

  private UUID npcId;

  public RemoveImplantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RemoveImplantRequest(UUID implantId, UUID npcId) {
    this.implantId = implantId;
    this.npcId = npcId;
  }

  public RemoveImplantRequest implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° РґР»СЏ СѓРґР°Р»РµРЅРёСЏ
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° РґР»СЏ СѓРґР°Р»РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public RemoveImplantRequest npcId(UUID npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC (С„РёРєСЃС‘СЂР°) РґР»СЏ СѓРґР°Р»РµРЅРёСЏ
   * @return npcId
   */
  @NotNull @Valid 
  @Schema(name = "npc_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC (С„РёРєСЃС‘СЂР°) РґР»СЏ СѓРґР°Р»РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_id")
  public UUID getNpcId() {
    return npcId;
  }

  public void setNpcId(UUID npcId) {
    this.npcId = npcId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoveImplantRequest removeImplantRequest = (RemoveImplantRequest) o;
    return Objects.equals(this.implantId, removeImplantRequest.implantId) &&
        Objects.equals(this.npcId, removeImplantRequest.npcId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, npcId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoveImplantRequest {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
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

