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
 * Р—Р°РїСЂРѕСЃ РЅР° РІР°Р»РёРґР°С†РёСЋ СѓСЃС‚Р°РЅРѕРІРєРё РёРјРїР»Р°РЅС‚Р°
 */

@Schema(name = "ValidateInstallRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РІР°Р»РёРґР°С†РёСЋ СѓСЃС‚Р°РЅРѕРІРєРё РёРјРїР»Р°РЅС‚Р°")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ValidateInstallRequest {

  private UUID implantId;

  private UUID targetSlot;

  public ValidateInstallRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidateInstallRequest(UUID implantId, UUID targetSlot) {
    this.implantId = implantId;
    this.targetSlot = targetSlot;
  }

  public ValidateInstallRequest implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р° РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public ValidateInstallRequest targetSlot(UUID targetSlot) {
    this.targetSlot = targetSlot;
    return this;
  }

  /**
   * Р¦РµР»РµРІРѕР№ СЃР»РѕС‚ РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё
   * @return targetSlot
   */
  @NotNull @Valid 
  @Schema(name = "target_slot", description = "Р¦РµР»РµРІРѕР№ СЃР»РѕС‚ РґР»СЏ СѓСЃС‚Р°РЅРѕРІРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_slot")
  public UUID getTargetSlot() {
    return targetSlot;
  }

  public void setTargetSlot(UUID targetSlot) {
    this.targetSlot = targetSlot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidateInstallRequest validateInstallRequest = (ValidateInstallRequest) o;
    return Objects.equals(this.implantId, validateInstallRequest.implantId) &&
        Objects.equals(this.targetSlot, validateInstallRequest.targetSlot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, targetSlot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidateInstallRequest {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    targetSlot: ").append(toIndentedString(targetSlot)).append("\n");
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

