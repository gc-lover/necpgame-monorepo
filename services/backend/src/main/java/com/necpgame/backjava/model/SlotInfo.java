package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * РРЅС„РѕСЂРјР°С†РёСЏ Рѕ СЃР»РѕС‚Рµ РёРјРїР»Р°РЅС‚Р°
 */

@Schema(name = "SlotInfo", description = "РРЅС„РѕСЂРјР°С†РёСЏ Рѕ СЃР»РѕС‚Рµ РёРјРїР»Р°РЅС‚Р°")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class SlotInfo {

  private UUID slotId;

  private Boolean isOccupied;

  private JsonNullable<UUID> installedImplantId = JsonNullable.<UUID>undefined();

  private Boolean canInstall;

  public SlotInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SlotInfo(UUID slotId, Boolean isOccupied, Boolean canInstall) {
    this.slotId = slotId;
    this.isOccupied = isOccupied;
    this.canInstall = canInstall;
  }

  public SlotInfo slotId(UUID slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃР»РѕС‚Р°
   * @return slotId
   */
  @NotNull @Valid 
  @Schema(name = "slot_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃР»РѕС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot_id")
  public UUID getSlotId() {
    return slotId;
  }

  public void setSlotId(UUID slotId) {
    this.slotId = slotId;
  }

  public SlotInfo isOccupied(Boolean isOccupied) {
    this.isOccupied = isOccupied;
    return this;
  }

  /**
   * Р—Р°РЅСЏС‚ Р»Рё СЃР»РѕС‚
   * @return isOccupied
   */
  @NotNull 
  @Schema(name = "is_occupied", description = "Р—Р°РЅСЏС‚ Р»Рё СЃР»РѕС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("is_occupied")
  public Boolean getIsOccupied() {
    return isOccupied;
  }

  public void setIsOccupied(Boolean isOccupied) {
    this.isOccupied = isOccupied;
  }

  public SlotInfo installedImplantId(UUID installedImplantId) {
    this.installedImplantId = JsonNullable.of(installedImplantId);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅРѕРіРѕ РёРјРїР»Р°РЅС‚Р° (РµСЃР»Рё Р·Р°РЅСЏС‚)
   * @return installedImplantId
   */
  @Valid 
  @Schema(name = "installed_implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅРѕРіРѕ РёРјРїР»Р°РЅС‚Р° (РµСЃР»Рё Р·Р°РЅСЏС‚)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installed_implant_id")
  public JsonNullable<UUID> getInstalledImplantId() {
    return installedImplantId;
  }

  public void setInstalledImplantId(JsonNullable<UUID> installedImplantId) {
    this.installedImplantId = installedImplantId;
  }

  public SlotInfo canInstall(Boolean canInstall) {
    this.canInstall = canInstall;
    return this;
  }

  /**
   * РњРѕР¶РЅРѕ Р»Рё СѓСЃС‚Р°РЅРѕРІРёС‚СЊ РёРјРїР»Р°РЅС‚ РІ СЌС‚РѕС‚ СЃР»РѕС‚
   * @return canInstall
   */
  @NotNull 
  @Schema(name = "can_install", description = "РњРѕР¶РЅРѕ Р»Рё СѓСЃС‚Р°РЅРѕРІРёС‚СЊ РёРјРїР»Р°РЅС‚ РІ СЌС‚РѕС‚ СЃР»РѕС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("can_install")
  public Boolean getCanInstall() {
    return canInstall;
  }

  public void setCanInstall(Boolean canInstall) {
    this.canInstall = canInstall;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlotInfo slotInfo = (SlotInfo) o;
    return Objects.equals(this.slotId, slotInfo.slotId) &&
        Objects.equals(this.isOccupied, slotInfo.isOccupied) &&
        equalsNullable(this.installedImplantId, slotInfo.installedImplantId) &&
        Objects.equals(this.canInstall, slotInfo.canInstall);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotId, isOccupied, hashCodeNullable(installedImplantId), canInstall);
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
    sb.append("class SlotInfo {\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    isOccupied: ").append(toIndentedString(isOccupied)).append("\n");
    sb.append("    installedImplantId: ").append(toIndentedString(installedImplantId)).append("\n");
    sb.append("    canInstall: ").append(toIndentedString(canInstall)).append("\n");
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

