package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootConfigResponseDropCapsInner
 */

@JsonTypeName("LootConfigResponse_dropCaps_inner")

public class LootConfigResponseDropCapsInner {

  private @Nullable String itemTemplateId;

  private @Nullable Integer weeklyCap;

  public LootConfigResponseDropCapsInner itemTemplateId(@Nullable String itemTemplateId) {
    this.itemTemplateId = itemTemplateId;
    return this;
  }

  /**
   * Get itemTemplateId
   * @return itemTemplateId
   */
  
  @Schema(name = "itemTemplateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemTemplateId")
  public @Nullable String getItemTemplateId() {
    return itemTemplateId;
  }

  public void setItemTemplateId(@Nullable String itemTemplateId) {
    this.itemTemplateId = itemTemplateId;
  }

  public LootConfigResponseDropCapsInner weeklyCap(@Nullable Integer weeklyCap) {
    this.weeklyCap = weeklyCap;
    return this;
  }

  /**
   * Get weeklyCap
   * @return weeklyCap
   */
  
  @Schema(name = "weeklyCap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weeklyCap")
  public @Nullable Integer getWeeklyCap() {
    return weeklyCap;
  }

  public void setWeeklyCap(@Nullable Integer weeklyCap) {
    this.weeklyCap = weeklyCap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootConfigResponseDropCapsInner lootConfigResponseDropCapsInner = (LootConfigResponseDropCapsInner) o;
    return Objects.equals(this.itemTemplateId, lootConfigResponseDropCapsInner.itemTemplateId) &&
        Objects.equals(this.weeklyCap, lootConfigResponseDropCapsInner.weeklyCap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemTemplateId, weeklyCap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootConfigResponseDropCapsInner {\n");
    sb.append("    itemTemplateId: ").append(toIndentedString(itemTemplateId)).append("\n");
    sb.append("    weeklyCap: ").append(toIndentedString(weeklyCap)).append("\n");
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

