package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemEquipRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ItemEquipRequest {

  private String itemInstanceId;

  private String slotType;

  @Valid
  private Map<String, Object> overrides = new HashMap<>();

  public ItemEquipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemEquipRequest(String itemInstanceId, String slotType) {
    this.itemInstanceId = itemInstanceId;
    this.slotType = slotType;
  }

  public ItemEquipRequest itemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  @NotNull 
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemInstanceId")
  public String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemEquipRequest slotType(String slotType) {
    this.slotType = slotType;
    return this;
  }

  /**
   * Get slotType
   * @return slotType
   */
  @NotNull 
  @Schema(name = "slotType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotType")
  public String getSlotType() {
    return slotType;
  }

  public void setSlotType(String slotType) {
    this.slotType = slotType;
  }

  public ItemEquipRequest overrides(Map<String, Object> overrides) {
    this.overrides = overrides;
    return this;
  }

  public ItemEquipRequest putOverridesItem(String key, Object overridesItem) {
    if (this.overrides == null) {
      this.overrides = new HashMap<>();
    }
    this.overrides.put(key, overridesItem);
    return this;
  }

  /**
   * Get overrides
   * @return overrides
   */
  
  @Schema(name = "overrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrides")
  public Map<String, Object> getOverrides() {
    return overrides;
  }

  public void setOverrides(Map<String, Object> overrides) {
    this.overrides = overrides;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemEquipRequest itemEquipRequest = (ItemEquipRequest) o;
    return Objects.equals(this.itemInstanceId, itemEquipRequest.itemInstanceId) &&
        Objects.equals(this.slotType, itemEquipRequest.slotType) &&
        Objects.equals(this.overrides, itemEquipRequest.overrides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, slotType, overrides);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemEquipRequest {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    slotType: ").append(toIndentedString(slotType)).append("\n");
    sb.append("    overrides: ").append(toIndentedString(overrides)).append("\n");
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

