package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.AutoLootSetting;
import com.necpgame.lootservice.model.SmartLootSetting;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootSettingsUpdateRequest
 */


public class LootSettingsUpdateRequest {

  private @Nullable SmartLootSetting smartLoot;

  private @Nullable AutoLootSetting autoLoot;

  public LootSettingsUpdateRequest smartLoot(@Nullable SmartLootSetting smartLoot) {
    this.smartLoot = smartLoot;
    return this;
  }

  /**
   * Get smartLoot
   * @return smartLoot
   */
  @Valid 
  @Schema(name = "smartLoot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smartLoot")
  public @Nullable SmartLootSetting getSmartLoot() {
    return smartLoot;
  }

  public void setSmartLoot(@Nullable SmartLootSetting smartLoot) {
    this.smartLoot = smartLoot;
  }

  public LootSettingsUpdateRequest autoLoot(@Nullable AutoLootSetting autoLoot) {
    this.autoLoot = autoLoot;
    return this;
  }

  /**
   * Get autoLoot
   * @return autoLoot
   */
  @Valid 
  @Schema(name = "autoLoot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoLoot")
  public @Nullable AutoLootSetting getAutoLoot() {
    return autoLoot;
  }

  public void setAutoLoot(@Nullable AutoLootSetting autoLoot) {
    this.autoLoot = autoLoot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootSettingsUpdateRequest lootSettingsUpdateRequest = (LootSettingsUpdateRequest) o;
    return Objects.equals(this.smartLoot, lootSettingsUpdateRequest.smartLoot) &&
        Objects.equals(this.autoLoot, lootSettingsUpdateRequest.autoLoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(smartLoot, autoLoot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootSettingsUpdateRequest {\n");
    sb.append("    smartLoot: ").append(toIndentedString(smartLoot)).append("\n");
    sb.append("    autoLoot: ").append(toIndentedString(autoLoot)).append("\n");
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

